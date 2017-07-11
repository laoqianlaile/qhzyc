package com.ces.component.tzjyxxxz.service;

import java.util.List;
import java.util.Map;

import javax.activation.DataHandler;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.ces.component.cspt.entity.TCsptZsxxEntity;
import com.ces.component.trace.utils.SerialNumberUtil;
import com.ces.component.trace.utils.TraceChainUtil;
import com.ces.component.tzjyxxxz.dao.TzjyxxxzDao;
import com.ces.config.datamodel.message.MessageModel;
import com.ces.config.dhtmlx.dao.common.DatabaseHandlerDao;
import com.ces.component.trace.service.base.TraceShowModuleDefineDaoService;
import com.ces.config.utils.AppDefineUtil;
import com.ces.config.utils.JsonUtil;
import com.ces.xarch.core.entity.StringIDEntity;
import com.fasterxml.jackson.databind.JsonNode;

@Component
public class TzjyxxxzService extends TraceShowModuleDefineDaoService<StringIDEntity, TzjyxxxzDao> {

    public Object getJyzhByHzbm(String hzbm){
		String tzcbm = SerialNumberUtil.getInstance().getCompanyCode();
		String sql = "select T.SZCDJYZH,T.HZBM,T.HZMC,T.ID from T_TZ_RPJYJYXX T left join T_TZ_RPJYJYMXXX P on P.PID = T.ID where P.JYZT='1' AND T.TZCBM LIKE '%" + tzcbm
				+ "%' and T.HZBM LIKE '%"+hzbm+"%' group by T.SZCDJYZH,T.HZBM,T.HZMC,T.ID order by T.SZCDJYZH desc";
		List<Map<String,Object>> map = DatabaseHandlerDao.getInstance().queryForMaps(sql);
		return map;
    }
    
    public Object getRpjyxx(String szcdjyzh){
		return getDao().getRpjyxx(szcdjyzh);
	}
    
    public Object getLzxxByJyzh(String szcdjyzh){
    	String tzcbm = SerialNumberUtil.getInstance().getCompanyCode();
		String sql = "select T.DWCPJYZH,T.RPPZJYZH from T_TZ_RPJYJYMXXX T left join T_TZ_RPJYJYXX P on T.PID = P.ID where t.jyzt='1' and P.SZCDJYZH='"+szcdjyzh+"' ";// and T.JYZT = '1' 控制是否显示出场的数据
		List<Map<String,Object>> map = DatabaseHandlerDao.getInstance().queryForMaps(sql);
		return map;
    }
    
    public String getZsm(){
    	String zsm = SerialNumberUtil.getInstance().getSerialNumber("TZ", "ZSM", true);
    	return zsm;
    }
    
    public void setJyzt(String szcdjyzh, List<String> dwcpjyzhList) {
    	String dwcpjyzhs = "";
    	for(String dwcpjyzh : dwcpjyzhList){
    		dwcpjyzhs += (dwcpjyzh);
    		dwcpjyzhs += (",");
    	}
    	dwcpjyzhs = dwcpjyzhs.substring(0, dwcpjyzhs.length()-1);
    	String sql = "update T_TZ_RPJYJYMXXX T set T.JYZT='2' where PID=(select P.ID from T_TZ_RPJYJYXX P where P.SZCDJYZH='"
					 +szcdjyzh+"') and T.DWCPJYZH in (" +dwcpjyzhs+ ")";
    	DatabaseHandlerDao.getInstance().executeSql(sql);
    }
    /**
     * 获取交易凭证号
     * @param szcdjyzh
     * @return
     */
    public String getJypzh(String szcdjyzh){
    	String sql = "SELECT T.JYPZH FROM T_TZ_SZJCXX T WHERE T.SZCDJYZH='"+szcdjyzh+"'";
    	return (String) DatabaseHandlerDao.getInstance().queryForMap(sql).get("JYPZH");
    }
    @Transactional
    @Override
    public Object saveAll(String tableId, String entityJson, String dTableId, String dEntitiesJson, Map<String, Object> paramMap) {
    	JsonNode entity = JsonUtil.json2node(entityJson);
    	Map<String, String> dataMap = node2map(entity);//主表信息
    	MessageModel message =  (MessageModel) super.saveAll(tableId, entityJson, dTableId, dEntitiesJson, paramMap);
    	Map<String,Object> map = (Map) message.getData();
    	List<Map<String,String>> detail = (List<Map<String, String>>) map.get("detail");//从表信息
    	Map<String,String> master = (Map<String, String>)map.get("master");//主表信息
    		/*****同步追溯信息*****/
        	TCsptZsxxEntity zsEntity = new TCsptZsxxEntity();
        	zsEntity.setJypzh(getJypzh(master.get("SZCDJYZH")));//根据生猪产地检疫证号从进场获得交易凭着号
        	zsEntity.setJhpch(master.get("JCPCH"));
        	zsEntity.setZsm(master.get("ZSM"));
        	zsEntity.setQybm(master.get("TZCBM"));
        	zsEntity.setQymc(master.get("TZCMC"));
        	zsEntity.setJyzbm(master.get("HZBM"));
        	zsEntity.setJyzmc(master.get("HZMC"));
        	zsEntity.setMjmc(master.get("MZMC"));
        	zsEntity.setMjbm(master.get("MZBM"));
        	zsEntity.setXtlx("4");
        	zsEntity.setRefId(master.get("ID"));
        	TraceChainUtil.getInstance().syncZsxx(zsEntity);
        	/********结束*******/
    	return message;
    }
}
