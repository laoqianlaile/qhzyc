package com.ces.component.sdzyccjgclgpsjkxx.action;

import com.ces.component.sdzyccjgclgpsjkxx.dao.SdzyccjgclgpsjkxxDao;
import com.ces.component.sdzyccjgclgpsjkxx.service.SdzyccjgclgpsjkxxService;
import com.ces.component.trace.action.base.TraceShowModuleDefineServiceDaoController;
import com.ces.xarch.core.entity.StringIDEntity;

public class SdzyccjgclgpsjkxxController extends TraceShowModuleDefineServiceDaoController<StringIDEntity, SdzyccjgclgpsjkxxService, SdzyccjgclgpsjkxxDao> {

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
    /*
    查看山东中药材粗加工车辆设备的型号下拉框的数据
     */
    public void searchGpsGridData(){
        setReturnData(getService().getGpsGridData());
    }
}
