package com.ces.component.csscsmjc.service;

import ces.sdk.util.StringUtil;
import cesgroup.rear.lib.client.enterprise.RearClientBuilder;
import com.ces.component.cspt.entity.TCsptZsxxEntity;
import com.ces.component.farm.utils.TxtUtils;
import com.ces.component.trace.utils.CompanyInfoUtil;
import com.ces.component.trace.utils.JSON;
import com.ces.component.trace.utils.SerialNumberUtil;
import com.ces.component.trace.utils.TraceChainUtil;
import com.ces.config.datamodel.message.MessageModel;
import com.ces.config.dhtmlx.dao.common.DatabaseHandlerDao;
import com.ces.config.utils.AppDefineUtil;
import com.ces.config.utils.ComponentFileUtil;
import com.ces.config.utils.JsonUtil;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import enterprise.endpoints.EnterpriseService;
import enterprise.entity.TraceEntity;
import enterprise.entity.TradeInEntity;
import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.struts2.ServletActionContext;
import org.aspectj.weaver.tools.Trace;
import org.springframework.stereotype.Component;

import com.ces.component.trace.dao.TraceShowModuleDao;
import com.ces.component.trace.service.base.TraceShowModuleDefineDaoService;
import com.ces.xarch.core.entity.StringIDEntity;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

@Component
public class CsscsmjcService extends TraceShowModuleDefineDaoService<StringIDEntity, TraceShowModuleDao> {

