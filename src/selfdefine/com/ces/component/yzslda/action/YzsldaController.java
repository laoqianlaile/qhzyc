package com.ces.component.yzslda.action;

import com.ces.component.trace.utils.SerialNumberUtil;
import com.ces.component.trace.dao.TraceShowModuleDao;
import com.ces.component.yzslda.service.YzsldaService;
import com.ces.component.trace.action.base.TraceShowModuleDefineServiceDaoController;
import com.ces.xarch.core.entity.StringIDEntity;

public class YzsldaController extends TraceShowModuleDefineServiceDaoController<StringIDEntity, YzsldaService, TraceShowModuleDao> {

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
    public String getSlbh(){
    	super.setReturnData(SerialNumberUtil.getInstance().getSerialNumber("YZ", "YZSLBH", false));
    	return null;
    }
    
    public  void getSldaGrid(){
        String self = getParameter("self");
    	setReturnData(getService().getSlda(self));
    }

}