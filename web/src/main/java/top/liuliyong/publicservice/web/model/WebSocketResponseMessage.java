package top.liuliyong.publicservice.web.model;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * websocket响应消息类
 *
 * @Author liyong.liu
 * @Date 2019-04-25
 **/
@Data
@AllArgsConstructor
public class WebSocketResponseMessage {
    private long timestamp;
    private int rtn;
    private String msg;
}
