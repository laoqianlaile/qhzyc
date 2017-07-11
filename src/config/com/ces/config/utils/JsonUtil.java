package com.ces.config.utils;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonUtil {
    
    private static Log log = LogFactory.getLog(JsonUtil.class);
    
    private static ObjectMapper mapper;
    
    static {
    	mapper = new ObjectMapper();
    	mapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_CONTROL_CHARS, true);
    }
    
    public static ObjectMapper getMapperInstance() {
        return mapper;
    }
    
    /**
     * qiucs 2011-11-18 上午10:14:00
     * <p>描述: 将对象转换为JSON字符串 </p>
     * @return String
     */
    public static String bean2json(Object bean) {
        String json = null;
        try {
            ObjectMapper mapper = getMapperInstance();
            json = mapper.writeValueAsString(bean);
        } catch (JsonProcessingException e) {
            log.error("bean转json出错", e);
        }
        return json;
    }
    
    /**
     * qiucs 2011-11-18 上午10:14:56
     * <p>描述: 将json字符串转换为JsonNode对象 </p>
     * @return JsonNode
     */
    public static JsonNode json2node(String json) {
        JsonNode node = null;
        try {
            ObjectMapper mapper = getMapperInstance();
            node = mapper.readTree(json);
        } catch (Exception e) {
            log.error("json转JsonNode出错", e);
        }
        return node;
    }
    
    /**
     * qiucs 2015-8-4 下午4:08:11
     * <p>描述: 将json字符串转换为指定对象 </p>
     * @return T
     */
    public static  <T> T toBean(String json, Class<T> beanClass) {
    	T bean = null;
    	try {
			bean = getMapperInstance().readValue(json, beanClass);
		} catch (Exception e) {
			e.printStackTrace();
            log.error("json转对象(" + beanClass + ")出错", e);
		}
    	return bean;
    }
}
