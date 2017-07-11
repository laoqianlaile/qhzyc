package com.ces.component.hqfxjjgypcjj.action;

import com.ces.component.hqfxjjgypcjj.dao.HqfxjjgypcjjDao;
import com.ces.component.hqfxjjgypcjj.service.HqfxjjgypcjjService;
import com.ces.component.trace.action.base.TraceShowModuleDefineServiceDaoController;
import com.ces.xarch.core.entity.StringIDEntity;

import java.util.List;


public class HqfxjjgypcjjController extends TraceShowModuleDefineServiceDaoController<StringIDEntity, HqfxjjgypcjjService, HqfxjjgypcjjDao> {

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
        String ypmc = getParameter("ypmc");
        List<String> dateAxis = getDate();
        this.setReturnData(getService().serachjson(kssj,jssj,ypmc,rqlx,dateAxis));
    }

    public void searchypmc(){
        this.setReturnData(getService().serachyp());
    }

    public List<String> getDate(){
        String rqlx = getParameter("rqlx");
        String kssj = getParameter("kssj");
        String jssj = getParameter("jssj");

        return getService().getContinuousDate(rqlx,kssj,jssj);
    }
}
