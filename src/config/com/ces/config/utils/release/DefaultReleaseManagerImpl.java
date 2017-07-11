package com.ces.config.utils.release;

import java.io.OutputStreamWriter;
import java.util.Map;

/**
 * 默认的系统发布管理实现
 * 
 * @author wanglei
 * @date 2015-5-28
 */
public class DefaultReleaseManagerImpl implements ReleaseManager {

    /*
     * (non-Javadoc)
     * @see com.ces.config.utils.release.ReleaseManager#createOtherSelfDefineTableSql(java.io.OutputStreamWriter,
     * java.util.Map)
     */
    @Override
    public void createOtherSelfDefineTableSql(OutputStreamWriter ow, Map<String, Object> map) {

    }

    /*
     * (non-Javadoc)
     * @see com.ces.config.utils.release.ReleaseManager#createOtherInitDataSql(java.io.OutputStreamWriter,
     * java.util.Map)
     */
    @Override
    public void createOtherInitDataSql(OutputStreamWriter ow, Map<String, Object> map) {

    }

}
