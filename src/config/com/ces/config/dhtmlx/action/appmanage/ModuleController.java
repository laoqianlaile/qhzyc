package com.ces.config.dhtmlx.action.appmanage;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.rest.DefaultHttpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.ces.config.datamodel.message.MessageModel;
import com.ces.config.dhtmlx.action.base.ConfigDefineServiceDaoController;
import com.ces.config.dhtmlx.dao.appmanage.ModuleDao;
import com.ces.config.dhtmlx.entity.appmanage.Module;
import com.ces.config.dhtmlx.entity.component.Component;
import com.ces.config.dhtmlx.entity.component.ComponentVersion;
import com.ces.config.dhtmlx.entity.construct.Construct;
import com.ces.config.dhtmlx.entity.construct.ConstructDetail;
import com.ces.config.dhtmlx.entity.menu.Menu;
import com.ces.config.dhtmlx.json.entity.common.DhtmlxTreeNode;
import com.ces.config.dhtmlx.service.appmanage.ModuleService;
import com.ces.config.dhtmlx.service.component.ComponentService;
import com.ces.config.dhtmlx.service.component.ComponentVersionService;
import com.ces.config.dhtmlx.service.construct.ConstructDetailService;
import com.ces.config.dhtmlx.service.construct.ConstructService;
import com.ces.config.dhtmlx.service.menu.MenuService;
import com.ces.config.utils.CommonUtil;
import com.ces.config.utils.ConstantVar;
import com.ces.config.utils.ModuleUtil;
import com.ces.config.utils.StringUtil;
import com.ces.config.utils.TableUtil;
import com.ces.utils.BeanUtils;
import com.ces.xarch.core.exception.FatalException;
import com.ces.xarch.core.web.jackson.BeanFilter;
import com.google.common.collect.Lists;

public class ModuleController extends ConfigDefineServiceDaoController<Module, ModuleService, ModuleDao> {

    private static final long serialVersionUID = -4609059474518520871L;
    
    private static Log log = LogFactory.getLog(ModuleController.class);

    /**
     * 映射模型
     * 
     * @author wangmi
     * @date 2013-06-17 16:06:21
     */
    @Override
    protected void initModel() {
        setModel(new Module());
    }

    /*
     * (非 Javadoc) <p>标题: setService</p> <p>描述: 注入服务层(Service)</p> @param service
     * @see com.ces.xarch.core.web.struts2.BaseController#setService(com.ces.xarch.core.service.AbstractService)
     */
    @Override
    @Autowired
    @Qualifier("moduleService")
    protected void setService(ModuleService service) {
        super.setService(service);
    }

    /*
     * (非 Javadoc)   
     * <p>描述: </p>   
     * @see com.ces.xarch.core.web.struts2.BaseController#show()
     */
    @Override
    public Object show() throws FatalException {
        BeanUtils.copy(ModuleUtil.getModule(getId()),model);
        processFilter((BeanFilter)model);
        return new DefaultHttpHeaders("show").disableCaching();
    }

