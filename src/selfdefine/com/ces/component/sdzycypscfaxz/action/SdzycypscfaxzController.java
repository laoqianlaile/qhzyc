package com.ces.component.sdzycypscfaxz.action;

import com.ces.component.sdzycypscfaxz.dao.SdzycypscfaxzDao;
import com.ces.component.sdzycypscfaxz.service.SdzycypscfaxzService;
import com.ces.component.trace.action.base.TraceShowModuleDefineServiceDaoController;
import com.ces.xarch.core.entity.StringIDEntity;

public class SdzycypscfaxzController extends TraceShowModuleDefineServiceDaoController<StringIDEntity, SdzycypscfaxzService, SdzycypscfaxzDao> {

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
