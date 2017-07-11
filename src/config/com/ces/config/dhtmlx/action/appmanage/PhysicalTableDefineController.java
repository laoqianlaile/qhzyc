package com.ces.config.dhtmlx.action.appmanage;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.rest.DefaultHttpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import com.ces.config.datamodel.message.MessageModel;
import com.ces.config.dhtmlx.action.base.ConfigDefineServiceDaoController;
import com.ces.config.dhtmlx.dao.appmanage.PhysicalTableDefineDao;
import com.ces.config.dhtmlx.entity.appmanage.Module;
import com.ces.config.dhtmlx.entity.appmanage.PhysicalTableDefine;
import com.ces.config.dhtmlx.entity.appmanage.TreeDefine;
import com.ces.config.dhtmlx.entity.component.ComponentConfig;
import com.ces.config.dhtmlx.service.appmanage.ModuleService;
import com.ces.config.dhtmlx.service.appmanage.PhysicalTableDefineService;
import com.ces.config.dhtmlx.service.appmanage.TreeDefineService;
import com.ces.config.utils.StringUtil;
import com.ces.xarch.core.exception.FatalException;
import com.ces.xarch.core.logger.Logger;

public class PhysicalTableDefineController extends ConfigDefineServiceDaoController<PhysicalTableDefine, PhysicalTableDefineService, PhysicalTableDefineDao> {
	
	private static final long serialVersionUID = 1L;
	
	private static Log log = LogFactory.getLog(PhysicalTableDefineController.class);
	
	@Override
    protected void initModel() {
        setModel(new PhysicalTableDefine());
    }

    /*
     * (非 Javadoc)
     * <p>标题: setService</p>
     * <p>描述: 注入服务层(Service)</p>
     * @param service
     * @see com.ces.xarch.core.web.struts2.BaseController#setService(com.ces.xarch.core.service.AbstractService)
     */
    @Override
    @Autowired
    @Qualifier("physicalTableDefineService")
    protected void setService(PhysicalTableDefineService service) {
        super.setService(service);
    }

    /**
     * qiucs 2013-9-18
     * <p>标题: adjustShowOrder</p>
     * <p>描述: 拖拽调整顺序</p>
     * 
     * @return Object 返回类型
     * @throws
     */
    public Object adjustShowOrder() throws FatalException {
        try {
            String tableTreeId = getParameter("P_tableTreeId");
            String sourceIds = getParameter("P_sourceIds");
            String targetId = getParameter("P_targetId");
            getService().adjustShowOrder(tableTreeId, sourceIds, targetId);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }
        return new DefaultHttpHeaders(SUCCESS).disableCaching();
    }

    /**
     * qiucs 2013-8-15
     * <p>标题: synViews</p>
     * <p>描述: 同步数据库中的视图</p>
     * 
     * @return Object 返回类型
     * @throws
     */
    public Object synViews() {
        try {
            setReturnData(getService().synViews());
        } catch (Exception e) {
            e.printStackTrace();
            setReturnData(new MessageModel(Boolean.FALSE, ""));
        }
        return NONE;
    }

    /**
     * qiucs 2013-8-26
     * <p>标题: comboTables</p>
     * <p>描述: 所有物理表下拉框数据</p>
     * 
     * @return Object 返回类型
     * @throws
     */
    public Object comboOfTables() {
        String groupId = getParameter("P_groupId");
        if (StringUtil.isNotEmpty(groupId)) {
        	setReturnData(getService().getComboOfTables(groupId));
        } else {
        	Boolean includeView = StringUtil.isBooleanTrue(getParameter("P_includeView"));
        	setReturnData(getService().getComboOfTables(includeView));
        }
        return new DefaultHttpHeaders(SUCCESS).disableCaching();
    }

    /**
     * gmh 2013-10-11
     * <p>标题: comboTables</p>
     * <p>描述: 所有物理表下拉框数据</p>
     * 
     * @return Object 返回类型
     * @throws
     */
    public Object getTableShowName() {
        setReturnData(getService().getTableShowName(getParameter("Q_tableId")));
        return new DefaultHttpHeaders(SUCCESS).disableCaching();
    }

