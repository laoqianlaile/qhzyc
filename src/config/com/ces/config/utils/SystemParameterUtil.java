package com.ces.config.utils;

/**
 * 系统参数模块工具类
 * 
 * @author wanglei
 * @date 2013-10-12
 */
public class SystemParameterUtil {

    private static SystemParameterUtil instance = new SystemParameterUtil();

    /** * 系统参数存储Map，key为系统参数名称，value为系统参数值 */
    // private static Map<String, String> paramMap = new HashMap<String, String>();

    /** * 系统参数在Ehcache中的cache名称 （存储name-value） */
    private static final String SYSTEM_PARAMETER = "SYSTEM_PARAMETER";

    /** * 系统参数在Ehcache中的cache名称 （存储id-value） */
    private static final String SYSTEM_PARAMETER1 = "SYSTEM_PARAMETER1";

    private SystemParameterUtil() {
    }

    /**
     * 获取SystemParameterUtil实例
     * 
     * @return SystemParameterUtil
     */
    public static SystemParameterUtil getInstance() {
        return instance;
    }

    /**
     * 获取系统参数值
     * 
     * @param systemParamName 系统参数名称
     * @return String
     */
    public String getSystemParamValue(String systemParamName) {
        return String.valueOf(EhcacheUtil.getCache(SYSTEM_PARAMETER, systemParamName));
    }

    /**
     * 向paramMap中添加元素
     * 
     * @param systemParamName 系统参数名称
     * @param systemParamValue 系统参数值
     */
    public void putSystemParamValue(String systemParamName, String systemParamValue) {
        EhcacheUtil.setCache(SYSTEM_PARAMETER, systemParamName, systemParamValue);
    }

    /**
     * 移除系统参数
     * 
     * @param systemParamName 系统参数名称
     */
    public void removeSystemParam(String systemParamName) {
        EhcacheUtil.removeCache(SYSTEM_PARAMETER, systemParamName);
    }

    /**
     * 获取系统参数值
     * 
     * @param systemParamId 系统参数ID
     * @return String
     */
    public String getSystemParamValue1(String systemParamId) {
        return String.valueOf(EhcacheUtil.getCache(SYSTEM_PARAMETER1, systemParamId));
    }

    /**
     * 向paramMap中添加元素
     * 
     * @param systemParamId 系统参数ID
     * @param systemParamValue 系统参数值
     */
    public void putSystemParamValue1(String systemParamId, String systemParamValue) {
        EhcacheUtil.setCache(SYSTEM_PARAMETER1, systemParamId, systemParamValue);
    }

    /**
     * 移除系统参数
     * 
     * @param systemParamId 系统参数ID
     */
    public void removeSystemParam1(String systemParamId) {
        EhcacheUtil.removeCache(SYSTEM_PARAMETER1, systemParamId);
    }

    /**
     * 获取发布系统的前台框架
     * 
     * @return String
     */
    public String getReleaseSystemUI() {
        return getSystemParamValue("构件前台");
    }

    /**
     * qiucs 2014-8-6
     * <p>描述: 判断是否为组件库前台</p>
     * 
     * @return boolean 返回类型
     */
    public boolean isCoralUI() {
        return "coral40".equals(getSystemParamValue("构件前台"));
    }
}
