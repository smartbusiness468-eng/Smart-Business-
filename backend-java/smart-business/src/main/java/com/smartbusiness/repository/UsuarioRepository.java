package com.smartbusiness.repository;

import com.smartbusiness.entity.Empresa;
import com.smartbusiness.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    Optional<Usuario> findByEmail(String email);
    List<Usuario> findByEmpresa(Empresa empresa);
    boolean existsByEmail(String email);
}
