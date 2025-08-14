// Classe de serviço responsável por gerar e validar os tokens JWT.
package com.alura.forumhub.service;

import com.alura.forumhub.domain.usuario.Usuario;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Service
public class TokenService {

    // Injeta a chave secreta definida no arquivo application.properties.
    @Value("${api.security.token.secret}")
    private String secret;

    private static final String ISSUER = "API ForumHub"; // Emissor do token.

    // Gera um novo token JWT para o usuário fornecido.
    public String gerarToken(Usuario usuario) {
        try {
            // Define o algoritmo de assinatura (HMAC256) com a nossa chave secreta.
            Algorithm algoritmo = Algorithm.HMAC256(secret);
            return JWT.create()
                    .withIssuer(ISSUER) // Define o emissor.
                    .withSubject(usuario.getEmail()) // Define o "assunto" do token, geralmente o identificador do usuário.
                    .withExpiresAt(dataExpiracao()) // Define o tempo de validade do token.
                    .sign(algoritmo); // Assina o token.
        } catch (JWTCreationException exception){
            throw new RuntimeException("Erro ao gerar token jwt", exception);
        }
    }

    // Valida um token JWT e retorna o "subject" (email) se o token for válido.
    public String getSubject(String tokenJWT) {
        try {
            Algorithm algoritmo = Algorithm.HMAC256(secret);
            return JWT.require(algoritmo)
                    .withIssuer(ISSUER) // Verifica se o emissor é o mesmo.
                    .build()
                    .verify(tokenJWT) // Tenta verificar a assinatura do token. Se falhar, lança uma exceção.
                    .getSubject(); // Retorna o subject.
        } catch (JWTVerificationException exception) {
            // Lança uma exceção se o token for inválido ou estiver expirado.
            throw new RuntimeException("Token JWT inválido ou expirado!");
        }
    }

    // Define a data de expiração do token (neste caso, 2 horas a partir de agora).
    private Instant dataExpiracao() {
        return LocalDateTime.now().plusHours(2).toInstant(ZoneOffset.of("-03:00"));
    }
}