package com.smartbusiness.controller;

import com.smartbusiness.dto.CadastroEmpresaRequest;
import com.smartbusiness.dto.LoginRequest;
import com.smartbusiness.dto.LoginResponse;
import com.smartbusiness.entity.Usuario;
import com.smartbusiness.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "Autenticação", description = "Login, cadastro de empresa e dados do usuário logado")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    @Operation(summary = "Fazer login", description = "Retorna um token JWT para uso nas demais rotas")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }

    @PostMapping("/cadastro")
    @Operation(summary = "Cadastrar nova empresa", description = "Cria a empresa e o primeiro usuário ADMIN")
    public ResponseEntity<LoginResponse> cadastrar(@Valid @RequestBody CadastroEmpresaRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(authService.cadastrarEmpresa(request));
    }

    @GetMapping("/me")
    @Operation(summary = "Dados do usuário logado", description = "Retorna nome, email, perfil e empresa do token atual")
    public ResponseEntity<Map<String, Object>> me() {
        Usuario usuario = authService.usuarioLogado();
        return ResponseEntity.ok(Map.of(
                "id", usuario.getId(),
                "nome", usuario.getNome(),
                "email", usuario.getEmail(),
                "perfil", usuario.getPerfil() != null ? usuario.getPerfil().name() : "",
                "empresaId", usuario.getEmpresa() != null ? usuario.getEmpresa().getId() : "",
                "empresaNome", usuario.getEmpresa() != null ? usuario.getEmpresa().getNome() : ""
        ));
    }
}
