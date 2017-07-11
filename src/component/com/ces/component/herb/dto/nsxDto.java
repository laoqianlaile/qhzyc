package com.ces.component.herb.dto;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 * 农事项Dto
 * Created by Administrator on 2016/6/1.
 */
@XmlRootElement(name = "nsx")
public class nsxDto {
    private String token;
    private String id;//用户id
    private String loginName;//用户姓名
    private String password;//用户密码
    private String telephone;

    public String getToken() {
        return token;
    }

    public String getId() {
        return id;
    }

    public String getLoginName() {
        return loginName;
    }
    @XmlTransient
    public String getPassword() {
        return password;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }
}
