package cc.chengheng.rabbitmq.config;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    /**
     * 配置一个交换机
     * @return
     */
    @Bean
    public DirectExchange directExchange() {

        return new DirectExchange("bootDirectExchange");
    }

    /**
     * 配置一个队列
     * @return
     */
    @Bean
    public Queue directQueue () {
        return new Queue("bootDirectQueue");
    }


    /**
     * 配置一个队列和交换机的绑定
     * @param directQueue 需要绑定的队列，参数名必须与某个@Bean的方法名完全相同，会自动注入
     * @param directExchange 需要绑定的交换机的对象，参数名必须与某个@Bean的方法名完全相同，会自动注入
     * @return
     */
    @Bean
    public Binding directBinding(Queue directQueue, DirectExchange directExchange) {

        // 绑定队列 到 指定的交换机 与 路由键
        return BindingBuilder.bind(directQueue).to(directExchange).with("bootDirectRoutingKey");
    }
}
