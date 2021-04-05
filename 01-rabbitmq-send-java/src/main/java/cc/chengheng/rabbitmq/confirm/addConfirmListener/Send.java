package cc.chengheng.rabbitmq.confirm.addConfirmListener;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ConfirmListener;
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

            // 队列绑定
            channel.queueBind("confirmQueue", "directConfirmExchange", "confirmRoutingKey");

            String message = "普通发送者确认模式测试消息！";

            // 启动发送者确认模式
            channel.confirmSelect();

            // 异步消息确认监听器，需要在发送消息前启动
            channel.addConfirmListener(new ConfirmListener() {

                // 消息确定以后的回调方法
                @Override
                public void handleAck(long deliveryTag/* 被确认的消息编号 */, boolean multiple/* 当前消息是否同时没有确认了多个 */) throws IOException {
                    System.out.println("消息被确认了--- 消息编号:" + deliveryTag + "是否确认了多条：" + multiple);
                }

                /**
                 * 消息没有确认的回调方法
                 * @param deliveryTag
                 * @param multiple true  表示小于当前编号的所有消息可能都没有发送成功需要进行消息的补发
                 *                 false 表示当前编号的消息没法发送成功需要进行补发
                 * @throws IOException
                 */
                @Override
                public void handleNack(long deliveryTag/* 没有被确认的消息编号 */, boolean multiple/* 当前消息是否确认了多个 */) throws IOException {
                    System.out.println("消息没有被确认了--- 消息编号:" + deliveryTag + "是否没有确认了多条：" + multiple);
                }
            });

            for (int i = 0; i <= 10000; i++) {
                channel.basicPublish("directConfirmExchange", "confirmRoutingKey", null, message.getBytes(StandardCharsets.UTF_8));
            }

            System.out.println("消息发送成功");

        } catch (IOException | TimeoutException e) {
            e.printStackTrace();
        } finally {

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
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