    /*
     * (非 Javadoc) <p>标题: destroy</p> <p>描述: </p> @return @throws FatalException
     * @see com.ces.xarch.core.web.struts2.BaseController#destroy()
     */
    @Override
    public Object destroy() throws FatalException {
        try {
            getService().delete(getId());
        } catch (RuntimeException e) {
            setStatus(false);
            setMessage(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }
        return new DefaultHttpHeaders(SUCCESS).disableCaching();
    }

    /**
     * 将被选择构件的应用配置应用到其他构件中
     * 
     * @return
     * @date 2014-5-8 12:57:25
     */
    public Object appApplyToModules() {
        try {
            String tableId = getParameter("P_tableId");
            String moduleId = getParameter("P_moduleId");
            String appToModuleIds = getParameter("P_appToModuleIds");
            String userId = CommonUtil.getUser().getId();
            getService().appApplyToModules(tableId, moduleId, appToModuleIds, userId);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }
        return new DefaultHttpHeaders(SUCCESS).disableCaching();
    }

    /**
     * qiucs 2013-9-9
     * <p>
     * 描述: 拖拽调整顺序
     * </p>
     * 
     * @return Object 返回类型
     * @throws
     */
    public Object adjustShowOrder() {
        try {
            String componentAreaId = getParameter("P_componentAreaId");
            String sourceIds = getParameter("P_sourceIds");
            String targetId = getParameter("P_targetId");
            getService().adjustShowOrder(componentAreaId, sourceIds, targetId);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }
        return new DefaultHttpHeaders(SUCCESS).disableCaching();
    }

    /**
     * qiucs 2013-9-29
     * <p>
     * 描述: 判断是否被绑定过
     * </p>
     * 
     * @return Object 返回类型
     * @throws FatalException
     */
    public Object checkModuleBinding() throws FatalException {
        try {
            boolean flag = false;
            String componentCode = getId();
            Component component = getService(ComponentService.class).getComponentByCode(componentCode);
            if (component != null) {
                List<ComponentVersion> componentVersionList = getService(ComponentVersionService.class).getComponentVersionListByComponentId(component.getId());
                if (CollectionUtils.isNotEmpty(componentVersionList)) {
                    for (ComponentVersion componentVersion : componentVersionList) {
                        if (flag) {
                            break;
                        }
                        // 绑定了菜单
                        List<Menu> menuList = getService(MenuService.class).getByComponentVersionId(componentVersion.getId());
                        if (CollectionUtils.isNotEmpty(menuList)) {
                            flag = true;
                        }
                        if (!flag) {
                            // 是某组合构件的基础构件
                            List<Construct> constructList = getService(ConstructService.class).getByBaseComponentVersionId(componentVersion.getId());
                            if (CollectionUtils.isNotEmpty(constructList)) {
                                flag = true;
                            }
                        }
                        if (!flag) {
                            // 绑定了某组合构件中的预留区
                            List<ConstructDetail> constructDetailList = getService(ConstructDetailService.class).getByComponentVersionId(
                                    componentVersion.getId());
                            if (CollectionUtils.isNotEmpty(constructDetailList)) {
                                flag = true;
                            }
                        }
                    }
                }
            }
            setReturnData(new MessageModel(flag, "OK"));
        } catch (Exception e) {
            e.printStackTrace();
            setReturnData(new MessageModel(Boolean.FALSE, "ERROR"));
        }
        return NONE;
    }

    /**
     * qiucs 2014-1-21
     * <p>
     * 描述: 根据模块ID获取对应的版本构件ID
     * </p>
     * 
     * @return Object 返回类型
     */
    public Object getComponentVersionId() {
        try {
            String componentVersionId = getService().getComponentVersionId(getId());
            if (null == componentVersionId)
                componentVersionId = "";
            setReturnData(componentVersionId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return NONE;
    }

    /**
     * qiucs 2014-3-20
     * <p>描述: 根据构件版本ID获取模块ID</p>
     */
    public Object obtainId() {
        try {
            String componentVersionId = getParameter("P_cvId");
            setReturnData(StringUtil.null2empty(getService().getIdByComponentVersionIdOr(componentVersionId)));
        } catch (Exception e) {
            // TODO: handle exception
        }
        return NONE;
    }

    /**
     * wl 2014-4-29
     * <p>描述: 根据模块名称获取模块ID</p>
     */
    public Object getModuleIdByName() {
        try {
            String moduleName = getParameter("P_moduleName");
            setReturnData(StringUtil.null2empty(getService().getIdByName(moduleName)));
        } catch (Exception e) {
            // TODO: handle exception
        }
        return NONE;
    }
    
    /**
     * 获取“应用到”模块树
     * 
     * @return Object
     */
    public Object getModuleCopyTree() {
        list = getDataModel(getModelTemplate());
        processFilter(list);
        String treeNodeId = getId();
        String tableId = getParameter("tableId");
        List<DhtmlxTreeNode> treeNodelist = Lists.newArrayList();
        DhtmlxTreeNode treeNode = null;
        List<Module> modules = getService().getByTableId(tableId);
        if ("-1".equals(treeNodeId) && CollectionUtils.isNotEmpty(modules)) {
            for (Module m : modules) {
                treeNode = new DhtmlxTreeNode();
                treeNode.setId(String.valueOf(m.getId()));
                treeNode.setText(String.valueOf(m.getName()));
                treeNode.setIm0(ConstantVar.IconTreeNode.LEAF);
                treeNode.setIm1(ConstantVar.IconTreeNode.FOLDER_OPEN);
                treeNode.setIm2(ConstantVar.IconTreeNode.FOLDER_CLOSE);
                treeNode.setChild("0");
                treeNodelist.add(treeNode);
            }
        }
        list.setData(treeNodelist);
        return new DefaultHttpHeaders("success").disableCaching();
    }
    
    /**
     * qiucs 2014-9-23 
     * <p>描述: 获取与表ID相关的模块</p>
     */
    public Object getWorkflowModule() {
        String tableId = getParameter("P_tableId");
        try {
            setReturnData(getService().getWorkflowModuleByTableId(tableId));
        } catch (Exception e) {
            log.error("获取与表ID(" + TableUtil.getTableName(tableId) + ")相关联的模块出错", e);
        }
        
        return NONE;
    }
    
    /**
     * qiucs 2015-7-2 上午10:42:28
     * <p>描述: 转换自定义构件配置信息 </p>
     * @return Object
     */
    public Object toPageModule() {
    	try {
    		String id = getId();
    		String physicalGroupId = getParameter("P_groupId");
    		setReturnData(getService().toPageModule(id, physicalGroupId));
    	} catch (Exception e) {
    		log.error("转换自定义构件配置信息出错", e);
		}
    	
    	return NONE;
    }
}
