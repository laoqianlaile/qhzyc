package com.ces.component.yzzsda.action;

import com.ces.component.trace.utils.SerialNumberUtil;
import com.ces.component.trace.dao.TraceShowModuleDao;
import com.ces.component.yzzsda.service.YzzsdaService;
import com.ces.component.trace.action.base.TraceShowModuleDefineServiceDaoController;
import com.ces.xarch.core.entity.StringIDEntity;

public class YzzsdaController extends TraceShowModuleDefineServiceDaoController<StringIDEntity, YzzsdaService, TraceShowModuleDao> {

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

    public String getZsbh(){
    	super.setReturnData(SerialNumberUtil.getInstance().getSerialNumber("YZ", "ZSBH", false));
    	return null;
    }
    
    public String getZsdaGrid(){
    	String pageType = getParameter("P_pageType");
    	String syzt = getRequest().getParameter("syzt");
        String self = getRequest().getParameter("self");
    	if(!"grid".equals(pageType))
    		syzt="2";
    	setReturnData(getService().getZsda("1",syzt,self));
    	return null;
    }
    
    public void checkOnlybh(){
    	String csbh = getRequest().getParameter("csbh");
    	setReturnData(getService().checkOnlybh(csbh));
    }
    
    public void getCsdaFzr(){
    	String csbh = getRequest().getParameter("csbh");
    	setReturnData(getService().getZsdaFzr(csbh));
    }
    
    public void updataSyzt(){
    	String zsbh = getRequest().getParameter("zsbh");
    	// 1 为使用状态 2为空闲状态
    	getService().updataSyzt(zsbh, "1");
    	setReturnData("success");    	
    }
}