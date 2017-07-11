package com.ces.config.action.base;

import com.ces.config.service.base.StringIDConfigService;
import com.ces.xarch.core.entity.StringIDEntity;
import com.ces.xarch.core.persistence.jpa.StringIDDao;
/**
 * 自定义服务层（Service），但不用复写持久层（Dao）
 * <p>描述: 系统配置平台Controller类的基类</p>
 * <p>公司: 上海中信信息发展股份有限公司</p>
 * @author qiucs   
 * @date 2014-8-27 下午10:18:02
 *
 * @param <T>
 * @param <Service>
 */
public class StringIDConfigDefineServiceController<T extends StringIDEntity, Service extends StringIDConfigService<T>> 
            extends StringIDConfigDefineServiceDaoController<T, Service, StringIDDao<T>> {

    private static final long serialVersionUID = -3213289396100481877L;

}
