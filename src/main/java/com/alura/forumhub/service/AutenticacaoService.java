// Esta classe é a ponte entre o Spring Security e nosso sistema de usuários.
package com.alura.forumhub.service;

import com.alura.forumhub.repository.UsuarioRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class AutenticacaoService implements UserDetailsService {

    private final UsuarioRepository repository;

    public AutenticacaoService(UsuarioRepository repository) {
        this.repository = repository;
    }

    // O Spring Security chama este método quando precisa carregar os dados de um usuário
    // para realizar a autenticação.
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // No nosso sistema, o "username" é o email do usuário.
        // Buscamos o usuário no banco de dados pelo email.
        return repository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado com o email: " + username));
    }
}