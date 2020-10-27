package com.annoy.consumer;

import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

/**
 * @ClassName : MyConsumer
 * @Author : annoy
 * @Date: 2020/10/27 19:04
 * @Description : 消费者
 */
@Component
@PropertySource("classpath:application.yml")
public class MyConsumer {

    @RabbitHandler
    @RabbitListener(queues="${annoy.rabbitmq.directQueue}", containerFactory = "rabbitListenerContainerFactory")
    public void directConsumer(String msg){
        System.out.println("direct consumer msg is :" + msg);
    }

    @RabbitHandler
    @RabbitListener(queues="${annoy.rabbitmq.topicQueue}", containerFactory = "rabbitListenerContainerFactory")
    public void topicConsumer(String msg){
        System.out.println("topic consumer msg is :" + msg);
    }

    @RabbitHandler
    @RabbitListener(queues="${annoy.rabbitmq.fanoutQueue1}", containerFactory = "rabbitListenerContainerFactory")
    public void fanoutConsumer1(String msg){
        System.out.println("fanout consumer 1 msg is :" + msg);
    }

    @RabbitHandler
    @RabbitListener(queues="${annoy.rabbitmq.fanoutQueue2}", containerFactory = "rabbitListenerContainerFactory")
    public void fanoutConsumer2(String msg){
        System.out.println("fanout consumer 2 msg is :" + msg);
    }

    @RabbitHandler
    @RabbitListener(queues="${annoy.rabbitmq.delayedQueue}", containerFactory = "rabbitListenerContainerFactory")
    public void delayedConsumer(String msg){
        System.out.println("delayed consumer msg is :" + msg);
    }
}
