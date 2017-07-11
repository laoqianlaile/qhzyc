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

import com.ces.config.dhtmlx.dao.appmanage.LogicGroupDefineDao;
import com.ces.config.dhtmlx.dao.appmanage.LogicGroupRelationDao;
import com.ces.config.dhtmlx.dao.appmanage.LogicTableRelationDao;
import com.ces.config.dhtmlx.dao.appmanage.ModuleDao;
import com.ces.config.dhtmlx.dao.appmanage.PhysicalGroupDefineDao;
import com.ces.config.dhtmlx.entity.appmanage.LogicGroupDefine;
import com.ces.config.dhtmlx.json.entity.common.DhtmlxComboOption;
import com.ces.config.dhtmlx.service.base.ConfigDefineDaoService;
import com.ces.config.utils.StringUtil;
import com.google.common.collect.Lists;

/**
 * 逻辑表组定义表处理层
 * 
 * @author qiujinwei
 */
@Component
public class LogicGroupDefineService extends ConfigDefineDaoService<LogicGroupDefine, LogicGroupDefineDao> {
	
	/*
     * (非 Javadoc)
     * <p>标题: bindingDao</p>
     * <p>描述: 注入自定义持久层(Dao)</p>
     * @param entityClass
     * @see com.ces.xarch.core.service.AbstractService#bindingDao(java.lang.Class)
     */
    @Autowired
    @Qualifier("logicGroupDefineDao")
    @Override
    protected void setDaoUnBinding(LogicGroupDefineDao dao) {
        super.setDaoUnBinding(dao);
    }
    
    /**
     * qiujinwei 2014-11-14 
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
        return getTreeNode(null, false);
    }
    
    /**
     * qiucs 2014-12-10 
     * <p>描述: 封闭为树节点json</p>
     * @return List<Map<String,Object>>    返回类型   
     * @throws
     */
    public List<Map<String, Object>> getTreeNode(String idPre, boolean isParent) {
        Sort sort = new Sort("showOrder");
        idPre = StringUtil.null2empty(idPre);
        List<LogicGroupDefine> list = getDao().findAll(sort);
        List<Map<String, Object>> data = new ArrayList<Map<String, Object>>();
        for (LogicGroupDefine entity : list) {
            data.add(beanToTreeNode(entity, idPre, isParent));
        }
        return data;
    }
    
    /**
     * qiucs 2014-11-18 
     * <p>描述: 实体bean转为树节点</p>
     * @return Map<String,Object>    返回类型   
     * @throws
     */
    private Map<String, Object> beanToTreeNode(LogicGroupDefine entity, String idPre, boolean isParent) {
        Map<String, Object> data = new HashMap<String, Object>();
        
        List<Map<String, String>> userdata = new ArrayList<Map<String, String>>();
        Map<String, String> item = new HashMap<String, String>();
        item.put("name", "type");
        item.put("content", "2");
        userdata.add(item);
        item = new HashMap<String, String>();
        item.put("name", "classification");
        item.put("content", "LG");
        userdata.add(item);
        
        data.put("id", idPre.concat(entity.getCode()));
        data.put("text", entity.getGroupName());
        data.put("type", "0");
        data.put("child", isParent);
        data.put("userdata", userdata);
        return data;
    }
    
    @Override
    @Transactional
    public LogicGroupDefine save(LogicGroupDefine entity){
    	if (StringUtil.isEmpty(entity.getId()) || StringUtil.isEmpty(entity.getShowOrder())) {
            Integer maxShowOrder = getDao().getMaxShowOrder();
            if (null == maxShowOrder) {
                maxShowOrder = new Integer(0);
            }
            entity.setShowOrder((maxShowOrder+1));
            entity.setStatus("0");
        } else {
        	LogicGroupDefine oldLogicGroupDefine = getByID(entity.getId());
        	if (!oldLogicGroupDefine.getCode().equals(entity.getCode())) {
        		String oldLogicGroupCode = oldLogicGroupDefine.getCode();
        		String newLogicGroupCode = entity.getCode();
				//同步自定义构建
        		getDaoFromContext(ModuleDao.class).batchUpdateLogicGroupCode(oldLogicGroupCode, newLogicGroupCode);
        		//同步逻辑表组关系
        		getDaoFromContext(LogicGroupRelationDao.class).batchUpdateLogicGroupCode(oldLogicGroupCode, newLogicGroupCode);
        		//同步逻辑表组关系列
        		getDaoFromContext(LogicTableRelationDao.class).batchUpdateLogicGroupCode(oldLogicGroupCode, newLogicGroupCode);
        		//同步物理表组
        		getDaoFromContext(PhysicalGroupDefineDao.class).batchUpdateLogicGroupCode(oldLogicGroupCode, newLogicGroupCode);
			}
        }
		return getDao().save(entity);
    	
    }
    
    /**
     * 获取所有的逻辑表组
     * 
     * @return List<LogicGroupDefine>
     */
    public List<LogicGroupDefine> getAllLogicGroupDefines() {
        return getDao().getAllLogicGroupDefines();
    }
    
    /**
     * 根据编码获取逻辑表组
     * 
     * @param code 逻辑表组编码
     * @return LogicGroupDefine
     */
    public LogicGroupDefine getByCode(String code) {
        return getDao().getByCode(code);
    }
    
    /**
     * 获取逻辑表组下拉框选项
     * 
     * @return Object
     */
    public Object comboOfLogicTableGroups() {
        List<DhtmlxComboOption> opts = Lists.newArrayList();
        try {
            List<LogicGroupDefine> logicGroupDefineList = getDao().getAllLogicGroupDefines();
            for (LogicGroupDefine logicGroup : logicGroupDefineList) {
                DhtmlxComboOption option = new DhtmlxComboOption();
                option.setValue(logicGroup.getCode());
                option.setText(logicGroup.getGroupName());
                opts.add(option);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }
        return opts;
    }
    
    /**
     * <p>描述: 更新逻辑表组状态信息</p>
     * 
     * @param logicTableGroupCode 逻辑表组Code
     * @param parentTableCode 逻辑表组中父逻辑表Code
     * @return void 返回类型
     * @throws
     */
    public void updateStatus(String code, boolean status){
    	if (StringUtil.isEmpty(code)) return;
    	LogicGroupDefine entity = getByCode(code);
    	if (status) {//逻辑表组应用状态更新为已应用
			entity.setStatus("1");
			getDao().save(entity);
		} else {//逻辑表组应用状态更新为未应用
			if (getDao().getRelationInModule(code).isEmpty()) {
				entity.setStatus("0");
				getDao().save(entity);
			}
		}
    }
    
}
