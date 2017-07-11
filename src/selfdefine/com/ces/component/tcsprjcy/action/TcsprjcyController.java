package com.ces.component.tcsprjcy.action;

import com.ces.component.trace.dao.TraceShowModuleDao;
import com.ces.component.tcsprjcy.service.TcsprjcyService;
import com.ces.component.trace.action.base.TraceShowModuleDefineServiceDaoController;
import com.ces.xarch.core.entity.StringIDEntity;

public class TcsprjcyController extends TraceShowModuleDefineServiceDaoController<StringIDEntity, TcsprjcyService, TraceShowModuleDao> {

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
     * 获取弹出式检测员信息
     */
    public void getJcyxxGrid(){
    	setReturnData(this.getService().getJcyxx());
    }

}