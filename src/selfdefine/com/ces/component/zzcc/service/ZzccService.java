package com.ces.component.zzcc.service;

import com.ces.component.cspt.entity.TCsptZsxxEntity;
import com.ces.component.trace.utils.CompanyInfoUtil;
import com.ces.component.trace.utils.JSON;
import com.ces.component.trace.utils.SerialNumberUtil;
import com.ces.component.trace.utils.TraceChainUtil;
import com.ces.config.dhtmlx.dao.common.DatabaseHandlerDao;
import com.fasterxml.jackson.core.type.TypeReference;
import org.apache.ws.commons.schema.TypeReceiver;
import org.codehaus.jettison.json.JSONObject;
import org.springframework.stereotype.Component;

import com.ces.component.trace.dao.TraceShowModuleDao;
import com.ces.component.trace.service.base.TraceShowModuleDefineDaoService;
import com.ces.xarch.core.entity.StringIDEntity;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Component
public class ZzccService extends TraceShowModuleDefineDaoService<StringIDEntity, TraceShowModuleDao> {

    public Object deleteCc(String dataId) {
        String sql = "update t_zz_cc set is_delete = '1' where id = ?";
        int result = DatabaseHandlerDao.getInstance().executeSql(sql, new Object[]{dataId});
        if (1 == result) return "SUCCESS";
        else return "ERROR";
    }

    public Object deleteCcTrp(String dataId) {
        String sql = "update t_zz_cctrp set is_delete = '1' where id = ?";
        int result = DatabaseHandlerDao.getInstance().executeSql(sql, new Object[]{dataId});
        if (1 == result) return "SUCCESS";
        else return "ERROR";
    }

    /**
     * 按批次出场：获取区域编号
     *
     * @return
     */
    public Object getQybh() {
        String qybm = SerialNumberUtil.getInstance().getCompanyCode();
        StringBuilder sql = new StringBuilder("SELECT T.QYBH,T.QYMC FROM T_ZZ_QYXX T WHERE T.IS_DELETE <> '1' AND T.QYBM = ?");
        List<Map<String, Object>> dataList = DatabaseHandlerDao.getInstance().queryForMaps(sql.toString(), new Object[]{qybm});
        return dataList;
    }

    /**
     * 按批次出场：根据区域编号获取地块编号
     *
     * @param qybh 区域编号
     * @return
     */
    public Object getDkbhByQybh(String qybh) {
        String qybm = SerialNumberUtil.getInstance().getCompanyCode();
        StringBuilder sql = new StringBuilder("SELECT T.DKBH,T.DKMC FROM T_ZZ_DKXX T WHERE T.IS_DELETE <> '1' AND T.QYBM = ? AND T.QYBH = ?");
        List<Map<String, Object>> dataList = DatabaseHandlerDao.getInstance().queryForMaps(sql.toString(), new Object[]{qybm, qybh});
        return dataList;
    }

    /**
     * 按批次出场：根据地块编号获取生产档案编号
     *
     * @param dkbh 地块编号
     * @return
     */
    public Object getScdabhByDkbh(String dkbh) {
        String qybm = SerialNumberUtil.getInstance().getCompanyCode();
        StringBuilder sql = new StringBuilder("SELECT T.SCDABH,T.SCDABH AS SCDA FROM T_ZZ_SCDA T WHERE T.IS_DELETE <> '1' AND T.QYBM = ? AND T.DKBH = ?");
        List<Map<String, Object>> dataList = DatabaseHandlerDao.getInstance().queryForMaps(sql.toString(), new Object[]{qybm, dkbh});
        return dataList;
    }

    /**
     * 按批次出场：根据生产档案编号获取采收批次号
     *
     * @param scdabh 生产档案编号
     * @return
     */
    public Object getCspchByScdabh(String scdabh) {
        String qybm = SerialNumberUtil.getInstance().getCompanyCode();
        StringBuilder sql = new StringBuilder("SELECT B.PCH,B.PCH AS PC,B.ZLDJ,B.ZL,B.KCZL FROM T_ZZ_CSGL A,T_ZZ_CSNZWXQ B WHERE A.ID = B.PID AND A.IS_DELETE <> '1' AND B.IS_DELETE <> '1' AND B.KCZL IS NOT NULL AND B.ZL IS NOT NULL AND A.QYBM = ? AND A.SCDABH = ?");
        List<Map<String, Object>> dataList = DatabaseHandlerDao.getInstance().queryForMaps(sql.toString(), new Object[]{qybm, scdabh});
        return dataList;
    }

