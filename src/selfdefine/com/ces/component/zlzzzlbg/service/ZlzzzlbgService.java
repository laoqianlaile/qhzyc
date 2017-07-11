package com.ces.component.zlzzzlbg.service;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import com.ces.component.trace.utils.SerialNumberUtil;
import com.ces.component.trace.dao.TraceShowModuleDao;
import com.ces.config.dhtmlx.dao.common.DatabaseHandlerDao;
import org.springframework.stereotype.Component;

import com.ces.component.trace.service.base.TraceShowModuleDefineDaoService;
import com.ces.xarch.core.entity.StringIDEntity;

@Component
public class ZlzzzlbgService extends TraceShowModuleDefineDaoService<StringIDEntity, TraceShowModuleDao> {

    public Object getPzxx() {
        String qybm= SerialNumberUtil.getInstance().getCompanyCode();
        String sql = "select distinct CPMC,CPBH from v_zlzz_tpzz where qybm = '"+qybm+"' and CPMC is not null";
        List<Map<String,Object>> maps = DatabaseHandlerDao.getInstance().queryForMaps(sql);
        return maps;
    }

    public Object getAql(Map<String, String> param) throws ParseException {
        //获取月list
        List<String> dateStrs = new ArrayList<String>();
        Date startDate = new SimpleDateFormat("yyyy-MM").parse(param.get("startDate"));//定义起始日期
        Date endDate = new SimpleDateFormat("yyyy-MM").parse(param.get("endDate"));//定义结束日期
        Calendar cd = Calendar.getInstance();//定义日期实例
        cd.setTime(startDate);//设置日期起始时间
        while(cd.getTime().before(endDate)){//判断是否到结束日期
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
            String str = sdf.format(cd.getTime());
            dateStrs.add(str);
            cd.add(Calendar.MONTH, 1);//进行当前日期月份加1
        }
        dateStrs.add(param.get("endDate"));
        //获取结果map
        List<Map<String,Object>> resultMaps = new ArrayList<Map<String, Object>>();
        for (String date : dateStrs) {
            Map<String,Object> resultMap = new HashMap<String, Object>();
            String sql="select zl,ccwtbj from v_zlzz_tpzz where cpbh = '"+param.get("cpbh")+"' and ccsj like '"+date+"%' and qymc = '"+param.get("scqy")+"'";
//            String sql = "select s.zl,s.wtbj from t_zz_csnzwxq s where s.pid in (select t.id from t_zz_ccgl t where t.pzbh = '"+param.get("cpbh")+"' and t.ccsj like '"+date+"%') ";
            List<Map<String,Object>> monthlyMaps = DatabaseHandlerDao.getInstance().queryForMaps(sql);
            float totalWeight = 0;
            float fineWeight = 0;
            for (Map<String,Object> monthlyMap : monthlyMaps) {
                totalWeight += Float.parseFloat(monthlyMap.get("ZL") == null ? "":monthlyMap.get("ZL").toString());
                if (!(monthlyMap.get("CCWTBJ") == null ? "":monthlyMap.get("CCWTBJ").toString()).equals("1")) {
                     fineWeight += Float.parseFloat(monthlyMap.get("ZL").toString());
                }
            }
            if (totalWeight != 0) {
                BigDecimal bigDecimal = new BigDecimal((fineWeight/totalWeight)*100);
                bigDecimal = bigDecimal.setScale(2);
                float rate = bigDecimal.floatValue();
                resultMap.put("date",date);
                resultMap.put("rate",rate);
                resultMaps.add(resultMap);
            }
        }
        return resultMaps;
    }
}