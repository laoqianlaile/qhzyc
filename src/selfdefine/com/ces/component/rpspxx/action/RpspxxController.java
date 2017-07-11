package com.ces.component.rpspxx.action;

import com.ces.component.trace.dao.TraceShowModuleDao;
import com.ces.component.rpspxx.service.RpspxxService;
import com.ces.component.trace.action.base.TraceShowModuleDefineServiceDaoController;
import com.ces.xarch.core.entity.StringIDEntity;

public class RpspxxController extends TraceShowModuleDefineServiceDaoController<StringIDEntity, RpspxxService, TraceShowModuleDao> {

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

    public void getSpmc() {
    	super.setReturnData(getService().getSpmc(spbm));
    }
}