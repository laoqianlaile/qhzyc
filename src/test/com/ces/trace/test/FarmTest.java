package com.ces.trace.test;

import com.ces.component.farm.service.FarmService;
import com.ces.component.farm.utils.TxtUtils;
import com.ces.component.trace.utils.HttpClientUtil;
import com.ces.config.utils.ComponentFileUtil;
import com.ces.xarch.core.web.listener.XarchListener;
import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.message.BasicNameValuePair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

/**
 * Created by bdz on 2015/12/8.
 */
public class FarmTest {

    static String baseUrl = "http://localhost:8080/config1.0/services/jaxrs/farm";
    static String token = "0e62q9DHOjE0NTcyNTQ2ODg4NjY6NTMxODA3NWM5MTIwMDY3OGRlYzQyNmQ1ZGFmZDgxZmM";

    public static void main(String[] args) throws IOException {
//        test_login();
//        test_query();
//        test_save();
        test_queryCpxxByPch();
        test_cktm();
//        File newFile = new File("D:/test"+ DateFormatUtils.format(new Date(),"yyyyMMddhhmmss")+".txt");
//        try {
//            TxtUtils.writeTxtFile("2222",newFile,"UTF-8");
//            TxtUtils.createFile(newFile);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
    }

    static void test_login() throws IOException {
        String url = baseUrl + "/login";
        List<NameValuePair> urlParameters = new ArrayList<NameValuePair>();
        urlParameters.add(new BasicNameValuePair("username", "杨东星"));
        urlParameters.add(new BasicNameValuePair("password", "000000"));
        UrlEncodedFormEntity urlEncodedFormEntity = new UrlEncodedFormEntity(urlParameters,"UTF-8");
        String result = HttpClientUtil.loadMessageByPost(url, "application/x-www-form-urlencoded", urlEncodedFormEntity);
        System.out.println(result);
    }

    static void test_queryCpxxByPch() throws IOException {
        String url = baseUrl + "/queryCpxxByPch";
        List<NameValuePair> urlParameters = new ArrayList<NameValuePair>();
        urlParameters.add(new BasicNameValuePair("Farm-Token", token));
        urlParameters.add(new BasicNameValuePair("cspch", "000000615000001101"));
        urlParameters.add(new BasicNameValuePair("qybm", "000000615"));
        UrlEncodedFormEntity urlEncodedFormEntity = new UrlEncodedFormEntity(urlParameters);
        String result = HttpClientUtil.loadMessageByPost(url, "application/x-www-form-urlencoded", urlEncodedFormEntity);
        System.out.println(result);
    }

    static void test_cktm() throws IOException {
        String url = baseUrl + "/cktm";
        List<NameValuePair> urlParameters = new ArrayList<NameValuePair>();
        urlParameters.add(new BasicNameValuePair("Farm-Token", token));
        urlParameters.add(new BasicNameValuePair("spzsm", "00000061500000000005"));
        urlParameters.add(new BasicNameValuePair("qybm", "000000615"));
        UrlEncodedFormEntity urlEncodedFormEntity = new UrlEncodedFormEntity(urlParameters);
        String result = HttpClientUtil.loadMessageByPost(url, "application/x-www-form-urlencoded", urlEncodedFormEntity);
        System.out.println(result);
    }

    static void test_query() throws IOException {
        String url = baseUrl + "/queryCpxxByPch";
        List<NameValuePair> urlParameters = new ArrayList<NameValuePair>();
        urlParameters.add(new BasicNameValuePair("Farm-Token", token));
        urlParameters.add(new BasicNameValuePair("cspch", "00000001300000251"));
        UrlEncodedFormEntity urlEncodedFormEntity = new UrlEncodedFormEntity(urlParameters);
        String result = HttpClientUtil.loadMessageByPost(url, "application/x-www-form-urlencoded", urlEncodedFormEntity);
        System.out.println(result);
    }
    static void test_queryC() throws IOException {
        String url = baseUrl + "/bzc";
        List<NameValuePair> urlParameters = new ArrayList<NameValuePair>();
        urlParameters.add(new BasicNameValuePair("Farm-Token", token));
        urlParameters.add(new BasicNameValuePair("qybm", "000000013"));
        UrlEncodedFormEntity urlEncodedFormEntity = new UrlEncodedFormEntity(urlParameters);
        String result = HttpClientUtil.loadMessageByPost(url, "application/x-www-form-urlencoded", urlEncodedFormEntity);
        System.out.println(result);
    }
    static void test_save() throws IOException {
        String url = baseUrl + "/saveYbzxx";
        List<NameValuePair> urlParameters = new ArrayList<NameValuePair>();
        urlParameters.add(new BasicNameValuePair("Farm-Token", token));
        urlParameters.add(new BasicNameValuePair("cspch", "00000030400000014"));
        urlParameters.add(new BasicNameValuePair("cId", "408a9684510e542601510ec207bd0086,408a9684510e542601510eba7a380054"));
        urlParameters.add(new BasicNameValuePair("qybm", "000000304"));
        urlParameters.add(new BasicNameValuePair("cpbh", "001"));
        UrlEncodedFormEntity urlEncodedFormEntity = new UrlEncodedFormEntity(urlParameters);
        String result = HttpClientUtil.loadMessageByPost(url, "application/x-www-form-urlencoded", urlEncodedFormEntity);
        System.out.println(result);
    }
}
