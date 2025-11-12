package com.observability.producer.service;

import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import io.micrometer.core.instrument.Counter;

@Service
public class KafkaProducerService {

    private static final String TOPIC = "observability_topic";
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final Counter messagesProducedCounter;

    public KafkaProducerService(KafkaTemplate<String, String> kafkaTemplate, MeterRegistry registry) {
        this.kafkaTemplate = kafkaTemplate;

        // Define a métrica customizada
        this.messagesProducedCounter = Counter.builder("producer_messages_total")
                .description("Número total de mensagens produzidas")
                .register(registry);
    }

    public void sendMessage(String message) {
        kafkaTemplate.send(TOPIC, message);
        messagesProducedCounter.increment(); // Incrementa o contador a cada envio
        System.out.println("✅ Mensagem enviada para o Kafka: " + message);
    }
}
