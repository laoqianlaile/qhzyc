package com.ces.component.zznsxgz.service;

import com.ces.component.trace.utils.SerialNumberUtil;
import com.ces.component.trace.dao.TraceShowModuleDao;
import com.ces.config.dhtmlx.dao.common.DatabaseHandlerDao;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;
import com.ces.config.utils.StringUtil;
import com.ces.component.trace.service.base.TraceShowModuleDefineDaoService;
import com.ces.xarch.core.entity.StringIDEntity;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Component
public class ZznsxgzService extends TraceShowModuleDefineDaoService<StringIDEntity, TraceShowModuleDao> {

    public Object nsxgzSearch(PageRequest pageRequest, Map<String, String> queryParam) {
        String qybm = SerialNumberUtil.getInstance().getCompanyCode();
        StringBuilder sql = new StringBuilder("select v.* from v_zz_nsxgz v where v.qybm='" + qybm + "' ");
        if (StringUtil.isNotEmpty(queryParam.get("ygStart"))) {
            sql.append(" and to_date(v.yjzysj,'yyyy-mm-dd')>=to_date('" + queryParam.get("ygStart") + "','yyyy-mm-dd')");
        }
        if (StringUtil.isNotEmpty(queryParam.get("ygEnd"))) {
            sql.append(" and to_date(v.yjzysj,'yyyy-mm-dd')<=to_date('" + queryParam.get("ygEnd") + "','yyyy-mm-dd')");
        }
        if (StringUtil.isNotEmpty(queryParam.get("wcStart"))) {
            sql.append(" and to_date(substr(v.jssj,1,10),'yyyy-mm-dd')>=to_date('" + queryParam.get("wcStart") + "','yyyy-mm-dd')");
        }
        if (StringUtil.isNotEmpty(queryParam.get("wcEnd"))) {
            sql.append(" and to_date(substr(v.jssj,1,10),'yyyy-mm-dd')<=to_date('" + queryParam.get("wcEnd") + "','yyyy-mm-dd')");
        }
        if (StringUtil.isNotEmpty(queryParam.get("nsxlx"))) {
            sql.append(" and v.nsxlx = '" + queryParam.get("nsxlx") + "'");
        }
        if (StringUtil.isNotEmpty(queryParam.get("fzr"))) {
            sql.append(" and v.fzrbh = '" + queryParam.get("fzr") + "'");
        }
        if (StringUtil.isNotEmpty(queryParam.get("dkmc"))) {
            sql.append(" and v.dkmc like '%" + queryParam.get("dkmc") + "%'");
        }
        if (StringUtil.isNotEmpty(queryParam.get("pjzt"))) {
            if (queryParam.get("pjzt").equals("2")) sql.append(" and v.pj is not null");
            else if (queryParam.get("pjzt").equals("3")) sql.append(" and v.pj is null");
        }
        if (StringUtil.isNotEmpty(queryParam.get("rwzt"))) {
            if (queryParam.get("rwzt").equals("2")) sql.append(" and v.zt = '2'");
            else if (queryParam.get("rwzt").equals("3")) sql.append(" and v.zt = '3'");
        }
        sql.append(" order by  v.zt desc,v.SCDABH desc,v.yjzysj desc ");
        return queryPage(pageRequest, sql.toString());
    }

    /**
     * 分页查询
     *
     * @param pageRequest
     * @param sql
     * @return Object
     */
    private Object queryPage(PageRequest pageRequest, String sql) {
        if (StringUtil.isEmpty(sql)) {
            return null;
        }
        //查总数
       // String count = "select count(*) as count from (" + sql + ")";
        //System.out.println("countSql:" + count);
       // Map map = DatabaseHandlerDao.getInstance().queryForMap(count);
        List list = DatabaseHandlerDao.getInstance().queryForMaps(sql);
        //总数
//        double total = Double.parseDouble(map.get("COUNT").toString());
        double total = list.size();
        int pagesize = pageRequest.getPageSize();
        //总页数
        int totalPages = 0;
        if (pagesize == 0) {
            totalPages = (int) Math.ceil(total);
        } else {
            totalPages = (int) Math.ceil(total / pagesize);
        }
        int begin = pageRequest.getOffset();
        int end = begin + pagesize;
        if (begin > total) {
            int remainder = (int) (total % pageRequest.getPageSize());
            end = (int) total;
            begin = (int) (total - (remainder == 0 ? pageRequest.getPageSize() : remainder));
        }

        List<Map<String, Object>> data = DatabaseHandlerDao.getInstance().pageMaps(sql, begin, end);
        if (data == null) {
            return null;
        } else {
            Map<String, Object> resultMap = new HashMap<String, Object>();
            resultMap.put("data", data);
            resultMap.put("total", (int) total);
            resultMap.put("totalPages", totalPages);
            resultMap.put("pageNumber", pageRequest.getPageNumber() + 1);
            return resultMap;
        }
    }

    public Object getFzr() {
        String qybm = SerialNumberUtil.getInstance().getCompanyCode();
        String sql = "select distinct t.xm as dkfzr,t.gzrybh as dkfzrbh from t_zz_gzryda t where t.qybm=?";
        return DatabaseHandlerDao.getInstance().queryForMaps(sql, new Object[]{qybm});
    }

