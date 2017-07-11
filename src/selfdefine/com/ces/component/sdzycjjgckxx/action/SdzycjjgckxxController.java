package com.ces.component.sdzycjjgckxx.action;

import com.ces.component.sdzycjjgckxx.dao.SdzycjjgckxxDao;
import com.ces.component.sdzycjjgckxx.service.SdzycjjgckxxService;
import com.ces.component.trace.action.base.TraceShowModuleDefineServiceDaoController;
import com.ces.xarch.core.entity.StringIDEntity;

public class SdzycjjgckxxController extends TraceShowModuleDefineServiceDaoController<StringIDEntity, SdzycjjgckxxService, SdzycjjgckxxDao> {

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

    /**
     * 获取山东中药材仓库信息
     */
    public void getCkxx(){
        setReturnData(getService().getCkxx());
    }
}
