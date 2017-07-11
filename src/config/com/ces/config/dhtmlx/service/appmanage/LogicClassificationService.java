package com.ces.config.dhtmlx.service.appmanage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import com.ces.config.dhtmlx.dao.appmanage.LogicClassificationDao;
import com.ces.config.dhtmlx.entity.appmanage.LogicClassification;
import com.ces.config.dhtmlx.service.base.ConfigDefineDaoService;

@Component
public class LogicClassificationService extends ConfigDefineDaoService<LogicClassification, LogicClassificationDao> {
	
	/*
     * (非 Javadoc)   
     * <p>标题: bindingDao</p>   
     * <p>描述: 注入自定义持久层(Dao)</p>   
     * @param entityClass   
     * @see com.ces.xarch.core.service.AbstractService#bindingDao(java.lang.Class)
     */
    @Autowired
    @Qualifier("logicClassificationDao")
    @Override
    protected void setDaoUnBinding(LogicClassificationDao dao) {
        super.setDaoUnBinding(dao);
    }

    /**
     * qiujinwei 2015-01-28
     * <p>描述: 获取物理表分类节点</p>
     * @throws
     */
    public Object getTreeNode() { 
        Sort sort = new Sort("showOrder");
        List<LogicClassification> list = getDao().findAll(sort);
        List<Map<String, Object>> data = new ArrayList<Map<String, Object>>();
        for (LogicClassification entity : list) {
            data.add(beanToTreeNode(entity));
        }
        return data;
    }
    
    /**
     * qiujinwei 2015-01-28 
     * <p>描述: 实体bean转为树节点</p>
     * @return Map<String,Object>    返回类型   
     * @throws
     */
    private Map<String, Object> beanToTreeNode(LogicClassification entity) {
        Map<String, Object> data = new HashMap<String, Object>();
        
        List<Map<String, String>> userdata = new ArrayList<Map<String, String>>();
        Map<String, String> item = new HashMap<String, String>();
        item.put("name", "type");
        item.put("content", "6");
        userdata.add(item);
        item = new HashMap<String, String>();
        item.put("name", "classification");
		item.put("content", entity.getCode());
        userdata.add(item);
        
        data.put("id", "-".concat(entity.getCode()));
        data.put("text", entity.getName());
        data.put("type", "0");
        data.put("child", Boolean.TRUE);
        data.put("userdata", userdata);
        return data;
    }


}
