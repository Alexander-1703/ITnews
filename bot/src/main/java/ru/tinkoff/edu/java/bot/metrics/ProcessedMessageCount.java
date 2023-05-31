package ru.tinkoff.edu.java.bot.metrics;

import org.springframework.stereotype.Component;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;

@Component
public class ProcessedMessageCount {
    private static final String PROCESSED_BOT_MESSAGE_METRIC = "processed_bot_message_count";

    private final Counter messageCounter;

    public ProcessedMessageCount(MeterRegistry registry) {
        this.messageCounter = Counter.builder(PROCESSED_BOT_MESSAGE_METRIC)
            .description("Number of messages processed by the bot")
            .register(registry);
    }

    public void increment() {
        messageCounter.increment();
    }
}
