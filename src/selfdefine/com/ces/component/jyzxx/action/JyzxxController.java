package com.ces.component.jyzxx.action;

import java.util.Map;

import com.ces.component.trace.dao.TraceShowModuleDao;
import com.ces.component.jyzxx.service.JyzxxService;
import com.ces.component.trace.utils.SerialNumberUtil;
import com.ces.component.zxtqyda.service.ZxtqydaService;
import com.ces.component.trace.action.base.TraceShowModuleDefineServiceDaoController;
import com.ces.config.utils.ConstantVar;
import com.ces.xarch.core.entity.StringIDEntity;
import com.ces.xarch.core.exception.BusinessException;
import com.ces.xarch.core.exception.FatalException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.rest.DefaultHttpHeaders;

public class JyzxxController extends TraceShowModuleDefineServiceDaoController<StringIDEntity, JyzxxService, TraceShowModuleDao> {

    private static Log log = LogFactory.getLog(JyzxxController.class);

    @Override
    protected void initModel() {
        setModel(new StringIDEntity());
    }
	
    private static final long serialVersionUID = 1L;

    public void getGysxxData(){
    	String id= getParameter("id");
    	setReturnData(getService().getGysxxData(id));
    }
    
    //验证该身份证号是否已备案
    public Object validateIdno(){
    	String idno = getParameter("idno");
    	Object result = getService().validateIdno(idno);
    	setReturnData(result);
    	return SUCCESS;
    }

    //是否在当前企业下
    public Object isInCurrentQy(){
        String idno = getParameter("idno");
        setReturnData(getService().isInCurrentQy(idno));
        return SUCCESS;
    }
    public Object getJyzbm(){
    	setReturnData(SerialNumberUtil.getInstance().getSerialNumber("COMMON","JYZBM", false, true));
    	return SUCCESS;
    }
    
    public Object getCompanyInfo(){
    	String menuId = getParameter("menuId");
    	Map<String,Object> map = getService(ZxtqydaService.class).getQydaByMenuId(menuId);
    	setReturnData(map);
    	return SUCCESS;
    }
    
    @Override
    public Object save() throws FatalException {
        try {
            // 获取表ID
            String tableId   = getParameter(P_TABLE_ID);
            String entityJson = getParameter(E_ENTITY_JSON);
            String isNewStr = getParameter("isNew");
            boolean isNew = Boolean.parseBoolean(isNewStr);
            // 保存数据
            Map<String, String> dataMap = getService().saveData(tableId, entityJson, getMarkParamMap(),isNew);
            String id = dataMap.get(ConstantVar.ColumnName.ID);
            // 从数据库重新查出来（防止有触发器更新数据）
            String op = getParameter(P_OP);
            if ("save".equals(op)) {
                setReturnData(getService().getById(id, tableId, dataMap));
            } else {
            	dataMap.clear();
            	dataMap.put(ConstantVar.ColumnName.ID, id);
                setReturnData(dataMap);
            }
        } catch (Exception e) {
        	processException(e, BusinessException.class);
        	//log.error("保存出错", e);
        }
        
        return NONE;
    }


    public void getJyzxx(){
        String zt = getParameter("zt");
        String xtlx = getParameter("xtlx");
        String jyzbm = getParameter("jyzbm");
        setReturnData(getService().getComJyzxx(zt,xtlx,jyzbm));
    }

    //读码获取经营者id
    public Object getJyzxxByBmAndQybm(){
        String jyzbm = getParameter("jyzbm");
        setReturnData(getService().getJyzxxByBmAndQybm(jyzbm));
        return SUCCESS;
    }

    //读码新增经营者
    public Object createJyz(){
        String id = getParameter("id");
        String xtlx = getParameter("xltx");
        String barCode = getParameter("barCode");
        setReturnData(getService().createJyz(id, xtlx, barCode));
        return SUCCESS;
    }

    //根据经营者编码获取其个人信息
    public Object getJyzInfo(){
        String jyzbm = getParameter("jyzbm");
        setReturnData(getService().getJyzInfo(jyzbm));
        return SUCCESS;
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

    public void getXgxx(){
        String id=getParameter("id");
        setReturnData(getService().getXgxx(id));
    }
}