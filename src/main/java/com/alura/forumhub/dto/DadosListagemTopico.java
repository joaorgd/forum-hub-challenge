package com.alura.forumhub.dto;

import com.alura.forumhub.domain.topico.Topico;

import java.time.LocalDateTime;

// DTO para listar tópicos
public record DadosListagemTopico(Long id, String titulo, String mensagem, LocalDateTime dataCriacao) {
    public DadosListagemTopico(Topico topico) {
        this(topico.getId(), topico.getTitulo(), topico.getMensagem(), topico.getDataCriacao());
    }
}
