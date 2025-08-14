// Esta classe é o coração da configuração de segurança da sua API.
package com.alura.forumhub.infra.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration // Indica que esta é uma classe de configuração do Spring.
@EnableWebSecurity // Habilita a configuração de segurança web personalizada.
public class SecurityConfigurations {

    private final SecurityFilter securityFilter;

    public SecurityConfigurations(SecurityFilter securityFilter) {
        this.securityFilter = securityFilter;
    }

    @Bean // @Bean expõe o objeto retornado pelo método como um Bean gerenciado pelo Spring.
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                // Desabilita a proteção contra CSRF (Cross-Site Request Forgery),
                // pois a autenticação via token JWT já previne este tipo de ataque.
                .csrf(csrf -> csrf.disable())

                // Configura a política de gerenciamento de sessão como STATELESS.
                // Isso significa que a API não guardará estado de autenticação no servidor.
                // Cada requisição precisará enviar o token.
                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                // Configura as regras de autorização para as requisições HTTP.
                .authorizeHttpRequests(req -> {
                    // Permite que requisições POST para a URL "/login" sejam feitas sem autenticação.
                    req.requestMatchers(HttpMethod.POST, "/login").permitAll();
                    // Exige que todas as outras requisições ("anyRequest") sejam autenticadas.
                    req.anyRequest().authenticated();
                })

                // Adiciona o nosso filtro personalizado (SecurityFilter) para ser executado
                // ANTES do filtro padrão de autenticação do Spring (UsernamePasswordAuthenticationFilter).
                .addFilterBefore(securityFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    // Necessário para o AutenticacaoController poder injetar e usar o AuthenticationManager.
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    // Define o algoritmo de criptografia de senhas que será usado na aplicação (BCrypt).
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}