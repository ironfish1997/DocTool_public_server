package model;

import lombok.Data;

import java.io.Serializable;

/**
 * 联系信息
 *
 * @Author liyong.liu
 * @Date 2019/3/12
 **/
@Data
public class Contacts implements Serializable {
    private String wechat;
    private String qq;
    private String phone_number;
    private String email;

    public Contacts(String wechat, String qq, String phone_number, String email) {
        this.wechat = wechat;
        this.qq = qq;
        this.phone_number = phone_number;
        this.email = email;
    }

    public Contacts() {
    }
}