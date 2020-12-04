package com.example.springbootdemorabbitmq.controller;

import com.example.springbootdemorabbitmq.config.RabbitMq;
import lombok.AllArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Administrator
 * @description
 * @date 2020/12/2
 */
@AllArgsConstructor
@RestController
public class TestController {


    RabbitTemplate rabbitTemplate;

    @RequestMapping("/send")
    public String sendMsg(@RequestParam String key, @RequestParam String msg){
        rabbitTemplate.convertAndSend(RabbitMq.EXCHANGE_NAME,key,msg);
        return "发送成功";
    }
    @RequestMapping("/send2")
    public String sendMsg2(@RequestParam String key, @RequestParam String msg){
        rabbitTemplate.convertAndSend(RabbitMq.TTL_EXCHANGE,key,msg);
        return "发送成功";
    }
}
