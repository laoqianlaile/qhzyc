package com.ces.component.herb.webservice.service;

import ces.sdk.system.exception.SystemFacadeException;
import ces.sdk.util.MD5;
import com.ces.component.farm.dto.TyyhDto;
import com.ces.component.farm.utils.FarmCommonUtil;
import com.ces.component.farm.webservice.service.FarmResponseUtil;
import com.ces.component.herb.service.HerbService;
import com.ces.component.herb.service.nsxService;
import com.ces.component.herb.utils.HerbUserUtil;
import com.ces.config.utils.StringUtil;
import com.ces.utils.TokenUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.Map;

/**
 * Created by 黄翔宇 on 15/7/1.
 * 山东中药材手机端接口
 */
@Component()
@Path("/herb")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
public class HerbJaxRsService {

    @Autowired
    private nsxService nsxService;

    @Autowired
    private HerbService herbService;

	@Path("/signin")
	@POST
	public Response login(@FormParam("username") String username, @FormParam("password") String password) {
        TyyhDto jyshDto = null;
        try {
            jyshDto = HerbUserUtil.loadUser(username);
        } catch (SystemFacadeException e) {
            e.printStackTrace();
        }
        if (jyshDto == null) {
			return HerbResponseUtil.generateResponse(com.ces.component.aquatic.webservice.service.AquaticResponseUtil.RES_ERROR, "用户名或密码错误");
		}
		String md5Pass = new MD5().getMD5ofStr(password);
		if (!jyshDto.getPassword().equalsIgnoreCase(md5Pass)) {
			return HerbResponseUtil.generateResponse(com.ces.component.aquatic.webservice.service.AquaticResponseUtil.RES_ERROR, "用户名或密码错误");
		}
		String token = TokenUtils.getToken(username, password,3000);
		jyshDto.setPassword("");
		jyshDto.setToken(token);
		return HerbResponseUtil.generateResponse(com.ces.component.aquatic.webservice.service.AquaticResponseUtil.RES_OK, jyshDto);
	}

    /**
     * 指定signin设置response
     * 的请求头
     * @return
     */
    @Path("/signin")
    @OPTIONS
    public Response login() {
        return Response.ok().header("Access-Control-Allow-Origin", "*")
                .header("Access-Control-Allow-Methods", "POST,OPTIONS")
                .header("Access-Control-Allow-Headers", "Content-Type, Authorization, Accept,X-Requested-With, Xarch-Token, Connection")
                .build();

    }

    @Path("/signout")
    @POST
    public Response logout(@FormParam("Herb-Token") String token) {
        Object obj = FarmCommonUtil.tokenMap.get(token);
        if (obj == null)
            return FarmResponseUtil.generateResponse(FarmResponseUtil.RES_ERROR, "Token无效");
        FarmCommonUtil.tokenMap.remove(token);

//        String username = String.valueOf(FarmCommonUtil.loginuserMap.get(token));
//        FarmCommonUtil.loginuserMap.remove(token);
//        FarmCommonUtil.loginuserMap.remove(username);

        return FarmResponseUtil.generateResponse(FarmResponseUtil.RES_OK, "注销成功");
    }

    @Path("/getNsxxx")
    @GET
    public Response getNsxxx(@QueryParam("username") String username){
        Map<String,Object>   nsxxx = nsxService.searchNsx(username);
        return HerbResponseUtil.generateResponse( nsxxx);
    }

    @Path("/startTask")
    @POST
    public Response startTask(@QueryParam("id") String id,@QueryParam("username") String username){
        if(nsxService.dataVerify(id)){
            nsxService.updateNsxzt(id,username);
            return HerbResponseUtil.generateResponse(HerbResponseUtil.RES_OK);
        }else{
            return HerbResponseUtil.generateResponse(HerbResponseUtil.RES_ERROR);
        }
    }

    @Path("/endTask")
    @POST
    public Response endTask(@FormParam("id") String id){
        if(id != null){
            nsxService.endNsx(id);
            return HerbResponseUtil.generateResponse(HerbResponseUtil.RES_OK);
        }
        return HerbResponseUtil.generateResponse(HerbResponseUtil.RES_ERROR);
    }

    /**
     * 通用粗加工原料、加工、交易，精加工药材入库、饮片加工、饮片交易：增 修 方法
     * @param type
     * @param jsondata
     * @return
     */
    @Path("/herbsave")
    @POST
    public Response save(@FormParam("type")String type,@FormParam("jsondata")String jsondata){
            return HerbResponseUtil.generateResponse( herbService.save(type,jsondata));
    }

    /**
     * 通用粗加工原料、加工、交易，精加工药材入库、饮片加工、饮片交易：删除 方法
     * @param type
     * @param id
     * @return
     */
    @Path("/herbdelete")
    @GET
    public void delete(@FormParam("type")String type,@FormParam("id")String id){
        herbService.delete(type,id);
        //return HerbResponseUtil.generateResponse(herbService.delete(type,id));
    }

