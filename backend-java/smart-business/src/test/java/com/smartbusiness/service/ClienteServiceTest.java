package com.smartbusiness.service;

import com.smartbusiness.dto.ClienteDTO;
import com.smartbusiness.entity.Cliente;
import com.smartbusiness.entity.Empresa;
import com.smartbusiness.entity.Perfil;
import com.smartbusiness.entity.Usuario;
import com.smartbusiness.exception.ResourceNotFoundException;
import com.smartbusiness.repository.ClienteRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ClienteServiceTest {

    @Mock
    private ClienteRepository clienteRepository;

    @Mock
    private AuthService authService;

    @InjectMocks
    private ClienteService clienteService;

    private Empresa empresa;
    private Usuario usuario;

    @BeforeEach
    void setUp() {
        empresa = new Empresa();
        empresa.setNome("Empresa Teste");

        usuario = new Usuario();
        usuario.setNome("Admin");
        usuario.setEmail("admin@teste.com");
        usuario.setPerfil(Perfil.ADMIN);
        usuario.setEmpresa(empresa);
    }

    @Test
    void listarTodos_deveRetornarClientesDaEmpresa() {
        Cliente c = new Cliente();
        c.setNome("João");
        c.setEmpresa(empresa);

        when(authService.usuarioLogado()).thenReturn(usuario);
        when(clienteRepository.findByEmpresa(empresa)).thenReturn(List.of(c));

        List<Cliente> resultado = clienteService.listarTodos();

        assertThat(resultado).hasSize(1);
        assertThat(resultado.get(0).getNome()).isEqualTo("João");
    }

    @Test
    void salvar_deveCriarClienteComEmpresaDoUsuario() {
        ClienteDTO dto = new ClienteDTO();
        dto.setNome("Maria");
        dto.setEmail("maria@email.com");

        when(authService.usuarioLogado()).thenReturn(usuario);
        when(clienteRepository.save(any(Cliente.class))).thenAnswer(inv -> inv.getArgument(0));

        Cliente salvo = clienteService.salvar(dto);

        assertThat(salvo.getNome()).isEqualTo("Maria");
        assertThat(salvo.getEmpresa()).isEqualTo(empresa);
    }

    @Test
    void buscarPorId_quandoNaoExiste_deveLancarException() {
        when(authService.usuarioLogado()).thenReturn(usuario);
        when(clienteRepository.findByIdAndEmpresa(99L, empresa)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> clienteService.buscarPorId(99L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Cliente não encontrado");
    }

    @Test
    void excluir_deveChamarDeleteNoRepositorio() {
        Cliente cliente = new Cliente();
        cliente.setNome("Pedro");
        cliente.setEmpresa(empresa);

        when(authService.usuarioLogado()).thenReturn(usuario);
        when(clienteRepository.findByIdAndEmpresa(1L, empresa)).thenReturn(Optional.of(cliente));

        clienteService.excluir(1L);

        verify(clienteRepository, times(1)).delete(cliente);
    }
}
