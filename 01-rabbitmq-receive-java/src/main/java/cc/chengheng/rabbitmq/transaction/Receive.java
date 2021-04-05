package cc.chengheng.rabbitmq.transaction;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class Receive {
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

            // 队列声明，rabbitmq 创建队列
            channel.queueDeclare("transactionQueue", true, false, false, null);

            // 交换机声明
            channel.exchangeDeclare("directTransactionExchange", "direct", true);

            // 队列绑定到指定的交换机
            channel.queueBind("transactionQueue", "directTransactionExchange", "transactionRoutingKey");

            // 开启事务：不提交，消费者依然可以获取队列中的消息，并且将队列中接收的消息移除
            channel.txSelect();

            // 获取队列中的数据消息
            channel.basicConsume("transactionQueue", true, "", new DefaultConsumer(channel) {

                // 处理投递信息
                @Override
                public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                    String message = new String(body);
                    System.out.println("消费者--" + message);

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
