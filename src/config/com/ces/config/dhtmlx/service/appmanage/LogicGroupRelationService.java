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
import com.ces.config.dhtmlx.dao.appmanage.LogicGroupRelationDao;
import com.ces.config.dhtmlx.entity.appmanage.LogicGroupRelation;
import com.ces.config.dhtmlx.service.base.ConfigDefineDaoService;
import com.ces.config.utils.StringUtil;

/**
 * 逻辑表组和逻辑表关系表
 * 
 * @author qiujinwei
 */
@Component
public class LogicGroupRelationService extends ConfigDefineDaoService<LogicGroupRelation, LogicGroupRelationDao> {
	
	/*
     * (非 Javadoc)
     * <p>标题: bindingDao</p>
     * <p>描述: 注入自定义持久层(Dao)</p>
     * @param entityClass
     * @see com.ces.xarch.core.service.AbstractService#bindingDao(java.lang.Class)
     */
    @Autowired
    @Qualifier("logicGroupRelationDao")
    @Override
    protected void setDaoUnBinding(LogicGroupRelationDao dao) {
        super.setDaoUnBinding(dao);
    }
    
    @Override
    @Transactional
    public LogicGroupRelation save(LogicGroupRelation entity){
    	if (StringUtil.isEmpty(entity.getId()) || StringUtil.isEmpty(entity.getShowOrder())) {
            Integer maxShowOrder = getDao().getMaxShowOrder(entity.getGroupCode());
            if (null == maxShowOrder) {
                maxShowOrder = new Integer(0);
            }
            entity.setShowOrder((maxShowOrder+1));
            entity = getDao().save(entity);
        } else {
            entity = getDao().save(entity);
        }
    	//更新逻辑表状态信息
    	getService(LogicTableDefineService.class).updateStatus(entity.getTableCode(), Boolean.TRUE);
    	return (entity);
    }
    
    /**
     * qiucs 2014-11-28 
     * <p>描述: 获取逻辑表组下主逻辑表</p>
     * @param  logicGroupCode
     * @return String    返回类型   
     */
    public String getMainLogicTableCode(String groupCode) {
        return getDao().getMainLogicGroupTableCode(groupCode);
    }
    
    /**
     * 根据逻辑表组编码获取逻辑表组下逻辑表的关联关系
     * 
     * @param groupCode 逻辑表组Code
     * @return List<LogicGroupRelation>
     */
    public List<LogicGroupRelation> getByGroupCode(String groupCode) {
        return getDao().getByGroupCode(groupCode);
    }
    
    /**
     * qiujinwei 2014-12-08 
     * <p>描述: 更新父逻辑表</p>
     * @param  parentTableCode
     * @param  id
     * @return MessageModel    返回类型   
     * @throws
     */
    @Transactional
    public MessageModel update(String parentTableCode, String id) {
    	LogicGroupRelation entity = getByID(id);
    	entity.setParentTableCode(parentTableCode);
    	getDao().save(entity);
        return MessageModel.trueInstance("OK");
    }
    
