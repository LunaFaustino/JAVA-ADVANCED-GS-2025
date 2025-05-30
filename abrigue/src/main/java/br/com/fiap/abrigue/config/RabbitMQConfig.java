package br.com.fiap.abrigue.config;

import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@EnableRabbit
@Profile("!test")
public class RabbitMQConfig {

    public static final String ABRIGO_CAPACIDADE_BAIXA_QUEUE = "abrigo.capacidade.baixa.queue";
    public static final String RECURSO_ESTOQUE_BAIXO_QUEUE = "recurso.estoque.baixo.queue";
    public static final String ALERTAS_CRITICOS_QUEUE = "alertas.criticos.queue";

    @Bean
    public Queue abrigoCapacidadeBaixaQueue() {
        return new Queue(ABRIGO_CAPACIDADE_BAIXA_QUEUE, true); // true = durável
    }

    @Bean
    public Queue recursoEstoqueBaixoQueue() {
        return new Queue(RECURSO_ESTOQUE_BAIXO_QUEUE, true);
    }

    @Bean
    public Queue alertasCriticosQueue() {
        return new Queue(ALERTAS_CRITICOS_QUEUE, true);
    }

    @Bean
    public Jackson2JsonMessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(messageConverter());
        return template;
    }
}