   /* public Map<String,Object>getsmjcxx(String barCode){
        Map<String,Object> resultMap = new HashMap<String, Object>();
        resultMap.put("result","success");
        String sql ="select l.gysbh,l.gysmc,h.cd,h.xsddh,h.pch,h.ycmc,h.jyzl,h.jydj from t_sdzyc_jjg_ycgysxx l inner join (select n.cd,m.qybm,m.xsddh,m.pch,m.ycmc,m.jyzl,m.jydj from T_sdzyc_JJG_YYCRKXX n  inner join (select y.bzsl,x.qybm,x.ycmc,x.jydj,x.jyzl,x.xsddh,x.pch from t_sdzyc_jjg_ypbzxx y inner join(select t.qybm,t.xsddh,t.ycmc,t.jydj,m.jyzl,m.pch from t_Sdzyc_Jjg_Cpjyxx t inner join t_Sdzyc_Jjg_Cpjyxxxx m on t.xsddh=m.xsddh) x\n" +
                "on x.pch=y.scpch)m on n.pch=m.pch) h on l.qybm= h.qybm \n";
        return  DatabaseHandlerDao.getInstance().queryForMap(sql,new String[]{barCode});
    }
*/
    public Map<String,Object> searchjjgjyData(String barCode){
        Map<String,Object> resultMap = new HashMap<String, Object>();
        resultMap.put("result","success");
        Map<String, Object> jydataMap = null ;
        String jysql = "select jhpch  from t_cs_scjcxx t where jhpch =? ";

        //查询出场条码是否已经进场
        jydataMap = DatabaseHandlerDao.getInstance().queryForMap(jysql,new String[]{barCode});
        if(jydataMap==null || jydataMap.isEmpty()) {
            Map<String, Object> dataMap = null;
            String sql = "select ID,t.qybm,t.XSDDH, t.YCMC,t.SFJY,t.JYZL,t.JYDJ" +
                    "  from T_sdzyc_jjg_cpjyxx t" +
                    " where QYXSDDH =? ";

            //查询有效的出场信息:根据出场条码
            dataMap = DatabaseHandlerDao.getInstance().queryForMap(sql, new String[]{barCode});
             if (dataMap != null && !dataMap.isEmpty()){
           // if (dataMap != null) {
                if (dataMap.get("XSDDH") != null) {
                String xsddh = dataMap.get("XSDDH").toString();
                String qyxxSql = "select * from t_Sdzyc_Qyda t where t.dwlx='JJGQY'and t.QYBM=?";
                Map<String, Object> qyxxMap = DatabaseHandlerDao.getInstance().queryForMap(qyxxSql, new Object[]{dataMap.get("QYBM")});
                resultMap.put("qyData", qyxxMap);
                resultMap.put("type", 1);
                //查询精加工药材交易信息
                /*select n.jydz as CD,c.qybm,c.xsddh,c.qyxsddh,c.pch,c.ypmc,c.jyzl,c.jydj,c.bzsl,c.qyscpch,c.scrq,c.bzpch,c.qybzpch from t_sdzyc_qyda  n  inner join   (select m.bzsl,m.qybm,m.ypmc,m.jydj,m.jyzl,m.xsddh,m.qyxsddh,m.pch,a.qyscpch,a.yycpch,a.scrq,m.bzpch,m.qybzpch from t_Sdzyc_Jjg_Ypscxx a inner join                                 (select y.bzsl,y.bzpch,y.qybzpch,x.qybm,x.ypmc,x.jydj,x.jyzl,x.xsddh,x.qyxsddh,x.pch from t_sdzyc_jjg_ypbzxx y inner join t_Sdzyc_Jjg_Cpjyxxxx x
                on x.bzpch=y.bzpch)m on m.pch=a.scpch )c on n.qybm=c.qybm and n.dwlx='JJGQY' and c.xsddh='1101273000082431702152633'*/
                String jyxxsql = "select n.jydz as CD,c.qybm,c.xsddh,c.qyxsddh,c.pch,c.ypmc,c.jyzl,c.jydj,c.bzsl,c.qyscpch,c.scrq,c.bzpch,c.qybzpch from t_sdzyc_qyda  n  inner join (select m.bzsl,m.qybm,m.ypmc,m.jydj,m.jyzl,m.xsddh,m.qyxsddh,m.pch,a.qyscpch,a.yycpch,a.scrq,m.bzpch,m.qybzpch from t_Sdzyc_Jjg_Ypscxx a inner join (select y.bzsl,y.bzpch,y.qybzpch,x.qybm,x.ypmc,x.jydj,x.jyzl,x.xsddh,x.qyxsddh,x.pch from t_sdzyc_jjg_ypbzxx y inner join t_Sdzyc_Jjg_Cpjyxxxx x\n" +
                        "on x.bzpch=y.bzpch)m on m.pch=a.scpch )c on n.qybm=c.qybm  and n.dwlx='JJGQY' and c.xsddh=?";
                List<Map<String, Object>> jyxxsqlList = DatabaseHandlerDao.getInstance().queryForMaps(jyxxsql, new String[]{xsddh});
                if (jyxxsqlList != null && jyxxsqlList.size() > 0) {
                    resultMap.put("jyData", jyxxsqlList);
                }
                //获取列表需要的信息，交易详细信息
               /* select t.id,t.xsddh, t.pch,t.ypmc,t.jyzl,t.jydj,y.bzsl,t.qypch,t.qyxsddh,sc.scrq,sc.cdmc,y.bzpch,y.qybzpch from t_Sdzyc_Jjg_Cpjyxxxx t inner join t_Sdzyc_Jjg_Ypbzxx y on t.bzpch=y.bzpch
                inner join t_Sdzyc_Jjg_Ypscxx sc on t.pch = sc.scpch and t.xsddh='1101273000082431702162653'*/
                /*String jyxxxqsql = "select t.id,t.xsddh, t.pch,t.ypmc,t.jyzl,t.jydj,y.bzsl，t.qypch,t.qyxsddh from t_Sdzyc_Jjg_Cpjyxxxx t inner join t_Sdzyc_Jjg_Ypbzxx y on t.bzpch=y.bzpch where t.xsddh=?";*/
                String jyxxxqsql = "select t.id,t.xsddh, t.pch,t.ypmc,t.jyzl,t.jydj,y.bzsl,t.qypch,t.qyxsddh,sc.scrq,sc.cdmc,y.bzpch,y.qybzpch from t_Sdzyc_Jjg_Cpjyxxxx t inner join t_Sdzyc_Jjg_Ypbzxx y on t.bzpch=y.bzpch inner join t_Sdzyc_Jjg_Ypscxx sc on t.pch = sc.scpch and t.xsddh=?";
                List<Map<String, Object>> jyxxxqList = DatabaseHandlerDao.getInstance().queryForMaps(jyxxxqsql, new String[]{xsddh});
                if (jyxxxqList != null && jyxxxqList.size() > 0) {
                    resultMap.put("xqData", jyxxxqList);
                }
                resultMap.put("type", 1);
                return resultMap;
            }
        }
        }
        resultMap.put("result","error");
        return resultMap;
    }

