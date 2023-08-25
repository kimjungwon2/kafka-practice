package com.example.springkafka;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class KafkaProduceController {

    @Autowired
    private KafkaProduceService kafkaProduceService;

    @RequestMapping("/publish")
    public String publish(String message){
        kafkaProduceService.send(message);

        return "published a message :"+message;
    }

    @RequestMapping("/publish2")
    public String publishWithCallback(String message){
        kafkaProduceService.sendWithCallback(message);

        return "published a message with callback :"+message;
    }
}
