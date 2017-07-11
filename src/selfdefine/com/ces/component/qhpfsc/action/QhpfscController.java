package com.ces.component.qhpfsc.action;

import com.ces.component.qhpfsc.dao.QhpfscDao;
import com.ces.component.qhpfsc.service.QhpfscService;
import com.ces.component.trace.action.base.TraceShowModuleDefineServiceDaoController;
import com.ces.config.dhtmlx.dao.common.DatabaseHandlerDao;
import com.ces.xarch.core.entity.StringIDEntity;
import com.ces.xarch.core.exception.BusinessException;
import com.ces.xarch.core.exception.FatalException;
import org.apache.struts2.rest.DefaultHttpHeaders;

import java.util.List;
import java.util.Map;

public class QhpfscController extends TraceShowModuleDefineServiceDaoController<StringIDEntity, QhpfscService, QhpfscDao> {

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

    public void getccpch(){
        this.setReturnData(getService().getCcpch());
    }

    @Override
    public Object destroy() throws FatalException {
        try {
            String ids      = getId();
            String sqlc="select t.pch,t.id from  t_qh_pfccxx t where id IN ('" + ids.replace(",", "','") + "')";
            List<Map<String,Object>> jgpchList = DatabaseHandlerDao.getInstance().queryForMaps(sqlc);
            for (Map<String,Object> dataMap: jgpchList) {
                //String pch = String.valueOf(dataMap.get("PCH"));
                String id = String.valueOf(dataMap.get("ID"));
                getService().delete(id);
            }
        } catch (Exception e) {
            processException(e, BusinessException.class);
        }
        return new DefaultHttpHeaders(SUCCESS).disableCaching();

    }
}
