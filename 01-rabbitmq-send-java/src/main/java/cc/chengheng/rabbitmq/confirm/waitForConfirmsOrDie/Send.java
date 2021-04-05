package cc.chengheng.rabbitmq.confirm.waitForConfirmsOrDie;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeoutException;

public class Send {
    public static void main(String[] args) throws IOException {
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
            connection = factory.newConnection(); // 建立连接
            channel = connection.createChannel(); // 建立通道

            // 队列声明，rabbitmq 创建队列
            channel.queueDeclare("confirmQueue", true, false, false, null);

            // 交换机声明
            channel.exchangeDeclare("directConfirmExchange", "direct", true);


            channel.queueBind("confirmQueue", "directConfirmExchange", "confirmRoutingKey");
            String message = "普通发送者确认模式测试消息！";

            // 启动发送者确认模式， 如果抛InterruptedException异常，表示失败，需要补发消息
            channel.confirmSelect();

            channel.basicPublish("directConfirmExchange", "confirmRoutingKey", null, message.getBytes(StandardCharsets.UTF_8));


            // 批量消息确认，它会同时向服务中确认之前当前通道中发送的所有消息是否已经全部成功写入
            channel.waitForConfirmsOrDie();

            System.out.println("消息发送成功 ");

        } catch (IOException | TimeoutException | InterruptedException e) {
            e.printStackTrace();
        } finally {


            if (channel != null) {
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
