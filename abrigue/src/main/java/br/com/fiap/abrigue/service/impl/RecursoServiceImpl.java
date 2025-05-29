package br.com.fiap.abrigue.service.impl;

import br.com.fiap.abrigue.config.RabbitMQConfig;
import br.com.fiap.abrigue.model.entity.Recurso;
import br.com.fiap.abrigue.model.enums.TipoRecurso;
import br.com.fiap.abrigue.repository.RecursoRepository;
import br.com.fiap.abrigue.service.MessagePublisherService;
import br.com.fiap.abrigue.service.RecursoService;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class RecursoServiceImpl implements RecursoService {

    private final RecursoRepository recursoRepository;
    private final MessagePublisherService messagePublisherService;

    public RecursoServiceImpl(RecursoRepository recursoRepository,
                              MessagePublisherService messagePublisherService) {
        this.recursoRepository = recursoRepository;
        this.messagePublisherService = messagePublisherService;
    }

    @Override
    public List<Recurso> listarTodos() {
        return recursoRepository.findAll();
    }

    @Override
    public List<Recurso> listarPorAbrigo(Long abrigoId) {
        return recursoRepository.findByAbrigoId(abrigoId);
    }

    @Override
    public Optional<Recurso> buscarPorId(Long id) {
        return recursoRepository.findById(id);
    }

    @Override
    public Recurso salvar(Recurso recurso) {
        Integer quantidadeAnterior = null;

        if (recurso.getId() != null) {
            Optional<Recurso> recursoExistente = buscarPorId(recurso.getId());
            if (recursoExistente.isPresent()) {
                quantidadeAnterior = recursoExistente.get().getQuantidade();
            }
        }

        recurso.setDataAtualizacao(new Date());
        Recurso recursoSalvo = recursoRepository.save(recurso);

        verificarEstoqueBaixo(recursoSalvo, quantidadeAnterior);

        return recursoSalvo;
    }

    @Override
    public void deletar(Long id) {
        recursoRepository.deleteById(id);
    }

    @Override
    public List<Recurso> buscarPorNome(String nome) {
        return recursoRepository.findByNomeContainingIgnoreCase(nome);
    }

    @Override
    public List<Recurso> listarPorTipo(TipoRecurso tipo) {
        return recursoRepository.findByTipo(tipo);
    }

    @Override
    public List<Recurso> listarRecursosComEstoqueBaixo() {
        return recursoRepository.findRecursosComEstoqueBaixo();
    }

    @Override
    public List<Recurso> listarPorAbrigoETipo(Long abrigoId, TipoRecurso tipo) {
        return recursoRepository.findByAbrigoIdAndTipo(abrigoId, tipo);
    }

    @Override
    public boolean isEstoqueBaixo(Recurso recurso) {
        return recurso.getQuantidade() <= 10;
    }

    @Override
    public int getTotalRecursosPorAbrigo(Long abrigoId) {
        return listarPorAbrigo(abrigoId).size();
    }

    @Override
    public Recurso adicionarQuantidade(Long recursoId, Integer quantidadeAdicional) {
        Optional<Recurso> recursoOpt = buscarPorId(recursoId);

        if (recursoOpt.isPresent()) {
            Recurso recurso = recursoOpt.get();
            Integer quantidadeAnterior = recurso.getQuantidade();

            recurso.setQuantidade(recurso.getQuantidade() + quantidadeAdicional);

            return salvar(recurso);
        }

        throw new RuntimeException("Recurso não encontrado com ID: " + recursoId);
    }

    @Override
    public Recurso removerQuantidade(Long recursoId, Integer quantidadeRemover) {
        Optional<Recurso> recursoOpt = buscarPorId(recursoId);

        if (recursoOpt.isPresent()) {
            Recurso recurso = recursoOpt.get();

            if (recurso.getQuantidade() >= quantidadeRemover) {
                recurso.setQuantidade(recurso.getQuantidade() - quantidadeRemover);
                return salvar(recurso);
            } else {
                throw new RuntimeException("Quantidade insuficiente em estoque. Disponível: " +
                        recurso.getQuantidade() + ", Solicitado: " + quantidadeRemover);
            }
        }

        throw new RuntimeException("Recurso não encontrado com ID: " + recursoId);
    }

    private void verificarEstoqueBaixo(Recurso recurso, Integer quantidadeAnterior) {
        try {
            boolean estoqueAtualBaixo = isEstoqueBaixo(recurso);
            boolean estoqueAnteriorBaixo = quantidadeAnterior != null && quantidadeAnterior <= 10;

            if (estoqueAtualBaixo && (quantidadeAnterior == null || !estoqueAnteriorBaixo)) {
                messagePublisherService.enviarMensagemRecursoEstoqueBaixo(recurso);
            }

            if (recurso.getQuantidade() <= 3) {
                var alertaCritico = new java.util.HashMap<String, Object>();
                alertaCritico.put("tipo", "CRITICO");
                alertaCritico.put("recursoId", recurso.getId());
                alertaCritico.put("nomeRecurso", recurso.getNome());
                alertaCritico.put("quantidade", recurso.getQuantidade());
                alertaCritico.put("mensagem", "ESTOQUE CRÍTICO - AÇÃO URGENTE NECESSÁRIA");

                messagePublisherService.enviarMensagem(RabbitMQConfig.ALERTAS_CRITICOS_QUEUE, alertaCritico);
            }

        } catch (Exception e) {
            System.err.println("Erro ao verificar estoque baixo: " + e.getMessage());
        }
    }
}