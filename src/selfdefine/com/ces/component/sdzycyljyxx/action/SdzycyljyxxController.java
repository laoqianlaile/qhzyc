package com.ces.component.sdzycyljyxx.action;

import com.ces.component.sdzycyljyxx.dao.SdzycyljyxxDao;
import com.ces.component.sdzycyljyxx.service.SdzycyljyxxService;
import com.ces.component.trace.action.base.TraceShowModuleDefineServiceDaoController;
import com.ces.config.datamodel.message.MessageModel;
import com.ces.config.dhtmlx.dao.common.DatabaseHandlerDao;
import com.ces.xarch.core.entity.StringIDEntity;
import com.ces.xarch.core.exception.BusinessException;
import com.ces.xarch.core.exception.FatalException;
import org.apache.struts2.rest.DefaultHttpHeaders;

import java.util.List;
import java.util.Map;

public class SdzycyljyxxController extends TraceShowModuleDefineServiceDaoController<StringIDEntity, SdzycyljyxxService, SdzycyljyxxDao> {

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
    @Override
    public Object destroy() throws FatalException {
        try {
            String ids      = getId();
            String sqlc="select t.cspch,t.id from  t_sdzyc_yljyxx t where id IN ('" + ids.replace(",", "','") + "')";
            List<Map<String,Object>> jgpchList = DatabaseHandlerDao.getInstance().queryForMaps(sqlc);
            for (Map<String,Object> dataMap: jgpchList) {
                String cspch = String.valueOf(dataMap.get("CSPCH"));
                String id = String.valueOf(dataMap.get("ID"));
               /* String sqlrk = "select pch from  t_sdzyc_cjg_ylrkxx t where pch = '" + cspch + "'";
                List<Map<String, Object>> rkpchList = DatabaseHandlerDao.getInstance().queryForMaps(sqlrk);
                if (rkpchList.size() > 0) {
                    cspchs = cspchs+cspch;
                } else {*/
                    getService().delete(cspch,id);
              /*  }*/
            }
        } catch (Exception e) {
            processException(e, BusinessException.class);
        }
        return new DefaultHttpHeaders(SUCCESS).disableCaching();
    }
    public void queryCspch(){
        String cspch = getParameter("cspch");
        setReturnData(getService().searchDataByCspch(cspch));
    }
}

