package com.ces.config.dhtmlx.service.appmanage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.ces.config.datamodel.message.MessageModel;
import com.ces.config.datamodel.option.OptionModel;
import com.ces.config.dhtmlx.dao.appmanage.TableTreeDao;
import com.ces.config.dhtmlx.entity.appmanage.TableTree;
import com.ces.config.dhtmlx.service.base.ConfigDefineDaoService;
import com.ces.config.utils.StringUtil;
import com.google.common.collect.Lists;

@Component
public class TableTreeService extends ConfigDefineDaoService<TableTree, TableTreeDao> {
    
    /**
     * 根据名称和父节点ID获取TableTree
     * 
     * @param name 名称
     * @param parentId 父节点ID
     * @return TableTree
     */
    public TableTree getByNameAndParentId(String name, String parentId) {
        return getDao().getByNameAndParentId(name, parentId);
    }
    
    /**
     * qiucs 2014-11-18 
     * <p>描述: 实体bean转为树节点</p>
     * @return Map<String,Object>    返回类型   
     * @throws
     */
    public List<Map<String, Object>> getTreeNode(Specification<TableTree> spec, String id) {
        List<TableTree> list = find(spec, new Sort("showOrder"));
        List<Map<String, Object>> data = new ArrayList<Map<String, Object>>();
        // table tree
        for (TableTree entity : list) {
            data.add(beanToTreeNode(entity));
        }
        
        // physicial table
		data.addAll(getService(PhysicalTableDefineService.class).getTreeNode(id));
		
		// logic table
		data.addAll(getService(LogicTableDefineService.class).getTreeNode3(id));
		
        return data;
    }
    
    /**
     * qiucs 2014-12-15 
     * <p>描述: 实体bean转为树节点</p>
     * @return Map<String,Object>    返回类型   
     * @throws
     */
    public List<Map<String, Object>> getTreeNode(Specification<TableTree> spec, String id, String tableId) {
        List<TableTree> list = find(spec, new Sort("showOrder"));
        List<Map<String, Object>> data = new ArrayList<Map<String, Object>>();
        // table tree
        for (TableTree entity : list) {
            data.add(beanToOpenedTreeNode(entity));
        }
        
        // physicial table
		data.addAll(getService(PhysicalTableDefineService.class).getTreeNode2(id, tableId));
		
        return data;
    }
    
