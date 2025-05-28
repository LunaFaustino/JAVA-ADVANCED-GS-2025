package br.com.fiap.abrigue.config;

import com.rabbitmq.client.ConnectionFactory;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableRabbit
public class RabbitMQConfig {

    public static final String PESSOA_CADASTRADA_QUEUE = "pessoa.cadastrada.queue";
    public static final String RECURSO_ESTOQUE_BAIXO_QUEUE = "recurso.estoque.baixo.queue";

    @Bean
    public Queue pessoaCadastradaQueue() {
        return new Queue(PESSOA_CADASTRADA_QUEUE, true); // true = durável
    }

    @Bean
    public Queue recursoEstoqueBaixoQueue() {
        return new Queue(RECURSO_ESTOQUE_BAIXO_QUEUE, true);
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
