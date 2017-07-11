package com.ces.component.tcstzjyz.action;

import com.ces.component.trace.dao.TraceShowModuleDao;
import com.ces.component.tcstzjyz.service.TcstzjyzService;
import com.ces.component.trace.action.base.TraceShowModuleDefineServiceDaoController;
import com.ces.xarch.core.entity.StringIDEntity;

public class TcstzjyzController extends TraceShowModuleDefineServiceDaoController<StringIDEntity, TcstzjyzService, TraceShowModuleDao> {

    private static final long serialVersionUID = 1L;
    private String opponent;
    
    
    public String getOpponent() {
		return opponent;
	}

	public void setOpponent(String opponent) {
		this.opponent = opponent;
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
    
    public void getJyzdaGrid(){
		super.setReturnData(getService().getJyzda(opponent));
	}
}