package com.ces.config.utils.release;

import java.io.OutputStreamWriter;
import java.util.Map;

/**
 * 系统发布管理接口
 * 
 * @author wanglei
 * @date 2015-5-28
 */
public interface ReleaseManager {

    /**
     * 向7_selfdefine_table.sql中添加额外的初始化脚本
     * 
     * @param ow 7_selfdefine_table.sql文件的OutputStreamWriter
     * @param map 导出的数据Map
     */
    void createOtherSelfDefineTableSql(OutputStreamWriter ow, Map<String, Object> map);

    /**
     * 向8_init_data.sql中添加额外的初始化脚本
     * 
     * @param ow 8_init_data.sql文件的OutputStreamWriter
     * @param map 导出的数据Map
     */
    void createOtherInitDataSql(OutputStreamWriter ow, Map<String, Object> map);
}
