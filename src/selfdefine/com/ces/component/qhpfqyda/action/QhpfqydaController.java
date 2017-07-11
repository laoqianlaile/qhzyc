package com.ces.component.qhpfqyda.action;

import com.ces.component.qhpfqyda.dao.QhpfqydaDao;
import com.ces.component.qhpfqyda.service.QhpfqydaService;
import com.ces.component.trace.action.base.TraceShowModuleDefineServiceDaoController;
import com.ces.xarch.core.entity.StringIDEntity;

public class QhpfqydaController extends TraceShowModuleDefineServiceDaoController<StringIDEntity, QhpfqydaService, QhpfqydaDao> {

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
