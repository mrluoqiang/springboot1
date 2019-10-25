package com.study.springboot01.com.lq.controller;

import com.study.springboot01.com.lq.config.RabbitConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.support.CorrelationData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.UUID;

/**
 * About:
 * Other:
 * Created: qiang luo
 * Date: 2019-10-12 16:21
 * Editored:
 */
@Controller
@Slf4j
@RequestMapping(value = "rab-pd")
public class RabbitProduce2 {
    @Autowired
    RabbitTemplate rabbitTemplate;

    @RequestMapping(value = "send")
    @ResponseBody
    public String sendMsg(String content){
        rabbitTemplate.setMandatory(true);
        CorrelationData correlationId = new CorrelationData(UUID.randomUUID().toString());
        //把消息放入ROUTINGKEY_A对应的队列当中去，对应的是队列A
        rabbitTemplate.convertAndSend(RabbitConfig.EXCHANGE_B, RabbitConfig.ROUTINGKEY_B, content, correlationId);
        return content;
    }
}
