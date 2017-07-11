package com.ces.component.jyzxx.service;

import java.text.SimpleDateFormat;
import java.util.*;

import com.ces.component.trace.utils.SerialNumberUtil;
import com.ces.config.dhtmlx.entity.database.Database;
import freemarker.core.ReturnInstruction;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.ces.component.trace.dao.TraceShowModuleDao;
import com.ces.config.dhtmlx.dao.common.DatabaseHandlerDao;
import com.ces.component.trace.service.base.TraceShowModuleDefineDaoService;
import com.ces.config.utils.AppDefineUtil;
import com.ces.config.utils.JsonUtil;
import com.ces.xarch.core.entity.StringIDEntity;
import com.fasterxml.jackson.databind.JsonNode;

import javax.xml.crypto.Data;

@Component
public class JyzxxService extends TraceShowModuleDefineDaoService<StringIDEntity, TraceShowModuleDao> {
	
	
	/**
	 * 获取供应商信息
	 * @return
	 */
	public Map<String,Object> getGysxxData(String id){
		String sql = "SELECT * FROM T_CS_SCJCXX WHERE ID='"+id+"'";
		Map<String,Object> map = (Map<String,Object>)DatabaseHandlerDao.getInstance().queryForMap(sql);		
		return map;
	}
	
    public Object validateIdno(String idno) {
        String sql = "select * from t_common_jyz where GSZCDJZHHSFZH = '" + idno + "'";
        Map<String, Object> map = DatabaseHandlerDao.getInstance().queryForMap(sql);
        if (map.isEmpty()) {
            return "NONE";
        } else {
            return map;
        }
    }
    
    
    
    @Transactional
    public Map<String, String> saveData(String tableId, String entityJson,
                                        Map<String, Object> paramMap, boolean isNew) {
        String tableName = "T_COMMON_JYZ";//父表经营者基本信息表
        String childTableName = "T_COMMON_JYZ_DETAIL";//z子表经营者信息表
        String copyJson = entityJson;
        JsonNode entityNode = JsonUtil.json2node(entityJson);
        Map<String, String> dataMap = node2map(entityNode);
        Map<String, String> map1 = new HashMap<String, String>();//存放父表信息
        Map<String, String> map2 = new HashMap<String, String>();//存放子表信息
        String dataId = dataMap.get("ID");//获得页面视图中拼接的ID 例如：124125125125_ywqoqyuquuq 以 “_”  进行拼接
        String pid = "";
        String cid = "";
        if (dataId != null && !"".equals(dataId)) {//是修改操作，把ID提取出来
            String[] ids = dataId.split("_");
            pid = ids[0];
            map1.put("ID", pid);
            cid = ids[1];
            map2.put("ID", cid);
        } else if (!isNew) {
            map1.put("ID", getJyzId(dataMap.get("A_GSZCDJZHHSFZH")));
        }
        //如果是第一次备案在父表中插入一条备案数据，以后再其他子系统中如果需要再次备案，只需录入身份证号进行数据获取，
        //在进行相关信息的修改即可无须重复录入重复记录
        /***开始填加父表信息****/
        map1.put("JYZMC", dataMap.get("A_JYZMC"));//经营者名称
        map1.put("JYZBM", dataMap.get("A_JYZBM"));//企业编码
        map1.put("GSZCDJZHHSFZH", dataMap.get("A_GSZCDJZHHSFZH"));//工商登记证号或身份证号
        map1.put("JYZXZ", dataMap.get("A_JYZXZ"));//经营者性质
        map1.put("FRDB", dataMap.get("A_FRDB"));//法人代表
        map1.put("BARQ", dataMap.get("A_BARQ"));//备案日期
        //保存父表记录
        pid = super.saveOne(tableName, map1);
        /*****结束父表信息的添加**************/

        /***开始填加子表信息****/
        map2.put("SJHM", dataMap.get("B_SJHM"));//手机号
        map2.put("JYLX", dataMap.get("B_JYLX"));//经营类型
        map2.put("ZT", dataMap.get("B_ZT"));//状态
        map2.put("QYBM", dataMap.get("B_QYBM"));//企业编码
        map2.put("XTLX", dataMap.get("B_XTLX"));//系统类型
        map2.put("RECORD", dataMap.get("B_RECORD"));//是否备案 1代表是第一次备案 0代表不是第一次备案
        map2.put("PID", pid);
        /*****结束子表信息的添加**************/
        //保存子表记录
        cid = super.saveOne(childTableName, map2);
        dataMap.put(AppDefineUtil.C_ID, pid + "_" + cid);
        return dataMap;
    }

