package com.ces.config.dhtmlx.entity.database;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.ces.xarch.core.entity.StringIDEntity;

/**
 * 数据库实体类
 * 
 * @author wanglei
 * @date 2013-07-10
 */
@Entity
@Table(name = "T_XTPZ_DATABASE")
public class Database extends StringIDEntity {
    private static final long serialVersionUID = -4989586296811633396L;

    /** * 数据库名称 */
    private String name;

    /** * 数据库实例名 */
    private String instanceName;

    /** * IP地址 */
    private String ip;

    /** * 端口号 */
    private Integer port;

    /** * 数据库类型 0-ORACLE 1-SQLSERVER 2-国产达梦 */
    private String type;

    /** * 用户名 */
    private String userName;

    /** * 密码 */
    private String password;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getInstanceName() {
        return instanceName;
    }

    public void setInstanceName(String instanceName) {
        this.instanceName = instanceName;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}
