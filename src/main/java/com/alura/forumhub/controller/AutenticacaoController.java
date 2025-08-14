// Controller responsável pelo endpoint de login.
package com.alura.forumhub.controller;

import com.alura.forumhub.domain.usuario.Usuario;
import com.alura.forumhub.dto.DadosAutenticacao;
import com.alura.forumhub.dto.DadosTokenJWT;
import com.alura.forumhub.service.TokenService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/login")
public class AutenticacaoController {

    private final AuthenticationManager manager;
    private final TokenService tokenService;

    public AutenticacaoController(AuthenticationManager manager, TokenService tokenService) {
        this.manager = manager;
        this.tokenService = tokenService;
    }

    @PostMapping
    public ResponseEntity efetuarLogin(@RequestBody @Valid DadosAutenticacao dados) {
        // 1. Cria um objeto de autenticação com as credenciais recebidas.
        var authenticationToken = new UsernamePasswordAuthenticationToken(dados.email(), dados.senha());

        // 2. Chama o AuthenticationManager do Spring para realizar a autenticação.
        //    Ele usará o AutenticacaoService para buscar o usuário e o PasswordEncoder para comparar as senhas.
        var authentication = manager.authenticate(authenticationToken);

        // 3. Se a autenticação for bem-sucedida, gera o token JWT.
        var tokenJWT = tokenService.gerarToken((Usuario) authentication.getPrincipal());

        // 4. Retorna o token JWT no corpo da resposta com status 200 OK.
        return ResponseEntity.ok(new DadosTokenJWT(tokenJWT));
    }
}