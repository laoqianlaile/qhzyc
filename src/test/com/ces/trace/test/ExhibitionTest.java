package com.ces.trace.test;

import com.ces.component.trace.utils.HttpClientUtil;

import java.io.IOException;

/**
 * Created by Synge on 2015/10/26.
 */
public class ExhibitionTest {

//    static String baseUrl = "http://192.10.22.171:8080/config1.0/services/jaxrs/exhibition";
//    static String baseUrl = "http://10.10.40.5/config1.0/services/jaxrs/exhibition";
//    static String baseUrl = "http://localhost:8088/config1.0/services/jaxrs/exhibition";
    static String baseUrl = "http://10.10.40.5:8080/config1.0/services/jaxrs/exhibition";


    public static void main(String[] args) throws IOException {
//        test_getWorkLoad();
//        test_getSalesVolume();
//        test_getSatisfaction();
//        test_getSafety();
        test_getIotByDkbh();
    }

    static void test_getWorkLoad() throws IOException {
        String url = baseUrl + "/getWorkLoad";
        String result = HttpClientUtil.loadMessageByGet(url);
        System.out.println(result);
    }

    static void test_getSalesVolume() throws IOException {
        String url = baseUrl + "/getSalesVolume";
        String result = HttpClientUtil.loadMessageByGet(url);
        System.out.println(result);
    }

    static void test_getSatisfaction() throws IOException {
        String url = baseUrl + "/getSatisfaction";
        String result = HttpClientUtil.loadMessageByGet(url);
        System.out.println(result);
    }

    static void test_getSafety() throws IOException {
        String url = baseUrl + "/getSafety";
        String result = HttpClientUtil.loadMessageByGet(url);
        System.out.println(result);
    }

    static void test_getIotByDkbh() throws IOException {
        String url = baseUrl + "/getIotByDkbh?dkbh=dgjd21260008";
        String result = HttpClientUtil.loadMessageByGet(url);
        System.out.println(result);
    }

}
