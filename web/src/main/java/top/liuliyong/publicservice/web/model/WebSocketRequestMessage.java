package top.liuliyong.publicservice.web.model;

import lombok.Getter;

/**
 * web socket请求消息类
 *
 * @Author liyong.liu
 * @Date 2019-04-25
 **/
@Getter
public class WebSocketRequestMessage {
    private String reqPath;
    private String msg;
}
