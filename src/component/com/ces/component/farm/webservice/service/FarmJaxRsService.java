package com.ces.component.farm.webservice.service;

import ces.sdk.system.exception.SystemFacadeException;
import ces.sdk.util.MD5;
import com.ces.component.farm.dto.*;
import com.ces.component.farm.service.FarmService;
import com.ces.component.farm.utils.FarmCommonUtil;
import com.ces.component.farm.utils.FarmUserUtil;
import com.ces.component.trace.utils.ResponseUtil;
import com.ces.config.utils.StringUtil;
import com.ces.config.utils.UUIDGenerator;
import com.ces.utils.TokenUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Path("/farm")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
public class FarmJaxRsService {
    @Autowired
    private FarmService searchService;

    /**
     * (1)登录
     *
     * @param username
     * @param password
     * @return
     * @throws SystemFacadeException
     */
    @Path("/login")
    @POST
    public Response login(@FormParam("username") String username, @FormParam("password") String password) throws SystemFacadeException {
        //TODO 测试用，正式环境删除
        FarmCommonUtil.loginuserMap.clear();
        //判断是否已登录
        if (FarmCommonUtil.loginuserMap.get(username) != null) {
            return FarmResponseUtil.generateResponse(FarmResponseUtil.RES_ERROR, "用户已登录");
        }

        TyyhDto dto = FarmUserUtil.loadUser(username);
        if (dto == null) {
            return FarmResponseUtil.generateResponse(FarmResponseUtil.RES_ERROR, "用户名或密码错误或账号未开通");
        }
        String md5Pass = new MD5().getMD5ofStr(password);
        if (!dto.getPassword().equalsIgnoreCase(md5Pass)) {
            return FarmResponseUtil.generateResponse(FarmResponseUtil.RES_ERROR, "用户名或密码错误");
        }
        String token = TokenUtils.getToken(username, password, 3000);
        dto.setPassword("");
        dto.setToken(token);
        FarmCommonUtil.tokenMap.put(token, String.valueOf(System.currentTimeMillis() + 24 * 60 * 60 * 1000));
        FarmCommonUtil.loginuserMap.put(username, token);
        FarmCommonUtil.loginuserMap.put(token, username);
        return FarmResponseUtil.generateResponse(FarmResponseUtil.RES_OK, dto);
    }

    /**
     * (2)注销
     *
     * @param token
     * @return
     */
    @Path("/logout")
    @POST
    public Response logout(@FormParam("Farm-Token") String token) {
        Object obj = FarmCommonUtil.tokenMap.get(token);
        if (obj == null)
            return FarmResponseUtil.generateResponse(FarmResponseUtil.RES_ERROR, "Token无效");
        FarmCommonUtil.tokenMap.remove(token);

        String username = String.valueOf(FarmCommonUtil.loginuserMap.get(token));
        FarmCommonUtil.loginuserMap.remove(token);
        FarmCommonUtil.loginuserMap.remove(username);

        return FarmResponseUtil.generateResponse(FarmResponseUtil.RES_OK, "注销成功");
    }

    /**
     * (3) 查询
     *
     * @param sqydm    市区域代码
     * @param dqqydm   地区区域代码
     * @param khmc     客户名称
     * @param qybm     企业ID
     * @param pageNo
     * @param pageSize
     * @return
     */
    @Path("/khxx")
    @POST
    public Response search(@FormParam("sqydm") String sqydm,
                           @FormParam("dqqydm") String dqqydm,
                           @FormParam("khmc") String khmc,
                           @FormParam("qybm") String qybm,
                           @FormParam("pageNo") String pageNo,
                           @FormParam("pageSize") String pageSize) {
        if (StringUtil.isEmpty(pageNo)) {
            pageNo = "1";
        }
        if (StringUtil.isEmpty(pageSize)) {
            pageSize = "10";
        }
        PageRequest pageRequest = new PageRequest(Integer.valueOf(pageNo), Integer.valueOf(pageSize));
        Page page = searchService.searchKhxx(sqydm, dqqydm, khmc, qybm, pageRequest);
        Map map = FarmCommonUtil.putPageToMap(page, pageSize, pageNo);
        return FarmResponseUtil.generateResponse(FarmResponseUtil.RES_OK, map);
    }

