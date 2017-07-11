package com.ces.component.cpfl.action;

import com.ces.component.cpfl.dao.CpflDao;
import com.ces.component.cpfl.service.CpflService;
import com.ces.component.trace.action.base.TraceShowModuleDefineServiceDaoController;
import com.ces.xarch.core.entity.StringIDEntity;

import java.util.HashMap;
import java.util.Map;

public class CpflController extends TraceShowModuleDefineServiceDaoController<StringIDEntity, CpflService, CpflDao> {

    private static final long serialVersionUID = 1L;
    private String spbm;
    
    
	public String getSpbm() {
		return spbm;
	}

	public void setSpbm(String spbm) {
		this.spbm = spbm;
	}

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

    @Override
    protected void setService(CpflService service){
    	super.setService(service);
    }
    
    public void getCpflGrid(){
    	this.setReturnData(getService().getCpfl());
    }
    
    public void getSpmc(){
    	this.setReturnData(getService().getSpmc(spbm));
    }
}
