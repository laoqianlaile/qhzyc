package com.ces.config.utils.release;

import java.io.OutputStreamWriter;
import java.util.Map;

/**
 * 系统发布管理工具类
 * 
 * @author wanglei
 * @date 2015-5-28
 */
public class ReleaseUtil {

    private static ReleaseUtil instance = null;

    private ReleaseManager releaseManager;

    /**
     * 私有构造方法
     */
    private ReleaseUtil() {

    }

    /**
     * 创建实例方法
     * 
     * @return ReleaseUtil
     */
    public static ReleaseUtil getInstance() {
        if (instance == null) {
            synchronized (ReleaseUtil.class) {
                if (instance == null) {
                    instance = new ReleaseUtil();
                }
            }
        }
        return instance;
    }

    public ReleaseManager getReleaseManager() {
        if (releaseManager == null) {
            releaseManager = new DefaultReleaseManagerImpl();
        }
        return releaseManager;
    }

    public void setReleaseManager(ReleaseManager releaseManager) {
        this.releaseManager = releaseManager;
    }

    /**
     * 向7_selfdefine_table.sql中添加额外的初始化脚本
     * 
     * @param ow 7_selfdefine_table.sql文件的OutputStreamWriter
     * @param map 导出的数据Map
     */
    public void createOtherSelfDefineTableSql(OutputStreamWriter ow, Map<String, Object> map) {
        getReleaseManager().createOtherSelfDefineTableSql(ow, map);
    }

    /**
     * 向8_init_data.sql中添加额外的初始化脚本
     * 
     * @param ow 8_init_data.sql文件的OutputStreamWriter
     * @param map 导出的数据Map
     */
    public void createOtherInitDataSql(OutputStreamWriter ow, Map<String, Object> map) {
        getReleaseManager().createOtherInitDataSql(ow, map);
    }
}
