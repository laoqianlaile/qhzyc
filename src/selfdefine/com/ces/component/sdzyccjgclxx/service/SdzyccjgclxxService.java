package com.ces.component.sdzyccjgclxx.service;

import com.ces.component.sdzyccjgclxx.dao.SdzyccjgclxxDao;
import com.ces.component.trace.service.base.TraceShowModuleDefineDaoService;
import com.ces.component.trace.utils.SerialNumberUtil;
import com.ces.config.dhtmlx.dao.common.DatabaseHandlerDao;
import com.ces.xarch.core.entity.StringIDEntity;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class SdzyccjgclxxService extends TraceShowModuleDefineDaoService<StringIDEntity, SdzyccjgclxxDao> {

    /**
     *
     * @return
     */
    public Map<String,Object> getCjgClxx(){
        String qybm = SerialNumberUtil.getInstance().getCompanyCode();
        String sql = "select ID,CPH,CLLX,DJRQ,SYZT,BZ,QYBM from T_SDZYC_CJG_CLXX t_  where  is_delete = '0' AND  qybm =? order by CREATE_TIME DESC ";
        List<Map<String,Object>> list = DatabaseHandlerDao.getInstance().queryForMaps(sql,new Object[]{qybm});
        Map<String,Object> map = new HashMap<String, Object>();
        map.put("data",list);
        return map;
    }
}
