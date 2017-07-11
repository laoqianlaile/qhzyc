package com.ces.component.prjcxx2.action;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.rest.DefaultHttpHeaders;
import org.springframework.transaction.annotation.Transactional;

import ces.coral.lang.StringUtil;

import com.ces.component.trace.dao.TraceShowModuleDao;
import com.ces.component.prjcxx2.service.Prjcxx2Service;
import com.ces.component.trace.utils.CompanyInfoUtil;
import com.ces.component.trace.utils.SerialNumberUtil;
import com.ces.component.trace.action.base.TraceShowModuleDefineServiceDaoController;
import com.ces.config.utils.ConstantVar;
import com.ces.config.utils.JsonUtil;
import com.ces.xarch.core.entity.StringIDEntity;
import com.ces.xarch.core.exception.BusinessException;
import com.ces.xarch.core.exception.FatalException;
import com.fasterxml.jackson.databind.JsonNode;

public class Prjcxx2Controller extends TraceShowModuleDefineServiceDaoController<StringIDEntity, Prjcxx2Service, TraceShowModuleDao> {

    private static final long serialVersionUID = 1L;
    private static Log log = LogFactory.getLog(TraceShowModuleDefineServiceDaoController.class);
    private String zspzh;
    private String pfsbm;
    
    public String getZspzh() {
		return zspzh;
	}

	public void setZspzh(String zspzh) {
		this.zspzh = zspzh;
	}
    
    public String getPfsbm() {
		return pfsbm;
	}

	public void setPfsbm(String pfsbm) {
		this.pfsbm = pfsbm;
	}

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
    
    public void getInitformData(){
    	String pfscbm = SerialNumberUtil.getInstance().getCompanyCode();
    	String pfscmc = CompanyInfoUtil.getInstance().getCompanyName("PR", pfscbm);
    	Map map = new HashMap();
    	map.put("PFSCBM", pfscbm);
    	map.put("PFSCMC", pfscmc);
    	setReturnData(map);
    }
    public void getZspzhGrid(){
    	setReturnData(this.getService().getZspzhList(pfsbm));
    }
    public void getSpxx(){
    	setReturnData(this.getService().getSpxx(zspzh));
    }
    /**
     * qiucs 2013-9-12 
     * <p>描述: 保存</p>
     * 覆写父方法
     */
    @Override
    public Object save() throws FatalException {
        try {
            // 获取表ID
            String tableId   = getParameter(P_TABLE_ID);
            String entityJson = getParameter(E_ENTITY_JSON);
            JsonNode entityNode = JsonUtil.json2node(entityJson);
        	Map<String, String> data = node2map(entityNode);
        	String oldZspzh = "";
        	String zspzh = data.get("ZSPZH");
        	//判断是否为修改
			if (StringUtil.isNotBlank(data.get("ID"))) {
				oldZspzh = this.getService().getZspzh(data.get("ID"));
				if (oldZspzh.equals(zspzh))//判断追溯凭证号是否改变
					oldZspzh = "";
			}
            // 保存数据
            Map<String, String> dataMap = getService().saveJcxx(tableId, entityJson, getMarkParamMap(),oldZspzh,zspzh);
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
        	log.error("保存出错", e);
        }
        
        return NONE;
    }
    /**
     * qiucs 2015-2-28 上午11:23:29
     * <p>描述: 将JsonNode转换为键值对 </p>
     * @return Map<String,String>
     */
    protected Map<String, String> node2map(JsonNode node) {
		Map<String, String> dMap = new HashMap<String, String>();
		Iterator<String> it = node.fieldNames();
		while (it.hasNext()) {
			String col = (String) it.next();
			dMap.put(col, node.get(col).asText());
		}
		return dMap; 
	}

    public void getJyzxxByZspzh(){
    	super.setReturnData(getService().getJyzxxByZspzh(zspzh));
    }
	
	
}