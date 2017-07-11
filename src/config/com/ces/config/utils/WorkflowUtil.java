package com.ces.config.utils;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.ehcache.Cache;

import org.apache.struts2.ServletActionContext;

import com.ces.coflow.web.util.WebCoflowUitl;
import com.ces.config.dhtmlx.entity.appmanage.WorkflowDefine;
import com.ces.config.dhtmlx.entity.appmanage.WorkflowFormSetting;
import com.ces.config.dhtmlx.entity.appmanage.WorkflowVersion;
import com.ces.config.dhtmlx.service.appmanage.WorkflowButtonSettingService;
import com.ces.config.dhtmlx.service.appmanage.WorkflowDefineService;
import com.ces.config.dhtmlx.service.appmanage.WorkflowFormSettingService;
import com.ces.config.dhtmlx.service.appmanage.WorkflowVersionService;
import com.ces.xarch.core.web.listener.XarchListener;

public class WorkflowUtil {
    
    private static Map<String, String> boxMap = null;

    private static Map<String, String> opMap = null;
    // 工作流定义缓存KEY
    private static final String EH_WORKFLOW_ENTITY = "WORKFLOW_ENTITY";
    // 工作流版本缓存KEY
    private static final String EH_VERSION_ENTITY  = "VERSION_ENTITY";
    // 按钮设置缓存KEY
    private static final String EH_WORKFLOW_BUTTON = "WORKFLOW_BUTTON";
    // 表单设置缓存KEY
    private static final String EH_WORKFLOW_FORM = "WORKFLOW_FORM";
    
    /** 流程实例字段英文名称.*/
    public static final String C_PROCESS_INSTANCE_ID = "PROCESS_INSTANCE_ID";
    /** 流程创建用户ID字段英文名称.*/
    public static final String C_REGISTER_USER_ID    = "REGISTER_USER_ID";
    /** 所属流程编码字段英文名称.*/
    public static final String C_BELONG_WORKFLOW_CODE = "BELONG_WORKFLOW_CODE";
    /** 工作项状态对应的编码CODE值.*/
    public static final String WORKITEMSTATUS_CODE   = "WORKITEM_STATUS";
    
    /** 列名.*/
    public static interface Alias {
        public String processInstanceId = "PROCESS_INSTANCE_ID_"; /* 流程实例ID.*/
        public String processId      = "WI_PROCESS_ID_"; /* 流程实例ID.*/
        public String workitemId     = "WI_ID_";     /* 工作项ID.*/
        public String workitemStatus = "WI_STATUS_"; /* 工作项状态.*/
        public String workitemActivityId= "WI_ACTIVITY_ID_";/* 节点ID.*/
        public String workitemOwnerId   = "WI_OWNER_ID_";   /* 工作项所属用户ID.*/
        public String workitemStatusKey = "WI_STATUS_KEY_"; /* 工作项状态.*/
    }
    
    /** 工作箱.*/
    public static interface Box {
        public String applyfor = "applyfor"; /* 申请箱.*/
        public String todo     = "todo";     /* 待办箱.*/
        public String hasdone  = "hasdone";  /* 已办箱.*/
        public String complete = "complete"; /* 办结箱.*/
        public String toread   = "toread";   /* 待阅箱.*/
        public String hasread  = "hasread";  /* 待阅箱.*/
    }
    /** 工作流动作.*/
    public static interface Op {
        public String start   = "start";   /* 启动.*/
        public String complete= "complete";/* 提交.*/
        public String checkout= "checkout";/* 签收.*/
        public String untread = "untread"; /* 退回.*/
        public String recall  = "recall";  /* 撤回.*/
        public String reassign= "reassign";/* 转办.*/
        public String deliver = "deliver"; /* 传阅.*/
        public String track   = "track";   /* 跟踪.*/
        public String hasread = "hasread"; /* 阅毕.*/
        public String hasten  = "hasten";  /* 催办.*/
        public String suspend  = "suspend";  /* 中止（删除）.*/
        public String termination= "termination";  /* 终止.*/
    }
    /** 工作流动作名称.*/
    public static interface Name {
        public String start   = "启动"; /* 启动.*/
        public String complete= "提交"; /* 提交.*/
        public String checkout= "签收"; /* 签收.*/
        public String untread = "退回"; /* 退回.*/
        public String recall  = "撤回"; /* 撤回.*/
        public String reassign= "转办"; /* 转办.*/
        public String deliver = "传阅"; /* 传阅.*/
        public String track   = "跟踪"; /* 跟踪.*/
        public String hasread = "阅毕"; /* 阅毕.*/
        public String hasten  = "催办"; /* 催办.*/
        public String suspend  = "中止（删除）";  /* 中止（删除）.*/
        public String termination = "终止";  /* 终止.*/
    }
    /**
     * qiucs 2013-10-8 
     * <p>描述: </p>
     * @return Map<String,String>    返回类型   
     * @throws
     */
    public static Map<String, String> getBoxMap() {
        if (null == boxMap) {
            boxMap = new HashMap<String, String>();
            boxMap.put(Box.applyfor, "申请箱");
            boxMap.put(Box.todo, "待办箱");
            boxMap.put(Box.hasdone, "已办箱");
            boxMap.put(Box.complete, "办结箱");
            boxMap.put(Box.toread, "待阅箱");
            boxMap.put(Box.hasread, "已阅箱");
        }
            
        return boxMap;    
    }
    
