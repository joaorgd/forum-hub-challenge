package com.alura.forumhub.repository;

import com.alura.forumhub.domain.topico.Topico;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TopicoRepository extends JpaRepository<Topico, Long> {

    // Valida se um tópico com o mesmo título já existe.
    // A anotação de validação (@NotBlank) foi removida. Veja a explicação abaixo.
    boolean existsByTitulo(String titulo);

    // Valida se um tópico com a mesma mensagem já existe.
    // Este método é necessário para a regra de negócio no seu TopicoService.
    boolean existsByMensagem(String mensagem);

    // Encontra um tópico pelo título exato.
    // Retorna um Optional para tratar casos onde o tópico não é encontrado.
    Optional<Topico> findByTitulo(String titulo);

    // Exemplo de busca paginada por um atributo de uma entidade relacionada (autor).
    // Útil para uma funcionalidade "ver todos os tópicos de um usuário".
    Page<Topico> findAllByAutorNome(String nomeAutor, Pageable paginacao);
}