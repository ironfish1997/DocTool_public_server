package top.liuliyong.publicservice.common.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

/**
 * 消息队列中传递的消息对象
 *
 * @Author liyong.liu
 * @Date 2019-04-25
 **/
@Data
@AllArgsConstructor
public class MQMessageObject implements Serializable {
    private static final long serialVersionUID = 876323262645176354L;
    private long time;
    private String type;
    private String id;
    private Object data;
}
