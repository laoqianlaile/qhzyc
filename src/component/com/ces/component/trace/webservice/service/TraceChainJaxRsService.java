package com.ces.component.trace.webservice.service;

import com.ces.component.cspt.entity.TCsptZsxxEntity;
import com.ces.component.cspt.service.ZhuisuliantiaoSerivce;
import com.ces.component.trace.service.TraceChainService;
import com.ces.component.trace.utils.ResponseUtil;
import com.ces.config.utils.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Path("/traceThain")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class TraceChainJaxRsService {

    @Autowired
    private ZhuisuliantiaoSerivce zhuisuliantiaoSerivce;

    @Autowired
    private TraceChainService traceChainService;

    /**
     * Webservice反向追溯链条
     *
     * @param zsm
     * @return
     */
    @GET
    @Path("/getRevTraceChain")
//	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @SuppressWarnings("rawtypes")
    public Response getRevTraceChain(@QueryParam("zsm") String zsm) {
        //获取起始节点
        Map startNode = traceChainService.queryStartNode(zsm);
        //获取加工原料追溯信息
        List<Map<String, Object>> jgList = zhuisuliantiaoSerivce.queryJgzsxx(zsm);
        //商品信息
        Map spMap = new HashMap();
        if (!startNode.isEmpty()) {//判断是否存在于追溯表中
            //查询产品信息
            spMap = traceChainService.queryByZsm(startNode, zsm);
        } else if (jgList.size() > 0) {//判断是否数据加工产品
            Map jgMap = jgList.get(0);
            //加工厂基本信息
            spMap = traceChainService.queryByZsm(jgMap, zsm);
            //原料信息
            spMap.put("YLXX", jgList);
        }
        return ResponseUtil.generateResponse(spMap);
    }

    /**
     * WebService 获取详细信息
     *
     * @param qybm  企业编码
     * @param refId 关联ID
     * @param xtlx  系统类型
     * @return 返回查询结果
     */
    @POST
    @Path("/getDetailInfo")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response getDetailInfo(@FormParam("qybm") String qybm, @FormParam("refId") String refId, @FormParam("xtlx") String xtlx) {
        TCsptZsxxEntity entity = new TCsptZsxxEntity();
        entity.setQybm(qybm);
        entity.setXtlx(xtlx);
        entity.setRefId(refId);
        Map map = zhuisuliantiaoSerivce.getZhuisuXxxx(entity);
        return ResponseUtil.generateResponse(map);
    }

    /**
     * 正向追溯
     *
     * @param zzyzpch 种植养殖批次号
     * @return
     */
    @GET
    @Path("/getFwordTraceChain")
    public Response getFwordTraceChain(@QueryParam("zzyzpch") String zzyzpch) {
        Map map = traceChainService.getTree(zzyzpch);
        return ResponseUtil.generateResponse(map);
    }

    /**
     * 将page转换成coral4.0能用的格式
     *
     * @param page
     * @param pageSize
     * @param pageNo
     * @return
     */
    public Map putPageToMap(Page page, String pageSize, String pageNo) {
        if (page == null) {
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
     * 根据当前坐标获取一定半径范围内的市场信息
     *
     * @param mlong  经度
     * @param mlat   维度
     * @param radius 距离
     * @return
     */
    @GET
    @Path("/getNearMarkets")
    public Response getNearMarkets(@QueryParam("mlong") String mlong, @QueryParam("mlat") String mlat, @QueryParam("radius") String radius) {
        if (StringUtil.isEmpty(radius) || "0".equals(radius)) {
            radius = "1000";
        }
        List<Map<String, Object>> nearMarkets = traceChainService.getNearMarkets(mlong, mlat, radius);
        for (Map<String, Object> nearMarket : nearMarkets) {
            String n1 = String.valueOf(nearMarket.get("JD"));
            String e1 = String.valueOf(nearMarket.get("WD"));
            String distance = traceChainService.distance(n1, e1, mlong, mlat);
            nearMarket.put("DISTANCE", distance);
        }
        return ResponseUtil.generateResponse(nearMarkets);
    }

    /**
     * 获取企业详细信息
     *
     * @param qybm
     * @return
     */
    @GET
    @Path("/getQyxxDetail")
    public Response getQyxxDetail(@QueryParam("qybm") String qybm) {
        Map<String, Object> data = traceChainService.getQyxxDetail(qybm);
        return ResponseUtil.generateResponse(data);
    }

    /**
     * 门店评价
     * @param mdbm
     * @param pjdj
     * @param pjnr
     * @return
     */
    @POST
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Path("/addComment")
    public Response addComment(@FormParam("mdbm") String mdbm, @FormParam("pjdj") String pjdj, @FormParam("pjnr") String pjnr) {
        try {
            traceChainService.addComment(mdbm, pjdj, pjnr);
            return ResponseUtil.generateResponse("1");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseUtil.generateResponse("0");
        }
    }

    /**
     * 查询评价内容
     * @param mdbm
     * @return
     */
    @GET
    @Path("/getComment")
    public Response getComment(@QueryParam("mdbm") String mdbm) {
        return ResponseUtil.generateResponse(traceChainService.getComment(mdbm));
    }

    @GET
    @Path("/getMdPrice")
    public Response getMdPrice(@QueryParam("mdbm") String mdbm){
        
        return null;
    }
}
