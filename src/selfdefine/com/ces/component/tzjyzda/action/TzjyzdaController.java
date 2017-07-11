package com.ces.component.tzjyzda.action;

import com.ces.component.trace.utils.SerialNumberUtil;
import com.ces.component.tzjyzda.dao.TzjyzdaDao;
import com.ces.component.tzjyzda.service.TzjyzdaService;
import com.ces.component.trace.action.base.TraceShowModuleDefineServiceDaoController;
import com.ces.xarch.core.entity.StringIDEntity;

public class TzjyzdaController extends TraceShowModuleDefineServiceDaoController<StringIDEntity, TzjyzdaService, TzjyzdaDao> {

    private static final long serialVersionUID = 1L;
    private String jyzbh;
    
    public String getJyzbh() {
		return jyzbh;
	}

	public void setJyzbh(String jyzbh) {
		this.jyzbh = jyzbh;
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

    public void getJyzbm(){//获取经营者流水号
    	String zl = this.getRequest().getParameter("Zl");
    	super.setReturnData(SerialNumberUtil.getInstance().getSerialNumber("TZ",zl,true));
    }
    
    public void getJyzda(){//获取经营者下拉列表数据
    	super.setReturnData(getService().getJyzda(jyzbh));
    }
    
    public void getDddByJyzbh(){//根据经营者编码获取商品到达地
    	super.setReturnData(getService().getDddByJyzbh(jyzbh));
    }
}
