package com.ces.component.sdzyccjgclgpsjkxx.service;

import com.ces.component.sdzyccjgclgpsjkxx.dao.SdzyccjgclgpsjkxxDao;
import com.ces.component.trace.service.base.TraceShowModuleDefineDaoService;
import com.ces.component.trace.utils.SerialNumberUtil;
import com.ces.config.dhtmlx.dao.common.DatabaseHandlerDao;
import com.ces.xarch.core.entity.StringIDEntity;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class SdzyccjgclgpsjkxxService extends TraceShowModuleDefineDaoService<StringIDEntity, SdzyccjgclgpsjkxxDao> {
    /**
     * 获取型号下拉框的数据
     * @return
     */
    public Object getGpsGridData(){
        String qybm = SerialNumberUtil.getInstance().getCompanyCode();
        String sql ="select ID,BH,SBSBH,PP,XH,SSQY,SSDW,QYRQ,SYNX from T_QYPT_SBGL t  where t.qybm='"+qybm+"' order by BH DESC ";
        List<Map<String,Object>> list=DatabaseHandlerDao.getInstance().queryForMaps(sql);
        Map<String,Object> dataMap = new HashMap<String, Object>();
        dataMap.put("data" , list);
        return dataMap;
    }
}
