package com.ces.component.sdzycjjgycspxx.action;

import com.ces.component.sdzycjjgycspxx.dao.SdzycjjgycspxxDao;
import com.ces.component.sdzycjjgycspxx.service.SdzycjjgycspxxService;
import com.ces.component.trace.action.base.TraceShowModuleDefineServiceDaoController;
import com.ces.xarch.core.entity.StringIDEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SdzycjjgycspxxController extends TraceShowModuleDefineServiceDaoController<StringIDEntity, SdzycjjgycspxxService, SdzycjjgycspxxDao> {

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
