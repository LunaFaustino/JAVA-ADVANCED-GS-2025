package br.com.fiap.abrigue.service;

import br.com.fiap.abrigue.model.entity.Recurso;
import br.com.fiap.abrigue.model.enums.TipoRecurso;
import br.com.fiap.abrigue.repository.RecursoRepository;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class RecursoService {

    private final RecursoRepository recursoRepository;

    public RecursoService(RecursoRepository recursoRepository) {
        this.recursoRepository = recursoRepository;
    }

    public List<Recurso> listarTodos() {
        return recursoRepository.findAll();
    }

    public List<Recurso> listarPorAbrigo(Long abrigoId) {
        return recursoRepository.findByAbrigoId(abrigoId);
    }

    public Optional<Recurso> buscarPorId(Long id) {
        return recursoRepository.findById(id);
    }

    public Recurso salvar(Recurso recurso) {
        recurso.setDataAtualizacao(new Date());
        return recursoRepository.save(recurso);
    }

    public void deletar(Long id) {
        recursoRepository.deleteById(id);
    }

    public List<Recurso> buscarPorNome(String nome) {
        return recursoRepository.findByNomeContainingIgnoreCase(nome);
    }

    public List<Recurso> listarPorTipo(TipoRecurso tipo) {
        return recursoRepository.findByTipo(tipo);
    }

    public List<Recurso> listarRecursosComEstoqueBaixo() {
        return recursoRepository.findRecursosComEstoqueBaixo();
    }

    public List<Recurso> listarPorAbrigoETipo(Long abrigoId, TipoRecurso tipo) {
        return recursoRepository.findByAbrigoIdAndTipo(abrigoId, tipo);
    }

    public boolean isEstoqueBaixo(Recurso recurso) {
        return recurso.getQuantidade() <= 10;
    }

    public int getTotalRecursosPorAbrigo(Long abrigoId) {
        return listarPorAbrigo(abrigoId).size();
    }
}
