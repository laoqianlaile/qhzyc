package com.ces.component.cyqyda.action;

import com.ces.component.cyqyda.dao.CyqydaDao;
import com.ces.component.cyqyda.service.CyqydaService;
import com.ces.component.trace.utils.SerialNumberUtil;
import com.ces.component.trace.action.base.TraceShowModuleDefineServiceDaoController;
import com.ces.xarch.core.entity.StringIDEntity;

public class CyqydaController extends TraceShowModuleDefineServiceDaoController<StringIDEntity, CyqydaService, CyqydaDao> {

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
    
    public Object getIdByQybm(){
    	String qyId = getService().getIdByQybm();
    	setReturnData(qyId);
    	return null;
    }
    public String getQybm(){
    	setReturnData(SerialNumberUtil.getInstance().getCompanyCode());
    	return null;
    }
}
