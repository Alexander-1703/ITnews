package ru.tinkoff.edu.java.scrapper.configuration.rabbitmq;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import ru.tinkoff.edu.java.scrapper.configuration.ApplicationConfig;

@Configuration
@ConditionalOnProperty(prefix = "scrapper", name = "use-queue", havingValue = "true")
public class RabbitMQConfig {
    private static final String DLQ = ".dlq";

    private final String exchange;
    private final String queue;
    private final String routingKey;

    public RabbitMQConfig(ApplicationConfig config) {
        this.exchange = config.rabbitMQ().exchange();
        this.queue = config.rabbitMQ().queue();
        this.routingKey = config.rabbitMQ().routingKey();
    }

    @Bean
    public DirectExchange directExchange() {
        return new DirectExchange(exchange, true, false);
    }

    @Bean
    public Queue queue() {
        return QueueBuilder
            .durable(queue)
            .withArgument("x-dead-letter-exchange", exchange + DLQ)
            .withArgument("x-dead-letter-routing-key", routingKey + DLQ)
            .build();
    }

    @Bean
    public Binding binding(Queue queue, DirectExchange exchange) {
        return BindingBuilder
            .bind(queue)
            .to(exchange)
            .with(routingKey);
    }

    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}
