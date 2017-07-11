package com.ces.trace.test;

import com.ces.component.trace.utils.HttpClientUtil;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.message.BasicNameValuePair;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by 黄翔宇 on 15/7/1.
 */
public class TraceTest {

	static String baseUrl = "http://127.0.0.1:8080/config1.0/services/jaxrs/traceThain";

	public static void main(String[] args) throws IOException {
//        test_getRevTraceChain();
        test_getNearMarkets();
//        test_getQyxxDetail();
    }

    static void test_getRevTraceChain() throws IOException {
		String url = baseUrl + "/getRevTraceChain?zsm=00000015500000000009";
		String result = HttpClientUtil.loadMessageByGet(url);
		System.out.println(result);
	}

	static void test_getNearMarkets() throws IOException {
		String url = baseUrl + "/getNearMarkets?mlong=0&mlat=0&radius=1000000000";
		String result = HttpClientUtil.loadMessageByGet(url);
		System.out.println(result);
	}

	static void test_getQyxxDetail() throws IOException {
		String url = baseUrl + "/getQyxxDetail?qybm=000000304";
		String result = HttpClientUtil.loadMessageByGet(url);
		System.out.println(result);
	}

}