    /*
     * (non-Javadoc)
     * @see com.ces.xarch.core.web.struts2.BaseController#destroy()
     * @author Administrator
     * @date 2013-10-23 12:57:04
     */
    @Override
    @Logger(action = "删除", logger = "${id}")
    public Object destroy() throws FatalException {
        try {
            getService().delete(getId());
            setReturnData(new MessageModel(Boolean.TRUE, ""));
        } catch (RuntimeException e) {
            setReturnData(new MessageModel(Boolean.FALSE, e.getMessage()));
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }
        return NONE;
    }

    /**
     * 获取构件关联的表
     * 
     * @return Object
     */
    @SuppressWarnings("unchecked")
    public Object getComponentTables() {
        HttpServletRequest request = ServletActionContext.getRequest();
        String componentConfigKey = request.getParameter("componentConfigKey");
        ComponentConfig componentConfig = (ComponentConfig) request.getSession().getAttribute(componentConfigKey);

        if (componentConfig == null) {
            setReturnData("上传失败");
        } else {
            list = getDataModel(getModelTemplate());
            processFilter(list);
            Map<String, PhysicalTableDefine> physicalTableDefineMap = (Map<String, PhysicalTableDefine>) componentConfig.getSelfDefineConfig().get("physicalTable");
            List<PhysicalTableDefine> physicalTableDefineList = new ArrayList<PhysicalTableDefine>();
            if (MapUtils.isNotEmpty(physicalTableDefineMap)) {
                physicalTableDefineList.addAll(physicalTableDefineMap.values());
            }
            list.setData(physicalTableDefineList);
        }
        return new DefaultHttpHeaders(SUCCESS).disableCaching();
    }

    /**
     * 根据名称获取构件相关表
     * 
     * @return Object
     */
    public Object getComponentTableByName() {
        HttpServletRequest request = ServletActionContext.getRequest();
        String tableName = request.getParameter("tableName");
        PhysicalTableDefine physicalTableDefine = getService().getByTableName(tableName);
        if (physicalTableDefine == null) {
            setReturnData("{'exist':false, 'message':'该表没有冲突'}");
        } else {
            setReturnData("{'id':'" + physicalTableDefine.getId() + "', 'exist':true}");
        }
        return new DefaultHttpHeaders(SUCCESS).disableCaching();
    }

    /**
     * 根据名称获取构件相关表Id
     * 
     * @return Object
     */
    public Object getIdByName() {
        HttpServletRequest request = ServletActionContext.getRequest();
        String tableName = request.getParameter("tableName");
        PhysicalTableDefine physicalTableDefine = getService().getByTableName(tableName);
        if (physicalTableDefine == null) {
            setReturnData(null);
        } else {
            setReturnData(physicalTableDefine.getId());
        }
        return new DefaultHttpHeaders(SUCCESS).disableCaching();
    }

    /**
     * 获取自定义构件中用到的表
     * 
     * @return Object
     */
    public Object getSelfDefineTableTree() {
        String componentVersionId = getParameter("componentVersionId");
        list = getDataModel(getModelTemplate());
        processFilter(list);
        PageRequest pageRequest = buildPageRequest();
        Sort sort = (null != pageRequest ? (null != pageRequest.getSort() ? pageRequest.getSort() : null) : null);
        List<PhysicalTableDefine> physicalTableDefineList = null;
        if (null == sort) {
        	physicalTableDefineList = getService().find(buildSpecification());
        } else {
        	physicalTableDefineList = getService().find(buildSpecification(), sort);
        }
        if (CollectionUtils.isNotEmpty(physicalTableDefineList)) {
            Set<String> tableIdSet = getTableDefineOfComponent(componentVersionId);
            PhysicalTableDefine physicalTableDefine = null;
            for (Iterator<PhysicalTableDefine> iterator = physicalTableDefineList.iterator(); iterator.hasNext();) {
            	physicalTableDefine = iterator.next();
                if (CollectionUtils.isNotEmpty(tableIdSet) && tableIdSet.contains(physicalTableDefine.getId())) {
                    continue;
                } else {
                    iterator.remove();
                }
            }
        }
        list.setData(beforeProcessTreeData(physicalTableDefineList));
        return new DefaultHttpHeaders("success").disableCaching();
    }

