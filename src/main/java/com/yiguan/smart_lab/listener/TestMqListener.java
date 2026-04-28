/*package com.yiguan.smart_lab.listener;

import com.yiguan.smart_lab.config.RabbitMqConfig;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class TestMqListener {

    @RabbitListener(queues = RabbitMqConfig.TEST_QUEUE)
    public void receive(String message){
        System.out.println("收到RabbitMQ消息：" + message);
    }
}*/
