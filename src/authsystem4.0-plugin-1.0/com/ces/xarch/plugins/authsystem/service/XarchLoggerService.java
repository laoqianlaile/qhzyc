/**
 * <p>Copyright:Copyright(c) 2014</p>
 * <p>Company:上海中信信息发展股份有限公司</p>
 * <p>包名:com.ces.xarch.plugins.authsystem.service</p>
 * <p>文件名:XarchLoggerService.java</p>
 * <p>类更新历史信息</p>
 * @todo Reamy(杨木江 yangmujiang@sohu.com) 创建于 2014-01-13 10:25:51
 */
package com.ces.xarch.plugins.authsystem.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import ces.sdk.system.bean.OpLogInfo;
import ces.sdk.system.bean.UserInfo;
import ces.sdk.system.dbbean.DbOpLogInfo;
import ces.sdk.system.exception.SystemFacadeException;
import ces.sdk.system.facade.OpLogInfoFacade;
import ces.sdk.system.facade.UserInfoFacade;

import com.ces.xarch.core.entity.BusinessLogEntity;
import com.ces.xarch.core.logger.LoggerService;
import com.ces.xarch.core.security.entity.SysUser;
import com.ces.xarch.plugins.authsystem.utils.FacadeUtil;

/**
 * 日志记录服务类.
 * <p>描述:负责操作日志的管理</p>
 * <p>Company:上海中信信息发展股份有限公司</p>
 * @author Reamy(杨木江 yangmujiang@sohu.com)
 * @date 2013-06-13  09:36:46
 * @version 1.0.2013.0613
 */
@Component("businessLoggerService")
public class XarchLoggerService implements LoggerService {
	/** 系统编号. */
	private String appKey = "";
	
	/* (non-Javadoc)
	 * @see com.ces.xarch.core.logger.LoggerService#save(com.ces.xarch.core.entity.BusinessLogEntity)
	 * @author Reamy(杨木江 yangmujiang@sohu.com)
	 * @date 2014-01-13 10:29:41
	 */
	@Override
	@Transactional
	public BusinessLogEntity save(BusinessLogEntity entity) {
		OpLogInfoFacade logInfoFacade = FacadeUtil.getLogInfoFacade();
		OpLogInfo opLogInfo = new DbOpLogInfo();
		opLogInfo.setType(processModel(entity.getModel()));
		opLogInfo.setUrl(entity.getUrl());
		opLogInfo.setLogDate(entity.getTime());
		opLogInfo.setIp(entity.getIp());
		opLogInfo.setMessage(entity.getOpTarget());
		opLogInfo.setOperate(entity.getAction());
		opLogInfo.setUserId(entity.getUserId()==null?"":entity.getUserId());
		opLogInfo.setStatus("成功".equals(entity.getResult())?1:0);
		opLogInfo.setAppKey(appKey);
		opLogInfo.setNote("");
		
		if (entity.getUserId() != null) {
			if (SecurityContextHolder.getContext().getAuthentication() == null) {
				UserInfoFacade userFacade = FacadeUtil.getUserInfoFacade();
				
				UserInfo userInfo = userFacade.findByID(opLogInfo.getUserId());
				
				if (userInfo != null) {
					opLogInfo.setUserName(userInfo.getName());
				}
				
				userInfo = null;
				
				userFacade = null;
			} else {
				opLogInfo.setUserName(((SysUser)SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getName());
			}
		}
		
		try {
			if ("登录系统".equals(entity.getModel()) && "登录".equals(entity.getAction())) {
				opLogInfo.setIp(entity.getIp());
				opLogInfo.setType("1");
				logInfoFacade.addSysLogInfo(opLogInfo);
			} else {
				logInfoFacade.addOpLogInfo(opLogInfo);
			}
		} catch (SystemFacadeException e) {
			throw new RuntimeException(e);
		}
		
		return entity;
	}
	
	/**
	 * 处理模块名称.
	 * 当模块名称长度大于200时，只截取Action名
	 * @author Reamy(杨木江 yangmujiang@sohu.com)
	 * @date 2014-01-13  12:23:20
	 */
	private String processModel(String model) {
		String type = model;
		
		if (type.length() > 200) {
			String[] temp = type.split("[/]");
			
			if (temp != null && temp.length > 0) {
				type = temp[temp.length -1];
			}
		}
		
		if (type.length() > 200) {
			type = type.substring(0,200);
		}
		
		return type;
	}

	/**
	 * 设置系统编号.
	 * @author Reamy(杨木江 yangmujiang@sohu.com)
	 * @date 2014-01-13  12:32:49
	 */
	@Autowired(required=false)
	@Qualifier("authSystem_appKey")
	public void setAppKey(String appKey) {
		this.appKey = appKey;
	}
}
