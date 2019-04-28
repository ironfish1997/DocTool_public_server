package top.liuliyong.publicservice.common.enums;

import lombok.Getter;

/**
 * 消息队列名称
 *
 * @Author liyong.liu
 * @Date 2019-04-28
 **/
@Getter
public enum MQQueueEnum {
    TEMPMESSAGEQUEUE("systemTempNotificationQueue"),
    PERMANENTMESSAGEQUEUE("systemPermanentNotificationQueue"),
    ;
    private String name;

    MQQueueEnum(String name) {
        this.name = name;
    }
}
