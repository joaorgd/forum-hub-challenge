package com.alura.forumhub.repository;

import com.alura.forumhub.domain.topico.Topico;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TopicoRepository extends JpaRepository<Topico, Long> {
    // Método para verificar a existência de um tópico com mesmo título E mesma mensagem.
    boolean existsByTituloAndMensagem(String titulo, String mensagem);
}