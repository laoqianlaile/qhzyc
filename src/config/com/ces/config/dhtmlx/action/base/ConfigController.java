package com.ces.config.dhtmlx.action.base;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.ces.config.dhtmlx.service.base.ConfigService;
import com.ces.xarch.core.entity.StringIDEntity;

/**
 * 不用复写服务层（Service）和持久层（Dao）
 * <p>描述: 系统配置平台Controller类的基类</p>
 * <p>公司: 上海中信信息发展股份有限公司</p>
 * @author qiucs   
 * @date 2013-7-12 上午10:18:02
 *
 * @param <T>
 */
public class ConfigController<T extends StringIDEntity> extends ConfigDefineServiceController<T, ConfigService<T>> {

    private static final long serialVersionUID = 861528324618876909L;

    /*
     * (非 Javadoc)   
     * <p>标题: setService</p>   
     * <p>描述: 注入服务层(Service)</p>   
     * @param service   
     * @see com.ces.xarch.core.web.struts2.BaseController#setService(com.ces.xarch.core.service.AbstractService)
     */
    @Override
    @Autowired
    @Qualifier("configService")
    protected void setService(ConfigService<T> service) {
        // TODO Auto-generated method stub
        super.setService(service);
    }

}
