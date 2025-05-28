package br.com.fiap.abrigue.service;

import br.com.fiap.abrigue.config.RabbitMQConfig;
import br.com.fiap.abrigue.dto.AbrigoCapacidadeBaixaMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class MessageConsumerService {

    private static final Logger logger = LoggerFactory.getLogger(MessageConsumerService.class);

    @RabbitListener(queues = RabbitMQConfig.ABRIGO_CAPACIDADE_BAIXA_QUEUE)
    public void processarAbrigoCapacidadeBaixa(AbrigoCapacidadeBaixaMessage message) {
        try {
            logger.info("=== ALERTA: ABRIGO COM CAPACIDADE BAIXA ===");
            logger.info("Processando mensagem: {}", message);

            simularProcessamento();

            logger.warn("ATENÇÃO: O abrigo '{}' está com baixa capacidade disponível!",
                    message.getNomeAbrigo());
            logger.warn("Endereço: {}", message.getEndereco());
            logger.warn("Ocupação atual: {}/{} vagas ({:.1f}% ocupado)",
                    message.getVagasOcupadas(), message.getCapacidadeMaxima(), message.getPercentualOcupacao());
            logger.warn("Apenas {} vagas disponíveis!", message.getVagasDisponiveis());

            if (message.getPercentualOcupacao() >= 90) {
                logger.error("CRÍTICO: Abrigo quase lotado! Ação urgente necessária!");
                logger.info("Iniciando protocolo de emergência para realocação...");
            } else if (message.getPercentualOcupacao() >= 80) {
                logger.warn("ALERTA: Preparando protocolos de contingência...");
                logger.info("Buscando abrigos alternativos na região...");
            }

            if (message.getResponsavel() != null && !message.getResponsavel().trim().isEmpty()) {
                logger.info("Notificando responsável: {}", message.getResponsavel());
                if (message.getTelefone() != null) {
                    logger.info("Enviando SMS para: {}", message.getTelefone());
                }
            }

            logger.info("=== PROCESSAMENTO DE ALERTA CONCLUÍDO ===");

        } catch (Exception e) {
            logger.error("Erro ao processar mensagem de abrigo com capacidade baixa: {}", e.getMessage(), e);
        }
    }

    @RabbitListener(queues = RabbitMQConfig.ALERTAS_CRITICOS_QUEUE)
    public void processarAlertaCritico(Map<String, Object> message) {
        try {
            logger.error("=== 🚨 ALERTA CRÍTICO 🚨 ===");
            logger.error("Processando alerta crítico: {}", message);

            String tipo = (String) message.get("tipo");
            String nomeAbrigo = (String) message.get("nomeAbrigo");
            String nomeRecurso = (String) message.get("nomeRecurso");
            String mensagemAlerta = (String) message.get("mensagem");

            simularProcessamento();

            if ("CRITICO_CAPACIDADE".equals(tipo)) {
                Double percentualOcupacao = (Double) message.get("percentualOcupacao");
                Integer vagasDisponiveis = (Integer) message.get("vagasDisponiveis");

                logger.error("🏠 CAPACIDADE CRÍTICA: Abrigo '{}' está {}% ocupado!",
                        nomeAbrigo, percentualOcupacao);
                logger.error("⚠️  Apenas {} vagas disponíveis!", vagasDisponiveis);
                logger.error("🚨 {}", mensagemAlerta);

                logger.error("📞 ACIONANDO PROTOCOLO DE EMERGÊNCIA!");
                logger.error("🚑 Contactando serviços de emergência...");
                logger.error("🏥 Buscando abrigos alternativos urgentemente...");
                logger.error("📧 Notificando autoridades competentes...");

            } else if ("CRITICO".equals(tipo)) {
                Integer quantidade = (Integer) message.get("quantidade");

                logger.error("📦 ESTOQUE CRÍTICO: Recurso '{}' com apenas {} unidades!",
                        nomeRecurso, quantidade);
                logger.error("🚨 {}", mensagemAlerta);

                logger.error("📞 ACIONANDO FORNECEDORES DE EMERGÊNCIA!");
                logger.error("🚚 Solicitando entrega urgente...");
                logger.error("📧 Notificando coordenadores de suprimentos...");
            }

            logger.error("📱 Enviando SMS de emergência para responsáveis...");
            logger.error("📧 Disparando emails de alta prioridade...");
            logger.error("🔔 Ativando sistema de notificações push...");

            logger.error("=== 🚨 ALERTA CRÍTICO PROCESSADO 🚨 ===");

        } catch (Exception e) {
            logger.error("Erro ao processar alerta crítico: {}", e.getMessage(), e);
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
            Thread.sleep(1000); // Simula 1 segundo de processamento
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    private void processarNotificacao(String tipo, String mensagem) {
        logger.info("Processando notificação do tipo: {}", tipo);
        logger.info("Mensagem: {}", mensagem);
    }
}