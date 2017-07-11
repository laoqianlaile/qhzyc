package com.ces.config.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * <p>描述: 资源文件cfg_common.properties辅助工具</p>
 * <p>公司: 上海中信信息发展股份有限公司</p>
 * @author qiucs   
 * @date 2014-3-6 下午6:30:58
 *
 */
public class CfgCommonUtil {
    
    private static Log log = LogFactory.getLog(CfgCommonUtil.class);

    private static ResourceBundle rb = null;
    
    static {
        try {
            String path = CommonUtil.getAppRootPath() + "WEB-INF" + File.separator + 
            		"conf" + File.separator + 
            		"prop" + File.separator + 
            		"cfg_common.properties";
            InputStream in = new FileInputStream(new File(path));
            rb = new PropertyResourceBundle(in);
        } catch (Exception e) {
            log.error("加载配置文件cfg_common.properties出错！", e);
        }
    }
    
    /**
     * qiucs 2014-3-6 
     * <p>描述: 根据KEY获取配置文件cfg_common.properties的值</p>
     */
    public static String getValue(String key) {
        return rb.getString(key);
    }
    
    /**
     * qiucs 2014-3-6 
     * <p>描述: 获取控制索引个数</p>
     */
    public static int getIndexNumber() {
        return Integer.parseInt(StringUtil.null2zero(getValue("table.index.number")));
    }
    
    /**
     * 是否是发布的系统
     */
    public static boolean isReleasedSystem() {
        return StringUtil.isBooleanTrue(getValue("released_system"));
    }
}
