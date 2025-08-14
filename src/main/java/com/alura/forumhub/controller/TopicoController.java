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
    @Transactional
    public ResponseEntity<DadosDetalhamentoTopico> cadastrar(@RequestBody @Valid DadosCadastroTopico dados, @AuthenticationPrincipal Usuario autor, UriComponentsBuilder uriBuilder) {
        var topicoDto = topicoService.criar(dados, autor);
        var uri = uriBuilder.path("/topicos/{id}").buildAndExpand(topicoDto.id()).toUri();
        return ResponseEntity.created(uri).body(topicoDto);
    }

    @GetMapping
    public ResponseEntity<Page<DadosListagemTopico>> listar(@PageableDefault(size = 10, sort = {"dataCriacao"}) Pageable paginacao) {
        var page = topicoService.listar(paginacao);
        return ResponseEntity.ok(page);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DadosDetalhamentoTopico> detalhar(@PathVariable Long id) {
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
        return ResponseEntity.noContent().build();
    }
}