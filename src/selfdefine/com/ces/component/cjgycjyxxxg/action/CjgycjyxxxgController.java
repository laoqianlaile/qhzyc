package com.ces.component.cjgycjyxxxg.action;

import com.ces.component.cjgycjyxxxg.dao.CjgycjyxxxgDao;
import com.ces.component.cjgycjyxxxg.service.CjgycjyxxxgService;
import com.ces.component.trace.action.base.TraceShowModuleDefineServiceDaoController;
import com.ces.xarch.core.entity.StringIDEntity;

public class CjgycjyxxxgController extends TraceShowModuleDefineServiceDaoController<StringIDEntity, CjgycjyxxxgService, CjgycjyxxxgDao> {

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
