package com.ces.component.tcszzxpzxx.action;

import com.ces.component.trace.dao.TraceShowModuleDao;
import com.ces.component.tcszzxpzxx.service.TcszzxpzxxService;
import com.ces.component.trace.action.base.TraceShowModuleDefineServiceDaoController;
import com.ces.xarch.core.entity.StringIDEntity;

public class TcszzxpzxxController extends TraceShowModuleDefineServiceDaoController<StringIDEntity, TcszzxpzxxService, TraceShowModuleDao> {

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
        String plbh = getParameter("plbh");
        setReturnData(getService().getGrid(plbh));
    }
}