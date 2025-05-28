package br.com.fiap.abrigue.service;

import br.com.fiap.abrigue.config.RabbitMQConfig;
import br.com.fiap.abrigue.dto.PessoaCadastradaMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class MessageConsumerService {

    private static final Logger logger = LoggerFactory.getLogger(MessageConsumerService.class);

    @RabbitListener(queues = RabbitMQConfig.PESSOA_CADASTRADA_QUEUE)
    public void processarPessoaCadastrada(PessoaCadastradaMessage message) {
        try {
            logger.info("=== NOVA PESSOA CADASTRADA ===");
            logger.info("Processando mensagem: {}", message);

            simularProcessamento();

            logger.info("Pessoa {} foi cadastrada no abrigo {} com sucesso!",
                    message.getNomePessoa(), message.getNomeAbrigo());
            logger.info("CPF: {}, Data/Hora: {}", message.getCpf(), message.getDataHora());

            if (message.getNomeAbrigo() != null) {
                logger.info("Enviando notificação para responsável do abrigo: {}", message.getNomeAbrigo());
            }

            logger.info("=== PROCESSAMENTO CONCLUÍDO ===");

        } catch (Exception e) {
            logger.error("Erro ao processar mensagem de pessoa cadastrada: {}", e.getMessage(), e);
        }
    }

    @RabbitListener(queues = RabbitMQConfig.RECURSO_ESTOQUE_BAIXO_QUEUE)
    public void processarRecursoEstoqueBaixo(Map<String, Object> message) {
        try {
            logger.info("=== ALERTA: ESTOQUE BAIXO ===");
            logger.info("Processando alerta de estoque baixo: {}", message);

            String nomeRecurso = (String) message.get("nomeRecurso");
            Integer quantidade = (Integer) message.get("quantidade");
            String tipoRecurso = (String) message.get("tipoRecurso");
            String nomeAbrigo = (String) message.get("nomeAbrigo");

            simularProcessamento();

            logger.warn("ATENÇÃO: O recurso '{}' do tipo '{}' está com estoque baixo!",
                    nomeRecurso, tipoRecurso);
            logger.warn("Quantidade atual: {} unidades no abrigo: {}", quantidade, nomeAbrigo);

            if (quantidade <= 5) {
                logger.error("CRÍTICO: Estoque muito baixo para o recurso: {}", nomeRecurso);
            }

            logger.info("=== ALERTA PROCESSADO ===");

        } catch (Exception e) {
            logger.error("Erro ao processar alerta de estoque baixo: {}", e.getMessage(), e);
        }
    }

    private void simularProcessamento() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    private void processarNotificacao(String tipo, String mensagem) {
        logger.info("Processando notificação do tipo: {}", tipo);
        logger.info("Mensagem: {}", mensagem);
    }
}