    /**
     * 根据身份证号进行相关的数据查询获得 对应父表中的ID
     *
     * @param gszcdjzhhsfzh
     * @return
     */
    public String getJyzId(String gszcdjzhhsfzh) {
        String sql = "select id from t_common_jyz t where t.GSZCDJZHHSFZH = '" + gszcdjzhhsfzh + "'";
        return DatabaseHandlerDao.getInstance().queryForObject(sql).toString();
    }

    /**
     * 根据不同系统，不同企业，不同状态 获得对应的经营者信息
     *
     * @param zt   经营者状态 1启用 2禁用
     * @param xtlx 系统状态 ：PC:蔬菜批发市场 PR:肉品批发市场 TZ:屠宰市场
     * @return 下拉框列表数据 ：Map<String,Object>
     */
    public Map<String, Object> getComJyzxx(String zt, String xtlx, String jyzbm) {
        //根据视图筛选满足条件的信息
        String sql = "select distinct * from (" +
                "select   " +
                "t.A_JYZBM as JYZBM," +
                "t.A_JYZMC as JYZMC," +
                "z.name as JYLX " +
                "from " +
                "V_COMMON_JYZ_COMMON_JYZ_DETAIL t,T_XTPZ_CODE z " +
                "where " +
                "z.code_type_code ='JYZLX' " +
                "and (t.B_JYLX=z.value) " +
                " " +
                "and t.B_XTLX=? ";
        String qybm = getQybm();
        List<String> param = new ArrayList<String>();
        //param.add(qybm);
        param.add(xtlx);
        if (null != zt && !"".equals(zt)) {
            param.add(zt);
            sql += " and t.B_ZT=? ";
        }
        if (null != jyzbm && !"".equals(jyzbm)) {
            param.add(jyzbm);
            sql += " and t.A_JYZBM <> ? ";
        }
        sql += "AND T.B_QYBM = '" + qybm + "' order by A_JYZBM DESC)";
        List<Map<String, Object>> dataList = DatabaseHandlerDao.getInstance().queryForMaps(sql, param.toArray());
        //封装下拉列表格式的数据
        Map<String, Object> mapData = new HashMap<String, Object>();
        mapData.put("data", dataList);
        return mapData;
    }

    /**
     * 获得当前登录用户企业编码
     *
     * @return
     */
    public String getQybm() {
        return SerialNumberUtil.getInstance().getCompanyCode();
    }

    /**
     * 默认权限过滤
     *
     * @return
     */
    public String defaultCode() {
        String code = SerialNumberUtil.getInstance().getCompanyCode();
        String defaultCode = " ";
        if (code != null && !"".equals(code))
            defaultCode = AppDefineUtil.RELATION_AND + " B_QYBM = '" + code + "' ";
        return defaultCode;
    }

    @Override
    protected String buildCustomerFilter(String tableId,
                                         String componentVersionId, String moduleId, String menuId,
                                         Map<String, Object> paramMap) {
        // 返回过滤条件时，要以AppDefineUtil.RELATION_AND、AppDefineUtil.RELATION_OR开头，如下所示：
        return defaultCode();
    }

