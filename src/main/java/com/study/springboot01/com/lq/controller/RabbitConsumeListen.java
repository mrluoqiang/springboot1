package com.study.springboot01.com.lq.controller;
import com.rabbitmq.client.Channel;
import com.study.springboot01.com.lq.config.RabbitConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.ChannelAwareMessageListener;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.Date;

/**
 * About:
 * Other:
 * Created: qiang luo
 * Date: 2019-10-11 16:07
 * Editored:
 */
//MessageConverter---消息内容转换的，可以自定义一个类去实现这个接口，完成自定义message对象转换成java对象
@Component
@Slf4j
//@RabbitListener(queues = RabbitConfig.QUEUE_B)//CalonDirectQueue为队列名称
//为了启用 手动 ack 模式，消费者需要实现 ChannelAwareMessageListener接口
public class RabbitConsumeListen implements ChannelAwareMessageListener {

    @Override
    public void onMessage(Message message, Channel channel) throws Exception {
        MessageProperties messageProperties = message.getMessageProperties();

        // 代表投递的标识符，唯一标识了当前信道上的投递，通过 deliveryTag ，消费者就可以告诉 RabbitMQ 确认收到了当前消息，见下面的方法
        long deliveryTag = messageProperties.getDeliveryTag();

        // 如果是重复投递的消息，redelivered 为 true
        Boolean redelivered = messageProperties.getRedelivered();

        // 获取生产者发送的原始消息
        //Object originalMessage = messageConverter.fromMessage(message);
        String text = new String(message.getBody());
        log.info("consume message = {} , deliveryTag = {} , redelivered = {}"
                , text, deliveryTag, redelivered);
        if(redelivered){
            System.out.println("NACK");
            channel.basicNack(message.getMessageProperties().getDeliveryTag(), false, false);
        }else{
            // 代表消费者确认收到当前消息，第二个参数表示一次是否 ack 多条消息
            channel.basicAck(deliveryTag, false);
        }
        // 代表消费者拒绝一条或者多条消息，第二个参数表示一次是否拒绝多条消息，第三个参数表示是否把当前消息重新入队
//        channel.basicNack(deliveryTag, false, false);

        // 代表消费者拒绝当前消息，第二个参数表示是否把当前消息重新入队
//        channel.basicReject(deliveryTag,false);
    }
}
