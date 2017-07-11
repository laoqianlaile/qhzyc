package com.ces.component.scqyda.action;

import com.ces.component.trace.dao.TraceShowModuleDao;
import com.ces.component.scqyda.service.ScqydaService;
import com.ces.component.trace.utils.CompanyInfoUtil;
import com.ces.component.trace.utils.SerialNumberUtil;
import com.ces.component.trace.action.base.TraceShowModuleDefineServiceDaoController;
import com.ces.xarch.core.entity.StringIDEntity;

public class ScqydaController extends TraceShowModuleDefineServiceDaoController<StringIDEntity, ScqydaService, TraceShowModuleDao> {

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
    public String getQybm(){
        setReturnData(getService().qybm());
        return null;
    }
    public String getQymc(){
        setReturnData(getService().qymc());
        return null;
    }
    public String getQyda(){
        setReturnData(getService().getQyda());
        return null;
    }
}