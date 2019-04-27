package top.liuliyong.publicservice.web.websocket.common;

import lombok.Data;

/**
 * @Author liyong.liu
 * @Date 2019-04-27
 **/
@Data
public class Message {
    private int type;
    private String msg;
}
