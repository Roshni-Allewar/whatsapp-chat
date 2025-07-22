package org.example.kafka;


import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;

@Service
public class MessageProducer {

    private final KafkaTemplate<String, String> kafkaTemplate;


    @Value( "${kafka.topic.chat}")
    private String topicName;

    public MessageProducer(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendMessage(String messagePayload) {
        kafkaTemplate.send(topicName, messagePayload);
    }
}