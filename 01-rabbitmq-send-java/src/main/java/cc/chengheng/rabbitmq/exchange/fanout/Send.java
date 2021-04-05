package cc.chengheng.rabbitmq.exchange.fanout;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeoutException;

public class Send {
    public static void main(String[] args) {
        /**
         * 创建连接工厂
         */
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("192.168.1.108");   // ip
        factory.setPort(5672);              // 端口
        factory.setUsername("admin");        // 账号
        factory.setPassword("admin");        // 密码

        Connection connection = null;       // 定义连接
        Channel channel = null;             // 定义通道

        try {
            connection = factory.newConnection();
            channel = connection.createChannel();


            /*---------------------------------------------------------------
             * 由于使用fanout类型的交换机，也就是广播，因此接收方肯定会有多个因此，不建议在
             * 消息发送时来创建队列，绑定交换机，建议在消费者也就是接收方创建
             * 发送消息时候要确定交换机存在
            // 队列声明
            channel.queueDeclare("myDirectQueue", true, false, false, null);

            // 交换机声明
            channel.exchangeDeclare("directExchange", "direct", true);

            // 绑定交换机
            channel.queueBind("myDirectQueue", "directExchange", "directRoutingKey");
            ---------------------------------------------------------------*/


            String message = "fanout的消息测试";
            channel.basicPublish("fanoutExchange", "", null, message.getBytes(StandardCharsets.UTF_8));

            System.out.println("消息发送成功");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        } finally {
            if(channel != null) {
                try {
                    channel.close();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (TimeoutException e) {
                    e.printStackTrace();
                }
            }
            if (connection != null) {
                try {
                    connection.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
