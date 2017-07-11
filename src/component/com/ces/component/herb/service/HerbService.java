package com.ces.component.herb.service;

import com.ces.component.herb.dto.HerbDao;
import com.ces.component.herb.utils.HerbCommonUtil;
import com.ces.component.herb.utils.JSONUtil;
import com.ces.component.trace.service.base.TraceShowModuleDefineDaoService;
import com.ces.component.trace.utils.QRCodeUtil;
import com.ces.component.trace.utils.SerialNumberUtil;
import com.ces.component.trace.utils.StatisticalCodeUtil;
import com.ces.config.dhtmlx.dao.common.DatabaseHandlerDao;
import com.ces.config.dhtmlx.service.appmanage.ColumnDefineService;
import com.ces.config.utils.*;
import com.ces.xarch.core.entity.StringIDEntity;
import com.opensymphony.xwork2.ActionContext;
import net.sf.json.JSONObject;
import org.apache.struts2.ServletActionContext;
import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by Plain on 2016/6/25.
 */
@Component
public class HerbService extends TraceShowModuleDefineDaoService<StringIDEntity, HerbDao> {
    @PersistenceContext
    private EntityManager entityManager;

    public EntityManager getEntityManager() {
        return entityManager;
    }
    @Transactional
    public Map<String,String> save(String type,String jsondata){
        Map<String,String> dataMap = null;
        try {
            dataMap = JSONUtil.jsonStringToMap(jsondata);
            ServletActionContext.getRequest().getSession().setAttribute("_companyCode_", dataMap.get("QYBM"));//将企业编码写入session
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("json数据转换Map失败");
        }
        String tabname = getTableName(type);
        String id = dataMap.get("ID");
        if(StringUtil.isNotEmpty(id)){//
            dataMap = getSerialNumber(type,dataMap);
        }
        id = saveOne(tabname,dataMap);
        dataMap.put("ID",id);
        return dataMap;
    }


    @Transactional
    public void delete(String type,String id){
        String tabname = getTableName(type);
        String sql = " delete from "+tabname+" where id ='"+id+"'";
         DatabaseHandlerDao.getInstance().executeSql(sql);
    }

