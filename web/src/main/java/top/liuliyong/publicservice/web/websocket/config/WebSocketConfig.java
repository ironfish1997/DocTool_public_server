package top.liuliyong.publicservice.web.websocket.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
import top.liuliyong.publicservice.web.websocket.handler.MyHandshakeHandler;
import top.liuliyong.publicservice.web.websocket.interceptor.AuthHandshakeInterceptor;

/**
 * WebSocket配置类
 *
 * @Author liyong.liu
 * @Date 2019-04-24
 **/
@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Autowired
    private AuthHandshakeInterceptor authHandshakeInterceptor;

    @Autowired
    private MyHandshakeHandler myHandshakeHandler;

    /**
     * Configure message broker options.
     */
    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        //订阅广播 Broker（消息代理）名称
        registry.enableSimpleBroker("/topic"); // Enables a simple in-memory broker
        //客户端需要把消息发送到/message/xxx地址
        registry.setApplicationDestinationPrefixes("/message/");
        //点对点使用的订阅前缀（客户端订阅路径上会体现出来），不设置的话，默认也是/user/
        registry.setUserDestinationPrefix("/user/");
    }

    /**
     * Register STOMP endpoints mapping each to a specific URL and (optionally)
     * enabling and configuring SockJS fallback options.
     */
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/websocket/{session_id}").setAllowedOrigins("*").addInterceptors(authHandshakeInterceptor)
//                .setHandshakeHandler(myHandshakeHandler)
                .withSockJS();
    }
}
