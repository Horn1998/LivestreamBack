package com.horn.energy_blockchain.Utils;/*
 *@Author: horn
 *@DATE: 2022/10/31 0031 19:08
 *@Description
 *@Version 1.0
 */

import com.alibaba.fastjson.JSON;
import com.horn.energy_blockchain.Entity.Message;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public class MQHelper {

    //队列名称
    private static final String QUEUE = "DeviceGroup";
    //消息 参数
    private static final String HOST = "101.34.3.60";
    private static final Integer PORT = 5672;
    private static final String USERNAME = "horn1998";
    private static final String PASSWORD = "wzh199810";
    private static String VIRTUALHOST = "/";//虚拟机

    //发送消息
    public static void sendMesssage(String message) throws Exception {
        Connection connection = null;
        Channel channel = null;
        try {
            ConnectionFactory factory = new ConnectionFactory();
            //初始化
            factory.setHost(HOST);
            factory.setPort(PORT);
            factory.setUsername(USERNAME);
            factory.setPassword(PASSWORD);

            //rabbitmq默认虚拟机名称为“/”，虚拟机相当于一个独立的mq服务器
            factory.setVirtualHost(VIRTUALHOST);

            //创建与RabbitMQ服务的TCP连接
            connection  = factory.newConnection();
            //创建与Exchange的通道，每个连接可以创建多个通道，每个通道代表一个会话任务
            channel = connection.createChannel();

            Message msg = JSON.parseObject(message, Message.class);
            /**
             * 声明队列，如果Rabbit中没有此队列将自动创建
             * param1:队列名称
             * param2:是否持久化
             * param3:队列是否独占此连接
             * param4:队列不再使用时是否自动删除此队列
             * param5:队列参数
             */
            channel.queueDeclare(msg.getRoom(), true, false, false, null);

            /**
             * 消息发布方法
             * param1：Exchange的名称，如果没有指定，则使用Default Exchange
             * param2:routingKey,消息的路由Key，是用于Exchange（交换机）将消息转发到指定的消息队列
             * param3:消息包含的属性
             * param4：消息体
             */
            /**
             * 这里没有指定交换机，消息将发送给默认交换机，每个队列也会绑定那个默认的交换机，但是不能显示绑定或解除绑定
             *　默认的交换机，routingKey等于队列名称
             */
            channel.basicPublish("", msg.getRoom(), null, message.getBytes());

        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            if(channel != null)
            {
                channel.close();
            }
            if(connection != null)
            {
                connection.close();
            }
        }
    }
}
