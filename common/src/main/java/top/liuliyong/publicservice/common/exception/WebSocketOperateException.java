package top.liuliyong.publicservice.common.exception;

import top.liuliyong.publicservice.common.enums.StatusEnum;

/**
 * websocket操作错误
 * @Author liyong.liu
 * @Date 2019-04-26
 **/
public class WebSocketOperateException extends RuntimeException {
    private Integer rtn;
    private String msg;

    public WebSocketOperateException(Integer rtn, String msg) {
        this.msg = msg;
        this.rtn = rtn;
    }

    public WebSocketOperateException(StatusEnum statusEnum) {
        this.msg = statusEnum.getMsg();
        this.rtn = statusEnum.getCode();
    }
}
