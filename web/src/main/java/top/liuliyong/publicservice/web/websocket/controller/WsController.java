package top.liuliyong.publicservice.web.websocket.controller;

import com.alibaba.fastjson.JSON;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.user.SimpUser;
import org.springframework.messaging.simp.user.SimpUserRegistry;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;
import top.liuliyong.publicservice.common.model.MQMessageObject;
import top.liuliyong.publicservice.repository.util.JedisUtil;
import top.liuliyong.publicservice.web.model.WebSocketResponseMessage;
import top.liuliyong.publicservice.web.websocket.common.Constants;
import top.liuliyong.publicservice.web.websocket.common.Message;

import java.util.Date;

/**
 * @Author liyong.liu
 * @Date 2019-04-25
 **/
@RestController
@Slf4j
@Api(value = "websocketController", description = "websocket相关服务")
@Validated
public class WsController {

    private final SimpMessagingTemplate messagingTemplate;

    private final SimpUserRegistry userRegistry;

    public WsController(SimpMessagingTemplate messagingTemplate, SimpUserRegistry userRegistry) {
        this.messagingTemplate = messagingTemplate;
        this.userRegistry = userRegistry;
    }

    /**
     * 从systemTempNotificationQueue消息队列获取消息并推送临时消息到/topic/tempNotifications WS连接
     */
    @RabbitListener(bindings = @QueueBinding(value = @Queue("systemTempNotificationQueue"), exchange = @Exchange("systemExchange"), key = "systemTempNotification"))
    public void sendTempNotifications(MQMessageObject msg) {
        log.info("获取到新的临时消息: " + JSON.toJSONString(msg));
        SimpUser simpUser = userRegistry.getUser(msg.getId());
        if (simpUser != null && StringUtils.isEmpty(simpUser.getName())) {
            messagingTemplate.convertAndSendToUser(msg.getId(), "/topic/tempNotifications", msg);
        }
        //否则将消息存储到redis，等用户上线后主动拉取未读消息
        else {
            //存储消息的Redis列表名
            String listKey = Constants.REDIS_UNREAD_MSG_PREFIX + msg.getId() + ":/topic/tempNotifications";
//            log.info(MessageFormat.format("消息接收者{0}还未建立WebSocket连接，{1}发送的消息【{2}】将被存储到Redis的【{3}】列表中", msg.getId(), , payload, listKey));

            //存储消息到Redis中
            JedisUtil.lpush(listKey, msg, 0);
        }
    }

    /**
     * 从systemPermanentNotificationQueue消息队列获取消息并推送永久消息到/topic/permanentNotifications WS连接
     */
    @RabbitListener(bindings = @QueueBinding(value = @Queue("systemPermanentNotificationQueue"), exchange = @Exchange("systemExchange"), key = "systemPermanentNotification"))
    public void sendPermanentNotifications(String notificationMsg) {
        log.info("获取到新的长期消息: " + notificationMsg);
        WebSocketResponseMessage rtn = new WebSocketResponseMessage(new Date().getTime(), 0, notificationMsg);
        messagingTemplate.convertAndSend("/topic/permanentNotifications", rtn);
    }

    @MessageMapping(value = "/sendMessage")
    public void sendMessage(SimpMessageHeaderAccessor sha, Message params) {
        log.info("sendMessage: {}", params);
        String session_id = sha.getSessionId();
        messagingTemplate.convertAndSendToUser("admin", "/topic/tempNotifications", params);
    }

}
