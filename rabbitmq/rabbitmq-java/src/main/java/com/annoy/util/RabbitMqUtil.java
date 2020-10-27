package com.annoy.util;

/**
 * @author Annoy
 *
 * rabbitMq 工具类
 */
public class RabbitMqUtil {

    /**
     * rabbitmq ip
     */
    public static final String RabbitMqHost = "49.234.124.196";

    /**
     * rabbitmq 端口
     */
    public static final Integer RabbitMqPort = 5672;

    /**
     * rabbitmq 虚拟机
     */
    public static final String RabbitMqVirtualHost= "/";

    /**
     * rabbitmq 用户名
     */
    public static final String RabbitMqUser = "admin";

    /**
     * rabbitmq 密码
     */
    public static final String RabbitMqPwd = "admin";


    public static interface ExchangeName{
      String DIRECT_EXCHANGE = "DIRECT_EXCHANGE";           //直连交换机
      String TOPIC_EXCHANGE = "TOPIC_EXCHANGE";                 //主题交换机
      String FANOUT_EXCHANGE = "FANOUT_EXCHANGE";           //广播交换机
      String DELAYED_EXCHANGE = "DELAYED_EXCHANGE";         //延时交换机
    }

    public static interface QueueName{
        String DIRECT_QUEUE = "DIRECT_QUEUE";           //直连交换机绑定的队列
        String TOPIC_QUEUE = "TOPIC_QUEUE";                 //主题交换机绑定的队列
        String FANOUT_QUEUE = "FANOUT_QUEUE";           //广播交换机绑定的队列
        String DELAYED_QUEUE = "DELAYED_QUEUE";         //延时交换机绑定的队列
    }
}
