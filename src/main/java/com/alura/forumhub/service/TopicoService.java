package com.alura.forumhub.service;

import com.alura.forumhub.domain.topico.Topico;
import com.alura.forumhub.domain.usuario.Usuario;
import com.alura.forumhub.dto.DadosCadastroTopico;
import com.alura.forumhub.dto.DadosDetalhamentoTopico;
import com.alura.forumhub.repository.TopicoRepository;
// import com.alura.forumhub.infra.exception.ValidacaoException; // Se for usar exceções customizadas
import org.springframework.stereotype.Service;

@Service
public class TopicoService {

    private final TopicoRepository topicoRepository;

    // Injeção de dependência via construtor
    public TopicoService(TopicoRepository topicoRepository) {
        this.topicoRepository = topicoRepository;
    }

    public DadosDetalhamentoTopico criar(DadosCadastroTopico dados, Usuario autor) {
        // if (topicoRepository.existsByTitulo(dados.titulo())) {
        //     throw new ValidacaoException("Já existe um tópico com este título.");
        // }
        // if (topicoRepository.existsByMensagem(dados.mensagem())) {
        //     throw new ValidacaoException("Já existe um tópico com esta mensagem.");
        // }

        var topico = new Topico(dados.titulo(), dados.mensagem(), autor);
        topicoRepository.save(topico);

        return new DadosDetalhamentoTopico(topico);
    }

    // Implementação dos outros métodos do serviço (listar, detalhar, atualizar, excluir)...
}
