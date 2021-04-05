package cc.chengheng.rabbitmq.confirm;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeoutException;

public class Receive {
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
        Channel channel = null;         // 定义通道

        try {

            connection = factory.newConnection(); // 建立连接
            channel = connection.createChannel(); // 建立通道

            // 队列声明
            channel.queueDeclare("confirmQueue", true, false, false, null);

            // 交换机声明
            channel.exchangeDeclare("directConfirmExchange", "direct", true);

            // 队列绑定到指定的交换机
            channel.queueBind("confirmQueue", "directConfirmExchange", "confirmRoutingKey");

            // 开启事务
            channel.txSelect();

            /**
             * 处理队列中的消息
             * 参数2： true  表示自动消息确认，确认以后消息会从队列中移除
             *        false 表示手动确认消息
             */
            channel.basicConsume("confirmQueue", false, "", new DefaultConsumer(channel) {

                // 处理接收到的消息回调
                @Override
                public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
//                    System.out.println(10/0);

                    //  获取当前消息是否被接收过一次。 true：接收过；false：没有接收过
                    System.out.println(envelope.isRedeliver());

                    String message = new String(body, StandardCharsets.UTF_8);
                    System.out.println("消费者 处理了消息-----" + message);

                    // 获取消息的编号
                    long deliveryTag = envelope.getDeliveryTag();



                    //获取当前内部类中的Channel通道
                    Channel c = this.getChannel();

                    //手动确认消息，确认以后表示当前消息已经成功处理了，需要从队列中移除
                    c.basicAck(deliveryTag, true);

                    // 如果启动事务，消息消费者确认模式为手动确认，必须要提交事务
                    c.txCommit();
                }

            });




        } catch (IOException | TimeoutException e) {
            e.printStackTrace();
        } finally {

        }
    }
}
