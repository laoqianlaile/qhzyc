package com.ces.component.cjgylrkwgxz.service;

import cesgroup.rear.lib.client.enterprise.RearClientBuilder;
import com.ces.component.trace.utils.*;
import com.ces.config.dhtmlx.dao.common.DatabaseHandlerDao;
import com.ces.config.utils.AppDefineUtil;
import com.ces.config.utils.JsonUtil;
import com.ces.config.utils.StringUtil;
import com.fasterxml.jackson.databind.JsonNode;
import enterprise.endpoints.EnterpriseService;
import enterprise.entity.TradeInEntity;
import org.apache.struts2.ServletActionContext;
import org.springframework.stereotype.Component;

import com.ces.component.cjgylrkwgxz.dao.CjgylrkwgxzDao;
import com.ces.component.trace.service.base.TraceShowModuleDefineDaoService;
import com.ces.xarch.core.entity.StringIDEntity;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class CjgylrkwgxzService extends TraceShowModuleDefineDaoService<StringIDEntity, CjgylrkwgxzDao> {
    private final String REAL_PATH = ServletActionContext.getServletContext().getRealPath("/spzstpfj");
    /**
     *
     * @param paramMap
     * @return
     */
    public Map<String, String> saveDate(Map<String, String> paramMap) {
        Map<String, String> dataMap = DataTypeConvertUtil.getInstance().mapObj2mapStrFile(paramMap);
        String id = dataMap.get("ID");
        String base64 = String.valueOf(dataMap.get("JCXXLJ"));
        String imageSuffix = String.valueOf(dataMap.get("IMGSUFFIX"));
        dataMap.remove("JCXXLJ");
        dataMap.remove("IMGSUFFIX");
        if(StringUtil.isEmpty(id)){//鏄柊澧炴搷浣?
            String companyCode = SerialNumberUtil.getInstance().getCompanyCode();
            String wgpch = "W"+ StatisticalCodeUtil.getInstance().getTwentyFivePcm("CJG", "SDZYC", "SCZYCCJGYLWGBH");
            dataMap.put("PCH",wgpch);
            // dataMap.put("QYPCH",wgpch);
            String ylrkbh = SerialNumberUtil.getInstance().getSerialNumber("SDZYC", "SDZYCCJGYLRKBH", true);
            dataMap.put("RKBH",ylrkbh);
            dataMap.put("QYBM", companyCode);
            dataMap.put("CGLX","外购");
            String filename = ImageCompressUtil.getInstance().encordMd5(base64)+"."+imageSuffix;
            File file = new File(REAL_PATH+"/"+filename);
            if(!file.exists()){//文件上传重复，不再进行文件上传操作
                try {
                    Base64FileUtil.decoderBase64File(base64, REAL_PATH + "/" + filename);//base64文件上传
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            dataMap.put("JCXXLJ",filename);

            saveOne("T_SDZYC_CJG_YLRKXX", dataMap);
            sendCreateTradeInService(dataMap);
        }else{
            saveOne("T_SDZYC_CJG_YLRKXX", dataMap);
        }
        dataMap.put(AppDefineUtil.C_ID, id);
        //淇濆瓨鎴愬姛鍚屾澶勭悊绉嶆浼佷笟鍏ュ簱淇℃伅鏇存敼涓哄璐?
        String sql = "update T_SDZYC_CJG_YLRKXX set LYZT=1 where PCH=?";
        DatabaseHandlerDao.getInstance().executeSql(sql,new Object[]{dataMap.get("PCH")});
        return dataMap;
    }

    private void sendCreateTradeInService(Map<String, String> dataMap){
        EnterpriseService service = new RearClientBuilder().build(EnterpriseService.class);
        TradeInEntity inEntity = new TradeInEntity();
        inEntity.setId(dataMap.get("ID"));
        inEntity.setBatch_no(dataMap.get("PCH"));
        inEntity.setHerb_name(dataMap.get("YLMC"));
        if(StringUtil.isNotEmpty(dataMap.get("GYS"))){
            inEntity.setOrigin(dataMap.get("GYS"));
        }else{
            inEntity.setOrigin(dataMap.get("YLCD"));
        }
        if(dataMap.get("RKZL")!=null&&dataMap.get("RKZL")!="") {
            inEntity.setWeight(Float.parseFloat(dataMap.get("RKZL")));
        }else{
            inEntity.setWeight(0);
        }
        inEntity.setDate(dateToLong(dataMap.get("RKSJ").toString()));
        inEntity.setTest_link(dataMap.get("JYDH").toString());
        inEntity.setPerson_in_charge(dataMap.get("RKDJFZR").toString());
        inEntity.setComp_code(dataMap.get("QYBM"));
        inEntity.setComp_type("2");
        inEntity.setChannel(dataMap.get("LYZT").toString());
        inEntity.setCglx(dataMap.get("CGLX").toString());
        String sql = "select distinct t.ycxmname from t_sdzyc_zycspbm t where t.zsspm=?";
        inEntity.setHerb_name_detail(String.valueOf(DatabaseHandlerDao.getInstance().queryForMap(sql, new String[]{dataMap.get("YCDM")}).get("ycxmname")));
        service.createTradeIn(inEntity);
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
    public Map<String,Object> searchycmcComboGridData() {
        String sql = "select * from T_SDZYC_ZYCSPBM where qylx='CJG' " + defaultCode();
        return getGridTypeData(DatabaseHandlerDao.getInstance().queryForMaps(sql));
    }

    public Map<String,Object> getGridTypeData(List<Map<String,Object>> list){
        Map<String,Object> dataMap = new HashMap<String, Object>();
        dataMap.put("data",list);
        return  dataMap;
    }

    public  String defaultCode(){
        String code= SerialNumberUtil.getInstance().getCompanyCode();
        String  defaultCode=" ";
        if(code!=null && !"".equals(code) )
            defaultCode= AppDefineUtil.RELATION_AND+" QYBM= '"+code+"' "+ AppDefineUtil.RELATION_AND+" is_delete <> '1'";
        return defaultCode;
    }

    /**
     * 更新原料入库检测信息链接
     * @param id
     * @return
     */
    public int updateJcxx(String id) {
        String sql =  "update T_SDZYC_CJG_YLRKXX set jcxxlj='' where id=? and is_delete <> '1'";
        return DatabaseHandlerDao.getInstance().executeSql(sql,new String[]{id});
    }

    /**
     * 通过id 查找原料入库信息
     * @param id
     * @return
     */
    public Map<String,Object> searchById(String id) {
        String sql =  "select * from T_SDZYC_CJG_YLRKXX where id=? and is_delete <> '1'";
        return DatabaseHandlerDao.getInstance().queryForMap(sql,new String[]{id});
    }
}
