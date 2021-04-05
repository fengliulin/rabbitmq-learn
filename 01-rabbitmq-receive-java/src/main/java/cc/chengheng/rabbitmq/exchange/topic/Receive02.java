package cc.chengheng.rabbitmq.exchange.topic;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class Receive02 {
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
            connection = factory.newConnection(); // 建立连接
            channel = connection.createChannel(); // 建立通道

            channel.queueDeclare("topicQueue02", true, false, false, null);
            channel.exchangeDeclare("topicExchange", "topic", true);

            //aa.*  只匹配一级单词
            channel.queueBind("topicQueue02", "topicExchange", "aa.*");


            // 获取队列中的数据消息
            channel.basicConsume("topicQueue02", true, "", new DefaultConsumer(channel) {

                // 处理投递信息
                @Override
                public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                    String message = new String(body);
                    System.out.println("Receive01消费者aa.*--" + message);

                }
            });

        } catch (IOException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        } finally {

        }
    }
}
