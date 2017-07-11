package com.ces.component.qhpfyljydwjscxg.action;

import com.ces.component.qhpfyljydwjscxg.dao.QhpfyljydwjscxgDao;
import com.ces.component.qhpfyljydwjscxg.service.QhpfyljydwjscxgService;
import com.ces.component.trace.action.base.TraceShowModuleDefineServiceDaoController;
import com.ces.xarch.core.entity.StringIDEntity;

public class QhpfyljydwjscxgController extends TraceShowModuleDefineServiceDaoController<StringIDEntity, QhpfyljydwjscxgService, QhpfyljydwjscxgDao> {

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