    /**
     * 按批次出场：根据采收批次号获取批次信息
     *
     * @param cspch 采收批次号
     * @return
     */
    public Object getApcccByCspch(String cspch) {
        String qybm = SerialNumberUtil.getInstance().getCompanyCode();
        StringBuilder sql = new StringBuilder("SELECT A.QYBH,A.QVMC AS QYMC,A.DKBH,A.DKMC,A.SCDABH,B.PCH,B.ZL,B.KCZL,B.ZLDJ,A.PLBH,A.PL,A.PZBH,A.PZ FROM T_ZZ_CSGL A,T_ZZ_CSNZWXQ B WHERE A.ID = B.PID AND A.IS_DELETE <> '1' AND B.IS_DELETE <> '1' AND A.QYBM = ? AND B.PCH = ?");
        Map<String, Object> dataMap = DatabaseHandlerDao.getInstance().queryForMap(sql.toString(), new Object[]{qybm, cspch});
        if (dataMap.size() == 0) {
            dataMap.put("RESULT", "ERROR");
        }
        return dataMap;
    }

    /**
     * 按批次出场：获取客户信息
     *
     * @return
     */
    public Object getKhxx() {
        String qybm = SerialNumberUtil.getInstance().getCompanyCode();
        StringBuilder sql = new StringBuilder("SELECT KHBH,KHMC FROM T_ZZ_XSDDXX T WHERE T.QYBM = ? and t.ddzt = '1' group by KHMC,KHBH");
        List<Map<String, Object>> dataList = DatabaseHandlerDao.getInstance().queryForMaps(sql.toString(), new Object[]{qybm});
        return dataList;
    }


    /**
     * 按批次出场：根据客户编号获取销售订单编号
     *
     * @return
     */
    public Object getXsddbhByKhbh(String ddbh, String khbh) {
        String qybm = SerialNumberUtil.getInstance().getCompanyCode();
        String xsddbhs[] = ddbh.split(",");
        StringBuilder sql = new StringBuilder("SELECT KHBH,KHMC,DDBH FROM T_ZZ_XSDDXX T WHERE T.QYBM = ? AND T.DDZT = '1' AND T.KHBH = ? AND T.IS_DELETE <> '1'");
        if (xsddbhs.length != 0 && !"".equals(xsddbhs[0])) {
            sql.append("and t.ddbh not in (");
            for (int i = 0; i < xsddbhs.length; i++) {
                sql.append("'" + xsddbhs[i] + "'");
                if ((i + 1) < xsddbhs.length) {
                    sql.append(",");
                }
            }
            sql.append(")");
        }
        List<Map<String, Object>> dataList = DatabaseHandlerDao.getInstance().queryForMaps(sql.toString(), new Object[]{qybm, khbh});
        return dataList;
    }

    /**
     * 按批次出场：获取配送方式
     *
     * @return
     */
    public Object getPsfs() {
        StringBuilder sql = new StringBuilder("SELECT T.SJBM,T.SJMC FROM T_COMMON_SJLX_CODE T WHERE T.LXBM = 'PSFS' AND T.IS_DELETE <> '1'");
        List<Map<String, Object>> dataList = DatabaseHandlerDao.getInstance().queryForMaps(sql.toString());
        return dataList;
    }

