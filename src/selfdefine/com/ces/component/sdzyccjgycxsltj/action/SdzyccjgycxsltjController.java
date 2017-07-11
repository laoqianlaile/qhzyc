package com.ces.component.sdzyccjgycxsltj.action;

import com.ces.component.sdzyccjgycxsltj.dao.SdzyccjgycxsltjDao;
import com.ces.component.sdzyccjgycxsltj.service.SdzyccjgycxsltjService;
import com.ces.component.trace.action.base.TraceShowModuleDefineServiceDaoController;
import com.ces.xarch.core.entity.StringIDEntity;


public class SdzyccjgycxsltjController extends TraceShowModuleDefineServiceDaoController<StringIDEntity, SdzyccjgycxsltjService, SdzyccjgycxsltjDao> {

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

    public void searchj() {
        String kssj = getParameter("kssj");
        String jssj = getParameter("jssj");
        String rqlx = getParameter("rqlx");
        String cpmc = getParameter("cpmc");
        this.setReturnData(getService().serachjson(kssj, jssj, rqlx,cpmc));
    }
}

   /* public void searchypmc(){
        this.setReturnData(getService().serachyp());
    }

    public List<String> getDate(){
        String rqlx = getParameter("rqlx");
        String kssj = getParameter("kssj");
        String jssj = getParameter("jssj");

        return getService().getContinuousDate(rqlx,kssj,jssj);
    }
}*/
