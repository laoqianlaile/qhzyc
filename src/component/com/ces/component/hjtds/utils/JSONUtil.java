package com.ces.component.hjtds.utils;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import java.util.*;

/**
 * Created by Plain on 2016/6/25.
 */
public class JSONUtil {

    public static List<Map<String, String>> jsonStringToList(String rsContent) throws Exception {
        JSONArray arry = JSONArray.fromObject(rsContent);
        List<Map<String, String>> rsList = new ArrayList<Map<String, String>>();
        for (int i = 0; i < arry.size(); i++) {
            JSONObject jsonObject = arry.getJSONObject(i);
            Map<String, String> map = new HashMap<String, String>();
            for (Iterator<?> iter = jsonObject.keys(); iter.hasNext(); ) {
                String key = (String) iter.next();
                String value = jsonObject.get(key).toString();
                map.put(key, value);
            }
            rsList.add(map);
        }
        return rsList;
    }
    public static Map<String, String> jsonStringToMap(String rsContent) throws Exception {
            JSONObject jsonObject = JSONObject.fromObject(rsContent);
            Map<String, String> map = new HashMap<String, String>();
            for (Iterator<?> iter = jsonObject.keys(); iter.hasNext(); ) {
                String key = (String) iter.next();
                String value = jsonObject.get(key).toString();
                map.put(key, value);
            }
        return map;
    }
    public static List<Map<String, String>> jsonStringToList(JSONArray arry) throws Exception {
        List<Map<String, String>> rsList = new ArrayList<Map<String, String>>();
        for (int i = 0; i < arry.size(); i++) {
            JSONObject jsonObject = arry.getJSONObject(i);
            Map<String, String> map = new HashMap<String, String>();
            for (Iterator<?> iter = jsonObject.keys(); iter.hasNext(); ) {
                String key = (String) iter.next();
                String value = jsonObject.get(key).toString();
                map.put(key, value);
            }
            rsList.add(map);
        }
        return rsList;
    }
    public static Map<String, String> jsonStringToMap(JSONObject jsonObject) throws Exception {
        Map<String, String> map = new HashMap<String, String>();
        for (Iterator<?> iter = jsonObject.keys(); iter.hasNext(); ) {
            String key = (String) iter.next();
            String value = jsonObject.get(key).toString();
            map.put(key, value);
        }
        return map;
    }
}
