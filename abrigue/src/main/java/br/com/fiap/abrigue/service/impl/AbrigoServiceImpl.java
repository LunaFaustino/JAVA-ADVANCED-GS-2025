package br.com.fiap.abrigue.service.impl;

import br.com.fiap.abrigue.config.RabbitMQConfig;
import br.com.fiap.abrigue.model.entity.Abrigo;
import br.com.fiap.abrigue.model.enums.StatusAbrigo;
import br.com.fiap.abrigue.repository.AbrigoRepository;
import br.com.fiap.abrigue.service.AbrigoService;
import br.com.fiap.abrigue.service.MessagePublisherService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AbrigoServiceImpl implements AbrigoService {

    private final AbrigoRepository abrigoRepository;
    private final MessagePublisherService messagePublisherService;

    public AbrigoServiceImpl(AbrigoRepository abrigoRepository,
                             MessagePublisherService messagePublisherService) {
        this.abrigoRepository = abrigoRepository;
        this.messagePublisherService = messagePublisherService;
    }

    @Override
    public List<Abrigo> listarTodos() {
        return abrigoRepository.findAll();
    }

    @Override
    public List<Abrigo> listarAtivos() {
        return abrigoRepository.findByStatus(StatusAbrigo.ATIVO);
    }

    @Override
    public Optional<Abrigo> buscarPorId(Long id) {
        return abrigoRepository.findById(id);
    }

    @Override
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

    @Override
    public void deletar(Long id) {
        abrigoRepository.deleteById(id);
    }

    @Override
    public List<Abrigo> buscarPorNome(String nome) {
        return abrigoRepository.findByNomeContainingIgnoreCase(nome);
    }

    @Override
    public List<Abrigo> listarAbrigosComVagas() {
        return abrigoRepository.findAbrigosComVagas();
    }

    @Override
    public int calcularVagasDisponiveis(Abrigo abrigo) {
        return abrigo.getCapacidadeMaxima() - abrigo.getVagasOcupadas();
    }

    @Override
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