    public static Map<String, String> getOpMap () {
        if (null == opMap) {
            opMap = new HashMap<String, String>();
            opMap.put(Op.start, Name.start);
            opMap.put(Op.complete, Name.complete);
            opMap.put(Op.checkout, Name.checkout);
            opMap.put(Op.untread, Name.untread);
            opMap.put(Op.recall, Name.recall);
            opMap.put(Op.reassign, Name.reassign);
            opMap.put(Op.deliver, Name.deliver);
            opMap.put(Op.track, Name.track);
            opMap.put(Op.hasread, Name.hasread);
        }
        return opMap;
    }
    
    /**
     * qiucs 2013-10-8 
     * <p>描述: 工作箱名称</p>
     * @param  boxKey
     * @return String    返回类型   
     * @throws
     */
    public static String getBoxName(String boxKey) {
        return getBoxMap().get(boxKey);
    }
    
    public static Integer getBoxIndex(String boxKey) {
        if (Box.applyfor.equals(boxKey)) {
            return Integer.parseInt("1");
        }
        if (Box.todo.equals(boxKey)) {
            return Integer.parseInt("2");
        }
        if (Box.hasdone.equals(boxKey)) {
            return Integer.parseInt("3");
        }
        if (Box.complete.equals(boxKey)) {
            return Integer.parseInt("4");
        }
        if (Box.toread.equals(boxKey)) {
            return Integer.parseInt("5");
        }
        return null;
    }
    
    /**
     * qiucs 2015-3-8 上午11:03:21
     * <p>描述: 根据工作流ID获取业务表ID </p>
     * @return String
     */
    public static String getBusinessTableId(String id) {
    	WorkflowDefine entity = getWorkflowEntity(id);
    	if (null == entity) return StringUtil.EMPTY;
    	return entity.getBusinessTableId();
    }
    
    /**
     * qiucs 2014-12-16 
     * <p>描述: 获取工作流程编码</p>
     */
    public static String getWorkflowCode(String id) {
    	WorkflowDefine entity = getWorkflowEntity(id);
    	if (null == entity) {
    		return null;
    	}
        return entity.getWorkflowCode();
    }
    
    /**
     * qiucs 2015-7-19 下午3:06:43
     * <p>描述: 获取工作流程编码 </p>
     * @return String
     */
    public static String getWorkflowCodeByPackageId(String packageId) {
    	return packageId.replaceFirst("PACKAGE_", "");
    }
    
    /**
     * qiucs 2014-12-16 
     * <p>描述: 根据ID获取工作流程包ID</p>
     */
    public static String getPackageIdById(String id) {
    	WorkflowDefine entity = getWorkflowEntity(id);
    	if (null == entity) return null;
        return getPackageIdByCode(entity.getWorkflowCode());
    }
    
    /**
     * qiucs 2014-12-16 
     * <p>描述: 根据ID获取工作流程流程ID</p>
     */
    public static String getProcessIdById(String id) {
    	WorkflowDefine entity = getWorkflowEntity(id);
    	if (null == entity) return null;
        return getProcessIdByCode(entity.getWorkflowCode());
    }
    
    /**
     * qiucs 2014-12-16 
     * <p>描述: 根据编码获取工作流程包ID</p>
     */
    public static String getPackageIdByCode(String code) {
        return "PACKAGE_".concat(code);
    }
    
    /**
     * qiucs 2014-12-16 
     * <p>描述: 根据编码获取工作流程流程ID</p>
     */
    public static String getProcessIdByCode(String code) {
        return "PROCESS_".concat(code);
    }
    
