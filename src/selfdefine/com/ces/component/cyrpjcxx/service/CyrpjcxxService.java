package com.ces.component.cyrpjcxx.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.ces.component.cspt.entity.TCsptZsxxEntity;
import com.ces.component.cyrpjcxx.dao.CyrpjcxxDao;
import com.ces.component.trace.utils.CompanyInfoUtil;
import com.ces.component.trace.utils.SerialNumberUtil;
import com.ces.component.trace.utils.TraceChainUtil;
import com.ces.config.dhtmlx.dao.common.DatabaseHandlerDao;
import com.ces.component.trace.service.base.TraceShowModuleDefineDaoService;
import com.ces.config.utils.AppDefineUtil;
import com.ces.config.utils.JsonUtil;
import com.ces.config.utils.StringUtil;
import com.ces.xarch.core.entity.StringIDEntity;
import com.fasterxml.jackson.databind.JsonNode;

@Component
public class CyrpjcxxService extends TraceShowModuleDefineDaoService<StringIDEntity, CyrpjcxxDao> {

	/**
	 * 默认权限过滤
	 * @return
	 */
	public  String defaultCode(){
		String code=SerialNumberUtil.getInstance().getCompanyCode();
		String  defaultCode=" ";
		if(code!=null && !"".equals(code) )
			defaultCode=" AND CYXFDWBM = '"+code+"'";
		return defaultCode;
	}

	@Override
	protected String buildCustomerFilter(String tableId,
			String componentVersionId, String moduleId, String menuId,
			Map<String, Object> paramMap) {
		  // 返回过滤条件时，要以AppDefineUtil.RELATION_AND、AppDefineUtil.RELATION_OR开头，如下所示：
        return defaultCode();
	}
	
	public List  getSpmc(){
    	List list =  getDao().getAllSpmc();
    	return list;
    }
    
	public String dateToString(Date date){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return sdf.format(date);
	}
	
	@Transactional
	@Override
    public Map<String, String> save(String tableId, String entityJson, Map<String, Object> paramMap) {
    	String tableName = getTableName(tableId);
    	JsonNode entityNode = JsonUtil.json2node(entityJson);
    	Map<String, String> dataMap = node2map(entityNode);
    	String id = save(tableName, dataMap, paramMap);
    	dataMap.put(AppDefineUtil.C_ID, id);
    	/*****同步追溯信息*****/
    	TCsptZsxxEntity entity = new TCsptZsxxEntity();
    	entity.setJypzh(dataMap.get("ZSPZH"));
    	entity.setJhpch(dataMap.get("JCPCH"));
    	entity.setQybm(dataMap.get("CYXFDWBM"));
    	entity.setQymc(dataMap.get("CYXFDWMC"));
    	entity.setJyzbm(dataMap.get("GYSBM"));
    	entity.setJyzmc(dataMap.get("GYSMC"));
    	entity.setXtlx("9");
    	entity.setRefId(dataMap.get("ID"));
    	TraceChainUtil.getInstance().syncZsxx(entity);
    	/********结束*******/
        return dataMap;
    }
	
	/**
     * qiucs 2013-9-12 
     * <p>描述: 表单保存</p>
     * @return String    返回类型   
     */
    @Transactional
    public String save(String tableName, Map<String, String> dataMap, Map<String, Object> paramMap) {
        // 1. 获取表名
        if (StringUtil.isEmpty(tableName)) return "";
        // 2. 保存前，业务处理接口
        processBeforeSave(tableName, dataMap, paramMap);
        // 3. 根据ID判断是新增，还是修改
        String id = saveOne(tableName, dataMap);
        // 保存后，业务处理接口
        processAfterSave(tableName, dataMap, paramMap);
        
        /*****同步追溯信息*****/
    	TCsptZsxxEntity entity = new TCsptZsxxEntity();
    	entity.setJypzh(dataMap.get("ZSPZH"));
    	entity.setJhpch(dataMap.get("JHPCH"));
    	entity.setQybm(dataMap.get("CYXFDWBM"));
    	entity.setQymc(dataMap.get("CYXFDWMC"));
    	entity.setJyzbm(dataMap.get("GYSBM"));
    	entity.setJyzmc(dataMap.get("GYSMC"));
    	entity.setXtlx("9");
    	entity.setRefId(dataMap.get("ID"));
    	TraceChainUtil.getInstance().syncZsxx(entity);
        return id;
    }

