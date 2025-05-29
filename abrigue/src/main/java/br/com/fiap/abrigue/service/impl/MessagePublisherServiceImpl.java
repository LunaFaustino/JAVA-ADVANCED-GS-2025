package br.com.fiap.abrigue.service.impl;

import br.com.fiap.abrigue.config.RabbitMQConfig;
import br.com.fiap.abrigue.dto.AbrigoCapacidadeBaixaMessage;
import br.com.fiap.abrigue.model.entity.Abrigo;
import br.com.fiap.abrigue.model.entity.Recurso;
import br.com.fiap.abrigue.service.MessagePublisherService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
public class MessagePublisherServiceImpl implements MessagePublisherService {

    private static final Logger logger = LoggerFactory.getLogger(MessagePublisherServiceImpl.class);

    private final RabbitTemplate rabbitTemplate;

    public MessagePublisherServiceImpl(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @Override
    public void enviarMensagemAbrigoCapacidadeBaixa(Abrigo abrigo) {
        try {
            AbrigoCapacidadeBaixaMessage message = new AbrigoCapacidadeBaixaMessage(
                    abrigo.getId(),
                    abrigo.getNome(),
                    abrigo.getEndereco(),
                    abrigo.getCapacidadeMaxima(),
                    abrigo.getVagasOcupadas(),
                    abrigo.getResponsavel(),
                    abrigo.getTelefone()
            );

            rabbitTemplate.convertAndSend(RabbitMQConfig.ABRIGO_CAPACIDADE_BAIXA_QUEUE, message);

            logger.info("Mensagem enviada para fila {}: {}",
                    RabbitMQConfig.ABRIGO_CAPACIDADE_BAIXA_QUEUE, message);

        } catch (Exception e) {
            logger.error("Erro ao enviar mensagem de abrigo com capacidade baixa: {}", e.getMessage(), e);
        }
    }

    @Override
    public void enviarMensagemRecursoEstoqueBaixo(Recurso recurso) {
        try {
            var message = new java.util.HashMap<String, Object>();
            message.put("recursoId", recurso.getId());
            message.put("nomeRecurso", recurso.getNome());
            message.put("quantidade", recurso.getQuantidade());
            message.put("tipoRecurso", recurso.getTipo().getDescricao());
            message.put("abrigoId", recurso.getAbrigo() != null ? recurso.getAbrigo().getId() : null);
            message.put("nomeAbrigo", recurso.getAbrigo() != null ? recurso.getAbrigo().getNome() : null);
            message.put("dataHora", java.time.LocalDateTime.now());

            rabbitTemplate.convertAndSend(RabbitMQConfig.RECURSO_ESTOQUE_BAIXO_QUEUE, message);

            logger.info("Mensagem de estoque baixo enviada para fila {}: {}",
                    RabbitMQConfig.RECURSO_ESTOQUE_BAIXO_QUEUE, message);

        } catch (Exception e) {
            logger.error("Erro ao enviar mensagem de estoque baixo: {}", e.getMessage(), e);
        }
    }

    @Override
    public void enviarMensagem(String fila, Object mensagem) {
        try {
            rabbitTemplate.convertAndSend(fila, mensagem);
            logger.info("Mensagem enviada para fila {}: {}", fila, mensagem);
        } catch (Exception e) {
            logger.error("Erro ao enviar mensagem para fila {}: {}", fila, e.getMessage(), e);
        }
    }
}