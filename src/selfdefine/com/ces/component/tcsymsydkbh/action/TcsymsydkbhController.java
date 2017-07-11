package com.ces.component.tcsymsydkbh.action;

import com.ces.component.tcsymsydkbh.dao.TcsymsydkbhDao;
import com.ces.component.tcsymsydkbh.service.TcsymsydkbhService;
import com.ces.component.trace.action.base.TraceShowModuleDefineServiceDaoController;
import com.ces.xarch.core.entity.StringIDEntity;

public class TcsymsydkbhController extends TraceShowModuleDefineServiceDaoController<StringIDEntity, TcsymsydkbhService, TcsymsydkbhDao> {

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
