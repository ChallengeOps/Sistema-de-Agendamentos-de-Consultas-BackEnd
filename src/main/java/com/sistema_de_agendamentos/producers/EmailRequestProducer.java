package com.sistema_de_agendamentos.producers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EmailRequestProducer {

    @Autowired
    private AmqpTemplate amqpTemplate;

    private final ObjectMapper objectMapper = new ObjectMapper();

    public void sendEmailRequest(Object emailRequest) {
        try {
            String message = objectMapper.writeValueAsString(emailRequest);
            amqpTemplate.convertAndSend(
                    "email-request-queue",
                    "email-request-rout-key", message);
        } catch (Exception e) {
            System.err.println("Error sending email request: " + e.getMessage());
        }
    }
}
