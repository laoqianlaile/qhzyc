package com.ces.config.action.base;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.rest.DefaultHttpHeaders;
import org.springframework.data.domain.Page;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.Assert;

import ces.workflow.graph.Palette;
import ces.workflow.graph.stat.ProcessTrack;
import ces.workflow.util.WFTransaction;
import ces.workflow.wapi.ActionLog;
import ces.workflow.wapi.ActivityInstance;
import ces.workflow.wapi.AdminAPI;
import ces.workflow.wapi.ClientAPI;
import ces.workflow.wapi.Coflow;
import ces.workflow.wapi.ProcessInstance;
import ces.workflow.wapi.WFContext;
import ces.workflow.wapi.WFException;
import ces.workflow.wapi.define.DefineService;
import ces.workflow.wapi.org.WFOrgAccess;
import ces.workflow.wapi.status.WFStatus;

import com.ces.config.dao.base.ShowModuleStringIDDao;
import com.ces.config.datamodel.message.MessageModel;
import com.ces.config.datamodel.page.DefaultPageModel;
import com.ces.config.datamodel.page.utils.FramePageModuleHandling;
import com.ces.config.dhtmlx.entity.appmanage.AppReport;
import com.ces.config.dhtmlx.entity.appmanage.Module;
import com.ces.config.dhtmlx.entity.appmanage.TreeDefine;
import com.ces.config.dhtmlx.json.entity.appmanage.GridData;
import com.ces.config.dhtmlx.service.appmanage.AppReportService;
import com.ces.config.dhtmlx.service.appmanage.TreeDefineService;
import com.ces.config.dhtmlx.service.component.ComponentVersionService;
import com.ces.config.scheme.DefaultDocumentScheme;
import com.ces.config.service.base.ShowModuleDefineDaoService;
import com.ces.config.service.base.ShowModuleDefineDaoService.SearchParameter;
import com.ces.config.utils.AppDefineUtil;
import com.ces.config.utils.CommonUtil;
import com.ces.config.utils.ConstantVar;
import com.ces.config.utils.FileUtil;
import com.ces.config.utils.ModuleUtil;
import com.ces.config.utils.StringUtil;
import com.ces.config.utils.WorkflowUtil;
import com.ces.xarch.core.entity.StringIDEntity;
import com.ces.xarch.core.exception.BusinessException;
import com.ces.xarch.core.exception.FatalException;
import com.ces.xarch.core.logger.Logger;
import com.ces.xarch.core.plugin.coflow.WFActionLog;
import com.ces.xarch.core.security.entity.SysUser;
import com.ces.xarch.core.web.frame.FrameDataModel;
import com.ces.xarch.plugins.coflow.graph.XarchProcessTrack;
import com.ces.xarch.plugins.coflow.graph.svg.SvgUtils;
import com.google.common.collect.Maps;

/**
 * <p>描述: 自定义模块展现Controller基础类</p>
 * <p>公司: 上海中信信息发展股份有限公司</p>
 * @author qiucs   
 * @date 2014-3-11 下午6:02:16
 *
 * @param <T>
 * @param <Service>
 * @param <Dao>
 */
