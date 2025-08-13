package com.alura.forumhub.controller;


import com.alura.forumhub.domain.topico.Topico;
import com.alura.forumhub.domain.usuario.Usuario;
import com.alura.forumhub.dto.DadosCadastroTopico;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

// no pacote com.alura.forumhub.controller
@RestController
@RequestMapping("/topicos")
public class TopicoController {

    @Autowired
    private Topico topicoService; // Injetar o serviço

    @PostMapping
    @Transactional
    public ResponseEntity cadastrar(@RequestBody @Valid DadosCadastroTopico dados, @AuthenticationPrincipal Usuario autor) {
        // O autor é pego do usuário logado, não mais do DTO
        var topicoDetalhado = topicoService.criar(dados, autor);
        // Retornar 201 Created com a localização do novo recurso
        // UriComponentsBuilder uriBuilder...
        return ResponseEntity.ok(topicoDetalhado);
    }

    // Os outros métodos (listar, detalhar, atualizar, deletar) podem ser implementados
    // de forma similar, delegando a lógica para o TopicoService.
    // O método de atualização e deleção deve verificar se o usuário logado é o autor do tópico.
}