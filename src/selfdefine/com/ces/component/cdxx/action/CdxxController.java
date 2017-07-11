package com.ces.component.cdxx.action;

import com.ces.component.trace.dao.TraceShowModuleDao;
import com.ces.component.cdxx.service.CdxxService;
import com.ces.component.trace.action.base.TraceShowModuleDefineServiceDaoController;
import com.ces.xarch.core.entity.StringIDEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

public class CdxxController extends TraceShowModuleDefineServiceDaoController<StringIDEntity, CdxxService, TraceShowModuleDao> {

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

    @Override
    protected void setService(CdxxService service){
    	super.setService(service);
    }
    
    public void getCdxxGrid(){
    	this.setReturnData(getService().getCdxx());
    }

    public Object getShdqxxGrid(){
        setReturnData(getService().getShdqxxGrid());
        return SUCCESS;
    }

    /**
     * 复写产地信息弹出框
     * @return
     */

    public Object searchCandi(){
        String value=getParameter("value");
//		setReturnData(getService().searchCandi(value));
        PageRequest pageRequest = this.buildPageRequest();
        list = getDataModel(getModelTemplate());
        Page<Object> page = this.getService().searchCandi(pageRequest, value);
        list.setData(page);
        return null;

    }
}