/*package com.yiguan.smart_lab.controller;

import com.yiguan.smart_lab.common.Result;
import com.yiguan.smart_lab.config.RabbitMqConfig;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MqTestController {

    private final RabbitTemplate rabbitTemplate;

    public MqTestController(RabbitTemplate rabbitTemplate){
        this.rabbitTemplate = rabbitTemplate;
    }

    @GetMapping("/mq/send")
    public Result<String> send(){
        rabbitTemplate.convertAndSend(RabbitMqConfig.TEST_QUEUE, "hello, rabbitmq");
        return Result.success("消息发送成功");
    }
}*/
