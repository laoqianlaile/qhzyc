package com.ces.component.sdzyccpjyxz.action;

import com.ces.component.sdzyccpjyxz.dao.SdzyccpjyxzDao;
import com.ces.component.sdzyccpjyxz.service.SdzyccpjyxzService;
import com.ces.component.trace.action.base.TraceShowModuleDefineServiceDaoController;
import com.ces.xarch.core.entity.StringIDEntity;
import com.ces.xarch.core.exception.BusinessException;
import com.ces.xarch.core.exception.FatalException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.rest.DefaultHttpHeaders;

public class SdzyccpjyxzController extends TraceShowModuleDefineServiceDaoController<StringIDEntity, SdzyccpjyxzService, SdzyccpjyxzDao> {

    private static final long serialVersionUID = 1L;
    private static Log log = LogFactory.getLog(SdzyccpjyxzController.class);
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
山东中药材饮片交易批次号下拉列表数据
 */
    public void  searchYpjypchGridData(){
        setReturnData(getService().searchYpjypchGridData());
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
    public void getPchkcxx(){
        String  pch = getParameter("pch");
        setReturnData(getService().getKcxxByQypch(pch));
    }
}
