package com.horn.energy_blockchain.Utils;/*
 *@Author: horn
 *@DATE: 2022/10/31 0031 19:16
 *@Description
 *@Version 1.0
 */

import com.alibaba.fastjson.JSONObject;
import com.horn.energy_blockchain.Entity.Message;
import com.horn.energy_blockchain.Service.IMessageService;
import com.rabbitmq.client.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class RabbitmqServer {
    @Autowired
    IMessageService IMessageService;

    //队列名称
    private static final String QUEUE = "DeviceGroup";


    public void run(String QUEUE) throws Exception {
        ConnectionFactory factory = new ConnectionFactory();

        //设置mq所在的服务器的ip和端口
//        factory.setHost("10.128.206.152");
        factory.setHost("101.34.3.60");
        factory.setPort(5672);
        factory.setUsername("horn1998");
        factory.setPassword("wzh199810");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();
        //声明队列
        channel.queueDeclare(QUEUE, true, false, false, null);
        //定义消费方法
        DefaultConsumer consumer = new DefaultConsumer(channel){
            /**
             * 消费者接收消息调用此方法
             * @param consumerTag 消费者的标签，在channel.basicConsume()去指定
             * @param envelope 消息包的内容，可从中获取消息id，消息routingkey，交换机，消息和重传标志(收到消息失败后是否需要重新发送)
             * @param properties
             * @param body
             * @throws IOException
             */
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                //交换机
                String exchange = envelope.getExchange();
                //路由key
                String routingKey = envelope.getRoutingKey();
                //消息id
                long deliveryTag = envelope.getDeliveryTag();
                //消息内容
                String msg = new String(body, "utf-8");
                System.out.println("接收的消息为：" + msg+" 消息id为："+deliveryTag);
                add(msg);
            }
        };
        /**
         * 监听队列String queue, boolean autoAck,Consumer callback
         * 参数明细
         * 1、队列名称
         * 2、是否自动回复，设置为true为表示消息接收到自动向mq回复接收到了，mq接收到回复会删除消息，设置为false则需要手动回复
         * 3、消费消息的方法，消费者接收到消息后调用此方法
         */
        channel.basicConsume(QUEUE, true, consumer);
        //获取历史聊天记录

        //封装成json对象

        //返回已有历史数据
    }

    public void add(String body){
        try {
            //1. 构造message数据体
            Message message = JSONObject.parseObject(body, Message.class);
            //2. 将message写入数据库
            IMessageService.saveMessage(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