    public List<Map<String, Object>> queryList( String type,String qybm){
        String tabname = getTableName(type);
        String sql = " select * from "+tabname +" where qybm='"+qybm+"' order by CREATE_time desc";
        Query query = getEntityManager().createNativeQuery(sql);
        query.setFirstResult(0);
        query.setMaxResults(10);
        return query.unwrap(SQLQuery.class)
                .setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP)
                .list();
    }

    public Map<String, Object> queryOne( String type,String qybm,String id ){
        String tabname = getTableName(type);
        String sql = " select * from "+tabname +" where qybm='"+qybm +"' and id='"+id+"'";
        return DatabaseHandlerDao.getInstance().queryForMap(sql);
    }

    @Transactional
    public String saveAll(String type,String jsondata){
        JSONObject jsonObject = JSONObject.fromObject(jsondata);
        JSONObject pdata = jsonObject.getJSONObject("pdata");//主表字段
        try {
            Map<String,String> dataMap = JSONUtil.jsonStringToMap(pdata);
            List<Map<String,String>> cdataMap = JSONUtil.jsonStringToList(jsonObject.getJSONArray("cdata"));//子表字段
            ActionContext ac = ActionContext.getContext();
            HttpServletRequest request =(HttpServletRequest)ac.get(ServletActionContext.HTTP_REQUEST);
            HttpSession session = request.getSession();
            session.setAttribute("_companyCode_",dataMap.get("qybm"));
            String id = dataMap.get("ID");
            if(!StringUtil.isNotEmpty(id)){//判断为新增
                id = UUIDGenerator.uuid();
                dataMap.put("ID", id);
                getSerialNumber(type,dataMap);
                for ( Map<String, String> map : cdataMap){//保存到交易详细信息表中
                    map.put("xsddh",dataMap.get("xsddh"));
                    map.put("dzsfd",dataMap.get("xsddh"));//将销售订单号赋予电子随附单
                    String text = dataMap.get("xsddh");
                    String path = request.getRealPath("/");
                    path  = "cjgycjy".equals(type)?path+"qrCode/cjg":path+"qrCode/jjg";
                    QRCodeUtil.encode(text, path);
                    saveOne(getChildTableName(type),map);
                    //发送交易数据到省平台
                    //sendTradeOut(type,dataMap,map);
                }
            }else{
                for ( Map<String, String> map : cdataMap){//保存到交易详细信息表中
                    map.put("xsddh",dataMap.get("xsddh"));
                    map.put("dzsfd",dataMap.get("xsddh"));//将销售订单号赋予电子随附单
                    map.put("YPTZSM", dataMap.get("xsddh")+dataMap.get("pch"));
                    saveOne(getChildTableName(type),map);
                }
            }

            //保存到交易主表中
            saveOne(getTableName(type),dataMap);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public  void sendTradeOut(String type,Map<String,String> dataMap,Map<String,String> map){
          if("t_sdzyc_cjg_ycjyxx".equalsIgnoreCase(getTableName(type)))
              HerbCommonUtil.getInstance().cjg_sendCreatetTraceOutService(map,dataMap,HerbCommonUtil.getInstance().cjg_createTraceInfo(map));
          else
              HerbCommonUtil.getInstance().jjg_sendCreatetTraceOutService(map,dataMap,HerbCommonUtil.getInstance().jjg_createTraceInfo(map));
    }


    /**
     * 采购数据上传到省平台
     */
    public void sendCreateTrade(String tabName,Map<String, String> dataMap){
        if("t_sdzyc_cjg_ylrkxx".equalsIgnoreCase(tabName))
            HerbCommonUtil.getInstance().cjg_sendCreateTradeInService(dataMap);
//        else if("t_sdzyc_cjg_ycjyxx".equalsIgnoreCase(tabName))
//            HerbCommonUtil.getInstance().cjg_sendCreateTradeInService(dataMap);
        else if("t_sdzyc_jjg_yycrkxx".equalsIgnoreCase(tabName))
            HerbCommonUtil.getInstance().jjg_sendCreateTradeInService(dataMap);
//        else if("t_sdzyc_jjg_cpjyxx".equalsIgnoreCase(tabName))
//            HerbCommonUtil.getInstance().cjg_sendCreateTradeInService(dataMap);
    }
    public String getTableName(String type){
        if("cjgylrk".equalsIgnoreCase(type))//粗加工原料入库信息
            return "t_sdzyc_cjg_ylrkxx";
        else if("cjgycjg".equalsIgnoreCase(type))//粗加工药材加工信息
            return "t_sdzyc_cjg_ycjgxx";
        else if("cjgycjy".equalsIgnoreCase(type))//粗加工药材交易信息
            return "t_sdzyc_cjg_ycjyxx";
        else if("cjgycjy_st".equalsIgnoreCase(type))//粗加工药材交易信息视图显示
            return "V_SDZYC_CJG_YCJYXX";
        else if("jjgycrk".equalsIgnoreCase(type))//精加工药材入库信息
            return "t_sdzyc_jjg_yycrkxx";
        else if ("jjgypsc".equalsIgnoreCase(type))//精加工饮片生产信息
            return "t_sdzyc_jjg_ypscxx";
        else if("jjgypjy".equalsIgnoreCase(type))//精加工饮片交易信息
            return "t_sdzyc_jjg_cpjyxx";
        else if("jjgypjy_st".equalsIgnoreCase(type))//精加工饮片交易信息视图显示
            return "V_SDZYC_JJG_YPJYXX";
        return null;
    }

    public String getChildTableName(String type){
        if("cjgycjy".equalsIgnoreCase(type)){
            return "t_sdzyc_cjg_ycjyxxxx";
        }else if("jjgypjy".equalsIgnoreCase(type)){
            return "t_sdzyc_jjg_cpjyxxxx";
        }
        return null;
    }

    /**
     * 根据企业类型获取流水号并进行保存
     * @param type
     * @param dataMap
     * @return
     */
    public Map<String, String> getSerialNumber(String type,Map<String ,String > dataMap){
        if("cjgylrk".equalsIgnoreCase(type)) {//粗加工原料入库信息
            SerialNumberUtil.getInstance().getSerialNumber("SDZYC", "SDZYCCJGYCRKBH", false);
            String ycrkbh = SerialNumberUtil.getInstance().getSerialNumber("SDZYC", "SDZYCCJGYCRKBH", false);
            dataMap.put("RKBH", ycrkbh);
            return dataMap;
        }else if("cjgycjg".equalsIgnoreCase(type)) {//粗加工药材加工信息
            String jgpch = SerialNumberUtil.getInstance().getSerialNumber("SDZYC", "SDZYCJGPCH", false);
            dataMap.put("JGPCH", jgpch);
            return dataMap;
        }else if("cjgycjy".equalsIgnoreCase(type)) {//粗加工药材交易信息
            String ycxsddh= StatisticalCodeUtil.getInstance().getTwentyFivePcm("CJG","SDZYC","SDZYCYCXSDDH");
            dataMap.put("XSDDH",ycxsddh);
            return dataMap;
        }else if("jjgycrk".equalsIgnoreCase(type)) {//精加工药材入库信息
            return dataMap;
        }else if ("jjgypsc".equalsIgnoreCase(type)) {//精加工饮片生产信息
            String jjggysbh = StatisticalCodeUtil.getInstance().getTwentyFivePcm("JJG","SDZYC", "SDZYCJJGYPSCPCH");
            dataMap.put("scpch",jjggysbh);
            return dataMap;
        }else if("jjgypjy".equalsIgnoreCase(type)) {//精加工饮片交易信息
            String xxddhh = SerialNumberUtil.getInstance().getSerialNumber("SDZYC", "SDZYCJJGYPJYXSDDH", true);
            dataMap.put("xsddh",xxddhh);
            return dataMap;
        }
        return dataMap;
    }
    @Transactional
    protected String saveOne(String tableName, Map<String, String> dataMap) {
        String id = dataMap.get(AppDefineUtil.C_ID);
        if (StringUtil.isEmpty(id)) {
            //新增
            id = UUIDGenerator.uuid();
            dataMap.put("ID", id);
            insert(tableName, dataMap);
            sendCreateTrade(tableName, dataMap);
        } else {
            update(tableName, dataMap);
        }
        return id;
    }
    protected void insert(String tableName, Map<String, String> dataMap) {
        String tableId = TableUtil.getTableId(tableName);
        if (StringUtil.isNotEmpty(tableId)) {
            long cnt = 0;
            if (!dataMap.containsKey("CREATE_USER")) {
                getService(ColumnDefineService.class).count("EQ_columnName=CREATE_USER;EQ_tableId=" + tableId);

            }
            if (!dataMap.containsKey("CREATE_TIME")) {
                cnt = getService(ColumnDefineService.class).count("EQ_columnName=CREATE_TIME;EQ_tableId=" + tableId);
                if (cnt > 0) {
                    dataMap.put("CREATE_TIME", DateUtil.currentTime());
                }
            }
        }
        Set<String> set = dataMap.keySet();
        Iterator<String> it = set.iterator();
        StringBuffer cSql = new StringBuffer("");
        StringBuffer vSql = new StringBuffer("");
        while(it.hasNext()) {
            String key = it.next();
            String val = dataMap.get(key);
            cSql.append(",").append(key);
            vSql.append(",'").append(StringUtil.null2empty(val)).append("'");
        }
        StringBuffer sql = new StringBuffer();
        sql.append("insert into ")
                .append(tableName).append("(").append(cSql.substring(1)).append(") values (")
                .append(vSql.substring(1)).append(")");
        DatabaseHandlerDao dao = DatabaseHandlerDao.getInstance();
        dao.executeSql(sql.toString());
    }


    /** 根据ID修改指定表中数据 */
    @Transactional
    protected void update(String tableName, Map<String, String> dataMap) {
        //
        String id = dataMap.get(AppDefineUtil.C_ID);
        dataMap.remove(AppDefineUtil.C_ID);
        //
        String tableId = TableUtil.getTableId(tableName);
        if (StringUtil.isNotEmpty(tableId)) {
            long cnt = 0;
            if (!dataMap.containsKey("UPDATE_USER")) {
                getService(ColumnDefineService.class).count("EQ_columnName=UPDATE_USER;EQ_tableId=" + tableId);

            }
            if (!dataMap.containsKey("UPDATE_TIME")) {
                cnt = getService(ColumnDefineService.class).count("EQ_columnName=UPDATE_TIME;EQ_tableId=" + tableId);
                if (cnt > 0) {
                    dataMap.put("UPDATE_TIME", DateUtil.currentTime());
                }
            }
        }
        Set<String> set = dataMap.keySet();
        Iterator<String> it = set.iterator();
        StringBuffer cSql = new StringBuffer("");
        while(it.hasNext()) {
            String key = it.next();
            String val = dataMap.get(key);
            cSql.append(",").append(key).append("='").append(StringUtil.null2empty(val)).append("'");
        }

        StringBuffer sql = new StringBuffer();
        sql.append("update ")
                .append(tableName).append(" set ").append(cSql.substring(1))
                .append(" where ")
                .append(AppDefineUtil.C_ID).append("='").append(id).append("'");
        //System.out.println(sql);
        DatabaseHandlerDao dao = DatabaseHandlerDao.getInstance();
        dao.executeSql(sql.toString());

        dataMap.put(AppDefineUtil.C_ID, id);
    }

    /**
     * 库存管理.
     * @param type type
     * @return list
     */
    public  List<Map<String, Object>> queryStocks(String type,String qybm){
        //粗加工表
        String tab = "V_SDZYC_CJG_KCGLXX";//入库
        String name = "ycmc";
        //精加工
        if("jjg".equalsIgnoreCase(type)){
            tab = "V_SDZYC_JJG_KCGLXX";
            name = "ypmc";
        }
        String sql = "SELECT qypch,"+name+",kc,qybm FROM "+tab+"  WHERE kc > 0 AND QYBM= '"+qybm+"'";
        Query query = getEntityManager().createNativeQuery(sql);
        query.setFirstResult(0);
        query.setMaxResults(10);
        return query.unwrap(SQLQuery.class)
                .setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP)
                .list();
    }

    /**
     * 通过采收批次号获取原料入库数据
     * @param cspch
     * @return
     */
    public List<Map<String,Object>> searchDataByCspch(String cspch){
        String sql = "select DISTINCT m.*\n" +
                "  from (select c.qycspch,\n" +
                "               c.cspch,\n" +
                "               c.cszl,\n" +
                "               c.ycmc,\n" +
                "               c.ycdm,\n" +
                "               c.jssj,\n" +
                "               c.zzpch,\n" +
                "               s.dkbh,\n" +
                "               j.jdmc,\n" +
                "               j.ssdq,\n" +
                "               c.qybm\n" +
                "          from t_sdzyc_csglxx c, T_ZZ_SCDA s, t_sdzyc_jdxx j\n" +
                "         where c.zzpch = s.zzpch\n" +
                "           and c.qybm = s.qybm\n" +
                "           and s.qybm = j.qybm\n" +
                "           and s.jdbh = j.jdbh) m,\n" +
                "       t_sdzyc_qyda q\n" +
                " where q.qybm = m.qybm\n" +
                "   and m.cspch = '"+cspch+"'" ;
        return DatabaseHandlerDao.getInstance().queryForMaps(sql);
    }

    /**
     * 通过企业销售订单号获取精加工药材采购数据
     * @param xsddh
     * @return
     */
    public List<Map<String,Object>>  searchYcrkxxByxsddh(String xsddh){
        String sql = "select T.QYPCH," +
                "T.CSPCH," +
                "t.ycdm," +
                "t.cdmc," +
                "t.jyzl," +
                "t.ycmc," +
                "t.pch," +
                "t.qybm," +
                "t.xsddh," +
                "m.QYXSDDH," +
                "t.yptzsm " +
                "from T_SDZYC_CJG_YCJYXXXX  t " +
                "inner join T_sdzyc_cjg_ycjyxx m " +
                "on t.xsddh=m.xsddh " +
                "where m.XSDDH= '"+xsddh+"'";
        return DatabaseHandlerDao.getInstance().queryForMaps(sql);
    }

    /**
     * 获取粗加工仓库信息
     * @param qybm
     * @return
     */
    public List<Map<String,Object>> getCjgCkxx(String qybm){
        String sql = "select ckmc from t_sdzyc_cjg_ckxx where qybm='"+qybm+"'";
        List<Map<String,Object>> list_cjgCkxx = DatabaseHandlerDao.getInstance().queryForMaps(sql);
        return list_cjgCkxx;
    }

    /**
     * 获取精加工仓库信息
     * @param qybm
     * @return
     */
    public List<Map<String,Object>> getJjgCkxx(String qybm){
        String sql = "select ckmc from t_sdzyc_Jjg_ckxx where qybm='"+qybm+"'";
        List<Map<String,Object>> list_jjgCkxx = DatabaseHandlerDao.getInstance().queryForMaps(sql);
        return list_jjgCkxx;
    }


}
