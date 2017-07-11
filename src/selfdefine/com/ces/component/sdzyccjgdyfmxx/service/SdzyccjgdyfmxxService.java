package com.ces.component.sdzyccjgdyfmxx.service;

import cesgroup.rear.lib.client.enterprise.RearClientBuilder;
import com.ces.component.trace.utils.DataTypeConvertUtil;
import com.ces.component.trace.utils.SerialNumberUtil;
import com.ces.component.trace.utils.StatisticalCodeUtil;
import com.ces.config.dhtmlx.dao.common.DatabaseHandlerDao;
import com.ces.config.utils.AppDefineUtil;
import com.ces.config.utils.ComponentFileUtil;
import com.ces.config.utils.JsonUtil;
import com.ces.config.utils.StringUtil;
import com.fasterxml.jackson.databind.JsonNode;
import enterprise.endpoints.EnterpriseService;
import enterprise.entity.ImprecisionEntity;
import enterprise.entity.ImprecisionPack;
import enterprise.entity.TestEntity;
import enterprise.entity.TraceEntity;
import org.apache.struts2.ServletActionContext;
import org.springframework.stereotype.Component;

import com.ces.component.sdzyccjgdyfmxx.dao.SdzyccjgdyfmxxDao;
import com.ces.component.trace.service.base.TraceShowModuleDefineDaoService;
import com.ces.xarch.core.entity.StringIDEntity;
import sun.misc.BASE64Encoder;

import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;

@Component
public class SdzyccjgdyfmxxService extends TraceShowModuleDefineDaoService<StringIDEntity, SdzyccjgdyfmxxDao> {
    private final String REAL_PATH = ServletActionContext.getServletContext().getRealPath("/zsm");

    public void sendCreateImprecisionPackService(Map<String, String> map){
        EnterpriseService service = new RearClientBuilder().build(EnterpriseService.class);
        ImprecisionPack pe = imprecisionPackService(map);
        service.createImprecisionPack(pe);
    }
    public void sendModifyImprecisionPackService(String id,Map<String, String> map){
        EnterpriseService service = new RearClientBuilder().build(EnterpriseService.class);
        ImprecisionPack pe = imprecisionPackService(map);
        service.modifyImprecisionPack(id,pe);
    }
    public void sendDelImprecisionPackService(String id){
        EnterpriseService service = new RearClientBuilder().build(EnterpriseService.class);
        service.deleteImprecisionPack(id);
    }
    private ImprecisionPack imprecisionPackService (Map<String, String> map)
    {
        ImprecisionPack pe = new ImprecisionPack();
        pe.setId(map.get("ID"));
        String scpch = map.get("SCPCH").toString();
        pe.setPrecision_batch_no(scpch);
        pe.setProduct_code(map.get("YCDM").toString());
        pe.setProduct_name(map.get("YCMC").toString());
        pe.setSpec(map.get("BZGG").toString());
        // pe.setBox_weight(map.get("MXZL").toString());
        pe.setPack_weight(map.get("BZGG").toString());
        pe.setDate(map.get("BZSJ").toString());
        String sql = "select t.id from t_sdzyc_cjg_ycjgxx t where t.jgpch=?";
        pe.setProcess_id(String.valueOf(DatabaseHandlerDao.getInstance().queryForMap(sql, new String[]{scpch}).get("ID")));
        pe.setBase64(map.get("base64"));
        pe.setImage(map.get("BZTP").toString());
        pe.setComp_code(map.get("QYBM").toString());
        return pe;
    }
    public void sendCreatePreLoadService(List<TraceEntity> traceEntities){
        EnterpriseService service = new RearClientBuilder().build(EnterpriseService.class);
        service.preLoad(traceEntities);
    }

