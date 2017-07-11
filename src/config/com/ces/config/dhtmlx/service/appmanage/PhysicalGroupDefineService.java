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

import com.ces.config.datamodel.message.MessageModel;
import com.ces.config.dhtmlx.dao.appmanage.PhysicalGroupDefineDao;
import com.ces.config.dhtmlx.entity.appmanage.PhysicalGroupDefine;
import com.ces.config.dhtmlx.entity.appmanage.PhysicalGroupRelation;
import com.ces.config.dhtmlx.service.base.ConfigDefineDaoService;
import com.ces.config.utils.GroupUtil;
import com.ces.config.utils.StringUtil;
import com.ces.config.utils.TableUtil;

/**
 * 物理表组定义表处理层
 * 
 * @author qiujinwei
 */
@Component
public class PhysicalGroupDefineService extends ConfigDefineDaoService<PhysicalGroupDefine, PhysicalGroupDefineDao> {
	
	/*
     * (非 Javadoc)
     * <p>标题: bindingDao</p>
     * <p>描述: 注入自定义持久层(Dao)</p>
     * @param entityClass
     * @see com.ces.xarch.core.service.AbstractService#bindingDao(java.lang.Class)
     */
    @Autowired
    @Qualifier("physicalGroupDefineDao")
    @Override
    protected void setDaoUnBinding(PhysicalGroupDefineDao dao) {
        super.setDaoUnBinding(dao);
    }
    
    /**
     * qiujinwei 2014-11-17 
     * <p>标题: adjustShowOrder</p>
     * <p>描述: 拖拽调整显示顺序</p>
     * @param  beginIds
     * @param  endId    设定参数   
     * @return void    返回类型   
     * @throws
     */
    @Transactional
    public void adjustShowOrder(String sourceIds, String targetId) {
        String[] idArr = sourceIds.split(",");
        int len = idArr.length;
        
        int sBeginShowOrder   = getDao().getShowOrderById(idArr[0]);
        int sEndShowOrder     = (len > 1) ? getDao().getShowOrderById(idArr[len - 1]) : sBeginShowOrder;
        if (sBeginShowOrder > sEndShowOrder) {
            sBeginShowOrder = sBeginShowOrder^sEndShowOrder;
            sEndShowOrder   = sBeginShowOrder^sEndShowOrder;
            sBeginShowOrder = sBeginShowOrder^sEndShowOrder;
        }
        int tShowOrder        = getDao().getShowOrderById(targetId);
        
        int increaseNum = 0, differLen = 0, begin = 0, end = 0;
        boolean isUp = false;
        if (sBeginShowOrder > tShowOrder) {
            isUp = true;
        }
        if (isUp) { 
            increaseNum = len;
            differLen = tShowOrder - sBeginShowOrder;
            begin = tShowOrder-1;
            end   = sBeginShowOrder;
        } else { 
            increaseNum = -len;
            differLen = tShowOrder - sEndShowOrder;
            begin = sEndShowOrder;
            end   = tShowOrder + 1;
        }
        // update between sourceIds and targetId
        getDao().batchUpdateShowOrder(begin, end, increaseNum);
        // update sourceIds
        for (int i = 0; i < len; i++) {
            getDao().updateShowOrderById(idArr[i], differLen);
        }
    }
    
    /**
     * qiucs 2014-11-18 
     * <p>描述: 封闭为树节点json</p>
     * @return List<Map<String,Object>>    返回类型   
     * @throws
     */
    public List<Map<String, Object>> getTreeNode() {
        return getTreeNode(null);
    }
    
    /**
     * qiucs 2014-12-10 
     * <p>描述: 封闭为树节点json</p>
     * @return List<Map<String,Object>> 返回类型   
     * @throws
     */
    public List<Map<String, Object>> getTreeNode(String idPre) {
        Sort sort = new Sort("showOrder");
        idPre = StringUtil.null2empty(idPre);
        List<PhysicalGroupDefine> list = getDao().findAll(sort);
        List<Map<String, Object>> data = new ArrayList<Map<String, Object>>();
        for (PhysicalGroupDefine entity : list) {
            data.add(beanToTreeNode(entity, idPre));
        }
        return data;
    }
    
    /**
     * qiucs 2014-11-18 
     * <p>描述: 实体bean转为树节点</p>
     * @return Map<String,Object>    返回类型   
     * @throws
     */
    private Map<String, Object> beanToTreeNode(PhysicalGroupDefine entity, String idPre) {
        Map<String, Object> data = new HashMap<String, Object>();
        
        List<Map<String, String>> userdata = new ArrayList<Map<String, String>>();
        Map<String, String> item = new HashMap<String, String>();
        item.put("name", "type");
        item.put("content", "3");
        userdata.add(item);
        item = new HashMap<String, String>();
        item.put("name", "classification");
        item.put("content", "PG");
        userdata.add(item);
        
        data.put("id", idPre.concat(entity.getId()));
        data.put("text", entity.getGroupName());
        data.put("type", "0");
        data.put("child", Boolean.TRUE);
        data.put("userdata", userdata);
        return data;
    }
    