    /**
     * qiucs 2014-12-16 
     * <p>描述: 从缓存中根据ID获取工作流定义实体对象</p>
     */
    public static WorkflowDefine getWorkflowEntity(String id) {
    	WorkflowDefine entity = null;
    	Object obj = EhcacheUtil.getCache(EH_WORKFLOW_ENTITY, id);
    	if (null == obj) {
    		entity = XarchListener.getBean(WorkflowDefineService.class).getByID(id);
    		if (null != entity) addWorkflowEntity(entity);
    	} else {
    		entity = (WorkflowDefine) obj;
    	}
    	return entity;
    }
    
    /**
     * qiucs 2014-12-16 
     * <p>描述: 向缓存中添加工作流定义实体对象</p>
     */
    public static void addWorkflowEntity(WorkflowDefine entity) {
    	EhcacheUtil.setCache(EH_WORKFLOW_ENTITY, entity.getId(), entity);
    }
    
    /**
     * qiucs 2014-12-16 
     * <p>描述: 从缓存中删除工作流定义实体对象</p>
     */
    public static void removeWorkflowEntity(String id) {
    	EhcacheUtil.removeCache(EH_WORKFLOW_ENTITY, id);
    }
    
    public static String getDocumentTableName(String code) {
    	return "T_CF_DOCUMENT_".concat(code);
    }
    
    public static String getAssistTableName(String code) {
    	return "T_CF_ASSIST_".concat(code);
    }
    
    public static String getConfirmTableName(String code) {
    	return "T_CF_CONFIRM_".concat(code);
    }
    
    /**
     * qiucs 2014-12-17 下午9:17:01
     * <p>描述: 状态：UNDEFINED-未定义，LOCAL-本地，UNREGIST-未注册，RUNNING-运行中，UPDATED-已修改并运行，STOPPED-已停止，ERROR-出错 </p>
     * @return String
     */
    public static String getStatusText(String status) {
    	return WebCoflowUitl.transStatus2Chinese(status.toLowerCase());
    }
    
    /**
     * qiucs 2015-3-25 上午11:50:17
     * <p>描述: 获取xpdl文件路径（服务） </p>
     * @return String
     */
    public static String getServerXpdlFilePath(String fileName) {
    	String filePath = "";
    	String xpdlFilesPath = ServletActionContext.getServletContext().getRealPath("/WEB-INF/conf/");
    	File xpdlFilesPathFile = new File(xpdlFilesPath);
    	if(!xpdlFilesPathFile.exists()) xpdlFilesPathFile.mkdirs();
    	filePath = xpdlFilesPath + File.separator + fileName;
        return filePath;
    }
    
    /**
     * qiucs 2015-3-25 上午11:50:17
     * <p>描述: 获取xpdl文件路径（本地） </p>
     * @return String
     */
    public static String getLocalXpdlFilePath(String fileName) {
    	String filePath = "";
    	String tempxpdlPath = ServletActionContext.getServletContext().getRealPath("/WEB-INF/conf/tempxpdl/");
    	File tempxpdlPathFile = new File(tempxpdlPath);
    	if(!tempxpdlPathFile.exists()) tempxpdlPathFile.mkdirs();
    	filePath = tempxpdlPath + File.separator + fileName;
        return filePath;
    }
    
    public static String getFileName(String code, String version) {
    	return code.concat("-").concat(version).concat(".xpdl");
    }
    
    /**
     * qiucs 2015-1-6 下午11:32:06
     * <p>描述: 获取公式定义文件路径 </p>
     * @return String
     */
    public static String getFormulaFilePath() {
    	String filePath = ServletActionContext.getServletContext().getRealPath("/WEB-INF/conf/");
    	File file = new File(filePath);
    	if(!file.exists()) file.mkdirs();
    	return (filePath + File.separator + "Formula.xml");
    }

	/**
	 * qiucs 2014-12-26 下午1:09:25
	 * <p>描述: 工作流业务表对应的视图名  </p>
	 * @return String
	 */
	public static String getViewName(String code) {
		return "V_CF_".concat(code);
	}
	
	/**
	 * qiucs 2014-12-27 下午4:01:09
	 * <p>描述: 获取视图ID </p>
	 * @return String
	 */
	public static String getViewId(String workflowId) {
		String code = getWorkflowEntity(workflowId).getWorkflowCode();
		String viewName = getViewName(code);
		return TableUtil.getTableId(viewName);
	}
	
