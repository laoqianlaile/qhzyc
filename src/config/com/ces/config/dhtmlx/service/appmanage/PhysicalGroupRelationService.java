package com.ces.config.dhtmlx.service.appmanage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.ces.config.dhtmlx.dao.appmanage.PhysicalGroupRelationDao;
import com.ces.config.dhtmlx.entity.appmanage.LogicGroupRelation;
import com.ces.config.dhtmlx.entity.appmanage.PhysicalGroupRelation;
import com.ces.config.dhtmlx.entity.appmanage.PhysicalTableDefine;
import com.ces.config.dhtmlx.service.base.ConfigDefineDaoService;
import com.ces.config.utils.GroupUtil;
import com.ces.config.utils.StringUtil;
import com.ces.config.utils.TableUtil;

/**
 * 物理表组与物理表关系表处理层
 * 
 * @author qiujinwei
 */
@Component
public class PhysicalGroupRelationService extends ConfigDefineDaoService<PhysicalGroupRelation, PhysicalGroupRelationDao> {
	
	/*
     * (非 Javadoc)
     * <p>标题: bindingDao</p>
     * <p>描述: 注入自定义持久层(Dao)</p>
     * @param entityClass
     * @see com.ces.xarch.core.service.AbstractService#bindingDao(java.lang.Class)
     */
    @Autowired
    @Qualifier("physicalGroupRelationDao")
    @Override
    protected void setDaoUnBinding(PhysicalGroupRelationDao dao) {
        super.setDaoUnBinding(dao);
    }
    
    /**
     * qiucs 2014-11-28 
     * <p>描述: 获取物理表组下的物理表</p>
     * @param  groupId
     * @return List<PhysicalGroupRelation>    返回类型   
     */
    public List<PhysicalGroupRelation> findByGroupId(String groupId) {
        return getDao().findByGroupId(groupId);
    }
    
    /**
     * qiucs 2014-11-28 
     * <p>描述: 根据物理表Id和物理表组Id获取UUID</p>
     * @param  groupId
     * @return String    返回类型   
     */
    public String getIdByTableIdAndGroupId(String tableId, String groupId) {
        return getDao().getIdByTableIdAndGroupId(tableId, groupId);
    }
    
    @Transactional
    public List<PhysicalGroupRelation> save(String tableIds, String groupId) {
    	deleteAll(groupId);
    	String[] idArr = tableIds.split(",");
    	List<PhysicalGroupRelation> list = new ArrayList<PhysicalGroupRelation>();
    	List<LogicGroupRelation> logicList = getService(LogicGroupRelationService.class).getByGroupCode(getService(PhysicalGroupDefineService.class).getByID(groupId).getLogicGroupCode());
    	for (int i = 0; i < idArr.length; i++) {
    		if (StringUtil.isEmpty(idArr[i])) continue;
			PhysicalGroupRelation entity = new PhysicalGroupRelation();
			entity.setTableId(idArr[i]);
			entity.setGroupId(groupId);
			for (LogicGroupRelation logicGroupRelation : logicList) {
				if (logicGroupRelation.getTableCode().equals(getService(PhysicalTableDefineService.class).getByID(idArr[i]).getLogicTableCode())) {
					entity.setShowOrder(logicGroupRelation.getShowOrder());
				}
			}
			list.add(entity);
		}
    	// 移除缓存
    	GroupUtil.removeLogicGroupCode(groupId);
    	return getDao().save(list);
    }
    
    @Transactional
    public void deleteAll(String groupId){
    	List<String> idArr = getDao().getIdsByGroupId(groupId);
    	for (String id : idArr) {
			getDao().delete(id);
		}
    }
    
    /**
     * qiujinwei 2014-12-08 
     * <p>描述: 根据物理表组ID删除表关系</p>
     * @param  tableId    设定参数   
     */
    @Transactional
    public void deleteByGroupId(String groupId) {
        getDao().deleteByGroupId(groupId);
    }
    
    /**
     * qiujinwei 2014-12-09
     * <p>标题: getPhysicalGroupRelationByGroupId</p>
     * <p>描述: 根据物理表组Id获取表关系</p>
     * @return List<String>    返回类型   
     * @throws
     */
    public List<Object[]> getPhysicalGroupRelationByGroupId(String groupId){
    	return getDao().getPhysicalGroupRelationByGroupId(groupId);
    }
    
    /**
     * <p>描述: 封闭为树节点json</p>
     * @return List<Map<String,Object>>    返回类型   
     * @throws
     */
    public List<Map<String, Object>> getTreeNode(String groupId) {
    	String filter = "EQ_groupId=" + groupId;
        List<PhysicalGroupRelation> list = find(filter, new Sort("showOrder"));
        List<Map<String, Object>> data = new ArrayList<Map<String, Object>>();
        for (PhysicalGroupRelation entity : list) {
            data.add(beanToTreeNode(entity));
        }
        return data;
    }
    
    /**
     * <p>描述: 实体bean转为树节点</p>
     * @return Map<String,Object>    返回类型   
     * @throws
     */
    private Map<String, Object> beanToTreeNode(PhysicalGroupRelation entity) {
        Map<String, Object> data = new HashMap<String, Object>();
        PhysicalTableDefine table = TableUtil.getTableEntity(entity.getTableId());
        
        List<Map<String, String>> userdata = new ArrayList<Map<String, String>>();
        Map<String, String> item = new HashMap<String, String>();
        item.put("name", "type");
        item.put("content", "5");
        userdata.add(item);
        item = new HashMap<String, String>();
        item.put("name", "classification");
        item.put("content", table.getClassification());
        userdata.add(item);
        
        data.put("id", entity.getId().concat(table.getId()));
        data.put("text", table.getShowName());
        data.put("type", "1");
        data.put("child", Boolean.FALSE);
        data.put("userdata", userdata);
        return data;
    }
    
    /**
     * qiucs 2015-10-15 下午4:05:09
     * <p>描述: 根据逻辑表CODE获取物理表组节点相关信息 </p>
     * @return List<Object[]>
     */
    public List<Object[]> getPhysicalGroupNode(String logicGroupCode) {
    	return getDao().getPhysicalGroupNode(logicGroupCode);
    }
    
}