    /**
     * (4)出库条码查询
     *
     * @param spzsm 商品追溯码
     * @return
     */
    @Path("/cktm")
    @POST
    public Response searchCktm(@FormParam("spzsm") String spzsm,@FormParam("qybm") String qybm) {
//		Object obj = FarmCommonUtil.tokenMap.get(token);
//		if(obj == null)
//			return FarmResponseUtil.generateResponse(FarmResponseUtil.RES_ERROR, "Token无效");

        CktmDto dto = searchService.searchCktm(spzsm,qybm);
        if (dto == null)
            return FarmResponseUtil.generateResponse(FarmResponseUtil.RES_ERROR, "无对应批次！");

        return FarmResponseUtil.generateResponse(FarmResponseUtil.RES_OK, dto);
    }

//	/** 
//	 * (5)产品条码确定
//	 * @param spzsm 商品追溯码
//	 * @param khId 客户ID
//	 * @param rq 日期
//	 * @return
//	 */
//	@Path("/cptmqd")
//	@POST
//	public Response searchCptmxx(@FormParam("spzsm") String spzsm,
//			   					 @FormParam("khId") String khId,
//			   					 @FormParam("rq") String rq){
//		CptmDto dto = searchService.searchCkdxx(spzsm, khId, rq);
//		if(dto==null)
//			return FarmResponseUtil.generateResponse(FarmResponseUtil.RES_ERROR, "无查询结果");
//		return FarmResponseUtil.generateResponse(FarmResponseUtil.RES_OK, dto);
//	}


    /**
     * (6)出库信息更新
     *
     * @param cpzsm  产品追溯码
     * @param ckdId  出库单ID
     * @param cpckId 产品出库ID
     * @param cpzlId 产品种类ID
     * @param zl     重量
     * @param sl     数量
     * @param qyId   单位ID
     * @param sl     客户ID
     */
    @Path("/updateCkxx")
    @POST
    public Response updateCkxx(@FormParam("cpzsm") String cpzsm,
                               @FormParam("ckdId") String ckdId,
                               @FormParam("cpckId") String cpckId,
                               @FormParam("cpzlId") String cpzlId,
                               @FormParam("zl") String zl,
                               @FormParam("sl") String sl,
                               @FormParam("qyId") String qyId,
                               @FormParam("khId") String khId,
                               @FormParam("qybm") String qybm) {
        CptmDto dto = searchService.updateCkxx(cpzsm, ckdId, cpckId, cpzlId, zl, sl, qyId, khId, qybm);
        if (dto != null) {
            return FarmResponseUtil.generateResponse(FarmResponseUtil.RES_OK, dto);
        } else {
            return FarmResponseUtil.generateResponse(FarmResponseUtil.RES_ERROR, "参数异常");
        }
    }

    /**
     * 新增出场主表信息
     * @param khId  客户id
     * @return ckdId    出库单ID
     */
    @Path("/addCcxx")
    @POST
    public Response addCcxx(@FormParam("khId") String khId){
        if(StringUtil.isNotEmpty(khId)) {
            Map map = new HashMap();
            map.put("ckdId", searchService.insertCcxx(khId));
            return FarmResponseUtil.generateResponse(FarmResponseUtil.RES_OK, map);
        }else{
            return FarmResponseUtil.generateResponse(FarmResponseUtil.RES_ERROR, "参数为空");
        }
    }

    /**
     * (7)出库信息产品信息查询
     * @param qyId 单位ID
     * @param rq 日期
     * @param khId 客户ID
     * @param pageNo
     * @param pageSize
     * @return
     */
//	@Path("/ckcpxx")
//	@POST
//	public Response searchCkcpxx(@FormParam("qyId") String qyId,
//								@FormParam("rq") String rq,
//								@FormParam("khId") String khId,
//								@FormParam("pageNo") String pageNo,
//								@FormParam("pageSize") String pageSize){
//		PageRequest pageRequest = new PageRequest(Integer.valueOf(pageNo),Integer.valueOf(pageSize));
//		Page page = searchService.searchCkcpxx(qyId,rq,khId,pageRequest);
//		Map map = FarmCommonUtil.getInstance().putPageToMap(page, pageSize, pageNo);
//		return FarmResponseUtil.generateResponse(FarmResponseUtil.RES_OK, map);
//	}

    /**
     * (8)出库单出库产品信息查询
     *
     * @param ckdId    出库单ID
     * @param pageNo
     * @param pageSize
     * @return
     */
    @Path("/ckdcpxx")
    @POST
    public Response searchCkdcpxx(@FormParam("ckdId") String ckdId,
                                  @FormParam("pageNo") String pageNo,
                                  @FormParam("pageSize") String pageSize) {
        PageRequest pageRequest = new PageRequest(Integer.valueOf(pageNo), Integer.valueOf(pageSize));
        Page page = searchService.searchCkdckcpxx(ckdId, pageRequest);
        if (page == null) {
            return FarmResponseUtil.generateResponse(FarmResponseUtil.RES_ERROR, "出库单ID为空");
        }
        Map map = FarmCommonUtil.putPageToMap(page, pageSize, pageNo);
        return FarmResponseUtil.generateResponse(FarmResponseUtil.RES_OK, map);
    }