    /**
     * qiucs 2014-11-28 
     * <p>描述: 获取物理表组对应的逻辑表组编码</p>
     */
    public String getLogicGroupCode(String id) {
        PhysicalGroupDefine entity = getByID(id);
        return entity.getLogicGroupCode();
    }
    
    /**
     * qiucs 2014-11-28 
     * <p>描述: 获取物理表组中的物理表</p>
     * @return Map<String,Object>    返回类型   
     */
    public Map<String, Object> getGroupTables(String id) {
        Map<String, Object> data = new HashMap<String, Object>();
        data.put("logicGroupCode", GroupUtil.getLogicGroupCode(id));
        List<PhysicalGroupRelation> list = getService(PhysicalGroupRelationService.class).findByGroupId(id);
        for (PhysicalGroupRelation r : list) {
            Map<String, String> item = new HashMap<String, String>();
            item.put("id", r.getTableId());
            item.put("name", TableUtil.getTableName(r.getTableId()));
            data.put(TableUtil.getLogicTableCode(r.getTableId()), item);
        }
        return data;
    }
    
    /**
     * qiucs 2015-7-4 上午9:29:09
     * <p>描述: 获取物理表组中的物理表 </p>
     * @return Map<String,String>
     */
    public Map<String, String> getPhysicalGroupTables(String id) {
        Map<String, String> data = new HashMap<String, String>();
        List<PhysicalGroupRelation> list = getService(PhysicalGroupRelationService.class).findByGroupId(id);
        for (PhysicalGroupRelation r : list) {
            data.put(TableUtil.getLogicTableCode(r.getTableId()), r.getTableId());
        }
        return data;
    }
    
    /**
     * qiucs 2014-11-28 
     * <p>描述: 获取物理表组下的主物理表</p>
     * @param  id
     * @param  mainLogicTableCode
     * @return MessageModel    返回类型   
     * @throws
     */
    public MessageModel getMainPhysicalTableId(String id, String mainLogicTableCode) {
        List<PhysicalGroupRelation> list = getService(PhysicalGroupRelationService.class).findByGroupId(id);
        if (null == list || list.isEmpty()) return MessageModel.falseInstance("该物理表组未绑定物理表！");
        for (PhysicalGroupRelation r : list) {
            String tableId = r.getTableId();
            if (mainLogicTableCode.equals(TableUtil.getLogicTableCode(tableId))) {
                return MessageModel.trueInstance(tableId);
            }
        }
        return MessageModel.falseInstance("该物理表组的物理表与其对应的逻辑表组下逻辑表不一致，请检查！");
    }
    
    @Override
    @Transactional
    public void delete(String id) {
    	getDao().delete(id);
    	clearGroupRelation(id);
    	GroupUtil.removeLogicGroupCode(id);
    	GroupUtil.removePhysicalGroupTables(id);
    }
    
    /**
     * qiujinwei 2014-12-08
     * <p>描述: 删除与物理表组相关的物理表关系</p>
     * @param  id
     * @return     返回类型   
     * @throws
     */
    public void clearGroupRelation(String id){
    	getService(PhysicalGroupRelationService.class).deleteByGroupId(id);
    }
    
    @Override
    @Transactional
    public PhysicalGroupDefine save(PhysicalGroupDefine entity) {
    	if (StringUtil.isEmpty(entity.getId()) || StringUtil.isEmpty(entity.getShowOrder())) {
            Integer maxShowOrder = getDao().getMaxShowOrder();
            if (null == maxShowOrder) {
                maxShowOrder = new Integer(0);
            }
            entity.setShowOrder((maxShowOrder+1));
            entity = getDao().save(entity);
        } else {
            entity = getDao().save(entity);
        }
    	GroupUtil.addLogicGroupCode(entity.getId(), entity.getLogicGroupCode());
    	return (entity);
    }
    
    @Transactional
    public PhysicalGroupDefine autoMadeEntity(String logicGroupCode, String groupName, String code, String remark){
    	PhysicalGroupDefine entity = new PhysicalGroupDefine();
    	entity.setLogicGroupCode(logicGroupCode);
    	entity.setGroupName(groupName);
    	entity.setCode(code);
    	entity.setRemark(remark);
    	return save(entity);
    }
    
    /**
     * 根据物理表ID获取物理表组ID
     * @param tableId 物理表ID
     * @return String
     */
    public String getByTableId(String tableId){
    	return getDao().getByTableId(tableId);
    }
    
    /**
     * 根据逻辑表组code获取物理表组
     * 
     * @param logicGroupCode 物理表组所属逻辑表组CODE 
     * @return List<PhysicalGroupDefine>
     */
    public List<PhysicalGroupDefine> getByLogicGroupCode(String logicGroupCode) {
        return getDao().getByLogicGroupCode(logicGroupCode);
    }
}
