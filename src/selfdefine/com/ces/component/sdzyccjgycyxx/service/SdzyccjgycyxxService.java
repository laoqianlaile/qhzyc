package com.ces.component.sdzyccjgycyxx.service;

import cesgroup.rear.lib.client.enterprise.RearClientBuilder;
import com.ces.component.trace.utils.SerialNumberUtil;
import com.ces.component.trace.utils.StatisticalCodeUtil;
import com.ces.config.dhtmlx.dao.common.DatabaseHandlerDao;
import com.ces.config.utils.AppDefineUtil;
import com.ces.config.utils.JsonUtil;
import com.ces.config.utils.StringUtil;
import com.ces.config.utils.UUIDGenerator;
import com.fasterxml.jackson.databind.JsonNode;
import enterprise.endpoints.EnterpriseService;
import enterprise.entity.TestEntity;
import enterprise.entity.TestFileEntity;
import enterprise.entity.TradeOutEntity;
import org.apache.commons.io.FileUtils;
import org.apache.struts2.ServletActionContext;
import org.springframework.stereotype.Component;

import com.ces.component.sdzyccjgycyxx.dao.SdzyccjgycyxxDao;
import com.ces.component.trace.service.base.TraceShowModuleDefineDaoService;
import com.ces.xarch.core.entity.StringIDEntity;
import org.springframework.transaction.annotation.Transactional;
import sun.misc.BASE64Encoder;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Component
public class SdzyccjgycyxxService extends TraceShowModuleDefineDaoService<StringIDEntity, SdzyccjgycyxxDao> {
    private final String REAL_PATH = ServletActionContext.getServletContext().getRealPath("/spzstpfj");
    @Transactional
    public Object save(Map map, List<File> imageUpload, List<String> imageUploadFileName, String REAL_PATH) {
        boolean isNew = false;
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
        String id = String.valueOf(newMap.get("ID"));
        if(StringUtil.isEmpty(id)){//是新增操作
            String jybh = SerialNumberUtil.getInstance().getSerialNumber("SDZYC","SDZYCYCJYBH",false);//SerialNumberUtil.getInstance().getSerialNumber("SDZYC", "SDZYCCSPCH", false);
            String jylx="政府飞检";
            newMap.put("JYXX",jylx);
            newMap.put("JYBH", jybh);
            isNew =true;
        }
        if( null != imageUpload){
            //进行文件上传，则删除历史保存文件
            File oldFile = new File(REAL_PATH+"/"+newMap.get("T_SDZYC_CJG_YCJXX"));
            String[] args = imageUploadFileName.get(0).split("\\.");
            String fileNameWithStamp = System.currentTimeMillis()+ UUIDGenerator.uuid()+"."+args[1];
            File newFile = new File(REAL_PATH+"/"+fileNameWithStamp);
            try {
                if(oldFile.exists()){
                    oldFile.delete();
                }
                FileUtils.copyFile(imageUpload.get(0),newFile);
                //文件上传成功进行数据保存
                newMap.put("JYWJ",fileNameWithStamp);
                id = save("T_SDZYC_CJG_YCJXX" ,newMap ,null);
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
        }else{
            id = save("T_SDZYC_CJG_YCJXX" ,newMap ,null);
        }

        String updSql ="update t_sdzyc_cjg_ycjgxx set jyjg=? where  jgpch=?";
        DatabaseHandlerDao.getInstance().executeSql(updSql,new String[]{String.valueOf(newMap.get("JYJG")),String.valueOf(newMap.get("PCH"))});
        String sql = "select id from t_sdzyc_cjg_ycjgxx where jgpch = ? and qybm = ?";
        Map<String,Object> mapData = DatabaseHandlerDao.getInstance().queryForMap(sql,new String[]{String.valueOf(newMap.get("pch")),SerialNumberUtil.getInstance().getCompanyCode()});
        String processId = String.valueOf(mapData.get("ID"));
        newMap.put("process_id",processId==null?"":processId.toString());
        newMap.put("craft",(String)mapData.get("JGGY"));
        if(isNew) {
            sendCreateTestEnttity(newMap);
        }else{
            sendModifyTestEnttity(id,newMap);
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

    public void sendCreateTestEnttity(Map<String, String> map){
        EnterpriseService service = new RearClientBuilder().build(EnterpriseService.class);
        TestEntity testEntity = new TestEntity();
        testEntity.setId(map.get("ID"));
        testEntity.setBatch_no(map.get("PCH"));
        testEntity.setComp_code(map.get("QYBM"));
        testEntity.setDate(dateToLong(map.get("JYSJ")));
        testEntity.setPerson(map.get("JYRY"));
        testEntity.setComp_type("2");
        testEntity.setProcess_id(map.get("process_id"));
        testEntity.setGrade(map.get("ZLJB"));
        testEntity.setCraft(map.get("craft"));
        testEntity.setStorage_condition(map.get("CCTJ"));
        if (!StringUtil.isEmpty(map.get("JYFF"))) {
            testEntity.setMethod(map.get("JYFF"));
        }
        service.createTest(testEntity);
    }
    public void sendModifyTestEnttity(String id,Map<String, String> map){
        EnterpriseService service = new RearClientBuilder().build(EnterpriseService.class);
        TestEntity testEntity = new TestEntity();
        testEntity.setId(map.get("ID"));
        testEntity.setBatch_no(map.get("PCH"));
        testEntity.setComp_code(map.get("QYBM"));
        testEntity.setDate(dateToLong(map.get("JYSJ")));
        testEntity.setPerson(map.get("JYRY"));
        testEntity.setComp_type("2");
        testEntity.setProcess_id(map.get("process_id"));
        testEntity.setGrade(map.get("ZLJB"));
        testEntity.setCraft(map.get("craft"));
        testEntity.setStorage_condition(map.get("CCTJ"));
        if (!StringUtil.isEmpty(map.get("JYFF"))) {
            testEntity.setMethod(map.get("JYFF"));
        }
        service.modifyTest(id,testEntity);
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

    public Map<String,Object> getGridTypeData(List<Map<String,Object>> list){
        Map<String,Object> dataMap = new HashMap<String, Object>();
        dataMap.put("data",list);
        return  dataMap;
    }
    //根据多表关联查询出药材入库需要的信息
    public Map<String,Object> searchycjyxxByckbh(){
            String sql = "select y.ylpch, y.qybm, y.jgpch, y.ylmc, y.jgzzl, y.jyjg,Y.QYPCH,Y.QYJGPCH, g.ylcd,t.bzgg\n" +
                    "  from (select m.ylpch,\n" +
                    "               m.qybm,\n" +
                    "               m.jgpch,\n" +
                    "               m.ylmc,\n" +
                    "               m.lldh,\n" +
                    "               m.jgzzl,\n" +
                    "               m.jyjg,\n" +
                    "               M.QYPCH,\n" +
                    "               M.QYJGPCH,\n" +
                    "               n.pch,\n" +
                    "               m.scrq as scrq\n "+
                    "          from T_SDZYC_CJG_YCJGXX m\n " +
                    "         inner join T_SDZYC_CJG_YLLLXXXX n\n " +
                    "            on m.lldh = n.lldh and m.sfrk='0') y\n " +
                    " inner join T_SDZYC_CJG_YLRKXX g\n " +
                    "    on g.pch = y.pch and y.qybm=? " +
                    " inner join T_SDZYC_CJG_DYFMXX  t\n "+
                    "    on t.scpch=y.jgpch "+
                    "where y.jyjg='1'   order by y.scrq desc";
        List<Map<String, Object>> list = DatabaseHandlerDao.getInstance().queryForMaps(sql,new String[]{SerialNumberUtil.getInstance().getCompanyCode()});
        return getGridTypeData(list);

    }

    public Map<String,Object> searchById(String id) {
        String sql =  "select * from t_sdzyc_cjg_ycjxx where id=?"+defaultCode();
        return DatabaseHandlerDao.getInstance().queryForMap(sql,new String[]{id});
    }

    public int updateCdzm(String id) {
        String sql =  "update t_sdzyc_cjg_ycjxx set jywj='' where id=?"+defaultCode();
        return DatabaseHandlerDao.getInstance().executeSql(sql,new String[]{id});
    }

    public  String defaultCode(){
        String code= SerialNumberUtil.getInstance().getCompanyCode();
        String  defaultCode=" ";
        if(code!=null && !"".equals(code) )
            defaultCode= AppDefineUtil.RELATION_AND+" QYBM= '"+code+"' "+ AppDefineUtil.RELATION_AND+" is_delete <> '1'";
        return defaultCode;
    }
    public void sendDelTestEntity(String id){
        EnterpriseService service = new RearClientBuilder().build(EnterpriseService.class);
        service.deleteTest(id);
    }
    @Override
    public void delete(String tableId, String dTableIds, String ids, boolean isLogicalDelete, Map<String, Object> paramMap) {
        super.delete(tableId, dTableIds, ids, isLogicalDelete, paramMap);
        //在进行删除之后取出ids进行附件删除
        String filter    =  " t_sdzyc_cjg_jcbgfjb WHERE PID IN ('" + ids.replace(",", "','") + "')";
        String sql = "SELECT  BGFJ FROM "+filter;
        List<String> dataList = DatabaseHandlerDao.getInstance().queryForList(sql);
        if ( dataList!=null && dataList.size()>0){
            for (String bgfj : dataList){
                String filePath = REAL_PATH+"/"+bgfj;
                File file = new File(filePath);
                if(file.exists()){
                    file.delete();
                }
            }
        }
    }

}
