package com.ces.component.sdzyccjgycjyxx.action;

import com.ces.component.sdzyccjgycjyxx.dao.SdzyccjgycjyxxDao;
import com.ces.component.sdzyccjgycjyxx.service.SdzyccjgycjyxxService;
import com.ces.component.trace.action.base.TraceShowModuleDefineServiceDaoController;
import com.ces.xarch.core.entity.StringIDEntity;
import com.ces.xarch.core.exception.BusinessException;
import com.ces.xarch.core.exception.FatalException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.rest.DefaultHttpHeaders;

public class SdzyccjgycjyxxController extends TraceShowModuleDefineServiceDaoController<StringIDEntity, SdzyccjgycjyxxService, SdzyccjgycjyxxDao> {

    private static final long serialVersionUID = 1L;
    private static Log log = LogFactory.getLog(SdzyccjgycjyxxController.class);

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
     * 获得药材入库下拉框数据
     */
    public void searchGridData(){
        setReturnData(getService().searchycjyxxComboGridData());
    }

    public void searchGridData1(){
        setReturnData(getService().searchycjyxxComboGridData1());
    }

    /**
     * 根据检验单号获取药材检验信息
     */
    public void searcycjyxx(){
        String jybh= getParameter("jybh");
        setReturnData(getService().searchycjyxxByjybh(jybh));
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
    public void queryPch(){
        String pch = getParameter("pch");
        setReturnData(getService().searchDataByPch(pch));
    }
}

