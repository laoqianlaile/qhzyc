package com.ces.component.sdzyccjgylllxx.action;

import com.ces.component.sdzyccjgylllxx.dao.SdzyccjgylllxxDao;
import com.ces.component.sdzyccjgylllxx.service.SdzyccjgylllxxService;
import com.ces.component.trace.action.base.TraceShowModuleDefineServiceDaoController;
import com.ces.config.dhtmlx.dao.common.DatabaseHandlerDao;
import com.ces.xarch.core.entity.StringIDEntity;
import com.ces.xarch.core.exception.BusinessException;
import com.ces.xarch.core.exception.FatalException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.rest.DefaultHttpHeaders;

import java.util.List;
import java.util.Map;

public class SdzyccjgylllxxController extends TraceShowModuleDefineServiceDaoController<StringIDEntity, SdzyccjgylllxxService, SdzyccjgylllxxDao> {

    private static final long serialVersionUID = 1L;
    private static Log log = LogFactory.getLog(SdzyccjgylllxxController.class);

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
     * 获得领料单号下拉框数据
     */
    public void searchGridData(){
        setReturnData(getService().searchylllxxComboGridData());
    }

    /**
     * 根据领料单号获取原料领料信息
     */
    public void searcylllxx(){
        String lldh= getParameter("lldh");
        setReturnData(getService().searchylllxxBylldh(lldh));
    }

    /**
     * 获取领料部门信息
     */
    public void searchLlbmxx(){
        setReturnData(getService().getLlbmxx());
    }

    /**
     * 获取物料规格信息
     * @return
     * @throws FatalException
     */
    public  void searchWlggxx(){
        setReturnData(getService().getGgxx());
    }

    @Override
    public Object destroy() throws FatalException {

        try {
            // 1. 获取表ID, ID
            String tableId = getParameter(P_TABLE_ID);
            String dTableId = getParameter(P_D_TABLE_IDS);
            String ids = getId();
          /*  String sqlc="select t.pch from  v_sdzyc_cjg_ylllxx t where issc='1' and id IN ('" + ids.replace(",", "','") + "')";
            List<Map<String,Object>> jgpchList = DatabaseHandlerDao.getInstance().queryForMaps(sqlc);
            if (jgpchList.size()>0) {
            }else {*/
                //执行复写的删除方法
                getService().delete(tableId, dTableId, ids, false, null);
           /* }*/
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

