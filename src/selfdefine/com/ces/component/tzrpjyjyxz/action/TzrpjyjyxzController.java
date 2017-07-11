package com.ces.component.tzrpjyjyxz.action;

import com.ces.component.tzrpjyjyxz.dao.TzrpjyjyxzDao;
import com.ces.component.tzrpjyjyxz.service.TzrpjyjyxzService;
import com.ces.component.trace.action.base.TraceShowModuleDefineServiceDaoController;
import com.ces.xarch.core.entity.StringIDEntity;

public class TzrpjyjyxzController extends TraceShowModuleDefineServiceDaoController<StringIDEntity, TzrpjyjyxzService, TzrpjyjyxzDao> {

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

    //根据货主编码获取检疫证号下拉列表数据
    public void getJyzhGridByHzbm(){
    	super.setReturnData(getService().getJyzhGridByHzbm(hzbm));
    }
    
    //获取货主编码、货主名称和实际进场数量
    public void getSzjyxx(){
    	super.setReturnData(getService().getSzjyxx(szcdjyzh));
    }
    
    //更改生猪检疫检疫状态
    public void setJyzt(){
    	String jyzl = this.getRequest().getParameter("Jyzh");
    	getService().setJyzt(jyzl);
    }
    
}
