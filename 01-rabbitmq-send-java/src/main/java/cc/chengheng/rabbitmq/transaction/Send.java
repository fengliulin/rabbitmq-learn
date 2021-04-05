package cc.chengheng.rabbitmq.transaction;

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
            channel.queueDeclare("transactionQueue", true, false, false, null);

            // 交换机声明
            channel.exchangeDeclare("directTransactionExchange", "direct", true);


            channel.queueBind("transactionQueue", "directTransactionExchange", "transactionRoutingKey");
            String message = "事务的测试消息！";

            // 启动一个事务，启动事务以后所有写入到队列的消息，必须调用txCommit()提交事物 或 txRollBack回滚事务
            channel.txSelect();
            channel.basicPublish("directTransactionExchange", "transactionRoutingKey", null, message.getBytes(StandardCharsets.UTF_8));
//            System.out.println(10 / 0);
            channel.basicPublish("directTransactionExchange", "transactionRoutingKey", null, message.getBytes(StandardCharsets.UTF_8));

            // 提交事务: 会把内存的数据持久化写入到队列中
            channel.txCommit();

            System.out.println("消息发送成功");

        } catch (IOException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        } finally {
            // 回滚事务，释放内存数据，不写入任何数据
            channel.txRollback();

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
