package com.ces.component.zzbzgl.service;

import ces.sdk.util.StringUtil;
import com.ces.component.trace.utils.JSON;
import com.ces.component.trace.utils.SerialNumberUtil;
import com.ces.config.dhtmlx.dao.common.DatabaseHandlerDao;
import com.ces.config.utils.AppDefineUtil;
import com.fasterxml.jackson.core.type.TypeReference;
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
public class ZzbzglService extends TraceShowModuleDefineDaoService<StringIDEntity, TraceShowModuleDao> {
    /**
     * 默认权限过滤
     *
     * @return
     */
    public String defaultCode() {
        String code = SerialNumberUtil.getInstance().getCompanyCode();
        String defaultCode = " ";
        if (code != null && !"".equals(code))
            defaultCode = AppDefineUtil.RELATION_AND + " QYBM = '" + code
                    + "' and is_delete='0'";
        return defaultCode;
    }

    @Override
    protected String buildCustomerFilter(String tableId,
                                         String componentVersionId, String moduleId, String menuId,
                                         Map<String, Object> paramMap) {
        // 返回过滤条件时，要以AppDefineUtil.RELATION_AND、AppDefineUtil.RELATION_OR开头，如下所示：
        return defaultCode();
    }

    @Transactional
    public Object saveBzxx(Map<String, String> formData, List<Map<String, String>> gridData) {
        if (formData.get("ID") == null || "".equals(formData.get("ID"))) {
            formData.put("BZLSH", SerialNumberUtil.getInstance().getSerialNumber("ZZ", "ZZBZLSH", false));
            formData.put("CPZSM", SerialNumberUtil.getInstance().getCompanyCode() + SerialNumberUtil.getInstance().getSerialNumber("ZZ", "ZZCPZSM", false));
        }

        StringBuilder sb;
        String id = saveOne("t_zz_bzgl", formData);
        String sql;
        sql = "select * from t_zz_bzglplxx t where t.pid = '" + id + "' AND T.IS_DELETE <> '1'";
        List<Map<String, Object>> oldPlxxList = DatabaseHandlerDao.getInstance().queryForMaps(sql);
        for (Map<String, Object> map : oldPlxxList) {
            sql = "UPDATE T_ZZ_CSNZWXQ T SET T.KCZL = T.KCZL + " + (Double.parseDouble(String.valueOf(map.get("JGQZL")) == "null" ? "0" : String.valueOf(map.get("JGQZL")))) + " WHERE T.PCH = '" + map.get("CSPCH") + "'";
            DatabaseHandlerDao.getInstance().executeSql(sql);
            sql = "update t_zz_csgl c set c.kczl = (select sum(b.kczl) from t_zz_csnzwxq b where b.pid = (select a.pid from t_zz_csnzwxq a where a.pch = '" + map.get("CSPCH") + "') and b.zldj != '4') where c.id = (select a.pid from t_zz_csnzwxq a where a.pch = '" + map.get("CSPCH") + "')";
            DatabaseHandlerDao.getInstance().executeSql(sql.toString());
            sql = "update t_zz_csgl t set t.ycczl = t.zlhj - t.kczl - (select nvl(a.zl,0) from t_zz_csnzwxq a where a.pch like '" + map.get("CSPCH").toString().substring(0,map.get("CSPCH").toString().length()-2) + "%' and a.is_delete <> '1' and a.zldj like '%4') where t.id = (select b.pid from t_zz_csnzwxq b where b.pch = '" + map.get("CSPCH") + "' and b.is_delete <> '1')";
            DatabaseHandlerDao.getInstance().executeSql(sql.toString());
        }
        sql = "update t_zz_bzglplxx set is_delete = '1' where pid = '" + id + "'";
        DatabaseHandlerDao.getInstance().executeSql(sql);
        if (gridData != null) {
            for (Map<String, String> map : gridData) {
                map.put("PID", id);
                map.put("IS_DELETE", "0");
                if ("".equals(String.valueOf(map.get("BZPCZSM")))) {
//                    map.put("BZPCZSM", SerialNumberUtil.getInstance().getCompanyCode() + SerialNumberUtil.getInstance().getSerialNumber("ZZ", "ZZBZPCZSM", false));
                    map.put("BZPCZSM", "");
                }
                sql = "UPDATE T_ZZ_CSNZWXQ T SET T.KCZL = T.KCZL - " + (Double.parseDouble(String.valueOf(map.get("JGQZL")) == "" ? "0" : String.valueOf(map.get("JGQZL")))) + " WHERE T.PCH = '" + map.get("CSPCH") + "'";
                DatabaseHandlerDao.getInstance().executeSql(sql);
                sql = "update t_zz_csgl c set c.kczl = (select sum(b.kczl) from t_zz_csnzwxq b where b.pid = (select a.pid from t_zz_csnzwxq a where a.pch = '" + map.get("CSPCH") + "')  and b.zldj != '4' ) where c.id = (select a.pid from t_zz_csnzwxq a where a.pch = '" + map.get("CSPCH") + "')";
                DatabaseHandlerDao.getInstance().executeSql(sql.toString());
                sql = "update t_zz_csgl t set t.ycczl = (t.zlhj - t.kczl - (select a.zl from t_zz_csnzwxq a where a.pch like '" + map.get("CSPCH").substring(0,map.get("CSPCH").length()-2) + "%' and a.is_delete <> '1' and a.zldj like '%4')) where t.id = (select b.pid from t_zz_csnzwxq b where b.pch = '" + map.get("CSPCH") + "' and b.is_delete <> '1')";
                DatabaseHandlerDao.getInstance().executeSql(sql.toString());
                saveOne("t_zz_bzglplxx", map);
            }
        }

        return id;
    }

