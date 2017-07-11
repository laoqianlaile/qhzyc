package com.ces.component.tzgzryda.action;

import com.ces.component.trace.utils.SerialNumberUtil;
import com.ces.component.trace.dao.TraceShowModuleDao;
import com.ces.component.tzgzryda.service.TzgzrydaService;
import com.ces.component.trace.action.base.TraceShowModuleDefineServiceDaoController;
import com.ces.xarch.core.entity.StringIDEntity;

public class TzgzrydaController extends TraceShowModuleDefineServiceDaoController<StringIDEntity, TzgzrydaService, TraceShowModuleDao> {

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

    public void getGzrybh(){
    	String zl = this.getRequest().getParameter("Zl");
    	super.setReturnData(SerialNumberUtil.getInstance().getSerialNumber("TZ",zl,false));
    }
}