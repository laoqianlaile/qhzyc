package com.ces.component.trace.webservice.service;

import com.ces.component.trace.utils.ResponseUtil;

import org.springframework.stereotype.Component;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.*;


/**
 * Created by Plain on 2016/6/23.
 */
@Component
@Path("/entererp")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class EnterErpJaxRsService {

    @POST
    @Path("/uploadData")
    public Response uploadData(String jsondata){
        return ResponseUtil.generateResponse("OK");
    }

    public static void main(String[] args){
        String jsondata="[{plnbez: \"000000001010040027\", charg: \"20160502\", hsdat: \"20160509\", vfdat: \"20180508\", matnr_ycl: \"000000001030010021\", charg_ycl: \"160502\", zflg_zh: \"\"}]";
        List<Map<String,String>> list = new ArrayList<Map<String, String>>();

//        PLNBEZ    	TYPE CHAR 18,     " 成品编码			(18位的C类型)
//        MAKTX     TYPE CHAR 40,     " 描述				(40位的C类型)
//        CHARG     TYPE CHAR 10,     " 成品批次			(10位的C类型)
//        HSDAT     TYPE DATS 8,      " 生产日期			(日期类型-8位)
//        VFDAT     TYPE DATS 8,      " 到期日				(日期类型-8位)
//        MATNR_YCL 	TYPE CHAR 18,     " 丹参-原材料		(18位的C类型)
//        MAKTX_YCL 	TYPE CHAR 40,     " 物料名称			(40位的C类型)
//        CHARG_YCL 	TYPE CHAR 10,     " 丹参批次			(40位的C类型)
//        ZFLG_ZH   	TYPE C LENGTH 6.   " 是否自种标示		(6位的C类型)

        try {
//            list =jsonStringToList(jsondata);
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println(list.size());
    }
//    /**
//     * qiucs 2015-2-28 上午11:23:29
//     * <p>描述: 将JsonNode转换为键值对 </p>
//     * @return Map<String,String>
//     */
//    protected static Map<String, String> node2map(JsonNode node) {
//        Map<String, String> dMap = new HashMap<String, String>();
//        Iterator<String> it = node.fieldNames();
//        while (it.hasNext()) {
//            String col = (String) it.next();
//            dMap.put(col, node.get(col).asText());
//        }
//        return dMap;
//    }
//    private static List<Map<String, String>> jsonStringToList(String rsContent) throws Exception
//    {
//        JSONArray arry = JSONArray.fromObject(rsContent);
//
//        System.out.println("json字符串内容如下");
//        System.out.println(arry);
//        List<Map<String, String>> rsList = new ArrayList<Map<String, String>>();
//        for (int i = 0; i < arry.size(); i++)
//        {
//            JSONObject jsonObject = arry.getJSONObject(i);
//            Map<String, String> map = new HashMap<String, String>();
//            for (Iterator<?> iter = jsonObject.keys(); iter.hasNext();)
//            {
//                String key = (String) iter.next();
//                String value = jsonObject.get(key).toString();
//                map.put(key, value);
//            }
//            rsList.add(map);
//        }
//        return rsList;
//    }
}
