package com.ces.component.zztrplylb.service;

import com.ces.component.trace.utils.SerialNumberUtil;
import com.ces.config.dhtmlx.dao.common.DatabaseHandlerDao;
import com.ces.config.utils.*;
import com.fasterxml.jackson.databind.JsonNode;
import org.apache.struts2.ServletActionContext;
import org.hibernate.Session;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

import com.ces.component.trace.dao.TraceShowModuleDao;
import com.ces.component.trace.service.base.TraceShowModuleDefineDaoService;
import com.ces.xarch.core.entity.StringIDEntity;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpSession;
import java.text.SimpleDateFormat;
import java.util.*;

@Component
public class ZztrplylbService extends TraceShowModuleDefineDaoService<StringIDEntity, TraceShowModuleDao> {

    public static final String tableName = "T_ZZ_TRPCKJBXX", dTableName = "T_ZZ_TRPCKMX";

    /**
     * 查询工作人员信息列表
     *
     * @return
     */
    public Object searchGzryxx() {
        String sql = "select gzrybh,xm from t_zz_gzryda t where " + defaultCode() + " order by t.gzrybh desc";
        return getGridAndListData(DatabaseHandlerDao.getInstance().queryForMaps(sql));
    }

    /**
     * 根据投入品类型查询投入品采购信息
     *
     * @param trplx
     * @return
     */
    public Object searchTrpmc(String trplx) {
        String sql = "select * from t_zz_trpcggl t where 1=1 ";
        if (trplx != "" && !"".equals(trplx)) {
            sql += " and t.trplx='" + trplx+"' ";
        }
        sql += defaultCode();
        List<Map<String, Object>> dataMap = DatabaseHandlerDao.getInstance().queryForMaps(sql);
        return dataMap;
    }

    public Map<String, Object> searchTrpxx(String trpbh) {
        String sql = "select * from t_zz_trpcggl t where trpbh=?" + defaultCode();
        Map<String, Object> dataMap = DatabaseHandlerDao.getInstance().queryForMap(sql, new String[]{trpbh});
        return dataMap;
    }

    /**
     * 查询投入品出库信息
     *
     * @param id
     * @return
     */
    public Object searchTrpckxx(String id) {
        String sql = "select * from " + tableName + " t where t.id='" + id + "'";
        return DatabaseHandlerDao.getInstance().queryForMap(sql);
    }

    /**
     * 根据主表ID获得投入品出库详细信息
     *
     * @param pid
     * @return
     */
    public Object searchTrpckxxmx(String pid) {
        String sql = "select * from " + dTableName + " t where t.pid='" + pid + "'";
        return getGridAndListData(DatabaseHandlerDao.getInstance().queryForMaps(sql));
    }

    /**
     * combobox与combogrid数据格式化处理
     *
     * @param dataMap
     * @return
     */
    public Map<String, Object> getGridAndListData(List<Map<String, Object>> dataMap) {
        Map<String, Object> data = new HashMap<String, Object>();
        data.put("data", dataMap);
        return data;
    }

    /**
     * 查询区域负责人信息列表
     *
     * @return
     */
    public Object searchQyfzr() {
//        String sql="select distinct ZZDYGLY,ZZDYGLYBH from v_sczz_trplygj  where qybm='"+SerialNumberUtil.getInstance().getCompanyCode()+"' and ZZDYGLY is not null and ZZDYGLYBH is not null ";
        String sql="select distinct FZR AS ZZDYGLY,FZRBH AS ZZDYGLYBH from v_zz_nsxgz  where qybm='"+SerialNumberUtil.getInstance().getCompanyCode()+"'";
//        String sql = "select distinct t.fzr,t.fzrbh from t_zz_qyxx t where 1=1 " + defaultCode();
        List<Map<String, Object>> dataList = DatabaseHandlerDao.getInstance().queryForMaps(sql);
        return getGridAndListData(dataList);
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
            defaultCode = AppDefineUtil.RELATION_AND + " QYBM = '" + code + "'" + AppDefineUtil.RELATION_AND + " is_delete != '1' ";
        return defaultCode;
    }

    @Override
    protected String buildCustomerFilter(String tableId,
                                         String componentVersionId, String moduleId, String menuId,
                                         Map<String, Object> paramMap) {
        // 返回过滤条件时，要以AppDefineUtil.RELATION_AND、AppDefineUtil.RELATION_OR开头，如下所示：
        return defaultCode();
    }

