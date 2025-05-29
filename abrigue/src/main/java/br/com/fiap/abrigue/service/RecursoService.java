package br.com.fiap.abrigue.service;

import br.com.fiap.abrigue.model.entity.Recurso;
import br.com.fiap.abrigue.model.enums.TipoRecurso;

import java.util.List;
import java.util.Optional;

public interface RecursoService {

    List<Recurso> listarTodos();

    List<Recurso> listarPorAbrigo(Long abrigoId);

    Optional<Recurso> buscarPorId(Long id);

    Recurso salvar(Recurso recurso);

    void deletar(Long id);

    List<Recurso> buscarPorNome(String nome);

    List<Recurso> listarPorTipo(TipoRecurso tipo);

    List<Recurso> listarRecursosComEstoqueBaixo();

    List<Recurso> listarPorAbrigoETipo(Long abrigoId, TipoRecurso tipo);

    boolean isEstoqueBaixo(Recurso recurso);

    int getTotalRecursosPorAbrigo(Long abrigoId);

    Recurso adicionarQuantidade(Long recursoId, Integer quantidadeAdicional);

    Recurso removerQuantidade(Long recursoId, Integer quantidadeRemover);
}