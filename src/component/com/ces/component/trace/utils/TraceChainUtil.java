package com.ces.component.trace.utils;

import com.ces.component.cspt.entity.TCsptZsxxEntity;
import com.ces.component.cspt.service.ZhuisuliantiaoSerivce;
import com.ces.xarch.core.web.listener.XarchListener;
import com.fasterxml.jackson.databind.JsonNode;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

@Component
public class TraceChainUtil {

	private static Log log = LogFactory.getLog(TraceChainUtil.class);
	
	public static TraceChainUtil getInstance() {
		return XarchListener.getBean(TraceChainUtil.class);
	}
	/**
	 * 同步追溯信息
	 * @param entity
	 * @return
	 */
	public boolean syncZsxx(TCsptZsxxEntity entity){
		ZhuisuliantiaoSerivce tCsptZsxxService =XarchListener.getBean(ZhuisuliantiaoSerivce.class);
		TCsptZsxxEntity en = tCsptZsxxService.getTCsptZsxxEntityByRefId(entity.getRefId());
		if(en!=null){//判断是否已存在
			entity.setId(en.getId());
			entity.setCreateTime(en.getCreateTime());
		}else{
			SimpleDateFormat  df = new SimpleDateFormat ("yyyy-MM-dd HH:mm:ss");
			entity.setCreateTime(df.format(new Date()));
		}
		try {
			tCsptZsxxService.insert(entity);
			return true;
		} catch (Exception e) {
			log.error("同步追溯信息错误："+e.getMessage());
			log.error(e);
			return false;
		}
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
}
