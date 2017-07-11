package com.ces.component.syxtzjb.service;

import cesgroup.rear.lib.client.enterprise.RearClientBuilder;
import com.ces.component.trace.utils.DataTypeConvertUtil;
import com.ces.component.trace.utils.EjDatabaseUtil;
import com.ces.component.trace.utils.SerialNumberUtil;
import com.ces.component.trace.utils.StatisticalCodeUtil;
import com.ces.config.dhtmlx.dao.common.DatabaseHandlerDao;
import com.ces.config.utils.StringUtil;
import enterprise.endpoints.EnterpriseService;
import enterprise.entity.*;
import org.springframework.stereotype.Component;

import com.ces.component.syxtzjb.dao.SyxtzjbDao;
import com.ces.component.trace.service.base.TraceShowModuleDefineDaoService;
import com.ces.xarch.core.entity.StringIDEntity;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Component
public class SyxtzjbService extends TraceShowModuleDefineDaoService<StringIDEntity, SyxtzjbDao> {
    @Transactional
    public Map<String, String>  ejscx() {
        /**
         * 原药材入库
         */
        Map<String, String> dataMap = new HashMap<String, String>();
        String QYBM = "000000824";
        List<Map<String, String>> ylsc = new ArrayList<Map<String, String>>();
        String batch_no = null;
        String wbatch_no = null;
        String sqlsc = "select  distinct sBatchNo,SapNo  from tbRecsLink  where isTrace=0 ";
        List<Map<String, Object>> listejsc = EjDatabaseUtil.queryForList(sqlsc);
        if (listejsc != null && listejsc.size() > 0) {

            for (Map<String, Object> ejmapsc : listejsc) {
                String sql = " select  top 1 *  from tbRecsLink  where isTrace=0  and sBatchNo ='" + ejmapsc.get("sBatchNo").toString() + "' and SapNo ='" + ejmapsc.get("SapNo").toString() + "' ";

                List<Map<String, Object>> listej = EjDatabaseUtil.queryForList(sql);
                if (listej != null && listej.size() > 0) {

                    for (Map<String, Object> ejmap : listej) {
                        String iRecId = ejmap.get("iRecId").toString();//记录ID
                        String sPrtCode = ejmap.get("sPrtCode").toString();//产品编码
                        Map<String, Object> pMap = searchcpmc(sPrtCode);
                        String cpmc = String.valueOf(pMap.get("PNmae"));
                        String sCode = ejmap.get("sCode").toString();// 条码	sCode
                        String sPcode = ejmap.get("sPcode").toString();// 父级条码
                        String sBatchNo = ejmap.get("sBatchNo").toString();// 成品批次号
                        String SapNo = ejmap.get("SapNo").toString();// SAP订单号
                        String dManufacturingDate = ejmap.get("dManufacturingDate").toString();// 生产日期
                        String dExpirationDate = ejmap.get("dExpirationDate").toString();// 有效期
                        String sBuyer = ejmap.get("sBuyer").toString();// 购买方或领料方
                        String iTrunk = ejmap.get("iTrunk").toString();// 是否是尾箱
                        String iFull = ejmap.get("iFull").toString();// 是否是整箱
                        String iCheck = ejmap.get("iCheck").toString();// 是否检验合格
                        String iSynStatus = ejmap.get("iSynStatus").toString();// 是否同步
                        String dSynDate = ejmap.get("dSynDate").toString();// 同步时间
                        String iPack = ejmap.get("iPack").toString();// 立体库是否同步
                        String dPacksynDate = ejmap.get("dPacksynDate").toString();// 立体库同步时间
                        String isTrace = ejmap.get("isTrace").toString();// 溯源是否同步
                        String tracesynTime = ejmap.get("tracesynTime").toString();//溯源同步时间

                        String dUpDate = ejmap.get("dUpDate").toString();// 数据上传时间
                        String dRecsLinkDate = ejmap.get("dRecsLinkDate").toString();// 数据关联时间
                        String ylrksj = new SimpleDateFormat("yyyy-MM-dd").format(new Date());

                        String rawweight = ejmap.get("rawweight").toString();// 原材料加工重量
                        String rawInTime = ejmap.get("rawInTime").toString();// 原材料入库时间	　
                        String rawSup = ejmap.get("rawSup").toString();// 原材料供应商	　
                        String rawAddr = ejmap.get("rawAddr").toString();// 原材料产地	　
                        String ramName = "阿胶";// 原材料　
                        String rawNo = "200001002";// 原材料　
                        String rawck = "原料库（驴皮库）";

                        dataMap.put("IRECID", iRecId);
                        dataMap.put("SPRTCODE", sPrtCode);
                        dataMap.put("PNAME", cpmc);
                        dataMap.put("SCODE", sCode);
                        dataMap.put("SPCODE", sPcode);
                        dataMap.put("SBATCHNO", sBatchNo);
                        dataMap.put("SAPNO", SapNo);
                        dataMap.put("DMANUFACTURINGDATE", dManufacturingDate);
                        dataMap.put("DEXPIRATIONDATE", dExpirationDate);
                        dataMap.put("SBUYER", sBuyer);
                        dataMap.put("ITRUNK", iTrunk);
                        dataMap.put("IFULL", iFull);
                        dataMap.put("ICHECK", iCheck);
                        dataMap.put("ISYNSTATUS", iSynStatus);
                        dataMap.put("DSYNDATE", dSynDate);
                        dataMap.put("IPACK", iPack);
                        dataMap.put("DPACKSYNDATE", dPacksynDate);
                        dataMap.put("ISTRACE", isTrace);
                        dataMap.put("DUPDATE", dUpDate);
                        dataMap.put("DRECSLINKDATE", dRecsLinkDate);
                        dataMap.put("YLRKSJ", ylrksj);
                        dataMap.put("RAWWEIGHT", rawweight);
                        dataMap.put("RAWINTIME", rawInTime);
                        dataMap.put("RAWADDR", rawAddr);
                        dataMap.put("RAWADDR", rawAddr);

                        Map<String, String> map = new HashMap<String, String>();
                        String newpch = "W" + StatisticalCodeUtil.getInstance().getTwentyFivePcm("JJG", "SDZYC", "SDZYCJJGWGPCH");
                        map.put("QYBM", QYBM);
                        map.put("PCH", newpch);
                        map.put("YCMC", ramName);
                        map.put("QYPCH", newpch);
                        map.put("YCDM", rawNo);
                        map.put("RKSJ", ylrksj);
                        map.put("RKZL", rawweight);
                        wbatch_no = newpch;

                        saveOne("T_SDZYC_JJG_YYCRKXX", map);
                        ylsc.add(map);
                        sendCreateTradeInService(map);


                        if (StringUtil.isEmpty(batch_no)) {
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
                                if (rkzl != null) {
                                    detail.put("LLZZL", String.valueOf(rkzl));
                                }
                                detail.put("PCH", String.valueOf(ylsc.get(i).get("PCH")));
                                detail.put("FLCK", rawck);
                                detail.put("YCDM", String.valueOf(ylsc.get(i).get("YCDM")));
                                detail.put("CSPCH", "");
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
                            scMaster.put("LLDH", lldh);
                            scMaster.put("YPMC", cpmc);
                            scMaster.put("JGZZL", rawweight);
                            scMaster.put("SCRQ", dManufacturingDate);
                            String qyscpch = String.valueOf(scpch.substring(scpch.length() - 11, scpch.length()));
                            scMaster.put("QYSCPCH", qyscpch);
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
                                if (rkzl != null) {
                                    detail.put("JGZZL", String.valueOf(rkzl));
                                }

                                detail.put("YCDM", String.valueOf(ylsc.get(i).get("YCDM")));
                                detail.put("CSPCH", String.valueOf(ylsc.get(i).get("CSPCH")));
                                detail.put("QYPCH", String.valueOf(ylsc.get(i).get("QYPCH")));
                                detail.put("PID", String.valueOf(scMaster.get("ID")));
                                saveOne("T_SDZYC_JJG_YPSCTL", detail);

                                detail.put("process_id", scMaster.get("ID"));
                                detail.put("SCPCH", scpch);
                                detail.put("LLDH", scMaster.get("LLDH"));
                                detail.put("LLMC", scMaster.get("YPMC"));
                                detail.put("CSPCH", scMaster.get("CSPCH"));
                                detail.put("YYCPCH", scMaster.get("YYCPCH"));
                                detail.put("LLZZL", rawweight);
                                PrecisionLlEntity pl = sendCreatePrecisionLlEntityService(detail);
                                pls.add(pl);
                            }
                            sendCreatePrecisionService(scMaster, pls);

                        }
                        /**
                         * 饮片包装信息
                         */
                        List<Map<String, Object>> zsm = searcgyZsm(sBatchNo,SapNo);
                        String bzpch = null;
                        if (ylsc != null && !ylsc.isEmpty()) {
                            Map<String, Object> bzMaster = new HashMap<String, Object>();

                            bzMaster.put("ID", "");
                            bzMaster.put("QYBM", QYBM);
                            bzMaster.put("YPMC", cpmc);
                            bzMaster.put("YCDM", sPrtCode);
                            // bzMaster.put("BZTP", filename);

                            bzpch = StatisticalCodeUtil.getInstance().getTwentyFivePcm("JJG", "SDZYC", "SDZYCJJGBZPCH");//包装批次号
                            bzMaster.put("BZPCH", bzpch);
                            String qybzpch = String.valueOf(bzpch.substring(bzpch.length() - 11, bzpch.length()));
                            bzMaster.put("QYBZPCH", qybzpch);

                            scpch = StatisticalCodeUtil.getInstance().getTwentyFivePcm("JJG", "SDZYC", "SDZYCJJGYPSCPCH");//生产批次号
                            String qypch = String.valueOf(scpch.substring(scpch.length() - 11, scpch.length()));
                            bzMaster.put("SCPCH", scpch);
                            bzMaster.put("QYPCH", qypch);
                            bzMaster.put("BZSJ", dManufacturingDate);
                            String bzId = saveOne("T_SDZYC_JJG_YPBZXX", DataTypeConvertUtil.getInstance().mapObj2mapStr(bzMaster));
                            bzMaster.put("ID", bzId);
                            String processId = String.valueOf(scMaster.get("ID"));
                            bzMaster.put("process_id", processId == null ? "" : processId.toString());

                            sendCreatePrecisionPackService(DataTypeConvertUtil.getInstance().mapObj2mapStr(bzMaster));

                            List<TraceEntity> traceEntities = traceEntityInfo(zsm, bzMaster, scMaster);
                            sendCreatePreLoadService(traceEntities);

                        }
                        String usql = "update tbRecsLink set isTrace=1,tracesynTime=substr(sysdate,0,19) where sBatchNo ='"+sBatchNo+"' and SapNo ='" +SapNo + "'" ;
                        EjDatabaseUtil.executeSql(usql);
                    }
                }
            }
        }
        return dataMap;
    }
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
            if(com.ces.config.utils.StringUtil.isNotEmpty(ylpch)) {
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
    public Map<String,Object> searchcpmc(String code){
        String sql = "select PNmae,Pspec from [tb_Product] where PCode=?";
        return DatabaseHandlerDao.getInstance().queryForMap(sql,new String[]{code});
    }
    /**
     * 通过爱创id查询电子监管二维码zsm
     */
    public List<Map<String, Object>> searcgyZsm(String sBatchNo,String SapNo) {
        String sql = "select  sCode as zsm from tbRecsLink t where t.isTrace=0 and t.sPcode is null and t.sBatchNo =? and t.SapNo =?";
        return DatabaseHandlerDao.getInstance().queryForMaps(sql, new String[]{sBatchNo,SapNo});
    }
}
