package cc.chengheng.rabbitmq;

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

            /**
             * 声明一个队列
             * queue -队列名称
             * durable -如果我们声明一个持久队列，则为true（该队列将在服务器重启后保留下来）
             * exclusive -如果我们声明一个排他队列，则为true（仅限此连接）
             * autoDelete -如果我们声明一个自动删除队列，则为true（服务器将在不再使用它时将其删除）
             * arguments -队列的其他属性（构造参数）
             *
             * 注意：
             *      队列没有，会给你创建一个新的，如果队列名字存在，那么就添加消息，采用的数据结构就是队列
             *      队列不存在，发送，数据就丢失了
             *      routingKey 就是 队列的名称
             */
            channel.queueDeclare("myQueue", true, false, false, null);
            String message = "我的RabbitMQ的测试消息test2";

            /**
             * 发送消息到MQ
             * exchange -将消息发布到的交换机，空字符串：不使用交换机
             * routingKey -路由键
             * props -消息的其他属性-路由标头等
             * body -邮件正文
             * 注意：
             *      队列名必须要与接受时完全一致
             */
            channel.basicPublish("", "myQueue1", null, message.getBytes(StandardCharsets.UTF_8));

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
