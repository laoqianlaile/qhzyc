package com.ces.component.tcsjclhpc.action;

import com.ces.component.trace.dao.TraceShowModuleDao;
import com.ces.component.tcsjclhpc.service.TcsjclhpcService;
import com.ces.component.trace.action.base.TraceShowModuleDefineServiceDaoController;
import com.ces.xarch.core.entity.StringIDEntity;

public class TcsjclhpcController extends TraceShowModuleDefineServiceDaoController<StringIDEntity, TcsjclhpcService, TraceShowModuleDao> {

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

    //获取进场理货编码
    public void getJclhpc(){
    	super.setReturnData(getService().getJclhpc());
    }
}