package com.ces.component.hqfxzzyccsl.action;

import com.ces.component.hqfxzzyccsl.dao.HqfxzzyccslDao;
import com.ces.component.hqfxzzyccsl.service.HqfxzzyccslService;
import com.ces.component.trace.action.base.TraceShowModuleDefineServiceDaoController;
import com.ces.xarch.core.entity.StringIDEntity;


public class HqfxzzyccslController extends TraceShowModuleDefineServiceDaoController<StringIDEntity, HqfxzzyccslService, HqfxzzyccslDao> {

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
        String ycmc = getParameter("ycmc");
        String ckqy = getParameter("ckqy");
        this.setReturnData(getService().serachjson(kssj,jssj,rqlx,ycmc,ckqy));
    }

    public void searchycmc(){
        this.setReturnData(getService().serachyc());
    }

    public void searchqymc(){
        this.setReturnData(getService().serachqy());
    }
}
