package com.smartbusiness.service;

import com.smartbusiness.dto.CadastroEmpresaRequest;
import com.smartbusiness.dto.LoginRequest;
import com.smartbusiness.dto.LoginResponse;
import com.smartbusiness.entity.Empresa;
import com.smartbusiness.entity.Perfil;
import com.smartbusiness.entity.Usuario;
import com.smartbusiness.exception.BusinessException;
import com.smartbusiness.repository.EmpresaRepository;
import com.smartbusiness.repository.UsuarioRepository;
import com.smartbusiness.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.Normalizer;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UsuarioRepository usuarioRepository;
    private final EmpresaRepository empresaRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;

    public LoginResponse login(LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getSenha())
        );

        UserDetails userDetails = userDetailsService.loadUserByUsername(request.getEmail());
        String token = jwtService.gerarToken(userDetails);

        Usuario usuario = usuarioRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new BusinessException("Usuário não encontrado"));

        return new LoginResponse(
                token,
                usuario.getNome(),
                usuario.getEmail(),
                usuario.getPerfil() != null ? usuario.getPerfil().name() : null,
                usuario.getEmpresa() != null ? usuario.getEmpresa().getId() : null,
                usuario.getEmpresa() != null ? usuario.getEmpresa().getNome() : null
        );
    }

    @Transactional
    public LoginResponse cadastrarEmpresa(CadastroEmpresaRequest request) {
        if (usuarioRepository.existsByEmail(request.getEmailAdmin())) {
            throw new BusinessException("Este email já está em uso");
        }

        Empresa empresa = new Empresa();
        empresa.setNome(request.getNomeEmpresa());
        empresa.setCnpj(request.getCnpj());
        empresa.setSlug(gerarSlug(request.getNomeEmpresa()));
        empresa.setAtiva(true);
        empresaRepository.save(empresa);

        Usuario admin = new Usuario();
        admin.setNome(request.getNomeAdmin());
        admin.setEmail(request.getEmailAdmin());
        admin.setSenha(passwordEncoder.encode(request.getSenhaAdmin()));
        admin.setPerfil(Perfil.ADMIN);
        admin.setAtivo(true);
        admin.setEmpresa(empresa);
        usuarioRepository.save(admin);

        UserDetails userDetails = userDetailsService.loadUserByUsername(admin.getEmail());
        String token = jwtService.gerarToken(userDetails);

        return new LoginResponse(token, admin.getNome(), admin.getEmail(),
                Perfil.ADMIN.name(), empresa.getId(), empresa.getNome());
    }

    public Usuario usuarioLogado() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new BusinessException("Usuário não encontrado"));
    }

    private String gerarSlug(String nome) {
        String slug = Normalizer.normalize(nome, Normalizer.Form.NFD)
                .replaceAll("[^\\p{ASCII}]", "")
                .toLowerCase()
                .replaceAll("[^a-z0-9]", "-")
                .replaceAll("-+", "-")
                .replaceAll("^-|-$", "");

        String base = slug;
        int i = 1;
        while (empresaRepository.findBySlug(slug).isPresent()) {
            slug = base + "-" + i++;
        }
        return slug;
    }
}