    public void deleteNsx(List<Map<String, String>> maps) {
        for (Map<String, String> map : maps) {
            String tableName = getTable(map.get("lx"));
            //删除农事项
            String sql = "update " + tableName + " set is_delete = '1' where id = '" + map.get("id").toString() + "'";
            DatabaseHandlerDao.getInstance().executeSql(sql);
            //删除农事项投入品
            String sqlTrp = "update " + tableName + "TRP set is_delete = '1' where pid = '" + map.get("id").toString() + "'";
            DatabaseHandlerDao.getInstance().executeSql(sqlTrp);
        }
    }

    public void judgeNsx(String ids, String pj) {
        String[] idArray = ids.split(",");
        String condition = "('!'";
        for (String id : idArray) {
            condition += ",'" + id + "'";
        }
        condition += ")";
        String sql = "update t_zz_czjl set pj = '" + pj + "' where pid in " + condition;
        DatabaseHandlerDao.getInstance().executeSql(sql);
    }

    public void updateNsx(String id, String ygsj, String fzr, String fzrbh, String lx) {
        String tableName = getTable(lx);
        String sql = "";
        //在前台判断是否已操作,此处判断是否为起始
        //判断是否为未操作的起始农事项(若是,只修改预估时间1和预估时间2,否则另修改间隔时间)
        String isStartSql = "select qsnsx from " + tableName + " where id = '" + id + "'";
        Map<String, Object> map = DatabaseHandlerDao.getInstance().queryForMap(isStartSql);
        if (map.get("QSNSX").toString().equals("1")) {
            sql ="update "+tableName+" set  ygsj1   = to_date('"+ygsj+"', 'yyyy-mm-dd') - czfdsj\\:\\:integer," +
                    "       ygsj2   = to_date('"+ygsj+"', 'yyyy-mm-dd') + czfdsj\\:\\:integer," +
                    "       fzr = '" + fzr + "'," +
                    "       fzrbh = '" + fzrbh + "'" +
                    " where id = '"+id+"'";
            //修改主表生产档案起始农事项时间
            String setStart = "update t_zz_scda set qsnsxsj = '" + ygsj + "' where id = (select pid from " + tableName + " where id = '" + id + "')";
            DatabaseHandlerDao.getInstance().executeSql(setStart);
        } else {
            sql = "update " + tableName + " t" +
                    "   set nsxjgsj = nsxjgsj +" +
                    "                   (to_date('" + ygsj + "', 'yyyy-mm-dd') -" +
                    "                   (to_date(ygsj1, 'yyyy-mm-dd') +  czfdsj\\:\\:integer))," +
                    "       ygsj1   = to_date('" + ygsj + "', 'yyyy-mm-dd') -" +
                    "                           czfdsj\\:\\:integer," +
                    "       ygsj2   = to_date('" + ygsj + "', 'yyyy-mm-dd') +" +
                    "                           czfdsj\\:\\:integer," +
                    "       fzr = '" + fzr + "'," +
                    "       fzrbh = '" + fzrbh + "'" +
                    " where id = '" + id + "'";
        }
        DatabaseHandlerDao.getInstance().executeSql(sql);
    }

    private String getTable(String lx) {
        String tableName = "";
        if (lx.equals("播种")) {
            tableName = "T_ZZ_SCBZ";
        } else if (lx.equals("灌溉")) {
            tableName = "T_ZZ_SCGG";
        } else if (lx.equals("施肥")) {
            tableName = "T_ZZ_SCSF";
        } else if (lx.equals("用药")) {
            tableName = "T_ZZ_SCYY";
        } else if (lx.equals("采收")) {
            tableName = "T_ZZ_SCCS";
        } else if (lx.equals("锄草")) {
            tableName = "T_ZZ_SCCC";
        } else if (lx.equals("其他")) {
            tableName = "T_ZZ_SCQT";
        }
        return tableName;
    }

    @Transactional
    public Object getCzr(){
        StringBuilder sql = new StringBuilder("SELECT T.GZRYBH AS VALUE,T.XM AS TEXT FROM T_ZZ_GZRYDA T WHERE T.IS_DELETE <> '1' AND T.QYZT = '1' AND T.QYBM = ? ");
        List<Map<String, Object>> dataList = DatabaseHandlerDao.getInstance().queryForMaps(sql.toString(), new Object[]{SerialNumberUtil.getInstance().getCompanyCode()});
        return dataList;
    }

    @Transactional
    public Object sjwhUpdate(String kssj, String jssj, String czrbh, String czr, String id){
        StringBuilder sql = new StringBuilder("UPDATE T_ZZ_CZJL T SET T.KSSJ = '" + kssj + "',T.JSSJ = '" + jssj + "',T.CZRBH = '" + czrbh + "',T.CZR = '" + czr + "' WHERE T.PID = '" + id + "'");
        DatabaseHandlerDao.getInstance().executeSql(sql.toString());
        return null;
    }
}