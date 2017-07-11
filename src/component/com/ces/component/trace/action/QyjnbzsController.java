package com.ces.component.trace.action;

import com.ces.component.trace.action.base.TraceShowModuleDefineServiceDaoController;
import com.ces.component.trace.dao.QyjnbzsDao;
import com.ces.component.trace.service.QyjnbzsService;
import com.ces.xarch.core.entity.StringIDEntity;

/**
 * Created by hpsgt on 2016-11-17.
 */
public class QyjnbzsController extends TraceShowModuleDefineServiceDaoController<StringIDEntity, QyjnbzsService, QyjnbzsDao> {

    @Override
    protected void initModel() {
        setModel(new StringIDEntity());
    }

    public void searchYpsczsxx(){
        String id = getParameter("id");
        setReturnData(getService().searchYpsczsxx(id));
    }




}
