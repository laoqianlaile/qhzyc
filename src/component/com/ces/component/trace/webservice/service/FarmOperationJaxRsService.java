package com.ces.component.trace.webservice.service;

import com.ces.component.trace.service.FarmOpertionService;
import com.ces.component.trace.utils.ResponseUtil;
import com.ces.config.utils.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**食用农产品生产作业信息管理系统WebService
 * Created by bdz on 2015/8/25.
 */
@Component
@Path("/farmOperationService")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class FarmOperationJaxRsService {
    @Autowired
    private FarmOpertionService farmOpertionService;

    /**
     * 查询基地信息
     * @param qybm  企业编码
     * @return
     */
    @GET
    @Path("/queryBaseInfo")
//	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @SuppressWarnings("rawtypes")
    public Response queryBaseInfo(@QueryParam("qybm") String qybm) {
        return ResponseUtil.generateResponse(farmOpertionService.queryBaseInfo(qybm));
    }

    /**
     * 查询区域信息
     * @param qybm  企业编码
     * @param jdbh  基地编号
     * @return
     */
    @GET
    @Path("/queryAreaInfo")
//	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @SuppressWarnings("rawtypes")
    public Response queryAreaInfo(@QueryParam("qybm") String qybm,@QueryParam("jdbh") String jdbh) {
        return ResponseUtil.generateResponse(farmOpertionService.queryAreaInfo(qybm,jdbh));
    }

    /**
     * 查询地块信息
     * @param qybm  企业编码
     * @param jdbh  基地编号
     * @param qybh  区域编号
     * @return
     */
    @GET
    @Path("/queryPlotInfo")
//	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @SuppressWarnings("rawtypes")
    public Response queryPlotInfo(@QueryParam("qybm") String qybm,@QueryParam("jdbh") String jdbh,@QueryParam("qybh") String qybh) {
        return ResponseUtil.generateResponse(farmOpertionService.queryPlotInfo(qybm, jdbh, qybh));
    }
    /**
     * 查询单元信息
     * @param qybm  企业编码
     * @param jdbh  基地编号
     * @param qybh  区域编号
     * @param dkbh  地块编号编号
     * @return
     */
    @GET
    @Path("/queryUnitInfo")
//	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @SuppressWarnings("rawtypes")
    public Response queryUnitInfo(@QueryParam("qybm") String qybm,@QueryParam("jdbh") String jdbh,@QueryParam("qybh") String qybh,@QueryParam("dkbh") String dkbh) {
        return ResponseUtil.generateResponse(farmOpertionService.queryUnitInfo(qybm,jdbh,qybh,dkbh));
    }

    /**
     * 查询播种信息页面
     * @param qybm  企业编码
     * @param qybh  区域编号
     * @param dkbh  地块编号
     * @param dybh  单元编号（逗号分隔）
     * @param pageNumber    分页参数
     * @param pageSize      分页参数
     * @return
     */
    @POST
    @Path("/queryBzPage")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response queryBzPage(@QueryParam("qybm") String qybm,@QueryParam("qybh") String qybh,
                                @QueryParam("dkbh") String dkbh,@QueryParam("dybh") String dybh,
                                @FormParam("P_pageNumber")String pageNumber,@FormParam("P_pagesize") String pageSize){
        PageRequest pageRequest = new PageRequest(Integer.parseInt(pageNumber)-1,Integer.parseInt(pageSize));
        String[] items={"PZ"};
        Page page = farmOpertionService.queryAllPage(pageRequest, qybm, qybh, dkbh, dybh, items, "BZ");
        return ResponseUtil.generateResponse(putPageToMap(page));
    }

    /**
     * 查询播种所有id
     * @param qybm
     * @param qybh
     * @param dkbh
     * @param dybh
     * @return
     */
    @POST
    @Path("/queryBzIds")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response queryBzIds(@QueryParam("qybm") String qybm,@QueryParam("qybh") String qybh,
                                @QueryParam("dkbh") String dkbh,@QueryParam("dybh") String dybh){
        List list = farmOpertionService.queryIds(qybm, qybh, dkbh, dybh, "BZ");
        Map map = new HashMap();
        map.put("ids",list);
        return ResponseUtil.generateResponse(map);
    }
    /**
     * 播种再做一次
     * @param id
     * @return
     */
    @POST
    @Path("/doAgainBz")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response doAgainBz(@FormParam("id") String id){
        Map<String, String> map = new HashMap<String, String>();
        try {
            String[] items={"ZPFS","YL"};
            map.put("result", "success");
            farmOpertionService.doAgain(id,items,"BZ");
            return ResponseUtil.generateResponse(map);
        } catch (Exception e) {
            e.printStackTrace();
            map.put("result", "error");
            return ResponseUtil.generateResponse(map);
        }
    }

    /**
     * 查询灌溉信息页面
     * @param qybm  企业编码
     * @param qybh  区域编号
     * @param dkbh  地块编号
     * @param dybh  单元编号（逗号分隔）
     * @param pageNumber    分页参数
     * @param pageSize      分页参数
     * @return
     */
    @POST
    @Path("/queryGgPage")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response queryGgPage(@QueryParam("qybm") String qybm,@QueryParam("qybh") String qybh,
                                @QueryParam("dkbh") String dkbh,@QueryParam("dybh") String dybh,
                                @FormParam("P_pageNumber")String pageNumber,@FormParam("P_pagesize") String pageSize){
        PageRequest pageRequest = new PageRequest(Integer.parseInt(pageNumber)-1,Integer.parseInt(pageSize));
        String[] items = {"GGFS","SYLX","SYDJ"};
        Page page = farmOpertionService.queryAllPage(pageRequest, qybm, qybh, dkbh, dybh, items, "GG");
        return ResponseUtil.generateResponse(putPageToMap(page));
    }

    /**
     * 查询灌溉所有id
     * @param qybm
     * @param qybh
     * @param dkbh
     * @param dybh
     * @return
     */
    @POST
    @Path("/queryGgIds")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response queryGgIds(@QueryParam("qybm") String qybm,@QueryParam("qybh") String qybh,
                               @QueryParam("dkbh") String dkbh,@QueryParam("dybh") String dybh){
        List list = farmOpertionService.queryIds(qybm, qybh, dkbh, dybh,"GG");
        Map map = new HashMap();
        map.put("ids",list);
        return ResponseUtil.generateResponse(map);
    }
    /**
     * 灌溉再做一次
     * @param id
     * @return
     */
    @POST
    @Path("/doAgainGg")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response doAgainGg(@FormParam("id") String id){
        Map<String, String> map = new HashMap<String, String>();
        try {
            map.put("result", "success");
            String[] items={"GGFS","SYLX","SYDJ"};
            farmOpertionService.doAgain(id,items,"GG");
            return ResponseUtil.generateResponse(map);
        } catch (Exception e) {
            e.printStackTrace();
            map.put("result", "error");
            return ResponseUtil.generateResponse(map);
        }
    }

    /**
     * 查询操作记录
     * @param pId
     * @param pageNumber
     * @param pageSize
     * @return
     */
    @POST
    @Path("/queryCzjlPage")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response queryCzjlPage(@QueryParam("pId") String pId,@FormParam("P_pageNumber")String pageNumber,
                                  @FormParam("P_pagesize") String pageSize){
        PageRequest pageRequest = new PageRequest(Integer.parseInt(pageNumber)-1,Integer.parseInt(pageSize));
        Page page = farmOpertionService.queryCzjlByPId(pId,pageRequest);
        return ResponseUtil.generateResponse(putPageToMap(page));
    }

    /**
     * 新增或更新操作记录
     * @param pId       父ID
     * @param czr       操作人
     * @param czrbh     操作人编号
     * @return
     */
    @POST
    @Path("/saveOrUpdateCzjl")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response saveOrUpdateCzjl(@FormParam("pId") String pId,@FormParam("czr") String czr,@FormParam("czrbh") String czrbh){
        try {
            Map<String, String> map = new HashMap<String, String>();
            map.put("result", "success");
            farmOpertionService.saveCzjl(pId,czrbh,czr);
            return ResponseUtil.generateResponse(map);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseUtil.generateResponse("error");
        }
    }

    /**
     * 评价
     * @param id
     * @param pj
     * @return
     */
    @POST
    @Path("/updatePj")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response updatePj(@FormParam("id") String id,@FormParam("pj") String pj){
        try {
            Map<String, String> map = new HashMap<String, String>();
            map.put("result", "success");
            farmOpertionService.updateEvaluate(pj,id);
            return ResponseUtil.generateResponse(map);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseUtil.generateResponse("error");
        }
    }

    /**
     * 将page转换为coral4.0可用格式
     * @param page
     * @return
     */
    public Map<String,String> putPageToMap(Page page){
        Map map = new HashMap<String,String>();
        map.put("data",page.getContent());
        map.put("pageNumber",page.getNumber()+1);
        map.put("pageSize",page.getSize());
        map.put("total",page.getTotalElements());
        map.put("totalPages",page.getTotalPages());
        return map;
    }


    /**
     * 通过IC卡号查询地块或区域信息
     * @param card IC卡号
     * @param qybm 企业编码
     * @return
     */
    @GET
    @Path("/queryCardInfo")
//	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @SuppressWarnings("rawtypes")
    public Response queryCardInfo(@QueryParam("card") String card,@QueryParam("qybm") String qybm) {
        return ResponseUtil.generateResponse(farmOpertionService.queryCardInfo(card,qybm));
    }


    /**
     * 查询相关农事项
     * @param lx
     * @param qybm
     * @param qybh
     * @param dkbh
     * @return
     */
    @GET
    @Path("/queryNsx")
//	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @SuppressWarnings("rawtypes")
    public Response queryNsx(@QueryParam("lx") String lx,@QueryParam("qybm") String qybm,@QueryParam("qybh") String qybh,@QueryParam("dkbh") String dkbh) {
        return ResponseUtil.generateResponse(farmOpertionService.queryNsx(lx, qybm, qybh, dkbh));
    }


    /**
     *  农事项操作刷卡
     * @param card IC卡编号
     * @param qybm 当前企业编码
     * @param ids 操作的农事项ID
     * @param lx 操作类型
     * @return
     */
    @GET
    @Path("/operation")
//	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @SuppressWarnings("rawtypes")
    public Response operation(@QueryParam("card") String card, @QueryParam("qybm") String qybm, @QueryParam("ids") String ids, @QueryParam("lx") String lx) {
        return ResponseUtil.generateResponse(farmOpertionService.operation(card, qybm, ids, lx));
    }

    /**
     *  农事项刷卡
     * @param qybm 当前企业编码
     * @param ids 操作的农事项ID
     * @param lx 操作类型
     * @return
     */
    @GET
    @Path("/queryCzjl")
//	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @SuppressWarnings("rawtypes")
    public Response queryCzjl( @QueryParam("qybm") String qybm, @QueryParam("ids") String ids, @QueryParam("lx") String lx) {
        return ResponseUtil.generateResponse(farmOpertionService.queryCzjl(ids,lx));
    }


    /**
     *  农事项刷卡
     * @param ids 操作的农事项ID
     * @param lx 操作类型
     * @return
     */
    @GET
    @Path("/doOneAgain")
//	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @SuppressWarnings("rawtypes")
    public Response doOneAgain(@QueryParam("ids") String ids, @QueryParam("lx") String lx) {
        return ResponseUtil.generateResponse(farmOpertionService.doOneAgain(ids, lx));
    }


    /**
     *  评论农事项
     * @param cz 操作的相关信息
     * @param card 操作人
     * @param qybm 企业编码
     * @return
     */
    @GET
    @Path("/commentNsx")
//	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @SuppressWarnings("rawtypes")
    public Response commentNsx(@QueryParam("cz") String cz, @QueryParam("card") String card, @QueryParam("qybm") String qybm) {
        return ResponseUtil.generateResponse(farmOpertionService.commentNsx(cz, card, qybm));
    }

    /**
     *  查询企业编码
     * @return
     */
    @GET
    @Path("/getQybm")
//	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @SuppressWarnings("rawtypes")
    public Response getQybm(@QueryParam("ickbh") String ickbh) {
        return ResponseUtil.generateResponse(farmOpertionService.getQybm(ickbh));
    }


    /****************************************touch V2.0 begin********************************************/
    /**
     * 获取服务器时间
     * @return
     */
    @GET
    @Path("/getWebTime")
    @SuppressWarnings("rawtypes")
    public Response touchIn() {
        Map<String, Object> dataMap = new HashMap<String, Object>();
        Date d = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日");
        dataMap.put("date", sdf.format(d));
        sdf = new SimpleDateFormat("HH:mm:ss");
        dataMap.put("time", sdf.format(d));
        return ResponseUtil.generateResponse(dataMap);
    }
    /**
     * 刷卡进入
     * @param card IC卡号
     * @return
     */
    @GET
    @Path("/touchIn")
    @SuppressWarnings("rawtypes")
    public Response touchIn(@QueryParam("card") String card) {
        return ResponseUtil.generateResponse(farmOpertionService.touchIn(card));
    }

    /**查询区域
     * @param operatorIcCode
     * @return
     */
    @GET
    @Path("/queryArea")
    @SuppressWarnings("rawtypes")
    public Response queryArea(@QueryParam("operatorIcCode") String operatorIcCode) {
        return ResponseUtil.generateResponse(farmOpertionService.queryArea(operatorIcCode));
    }

    /**查询地块
     * @param areaCode
     * @param operatorIcCode
     * @return
     */
    @GET
    @Path("/queryLand")
    @SuppressWarnings("rawtypes")
    public Response queryLand(@QueryParam("areaCode") String areaCode, @QueryParam("operatorIcCode") String operatorIcCode) {
        return ResponseUtil.generateResponse(farmOpertionService.queryLand(areaCode, operatorIcCode));
    }



    /**查询农事项
     * @param area  区域编码
     * @param land  地块编码
     * @param farmingType   农事项类型
     * @param company   企业编码
     * @param is_end    是否结束的农事项
     * @return
     */
    @GET
    @Path("/queryFarmingItem")
    @SuppressWarnings("rawtypes")
    public Response queryFarmingItem(@QueryParam("area") String area, @QueryParam("land") String land, @QueryParam("farmingType") String farmingType, @QueryParam("company") String company, @QueryParam("is_end") String is_end, @QueryParam("operatorIcCode") String operatorIcCode) {
        return ResponseUtil.generateResponse(farmOpertionService.queryFarmingItem(area, land, farmingType, company, is_end, operatorIcCode));
    }

    /**
     * 操作农事项
     * @param sid 农事项ID
     * @param ickbh 刷卡卡号
     * @param farmingType   农事项类型
     * @return
     */
    @GET
    @Path("/operationFarmingItem")
    @SuppressWarnings("rawtypes")
    public Response operationFarmingItem(@QueryParam("sid") String sid, @QueryParam("ickbh") String ickbh, @QueryParam("farmingType") String farmingType) {
        return ResponseUtil.generateResponse(farmOpertionService.operationFarmingItem(sid, ickbh, farmingType));
    }


    /**
     * 回退
     * @param sid
     * @param farmingType
     * @return
     */
    @GET
    @Path("/rollBack")
    @SuppressWarnings("rawtypes")
    public Response rollBack(@QueryParam("sid") String sid, @QueryParam("farmingType") String farmingType) {
        return ResponseUtil.generateResponse(farmOpertionService.rollBack(sid, farmingType));
    }

    /**
     * 操作农事项为已开始状态
     * @param sid
     * @param farmingType
     * @return
     */
    @GET
    @Path("/beginOperation")
    @SuppressWarnings("rawtypes")
    public Response beginOperation(@QueryParam("sid") String sid, @QueryParam("ickbh") String ickbh, @QueryParam("farmingType") String farmingType) {
        return ResponseUtil.generateResponse(farmOpertionService.beginOperation(sid, ickbh, farmingType));
    }


    /**
     * 操作农事项为完成状态
     * @param sid
     * @param farmingType
     * @return
     */
    @GET
    @Path("/endOperation")
    @SuppressWarnings("rawtypes")
    public Response endOperation(@QueryParam("sid") String sid, @QueryParam("ickbh") String ickbh, @QueryParam("farmingType") String farmingType) {
        return ResponseUtil.generateResponse(farmOpertionService.endOperation(sid, ickbh, farmingType));
    }

    /**
     * 新增
     * @param sid
     * @param farmingType
     * @return
     */
    @GET
    @Path("/addFarmingItem")
    @SuppressWarnings("rawtypes")
    public Response addFarmingItem(@QueryParam("sid") String sid, @QueryParam("farmingType") String farmingType) {
        return ResponseUtil.generateResponse(farmOpertionService.addFarmingItem(sid, farmingType));
    }

    /**删除农事项
     * @param sid
     * @param farmingType
     * @return
     */
    @GET
    @Path("/deleteFarmingItem")
    @SuppressWarnings("rawtypes")
    public Response deleteFarmingItem(@QueryParam("sid") String sid, @QueryParam("farmingType") String farmingType) {
        return ResponseUtil.generateResponse(farmOpertionService.deleteFarmingItem(sid, farmingType));
    }



    /****************************************touch V2.0 end********************************************/

}
