package com.alura.forumhub.service;

import com.alura.forumhub.domain.topico.Topico;
import com.alura.forumhub.domain.usuario.Usuario;
import com.alura.forumhub.dto.DadosAtualizacaoTopico;
import com.alura.forumhub.dto.DadosCadastroTopico;
import com.alura.forumhub.dto.DadosDetalhamentoTopico;
import com.alura.forumhub.dto.DadosListagemTopico;
import com.alura.forumhub.infra.exception.ValidacaoException;
import com.alura.forumhub.repository.TopicoRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class TopicoService {

    private final TopicoRepository topicoRepository;

    public TopicoService(TopicoRepository topicoRepository) {
        this.topicoRepository = topicoRepository;
    }

    /**
     * Cria um novo tópico, aplicando as regras de negócio.
     */
    public DadosDetalhamentoTopico criar(DadosCadastroTopico dados, Usuario autor) {
        if (topicoRepository.existsByTituloAndMensagem(dados.titulo(), dados.mensagem())) {
            throw new ValidacaoException("Tópico duplicado: já existe um tópico com o mesmo título e mensagem.");
        }

        var topico = new Topico(dados.titulo(), dados.mensagem(), autor);
        topicoRepository.save(topico);

        return new DadosDetalhamentoTopico(topico);
    }

    /**
     * Lista todos os tópicos de forma paginada.
     */
    public Page<DadosListagemTopico> listar(Pageable paginacao) {
        return topicoRepository.findAll(paginacao).map(DadosListagemTopico::new);
    }

    /**
     * Detalha um tópico específico pelo seu ID.
     */
    public DadosDetalhamentoTopico detalhar(Long id) {
        var topico = topicoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Tópico não encontrado com o ID: " + id));
        return new DadosDetalhamentoTopico(topico);
    }

    /**
     * Atualiza um tópico existente.
     */
    public DadosDetalhamentoTopico atualizar(DadosAtualizacaoTopico dados, Usuario autorLogado) {
        var topico = topicoRepository.findById(dados.id())
                .orElseThrow(() -> new EntityNotFoundException("Tópico não encontrado com o ID: " + dados.id()));

        // Regra de negócio: Apenas o autor original pode editar o tópico.
        if (!topico.getAutor().equals(autorLogado)) {
            throw new ValidacaoException("Apenas o autor do tópico pode atualizá-lo.");
        }

        topico.atualizarInformacoes(dados);
        // A anotação @Transactional no controller cuidará do save.

        return new DadosDetalhamentoTopico(topico);
    }

    /**
     * Exclui um tópico existente.
     */
    public void excluir(Long id, Usuario autorLogado) {
        var topico = topicoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Tópico não encontrado com o ID: " + id));

        // Regra de negócio: Apenas o autor original pode excluir o tópico.
        if (!topico.getAutor().equals(autorLogado)) {
            throw new ValidacaoException("Apenas o autor do tópico pode excluí-lo.");
        }

        topicoRepository.delete(topico);
    }
}