package com.ces.component.cjgjyjcdwjsc.service;

import cesgroup.rear.lib.client.enterprise.RearClientBuilder;
import com.ces.component.cjgjyjcdwjsc.dao.CjgjyjcdwjscDao;
import com.ces.component.trace.service.base.TraceShowModuleDefineDaoService;
import com.ces.component.trace.utils.Base64FileUtil;
import com.ces.component.trace.utils.ImageCompressUtil;
import com.ces.component.trace.utils.SerialNumberUtil;
import com.ces.config.datamodel.message.MessageModel;
import com.ces.config.dhtmlx.dao.common.DatabaseHandlerDao;
import com.ces.config.utils.*;
import com.ces.xarch.core.entity.StringIDEntity;
import com.ces.xarch.core.exception.BusinessException;
import com.fasterxml.jackson.databind.JsonNode;
import enterprise.endpoints.EnterpriseService;
import enterprise.entity.TestEntity;
import enterprise.entity.TestFileEntity;
import org.apache.struts2.ServletActionContext;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Component
public class CjgjyjcdwjscService extends TraceShowModuleDefineDaoService<StringIDEntity, CjgjyjcdwjscDao> {
    private final String REAL_PATH = ServletActionContext.getServletContext().getRealPath("/spzstpfj");

    @Override
    public Object saveAll(String tableId, String entityJson, String dTableId, String dEntitiesJson, Map<String, Object> paramMap) {//
        Map<String, Object> returnData = new HashMap<String, Object>();
        boolean inserted = false;
        JsonNode entity = JsonUtil.json2node(entityJson);
        Map<String, String> dataMap = node2map(entity);
        String id = dataMap.get(AppDefineUtil.C_ID);
        if (StringUtil.isEmpty(id)) {
            inserted = true;
            id = UUIDGenerator.uuid();
            dataMap.put(AppDefineUtil.C_ID, id);
            String jybh = SerialNumberUtil.getInstance().getSerialNumber("SDZYC","SDZYCYCJYBH",false);
            dataMap.put("JYBH",jybh);
        }
        Map<String, String> relateDateMap = getRelateDateMap(tableId, dTableId, dataMap);
        String tableName = getTableName(tableId), dTableName = getTableName(dTableId);
        String sql = "select t.id,t.jgpch from t_sdzyc_cjg_ycjgxx t where t.jgpch=?";
        Map<String, Object> traceMap = DatabaseHandlerDao.getInstance().queryForMap(sql, new String[]{dataMap.get("PCH")});
        String process_id=(String) traceMap.get("ID");
        List<Map<String, String>> detailList;
        // 保存明细记录
        List<TestFileEntity> predataList = new ArrayList<TestFileEntity>();
        if(inserted){//新增进行数据同步操作

            detailList   = saveDetail(dTableName, dEntitiesJson, dataMap, relateDateMap, paramMap,predataList,process_id);
            //***********同步省平台数据开始****************//
            //同步主表信息
            sendCreateTestEntity(dataMap,predataList,process_id);
            //***********同步省平台数据结束****************//
        }else{
            detailList   = saveDetail(dTableName, dEntitiesJson, dataMap, relateDateMap, paramMap,predataList,process_id);
            sendModifyTestEnttity(id,dataMap,predataList,process_id);
        }
        // 保存明细后业务逻辑处理
        processMiddleSaveAll(tableName, dTableName, dataMap, detailList, paramMap);
        // 保存主表记录
        saveOne(tableName, dataMap, inserted);
        // 保存主表和明细后业务逻辑处理
        processAfterSaveAll(tableName, dTableName, dataMap, detailList, paramMap);
        //修改药材加工信息中的检验结果
        String updSql ="update t_sdzyc_cjg_ycjgxx set jyjg=? where  jgpch=?";
        DatabaseHandlerDao.getInstance().executeSql(updSql,new String[]{String.valueOf(dataMap.get("JYJG")),String.valueOf(dataMap.get("PCH"))});
        //
        returnData.put("master", dataMap);
        returnData.put("detail", detailList);

        return MessageModel.trueInstance(returnData);
    }


