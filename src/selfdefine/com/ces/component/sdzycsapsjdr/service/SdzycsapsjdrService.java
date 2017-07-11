package com.ces.component.sdzycsapsjdr.service;
import cesgroup.rear.lib.client.enterprise.RearClientBuilder;
import com.ces.component.trace.utils.*;
import com.ces.config.dhtmlx.dao.common.DatabaseHandlerDao;
import com.ces.config.utils.AppDefineUtil;
import com.ces.config.utils.StringUtil;

import enterprise.endpoints.EnterpriseService;
import enterprise.entity.*;
import org.apache.commons.io.FileUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.struts2.ServletActionContext;
import org.springframework.stereotype.Component;

import com.ces.component.sdzycsapsjdr.dao.SdzycsapsjdrDao;
import com.ces.component.trace.service.base.TraceShowModuleDefineDaoService;
import com.ces.xarch.core.entity.StringIDEntity;
import org.springframework.transaction.annotation.Transactional;

import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Component
public class SdzycsapsjdrService extends TraceShowModuleDefineDaoService<StringIDEntity, SdzycsapsjdrDao> {
    private final String REAL_PATH = ServletActionContext.getServletContext().getRealPath("/spzstpfj");
    public static String QYBM = SerialNumberUtil.getInstance().getCompanyCode();

