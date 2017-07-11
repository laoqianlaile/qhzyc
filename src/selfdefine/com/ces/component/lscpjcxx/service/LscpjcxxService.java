package com.ces.component.lscpjcxx.service;

import com.ces.component.cspt.entity.TCsptZsxxEntity;
import com.ces.component.trace.utils.SerialNumberUtil;
import com.ces.component.trace.utils.TraceChainUtil;
import com.ces.config.dhtmlx.dao.common.DatabaseHandlerDao;
import com.ces.config.dhtmlx.entity.database.Database;
import com.ces.config.utils.AppDefineUtil;
import com.ces.config.utils.JsonUtil;
import com.fasterxml.jackson.databind.JsonNode;
import org.apache.struts2.ServletActionContext;
import org.springframework.stereotype.Component;

import com.ces.component.trace.dao.TraceShowModuleDao;
import com.ces.component.trace.service.base.TraceShowModuleDefineDaoService;
import com.ces.xarch.core.entity.StringIDEntity;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@Component
public class LscpjcxxService extends TraceShowModuleDefineDaoService<StringIDEntity, TraceShowModuleDao> {

    public Map<String,Object> getJgcpchxx(String jgtmh){
        String sql = "select j.qybm,j.jgcmc,t.CPBH,t.CPMC,t.CCBZGG,t.CCBZSL,t.CPZZL,t.ZSM,t.PCH,t.CCDJ from t_jg_cpccxx t,t_jg_JGCDA j  where t.is_in='0' and t.cctmh='"+jgtmh+"' and j.qybm=t.qybm ";
        return DatabaseHandlerDao.getInstance().queryForMap(sql);
    }

    @Override
    @Transactional
    public Map<String, String> save(String tableId, String entityJson, Map<String, Object> paramMap) {
        String tableName = getTableName(tableId);
        JsonNode entityNode = JsonUtil.json2node(entityJson);
        Map<String, String> dataMap = node2map(entityNode);
        String id = save(tableName, dataMap, paramMap);
        dataMap.put(AppDefineUtil.C_ID, id);

        /*****同步追溯信息*****/
        TCsptZsxxEntity entity = new TCsptZsxxEntity();
        entity.setJypzh(dataMap.get("ZSPZH"));
        entity.setJhpch(dataMap.get("JHPCH"));
        entity.setQybm(dataMap.get("LSSCBM"));
        entity.setQymc(dataMap.get("LSSCMC"));
        entity.setJyzbm(dataMap.get("LSSBM"));
        entity.setJyzmc(dataMap.get("LSSMC"));
        entity.setXtlx("8");
        entity.setRefId(dataMap.get("ID"));
        TraceChainUtil.getInstance().syncZsxx(entity);
        /********结束*******/
        String barCode = ServletActionContext.getRequest().getParameter("barCode");
        if( null != barCode && barCode.contains("JG")){
            String sql = "update t_jg_cpccxx t set t.is_in = '1' where t.cctmh = '" + barCode + "'";
            DatabaseHandlerDao.getInstance().executeSql(sql);
        }
        return dataMap;
    }
    /**
     * 默认权限过滤
     * @return
     */
    public  String defaultCode(){
        String code= SerialNumberUtil.getInstance().getCompanyCode();
        String  defaultCode=" ";
        if(code!=null && !"".equals(code) )
            defaultCode=" AND LSSCBM = '"+code+"'";
        return defaultCode;
    }

    @Override
    protected String buildCustomerFilter(String tableId,
                                         String componentVersionId, String moduleId, String menuId,
                                         Map<String, Object> paramMap) {
        // 返回过滤条件时，要以AppDefineUtil.RELATION_AND、AppDefineUtil.RELATION_OR开头，如下所示：
        return defaultCode();
    }
    
    /**
     * 获取零售商信息
     */
    public Map<String,Object> getLssxx(String id){
    	String sql = "select * from T_LS_CPJXCC t where t.ID='"+id+"'";
		Map<String,Object> map = (Map<String,Object>)DatabaseHandlerDao.getInstance().queryForMap(sql);    	
    	return map;
    }
}