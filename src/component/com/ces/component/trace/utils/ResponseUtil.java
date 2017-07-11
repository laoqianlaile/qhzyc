package com.ces.component.trace.utils;

import javax.ws.rs.core.Response;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by 黄翔宇 on 15/6/4.
 */
public class ResponseUtil {

	/**
	 * webservice返回值构造
	 * @param value
	 * @return
	 */
	public static Response generateResponse(Object value) {
		Response.ResponseBuilder builder = Response.ok();
		if (value != null) {
			if (value instanceof String) {
				builder.entity(value);
			} else {
				builder.entity(JSON.toJSON(value));
			}
		}
		return builder
				.header("Access-Control-Allow-Origin", "*")
				.header("Access-Control-Allow-Methods", "POST, GET, PUT, DELETE, OPTIONS")
				.header("Access-Control-Allow-Headers", "Content-Type, Authorization, Accept,X-Requested-With, Xarch-Token")
				.build();
	}

	public static Response generateResponse() {
		return generateResponse(null);
	}

}
