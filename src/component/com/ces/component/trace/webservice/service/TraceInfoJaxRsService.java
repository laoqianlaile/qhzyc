package com.ces.component.trace.webservice.service;

import com.ces.component.trace.service.TraceInfoService;
import com.ces.component.trace.utils.ResponseUtil;
import com.ces.config.dhtmlx.dao.common.DatabaseHandlerDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.*;

/**
 * Created by Synge on 2015/9/29.
 */

@Component
@Path("/traceInfo")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class TraceInfoJaxRsService {

    @Autowired
    private TraceInfoService traceInfoService;

    /**
     * 根据追溯码获取散货/包装产品基本信息
     *
     * @return
     */
    @GET
    @Path("/getProductInfo")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getProductInfo(@QueryParam("zsm") String zsm) {
        Map<String, Object> dataMap = traceInfoService.getProductInfo(zsm);
        return ResponseUtil.generateResponse(dataMap);
    }


    /**
     * 获取物联网信息
     *
     * @param cspch 采收批次号
     * @return
     */
    @GET
    @Path("/getWlwxx")
    public Response getWlwxx(@QueryParam("cspch") String cspch) {
        Map<String, Object> datas = traceInfoService.getWlwxx(cspch);
        return ResponseUtil.generateResponse(datas);
    }

    /**
     * 根据散货或产品配料中的采收批次号获得企业相关信息
     *
     * @param cspch
     * @return
     */
    @GET
    @Path("/searchCompInfo")
    @Produces(MediaType.APPLICATION_JSON)
    public Response searchCompInfo(@QueryParam("cspch") String cspch) {
        Map<String, Object> dataMap = traceInfoService.searchCompInfo(cspch);
        return ResponseUtil.generateResponse(dataMap);
    }


    /**
     * 根据散货或产品配料中的采收批次号获得企业相关信息
     *
     * @param qybm
     * @return
     */
    @GET
    @Path("/searchRzxx")
    @Produces(MediaType.APPLICATION_JSON)
    public Response searchRzxx(@QueryParam("qybm") String qybm) {
        List<Map<String, Object>> dataMap = traceInfoService.searchRzxx(qybm);
        return ResponseUtil.generateResponse(dataMap);
    }


    /**
     * 根据散货或产品配料中的采收批次号获得作业相关信息
     *
     * @param cspch
     * @return
     */
    @GET
    @Path("/searchZyxx")
    @Produces(MediaType.APPLICATION_JSON)
    public Response searchZyxx(@QueryParam("cspch") String cspch, @QueryParam("zsm") String zsm) {
        Map<String, Object> dataMap = traceInfoService.searchZyxx(cspch, zsm);
        return ResponseUtil.generateResponse(dataMap);
    }

    /**
     * 根据散货或产品配料中的采收批次号获得销售门店相关信息
     *
     * @param cspch
     * @return
     */
    @GET
    @Path("/searchXsmdxx")
    @Produces(MediaType.APPLICATION_JSON)
    public Response searchXsmdxx(@QueryParam("cspch") String cspch) {
        List<Map<String, Object>> dataMap = traceInfoService.searchXsmdxx(cspch);
        return ResponseUtil.generateResponse(dataMap);
    }

    /**
     * 获取所有的门店信息
     *
     * @param cspch
     * @return
     */
    @GET
    @Path("/getMdxx")
    public Response getMdxx(@QueryParam("cspch") String cspch) {
        String sql = "select t.qybm from T_ZZ_CSNZWXQ t where t.pch = ?";
        Object qybm = DatabaseHandlerDao.getInstance().queryForObject(sql, new Object[]{cspch});
        sql = "select * from T_ZZ_KHMDXX t where t.qybm = ? and t.is_delete = '0'";
        List<Map<String, Object>> maps = DatabaseHandlerDao.getInstance().queryForMaps(sql, new Object[]{qybm});
        return ResponseUtil.generateResponse(maps);
    }

    /**
     * 根据采收批次号和追溯码获得需要计算的得分数据
     *
     * @param cspch
     * @param zsm
     * @return
     */
    @GET
    @Path("/xxwzdpfData")
    public Response xxwzdpfData(@QueryParam("cspch") String cspch, @QueryParam("zsm") String zsm, @QueryParam("qybm") String qybm) {
        //企业相关认证信息
        List<Map<String, Object>> rzxxMap = traceInfoService.searchRzxx(qybm);
        //物联网信息
        Map<String, Object> wlwxxMap = traceInfoService.getWlwxx(cspch);
        int score = 0;
        //获取企业认证数据得分
        if (rzxxMap != null && !rzxxMap.isEmpty()) {
            score = score + 1;
            for (Map<String, Object> map : rzxxMap) {
                if (!"".equals(map.get("TP"))) {
                    score = score + 1;
                    break;
                }
            }
        }
        //物联网信息
        int wlwxxScore = 0;
        if (wlwxxMap != null && !wlwxxMap.isEmpty()) {
            Set<String> keys = wlwxxMap.keySet();
            for(String key : keys){
                Object data= wlwxxMap.get(key);
                if(wlwxxScore == 12){
                    break;
                }
                if(data !="" )
                    wlwxxScore ++ ;

            }
        }
        //作业信息
        int zyxxScore = 0 ;
        Map<String, Object> zyxxMap = traceInfoService.searchZyxx(cspch, zsm);
        if (zyxxMap != null && !zyxxMap.isEmpty()) {
            Set<String> keys = zyxxMap.keySet();
            for(String key : keys){//循环遍历获得每个作业项目
                if(null==zyxxMap.get(key)||zyxxMap.get(key).equals("")||key.equals("lcxx")||key.equals("jgbz")){
                    continue;
                }
                List<Map<String, Object>> dataList= (List<Map<String, Object>>)zyxxMap.get(key);
                if(dataList == null || dataList.isEmpty()){
                    continue;
                }
                for (Map<String, Object> dataMap:dataList) {
                    Set<String> xxKeys = dataMap.keySet();
                    for(String xxkey : xxKeys){//根据作业项目判断信息的完整度进行分值计算
                        Object data= dataMap.get(xxkey);
                        if(zyxxScore == 33){//分值最多33
                            break;
                        }
                        if(data !="" )
                            zyxxScore ++ ;
                    }
                }


            }
        }
        score = score + wlwxxScore + zyxxScore;
        return ResponseUtil.generateResponse(score);
    }

    /**
     * 保存留言
     * @param lxfs
     * @param yjhjy
     * @param cpmyd
     * @return
     */
    @GET
    @Path("/saveSuggestion")
    public Response saveSuggestion(@QueryParam("lxfs") String lxfs,@QueryParam("yjhjy") String yjhjy,@QueryParam("cpmyd") String cpmyd){
        try {
            traceInfoService.saveSuggestion(lxfs,yjhjy,cpmyd);
            return ResponseUtil.generateResponse("success");
        } catch (Exception e) {
            return ResponseUtil.generateResponse("error");
        }
    }

    /**
     * 正向追溯树
     * @param zzyzpch 种植养殖批次号
     * @return
     */
    @GET
    @Path("/getFwordTraceChain")
    public Response getFwordTraceChain(@QueryParam("zzyzpch") String zzyzpch){
        Map map  = traceInfoService.getTree(zzyzpch);
        return ResponseUtil.generateResponse(map);
    }

    /**
     * 正向追溯树
     * @param zzyzpch 种植养殖批次号
     * @return
     */
    @GET
    @Path("/getEndNode")
    public Response getEndNode(@QueryParam("zzyzpch") String zzyzpch){
        List<Map<String,Object>> res = traceInfoService.getEndNode(zzyzpch);
        return ResponseUtil.generateResponse(res);
    }

    /*********************unity全景展示**********************/
    @GET
    @Path("/getUnitityTraceInfo")
    public Response getUnitityTraceInfo(@QueryParam("cspch") String cspch,@QueryParam("zsm") String zsm){
        Map<String,Object> map = traceInfoService.getUnitityTraceInfo(cspch,zsm);
        return ResponseUtil.generateResponse(map);
    }
    /*********************unity全景展示**********************/
}
