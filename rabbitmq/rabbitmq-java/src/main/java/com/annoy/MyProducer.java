package com.annoy;

import com.annoy.util.RabbitMqUtil;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Annoy
 */
public class MyProducer {

    public static void main(String[] args) throws Exception{
        ConnectionFactory factory = new ConnectionFactory();

        factory.setHost(RabbitMqUtil.RabbitMqHost);
        factory.setPort(RabbitMqUtil.RabbitMqPort);
        factory.setVirtualHost(RabbitMqUtil.RabbitMqVirtualHost);
        factory.setUsername(RabbitMqUtil.RabbitMqUser);
        factory.setPassword(RabbitMqUtil.RabbitMqPwd);

        //创建连接
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        //发送直连交换机消息
        String directMsg = "Hello, Annoy ! this is direct msg";
        channel.basicPublish(RabbitMqUtil.ExchangeName.DIRECT_EXCHANGE, "annoy.ok", null, directMsg.getBytes());

        //发送主题交换机消息
        String topMsg = "Hello, Annoy ! this is top msg";
        channel.basicPublish(RabbitMqUtil.ExchangeName.TOPIC_EXCHANGE, "annoy1.ok", null, topMsg.getBytes());


        //发送广播交换机消息
        String fanoutMsg = "Hello, Annoy ! this is fanout msg";
        channel.basicPublish(RabbitMqUtil.ExchangeName.FANOUT_EXCHANGE, "", null, fanoutMsg.getBytes());


        //发送延时消息
        Map<String, Object> headers = new HashMap<String, Object>();
        headers.put("x-delay", 100000);

        AMQP.BasicProperties.Builder props = new AMQP.BasicProperties.Builder()
                .headers(headers);
        SimpleDateFormat sf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        String delayedMsg = "Hello, Annoy ! this is delayed msg, now time is:" + sf.format(new Date());
        channel.basicPublish(RabbitMqUtil.ExchangeName.DELAYED_EXCHANGE, "annoy.delayed", props.build(), delayedMsg.getBytes());

        channel.close();
        connection.close();
    }
}
