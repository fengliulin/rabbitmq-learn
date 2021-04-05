package cc.chengheng.rabbitmq.exchange.direct;

import com.rabbitmq.client.*;

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
            connection = factory.newConnection(); // 建立连接
            channel = connection.createChannel(); // 建立通道

            // 队列声明，rabbitmq 创建队列
            channel.queueDeclare("myDirectQueue", true, false, false, null);

            // 交换机声明
            /**
             * exchange -交换机名称
             * type -交换机类型: direct  queue   topic headers(效率不高，不用，用前三个)
             * durable -如果我们声明持久交换，则为true（该交换将在服务器重启后保留下来）
             * 注意：
             *      交换机如果存在不声明，如果不存在交换机声明
             *      这个代码可以不存在，但是必须保存交换机已经被声明
             */
            channel.exchangeDeclare("directExchange", "direct", true);

            /**
             * 绑定交换机
             * queue -队列名称
             * exchange 交换机名称
             * routingKey -用于绑定的路由键(就是BindingKey)
             * 注意：
             *      在进行队列和交换机绑定时，必须确定交换机和队列已经成功声明
             */
            channel.queueBind("myDirectQueue", "directExchange", "directRoutingKey");
            String message = "direct的测试消息！";


            /**
             * 发送消息到指定队列
             * exchange -将消息发布到的交换机
             * routingKey -路由键，就是前面channel.queueBind 绑定的路由键(就是BindingKey)
             * props -消息的其他属性-路由标头等
             * body -邮件正文
             */
            channel.basicPublish("directExchange","directRoutingKey",null, message.getBytes(StandardCharsets.UTF_8));

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
