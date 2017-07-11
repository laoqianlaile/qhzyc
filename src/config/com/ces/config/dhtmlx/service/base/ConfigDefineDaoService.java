package com.ces.config.dhtmlx.service.base;

import org.springframework.transaction.annotation.Transactional;

import com.ces.config.service.base.StringIDConfigDefineDaoService;
import com.ces.xarch.core.entity.StringIDEntity;
import com.ces.xarch.core.persistence.jpa.StringIDDao;

/**
 * <p>描述: 系统配置平台服务层</p>
 * <p>公司: 上海中信信息发展股份有限公司</p>
 * @author qiucs   
 * @date 2013-7-12 下午2:13:21
 *
 * @param <T>
 * @param <Dao>
 */
@Transactional(readOnly = true)
public class ConfigDefineDaoService<T extends StringIDEntity, Dao extends StringIDDao<T>> extends StringIDConfigDefineDaoService<T, Dao>{
    
}
