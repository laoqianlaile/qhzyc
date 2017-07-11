package com.ces.component.zzccgl.service;

import com.ces.component.trace.utils.SerialNumberUtil;
import com.ces.config.dhtmlx.dao.common.DatabaseHandlerDao;
import com.ces.config.utils.AppDefineUtil;
import org.springframework.stereotype.Component;

import com.ces.component.trace.dao.TraceShowModuleDao;
import com.ces.component.trace.service.base.TraceShowModuleDefineDaoService;
import com.ces.xarch.core.entity.StringIDEntity;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class ZzccglService extends TraceShowModuleDefineDaoService<StringIDEntity, TraceShowModuleDao> {

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

    @Transactional
    public void deleteCcgl(String[] idArray) {
        for(String id : idArray){
            Object[] param = new Object[]{id};
            StringBuilder sql = new StringBuilder("SELECT * FROM T_ZZ_CCSHXX T WHERE T.PID = '" + id + "' AND T.IS_DELETE <> '1'");
            //散货删除
            for(Map<String, Object> map : DatabaseHandlerDao.getInstance().queryForMaps(sql.toString())){
                if (!"null".equals(String.valueOf(map.get("SYNID"))) && !"".equals(String.valueOf(map.get("SYNID")))) {
                    sql = new StringBuilder("UPDATE T_ZZ_CCGL T SET T.SYNID = '' WHERE T.ID = '" + id + "'");
                    DatabaseHandlerDao.getInstance().executeSql(sql.toString());
                    sql = new StringBuilder("SELECT T.SYNID FROM T_ZZ_PCCC T WHERE T.ID = '" + map.get("SYNID") + "'");
                    sql = new StringBuilder("UPDATE T_ZZ_PCCC T SET T.SYNID = '" + DatabaseHandlerDao.getInstance().queryForMap(sql.toString()).get("SYNID").toString().replaceAll(map.get("ID").toString() + ",", "") + "' WHERE T.ID = '" + map.get("SYNID") + "'");
                    DatabaseHandlerDao.getInstance().executeSql(sql.toString());
                    sql = new StringBuilder("DELETE FROM T_ZZ_PCCCKHXX T WHERE T.PID = '" + map.get("SYNID") + "' AND T.DDBH = '" + map.get("XSDDH") + "'");
                    DatabaseHandlerDao.getInstance().executeSql(sql.toString());
                }
                sql = new StringBuilder("UPDATE T_ZZ_CSNZWXQ T SET T.KCZL = T.KCZL + " + map.get("CCZL") + " WHERE T.PCH = '" + map.get("PCH") + "' AND T.IS_DELETE <> '1'");
                DatabaseHandlerDao.getInstance().executeSql(sql.toString());
                sql = new StringBuilder("update t_zz_csgl c set c.kczl = (select sum(b.kczl) from t_zz_csnzwxq b where b.pid = (select a.pid from t_zz_csnzwxq a where a.pch = '" + map.get("PCH") + "')  and b.zldj != '4') where c.id = (select a.pid from t_zz_csnzwxq a where a.pch = '" + map.get("PCH") + "')");
                DatabaseHandlerDao.getInstance().executeSql(sql.toString());
                sql = new StringBuilder("update t_zz_csgl t set t.ycczl = t.zlhj - t.kczl - (select nvl(a.zl,0) from t_zz_csnzwxq a where a.pch like '" + map.get("PCH").toString().substring(0,map.get("PCH").toString().length()-2) + "%' and a.is_delete <> '1' and a.zldj like '%4') where t.id = (select b.pid from t_zz_csnzwxq b where b.pch = '" + map.get("PCH") + "' and b.is_delete <> '1')");
                DatabaseHandlerDao.getInstance().executeSql(sql.toString());
            }
            //产品删除
            sql = new StringBuilder("SELECT * FROM T_ZZ_CCBZCPXX T WHERE T.PID = '" + id + "' AND T.IS_DELETE <> '1'");
            for(Map<String,Object> map : DatabaseHandlerDao.getInstance().queryForMaps(sql.toString())){
                sql = new StringBuilder("update t_zz_bzgl set kcjs =(kcjs + '" + map.get("CCJS") + "'),is_out = '0' where cpzsm ='" + map.get("CPZSM") + "' and is_delete <> '1'");
                DatabaseHandlerDao.getInstance().executeSql(sql.toString());
            }
            String ccglSql = "update T_ZZ_CCGL set is_delete = '1' where id = ?";
            String shSql = "update T_ZZ_CCSHXX set is_delete = '1' where pid = ?";
            String bzcpSql = "update T_ZZ_CCBZCPXX set is_delete = '1' where pid = ?";
            int ccglResult = DatabaseHandlerDao.getInstance().executeSql(ccglSql,param);
            int shResult = DatabaseHandlerDao.getInstance().executeSql(shSql,param);
            int bzcpResult = DatabaseHandlerDao.getInstance().executeSql(bzcpSql,param);
        };
    }

    public Object getCcxx(String ccid) {
        String shxxSql = "select t.pz,t.pzbh,t.cpzsm from t_zz_ccshxx t where t.pid = ?";
        String bzcpxxSql = "select t.cpmc,t.cpbh,t.cpzsm from t_zz_ccbzcpxx t where t.pid = ?";
        List<Map<String,Object>> shxx = DatabaseHandlerDao.getInstance().queryForMaps(shxxSql,new Object[]{ccid});
        List<Map<String,Object>> bzcpxx = DatabaseHandlerDao.getInstance().queryForMaps(bzcpxxSql,new Object[]{ccid});
        Map<String,Object> result = new HashMap<String, Object>();
        result.put("shxx",shxx);
        result.put("bzcpxx",bzcpxx);
        return result;
    }

    public Object getGridData(String id){
        String sql="select * from t_zz_bzgl where id =  '"+id+"'";
        return DatabaseHandlerDao.getInstance().queryForMap(sql);
    }

    public Object getShListByPid(String pid){
        StringBuilder sql = new StringBuilder("SELECT * FROM T_ZZ_CCSHXX T WHERE T.IS_DELETE <> '1' AND T.PID = '" + pid + "'");
        List<Map<String, Object>> dataList = DatabaseHandlerDao.getInstance().queryForMaps(sql.toString());
        for(Map<String, Object> map : dataList){
            map.put("URL","http://www.zhuisuyun.net/" + String.valueOf(map.get("CPZSM")));
        }
        return dataList;
    }
}