package top.liuliyong.publicservice.common.model;

import com.sun.xml.internal.ws.developer.Serialization;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 消息队列中传递的消息对象
 *
 * @Author liyong.liu
 * @Date 2019-04-25
 **/
@Data
@Serialization
@AllArgsConstructor
public class MQMessageObject {
    private String type;
    private String id;
    private Object data;
}
