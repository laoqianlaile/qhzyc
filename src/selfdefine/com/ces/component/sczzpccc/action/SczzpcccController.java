package com.ces.component.sczzpccc.action;

import com.ces.component.trace.dao.TraceShowModuleDao;
import com.ces.component.sczzpccc.service.SczzpcccService;
import com.ces.component.trace.action.base.TraceShowModuleDefineServiceDaoController;
import com.ces.xarch.core.entity.StringIDEntity;

public class SczzpcccController extends TraceShowModuleDefineServiceDaoController<StringIDEntity, SczzpcccService, TraceShowModuleDao> {

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


    public Object getPrint(){
        String id = getParameter("id");
        setReturnData(getService().getPrint(id));
        return SUCCESS;
    }

    public Object queryDetails(){
        String id = getParameter("id");
        setReturnData(getService().queryDetails(id));
        return SUCCESS;
    }

    public Object getPsdzByDdbh(){
        String ddbh = getParameter("ddbh");
        setReturnData(getService().getPsdzByDdbh(ddbh));
        return SUCCESS;
    }



}