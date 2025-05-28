package br.com.fiap.abrigue.service;

import br.com.fiap.abrigue.config.RabbitMQConfig;
import br.com.fiap.abrigue.dto.PessoaCadastradaMessage;
import br.com.fiap.abrigue.model.entity.Pessoa;
import br.com.fiap.abrigue.model.entity.Recurso;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
public class MessagePublisherService {

    private static final Logger logger = LoggerFactory.getLogger(MessagePublisherService.class);

    private final RabbitTemplate rabbitTemplate;

    public MessagePublisherService(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void enviarMensagemPessoaCadastrada(Pessoa pessoa) {
        try {
            PessoaCadastradaMessage message = new PessoaCadastradaMessage(
                    pessoa.getId(),
                    pessoa.getNome(),
                    pessoa.getCpf(),
                    pessoa.getAbrigo() != null ? pessoa.getAbrigo().getId() : null,
                    pessoa.getAbrigo() != null ? pessoa.getAbrigo().getNome() : null
            );

            rabbitTemplate.convertAndSend(RabbitMQConfig.PESSOA_CADASTRADA_QUEUE, message);

            logger.info("Mensagem enviada para fila {}: {}",
                    RabbitMQConfig.PESSOA_CADASTRADA_QUEUE, message);

        } catch (Exception e) {
            logger.error("Erro ao enviar mensagem de pessoa cadastrada: {}", e.getMessage(), e);
        }
    }

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

    public void enviarMensagem(String fila, Object mensagem) {
        try {
            rabbitTemplate.convertAndSend(fila, mensagem);
            logger.info("Mensagem enviada para fila {}: {}", fila, mensagem);
        } catch (Exception e) {
            logger.error("Erro ao enviar mensagem para fila {}: {}", fila, e.getMessage(), e);
        }
    }
}
