package com.ces.component.tcszzmdxx.action;

import com.ces.component.trace.dao.TraceShowModuleDao;
import com.ces.component.tcszzmdxx.service.TcszzmdxxService;
import com.ces.component.trace.action.base.TraceShowModuleDefineServiceDaoController;
import com.ces.xarch.core.entity.StringIDEntity;

public class TcszzmdxxController extends TraceShowModuleDefineServiceDaoController<StringIDEntity, TcszzmdxxService, TraceShowModuleDao> {

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
     * 获得指定客户下面的门店信息
     */
    public void searchGridData(){
        String khbh = getParameter("khbh");
        setReturnData(getService().searchMdxxComboGridData(khbh));
    }

    public void searchMdxx(){
        String id = getParameter("id");
        setReturnData(getService().searchMdxxById(id));
    }

}