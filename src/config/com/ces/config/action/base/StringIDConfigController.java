package com.ces.config.action.base;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.ces.config.service.base.StringIDConfigService;
import com.ces.xarch.core.entity.StringIDEntity;

/**
 * 不用复写服务层（Service）和持久层（Dao）
 * <p>描述: 系统配置平台Controller类的基类</p>
 * <p>公司: 上海中信信息发展股份有限公司</p>
 * @author qiucs   
 * @date 2014-8-27 下午10:18:02
 *
 * @param <T>
 */
public class StringIDConfigController<T extends StringIDEntity> extends StringIDConfigDefineServiceController<T, StringIDConfigService<T>> {

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
    @Qualifier("stringIDConfigService")
    protected void setService(StringIDConfigService<T> service) {
        // TODO Auto-generated method stub
        super.setService(service);
    }

}