    /**
     * qiujinwei 2014-12-08
     * <p>标题: adjustShowOrder</p>
     * <p>描述: 拖拽调整显示顺序</p>
     * 
     * @param beginIds
     * @param endId 设定参数
     * @return void 返回类型
     * @throws
     */
    @Transactional
    public void adjustShowOrder(String sourceIds, String targetId, String groupCode) {
        String[] idArr = sourceIds.split(",");
        int len = idArr.length;

        int sBeginShowOrder = getDao().getShowOrderById(idArr[0]);
        int sEndShowOrder = (len > 1) ? getDao().getShowOrderById(idArr[len - 1]) : sBeginShowOrder;
        if (sBeginShowOrder > sEndShowOrder) {
            sBeginShowOrder = sBeginShowOrder ^ sEndShowOrder;
            sEndShowOrder = sBeginShowOrder ^ sEndShowOrder;
            sBeginShowOrder = sBeginShowOrder ^ sEndShowOrder;
        }
        int tShowOrder = getDao().getShowOrderById(targetId);

        int increaseNum = 0, differLen = 0, begin = 0, end = 0;
        boolean isUp = false;
        if (sBeginShowOrder > tShowOrder) {
            isUp = true;
        }
        if (isUp) {
            increaseNum = len;
            differLen = tShowOrder - sBeginShowOrder;
            begin = tShowOrder - 1;
            end = sBeginShowOrder;
        } else {
            increaseNum = -len;
            differLen = tShowOrder - sEndShowOrder;
            begin = sEndShowOrder;
            end = tShowOrder + 1;
        }
        // update between sourceIds and targetId
        getDao().batchUpdateShowOrder(begin, end, increaseNum, groupCode);
        // update sourceIds
        for (int i = 0; i < len; i++) {
            getDao().updateShowOrderById(idArr[i], differLen);
            LogicGroupRelation entity = getByID(idArr[i]);
            if (checkParentCode(entity)) {
				entity.setParentTableCode(null);
				super.save(entity);
			}
        }
        List<LogicGroupRelation> list = getDao().getRelationFromBeginToEnd(begin + increaseNum, end + increaseNum, groupCode);
        for (LogicGroupRelation entity : list) {
        	if (checkParentCode(entity)) {
				entity.setParentTableCode(null);
				super.save(entity);
			}
		}
    }
    
    /**
     * qiujinwei 2014-12-09
     * <p>标题: saveAll</p>
     * <p>描述: 批量添加</p>
     * 
     * @return Object 返回类型
     * @throws
     */
    @Transactional
    public void saveAll(String codes, String groupCode){
    	String[] codeArr = codes.split(";");
    	Integer showOrder = getDao().getMaxShowOrder(groupCode);
    	if (showOrder == null) {
			showOrder = 0;
		}
    	for (int i = 0; i < codeArr.length; i++) {
        	if (StringUtil.isEmpty(codeArr[i])) {
				continue;
			}
        	LogicGroupRelation entity = new LogicGroupRelation();
        	entity.setTableCode(codeArr[i]);
        	entity.setGroupCode(groupCode);
        	entity.setShowOrder(++showOrder);
        	save(entity);
		}
    }
    
    /**
     * qiujinwei 2014-12-09
     * <p>标题: checkParentCode</p>
     * <p>描述: 父逻辑表检验</p>
     * 
     * @return Object 返回类型
     * @throws
     */
    public boolean checkParentCode(LogicGroupRelation entity){
    	boolean flag = false;
    	if(entity.getParentTableCode() != null){
    		if (entity.getShowOrder() < getDao().getShowOrderByCode(entity.getParentTableCode(), entity.getGroupCode())) {
    			flag = true;
    		}
    	}
    	return flag;
    }
    
    /**
     * qiujinwei 2014-12-09
     * <p>标题: check</p>
     * <p>描述: 检验该逻辑表组下的所有逻辑表父逻辑表是否符合要求</p>
     * 
     * @return List<LogicGroupRelation> 返回类型
     * @throws
     */
    public MessageModel check(String groupCode){
    	boolean flag = true;
    	StringBuffer message = new StringBuffer();
    	List<LogicGroupRelation> list = getDao().getByGroupCode(groupCode);
    	for (LogicGroupRelation entity : list) {
			if (entity.getParentTableCode() == null && entity.getShowOrder() != 1) {
				message.append(getService(LogicTableDefineService.class).getByCode(entity.getTableCode()).getShowName() + "  没有指定父逻辑表!<br/>");
				flag = false;
			} else if (checkParentCode(entity)) {
				message.append(getService(LogicTableDefineService.class).getByCode(entity.getTableCode()).getShowName() + "  父逻辑表指定错误!<br/>");
				flag = false;
			}
		}
    	if (flag) {
    		return new MessageModel(flag, "success");
		}
    	else {
    		return new MessageModel(flag, message.toString());
		}
    }

    /**
     * 获取逻辑表组和逻辑表关系表
     * 
     * @param groupCode 逻辑表组编码
     * @param tableCode 逻辑表编码
     * @return LogicGroupRelation
     */
    public LogicGroupRelation getByGroupCodeAndTableCode(String groupCode, String tableCode) {
        return getDao().getByGroupCodeAndTableCode(groupCode, tableCode);
    }
    
