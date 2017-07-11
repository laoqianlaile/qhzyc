package com.ces.config.dhtmlx.entity.authority;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.ces.xarch.core.entity.StringIDEntity;

/**
 * 编码权限实体类
 * 
 * @author luojinkai
 * @date 2015-03-12
 */
@Entity
@Table(name = "T_XTPZ_AUTHORITY_CODE")
public class AuthorityCode extends StringIDEntity {

    private static final long serialVersionUID = 1L;
    
    /** 对象ID: objectType=0时，则为角色KEY，当objectType=1时，则为用户ID. */
    private String objectId;

    /** 类型：0-角色 1-用户 */
    private String objectType;
    
    /***  菜单id    ***/
    private String menuId;
    
    /****  构件id   ****/
    private String componentVersionId;
    
    /**** 编码类型code ***/
    private String codeTypeCode;
    
    /**** 编码Json数组   *****/
    private String codeJson;

	public String getCodeTypeCode() {
		return codeTypeCode;
	}

	public void setCodeTypeCode(String codeTypeCode) {
		this.codeTypeCode = codeTypeCode;
	}

	public String getCodeJson() {
		return codeJson;
	}

	public void setCodeJson(String codeJson) {
		this.codeJson = codeJson;
	}

	public String getObjectId() {
		return objectId;
	}

	public void setObjectId(String objectId) {
		this.objectId = objectId;
	}

	public String getObjectType() {
		return objectType;
	}

	public void setObjectType(String objectType) {
		this.objectType = objectType;
	}

	public String getMenuId() {
		return menuId;
	}

	public void setMenuId(String menuId) {
		this.menuId = menuId;
	}

	public String getComponentVersionId() {
		return componentVersionId;
	}

	public void setComponentVersionId(String componentVersionId) {
		this.componentVersionId = componentVersionId;
	}
    
    

}