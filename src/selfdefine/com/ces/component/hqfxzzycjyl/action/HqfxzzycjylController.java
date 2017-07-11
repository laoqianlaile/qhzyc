package com.ces.component.hqfxzzycjyl.action;

import com.ces.component.hqfxzzycjyl.dao.HqfxzzycjylDao;
import com.ces.component.hqfxzzycjyl.service.HqfxzzycjylService;
import com.ces.component.trace.action.base.TraceShowModuleDefineServiceDaoController;
import com.ces.xarch.core.entity.StringIDEntity;


public class HqfxzzycjylController extends TraceShowModuleDefineServiceDaoController<StringIDEntity, HqfxzzycjylService, HqfxzzycjylDao> {

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

    public void searchj(){
        String kssj = getParameter("kssj");
        String jssj = getParameter("jssj");
        String rqlx = getParameter("rqlx");
        this.setReturnData(getService().serachjson(kssj,jssj,rqlx));
    }
}
