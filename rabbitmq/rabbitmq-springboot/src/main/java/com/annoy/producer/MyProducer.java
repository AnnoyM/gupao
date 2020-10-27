package com.annoy.producer;

import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * @ClassName : MyProducer
 * @Author : annoy
 * @Date: 2020/10/27 19:11
 * @Description : 生产者
 */
@Component
@PropertySource("classpath:application.yml")
public class MyProducer {

    @Value("${annoy.rabbitmq.directExchange}")
    private String directExchange;

    @Value("${annoy.rabbitmq.topicExchange}")
    private String topicExchange;

    @Value("${annoy.rabbitmq.fanoutExchange}")
    private String fanoutExchange;

    @Value("${annoy.rabbitmq.delayedExchange}")
    private String delayedExchange;


    @Autowired
    AmqpTemplate annoyTemplate;

    @PostConstruct
    public void send(){
        try{
            annoyTemplate.convertAndSend(directExchange, "annoy.ok", "this is direct msg");
            annoyTemplate.convertAndSend(topicExchange, "annoy1.*", "this is topic msg");
            annoyTemplate.convertAndSend(fanoutExchange, "", "this is fanout msg 1");
            annoyTemplate.convertAndSend(fanoutExchange, "", "this is fanout msg 2");

            //发送延时消息

            annoyTemplate.convertAndSend(delayedExchange, "annoy.delayed", "this is delayed msg", new MessagePostProcessor() {
                public Message postProcessMessage(Message message) throws AmqpException {
                    message.getMessageProperties().setHeader("x-delay", 1000*60);
                    return message;
                }
            });

        } catch (Exception e ){
            System.out.printf(e.getMessage(), e);
        }
    }
}
