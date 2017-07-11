package com.ces.component.zzfalb.service;

import com.ces.component.trace.utils.SerialNumberUtil;
import com.ces.config.dhtmlx.dao.common.DatabaseHandlerDao;
import com.ces.config.utils.AppDefineUtil;
import org.springframework.stereotype.Component;

import com.ces.component.trace.dao.TraceShowModuleDao;
import com.ces.component.trace.service.base.TraceShowModuleDefineDaoService;
import com.ces.xarch.core.entity.StringIDEntity;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.*;

@Component
public class ZzfalbService extends TraceShowModuleDefineDaoService<StringIDEntity, TraceShowModuleDao> {

    /**
     * 默认权限过滤
     *
     * @return
     */
    public String defaultCode() {
        String qybm = SerialNumberUtil.getInstance().getCompanyCode();
        String defaultCode = " ";
        if (qybm != null && !"".equals(qybm))
            defaultCode = AppDefineUtil.RELATION_AND + " QYBM = '" + qybm
                    + "' " + AppDefineUtil.RELATION_AND + "IS_DELETE<>'1'";
        return defaultCode;
    }

    @Override
    protected String buildCustomerFilter(String tableId,
                                         String componentVersionId, String moduleId, String menuId,
                                         Map<String, Object> paramMap) {
        // 返回过滤条件时，要以AppDefineUtil.RELATION_AND、AppDefineUtil.RELATION_OR开头，如下所示：
        return defaultCode();
    }

    public void deleteZzfa(String[] idArray) {
        for (String id : idArray) {
            Object[] param = new Object[]{id};
            String zzfaSql = "update T_ZZ_FAPZ set is_delete = '1' where id = ?";
            DatabaseHandlerDao.getInstance().executeSql(zzfaSql, param);

            deleteNsxAndTrp("bzfa", param);
            deleteNsxAndTrp("gg", param);
            deleteNsxAndTrp("sf", param);
            deleteNsxAndTrp("yy", param);
            deleteNsxAndTrp("jc", param);
            deleteNsxAndTrp("cc", param);
            deleteNsxAndTrp("cs", param);
            deleteNsxAndTrp("qt", param);
        }
        ;
    }

