package com.ces.config.dao.base;

import org.springframework.data.jpa.repository.Query;

import com.ces.xarch.core.entity.StringIDEntity;
import com.ces.xarch.core.persistence.jpa.StringIDDao;
/**
 * <p>描述: 自定义模块展现Dao基础类</p>
 * <p>公司: 上海中信信息发展股份有限公司</p>
 * @author qiucs   
 * @date 2014-3-11 下午6:04:13
 *
 * @param <T>
 */
public interface ShowModuleStringIDDao<T extends StringIDEntity> extends StringIDDao<T> {
    
    /**
     * qiucs 2014-10-15 
     * <p>描述: 获取当前可用的存储位置</p>
     * @return Object    返回类型   
     * @throws
     */
    @Query(value="select t.location from t_xtpz_system_document_path t where t.available='0' ", nativeQuery=true)
    public String findDocUploadStoreLaction();
}
