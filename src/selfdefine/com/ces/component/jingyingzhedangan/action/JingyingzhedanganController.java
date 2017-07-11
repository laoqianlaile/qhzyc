package com.ces.component.jingyingzhedangan.action;

import com.ces.component.trace.dao.TraceShowModuleDao;
import com.ces.component.jingyingzhedangan.service.JingyingzhedanganService;
import com.ces.component.trace.utils.SerialNumberUtil;
import com.ces.component.trace.action.base.TraceShowModuleDefineServiceDaoController;
import com.ces.config.utils.CommonUtil;
import com.ces.xarch.core.entity.StringIDEntity;
import com.ces.xarch.core.security.entity.SysUser;

public class JingyingzhedanganController extends TraceShowModuleDefineServiceDaoController<StringIDEntity, JingyingzhedanganService, TraceShowModuleDao> {

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
    public String getUserName(){
    	
    	SysUser user=CommonUtil.getUser();
    	super.setReturnData(user);
    	return null;
    }
    
    public String getMaxId(){
    	return null;
    }
    
    public void getJyzbm(){
    	String zl = this.getRequest().getParameter("Zl");
    	super.setReturnData(SerialNumberUtil.getInstance().getSerialNumber("PC",zl,true));
    }
    
    public void getJyzda(){
    	super.setReturnData(getService().getJyzda(jyzbh));
    }
    
    public void getFilterJyzda(){
    	String jyzbm=getRequest().getParameter("jyzbm");
    	setReturnData(getService().getFilterJyzda(jyzbm));
    	
    }
}