    @Transactional
    public Object savePcccxx(Map<String, String> formData, List<Map<String, String>> gridList) {
        String qybm = SerialNumberUtil.getInstance().getCompanyCode();
        formData.put("QYBM", qybm);
        String masterId = saveOne("T_ZZ_PCCC", formData);
        double cczzl = 0.0;
        StringBuilder sql;
        for (Map<String, String> map : gridList) {
            cczzl += Double.parseDouble(map.get("CCZL"));
            map.remove("OP");
            map.put("PID", masterId);
            map.put("QYBM", qybm);
            map.put("SFDH", "ZZ" + SerialNumberUtil.getInstance().getSerialNumber("ZZ","CCPCH",true));
            map.put("CPZSM", SerialNumberUtil.getInstance().getSerialNumber("ZZ","ZZCPZSM",true));
            map.put("ZSPCH", SerialNumberUtil.getInstance().getSerialNumber("ZZ","ZZZSPCH",true));
            saveOne("T_ZZ_PCCCKHXX", map);
            sql = new StringBuilder("UPDATE T_ZZ_XSDDXX T SET T.DDZT = '2' WHERE T.DDBH = ? AND T.QYBM = ?");
            DatabaseHandlerDao.getInstance().executeSql(sql.toString(), new Object[]{map.get("DDBH"), qybm});
        }
        sql = new StringBuilder("UPDATE T_ZZ_CSNZWXQ T SET T.KCZL = (T.KCZL - ?) WHERE T.PCH = ? AND T.IS_DELETE <> '1'");
        BigDecimal bd2 = new BigDecimal((cczzl + ""));
        DatabaseHandlerDao.getInstance().executeSql(sql.toString(), new Object[]{bd2, formData.get("PCH")});
        sql = new StringBuilder("update t_zz_csgl c set c.kczl = (select sum(b.kczl) from t_zz_csnzwxq b where b.pid = (select a.pid from t_zz_csnzwxq a where a.pch = '" + formData.get("PCH").toString() + "')  and b.zldj != '4') where c.id = (select a.pid from t_zz_csnzwxq a where a.pch = '" + formData.get("PCH").toString() + "')");
        DatabaseHandlerDao.getInstance().executeSql(sql.toString());
        sql = new StringBuilder("update t_zz_csgl t set t.ycczl = t.zlhj - t.kczl - (select nvl(a.zl,0) from t_zz_csnzwxq a where a.pch like '" + formData.get("PCH").toString().substring(0,formData.get("PCH").toString().length()-2) + "%' and a.is_delete <> '1' and a.zldj like '%4') where t.id = (select b.pid from t_zz_csnzwxq b where b.pch = '" + formData.get("PCH") + "' and b.is_delete <> '1')");
        DatabaseHandlerDao.getInstance().executeSql(sql.toString());

            StringBuilder synId = new StringBuilder();
            /**********************同步出场管理 begin************************************/
            sql = new StringBuilder("select A.CCLSH,B.KHMC,B.DDBH AS XSDDH,B.CCSJ,B.PSFS,B.CCZL AS ZZL,A.BZ,A.QYBM,B.KHBH,'0' AS IS_DELIVERED,b.psdz,b.sfdh,b.id AS SID  from t_zz_pccc a,t_zz_pccckhxx b where a.id = b.pid and a.is_delete <> '1' and b.is_delete <> '1' and a.id = '" + masterId + "'");
            List<Map<String, Object>> khxxList = DatabaseHandlerDao.getInstance().queryForMaps(sql.toString());
            String khxxString = JSON.toJSON(khxxList);
            List<Map<String, String>> khList = JSON.fromJSON(khxxString, new TypeReference<List<Map<String, String>>>() {
            });
            for (Map<String, String> khxxMap : khList) {
                String sid = khxxMap.get("SID");
                khxxMap.remove("SID");
                khxxMap.put("SYNID", sid);
                String mId = saveOne("T_ZZ_CCGL", khxxMap);
                //保存同步后的ID
                sql = new StringBuilder("UPDATE T_ZZ_PCCCKHXX T SET T.SYNID = '" + mId + "' WHERE T.ID = '" + sid + "'");
                DatabaseHandlerDao.getInstance().executeSql(sql.toString());
                sql = new StringBuilder("select a.pch as CSLSH,A.PZ,A.PZBH,A.ZL AS CSZZL,b.cczl,a.pch as pch,a.kczl,'0' as YJZT,a.qybm,B.CPZSM,b.ZSPCH  from t_zz_pccc a left join t_zz_pccckhxx b on a.id = b.pid where a.is_delete <> '1' and b.is_delete <> '1' and b.id = '" + sid + "'");
                Map<String, Object> shxxMap = DatabaseHandlerDao.getInstance().queryForMap(sql.toString());
                shxxMap.put("CSLSH",shxxMap.get("CSLSH").toString().substring(0,17));
                shxxMap.put("PID", mId);
                Set<String> key = shxxMap.keySet();
                Map<String, String> shMap = new HashMap<String, String>();
                for(String k:key){
                    shMap.put(k,shxxMap.get(k).toString());
                }
                shMap.put("SYNID", masterId);
                String shid = saveOne("T_ZZ_CCSHXX", shMap);
                synId.append(shid + ",");
                /**************************同步追溯信息begin********************************/
                TCsptZsxxEntity entity = new TCsptZsxxEntity();
                entity.setZsm(shxxMap.get("CPZSM").toString());
                entity.setJhpch(shxxMap.get("ZSPCH").toString());
                entity.setQybm(khxxMap.get("QYBM"));
                entity.setQymc(CompanyInfoUtil.getInstance().getCompanyName("ZZ", khxxMap.get("QYBM")));
                entity.setXtlx("1");
                entity.setRefId(shid);
                entity.setZZYZPCH(formData.get("PCH"));
                TraceChainUtil.getInstance().syncZsxx(entity);
                /**************************同步追溯信息end**********************************/
            }
        /**********************同步出场管理 end************************************/
        sql = new StringBuilder("UPDATE T_ZZ_PCCC T SET T.SYNID = '" + synId.toString() + "' WHERE T.ID = '" + masterId + "'");
        DatabaseHandlerDao.getInstance().executeSql(sql.toString());
        return true;
    }

