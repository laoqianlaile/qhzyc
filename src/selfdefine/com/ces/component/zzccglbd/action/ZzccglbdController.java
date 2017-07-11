package com.ces.component.zzccglbd.action;

import com.ces.component.trace.utils.JSON;
import com.ces.component.trace.utils.SerialNumberUtil;
import com.ces.component.trace.dao.TraceShowModuleDao;
import com.ces.component.zzccglbd.service.ZzccglbdService;
import com.ces.component.trace.action.base.TraceShowModuleDefineServiceDaoController;
import com.ces.xarch.core.entity.StringIDEntity;
import com.fasterxml.jackson.core.type.TypeReference;

import javax.sql.rowset.serial.SerialArray;
import java.util.Map;

public class ZzccglbdController extends TraceShowModuleDefineServiceDaoController<StringIDEntity, ZzccglbdService, TraceShowModuleDao> {

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

    public Object saveCcxx(){
        String ccxx = getParameter("ccxx");
        Map<String, Object> dataMap = JSON.fromJSON(ccxx, new TypeReference<Map<String, Object>>() {});
        setReturnData(getService().saveCcxx(dataMap));
        return SUCCESS;
    }


}