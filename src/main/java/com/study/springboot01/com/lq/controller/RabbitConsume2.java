package com.study.springboot01.com.lq.controller;

import com.rabbitmq.client.Channel;
import com.study.springboot01.com.lq.config.RabbitConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * About:
 * Other:
 * Created: qiang luo
 * Date: 2019-10-12 17:15
 * Editored:
 */
@Component
@Slf4j
@RabbitListener(queues = RabbitConfig.QUEUE_B)//CalonDirectQueue为队列名称
public class RabbitConsume2 {

    @RabbitHandler
    public void handler2(String content, Channel channel, Message message) throws IOException {
        //String text=new String(message.getBody());
        long deliveryTag = message.getMessageProperties().getDeliveryTag();
        System.out.println("消费者李："+content);
        //需要手动ack确认消息
        channel.basicAck(deliveryTag,false);
    }
}
