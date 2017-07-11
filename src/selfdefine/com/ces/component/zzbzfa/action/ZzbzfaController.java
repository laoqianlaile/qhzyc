package com.ces.component.zzbzfa.action;

import com.ces.component.trace.dao.TraceShowModuleDao;
import com.ces.component.zzbzfa.service.ZzbzfaService;
import com.ces.component.trace.action.base.TraceShowModuleDefineServiceDaoController;
import com.ces.xarch.core.entity.StringIDEntity;

public class ZzbzfaController extends TraceShowModuleDefineServiceDaoController<StringIDEntity, ZzbzfaService, TraceShowModuleDao> {

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

    public Object deleteBzfa() {
        String dataId = getParameter("dataId");
        setReturnData(getService().deleteBzfa(dataId));
        return SUCCESS;
    }

    //获取投入品信息
    public Object getTrpxx () {
        String lx = getParameter("lx");
        setReturnData(getService().getTrpxx(lx));
        return SUCCESS;
    }

    //获取通用名信息
    public Object getTym() {
        setReturnData(getService().getTym());
        return SUCCESS;
    }
    //删除播种投入品信息
    public Object deleteBzfaTrp () {
        String dataId = getParameter("dataId");
        setReturnData(getService().deleteBzfaTrp(dataId));
        return SUCCESS;
    }

}