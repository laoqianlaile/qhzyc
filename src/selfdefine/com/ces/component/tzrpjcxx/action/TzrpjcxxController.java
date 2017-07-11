package com.ces.component.tzrpjcxx.action;

import com.ces.component.tzrpjcxx.dao.TzrpjcxxDao;
import com.ces.component.tzrpjcxx.service.TzrpjcxxService;
import com.ces.component.trace.action.base.TraceShowModuleDefineServiceDaoController;
import com.ces.xarch.core.entity.StringIDEntity;

public class TzrpjcxxController extends TraceShowModuleDefineServiceDaoController<StringIDEntity, TzrpjcxxService, TzrpjcxxDao> {

    private static final long serialVersionUID = 1L;
    private String hzbm;
    private String szcdjyzh;
    
    
    public String getSzcdjyzh() {
		return szcdjyzh;
	}

	public void setSzcdjyzh(String szcdjyzh) {
		this.szcdjyzh = szcdjyzh;
	}

	public String getHzbm() {
		return hzbm;
	}

	public void setHzbm(String hzbm) {
		this.hzbm = hzbm;
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

    public void getJyzhGridByHzbm() {//根据货主编码获取检疫证号
    	super.setReturnData(getService().getJyzhGridByHzbm(hzbm));
    }

	public void getJyzGrid(){
		super.setReturnData(getService().getJyzhGrid());
	}
    
    public void getSzjyxx() {//根据生猪进场检疫证号获取检疫证进场数量
    	super.setReturnData(getService().getSzjyxx(szcdjyzh));
    }
}
