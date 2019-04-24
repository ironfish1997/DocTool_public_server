package exception;

import enums.StatusEnum;
import lombok.Data;

/**
 * @Author liyong.liu
 * @Date 2019-04-22
 **/
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