    /**
     * qiucs 2014-11-18 
     * <p>描述: 实体bean转为树节点</p>
     * @return Map<String,Object>    返回类型   
     * @throws
     */
    public Map<String, Object> beanToTreeNode(TableTree entity) {
        Map<String, Object> data = new HashMap<String, Object>();
        
        List<Map<String, String>> userdata = new ArrayList<Map<String, String>>();
        Map<String, String> item = new HashMap<String, String>();
        item.put("name", "type");
        if (entity.getNodeType().equals("0")) {
        	item.put("content", "6");
		} else {
			item.put("content", "0");
		}
        userdata.add(item);
        item = new HashMap<String, String>();
        item.put("name", "classification");
        item.put("content", entity.getClassification());
        userdata.add(item);
        
        data.put("id", entity.getId());
        data.put("text", entity.getName());
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
    public Map<String, Object> beanToOpenedTreeNode(TableTree entity) {
        Map<String, Object> data = new HashMap<String, Object>();
        
        List<Map<String, String>> userdata = new ArrayList<Map<String, String>>();
        Map<String, String> item = new HashMap<String, String>();
        item.put("name", "type");
        if (entity.getNodeType().equals("0")) {
        	item.put("content", "6");
		} else {
			item.put("content", "0");
		}
        userdata.add(item);
        item = new HashMap<String, String>();
        item.put("name", "classification");
        item.put("content", entity.getClassification());
        userdata.add(item);
        
        data.put("id", entity.getId());
        data.put("text", entity.getName());
        data.put("type", "0");
        data.put("child", Boolean.TRUE);
        data.put("open", Boolean.TRUE);
        data.put("userdata", userdata);
        return data;
    }
    
    public List<Map<String, Object>> getPhysicalTreeNode(Specification<TableTree> spec){
    	List<TableTree> list = find(spec, new Sort("showOrder"));
        List<Map<String, Object>> data = new ArrayList<Map<String, Object>>();
        for (TableTree entity : list) {
            data.add(beanToRadioTreeNode(entity));
        }
        return data;
    }
    
    private Map<String, Object> beanToRadioTreeNode(TableTree entity) {
        Map<String, Object> data = new HashMap<String, Object>();
        
        List<Map<String, String>> userdata = new ArrayList<Map<String, String>>();
        Map<String, String> item = new HashMap<String, String>();
        item.put("name", "type");
        item.put("content", "0");
        userdata.add(item);
        item = new HashMap<String, String>();
        item.put("name", "classification");
        item.put("content", entity.getClassification());
        userdata.add(item);
        
        data.put("id", entity.getId());
        data.put("text", entity.getName());
        data.put("type", "0");
        data.put("radio","1");
        data.put("child", Boolean.TRUE);
        data.put("userdata", userdata);
        return data;
    }
    
    public Object comboOfTableTree(){
    	List<OptionModel> opts = Lists.newArrayList();
    	List<TableTree> firstList = new ArrayList<TableTree>();
    	List<Map<String, Object>> classificationList = (List<Map<String, Object>>) getService(TableClassificationService.class).getTreeNode();
    	for (int i = 0; i < classificationList.size(); i++) {
    		firstList.addAll(getDao().getFirst(classificationList.get(i).get("id").toString()));
		}
        for (TableTree entity : firstList) {
        	OptionModel option = new OptionModel();
            option.setValue(String.valueOf(entity.getId()));
            option.setText(String.valueOf(entity.getName()));
            opts.add(option);
            String space = "";
            //找出此父节点下的所有子节点
        	List<TableTree> nodes = getDao().getByParentId(entity.getId());
        	if(nodes.isEmpty() || nodes == null)
        		continue;
            this.setChlidNodes(nodes, space, opts);
        }
        return opts;
    }
    
//    private OptionModel getChildNodes(){
//    	OptionModel option = new OptionModel();
//        option.setValue(String.valueOf(table[0]));
//        option.setText(String.valueOf(table[1]));
//    }
    /**
     * 插入子节点
     */
    private  void setChlidNodes(List<TableTree> nodeList,String space,List<OptionModel> optionList)
    {
    	for (TableTree tableTree : nodeList) {
    		space += "　";
    		OptionModel option = new OptionModel();
    		option.setValue(tableTree.getId());
    		option.setText(space + tableTree.getName());
    		optionList.add(option);
    		List<TableTree>  treelist = getDao().getByParentId(tableTree.getId());
    		//如果还有下一层 继续循环
    		while(treelist != null && !treelist.isEmpty())
    		   setChlidNodes(treelist, space, optionList);
		}
    }
    
    @Override
    @Transactional
	public TableTree save(TableTree entity) {
        if (StringUtil.isEmpty(entity.getId()) || StringUtil.isEmpty(entity.getShowOrder())) {
            Integer maxShowOrder = getDao().getMaxShowOrderByParentId(entity.getParentId());
            if (null == maxShowOrder) {
                maxShowOrder = new Integer(0);
            }
            entity.setShowOrder((maxShowOrder+1));
            entity = getDao().save(entity);
        } else {
            entity = getDao().save(entity);
        }
        return entity;
    }
    
    @Transactional
	public TableTree save(String parentId, String name, String classification, String tablePrefix) {
    	TableTree entity = new TableTree();
    	entity.setParentId(parentId);
    	entity.setClassification(classification);
    	entity.setName(name);
    	entity.setNodeType("1");
    	entity.setTablePrefix(tablePrefix);
    	return save(entity);
    }
    
    /**
     * qiujinwei 2015-03-24 
     * <p>描述: 节点删除校验</p>
     * @return     返回类型   
     * @throws
     */
    public MessageModel checkDelete(String id){
    	if (getService(PhysicalTableDefineService.class).checkTable(id)) {
			return new MessageModel(Boolean.FALSE, "节点下存在物理表");
		} else if (checktableTree(id)) {
			return new MessageModel(Boolean.FALSE, "节点下存在子节点");
		}
    	return new MessageModel(Boolean.TRUE, "success");
    }
    
    /**
     * qiujinwei 2015-03-24 
     * <p>描述: 验证节点下是否存在子节点</p>
     * @return     返回类型   
     * @throws
     */
    public boolean checktableTree(String parentId){
    	if (null == getDao().getByParentId(parentId) || getDao().getByParentId(parentId).isEmpty()) return false;
    	return true;
    }
    
    /**
     * qiujinwei 2015-05-26 
     * <p>描述: 根据物理表分类节点获取表前缀</p>
     * @return     返回类型   
     * @throws
     */
    public String getTablePrefix(String tableTreeId){
    	String tablePrefix = "0";
    	if (tableTreeId.length() < 32) {
			return tablePrefix; //使用默认表前缀
		} else {
			TableTree entity = getByID(tableTreeId);
			if (StringUtil.isNotEmpty(entity.getTablePrefix())) {
				tablePrefix = entity.getTablePrefix();
			} else {
				tablePrefix = getTablePrefix(entity.getParentId());
			}
			return tablePrefix;
		}
    }

}
