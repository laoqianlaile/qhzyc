package com.ces.component.zzzzda.action;

import com.ces.component.trace.utils.SerialNumberUtil;
import com.ces.component.trace.dao.TraceShowModuleDao;
import com.ces.component.zzzzda.service.ZzzzdaService;
import com.ces.component.trace.action.base.TraceShowModuleDefineServiceDaoController;
import com.ces.xarch.core.entity.StringIDEntity;

public class ZzzzdaController extends TraceShowModuleDefineServiceDaoController<StringIDEntity, ZzzzdaService, TraceShowModuleDao> {

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
    
    public void getCzbh(){
    	super.setReturnData(SerialNumberUtil.getInstance().getSerialNumber("ZZ", "CZBH", false));
    }

    
    public void getCzdaGrid(){
    	this.setReturnData(getService().getCzda());
    	
    }
    
    public void getCzdaObj(){
    	String czbh= getRequest().getParameter("czbh");
    	setReturnData(getService().getCzdaObj(czbh));
    }
}