    @Transactional
    public Object updatePcccxx(Map<String, String> formData, List<Map<String, String>> gridList){
        String qybm = SerialNumberUtil.getInstance().getCompanyCode();
        StringBuilder sql ;

        Map<String,Object> oldFormData = DatabaseHandlerDao.getInstance().queryForMap("SELECT * FROM T_ZZ_PCCC T WHERE T.ID = '" + formData.get("ID") + "'");
        List<Map<String, Object>> oldGridData = DatabaseHandlerDao.getInstance().queryForMaps("SELECT * FROM T_ZZ_PCCCKHXX T WHERE T.PID = '" + formData.get("ID") + "'");
        double oldCczzl = 0.0;
        for (Map<String, Object> map : oldGridData) {
            oldCczzl += Double.parseDouble(String.valueOf(map.get("CCZL")));
            DatabaseHandlerDao.getInstance().executeSql("UPDATE T_ZZ_XSDDXX T SET T.DDZT = '1' WHERE T.DDBH = ? AND T.QYBM = ?", new Object[]{map.get("DDBH"), qybm});
            //删除原来数据
            DatabaseHandlerDao.getInstance().executeSql("DELETE FROM T_ZZ_CCGL T WHERE T.SYNID = '" + map.get("ID") + "'");
        }
        double d2 = Double.parseDouble((oldCczzl + ""));
        DatabaseHandlerDao.getInstance().executeSql("UPDATE T_ZZ_CSNZWXQ T SET T.KCZL = (T.KCZL + ?) WHERE T.PCH = ? AND T.IS_DELETE <> '1'", new Object[]{d2, oldFormData.get("PCH")});
        sql = new StringBuilder("update t_zz_csgl c set c.kczl = (select sum(b.kczl) from t_zz_csnzwxq b where b.pid = (select a.pid from t_zz_csnzwxq a where a.pch = '" + oldFormData.get("PCH").toString() + "')  and b.zldj != '4') where c.id = (select a.pid from t_zz_csnzwxq a where a.pch = '" + oldFormData.get("PCH").toString() + "')");
        DatabaseHandlerDao.getInstance().executeSql(sql.toString());
        sql = new StringBuilder("update t_zz_csgl t set t.ycczl = t.zlhj - t.kczl - (select nvl(a.zl,0) from t_zz_csnzwxq a where a.pch like '" + oldFormData.get("PCH").toString().substring(0,oldFormData.get("PCH").toString().length()-2) + "%' and a.is_delete <> '1' and a.zldj like '%4') where t.id = (select b.pid from t_zz_csnzwxq b where b.pch = '" + oldFormData.get("PCH") + "' and b.is_delete <> '1')");
        DatabaseHandlerDao.getInstance().executeSql(sql.toString());
        //删除原来数据
        DatabaseHandlerDao.getInstance().executeSql("DELETE FROM T_ZZ_CCSHXX T WHERE T.SYNID = '" + formData.get("ID") + "'");


        StringBuilder newIds = new StringBuilder("");
        for(Map<String,String> map : gridList){
            if(!"".equals(map.get("ID")))
            newIds.append(map.get("ID"));
        }

        sql = new StringBuilder("SELECT * FROM T_ZZ_PCCCKHXX T WHERE T.PID = '" + formData.get("ID") + "'");
        for(Map<String, Object> map : DatabaseHandlerDao.getInstance().queryForMaps(sql.toString())){
            if(!newIds.toString().contains(String.valueOf(map.get("ID")))){
                sql = new StringBuilder("DELETE FROM T_ZZ_PCCCKHXX T WHERE T.ID = '" + map.get("ID") + "'");
                DatabaseHandlerDao.getInstance().executeSql(sql.toString());
            }
        }

        /*****************************************删除  保存  分割线******************************************/

        formData.put("QYBM", qybm);
        String masterId = saveOne("T_ZZ_PCCC", formData);
        double cczzl = 0.0;
        for (Map<String, String> map : gridList) {
            cczzl += Double.parseDouble(map.get("CCZL"));
            map.remove("OP");
            map.put("PID", masterId);
            map.put("QYBM", qybm);
            if("".equals(String.valueOf(map.get("ID")))){
                map.put("SFDH", "ZZ" + SerialNumberUtil.getInstance().getSerialNumber("ZZ","CCPCH",true));
                map.put("CPZSM", SerialNumberUtil.getInstance().getSerialNumber("ZZ","ZZCPZSM",true));
                map.put("ZSPCH", SerialNumberUtil.getInstance().getSerialNumber("ZZ","ZZZSPCH",true));
            }
            saveOne("T_ZZ_PCCCKHXX", map);
            sql = new StringBuilder("UPDATE T_ZZ_XSDDXX T SET T.DDZT = '2' WHERE T.DDBH = ? AND T.QYBM = ?");
            DatabaseHandlerDao.getInstance().executeSql(sql.toString(), new Object[]{map.get("DDBH"), qybm});
        }
        sql = new StringBuilder("UPDATE T_ZZ_CSNZWXQ T SET T.KCZL = (T.KCZL - ?) WHERE T.PCH = ? AND T.IS_DELETE <> '1'");
        BigDecimal bd2 = new BigDecimal((cczzl + ""));
        DatabaseHandlerDao.getInstance().executeSql(sql.toString(), new Object[]{bd2, formData.get("PCH")});
        sql = new StringBuilder("update t_zz_csgl c set c.kczl = (select sum(b.kczl) from t_zz_csnzwxq b where b.pid = (select a.pid from t_zz_csnzwxq a where a.pch = '" + formData.get("PCH").toString() + "')  and b.zldj != '4') where c.id = (select a.pid from t_zz_csnzwxq a where a.pch = '" + formData.get("PCH").toString() + "')");
        DatabaseHandlerDao.getInstance().executeSql(sql.toString());
        sql = new StringBuilder("update t_zz_csgl t set t.ycczl = t.zlhj - t.kczl - (select nvl(a.zl,0) from t_zz_csnzwxq a where a.pch like '" + formData.get("PCH").toString().substring(0,formData.get("PCH").toString().length()-2) + "%' and a.is_delete <> '1' and a.zldj like '%4') where t.id = (select b.pid from t_zz_csnzwxq b where b.pch = '" + formData.get("PCH") + "' and b.is_delete <> '1')");
        DatabaseHandlerDao.getInstance().executeSql(sql.toString());

        StringBuilder synId = new StringBuilder();
        /**********************同步出场管理 begin************************************/
        sql = new StringBuilder("select A.CCLSH,B.KHMC,B.DDBH AS XSDDH,B.CCSJ,B.PSFS,B.CCZL AS ZZL,A.BZ,A.QYBM,B.KHBH,'0' AS IS_DELIVERED,b.psdz,b.sfdh,b.id AS SID  from t_zz_pccc a,t_zz_pccckhxx b where a.id = b.pid and a.is_delete <> '1' and b.is_delete <> '1' and a.id = '" + masterId + "'");
        List<Map<String, Object>> khxxList = DatabaseHandlerDao.getInstance().queryForMaps(sql.toString());
        String khxxString = JSON.toJSON(khxxList);
        List<Map<String, String>> khList = JSON.fromJSON(khxxString, new TypeReference<List<Map<String, String>>>() {
        });
        for (Map<String, String> khxxMap : khList) {
            String sid = khxxMap.get("SID");
            khxxMap.remove("SID");
            khxxMap.put("SYNID", sid);
            String mId = saveOne("T_ZZ_CCGL", khxxMap);
            //保存同步后的ID
            sql = new StringBuilder("UPDATE T_ZZ_PCCCKHXX T SET T.SYNID = '" + mId + "' WHERE T.ID = '" + sid + "'");
            DatabaseHandlerDao.getInstance().executeSql(sql.toString());
            sql = new StringBuilder("select a.pch as CSLSH,A.PZ,A.PZBH,A.ZL AS CSZZL,b.cczl,a.pch as pch,a.kczl,'0' as YJZT,a.qybm,B.CPZSM ,b.zspch from t_zz_pccc a left join t_zz_pccckhxx b on a.id = b.pid where a.is_delete <> '1' and b.is_delete <> '1' and b.id = '" + sid + "'");
            Map<String, Object> shxxMap = DatabaseHandlerDao.getInstance().queryForMap(sql.toString());
            shxxMap.put("CSLSH",shxxMap.get("CSLSH").toString().substring(0,17));
            shxxMap.put("PID", mId);
            Set<String> key = shxxMap.keySet();
            Map<String, String> shMap = new HashMap<String, String>();
            for(String k:key){
                shMap.put(k,shxxMap.get(k).toString());
            }
            shMap.put("SYNID", masterId);
            String shid = saveOne("T_ZZ_CCSHXX", shMap);
            synId.append(shid + ",");
            /**************************同步追溯信息begin********************************/
            TCsptZsxxEntity entity = new TCsptZsxxEntity();
            entity.setZsm(shxxMap.get("CPZSM").toString());
            entity.setJhpch(shxxMap.get("ZSPCH").toString());
            entity.setQybm(khxxMap.get("QYBM"));
            entity.setQymc(CompanyInfoUtil.getInstance().getCompanyName("ZZ", khxxMap.get("QYBM")));
            entity.setXtlx("1");
            entity.setRefId(shid);
            entity.setZZYZPCH(formData.get("PCH"));
            TraceChainUtil.getInstance().syncZsxx(entity);
            /**************************同步追溯信息end**********************************/
        }
        /**********************同步出场管理 end************************************/
        sql = new StringBuilder("UPDATE T_ZZ_PCCC T SET T.SYNID = '" + synId.toString() + "' WHERE T.ID = '" + masterId + "'");
        DatabaseHandlerDao.getInstance().executeSql(sql.toString());
        return true;
    }

