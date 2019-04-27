package top.liuliyong.publicservice.common.enums;

import lombok.Getter;

/**
 * @Author liyong.liu
 * @Date 2019-04-21
 **/
@Getter
public enum StatusEnum {
    LACK_OF_INFORMATION(-10038, "lack of information"),
    NOT_FOUND(-10039, "target not found");
    private int code;
    private String msg;

    StatusEnum(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
