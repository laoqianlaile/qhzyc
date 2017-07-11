package com.ces.config.dhtmlx.dao.code;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import com.ces.config.dhtmlx.entity.code.Code;
import com.ces.xarch.core.persistence.jpa.StringIDDao;

/**
 * 编码Dao
 * 
 * @author wanglei
 * @date 2013-07-15
 */
public interface CodeDao extends StringIDDao<Code> {

    /**
     * 根据编码类型编码和编码父ID获取编码
     * 
     * @param codeTypeCode 编码类型编码
     * @return List<Code>
     */
    @Query("from Code where codeTypeCode=? and parentId is null")
    public List<Code> getByCodeTypeCodeAndParentIdIsNull(String codeTypeCode);

    /**
     * 根据编码父ID获取编码
     * 
     * @param parentId 编码父ID
     * @return List<Code>
     */
    public List<Code> getByParentId(String parentId);

    /**
     * 根据名称获取编码
     * 
     * @param name 编码名称
     * @param codeTypeCode 编码类型编码
     * @return Code
     */
    public Code getByNameAndCodeTypeCode(String name, String codeTypeCode);

    /**
     * 根据值获取编码
     * 
     * @param value 编码值
     * @param codeTypeCode 编码类型编码
     * @return Code
     */
    public Code getByValueAndCodeTypeCode(String value, String codeTypeCode);

    /**
     * 更改编码类型编码
     * 
     * @param oldCodeTypeCode 旧的编码类型编码
     * @param newCodeTypeCode 新的编码类型编码
     */
    @Transactional
    @Modifying
    @Query("update Code set codeTypeCode=? where codeTypeCode=?")
    public void updateCodeTypeCode(String oldCodeTypeCode, String newCodeTypeCode);

    /**
     * 根据编码类型编码删除编码
     * 
     * @param codeTypeCode 编码类型编码
     */
    @Transactional
    @Modifying
    @Query("delete from Code where codeTypeCode=?")
    public void deleteByCodeTypeCode(String codeTypeCode);

    /**
     * 获取编码类型下的编码最大显示顺序
     * 
     * @param codeTypeCode 编码类型编码
     */
    @Query("select max(showOrder) from Code where codeTypeCode=?")
    public Integer getMaxShowOrder(String codeTypeCode);

    /**
     * 获取显示顺序范围内的编码
     * 
     * @param start 开始显示顺序
     * @param end 结束显示顺序
     * @param codeTypeCode 编码类型编码
     * @return List<Code>
     */
    public List<Code> getByShowOrderBetweenAndCodeTypeCode(Integer start, Integer end, String codeTypeCode);
    

    /**
     * qiucs 2015-8-28 下午2:51:31
     * <p>描述: 获取需要使用缓存的编码 </p>
     * @return List<Code>
     */
    @Query("select t from Code t, CodeType p where t.codeTypeCode=p.code and p.isCache='1' order by t.codeTypeCode, t.showOrder")
    public List<Code> getUseCacheCodeList();
}