    public Object getBzxx(String id) {
        String sql = "";
        sql = "select * from t_zz_bzgl where id = '" + id + "' and is_delete = '0'";
        Map<String, Object> map = DatabaseHandlerDao.getInstance().queryForMap(sql);
        return map;
    }

    public Object getBzxxPlxx(String id) {
        String sql = "";
        sql = "select * from t_zz_bzglplxx t where t.pid ='" + id + "' and is_delete = '0'";
        List<Map<String, Object>> dataList = DatabaseHandlerDao.getInstance().queryForMaps(sql);
        for (Map<String, Object> map : dataList) {
            sql = "SELECT T.KCZL FROM T_ZZ_CSNZWXQ T WHERE T.PCH = '" + map.get("CSPCH") + "'";
            Map<String, Object> kczl = DatabaseHandlerDao.getInstance().queryForMap(sql);
            map.put("KCZL", Double.parseDouble(String.valueOf(kczl.get("KCZL")) == "null" ? "0" : String.valueOf(kczl.get("KCZL"))) + Double.parseDouble(String.valueOf(map.get("JGQZL")) == "null" ? "0" : String.valueOf(map.get("JGQZL"))));
        }
        Map<String, Object> dataMap = new HashMap<String, Object>();
        dataMap.put("data", dataList);
        dataMap.put("pageNumber", 1);
        dataMap.put("pageSize", dataList.size());
        dataMap.put("total", 1);
        dataMap.put("totalPages", 1);
        return dataMap;
    }

    @Override
    @Transactional
    public void delete(String tableId, String dTableIds, String ids, boolean isLogicalDelete, Map<String, Object> paramMap) {
        super.delete(tableId, dTableIds, ids, isLogicalDelete, paramMap);
        String id[] = ids.split(",");
        String sql = "";
        for (int i = 0; i < id.length; i++) {
            sql = "select * from t_zz_bzglplxx t where t.pid = '" + id + "' AND T.IS_DELETE <> '1'";
            List<Map<String, Object>> oldPlxxList = DatabaseHandlerDao.getInstance().queryForMaps(sql);
            for (Map<String, Object> map : oldPlxxList) {
                sql = "UPDATE T_ZZ_CSNZWXQ T SET T.KCZL = T.KCZL + " + (Double.parseDouble(String.valueOf(map.get("JGQZL")) == "null" ? "0" : String.valueOf(map.get("JGQZL")))) + " WHERE T.PCH = '" + map.get("CSPCH") + "'";
                DatabaseHandlerDao.getInstance().executeSql(sql);
                sql = "update t_zz_csgl c set c.kczl = (select sum(b.kczl) from t_zz_csnzwxq b where b.pid = (select a.pid from t_zz_csnzwxq a where a.pch = '" + map.get("CSPCH") + "')  and b.zldj != '4') where c.id = (select a.pid from t_zz_csnzwxq a where a.pch = '" + map.get("CSPCH") + "')";
                DatabaseHandlerDao.getInstance().executeSql(sql.toString());
                sql = "update t_zz_csgl t set t.ycczl = t.zlhj - t.kczl - (select nvl(a.zl,0) from t_zz_csnzwxq a where a.pch like '" + map.get("CSPCH").toString().substring(0,map.get("CSPCH").toString().length()-2) + "%' and a.is_delete <> '1' and a.zldj like '%4') where t.id = (select b.pid from t_zz_csnzwxq b where b.pch = '" + map.get("CSPCH") + "' and b.is_delete <> '1')";
                DatabaseHandlerDao.getInstance().executeSql(sql.toString());
            }
            sql = "update t_zz_bzglplxx set is_delete = '1' where pid = '" + id[i] + "'";
            DatabaseHandlerDao.getInstance().executeSql(sql);
        }
    }

