package com.ces.config.dhtmlx.service.appmanage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import ces.workflow.wapi.define.WFDefineException;

import com.ces.config.datamodel.message.MessageModel;
import com.ces.config.dhtmlx.dao.appmanage.WorkflowFormSettingDao;
import com.ces.config.dhtmlx.entity.appmanage.WorkflowActivity;
import com.ces.config.dhtmlx.entity.appmanage.WorkflowFormSetting;
import com.ces.config.dhtmlx.service.base.ConfigDefineDaoService;
import com.ces.config.utils.StringUtil;
import com.ces.config.utils.WorkflowUtil;
import com.ces.utils.BeanUtils;
import com.google.common.collect.Lists;

@Component
public class WorkflowFormSettingService extends ConfigDefineDaoService<WorkflowFormSetting, WorkflowFormSettingDao>{

    /*
     * (非 Javadoc)   
     * <p>标题: bindingDao</p>   
     * <p>描述: 注入自定义持久层(Dao)</p>   
     * @param entityClass   
     * @see com.ces.xarch.core.service.AbstractService#bindingDao(java.lang.Class)
     */
    @Autowired
    @Qualifier("workflowFormSettingDao")
    @Override
    protected void setDaoUnBinding(WorkflowFormSettingDao dao) {
        super.setDaoUnBinding(dao);
    }

    /**
     * qiucs 2013-9-6 
     * <p>标题: save</p>
     * <p>描述: 保存</p>
     * @param  workflowVersionId
     *                   --工作流版本ID
     * @param  activityId  
     *                   --流程节点ID
     * @param  rowsValue
     *                  --表单字段IDs(;分隔开)
     * @return void    返回类型   
     * @throws
     */
    @Transactional
    public MessageModel save(String workflowVersionId, String activityId, String rowsValue) {
        // 1. delete old configuration
        getDao().deleteByFk(workflowVersionId, activityId);
        // 2. save current configuration
        if (StringUtil.isNotEmpty(rowsValue)) {
        	List<WorkflowFormSetting> list = Lists.newArrayList();
            String[] rowArr = rowsValue.split(";");
            for (String row : rowArr) {
            	String[] cols = row.split(",");
                WorkflowFormSetting entity = new WorkflowFormSetting();
                entity.setWorkflowVersionId(workflowVersionId);
                entity.setActivityId(activityId);
                entity.setColumnId(cols[0]);
                entity.setDisabled(cols[1]);
                if (cols.length == 3) {
                	entity.setDefaultValue(cols[2]);
                }
                list.add(entity);
            }
            super.save(list);
        }
        // 清除缓存数据
        String key = WorkflowUtil.formSettingKey(workflowVersionId, activityId);
        WorkflowUtil.removeFormSetting(key);
        return MessageModel.trueInstance("OK");
    }

    /**
     * qiucs 2013-10-14 
     * <p>描述: 获取工作流程节点的表单配置栏位信息</p>
     * @return Object    返回类型   
     * @throws WFDefineException 
     */
    public Map<String, WorkflowFormSetting> getFormSettingMap(String workflowVersionId, String activityId) {
    	Map<String, WorkflowFormSetting> settingMap = new HashMap<String, WorkflowFormSetting>();
        if (WorkflowActivity.START_ACTIVITY.equals(activityId)) {
            activityId = getService(WorkflowVersionService.class).getStartActivityId(workflowVersionId);
        }
        List<WorkflowFormSetting> list = WorkflowUtil.getFormSetting(workflowVersionId, activityId);;
        WorkflowFormSetting formSetting = null;
        for (int i = 0, len = list.size(); i < len; i++) {
        	formSetting = list.get(i);
        	settingMap.put(formSetting.getColumnId(), formSetting);
        }
        return settingMap;
    }
    
    public List<WorkflowFormSetting> getFormSettingList(String workflowVersionId, String activityId) {
    	if (WorkflowActivity.START_ACTIVITY.equals(activityId)) {
            activityId = getService(WorkflowVersionService.class).getStartActivityId(workflowVersionId);
        }
        return getDao().getFormSettingList(workflowVersionId, activityId);
    }
    
    /**
     * 获取工作流表单设置
     * 
     * @param workflowVersionId 工作流ID
     * @param moduleId 模块ID
     * @return List<WorkflowFormSetting> 返回类型
     */
    public List<WorkflowFormSetting> findByWorkflowVersionId(String workflowVersionId) {
        return find("EQ_workflowVersionId=" + workflowVersionId);
    }
    
    /**
     * qiucs 2013-11-27 
     * <p>描述: 根据工作流版本ID删除配置</p>
     * @param  workflowVersionId    设定参数   
     */
    public void deleteByWorkflowVersionId(String workflowVersionId) {
        getDao().deleteByWorkflowVersionId(workflowVersionId);
    }
    
    /**
     * qiucs 2015-4-28 上午9:32:36
     * <p>描述: 工作流表单设置（从一个版本复制到另一个版本） </p>
     * @return void
     */
    @Transactional
    public void copyWorkflow(String fromVersionId, String toVersionId) {
    	List<WorkflowFormSetting> list = find("EQ_workflowVersionId=" + fromVersionId);
    	int i = 0, len = list.size();
    	WorkflowFormSetting entity = null;
    	List<WorkflowFormSetting> destList = new ArrayList<WorkflowFormSetting>();
    	for (; i < len; i++) {
    		entity = new WorkflowFormSetting();
    		BeanUtils.copy(list.get(i), entity);
    		entity.setId(null);
    		entity.setWorkflowVersionId(toVersionId);
    		destList.add(entity);
    	}
    	list = null;
    	if (!destList.isEmpty()) save(destList);
    	destList = null;
    }
}