    /**
     * 保存出库信息
     *
     * @param entityJson
     * @param pEntitiesJson
     * @param dEntitiesJson
     * @return
     */
    @Transactional
    public Object saveTrplyxx(String entityJson, String pEntitiesJson, String dEntitiesJson) {
        Map<String, Object> returnData = new HashMap<String, Object>();
        boolean inserted = false;
        //父表json数据
        JsonNode entity = JsonUtil.json2node(entityJson);
        Map<String, String> dataMap = node2map(entity);
        if (StringUtil.isEmpty(dataMap.get(AppDefineUtil.C_ID)) || dataMap.get(AppDefineUtil.C_ID).startsWith("tem_")) {
            dataMap.put(AppDefineUtil.C_ID, UUIDGenerator.uuid());
            inserted = true;
        }
        //Map<String, String> relateDateMap = getRelateDateMap(tableId, dTableId, dataMap);
        String tableName = "T_ZZ_TRPCKJBXX", pTableName = "T_ZZ_TRPCKSCDA", dTableName = "T_ZZ_TRPCKMX";
        // 保存明细记录
        List<Map<String, String>> dList = new ArrayList<Map<String, String>>();
        List<Map<String, String>> pList = new ArrayList<Map<String, String>>();
        String id = null;
        JsonNode entities = JsonUtil.json2node(dEntitiesJson);
        JsonNode pEntities = JsonUtil.json2node(pEntitiesJson);
        Map<String, String> dMap = new HashMap<String, String>();
        Map<String, String> pMap = new HashMap<String, String>();
        //删除历史数据
        updataTrpcgkcl(String.valueOf(dataMap.get("ID")));
        //循环遍历生产档案管理列表数据进行保存
        for (int i = 0, len = pEntities.size(); i < len; i++) {
            pMap = node2map(pEntities.get(i));
            pMap.put("PID", dataMap.get("ID"));
            pMap.remove("CZ");
            id = pMap.get(AppDefineUtil.C_ID);
            if (StringUtil.isNotEmpty(id) && id.startsWith("UNSAVE_")) {
                pMap.remove(AppDefineUtil.C_ID);
            }
            pMap.put("QYBM", SerialNumberUtil.getInstance().getCompanyCode());
            //保存生产档案的一条记录
            id = saveOne(pTableName, pMap);
            pList.add(pMap);
            //循环遍历投入品列表信息进行保存操作
            for (int j = 0, clen = entities.size(); j < clen; j++) {//需要删除保存的历史
                String cid = "";
                dMap = node2map(entities.get(j));
                //如果保存的是属于同一个农事操作项就进行保存
                if (dMap.get("NSX").equals(pMap.get("NSX"))) {
                    dMap.put("PID", id);
                    dMap.remove("CZ");
                    cid = dMap.get(AppDefineUtil.C_ID);
                    if (StringUtil.isNotEmpty(cid) && cid.startsWith("UNSAVE_")) {
                        dMap.remove(AppDefineUtil.C_ID);
                    }
                    dMap.put("QYBM", SerialNumberUtil.getInstance().getCompanyCode());
                    cid = saveOne(dTableName, dMap);
                    String trpSql = " update t_zz_trpcggl set KCSL=(KCSL-" + dMap.get("CKSL") + ") where TRPBH='" + dMap.get("TRPBH") + "'" + defaultCode();
                    DatabaseHandlerDao.getInstance().executeSql(trpSql);
                    dList.add(dMap);
                }

            }
        }

        dataMap.put("QYBM", SerialNumberUtil.getInstance().getCompanyCode());
        // 保存主表记录
        saveOne(tableName, dataMap, inserted);
        //
        returnData.put("master", dataMap);
        returnData.put("pDetail", pList);
        returnData.put("dDetail", dList);
        return returnData;
    }

