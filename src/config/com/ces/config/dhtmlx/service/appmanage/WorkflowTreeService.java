package com.ces.config.dhtmlx.service.appmanage;

import java.util.ArrayList;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.ces.config.datamodel.message.MessageModel;
import com.ces.config.dhtmlx.dao.appmanage.WorkflowTreeDao;
import com.ces.config.dhtmlx.entity.appmanage.WorkflowTree;
import com.ces.config.dhtmlx.service.base.ConfigDefineDaoService;
import com.ces.config.utils.StringUtil;

@Component
public class WorkflowTreeService extends ConfigDefineDaoService<WorkflowTree, WorkflowTreeDao> {
    
    private static Log log = LogFactory.getLog(WorkflowTreeService.class);

    /*
     * (非 Javadoc)   
     * <p>标题: bindingDao</p>   
     * <p>描述: 注入自定义持久层(Dao)</p>   
     * @param entityClass   
     * @see com.ces.xarch.core.service.AbstractService#bindingDao(java.lang.Class)
     */
    @Autowired
    @Qualifier("workflowTreeDao")
    @Override
    protected void setDaoUnBinding(WorkflowTreeDao dao) {
        super.setDaoUnBinding(dao);
    }
    
    @Transactional
    public WorkflowTree save(WorkflowTree entity) {
        if (StringUtil.isEmpty(entity.getId())) {
            Integer showOrder = getDao().getMaxShowOrderByParentId(entity.getParentId());
            if (null == showOrder) showOrder = Integer.parseInt("1");
            entity.setShowOrder(showOrder);
        }
        return getDao().save(entity);
    }

    /**
     * qiucs 2014-12-15 
     * <p>描述: 封装为树节点json</p>
     * @param treeId
     * @param idPre  ID前缀符
     * @return
     */
    public List<Map<String, Object>> getTreeNode(String id, String idPre) {
    	String filters = "EQ_parentId=" + id;
        List<WorkflowTree> list = find(filters, new Sort("showOrder"));
        List<Map<String, Object>> data = new ArrayList<Map<String, Object>>();
        if (StringUtil.isEmpty(idPre)) idPre = "";
        for (WorkflowTree entity : list) {
            data.add(beanToTreeNode(entity, idPre));
        }
        //
        data.addAll(getService(WorkflowDefineService.class).getTreeNode(id, "-CF"));
        return data;
    }
    
    /**
     * qiucs 2014-11-18 
     * <p>描述: 实体bean转为树节点</p>
     * @return Map<String,Object>    返回类型   
     * @throws
     */
    private Map<String, Object> beanToTreeNode(WorkflowTree entity, String idPre) {
        Map<String, Object> data = new HashMap<String, Object>();
        List<Map<String, String>> userdata = new ArrayList<Map<String, String>>();
        Map<String, String> item = new HashMap<String, String>();
        item.put("name", "type");
        item.put("content", "0");
        userdata.add(item);
        
        data.put("id", idPre.concat(entity.getId()));
        data.put("text", entity.getName());
        data.put("child", Boolean.TRUE);
        data.put("userdata", userdata);
        return data;
    }

    /**
     * qiucs 2014-12-16 
     * <p>描述: 校验该节点下是否有子节点</p>
     * @return MessageModel    返回类型   
     * @throws
     */
	public Object checkDelete(String id) {
		long cnt = getDao().countByParentId(id);
		if (cnt > 0) return MessageModel.falseInstance("OK");
		cnt = getService(WorkflowDefineService.class).count("EQ_workflowTreeId=" + id);
		if (cnt > 0) return MessageModel.falseInstance("OK");
		return MessageModel.trueInstance("OK");
	}
    
	/**
     * wl 2015-3-17 
     * <p>描述: 根据父ID和名称获取节点</p>
     * @param parentId
     * @param name
     * @return
     */
    public WorkflowTree getByParentIdAndName(String parentId, String name) {
        return getDao().getByParentIdAndName(parentId, name);
    }
}
