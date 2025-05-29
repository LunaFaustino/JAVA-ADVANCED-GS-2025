package br.com.fiap.abrigue.service;

import br.com.fiap.abrigue.model.entity.Pessoa;
import br.com.fiap.abrigue.model.enums.SituacaoEspecial;

import java.util.List;
import java.util.Optional;

public interface PessoaService {

    List<Pessoa> listarTodas();

    List<Pessoa> listarPorAbrigo(Long abrigoId);

    Optional<Pessoa> buscarPorId(Long id);

    Optional<Pessoa> buscarPorCpf(String cpf);

    Pessoa salvar(Pessoa pessoa);

    void deletar(Long id);

    List<Pessoa> buscarPorNome(String nome);

    List<Pessoa> listarPorSituacaoEspecial(SituacaoEspecial situacaoEspecial);

    boolean cpfJaExiste(String cpf, Long idPessoa);
}