    /**
     * 获取自定义构件下的所有表ID
     * 
     * @param componentVersionId 自定义构件版本ID
     * @return Set<String>
     */
    private Set<String> getTableDefineOfComponent(String componentVersionId) {
        Set<String> tableIdSet = new HashSet<String>();
        Module module = getService(ModuleService.class).findByComponentVersionId(componentVersionId);
        if ("1".equals(module.getTemplateType())) {
            // 布局一 左右结构页面（左边为树）
            String treeId = module.getTreeId();
            List<TreeDefine> treeDefineList = getService(TreeDefineService.class).getAllChildren(treeId, null);
            TreeDefine rootNode = getService(TreeDefineService.class).getByID(treeId);
            treeDefineList.add(rootNode);
            for (TreeDefine treeDefine : treeDefineList) {
                if (StringUtil.isNotEmpty(treeDefine.getTableId())) {
                    tableIdSet.add(treeDefine.getTableId());
                    getTableParentId(tableIdSet, treeDefine.getTableId());
                }
                /*if (StringUtil.isNotEmpty(treeDefine.getTable2Id())) {
                    tableIdSet.add(treeDefine.getTable2Id());
                    getTableParentId(tableIdSet, treeDefine.getTable2Id());
                }
                if (StringUtil.isNotEmpty(treeDefine.getTable3Id())) {
                    tableIdSet.add(treeDefine.getTable3Id());
                    getTableParentId(tableIdSet, treeDefine.getTable3Id());
                }*/
            }
        } else if ("2".equals(module.getTemplateType())) {
            // 布局三 上下结构页面
            tableIdSet.add(module.getTable1Id());
            tableIdSet.add(module.getTable2Id());
            getTableParentId(tableIdSet, module.getTable1Id());
            getTableParentId(tableIdSet, module.getTable2Id());
        } else if ("3".equals(module.getTemplateType())) {
            // 布局二 整张页面
            tableIdSet.add(module.getTable1Id());
            getTableParentId(tableIdSet, module.getTable1Id());
        }
        return tableIdSet;
    }
    
    /**
     * 获取表的父节点的ID
     * 
     * @param tableIdSet 表ID集合
     * @param tableId 表ID
     */
    private void getTableParentId(Set<String> tableIdSet, String tableId) {
    	PhysicalTableDefine physicalTableDefine = getService().getByID(tableId);
        if (physicalTableDefine != null) {
            if (!"-A".equals(physicalTableDefine.getClassification()) && !"-P".equals(physicalTableDefine.getClassification()) && !"-D".equals(physicalTableDefine.getClassification())
                    && !tableIdSet.contains(physicalTableDefine.getTableTreeId())) {
                tableIdSet.add(physicalTableDefine.getTableTreeId());
                getTableParentId(tableIdSet, physicalTableDefine.getTableTreeId());
            }
        }
    }
    
    /**
     * qiujinwei 2014-12-01
     * <p>标题: getTableByRelation</p>
     * <p>描述: 获取物理表组下的物理表</p>
     * 
     * @return Object 返回类型
     * @throws
     */
    public Object getTableByRelation() throws FatalException {
    	try {
            String groupId = getParameter("P_groupId");
            setReturnData(getService().getPTIncludingPG(groupId));
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }
        return new DefaultHttpHeaders(SUCCESS).disableCaching();
    }
    
    /**
     * qiujinwei 2014-12-15
     * <p>标题: checkAllUnique</p>
     * <p>描述: 批量检查表唯一性</p>
     * 
     * @return Object 返回类型
     * @throws
     */
    public Object checkAllUnique(){
    	try {
            String tableNames = getParameter("P_tableNames");
            setReturnData(getService().getPTIncludingPG(tableNames));
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }
        return new DefaultHttpHeaders(SUCCESS).disableCaching();
    }
    
    /**
     * qiucs 2015-2-13 上午9:45:19
     * <p>描述: 检索是否可以创建索引库 </p>
     * @return Object
     */
    public Object canCreateIndex() {
    	try {
			setReturnData(getService().canCreateIndex(getId()));
		} catch (Exception e) {
			log.error("检索是否可以创建索引库出错", e);
		}
    	
    	return NONE;
    }

}
