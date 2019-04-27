package top.liuliyong.controller.MQsender;


import org.junit.Test;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import top.liuliyong.controller.BaseTest;

@Component
public class WsControllerTest extends BaseTest {
    @Autowired
    private AmqpTemplate amqpTemplate;

    @Test
    public void send() {
        amqpTemplate.convertAndSend("systemNotification", "这是一条系统消息");
    }
}