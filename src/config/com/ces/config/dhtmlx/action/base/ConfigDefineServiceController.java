package com.ces.config.dhtmlx.action.base;

import com.ces.config.dhtmlx.service.base.ConfigService;
import com.ces.xarch.core.entity.StringIDEntity;
import com.ces.xarch.core.persistence.jpa.StringIDDao;
/**
 * 自定义服务层（Service），但不用复写持久层（Dao）
 * <p>描述: 系统配置平台Controller类的基类</p>
 * <p>公司: 上海中信信息发展股份有限公司</p>
 * @author qiucs   
 * @date 2013-7-12 上午10:18:02
 *
 * @param <T>
 * @param <Service>
 */
public class ConfigDefineServiceController<T extends StringIDEntity, Service extends ConfigService<T>> extends ConfigDefineServiceDaoController<T, Service, StringIDDao<T>> {

    private static final long serialVersionUID = -3213289396100481877L;

}
