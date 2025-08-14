// Este filtro intercepta TODAS as requisições para validar o token JWT.
package com.alura.forumhub.infra.security;

import com.alura.forumhub.repository.UsuarioRepository;
import com.alura.forumhub.service.TokenService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component // Marca esta classe como um componente do Spring para que possa ser injetada.
public class SecurityFilter extends OncePerRequestFilter {

    private final TokenService tokenService;
    private final UsuarioRepository usuarioRepository;

    public SecurityFilter(TokenService tokenService, UsuarioRepository usuarioRepository) {
        this.tokenService = tokenService;
        this.usuarioRepository = usuarioRepository;
    }

    // Este método é executado para cada requisição que chega na API.
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // 1. Recupera o token do cabeçalho da requisição.
        var tokenJWT = recuperarToken(request);

        // 2. Se um token foi encontrado...
        if (tokenJWT != null) {
            // 3. Valida o token e extrai o "subject" (que é o email do usuário).
            var subject = tokenService.getSubject(tokenJWT);
            // 4. Busca o usuário correspondente no banco de dados.
            var usuarioOpt = usuarioRepository.findByEmail(subject);

            if (usuarioOpt.isPresent()) {
                var usuario = usuarioOpt.get();
                // 5. Cria um objeto de autenticação para o Spring Security.
                var authentication = new UsernamePasswordAuthenticationToken(usuario, null, usuario.getAuthorities());

                // 6. Define o usuário como autenticado no contexto de segurança do Spring para esta requisição.
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }

        // 7. Continua o fluxo da requisição, passando para o próximo filtro na cadeia.
        filterChain.doFilter(request, response);
    }

    // Método auxiliar para extrair o token do cabeçalho "Authorization".
    private String recuperarToken(HttpServletRequest request) {
        var authorizationHeader = request.getHeader("Authorization");
        if (authorizationHeader != null) {
            // O token vem no formato "Bearer eyJhbGciOiJI...", então removemos o prefixo "Bearer ".
            return authorizationHeader.replace("Bearer ", "");
        }
        return null;
    }
}