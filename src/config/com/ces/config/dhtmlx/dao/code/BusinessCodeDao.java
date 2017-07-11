package com.ces.config.dhtmlx.dao.code;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import com.ces.config.dhtmlx.entity.code.BusinessCode;
import com.ces.xarch.core.persistence.jpa.StringIDDao;

/**
 * 业务表编码Dao
 * 
 * @author wanglei
 * @date 2014-09-17
 */
public interface BusinessCodeDao extends StringIDDao<BusinessCode> {

    /**
     * 根据编码类型编码获取相关的业务表编码
     * 
     * @param codeTypeCode 编码类型编码
     * @return BusinessCode
     */
    public BusinessCode getByCodeTypeCode(String codeTypeCode);

    /**
     * 根据业务表名称获取相关的业务表编码
     * 
     * @param tableName 业务表名称
     * @return BusinessCode
     */
    public BusinessCode getByTableName(String tableName);

    /**
     * 根据编码类型编码删除业务表编码
     * 
     * @param codeTypeCode 编码类型编码
     */
    @Transactional
    @Modifying
    @Query("delete from BusinessCode where codeTypeCode=?")
    public void deleteByCodeTypeCode(String codeTypeCode);
}
