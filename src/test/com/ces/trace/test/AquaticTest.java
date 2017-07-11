package com.ces.trace.test;

import com.ces.component.aquatic.dto.TzDto;
import com.ces.component.trace.utils.HttpClientUtil;
import com.ces.component.trace.utils.JSON;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by 黄翔宇 on 15/7/1.
 */
public class AquaticTest {

	static String baseUrl = "http://192.168.1.154:8080/config1.0/services/jaxrs/aquatic";

	static String token = "MToxNDM4MjUzMzYwNDM1OmEzN2QzZjRmY2MwMzMxNWFmNjg5ODhhMzYzZWEzMmUz";

	public static void main(String[] args) throws IOException {
//        test_login();
//        test_uploadLedger();
//        test_queryLedger();
//        test_queryLedgerAll();
        test_getTzxx();
//		  test_getTzxxAll();
//        test_modifyShjbxx();
//        test_modifyJypl();
//        test_searchJyplxx();
//        test_searchSptzxx();
//        test_queryLedgerList();
//		  test_getTzDetail();
    }

    static void test_login() throws IOException {
		String url = baseUrl + "/login";
		List<NameValuePair> urlParameters = new ArrayList<NameValuePair>();
		urlParameters.add(new BasicNameValuePair("username", "1"));
		urlParameters.add(new BasicNameValuePair("password", "2222"));
		urlParameters.add(new BasicNameValuePair("userType", "1"));
		UrlEncodedFormEntity urlEncodedFormEntity = new UrlEncodedFormEntity(urlParameters);
		String result = HttpClientUtil.loadMessageByPost(url, "application/x-www-form-urlencoded", urlEncodedFormEntity);
		System.out.println(result);
	}

	static void test_uploadLedger() throws IOException {
		String url = baseUrl + "/uploadLedger?Aquatic-Token=" + token + "&User-Type=1";
		MultipartEntityBuilder multipartEntityBuilder = MultipartEntityBuilder.create();
		multipartEntityBuilder.addPart("uploadedFile", new FileBody(new File("/Users/WILL/Desktop/1.jpg")));
		multipartEntityBuilder.addTextBody("qybm", "qybm中文", ContentType.APPLICATION_JSON);
		multipartEntityBuilder.addTextBody("qymc", "qymc");
		multipartEntityBuilder.addTextBody("shid", "shid11");
		multipartEntityBuilder.addTextBody("shmc", "shmc");
		multipartEntityBuilder.addTextBody("spbh", "spbh");
		multipartEntityBuilder.addTextBody("tzlx", "7");
		String result = HttpClientUtil.loadMessageByPost(url, "multipart/form-data", multipartEntityBuilder.build());
		System.out.println(result);
	}

	static void test_queryLedger() throws IOException {//商户日期分类台账查询
		String url = baseUrl + "/queryLedger?Aquatic-Token=" + token + "&User-Type=2";
		List<NameValuePair> urlParameters = new ArrayList<NameValuePair>();
		urlParameters.add(new BasicNameValuePair("shid", "402881e44e6632d8014e665ae01c00ab"));
		urlParameters.add(new BasicNameValuePair("tzlx", ""));
		urlParameters.add(new BasicNameValuePair("P_pageNumber", "1"));
		urlParameters.add(new BasicNameValuePair("P_pageSize", "10"));
		UrlEncodedFormEntity urlEncodedFormEntity = new UrlEncodedFormEntity(urlParameters);
		String result = HttpClientUtil.loadMessageByPost(url, "application/x-www-form-urlencoded", urlEncodedFormEntity);
		System.out.println(result);
	}

	static void test_queryLedgerAll() throws IOException {//商户日期分类台账查询
		String url = baseUrl + "/queryLedgerAll?Aquatic-Token=" + token + "&User-Type=1";
		List<NameValuePair> urlParameters = new ArrayList<NameValuePair>();
		urlParameters.add(new BasicNameValuePair("tzlx", "jinhuo"));
		urlParameters.add(new BasicNameValuePair("P_pageNumber", "1"));
		urlParameters.add(new BasicNameValuePair("P_pageSize", "10"));
		UrlEncodedFormEntity urlEncodedFormEntity = new UrlEncodedFormEntity(urlParameters);
		String result = HttpClientUtil.loadMessageByPost(url, "application/x-www-form-urlencoded", urlEncodedFormEntity);
		System.out.println(result);
	}



	static void test_getTzxx() throws IOException {
		String url = baseUrl + "/getTzxx?Aquatic-Token=" + token + "&User-Type=2&tzid=4028811a4e8cb5d8014e8f474e4e0000";
		String result = HttpClientUtil.loadMessageByGet(url);
		System.out.println(result);
	}

