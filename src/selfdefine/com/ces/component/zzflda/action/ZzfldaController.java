package com.ces.component.zzflda.action;

import com.ces.component.trace.utils.SerialNumberUtil;
import com.ces.component.trace.dao.TraceShowModuleDao;
import com.ces.component.zzflda.service.ZzfldaService;
import com.ces.component.trace.action.base.TraceShowModuleDefineServiceDaoController;
import com.ces.xarch.core.entity.StringIDEntity;

public class ZzfldaController extends TraceShowModuleDefineServiceDaoController<StringIDEntity, ZzfldaService, TraceShowModuleDao> {

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
    
    public String getFlbh(){
    	super.setReturnData(SerialNumberUtil.getInstance().getSerialNumber("ZZ", "FLBH", false));
    	return null;
    }
    
    public void getFldaGrid(){
    	setReturnData(getService().getFlda());
    }

    
}