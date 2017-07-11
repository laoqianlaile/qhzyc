package com.ces.component.qyptsbgl.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.ces.component.trace.dao.TraceShowModuleDao;
import com.ces.component.trace.service.base.TraceShowModuleDefineDaoService;
import com.ces.component.trace.utils.SerialNumberUtil;
import com.ces.config.dhtmlx.dao.common.DatabaseHandlerDao;
import com.ces.xarch.core.entity.StringIDEntity;

@Component
public class QyptsbglService extends TraceShowModuleDefineDaoService<StringIDEntity, TraceShowModuleDao> {

	/**保存导入数据
	 * @param dataList
	 * @return
	 */
	@Transactional
	public Object importXls(List<Map<String,String>> dataList){	
        Map<String,String> resultMap = new HashMap<String, String>();
        Map<String,Object> shareMap;
        if(dataList.size() == 0){
            resultMap.put("RESULT","ERROR");
            resultMap.put("MSG","模版里无数据！");
            return resultMap;
        }
        resultMap.put("RESULT","SUCCESS");
        resultMap.put("MSG","成功导入数据！");
        for(Map<String,String> map:dataList){
        if("SUCCESS".equals(resultMap.get("RESULT"))){
        	//导入时获取所属企业编码
        	 String sql = "SELECT distinct (B.ZHBH) from T_QYPT_ZHGL B,T_QYPT_SBGL A WHERE B.ZHBH=A.QYBM AND A.IS_DELETE <> '1' AND A.SSQY =? ";
             shareMap = DatabaseHandlerDao.getInstance().queryForMap(String.valueOf(sql),new Object[]{ map.get("SSQY")});
             map.put("QYBM",String.valueOf(shareMap.get("ZHBH")));
           //导入时获取所属单位编码
             String sqll = "SELECT distinct (B.ZHBH) from T_QYPT_ZHGL B,T_QYPT_SBGL A WHERE B.ZHBH=A.DWBM AND A.IS_DELETE <> '1' AND A.SSDW =? ";
             shareMap = DatabaseHandlerDao.getInstance().queryForMap(String.valueOf(sqll),new Object[]{ map.get("SSDW")});
             map.put("DWBM",String.valueOf(shareMap.get("ZHBH")));
            //导入保存所有数据 
             saveOne("T_QYPT_SBGL", map); 
         }
     }
		return resultMap;
	}
}