    /**
     * 投入品出库明细保存方法
     * @param entityJson 父表数据
     * @param dEntitiesJson 子表列表数据
     * @return
     */
    public String saveTrplyxx_modify(String entityJson,String dEntitiesJson){

        String result=checkCksl(dEntitiesJson);
        if("no".equals(result)){
            return "fail";
        }


        String tableName = "T_ZZ_TRPCKJBXX", dTableName = "T_ZZ_TRPCKMX";
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        //父表json数据
        JsonNode entity = JsonUtil.json2node(entityJson);
        //父表单内容
        Map<String, String> dataMap = node2map(entity);
        dataMap.remove("LYSJ");
        dataMap.put("LYSJ",sdf.format(new Date()));

        //判断是否新增操作
        if (StringUtil.isEmpty(dataMap.get(AppDefineUtil.C_ID)) || dataMap.get(AppDefineUtil.C_ID).startsWith("tem_")) {
            dataMap.remove("ID");
        }
        //保存主表记录
        dataMap.put("QYBM", SerialNumberUtil.getInstance().getCompanyCode());

        SimpleDateFormat sdf1=new SimpleDateFormat("yyyyMMdd");
        String cklshfufix=sdf1.format(new Date());
        String username=CommonUtil.getUser().getName();
        dataMap.put("CZR", username);
        String cklsh=null;
//        dataMap.put("CKLS",cklsh);



        String lshsql="select max(cklsh) as cklsh from T_ZZ_TRPCKJBXX";
        Map<String,Object> maxlsh=DatabaseHandlerDao.getInstance().queryForMap(lshsql);
        int kk=0;
        if(maxlsh.get("CKLSH")==null){
            cklsh=cklshfufix+"01";
        }else{
            if(maxlsh.get("CKLSH").toString().substring(0,8).equals(cklshfufix)){
                int i=Integer.parseInt(maxlsh.get("CKLSH").toString().substring(8))+1;
                if(i<10){
                    cklsh=cklshfufix+"0"+i;
                }else{
                    cklsh=cklshfufix+i;
                }

            }else{
                cklsh=cklshfufix+"01";
            }
        }
        dataMap.put("CKLSH",cklsh);

        dataMap.put("CZSJ",sdf.format(new Date()));
        saveOne(tableName, dataMap);
        //处理cDataMap里面的数据
        //添加父表关联ID数据

        //保存第二部分列表数据
        JsonNode dEntities = JsonUtil.json2node(dEntitiesJson);
        //删除历史数据
        updataTrpcgkcl_modify(String.valueOf(dataMap.get("ID")));
        HttpSession se=ServletActionContext.getRequest().getSession();

        //循环遍历生产档案管理列表数据进行保存
        for (int i = 0, len = dEntities.size(); i < len; i++) {
            Map<String,String> dMap = node2map(dEntities.get(i));
            dMap.put("PID", dataMap.get("ID"));

//            SerialNumberUtil
            dMap.remove("CZ");
            String id = dMap.get(AppDefineUtil.C_ID);
            if (StringUtil.isNotEmpty(id) || id.startsWith("UNSAVE_")) {
                dMap.remove(AppDefineUtil.C_ID);
            }
            dMap.put("QYBM", SerialNumberUtil.getInstance().getCompanyCode());
            String forGysmcsql="select distinct GYSMC from t_zz_trpcggl where GYSBH = '"+dMap.get("GYSMC")+"' and qybm = '"+SerialNumberUtil.getInstance().getCompanyCode()+"'";
            Map<String,Object> gysmc=DatabaseHandlerDao.getInstance().queryForMap(forGysmcsql);
            dMap.put("GYSBH",dMap.get("GYSMC").toString());
            dMap.put("GYSMC",gysmc.get("GYSMC").toString());

            //保存生产档案的一条记录
            saveOne(dTableName, dMap);

            //修改对应投入品的库存量
            String trpSql = "update t_zz_trpcggl set KCSL=(KCSL-" + dMap.get("CKSL") + ") where TRPBH='" + dMap.get("TRPBH") + "'" + defaultCode();
            DatabaseHandlerDao.getInstance().executeSql(trpSql);
            //修改相关投入品表的相关字段;

            String trptablename=getTableNameById(dMap.get("BID"));
//            if(trptablename.equals("")){
//                return "fail";
//            }
            String xgsctrpsql="update "+trptablename+" set LYBJ = 1 , SJYL = '"+dMap.get("CKSL")+"' where id = '"+dMap.get("BID")+"'";
            DatabaseHandlerDao.getInstance().executeSql(xgsctrpsql);
        }
        return dataMap.get(AppDefineUtil.C_ID);
    }

    /**
     * 通过表ID 获得表的字段
     */
    public String getTableNameById(String id){
        String sql=null;
        DatabaseHandlerDao dbutil= DatabaseHandlerDao.getInstance();
        Map<String,Object> map=new HashMap<String, Object>();
        sql="select * from t_zz_scbztrp where id = '"+id+"'";
        map=dbutil.queryForMap(sql);
        if(!map.isEmpty()){return "t_zz_scbztrp";};

        sql="select * from t_zz_scggtrp where id = '"+id+"'";
        map=dbutil.queryForMap(sql);
        if(!map.isEmpty()){return "t_zz_scggtrp";};

        sql="select * from t_zz_scsftrp where id = '"+id+"'";
        map=dbutil.queryForMap(sql);
        if(!map.isEmpty()){return "t_zz_scsftrp";};

        sql="select * from t_zz_scyytrp where id = '"+id+"'";
        map=dbutil.queryForMap(sql);
        if(!map.isEmpty()){return "t_zz_scyytrp";};

        sql="select * from t_zz_scjctrp where id = '"+id+"'";
        map=dbutil.queryForMap(sql);
        if(!map.isEmpty()){return "t_zz_scjctrp";};

        sql="select * from t_zz_sccstrp where id = '"+id+"'";
        map=dbutil.queryForMap(sql);
        if(!map.isEmpty()){return "t_zz_sccstrp";};

        sql="select * from t_zz_scqttrp where id = '"+id+"'";
        map=dbutil.queryForMap(sql);
        if(!map.isEmpty()){return "t_zz_scqttrp";};


        return "";
    }

    /**
     * 根据地块编号获取生产档案信息
     *
     * @return
     */
    public Map<String, Object> searchScda(String dkbh) {
        String sql = "select * from t_zz_scda where 1=1 and dkbh = ? " + defaultCode();
        return getGridAndListData(DatabaseHandlerDao.getInstance().queryForMaps(sql, new String[]{dkbh}));
    }

    /**
     * 根据生产档案获得的对应的详细信息
     *
     * @param scdabh
     * @return
     */
    public Map<String, Object> searchScdaObj(String scdabh) {
        String sql = "select * from t_zz_scda where 1=1 and scdabh=? " + defaultCode();
        return DatabaseHandlerDao.getInstance().queryForMap(sql, new String[]{scdabh});
    }

    /**
     * 根据负责人编号查询此负责人需要负责的区域信息
     *
     * @param fzrbh
     * @return
     */
    public Map<String, Object> searchQyxx(String fzrbh) {
        String sql = "select distinct ssqybh,ssqymc from v_sczz_trplygj where qybm = '" +SerialNumberUtil.getInstance().getCompanyCode()+ "'";
        return getGridAndListData(DatabaseHandlerDao.getInstance().queryForMaps(sql));
    }

