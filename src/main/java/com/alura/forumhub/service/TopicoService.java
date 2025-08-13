package com.alura.forumhub.service;

import com.alura.forumhub.domain.topico.Topico;
import com.alura.forumhub.domain.usuario.Usuario;
import com.alura.forumhub.dto.DadosCadastroTopico;
import com.alura.forumhub.dto.DadosDetalhamentoTopico;
import com.alura.forumhub.infra.exception.ValidacaoException;
import com.alura.forumhub.repository.TopicoRepository;
import org.springframework.stereotype.Service;

@Service
public class TopicoService {

    private final TopicoRepository topicoRepository;

    // Injeção de dependência do repositório via construtor (melhor prática)
    public TopicoService(TopicoRepository topicoRepository) {
        this.topicoRepository = topicoRepository;
    }

    /**
     * Método principal para a lógica de criação de um novo tópico.
     */
    public DadosDetalhamentoTopico criar(DadosCadastroTopico dados, Usuario autor) {
        // 1. Validação das regras de negócio
        if (topicoRepository.existsByTitulo(dados.titulo())) {
            throw new ValidacaoException("Já existe um tópico com este título. Por favor, escolha outro.");
        }
        if (topicoRepository.existsByMensagem(dados.mensagem())) {
            throw new ValidacaoException("Já existe um tópico com esta mensagem. Verifique se não é um post duplicado.");
        }

        // 2. Criação da instância da entidade
        var topico = new Topico(
                dados.titulo(),
                dados.mensagem(),
                autor
        );

        // 3. Persistência no banco de dados
        topicoRepository.save(topico);

        // 4. Retorno de um DTO com os dados detalhados
        return new DadosDetalhamentoTopico(topico);
    }
}