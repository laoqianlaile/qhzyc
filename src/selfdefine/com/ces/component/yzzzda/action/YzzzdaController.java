package com.ces.component.yzzzda.action;

import com.ces.component.trace.utils.SerialNumberUtil;
import com.ces.component.trace.dao.TraceShowModuleDao;
import com.ces.component.yzzzda.service.YzzzdaService;
import com.ces.component.trace.action.base.TraceShowModuleDefineServiceDaoController;
import com.ces.xarch.core.entity.StringIDEntity;

public class YzzzdaController extends TraceShowModuleDefineServiceDaoController<StringIDEntity, YzzzdaService, TraceShowModuleDao> {

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
    
    /**
     * 获得仔猪批次号的编号
     * @return
     */
    public String getZzpch(){
    	super.setReturnData(SerialNumberUtil.getInstance().getSerialNumber("YZ", "YZZZPCH", false));
    	return null;
    }
    
    /**
     * 获得仔猪批次号数据
     * 
     * @return
     */
    public String getZzdaGrid(){
    	setReturnData(getService().getZzda());    	
    	return null;
    }
    
    public String getZzdaByzzbh(){
    	String zzpch = getParameter("zzpch");
    	setReturnData(getService().getZzdaByzzbh(zzpch));
    	return null;
    }
    
    public String getSurplusSl(){
    	String zzpch =getRequest().getParameter("zzpch");
    	setReturnData(getService().getSurplusSl(zzpch));
    	return null;
    }
    

}