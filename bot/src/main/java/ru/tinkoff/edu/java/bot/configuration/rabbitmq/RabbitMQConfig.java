package ru.tinkoff.edu.java.bot.configuration.rabbitmq;

import java.util.HashMap;
import java.util.Map;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.support.converter.ClassMapper;
import org.springframework.amqp.support.converter.DefaultClassMapper;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import ru.tinkoff.edu.java.bot.configuration.ApplicationConfig;
import ru.tinkoff.edu.java.bot.dto.request.LinkUpdateRequest;

@Configuration
@ConditionalOnProperty(prefix = "scrapper", name = "use-queue", havingValue = "true")
public class RabbitMQConfig {
    private static final String DLQ = ".dlq";
    private static final String DLX = ".dlx";

    private final String exchange;
    private final String queue;
    private final String routingKey;

    public RabbitMQConfig(ApplicationConfig config) {
        this.exchange = config.rabbitMQ().exchange();
        this.queue = config.rabbitMQ().queue();
        this.routingKey = config.rabbitMQ().routingKey();
    }

    @Bean
    public ClassMapper classMapper() {
        Map<String, Class<?>> mappings = new HashMap<>();
        mappings.put("ru.tinkoff.edu.java.scrapper.dto.request.LinkUpdateRequest", LinkUpdateRequest.class);

        DefaultClassMapper classMapper = new DefaultClassMapper();
        classMapper.setTrustedPackages("ru.tinkoff.edu.java.scrapper.service.dto.request.*");
        classMapper.setIdClassMapping(mappings);
        return classMapper;
    }

    @Bean
    public MessageConverter jsonMessageConverter(ClassMapper classMapper) {
        Jackson2JsonMessageConverter jsonConverter = new Jackson2JsonMessageConverter();
        jsonConverter.setClassMapper(classMapper);
        return jsonConverter;
    }

    @Bean
    public DirectExchange deadLetterExchange() {
        return new DirectExchange(exchange + DLX, true, false);
    }

    @Bean
    public Queue deadLetterQueue() {
        return new Queue(queue + DLQ, true, false, false);
    }

    @Bean
    public Binding deadLetterBinding(Queue queue, DirectExchange exchange) {
        return BindingBuilder
            .bind(queue)
            .to(exchange)
            .with(routingKey + DLQ);
    }
}