    @Transactional
    public Object deletePcccxx(String id){
        StringBuilder sql;
        String qybm = SerialNumberUtil.getInstance().getCompanyCode();
        for(String ids : id.split(",")){
            Map<String, Object> formData = DatabaseHandlerDao.getInstance().queryForMap("SELECT * FROM T_ZZ_PCCC T WHERE T.ID = '" + ids + "'");
            sql = new StringBuilder("SELECT * FROM T_ZZ_PCCCKHXX T WHERE T.PID = '" + ids + "' AND T.IS_DELETE <> '1'");
                double oldCczzl = 0.0;
                for (Map<String, Object> map : DatabaseHandlerDao.getInstance().queryForMaps(sql.toString())) {
                    oldCczzl += Double.parseDouble(String.valueOf(map.get("CCZL")));
                    DatabaseHandlerDao.getInstance().executeSql("UPDATE T_ZZ_XSDDXX T SET T.DDZT = '1' WHERE T.DDBH = ? AND T.QYBM = ?", new Object[]{map.get("DDBH"), qybm});
                    //删除原来数据
                    DatabaseHandlerDao.getInstance().executeSql("UPDATE T_ZZ_PCCCKHXX T SET T.IS_DELETE = '1' WHERE T.ID = '" + map.get("ID") + "'");
                    DatabaseHandlerDao.getInstance().executeSql("UPDATE T_ZZ_CCGL T SET T.IS_DELETE = '1' WHERE T.SYNID = '" + map.get("ID") + "'");
                }
                double d2 = Double.parseDouble((oldCczzl + ""));
                DatabaseHandlerDao.getInstance().executeSql("UPDATE T_ZZ_CSNZWXQ T SET T.KCZL = (T.KCZL + ?) WHERE T.PCH = ? AND T.IS_DELETE <> '1'", new Object[]{d2, formData.get("PCH")});
                sql = new StringBuilder("update t_zz_csgl c set c.kczl = (select sum(b.kczl) from t_zz_csnzwxq b where b.pid = (select a.pid from t_zz_csnzwxq a where a.pch = '" + formData.get("PCH").toString() + "')  and b.zldj != '4') where c.id = (select a.pid from t_zz_csnzwxq a where a.pch = '" + formData.get("PCH").toString() + "')");
                DatabaseHandlerDao.getInstance().executeSql(sql.toString());
                sql = new StringBuilder("update t_zz_csgl t set t.ycczl = t.zlhj - t.kczl - (select nvl(a.zl,0) from t_zz_csnzwxq a where a.pch like '" + formData.get("PCH").toString().substring(0,formData.get("PCH").toString().length()-2) + "%' and a.is_delete <> '1' and a.zldj like '%4') where t.id = (select b.pid from t_zz_csnzwxq b where b.pch = '" + formData.get("PCH") + "' and b.is_delete <> '1')");
                DatabaseHandlerDao.getInstance().executeSql(sql.toString());
                //删除原来数据
                DatabaseHandlerDao.getInstance().executeSql("UPDATE T_ZZ_PCCC T SET T.IS_DELETE = '1' WHERE T.ID = '" + formData.get("ID") + "'");
                DatabaseHandlerDao.getInstance().executeSql("UPDATE T_ZZ_CCSHXX T SET T.IS_DELETE = '1' WHERE T.SYNID = '" + formData.get("ID") + "'");
        }
        return "1";
    }


