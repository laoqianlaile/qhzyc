package com.ces.component.sdzyccjgycspxx.action;

import com.ces.component.sdzyccjgycspxx.dao.SdzyccjgycspxxDao;
import com.ces.component.sdzyccjgycspxx.service.SdzyccjgycspxxService;
import com.ces.component.trace.action.base.TraceShowModuleDefineServiceDaoController;
import com.ces.xarch.core.entity.StringIDEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SdzyccjgycspxxController extends TraceShowModuleDefineServiceDaoController<StringIDEntity, SdzyccjgycspxxService, SdzyccjgycspxxDao> {

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

    public void getZycLi(){
        String rowData = getParameter("rowData");
        List<Map<String,String>> dataList = new ArrayList<Map<String, String>>();
        dataList = ces.sdk.util.JsonUtil.fromJSON(rowData,dataList.getClass());
        setReturnData(getService().saveycspxx(dataList));
    }

    public void searchGridData(){
        setReturnData(getService().searchYclComboGridData());
    }
}
