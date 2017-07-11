package com.ces.component.ttqyda.action;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.ces.component.csqyda.entity.CsqydaEntity;
import com.ces.component.csqyda.service.CsqydaService;
import com.ces.component.trace.utils.SerialNumberUtil;
import com.ces.component.ttqyda.dao.TtqydaDao;
import com.ces.component.ttqyda.service.TtqydaService;
import com.ces.component.trace.action.base.TraceShowModuleDefineServiceDaoController;
import com.ces.xarch.core.entity.StringIDEntity;

public class TtqydaController extends TraceShowModuleDefineServiceDaoController<StringIDEntity, TtqydaService, TtqydaDao> {

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
    
    @Autowired
    @Qualifier("ttqydaService")
    @Override
    protected void setService(TtqydaService service) {
        super.setService(service);
    }
    
    public Object getIdByQybm(){
    	String qyId = getService().getIdByQybm();
    	setReturnData(qyId);
    	return null;
    }

}
