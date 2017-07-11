package com.ces.component.qyptzhgl.action;

import com.ces.component.trace.dao.TraceShowModuleDao;
import com.ces.component.qyptzhgl.service.QyptzhglService;
import com.ces.component.trace.action.base.TraceShowModuleDefineServiceDaoController;
import com.ces.xarch.core.entity.StringIDEntity;

public class QyptzhglController extends TraceShowModuleDefineServiceDaoController<StringIDEntity, QyptzhglService, TraceShowModuleDao> {

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

    public Object getZhbhByUUID() {
        String uuid = getParameter("uuid");
        setReturnData(getService().getZhbhByUUID(uuid));
        return SUCCESS;
    }

}