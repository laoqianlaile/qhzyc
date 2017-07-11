package com.ces.component.qyptmdspgl.service;

import com.ces.component.trace.utils.SerialNumberUtil;
import com.ces.config.dhtmlx.dao.common.DatabaseHandlerDao;
import com.ces.config.utils.AppDefineUtil;
import org.springframework.stereotype.Component;

import com.ces.component.trace.dao.TraceShowModuleDao;
import com.ces.component.trace.service.base.TraceShowModuleDefineDaoService;
import com.ces.xarch.core.entity.StringIDEntity;

import java.text.SimpleDateFormat;
import java.util.*;

@Component
public class QyptmdspglService extends TraceShowModuleDefineDaoService<StringIDEntity, TraceShowModuleDao> {

    public Object checkSpid(List<String> spidList, String mdid, List<String> spbmList) {
        //返回门店已存在的商品列表
        List<Map<String,Object>> exsitSp = new ArrayList<Map<String, Object>>();
        //判断请求中是否有mdid没有进行查询
        if("".equals(mdid)||null==mdid){
            mdid = SerialNumberUtil.getInstance().getIdByCompanyCode();
        }
        //判断请求中是否有spid，若无按照spmc查找spid
        if(null==spidList||spidList.size() == 0||spidList.isEmpty()) {
            spidList = new ArrayList<String>();
            for(String spmc:spbmList){
                String sql = "select t.id from t_common_scspxx t where t.spbm = '"+spmc+"'";
                Map<String,Object> spidmap = DatabaseHandlerDao.getInstance().queryForMap(sql);
                spidList.add(spidmap.get("ID").toString());
            }
        }
        for(String spid:spidList){
            String sql1 = "select t.id,t.spid,t.mdid,t.spmc,t.spbm from T_QYPT_SCSPXX_ZHGL t where t.spid = '"+spid+"' and t.mdid = '" + mdid + "'";
            List<Map<String,Object>> resultMaps = DatabaseHandlerDao.getInstance().queryForMaps(sql1);
            if(resultMaps.size()!=0){
                exsitSp.add(resultMaps.get(0));
            }else{
                String sql2 = "select T.SPBM,T.SPMC from t_common_scspxx t where id = '"+spid+"'";
                Map<String,Object> spMap = DatabaseHandlerDao.getInstance().queryForMap(sql2);
                Map<String,String> map = new HashMap<String, String>();
                map.put("SPID",spid);
                map.put("MDID",mdid);
                map.put("SPMC",spMap.get("SPMC").toString());
                map.put("SPBM",spMap.get("SPBM").toString());
                saveOne("T_QYPT_SCSPXX_ZHGL", map);
            }
        }
        return exsitSp;
    }

}