    /**
     * (9)出库单上产品删除
     *
     * @param cpId
     * @return
     */
    @Path("/delcpxx")
    @POST
    public Response delCkdcpxx(@FormParam("cpId") String cpId) {
        if (searchService.deleteCkdcp(cpId)) {
            return FarmResponseUtil.generateResponse(FarmResponseUtil.RES_OK, "删除成功");
        } else {
            return FarmResponseUtil.generateResponse(FarmResponseUtil.RES_ERROR, "删除失败，参数无效");
        }
    }

    /**
     * (10)销售条码查询
     *
     * @param spzsm 商品追溯码
     * @return
     */
    @Path("/xstm")
    @POST
    public Response searchXstm(@FormParam("spzsm") String spzsm,@FormParam("qybm") String qybm) {
        CktmDto dto = searchService.searchCktm(spzsm,qybm);
        if (dto == null)
            return FarmResponseUtil.generateResponse(FarmResponseUtil.RES_ERROR, "参数无效");

        return FarmResponseUtil.generateResponse(FarmResponseUtil.RES_OK, dto);
    }

    /**
     * (11)销售条码确认
     *
     * @param spzsm 商品追溯码
     * @param mdId  门店ID
     * @return
     */
    @Path("/xstmqr")
    @POST
    public Response xstmqr(@FormParam("spzsm") String spzsm,
                           @FormParam("mdId") String mdId) {
        if (StringUtil.isNotEmpty(spzsm) && StringUtil.isNotEmpty(mdId)) {
            XstmDto dto = searchService.xstmqd(spzsm, mdId);
            if (dto != null) {
                return FarmResponseUtil.generateResponse(FarmResponseUtil.RES_OK, dto);
            }
        }
        return FarmResponseUtil.generateResponse(FarmResponseUtil.RES_ERROR, "参数异常");
    }

    /**
     * (12)销售信息更新
     *
     * @param cpxsId 产品销售ID
     * @param cpzlId 产品种类ID
     * @param zl     重量
     * @param sl     数量
     * @return
     */
    @Path("/updateXsxx")
    @POST
    public Response updateXscpxx(@FormParam("cpxsId") String cpxsId,
                                 @FormParam("cpzlId") String cpzlId,
                                 @FormParam("zl") String zl,
                                 @FormParam("sl") String sl) {
        if (StringUtil.isNotEmpty(cpxsId)) {
            searchService.updateXscp(cpxsId, cpzlId, zl, sl);
            return FarmResponseUtil.generateResponse(FarmResponseUtil.RES_OK, "更新成功");
        }
        return FarmResponseUtil.generateResponse(FarmResponseUtil.RES_ERROR, "参数异常");
    }


    /**
     * (13)销售单产品信息查询
     *
     * @param qyId
     * @param mdId
     * @param pageNo
     * @param pageSize
     * @return
     */
    @Path("/xsdcpxx")
    @POST
    public Response searchXsdcpxx(@FormParam("qyId") String qyId,
                                  @FormParam("mdId") String mdId,
                                  @FormParam("pageNo") String pageNo,
                                  @FormParam("pageSize") String pageSize) {
        if (StringUtil.isEmpty(mdId)) {
            return FarmResponseUtil.generateResponse(FarmResponseUtil.RES_ERROR, "参数无效");
        }
        if (StringUtil.isEmpty(pageNo)) {
            pageNo = "1";
        }
        if (StringUtil.isEmpty(pageSize)) {
            pageSize = "10";
        }
        PageRequest pageRequest = new PageRequest(Integer.valueOf(pageNo), Integer.valueOf(pageSize));
        Page page = searchService.searchXsdcpxx(qyId, mdId, pageRequest);
        Map map = FarmCommonUtil.putPageToMap(page, pageSize, pageNo);

        return FarmResponseUtil.generateResponse(FarmResponseUtil.RES_OK, map);
    }

//	/**
//	 * (14)销售单产品信息查询2
//	 * @param xsdId 销售单ID
//	 * @param pageNo
//	 * @param pageSize
//	 * @return
//	 */
//	@Path("/xsdcpxx2")
//	@POST
//	public Response searchXsdcpxx2(@FormParam("xsdId")String xsdId,
//			                       @FormParam("pageNo")String pageNo,
//			                       @FormParam("pageSize")String pageSize){
//		
//		if(StringUtil.isEmpty(xsdId))
//			return FarmResponseUtil.generateResponse(FarmResponseUtil.RES_ERROR, "参数无效");
//		PageRequest pageRequest = new PageRequest(Integer.valueOf(pageNo),Integer.valueOf(pageSize));
//		Page page = searchService.serchXsdcpxx2(xsdId,pageRequest);
//		Map map = FarmCommonUtil.getInstance().putPageToMap(page, pageSize, pageNo);
//		
//		return FarmResponseUtil.generateResponse(FarmResponseUtil.RES_OK, map);
//	}