	/**
	 * qiucs 2014-12-29 下午12:41:18
	 * <p>描述: 获取工作箱名称 </p>
	 * @return String
	 */
	public static String getBoxName(WorkflowDefine entity, String box) {
		if (Box.applyfor.equals(box)) return entity.getNameApplyfor();
		if (Box.todo.equals(box)) return entity.getNameTodo();
		if (Box.hasdone.equals(box)) return entity.getNameHasdone();
		if (Box.complete.equals(box)) return entity.getNameComplete();
		if (Box.toread.equals(box)) return entity.getNameToread();
		if (Box.hasread.equals(box)) return entity.getNameHasread();
		
		return null;
	}
	
	/**
	 * qiucs 2015-1-6 下午12:46:23
	 * <p>描述: 判断是否为审批意见字段 </p>
	 * @return boolean
	 */
	public static boolean isOpinionColumnName(String col) {
		if ("assistOpinion".equals(col) || "confirmOpinion".equals(col)) return true;
		return false;
	}
	
	/**
	 * qiucs 2015-1-7 下午4:54:02
	 * <p>描述: 获取移除待办箱按钮设置 </p>
	 * @return List<String>
	 */
	public static List<String> getButtonSetting(String workflowVersionId, String activityId, String type) {
		List<String> list = null;
		String key = buttonSettingKey(workflowVersionId, activityId, type);
		Object obj = EhcacheUtil.getCache(EH_WORKFLOW_BUTTON, key);
    	if (null == obj) {
    		list = XarchListener.getBean(WorkflowButtonSettingService.class).getHiddenButtons(workflowVersionId, activityId, type);
    		//if (CollectionUtils.isNotEmpty(list)) 
    		addButtonSetting(key, list);
    	} else {
    		list = (List<String>) obj;
    	}
    	
    	return list;
	}
	
	/**
	 * qiucs 2015-1-7 下午4:53:40
	 * <p>描述: 添加待办箱按钮设置 </p>
	 * @return void
	 */
	public static void addButtonSetting(String key, List<String> list) {
		EhcacheUtil.setCache(EH_WORKFLOW_BUTTON, key, list);
	}
	
	/**
	 * qiucs 2015-1-7 下午4:53:20
	 * <p>描述: 移除待办箱按钮设置 </p>
	 * @return void
	 */
	public static void removeButtonSetting(String key) {
		EhcacheUtil.removeCache(EH_WORKFLOW_BUTTON, key);
	}
	
	/**
	 * qiucs 2015-1-7 下午4:52:49
	 * <p>描述: 待办箱按钮设置缓存KEY </p>
	 * @return String
	 */
	public static String buttonSettingKey(String workflowVersionId, String activityId, String type) {
		return new StringBuilder().append(workflowVersionId)
				.append("-")
				.append(activityId)
				.append("-")
				.append(type)
				.toString();
	}
	
	/**
	 * qiucs 2015-1-7 下午5:03:20
	 * <p>描述: 移除待办箱按钮设置 </p>
	 * @return void
	 */
	public static void removeButtonSettingByWorkflowVersionId(String workflowVersionId) {
		Cache cache = EhcacheUtil.getCache(EH_WORKFLOW_BUTTON);
        @SuppressWarnings("unchecked")
        List<String> cacheKeys = cache.getKeysNoDuplicateCheck();
        if (null == cacheKeys) return;
        int len = cacheKeys.size();
        String key = null;
        for (int i = 0; i < len; i++) {
        	key = cacheKeys.get(i);
        	if (key.startsWith(workflowVersionId)) {
        		EhcacheUtil.removeCache(EH_WORKFLOW_BUTTON, key);
        	}
        }
	}
	
	/**
	 * qiucs 2015-1-7 下午5:21:44
	 * <p>描述: 获取流程版本缓存信息 </p>
	 * @return WorkflowVersion
	 */
	public static WorkflowVersion getWorkflowVersion(String id) {
		WorkflowVersion entity = null;
    	Object obj = EhcacheUtil.getCache(EH_VERSION_ENTITY, id);
    	if (null == obj) {
    		entity = XarchListener.getBean(WorkflowVersionService.class).getByID(id);
    		if (null != entity) addWorkflowVersion(entity);
    	} else {
    		entity = (WorkflowVersion) obj;
    	}
    	return entity;
	}
	
	/**
	 * qiucs 2015-1-7 下午5:21:24
	 * <p>描述: 获取流程版本缓存信息  </p>
	 * @return WorkflowVersion
	 */
	public static WorkflowVersion getWorkflowVersion(String workflowId, String version) {
		WorkflowVersion entity = null;
		String key = workflowVersionKey(workflowId, version);
    	Object obj = EhcacheUtil.getCache(EH_VERSION_ENTITY, key);
    	if (null == obj) {
    		String filters = "EQ_workflowId=" + workflowId + ";EQ_version=" + version;
    		entity = XarchListener.getBean(WorkflowVersionService.class).findOne(filters);
    		if (null != entity) addWorkflowVersion(entity);
    	} else {
    		entity = (WorkflowVersion) obj;
    	}
    	return entity;
	}

