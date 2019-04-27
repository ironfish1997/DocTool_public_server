package top.liuliyong.publicservice.web.websocket.handler;

import com.alibaba.fastjson.JSON;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.support.DefaultHandshakeHandler;
import top.liuliyong.account.client.AccountClient;
import top.liuliyong.account.common.response.AccountOperationResponse;
import top.liuliyong.account.model.Account;
import top.liuliyong.publicservice.web.websocket.common.MyPrincipal;

import javax.servlet.http.HttpSession;
import java.security.Principal;
import java.util.Map;

/**
 * @Author liyong.liu
 * @Date 2019-04-26
 **/
@Component
public class MyHandshakeHandler extends DefaultHandshakeHandler {
    private final AccountClient accountClient;

    public MyHandshakeHandler(AccountClient accountClient) {
        this.accountClient = accountClient;
    }

    /**
     * A method that can be used to associate a user with the WebSocket session
     * in the process of being established. The default implementation calls
     * {@link ServerHttpRequest#getPrincipal()}
     * <p>Subclasses can provide custom logic for associating a user with a session,
     * for example for assigning a name to anonymous users (i.e. not fully authenticated).
     *
     * @param request    the handshake request
     * @param wsHandler  the WebSocket handler that will handle messages
     * @param attributes handshake attributes to pass to the WebSocket session
     * @return the user for the WebSocket session, or {@code null} if not available
     */
    @Override
    protected Principal determineUser(ServerHttpRequest request, WebSocketHandler wsHandler, Map<String, Object> attributes) {
        if (request instanceof ServletServerHttpRequest) {
            String path = request.getURI().getPath();
            String session_id = path.split("session_id=")[1];
            session_id = session_id.split("/")[0];
            if (session_id != null && session_id.length() > 0) {
                System.out.println(">>>>>> 当前session_id=" + session_id);
                if (attributes.get("account") != null) {
                    logger.info(">>>>>> 上传到连接map");
                    Account resultAcc = JSON.parseObject(JSON.toJSONString(attributes.get("account")), Account.class);
                    return new MyPrincipal(resultAcc.getAccount_id());
                }
                ServletServerHttpRequest serverHttpRequest = (ServletServerHttpRequest) request;
                HttpSession session = serverHttpRequest.getServletRequest().getSession();
                AccountOperationResponse accountCheckResult = accountClient.logincheck(session_id);
                if (accountCheckResult != null && accountCheckResult.getRtn() == 0) {
                    attributes.put("account", accountCheckResult.getData());
                    Account resultAcc = JSON.parseObject(JSON.toJSONString(accountCheckResult.getData()), Account.class);
                    logger.info(">>>>>> 上传到连接map");
                    return new MyPrincipal(resultAcc.getAccount_id());
                }
            }
        }
        return null;
    }
}
