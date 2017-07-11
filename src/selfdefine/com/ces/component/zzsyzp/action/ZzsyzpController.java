package com.ces.component.zzsyzp.action;

import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.rest.DefaultHttpHeaders;

import com.ces.component.trace.dao.TraceShowModuleDao;
import com.ces.component.zzsyzp.service.ZzsyzpService;
import com.ces.component.trace.action.base.TraceShowModuleDefineServiceDaoController;
import com.ces.config.utils.ConstantVar;
import com.ces.xarch.core.entity.StringIDEntity;
import com.ces.xarch.core.exception.BusinessException;
import com.ces.xarch.core.exception.FatalException;

public class ZzsyzpController extends TraceShowModuleDefineServiceDaoController<StringIDEntity, ZzsyzpService, TraceShowModuleDao> {

    private static final long serialVersionUID = 1L;
    private static Log log = LogFactory.getLog(ZzsyzpController.class);
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
	public Object save() throws FatalException {
		try {
            // 获取表ID
            String tableId   = getParameter(P_TABLE_ID);
            String entityJson = getParameter(E_ENTITY_JSON);
            // 保存数据
            Map<String, String> dataMap = getService().save(tableId, entityJson, getMarkParamMap());
            String id = dataMap.get(ConstantVar.ColumnName.ID);
        	dataMap.clear();
        	dataMap.put(ConstantVar.ColumnName.ID, id);
            setReturnData(dataMap);
        } catch (Exception e) {
        	processException(e, BusinessException.class);
        	log.error("保存出错", e);
        }
        
        return NONE;
	}

	@Override
	public Object destroy() throws FatalException {
		  
        try {
            // 1. 获取表ID, ID
            String tableId  = getParameter(P_TABLE_ID);
            String dTableId = getParameter(P_D_TABLE_IDS);
            String ids      = getId();
            getService().delete(tableId, dTableId, ids, false, null);
        } catch (Exception e) {
            processException(e, BusinessException.class);
            log.error("删除出错", e);
        }        
        
        return new DefaultHttpHeaders(SUCCESS).disableCaching();
	}

}