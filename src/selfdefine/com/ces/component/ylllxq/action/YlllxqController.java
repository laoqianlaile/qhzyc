package com.ces.component.ylllxq.action;

import com.ces.component.ylllxq.dao.YlllxqDao;
import com.ces.component.ylllxq.service.YlllxqService;
import com.ces.component.trace.action.base.TraceShowModuleDefineServiceDaoController;
import com.ces.xarch.core.entity.StringIDEntity;

public class YlllxqController extends TraceShowModuleDefineServiceDaoController<StringIDEntity, YlllxqService, YlllxqDao> {

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
