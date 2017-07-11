package com.ces.config.dhtmlx.entity.code;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.ces.xarch.core.entity.StringIDEntity;

/**
 * 业务表编码实体类
 * 
 * @author wanglei
 * @date 2014-09-17
 */
@Entity
@Table(name = "T_XTPZ_BUSINESS_CODE")
public class BusinessCode extends StringIDEntity {

    private static final long serialVersionUID = 442464086927866293L;
    
    public static String TABLE = "0";
    public static String JAVA  = "1";
    
    /** * 业务编码类型： 0-业务表， 1-JAVA应用类(实现CodeApplication接口) */
    private String businessCodeType = "0";

    /** * 业务表名称 */
    private String tableName;

    /** * 编码名称对应业务表字段 */
    private String codeNameField;

    /** * 编码值对应业务表字段 */
    private String codeValueField;

    /** * 显示顺序对应业务表字段 */
    private String showOrderField;

    /** * ID字段对应业务表字段 */
    private String idField;

    /** * PARENT_ID字段对应业务表字段 */
    private String parentIdField;

    /** * 编码类型编码 */
    private String codeTypeCode;

    /** * 是否是系统管理平台的表 0：不是，1：是 */
    private String isAuth;

    /** * 是否定时更新 */
    private String isTimingUpdate;

    /** * 定时更新间隔 */
    private String period;
    
    /** * 类全名 */
    private String className;

    public String getBusinessCodeType() {
		return businessCodeType;
	}

	public void setBusinessCodeType(String businessCodeType) {
		this.businessCodeType = businessCodeType;
	}

	public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getCodeNameField() {
        return codeNameField;
    }

    public void setCodeNameField(String codeNameField) {
        this.codeNameField = codeNameField;
    }

    public String getCodeValueField() {
        return codeValueField;
    }

    public void setCodeValueField(String codeValueField) {
        this.codeValueField = codeValueField;
    }

    public String getShowOrderField() {
        return showOrderField;
    }

    public void setShowOrderField(String showOrderField) {
        this.showOrderField = showOrderField;
    }

    public String getIdField() {
        return idField;
    }

    public void setIdField(String idField) {
        this.idField = idField;
    }

    public String getParentIdField() {
        return parentIdField;
    }

    public void setParentIdField(String parentIdField) {
        this.parentIdField = parentIdField;
    }

    public String getCodeTypeCode() {
        return codeTypeCode;
    }

    public void setCodeTypeCode(String codeTypeCode) {
        this.codeTypeCode = codeTypeCode;
    }

    public String getIsAuth() {
        return isAuth;
    }

    public void setIsAuth(String isAuth) {
        this.isAuth = isAuth;
    }

    public String getIsTimingUpdate() {
        return isTimingUpdate;
    }

    public void setIsTimingUpdate(String isTimingUpdate) {
        this.isTimingUpdate = isTimingUpdate;
    }

    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}
}
