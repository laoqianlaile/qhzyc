package com.ces.component.sdzyccjgclxx.action;

import com.ces.component.sdzyccjgclxx.dao.SdzyccjgclxxDao;
import com.ces.component.sdzyccjgclxx.service.SdzyccjgclxxService;
import com.ces.component.trace.action.base.TraceShowModuleDefineServiceDaoController;
import com.ces.xarch.core.entity.StringIDEntity;

public class SdzyccjgclxxController extends TraceShowModuleDefineServiceDaoController<StringIDEntity, SdzyccjgclxxService, SdzyccjgclxxDao> {

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
     * 获取粗加工车辆信息下拉框数据
     */
    public void getClxxGrid(){setReturnData(getService().getCjgClxx());}
}