    /**
     * 条码进场保存
     * @param barCode
     * @return
     */
    @Transactional
    public List<String> barCodeSave(String barCode) {
        //返回插入表的ID
        if(null !=barCode && !"".equals(barCode) && barCode.contains("PR")){
            return savePrData(barCode);
        }
        if(null !=barCode && !"".equals(barCode) && barCode.contains("TZ")){
            return saveTzData(barCode);
        }
        return null;
    }

    public List<String> savePrData(String barCode){
        List<String> ids = new ArrayList<String>();
        String sql = "select * from t_pr_jyxx t where t.is_in = '0' and upper(t.jytmh) = '" + barCode.toUpperCase() + "'";
        Map<String, Object> jyxx = DatabaseHandlerDao.getInstance().queryForMap(sql);
        if (!jyxx.isEmpty()) {
            Map<String,String> dataMap = new HashMap<String,String>();
            dataMap.put("CYXFDWBM", SerialNumberUtil.getInstance().getCompanyCode());
            dataMap.put("CYXFDWMC", CompanyInfoUtil.getInstance().getCompanyName("CY", SerialNumberUtil.getInstance().getCompanyCode()));
            dataMap.put("JCRQ", dateToString(new Date()));
            dataMap.put("GYSBM", String.valueOf(jyxx.get("PFSBM")));
            dataMap.put("GYSMC", String.valueOf(jyxx.get("PFSMC")));
            dataMap.put("SPBM", String.valueOf(jyxx.get("SPBM")));
            dataMap.put("SPMC", String.valueOf(jyxx.get("SPMC")));
            dataMap.put("ZL", String.valueOf(jyxx.get("ZL")));
            dataMap.put("DJ", String.valueOf(jyxx.get("DJ")));
            dataMap.put("JHPCH", String.valueOf(jyxx.get("JHPCH")));
            dataMap.put("ZSPZH", String.valueOf(jyxx.get("ZSM")));
            dataMap.put("GHSCBM", String.valueOf(jyxx.get("PFSCBM")));
            dataMap.put("GHSCMC", String.valueOf(jyxx.get("PFSCMC")));
            String jcId = save("T_CY_RPJCXX", dataMap, null);

            ids.add(jcId);
            //更新批肉交易（已进场）
            sql = "update t_pr_jyxx t set t.is_in = '1' where upper(t.jytmh) = '" + barCode.toUpperCase() + "'";
            DatabaseHandlerDao.getInstance().executeSql(sql);
        }
        return ids;
    }

    public List<String> saveTzData(String barCode){
        List<String> ids = new ArrayList<String>();
        String sql = "select * from t_tz_jyxx t where t.is_in = '0' and upper(t.jytmh) = '" + barCode.toUpperCase() + "'";
        Map<String, Object> jyxx = DatabaseHandlerDao.getInstance().queryForMap(sql);
        if (!jyxx.isEmpty()) {
            Map<String,String> dataMap = new HashMap<String,String>();
            dataMap.put("CYXFDWBM", SerialNumberUtil.getInstance().getCompanyCode());
            dataMap.put("CYXFDWMC", CompanyInfoUtil.getInstance().getCompanyName("CY", SerialNumberUtil.getInstance().getCompanyCode()));
            dataMap.put("JCRQ", dateToString(new Date()));
            dataMap.put("GYSBM", String.valueOf(jyxx.get("MZBM")));
            dataMap.put("GYSMC", String.valueOf(jyxx.get("MZMC")));
            dataMap.put("SPBM", String.valueOf(jyxx.get("SPBM")));
            dataMap.put("SPMC", String.valueOf(jyxx.get("SPMC")));
            dataMap.put("ZL", String.valueOf(jyxx.get("ZL")));
            dataMap.put("DJ", String.valueOf(jyxx.get("DJ")));
            dataMap.put("JHPCH", String.valueOf(jyxx.get("JCPCH")));
            dataMap.put("ZSPZH", String.valueOf(jyxx.get("ZSM")));
            dataMap.put("GHSCBM", String.valueOf(jyxx.get("TZCBM")));
            dataMap.put("GHSCMC", String.valueOf(jyxx.get("TZCBM")));
            String jcId = save("T_CY_RPJCXX", dataMap, null);
            ids.add(jcId);
            //更新批肉交易（已进场）
            sql = "update t_tz_jyxx t set t.is_in = '1' where upper(t.jytmh) = '" + barCode.toUpperCase() + "'";
            DatabaseHandlerDao.getInstance().executeSql(sql);
        }
        return ids;
    }
}
