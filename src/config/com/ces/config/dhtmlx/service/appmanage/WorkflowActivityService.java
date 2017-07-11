package com.ces.config.dhtmlx.service.appmanage;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.ces.config.dhtmlx.dao.appmanage.WorkflowActivityDao;
import com.ces.config.dhtmlx.entity.appmanage.WorkflowActivity;
import com.ces.config.dhtmlx.service.base.ConfigDefineDaoService;

@Component
public class WorkflowActivityService extends ConfigDefineDaoService<WorkflowActivity, WorkflowActivityDao>{

    /*
     * (非 Javadoc)   
     * <p>标题: bindingDao</p>   
     * <p>描述: 注入自定义持久层(Dao)</p>   
     * @param entityClass   
     * @see com.ces.xarch.core.service.AbstractService#bindingDao(java.lang.Class)
     */
    @Autowired
    @Qualifier("workflowActivityDao")
    @Override
    protected void setDaoUnBinding(WorkflowActivityDao dao) {
        super.setDaoUnBinding(dao);
    }
    
    /**
     * qiucs 2013-9-4 
     * <p>标题: save</p>
     * <p>描述: 保存配置文件中的流程节点</p>
     * @param  workflowId
     * @param  activities    设定参数   
     * @return void    返回类型   
     * @throws
     */
    @Transactional
    public List<WorkflowActivity> save(String packageId, String packageVersion, String processId, List<Map<String, String>> activityList) {
        // 1. 判断是否存在流程节点
        if (null == activityList || activityList.isEmpty()) {
            return null;
        }
        // 2. 保存当前配置文件的流程节点
        Map<String, String> activity = null;
        WorkflowActivity entity = null;
        getDao().deleteByFk(packageId, packageVersion, processId);
        List<WorkflowActivity> list = new ArrayList<WorkflowActivity>();
        for (int i = 0, len = activityList.size(); i < len; i++) {
            entity = new WorkflowActivity();
            activity = activityList.get(i);
            entity.setPackageId(packageId);
            entity.setPackageVersion(packageVersion);
            entity.setProcessId(processId);
            entity.setActivityId(activity.get("id"));
            entity.setActivityName(activity.get("name"));
            entity.setActivityType(activity.get("type"));
            entity.setShowOrder(i);
            list.add(entity);
        }
        save(list);
        
        return list;
    }


    /**
     * qiucs 2015-4-13 下午6:13:36
     * <p>描述: 根据流程版本号删除节点</p>
     * @param  workflowId    设定参数   
     */
    @Transactional
    public void deleteByFk(String packageId, String packageVersion, String processId) {
    	getDao().deleteByFk(packageId, packageVersion, processId);
    }

    /**
     * qiucs 2015-4-13 下午6:21:54
     * <p>描述: 根据流程删除节点 </p>
     * @return void
     */
    @Transactional
    public void deleteByFk(String packageId, String processId) {
    	getDao().deleteByFk(packageId, processId);
    }
    
    /**
     * qiucs 2015-4-14 上午9:55:17
     * <p>描述: 获取开始节点 </p>
     * @return WorkflowActivity
     */
    public WorkflowActivity getStartActivity(String packageId, String packageVersion, String processId) {
    	return getDao().getStartActivity(packageId, packageVersion, processId);
    }
}
