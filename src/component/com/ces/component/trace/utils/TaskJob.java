package com.ces.component.trace.utils;

import com.ces.config.dhtmlx.dao.common.DatabaseHandlerDao;
import org.apache.log4j.Logger;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by hpsgt on 2016-11-16.
 */
public class TaskJob {
    public static Logger log = Logger.getLogger(TaskJob.class);

    @Transactional
    public void updateLsh() {
        // TODO Auto-generated method stub
        try {
            log.info("处理任务开始>........");
            // 业务逻辑代码调用
            String updSql = "update t_sdzyc_lshbm set lsh= 0 where is_update= 1 ";
            int count = DatabaseHandlerDao.getInstance().executeSql(updSql);
            System.out.println("定时更新流水号，共更新："+count+" 条");
            log.info("处理任务结束!");

        } catch (Exception e) {
            log.error("处理任务出现异常", e);
        }
    }
}
