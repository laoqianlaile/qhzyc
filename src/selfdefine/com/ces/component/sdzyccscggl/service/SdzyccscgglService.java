package com.ces.component.sdzyccscggl.service;

import cesgroup.rear.lib.client.enterprise.RearClientBuilder;
import com.ces.component.sdzyccscggl.dao.SdzyccscgglDao;
import com.ces.component.trace.service.base.TraceShowModuleDefineDaoService;
import com.ces.component.trace.utils.QRCodeUtil;
import com.ces.component.trace.utils.SerialNumberUtil;
import com.ces.component.trace.utils.StatisticalCodeUtil;
import com.ces.config.dhtmlx.dao.common.DatabaseHandlerDao;
import com.ces.config.utils.AppDefineUtil;
import com.ces.config.utils.StringUtil;
import com.ces.config.utils.UUIDGenerator;
import com.ces.xarch.core.entity.StringIDEntity;
import enterprise.endpoints.EnterpriseService;
import enterprise.entity.PlantEntity;
import enterprise.entity.PlantTask;
import enterprise.entity.TestEntity;
import enterprise.entity.TraceEntity;
import org.apache.commons.io.FileUtils;
import org.apache.struts2.ServletActionContext;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import sun.misc.BASE64Encoder;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import static org.aspectj.weaver.tools.cache.SimpleCacheFactory.path;

@Component
public class SdzyccscgglService extends TraceShowModuleDefineDaoService<StringIDEntity, SdzyccscgglDao> {
    //    public Map<String, String> save(String tableId, String entityJson, Map<String, Object> paramMap) {
//        String tableName = getTableName(tableId);
//        JsonNode entityNode = JsonUtil.json2node(entityJson);
//        Map<String, String> dataMap = node2map(entityNode);
//        String id = dataMap.get("ID");
//        if(StringUtil.isEmpty(id)){//是新增操作
//            String cspch = SerialNumberUtil.getInstance().getSerialNumber("SDZYC", "SDZYCCSPCH", false);
//            dataMap.put("CSPCH",cspch);
//        }
//        id = save(tableName, dataMap, paramMap);
//        dataMap.put(AppDefineUtil.C_ID, id);
//        return dataMap;
//    }
    public Map<String,Object> getCspch(){
        String qybm = SerialNumberUtil.getInstance().getCompanyCode();
        String sql = "select T.qycspch as qypch,T.CSPCH,T.ZZPCH,T.YCMC,T.YCDM,T.CSZL from T_SDZYC_CSGLXX T where sfjc='0' and qybm='"+qybm+"'  order by create_time desc";
        List<Map<String, Object>> list = DatabaseHandlerDao.getInstance().queryForMaps(sql);
        return getResultData(list);
    }
    public Map<String,Object> getZzpch(){
        String sql = "select T.ZZPCH,T.ZMZZMC,T.YCMC,T.YCDM,T.JDMC,T.DKMC from T_ZZ_SCDA T where qybm=? order by zzpch desc";
        List<Map<String, Object>> list = DatabaseHandlerDao.getInstance().queryForMaps(sql,new String[]{SerialNumberUtil.getInstance().getCompanyCode()});
        return getResultData(list);
    }
    public Map<String,Object> getylrkcspch(){
        String sql = "select q.qymc, m.*\n" +
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
                "         where c.sfjc='0' and c.zzpch = s.zzpch\n" +
                "           and c.qybm = s.qybm\n" +
                "           and s.qybm = j.qybm\n" +
                "           and s.jdbh = j.jdbh  order by c.cspch desc) m,\n" +
                "       t_sdzyc_qyda q\n" +
                " where q.qybm = m.qybm\n" +
                "   and q.dwlx = 'ZZQY'\n" +
                "   and q.qybm = ?\n " +
                " order by m.jssj desc ";
        List<Map<String, Object>> list = DatabaseHandlerDao.getInstance().queryForMaps(sql,new String[]{SerialNumberUtil.getInstance().getCompanyCode()});
        return getResultData(list);
    }
    public Map<String,Object> getResultData(List<Map<String,Object>> data){
        Map<String,Object> result = new HashMap<String,Object>();
        result.put("data", data);
        return result;
    }

