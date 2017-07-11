package com.ces.component.jjgscllxxxq.action;

import com.ces.component.jjgscllxxxq.dao.JjgscllxxxqDao;
import com.ces.component.jjgscllxxxq.service.JjgscllxxxqService;
import com.ces.component.trace.action.base.TraceShowModuleDefineServiceDaoController;
import com.ces.xarch.core.entity.StringIDEntity;

public class JjgscllxxxqController extends TraceShowModuleDefineServiceDaoController<StringIDEntity, JjgscllxxxqService, JjgscllxxxqDao> {

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
