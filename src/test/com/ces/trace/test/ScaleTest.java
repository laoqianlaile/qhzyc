package com.ces.trace.test;

import com.ces.component.trace.utils.HttpClientUtil;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.message.BasicNameValuePair;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Synge on 2015/10/19.
 */
public class ScaleTest {
    static String baseUrl = "http://127.0.0.1:8080/config1.0/services/jaxrs/scale";
//    static String baseUrl = "http://127.0.0.1:8081/jlzs/services/jaxrs/fxzs";

//    static String token = "MToxNDM4MjUzMzYwNDM1OmEzN2QzZjRmY2MwMzMxNWFmNjg5ODhhMzYzZWEzMmUz";

    public static void main(String[] args) throws IOException {
        test_queryScdaByIckh();
//        test_getCslsh();
//        test_saveCsxx();

//        test_getYtqyBySpxx();
//        test_getFxzsByPm();
//        test_getFxzsByXm();
    }

    static void test_queryScdaByIckh() throws IOException {
        String url = baseUrl + "/queryScdaByDkbh?dkbh=0000000010011";
        String result = HttpClientUtil.loadMessageByGet(url);
        System.out.println(result);
    }

    static void test_getCslsh() throws IOException {
        String url = baseUrl + "/getCslsh?qybm=000000013";
        String result = HttpClientUtil.loadMessageByGet(url);
        System.out.println(result);
    }

    static void test_saveCsxx() throws IOException {//商户日期分类台账查询
        String url = baseUrl + "/saveCsxx";
        List<NameValuePair> urlParameters = new ArrayList<NameValuePair>();
        urlParameters.add(new BasicNameValuePair("pl", "茄子"));
        urlParameters.add(new BasicNameValuePair("zzdymc", "区域1地块1单元2"));
        urlParameters.add(new BasicNameValuePair("qybh", "0x001"));
        urlParameters.add(new BasicNameValuePair("qymc", "hxy区域1"));
        urlParameters.add(new BasicNameValuePair("zzdybh", "0x001000103"));
        urlParameters.add(new BasicNameValuePair("dkmc", "区域1地块1"));
        urlParameters.add(new BasicNameValuePair("pzbh", "005"));
        urlParameters.add(new BasicNameValuePair("plbh", "008"));
        urlParameters.add(new BasicNameValuePair("dkbh", "0x0010001"));
        urlParameters.add(new BasicNameValuePair("scdabh", "0x001000103201503"));
        urlParameters.add(new BasicNameValuePair("pz", "七宝茄子"));
        urlParameters.add(new BasicNameValuePair("cslsh", "0000000130000070"));
        urlParameters.add(new BasicNameValuePair("pch", "0000000130000070001"));
        urlParameters.add(new BasicNameValuePair("zldj", "1"));
        urlParameters.add(new BasicNameValuePair("dyzs", "5"));
        urlParameters.add(new BasicNameValuePair("cssj", "2015-10-08"));
        urlParameters.add(new BasicNameValuePair("zl", "600"));
        urlParameters.add(new BasicNameValuePair("qybm", "000000013"));

        UrlEncodedFormEntity urlEncodedFormEntity = new UrlEncodedFormEntity(urlParameters,"UTF-8");
        String result = HttpClientUtil.loadMessageByPost(url, "application/x-www-form-urlencoded", urlEncodedFormEntity);
        System.out.println(result);
    }

    static void test_getYtqyBySpxx() throws IOException {
        String url = baseUrl + "/getYtqyBySpxx?barCode=6901028300056";
        String result = HttpClientUtil.loadMessageByGet(url);
        System.out.println(result);
    }

    static void test_getFxzsByPm() throws IOException {
        String url = baseUrl + "/getFxzsByPm?bottleNo=3656265";
        String result = HttpClientUtil.loadMessageByGet(url);
        System.out.println(result);
    }

    static void test_getFxzsByXm() throws IOException {
        String url = baseUrl + "/getFxzsByXm?boxNo=2848484848";
        String result = HttpClientUtil.loadMessageByGet(url);
        System.out.println(result);
    }
}
