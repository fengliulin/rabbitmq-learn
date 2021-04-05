package cc.chengheng.rabbitmq.service.Impl;

import cc.chengheng.rabbitmq.service.ReceiveService;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service("receiveService")
public class ReceiveServiceImpl implements ReceiveService {

    // 注入Amqp的模板类，利用这个对象来发送和接收消息
    @Resource
    private AmqpTemplate amqpTemplate;

    /**
     * 这个接收不是不间断接收消息，只能接收一次
     */
    @Override
    public void receive() {
        String message = (String) amqpTemplate.receiveAndConvert("bootDirectQueue");
        System.out.println(message);
    }

    /**
     * -@RabbitListener RabbitMQ的消息监听注解：用于持续性的自动接收消息
     * 这个方法不需要手动调用Spring，会自动运行这个监听
     *
     * @param message 接收到的具体的消息数据
     * 注意：
     *      如果监听方法正常结束，Spring就会自动确认消息，如果出现异常则不会确认消息；
     *      因此在消息处理时我们需要做好消息的防止重复处理
     */
    @RabbitListener(queues = {"bootDirectQueue"})
    @Override
    public void directReceive(String message) {
        System.out.println("监听器接收的消息----" + message);
//        System.out.println(10 / 0);
    }

//    @RabbitListener(bindings = {
//            @QueueBinding( // 队列和交换机绑定
//                    value = @Queue(), // 没有指定参数创建一个随机的队列
//                    exchange = @Exchange(name = "fanoutExchange", type = "fanout") // 创建一个交换机
//            )
//    })
//    public void fanoutReceive01(String message) {
//        System.out.println("监听器接收的消息----" + message);
//    }
//
//    @RabbitListener(bindings = {
//            @QueueBinding( // 队列和交换机绑定
//                    value = @Queue(), // 没有指定参数创建一个随机的队列
//                    exchange = @Exchange(name = "fanoutExchange", type = "fanout") // 创建一个交换机
//            )
//    })
//
//    @RabbitListener(bindings = {
//            @QueueBinding( // 队列和交换机绑定
//                    value = @Queue("topic.customQueue01"), // 没有指定参数创建一个随机的队列，有参数用有参数的名称生成队列
//                    key = {"aa"}, // 生一个路由键和路由名称绑定
//                    exchange = @Exchange(name = "topicExchange", type = "topic") // 创建一个交换机
//            )
//    })
//    public void topicReceive01(String message) {
//        System.out.println("topic01消费者 ---- aa ----" + message);
//    }
//
//
//
//    @RabbitListener(bindings = {
//            @QueueBinding( // 队列和交换机绑定
//                    value = @Queue("topic.customQueue02"), // 没有指定参数创建一个随机的队列，有参数用有参数的名称生成队列
//                    key = {"aa.*"}, // 生一个路由键和路由名称绑定
//                    exchange = @Exchange(name = "topicExchange", type = "topic") // 创建一个交换机
//            )
//    })
//    public void topicReceive02(String message) {
//        System.out.println("topic01消费者 ---- aa.* ----" + message);
//    }
//
//
//
//    @RabbitListener(bindings = {
//            @QueueBinding( // 队列和交换机绑定
//                    value = @Queue("topic.customQueue03"), // 没有指定参数创建一个随机的队列，有参数用有参数的名称生成队列
//                    key = {"aa.#"}, // 生一个路由键和路由名称绑定
//                    exchange = @Exchange(name = "topicExchange", type = "topic") // 创建一个交换机
//            )
//    })
//    public void topicReceive03(String message) {
//        System.out.println("topic01消费者 ---- aa.# ----" + message);
//    }
}
