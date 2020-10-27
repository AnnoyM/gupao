package com.annoy.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Annoy
 */
@Configuration
@PropertySource("classpath:application.yml")
public class RabbitMqConfig {

    @Value("${annoy.rabbitmq.directExchange}")
    private String directExchange;

    @Value("${annoy.rabbitmq.topicExchange}")
    private String topicExchange;

    @Value("${annoy.rabbitmq.fanoutExchange}")
    private String fanoutExchange;

    @Value("${annoy.rabbitmq.delayedExchange}")
    private String delayedExchange;

    @Value("${annoy.rabbitmq.directQueue}")
    private String directQueue;

    @Value("${annoy.rabbitmq.topicQueue}")
    private String topicQueue;

    @Value("${annoy.rabbitmq.fanoutQueue1}")
    private String fanoutQueue1;

    @Value("${annoy.rabbitmq.fanoutQueue2}")
    private String fanoutQueue2;

    @Value("${annoy.rabbitmq.delayedQueue}")
    private String delayedQueue;
    /**
     * 定义一个直连交换机
     * @return
     */
    @Bean("directExchange")
    public DirectExchange directExchange(){
        return new DirectExchange(directExchange);
    }

    /**
     * 定义一个主题交换机
     * @return
     */
    @Bean("topicExchange")
    public TopicExchange topicExchange(){
        return new TopicExchange(topicExchange);
    }

    /**
     * 定义一个广播交换机
     * @return
     */
    @Bean("fanoutExchange")
    public FanoutExchange fanoutExchange(){
        return new FanoutExchange(fanoutExchange);
    }

    /**
     * 定义一个延时交换机
     * @return
     */
    @Bean("delayedExchange")
    public CustomExchange delayedExchange(){
        Map<String, Object> args = new HashMap<String, Object>();
        args.put("x-delayed-type", "direct");
        return new CustomExchange(delayedExchange,"x-delayed-message",true, false,args);
    }

    /**
     * 定义队列
     * @return
     */
    @Bean("directQueue")
    public Queue directQueue(){
        return new Queue(directQueue);
    }

    @Bean("topicQueue")
    public Queue topicQueue(){
        return new Queue(topicQueue);
    }

    @Bean("fanoutQueue1")
    public Queue fanoutQueue1(){
        return new Queue(fanoutQueue1);
    }

    @Bean("fanoutQueue2")
    public Queue fanoutQueue2(){
        return new Queue(fanoutQueue2);
    }

    @Bean("delayedQueue")
    public Queue delayedQueue(){
        return new Queue(delayedQueue);
    }

    @Bean
    public Binding bingDirectQueue(@Qualifier("directQueue") Queue queue, @Qualifier("directExchange") DirectExchange exchange){
        return BindingBuilder.bind(queue).to(directExchange()).with("annoy.ok");
    }


    @Bean
    public Binding bingTopicQueue(@Qualifier("topicQueue") Queue queue, @Qualifier("topicExchange") TopicExchange topicExchange ){
        return BindingBuilder.bind(queue).to(topicExchange).with("annoy1.*");
    }

    @Bean
    public Binding bingFanoutQueue1(@Qualifier("fanoutQueue1") Queue queue, @Qualifier("fanoutExchange") FanoutExchange fanoutExchange){
        return BindingBuilder.bind(queue).to(fanoutExchange);
    }

    @Bean
    public Binding bingFanoutQueue2(@Qualifier("fanoutQueue2") Queue queue, @Qualifier("fanoutExchange") FanoutExchange fanoutExchange){
        return BindingBuilder.bind(queue).to(fanoutExchange);
    }

    @Bean
    public Binding bingDelayedQueue(@Qualifier("delayedQueue") Queue queue, @Qualifier("delayedExchange") CustomExchange customExchange){
        return BindingBuilder.bind(queue).to(customExchange).with("annoy.delayed").noargs();
    }

    /**
     * 获取自定义的template,方便在rabbitmq、rocketmq、kafak 之间切换
     * @return
     */
    @Bean
    public AmqpTemplate annoyTemplate(final ConnectionFactory connectionFactory){
        final RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        return rabbitTemplate;
    }
}