    public Object getCsgl(String barCode) {
        String sql = "";
        sql = "select a.cslsh,b.kczl,a.qvmc,a.dkbh,a.dkmc,b.pch,a.pzbh from t_zz_csgl a,t_zz_csnzwxq b where a.id=b.pid and b.pch = '" + barCode + "' AND B.KCZL IS NOT NULL";
        Map<String, Object> dataMap = DatabaseHandlerDao.getInstance().queryForMap(sql);
        return dataMap;
    }


    /**
     * 保存打印信息
     *
     * @param bzlsh 包装流水号
     * @param bzxs  包装形式（小包装，大包装）
     * @param cpzsm 产品追溯码
     * @param id    主表（t_zz_bzgl）ID
     * @return
     */
    public Object savePrint(String bzlsh, String bzxs, String cpzsm, String id) {
        Map<String, String> dataMap = new HashMap<String, String>();
        dataMap.put("BZLSH", bzlsh);
        dataMap.put("BZXS", bzxs);
        dataMap.put("CPZSM", cpzsm);
        dataMap.put("PID", id);
        dataMap.put("IS_OUT", "0");
        saveOne("t_zz_bzgldymx", dataMap);
        return true;
    }

    public Object updateKczlForDelete(String ids){
        String id[] = ids.split(",");
        StringBuilder sql;
        for (int i = 0; i < id.length; i++){
            sql = new StringBuilder("SELECT * FROM T_ZZ_BZGLPLXX T WHERE T.PID = '" + id[i] + "' AND T.IS_DELETE <> '1'");
            List<Map<String, Object>> oldPlxxList = DatabaseHandlerDao.getInstance().queryForMaps(sql.toString());
            for (Map<String, Object> map : oldPlxxList) {
                sql = new StringBuilder("UPDATE T_ZZ_CSNZWXQ T SET T.KCZL = T.KCZL + " + (Double.parseDouble(String.valueOf(map.get("JGQZL")) == "null" ? "0" : String.valueOf(map.get("JGQZL")))) + " WHERE T.PCH = '" + map.get("CSPCH") + "'");
                DatabaseHandlerDao.getInstance().executeSql(sql.toString());
                sql = new StringBuilder("update t_zz_csgl c set c.kczl = (select sum(b.kczl) from t_zz_csnzwxq b where b.pid = (select a.pid from t_zz_csnzwxq a where a.pch = '" + map.get("CSPCH") + "')  and b.zldj != '4') where c.id = (select a.pid from t_zz_csnzwxq a where a.pch = '" + map.get("CSPCH") + "')");
                DatabaseHandlerDao.getInstance().executeSql(sql.toString());
                sql = new StringBuilder("update t_zz_csgl t set t.ycczl = t.zlhj - t.kczl - (select nvl(a.zl,0) from t_zz_csnzwxq a where a.pch like '" + map.get("CSPCH").toString().substring(0,map.get("CSPCH").toString().length()-2) + "%' and a.is_delete <> '1' and a.zldj like '%4') where t.id = (select b.pid from t_zz_csnzwxq b where b.pch = '" + map.get("CSPCH") + "' and b.is_delete <> '1')");
                DatabaseHandlerDao.getInstance().executeSql(sql.toString());
            }
            sql = new StringBuilder("UPDATE T_ZZ_BZGLPLXX SET IS_DELETE = '1' WHERE PID = '" + id[i] + "'");
            DatabaseHandlerDao.getInstance().executeSql(sql.toString());
        }
        return null;
    }
}