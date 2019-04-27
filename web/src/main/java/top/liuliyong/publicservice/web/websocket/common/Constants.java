package top.liuliyong.publicservice.web.websocket.common;

/**
 * 公共常量类
 *
 * @Author liyong.liu
 * @Date 2019-04-26
 **/
public class Constants {
    /**
     * 用户信息在session中存储的变量名
     */
    public static final String SESSION_USER = "SESSION_USER";

    /**
     * 登录页面的回调地址在session中存储的变量名
     */
    public static final String SESSION_LOGIN_REDIRECT_URL = "LOGIN_REDIRECT_URL";

    /**
     * 用户未读的WebSocket消息在Redis中存储的变量名的前缀
     */
    public static final String REDIS_UNREAD_MSG_PREFIX = "stomp-websocket:unread_msg:";

}
