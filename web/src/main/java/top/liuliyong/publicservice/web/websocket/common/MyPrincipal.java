package top.liuliyong.publicservice.web.websocket.common;

import java.security.Principal;

/**
 * 自定义principal
 *
 * @Author liyong.liu
 * @Date 2019-04-26
 **/
public class MyPrincipal implements Principal {
    private String account_id;

    public MyPrincipal(String account_id) {
        this.account_id = account_id;
    }

    @Override
    public String getName() {
        return account_id;
    }
}