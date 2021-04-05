package cc.chengheng.rabbitmq;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeoutException;

/**
 * 接受消息
 */
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
            connection = factory.newConnection();
            channel = connection.createChannel();

            // 这个代码在接收端，不需要，他是创建队列的。
//            channel.queueDeclare("myQueue1", true, false, false, null);

            /**
             * 接受消息
             * queue -队列名称: 当前消费者需要监听的队列名，队列名必须要与发送时的队列名完全一致
             * autoAck-如果服务器应考虑一旦传递已确认的消息，则为true；如果服务器应该期望显式确认，则返回false
             * consumerTag -客户生成的消费者标签以建立上下文
             * callback -消费者对象的接口
             * 注意：
             *      使用了basicConsume方法以后，会启动一个线程在持续的监听队列，如果队列中有信息的数据进入，则会自动接受消息
             *      因此不能关闭连接和通道
             */
            channel.basicConsume("myQueue", true, "", new DefaultConsumer(channel) {

                // 处理接收到的消息回调
                @Override
                public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                    String message = new String(body, StandardCharsets.UTF_8);
                    System.out.println("消费者-" + message);
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        } finally {
           // 不能关闭通道和连接，否则收不到消息,因为获取了消息，直接关闭了，就收不到消息了
        }
    }
}