    @Transactional
    public Object save(Map map, List<File> imageUpload, List<String> imageUploadFileName, String REAL_PATH) {
        Map newMap = new HashMap();
        Set<Map.Entry> allSet = map.entrySet();
        Iterator<Map.Entry> iter = allSet.iterator();
        while(iter.hasNext()){
            Map.Entry entry = iter.next();
            String[] valueArray = (String[])entry.getValue();
            String value = "" ;
            for( int i = 0 ; i < valueArray.length ; i ++){
                value = valueArray[i];
            }
            newMap.put(entry.getKey() , value );
        }
//        String id  = getService().save("T_SDZYC_JDXX" ,newMap ,null);
        String id = String.valueOf(newMap.get("ID"));
        if(StringUtil.isEmpty(id)){//是新增操作
            String cspch = StatisticalCodeUtil.getInstance().getTwentyFivePcm("ZZ","SDZYC","SDZYCYLJYJCBH");//SerialNumberUtil.getInstance().getSerialNumber("SDZYC", "SDZYCCSPCH", false);
            newMap.put("CSPCH",cspch);
            newMap.put("QYCSPCH",cspch.substring(cspch.length()-11,cspch.length()));
            id = save("T_SDZYC_CSGLXX" ,newMap ,null);
            try {
                //根据采收批次号进行随附单生成
                QRCodeUtil.encode(cspch, ServletActionContext.getServletContext().getRealPath("/qrCode/zz"));
            } catch (Exception e) {
                e.printStackTrace();
            }
            newMap.put(AppDefineUtil.C_ID, id);
            if(!StringUtil.isEmpty(id)){//数据保存成功执行文件上传操作
                if( null != imageUpload){
                    //进行文件上传，则删除历史保存文件
                    File oldFile = new File(REAL_PATH+"/"+newMap.get("TPSC"));
                    String[] args = imageUploadFileName.get(0).split("\\.");
                    String fileNameWithStamp = System.currentTimeMillis()+ UUIDGenerator.uuid()+"."+args[1];
                    File newFile = new File(REAL_PATH+"/"+fileNameWithStamp);
                    try {
                        if(oldFile.exists()){
                            oldFile.delete();
                        }
                        FileUtils.copyFile(imageUpload.get(0),newFile);
                        //文件上传成功进行数据保存
                        newMap.put("ID",id);
                        newMap.put("TPSC",fileNameWithStamp);
                        save("T_SDZYC_CSGLXX" ,newMap ,null);
                        try {
                            newMap.put("base64", encodeBase64File(REAL_PATH+"/"+fileNameWithStamp));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } catch (IOException e) {
                        //上传文件失败删除保存数据和图片
                        delete(id);
                        if(newFile.exists()){
                            oldFile.delete();
                        }
                    }

                }
            }
            sendCreatePlantService(searchScxxByZzpch(newMap));
            sendCreatePreLoadService(traceInfoByCspch(newMap));
            modifyScdaZt(newMap);
            return newMap;
        }
        save("T_SDZYC_CSGLXX" ,newMap ,null);
        newMap.put(AppDefineUtil.C_ID, id);
        if(!StringUtil.isEmpty(id)){//数据保存成功执行文件上传操作
            if( null != imageUpload){
                //进行文件上传，则删除历史保存文件
                File oldFile = new File(REAL_PATH+"/"+newMap.get("TPSC"));
                String fileNameWithStamp = System.currentTimeMillis() + "_" + imageUploadFileName.get(0);
                File newFile = new File(REAL_PATH+"/"+fileNameWithStamp);
                try {
                    if(oldFile.exists()){
                        oldFile.delete();
                    }
                    FileUtils.copyFile(imageUpload.get(0),newFile);
                    //文件上传成功进行数据保存
                    newMap.put("ID",id);
                    newMap.put("TPSC",fileNameWithStamp);
                    save("T_SDZYC_CSGLXX" ,newMap ,null);
                } catch (IOException e) {
                    //上传文件失败删除保存数据和图片
                    delete(id);
                    if(newFile.exists()){
                        oldFile.delete();
                    }
                }

            }
        }
        return newMap;
    }

    /**
     * 将文件转成base64 字符串
     * @param path
     * @return  *
     * @throws Exception
     */

    public static String encodeBase64File(String path) throws Exception {
        InputStream in;
        byte[] data = null;
        //读取图片字节数组
        try
        {
            in = new FileInputStream(path);
            data = new byte[in.available()];
            in.read(data);
            in.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        //对字节数组Base64编码
        BASE64Encoder encoder = new BASE64Encoder();
        return encoder.encode(data);//返回Base64编码过的字节数组字符串

    }

    public void modifyScdaZt(Map<String, String> newMap){
        String zzpch = newMap.get("ZZPCH");
        String sql = "update t_zz_scda t set zt='0' where zzpch=?";
        DatabaseHandlerDao.getInstance().executeSql(sql, new Object[]{zzpch});
    }
    private void sendCreatePlantService(Map<String, Object> map){
        EnterpriseService service = new RearClientBuilder().build(EnterpriseService.class);
        PlantEntity pe = new PlantEntity();
        pe.setComp_code((String) map.get("QYBM"));
        pe.setId((String)map.get("ID"));
        pe.setPlant_batch_no((String)map.get("ZZPCH"));
        pe.setSeed_name((String)map.get("ZMZZMC"));
        pe.setPerson_in_charge((String)map.get("ZZFZR"));
        pe.setHerb_name((String)map.get("YCMC"));
        if(!StringUtil.isEmpty(map.get("seedImage"))){
            pe.setSeed_image((String) map.get("seedImage"));
        }
        if (!StringUtil.isEmpty(map.get("TPSC"))) {
            pe.setImage((String)map.get("TPSC"));
        }
        pe.setArea(Float.parseFloat(String.valueOf(map.get("area"))));
        pe.setDev_key(String.valueOf(map.get("CGQZ")));
        pe.setHarvest_batch_no((String)map.get("CSPCH"));
        pe.setBlock(map.get("DKMC") == null ? "" :String.valueOf( map.get("DKMC")));
        pe.setPlantTasks((List<PlantTask>) map.get("task"));
        pe.setHarvest_begin_date(dateToLong((String)map.get("KSSJ"),null));
        pe.setHarvest_end_date(dateToLong((String)map.get("JSSJ"),null));
        String cszl = map.get("CSZL").toString();
        pe.setHarvest_weight(Float.parseFloat(cszl));
        pe.setPlant_date(dateToLong((String)map.get("ZZSJ"),null));
        pe.setBase64((String) map.get("base64"));
        pe.setSeed_base64((String) map.get("seedBase"));
        Map<String, Object> zzMap = new HashMap<String, Object>();
        String zzsql = "select ZZLY,ZZZL from t_zz_scda t where t.zzpch=? and qybm=?";
        zzMap = DatabaseHandlerDao.getInstance().queryForMap(zzsql, new String[]{(String)map.get("ZZPCH"),SerialNumberUtil.getInstance().getCompanyCode()});
        pe.setSeed_source(String.valueOf(zzMap.get("ZZLY")));
        pe.setSeed_weight(String.valueOf(zzMap.get("ZZZL")));
        service.createPlant(pe);
    }
    private void sendCreatePreLoadService(Map<String, Object> traceMap){
        EnterpriseService service = new RearClientBuilder().build(EnterpriseService.class);
        service.preLoad((List<TraceEntity>) traceMap.get("traceEntity"));
    }

    public Map<String, Object> traceInfoByCspch(Map<String, String> newMap){
        String zzpch = (String)newMap.get("ZZPCH");
        String sql1 = "select t.ID from T_ZZ_SCDA t where t.ZZPCH=? and t.qybm=?";
        Object processId = DatabaseHandlerDao.getInstance().queryForObject(sql1, new String[]{zzpch,SerialNumberUtil.getInstance().getCompanyCode()});
        String sql = "select distinct t.ycxmname from t_sdzyc_zycspbm t where t.zsspm=?";
        Map<String, Object> traceMap = new HashMap<String, Object>();
        traceMap.put("ycdetail",DatabaseHandlerDao.getInstance().queryForMap(sql, new String[]{newMap.get("YCDM")}).get("YCXMNAME"));
        List<TraceEntity> traceEntities = new ArrayList<TraceEntity>();
        TraceEntity traceEntity = new TraceEntity();
        traceEntity.setAtom(newMap.get("CSPCH"));
        traceEntity.setComp_code(newMap.get("QYBM"));
        traceEntity.setOut_trace_code(newMap.get("CSPCH"));
        traceEntity.setProcess_id(processId == null ? "" : processId.toString());
        traceEntity.setComp_type("1");
        traceEntities.add(traceEntity);
        traceMap.put("traceEntity", traceEntities);
        return traceMap;
    }
    public Map<String, Object> searchScxxByZzpch(Map<String, Object> newMap){
        String zzpch = (String)newMap.get("ZZPCH");
        Map<String, Object> tempMap = new HashMap<String, Object>();
        tempMap.putAll(newMap);
        tempMap.remove("ID");
        Map<String, Object> dataMap;
        String qybm = SerialNumberUtil.getInstance().getCompanyCode();
        String sql = "select t.ID, t.QYBM, t.ZZPCH, t.ZMZZMC, t.ZMZZBH, t.ZZFZR, t.ZZSJ, t.YCMC, t.QSNSXSJ, t.DKBH ,t.DKMC ,d.CGQZ from T_ZZ_SCDA t " +
            "left join t_sdzyc_zzdkxx d on t.dkbh = d.dkbh and d.qybm = ? where t.ZZPCH=? and t.qybm=?";
        dataMap = DatabaseHandlerDao.getInstance().queryForMap(sql, new String[]{qybm,zzpch,qybm});
/*        String tpsc = imgae((String)dataMap.get("ZMZZBH")).get("TPSC").toString();
        if (null != tpsc&& !"".equals(tpsc)) {
            dataMap.put("seedImage", tpsc);
            String REAL_PATH = ServletActionContext.getServletContext().getRealPath("/spzstpfj")+"/"+tpsc;
            try {
                dataMap.put("seedBase", encodeBase64File(REAL_PATH));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }*/

        String sql1 = "select t.MJ from t_sdzyc_zzdkxx t where t.DKBH=? and qybm=?";
        dataMap.put("area", DatabaseHandlerDao.getInstance().queryForMap(sql1, new String[]{(String) dataMap.get("DKBH"),SerialNumberUtil.getInstance().getCompanyCode()}).get("MJ"));
        List<PlantTask> taskList = searchNsxByPid(dataMap);
        List<TestEntity> testEntities = getTestEntity(dataMap);
        dataMap.put("test", testEntities);
        dataMap.put("task", taskList);
        dataMap.putAll(tempMap);
        return dataMap;
    }
    public Map<String, Object> imgae(String bh){
        String sql = "select t.tpsc from t_sdzyc_zzzm t where t.zzzmbh=? and qybm=?";
        return DatabaseHandlerDao.getInstance().queryForMap(sql, new String[]{bh,SerialNumberUtil.getInstance().getCompanyCode()});
    }
    public List<TestEntity> getTestEntity(Map<String, Object> dataMap){
        String zzpch = (String) dataMap.get("ZZPCH");
        List<TestEntity> testEntities = new ArrayList<TestEntity>();
        String sql = "select * from t_sdzyc_yljyjcxx t where t.zzpch =?";
        List<Map<String, Object>> testMap = DatabaseHandlerDao.getInstance().queryForMaps(sql, new String[]{zzpch});
        if (!StringUtil.isEmpty(testMap)){
            for (int i=0; i<testMap.size(); i++){
                TestEntity testEntity = new TestEntity();
                testEntity.setId((String) testMap.get(i).get("ID"));
                testEntity.setBatch_no((String) testMap.get(i).get("ZZPCH"));
                testEntity.setComp_code((String) testMap.get(i).get("QYBM"));
                testEntity.setDate(dateToLong((String) testMap.get(i).get("JYSJ"),null));
                testEntity.setPerson((String) testMap.get(i).get("JYRY"));
                testEntity.setMethod((String) testMap.get(i).get("JYFF"));
                testEntities.add(testEntity);
            }
        }
        return testEntities;
    }
    public List<PlantTask> searchNsxByPid(Map<String, Object> dataMap){
        List<PlantTask> tasks = new ArrayList<PlantTask>();
        //masterID
        String pid = (String) dataMap.get("ID");
        //播种
        String sql1 = "select * from t_zz_scbz t where t.pid=?";
        List<Map<String, Object>> bzMap = DatabaseHandlerDao.getInstance().queryForMaps(sql1, new String[]{pid});
        for (int i=0; i<bzMap.size(); i++){
            PlantTask plantTask = new PlantTask();
            String id = (String) bzMap.get(i).get("ID");
            sql1 = "select t.TRPMC, t.TRPLX from t_zz_scbztrp t where t.pid=?";
            List<Map<String, Object>> bztrpMap = DatabaseHandlerDao.getInstance().queryForMaps(sql1, new String[]{id});
            if (!StringUtil.isEmpty(bztrpMap)) {
                for (int j = 0; j < bztrpMap.size(); j++) {
                    String trptymbh = (String) bztrpMap.get(j).get("TRPMC");
                    String trplx = (String) bztrpMap.get(j).get("TRPLX");
                    if (trplx.equals("ZZ") || trplx.equals("zhongmiao")) {
                        Map<String, Object> tymMap = searchTrpByTypbh(trptymbh, trplx);
                        plantTask.setInputs((String) tymMap.get("ZZZMMC"));
                    } else if (trplx.equals("NJJ")) {
                        Map<String, Object> tymMap = searchTrpByTypbh(trptymbh, trplx);
                        plantTask.setInputs((String) tymMap.get("NJJMC"));
                    } else {
                        plantTask.setInputs((String) bztrpMap.get(j).get("TRPMC"));
                    }
                }
            }
            String pch = (String) dataMap.get("ZZPCH");
            plantTask.setBegin_date(dateToLong((String) dataMap.get("QSNSXSJ"),String.valueOf(bzMap.get(i).get("NSXJGSJ"))));
            plantTask.setComp_code((String) dataMap.get("QYBM"));
            plantTask.setId((String)bzMap.get(i).get("ID"));
            plantTask.setTask_no((String) bzMap.get(i).get("NSZYXBH"));
            plantTask.setTask_name((String) bzMap.get(i).get("NSZYXMC"));
            plantTask.setPlant_batch_no(pch);
            plantTask.setPlant_id(pid);
            tasks.add(plantTask);
        }
        //灌溉
        String sql2 = "select * from t_zz_scgg t where t.pid=?";
        List<Map<String, Object>> ggMap = DatabaseHandlerDao.getInstance().queryForMaps(sql2, new String[]{pid});
        for (int i=0; i<ggMap.size(); i++){
            PlantTask plantTask = new PlantTask();
            String id = (String) ggMap.get(i).get("ID");
            sql2 = "select t.TRPMC, t.TRPLX from t_zz_scggtrp t where t.pid=?";
            List<Map<String, Object>> ggtrpMap = DatabaseHandlerDao.getInstance().queryForMaps(sql2, new String[]{id});
            if (!StringUtil.isEmpty(ggtrpMap)) {
                for (int j = 0; j < ggtrpMap.size(); j++) {
                    String trptymbh = (String) ggtrpMap.get(j).get("TRPMC");
                    String trplx = (String) ggtrpMap.get(j).get("TRPLX");
                    if (trplx.equals("ZZ") || trplx.equals("zhongmiao")) {
                        Map<String, Object> tymMap = searchTrpByTypbh(trptymbh, trplx);
                        plantTask.setInputs((String) tymMap.get("ZZZMMC"));
                    } else if (trplx.equals("NJJ")) {
                        Map<String, Object> tymMap = searchTrpByTypbh(trptymbh, trplx);
                        plantTask.setInputs((String) tymMap.get("NJJMC"));
                    } else {
                        plantTask.setInputs((String) ggtrpMap.get(j).get("TRPMC"));
                    }
                }
            }
            String pch = (String) dataMap.get("ZZPCH");
            plantTask.setBegin_date(dateToLong((String) dataMap.get("QSNSXSJ"),String.valueOf(ggMap.get(i).get("NSXJGSJ"))));
            plantTask.setComp_code((String) dataMap.get("QYBM"));
            plantTask.setId((String)ggMap.get(i).get("ID"));
            plantTask.setTask_no((String) ggMap.get(i).get("NSZYXBH"));
            plantTask.setTask_name((String) ggMap.get(i).get("NSZYXMC"));
            plantTask.setPlant_batch_no(pch);
            plantTask.setPlant_id(pid);
            tasks.add(plantTask);
        }
        //施肥
        String sql3 = "select * from t_zz_scsf t where t.pid=?";
        List<Map<String, Object>> sfMap = DatabaseHandlerDao.getInstance().queryForMaps(sql3, new String[]{pid});
        for (int i=0; i<sfMap.size(); i++){
            PlantTask plantTask = new PlantTask();
            String id = (String) sfMap.get(i).get("ID");
            sql3 = "select t.TRPMC, t.TRPLX from t_zz_scsftrp t where t.pid=?";
            List<Map<String, Object>> sftrpMap = DatabaseHandlerDao.getInstance().queryForMaps(sql3, new String[]{id});
            if (!StringUtil.isEmpty(sftrpMap)) {
                for (int j = 0; j < sftrpMap.size(); j++) {
                    String trptymbh = (String) sftrpMap.get(j).get("TRPMC");
                    String trplx = (String) sftrpMap.get(j).get("TRPLX");
                    if (trplx.equals("ZZ") || trplx.equals("zhongmiao")) {
                        Map<String, Object> tymMap = searchTrpByTypbh(trptymbh, trplx);
                        plantTask.setInputs((String) tymMap.get("ZZZMMC"));
                    } else if (trplx.equals("NJJ")) {
                        Map<String, Object> tymMap = searchTrpByTypbh(trptymbh, trplx);
                        plantTask.setInputs((String) tymMap.get("NJJMC"));
                    } else {
                        plantTask.setInputs((String) sftrpMap.get(j).get("TRPMC"));
                    }
                }
            }
            String pch = (String) dataMap.get("ZZPCH");
            plantTask.setBegin_date(dateToLong((String) dataMap.get("QSNSXSJ"),String.valueOf(sfMap.get(i).get("NSXJGSJ"))));
            plantTask.setComp_code((String) dataMap.get("QYBM"));
            plantTask.setId((String)sfMap.get(i).get("ID"));
            plantTask.setTask_no((String) sfMap.get(i).get("NSZYXBH"));
            plantTask.setTask_name((String) sfMap.get(i).get("NSZYXMC"));
            plantTask.setPlant_batch_no(pch);
            plantTask.setPlant_id(pid);
            tasks.add(plantTask);
        }
        //用药
        String sql4 = "select * from t_zz_scyy t where t.pid=?";
        List<Map<String, Object>> yyMap = DatabaseHandlerDao.getInstance().queryForMaps(sql4, new String[]{pid});
        for (int i=0; i<yyMap.size(); i++){
            PlantTask plantTask = new PlantTask();
            String id = (String) yyMap.get(i).get("ID");
            sql4 = "select t.TRPMC, t.TRPLX from t_zz_scyytrp t where t.pid=?";
            List<Map<String, Object>> yytrpMap = DatabaseHandlerDao.getInstance().queryForMaps(sql4, new String[]{id});
            if (!StringUtil.isEmpty(yytrpMap)) {
                for (int j = 0; j < yytrpMap.size(); j++) {
                    String trptymbh = (String) yytrpMap.get(j).get("TRPMC");
                    String trplx = (String) yytrpMap.get(j).get("TRPLX");
                    if (trplx.equals("ZZ") || trplx.equals("zhongmiao")) {
                        Map<String, Object> tymMap = searchTrpByTypbh(trptymbh, trplx);
                        plantTask.setInputs((String) tymMap.get("ZZZMMC"));
                    } else if (trplx.equals("NJJ")) {
                        Map<String, Object> tymMap = searchTrpByTypbh(trptymbh, trplx);
                        plantTask.setInputs((String) tymMap.get("NJJMC"));
                    } else {
                        plantTask.setInputs((String) yytrpMap.get(j).get("TRPMC"));
                    }
                }
            }
            String pch = (String) dataMap.get("ZZPCH");
            plantTask.setBegin_date(dateToLong((String) dataMap.get("QSNSXSJ"),String.valueOf(yyMap.get(i).get("NSXJGSJ"))));
            plantTask.setComp_code((String) dataMap.get("QYBM"));
            plantTask.setId((String)yyMap.get(i).get("ID"));
            plantTask.setTask_no((String) yyMap.get(i).get("NSZYXBH"));
            plantTask.setTask_name((String) yyMap.get(i).get("NSZYXMC"));
            plantTask.setPlant_batch_no(pch);
            plantTask.setPlant_id(pid);
            tasks.add(plantTask);
        }
        //采收 :山东中药材不同步采收数据，采收时间在追溯页面上面拼接到种植树的最顶端

        //除草
        String sql6 = "select * from t_zz_sccc t where t.pid=?";
        List<Map<String, Object>> ccMap = DatabaseHandlerDao.getInstance().queryForMaps(sql6, new String[]{pid});
        for (int i=0; i<ccMap.size(); i++){
            PlantTask plantTask = new PlantTask();
            String id = (String) ccMap.get(i).get("ID");
            sql6 = "select t.TRPMC, t.TRPLX from t_zz_sccstrp t where t.pid=?";
            List<Map<String, Object>> cctrpMap = DatabaseHandlerDao.getInstance().queryForMaps(sql6, new String[]{id});
            if (!StringUtil.isEmpty(cctrpMap)) {
                for (int j = 0; j < cctrpMap.size(); j++) {
                    String trptymbh = (String) cctrpMap.get(j).get("TRPMC");
                    String trplx = (String) cctrpMap.get(j).get("TRPLX");
                    if (trplx.equals("ZZ") || trplx.equals("zhongmiao")) {
                        Map<String, Object> tymMap = searchTrpByTypbh(trptymbh, trplx);
                        plantTask.setInputs((String) tymMap.get("ZZZMMC"));
                    } else if (trplx.equals("NJJ")) {
                        Map<String, Object> tymMap = searchTrpByTypbh(trptymbh, trplx);
                        plantTask.setInputs((String) tymMap.get("NJJMC"));
                    } else {
                        plantTask.setInputs((String) cctrpMap.get(j).get("TRPMC"));
                    }
                }
            }
            String pch = (String) dataMap.get("ZZPCH");
            plantTask.setBegin_date(dateToLong((String) dataMap.get("QSNSXSJ"),String.valueOf(ccMap.get(i).get("NSXJGSJ"))));
            plantTask.setComp_code((String) dataMap.get("QYBM"));
            plantTask.setId((String)ccMap.get(i).get("ID"));
            plantTask.setTask_no((String) ccMap.get(i).get("NSZYXBH"));
            plantTask.setTask_name((String) ccMap.get(i).get("NSZYXMC"));
            plantTask.setPlant_batch_no(pch);
            plantTask.setPlant_id(pid);
            tasks.add(plantTask);
        }
        //其他
        String sql7 = "select * from t_zz_scqt t where t.pid=?";
        List<Map<String, Object>> qtMap = DatabaseHandlerDao.getInstance().queryForMaps(sql7, new String[]{pid});
        for (int i=0; i<qtMap.size(); i++){
            PlantTask plantTask = new PlantTask();
            String id = (String) qtMap.get(i).get("ID");
            sql7 = "select t.TRPMC, t.TRPLX from t_zz_scqttrp t where t.pid=?";
            List<Map<String, Object>> qttrpMap = DatabaseHandlerDao.getInstance().queryForMaps(sql7, new String[]{id});
            if (!StringUtil.isEmpty(qttrpMap)) {
                for (int j = 0; j < qttrpMap.size(); j++) {
                    String trptymbh = (String) qttrpMap.get(j).get("TRPMC");
                    String trplx = (String) qttrpMap.get(j).get("TRPLX");
                    if (trplx.equals("ZZ") || trplx.equals("zhongmiao")) {
                        Map<String, Object> tymMap = searchTrpByTypbh(trptymbh, trplx);
                        plantTask.setInputs((String) tymMap.get("ZZZMMC"));
                    } else if (trplx.equals("NJJ")) {
                        Map<String, Object> tymMap = searchTrpByTypbh(trptymbh, trplx);
                        plantTask.setInputs((String) tymMap.get("NJJMC"));
                    } else {
                        plantTask.setInputs((String) qttrpMap.get(j).get("TRPMC"));
                    }
                }
            }
            String pch = (String) dataMap.get("ZZPCH");
            plantTask.setBegin_date(dateToLong((String) dataMap.get("QSNSXSJ"),String.valueOf(qtMap.get(i).get("NSXJGSJ"))));
            plantTask.setComp_code((String) dataMap.get("QYBM"));
            plantTask.setId((String)qtMap.get(i).get("ID"));
            plantTask.setTask_no((String) qtMap.get(i).get("NSZYXBH"));
            plantTask.setTask_name((String) qtMap.get(i).get("NSZYXMC"));
            plantTask.setPlant_batch_no(pch);
            plantTask.setPlant_id(pid);
            tasks.add(plantTask);
        }

        return tasks;
    }
    public Map<String,Object> searchTrpByTypbh(String trpbh, String trplx) {
        String companyCode = SerialNumberUtil.getInstance().getCompanyCode();
        String sql = "";
        if (trplx.equals("ZZ")||trplx.equals("zhongmiao")){
            sql =  "select t.zzzmmc from t_sdzyc_zzzm t where t.zzzmbh=? and qybm=?";
        }else if (trplx.equals("NJJ")){
            sql = "select t.njjmc from t_sdzyc_njjxx t where t.njjbh=? and qybm=?";
        }
        Map<String, Object> tymMap = DatabaseHandlerDao.getInstance().queryForMap(sql,new String[]{trpbh,companyCode});
        return tymMap;
    }

    private Long dateToLong(String str,String addDate){
        SimpleDateFormat dd = new SimpleDateFormat("yyyy-MM-dd");
        Date date;
        long t = 0;
        try{
            date =  dd.parse(str);
            if(StringUtil.isNotEmpty(addDate)){
                Calendar cal = Calendar.getInstance();
                cal.setTime(date);
                cal.add(Calendar.DATE,Integer.parseInt(addDate));
                return cal.getTime().getTime();
            }
            t = date.getTime();
        }catch(ParseException e){
            e.printStackTrace();
        }
        return t;
    }

    public Map<String,Object> searchById(String id) {
        String sql =  "select * from t_sdzyc_csglxx where id=?"+defaultCode();
        return DatabaseHandlerDao.getInstance().queryForMap(sql,new String[]{id});
    }

    public int updateTpsc(String id) {
        String sql =  "update t_sdzyc_csglxx set tpsc='' where id=?"+defaultCode();
        return DatabaseHandlerDao.getInstance().executeSql(sql,new String[]{id});
    }

    public  String defaultCode(){
        String code= SerialNumberUtil.getInstance().getCompanyCode();
        String  defaultCode=" ";
        if(code!=null && !"".equals(code) )
            defaultCode= AppDefineUtil.RELATION_AND+" QYBM= '"+code+"' "+ AppDefineUtil.RELATION_AND+" is_delete <> '1'";
        return defaultCode;
    }
    public void delete(String cspch,String id){

        String Updatesql = "update t_zz_scda t set zt='1' where zzpch=?";
        DatabaseHandlerDao.getInstance().executeSql(Updatesql, new Object[]{cspch});
        String sql = " delete from t_sdzyc_csglxx where id  = '" + id + "'";
        DatabaseHandlerDao.getInstance().executeSql(sql);

    }
    public Map<String,Object> queryJyCspch(String pch){
        String sql =  "select * from t_sdzyc_csglxx where cspch='"+pch+"'";
        return getGridTypeData(DatabaseHandlerDao.getInstance().queryForMaps(sql));
    }
    public Map<String,Object> queryZzpch(String pch){
        String sql =  "select * from t_sdzyc_csglxx where zzpch='"+pch+"'";
        return getGridTypeData(DatabaseHandlerDao.getInstance().queryForMaps(sql));
    }
    public Map<String,Object> getGridTypeData(List<Map<String,Object>> list){
        Map<String,Object> dataMap = new HashMap<String, Object>();
        dataMap.put("data",list);
        return  dataMap;
    }
    public void sendDelPlantService(String id){
        EnterpriseService service = new RearClientBuilder().build(EnterpriseService.class);
        service.deletePlant(id);
    }
}