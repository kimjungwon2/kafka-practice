package com.example.springkafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class KafkaConsumer {

    private static final String TOPIC_NAME = "topic5";

    ObjectMapper objectMapper = new ObjectMapper();

    @KafkaListener(topics = TOPIC_NAME)
    public void listenMessage(String jsonMessage){
        try{
            MyMessage message = objectMapper.readValue(jsonMessage,MyMessage.class);
            System.out.println(">>"+message.getMessage()+","+message.getMessage());
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
