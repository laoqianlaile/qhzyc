package com.ces.config.dhtmlx.service.appmanage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.ces.config.datamodel.message.MessageModel;
import com.ces.config.dhtmlx.dao.appmanage.WorkflowButtonSettingDao;
import com.ces.config.dhtmlx.entity.appmanage.AppButton;
import com.ces.config.dhtmlx.entity.appmanage.WorkflowButtonSetting;
import com.ces.config.dhtmlx.service.base.ConfigDefineDaoService;
import com.ces.config.utils.StringUtil;
import com.ces.config.utils.WorkflowUtil;
import com.ces.utils.BeanUtils;
import com.google.common.collect.Lists;

@Component
public class WorkflowButtonSettingService extends ConfigDefineDaoService<WorkflowButtonSetting, WorkflowButtonSettingDao>{

    /**
     * qiucs 2014-12-23 下午6:01:40
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
    public MessageModel save(String workflowVersionId, String activityId, String buttonType, String rowsValue) {
        // 1. delete old configuration
        getDao().deleteByFk(workflowVersionId, activityId, buttonType);
        // 2. save current configuration
        String[] rowArr = null;
        if (StringUtil.isNotEmpty(rowsValue)) {
        	List<WorkflowButtonSetting> list = Lists.newArrayList();
            rowArr = rowsValue.split(";");
            for (String buttonCode : rowArr) {
            	WorkflowButtonSetting entity = new WorkflowButtonSetting();
                entity.setWorkflowVersionId(workflowVersionId);
                entity.setActivityId(activityId);
                entity.setButtonCode(buttonCode);
                entity.setButtonType(buttonType);
                list.add(entity);
            }
            super.save(list);
        }
        
        // 同步按钮设置缓存
        List<String> codeList = new ArrayList<String>();
        if (null != rowArr) codeList = Arrays.asList(rowArr);
        
        String key = WorkflowUtil.buttonSettingKey(workflowVersionId, activityId, buttonType);
        WorkflowUtil.addButtonSetting(key, codeList);
        
        return MessageModel.trueInstance("OK");
    }

    /**
     * qiucs 2014-12-23 下午6:01:40
     * <p>描述: 获取工作流程节点按钮配置</p>
     * @return Object    返回类型   
     */
    public List<String> getHiddenButtons(String workflowVersionId, String activityId, String buttonType) {
        return getDao().getHiddenButtons(workflowVersionId, activityId, buttonType);
    }
    
    /**
     * qiucs 2014-12-23 下午6:10:40
     * <p>描述: 根据工作流ID获取按钮配置 </p>
     * @return List<WorkflowButtonSetting>
     */
    public List<WorkflowButtonSetting> findByWorkflowVersionId(String workflowVersionId) {
        return find("EQ_workflowVersionId=" + workflowVersionId);
    }
    
    /**
     * qiucs 2014-12-23 下午6:09:48
     * <p>描述: 根据工作流版本ID删除配置</p>
     * @param  workflowVersionId    设定参数   
     */
    public void deleteByWorkflowVersionId(String workflowVersionId) {
        getDao().deleteByWorkflowVersionId(workflowVersionId);
        // 移除缓存中按钮设置
        WorkflowUtil.removeButtonSettingByWorkflowVersionId(workflowVersionId);
    }
    
    /**
     * qiucs 2014-12-23 下午6:11:40
     * <p>描述: 根据工作流节点删除配置</p>
     * @param  workflowVersionId    设定参数   
     */
    public void deleteByFk(String workflowVersionId, String activityId) {
        getDao().deleteByFk(workflowVersionId, activityId);
        // 移除缓存中按钮设置
        String 
        key = WorkflowUtil.buttonSettingKey(workflowVersionId, activityId, AppButton.BUTTON_FORM);
        WorkflowUtil.removeButtonSetting(key);
        
        key = WorkflowUtil.buttonSettingKey(workflowVersionId, activityId, AppButton.BUTTON_GRID);
        WorkflowUtil.removeButtonSetting(key);
    }
    
    /**
     * qiucs 2015-4-28 上午9:33:39
     * <p>描述: 工作流待办箱按钮设置（从一个版本复制到另一个版本） </p>
     * @return void
     */
    @Transactional
    public void copyWorkflow(String fromVersionId, String toVersionId) {
    	List<WorkflowButtonSetting> list = find("EQ_workflowVersionId=" + fromVersionId);
    	int i = 0, len = list.size();
    	WorkflowButtonSetting entity = null;
    	List<WorkflowButtonSetting> destList = new ArrayList<WorkflowButtonSetting>();
    	for (; i < len; i++) {
    		entity = new WorkflowButtonSetting();
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
