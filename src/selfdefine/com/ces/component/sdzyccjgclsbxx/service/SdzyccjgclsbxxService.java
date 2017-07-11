package com.ces.component.sdzyccjgclsbxx.service;

import com.ces.component.trace.utils.SerialNumberUtil;
import com.ces.config.dhtmlx.dao.common.DatabaseHandlerDao;
import org.springframework.stereotype.Component;

import com.ces.component.sdzyccjgclsbxx.dao.SdzyccjgclsbxxDao;
import com.ces.component.trace.service.base.TraceShowModuleDefineDaoService;
import com.ces.xarch.core.entity.StringIDEntity;

import java.util.Map;

@Component
public class SdzyccjgclsbxxService extends TraceShowModuleDefineDaoService<StringIDEntity, SdzyccjgclsbxxDao> {


    public Map<String,Object> searchSbh(String id){
        String sql = "select ID,QYBM,CPH,SBH,BZ from T_SDZYC_CJG_CLSBXX t_  where id=? and is_delete = '0' AND  qybm = ?";
        return DatabaseHandlerDao.getInstance().queryForMap(sql,new String[]{id, SerialNumberUtil.getInstance().getCompanyCode()});
    }
    
}
