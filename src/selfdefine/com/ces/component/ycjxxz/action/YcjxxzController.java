package com.ces.component.ycjxxz.action;

import com.ces.component.ycjxxz.dao.YcjxxzDao;
import com.ces.component.ycjxxz.service.YcjxxzService;
import com.ces.component.trace.action.base.TraceShowModuleDefineServiceDaoController;
import com.ces.xarch.core.entity.StringIDEntity;
import com.ces.xarch.core.exception.BusinessException;
import com.ces.xarch.core.exception.FatalException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.rest.DefaultHttpHeaders;

public class YcjxxzController extends TraceShowModuleDefineServiceDaoController<StringIDEntity, YcjxxzService, YcjxxzDao> {

    private static final long serialVersionUID = 1L;
    private static Log log = LogFactory.getLog(YcjxxzController.class);
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


    public void searchYcpch(){
        String qypch = getParameter("qypch");
        setReturnData(getService().searchYcpch(qypch));
    }
    @Override
    public Object destroy() throws FatalException {

        try {
            // 1. 获取表ID, ID
            String tableId = getParameter(P_TABLE_ID);
            String dTableId = getParameter(P_D_TABLE_IDS);
            String ids = getId();
            //执行复写的删除方法
            getService().delete(tableId, dTableId, ids, false, null);
        } catch (Exception e) {
            processException(e, BusinessException.class);
            log.error("删除出错", e);
        }

        return new DefaultHttpHeaders(SUCCESS).disableCaching();
    }
}