    public Map<String,Object> searchZzData(String barCode){
        Map<String,Object> resultMap = new HashMap<String, Object>();
        resultMap.put("result","success");
        Map<String, Object> dataMap = null ;
       /* String sql = "select ID,t.qybm,t.khmc,t.zzl,t.KHBH, PSZRR, XSDDH, CCSJ, PSFS, CPH,t.cclsh,t.SFDH" +
                "  from T_ZZ_CCGL t" +
                " where SFDH =? " +
                "   AND IS_DELIVERED <> '1'";
        //查询有效的出场信息:根据出场条码
        dataMap = DatabaseHandlerDao.getInstance().queryForMap(sql,new String[]{barCode});*/
        if(dataMap!=null && !dataMap.isEmpty()){
            //查询企业相关信息
            String qyxxSql = " select *from t_Sdzyc_Qyda t where t.dwlx='JJGQY'and T.QYBM=?";
            Map<String,Object> qyxxMap = DatabaseHandlerDao.getInstance().queryForMap(qyxxSql,new Object[]{dataMap.get("QYBM")});
            resultMap.put("qyData",qyxxMap);
            //查询出场详细信息
            //散货出场信息
            String shSql = "select t_.ID," +
                    "       t_.PCH," +
                    "       t_.CPZSM," +
                    "       t_.PZ," +
                    "       a.PLBH," +
                    "       t_.PZBH," +
                    "       t_.CSZZL," +
                    "       t_.KCZL," +
                    "       t_.CCZL," +
                    "       t_.CJZDBH,t_.ZSPCH," +
                    "       t_.PID " +
                    "  from T_ZZ_CCSHXX t_ left join t_zz_xpzxx a on t_.pzbh = a.pzbh and t_.qybm = a.qybm" +
                    " where (t_.PID = ? ) AND (t_.IS_DELETE <> '1')";
            List<Map<String,Object>> shList = DatabaseHandlerDao.getInstance().queryForMaps(shSql,new Object[]{dataMap.get("ID")});
            if(shList!=null && shList.size()>0){
                resultMap.put("shData",shList);
            }
            //产品出场信息
            String cpSql = "select t.bzlsh,t.cpbh,t.cpmc,t.cpdj,t.ccjs,t.zl,t.zsm,t.ZSPCH from T_ZZ_CCBZCPXX t where t.pid = ? AND (IS_DELETE <> '1') ";
            List<Map<String,Object>> cpList = DatabaseHandlerDao.getInstance().queryForMaps(cpSql,new Object[]{dataMap.get("ID")});
            if(cpList!=null && cpList.size()>0){
                resultMap.put("cpData",cpList);
            }

            resultMap.put("ccData",dataMap);
            resultMap.put("type",1);
            return resultMap;
        }
        resultMap.put("result","error");
        return resultMap;
    }

//    @Override
//    public Object saveAll(String tableId, String entityJson, String dTableId, String dEntitiesJson, Map<String, Object> paramMap) {
//        Object obj = super.saveAll(tableId, entityJson, dTableId, dEntitiesJson, paramMap);
//        JsonNode entity = JsonUtil.json2node(entityJson);
//        Map<String, String> dataMap = node2map(entity);
//        String sql = "update T_ZZ_CCGL t set IS_DELIVERED = '1' where SFDH =?";
//        DatabaseHandlerDao.getInstance().executeSql(sql,new Object[]{dataMap.get("JHPCH")});
//        return obj;
//    }

    private void sendTradeIn (TradeInEntity tradeInEntity) {
        EnterpriseService service = new RearClientBuilder().build(EnterpriseService.class);
        service.createTradeIn(tradeInEntity);
    }

    private void sendPreload (List<TraceEntity> traceEntities) {
        EnterpriseService service = new RearClientBuilder().build(EnterpriseService.class);
        service.preLoad(traceEntities);
    }

