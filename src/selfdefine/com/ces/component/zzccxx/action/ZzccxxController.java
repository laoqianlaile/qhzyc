package com.ces.component.zzccxx.action;

import com.ces.component.trace.utils.SerialNumberUtil;
import com.ces.component.trace.dao.TraceShowModuleDao;
import com.ces.component.zzccxx.service.ZzccxxService;
import com.ces.component.trace.action.base.TraceShowModuleDefineServiceDaoController;
import com.ces.xarch.core.entity.StringIDEntity;

public class ZzccxxController extends TraceShowModuleDefineServiceDaoController<StringIDEntity, ZzccxxService, TraceShowModuleDao> {

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
    public String getCcpch(){
    	setReturnData(SerialNumberUtil.getInstance().getSerialNumber("ZZ", "CCPCH", true));
    	return null;
    }
    public String getCcbm(){
    	setReturnData(SerialNumberUtil.getInstance().getSerialNumber("ZZ", "CCBM", false));
    	return null;
    }
    public String getTm(){
    	setReturnData(SerialNumberUtil.getInstance().getSerialNumber("ZZ", "ZZTM", true));
    	return null;
    }
    public String getZsm(){
    	setReturnData(SerialNumberUtil.getInstance().getSerialNumber("ZZ", "ZZZSM", true));
    	return null;
    }
}