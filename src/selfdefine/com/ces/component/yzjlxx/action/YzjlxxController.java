package com.ces.component.yzjlxx.action;

import com.ces.component.trace.utils.SerialNumberUtil;
import com.ces.component.trace.dao.TraceShowModuleDao;
import com.ces.component.yzjlxx.service.YzjlxxService;
import com.ces.component.trace.action.base.TraceShowModuleDefineServiceDaoController;
import com.ces.xarch.core.entity.StringIDEntity;

public class YzjlxxController extends TraceShowModuleDefineServiceDaoController<StringIDEntity, YzjlxxService, TraceShowModuleDao> {

    private static final long serialVersionUID = 1L;
    
    /*
     * (非 Javadoc)   
     * <p>标题: initModel</p>   
     * <p>描述: </p>      
     * @see com.ces.xarch.core.web.struts2.BaseController#initModel()
     */
    protected void initModel() {
        setModel(new StringIDEntity());
    }

    public String getYzpch(){
    	setReturnData(SerialNumberUtil.getInstance().getSerialNumber("YZ", "YZPCH", false));
    	return null;
    	
    }
    
    /**
     * 获得进栏下拉数据
     * @return
     */
    public String getJlxxGrid(){
    	String qyzt="1";
    	String pageType=getParameter("P_pageType");
        String self = getParameter("self");
    	if("grid".equals(pageType)){
    		qyzt="";
    	}
    	setReturnData(getService().getJlxx(qyzt,self));
    	return null;
    }
    
    public String getJlxxFzr(){
    	String yzpch=getRequest().getParameter("yzpch");
    	setReturnData(getService().getJlxxFzr(yzpch));
    	return null;
    }
    
    public void updateJlzt(){
    	String yzpch=getRequest().getParameter("yzpch");
    	getService().updateJlzt(yzpch);
    	setReturnData("success");
    }
    
}