	static void test_getTzxxAll() throws IOException {
			String url = baseUrl + "/getTzxxAll?Aquatic-Token=" + token + "&User-Type=1&tzlx=8&date=2015-07-27";
			String result = HttpClientUtil.loadMessageByGet(url);
			System.out.println(result);
	}

    static void test_modifyShjbxx() throws IOException {
        String url = baseUrl + "/modifyShjbxx?Aquatic-Token=" + token + "&User-Type=1";
        List<NameValuePair> urlParameters = new ArrayList<NameValuePair>();
        urlParameters.add(new BasicNameValuePair("shid", "402881e44e6632d8014e665ae01c00ab"));
        urlParameters.add(new BasicNameValuePair("sjh", "18771231122"));
        UrlEncodedFormEntity urlEncodedFormEntity = new UrlEncodedFormEntity(urlParameters);
        String result = HttpClientUtil.loadMessageByPost(url, "application/x-www-form-urlencoded", urlEncodedFormEntity);
        System.out.println(result);
    }

    static void test_modifyJypl() throws IOException {
        String url = baseUrl + "/modifyJypl?Aquatic-Token=" + token + "&User-Type=1";
        List<NameValuePair> urlParameters = new ArrayList<NameValuePair>();
        urlParameters.add(new BasicNameValuePair("shid", "402881e44e6632d8014e665ae01c00ab"));
        urlParameters.add(new BasicNameValuePair("jyplbm", "5160-2006"));
        UrlEncodedFormEntity urlEncodedFormEntity = new UrlEncodedFormEntity(urlParameters);
        String result = HttpClientUtil.loadMessageByPost(url, "application/x-www-form-urlencoded", urlEncodedFormEntity);
        System.out.println(result);
    }
    static void test_searchJyplxx() throws IOException {
        String url = baseUrl + "/searchJyplxx?Aquatic-Token=" + token + "&User-Type=1";
        List<NameValuePair> urlParameters = new ArrayList<NameValuePair>();
        urlParameters.add(new BasicNameValuePair("P_pageNumber", "1"));
        urlParameters.add(new BasicNameValuePair("P_pageSize", "20"));
        UrlEncodedFormEntity urlEncodedFormEntity = new UrlEncodedFormEntity(urlParameters);
        String result = HttpClientUtil.loadMessageByPost(url, "application/x-www-form-urlencoded", urlEncodedFormEntity);
        System.out.println(result);
    }
    static void test_searchSptzxx() throws IOException {
        String url = baseUrl + "/searchSptzxx?Aquatic-Token=" + token + "&User-Type=1";
        List<NameValuePair> urlParameters = new ArrayList<NameValuePair>();
        urlParameters.add(new BasicNameValuePair("shid", "402881e44e6632d8014e665ae01c00ab"));
        urlParameters.add(new BasicNameValuePair("P_pageNumber", "1"));
        urlParameters.add(new BasicNameValuePair("P_pageSize", "20"));
        UrlEncodedFormEntity urlEncodedFormEntity = new UrlEncodedFormEntity(urlParameters);
        String result = HttpClientUtil.loadMessageByPost(url, "application/x-www-form-urlencoded", urlEncodedFormEntity);
        System.out.println(result);
    }
	static void test_queryLedgerList() throws IOException {
		String url = baseUrl + "/queryLedgerList?Aquatic-Token=" + token + "&User-Type=2";
		List<NameValuePair> urlParameters = new ArrayList<NameValuePair>();
//		urlParameters.add(new BasicNameValuePair("shId", "402881e44e6632d8014e665ae01c00ab"));
		urlParameters.add(new BasicNameValuePair("sbSjqs", "2015-07-01"));
		urlParameters.add(new BasicNameValuePair("sbSjjs", "2015-07-10"));
		urlParameters.add(new BasicNameValuePair("P_pageNumber", "1"));
		urlParameters.add(new BasicNameValuePair("P_pageSize", "20"));
		UrlEncodedFormEntity urlEncodedFormEntity = new UrlEncodedFormEntity(urlParameters);
		String result = HttpClientUtil.loadMessageByPost(url, "application/x-www-form-urlencoded", urlEncodedFormEntity);
		System.out.println(result);
	}

	static void test_getTzDetail() throws IOException {
		String url = baseUrl + "/getTzDetail?Aquatic-Token=" + token + "&User-Type=1&tzid=402881f54e67534f014e675a518d0006";
		String result = HttpClientUtil.loadMessageByGet(url);
		System.out.println(result);
	}
}
