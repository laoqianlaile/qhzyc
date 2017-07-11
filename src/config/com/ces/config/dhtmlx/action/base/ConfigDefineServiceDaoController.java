package com.ces.config.dhtmlx.action.base;

import java.io.Serializable;
import org.apache.struts2.ServletActionContext;

import com.ces.config.action.base.StringIDConfigDefineServiceDaoController;
import com.ces.config.dhtmlx.service.base.ConfigDefineDaoService;
import com.ces.xarch.core.entity.BaseEntity;
import com.ces.xarch.core.entity.StringIDEntity;
import com.ces.xarch.core.persistence.jpa.StringIDDao;
import com.ces.xarch.core.web.frame.FrameDataModel;
import com.ces.xarch.core.web.frame.utils.FrameDataModuleHandling;

/**
 * 自定义服务层（Service）和持久层（Dao）
 * <p>描述: 系统配置平台Controller类的基类</p>
 * <p>公司: 上海中信信息发展股份有限公司</p>
 * @author qiucs   
 * @date 2013-7-12 上午10:18:02
 *
 * @param <T>
 * @param <Service>
 * @param <Dao>
 */
public class ConfigDefineServiceDaoController<T extends StringIDEntity, Service extends ConfigDefineDaoService<T, Dao>, Dao extends StringIDDao<T>>
        extends StringIDConfigDefineServiceDaoController<T, Service, Dao> {
    
    private static final long serialVersionUID = -5883166752449385219L;
    
    private boolean status = true;
    
    private String message ;

    /* (non-Javadoc)
     * @see com.ces.xarch.core.web.struts2.BaseController#getDataModel(java.lang.Class, java.io.Serializable, java.lang.String)
     * @date 2014-07-03 21:28:42
     */
    @Override
    public <OID extends Serializable, OT extends BaseEntity<OID>> FrameDataModel<OT, OID> getDataModel(Class<OT> otClass, OID oid, String modelName) {
        ServletActionContext.getRequest().setAttribute(FrameDataModuleHandling.FRAME_PARAM, "dhtmlx");
        
        return super.getDataModel(otClass, oid, modelName);
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
    
}
