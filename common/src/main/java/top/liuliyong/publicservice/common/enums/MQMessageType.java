package top.liuliyong.publicservice.common.enums;

import lombok.Getter;

/**
 * @Author liyong.liu
 * @Date 2019-04-25
 **/
@Getter
public enum MQMessageType {
    WEBSOCKET_SERVER("WEBSOCKET_SERVER"),
    MESSAGE("MESSAGE"),
    ;

    private String type;

    MQMessageType(String type) {
        this.type = type;
    }
}
