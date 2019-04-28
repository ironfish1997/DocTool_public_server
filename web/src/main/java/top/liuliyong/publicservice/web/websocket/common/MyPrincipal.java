package top.liuliyong.publicservice.web.websocket.common;

import java.security.Principal;

/**
 * 自定义principal
 *
 * @Author liyong.liu
 * @Date 2019-04-26
 **/
public class MyPrincipal implements Principal {
    private String area;

    public MyPrincipal(String area) {
        this.area = area;
    }

    @Override
    public String getName() {
        return area;
    }
}