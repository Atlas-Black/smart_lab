package com.yiguan.smart_lab.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class RabbitMqConfig {

    //正常交换机
    public static final String BORROW_EXCHANGE = "borrwo.exchange";

    //正常队列（带TTL, 过期后转死信）
    public static final String BORROW_DELAY_QUEUE = "borrow.delay.queue";

    //死信交换机
    public static final String BORROW_DLX_EXCHANGE = "borrwo.dlx.exchange";

    //死信队列
    public static final String BORROW_DLX_QUEUE = "borrow.dlx.queue";

    //路由键
    public static final String BORROW_ROUTING_KEY = "borrow.delay";
    public static final String BORROW_DLX_ROUTING_KEY = "borrow.dlx";

    @Bean
    public DirectExchange borrowExchange(){
        return new DirectExchange(BORROW_EXCHANGE);
    }

    @Bean
    public DirectExchange borrowDlxExchange(){
        return new DirectExchange(BORROW_DLX_EXCHANGE);
    }

    @Bean
    public Queue borrowDelayQueue(){
        Map<String, Object> args = new HashMap<>();
        args.put("x-dead-letter-exchange", BORROW_DLX_EXCHANGE);
        args.put("x-dead-letter-routing-key", BORROW_DLX_ROUTING_KEY);

        return new Queue(BORROW_DELAY_QUEUE, true, false, false, args);
    }

    @Bean
    public Queue borrowDlxQueue(){
        return new Queue(BORROW_DLX_QUEUE, true);
    }
    @Bean
    public Binding borrowBinding(){
        return BindingBuilder.bind(borrowDelayQueue())
                .to(borrowExchange())
                .with(BORROW_ROUTING_KEY);
    }

    @Bean
    public Binding borrowDlxBinding(){
        return BindingBuilder.bind(borrowDlxQueue())
                .to(borrowDlxExchange())
                .with(BORROW_DLX_ROUTING_KEY);
    }

    @Bean
    public MessageConverter messageConverter(){
        return new Jackson2JsonMessageConverter();
    }
}
