package com.ces.component.cjgycjyxxxz.action;

import com.ces.component.cjgycjyxxxz.dao.CjgycjyxxxzDao;
import com.ces.component.cjgycjyxxxz.service.CjgycjyxxxzService;
import com.ces.component.trace.action.base.TraceShowModuleDefineServiceDaoController;
import com.ces.xarch.core.entity.StringIDEntity;

public class CjgycjyxxxzController extends TraceShowModuleDefineServiceDaoController<StringIDEntity, CjgycjyxxxzService, CjgycjyxxxzDao> {

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

    public Object getCSPCH(){
        String pch = getParameter("pch");
        setReturnData(getService().getCSPCH(pch));
        return SUCCESS;
    }

    public void getPchkcxx(){
        String  pch = getParameter("pch");
        setReturnData(getService().getKcxxByQypch(pch));
    }
}