    /**
     * (15)销售单产品删除
     *
     * @param cpxsId 产品销售ID
     * @return
     */
    @Path("/delxsdcpxx")
    @POST
    public Response delXsdcpxx(@FormParam("cpxsId") String cpxsId) {
        if (StringUtil.isNotEmpty(cpxsId)) {
            if (searchService.delXsdcp(cpxsId)) {
                return FarmResponseUtil.generateResponse(FarmResponseUtil.RES_OK, "删除成功");
            }
        }
        return FarmResponseUtil.generateResponse(FarmResponseUtil.RES_ERROR, "参数无效");
    }

    /**
     * (16)采收单条码扫码查询
     *
     * @param cspch 采收批次号
     * @return
     */
    @Path("/cstm")
    @POST
    public Response searchCstm(@FormParam("cspch") String cspch) {
        CscpDto dto = searchService.searchCstm(cspch);
        if (dto == null)
            return FarmResponseUtil.generateResponse(FarmResponseUtil.RES_ERROR, "参数无效");
        return FarmResponseUtil.generateResponse(FarmResponseUtil.RES_OK, dto);
    }


    /**
     * (17)包装秤查询
     *
     * @param qybm 企业编码
     * @return
     */
    @Path("/bzc")
    @POST
    public Response searchBzc(@FormParam("qybm") String qybm) {
        if (StringUtil.isEmpty(qybm)) {
            return FarmResponseUtil.generateResponse(FarmResponseUtil.RES_ERROR, "参数无效");
        }
        PageRequest pageRequest = new PageRequest(1, 1000);
        Page page = searchService.searchBzc(qybm, pageRequest);
        Map map = FarmCommonUtil.putPageToMap(page, "1000", "1");
        return FarmResponseUtil.generateResponse(FarmResponseUtil.RES_OK, map);
    }


    /**
     * (18)包装绑定
     *
     * @param cslsh 采收流水号
     * @param csId  采收ID
     * @param cId   称ID(以逗号分隔)
     * @return
     */
    @Path("/bzbd")
    @POST
    public Response searchBzbd(@FormParam("cslsh") String cslsh,
                               @FormParam("csId") String csId,
                               @FormParam("cId") String cId) {
        if (StringUtil.isEmpty(csId) || StringUtil.isEmpty(cId)) {
            return FarmResponseUtil.generateResponse(FarmResponseUtil.RES_ERROR, "参数为空");
        }
        if (searchService.bzcbd(csId, cId)) {
            return FarmResponseUtil.generateResponse(FarmResponseUtil.RES_OK, "绑定成功");
        }
        return FarmResponseUtil.generateResponse(FarmResponseUtil.RES_ERROR, "绑定失败");
    }
    /**
     * (19)根据采收批次号查询产品
     * @param cspch
     * @return
     */
    @POST
    @Path("/queryCpxxByPch")
    public Response queryCpxxByPch(@FormParam("cspch") String cspch,@FormParam("qybm") String qybm) {
        List list = searchService.queryCpxxByPch(cspch,qybm);
        return FarmResponseUtil.generateResponse(FarmResponseUtil.RES_OK, list);
    }


    /**
     * 保存预包装信息
     * @param cspch
     * @param cId
     * @param cpbh
     * @param qybm
     * @return
     */
    @POST
    @Path("/saveYbzxx")
    public Response saveYbzxx(@FormParam("cspch") String cspch,@FormParam("cId") String cId,
                              @FormParam("cpbh")String cpbh,@FormParam("qybm") String qybm) {
        if (StringUtil.isEmpty(cspch) || StringUtil.isEmpty(cId)
                || StringUtil.isEmpty(cpbh)|| StringUtil.isEmpty(qybm)) {
            return FarmResponseUtil.generateResponse(FarmResponseUtil.RES_ERROR, "参数为空");
        }
        if (searchService.saveYbzxx(cspch,cpbh,qybm,cId)) {
            return FarmResponseUtil.generateResponse(FarmResponseUtil.RES_OK, "保存成功");
        }
        return FarmResponseUtil.generateResponse(FarmResponseUtil.RES_ERROR, "保存失败");
    }
}
