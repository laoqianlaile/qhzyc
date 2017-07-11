package com.ces.config.dhtmlx.service.appmanage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import com.ces.config.dhtmlx.dao.appmanage.TableClassificationDao;
import com.ces.config.dhtmlx.entity.appmanage.TableClassification;
import com.ces.config.dhtmlx.service.base.ConfigDefineDaoService;
import com.ces.config.utils.StringUtil;

@Component
public class TableClassificationService extends ConfigDefineDaoService<TableClassification, TableClassificationDao> {
    
    /*
     * (非 Javadoc)   
     * <p>标题: bindingDao</p>   
     * <p>描述: 注入自定义持久层(Dao)</p>   
     * @param entityClass   
     * @see com.ces.xarch.core.service.AbstractService#bindingDao(java.lang.Class)
     */
    @Autowired
    @Qualifier("tableClassificationDao")
    @Override
    protected void setDaoUnBinding(TableClassificationDao dao) {
        super.setDaoUnBinding(dao);
    }

    /**
     * qiucs 2014-11-18 
     * <p>描述: 获取物理表分类节点</p>
     * @throws
     */
    public Object getTreeNode() {
        Sort sort = new Sort("showOrder");
        List<TableClassification> list = getDao().findAll(sort);
        List<Map<String, Object>> data = new ArrayList<Map<String, Object>>();
        for (TableClassification entity : list) {
        	if (StringUtil.isEmpty(entity.getPrefix())) continue;
            data.add(beanToTreeNode(entity));
        }
        return data;
    }
    
    /**
     * qiujinwei 2015-08-24 
     * <p>描述: 获取展开的物理表分类节点</p>
     * @throws
     */
    public Object getOpenedTreeNode() {
        Sort sort = new Sort("showOrder");
        List<TableClassification> list = getDao().findAll(sort);
        List<Map<String, Object>> data = new ArrayList<Map<String, Object>>();
        for (TableClassification entity : list) {
        	if (StringUtil.isEmpty(entity.getPrefix())) continue;
            data.add(beanToOpenedTreeNode(entity));
        }
        return data;
    }
    
    /**
     * qiujinwei 2014-12-30 
     * <p>描述: 获取视图分类节点</p>
     * @return Object    返回类型   
     */
    public Object getTreeNodeOfView() {
        Sort sort = new Sort("showOrder");
        List<TableClassification> list = getDao().findAll(sort);
        List<Map<String, Object>> data = new ArrayList<Map<String, Object>>();
        for (TableClassification entity : list) {
        	if (StringUtil.isEmpty(entity.getPrefix())){
        		data.add(beanToTreeNode(entity));
        	}
        }
        return data;
    }
    
    /**
     * qiucs 2014-11-18 
     * <p>描述: 实体bean转为树节点</p>
     * @return Map<String,Object>    返回类型   
     * @throws
     */
    private Map<String, Object> beanToTreeNode(TableClassification entity) {
        Map<String, Object> data = new HashMap<String, Object>();
        
        List<Map<String, String>> userdata = new ArrayList<Map<String, String>>();
        Map<String, String> item = new HashMap<String, String>();
        item.put("name", "type");
        item.put("content", "0");
        userdata.add(item);
        item = new HashMap<String, String>();
        item.put("name", "classification");
        if (entity.getPrefix() == null) {//视图分类
        	item.put("content", "V");
		} else {
			item.put("content", entity.getCode());
		}
        userdata.add(item);
        
        data.put("id", "-".concat(entity.getCode()));
        data.put("text", entity.getName());
        data.put("prefix", entity.getPrefix());
        data.put("type", "0");
        data.put("child", Boolean.TRUE);
        data.put("userdata", userdata);
        return data;
    }
    
    /**
     * qiujinwei 2015-08-24 
     * <p>描述: 实体bean转为树节点</p>
     * @return Map<String,Object>    返回类型   
     * @throws
     */
    private Map<String, Object> beanToOpenedTreeNode(TableClassification entity) {
        Map<String, Object> data = new HashMap<String, Object>();
        
        List<Map<String, String>> userdata = new ArrayList<Map<String, String>>();
        Map<String, String> item = new HashMap<String, String>();
        item.put("name", "type");
        item.put("content", "0");
        userdata.add(item);
        item = new HashMap<String, String>();
        item.put("name", "classification");
        if (entity.getPrefix() == null) {//视图分类
        	item.put("content", "V");
		} else {
			item.put("content", entity.getCode());
		}
        userdata.add(item);
        
        data.put("id", "-".concat(entity.getCode()));
        data.put("text", entity.getName());
        data.put("prefix", entity.getPrefix());
        data.put("type", "0");
        data.put("child", Boolean.TRUE);
        data.put("open", Boolean.TRUE);
        data.put("userdata", userdata);
        return data;
    }

}
