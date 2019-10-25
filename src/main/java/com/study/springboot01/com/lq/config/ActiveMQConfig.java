//package com.study.springboot01.com.lq.config;
//
//import org.apache.activemq.ActiveMQConnectionFactory;
//import org.apache.activemq.command.ActiveMQQueue;
//import org.apache.activemq.command.ActiveMQTopic;
//import org.springframework.beans.factory.annotation.Configurable;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Bean;
//import org.springframework.jms.config.JmsListenerContainerFactory;
//import org.springframework.jms.config.SimpleJmsListenerContainerFactory;
//import org.springframework.jms.core.JmsMessagingTemplate;
//import org.springframework.stereotype.Component;
//
//import javax.jms.ConnectionFactory;
//import javax.jms.Queue;
//import javax.jms.Topic;
//
///**
// * About:
// * Other:
// * Created: qiang luo
// * Date: 2019-09-29 16:35
// * Editored:
// */
//@Configurable
//@Component
//public class ActiveMQConfig {
//
//    @Value("${spring.activemq.broker-url}")
//    private String brokerUrl;
//
//    @Value("${spring.activemq.user}")
//    private String username;
//
//    @Value("${spring.activemq.topic-name}")
//    private String password;
//
//    @Value("${spring.activemq.queue-name}")
//    private String queueName;
//
//    @Value("${spring.activemq.topic-name}")
//    private String topicName;
//
//    //创建一个activemq连接工厂
//    @Bean
//    public ConnectionFactory connectionFactory(){
//        return new ActiveMQConnectionFactory(username, password, brokerUrl);
//    }
//    //
//    @Bean
//    public JmsMessagingTemplate jmsMessageTemplate(){
//
//        return new JmsMessagingTemplate(connectionFactory());
//    }
//
//    // 在Queue模式中，对消息的监听需要对containerFactory进行配置
//    @Bean("queueListener")
//    public JmsListenerContainerFactory<?> queueJmsListenerContainerFactory(ConnectionFactory connectionFactory){
//        SimpleJmsListenerContainerFactory factory = new SimpleJmsListenerContainerFactory();
//        factory.setConnectionFactory(connectionFactory);
//        factory.setPubSubDomain(false);
//        return factory;
//    }
//
//    //在Topic模式中，对消息的监听需要对containerFactory进行配置
//    @Bean("topicListener")
//    public JmsListenerContainerFactory<?> topicJmsListenerContainerFactory(ConnectionFactory connectionFactory){
//        SimpleJmsListenerContainerFactory factory = new SimpleJmsListenerContainerFactory();
//        factory.setConnectionFactory(connectionFactory);
//        factory.setPubSubDomain(true);
//        return factory;
//    }
//    @Bean
//    public Queue queue() {
//        return new ActiveMQQueue(queueName);
//    }
//    @Bean
//    public Topic topic() {
//        return new ActiveMQTopic(topicName);
//    }
//}
