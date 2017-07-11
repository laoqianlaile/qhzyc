package com.ces.component.herb.utils;

import ces.coral.lang.StringUtil;
import cesgroup.rear.lib.client.enterprise.RearClientBuilder;
import com.ces.config.dhtmlx.dao.common.DatabaseHandlerDao;
import com.ces.xarch.core.web.listener.XarchListener;
import enterprise.endpoints.EnterpriseService;
import enterprise.entity.TraceEntity;
import enterprise.entity.TradeInEntity;
import enterprise.entity.TradeOutEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 工具类
 * Created by bdz on 2015/7/7.
 */
@Component
public class HerbCommonUtil {
    public static HerbCommonUtil getInstance(){
        return XarchListener.getBean(HerbCommonUtil.class);
    }
    /**
     * 将page转换成coral4.0能用的格式
     * @param page
     * @param pageSize
     * @param pageNo
     * @return
     */
    public Map putPageToMap(Page page,String pageSize,String pageNo){
        if(page==null){
            return null;
        }
        Map map = new HashMap();
        map.put("data", page.getContent());
        map.put("total", page.getTotalElements());
        map.put("pageSize", pageSize);
        map.put("pageNumber", pageNo);
        map.put("totalPages", page.getTotalPages());
        return map;
    }
    /**
     * 分页查询
     *
     * @param pageRequest
     * @param sql
     * @return Object
     */
    public Page queryPage(PageRequest pageRequest, String sql) {
        pageRequest = new PageRequest(pageRequest.getPageNumber()-1,pageRequest.getPageSize());
        if (StringUtil.isBlank(sql)) {
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

    /**
     * 粗加工原料入库。省平台
     * @param dataMap
     */
    public void cjg_sendCreateTradeInService(Map<String, String> dataMap){
        EnterpriseService service = new RearClientBuilder().build(EnterpriseService.class);
        TradeInEntity inEntity = new TradeInEntity();
        inEntity.setId(dataMap.get("ID"));
        inEntity.setBatch_no(dataMap.get("PCH"));
        inEntity.setHerb_name(dataMap.get("YLMC"));
        inEntity.setOrigin(dataMap.get("YLCD"));
        if(dataMap.get("RKZL")!=null&&dataMap.get("RKZL")!="") {
            inEntity.setWeight(Float.parseFloat(dataMap.get("RKZL")));
        }else{
            inEntity.setWeight(0);
        }
        inEntity.setDate(dateToLong(dataMap.get("RKSJ")));
        inEntity.setTest_link("".equalsIgnoreCase(dataMap.get("JYDH"))?"":dataMap.get("JYDH"));//检验单号
        inEntity.setPerson_in_charge(dataMap.get("RKDJFZR"));
        inEntity.setComp_code(dataMap.get("QYBM"));
        inEntity.setComp_type("2");
        inEntity.setChannel(dataMap.get("LYZT"));
        String sql = "select distinct t.ycxmname from t_sdzyc_zycspbm t where t.zsspm=?";
//        inEntity.setHerb_name_detail(String.valueOf(DatabaseHandlerDao.getInstance().queryForMap(sql, new String[]{dataMap.get("YCDM")}).get("ycxmname")));
        service.createTradeIn(inEntity);
    }

    /**
     * 粗加工交易省平台
     * @param dataMap
     * @param masterMap
     * @param traceMap
     */
    public void cjg_sendCreatetTraceOutService(Map<String, String> dataMap, Map<String, String> masterMap, Map<String, Object> traceMap){
        EnterpriseService service = new RearClientBuilder().build(EnterpriseService.class);
        TradeOutEntity tradeOutEntity = new TradeOutEntity();
        tradeOutEntity.setId(dataMap.get("ID"));
        tradeOutEntity.setBatch_no(dataMap.get("PCH"));
        tradeOutEntity.setOrder_no(dataMap.get("XSDDH"));
        tradeOutEntity.setHerb_name(dataMap.get("YCMC"));
        //tradeOutEntity.setHerb_code(dataMap.get("YCDM"));
       // tradeOutEntity.setIs_tested(dataMap.get("SFJY"));
        tradeOutEntity.setWeight(Float.parseFloat(dataMap.get("JYZL")));
        tradeOutEntity.setPrice(Float.parseFloat(dataMap.get("JYDJ")));
        tradeOutEntity.setDate(dateToLong(masterMap.get("JYSJ")));
        tradeOutEntity.setBuyer(masterMap.get("CGF"));
        tradeOutEntity.setComp_code(dataMap.get("QYBM"));
        tradeOutEntity.setComp_type(dataMap.get("2"));
        tradeOutEntity.setHerb_name_detail((String) traceMap.get("ycdetail"));
        tradeOutEntity.setTraceEntities((List<TraceEntity>) traceMap.get("traceEntity"));
        service.createTradeOut(tradeOutEntity);
    }

    /**
     * 粗加工交易的数据键值对
     * @param dataMap
     * @return
     */
    public Map<String, Object> cjg_createTraceInfo(Map<String, String> dataMap){
        String sql = "select t.ID, t.YLPCH, t.JGPCH from t_sdzyc_cjg_ycjgxx t where t.jgpch=? and t.jyjg='1'";
        Map<String, Object> traceMap = DatabaseHandlerDao.getInstance().queryForMap(sql, new String[]{dataMap.get("PCH")});
        String sql1 = "select distinct t.ycxmname from t_sdzyc_zycspbm t where t.zsspm=?";
        //traceMap.put("ycdetail",DatabaseHandlerDao.getInstance().queryForMap(sql1, new String[]{dataMap.get("YCDM")}).get("YCXMNAME"));
        sql1 = "select t.ID from t_sdzyc_cjg_ylrkxx t where t.pch=?";
        Map<String, Object> rkMap = DatabaseHandlerDao.getInstance().queryForMap(sql1, new String[]{dataMap.get("CSPCH")});
        List<TraceEntity> traceEntities = new ArrayList<TraceEntity>();
        TraceEntity traceEntity = new TraceEntity();
        traceEntity.setIn_id((String) rkMap.get("ID"));
        traceEntity.setRef_id(dataMap.get("ID"));
        traceEntity.setAtom((String) traceMap.get("YLPCH"));
        traceEntity.setIn_trace_code(dataMap.get("CSPCH"));
        traceEntity.setOut_trace_code(dataMap.get("YPTZSM"));
        traceEntity.setProcess_id((String) traceMap.get("ID"));
        traceEntity.setComp_code(dataMap.get("QYBM"));
        traceEntity.setComp_type("2");
        traceEntities.add(traceEntity);
        traceMap.put("traceEntity", traceEntities);
        return traceMap;
    }

    /**
     * 精加工药材加工上传省平台数据
     * @param dataMap
     */
    public void jjg_sendCreateTradeInService(Map<String, String> dataMap){
        EnterpriseService service = new RearClientBuilder().build(EnterpriseService.class);
        TradeInEntity inEntity = new TradeInEntity();
        inEntity.setId(dataMap.get("ID"));
        inEntity.setBatch_no(dataMap.get("PCH"));
        inEntity.setHerb_name(dataMap.get("YCMC"));
        inEntity.setOrigin(dataMap.get("CD"));
        inEntity.setWeight(Float.parseFloat(dataMap.get("RKZL")));
        inEntity.setDate(dateToLong(dataMap.get("RKSJ")));
        inEntity.setTest_link(dataMap.get("JYXXLJ"));
        inEntity.setPerson_in_charge(dataMap.get("FZR"));
        inEntity.setComp_code(dataMap.get("QYBM"));
        inEntity.setComp_type("3");
        inEntity.setChannel(dataMap.get("LYZT"));
        String sql = "select distinct t.ycxmname from t_sdzyc_zycspbm t where t.zsspm=?";
       // inEntity.setHerb_name_detail(String.valueOf(DatabaseHandlerDao.getInstance().queryForMap(sql, new String[]{dataMap.get("YCDM")}).get("ycxmname")));
        service.createTradeIn(inEntity);
    }

    /**
     * 精加工饮片交易上传省平台
     * @param dataMap
     * @param masterMap
     * @param traceMap
     */
    public void jjg_sendCreatetTraceOutService(Map<String, String> dataMap, Map<String, String> masterMap, Map<String, Object> traceMap){
        EnterpriseService service = new RearClientBuilder().build(EnterpriseService.class);
        TradeOutEntity tradeOutEntity = new TradeOutEntity();
        tradeOutEntity.setId(dataMap.get("ID"));
        tradeOutEntity.setBatch_no(dataMap.get("PCH"));
        tradeOutEntity.setOrder_no(dataMap.get("XSDDH"));
        tradeOutEntity.setHerb_name(dataMap.get("YPMC"));
        tradeOutEntity.setHerb_code(dataMap.get("YCDM"));
        tradeOutEntity.setIs_tested(dataMap.get("SFJY"));
        tradeOutEntity.setWeight(Float.parseFloat(dataMap.get("JYZL")));
        tradeOutEntity.setPrice(Float.parseFloat(dataMap.get("JYDJ")));
        tradeOutEntity.setDate(dateToLong(masterMap.get("JYSJ")));
        tradeOutEntity.setBuyer(masterMap.get("CGF"));
        tradeOutEntity.setComp_code(dataMap.get("QYBM"));
        tradeOutEntity.setComp_type(dataMap.get("3"));
        tradeOutEntity.setHerb_name_detail(("ycdetail"));
        tradeOutEntity.setTraceEntities((List<TraceEntity>) traceMap.get("traceEntity"));
        service.createTradeOut(tradeOutEntity);
    }

    /**
     *精加工交易的数据键值对
     * @param dataMap
     * @return
     */
    public Map<String, Object> jjg_createTraceInfo(Map<String, String> dataMap){
        String sql = "select t.id, t.cspch, t.scpch from t_sdzyc_jjg_ypscxx t where t.scpch=?";
        Map<String, Object> traceMap = DatabaseHandlerDao.getInstance().queryForMap(sql, new String[]{dataMap.get("PCH")});
        String sql1 = "select distinct t.ycxmname from t_sdzyc_zycspbm t where t.zsspm=?";
        traceMap.put("ycdetail",DatabaseHandlerDao.getInstance().queryForMap(sql1, new String[]{dataMap.get("YCDM")}).get("YCXMNAME"));
        List<TraceEntity> traceEntities = new ArrayList<TraceEntity>();
        sql1 = "select t.yptzsm from t_sdzyc_jjg_yycrkxx t where t.cspch=? and t.cspch<>''";
        Map<String, Object> rkMap = DatabaseHandlerDao.getInstance().queryForMap(sql1, new String[]{dataMap.get("CSPCH")});
        TraceEntity traceEntity = new TraceEntity();
        traceEntity.setId((String) rkMap.get("ID"));
        traceEntity.setRef_id(dataMap.get("ID"));
        traceEntity.setAtom(dataMap.get("CSPCH"));
        traceEntity.setIn_trace_code((String) rkMap.get("YPTZSM"));
        traceEntity.setOut_trace_code(dataMap.get("YPTZSM"));
        traceEntity.setProcess_id((String) traceMap.get("ID"));
        traceEntity.setComp_code(dataMap.get("QYBM"));
        traceEntity.setComp_type("3");
        traceEntities.add(traceEntity);
        traceMap.put("traceEntity", traceEntities);
        return traceMap;
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



}
