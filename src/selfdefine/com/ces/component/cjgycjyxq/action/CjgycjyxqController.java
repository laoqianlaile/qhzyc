package com.ces.component.cjgycjyxq.action;

import com.ces.component.cjgycjyxq.dao.CjgycjyxqDao;
import com.ces.component.cjgycjyxq.service.CjgycjyxqService;
import com.ces.component.trace.action.base.TraceShowModuleDefineServiceDaoController;
import com.ces.xarch.core.entity.StringIDEntity;

public class CjgycjyxqController extends TraceShowModuleDefineServiceDaoController<StringIDEntity, CjgycjyxqService, CjgycjyxqDao> {

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
