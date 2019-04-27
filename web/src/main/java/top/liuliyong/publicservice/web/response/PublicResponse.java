package top.liuliyong.publicservice.web.response;

import top.liuliyong.publicservice.common.enums.StatusEnum;
import lombok.Data;

/**
 * 辖区居民模块返回体
 *
 * @Author liyong.liu
 * @Date 2019-04-21
 **/
@Data
public class PublicResponse {
    //返回状态
    private int rtn;
    //返回信息
    private String msg;
    //返回数据体
    private Object data;

    public PublicResponse(int rtn, String msg, Object data) {
        this.rtn = rtn;
        this.msg = msg;
        this.data = data;
    }

    public PublicResponse(StatusEnum se) {
        this.rtn = se.getCode();
        this.msg = se.getMsg();
        this.data = null;
    }
}
