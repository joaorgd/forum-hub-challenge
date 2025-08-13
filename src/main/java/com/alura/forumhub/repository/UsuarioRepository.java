package com.alura.forumhub.repository;

import com.alura.forumhub.domain.usuario.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    // Método para buscar usuário pelo email (usado no Spring Security)
    Optional<Usuario> findByEmail(String email);
}