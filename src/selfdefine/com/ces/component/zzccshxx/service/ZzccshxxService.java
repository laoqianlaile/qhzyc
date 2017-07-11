package com.ces.component.zzccshxx.service;

import com.ces.component.trace.utils.SerialNumberUtil;
import com.ces.config.dhtmlx.dao.common.DatabaseHandlerDao;
import com.ces.config.utils.AppDefineUtil;
import org.springframework.stereotype.Component;

import com.ces.component.trace.dao.TraceShowModuleDao;
import com.ces.component.trace.service.base.TraceShowModuleDefineDaoService;
import com.ces.xarch.core.entity.StringIDEntity;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class ZzccshxxService extends TraceShowModuleDefineDaoService<StringIDEntity, TraceShowModuleDao> {

    /**
     * 默认权限过滤
     *
     * @return
     */
    public String defaultCode() {
        String code = SerialNumberUtil.getInstance().getCompanyCode();
        String defaultCode = " ";
        if (code != null && !"".equals(code))
            defaultCode = AppDefineUtil.RELATION_AND + " QYBM = '" + code + "' "
                    + AppDefineUtil.RELATION_AND + " IS_DELETE <> '1' ";
        return defaultCode;
    }

    @Override
    protected String buildCustomerFilter(String tableId,
                                         String componentVersionId, String moduleId, String menuId,
                                         Map<String, Object> paramMap) {
        // 返回过滤条件时，要以AppDefineUtil.RELATION_AND、AppDefineUtil.RELATION_OR开头，如下所示：
        return defaultCode();
    }

    public Object getCsxx() {
        String sql = "select * from t_zz_csgl where 1 = 1 " + defaultCode() +" AND KCZL > 0";
        List<Map<String,Object>> maps = DatabaseHandlerDao.getInstance().queryForMaps(sql);
        return maps;
    }

    @Transactional
    public Object getCspcxx(String batchcode) {
        String sql = "select * from t_zz_csnzwxq where pch = ? and kczl > 0" + defaultCode();
        Map<String,Object> map = DatabaseHandlerDao.getInstance().queryForMap(sql, new Object[]{batchcode});
        String csglSql = "select t.pz,t.pzbh from t_zz_csgl t where t.id = ?";
        Map<String,Object> csglMap = DatabaseHandlerDao.getInstance().queryForMap(csglSql,new Object[]{(map.get("PID")==null?"":map.get("PID")).toString()});
        map.put("PZ", (csglMap.get("PZ") == null ? "" : csglMap.get("PZ")).toString());
        map.put("PZBH",(csglMap.get("PZBH") == null ? "":csglMap.get("PZBH")).toString());
        return map;
    }

    @Transactional
    public Object getShccByFj(String id){
        String sql="select 'temp_'||rownum as ID, ZLDJ, PCH,ZL AS CSZZL,KCZL from T_ZZ_CSNZWXQ where PID='"+id+"' and (KCZL > 0) and (ZLDJ <> 4) order by ZLDJ";
        String sql1=" select CSLSH,PZ,PZBH,KCZL as QCZL from t_zz_csgl where id='"+id+"'";
        Map<String,Object> map=DatabaseHandlerDao.getInstance().queryForMap(sql1);
        map.get("CSLSH");
        map.get("PZ");
        map.get("PZBH");
        map.get("QCZL");
        List<Map<String,Object>> list=new ArrayList<Map<String, Object>>();
        list= DatabaseHandlerDao.getInstance().queryForMaps(sql);
        for(int i=0;i<list.size();i++){
            list.get(i).put("CSLSH",map.get("CSLSH"));
            list.get(i).put("PZ",map.get("PZ"));
            list.get(i).put("PZBH",map.get("PZBH"));
            //list.get(i).put("QCZL",map.get("QCZL"));
        }
        Map<String,Object> map1=new HashMap<String, Object>();
        map1.put("data",list);

        return map1;
    }

    @Transactional
    public Object getShccKczl(String pch, String cczl){
        StringBuilder sql = new StringBuilder("SELECT T.KCZL FROM T_ZZ_CSNZWXQ T WHERE T.PCH = '" + pch + "' AND T.IS_DELETE <> '1'");
        Map<String, Object> kczlMap = DatabaseHandlerDao.getInstance().queryForMap(sql.toString());
        return (Double.parseDouble(String.valueOf(kczlMap.get("KCZL")).equals("null") ? "0" : (String.valueOf(kczlMap.get("KCZL")).equals("") ? "0" : String.valueOf(kczlMap.get("KCZL")))) + Double.parseDouble("".equals(cczl) ? "0" : cczl));
    }


}