@Result(name="coflowGraph",location="/WEB-INF/views/coflow/graph.jsp")
public class ShowModuleDefineServiceDaoController<T extends StringIDEntity, Service extends ShowModuleDefineDaoService<T, Dao>, Dao extends ShowModuleStringIDDao<T>> 
            extends StringIDConfigDefineServiceDaoController<T, Service, Dao> {

    private static final long serialVersionUID = -1295053646724834541L;
    
    private static Log log = LogFactory.getLog(ShowModuleDefineServiceDaoController.class);
    /* 参数名称前缀.*/
    protected static final String PARAM_PRE = "P_";
    /* 表ID参数名称.*/
    protected static final String P_TABLE_ID  = "P_tableId";
    /* 表名后缀参数名称.*/
    protected static final String P_TABLE_SUFFIX  = "P_tableSuffix";
    /* 模块ID参数名称.*/
    protected static final String P_MODULE_ID = "P_moduleId";
    /* 版本构件ID参数名称.*/
    protected static final String P_COMPONENT_VERSION_ID = "P_componentVersionId";
    /* 版本构件ID参数名称.*/
    protected static final String P_MENU_ID   = "P_menuId";
    /* 查询字段参数名称.*/
    protected static final String P_COLUMNS   = "P_columns";
    /* 查询字段类型参数名称.*/
    protected static final String P_DATATYPES = "P_datatypes";
    /* 查询字段编码类型值参数名称.*/
    protected static final String P_CODETYPES = "P_codetypes";
    /* 查询字段类型.*/
    protected static final String P_TYPES = "P_types";
    /* 链接地址.*/
    protected static final String P_URLS  = "P_urls";
    /* 查询过滤条件参数名称.*/
    protected static final String P_SEARCH_FILTER = "P_filter";
    /* 主表ID参数名称.*/
    protected static final String P_M_TABLE_ID = "P_M_tableId";
    /* 主表数据ID参数名称.*/
    protected static final String P_M_DATA_ID  = "P_M_dataId";
    /* 从表ID参数名称.*/
    protected static final String P_D_TABLE_IDS= "P_D_tableIds";
    /* 工作流ID参数名称.*/
    protected static final String P_WORKFLOW_ID= "P_workflowId";
    /* 操作参数名称.*/
    protected static final String P_OP  = "P_op";
    /* 工作箱参数名称.*/
    protected static final String P_BOX = "P_box";
    /* 流程实例参数名称.*/
    protected static final String P_PROCESS_INSTANCE_ID  = "P_processInstanceId";
    /* 工作项参数名称.*/
    protected static final String P_WORKITEM_ID   = "P_workitemId";
    /* 提交工作项相关参数名称.*/
    protected static final String P_PERFORMER_IDS = "P_performers";
    /* 节点ID参数名称.*/
    protected static final String P_ACTIVITY_ID   = "P_activityId";
    /* 提交下个节点ID参数名称.*/
    protected static final String P_NEXT_ACTIVITY_ID  = "P_nextActivityId";
    /* 时间戳参数名称.*/
    protected static final String P_TIMESTAMP  = "P_timestamp";
    /* UI列表构件参数名称.*/
    protected static final String P_CGRID_DIV_ID  = "P_cgridDivId";
    /* 工作项参数名称.*/
    protected static final String P_OPINION  = "P_opinion";
    /* 数据模型：代码项下拉框.*/
    protected static final String E_MODEL_NAME_CODE = "code";
    /* 数据模型：用户.*/
    protected static final String E_MODEL_NAME_USER = "user";
    /* 数据模型：组织.*/
    protected static final String E_MODEL_NAME_ORG  = "org";
    /* 参数名-表单元素JSON值.*/
    protected static final String E_ENTITY_JSON  = "E_entityJson";
    /* 参数名-明细列表数据JSON值.*/
    protected static final String E_D_ENTITIES_JSON  = "E_dEntitiesJson";
    
    private String uploadifyFileName; // 上传文件名称
    private File uploadify;           // 上传文件对象
    
    /**
     * qiucs 2014-8-6 
     * <p>描述: </p>
     * @return String    返回类型   
     */
    @Action(value="/ces/Q1", results={@Result(name=SUCCESS,location="/WEB-INF/views/toui/index.jsp")})
    public Object toUI() {
        try {
            String moduleId = getParameter(P_MODULE_ID);
            String uiType   = getParameter("P_uiType");
            String layoutType= getParameter("P_layoutType");
            String cvId     = ModuleUtil.getComponentVersionId(moduleId);
            String groupId  = getParameter("P_groupId");
            String tableId  = getParameter("P_tableId");
            String columns  = getParameter("P_columns");
            String url      = null;
            if (StringUtil.isEmpty(uiType)) uiType = Module.UI_CORAL;
            if (StringUtil.isNotEmpty(cvId)) {
                url = (Module.UI_DHTMLX.equals(uiType) ? ConstantVar.UI.DHX_FOLDER : ConstantVar.UI.CUI_FOLDER) 
                        + "/views/" + getService(ComponentVersionService.class).getByID(cvId).getUrl();
            }
            //if (StringUtil.isEmpty(url)) {
                url  = (Module.UI_DHTMLX.equals(uiType) ? ConstantVar.UI.DHX_FOLDER : ConstantVar.UI.CUI_FOLDER) 
                        + "/views/" + AppDefineUtil.getUrl(layoutType, moduleId, uiType);
            //}
                
            if (StringUtil.isNotEmpty(groupId)) url += "&P_groupId=" + groupId;
            if (StringUtil.isNotEmpty(tableId)) url += "&P_tableId=" + tableId;
            if (StringUtil.isNotEmpty(columns)) url += "&P_columns=" + columns;
            
            if (Module.UI_DHTMLX.equals(uiType)) {
                getRequest().getRequestDispatcher(url).forward(getRequest(), getResponse());
                return NONE;
            } else {
                getRequest().setAttribute("url", url);
            }
        } catch (Exception e) {
            log.error("组件库UI展现出错", e);
        }
        
        return new DefaultHttpHeaders(SUCCESS).disableCaching();
    }
    
    /**
     * qiucs 2013-9-12 
     * <p>描述: 保存</p>
     */
    @Logger(model = "[${P_menuId}]", action = "保存", logger = "保存内容：${E_entityJson}|3")
    public Object save() throws FatalException {
        try {
            // 获取表ID
            String tableId   = getParameter(P_TABLE_ID);
            String entityJson = getParameter(E_ENTITY_JSON);
            // 保存数据
            Map<String, String> dataMap = getService().save(tableId, entityJson, getMarkParamMap());
            String id = dataMap.get(ConstantVar.ColumnName.ID);
            String op = getParameter(P_OP);
            /*if ("business".equals(op)) {
            	// dataMap为业务处理后的数据，直接返回到前台
                setReturnData(dataMap);
            } else */
            if ("save".equals(op)) {
            	// 从数据库重新查出来（防止有触发器更新数据）
                setReturnData(getService().getById(id, tableId, dataMap));
            } else {
                setReturnData(dataMap);
            }
        } catch (Exception e) {
        	processException(e, BusinessException.class);
        	log.error("保存出错", e);
        }
        
        return NONE;
    }
    
    /**
     * qiucs 2015-2-28 上午9:32:16
     * <p>描述: 保存主表及从列表信息 </p>
     * @return Object
     */
    @Logger(model = "[${P_menuId}]", action = "级联保存", logger = "保存内容：${E_entityJson}|3")
    public Object saveAll() {
    	try {
			String entityJson = getParameter(E_ENTITY_JSON);
			String tableId = getParameter(P_TABLE_ID);
			String dEntitiesJson = getParameter(E_D_ENTITIES_JSON);
			String dTableId = getParameter(P_D_TABLE_IDS);
			setReturnData(getService().saveAll(tableId, entityJson, dTableId, dEntitiesJson, getMarkParamMap()));
		} catch (Exception e) {
			processException(e, BusinessException.class);
			log.error("保存主表及从列表信息", e);
		}
    	
    	return NONE;
    }
    
    /**
     * qiucs 2013-9-12 
     * <p>描述: 查看</p>
     */
    @Logger(model = "[${P_menuId}]", action = "查看", logger = "查看记录ID：${id}|5")
    public Object show() throws FatalException {
        try {
            // 1. 获取表ID,模块ID,ID
            String tableId  = getParameter(P_TABLE_ID);
            String componentVersionId = getParameter(P_COMPONENT_VERSION_ID);
            String menuId = getParameter(P_MENU_ID);
            String workflowId = getParameter(P_WORKFLOW_ID);
            String processInstanceId = getParameter(P_PROCESS_INSTANCE_ID);
            String id       = getId();
            setReturnData(getService().getFormData(tableId, componentVersionId, menuId, id, workflowId, processInstanceId));
        } catch (Exception e) {
        	log.error("查看出错", e);
        }
        return new DefaultHttpHeaders(SUCCESS).disableCaching();
    }
    
    /**
     * qiucs 2013-9-13 
     * <p>描述: 列表查询</p>
     */
    @Logger(model = "[${P_menuId}]", action = "查询", logger = "列表查询|4")
    public Object search() throws FatalException {
        try {
            long start = System.currentTimeMillis();
            // 查询参数
            SearchParameter parameter = getSearchParameter();
            //
            if (this.FRAME_NAME_DHTMLX.equals(getFrameName())) {
                list = new GridData<T>();
            } else {
                list = getDataModel(getModelTemplate());
                processFilter(list);
            }
            Page<Object> page = getService().search(parameter);
            list.setData(page);
            log.debug("search total time: " + (System.currentTimeMillis() - start)  + " 毫秒.");
        } catch (Exception e) {
            log.error("列表查询出错", e);
        }
        
        return new DefaultHttpHeaders(SUCCESS).disableCaching();
    }

    /**
	 * qiucs 2015-3-9 上午11:56:07
	 * <p>描述: 查询参数构件 </p>
	 * @return SearchParameter
	 */
	protected SearchParameter getSearchParameter() {
		return getService().makeupSearchParameter(
				getParameter(P_TABLE_ID),
		        /* 表名后缀.*/
				getParameter(P_TABLE_SUFFIX),
		        /* 模块ID.*/
				getParameter(P_MODULE_ID),
		        /* 版本构件ID.*/
				getParameter(P_COMPONENT_VERSION_ID),
		        /* 菜单ID.*/
		        getParameter(P_MENU_ID),
		        /* 列表对应字段.*/
		        getParameter(P_COLUMNS),
		        /* 字段对应的数据类型.*/
		        getParameter(P_DATATYPES),
		        /* 字段对编码的值.*/
		        getParameter(P_CODETYPES),
		        /* 字段字段类型.*/
		        getParameter(P_TYPES),
		        /* 字段字段类型.*/
		        getParameter(P_URLS),
		        /* 前台查询过滤条件.*/
		        getParameter(P_SEARCH_FILTER),
		        /* 主表ID.*/
		        getParameter(P_M_TABLE_ID),
		        /* 主列表选中的记录ID.*/
		        getParameter(P_M_DATA_ID),
		        /* 工作流ID.*/
		        getParameter(P_WORKFLOW_ID),
		        /* 工作箱.*/
		        getParameter(P_BOX),
		        /* 分页信息.*/
		        buildPageRequest(),
		        /* 生成自定义构件是的时间戳.*/
		        getParameter(P_TIMESTAMP),
		        /* 生成自定义构件是的时间戳.*/
		        getParameter(P_CGRID_DIV_ID),
		        /* 生成自定义列表构件ID.*/
		        getMarkParamMap()
			);
	}

	/*
     * (非 Javadoc)   
     * <p>描述: 删除</p>   
     * @return
     * @throws FatalException   
     * @see com.ces.xarch.core.web.struts2.BaseController#destroy()
     */
    @Logger(model = "[${P_menuId}]", action = "删除", logger = "删除记录ID：${id}|1")
    public Object destroy() throws FatalException {
        
        try {
            // 1. 获取表ID, ID
            String tableId  = getParameter(P_TABLE_ID);
            String dTableId = getParameter(P_D_TABLE_IDS);
            String ids      = getId();
            boolean isLogicalDelete   = StringUtil.isBooleanTrue(getParameter("P_isLogicalDelete"));
        	getService().delete(tableId, dTableId, ids, isLogicalDelete, getMarkParamMap());
        } catch (Exception e) {
            processException(e, BusinessException.class);
            log.error("删除出错", e);
        }        
        
        return new DefaultHttpHeaders(SUCCESS).disableCaching();
    }
    
    
    /* (non-Javadoc)
     * @see com.ces.config.action.base.StringIDConfigDefineServiceDaoController#processTree()
     */
    @Override
	protected void processTree() throws FatalException {
    	list = getDataModel(DATA_MODEL_TREE);
        processFilter(list);
		Map<String, Object> paramMap = getMarkParamMap();
        /*paramMap.put("moduleId", getParameter("P_moduleId"));
        paramMap.put("menuId", getParameter("P_menuId"));
        paramMap.put("showNodeCount", getParameter("P_showNodeCount"));*/
        String parentId = getId();
        List<TreeDefine> items = getService(TreeDefineService.class).getTreeNodes(parentId, paramMap);
        // 树节点过滤处理
        items = processTreeData(items, paramMap);
        // 树节点权限处理
        items = processNodeAuthority(items, paramMap);
        // 
        list.setData(items);
        // 
        if (FRAME_NAME_CORAL.equals(getFrameName())) {
            if (null == list.getData()) setReturnData(new ArrayList<TreeDefine>());
            else setReturnData(list.getData());
        }
	}
    
    /**
     * qiucs 2014-12-18 下午4:32:04
     * <p>描述: 树节点过滤处理（二次开发使用） </p>
     * @return List<TreeDefine>
     */
    protected List<TreeDefine> processTreeData(List<TreeDefine> list, Map<String, Object> paramMap) {
    	
    	return list;
    }
    
    /**
     * qiucs 2014-12-18 下午4:43:56
     * <p>描述: 树节点权限处理 </p>
     * @return List<TreeDefine>
     */
    protected List<TreeDefine> processNodeAuthority(List<TreeDefine> list, Map<String, Object> paramMap) {
    	if (null == list || list.isEmpty()) return list;
        String treeIds = getParameter("P_treeIds");
        if (StringUtil.isNotEmpty(treeIds)) {
            for (int i = list.size() - 1; i > -1; i--) {
                TreeDefine tree = list.get(i);
                /*if (!(("1".equals(tree.getDynamic()) && treeIds.indexOf(tree.getDynamicFromId()) > -1) 
                        || ("1".equals(tree.getDynamic()) && treeIds.indexOf(tree.getId()) > -1 ) 
                        || treeIds.indexOf(tree.getId()) > -1)) {*/
                String treeId = tree.getId();
                if (treeId.indexOf("_") != -1) {
                    treeId = treeId.substring(0, treeId.indexOf("_"));
                }
                if (treeIds.indexOf(treeId) == -1) {
                    list.remove(i);
                }
            }
        }
    	return list;
    }

	/**
     * qiucs 2014-9-9 
     * <p>描述: 批量修改</p>
     * @return Object    返回类型   
     * @throws
     */
    @Logger(model = "[${P_menuId}]", action = "批量修改", logger = "修改内容（表ID：${P_tableId}，字段信息：${P_fields}）|3")
    public Object batchUpdate() {
        try {
            String tableId = getParameter(P_TABLE_ID);
            String moduleId= getParameter(P_MODULE_ID);
            String scope   = getParameter("P_scope");
            String ids     = getParameter("P_ids");
            String fields  = getParameter("P_fields");
            String timestamp = getParameter(P_TIMESTAMP);
            setReturnData(getService().batchUpdate(moduleId, tableId, scope, ids, fields, timestamp, getMarkParamMap()));
        } catch (Exception e) {
            log.error("批量修改出错", e);
        }
        
        return NONE;
    }
    
    /**
     * qiucs 2013-9-12 
     * <p>描述: 工作流流转</p>
     */
    @Logger(model = "[${P_menuId}]", action = "流程流转", logger = "流转流程ID: ${P_workflowId}|5")
    public Object coflow() {
        try {
            Map<String, Object> rMap = Maps.newHashMap(); 
            String op = getParameter(P_OP);
            String opinion = getParameter(P_OPINION);
            // 获取表ID
            String tableId = getParameter(P_TABLE_ID);
            // 流程定义ID
            String workflowId = getParameter(P_WORKFLOW_ID);
            // 根据流程定义ID获取对应的表ID（如果表ID没传入时）
            if (StringUtil.isEmpty(tableId) && StringUtil.isNotEmpty(workflowId)) {
            	tableId = WorkflowUtil.getBusinessTableId(workflowId);            			
            }
            // 获取工作项ID
            String workitemIdStr = getParameter(P_WORKITEM_ID);
            workitemIdStr = StringUtil.null2zero(workitemIdStr);
            if (WorkflowUtil.Op.start.equals(op) || WorkflowUtil.Op.complete.equals(op)) {
                // 把表单数据及参数存放在Map中
            	String entityJson = getParameter(E_ENTITY_JSON);
            	if (StringUtil.isNotEmpty(entityJson)) {
            		Map<String, String> dataMap = getService().save(tableId, entityJson, getMarkParamMap());
            		// 保存数据
            		String id = dataMap.get(ConstantVar.ColumnName.ID);
            		model.setId(id);
            	}
            }
            long workitemId = getService().runCoflow(workflowId, 
            		tableId, 
            		model.getId(), 
            		op, 
            		Long.parseLong(workitemIdStr), 
            		getWfContext(workitemIdStr, op), 
            		opinion);
            rMap.put("ID", model.getId());
            rMap.put("workitemId", workitemId);
            setReturnData(rMap);
        } catch (Exception e) {
        	processException(e, WFException.class, BusinessException.class);
            log.error("工作流操作出错", e);
        }
        
        return SUCCESS;
    }
    /**
     * qiucs 2013-9-12 
     * <p>描述: 把表单数据与参数存放在Map中</p>
     */
    protected Map<String, String> getDataMap() {
        Map<String, String> dMap = Maps.newHashMap();
        HttpServletRequest request = getRequest();
        Enumeration<String> cenum = request.getParameterNames();
        while (cenum.hasMoreElements()) {
            String col = (String) cenum.nextElement();
            if (col.startsWith(PARAM_PRE) ||
            		WorkflowUtil.isOpinionColumnName(col)) continue;
            String val = request.getParameter(col);
            dMap.put(col, val);
        }
        return dMap;
    }
    
    /**
     * qiucs 2015-2-28 上午9:45:16
     * <p>描述: 获取以“P_”打头参数  </p>
     * @return Map<String, Object>
     *         --key为参数去掉“P_”：如果P_tableId，则key=tableId
     *         --value: 如果只一个值，则为String; 如果多个值，则为String[]
     */
    protected Map<String, Object> getMarkParamMap() {
    	Map<String, Object> pMap = new HashMap<String, Object>();
    	
    	Map<String, String[]> reqMap = getRequest().getParameterMap();    	
    	Set<String> keySet = reqMap.keySet();    	
    	Iterator<String> it = keySet.iterator();
    	int beginIdx = PARAM_PRE.length();
    	String[] valArr = null;
    	while (it.hasNext()) {
			String key = (String) it.next();
			if (key.startsWith(PARAM_PRE)) {
				valArr = reqMap.get(key);
				if (null != valArr && 1 == valArr.length) {
					pMap.put(key.substring(beginIdx), valArr[0]);
				} else {
					pMap.put(key.substring(beginIdx), valArr);
					if  (null != valArr && valArr.length > 1) {
						log.warn("参数（" + key + "）有多个值：" + Arrays.toString(valArr));
					}
				}
			}
		}
    	
    	return pMap;
    }
    
    /**
     * qiucs 2015-3-3 下午5:22:24
     * <p>描述: 把符合指定Exception的错误消息返回到前台 </p>
     * @return void
     */
    protected void processException(Exception throwE, Class<?>... clazzes) {
    	Throwable e = throwE;
    	Set<Class<?>> clazzSet = new HashSet<Class<?>>();
    	for (Class<?> ec : clazzes) clazzSet.add(ec);
    	do {
    		if (clazzSet.contains(e.getClass())) {
    			setReturnData(MessageModel.falseInstance(e.getMessage()));
    			break;
    		}
    		e = e.getCause();
			
		} while(null != e);
    }
    /**
     * qiucs 2013-9-12 
     * <p>描述: </p>
     */
    protected HttpServletRequest getRequest() {
        return ServletActionContext.getRequest();
    }
    /**
     * qiucs 2013-9-12 
     * <p>描述: </p>
     */
    protected HttpServletResponse getResponse() {
        return ServletActionContext.getResponse();
    }
    
    /**
     * qiucs 2015-8-21 上午11:16:01
     * <p>描述: 测试 </p>
     * @return Object
     */
    public Object test() {
        return MessageModel.trueInstance("OK");
    }
    
    public WFContext getWfContext(String workitemId, String op) {
        WFContext context = new WFContext();
        try {
            if (WorkflowUtil.Op.complete.equals(op)) {
                String performerIds = getParameter(P_PERFORMER_IDS);
                if (!StringUtil.isEmpty(performerIds)) {
                    String seperator = "^_^";
                    StringTokenizer to = new StringTokenizer(performerIds, ",");
                    while (to.hasMoreTokens()) {
                        String performers = to.nextToken();
                        if (!"".equals(performers)) {
                            String activityId = performers.substring(0, performers.indexOf(seperator));
                            String performerId = performers.substring(performers.indexOf(seperator) + seperator.length());
                            context.addUserSelect(activityId, performerId);
                        }
                    }
                }
                context.setCurrentUserId(CommonUtil.getUser().getId());
                String summary = CommonUtil.getUser().getName() + ":" + CommonUtil.getUser().getId();
                context.setNote(summary);
                context.setSummary(summary);
            } else if (WorkflowUtil.Op.deliver.equals(op) || WorkflowUtil.Op.reassign.equals(op)) {
                context.put("targetUserIds", getParameter("P_targetUserIds"));
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }
        return context;
    }
    
    /**
     * qiucs 2014-8-13 
     * <p>描述: 获取手动提交到下一节点信息</p>
     * @return Object    返回类型   
     */
    public Object coflowNextStep () {
        
        try {
            String workitemIdStr = getParameter(P_WORKITEM_ID);
            setReturnData(getService().coflowNextStep(Long.parseLong(workitemIdStr)));
        } catch (Exception e) {
            log.error("获取手动提交到下一节点信息出错", e);
        }
        
        return NONE;
    }
    
    /**
     * qiucs 2014-8-13 
     * <p>描述: 判断当前工作项是否为人工控制</p>
     * @return Object    返回类型   
     * @throws
     */
    public Object isUserControl() {
        try {
            String workitemId = getParameter(P_WORKITEM_ID);
            setReturnData(getService().isUserControl(Long.parseLong(StringUtil.null2zero(workitemId))));
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }
        
        return NONE;
    }
    public Object currentActivity() {
    	try {
    		String workitemId = getParameter(P_WORKITEM_ID);
    		setReturnData(getService().getCurrentActivityCanChange(Long.parseLong(StringUtil.null2zero(workitemId))));
    	} catch (Exception e) {
    		e.printStackTrace();
    		throw new RuntimeException(e.getMessage());
    	}
    	
    	return NONE;
    }
    public Object nextActivityPerformer() {
        try {
            String workitemId = getParameter(P_WORKITEM_ID);
            setReturnData(getService().getNextActivityPerformer(Long.parseLong(StringUtil.null2zero(workitemId))));
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }
        
        return NONE;
    }
    public Object backActivityPerformer() {
        try {
            String workitemId = getParameter(P_WORKITEM_ID);
            setReturnData(getService().getBackActivityPerformer(Long.parseLong(StringUtil.null2zero(workitemId))));
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }
        
        return NONE;
    }
    
    /**
     * qiucs 2013-10-23 
     * <p>描述: 从主表中取与明细表关系字段的值</p>
     * @return Object    返回类型   
     * @throws
     */
    public Object relationData() {
        try {
            String tableId       = getParameter(P_TABLE_ID);   // detail table id
            String masterTableId = getParameter(P_M_TABLE_ID); // master table id 
            String masterDataId  = getParameter(P_M_DATA_ID);  // master data  id
            String inheritItems  = getParameter("P_inheritItems");
            setReturnData(getService().getRelationData(tableId, masterTableId, masterDataId, inheritItems));
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }
        
        return NONE;
    }

    /*
     * qiucs 2015-8-21 上午11:16:56
     * <p>描述: 流程跟踪 </p>
     * (non-Javadoc)
     * @see com.ces.xarch.core.web.struts2.BaseController#coflowTrack()
     */
	@Action(results={@Result(name="coflowGraph",location="/WEB-INF/views/coflow/graph.jsp")})
    public Object coflowTrack() throws FatalException {
        try {
            String processInstanceId = getParameter("P_processInstanceId");
            String activityInstanceId = getParameter("P_activityInstanceId");
            String op = getParameter(P_OP);
            if (op == "graph" || "graph".equals(op)) {
                ServletActionContext.getRequest().setAttribute("processInstanceId", processInstanceId);
                return "coflowGraph";
            } else if (op == "logs" || "logs".equals(op)) {
                FrameDataModel<WFActionLog, String> list = getDataModel(WFActionLog.class, null, getModelTemplate());
                processFilter(list);
                if (null != processInstanceId && !"".equals(processInstanceId)) {
                    list.setData(getActivityLogsByProcessInstanceId(Long.valueOf(processInstanceId)));
                } else if (null != activityInstanceId && !"".equals(activityInstanceId)) {
                    list.setData(getActivityLogsByActivityInstanceId(Long.valueOf(activityInstanceId)));
                }
                setReturnData(list);
                return new DefaultHttpHeaders("coflow-log").disableCaching();
            } else {
                setReturnData(graph(op, Long.valueOf(processInstanceId)));
                return new DefaultHttpHeaders("coflow-graph").disableCaching();
            }
        } catch (Exception ex) {
            throw new FatalException("工作流操作失败", ex);
        }
    }

    public String graph(String op, long processInstanceId) throws Exception {
        Assert.isTrue(processInstanceId != 0, "获取流程图时必须指定流程实例ID");
        WFTransaction t = WFTransaction.begin();
        String monitor = "";
        try {
            if (op == "svgGraph" || "svgGraph".equals(op)) {
                XarchProcessTrack processTrack = new XarchProcessTrack(processInstanceId, true);
                Palette palette = processTrack.generatePalette(10, 10);
                monitor = SvgUtils.svgtop() + SvgUtils.defs() + palette.getCode() + SvgUtils.svgbuttom();
            } else if (op == "vmlGraph" || "vmlGraph".equals(op)) {
                monitor += "<v:group ID=\"coflow-graph\" style=\"position:absolute;top:0;left:0;WIDTH:1000px;HEIGHT:1300px;\" coordsize =\"1000, 1000\">";
                ProcessTrack p = new ProcessTrack(processInstanceId, true);
                Palette pl = new Palette();
                pl.ACTIVITY_LOG_URL = "javascript:loadLog('CFAIID');void(0);";
                pl.ACTIVITY_LOG_PRE_TAG = "<center style='font-size:9pt;color:blue'>";
                pl.ACTIVITY_LOG_URL_TARGET = "";
                pl = p.generatePalette(pl, 10, 10);
                monitor += pl.getCode();
                monitor += "</v:group>";
            }
            t.commit();
        } catch (Exception e) {
            throw e;
        }
        return monitor;
    }
	 
	public List<WFActionLog> getActivityLogsByProcessInstanceId(long processInstanceId) throws Exception {
		SysUser sysUser = ((SysUser)SecurityContextHolder.getContext().getAuthentication().getPrincipal());
		String userId = sysUser.getId();
		AdminAPI admin = Coflow.getAdminAPI(userId);
		DefineService defineService = Coflow.getDefineService();
		WFOrgAccess orgAccess = Coflow.getOrgAccess();
		ProcessInstance ProcessInstance = admin.getProcessInstance(processInstanceId);			
		String packageId = ProcessInstance.getPackageId();
		String processId = ProcessInstance.getProcessId();
		String processNmae = defineService.getProcess(packageId, processId).getName();
		List<WFActionLog> result = new ArrayList<WFActionLog>();
		WFActionLog log = null;
		ActionLog[] als = null;
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			//获得流程实例的操作日志列表
			als = admin.listActionLogs(processInstanceId);
		} catch(WFException e) {
			e.printStackTrace();
		}
		for (int i = 0; i < als.length; i++) {
			ActionLog actionLog = (ActionLog) als[i];
			String activityId = actionLog.getActivityId() != null ? actionLog.getActivityId() : "";
			String performerId = actionLog.getPerformerId() != null? actionLog.getPerformerId() : "";
			String actionName = actionLog.getActionName() != null ? actionLog.getActionName() : null;
			String actionTime = actionLog.getActionTime() != null ? df.format(actionLog.getActionTime()) : null;
			String actionType = WFStatus.getStatus(actionLog.getActionType()).getName();
			String note = actionLog.getNote() !=null ? actionLog.getNote() : "" ;
			String activityName = defineService.getActivity(packageId, processId, activityId) != null ? defineService.getActivity(packageId, processId, activityId).getName() : null;
			String performerName = null;
			if (performerId != null && !"".equals(performerId)) {
				if (performerId.equals(String.valueOf(Integer.MIN_VALUE)) || "-1".equals(performerId)) {
					performerName = "<font color=red>系统</font>";
				} else {
					performerName = orgAccess.getUserById(performerId) != null ? orgAccess.getUserById(performerId).getName():"";
				}
			}
			log = new WFActionLog();
			log.setId(actionLog.getId()+"");
			log.setActivityName(activityName);
			log.setProcessNmae(processNmae);
			log.setPerformerName(performerName);
			log.setActionName(actionName);
			log.setActionTime(actionTime);
			log.setActionType(actionType);
			log.setNote(note);
			result.add(log);
		}		
		return result;
	}
	public List<WFActionLog> getActivityLogsByActivityInstanceId(long activityInstanceId) throws Exception {
		SysUser sysUser = ((SysUser)SecurityContextHolder.getContext().getAuthentication().getPrincipal());
		String userId = sysUser.getId();
		AdminAPI admin = Coflow.getAdminAPI(userId);
		DefineService defineService = Coflow.getDefineService();
		WFOrgAccess orgAccess = Coflow.getOrgAccess();
		ClientAPI clientAPI = Coflow.getClientAPI(userId);
		ActivityInstance activityInstance = clientAPI.getActivityInstance(activityInstanceId);
		ProcessInstance processInstance = admin.getProcessInstance(activityInstance.getProcessInstanceId());
		String processNmae = defineService.getProcess(processInstance.getPackageId(), processInstance.getProcessId()).getName();
		List<WFActionLog> result = new ArrayList<WFActionLog>();
		WFActionLog log = null;
		ActionLog[] als = null;
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			//获得流程实例的操作日志列表
			als = admin.listActionLogsByActivity(activityInstanceId);
		} catch(WFException e) {
			e.printStackTrace();
		}
		for (int i = 0; i < als.length; i++) {
			ActionLog actionLog = (ActionLog) als[i];
			String activityId = actionLog.getActivityId() != null ? actionLog.getActivityId() : "";
			String performerId = actionLog.getPerformerId() != null? actionLog.getPerformerId() : "";
			String actionName = actionLog.getActionName() != null ? actionLog.getActionName() : null;
			String actionTime = actionLog.getActionTime() != null ? df.format(actionLog.getActionTime()) : null;
			String actionType = WFStatus.getStatus(actionLog.getActionType()).getName();
			String note = actionLog.getNote() !=null ? actionLog.getNote() : "" ;
			String activityName = defineService.getActivity(processInstance.getPackageId(), processInstance.getProcessId(), activityId) != null ? defineService.getActivity(processInstance.getPackageId(), processInstance.getProcessId(), activityId).getName() : null;
			String performerName = null;
			if (performerId != null && !"".equals(performerId)) {
				if (performerId.equals(String.valueOf(Integer.MIN_VALUE)) || "-1".equals(performerId)) {
					performerName = "<font color=red>系统</font>";
				} else {
					performerName = orgAccess.getUserById(performerId) != null ? orgAccess.getUserById(performerId).getName():"";
				}
			}
			log = new WFActionLog();
			log.setId(actionLog.getId()+"");
			log.setActivityName(activityName);
			log.setProcessNmae(processNmae);
			log.setPerformerName(performerName);
			log.setActionName(actionName);
			log.setActionTime(actionTime);
			log.setActionType(actionType);
			log.setNote(note);
			result.add(log);
		}		
		return result;
	}

	/**
     * 获取上一次查询的查询条件
     * 
     * @return Object
     */
    public Object getQueryCondition() {
    	String tableId   = getParameter(P_TABLE_ID);
        String timestamp = getParameter(P_TIMESTAMP);
        setReturnData(CommonUtil.getQueryFilter(tableId + timestamp));
        return new DefaultHttpHeaders(SUCCESS).disableCaching();
    }
    
    /**
     * qiucs 2014-7-14 
     * <p>描述: 生成自定义页面配置前台UI数据模型.</p>
     * @return Object    返回类型   
     */
    public Object page() {
        long start = System.currentTimeMillis();
        try {
            if (E_MODEL_NAME_CODE.equals(getModelTemplate()) ||
                    E_MODEL_NAME_USER.equals(getModelTemplate())||
                    E_MODEL_NAME_ORG.equals(getModelTemplate())) {
                setReturnData(getPageModel(getModelTemplate()).getData());
            } else {
                setReturnData(getPageModel(getModelTemplate()));
            }
        } catch (Exception e) {
            log.error("生成自定义页面配置前台UI数据模型出错", e);
        }
        log.info("page(" + getModelTemplate() + ") total time: " + (System.currentTimeMillis() - start) + " 毫秒.");
        return NONE;
    }
    
    /**
     * qiucs 2014-7-14 
     * <p>描述: 获取自定义页面配置数据模型.</p>
     * @return DefaultPageModel    返回类型   
     */
    protected DefaultPageModel getPageModel(String modelName) {
        DefaultPageModel pageModel = FramePageModuleHandling.getPageModel(getFrameName(), modelName, getModelClass(), getId());
        pageModel.setTableId(getParameter(P_TABLE_ID));
        pageModel.setModuleId(getParameter(P_MODULE_ID));
        pageModel.setComponentVersionId(getParameter(P_COMPONENT_VERSION_ID));
        pageModel.setMenuId(getParameter(P_MENU_ID));
        pageModel.setWorkflowId(getParameter(P_WORKFLOW_ID));
        pageModel.setBox(getParameter(P_BOX));
        pageModel.init();
        return pageModel;
    }
    
    /**
     * qiucs 2014-8-22 
     * <p>描述: 缩略图</p>
     * @return Object    返回类型   
     * @throws
     */
    public Object thumbnail() {
        HttpServletResponse response = ServletActionContext.getResponse();
        FileInputStream fis = null;
        ServletOutputStream ops = null;
        String id      = getId();
        String tableId = getParameter("P_tableId");
        try {
            DefaultDocumentScheme doc = getService().getDocumentScheme(tableId, id);
            String filePath = null, fileName = null;//doc.getFileFormat().toLowerCase();
            File file = null;
            if (null != doc) {
            	filePath = doc.getFilePath();
            	fileName = doc.getOriginName();
            	file = new File(filePath);
            }
            if (null == file || !file.exists()) {
                filePath = CommonUtil.getAppRootPath() + "cfg-resource/coral40/common/css/images/default-thumb.png";
                file = new File(filePath);
            	fileName = file.getName();
            }
            //System.out.println("filePath: " + filePath);
            //System.out.println("fileName: " + fileName);
            response.setHeader("Content-Disposition", "inline;filename=\"" + fileName + "\"");
            response.setContentType("application/octet-stream; charset=GBK");
            response.setHeader("Content-Length", String.valueOf(file.length()));

            fis = new FileInputStream(file);
            ops = response.getOutputStream();
            int n = 0;
            byte b[] = new byte[1024 * 8];
            while ((n = fis.read(b)) != -1 && ops != null) {
                ops.write(b, 0, n);
            }
            ops.flush();
        } catch (IOException e) {
            log.error("获取缩略图出错", e);
        } finally {
            try {
                if (fis != null)
                    fis.close();
                if (ops != null)
                    ops.close();
            } catch (Exception e) {
                fis = null;
                ops = null;
                log.error("获取缩略图时，关闭IO流出错", e);
            }
        }
        return NONE;
    }
    
    /**
     * qiucs 2015-3-8 下午9:35:17
     * <p>描述: 获取附件表 </p>
     * @return Object
     */
    public Object findDocumentTableId() {
    	try {
			setReturnData(getService().findDocumentTableId(getParameter(P_TABLE_ID)));
		} catch (Exception e) {
			log.error("获取附件表出错", e);
		}
    	return NONE;
    }
    
    /**
     * qiucs 2015-3-16 下午3:51:49
     * <p>描述: 获取明细表表ID </p>
     * @return Object
     */
    public Object findDetailTableId() {
    	try {
			setReturnData(getService().findDetailTableId(getParameter(P_TABLE_ID)));
		} catch (Exception e) {
			log.error("获取附件表出错", e);
		}
    	return NONE;
    }
    
    /**
     * qiucs 2014-10-15 
     * <p>描述: 电子文件上传</p>
     * @return Object    返回类型   
     * @throws
     */
    public Object upload() {
        try {
            String docTableId = getParameter("P_docTableId"); // 附件表表ID
            String tableId    = getParameter(P_TABLE_ID);     // 列表对应的表ID
            String relateId   = getId(); // 关联数据ID
            getService().docUpload(relateId, uploadify, uploadifyFileName, docTableId, tableId, getMarkParamMap());
        } catch (Exception e) {
            log.error("文件上传失败", e);
        }
        return NONE;
    }
    
    /**
     * qiucs 2014-10-15 
     * <p>描述: 电子文件上传校验</p>
     * @return Object    返回类型   
     * @throws
     */
    public Object checkUpload () {
        try {
            String tableId = getParameter(P_TABLE_ID);
            setReturnData(getService().checkUpload(tableId));
        } catch (Exception e) {
            log.error("文件上传校验出错", e);
        }
        
        return NONE;
    }

    /**
     * <p>描述: 获取可用的报表.</p>
     * @return Object    返回类型   
     */
    public Object report() {
        long start = System.currentTimeMillis();
        try {
            String tableId = getParameter("P_tableId");
            String componentVersionId = getParameter("P_componentVersionId");
            String menuId = getParameter("P_menuId");
            String userId = CommonUtil.getUser().getId();
            List<AppReport> appReportList = getService(AppReportService.class).findDefineList(tableId, componentVersionId, menuId, userId);
            List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
            if (CollectionUtils.isNotEmpty(appReportList)) {
                Map<String, Object> map = null;
                AppReport appReport = null;
                int i = 0, len = appReportList.size();
                for (; i < len; i++) {
                    map = new HashMap<String, Object>();
                    appReport = appReportList.get(i);
                    map.put("text", appReport.getShowName());
                    map.put("value", appReport.getReportId());
                    if (0 == i) {
                    	map.put("selected", Boolean.TRUE);
                    }
                    list.add(map);
                }
            }
            setReturnData(list);
        } catch (Exception e) {
            log.error("获取可用的报表", e);
        }
        log.info("report total time: " + (System.currentTimeMillis() - start) + " 毫秒.");
        return NONE;
    }
    
    /**
     * qiucs 2015-1-9 下午5:03:13
     * <p>描述: 工作流待办箱按钮设置 </p>
     * @return Object
     */
    public Object coflowButtonSetting() {
    	
    	try {
			String workflowId = getParameter(P_WORKFLOW_ID);
			String workitemId = getParameter(P_WORKITEM_ID);
			String activityId = getParameter(P_ACTIVITY_ID);
			setReturnData(getService().getCoflowButtonSetting(workflowId, workitemId, activityId));
		} catch (Exception e) {
			log.error("获取工作流待办箱按钮设置", e);
		}
    	
    	return NONE;
    }
    
    /**
     * qiucs 2015-5-14 下午4:35:34
     * <p>描述: 查看文件 </p>
     * @return String
     */
    public String viewDocument() {
        //System.out.println("view=======================");
        HttpServletResponse response = ServletActionContext.getResponse();
        FileInputStream fis = null;
        //DecryptFileInputStream fis = null;
        ServletOutputStream ops = null;
        String id      = getId();
        String tableId = getParameter(P_TABLE_ID);
        try {
        	DefaultDocumentScheme doc = getService().getDocumentScheme(tableId, id);
        	String filePath = doc.getFilePath();
            String format   = doc.getFileFormat().toLowerCase();//*/
            /*String filePath = "D:/test.pdf";
            String format   = "pdf";*/
            String contentType = FileUtil.getMimeType(format);
            File f = new File(filePath);
            //System.out.println(f.getAbsolutePath()+"  "+f.getPath()+"  "+f.getName());
            if (!f.exists()) {
                response.sendRedirect("common/404.jsp");
                return NONE;
            }
            String fileName = doc.getOriginName().concat(".").concat(format);
            response.setHeader("Content-Disposition", "inline;filename=" + fileName);
            if (!"pdf".equals(format)) {
                if (contentType != null)
                    response.setContentType(contentType + "; charset=GBK");
                else
                    response.setContentType("application/octet-stream; charset=GBK");
            }
            response.setHeader("Content-Length", String.valueOf(f.length()));

            fis = new FileInputStream(f);
            //fis = new DecryptFileInputStream(f,obj);
            ops = response.getOutputStream();
            int n = 0;
            byte b[] = new byte[1024 * 8];
            while ((n = fis.read(b)) != -1 && ops != null) {
                ops.write(b, 0, n);
            }
            ops.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (fis != null)
                    fis.close();
                if (ops != null)
                    ops.close();
            } catch (Exception ee) {
                fis = null;
                ops = null;
            }
        }
        return NONE;
    }
    
    /**
     * qiucs 2015-5-15 上午11:08:53
     * <p>描述: TODO(这里用一句话描述这个方法的作用) </p>
     * @return Object
     */
    public Object documentOptions() {
    	try {
			String tableId = getParameter(P_TABLE_ID);
			String masterDataId = getId();
			setReturnData(getService().getDocumentOptions(tableId, masterDataId));
		} catch (Exception e) {
			log.error("获取附件下拉数据出错", e);
		}
    	
    	return NONE;
    }

    public String getUploadifyFileName() {
        return uploadifyFileName;
    }

    public void setUploadifyFileName(String uploadifyFileName) {
        this.uploadifyFileName = uploadifyFileName;
    }

    public File getUploadify() {
        return uploadify;
    }

    public void setUploadify(File uploadify) {
        this.uploadify = uploadify;
    }
}
