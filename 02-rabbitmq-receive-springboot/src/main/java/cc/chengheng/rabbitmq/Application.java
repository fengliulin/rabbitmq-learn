package cc.chengheng.rabbitmq;

import cc.chengheng.rabbitmq.service.ReceiveService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        ConfigurableApplicationContext run = SpringApplication.run(Application.class, args);
        ReceiveService sendService = (ReceiveService) run.getBean("receiveService");

//        sendService.receive();
    }

}