    @Override
    public void delete(String tableId, String dTableIds, String ids, boolean isLogicalDelete, Map<String, Object> paramMap) {
        String[] dataIds = new String[]{};
        String deleteId = "";
        if (ids != null && !"".equals(ids)) {//是修改操作，把ID提取出来
            dataIds = ids.split(",");
            int length = dataIds.length;
            for (int i = 0; i < length; i++) {
                deleteId += dataIds[i].split("_")[1];
                if (i < length - 1) {
                    deleteId += ",";
                }
            }
        }
        String childTableName = "T_COMMON_JYZ_DETAIL";//z子表经营者信息表
        String sql = " delete from " + childTableName + " where ID IN ('" + deleteId.replace(",", "','") + "')";
        DatabaseHandlerDao.getInstance().executeSql(sql);
    }

    public Object getJyzxxByBmAndQybm(String jyzbm) {
        String qybm = SerialNumberUtil.getInstance().getCompanyCode();
        //根据条码带过来的经营者编码查询是否存在此经营者
        String sql = "select * from t_common_jyz t where t.jyzbm = '" + jyzbm + "'";
        Map<String, Object> zbMap = DatabaseHandlerDao.getInstance().queryForMap(sql);
        if (zbMap.isEmpty()) {
            if (jyzbm.length() == 9) {
                zbMap.putAll(addZzjyz(jyzbm));
            } else {
                zbMap.put("result","FATAL");
                return zbMap;
            }
        }
        String id = zbMap.get("ID").toString();
        //根据当前企业编码和经营者主表ID获得当前系统下面的经营者编码
        String sql2 = "select id from t_common_jyz_detail where qybm = '" + qybm + "' and pid = '" + id + "'";
        List<Map<String, Object>> maps = DatabaseHandlerDao.getInstance().queryForMaps(sql2);
        //当前系统中没有此经营者返回主表关联ID
        if (maps.isEmpty()) {
            zbMap.put("result","NOTIN");
            return zbMap;
        }
        //当前系统已存在此经营者
        zbMap.put("result", "SUCCESS");
        return zbMap;
    }

