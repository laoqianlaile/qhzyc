package com.ces.config.servlet;

import java.io.IOException;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimerTask;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import ces.sdk.system.bean.OpLogInfo;
import ces.sdk.system.dbbean.DbOpLogInfo;
import ces.sdk.system.exception.SystemFacadeException;

import com.ces.config.datamodel.message.MessageModel;
import com.ces.config.dhtmlx.entity.appmanage.TimingEntity;
import com.ces.config.dhtmlx.entity.appmanage.TimingLog;
import com.ces.config.dhtmlx.service.appmanage.TimingLogService;
import com.ces.xarch.core.web.listener.XarchListener;
import com.ces.xarch.plugins.authsystem.utils.FacadeUtil;

public class Task  extends TimerTask {   
    
    private static Log log = LogFactory.getLog(Task.class);
    
    private TimingEntity timingEntity ;
    
    private TimingLog timingLog;
       
    private static boolean isRunning = true; 
       
	public TimingEntity getTimingEntity() {
		return timingEntity;
	}

	public void setTimingEntity(TimingEntity timingEntity) {
		this.timingEntity = timingEntity;
	}

	public TimingLog gettimingLog() {
		return timingLog;
	}

	public void settimingLog(TimingLog timingLog) {
		this.timingLog = timingLog;
	}

	@SuppressWarnings({"unchecked", "rawtypes"})
    @Override  
    public void run() {
        if (isRunning) {
        	timingLog = new TimingLog();
        	timingLog.setTimingId(timingEntity.getId());
        	timingLog.setStartDate(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
            OpLogInfo opLogInfo = new DbOpLogInfo();
            opLogInfo.setType("定时任务管理");
            opLogInfo.setLogDate(new Date());
            opLogInfo.setOperate("执行定时任务");
            opLogInfo.setNote("定时任务:"+timingEntity.getName());
            opLogInfo.setStatus(1);
            
            String type = timingEntity.getType();
            String beanId = timingEntity.getBeanId();
            Object result = null;
            
            // java -类型
            if (type.equals("0")) {
                if (beanId != null) {
                    Object bean = null;
                    try {
                        bean = XarchListener.getBean(beanId);
                        if (null == bean) {
                            log.error(beanId + " 不存在，请检查！");
                            timingLog.setSuccess("0");
                            timingLog.setMessage(beanId + " 不存在，请检查！");
                            timingLog.setEndDate(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
                            XarchListener.getBean(TimingLogService.class).save(timingLog);
                            opLogInfo.setMessage(beanId + " 不存在，请检查！");
                            FacadeUtil.getLogInfoFacade().addOpLogInfo(opLogInfo);
                            return;
                        }
                    } catch (Exception e) {
                        log.error("定时任务：获取" + beanId + " 对象出错，请检查！", e);
                        timingLog.setSuccess("0");
                        timingLog.setMessage("定时任务：获取" + beanId + " 对象出错，请检查！");
                        timingLog.setEndDate(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
                        XarchListener.getBean(TimingLogService.class).save(timingLog);
                        opLogInfo.setMessage("定时任务：获取" + beanId + " 对象出错，请检查！");
                        try {
							FacadeUtil.getLogInfoFacade().addOpLogInfo(opLogInfo);
						} catch (SystemFacadeException e1) {
							e1.printStackTrace();
						}
                        return;
                    }
                    
                    Class clazz = bean.getClass();
                    Method md = null;
                    String method = timingEntity.getMethod();
                    try {
                        md = clazz.getDeclaredMethod(method);
                        if (null == md) {
                            log.error(bean + " 中的方法名（" + method + "）不存在，请检查！");
                            timingLog.setSuccess("0");
                            timingLog.setMessage(bean + " 中的方法名（" + method + "）不存在，请检查！");
                            timingLog.setEndDate(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
                            XarchListener.getBean(TimingLogService.class).save(timingLog);
                            opLogInfo.setMessage(bean + " 中的方法名（" + method + "）不存在，请检查！");
                            FacadeUtil.getLogInfoFacade().addOpLogInfo(opLogInfo);
                            return;
                        }
                        result = md.invoke(bean);
                    } catch (Exception e) {
                        log.error("执行Bean对象(" + beanId + ")方法( " + method + " )出错！", e);
                        timingLog.setSuccess("0");
                        timingLog.setMessage("执行Bean对象(" + beanId + ")方法( " + method + " )出错！");
                        timingLog.setEndDate(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
                        XarchListener.getBean(TimingLogService.class).save(timingLog);
                        opLogInfo.setMessage("执行Bean对象(" + beanId + ")方法( " + method + " )出错！");
                        try {
							FacadeUtil.getLogInfoFacade().addOpLogInfo(opLogInfo);
						} catch (SystemFacadeException e1) {
							e1.printStackTrace();
						}
                        return;
                    } 
                }
            } else if (type.equals("1")) { // 批量类型
                String command = timingEntity.getCommand();
                Runtime rt = Runtime.getRuntime();
                try {
                    rt.exec("cmd /c start " + command);
                } catch (IOException e) {
                    log.error("执行CMD命令出错！", e);
                    timingLog.setSuccess("0");
                    timingLog.setMessage("执行CMD命令出错！");
                    timingLog.setEndDate(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
                    XarchListener.getBean(TimingLogService.class).save(timingLog);
                    opLogInfo.setMessage("执行CMD命令出错！");
                    try {
						FacadeUtil.getLogInfoFacade().addOpLogInfo(opLogInfo);
					} catch (SystemFacadeException e1) {
						e1.printStackTrace();
					}
                    return;
                }
            }
            if (result != null) {
            	if (result instanceof MessageModel) {
            		if (((MessageModel) result).getSuccess()) {
            			timingLog.setSuccess("1");
                        timingLog.setMessage(((MessageModel) result).getMessage());
                        timingLog.setEndDate(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
					}
            		else {
            			timingLog.setSuccess("0");
                        timingLog.setMessage(((MessageModel) result).getMessage());
                        timingLog.setEndDate(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
					}
				}
            	else {
            		timingLog.setSuccess("1");
                    timingLog.setMessage("执行成功！返回值为" + result);
                    timingLog.setEndDate(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
				}
			}
            else {
            	timingLog.setSuccess("1");
                timingLog.setMessage("执行成功！没有返回值");
                timingLog.setEndDate(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
			}
            opLogInfo.setMessage("执行成功！");
            try {
				FacadeUtil.getLogInfoFacade().addOpLogInfo(opLogInfo);
			} catch (SystemFacadeException e) {
				e.printStackTrace();
			}
            log.info("时间=" + new Date() + " - 任务执行完成!----->任务名称为:" + timingEntity.getName());
            new TimingLogService().save(timingLog);
        }
    }   
}  
