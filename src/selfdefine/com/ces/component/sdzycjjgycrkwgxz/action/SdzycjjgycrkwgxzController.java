package com.ces.component.sdzycjjgycrkwgxz.action;

import com.ces.component.sdzycjjgycrkwgxz.dao.SdzycjjgycrkwgxzDao;
import com.ces.component.sdzycjjgycrkwgxz.service.SdzycjjgycrkwgxzService;
import com.ces.component.trace.action.base.TraceShowModuleDefineServiceDaoController;
import com.ces.xarch.core.entity.StringIDEntity;

public class SdzycjjgycrkwgxzController extends TraceShowModuleDefineServiceDaoController<StringIDEntity, SdzycjjgycrkwgxzService, SdzycjjgycrkwgxzDao> {

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

    public void searchGridData(){
        setReturnData(getService().searchycmcComboGridData());
    }
}
