package br.com.fiap.abrigue.service;

import br.com.fiap.abrigue.model.entity.Abrigo;
import br.com.fiap.abrigue.model.enums.StatusAbrigo;
import br.com.fiap.abrigue.repository.AbrigoRepository;
import org.springframework.stereotype.Service;
import br.com.fiap.abrigue.config.RabbitMQConfig;

import java.util.List;
import java.util.Optional;

@Service
public class AbrigoService {

    private final AbrigoRepository abrigoRepository;
    private final MessagePublisherService messagePublisherService; // Nova dependência

    public AbrigoService(AbrigoRepository abrigoRepository,
                         MessagePublisherService messagePublisherService) {
        this.abrigoRepository = abrigoRepository;
        this.messagePublisherService = messagePublisherService;
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

        Double percentualAnterior = null;
        if (abrigo.getId() != null) {
            Optional<Abrigo> abrigoExistente = buscarPorId(abrigo.getId());
            if (abrigoExistente.isPresent()) {
                Abrigo anterior = abrigoExistente.get();
                percentualAnterior = calcularPercentualOcupacao(anterior);
            }
        }

        if (abrigo.getVagasOcupadas() >= abrigo.getCapacidadeMaxima()) {
            abrigo.setStatus(StatusAbrigo.LOTADO);
        } else if (abrigo.getStatus() == StatusAbrigo.LOTADO) {
            abrigo.setStatus(StatusAbrigo.ATIVO);
        }

        Abrigo abrigoSalvo = abrigoRepository.save(abrigo);

        verificarCapacidadeBaixa(abrigoSalvo, percentualAnterior);

        return abrigoSalvo;
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

    private void verificarCapacidadeBaixa(Abrigo abrigo, Double percentualAnterior) {
        try {
            double percentualAtual = calcularPercentualOcupacao(abrigo);

            boolean capacidadeAtualBaixa = percentualAtual >= 80.0;
            boolean capacidadeAnteriorBaixa = percentualAnterior != null && percentualAnterior >= 80.0;

            if (capacidadeAtualBaixa && (percentualAnterior == null || !capacidadeAnteriorBaixa)) {
                messagePublisherService.enviarMensagemAbrigoCapacidadeBaixa(abrigo);
            }

            if (percentualAtual >= 95.0) {
                var alertaCritico = new java.util.HashMap<String, Object>();
                alertaCritico.put("tipo", "CRITICO_CAPACIDADE");
                alertaCritico.put("abrigoId", abrigo.getId());
                alertaCritico.put("nomeAbrigo", abrigo.getNome());
                alertaCritico.put("percentualOcupacao", percentualAtual);
                alertaCritico.put("vagasDisponiveis", calcularVagasDisponiveis(abrigo));
                alertaCritico.put("mensagem", "ABRIGO QUASE LOTADO - AÇÃO URGENTE NECESSÁRIA");

                messagePublisherService.enviarMensagem(RabbitMQConfig.ALERTAS_CRITICOS_QUEUE, alertaCritico);
            }

        } catch (Exception e) {
            System.err.println("Erro ao verificar capacidade baixa: " + e.getMessage());
        }
    }
}
