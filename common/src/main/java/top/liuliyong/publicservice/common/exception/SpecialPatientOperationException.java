package top.liuliyong.publicservice.common.exception;

import lombok.EqualsAndHashCode;
import top.liuliyong.publicservice.common.enums.StatusEnum;
import lombok.Data;

/**
 * @Author liyong.liu
 * @Date 2019-04-22
 **/
@EqualsAndHashCode(callSuper = false)
@Data
public class SpecialPatientOperationException extends RuntimeException {

    private Integer rtn;
    private String msg;

    public SpecialPatientOperationException(Integer rtn, String msg) {
        this.msg = msg;
        this.rtn = rtn;
    }

    public SpecialPatientOperationException(StatusEnum statusEnum) {
        this.msg = statusEnum.getMsg();
        this.rtn = statusEnum.getCode();
    }
}
