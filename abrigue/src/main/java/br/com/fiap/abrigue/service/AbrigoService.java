package br.com.fiap.abrigue.service;

import br.com.fiap.abrigue.model.entity.Abrigo;
import br.com.fiap.abrigue.model.enums.StatusAbrigo;
import br.com.fiap.abrigue.repository.AbrigoRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AbrigoService {

    private final AbrigoRepository abrigoRepository;

    public AbrigoService(AbrigoRepository abrigoRepository) {
        this.abrigoRepository = abrigoRepository;
    }

    public List<Abrigo> listarTodos() {
        return abrigoRepository.findAll();
    }

    public List<Abrigo> listarAtivos() {
        return abrigoRepository.findByStatus(StatusAbrigo.ATIVO);
    }

    public Optional<Abrigo> buscarPorId(Long id) {
        return abrigoRepository.findById(id);
    }

    public Abrigo salvar(Abrigo abrigo) {

        if (abrigo.getVagasOcupadas() >= abrigo.getCapacidadeMaxima()) {
            abrigo.setStatus(StatusAbrigo.LOTADO);
        } else if (abrigo.getStatus() == StatusAbrigo.LOTADO) {
            abrigo.setStatus(StatusAbrigo.ATIVO);
        }

        return abrigoRepository.save(abrigo);
    }

    public void deletar(Long id) {
        abrigoRepository.deleteById(id);
    }

    public List<Abrigo> buscarPorNome(String nome) {
        return abrigoRepository.findByNomeContainingIgnoreCase(nome);
    }

    public List<Abrigo> listarAbrigosComVagas() {
        return abrigoRepository.findAbrigosComVagas();
    }

    public int calcularVagasDisponiveis(Abrigo abrigo) {
        return abrigo.getCapacidadeMaxima() - abrigo.getVagasOcupadas();
    }

    public double calcularPercentualOcupacao(Abrigo abrigo) {
        if (abrigo.getCapacidadeMaxima() == 0) return 0;
        return (double) abrigo.getVagasOcupadas() / abrigo.getCapacidadeMaxima() * 100;
    }
}