    @Transactional
    public Object saveAll(String xsddhs,String tableId, String entityJson, String dTableId, String dEntitiesJson, Map<String, Object> paramMap) {
        String[] xsddhsArr = JSON.fromJSON(xsddhs, new TypeReference<String[]>() {});
        entityJson = entityJson.replace("\"CSBM\":\"\"","\"CSBM\":\""+SerialNumberUtil.getInstance().getCompanyCode()+"\"");
        JsonNode entity = JsonUtil.json2node(entityJson);
        Map<String, String> dataMap = node2map(entity);//主表信息
        MessageModel message =  (MessageModel) super.saveAll(tableId, entityJson, dTableId, dEntitiesJson, paramMap);
        /*************同步数据start*************/
        String comp_code = SerialNumberUtil.getInstance().getCompanyCode();
        String sql = "select zsm from t_sdzyc_zsm where bzpch in (\n" +
            "select bzpch from t_sdzyc_jjg_ypbzxx where scpch in (\n" +
            "select pch from t_Sdzyc_Jjg_Cpjyxxxx where xsddh in (";
        String condition = "'!'";
        List p = new ArrayList();
        for (String xsddh : xsddhsArr) {
            condition += ",?";
            p.add(xsddh);
        }
        sql += condition;
        sql += ")))";
        List zsms = DatabaseHandlerDao.getInstance().queryForList(sql,p.toArray());
        String in_id = ((Map)((Map)message.getData()).get("master")).get("ID").toString();
        Date d = new Date();
        TradeInEntity tradeInEntity = new TradeInEntity();
        tradeInEntity.setId(in_id);
        tradeInEntity.setComp_code(comp_code);
        tradeInEntity.setDate(d.getTime());
        tradeInEntity.setComp_type("4");
        List<TraceEntity> traceEntities = new ArrayList<TraceEntity>();
        for (Object zsm : zsms) {
            TraceEntity traceEntity = new TraceEntity();
            traceEntity.setComp_type("4");
            traceEntity.setComp_code(comp_code);
            traceEntity.setIn_id(in_id);
            traceEntity.setIn_trace_code(zsm.toString());
            traceEntities.add(traceEntity);
        }
        sendPreload(traceEntities);
        sendTradeIn(tradeInEntity);
        /*************同步数据end*************/

        //看不出来下面有什么用 by Synge
        Map<String,Object> map = (Map) message.getData();
        List<Map<String,String>> detail = (List<Map<String, String>>) map.get("detail");//从表信息
        dataMap.put("CSBM",SerialNumberUtil.getInstance().getCompanyCode());
//        String csmc = CompanyInfoUtil.getInstance().getCompanyName("CS",dataMap.get("CSBM"));
        //String cIds = queryCidsByDwbm(dataMap.get("CSBM:"""));
        List<Map<String,String>> newDetail = new ArrayList<Map<String, String>>();
        for(Map<String,String> m :detail){
           /* /*****同步追溯信息*****//*
            TCsptZsxxEntity zsEntity = new TCsptZsxxEntity();
            zsEntity.setJhpch(m.get("JHPCH"));
            zsEntity.setJypzh(m.get("LSPZH"));
            zsEntity.setQymc(csmc);
            zsEntity.setQybm(dataMap.get("CSBM"));
            zsEntity.setJyzbm(dataMap.get("GYSBM"));
            zsEntity.setJyzmc(dataMap.get("GYSMC"));
            zsEntity.setXtlx("7");
            zsEntity.setRefId(m.get("ID"));
            TraceChainUtil.getInstance().syncZsxx(zsEntity);
            *//********结束*******//*
*/
//            String zsm = m.get("LSPZH");
           /* if(StringUtil.isBlank(dataMap.get("ID"))) {
                if (StringUtil.isNotBlank(cIds)) {
                    if(isShcp(m.get("SPBM"))||(m.get("ZL")!=null&&!m.get("ZL").equals("")&&Float.parseFloat(m.get("ZL"))!=0)) {//判断是散货还是包装
                        newDetail.add(m);
                    }else{
                        m.put("SPBM","99999999");//包装标准码
                        newDetail.add(m);
                    }
                }
            }*/
            /********end*******/
        }
      /*  *//******生成TXT******//*
        if (newDetail.size()>0) {
            createTxt(cIds, newDetail, dataMap);
        }
        *//******生成TXT******//*
        *//******** 同步上家交易信息 ********//*
        String barCode = ServletActionContext.getRequest().getParameter("barCode");
        if (barCode != null && !"".equals(barCode)) {
            String prefix = barCode.substring(0,2);
            if (prefix.equalsIgnoreCase("PC")) {//原批发市场交易数据更改为已用状态
                String sql = "update t_pc_jyxx t set t.is_in = '1' where t.JYTMH = '" + barCode + "'";
                DatabaseHandlerDao.getInstance().executeSql(sql);
            } else if (prefix.equalsIgnoreCase("ZZ")) {//原种植场出场数据更给为已用状态
                String sql = "update T_ZZ_CCGL t set IS_DELIVERED = '1' where SFDH =?";
                DatabaseHandlerDao.getInstance().executeSql(sql,new Object[]{dataMap.get("JHPCH")});
            }else if (prefix.equalsIgnoreCase("YZ")) {//原养殖场出场数据更给为已用状态
                String sql = "update t_yz_clxx t set t.is_in = '1' where upper(t.cctmh) = '" + barCode + "'";
                DatabaseHandlerDao.getInstance().executeSql(sql);
            }else if (prefix.equalsIgnoreCase("TZ")) {//原屠宰场交易数据更给为已用状态
                String sql = "update t_tz_jyxx t set t.is_in = '1' where t.zsm = '" + dataMap.get("ZSPZH")+ "'";
                DatabaseHandlerDao.getInstance().executeSql(sql);
            }else if (prefix.equalsIgnoreCase("PR")) {//原肉品批发市场交易数据更给为已用状态
                String sql = "update t_pr_jyxx t set t.is_in = '1' where t.jytmh = '" + barCode + "'";
                DatabaseHandlerDao.getInstance().executeSql(sql);
            }
        }
        *//******** 同步上家交易信息 ********/

        return message;
    }

