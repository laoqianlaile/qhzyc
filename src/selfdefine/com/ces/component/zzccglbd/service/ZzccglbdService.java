package com.ces.component.zzccglbd.service;

import com.ces.component.cspt.entity.TCsptZsxxEntity;
import com.ces.component.trace.utils.CompanyInfoUtil;
import com.ces.component.trace.utils.SerialNumberUtil;
import com.ces.component.trace.utils.TraceChainUtil;
import com.ces.config.dhtmlx.dao.common.DatabaseHandlerDao;
import org.springframework.stereotype.Component;

import com.ces.component.trace.dao.TraceShowModuleDao;
import com.ces.component.trace.service.base.TraceShowModuleDefineDaoService;
import com.ces.xarch.core.entity.StringIDEntity;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Component
public class ZzccglbdService extends TraceShowModuleDefineDaoService<StringIDEntity, TraceShowModuleDao> {

    @Transactional
    public Object saveCcxx(Map<String, Object> dataMap) {
        StringBuilder sb;
        Map<String, Object> shareMap ;
        String companyCode = SerialNumberUtil.getInstance().getCompanyCode();
        List<Map<String, String>> shxxMaps = (List<Map<String, String>>) dataMap.get("shxx");
        List<Map<String, String>> cpxxMaps = (List<Map<String, String>>) dataMap.get("cpxx");
        Map<String, String> ccglMap = (Map<String, String>) dataMap.get("ccgl");
        if (ccglMap.get("ID").equals("")) {
//            cpxxMaps.remove("ID");
            if (cpxxMaps.size() != 0)
                cpxxMaps.get(0).remove(10);
            String cclsh = SerialNumberUtil.getInstance().getSerialNumber("ZZ", "CCLSH", false);
            String sfdh = "ZZ" + SerialNumberUtil.getInstance().getSerialNumber("ZZ", "CCPCH", true);
            ccglMap.put("CCLSH", cclsh);
            ccglMap.put("SFDH", sfdh);
            //更新订单统计中的订单状态
            String updatesql="update T_ZZ_XSDDXX set DDZT = '2' where qybm = '"+companyCode+"' and DDBH = '"+ccglMap.get("XSDDH")+"'";
            DatabaseHandlerDao.getInstance().executeSql(updatesql);
        } else {
            sb = new StringBuilder("SELECT * FROM T_ZZ_CCGL T WHERE T.ID = '" + ccglMap.get("ID") + "'");
            Map<String, Object> oldCcglMap = DatabaseHandlerDao.getInstance().queryForMap(sb.toString());
            if (!"".equals(String.valueOf(oldCcglMap.get("SYNID"))) && !"null".equals(String.valueOf(oldCcglMap.get("SYNID")))) {
                sb = new StringBuilder("UPDATE T_ZZ_PCCCKHXX T SET T.KHMC = '" + ccglMap.get("KHMC") + "',T.DDBH = '" + ccglMap.get("XSDDH") + "',T.CCSJ = '" + ccglMap.get("CCSJ") + "',T.PSFS = '" + ccglMap.get("PSFS") + "',T.KHBH = '" + ccglMap.get("KHBH") + "',T.PSDZ = '" + ccglMap.get("PSDZ") + "' WHERE T.ID = '" + oldCcglMap.get("SYNID") + "'");
                DatabaseHandlerDao.getInstance().executeSql(sb.toString());
            }
        }
        ccglMap.put("QYBM", companyCode);
        //保存生产主表信息
        String masterId = save("T_ZZ_CCGL", ccglMap, null);
        //保存出场散货信息
        if (shxxMaps != null) {
            StringBuilder newIDs = new StringBuilder();
            for (Map<String, String> shxxMap : shxxMaps) {
                //新增时去掉ID
                if (shxxMap.get("ID").startsWith("temp_")||"".equals(shxxMap.get("ID"))) {
                    shxxMap.put("ID", "");
                    String zsm = SerialNumberUtil.getInstance().getSerialNumber("ZZ", "ZZCPZSM", true);
                    shxxMap.put("CPZSM", zsm);
                    String zspch = SerialNumberUtil.getInstance().getSerialNumber("ZZ", "ZZZSPCH", true);
                    shxxMap.put("ZSPCH", zspch);
                } else if (!"".equals(String.valueOf(shxxMap.get("ID"))) && !"null".equals(String.valueOf(shxxMap.get("ID")))) {
                    //同步修改批次出场
                    sb = new StringBuilder("SELECT A.XSDDH,B.ID,B.SYNID,B.CCZL,B.PCH FROM T_ZZ_CCGL A LEFT JOIN T_ZZ_CCSHXX B ON A.ID = B.PID WHERE B.ID = '" + String.valueOf(shxxMap.get("ID")) + "'");
                    shareMap = DatabaseHandlerDao.getInstance().queryForMap(sb.toString());
                    if (!"null".equals(String.valueOf(shareMap.get("SYNID"))) && !"".equals(String.valueOf(shareMap.get("SYNID")))) {
                        sb = new StringBuilder("UPDATE T_ZZ_PCCCKHXX T SET T.CCZL = '" + shxxMap.get("CCZL") + "' WHERE T.PID = '" + shareMap.get("SYNID") + "' AND T.DDBH = '" + shareMap.get("XSDDH") + "'");
                        DatabaseHandlerDao.getInstance().executeSql(sb.toString());
                    }

                }
                shxxMap.put("PID", masterId);
                shxxMap.put("QYBM", companyCode);
                shxxMap.remove("COL_010");
                if (!shxxMap.get("ID").equals("")) {
                    sb = new StringBuilder("SELECT * FROM T_ZZ_CCSHXX T WHERE T.ID = '" + shxxMap.get("ID") + "'");
                    Map<String, Object> oldShxxMap = DatabaseHandlerDao.getInstance().queryForMap(sb.toString());
                    sb = new StringBuilder("update t_zz_csnzwxq set kczl = kczl + " + Float.parseFloat(String.valueOf(oldShxxMap.get("CCZL"))) + " where pch = '" + oldShxxMap.get("PCH") + "' and qybm = '" + companyCode + "'");
                    DatabaseHandlerDao.getInstance().executeSql(sb.toString());
                    sb = new StringBuilder("update t_zz_csgl c set c.kczl = (select sum(b.kczl) from t_zz_csnzwxq b where b.pid = (select a.pid from t_zz_csnzwxq a where a.pch = '" + oldShxxMap.get("PCH") + "')  and b.zldj != '4') where c.id = (select a.pid from t_zz_csnzwxq a where a.pch = '" + oldShxxMap.get("PCH") + "')");
                    DatabaseHandlerDao.getInstance().executeSql(sb.toString());
                    sb = new StringBuilder("update t_zz_csgl t set t.ycczl = t.zlhj - t.kczl - (select nvl(a.zl,0) from t_zz_csnzwxq a where a.pch like '" + oldShxxMap.get("PCH").toString().substring(0,oldShxxMap.get("PCH").toString().length()-2) + "%' and a.is_delete <> '1' and a.zldj like '%4') where t.id = (select b.pid from t_zz_csnzwxq b where b.pch = '" + oldShxxMap.get("PCH") + "' and b.is_delete <> '1')");
                    DatabaseHandlerDao.getInstance().executeSql(sb.toString());
                }
                String shNewId = save("T_ZZ_CCSHXX", shxxMap, null);
                newIDs.append(shNewId + ",");
                String sql = "update t_zz_csnzwxq set kczl = kczl - " + Float.parseFloat(shxxMap.get("CCZL")) + " where pch = '" + shxxMap.get("PCH") + "' and qybm = '" + companyCode + "'";
                DatabaseHandlerDao.getInstance().executeSql(sql);
                sql = "update t_zz_csgl c set c.kczl = (select sum(b.kczl) from t_zz_csnzwxq b where b.pid = (select a.pid from t_zz_csnzwxq a where a.pch = '" + shxxMap.get("PCH") + "') and b.zldj != '4') where c.id = (select a.pid from t_zz_csnzwxq a where a.pch = '" + shxxMap.get("PCH") + "')";
                DatabaseHandlerDao.getInstance().executeSql(sql);
                sql = "update t_zz_csgl t set t.ycczl = t.zlhj - t.kczl - (select nvl(a.zl,0) from t_zz_csnzwxq a where a.pch like '" + shxxMap.get("PCH").toString().substring(0,shxxMap.get("PCH").toString().length()-2) + "%' and a.is_delete <> '1' and a.zldj like '%4') where t.id = (select b.pid from t_zz_csnzwxq b where b.pch = '" + shxxMap.get("PCH") + "' and b.is_delete <> '1')";
                DatabaseHandlerDao.getInstance().executeSql(sql.toString());
                /****************同步追溯信息*****************/
                TCsptZsxxEntity entity = new TCsptZsxxEntity();
                entity.setZsm(shxxMap.get("CPZSM"));
                entity.setJhpch(shxxMap.get("ZSPCH"));
                entity.setQybm(ccglMap.get("QYBM"));
                entity.setQymc(CompanyInfoUtil.getInstance().getCompanyName("ZZ", ccglMap.get("QYBM")));
                entity.setXtlx("1");
                entity.setRefId(shNewId);
                entity.setZZYZPCH(shxxMap.get("PCH"));
                TraceChainUtil.getInstance().syncZsxx(entity);
                /****************同步追溯信息*****************/
            }
            sb = new StringBuilder("SELECT A.XSDDH,B.ID,B.SYNID,B.CCZL,B.PCH FROM T_ZZ_CCGL A LEFT JOIN T_ZZ_CCSHXX B ON A.ID = B.PID WHERE B.PID = '" + masterId + "'");
            for (Map<String, Object> map : DatabaseHandlerDao.getInstance().queryForMaps(sb.toString())) {
                //处理删除散货时的情况
                if (!newIDs.toString().contains(map.get("ID").toString())) {
                    sb = new StringBuilder("UPDATE T_ZZ_CCSHXX T SET T.IS_DELETE = '1' WHERE T.ID = '" + map.get("ID") + "'");
                    DatabaseHandlerDao.getInstance().executeSql(sb.toString());
                    if (!"null".equals(String.valueOf(map.get("SYNID"))) && !"".equals(String.valueOf(map.get("SYNID")))) {
                        sb = new StringBuilder("UPDATE T_ZZ_CCGL T SET T.SYNID = '' WHERE T.ID = '" + masterId + "'");
                        DatabaseHandlerDao.getInstance().executeSql(sb.toString());
                        sb = new StringBuilder("SELECT T.SYNID FROM T_ZZ_PCCC T WHERE T.ID = '" + map.get("SYNID") + "'");
                        sb = new StringBuilder("UPDATE T_ZZ_PCCC T SET T.SYNID = '" + DatabaseHandlerDao.getInstance().queryForMap(sb.toString()).get("SYNID").toString().replaceAll(map.get("ID").toString() + ",", "") + "' WHERE T.ID = '" + map.get("SYNID") + "'");
                        DatabaseHandlerDao.getInstance().executeSql(sb.toString());
                        sb = new StringBuilder("DELETE FROM T_ZZ_PCCCKHXX T WHERE T.PID = '" + map.get("SYNID") + "' AND T.DDBH = '" + map.get("XSDDH") + "'");
                        DatabaseHandlerDao.getInstance().executeSql(sb.toString());
                    }
                    sb = new StringBuilder("UPDATE T_ZZ_CSNZWXQ T SET T.KCZL = T.KCZL + " + map.get("CCZL") + " WHERE T.PCH = '" + map.get("PCH") + "' AND T.IS_DELETE <> '1'");
                    DatabaseHandlerDao.getInstance().executeSql(sb.toString());
                    sb = new StringBuilder("update t_zz_csgl c set c.kczl = (select sum(b.kczl) from t_zz_csnzwxq b where b.pid = (select a.pid from t_zz_csnzwxq a where a.pch = '" + map.get("PCH") + "')  and b.zldj != '4') where c.id = (select a.pid from t_zz_csnzwxq a where a.pch = '" + map.get("PCH") + "')");
                    DatabaseHandlerDao.getInstance().executeSql(sb.toString());
                    sb = new StringBuilder("update t_zz_csgl t set t.ycczl = t.zlhj - t.kczl - (select nvl(a.zl,0) from t_zz_csnzwxq a where a.pch like '" + map.get("PCH").toString().substring(0,map.get("PCH").toString().length()-2) + "%' and a.is_delete <> '1' and a.zldj like '%4') where t.id = (select b.pid from t_zz_csnzwxq b where b.pch = '" + map.get("PCH") + "' and b.is_delete <> '1')");
                    DatabaseHandlerDao.getInstance().executeSql(sb.toString());
                }
            }
        }

          //保存包装成品信息

        if (cpxxMaps != null)
            for (Map<String, String> cpxxMap : cpxxMaps) {//
                //新增时去掉ID
                if (cpxxMap == null) continue;
                if (cpxxMap.get("ID").startsWith("temp_")) {
                    cpxxMap.put("ID", "");
                }
            }
        //同步出场重量

        if (cpxxMaps != null) {
            StringBuilder newIDs = new StringBuilder();
            for (Map<String, String> cpxxMap : cpxxMaps) {
                //新增时去掉ID
                if (null==cpxxMap.get("ID")||cpxxMap.get("ID").startsWith("temp_")||cpxxMap.get("ID").equals("")) {
                    cpxxMap.put("ID", "");
                    String zsm = SerialNumberUtil.getInstance().getSerialNumber("ZZ", "ZZCPZSM", true);
                    cpxxMap.put("ZSM", zsm);
                    String zspch = SerialNumberUtil.getInstance().getSerialNumber("ZZ", "ZZZSPCH", true);
                    cpxxMap.put("ZSPCH", zspch);
                }else{
                    sb = new StringBuilder("SELECT * FROM T_ZZ_CCBZCPXX T WHERE T.ID = '" + cpxxMap.get("ID") + "'");
                    shareMap = DatabaseHandlerDao.getInstance().queryForMap(sb.toString());
                    sb = new StringBuilder("update t_zz_bzgl set kcjs =(kcjs + '" + shareMap.get("CCJS") + "') where cpzsm ='" + shareMap.get("CPZSM") + "'");
                    DatabaseHandlerDao.getInstance().executeSql(sb.toString());
                    updateBzCcFlag(String.valueOf(shareMap.get("CPZSM")));
                }
                //同步出场重量


                String ccjs = cpxxMap.get("CCJS");
                String cpzsm = cpxxMap.get("CPZSM");

                String updatesql = "update t_zz_bzgl set kcjs =(kcjs- '" + ccjs + "') where cpzsm ='" + cpzsm + "'";

                cpxxMap.put("PID", masterId);
                cpxxMap.remove("COL_010");
                cpxxMap.remove("KCJS");
                cpxxMap.put("QYBM", companyCode);
                String bzNewId = save("T_ZZ_CCBZCPXX", cpxxMap, null);
                newIDs.append(bzNewId + ",");
                String sql = "update t_zz_bzgldymx set is_out = '1' where cpzsm = '" + cpxxMap.get("CPZSM") + "'";
                DatabaseHandlerDao.getInstance().executeSql(sql);
                DatabaseHandlerDao.getInstance().executeSql(updatesql);
                updateBzCcFlag(String.valueOf(cpzsm));
                /****************同步追溯信息*****************/
                TCsptZsxxEntity entity = new TCsptZsxxEntity();
                entity.setZsm(cpxxMap.get("ZSM"));
                entity.setJhpch(cpxxMap.get("ZSPCH"));
                entity.setQybm(ccglMap.get("QYBM"));
                entity.setQymc(CompanyInfoUtil.getInstance().getCompanyName("ZZ", ccglMap.get("QYBM")));
                entity.setXtlx("1");
                entity.setRefId(bzNewId);
//                entity.setZZYZPCH(cpxxMap.get("CSPCH").toString());
                TraceChainUtil.getInstance().syncZsxx(entity);
                /****************同步追溯信息*****************/
            }
            sb = new StringBuilder("SELECT * FROM t_zz_ccbzcpxx T WHERE T.PID = '" + masterId + "' AND T.IS_DELETE <> '1'");
            for(Map<String, Object> map : DatabaseHandlerDao.getInstance().queryForMaps(sb.toString())){
                if(!newIDs.toString().contains(String.valueOf(map.get("ID")))){
                    sb = new StringBuilder("update t_zz_bzgl set kcjs =(kcjs + '" + map.get("CCJS") + "') where cpzsm ='" + map.get("CPZSM") + "'");
                    DatabaseHandlerDao.getInstance().executeSql(sb.toString());
                    updateBzCcFlag(String.valueOf(map.get("CPZSM")));
                    sb = new StringBuilder("update t_zz_ccbzcpxx t set t.is_delete = '1' where id = '" + map.get("ID") + "'");
                    DatabaseHandlerDao.getInstance().executeSql(sb.toString());
                }
            }
        }

        return masterId;

    }

    @Transactional
    public Object updateBzCcFlag(String cpzsm){
        StringBuilder sql = new StringBuilder("SELECT * FROM T_ZZ_BZGL T WHERE T.IS_DELETE <> '1' AND T.CPZSM = '" + cpzsm + "'");
        if("0".equals(String.valueOf(DatabaseHandlerDao.getInstance().queryForMap(sql.toString()).get("KCJS")))){
            sql = new StringBuilder("UPDATE T_ZZ_BZGL T SET T.IS_OUT = '1' WHERE T.CPZSM = '" + cpzsm + "' AND T.IS_DELETE <> '1'");
            DatabaseHandlerDao.getInstance().executeSql(sql.toString());
        }else{
            sql = new StringBuilder("UPDATE T_ZZ_BZGL T SET T.IS_OUT = '0' WHERE T.CPZSM = '" + cpzsm + "' AND T.IS_DELETE <> '1'");
            DatabaseHandlerDao.getInstance().executeSql(sql.toString());
        }
        return null;
    }

}
