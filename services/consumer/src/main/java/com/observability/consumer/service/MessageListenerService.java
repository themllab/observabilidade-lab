package com.observability.consumer.service;

import io.micrometer.core.instrument.MeterRegistry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import io.micrometer.core.instrument.Counter;

@Slf4j
@Service
public class MessageListenerService {

    private final Counter messagesConsumedCounter;

    public MessageListenerService(MeterRegistry registry) {
        this.messagesConsumedCounter = Counter.builder("consumer_messages_total")
                .description("Número total de mensagens consumidas")
                .register(registry);
    }

    @KafkaListener(topics = "observability_topic", groupId = "observability-group")
    public void consume(String message) {
        log.info("✅ Mensagem recebida do Kafka: {}", message);
        messagesConsumedCounter.increment();

        // Simula processamento (ex: salvar no banco, chamar outro serviço, etc)
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        log.info("📦 Processamento finalizado para mensagem: {}", message);
    }
}
