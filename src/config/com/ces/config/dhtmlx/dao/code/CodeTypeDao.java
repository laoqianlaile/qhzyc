package com.ces.config.dhtmlx.dao.code;

import java.util.List;

import org.springframework.data.jpa.repository.Query;

import com.ces.config.dhtmlx.entity.code.CodeType;
import com.ces.xarch.core.persistence.jpa.StringIDDao;

/**
 * 编码类型Dao
 * 
 * @author wanglei
 * @date 2013-07-15
 */
public interface CodeTypeDao extends StringIDDao<CodeType> {

    /**
     * 根据名称获取编码类型
     * 
     * @param name 编码类型名称
     * @return CodeType
     */
    public CodeType getByName(String name);

    /**
     * 根据编码获取编码类型
     * 
     * @param code 编码类型编码
     * @return CodeType
     */
    public CodeType getByCode(String code);

    /**
     * 获取系统下的编码类型最大显示顺序
     * 
     * @param systemId 系统ID
     */
    @Query("select max(showOrder) from CodeType where systemId=?")
    public Integer getMaxShowOrder(String systemId);

    /**
     * 获取显示顺序范围内的编码类型
     * 
     * @param start 开始显示顺序
     * @param end 结束显示顺序
     * @param systemId 系统ID
     * @return List<Code>
     */
    public List<CodeType> getByShowOrderBetweenAndSystemId(Integer start, Integer end, String systemId);
}