    /**
     * 根据逻辑表组编码获取逻辑表组下的逻辑表信息
     * 
     * @param groupCode 逻辑表组编码
     * @return List<Object>
     */
    public List<Map<String, String>> getLogicTablesByGroupCode(String groupCode){
    	List<Object[]> list = getDao().getLogicTablesByGroupCode(groupCode);
    	List<Map<String, String>> data = new ArrayList<Map<String,String>>();
    	for (int i = 0; i < list.size(); i++) {
    		Map<String, String> map = new HashMap<String, String>();
    		map.put("code", list.get(i)[0].toString());
    		map.put("showName", list.get(i)[1].toString());
    		map.put("showOrder", list.get(i)[2].toString());
    		data.add(map);
		}
    	return data;
    }
    
    /**
     * 根据逻辑表组编码和父逻辑表编码获取逻辑表下拉单信息
     * 
     * @param groupCode 逻辑表组编码
     * @param parentTableCode 父逻辑表编码
     * @return Object
     */
    public Object getComboOfLogicTable(String groupCode, String parentTableCode){
    	return getDao().getComboOfLogicTable(groupCode, parentTableCode);
    }
    
    @Override
    @Transactional
    public void delete(String id) {
    	LogicGroupRelation entity = getByID(id);
    	Integer begin = entity.getShowOrder();
    	Integer end = getDao().getMaxShowOrder(entity.getGroupCode()) + 1;
    	getDao().batchUpdateShowOrder(begin, end, -1, entity.getGroupCode());
    	getDao().delete(id);
    	//更新逻辑表状态信息
    	getService(LogicTableDefineService.class).updateStatus(entity.getTableCode(), Boolean.FALSE);
    }
    
    /**
     * qiujinwei 2014-12-19 
     * <p>描述: 实体bean转为树节点</p>
     * @return Map<String,Object>    返回类型   
     * @throws
     */
    public List<Map<String, Object>> getTreeNode(String groupCode) {
    	String filter = "EQ_groupCode=" + groupCode;
        List<LogicGroupRelation> list = find(filter, new Sort("showOrder"));
        List<Map<String, Object>> data = new ArrayList<Map<String, Object>>();
        for (LogicGroupRelation entity : list) {
            data.add(beanToTreeNode(entity));
        }
        return data;
    }
    
    /**
     * qiujinwei 2014-12-19 
     * <p>描述: 实体bean转为树节点</p>
     * @return Map<String,Object>    返回类型   
     * @throws
     */
    private Map<String, Object> beanToTreeNode(LogicGroupRelation entity) {
        Map<String, Object> data = new HashMap<String, Object>();
        
        List<Map<String, String>> userdata = new ArrayList<Map<String, String>>();
        Map<String, String> item = new HashMap<String, String>();
        item.put("name", "type");
        item.put("content", "4");
        userdata.add(item);
        item = new HashMap<String, String>();
        item.put("name", "classification");
        item.put("content", entity.getGroupCode());
        userdata.add(item);
        
        data.put("id", entity.getId());
        data.put("text", getService(LogicTableDefineService.class).getByCode(entity.getTableCode()).getShowName());
        data.put("type", "0");
        data.put("child", false);
        data.put("userdata", userdata);
        return data;
    }
    
    /**
     * 批量修改逻辑表编码
     * @param oldLogicTableCode 旧的逻辑表编码
     * @param newLogicTableCode 新的逻辑表编码
     */
    @Transactional
    public void batchUpdateTableCode(String oldLogicTableCode, String newLogicTableCode) {
    	getDao().batchUpdateTableCode(oldLogicTableCode, newLogicTableCode);
    }
    
    /**
     * 批量修改父逻辑表编码
     * @param oldLogicTableCode 旧的逻辑表编码
     * @param newLogicTableCode 新的逻辑表编码
     */
    @Transactional
    public void batchUpdateParentTableCode(String oldLogicTableCode, String newLogicTableCode) {
    	getDao().batchUpdateParentTableCode(oldLogicTableCode, newLogicTableCode);
    }
    
}
