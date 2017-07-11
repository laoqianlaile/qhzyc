package com.ces.component.scscspxx.action;

import com.ces.component.trace.dao.TraceShowModuleDao;
import com.ces.component.scscspxx.service.ScscspxxService;
import com.ces.component.trace.action.base.TraceShowModuleDefineServiceDaoController;
import com.ces.xarch.core.entity.StringIDEntity;

public class ScscspxxController extends TraceShowModuleDefineServiceDaoController<StringIDEntity, ScscspxxService, TraceShowModuleDao> {

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
    public void getspxxGrid(){
        setReturnData(getService().getspxxGrid());
    }
    //判断店铺编号是否重复
    public void getCheckspbh(){
        String spbh = getParameter("spbh");
        setReturnData(getService().getCheckspbh(spbh));
    }
}