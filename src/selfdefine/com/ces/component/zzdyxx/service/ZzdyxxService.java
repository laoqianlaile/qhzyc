package com.ces.component.zzdyxx.service;

import com.ces.component.trace.utils.SerialNumberUtil;
import com.ces.config.dhtmlx.dao.common.DatabaseHandlerDao;
import com.ces.config.utils.AppDefineUtil;
import org.springframework.stereotype.Component;

import com.ces.component.trace.dao.TraceShowModuleDao;
import com.ces.component.trace.service.base.TraceShowModuleDefineDaoService;
import com.ces.xarch.core.entity.StringIDEntity;

import javax.xml.crypto.Data;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class ZzdyxxService extends TraceShowModuleDefineDaoService<StringIDEntity, TraceShowModuleDao> {

    /**
     * 默认权限过滤
     * @return
     */
    public  String defaultCode(){
        String code= SerialNumberUtil.getInstance().getCompanyCode();
        String  defaultCode=" ";
        if(code!=null && !"".equals(code) )
            defaultCode= AppDefineUtil.RELATION_AND+" QYBM = '"+code+"' " +AppDefineUtil.RELATION_AND+" is_delete <> '1'";
        return defaultCode;
    }

    @Override
    protected String buildCustomerFilter(String tableId,
                                         String componentVersionId, String moduleId, String menuId,
                                         Map<String, Object> paramMap) {
        // 返回过滤条件时，要以AppDefineUtil.RELATION_AND、AppDefineUtil.RELATION_OR开头，如下所示：
        return defaultCode();
    }

    public Object getDkxx(String qybh,String dkbh) {
        String sql = "select t.dkbh,t.dkmc,t.mj,t.dkfzr,t.dkfzrbh from t_zz_dkxx t where t.is_delete <> '1' and qybm = ? ";
        List<String> params = new ArrayList<String>();
        params.add(SerialNumberUtil.getInstance().getCompanyCode());
        if(null!=qybh&&!"".equals(qybh)){
            sql += " and t.qybh = ?";
            params.add(qybh);
        }
        if(null!=dkbh&&!"".equals(dkbh)){
            sql += " and t.dkbh = ?";
            params.add(dkbh);
        }
        List<Map<String,Object>> maps = DatabaseHandlerDao.getInstance().queryForMaps(sql,params.toArray());
        Map<String,Object> resultMap = new HashMap<String, Object>();
        resultMap.put("data",maps);
        return resultMap;
    }

    public Object getJdxx() {
        String qybm = SerialNumberUtil.getInstance().getCompanyCode();
        String sql = "select t.jdmc,t.jdbh from t_zz_jdxx t where t.qybm = ? and t.is_delete <> '1'";
        List<Map<String,Object>> maps = DatabaseHandlerDao.getInstance().queryForMaps(sql,new Object[]{qybm});
        Map<String,Object> resultMap = new HashMap<String, Object>();
        resultMap.put("data",maps);
        return resultMap;
    }

    public Object getQyxx(String jdbh) {
        String qybm = SerialNumberUtil.getInstance().getCompanyCode();
        Object[] param = new Object[]{jdbh,SerialNumberUtil.getInstance().getCompanyCode()};
        String sql = "select t.QYBH,t.QYMC,t.QYMJ,t.FZR from T_ZZ_QYXX t where t.JDBH = ? and t.qybm = ? and t.IS_DELETE <>1";
        List<Map<String,Object>> maps = DatabaseHandlerDao.getInstance().queryForMaps(sql,new Object[]{jdbh,qybm});
        Map<String,Object> resultMap = new HashMap<String, Object>();
        resultMap.put("data",maps);
        return resultMap;
    }

    public Object checkBindle(List<String> idArr) {
        List<String> notBindedIds = new ArrayList<String>();
        for (String id : idArr) {
            String zzdybhSql = "select zzdybh from t_zz_dy where id = '" +id+ "'";
            Map<String,Object> map = DatabaseHandlerDao.getInstance().queryForMap(zzdybhSql);
            String checkCsglSql = "select id from t_zz_csgl where ZZDYBH = '"+map.get("ZZDYBH").toString()+"'";
            String checkScdaSql = "select id from t_zz_scda where ZZDYBH = '"+map.get("ZZDYBH").toString()+"'";
            List<Map<String,Object>> csglMaps = DatabaseHandlerDao.getInstance().queryForMaps(checkCsglSql);
            List<Map<String,Object>> scdaMaps = DatabaseHandlerDao.getInstance().queryForMaps(checkScdaSql);
            if (csglMaps.size() == 0 && scdaMaps.size() == 0) {
                notBindedIds.add(id);
            }
        }
        return notBindedIds;
    }
}