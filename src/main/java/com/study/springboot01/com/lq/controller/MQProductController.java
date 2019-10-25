//package com.study.springboot01.com.lq.controller;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.jms.core.JmsMessagingTemplate;
//import org.springframework.scheduling.annotation.Scheduled;
//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.ResponseBody;
//
//import javax.jms.Destination;
//import javax.jms.Queue;
//import javax.jms.Topic;
//
///**
// * About:
// * Other:
// * Created: qiang luo
// * Date: 2019-09-29 17:05
// * Editored:
// */
//@Controller
//@RequestMapping(value = "mqp")
//public class MQProductController {
//    @Autowired
//    private JmsMessagingTemplate jmsMessagingTemplate;
//
//    @Autowired
//    private Queue queue;
//
//    @Autowired
//    private Topic topic;
//
//    @GetMapping("/queue/send")
//    @ResponseBody
//    public String sendQueue(String str) {
//        this.sendMessage(queue, str);
//        return "success";
//    }
//
//    @GetMapping("/topic/send")
//    public String sendTopic(String str) {
//        this.sendMessage(topic, str);
//        return "success";
//    }
//
//    // 发送消息，destination是发送到的队列，message是待发送的消息
//    private void sendMessage(Destination destination, final String message){
//        jmsMessagingTemplate.convertAndSend(destination, message);
//    }
//
//    @Scheduled(cron = "*/30 * * * * ?")
//    public void send() {
//        jmsMessagingTemplate.convertAndSend(queue, "测试消息队列" + System.currentTimeMillis());
//        System.out.println("生产消息完成");
//    }
//}
