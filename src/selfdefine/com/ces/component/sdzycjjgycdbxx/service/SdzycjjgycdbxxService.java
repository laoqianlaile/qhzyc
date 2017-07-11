package com.ces.component.sdzycjjgycdbxx.service;

import com.ces.component.trace.utils.SerialNumberUtil;
import com.ces.config.dhtmlx.dao.common.DatabaseHandlerDao;
import org.springframework.stereotype.Component;

import com.ces.component.sdzycjjgycdbxx.dao.SdzycjjgycdbxxDao;
import com.ces.component.trace.service.base.TraceShowModuleDefineDaoService;
import com.ces.xarch.core.entity.StringIDEntity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class SdzycjjgycdbxxService extends TraceShowModuleDefineDaoService<StringIDEntity, SdzycjjgycdbxxDao> {

    /**
     * 获取精加工药材入库下拉列表数据
     * @return
     */
    public Map<String,Object> searchYcdbxxData(){
        String  companyCode = SerialNumberUtil.getInstance().getCompanyCode();
        String sql = "select * from t_Sdzyc_Jjg_Yycrkxx t where t.qybm= '"+companyCode+"'";
        return getGridTypeData(DatabaseHandlerDao.getInstance().queryForMaps(sql));
    }
    public Map<String,Object> getGridTypeData(List<Map<String,Object>> list){
        Map<String,Object> dataMap = new HashMap<String, Object>();
        dataMap.put("data",list);
        return  dataMap;
    }

    /**
     * 根据人员编号获得人员信息
     * @param rybh
     * @return
     */
    public Map<String,Object> searchFzrxx(String rybh){
        String sql ="select * from T_ZZ_GZRYDA t where t.gzrybh='"+rybh+"' and  t.qybm='"+SerialNumberUtil.getInstance().getCompanyCode()+"'";
        return getGridTypeData(DatabaseHandlerDao.getInstance().queryForMaps(sql));
    }

    /**
     * 调入仓库下拉框自定义数据
     * @param ckbh
     * @return
     */
    public List<Map<String,Object>> getDrckExceptDCCK(String ckbh){
        String  companyCode = SerialNumberUtil.getInstance().getCompanyCode();
        String sql = "select * from T_sdzyc_JJG_CKXX t where t.qybm='"+companyCode+"' and  ckmc<>'"+ckbh+"'";
        return DatabaseHandlerDao.getInstance().queryForMaps(sql);
    }
}