    /**
     * 添加种植企业作为经营者
     *
     * @param jyzbm 此处传值为种植企业的企业编码
     */
    private Map<String,String> addZzjyz(String jyzbm) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");//设置日期格式
        String now = df.format(new Date());
        String sql = "select * from t_zz_cdda where qybm='" + jyzbm + "'";
        Map<String,Object> qyxxMap = DatabaseHandlerDao.getInstance().queryForMap(sql);
        Map<String,String> jyzxxMap = new HashMap<String, String>();
        jyzxxMap.put("JYZMC", qyxxMap.get("QYMC") == null ? "" : qyxxMap.get("QYMC").toString());
        jyzxxMap.put("GSZCDJZHHSFZH",qyxxMap.get("GSZCDJZH") == null ? "" : qyxxMap.get("GSZCDJZH").toString());
        jyzxxMap.put("JYZXZ","1");
        jyzxxMap.put("FRDB",qyxxMap.get("FDDB") == null ? "" : qyxxMap.get("FDDB").toString());
        jyzxxMap.put("BARQ",now);
        jyzxxMap.put("JYZBM",jyzbm);
        String id = saveOne("T_COMMON_JYZ",jyzxxMap);
        jyzxxMap.put("ID",id);
        return jyzxxMap;
    }

    public Object createJyz(String id, String xtlx, String barCode) {
        String qybm = SerialNumberUtil.getInstance().getCompanyCode();
        String tmh = barCode;
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        Map<String, String> map = new HashMap<String, String>();
        Map<String, Object> dataMap = new HashMap<String, Object>();
        String jyzbm = "";
        String oldqybm = "";
        if (String.valueOf(barCode).length() > 2) {
            barCode = barCode.substring(0, 2);
        }
        String sql = "";
        //查询上家企业编码
        if ("ZZ".equals(barCode)) {
            map.put("jylx", "1");
        } else {
            if ("PR".equals(barCode)) {
                sql = "select pfscbm as oldqybm" +
                        " from t_pr_jyxx t" +
                        " where jytmh = '" + tmh + "'";
            } else if ("TZ".equals(barCode)) {
                sql = "select tzcbm as oldqybm" +
                        " from t_tz_jyxx a, t_tz_jymxxx b" +
                        " where a.id = b.pid" +
                        " and jytmh = '" + tmh + "'";
            } else if ("PC".equals(barCode)) {
                sql = "select pfscbm as oldqybm" +
                        " from t_pc_jyxx a, t_pc_jymxxx b" +
                        " where a.id = b.t_pc_jyxx_id" +
                        " and jytmh = '" + tmh + "'";
            }
            list = DatabaseHandlerDao.getInstance().queryForMaps(sql);
            oldqybm = String.valueOf(list.get(0).get("OLDQYBM"));
            sql = "select * from t_common_jyz where id = '" + id + "'";
            dataMap = DatabaseHandlerDao.getInstance().queryForMap(sql);
            jyzbm = String.valueOf(dataMap.get("JYZBM"));
            sql = "select JYLX" +
                    " from t_common_jyz a" +
                    " left join t_common_jyz_detail b" +
                    " on a.id = b.pid" +
                    " where b.xtlx = '" + barCode + "'" +
                    " and a.jyzbm = '" + jyzbm + "'" +
                    " and b.qybm ='" + oldqybm + "'";
            dataMap = DatabaseHandlerDao.getInstance().queryForMap(sql);
            map.put("jylx", String.valueOf(dataMap.get("JYLX")));
        }
        map.put("QYBM", qybm);
        map.put("PID", id);
        map.put("XTLX", xtlx);
        //map.put("JYZBM",SerialNumberUtil.getInstance().getSerialNumber("COMMON","JYZBM",false,true));
        String resultId = saveOne("T_COMMON_JYZ_DETAIL", map);
        return resultId;
    }

    public Object isInCurrentQy(String idno) {
        String qybm = SerialNumberUtil.getInstance().getCompanyCode();
        String sql = "select id from t_common_jyz where GSZCDJZHHSFZH = ?";
        Map<String, Object> map = DatabaseHandlerDao.getInstance().queryForMap(sql, new Object[]{idno});
        String masterId = map.get("ID").toString();
        String sql1 = "select id from t_common_jyz_detail where qybm = ? and pid = ?";
        List result = DatabaseHandlerDao.getInstance().queryForList(sql1, new Object[]{qybm, masterId});
        if (result.size() > 0) {
            return "YES";
        } else {
            return "NO";
        }
    }

    public Object getJyzInfo(String jyzbm) {
        String qybm = getQybm();
        String sql = "select * from (" +
                "select   " +
                "t.A_JYZBM as JYZBM," +
                "t.A_JYZMC as JYZMC," +
                "t.A_GSZCDJZHHSFZH as GSZCDJZHHSFZH," +
                "t.A_JYZXZ as JYZXZ," +
                "t.A_FRDB as FRDB," +
                "t.A_BARQ as BARQ," +
                "t.B_JYLX as JYLX," +
                "t.B_SJHM as SJHM " +
                "from " +
                "V_COMMON_JYZ_COMMON_JYZ_DETAIL t " +
                "where " +
                " t.B_QYBM=? and B_ZT = '1' and t.A_JYZBM = ? )";
        Map<String, Object> map = DatabaseHandlerDao.getInstance().queryForMap(sql, new Object[]{qybm, jyzbm});
        return map;
    }

    public Object getXgxx(String id){
        String sql ="select * from T_TZ_RPJYJYXX where id = '"+id+"'";
        Map<String,Object> map=DatabaseHandlerDao.getInstance().queryForMap(sql);
        return map;
    }
}