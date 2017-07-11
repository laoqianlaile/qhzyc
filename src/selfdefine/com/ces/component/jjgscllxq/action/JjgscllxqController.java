package com.ces.component.jjgscllxq.action;

import com.ces.component.jjgscllxq.dao.JjgscllxqDao;
import com.ces.component.jjgscllxq.service.JjgscllxqService;
import com.ces.component.trace.action.base.TraceShowModuleDefineServiceDaoController;
import com.ces.xarch.core.entity.StringIDEntity;

public class JjgscllxqController extends TraceShowModuleDefineServiceDaoController<StringIDEntity, JjgscllxqService, JjgscllxqDao> {

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
