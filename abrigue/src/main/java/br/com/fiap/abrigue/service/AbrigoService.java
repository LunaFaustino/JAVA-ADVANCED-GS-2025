package br.com.fiap.abrigue.service;

import br.com.fiap.abrigue.model.entity.Abrigo;

import java.util.List;
import java.util.Optional;

public interface AbrigoService {

    List<Abrigo> listarTodos();

    List<Abrigo> listarAtivos();

    Optional<Abrigo> buscarPorId(Long id);

    Abrigo salvar(Abrigo abrigo);

    void deletar(Long id);

    List<Abrigo> buscarPorNome(String nome);

    List<Abrigo> listarAbrigosComVagas();

    int calcularVagasDisponiveis(Abrigo abrigo);

    double calcularPercentualOcupacao(Abrigo abrigo);
}
