package org.formation.labbe.zoo.zoofront.bus;

import java.util.concurrent.TimeUnit;

import org.formation.labbe.zoo.zoofront.ZooFrontApplication;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class Runner {

    private final RabbitTemplate rabbitTemplate;
    private final Receiver receiver;

    public Runner(Receiver receiver, RabbitTemplate rabbitTemplate) {
        this.receiver = receiver;
        this.rabbitTemplate = rabbitTemplate;
    }

    public void Send(String object){
        rabbitTemplate.convertAndSend(ZooFrontApplication.topicExchangeName, "zoo-front", object);
    }


}