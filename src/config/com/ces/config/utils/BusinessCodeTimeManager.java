package com.ces.config.utils;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;

import com.ces.config.dhtmlx.entity.code.BusinessCodeTask;

/**
 * 业务表编码定时任务管理类
 **/
public class BusinessCodeTimeManager {

    public static final Timer timer = new Timer(true);

    public static Map<String, BusinessCodeTask> taskMap = new HashMap<String, BusinessCodeTask>();

    /**
     * 启动间隔任务
     */
    public static void startSchedule(String codeTypeCode, String period) {
        BusinessCodeTask businessCodeTask = new BusinessCodeTask(codeTypeCode);
        taskMap.put(codeTypeCode, businessCodeTask);
        long p = Long.parseLong(StringUtil.null2zero(period))*60*1000;
        if (p != 0) {
            timer.scheduleAtFixedRate(businessCodeTask, 2000, p);
        }
    }

    /**
     * 停止间隔任务
     */
    public static void stopSchedule(String codeTypeCode) {
        BusinessCodeTask businessCodeTask = taskMap.get(codeTypeCode);
        if (businessCodeTask != null) {
            businessCodeTask.cancel();
        }
        timer.purge();
    }

}