    @Transactional
    public Map<String, String> saveData(Map<String, String> paramMap, File uploadFile, String uploadFileFileName) {



        Map<String, String> dataMap = DataTypeConvertUtil.getInstance().mapObj2mapStrFile(paramMap);

        String base64 = String.valueOf(dataMap.get("YPTP"));
        String imageSuffix = String.valueOf(dataMap.get("IMGSUFFIX"));
        dataMap.remove("YPTP");
        dataMap.remove("IMGSUFFIX");

        List<Map<String, String>> excelListMap = null;
        try {
            excelListMap = ImportXls(uploadFile, uploadFileFileName);
        } catch (IOException e) {
            e.printStackTrace();
        }
        String id = dataMap.get("ID");

        if (StringUtil.isEmpty(id)) {
            String companyCode = SerialNumberUtil.getInstance().getCompanyCode();
            dataMap.put("QYBM", companyCode);
            dataMap.put("WJMC", uploadFileFileName);
            dataMap.put("SCSJ", DataTypeConvertUtil.getInstance().DataConvert("yyyy-MM-dd HH:mm:ss", new Date()));
            String filename = ImageCompressUtil.getInstance().encordMd5(base64)+"."+imageSuffix;
            File file = new File(REAL_PATH+"/"+filename);
            if(!file.exists()){//文件上传重复，不再进行文件上传操作
                try {
                    Base64FileUtil.decoderBase64File(base64, REAL_PATH + "/" + filename);//base64文件上传
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            dataMap.put("YPTP",filename);

            String sappch = dataMap.get("PCH");
            Map<String,Object> acMap = searchacxml(sappch);
            String acid = (String) acMap.get("ID");
            String spmc = String.valueOf(acMap.get("SPMC"));
            String spdm = String.valueOf(acMap.get("SPDM"));
            String jgsj = String.valueOf(acMap.get("MADEDATE"));

            //通过爱创id查询电子监管二维码pid获得zsm
            List<Map<String, Object>> zsm = searchDzjgewmZsm(acid);



            /**
             * 原药材入库
             */
            List<Map<String, String>> ylsc = new ArrayList<Map<String, String>>();
            String batch_no = null;
            String wbatch_no = null;
            for (int i = 0; i < excelListMap.size(); i++) {
                Map<String, String> excelMap = excelListMap.get(i);
                String gyspch = excelMap.get("GYSPC");
                //查询原药材入库 有则下面就不执行，没有则执行(有说明是自产，无则外购)
                Map<String, Object> yycrk = searchyycrk(gyspch);
                if (yycrk == null || yycrk.isEmpty()) {
                    Map<String, String> map = new HashMap<String, String>();
                    Map<String, Object> jyMap = searchycjyxxxx(gyspch);//通过供应商批次号查询出药材交易详细表
                    if (jyMap != null && !jyMap.isEmpty()) {
                        //更新原药材入库表
                        String qyjgpch = String.valueOf(gyspch.substring(gyspch.length() - 11, gyspch.length()));
                        map.put("QYBM",QYBM);
                        map.put("PCH",String.valueOf(jyMap.get("PCH")));
                        map.put("QYPCH", qyjgpch);
                        map.put("CGDH", String.valueOf(jyMap.get("XSSDH")));//
                        map.put("YCMC", String.valueOf(jyMap.get("YCMC")));//
                        map.put("CD", String.valueOf(jyMap.get("CDMC")));//
                        map.put("RKZL", String.valueOf(jyMap.get("JYZL")));
                        map.put("YCDM", String.valueOf(jyMap.get("YCDM")));
                        map.put("YPTZSM", String.valueOf(jyMap.get("YPTZSM")));
                        map.put("YCDM", String.valueOf(jyMap.get("YCDM")));
                        map.put("CSPCH", String.valueOf(jyMap.get("CSPCH")));
                        map.put("QYPCH", String.valueOf(jyMap.get("QYPCH")));
                        map.put("RKSJ", new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
                        String sql = "update T_SDZYC_cjg_YCJYXXXX set SFRK=1 where QYPCH=?";
                        DatabaseHandlerDao.getInstance().executeSql(sql, new String[]{gyspch});
                        batch_no = String.valueOf(jyMap.get("PCH"));
                    } else {
                        String newpch = "W" + StatisticalCodeUtil.getInstance().getTwentyFivePcm("JJG", "SDZYC", "SDZYCJJGWGPCH");
                        map.put("QYBM",QYBM);
                        map.put("PCH", newpch);
                        map.put("YCMC","丹参");
                        map.put("QYPCH",newpch);
                        map.put("YCDM",spdm);
                        map.put("RKSJ", new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
                        wbatch_no = newpch;
                    }
                    saveOne("T_SDZYC_JJG_YYCRKXX", map);
                    ylsc.add(map);
                    sendCreateTradeInService(map);
                } else {
                    saveOne("T_SDZYC_JJG_YYCRKXX", DataTypeConvertUtil.getInstance().mapObj2mapStr(yycrk));
                    ylsc.add(DataTypeConvertUtil.getInstance().mapObj2mapStr(yycrk));
                    sendCreateTradeInService(DataTypeConvertUtil.getInstance().mapObj2mapStr(yycrk));
                }
            }
            if(StringUtil.isEmpty(batch_no)){
                batch_no = wbatch_no;
            }
            /**
             * 生产领料
             */
            String lldh = null;
            if (ylsc != null && !ylsc.isEmpty()) {
                Map<String, String> master = new HashMap<String, String>();
                master.put("QYBM", QYBM);
                lldh = SerialNumberUtil.getInstance().getSerialNumber("SDZYC", "SDZYCJJGSCLLDH", true);//领料单号
                master.put("LLDH", lldh);
                for (int i = 0; i < ylsc.size(); i++) {
                    Map<String, String> detail = new HashMap<String, String>();
                    detail.put("LLDH", lldh);
                    detail.put("QYBM", QYBM);
                    detail.put("LLMC", String.valueOf(ylsc.get(i).get("YCMC")));
                    String rkzl = ylsc.get(i).get("RKZL");
                    if ( rkzl!=null){
                        detail.put("LLZZL", String.valueOf(rkzl));
                    }
                    detail.put("PCH", String.valueOf(ylsc.get(i).get("PCH")));
                    detail.put("FLCK", String.valueOf(ylsc.get(i).get("SLCK")));
                    detail.put("YCDM", String.valueOf(ylsc.get(i).get("YCDM")));
                    detail.put("CSPCH", String.valueOf(ylsc.get(i).get("CSPCH")));
                    detail.put("QYPCH", String.valueOf(ylsc.get(i).get("QYPCH")));
                    saveOne("T_SDZYC_JJG_SCLLXXXX", detail);
                }
                saveOne("T_SDZYC_JJG_SCLLXX", master);
            }


            /**
             * 饮片生产信息
             */
            String scpch = null;
            Map<String, String> scMaster = new HashMap<String, String>();
            if (ylsc != null && !ylsc.isEmpty()) {
                scMaster.put("QYBM", QYBM);
                scpch = StatisticalCodeUtil.getInstance().getTwentyFivePcm("JJG", "SDZYC", "SDZYCJJGYPSCPCH");//生产批次号
                scMaster.put("SCPCH", scpch);
                scMaster.put("YYCPCH", batch_no);
                scMaster.put("LLDH",lldh);
                scMaster.put("YPMC","丹参");
                scMaster.put("JGZZL", String.valueOf(20));
                scMaster.put("SCRQ",jgsj);
                String qyscpch = String.valueOf(scpch.substring(scpch.length() - 11, scpch.length()));
                scMaster.put("QYSCPCH",qyscpch);
               // Map<String,Object> scllMap = searchscllxx();
                //master.put("QYPCH",)
                saveOne("T_SDZYC_JJG_YPSCXX", scMaster);


                List<PrecisionLlEntity> pls = new ArrayList<PrecisionLlEntity>();
                for (int i = 0; i < ylsc.size(); i++) {
                    Map<String, String> detail = new HashMap<String, String>();
                    detail.put("LLDH", lldh);
                    detail.put("YYCPCH", String.valueOf(ylsc.get(i).get("PCH")));
                    detail.put("YCMC", String.valueOf(ylsc.get(i).get("YCMC")));
                    String rkzl = ylsc.get(i).get("RKZL");
                    if (rkzl!=null){
                        detail.put("JGZZL",String.valueOf(rkzl));
                    }

                    detail.put("YCDM", String.valueOf(ylsc.get(i).get("YCDM")));
                    detail.put("CSPCH", String.valueOf(ylsc.get(i).get("CSPCH")));
                    detail.put("QYPCH", String.valueOf(ylsc.get(i).get("QYPCH")));
                    detail.put("PID", String.valueOf(scMaster.get("ID")));
                    saveOne("T_SDZYC_JJG_YPSCTL", detail);

                    detail.put("process_id",scMaster.get("ID"));
                    detail.put("SCPCH",scpch);
                    detail.put("LLDH",scMaster.get("LLDH"));
                    detail.put("LLMC",scMaster.get("YPMC"));
                    detail.put("CSPCH",scMaster.get("CSPCH"));
                    detail.put("YYCPCH",scMaster.get("YYCPCH"));
                    detail.put("LLZZL","0");
                    PrecisionLlEntity pl = sendCreatePrecisionLlEntityService(detail);
                    pls.add(pl);
                }
                sendCreatePrecisionService(scMaster,pls);

            }

            /**
             * 饮片包装信息
             */
            String bzpch = null;
            if (ylsc != null && !ylsc.isEmpty()) {
                Map<String, Object> bzMaster = new HashMap<String, Object>();

                bzMaster.put("ID", "");
                bzMaster.put("QYBM", QYBM);
                bzMaster.put("YPMC",spmc);
                bzMaster.put("YCDM",spdm);
                bzMaster.put("BZTP",filename);

                bzpch = StatisticalCodeUtil.getInstance().getTwentyFivePcm("JJG", "SDZYC", "SDZYCJJGBZPCH");//包装批次号
                bzMaster.put("BZPCH", bzpch);
                String qybzpch = String.valueOf(bzpch.substring(bzpch.length() - 11, bzpch.length()));
                bzMaster.put("QYBZPCH",qybzpch);

                scpch = StatisticalCodeUtil.getInstance().getTwentyFivePcm("JJG", "SDZYC", "SDZYCJJGYPSCPCH");//生产批次号
                String qypch = String.valueOf(scpch.substring(scpch.length() - 11, scpch.length()));
                bzMaster.put("SCPCH", scpch);
                bzMaster.put("QYPCH", qypch);
                bzMaster.put("BZSJ",jgsj);
                String bzId = saveOne("T_SDZYC_JJG_YPBZXX", DataTypeConvertUtil.getInstance().mapObj2mapStr(bzMaster));
                bzMaster.put("ID",bzId);
                String processId = String.valueOf(scMaster.get("ID"));
                bzMaster.put("process_id",processId==null?"":processId.toString());
                bzMaster.put("base64",base64);
                bzMaster.put("imageName",filename);
                sendCreatePrecisionPackService(DataTypeConvertUtil.getInstance().mapObj2mapStr(bzMaster));

                List<TraceEntity> traceEntities = traceEntityInfo(zsm, bzMaster,scMaster);
                sendCreatePreLoadService(traceEntities);
            }

            id = save("t_sdzyc_sapsj", dataMap, null);
            dataMap.put("base64",base64);
            dataMap.put("imageName",filename);

        }else {
            id = save("t_sdzyc_sapsj", dataMap, null);
        }
            dataMap.put(AppDefineUtil.C_ID, id);
            return dataMap;
        }

    /**
     * 原药材入库
     */
    public Map<String,Object> searchyycrk(String pch){
        String sql = "select * from T_SDZYC_JJG_YYCRKXX where QYPCH=? and qybm = ?";
        return DatabaseHandlerDao.getInstance().queryForMap(sql,new String[]{pch,SerialNumberUtil.getInstance().getCompanyCode()});
    }

    /**
     * 查询爱创*/
    public Map<String,Object> searchacxml(String pch){
        String sql = "select * from T_SDZYC_ACXML where BATCHNO=?";
        return DatabaseHandlerDao.getInstance().queryForMap(sql,new String[]{pch});
        }
    /**
     *默认权限过滤
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
     * 获取药材交易信息批次
     */
    public Map<String,Object> searchycjyxxxx(String pch){
        String sql = "select * from T_SDZYC_CJG_YCJYXXXX where QYPCH=? AND qybm = ?";
        return DatabaseHandlerDao.getInstance().queryForMap(sql,new String[]{pch,SerialNumberUtil.getInstance().getCompanyCode()});
    }

    public Map<String,Object> getGridTypeData(List<Map<String,Object>> list){
        Map<String,Object> dataMap = new HashMap<String, Object>();
        dataMap.put("data", list);
        return  dataMap;
    }

    /**
     * 通过id 查找饮片包装信息
     * @param id
     * @return
     */
    public Map<String,Object> searchById(String id) {
        String sql =  "select * from T_SDZYC_SAPSJ where id=? and is_delete <> '1'";
        return DatabaseHandlerDao.getInstance().queryForMap(sql,new String[]{id});
    }

    /**
     * 上传饮片图片
     * @param id
     * @return
     */
    public int updateBzwj(String id) {
        String sql =  "update T_SDZYC_SAPSJ set YPTP ='' where id=? and is_delete <> '1'";
        return DatabaseHandlerDao.getInstance().executeSql(sql,new String[]{id});
    }

    /**
     * 通过爱创id查询电子监管二维码zsm
     */
    public List<Map<String, Object>> searchDzjgewmZsm(String pid) {
        String sql = "select t.zsm from T_SDZYC_DZJGMEWM t where t.pid=?";
        return DatabaseHandlerDao.getInstance().queryForMaps(sql, new String[]{pid});
    }

    //原料入库 饮片加工 包装  药材交易

    /**
     * 同步原药材入库信息
     */
    private void sendCreateTradeInService(Map<String, String> map){
        EnterpriseService service = new RearClientBuilder().build(EnterpriseService.class);
        TradeInEntity inEntity = new TradeInEntity();
        inEntity.setId(map.get("ID"));
        inEntity.setBatch_no(map.get("PCH"));
        inEntity.setHerb_code(map.get("YCDM"));
        inEntity.setHerb_name(map.get("YCMC"));
        inEntity.setOrigin(map.get("CD"));
        String rkzl = map.get("RKZL");
        if(rkzl!=null){
            inEntity.setWeight(Float.parseFloat(rkzl));
        }
        String rksj = map.get("RKSJ");
        if (rksj!=null){
            inEntity.setDate(dateToLong(rksj));
        }
        inEntity.setTest_link(map.get("JYXXLJ"));
        inEntity.setPerson_in_charge(map.get("FZR"));
        inEntity.setComp_code(map.get("QYBM"));
        inEntity.setComp_type("3");
        inEntity.setChannel(map.get("LYZT"));
        inEntity.setHerb_name_detail(map.get("YCMC"));
        service.createTradeIn(inEntity);
    }

    /**
     * 同步饮片生产信息
     */
    public void sendCreatePrecisionService(Map<String, String> map, List<PrecisionLlEntity> pls){
        EnterpriseService service = new RearClientBuilder().build(EnterpriseService.class);
        PrecisionEntity pe = new PrecisionEntity();
        pe.setId(map.get("ID"));
        pe.setComp_code(map.get("QYBM"));
        pe.setManufact_plan_no( map.get("SCFABH"));
        pe.setManufact_batch_no( map.get("SCPCH"));
        pe.setBatch_no(map.get("YYCPCH"));
        pe.setHerb_name(map.get("YPMC"));
        String scrq = map.get("SCRQ");
        if(scrq!=null){
            pe.setDate(dateToLong(scrq ));
        }
        String jgzzl = map.get("JGZZL");
        if(jgzzl!=null){
            pe.setWeight(Float.parseFloat(String.valueOf(jgzzl)));
        }
        pe.setStandard(map.get("ZXBZ"));
        pe.setCraft_man(map.get("GYY"));
        pe.setManager(map.get("SCJL"));
        pe.setImage(map.get("XCTP"));
        pe.setBase64(map.get("base64"));
        pe.setImage( map.get("imageName"));
        pe.setPrecisionlls(pls);
        service.createPrecision(pe);
    }

    /**
     * 同步饮片生产子表
     * @param detail
     */
    private PrecisionLlEntity sendCreatePrecisionLlEntityService(Map<String, String> detail) {
        PrecisionLlEntity pl = new PrecisionLlEntity();
        pl.setId(detail.get("ID"));
        pl.setProcess_id(detail.get("process_id"));
        pl.setScpch(detail.get("SCPCH"));
        pl.setLlzzl(detail.get("LLZZL"));
        pl.setLldh(detail.get("LLDH"));
        pl.setLlmc(detail.get("LLMC"));
        pl.setYlpch(detail.get("YYCPCH"));
        pl.setCspch(detail.get("CSPCH"));
        pl.setQypch(detail.get("QYPCH"));
        return pl;
    }

    /**
     * 同步饮片包装信息
     */
    public void sendCreatePrecisionPackService(Map<String, String> map ){
        EnterpriseService service = new RearClientBuilder().build(EnterpriseService.class);
        PrecisionPack pe = new PrecisionPack();
        pe.setId(map.get("ID"));
        pe.setPrecision_batch_no(map.get("SCPCH"));
        pe.setProduct_code(map.get("YCDM"));
        pe.setProduct_name(map.get("YPMC"));
        pe.setSpec(map.get("BZGG"));
        pe.setBox_no(map.get("XM"));
        pe.setBox_weight(map.get("BZZL"));
        pe.setPack_weight(map.get("BZGG"));
        pe.setDate(map.get("BZSJ"));
        pe.setProcess_id(map.get("process_id"));
        pe.setBase64(map.get("base64"));
        pe.setImage(map.get("imageName"));
        service.createPrecisionPack(pe);
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

    public void sendCreatePreLoadService(List<TraceEntity> traceEntities){
        EnterpriseService service = new RearClientBuilder().build(EnterpriseService.class);
        service.preLoad(traceEntities);
    }

    /**
     * 同步追溯码信息
     * @param zsm
     * @param bzMap
     * @param scMaster
     * @return
     */
    public List<TraceEntity> traceEntityInfo(List<Map<String, Object>> zsm, Map<String, Object> bzMap, Map<String, String> scMaster){
        List<TraceEntity> listtraceEntity =  new ArrayList<TraceEntity>();
        for (int i = 0;i<zsm.size();i++) {
            TraceEntity traceEntity = new TraceEntity();
           // String qypch = String.valueOf(bzMap.get("QYPCH"));

           // double weight =0;
            String ylpch = "";
            double oldWeight = Double.parseDouble(String.valueOf(scMaster.get("JGZZL")));
            if(oldWeight > 0 ){
                    ylpch = String.valueOf(scMaster.get("YYCPCH"));
            }

            //根据包装信息查询对应使用的原料批次号
            Map<String, Object> rkMap = new HashMap<String, Object>();
            if(StringUtil.isNotEmpty(ylpch)) {
                String sql = "select t.ID,t.yptzsm from t_sdzyc_cjg_ycjyxxxx t where t.pch=?";
                rkMap = DatabaseHandlerDao.getInstance().queryForMap(sql, new String[]{ylpch});
            }
            if (ylpch.length()<26){
                traceEntity.setAtom(ylpch);
            }
            traceEntity.setId((String) bzMap.get("ID"));
            traceEntity.setComp_code((String)bzMap.get("QYBM"));
            traceEntity.setIn_trace_code((String) rkMap.get("YPTZSM"));
            traceEntity.setOut_trace_code(String.valueOf(zsm.get(i).get("ZSM")));
            traceEntity.setProcess_id(scMaster.get("PID"));
            traceEntity.setComp_type("3");
            traceEntity.setPack_id((String)bzMap.get("ID"));
            traceEntity.setProcess_id(scMaster.get("ID"));
            listtraceEntity.add(traceEntity);
        }
        return listtraceEntity;

    }


    /**
     * 读取excel文件数据
     * @param uploadFile
     * @param uploadFileFileName
     * @return
     * @throws IOException
     */
    @Transactional
    public List<Map<String, String>> ImportXls(File uploadFile, String uploadFileFileName) throws IOException{
        String fileNameWithStamp = "";
        File destFile = null;
        if (uploadFile != null) {
            fileNameWithStamp = System.currentTimeMillis() + "_" + uploadFileFileName;
             destFile = new File(REAL_PATH + "/" + fileNameWithStamp);
            try {
                FileUtils.copyFile(uploadFile, destFile);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        Workbook book ;
        try {
            // Excel 2003获取方法
            book = new HSSFWorkbook(new FileInputStream(REAL_PATH + "/" + fileNameWithStamp));
        } catch (Exception ex) {
            // Excel 2007获取方法
            book = new XSSFWorkbook(new FileInputStream(REAL_PATH + "/" + fileNameWithStamp));
        }

        Sheet sheet = book.getSheetAt(0);
        // 定义 row、cell
        Row row;
        row = sheet.getRow(0);
        if (!"选择".equals(String.valueOf(row.getCell(0))) ||
                !"成品编码".equals(String.valueOf(row.getCell(1))) ||
                !"成品名称".equals(String.valueOf(row.getCell(2))) ||
                !"成品批次".equals(String.valueOf(row.getCell(3))) ||
                !"订单".equals(String.valueOf(row.getCell(4))) ||
                !"生产日期".equals(String.valueOf(row.getCell(5))) ||
                !"到期日".equals(String.valueOf(row.getCell(6))) ||
                !"BOM一级物料".equals(String.valueOf(row.getCell(7))) ||
                !"物料描述".equals(String.valueOf(row.getCell(8)))||
                !"一级批次".equals(String.valueOf(row.getCell(9)))||
                !"一级订单".equals(String.valueOf(row.getCell(10)))||
                !"BOM二级物料".equals(String.valueOf(row.getCell(11))) ||
                !"物料描述".equals(String.valueOf(row.getCell(12)))||
                !"二级物料批次".equals(String.valueOf(row.getCell(13)))||
                !"二级物料订单".equals(String.valueOf(row.getCell(14)))||
                !"BOM三级物料(丹参)".equals(String.valueOf(row.getCell(15)))||
                !"三级物料批次".equals(String.valueOf(row.getCell(16)))||
                !"工厂".equals(String.valueOf(row.getCell(17)))||
                !"供应商".equals(String.valueOf(row.getCell(18)))||
                !"供应商批次".equals(String.valueOf(row.getCell(19)))||
                !"制造商".equals(String.valueOf(row.getCell(20)))||
                !"是否自种".equals(String.valueOf(row.getCell(21)))){
                return null;
        }
        List<Map<String, String>> dataList = new ArrayList<Map<String, String>>();
        List<String> nameList = new ArrayList<String>();
        nameList.add("XZ");
        nameList.add("CPBM");
        nameList.add("CPMC");
        nameList.add("CPPC");
        nameList.add("DD");
        nameList.add("SCRQ");
        nameList.add("DQR");
        nameList.add("BOMYJWL");
        nameList.add("WLMS");
        nameList.add("YJPC");
        nameList.add("YJDD");
        nameList.add("BOMEJWL");
        nameList.add("EJWLMS");
        nameList.add("EJWLPC");
        nameList.add("EJWLDD");
        nameList.add("BOMSJWL");
        nameList.add("SJWLPC");
        nameList.add("GC");
        nameList.add("GYS");
        nameList.add("GYSPC");
        nameList.add("ZZS");
        nameList.add("SFZZ");

        //String cell;
        // 总共有多少列,从0开始(i表示列，j表示行)
        for (int i = 0 ; i < sheet.getRow(0).getLastCellNum(); i++) {
            // 处理空列
            if (sheet.getRow(0).getCell(i) == null) {
                continue;
            }
            for (int j = 1; j <= sheet.getLastRowNum(); j++) {
                row = sheet.getRow(j);
                // 处理空列
                if (row == null || row.getCell(i) == null) {
                    continue;
                }
                if (dataList.size() < j) {
                    dataList.add(new HashMap<String, String>());
                    dataList.get(j - 1).put("QYBM",QYBM);
                    dataList.get(j - 1).put("XZ","");
                    dataList.get(j - 1).put("CPBM","");
                    dataList.get(j - 1).put("CPMC","");
                    dataList.get(j - 1).put("CPPC","");
                    dataList.get(j - 1).put("DD","");
                    dataList.get(j - 1).put("SCRQ","");
                    dataList.get(j - 1).put("DQR","");
                    dataList.get(j - 1).put("BOMYJWL","");
                    dataList.get(j - 1).put("WLMS","");
                    dataList.get(j - 1).put("YJPC","");
                    dataList.get(j - 1).put("YJDD","");
                    dataList.get(j - 1).put("BOMEJWL","");
                    dataList.get(j - 1).put("EJWLMS","");
                    dataList.get(j - 1).put("EJWLPC","");
                    dataList.get(j - 1).put("EJWLDD","");
                    dataList.get(j - 1).put("BOMSJWL","");
                    dataList.get(j - 1).put("SJWLPC","");
                    dataList.get(j - 1).put("GC","");
                    dataList.get(j - 1).put("GYS","");
                    dataList.get(j - 1).put("GYSPC","");
                    dataList.get(j - 1).put("ZZS","");
                    dataList.get(j - 1).put("SFZZ","");
                }
                dataList.get(j - 1).put(nameList.get(i), String.valueOf(row.getCell(i)));
            }
        }
        return  dataList;
    }
}