    /**
     * 根据区域编号查询地块信息并且是非休耕状态的种植单元中的地块
     *
     * @return
     */
    public Map<String, Object> searchDkxx(String qybh) {
        String sql = "select distinct dkbh from v_sczz_trplygj where  qybm = '" +SerialNumberUtil.getInstance().getCompanyCode()+ "' and ssqybh ='"+qybh+"'";
        return getGridAndListData(DatabaseHandlerDao.getInstance().queryForMaps(sql));
    }


    /**
     * 查询所需投入品名以及用量
     *
     * @param tableName
     * @param pid
     * @return
     */
    public List<Map<String, Object>> searchSxtrp(String tableName, String pid) {
        String sql = "select * from t_zz_" + tableName + " where pid = ?";
        return DatabaseHandlerDao.getInstance().queryForMaps(sql, new String[]{pid});
    }

    /**
     * 根据生产档案ID和生产档案类型进行数据加载
     *
     * @param
     * @param
     * @return
     */
        public Map<String, Object> searchNsx(String qybh,String dkbh,String scdabh) {
//        if ((null != scdalx || !"".equals(scdalx)) && ((null != pid || !"".equals(pid)))) {
//            String tableName = "t_zz_" + scdalx;
//            String selectColumns = " NSZYXBH ,NSZYXMC  ";
//            String sql = "select " + selectColumns + " from " + tableName + " where 1=1 and pid = ?";
//            return getGridAndListData(DatabaseHandlerDao.getInstance().queryForMaps(sql, new String[]{pid}));
//        }
//        return null;
        String sql="select * from v_sczz_trplygj where ssqybh ='"+qybh+"' and dkbh = '"+dkbh+"' and scdabh = '"+scdabh+"'";
        Map<String,Object> map=new HashMap<String, Object>();
        List<Map<String,Object>> listmap=new ArrayList<Map<String,Object>>();

        listmap= DatabaseHandlerDao.getInstance().queryForMaps(sql);
        map.put("data",listmap);
        return map;
//        return null;
    }

    /**
     * 查询农事项详细
     *
     * @param pid     所属
     * @param scdalx  生产档案类型
     * @param nszyxbh 农事作业项编号
     * @return
     */
    public Map<String, Object> searchNsxObj(String pid, String scdalx, String nszyxbh) {
        String tableName = "t_zz_" + scdalx;
        String sql = "select * from " + tableName + " where pid= ? and nszyxbh=?";
        return DatabaseHandlerDao.getInstance().queryForMap(sql, new String[]{pid, nszyxbh});
    }

    /**
     * 根据农事项编号、生产档案ID获取所需投入品名称和用量
     *
     * @param pid
     * @param scdalx
     * @param nszyxbh
     * @return
     */
    public Map<String, Object> searchTrp(String pid, String scdalx, String nszyxbh, String id) {

        if (id != null && !"".equals(id)) {
            //先去投入品领用表中去查询是否有历史数据
            String trpSql = "select * from t_zz_trpckmx where pid='" + id + "' " + defaultCode();
            List<Map<String, Object>> trpList = DatabaseHandlerDao.getInstance().queryForMaps(trpSql);
            return getGridAndListData(trpList);
        }
        //如果有
        if ((null != nszyxbh || !"".equals(nszyxbh)) && ((null != pid || !"".equals(pid)))) {
            List<Map<String, Object>> trpPcData = new ArrayList<Map<String, Object>>();
            String tableName = "t_zz_" + scdalx + "trp";
            String sql = "select l.TRPLX,l.TRPMC,l.TRPBH,nvl(l.KCSL,0) kcsl,l.GYSMC,l.CPPCH SCPCH ,nvl(t.YL,0) CKSL,t.NSZYXBH NSX from t_zz_trpcggl l," + tableName + " t where l.TRPLX=t.TRPLX  and t.pid = ? and t.nszyxbh = ?";//and l.TRPMC=t.TRPMC
            //获得所需投入品名称和用量
            List<Map<String, Object>> trpData = DatabaseHandlerDao.getInstance().queryForMaps(sql, new String[]{pid, nszyxbh});
            //统计使用总的库存量
            int zkcl = 0;
            boolean isFull = false;
            String newtrplx = "";
            String oldtrplx = "";
            int num = 0;
            List<Map<String, Object>> newtrpData = new ArrayList<Map<String, Object>>();
            for (Map<String, Object> map : trpData) {//根据投入品类型、名称获得所有的批次信息
                //根据库存量和出库量进行逻辑判断
                if (num == 0) {
                    oldtrplx = map.get("TRPLX").toString();
                }
                num++;
                newtrplx = map.get("TRPLX").toString();
                //如果是不同类型的投入品在初始化所有的记录信息
                if (!oldtrplx.equals(newtrplx)) {
                    isFull = false;
                    num = 0;
                    zkcl = 0;
                }
                int kcl = Integer.parseInt(map.get("KCSL").toString());
                int ckl = Integer.parseInt(map.get("CKSL").toString());
                //叠加
                zkcl += kcl;
                int zckl = ckl;
                if (isFull) {//出库是否匹配成功
                    continue;

                }
                //如果使用的总库存量大于等于出库量
                if (zkcl >= ckl) {//第一个批次就可以满足出库量
                    map.put("CKSL", ckl);
                    isFull = true;
                } else if (zkcl < ckl) {//一个批次满足不了
                    if (zckl - ckl <= 0) {
                        map.put("CKSL", zckl - ckl);
                        isFull = true;
                    } else {
                        map.put("CKSL", ckl);
                    }
                }
                newtrpData.add(map);
            }
            return getGridAndListData(newtrpData);
        }
        return null;
    }

