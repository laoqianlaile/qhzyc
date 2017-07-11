package com.ces.component.trace.webservice.service;


import com.ces.component.bddt.service.BddtService;
import com.ces.component.trace.utils.ResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.Map;

@Component
@Path("/map")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class MapService {

    @Autowired
    private BddtService bddtSerivce;
    /**
     * Webservice 获取地址信息
     * @return
     */
    @GET
    @Path("/getAddress")
//	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response getAddress() {
        List<Map<String,Object>> list = bddtSerivce.getAddress();
        return ResponseUtil.generateResponse(list);
    }

    /**
     * WebService 获取城市市场
     * @param cdbm 城市编码
     * @param xtlx 系统类型
     * @return 返回查询结果
     */
    @GET
    @Path("/getShop")
//	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response getShop(@QueryParam("cdbm") String cdbm,@QueryParam("xtlx") String xtlx) {
        List<Map<String,Object>> list = bddtSerivce.getShop(cdbm,xtlx);
        return ResponseUtil.generateResponse(list);
    }

    /**
     * WebService 获取系统类型
     * @return 返回查询结果
     */
    @GET
    @Path("/getXtlx")
//	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response getXtlx() {
        List<Map<String,Object>> list = bddtSerivce.getXtlx();
        return ResponseUtil.generateResponse(list);
    }


}
