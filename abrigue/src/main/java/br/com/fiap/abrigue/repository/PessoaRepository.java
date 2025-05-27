package br.com.fiap.abrigue.repository;

import br.com.fiap.abrigue.model.entity.Pessoa;
import br.com.fiap.abrigue.model.enums.SituacaoEspecial;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PessoaRepository extends JpaRepository<Pessoa, Long> {

    List<Pessoa> findByAbrigoId(Long abrigoId);

    List<Pessoa> findByNomeContainingIgnoreCase(String nome);

    Optional<Pessoa> findByCpf(String cpf);

    List<Pessoa> findBySituacaoEspecial(SituacaoEspecial situacaoEspecial);

    @Query("SELECT COUNT(p) FROM Pessoa p WHERE p.abrigo.id = ?1")
    Long contarPessoasPorAbrigo(Long abrigoId);

    @Query("SELECT p FROM Pessoa p WHERE p.abrigo.id = ?1 AND p.situacaoEspecial != ?2")
    List<Pessoa> findByAbrigoIdAndSituacaoEspecialNot(Long abrigoId, SituacaoEspecial situacaoEspecial);
}
