package com.ces.component.trace.utils;

import com.ces.xarch.core.web.listener.XarchListener;
import org.springframework.stereotype.Component;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by Plain on 2016/5/30.
 */
@Component
public class DataTypeConvertUtil {
    public static DataTypeConvertUtil getInstance() {
        return XarchListener.getBean(DataTypeConvertUtil.class);
    }
    public Map<String,String> mapObj2mapStr(Map<String,Object> dataMap){
        if(dataMap == null || dataMap.size() == 0) return null ;
        Set<String> keySet =  dataMap.keySet();
        Map<String,String> strMap = new HashMap<String, String>();
        for (String key : keySet){
            strMap.put(key,String.valueOf(dataMap.get(key)));
        }
        return strMap;
    }
    public Map<String,String> mapObj2mapStrFile(Map map){
        Map<String, String> newMap = new HashMap<String, String>();
        Set<Map.Entry> set = map.entrySet();
        Iterator<Map.Entry> iterator = set.iterator();
        while (iterator.hasNext()) {
            Map.Entry entry = iterator.next();
            String[] valueArray = (String[]) entry.getValue();
            String value = "";
            for (int i = 0; i < valueArray.length; i++) {
                value = valueArray[i];
            }
            newMap.put(String.valueOf(entry.getKey()), value);
        }
        return newMap;
    }



    /**
     * 读取txt.properties里的输出url
     *
     * @return
     */
    public String readTxtProp(String name,String path) {
        Properties prop = new Properties();
        try {
            InputStream in = new FileInputStream(path);
            prop.load(in);
            String code = prop.getProperty(name).trim();
            return code ;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
       return null;
    }
    /**
     * 时间格式化
     * @param type
     * @param date
     * @return
     */
    public String DataConvert(String type,Date date){
        SimpleDateFormat sdf = new SimpleDateFormat(type);
        return sdf.format(date);
    }

}