    /**
     * 根据投入品名称加载所需投入品需要的批次信息
     *
     * @param pid
     * @param scdalx
     * @param nszyxbh
     * @param searchTrpmc
     * @return
     */
    public Map<String, Object> searchUpdTrp(String pid, String scdalx, String nszyxbh, String searchTrpmc) {
        if ((null != nszyxbh || !"".equals(nszyxbh)) && ((null != pid || !"".equals(pid)))) {
            List<Map<String, Object>> trpPcData = new ArrayList<Map<String, Object>>();
            String tableName = "t_zz_" + scdalx + "trp";
            String sql = "select l.TRPLX,l.TRPMC,l.TRPBH,nvl(l.KCSL,0) kcsl,l.GYSMC,l.CPPCH SCPCH ,nvl(t.YL,0) CKSL,t.NSZYXBH NSX from t_zz_trpcggl l," + tableName + " t where l.TRPLX=t.TRPLX and l.TRPMC=t.TRPMC  and t.pid = ? and t.nszyxbh = ? and t.TRPMC = '" + searchTrpmc + "' ";
            //获得所需投入品名称和用量
            List<Map<String, Object>> trpData = DatabaseHandlerDao.getInstance().queryForMaps(sql, new String[]{pid, nszyxbh});
            //统计使用总的库存量
            int zkcl = 0;
            boolean isFull = false;
            String newtrplx = "";
            String oldtrplx = "";
            int num = 0;
            List<Map<String, Object>> newtrpData = new ArrayList<Map<String, Object>>();
            for (Map<String, Object> map : trpData) {//根据投入品类型、名称获得所有的批次信息
                //根据库存量和出库量进行逻辑判断
                if (num == 0) {
                    oldtrplx = map.get("TRPLX").toString();
                }
                num++;
                newtrplx = map.get("TRPLX").toString();
                //如果是不同类型的投入品在初始化所有的记录信息
                if (!oldtrplx.equals(newtrplx)) {
                    isFull = false;
                    num = 0;
                    zkcl = 0;
                }
                int kcl = Integer.parseInt(map.get("KCSL").toString());
                int ckl = Integer.parseInt(map.get("CKSL").toString());
                //叠加
                zkcl += kcl;
                int zckl = ckl;
                if (isFull) {//出库是否匹配成功
                    map.put("CKSL", 0);
                    newtrpData.add(map);
                    continue;

                }
                //如果使用的总库存量大于等于出库量
                if (zkcl >= ckl) {//第一个批次就可以满足出库量
                    map.put("CKSL", ckl);
                    isFull = true;
                } else if (zkcl < ckl) {//一个批次满足不了
                    if (zckl - ckl <= 0) {
                        map.put("CKSL", zckl - ckl);
                        isFull = true;
                    } else {
                        map.put("CKSL", ckl);
                    }
                }
                newtrpData.add(map);
            }
            return getGridAndListData(newtrpData);
        }
        return null;
    }

    /**
     * @param pid
     * @param scdalx
     * @param nszyxbh
     * @param searchTrpmc
     * @return
     */
    public Map<String, Object> searchNsxTrpObj(String pid, String scdalx, String nszyxbh, String searchTrpmc) {
        String tableName = "t_zz_" + scdalx + "trp";
        String sql = "select * from " + tableName + " where pid='" + pid + "' and nszyxbh='" + nszyxbh + "' and trpmc='" + searchTrpmc + "' ";
        return DatabaseHandlerDao.getInstance().queryForMap(sql);
    }

    /**
     * 查询生产档案信息
     *
     * @param pid
     * @return
     */
    public Map<String, Object> searchTrpscdaxx(String pid) {
        String sql = "select * from t_zz_trpckmx where pid ='" + pid + "' ";
        return getGridAndListData(DatabaseHandlerDao.getInstance().queryForMaps(sql));
    }


