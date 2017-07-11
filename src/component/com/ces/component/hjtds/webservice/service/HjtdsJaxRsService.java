package com.ces.component.hjtds.webservice.service;

import ces.sdk.system.exception.SystemFacadeException;
import ces.sdk.util.MD5;
import com.ces.component.farm.dto.TyyhDto;
import com.ces.component.farm.utils.FarmCommonUtil;
import com.ces.component.farm.webservice.service.FarmResponseUtil;
import com.ces.component.herb.service.HerbService;
import com.ces.component.herb.service.nsxService;
import com.ces.component.herb.utils.HerbUserUtil;
import com.ces.component.hjtds.service.HjtdsService;
import com.ces.utils.TokenUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Created by 黄翔宇 on 15/7/1.
 * 水产系统手机端接口
 */
@Component()
@Path("/herb")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
public class HjtdsJaxRsService {

    @Autowired
    private nsxService nsxService;

    @Autowired
    private HerbService herbService;

    @Autowired
    private HjtdsService hjtdsService;
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
			return HjtdsResponseUtil.generateResponse(com.ces.component.aquatic.webservice.service.AquaticResponseUtil.RES_ERROR, "用户名或密码错误");
		}
		String md5Pass = new MD5().getMD5ofStr(password);
		if (!jyshDto.getPassword().equalsIgnoreCase(md5Pass)) {
			return HjtdsResponseUtil.generateResponse(com.ces.component.aquatic.webservice.service.AquaticResponseUtil.RES_ERROR, "用户名或密码错误");
		}
		String token = TokenUtils.getToken(username, password,3000);
		jyshDto.setPassword("");
		jyshDto.setToken(token);
		return HjtdsResponseUtil.generateResponse(com.ces.component.aquatic.webservice.service.AquaticResponseUtil.RES_OK, jyshDto);
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




    @Path("/hjtdsdata")
    @GET
    public Response processHjtdsData(@FormParam("jsondata") String jsondata){
        return  HjtdsResponseUtil.generateResponse(hjtdsService.processHjtdsJsonData(jsondata));
    }
}
