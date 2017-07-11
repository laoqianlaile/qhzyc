package com.ces.component.sensor.webservice.service;

import ces.sdk.system.exception.SystemFacadeException;
import ces.sdk.util.MD5;
import com.ces.component.farm.dto.TyyhDto;
import com.ces.component.farm.utils.FarmCommonUtil;
import com.ces.component.farm.utils.FarmUserUtil;
import com.ces.component.sensor.service.SensorResponseUtil;
import com.ces.component.sensor.service.SensorService;
import com.ces.utils.TokenUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Component
@Path("/sensor")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
public class SensorJaxRsService {
    @Autowired
    private SensorService sensorService;

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
            return com.ces.component.farm.webservice.service.FarmResponseUtil.generateResponse(com.ces.component.farm.webservice.service.FarmResponseUtil.RES_ERROR, "用户已登录");
        }

        TyyhDto dto = FarmUserUtil.loadUser(username);
        if (dto == null) {
            return com.ces.component.farm.webservice.service.FarmResponseUtil.generateResponse(com.ces.component.farm.webservice.service.FarmResponseUtil.RES_ERROR, "用户名或密码错误或账号未开通");
        }
        String md5Pass = new MD5().getMD5ofStr(password);
        if (!dto.getPassword().equalsIgnoreCase(md5Pass)) {
            return com.ces.component.farm.webservice.service.FarmResponseUtil.generateResponse(com.ces.component.farm.webservice.service.FarmResponseUtil.RES_ERROR, "用户名或密码错误");
        }
        String token = TokenUtils.getToken(username, password, 3000);
        dto.setPassword("");
        dto.setToken(token);
        FarmCommonUtil.tokenMap.put(token, String.valueOf(System.currentTimeMillis() + 24 * 60 * 60 * 1000));
        FarmCommonUtil.loginuserMap.put(username, token);
        FarmCommonUtil.loginuserMap.put(token, username);
        return com.ces.component.farm.webservice.service.FarmResponseUtil.generateResponse(com.ces.component.farm.webservice.service.FarmResponseUtil.RES_OK, dto);
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
            return com.ces.component.farm.webservice.service.FarmResponseUtil.generateResponse(com.ces.component.farm.webservice.service.FarmResponseUtil.RES_ERROR, "Token无效");
        FarmCommonUtil.tokenMap.remove(token);

        String username = String.valueOf(FarmCommonUtil.loginuserMap.get(token));
        FarmCommonUtil.loginuserMap.remove(token);
        FarmCommonUtil.loginuserMap.remove(username);

        return com.ces.component.farm.webservice.service.FarmResponseUtil.generateResponse(com.ces.component.farm.webservice.service.FarmResponseUtil.RES_OK, "注销成功");
    }

    @Path("/getHistoryData")
    @POST
    public Response InsertOrUpdate(@FormParam("data") String data){
//        JSONObject jsonObject = JSONObject.fromObject(data);
//        try {
//            List<Map<String,String>> dataMap = JSONUtil.jsonStringToList(jsonObject.getJSONArray("data"));
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
        if(data == null|| "".equals(data)){
            return SensorResponseUtil.generateResponse(SensorResponseUtil.RES_ERROR,"ERROR");
        }
        sensorService.insertOrUpdate(data);
        return SensorResponseUtil.generateResponse(SensorResponseUtil.RES_OK,"OK");
    }

}
