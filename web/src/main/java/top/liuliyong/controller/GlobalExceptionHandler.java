package top.liuliyong.controller;

import com.alibaba.fastjson.JSON;
import exception.ResidentOperationException;
import exception.SpecialPatientOperationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import top.liuliyong.response.PublicResponse;

import javax.servlet.http.HttpServletResponse;


/**
 * 全局错误处理
 *
 * @Author liyong.liu
 * @Date 2019/3/12
 **/
@ControllerAdvice
class GlobalExceptionHandler {
    private Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public Object defaultErrorHandler(HttpServletResponse res, Exception e) {
        logger.error("global catched error", e);
        int rtn = -10000;
        String msg = "unknown error";
        if (e instanceof ResidentOperationException) {
            ResidentOperationException result = (ResidentOperationException) e;
            msg = result.getMsg();
            rtn = result.getRtn();
        }
        if (e instanceof SpecialPatientOperationException) {
            SpecialPatientOperationException result = (SpecialPatientOperationException) e;
            msg = result.getMsg();
            rtn = result.getRtn();
        }
        PublicResponse error = new PublicResponse(rtn, msg, null);
        res.setStatus(500);
        return JSON.toJSONString(error);
    }

}