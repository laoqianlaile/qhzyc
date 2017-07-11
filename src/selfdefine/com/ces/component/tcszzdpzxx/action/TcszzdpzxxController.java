package com.ces.component.tcszzdpzxx.action;

import com.ces.component.trace.dao.TraceShowModuleDao;
import com.ces.component.tcszzdpzxx.service.TcszzdpzxxService;
import com.ces.component.trace.action.base.TraceShowModuleDefineServiceDaoController;
import com.ces.xarch.core.entity.StringIDEntity;

public class TcszzdpzxxController extends TraceShowModuleDefineServiceDaoController<StringIDEntity, TcszzdpzxxService, TraceShowModuleDao> {

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
     * 获取下拉列表数据
     */
    public void getGrid(){
        setReturnData(getService().getGrid());
    }

}