    @Override
    protected List<Map<String, String>> saveDetail(String tableName, String entitiesJson, Map<String, String> masterMap, Map<String, String> relateDateMap, Map<String, Object> paramMap) {
        List<Map<String, String>> dList = new ArrayList<Map<String, String>>();
        String id = null;
        JsonNode entities = JsonUtil.json2node(entitiesJson);
        Map<String, String> dataMap = null;
        StringBuilder sb = new StringBuilder();
        for (int i = 0, len = entities.size(); i < len; i++) {
            dataMap = node2map(entities.get(i));
            //处理页面传递过来的列表数据及base64图片数据

            String imgBase64 = dataMap.get("BGFJ");
            String fileType = null;
            String[] array = imgBase64.split(";base64,");
            String  filename = null;
            if(array.length ==1){//判断文件是否进行了修改操作
                filename = imgBase64;
                dataMap.remove("BGDX");
                dataMap.remove("BGLX");
            }else{
                String[] imageType = array[0].split("/");
                imgBase64 = array[1];
                fileType = imageType[1];
                //截取BGFJ-html数据中含有的img图片base64数据
                filename = encordMd5(imgBase64)+"."+fileType;
                dataMap.put("BGFJ",filename);
                dataMap.put("QYBM", SerialNumberUtil.getInstance().getCompanyCode());
                dataMap.remove("BGLX");
                dataMap.remove("BGDX");
            }

            dataMap.putAll(relateDateMap);
            id = dataMap.get(AppDefineUtil.C_ID);
            processBeforeSaveOneDetail(tableName, dataMap, masterMap, paramMap);
            if(StringUtil.isEmpty(id)){//是新增操作
                try {
                    File file1 = new File(imgBase64,REAL_PATH+"/"+filename);//MD5加密后的加载文件
                    if(!file1.exists()){//判断文件在服务器上面是否存在
                        Base64FileUtil.decoderBase64File(imgBase64,REAL_PATH+"/"+filename);
                    }
                    //进行图片压缩:缩略图处理 200等比压缩
                    ImageCompressUtil.getInstance().zipImageFile(REAL_PATH+"/"+filename,200,100,1f,"_smallIcon");
                } catch (Exception e) {
                    e.printStackTrace();
                }


            }
            id = saveOne(tableName, dataMap);
            sb.append(",'").append(id).append("'");
            processAfterSaveOneDetail(tableName, dataMap, masterMap, paramMap);
            dList.add(dataMap);
        }
        if (sb.length() > 0) {
            sb.deleteCharAt(0);
            deleteOverDetail(tableName, sb.toString(), relateDateMap);
        }
        return dList;
    }

    /**
     * qiucs 2015-2-28 上午11:24:51
     * <p>描述: 获取从表与主表关联关系的字段值 </p>
     * @return Map<String,String>
     */
    private Map<String, String> getRelateDateMap(String tableId, String dTableId, Map<String, String> dMap) {
        Map<String, List<String>> relationMap = TableUtil.getTableRelation(tableId, dTableId);
        if (null == relationMap || relationMap.isEmpty()) {
            throw new BusinessException("未获取到表（" + TableUtil.getTableName(tableId) + "）与表（" + TableUtil.getTableName(dTableId) + "）之间的关系字段，请检查这两张表的表关系配置！");
        }
        List<String> mList = relationMap.get(tableId);
        List<String> dList = relationMap.get(dTableId);

        Map<String, String> relateDateMap = new HashMap<String, String>();

        for (int i = 0, len = mList.size(); i < len; i++) {
            relateDateMap.put(dList.get(i), dMap.get(mList.get(i)));
        }

        return relateDateMap;
    }

    @Transactional
    protected List<Map<String, String>> saveDetail(String tableName, String entitiesJson, Map<String, String> masterMap, Map<String, String> relateDateMap, Map<String, Object> paramMap,List<TestFileEntity> dataList,
                                                   String process_id ) {
        List<Map<String, String>> dList = new ArrayList<Map<String, String>>();
        String id = null;
        JsonNode entities = JsonUtil.json2node(entitiesJson);
        Map<String, String> dataMap = null;
        StringBuilder sb = new StringBuilder();
        for (int i = 0, len = entities.size(); i < len; i++) {
            dataMap = node2map(entities.get(i));
            //处理页面传递过来的列表数据及base64图片数据

            String imgBase64 = dataMap.get("BGFJ");
            String fileType = null;
            if(imgBase64!=null){
                String[] array = imgBase64.split(";base64,");
                String[] imageType = array[0].split("/");
                imgBase64 =array[1];
                fileType = imageType[1];
            }
            //截取BGFJ-html数据中含有的img图片base64数据
            File file = new File(imgBase64);

            dataMap.put("BGDX", "0");
            if(file.exists()){
                dataMap.put("BGDX", String.valueOf(file.length()));
            }
            String filename = encordMd5(imgBase64)+"."+fileType;
            dataMap.put("BGFJ",filename);
            dataMap.put("QYBM", SerialNumberUtil.getInstance().getCompanyCode());
            dataMap.put("BGLX",fileType);
            dataMap.putAll(relateDateMap);
            id = dataMap.get(AppDefineUtil.C_ID);
            processBeforeSaveOneDetail(tableName, dataMap, masterMap, paramMap);
            if(StringUtil.isEmpty(id)){//是新增操作
                id = saveOne(tableName, dataMap);
                dataMap.put("base64",imgBase64);
                try {
                    File file1 = new File(imgBase64,REAL_PATH+"/"+filename);//MD5加密后的加载文件
                    if(!file1.exists()){//判断文件在服务器上面是否存在
                        Base64FileUtil.decoderBase64File(imgBase64,REAL_PATH+"/"+filename);
                    }
                    //进行图片压缩:缩略图处理 200等比压缩
                    ImageCompressUtil.getInstance().zipImageFile(REAL_PATH+"/"+filename,200,100,1f,"_smallIcon");
                    String sltname = encordMd5(imgBase64)+"_smallIcon."+fileType;
                    dataMap.put("SLT",sltname);
                    dataMap.put("SLTBASE64",Base64FileUtil.encodeBase64File(REAL_PATH+"/"+sltname));
                } catch (Exception e) {
                    e.printStackTrace();
                }

                TestFileEntity testfileEntity = new TestFileEntity();
                testfileEntity.setId(dataMap.get("ID"));
                testfileEntity.setTest_id(masterMap.get("ID"));
                testfileEntity.setProcess_id(process_id);
                testfileEntity.setType(dataMap.get("BGLX"));
                testfileEntity.setBgmc(dataMap.get("BGMC"));
                testfileEntity.setBglx(dataMap.get("BGLX"));
                testfileEntity.setBatch_no(masterMap.get("PCH"));
                testfileEntity.setDate((masterMap.get("JYSJ")));
                testfileEntity.setFile(dataMap.get("BGFJ"));
                testfileEntity.setBase64(dataMap.get("base64"));
                testfileEntity.setSlt(dataMap.get("SLT"));
                testfileEntity.setSltbase64(dataMap.get("SLTBASE64"));
                testfileEntity.setComp_code(dataMap.get("QYBM"));
                testfileEntity.setComp_type("2");
                dataList.add(testfileEntity);
            }else{
                id = saveOne(tableName, dataMap);
            }

            sb.append(",'").append(id).append("'");
            processAfterSaveOneDetail(tableName, dataMap, masterMap, paramMap);
            dList.add(dataMap);
        }
        if (sb.length() > 0) {
            sb.deleteCharAt(0);
            deleteOverDetail(tableName, sb.toString(), relateDateMap);
        }
        return dList;
    }

