package com.ces.component.zzickymxq.action;

import com.ces.component.trace.dao.TraceShowModuleDao;
import com.ces.component.zzickymxq.service.ZzickymxqService;
import com.ces.component.trace.action.base.TraceShowModuleDefineServiceDaoController;
import com.ces.xarch.core.entity.StringIDEntity;

public class ZzickymxqController extends TraceShowModuleDefineServiceDaoController<StringIDEntity, ZzickymxqService, TraceShowModuleDao> {

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
    public void initForm(){
        String id=getParameter("id");
        setReturnData(getService().initForm(id));

    }

}