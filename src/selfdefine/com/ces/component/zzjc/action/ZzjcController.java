package com.ces.component.zzjc.action;

import com.ces.component.trace.dao.TraceShowModuleDao;
import com.ces.component.zzjc.service.ZzjcService;
import com.ces.component.trace.action.base.TraceShowModuleDefineServiceDaoController;
import com.ces.xarch.core.entity.StringIDEntity;

public class ZzjcController extends TraceShowModuleDefineServiceDaoController<StringIDEntity, ZzjcService, TraceShowModuleDao> {

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

    public Object deleteJc() {
        String dataId = getParameter("dataId");
        setReturnData(getService().deleteJc(dataId));
        return SUCCESS;
    }

    //删除检测投入品
    public Object deleteJcTrp() {
        String dataId = getParameter("dataId");
        setReturnData(getService().deleteJcTrp(dataId));
        return SUCCESS;
    }
}