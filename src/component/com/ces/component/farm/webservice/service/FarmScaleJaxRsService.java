package com.ces.component.farm.webservice.service;

import com.ces.component.farm.service.FarmScaleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.Map;

/**
 * Created by Synge on 2015/10/19.
 */
@Component
@Path("/scale")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
public class FarmScaleJaxRsService {
    @Autowired
    private FarmScaleService farmScaleService;

    /**
     * 通过ic卡号查询生产档案
     * @param ickh
     * @return
     */
    @Path("/queryScdaByDkbh")
    @GET
    public Response queryScdaByDkbh (@QueryParam("dkbh") String dkbh) {
        Object mapList = farmScaleService.queryScdaByDkbh(dkbh);
        if (mapList==null) {
            return FarmResponseUtil.generateResponse(FarmResponseUtil.RES_ERROR, mapList);
        }
        return FarmResponseUtil.generateResponse(FarmResponseUtil.RES_OK, mapList);
    }

    /**
     * 获取采收流水号
     * @param qybm
     * @return
     */
    @Path("/getCslsh")
    @GET
    public Response getCslsh (@QueryParam("qybm") String qybm) {
        Map<String,String> cslsh = farmScaleService.getCslsh(qybm);
        return FarmResponseUtil.generateResponse(FarmResponseUtil.RES_OK, cslsh);
    }

    /**
     * 保存采收信息
     * @param qybm 企业编码
     * @param cslsh 采收流水号
     * @param qymc 区域名称
     * @param qybh 区域编号
     * @param dkbh 地块编号
     * @param dkmc 地块名称
     * @param zzdybh 种植单元编号
     * @param zzdymc 种植单元名称
     * @param scdabh 生产档案编号
     * @param pl 品类
     * @param plbh 品类编号
     * @param pz 品种
     * @param pzbh 品种编号
     * @param cssj 采收时间
     * @param zldj 质量等级
     * @param dyzs 打印张书
     * @param zl 重量
     * @param pch 批次号
     * @return
     */
    @Path("/saveCsxx")
    @POST
    public Response saveCsxx (@FormParam("qybm") String qybm,
                              @FormParam("cslsh") String cslsh,
                              @FormParam("qymc") String qymc,
                              @FormParam("qybh") String qybh,
                              @FormParam("dkbh") String dkbh,
                              @FormParam("dkmc") String dkmc,
                              @FormParam("zzdybh") String zzdybh,
                              @FormParam("zzdymc") String zzdymc,
                              @FormParam("scdabh") String scdabh,
                              @FormParam("pl") String pl,
                              @FormParam("plbh") String plbh,
                              @FormParam("pz") String pz,
                              @FormParam("pzbh") String pzbh,
                              @FormParam("cssj") String cssj,
                              @FormParam("zldj") String zldj,
                              @FormParam("dyzs") String dyzs,
                              @FormParam("zl") String zl,
                              @FormParam("pch") String pch
                              ) {
        String savaStatus = farmScaleService.saveCsxx(qybm,cslsh,qymc,qybh,dkbh,dkmc,zzdybh,zzdymc,scdabh,pl,plbh,pz,pzbh,cssj,zldj,dyzs,zl,pch);
        if (savaStatus.equals("SUCCESS")) {
            return FarmResponseUtil.generateResponse(FarmResponseUtil.RES_OK);
        } else {
            return FarmResponseUtil.generateResponse(FarmResponseUtil.RES_ERROR);
        }
    }

    /**
     * 通过追溯码获取散货小包装产品信息
     * @param cpzsm
     * @return
     */
    @Path("/getShxbzByCpzsm")
    @GET
    public Response getShxbzByCpzsm (@QueryParam("cpzsm") String cpzsm) {
        Map<String,Object> result = farmScaleService.getShxbzByCpzsm(cpzsm);
        return FarmResponseUtil.generateResponse(FarmResponseUtil.RES_OK,result);
    }
}
