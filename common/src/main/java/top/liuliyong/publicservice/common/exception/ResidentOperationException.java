package top.liuliyong.publicservice.common.exception;

import top.liuliyong.publicservice.common.enums.StatusEnum;

/**
 * @Author liyong.liu
 * @Date 2019-04-22
 **/
public class ResidentOperationException extends RuntimeException {
    private Integer rtn;
    private String msg;

    public ResidentOperationException(Integer rtn, String msg) {
        this.rtn = rtn;
        this.msg = msg;
    }

    public ResidentOperationException(StatusEnum statusEnum) {
        this.rtn = statusEnum.getCode();
        this.msg = statusEnum.getMsg();
    }

    public Integer getRtn() {
        return this.rtn;
    }

    public String getMsg() {
        return this.msg;
    }

}
