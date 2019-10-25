//package com.study.springboot01.com.lq.controller;
//
//import com.study.springboot01.com.lq.config.RabbitConfig;
//import com.sun.xml.internal.ws.developer.Serialization;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.amqp.core.Message;
//import org.springframework.amqp.rabbit.core.RabbitTemplate;
//import org.springframework.amqp.rabbit.support.CorrelationData;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.ResponseBody;
//
//import javax.annotation.PostConstruct;
//import java.util.HashMap;
//import java.util.Map;
//import java.util.UUID;
//
///**
// * About:
// * Other:
// * Created: qiang luo
// * Date: 2019-10-11 15:59
// * Editored:
// */
//@Controller
//@Slf4j
//@RequestMapping(value = "rab-pd")
////ConfirmCallback接口用于实现消息发送到RabbitMQ交换器后接收ack回调   即消息发送到exchange  ack
////ReturnCallback接口用于实现消息发送到RabbitMQ 交换器，但无相应队列与交换器绑定时的回调 ,即消息发送不到任何一个队列中  ack
//public class RabbbitProduce implements RabbitTemplate.ConfirmCallback, RabbitTemplate.ReturnCallback {
//    @Autowired
//    private RabbitTemplate rabbitTemplate;
//
//    // 标注了@PostConstruct注释的方法将在类实例化之后调用
//    // 标注了@PreDestroy注释的方法将在类销毁之前调用
//    @PostConstruct
//    public void init() {
//        rabbitTemplate.setConfirmCallback(this);
//        rabbitTemplate.setReturnCallback(this);
//    }
//    //ConfirmCallback接口的回调(消息成功到达交换器回调)
//    @Override
//    public void confirm(CorrelationData correlationData, boolean ack, String cause) {
//        if (ack) {
//            System.err.println("消息到达交换器:成功:" + correlationData);
//        } else {
//            System.err.println("消息到达交换器:失败:" + cause);
//        }
//        System.out.println("-----------------------------------------------------------------");
//    }
//    //ReturnCallback接口的回调
//    @Override
//    public void returnedMessage(Message message, int replyCode, String replyText, String exchange, String routingKey) {
//        String correlationId = message.getMessageProperties().getCorrelationIdString();
//        log.info("消息未成功到达队列。。。");
//        log.info("消息：{} 发送失败, 应答码：{} 原因：{} 交换机: {}  路由键: {}", message.getBody(), replyCode, replyText, exchange, routingKey);
//    }
//
//    @RequestMapping(value = "send")
//    @ResponseBody
//    public String sendMsg(String content){
//        rabbitTemplate.setMandatory(true);
//        CorrelationData correlationId = new CorrelationData(UUID.randomUUID().toString());
//        //把消息放入ROUTINGKEY_A对应的队列当中去，对应的是队列A
//        rabbitTemplate.convertAndSend(RabbitConfig.EXCHANGE_B, RabbitConfig.ROUTINGKEY_B, content, correlationId);
//        return content;
//    }
//}
