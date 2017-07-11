package com.ces.component.prjcxx.action;


import java.util.HashMap;
import java.util.Map;

import com.ces.component.trace.dao.TraceShowModuleDao;
import com.ces.component.prjcxx.service.PrjcxxService;
import com.ces.component.trace.utils.SerialNumberUtil;
import com.ces.component.trace.action.base.TraceShowModuleDefineServiceDaoController;
import com.ces.xarch.core.entity.StringIDEntity;

public class PrjcxxController extends TraceShowModuleDefineServiceDaoController<StringIDEntity, PrjcxxService, TraceShowModuleDao> {

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

    /**
     * 批发商下拉列表
     */
    public void getJyzdaGrid() {
    	super.setReturnData(getService().getJyzda(opponent));
    }
    public void getInitInfo(){
    	String qybm = SerialNumberUtil.getInstance().getCompanyCode();
    	Map map = new HashMap();
    	map.put("PFSCBM", qybm);
    	setReturnData(map);
    }
}