    //复制种植方案
    public Object copyZzfa(String dataId) {
        Object[] param = new Object[]{dataId};
        String zzfaSql = "select t.ycdm,t.ycmc,t.zzfamc,t.zzzmmc,t.zzzmbh,t.fasm,t.qybm from t_zz_fapz t where t.id = ?";
        String bzfaSql = "select t.id,t.nszyxbh,t.nszyxmc,t.nsxjgsj,t.is_start, t.zpfs,t.yl,t.cfcs,t.cfjgsj,t.is_start1 from t_zz_bzfa t where t.pid = ? and t.is_delete <> '1'";
        String ggSql = "select t.id,t.nszyxbh,t.nszyxmc,t.nsxjgsj,t.is_start, t.ggfs,t.sylx,sydj,t.cfcs,t.cfjgsj,t.is_start1 from t_zz_gg t where t.pid = ? and t.is_delete <> '1'";
        String sfSql = "select t.id,t.nszyxbh,t.nszyxmc,t.nsxjgsj,t.is_start,t.sfsd,t.cfcs,t.cfjgsj,t.is_start1 from t_zz_sf t where t.pid = ? and t.is_delete <> '1'";
        String yySql = "select t.id,t.nszyxbh,t.nszyxmc,t.nsxjgsj,t.is_start,t.yysd,t.cfcs,t.cfjgsj,t.is_start1 from t_zz_yy t where t.pid = ? and t.is_delete <> '1'";
        String jcSql = "select t.id,t.nszyxbh,t.nszyxmc,t.nsxjgsj,t.is_start,t.jcff,t.cyff,t.jcdw from t_zz_jc t where t.pid = ? and t.is_delete <> '1'";
        String ccSql = "select t.id,t.nszyxbh,t.nszyxmc,t.nsxjgsj,t.is_start,t.ccfs,t.cfcs,t.cfjgsj,t.is_start1 from t_zz_cc t where t.pid = ? and t.is_delete <> '1'";
        String csSql = "select t.id,t.nszyxbh,t.nszyxmc,t.nsxjgsj,t.is_start,t.csfs,t.cfcs,t.cfjgsj,t.is_start1 from t_zz_cs t where t.pid = ? and t.is_delete <> '1'";
        String qtSql = "select t.id,t.nszyxbh,t.nszyxmc,t.nsxjgsj,t.is_start,t.cfcs,t.cfjgsj,t.is_start1 from t_zz_qt t where t.pid = ? and t.is_delete <> '1'";

        Map<String, Object> zzfaMap = DatabaseHandlerDao.getInstance().queryForMap(zzfaSql, param);
        List<Map<String, Object>> bzfaMaps = DatabaseHandlerDao.getInstance().queryForMaps(bzfaSql, param);
        List<Map<String, Object>> ggMaps = DatabaseHandlerDao.getInstance().queryForMaps(ggSql, param);
        List<Map<String, Object>> sfMaps = DatabaseHandlerDao.getInstance().queryForMaps(sfSql, param);
        List<Map<String, Object>> yyMaps = DatabaseHandlerDao.getInstance().queryForMaps(yySql, param);
        List<Map<String, Object>> jcMaps = DatabaseHandlerDao.getInstance().queryForMaps(jcSql, param);
        List<Map<String, Object>> ccMaps = DatabaseHandlerDao.getInstance().queryForMaps(ccSql, param);
        List<Map<String, Object>> csMaps = DatabaseHandlerDao.getInstance().queryForMaps(csSql, param);
        List<Map<String, Object>> qtMaps = DatabaseHandlerDao.getInstance().queryForMaps(qtSql, param);

        //保存种植方案
        Map<String, String> newZzfa = new HashMap<String, String>();
        for (Map.Entry<String, Object> mapEntry : zzfaMap.entrySet()) {
            if (mapEntry.getValue() != null)
                newZzfa.put(mapEntry.getKey(), mapEntry.getValue().toString());
        }
        newZzfa.put("ZZFABH", SerialNumberUtil.getInstance().getSerialNumber("ZZ", "ZZFABH", false));
        String masterId = save("T_ZZ_FAPZ", newZzfa, null);
        //保存播种方案
        String bzfaTrpSql = "select t.nszyxbh,t.trptym,t.yt,t.tjyl from t_zz_bzfatrp t where t.pid = ? and t.is_delete <> '1'";
        saveNsxAndTrp(masterId, bzfaMaps, bzfaTrpSql, "bzfa");
        //保存灌溉
        String ggTrpSql = "select t.nszyxbh,t.trptym,t.yt,t.tjyl from t_zz_ggtrp t where t.pid = ? and t.is_delete <> '1'";
        saveNsxAndTrp(masterId, ggMaps, ggTrpSql, "gg");
        //保存施肥
        String sffaTrpSql = "select t.nszyxbh,t.trptym,t.yt,t.tjyl from t_zz_sftrp t where t.pid = ? and t.is_delete <> '1'";
        saveNsxAndTrp(masterId, sfMaps, sffaTrpSql, "sf");
        //保存用药
        String yyTrpSql = "select t.nszyxbh,t.trptym,t.yt,t.tjyl from t_zz_yytrp t where t.pid = ? and t.is_delete <> '1'";
        saveNsxAndTrp(masterId, yyMaps, yyTrpSql, "yy");
        //保存检测
        String jcTrpSql = "select t.nszyxbh,t.trptym,t.yt,t.tjyl from t_zz_jctrp t where t.pid = ? and t.is_delete <> '1'";
        saveNsxAndTrp(masterId, jcMaps, jcTrpSql, "jc");
        //保存锄草
        String ccTrpSql = "select t.nszyxbh,t.trptym,t.yt,t.tjyl from t_zz_cctrp t where t.pid = ? and t.is_delete <> '1'";
        saveNsxAndTrp(masterId, ccMaps, ccTrpSql, "cc");
        //保存采收
        String csTrpSql = "select t.nszyxbh,t.trptym,t.yt,t.tjyl from t_zz_cstrp t where t.pid = ? and t.is_delete <> '1'";
        saveNsxAndTrp(masterId, csMaps, csTrpSql, "cs");
        //保存其他
        String qtTrpSql = "select t.nszyxbh,t.trptym,t.yt,t.tjyl from t_zz_qttrp t where t.pid = ? and t.is_delete <> '1'";
        saveNsxAndTrp(masterId, qtMaps, qtTrpSql, "qt");
        return masterId;
    }

