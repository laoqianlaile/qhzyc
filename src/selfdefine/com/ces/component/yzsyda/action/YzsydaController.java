package com.ces.component.yzsyda.action;

import com.ces.component.trace.utils.SerialNumberUtil;
import com.ces.component.trace.dao.TraceShowModuleDao;
import com.ces.component.yzsyda.service.YzsydaService;
import com.ces.component.trace.action.base.TraceShowModuleDefineServiceDaoController;
import com.ces.xarch.core.entity.StringIDEntity;

public class YzsydaController extends TraceShowModuleDefineServiceDaoController<StringIDEntity, YzsydaService, TraceShowModuleDao> {

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
    public String getSybh(){
    	super.setReturnData(SerialNumberUtil.getInstance().getSerialNumber("YZ", "SYBH", false));
    	return null;
    }
    
    public void getSydaGrid(){
    	setReturnData(getService().getSyda());
    }
    
    public void getSydaXyq(){
    	String sybh=getRequest().getParameter("sybh");
    	setReturnData(getService().getSydaXyq(sybh));
    }

}