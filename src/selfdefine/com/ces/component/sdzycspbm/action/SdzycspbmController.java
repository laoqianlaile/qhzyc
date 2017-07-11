package com.ces.component.sdzycspbm.action;

import com.ces.component.sdzycspbm.dao.SdzycspbmDao;
import com.ces.component.sdzycspbm.service.SdzycspbmService;
import com.ces.component.trace.action.base.TraceShowModuleDefineServiceDaoController;
import com.ces.config.dhtmlx.dao.common.DatabaseHandlerDao;
import com.ces.config.utils.JsonUtil;
import com.ces.xarch.core.entity.StringIDEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SdzycspbmController extends TraceShowModuleDefineServiceDaoController<StringIDEntity, SdzycspbmService, SdzycspbmDao> {

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



}