    /**
     * 复制农事项and投入品
     *
     * @param masterId  配置方案id
     * @param nsxMaps   原农事项maps 包含id
     * @param getTrpSql 查询原投入品str,包含查询参数农事项id as ?
     * @param type      : bzfa,gg,sffa...
     */
    private void saveNsxAndTrp(String masterId, List<Map<String, Object>> nsxMaps, String getTrpSql, String type) {
        for (Map<String, Object> nsxMap : nsxMaps) {
            String oldNsxId = nsxMap.get("ID").toString();
            Map<String, String> newNsx = new HashMap<String, String>();
            for (Map.Entry<String, Object> mapEntry : nsxMap.entrySet()) {
                newNsx.put(mapEntry.getKey(), mapEntry.getValue()== null?"":mapEntry.getValue().toString());
            }
            newNsx.put("PID", masterId);
            newNsx.put("ID", "");
            newNsx.remove("YL");
            String newNsxId = save("t_zz_" + type, newNsx, null);
            List<Map<String, Object>> trpMaps = DatabaseHandlerDao.getInstance().queryForMaps(getTrpSql, new Object[]{oldNsxId});
            for (Map<String, Object> trpMap : trpMaps) {
                Map<String, String> newTrp = new HashMap<String, String>();
                for (Map.Entry<String, Object> trpMapEntry : trpMap.entrySet()) {
                    newTrp.put(trpMapEntry.getKey(), (trpMapEntry.getValue() == null ? "" : trpMapEntry.getValue()).toString());
                }
                newTrp.put("PID", newNsxId);
                newTrp.put("ID", "");
                save("t_zz_" + type + "trp", newTrp, null);
            }
        }
    }

    /**
     * 删除农事项及投入品
     *
     * @param type  bzfa,gg,sffa...
     * @param param 种植方案id
     */
    private void deleteNsxAndTrp(String type, Object[] param) {
        String nsxSql = "update t_zz_" + type + " set is_delete = '1' where pid = ?";
        String nsxIdsSql = "select id from t_zz_" + type + " where pid = ?";
        DatabaseHandlerDao.getInstance().executeSql(nsxSql, param);
        List<Map<String, Object>> maps = DatabaseHandlerDao.getInstance().queryForMaps(nsxIdsSql, param);
        for (Map<String, Object> map : maps) {
            String trpSql = "update t_zz_" + type + "trp set is_delete = '1' where pid = ?";
            DatabaseHandlerDao.getInstance().executeSql(trpSql, new Object[]{map.get("ID").toString()});
        }
    }

