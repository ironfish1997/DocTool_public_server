package top.liuliyong.publicservice.web.websocket.config;

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

    private final AuthHandshakeInterceptor authHandshakeInterceptor;

    private final MyHandshakeHandler myHandshakeHandler;

    public WebSocketConfig(AuthHandshakeInterceptor authHandshakeInterceptor, MyHandshakeHandler myHandshakeHandler) {
        this.authHandshakeInterceptor = authHandshakeInterceptor;
        this.myHandshakeHandler = myHandshakeHandler;
    }

    /**
     * Configure message broker options.
     */
    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        //客户端需要把消息发送到/message/xxx地址
        registry.setApplicationDestinationPrefixes("/message");
        //服务端广播消息的路径前缀，客户端需要相应订阅/topic/yyy这个地址的消息
        registry.enableSimpleBroker("/topic");
    }

    /**
     * Register STOMP endpoints mapping each to a specific URL and (optionally)
     * enabling and configuring SockJS fallback options.
     */
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/websocket/{session_id}").setAllowedOrigins("*").setHandshakeHandler(myHandshakeHandler).addInterceptors(authHandshakeInterceptor).withSockJS();
    }
}