    public Map<String, Object> herbProcessInfo(Map<String, String> dataMap){
        String scpch = dataMap.get("SCPCH");
        String sql = "select * from t_sdzyc_cjg_ycjgxx t where t.jgpch=?";
        Map<String, Object> cjgMap = DatabaseHandlerDao.getInstance().queryForMap(sql, new Object[]{scpch});
        String REAL_PATH = ServletActionContext.getServletContext().getRealPath("/spzstpfj")+"/"+cjgMap.get("XCTP");
        try {
            cjgMap.put("base64", encodeBase64File(REAL_PATH));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return cjgMap;
    }

    public List<TraceEntity> traceInfo(String zsm, Map<String, Object> cjgMap, Map<String, String> bzMap){
        List<TraceEntity> traceEntities = new ArrayList<TraceEntity>();
        TraceEntity traceEntity = new TraceEntity();
        String ylpch = String.valueOf(cjgMap.get("YLPCH"));
        String sql = "select t.id from t_sdzyc_cjg_ylrkxx t where t.pch=?";
        if (ylpch.length()<26){
            traceEntity.setAtom((String) cjgMap.get("YLPCH"));
        }
        traceEntity.setIn_id(String.valueOf(DatabaseHandlerDao.getInstance().queryForMap(sql, new String[]{ylpch}).get("ID")));
        traceEntity.setComp_code((String) cjgMap.get("QYBM"));
        traceEntity.setIn_trace_code((String) cjgMap.get("YLPCH"));
        traceEntity.setOut_trace_code(zsm);
        traceEntity.setProcess_id((String) cjgMap.get("ID"));
        traceEntity.setComp_type("2");
        traceEntity.setPack_id(bzMap.get("ID").toString());
        traceEntities.add(traceEntity);
        return traceEntities;
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
            File file = new File(path);
            if(file.isFile()){
                in = new FileInputStream(path);
                data = new byte[in.available()];
                in.read(data);
                in.close();
                //对字节数组Base64编码
                BASE64Encoder encoder = new BASE64Encoder();
                return encoder.encode(data);//返回Base64编码过的字节数组字符串
            }

        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
         return null;

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
     * 根据Id查询粗加工包装赋码信息
     * @param tableName
     * @param id
     * @return
     */
    public Map<String ,Object> getBzxx(String tableName,String id ){
        String sql = "select * from "+ tableName +" where id =?";
        return DatabaseHandlerDao.getInstance().queryForMap(sql,new String[]{id});
    }

    public void saveBzzsm(String bzpch,String zsm,Map<String,String> dataMap){
        String sql = "insert into T_SDZYC_ZSM(id,ZSM,BZPCH) values(sys_guid(),?,?)";
        DatabaseHandlerDao.getInstance().executeSql(sql,new String[]{zsm,bzpch});
        //sendCreatePreLoadService(traceInfo(zsm, herbProcessInfo(dataMap),dataMap));
    }
    public void deleteOldZsm(String bzpch){
        String delSql = "delete from T_SDZYC_ZSM where bzpch=?";
        DatabaseHandlerDao.getInstance().executeSql(delSql,new String[]{bzpch});
    }
    public void writText(String bzpch,String zsm,boolean need) throws IOException {
        String path = REAL_PATH+"/"+bzpch+".txt";
        String propertyPath = ComponentFileUtil.getConfigPath() +"/trace/zsm.properties";
        File file = new File(path);
            if(!file.exists())
                file.createNewFile();
            FileOutputStream fos = new FileOutputStream(file,need);
            fos.write((DataTypeConvertUtil.getInstance().readTxtProp("zsmUrl",propertyPath)+zsm+"\r\n").getBytes());

    }
    public List<Map<String,Object>> inquirebzpch(String bzpch){
       String  sql="select * from t_sdzyc_zsm t where t.bzpch=?";
        return DatabaseHandlerDao.getInstance().queryForMaps(sql, new String[]{bzpch});
    }

    /**
     * 通过id查询粗加工打印赋码信息
     * @param id
     * @return
     */
    public Map<String,Object> searchById(String id) {
        String sql =  "select * from t_sdzyc_cjg_dyfmxx where id=? and is_delete <> '1'";
        return DatabaseHandlerDao.getInstance().queryForMap(sql,new String[]{id});
    }

    /**
     * 添加筛选条件:企业编码 和删除判定
     * @return
     */
    public  String defaultCode(){
        String code= SerialNumberUtil.getInstance().getCompanyCode();
        String  defaultCode=" ";
        if(code!=null && !"".equals(code) )
            defaultCode= AppDefineUtil.RELATION_AND+" QYBM= '"+code+"' "+ AppDefineUtil.RELATION_AND+" is_delete <> '1'";
        return defaultCode;
    }

    /**
     * 更新包装文件
     * @param id
     * @return
     */
    public int updateBzwj(String id) {
        String sql =  "update t_sdzyc_cjg_dyfmxx set bztp='' where id=? and is_delete <> '1'";
        return DatabaseHandlerDao.getInstance().executeSql(sql,new String[]{id});
    }
    public TraceEntity traceEntityInfo(String zsm , Map<String,Object> bzMap){
        TraceEntity traceEntity = new TraceEntity();
        String scpch = String.valueOf(bzMap.get("SCPCH"));
        Map<String, Object> cjgMap = new HashMap<String, Object>();
        String cjgsql = "select t.id from t_sdzyc_cjg_ycjgxx t where t.jgpch=?";
        cjgMap = DatabaseHandlerDao.getInstance().queryForMap(cjgsql, new String[]{scpch});

        String ylpch = String.valueOf(cjgMap.get("YLPCH"));
        String sql = "select t.id from t_sdzyc_cjg_ylrkxx t where t.pch=?";
        if (ylpch.length()<26){
            traceEntity.setAtom((String) cjgMap.get("YLPCH"));
        }
        traceEntity.setIn_id(String.valueOf(DatabaseHandlerDao.getInstance().queryForMap(sql, new String[]{ylpch}).get("ID")));
        traceEntity.setComp_code(SerialNumberUtil.getInstance().getCompanyCode());
        traceEntity.setIn_trace_code((String) cjgMap.get("YLPCH"));
        traceEntity.setOut_trace_code(zsm);
        traceEntity.setProcess_id((String) cjgMap.get("ID"));
        traceEntity.setComp_type("2");
        traceEntity.setPack_id(bzMap.get("ID").toString());
        return traceEntity;
    }
    public void delete(String bzpch,String id){
        String sql = " delete from t_sdzyc_cjg_dyfmxx where id  = '" + id + "'";
        DatabaseHandlerDao.getInstance().executeSql(sql);
        String zsmsql = " delete from t_sdzyc_zsm where bzpch  = '" + bzpch + "'";
        DatabaseHandlerDao.getInstance().executeSql(zsmsql);
    }
}