    /*public boolean isShcp(String spbm){
        String sql = "select id from t_common_scspxx where spbm = ?";
//        String sql = "SELECT T.ID FROM T_ZZ_CCSHXX T WHERE T.CPZSM=?";
        List list = DatabaseHandlerDao.getInstance().queryForMaps(sql,new Object[]{spbm});
        if(list.size()>0){
            return true;
        }else{
            return false;
        }
    }*/
    /**
     * 根据单位编码获取单位下所有称ID
     * @param qybm
     * @return
     */
   /* public String queryCidsByDwbm(String qybm){
        String sql = "SELECT T.ID FROM T_QYPT_SBGL T WHERE T.DWBM = ?";
        List<Map<String,Object>> list = DatabaseHandlerDao.getInstance().queryForMaps(sql,new Object[]{qybm});
        String cIds ="";
        for(Map m:list){
            if(StringUtil.isNotBlank(cIds)){
                cIds+=",";
            }
            cIds+=m.get("ID").toString();
        }
        return cIds;
    }
*/
    /**
     * 下传TXT
     *
     * @param cId
     */
    public void createTxt(String cId,List<Map<String,String>> detailMaps, Map zbMap) {
        String content = "";
        for (Map<String,String> dm : detailMaps) {
            content +=  DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss") + "\t" + dm.get("LSPZH") + "\t" + dm.get("SPBM") +
                    "\t\t" + zbMap.get("GYSMC") + "\r\n";
        }
        content = content.substring(0,content.length()-2);
        String[] cIds = cId.split(",");
        File file = new File(readTxtProp());
        if  (!file .exists()  && !file .isDirectory()){
            file .mkdirs();
        }
        for (String id : cIds) {
            Map cxx = queryCxxByCid(id);
            String fileName = cxx.get("DWBM") + "_" + cxx.get("CCBH") + "_" + cxx.get("BH") + "_" + DateFormatUtils.format(new Date(), "yyyyMMddHHmmss") + ".txt";
            File newFile = new File(readTxtProp() + "/"+fileName);
            try {
                TxtUtils.writeTxtFile(content, newFile, null);
                TxtUtils.createFile(newFile);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 下传TXT
     *
     * @param cId
     * @param zsm
     */
    public void createTxt(String cId,Map detailMap, Map zbMap, String zsm) {
        String content = DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss") + "\t" + zsm + "\t" + detailMap.get("SPBM") +
                "\t\t" + zbMap.get("GYSMC");
        String[] cIds = cId.split(",");
        File file = new File(readTxtProp());
        if  (!file .exists()  && !file .isDirectory()){
            file .mkdirs();
        }
        for (String id : cIds) {
            Map cxx = queryCxxByCid(id);
            String fileName = cxx.get("DWBM") + "_" + cxx.get("CCBH") + "_" + cxx.get("BH") + "_" + DateFormatUtils.format(new Date(), "yyyyMMddHHmmss") + ".txt";
            File newFile = new File(readTxtProp() + "/"+fileName);
            try {
                TxtUtils.writeTxtFile(content, newFile, null);
                TxtUtils.createFile(newFile);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    /**
     * 通过称ID查询称信息
     *
     * @param cId
     * @return
     */
    public Map queryCxxByCid(String cId) {
        String sql = "SELECT T.ID,T.DWBM,T.CCBH,T.BH FROM T_QYPT_SBGL T WHERE T.ID= ?";
        return DatabaseHandlerDao.getInstance().queryForMap(sql, new Object[]{cId});
    }
    /**
     * 读取txt.properties里的输出url
     *
     * @return
     */
    public String readTxtProp() {
        Properties prop = new Properties();
        try {
            InputStream in = new FileInputStream(ComponentFileUtil.getConfigPath() + "trace/txt.properties");
            prop.load(in);
            String url = prop.getProperty("csDownUrl").trim();
            return url;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Object getCdmcByCdbm(String cdbm){
        StringBuilder sql = new StringBuilder("select CDBM,CDMC from t_common_cdxx WHERE CDBM = '" + cdbm + "'");
        Map<String, Object> dataMap = DatabaseHandlerDao.getInstance().queryForMap(sql.toString());
        return dataMap;
    }

}