    public Object preView(String kind, String datepicker, String id) {
        StringBuilder sql = new StringBuilder("select *" +
                "  from (select a.zzfamc, b.nszyxmc, b.nsxjgsj, 'bz' as lx, b.is_start,b.cfcs,b.cfjgsj" +
                "          from t_zz_fapz a, t_zz_bzfa b" +
                "         where a.id = b.pid and b.is_delete <> '1'" +
                "           and a.id = ?" +
                "        union" +
                "        select a.zzfamc, b.nszyxmc, b.nsxjgsj, 'gg' as lx, b.is_start,b.cfcs,b.cfjgsj" +
                "          from t_zz_fapz a, t_zz_gg b" +
                "         where a.id = b.pid and b.is_delete <> '1'" +
                "           and a.id = ?" +
                "        union" +
                "        select a.zzfamc, b.nszyxmc, b.nsxjgsj, 'sf' as lx, b.is_start,b.cfcs,b.cfjgsj" +
                "          from t_zz_fapz a, t_zz_sf b" +
                "         where a.id = b.pid and b.is_delete <> '1'" +
                "           and a.id = ?" +
                "        union" +
                "        select a.zzfamc, b.nszyxmc, b.nsxjgsj, 'yy' as lx, b.is_start,b.cfcs,b.cfjgsj" +
                "          from t_zz_fapz a, t_zz_yy b" +
                "         where a.id = b.pid and b.is_delete <> '1'" +
                "           and a.id = ?" +
                "        union" +
                "        select a.zzfamc, b.nszyxmc, b.nsxjgsj, 'cs' as lx, b.is_start,b.cfcs,b.cfjgsj" +
                "          from t_zz_fapz a, t_zz_cs b" +
                "         where a.id = b.pid and b.is_delete <> '1'" +
                "           and a.id = ?" +
                "        union" +
                "        select a.zzfamc, b.nszyxmc, b.nsxjgsj, 'qt' as lx, b.is_start,b.cfcs,b.cfjgsj" +
                "          from t_zz_fapz a, t_zz_qt b" +
                "         where a.id = b.pid and b.is_delete <> '1'" +
                "           and a.id = ?" +
                "        union" +
                "        select a.zzfamc, b.nszyxmc, b.nsxjgsj, 'cc' as lx, b.is_start,b.cfcs,b.cfjgsj" +
                "          from t_zz_fapz a, t_zz_cc b" +
                "         where a.id = b.pid and b.is_delete <> '1'" +
                "           and a.id = ?) x");
        if (!"".equals(kind) && !"null".equals(kind) && !"all".equals(kind)) {
            sql.append(" where x.lx = '" + kind + "' ");
        }
        sql.append(" order by x.is_start desc,x.nsxjgsj asc");
        List<Map<String, Object>> nsxList = DatabaseHandlerDao.getInstance().queryForMaps(sql.toString(), new Object[]{id, id, id, id, id, id, id});
        if(nsxList.size() == 0) return nsxList;
        List<Map<String, Object>> newNsxList = new ArrayList<Map<String, Object>>();
        for(Map<String, Object> map : nsxList){
            newNsxList.add(map);
            for(int i = 1; i < Integer.parseInt(String.valueOf(map.get("CFCS"))) ; i++){
                Map<String, Object> shortMap = new HashMap<String, Object>();
                shortMap.putAll(map);
                shortMap.put("NSXJGSJ",Integer.parseInt(String.valueOf(map.get("NSXJGSJ"))) + i * Integer.parseInt(String.valueOf(map.get("CFJGSJ"))));
                newNsxList.add(shortMap);
            }
        }
        Map nsxs [] = new Map[newNsxList.size()];
        for(int i = 0; i < newNsxList.size(); i++){
            nsxs[i] = newNsxList.get(i);
        }
        for(int i = 1 ; i < nsxs.length ; i++){
            for(int j = i+1 ; j < nsxs.length ; j++){
                if(Integer.parseInt(String.valueOf(nsxs[i].get("NSXJGSJ"))) > Integer.parseInt(String.valueOf(nsxs[j].get("NSXJGSJ")))){
                    Map<String, Object> shortMap = nsxs[i];
                    nsxs[i] = nsxs[j];
                    nsxs[j] = shortMap;
                }
            }
        }
        nsxList.clear();
        for(int i = 0 ; i < nsxs.length; i++) {
            nsxList.add(nsxs[i]);
        }
        nsxList.get(0).put("NSXJGSJ", "0");
        if (!"".equals(datepicker) && !"null".equals(datepicker)) {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            try {
                for (int i = 0; i < nsxList.size(); i++) {
                    Date date = null;
                    date = format.parse(datepicker);
                    String nsxjgsj = format.format(new Date(date.getTime() + Long.parseLong(String.valueOf(nsxList.get(i).get("NSXJGSJ"))) * 24 * 60 * 60 * 1000));
                    nsxList.get(i).put("NSXJGSJ", nsxjgsj);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if(nsxList.size() != 0)
        nsxList.get(nsxList.size()-1).put("IS_END","1");
        return nsxList;
    }

    @Transactional
    public Object getZyxbh(String id, String tablename){
        StringBuilder sql = new StringBuilder("SELECT T.NSZYXBH FROM " + tablename + " T WHERE T.PID = '" + id + "' ORDER BY T.NSZYXBH ASC");
        List<Map<String, Object>> dataList = DatabaseHandlerDao.getInstance().queryForMaps(sql.toString());
        for(Map<String, Object> map : dataList){
            map.put("text", map.get("NSZYXBH"));
            map.put("value", map.get("NSZYXBH"));
        }
        return dataList;
    }

}