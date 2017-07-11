package com.ces.config.dhtmlx.dao.authority;

import com.ces.config.dhtmlx.entity.authority.AuthorityCode;
import com.ces.xarch.core.persistence.jpa.StringIDDao;

/**
 * 编码权限DAO
 * 
 * @author luojinkai
 * @date 2015-03-12
 */
public interface AuthorityCodeDao extends StringIDDao<AuthorityCode> {

	public AuthorityCode findByObjectIdAndObjectTypeAndMenuIdAndComponentVersionIdAndCodeTypeCode(String oId, String oType, String mId, String cId, String cTypeCode);

}