    @Transactional
    public void updataTrpcgkcl(String pid) {
        String sql = "select * from t_zz_trpckscda where pid ='" + pid + "' ";
        List<Map<String, Object>> scdaList = DatabaseHandlerDao.getInstance().queryForMaps(sql);
        if (scdaList != null && !scdaList.isEmpty()) {//有数据存在的话
            for (Map<String, Object> map : scdaList) {
                String cpid = String.valueOf(map.get("ID"));//生产档案ID
                String cksql = "select * from t_zz_trpckmx where pid ='" + cpid + "' ";//通过生产档案领用记录ID获得对应记录
                //查询投入品明显的指定信息
                List<Map<String, Object>> ckList = DatabaseHandlerDao.getInstance().queryForMaps(cksql);
                if (ckList != null && !ckList.isEmpty()) {
                    for (Map<String, Object> ckMap : ckList) {
                        String cksl = String.valueOf(ckMap.get("CKSL"));
                        String trpSql = " update t_zz_trpcggl set KCSL=(KCSL+" + cksl + ") where TRPBH='" + ckMap.get("TRPBH") + "'" + defaultCode();
                        //还原历史数据中投入品领用的数量
                        DatabaseHandlerDao.getInstance().executeSql(trpSql);
                    }
                }
                //根据生产档案ID删除历史投入品记录
                cksql = "delete from t_zz_trpckmx where pid ='" + cpid + "' ";
                DatabaseHandlerDao.getInstance().executeSql(cksql);
            }
        }
    }

    /**
     * 根据出库明细ID进行修改后的库存数量进行还原，还原后删除历史记录
     * @param id
     */
    @Transactional
    public void updataTrpcgkcl_modify(String id) {
        String cpid = String.valueOf(id);//生产档案ID
        String cksql = "select * from t_zz_trpckmx where pid ='" + cpid + "' ";//通过生产档案领用记录ID获得对应记录
        //查询投入品明显的指定信息
        List<Map<String, Object>> ckList = DatabaseHandlerDao.getInstance().queryForMaps(cksql);
        if (ckList != null && !ckList.isEmpty()) {
            for (Map<String, Object> ckMap : ckList) {
                String cksl = String.valueOf(ckMap.get("CKSL"));
                String trpSql = " update t_zz_trpcggl set KCSL=(KCSL+" + cksl + ") where TRPBH='" + ckMap.get("TRPBH") + "'" + defaultCode();
                //还原历史数据中投入品领用的数量
                DatabaseHandlerDao.getInstance().executeSql(trpSql);
            }
        }
        //根据生产档案ID删除历史投入品记录
        cksql = "delete from t_zz_trpckmx where pid ='" + cpid + "' ";
        DatabaseHandlerDao.getInstance().executeSql(cksql);
    }
    /**    进行修改操作需要初始化数据，进行逻辑数据获取 **/
    /**
     * 根据区域长获得管辖
     *
     * @param
     * @return
     */
    public Map<String, Object> searchDkxxByQyfzr(String qybh) {
        //String sql = "select * from t_zz_dkxx where qybh in(select qybh from t_zz_qyxx where fzrbh='" + qybh + "' " + defaultCode() + ")";
        String sql ="select * from v_sczz_trplygj where ssqybh = '"+qybh+"'";
        return getGridAndListData(DatabaseHandlerDao.getInstance().queryForMaps(sql));
    }

    /**
     * 根据区域长，获得使用的所有生产档案
     *
     * @param
     * @return
     */
    public Map<String, Object> searchScdaByQyfzr(String qybh,String dkbh) {
//        String sql = "select * from t_zz_scda where dkbh in(select dkbh from t_zz_dkxx where qybh in(select qybh from t_zz_qyxx where fzrbh='" + fzrbh + "'))" + defaultCode();
        String sql="select distinct scdabh from v_sczz_trplygj where ssqybh = '"+qybh+"' and dkbh = '"+dkbh+"'";
        return getGridAndListData(DatabaseHandlerDao.getInstance().queryForMaps(sql));
    }


    /**
     * 删除领用记录中的生产档案，把领用的记录数据还原
     *
     * @param id
     */
    @Transactional
    public void deleteScdalyxx(String id) {
        String cksql = "select * from t_zz_trpckmx where id ='" + id + "' ";//通过生产档案领用记录ID获得对应记录
        //查询投入品明显的指定信息
        List<Map<String, Object>> ckList = DatabaseHandlerDao.getInstance().queryForMaps(cksql);
        if (ckList != null && !ckList.isEmpty()) {
            for (Map<String, Object> ckMap : ckList) {
                String cksl = String.valueOf(ckMap.get("CKSL"));
                String trpSql = " update t_zz_trpcggl set KCSL=(KCSL+" + cksl + ") where TRPBH='" + ckMap.get("TRPBH") + "'" + defaultCode();
                //还原历史数据中投入品领用的数量
                DatabaseHandlerDao.getInstance().executeSql(trpSql);
            }
        }
        String sql = "delete from t_zz_trpckmx where id='" + id + "'";
        DatabaseHandlerDao.getInstance().executeSql(sql);
    }

    /**
     * 根据生产档案ID ,生产档案类型，农事操作项目编号获得所需投入品信息
     *
     * @param pid
     * @param scdalx
     * @param nszyxbh
     * @return
     */
    public Map<String, Object> searchTreeParentNode(String pid, String scdalx, String nszyxbh) {
        String tableName = "t_zz_" + scdalx + "trp";//t_zz_trpckjbxx
        String sql = "select  t.trplx,t.trptym,t.trpmc  from  " + tableName + " t where t.pid = ? and t.nszyxbh =?";
        List<Map<String,Object>> dataList = DatabaseHandlerDao.getInstance().queryForMaps(sql, new String[]{pid, nszyxbh});
        Map<String,Object> dataMap = new HashMap<String, Object>();
        List<Map<String, Object>> mapList = new ArrayList<Map<String, Object>>();
        for ( Map<String, Object> map : dataList){
            dataMap.put("id",map.get("TRPMC"));
            dataMap.put("name",map.get("TRPTYM"));
            dataMap.put("isParent",true);
            dataMap.put("children",searchTreeChildNode(String.valueOf(map.get("TRPTYM"))));
            mapList.add(dataMap);
        }
        return getGridAndListData(mapList);
    }

