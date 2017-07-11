package com.ces.component.zzctda.action;

import ces.coral.upload.Request;

import com.ces.component.trace.utils.SerialNumberUtil;
import com.ces.component.trace.dao.TraceShowModuleDao;
import com.ces.component.zzctda.service.ZzctdaService;
import com.ces.component.trace.action.base.TraceShowModuleDefineServiceDaoController;
import com.ces.xarch.core.entity.StringIDEntity;

public class ZzctdaController extends TraceShowModuleDefineServiceDaoController<StringIDEntity, ZzctdaService, TraceShowModuleDao> {

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
    public String getCtbh(){
    	super.setReturnData(SerialNumberUtil.getInstance().getSerialNumber("ZZ", "CTBH", false));
    	return null;
    }
    
    public String updCtzt(){
    	//获得操作的菜田编号
    	String ctbh=this.getRequest().getParameter("ctbh");
    	//获得种植状态 从而进行控制菜田的状态改变
    	String zzzt=this.getRequest().getParameter("zzzt");
    	
    	getService().updCtzzzt(ctbh,zzzt);
    	setReturnData("success");
    	return null;
    }
    
    public void getCtdaGrid(){
    	this.setReturnData(getService().getCtda());
    }
    
    
    public String  getCtdaFzr(){
    	String ctbh = getRequest().getParameter("ctbh");
    	setReturnData(getService().getCtdaFzr(ctbh));
    	return null;
    }
    

}