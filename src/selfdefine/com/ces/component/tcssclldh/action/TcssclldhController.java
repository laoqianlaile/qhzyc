package com.ces.component.tcssclldh.action;

import com.ces.component.tcssclldh.dao.TcssclldhDao;
import com.ces.component.tcssclldh.service.TcssclldhService;
import com.ces.component.trace.action.base.TraceShowModuleDefineServiceDaoController;
import com.ces.xarch.core.entity.StringIDEntity;

public class TcssclldhController extends TraceShowModuleDefineServiceDaoController<StringIDEntity, TcssclldhService, TcssclldhDao> {

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

}
