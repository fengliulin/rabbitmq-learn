package cc.chengheng.rabbitmq.service;

public interface ReceiveService {

    void receive();

    void directReceive(String message);
}