    public void sendCreateTestEntity(Map<String, String> map,List<TestFileEntity>predataList,String process_id){
        EnterpriseService service = new RearClientBuilder().build(EnterpriseService.class);
        TestEntity testEntity = testService(map,predataList,process_id);
        service.createTest(testEntity);
    }
    public void sendModifyTestEnttity(String id,Map<String, String> map,List<TestFileEntity>predataList,String process_id){
        EnterpriseService service = new RearClientBuilder().build(EnterpriseService.class);
        TestEntity testEntity = testService(map,predataList,process_id);
        service.modifyTest(id,testEntity);
    }
    private TestEntity testService(Map<String, String> map,List<TestFileEntity>predataList,String process_id)
    {
        TestEntity testEntity = new TestEntity();
        testEntity.setId(map.get("ID"));
        String pch = map.get("PCH");
        testEntity.setBatch_no(pch);
        testEntity.setComp_code(map.get("QYBM"));
        testEntity.setDate(dateToLong(map.get("JYSJ")));
        testEntity.setPerson(map.get("JYRY"));
        testEntity.setMethod(searchDictname("JYFF",map.get("CHECKMETHOD")));
        testEntity.setGrade(searchDictname("ZLJB",map.get("ZLJB")));
        Map<String, Object> scMap = new HashMap<String, Object>();
        String sql = "select JGGY from t_sdzyc_cjg_ycjgxx t where t.jgpch=? and qybm=?";
        scMap = DatabaseHandlerDao.getInstance().queryForMap(sql, new String[]{pch,SerialNumberUtil.getInstance().getCompanyCode()});
        testEntity.setCraft(((String) scMap.get("JGGY")));
        testEntity.setStorage_condition(searchDictname("CCTJ",map.get("CCTJ")));
        if (!StringUtil.isEmpty(map.get("JYFF"))) {
            testEntity.setMethod(map.get("JYFF"));
        }
        testEntity.setComp_type("2");
        testEntity.setProcess_id(process_id);
        testEntity.setTestFiles(predataList);
        return testEntity;
    }

    public String searchDictname(String code,String cvalue){
        Map<String, Object> scMap = new HashMap<String, Object>();
        String sql = "select NAME from T_XTPZ_CODE where code_type_code =? AND VALUE =?";
        scMap = DatabaseHandlerDao.getInstance().queryForMap(sql, new String[]{code,cvalue});
        String name = (String)scMap.get("NAME");
        return name;
    }
    private Long dateToLong(String str){
        SimpleDateFormat dd = new SimpleDateFormat("yyyy-MM-dd");
        Date date;
        long t = 0;
        try{
            date =  dd.parse(str);
            t = date.getTime();
        }catch(ParseException e){
            e.printStackTrace();
        }
        return t;
    }
    /**
     * md5文件加密处理作为文件名称
     * @param s
     * @return
     */
    public String encordMd5(String s){
        char hexDigits[]={'0','1','2','3','4','5','6','7','8','9','A','B','C','D','E','F'};
        try {
            MessageDigest mdInst = MessageDigest.getInstance("MD5");
            byte[] btInput = s.getBytes();
            mdInst.update(btInput);
            // 获得密文
            byte[] md = mdInst.digest();
            // 把密文转换成十六进制的字符串形式
            int j = md.length;
            char str[] = new char[j * 2];
            int k = 0;
            for (int i = 0; i < j; i++) {
                byte byte0 = md[i];
                str[k++] = hexDigits[byte0 >>> 4 & 0xf];
                str[k++] = hexDigits[byte0 & 0xf];
            }
            return new String(str);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }
}