    /**
     * 根据投入品名称获得可用投入品批次
     *
     * @param trpmc
     * @return
     */
    public List<Map<String, Object>> searchTreeChildNode(String trpmc) {
        String sql = "select * from t_zz_trpcggl t where t.tym like '%"+trpmc+"%' " + defaultCode();
        List<Map<String,Object>> dataList = DatabaseHandlerDao.getInstance().queryForMaps(sql);
        Map<String,Object> dataMap = null;
        List<Map<String, Object>> mapList = new ArrayList<Map<String, Object>>();
        for ( Map<String, Object> map : dataList){
            dataMap = new HashMap<String, Object>();
            dataMap.put("id",map.get("RKLSH"));
            dataMap.put("name", map.get("TRPMC"));
            mapList.add(dataMap);
        }
        return mapList;
    }

    /**
     *根据选择的入库流水号查询详细信息
     * @param rklshs
     * @return
     */
    public Map<String, Object> searchSelectedTrpxx(String rklshs){
        String sql = "select * from t_zz_trpcggl t where t.TRPBH in( '" + rklshs.replace(",", "','") + "' )" + defaultCode();
        int i=1;
        return getGridAndListData(DatabaseHandlerDao.getInstance().queryForMaps(sql));
    }

    public Map<String, Object> searchTrpmx(String id){
        String sql = "select * from t_zz_trpckmx  where id= ?";
        return DatabaseHandlerDao.getInstance().queryForMap(sql, new String[]{id});
    }

    public Object searchNsxgrid(String lyr,String qssj,String jssj){
        String sql="select yl as cksl, bid, dkbh,scdabh as scda,nszyxmc as nsxmc,trplx,trptym as trpmc,trpmc as trpbh from v_sczz_trplygj where fzrbh = '"+lyr+"'  and ygsj1 <= '"+jssj+"' and ygsj2 >='"+qssj+"' and qybm='"+SerialNumberUtil.getInstance().getCompanyCode()+"'";
//        String sql="select * from v_sczz_trplygj where ssqybh ='"+qybh+"' and dkbh = '"+dkbh+"' and scdabh = '"+scdabh+"'";
        Map<String,Object> map=new HashMap<String, Object>();
        List<Map<String,Object>> listmap=new ArrayList<Map<String,Object>>();

        listmap= DatabaseHandlerDao.getInstance().queryForMaps(sql);
        map.put("data",listmap);
        return map;
//        return queryPage(pageRequest, sql);
    }

    public static Page queryPage(PageRequest pageRequest, String sql) {
//        pageRequest = new PageRequest(pageRequest.getPageNumber() - 1, pageRequest.getPageSize());
        if (ces.coral.lang.StringUtil.isBlank(sql)) {
            return null;
        }
        //查总数
        String count = "select count(*) as count from (" + sql + ")";
        Map map = DatabaseHandlerDao.getInstance().queryForMap(count);
        //总数
        long total = Long.parseLong(map.get("COUNT").toString());
        int begin = pageRequest.getOffset();
        int end = begin + pageRequest.getPageSize();
        if (begin > total) {
            int remainder = (int) (total % pageRequest.getPageSize());
            end = (int) total;
            begin = (int) (total - (remainder == 0 ? pageRequest.getPageSize() : remainder));
        }

        List<Map<String, Object>> content = DatabaseHandlerDao.getInstance().pageMaps(sql, begin, end);
        if (content == null) {
            return null;
        } else {
            return new PageImpl<Map<String, Object>>(content, pageRequest, total);
        }
    }

    public Object getGysData(String trpbh,String scpch){
        String sql =null;
//        if(scpch==""){
            sql="select distinct GYSMC,GYSBH from t_zz_trpcggl where trpbh = '"+trpbh+"' and qybm = '"+SerialNumberUtil.getInstance().getCompanyCode()+"' and GYSMC IS NOT NULL and GYSBH is not null";
//    }
//        else{
//            sql="select GYSMC,GYSBH from t_zz_trpcggl where trpbh = '"+trpbh+"' and cppch='"+scpch+"'";
//        }

        int i=0;
        return DatabaseHandlerDao.getInstance().queryForMaps(sql);
    }

    public Object getScpchData(String trpbh,String gysbh){
        String sql=null;
//        if(gysbh==""){
//            sql="select CPPCH,ID from t_zz_trpcggl where trpbh = '"+trpbh+"'";
//        }else{
            sql="select RKLSH,RKLSH AS RKLSHBH from t_zz_trpcggl where trpbh = '"+trpbh+"' and gysbh = '"+gysbh+"'";
//        }

        return DatabaseHandlerDao.getInstance().queryForMaps(sql);

    }