    /**
     *
     *
     * @return
     */
    public Object getCykh(String pch) {
        String companyCode = SerialNumberUtil.getInstance().getCompanyCode();
        StringBuilder sql = new StringBuilder("SELECT G.KHBH,G.KHMC FROM T_ZZ_PCCCKHXX G WHERE G.PID = (SELECT A.ID FROM T_ZZ_PCCC A,T_ZZ_CSNZWXQ B,T_ZZ_CSGL C WHERE A.PCH = B.PCH AND C.ID = B.PID AND C.PZBH = (SELECT D.PZBH FROM T_ZZ_CSGL D,T_ZZ_CSNZWXQ E WHERE D.ID = E.PID AND D.IS_DELETE <> '1' AND E.IS_DELETE <> '1' AND E.PCH = ?) AND A.CREATE_TIME = (SELECT MAX(F.CREATE_TIME) FROM T_ZZ_PCCC F WHERE F.PCH = ? AND F.IS_DELETE <> '1')) GROUP BY G.KHBH,G.KHMC" +
                " union select t.KHBH,KHMC from t_zz_xsddxx t where t.qybm = ? and ddzt = '1'");
        List<Map<String, Object>> dataList = DatabaseHandlerDao.getInstance().queryForMaps(sql.toString(), new Object[]{pch, pch, companyCode});
        Map<String, Object> mapData = new HashMap<String, Object>();
        mapData.put("data", dataList);
        mapData.put("pageNumber", 1);
        mapData.put("pageSize", dataList.size());
        mapData.put("total", 1);
        mapData.put("totalPages", 1);
        return mapData;
    }
}