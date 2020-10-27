package com.annoy;

import com.annoy.util.RabbitMqUtil;
import com.rabbitmq.client.*;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Annoy
 */
public class MyConsumer {

    public static void main(String[] args) throws Exception{
        //创建连接
        ConnectionFactory factory = new ConnectionFactory();

        factory.setHost(RabbitMqUtil.RabbitMqHost);
        factory.setPort(RabbitMqUtil.RabbitMqPort);
        factory.setVirtualHost(RabbitMqUtil.RabbitMqVirtualHost);
        factory.setUsername(RabbitMqUtil.RabbitMqUser);
        factory.setPassword(RabbitMqUtil.RabbitMqPwd);

        //建立连接
        Connection connection = factory.newConnection();
        //创建一个通道
        Channel channel = connection.createChannel();

        //声明一个直连交换机
        channel.exchangeDeclare(RabbitMqUtil.ExchangeName.DIRECT_EXCHANGE, "direct", false, false, null);
        //声明一个队列
        channel.queueDeclare(RabbitMqUtil.QueueName.DIRECT_QUEUE, false, false, false, null);

        //绑定交换机与队列，设置路由关键字
        channel.queueBind(RabbitMqUtil.QueueName.DIRECT_QUEUE, RabbitMqUtil.ExchangeName.DIRECT_EXCHANGE, "annoy.ok");

        //创建消费者
        Consumer consumer = new DefaultConsumer(channel){

            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                String msg = new String(body, "UTF-8");

                System.out.println("################# direct 直连交换机消息 ################");

                System.out.println("接收到的消息:" + msg);
                System.out.println("接收到的consumerTag:" + consumerTag);
                System.out.println("接收到的envelope:" + envelope);

            }
        };



        //开始获取消息
        channel.basicConsume(RabbitMqUtil.QueueName.DIRECT_QUEUE, true, consumer);


        //声明一个主题交换机
        channel.exchangeDeclare(RabbitMqUtil.ExchangeName.TOPIC_EXCHANGE, "topic", false, false, null);
        //声明一个队列
        channel.queueDeclare(RabbitMqUtil.QueueName.TOPIC_QUEUE, false, false, false, null);

        //绑定交换机与队列，设置路由关键字
        channel.queueBind(RabbitMqUtil.QueueName.TOPIC_QUEUE, RabbitMqUtil.ExchangeName.TOPIC_EXCHANGE, "annoy1.*");

        //创建消费者
        Consumer topicConsumer = new DefaultConsumer(channel){

            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                String msg = new String(body, "UTF-8");

                System.out.println("################# topic 主题交换机消息 ################");
                System.out.println("接收到的top消息:" + msg);
                System.out.println("接收到的consumerTag:" + consumerTag);
                System.out.println("接收到的envelope:" + envelope);

            }
        };

        //开始获取消息
        channel.basicConsume(RabbitMqUtil.QueueName.TOPIC_QUEUE, true, topicConsumer);


        //声明一个广播交换机
        channel.exchangeDeclare(RabbitMqUtil.ExchangeName.FANOUT_EXCHANGE, "fanout", false, false, null);
        //声明一个队列
        channel.queueDeclare(RabbitMqUtil.QueueName.FANOUT_QUEUE, false, false, false, null);
        //绑定交换机与队列，设置路由关键字
        channel.queueBind(RabbitMqUtil.QueueName.FANOUT_QUEUE, RabbitMqUtil.ExchangeName.FANOUT_EXCHANGE, "");

        //创建消费者
        Consumer fanoutConsumer1 = new DefaultConsumer(channel){

            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                String msg = new String(body, "UTF-8");

                System.out.println("################# fanout1 广播交换机消息 ################");
                System.out.println("接收到的top消息:" + msg);
                System.out.println("接收到的consumerTag:" + consumerTag);
                System.out.println("接收到的envelope:" + envelope);

            }
        };

        //开始获取消息
        channel.basicConsume(RabbitMqUtil.QueueName.FANOUT_QUEUE, true, fanoutConsumer1);

        //声明一个队列
        channel.queueDeclare(RabbitMqUtil.QueueName.FANOUT_QUEUE + "2", false, false, false, null);
        //绑定交换机与队列，设置路由关键字
        channel.queueBind(RabbitMqUtil.QueueName.FANOUT_QUEUE + "2", RabbitMqUtil.ExchangeName.FANOUT_EXCHANGE, "");

        //创建消费者
        Consumer fanoutConsumer2 = new DefaultConsumer(channel){

            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                String msg = new String(body, "UTF-8");

                System.out.println("################# fanout2 广播交换机消息 ################");
                System.out.println("接收到的top消息:" + msg);
                System.out.println("接收到的consumerTag:" + consumerTag);
                System.out.println("接收到的envelope:" + envelope);

            }
        };

        //开始获取消息
        channel.basicConsume(RabbitMqUtil.QueueName.FANOUT_QUEUE + "2", true, fanoutConsumer2);



        //声明一个广播交换机
        // 声明x-delayed-message类型的exchange
        Map<String, Object> argss = new HashMap<String, Object>();
        argss.put("x-delayed-type", "direct");
        channel.exchangeDeclare(RabbitMqUtil.ExchangeName.DELAYED_EXCHANGE, "x-delayed-message", false, false, argss);
        channel.queueDeclare(RabbitMqUtil.QueueName.DELAYED_QUEUE, false, false, false, null);
        //绑定交换机与队列，设置路由关键字
        channel.queueBind(RabbitMqUtil.QueueName.DELAYED_QUEUE , RabbitMqUtil.ExchangeName.DELAYED_EXCHANGE, "annoy.delayed");

        //创建消费者
        Consumer delayedConsumer = new DefaultConsumer(channel){
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                String msg = new String(body, "UTF-8");

                System.out.println("################# delayed 延时交换机消息 ################");
                SimpleDateFormat sf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
                System.out.println("接收到的delayed消息:" + msg + "; 当前时间:" + sf.format(new Date()));
                System.out.println("接收到的consumerTag:" + consumerTag);
                System.out.println("接收到的envelope:" + envelope);

            }
        };

        //开始获取消息
        channel.basicConsume(RabbitMqUtil.QueueName.DELAYED_QUEUE, true, delayedConsumer);
    }
}
