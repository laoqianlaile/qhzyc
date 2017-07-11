package com.ces.component.jiaoyixinxi.action;

import com.ces.component.trace.dao.TraceShowModuleDao;
import com.ces.component.jiaoyixinxi.service.JiaoyixinxiService;
import com.ces.component.trace.action.base.TraceShowModuleDefineServiceDaoController;
import com.ces.xarch.core.entity.StringIDEntity;

public class JiaoyixinxiController extends TraceShowModuleDefineServiceDaoController<StringIDEntity, JiaoyixinxiService, TraceShowModuleDao> {

    private static final long serialVersionUID = 1L;
    
    /*
     * (非 Javadoc)   
     * <p>标题: initModel</p>   
     * <p>描述: </p>      
     * @see com.ces.xarch.core.web.struts2.BaseController#initModel()
     */
    @Override
    protected void initModel() {
        setModel(new StringIDEntity());
    }

}