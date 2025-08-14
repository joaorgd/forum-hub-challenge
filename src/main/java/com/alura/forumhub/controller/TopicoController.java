// Controller com todos os endpoints para o CRUD de Tópicos.
package com.alura.forumhub.controller;

import com.alura.forumhub.domain.usuario.Usuario;
import com.alura.forumhub.dto.DadosAtualizacaoTopico;
import com.alura.forumhub.dto.DadosCadastroTopico;
import com.alura.forumhub.dto.DadosDetalhamentoTopico;
import com.alura.forumhub.dto.DadosListagemTopico;
import com.alura.forumhub.service.TopicoService;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("/topicos")
public class TopicoController {

    private final TopicoService topicoService;

    public TopicoController(TopicoService topicoService) {
        this.topicoService = topicoService;
    }

    @PostMapping
    @Transactional // Anotação para indicar que este método modifica o banco de dados.
    public ResponseEntity<DadosDetalhamentoTopico> cadastrar(@RequestBody @Valid DadosCadastroTopico dados, @AuthenticationPrincipal Usuario autor, UriComponentsBuilder uriBuilder) {
        // @RequestBody: Pega os dados do corpo da requisição.
        // @Valid: Aciona as validações definidas no DTO.
        // @AuthenticationPrincipal: Injeta o objeto do usuário que está logado.
        var topicoDto = topicoService.criar(dados, autor);
        // Cria a URI para o novo recurso criado, seguindo as boas práticas REST.
        var uri = uriBuilder.path("/topicos/{id}").buildAndExpand(topicoDto.id()).toUri();
        // Retorna o status 201 Created com a URI e os dados do novo tópico.
        return ResponseEntity.created(uri).body(topicoDto);
    }

    @GetMapping
    public ResponseEntity<Page<DadosListagemTopico>> listar(@PageableDefault(size = 10, sort = {"dataCriacao"}) Pageable paginacao) {
        // @PageableDefault: Define valores padrão para paginação e ordenação.
        var page = topicoService.listar(paginacao);
        return ResponseEntity.ok(page);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DadosDetalhamentoTopico> detalhar(@PathVariable Long id) {
        // @PathVariable: Pega o ID da URL (ex: /topicos/1).
        var topicoDto = topicoService.detalhar(id);
        return ResponseEntity.ok(topicoDto);
    }

    @PutMapping
    @Transactional
    public ResponseEntity<DadosDetalhamentoTopico> atualizar(@RequestBody @Valid DadosAtualizacaoTopico dados, @AuthenticationPrincipal Usuario autorLogado) {
        var topicoDto = topicoService.atualizar(dados, autorLogado);
        return ResponseEntity.ok(topicoDto);
    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity<Void> excluir(@PathVariable Long id, @AuthenticationPrincipal Usuario autorLogado) {
        topicoService.excluir(id, autorLogado);
        // Retorna o status 204 No Content, indicando sucesso sem corpo de resposta.
        return ResponseEntity.noContent().build();
    }
}