    /**
     * 通用粗加工原料、加工、交易，精加工药材入库、饮片加工、饮片交易：查询
     * @param type
     * @param qybm
     * @return
     */
    @Path("/herbquery")
    @GET
    public Response query(@FormParam("type")String type,@FormParam("qybm")String qybm){
        return HerbResponseUtil.generateResponse(herbService.queryList(type,qybm));
    }
  /**
     * 通用粗加工原料、加工、交易，精加工药材入库、饮片加工、饮片交易：查询单条数据
     * @param type
     * @param qybm
     * @return
     */
    @Path("/herbqueryOne")
    @GET
    public Response queryOne(@QueryParam("type") String type,@QueryParam("qybm")String qybm,@QueryParam("id")String id){
        return HerbResponseUtil.generateResponse(herbService.queryOne(type,qybm,id));
    }

    /**
     * 通用粗加工交易，精加工饮片交易：增方法
     * @param type
     * @param jsondata
     * @return
     */
    @Path("/herbsaveAll")
    @POST
    public Response saveAll(@FormParam("type")String type,@FormParam("jsondata")String jsondata){
        herbService.saveAll(type,jsondata);
        return HerbResponseUtil.generateResponse(HerbResponseUtil.generateResponse(HerbResponseUtil.RES_OK));
    }

    /**
     * 库存查询
     * @param type
     * @return
     */
    @Path("/herbstocks")
    @GET
    public Response queryStocks(@FormParam("type")String type,@FormParam("qybm")String qybm){
        return  HerbResponseUtil.generateResponse(herbService.queryStocks(type,qybm));
    }

    /**
     * 采收情况查询
     * @param userName userName
     * @return  Response
     */
    @Path("/queryRecovery")
    @GET
    public Response queryRecovery(@FormParam("userName")String userName){
        return HerbResponseUtil.generateResponse(nsxService.searchCsnsxList(userName));
    }

    /**
     * 开始采收
     * @param id
     * @return
     */
    @Path("/startRecovery")
    @GET
    public Response startRecovery(@FormParam("id")String id){
        if(StringUtil.isNotEmpty(id)){
            nsxService.startRecovery(id);
            return HerbResponseUtil.generateResponse(HerbResponseUtil.RES_OK);
        }else{
            return HerbResponseUtil.generateResponse(HerbResponseUtil.RES_ERROR);
        }
    }

    /**
     * 结束采收
     * @param zzpch
     * @param cszl
     * @param csmj
     * @return
     */
    @Path("/endRecovery")
    @GET
    public Response endRecovery(@FormParam("zzpch")String zzpch,@FormParam("cszl")String cszl,@FormParam("csmj")String csmj){
        if(StringUtil.isNotEmpty(zzpch)){
            nsxService.endRecovery(zzpch, cszl, csmj);
            return HerbResponseUtil.generateResponse(HerbResponseUtil.RES_OK);
        }else{
            return HerbResponseUtil.generateResponse(HerbResponseUtil.RES_ERROR);
        }
    }

    @Path("/getCspch")
    @GET
    public Response getCspch(@FormParam("zzpch") String zzpch){
        if(StringUtil.isNotEmpty(zzpch)){
            List<Map<String,Object>> cspch = nsxService.getCspch(zzpch);
            return  HerbResponseUtil.generateResponse(cspch);
        }
        return HerbResponseUtil.generateResponse(HerbResponseUtil.RES_ERROR) ;
    }

    /**
     * 通过采收批次号获取原料入库信息
     * @param cspch
     * @return
     */
    @Path("/getYlrkxx")
    @GET
    public Response getYlrkxxByCspch(@FormParam("cspch") String cspch){
        if(StringUtil.isNotEmpty(cspch)){
            List<Map<String,Object>> data = herbService.searchDataByCspch(cspch);
            return HerbResponseUtil.generateResponse(data);
        }
        return HerbResponseUtil.generateResponse(HerbResponseUtil.RES_ERROR);
    }

    /**
     * 通过企业销售订单号获取精加工药材采购数据
     * @param xsddh
     * @return
     */
    @Path("/getYcrkxx")
    @GET
    public Response getYycrkxxByQyxsddh(@FormParam("xsddh")String xsddh){
        if(StringUtil.isNotEmpty(xsddh)){
            List<Map<String,Object>> data = herbService.searchYcrkxxByxsddh(xsddh);
            return HerbResponseUtil.generateResponse(data);
        }
        return HerbResponseUtil.generateResponse(HerbResponseUtil.RES_ERROR);
    }

    /**
     * 获取粗加工仓库数据
     * @param qybm
     * @return
     */
    @Path("/getCjgckxx")
    @GET
    public Response getCjgckxx(@FormParam("qybm") String qybm){
        if(StringUtil.isNotEmpty(qybm)){
            List<Map<String,Object>> list = herbService.getCjgCkxx(qybm);
            return HerbResponseUtil.generateResponse(list);
        }
        return HerbResponseUtil.generateResponse(HerbResponseUtil.RES_ERROR);
    }

    /**
     * 获取精加工仓库信息
     * @param qybm
     * @return
     */
    @Path("/getJjgckxx")
    @GET
    public Response getJjgckxx(@FormParam("qybm") String qybm){
        if(StringUtil.isNotEmpty(qybm)){
            List<Map<String,Object>> list = herbService.getJjgCkxx(qybm);
            return HerbResponseUtil.generateResponse(list);
        }
        return HerbResponseUtil.generateResponse(HerbResponseUtil.RES_ERROR);
    }


}
