package com.ces.component.zzccbzcpxx.action;

import com.ces.component.trace.dao.TraceShowModuleDao;
import com.ces.component.zzccbzcpxx.service.ZzccbzcpxxService;
import com.ces.component.trace.action.base.TraceShowModuleDefineServiceDaoController;
import com.ces.xarch.core.entity.StringIDEntity;

public class ZzccbzcpxxController extends TraceShowModuleDefineServiceDaoController<StringIDEntity, ZzccbzcpxxService, TraceShowModuleDao> {

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

    public Object getBzcpxx () {
        String traceCode = getParameter("traceCode");
        setReturnData(getService().getBzcpxx(traceCode));
        return SUCCESS;
    }

    /**
     * 包装出场直接获取包装信息
     */
    public void getBzxx(){
        String id=getParameter("BZID");
        setReturnData(getService().getBzxx(id));
    }

    public Object getBzjs(){
        String cpzsm = getParameter("cpzsm");
        setReturnData(getService().getBzjs(cpzsm));
        return SUCCESS;
    }


}