    public Object getXgGysData(){
        String sql="select GYSMC,GYSBH from t_zz_trpcggl where qybm = '"+SerialNumberUtil.getInstance().getCompanyCode()+"'";
        return DatabaseHandlerDao.getInstance().queryForMaps(sql);
    }

    /**
     *  操作人信息加载
     * @return
     */

    public Object searchCzrxx(){
        String sql="select FZR as CZR,FZRBH as CZRBH from t_zz_qyxx where qybm= '"+SerialNumberUtil.getInstance().getCompanyCode()+"'";
        return DatabaseHandlerDao.getInstance().queryForMaps(sql);
    }


    /**
     * 获取打印数据
     */

    public Object getDayinData(String id){

        String sql="select TRPMC,RKLSH,CKSL from t_zz_trpckmx where pid = '"+id+"'";
        return DatabaseHandlerDao.getInstance().queryForMaps(sql);

    }

    public Page reloadDayiData(PageRequest pageRequest,String id){
        String sql="select * from t_zz_trpckmx where pid = '"+id+"'";
//        DatabaseHandlerDao.getInstance().queryForMaps(sql);
        Map<String,Object> maps=new HashMap<String,Object>();
        return queryPage(pageRequest, sql);
//        maps.put("data",DatabaseHandlerDao.getInstance().queryForMaps(sql));
//        return maps;
    }

    public Object getCkslData(String rklsh){
        String sql="select KCSL from t_zz_trpcggl where rklsh = '"+rklsh+"' and qybm = '"+SerialNumberUtil.getInstance().getCompanyCode()+"'";
        Map<String,Object> map=DatabaseHandlerDao.getInstance().queryForMap(sql);
        return map;
    }

    /**
     * 判断出库数量是否超过库存数量
     */
    public String checkCksl(String griddata){
        JsonNode pEntities = JsonUtil.json2node(griddata);
        Map<String,Integer> map=new HashMap<String,Integer>();
        Map<String,String> centermap=new HashMap<String,String>();
        Map<String,Object> database=new HashMap<String, Object>();
//        Set<String> set =new HashSet<>();
        boolean flag=true;

        String sql;
        for(int i=0;i<pEntities.size();i++){
            centermap=node2map(pEntities.get(i));
            if(map.containsKey(centermap.get("RKLSH"))){
                map.put(centermap.get("RKLSH"),map.get(centermap.get("RKLSH"))+Integer.parseInt(centermap.get("CKSL")));
            }else{
                map.put(centermap.get("RKLSH"),Integer.parseInt(centermap.get("CKSL")));
            }
        }
//        for(int i=0;i<map.size();i++){
//            sql="select CKLS from t_zz_trpcggl where rklsh = '"+map."'";
//        }
        for(Map.Entry<String,Integer> ent:map.entrySet()){
            sql="select KCSL from t_zz_trpcggl where rklsh ='"+ent.getKey()+"' and qybm = '"+SerialNumberUtil.getInstance().getCompanyCode()+"'";

            database=DatabaseHandlerDao.getInstance().queryForMap(sql);
//            Integer.getInteger();
            if(Integer.parseInt(String.valueOf(database.get("KCSL")))<ent.getValue()){
                flag=false;
            }

        };
        if(flag){return "ok";}else{
            return "no";
        }
    }

    public Object tjGridGysData(String qybh,String dkbh,String scda,String nsczx,String ids,String nsczxmc){
        String[] idsarr=ids.split(",");
        String id="";
        for(int i=0;i<idsarr.length;i++){
            if(i==(idsarr.length-1)){
                id=id+"'"+idsarr[i]+"'";
            }else{
                id=id+"'"+idsarr[i]+"'"+",";
            }
        };
        String sql;
        if("".equals(ids)){
            sql="select yl as cksl, scdabh as scda,nszyxmc as nsxmc,bid,trpmc as trpbh,dkbh,trplx,trptym as trpmc from v_sczz_trplygj where ssqybh = '"+qybh+"' and dkbh = '"+dkbh+"' and scdabh = '"+scda+"' and id ='"+nsczx+"' and nszyxmc = '"+nsczxmc+"'";
        }else{
            sql="select yl as cksl,scdabh as scda,nszyxmc as nsxmc,bid,trpmc as trpbh,dkbh,trplx,trptym as trpmc from v_sczz_trplygj where ssqybh = '"+qybh+"' and dkbh = '"+dkbh+"' and scdabh = '"+scda+"' and id ='"+nsczx+"' and nszyxmc = '"+nsczxmc+"' and bid  not in ("+id+")";
        }
//       sql="select * from v_sczz_trplygj where qybh = '"+qybh+"' and dkbh = '"+dkbh+"' and scdabh = '"+scda+"' and nszyxbh ='"+nsczx+"' and bid is not in ("+ids+")";
                List<Map<String,Object>> listmap=new ArrayList<Map<String, Object>>();
        listmap=DatabaseHandlerDao.getInstance().queryForMaps(sql);
        Map<String,Object> map=new HashMap<String, Object>();
        map.put("data",listmap);
        return map;
    }
}