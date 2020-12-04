package com.example.springbootdemorabbitmq.config;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Administrator
 * @description
 * @date 2020/12/1
 */
@Configuration
public class RabbitMq {

    public static final String EXCHANGE_NAME = "test_exchange";
    private static final String QUEUE_NAME = "test_queue";

    public static final String TTL_QUEUE = "ttl_queue";
    public static final String TTL_EXCHANGE = "ttl_exchange";


    @Bean("topicExchange")
    public Exchange topicExchange(){
        return ExchangeBuilder.topicExchange(EXCHANGE_NAME).durable(true).build();
    }

    @Bean("topicQueue")
    public Queue topicQueue(){
        return QueueBuilder.durable(QUEUE_NAME).build();
    }

    @Bean
    public Binding binding(@Qualifier("topicQueue") Queue queue, @Qualifier("topicExchange") Exchange exchange){
        return BindingBuilder.bind(queue).to(exchange).with("test.#").noargs();
    }


    /**
     * 过期队列 也可以用下面的方法设置过期时间
     * MessageProperties messageProperties = new MessageProperties();
    *  messageProperties.setExpiration("20000"); // 设置过期时间，单位：毫秒
    *  byte[] msgBytes = "测试消息自动过期".getBytes();
    *  Message message = new Message(msgBytes, messageProperties);
    *  rabbitTemplate.convertAndSend("TTL_EXCHANGE", "TTL", message);
     */
    @Bean("ttlQueue")
    public Queue ttlQueue() {
        Map<String, Object> map = new HashMap<>(16);
        // 队列中的消息未被消费则10秒后过期
        map.put("x-message-ttl", 30000);
        return new Queue(TTL_QUEUE, true,false,false, map);
    }

    @Bean("ttlExchange")
    public DirectExchange ttlExchange() {
        return new DirectExchange(TTL_EXCHANGE, true, false);
    }

    @Bean
    public Binding ttlBinding(@Qualifier("ttlQueue") Queue ttlQueue, @Qualifier("ttlExchange") DirectExchange ttlExchange){
        return BindingBuilder.bind(ttlQueue).to(ttlExchange).with("direct");
    }
}
