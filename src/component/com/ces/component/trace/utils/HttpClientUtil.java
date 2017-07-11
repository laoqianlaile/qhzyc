package com.ces.component.trace.utils;

import com.ces.config.utils.StringUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.protocol.HTTP;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class HttpClientUtil {

	public final static String defaultUrlEncoding = HTTP.UTF_8;//默认编码格式

	private static Log log = LogFactory.getLog(HttpClientUtil.class);

	private HttpClientUtil() {
	}

	/**
	 * 初始化 HttpClient
	 *
	 * @return
	 */
	public static HttpClient initHttpClient() {
		HttpClient httpClient = null;
		httpClient = new DefaultHttpClient();
		httpClient.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 10000); //请求超时 单位毫秒
		httpClient.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, 60000);//读取超时
		return httpClient;
	}

	/**
	 * 以GET方式获取资源
	 * @param url 目标地址
	 * @return
	 * @throws IOException
	 */
	public static String loadMessageByGet(String url) throws IOException {
		return loadMessageByGet(url, defaultUrlEncoding);
	}


	/**
	 * 以GET方式获取资源
	 * @param url 目标地址
	 * @param urlEncoding 编码方式
	 * @return
	 * @throws IOException
	 */
	public static String loadMessageByGet(String url, String urlEncoding) throws IOException {

		String result = null;
		HttpClient httpClient = null;
		BufferedReader reader = null;
		HttpGet get = null;
		try {
			if (StringUtil.isNotEmpty(url)) {
				urlEncoding = StringUtil.isEmpty(urlEncoding) ? defaultUrlEncoding : urlEncoding;
				httpClient = initHttpClient();
				get = new HttpGet(url);
				HttpResponse response = httpClient.execute(get);
				StringBuffer stringBuffer = new StringBuffer();
				//返回的内容实体
				HttpEntity entity = response.getEntity();
				if (entity != null) {
					reader = new BufferedReader(new InputStreamReader(entity.getContent(), urlEncoding));
					String content = null;
					while ((content = reader.readLine()) != null) {
						stringBuffer.append(content);
					}
				}
				result = stringBuffer.toString();
			}
		} finally {
			if (reader != null) {
				reader.close();
				reader = null;
			}
			if (get != null && !get.isAborted()) {
				get.abort();
				get = null;
			}
			if (httpClient != null) {
				httpClient.getConnectionManager().shutdown();
				httpClient = null;
			}
		}
		return result;
	}

	/**
	 * 以POST方式获取资源
	 * @param url         目标地址
	 * @param contentType 请求方式头
	 * @param entity      请求实体对象
	 * @return
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	public static String loadMessageByPost(String url, String contentType, HttpEntity entity) throws IOException {
		return loadMessageByPost(url, defaultUrlEncoding, contentType, entity);
	}

	/**
	 * 以POST方式获取资源
	 * @param url         目标地址
	 * @param urlEncoding 编码格式（编码格式默认utf-8）
	 * @param contentType 请求方式头
	 * @param entity      请求实体对象
	 * @return
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	public static String loadMessageByPost(String url, String urlEncoding, String contentType, HttpEntity entity) throws IOException {

		String result = null;//返回结果
		HttpClient httpClient = null;
		HttpPost post = null;
		BufferedReader reader = null;
		try {
			if (StringUtil.isNotEmpty(url)) {
				urlEncoding = StringUtil.isEmpty(urlEncoding) ? defaultUrlEncoding : urlEncoding;
				httpClient = initHttpClient();//初始化 httpClient
				post = new HttpPost(url);//创建 post
				post.setHeader("Content-Type",contentType);
				post.setEntity(entity);
			/*
			ArrayList<NameValuePair> nvps = new ArrayList<NameValuePair>();//创建参数容器
			for (Map.Entry<String, Object> entry : params.entrySet()) {//组装参数
				nvps.add(new BasicNameValuePair(entry.getKey(), entry.getValue().toString()));
			}
			MultipartEntityBuilder multipartEntityBuilder = MultipartEntityBuilder.create();
			multipartEntityBuilder.addPart("file1",new FileBody(new File("/Users/WILL/Desktop/JPA.png")));
			multipartEntityBuilder.addBinaryBody("file2", new File("/Users/WILL/Desktop/问题.docx"), ContentType.MULTIPART_FORM_DATA,"问题.docx");
			multipartEntityBuilder.addTextBody("test","test2");
			post.setEntity(multipartEntityBuilder.build());
			post.setEntity(new FileEntity(new File("/")));
			post.setEntity(new StringEntity(JSON.toJSON(params)));
			post.setEntity(new UrlEncodedFormEntity(nvps, urlEncoding));
			*/
				HttpResponse response = httpClient.execute(post);
				StringBuffer stringBuffer = new StringBuffer();
				HttpEntity resEntity = response.getEntity();
				if (resEntity != null) {
					reader = new BufferedReader(new InputStreamReader(resEntity.getContent(), urlEncoding));
					String content = null;
					while ((content = reader.readLine()) != null) {
						stringBuffer.append(content);
					}
				}
				result = stringBuffer.toString();
			}
		} finally {
			if (reader != null) {
				reader.close();
				reader = null;
			}
			if (post != null && !post.isAborted()) {
				post.abort();
				post = null;
			}
			if (httpClient != null) {
				httpClient.getConnectionManager().shutdown();
				httpClient = null;
			}
		}
		return result;
	}

}