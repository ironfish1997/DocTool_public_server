package top.liuliyong.publicservice.web.websocket.interceptor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;
import top.liuliyong.account.client.AccountClient;
import top.liuliyong.account.common.response.AccountOperationResponse;

import javax.servlet.http.HttpSession;
import java.util.Map;

/**
 * @Author liyong.liu
 * @Date 2019-04-26
 **/
@Component
@Slf4j
public class AuthHandshakeInterceptor implements HandshakeInterceptor {
    private final AccountClient accountClient;

    public AuthHandshakeInterceptor(AccountClient accountClient) {
        this.accountClient = accountClient;
    }


    /**
     * Invoked before the handshake is processed.
     *
     * @param request    the current request
     * @param response   the current response
     * @param wsHandler  the target WebSocket handler
     * @param attributes attributes from the HTTP handshake to associate with the WebSocket
     *                   session; the provided attributes are copied, the original map is not used.
     * @return whether to proceed with the handshake ({@code true}) or abort ({@code false})
     */
    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Map<String, Object> attributes) throws Exception {
        if (request instanceof ServletServerHttpRequest) {
            String path = request.getURI().getPath();
            String session_id = path.split("session_id=")[1];
            session_id = session_id.split("/")[0];
            if (session_id != null && session_id.length() > 0) {
                System.out.println("当前session_id=" + session_id);
                if (attributes.get("account") != null) {
                    log.info(">>>>>> 身份验证成功");
                    return true;
                }
                ServletServerHttpRequest serverHttpRequest = (ServletServerHttpRequest) request;
                HttpSession session = serverHttpRequest.getServletRequest().getSession();
                AccountOperationResponse accountCheckResult = accountClient.logincheck(session_id);
                if (accountCheckResult != null && accountCheckResult.getRtn() == 0) {
                    attributes.put("account", accountCheckResult.getData());
                    log.info(">>>>>> 身份验证成功");
                    return true;
                }
            }
            log.warn(">>>>>> 身份验证失败");
            return false;
        } else {
            log.warn(">>>>>> 身份验证失败");
            return false;
        }
    }

    /**
     * Invoked after the handshake is done. The response status and headers indicate
     * the results of the handshake, i.e. whether it was successful or not.
     *
     * @param request   the current request
     * @param response  the current response
     * @param wsHandler the target WebSocket handler
     * @param exception an exception raised during the handshake, or {@code null} if none
     */
    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Exception exception) {

    }
}