	/**
	 * qiucs 2015-1-7 下午5:09:35
	 * <p>描述: 添加流程版本缓存信息  </p>
	 * @return void
	 */
	public static void addWorkflowVersion(WorkflowVersion entity) {
		// 用版本ID缓存
		EhcacheUtil.setCache(EH_VERSION_ENTITY, entity.getId(), entity);
		// 用流程ID+版本号缓存
		String key = workflowVersionKey(entity.getWorkflowId(), entity.getVersion());
		EhcacheUtil.setCache(EH_VERSION_ENTITY, key, entity);
	}
	
	/**
	 * qiucs 2015-1-7 下午5:20:02
	 * <p>描述: 移除流程版本缓存信息  </p>
	 * @return void
	 */
	public static void removeWorkflowVersion(WorkflowVersion entity) {
		// 用版本ID缓存
		EhcacheUtil.removeCache(EH_VERSION_ENTITY, entity.getId());
		// 用流程ID+版本号缓存
		String key = workflowVersionKey(entity.getWorkflowId(), entity.getVersion());
		EhcacheUtil.removeCache(EH_VERSION_ENTITY, key);
	}
	
	/**
	 * qiucs 2015-1-7 下午5:20:02
	 * <p>描述: 移除流程版本缓存信息  </p>
	 * @return void
	 */
	public static void removeWorkflowVersion(String id) {
		WorkflowVersion entity = getWorkflowVersion(id);
		if (null == entity) return;
		// 用版本ID缓存
		EhcacheUtil.removeCache(EH_VERSION_ENTITY, entity.getId());
		// 用流程ID+版本号缓存
		String key = workflowVersionKey(entity.getWorkflowId(), entity.getVersion());
		EhcacheUtil.removeCache(EH_VERSION_ENTITY, key);
	}
	
	/**
	 * qiucs 2015-1-7 下午5:19:37
	 * <p>描述: 流程ID+流程版本组成的KEY </p>
	 * @return String
	 */
	public static String workflowVersionKey(String workflowId, String version) {
		return new StringBuilder().append(workflowId)
				.append("-").append(version)
				.toString();
	}
	
	/**
	 * qiucs 2015-1-14 下午2:17:35
	 * <p>描述: 待办箱表单缓存KEY </p>
	 * @return String
	 */
	public static String formSettingKey(String workflowVersionId, String activityId) {
		return new StringBuilder().append(workflowVersionId)
				.append("-").append(activityId)
				.toString();
	}
	
	/**
	 * qiucs 2015-1-14 下午2:47:13
	 * <p>描述: 获取待办箱表单配置 </p>
	 * @return List<String>
	 */
	public static List<WorkflowFormSetting> getFormSetting(String workflowVersionId, String activityId) {
		String key = formSettingKey(workflowVersionId, activityId);
		List<WorkflowFormSetting> list = null;

		Object obj = EhcacheUtil.getCache(EH_WORKFLOW_FORM, key);
		if (null == obj) {
			list = XarchListener.getBean(WorkflowFormSettingService.class).getFormSettingList(workflowVersionId, activityId);
			if (null == list) list = new ArrayList<WorkflowFormSetting>();
			addFormSetting(key, list);
		} else {
			list = (List<WorkflowFormSetting>)obj;
		}
		
		return list;
	}

	/**
	 * qiucs 2015-1-7 下午6:02:54
	 * <p>描述: TODO(这里用一句话描述这个方法的作用) </p>
	 * @return void
	 */
	public static void addFormSetting(String key, List<WorkflowFormSetting> list) {
		EhcacheUtil.setCache(EH_WORKFLOW_FORM, key, list);
	}
	
	/**
	 * qiucs 2015-1-14 上午9:59:25
	 * <p>描述: 移除表单设置 </p>
	 * @return void
	 */
	public static void removeFormSetting(String key) {
		EhcacheUtil.removeCache(EH_WORKFLOW_FORM, key);
	}

	/**
	 * qiucs 2015-8-25 下午2:46:18
	 * <p>描述: 判断是否为工作箱 </p>
	 * @return boolean
	 */
	public static boolean isBox(String box) {
		return getBoxMap().containsKey(box);
	}
}
