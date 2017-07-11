package com.ces.config.service.base;

import java.io.File;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.ServletActionContext;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import ces.workflow.core.dao.model.DataField;
import ces.workflow.wapi.Authorization;
import ces.workflow.wapi.ClientAPI;
import ces.workflow.wapi.Coflow;
import ces.workflow.wapi.ProcessInstance;
import ces.workflow.wapi.WFContext;
import ces.workflow.wapi.WFException;
import ces.workflow.wapi.WFFilter;
import ces.workflow.wapi.WFQuery;
import ces.workflow.wapi.Workitem;
import ces.workflow.wapi.define.Activity;
import ces.workflow.wapi.define.DefineService;
import ces.workflow.wapi.define.Transition;
import ces.workflow.wapi.define.WFDefineConstant;
import ces.workflow.wapi.define.WFDefineException;
import ces.workflow.wapi.define.WorkflowProcess;
import ces.workflow.wapi.status.WFStatus;

import com.ces.config.dao.base.ShowModuleStringIDDao;
import com.ces.config.datamodel.message.MessageModel;
import com.ces.config.datamodel.page.coral.GridPageModel;
import com.ces.config.dhtmlx.dao.common.DatabaseHandlerDao;
import com.ces.config.dhtmlx.entity.appmanage.AppButton;
import com.ces.config.dhtmlx.entity.appmanage.AppDefine;
import com.ces.config.dhtmlx.entity.appmanage.AppFormElement;
import com.ces.config.dhtmlx.entity.appmanage.AppGrid;
import com.ces.config.dhtmlx.entity.appmanage.CoflowOpinion;
import com.ces.config.dhtmlx.entity.appmanage.PhysicalTableDefine;
import com.ces.config.dhtmlx.entity.appmanage.TableRelation;
import com.ces.config.dhtmlx.entity.appmanage.TreeDefine;
import com.ces.config.dhtmlx.entity.appmanage.WorkflowConfirmOpinion;
import com.ces.config.dhtmlx.entity.appmanage.WorkflowDefine;
import com.ces.config.dhtmlx.entity.appmanage.WorkflowVersion;
import com.ces.config.dhtmlx.entity.component.ComponentVersion;
import com.ces.config.dhtmlx.json.entity.common.DhtmlxTreeNode;
import com.ces.config.dhtmlx.service.appmanage.AppFormElementService;
import com.ces.config.dhtmlx.service.appmanage.ColumnDefineService;
import com.ces.config.dhtmlx.service.appmanage.PhysicalTableDefineService;
import com.ces.config.dhtmlx.service.appmanage.TableRelationService;
import com.ces.config.dhtmlx.service.appmanage.WorkflowConfirmOpinionService;
import com.ces.config.dhtmlx.service.appmanage.WorkflowDefineService;
import com.ces.config.dhtmlx.service.appmanage.WorkflowVersionService;
import com.ces.config.dhtmlx.service.component.ComponentVersionService;
import com.ces.config.dhtmlx.service.menu.MenuService;
import com.ces.config.scheme.DefaultDocumentScheme;
import com.ces.config.utils.AppDefineUtil;
import com.ces.config.utils.CommonUtil;
import com.ces.config.utils.ConstantVar;
import com.ces.config.utils.DateUtil;
import com.ces.config.utils.IndexCommonUtil;
import com.ces.config.utils.JsonUtil;
import com.ces.config.utils.StringUtil;
import com.ces.config.utils.TableUtil;
import com.ces.config.utils.UUIDGenerator;
import com.ces.config.utils.WorkflowUtil;
import com.ces.config.utils.authority.AuthorityUtil;
import com.ces.xarch.core.entity.StringIDEntity;
import com.ces.xarch.core.exception.BusinessException;
import com.ces.xarch.core.security.entity.SysUser;
import com.ces.xarch.core.utils.SearchHelper;
import com.ces.xarch.core.web.listener.XarchListener;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
/**
 * <p>描述: 自定义模块展现Service基础类</p>
 * <p>公司: 上海中信信息发展股份有限公司</p>
 * @author qiucs
 * @date 2014-3-11 下午6:03:27
 *
 * @param <T>
 * @param <Dao>
 */
@Component
public class ShowModuleDefineDaoService<T extends StringIDEntity, Dao extends ShowModuleStringIDDao<T>> extends StringIDConfigDefineDaoService<T, Dao>{

    private static Log log = LogFactory.getLog(ShowModuleDefineDaoService.class);

    private final String processInstanceId = WorkflowUtil.Alias.processInstanceId/*.toUpperCase()*/;
    private final String workitemId        = WorkflowUtil.Alias.workitemId/*.toUpperCase()*/;
    private final String workitemStatus    = WorkflowUtil.Alias.workitemStatus/*.toUpperCase()*/;
    private final String workitemActivityId= WorkflowUtil.Alias.workitemActivityId/*.toUpperCase()*/;
    private final String workitemOwnerId   = WorkflowUtil.Alias.workitemOwnerId/*.toUpperCase()*/;
    // 流程ID在parametter中的key名称
    private final String WF_PROCESS_ID = "_WF_PROCESS_ID_";
    // 查询方式在parametter中的key名称
    protected final String P_SEARCH_TYPE = "searchType";


    /**
     * qiucs 2015-3-12 下午4:59:40
     * <p>描述: 表单保存 </p>
     * @param paramMap --参数Map（具体参数要求请查看ShowModuleDefineServiceDaoController.getMarkParamMap方法说明）
     * @return String
     */
    @Transactional
    public Map<String, String> save(String tableId, String entityJson, Map<String, Object> paramMap) {
        String tableName = getTableName(tableId);
        JsonNode entityNode = JsonUtil.json2node(entityJson);
        Map<String, String> dataMap = node2map(entityNode);
        String id = save(tableName, dataMap, paramMap);
        dataMap.put(AppDefineUtil.C_ID, id);
        return dataMap;
    }

    /**
     * qiucs 2013-9-12 
     * <p>描述: 表单保存</p>
     * @return String    返回类型   
     */
    @Transactional
    public String save(String tableName, Map<String, String> dataMap, Map<String, Object> paramMap) {
        // 1. 获取表名
        if (StringUtil.isEmpty(tableName)) return "";
        // 2. 保存前，业务处理接口
        processBeforeSave(tableName, dataMap, paramMap);
        // 3. 根据ID判断是新增，还是修改
        String id = saveOne(tableName, dataMap);
        // 保存后，业务处理接口
        processAfterSave(tableName, dataMap, paramMap);
        return id;
    }

    @Transactional
    protected String saveOne(String tableName, Map<String, String> dataMap) {
        String id = dataMap.get(AppDefineUtil.C_ID);
        if (StringUtil.isEmpty(id)) {
            // insert  
            id = UUIDGenerator.uuid();
            dataMap.put(AppDefineUtil.C_ID, id);
            insert(tableName, dataMap);
        } else {
            // update 
            update(tableName, dataMap);
        }
        return id;
    }

    /**
     * qiucs 2014-10-21 
     * <p>描述: 保存前，二次开发业务处理接口</p>
     * @param  tableName
     * @param  dataMap
     * @return Map<String,String>    返回类型   
     * @throws
     */
    @Transactional
    protected void processBeforeSave(String tableName, Map<String, String> dataMap, Map<String, Object> paramMap) {
        // dataMap中 key 为字段名，如ID/NAME...；value 为字段对应的值
        // dataMap.put("USER_NAME", "qiucs")
    }

    /**
     * qiucs 2014-10-21 
     * <p>描述: 保存后，二次开发业务处理接口</p>
     * @param  tableName
     * @param  dataMap    设定参数   
     * @return void    返回类型   
     * @throws
     */
    @Transactional
    protected void processAfterSave(String tableName, Map<String, String> dataMap, Map<String, Object> paramMap) {

    }


    /* 往指定表插入数据 */
    @Transactional
    protected void insert(String tableName, Map<String, String> dataMap) {
        String tableId = TableUtil.getTableId(tableName);
        if (StringUtil.isNotEmpty(tableId)) {
            long cnt = 0;
            if (!dataMap.containsKey("CREATE_USER")) {
                cnt = getService(ColumnDefineService.class).count("EQ_columnName=CREATE_USER;EQ_tableId=" + tableId);
                if (cnt > 0) {
                    dataMap.put("CREATE_USER", CommonUtil.getCurrentUserId());
                }
            }
            if (!dataMap.containsKey("CREATE_TIME")) {
                cnt = getService(ColumnDefineService.class).count("EQ_columnName=CREATE_TIME;EQ_tableId=" + tableId);
                if (cnt > 0) {
                    dataMap.put("CREATE_TIME", DateUtil.currentTime());
                }
            }
        }
        Set<String> set = dataMap.keySet();
        Iterator<String> it = set.iterator();
        StringBuffer cSql = new StringBuffer("");
        StringBuffer vSql = new StringBuffer("");
        while(it.hasNext()) {
            String key = it.next();
            String val = dataMap.get(key);
            cSql.append(",").append(key);
            vSql.append(",'").append(StringUtil.null2empty(val)).append("'");
        }
        StringBuffer sql = new StringBuffer();
        sql.append("insert into ")
                .append(tableName).append("(").append(cSql.substring(1)).append(") values (")
                .append(vSql.substring(1)).append(")");
        //System.out.println(sql);
        DatabaseHandlerDao dao = DatabaseHandlerDao.getInstance();
        dao.executeSql(sql.toString());
    }
    /** 根据ID修改指定表中数据 */
    @Transactional
    protected void update(String tableName, Map<String, String> dataMap) {
        // 
        String id = dataMap.get(AppDefineUtil.C_ID);
        dataMap.remove(AppDefineUtil.C_ID);
        //
        String tableId = TableUtil.getTableId(tableName);
        if (StringUtil.isNotEmpty(tableId)) {
            long cnt = 0;
            if (!dataMap.containsKey("UPDATE_USER")) {
                cnt = getService(ColumnDefineService.class).count("EQ_columnName=UPDATE_USER;EQ_tableId=" + tableId);
                if (cnt > 0) {
                    dataMap.put("UPDATE_USER", CommonUtil.getCurrentUserId());
                }
            }
            if (!dataMap.containsKey("UPDATE_TIME")) {
                cnt = getService(ColumnDefineService.class).count("EQ_columnName=UPDATE_TIME;EQ_tableId=" + tableId);
                if (cnt > 0) {
                    dataMap.put("UPDATE_TIME", DateUtil.currentTime());
                }
            }
        }
        //
        Set<String> set = dataMap.keySet();
        Iterator<String> it = set.iterator();
        StringBuffer cSql = new StringBuffer("");
        while(it.hasNext()) {
            String key = it.next();
            String val = dataMap.get(key);
            cSql.append(",").append(key).append("='").append(StringUtil.null2empty(val)).append("'");
        }

        StringBuffer sql = new StringBuffer();
        sql.append("update ")
                .append(tableName).append(" set ").append(cSql.substring(1))
                .append(" where ")
                .append(AppDefineUtil.C_ID).append("='").append(id).append("'");
        //System.out.println(sql);
        DatabaseHandlerDao dao = DatabaseHandlerDao.getInstance();
        dao.executeSql(sql.toString());

        dataMap.put(AppDefineUtil.C_ID, id);
    }

    /**
     * qiucs 2015-3-10 下午11:03:00
     * <p>描述: 单个保存 </p>
     * @return void
     */
    @Transactional
    protected void saveOne(String tableName, Map<String, String> dataMap, boolean inserted) {
        if (inserted) {
            // insert  
            insert(tableName, dataMap);
        } else {
            // update 
            update(tableName, dataMap);
        }
    }

    /**
     * qiucs 2015-2-28 上午11:25:58
     * <p>描述: 保存主从表的所有数据 </p>
     * @return Object
     */
    @Transactional
    public Object saveAll(String tableId, String entityJson, String dTableId, String dEntitiesJson, Map<String, Object> paramMap) {
        Map<String, Object> returnData = new HashMap<String, Object>();
        boolean inserted = false;
        JsonNode entity = JsonUtil.json2node(entityJson);
        Map<String, String> dataMap = node2map(entity);
        if (StringUtil.isEmpty(dataMap.get(AppDefineUtil.C_ID))) {
            inserted = true;
            dataMap.put(AppDefineUtil.C_ID, UUIDGenerator.uuid());
        }
        Map<String, String> relateDateMap = getRelateDateMap(tableId, dTableId, dataMap);
        String tableName = getTableName(tableId), dTableName = getTableName(dTableId);
        // 保存明细记录
        List<Map<String, String>> detailList   = saveDetail(dTableName, dEntitiesJson, dataMap, relateDateMap, paramMap);
        // 保存明细后业务逻辑处理
        processMiddleSaveAll(tableName, dTableName, dataMap, detailList, paramMap);
        // 保存主表记录
        saveOne(tableName, dataMap, inserted);
        // 保存主表和明细后业务逻辑处理
        processAfterSaveAll(tableName, dTableName, dataMap, detailList, paramMap);
        //
        returnData.put("master", dataMap);
        returnData.put("detail", detailList);

        return MessageModel.trueInstance(returnData);
    }

    /**
     * qiucs 2015-2-28 上午11:24:30
     * <p>描述: 保存明细表数据 </p>
     * @return Object
     */
    @Transactional
    protected List<Map<String, String>> saveDetail(String tableName, String entitiesJson,
                                                   Map<String, String> masterMap,
                                                   Map<String, String> relateDateMap,
                                                   Map<String, Object> paramMap) {
        List<Map<String, String>> dList = new ArrayList<Map<String, String>>();

        String id = null;
        JsonNode entities = JsonUtil.json2node(entitiesJson);
        Map<String, String> dataMap = null;
        StringBuilder sb = new StringBuilder();
        for (int i = 0, len = entities.size(); i < len; i++) {
            dataMap = node2map(entities.get(i));
            dataMap.putAll(relateDateMap);
            id = dataMap.get(AppDefineUtil.C_ID);
            if (StringUtil.isNotEmpty(id) && id.startsWith("UNSAVE_")) {
                dataMap.remove(AppDefineUtil.C_ID);
            }
            processBeforeSaveOneDetail(tableName, dataMap, masterMap, paramMap);
            id = saveOne(tableName, dataMap);
            sb.append(",'").append(id).append("'");
            processAfterSaveOneDetail(tableName, dataMap, masterMap, paramMap);
            dList.add(dataMap);
        }
        if (sb.length() > 0) {
            sb.deleteCharAt(0);
            deleteOverDetail(tableName, sb.toString(), relateDateMap);
        }
        return dList;
    }

    /**
     * qiucs 2015-3-11 上午10:02:29
     * <p>描述: 保存单条明细前业务逻辑处理 </p>
     * @return void
     */
    protected void processBeforeSaveOneDetail(String tableName, /*明细表表英文名称*/
                                              Map<String, String> dataMap,   /*明细表数据集*/
                                              Map<String, String> masterMap, /*主表数据集*/
                                              Map<String, Object> paramMap   /*预留参数数据集*/) {}

    /**
     * qiucs 2015-3-11 上午10:08:15
     * <p>描述: 保存单条明细后业务逻辑处理  </p>
     * @return void
     */
    protected void processAfterSaveOneDetail(String tableName, /*明细表表英文名称*/
                                             Map<String, String> dataMap,   /*明细表数据集*/
                                             Map<String, String> masterMap, /*主表数据集*/
                                             Map<String, Object> paramMap   /*预留参数数据集*/) {}

    /**
     * qiucs 2015-3-11 上午10:11:01
     * <p>描述: 保存条明细后与保存主表前之间的业务逻辑处理 </p>
     * @return void
     */
    protected void processMiddleSaveAll(String tableName/*主表表英文名称*/, String dTableName/*明细表表英文名称*/,
                                        Map<String, String> masterMap,        /*主表数据集*/
                                        List<Map<String, String>> detailList, /*所有明细表数据集*/
                                        Map<String, Object> paramMap          /*预留参数数据集*/) {}

    /**
     * qiucs 2015-3-11 上午10:11:01
     * <p>描述: 保存明细和主表之后的业务逻辑处理 </p>
     * @return void
     */
    protected void processAfterSaveAll(String tableName/*主表表英文名称*/, String dTableName/*明细表表英文名称*/,
                                       Map<String, String> masterMap,        /*主表数据集*/
                                       List<Map<String, String>> detailList, /*所有明细表数据集*/
                                       Map<String, Object> paramMap          /*预留参数数据集*/) {}

    /**
     * qiucs 2015-3-10 下午8:51:27
     * <p>描述: 删除多余的明细记录 </p>
     * @return void
     */
    @Transactional
    protected void deleteOverDetail(String tableName, String ids, Map<String, String> relateDateMap) {
        StringBuilder sb = new StringBuilder();
        sb.append("DELETE FROM ").append(tableName).append(" WHERE ");
        Set<String> keySet  = relateDateMap.keySet();
        Iterator<String> it = keySet.iterator();
        while (it.hasNext()) {
            String key = (String) it.next();
            sb.append(key).append("=").append("'").append(relateDateMap.get(key)).append("'").append(AppDefineUtil.RELATION_AND);
        }
        sb.append("ID NOT IN(").append(ids).append(")");
        DatabaseHandlerDao.getInstance().executeSql(sb.toString());
    }

    /**
     * qiucs 2015-2-28 上午11:24:51
     * <p>描述: 获取从表与主表关联关系的字段值 </p>
     * @return Map<String,String>
     */
    private Map<String, String> getRelateDateMap(String tableId, String dTableId, Map<String, String> dMap) {
        Map<String, List<String>> relationMap = TableUtil.getTableRelation(tableId, dTableId);
        if (null == relationMap || relationMap.isEmpty()) {
            throw new BusinessException("未获取到表（" + TableUtil.getTableName(tableId) + "）与表（" + TableUtil.getTableName(dTableId) + "）之间的关系字段，请检查这两张表的表关系配置！");
        }
        List<String> mList = relationMap.get(tableId);
        List<String> dList = relationMap.get(dTableId);

        Map<String, String> relateDateMap = new HashMap<String, String>();

        for (int i = 0, len = mList.size(); i < len; i++) {
            relateDateMap.put(dList.get(i), dMap.get(mList.get(i)));
        }

        return relateDateMap;
    }

    /**
     * qiucs 2015-2-28 上午11:23:29
     * <p>描述: 将JsonNode转换为键值对 </p>
     * @return Map<String,String>
     */
    protected Map<String, String> node2map(JsonNode node) {
        Map<String, String> dMap = new HashMap<String, String>();
        Iterator<String> it = node.fieldNames();
        while (it.hasNext()) {
            String col = (String) it.next();
            dMap.put(col, node.get(col).asText());
        }
        return dMap;
    }

    /**
     * qiucs 2013-12-19 
     * <p>标题: getById</p>
     * <p>描述: 根据ID取数据</p>
     * @param  id
     * @param  tableName
     * @param  dataMap
     * @return Object    返回类型   
     * @throws
     */
    public Object getById(String id, String tableId, Map<String, String> dataMap) {
        String tableName = getTableName(tableId);
        dataMap.put(AppDefineUtil.C_ID, id);
        Set<String> set = dataMap.keySet();
        Iterator<String> it = set.iterator();
        StringBuffer colsbuffer = new StringBuffer("");
        List<String> colList = new ArrayList<String>();

        while(it.hasNext()) {
            String key = it.next();
            colsbuffer.append(",a_.").append(key);
            colList.add(key);
        }
        dataMap.clear();
        colsbuffer.deleteCharAt(0);
        StringBuffer sql = new StringBuffer("select ").append(colsbuffer)
                .append(" from ").append(tableName).append(" a_ where ").append("a_.ID='").append(id).append("'");
        /*List<Object[]> rlt = DatabaseHandlerDao.getInstance().queryForList(String.valueOf(sql));
        if (null != rlt && !rlt.isEmpty()) {
            if (colList.size() == 1) {
                dataMap.put(colList.get(0), StringUtil.null2empty(rlt.get(0)));
            } else {
                Object[] obj = rlt.get(0);
                for (int i = 0; i < obj.length; i++) {
                    dataMap.put(colList.get(i), StringUtil.null2empty(obj[i]));
                }
            }
            
            dataMap.put(AppDefineUtil.C_ID, id);
        }
        return dataMap;*/
        /*RowMapHandlerImpl handler = new RowMapHandlerImpl();
        DatabaseHandlerDao.getInstance().jdbcQuery(String.valueOf(sql), handler);
        return handler.getSingleData();*/
        return DatabaseHandlerDao.getInstance().queryForMap(String.valueOf(sql));
    }
    /**
     * qiucs 2013-9-18 
     * <p>标题: getFormData</p>
     * <p>描述: 根据ID查看数据</p>
     * @param  tableId
     * @param  moduleId
     * @param  id
     * @return Object    返回类型   
     * @throws
     */
    public Object getFormData(String tableId, String componentVersionId, String menuId, String id, String workflowId, String processInstanceId) {
        // 1. 获取表名
        String tableName = getTableName(tableId);
        // 2. 
        if (StringUtil.isNotEmpty(workflowId)) {
            // 工作流应用定义规则
            componentVersionId = AppDefine.DEFAULT_DEFINE_ID;
            // WorkflowVersion.id
            if (StringUtil.isNotEmpty(processInstanceId)) {
                menuId = XarchListener.getBean(WorkflowVersionService.class).getFormVersionId(workflowId, processInstanceId);
            } else {
                menuId = XarchListener.getBean(WorkflowVersionService.class).getRunningVersionId(workflowId);
            }
        }
        List<AppFormElement> elements = getService(AppFormElementService.class).findDefineList(tableId, componentVersionId, menuId);
        StringBuffer colstr  = new StringBuffer(AppDefineUtil.C_ID);
        List<String> colList = new ArrayList<String>();
        colList.add(AppDefineUtil.C_ID);
        for (AppFormElement element : elements) {
            if (AppFormElement.SUBFIELD_ID.equals(element.getColumnId()) ||
                    AppFormElement.PLACEHOLDER_ID.equals(element.getColumnId())) continue;

            colstr.append(",").append(element.getColumnName());
            colList.add(element.getColumnName());
        }
        elements.clear(); elements = null;
        String sql = "select " + colstr.toString() + " from " + tableName + " where ID='" + id + "'";
        return DatabaseHandlerDao.getInstance().queryForMap(sql);
    }

    /**
     * qiucs 2013-10-23 
     * <p>描述: </p>
     * @param  tableId
     * @param  masterTableId
     * @param  masterDataId
     * @return Object    返回类型   
     * @throws
     */
    @SuppressWarnings("unchecked")
    public Object getRelationData(String tableId, String masterTableId, String masterDataId, String inheritItems) {
        Map<String, Object> rlt = new HashMap<String, Object>();
        try {
            // 表关系字段
            Map<String, List<String>> rMap = TableUtil.getTableRelation(masterTableId, tableId);
            List<String> mList = rMap.get(masterTableId);
            List<String> dList = rMap.get(tableId);
            String masterTableName = getTableName(masterTableId);
            String cols = "";
            for (String col : mList) {
                cols += ",t." + col;
            }
            // 继承字段
            String[] inheritColArr = null;
            if (StringUtil.isNotEmpty(inheritItems)) {
                Map<String, String> inheritMap = getService(ColumnDefineService.class).getInheritColumnMap(tableId, masterTableId);
                inheritColArr = inheritItems.split(",");
                String inheritCol = null;
                for (int i = 0, len = inheritColArr.length; i < len; i++) {
                    inheritCol = inheritColArr[i];
                    if (inheritMap.containsKey(inheritCol)) {
                        cols += ",t." + inheritMap.get(inheritCol);
                    } else {
                        cols += ",'' as " + inheritCol;
                        log.warn("从表（" + TableUtil.getTableName(tableId) + "）中字段（" + inheritCol
                                + "）在主表（" + TableUtil.getTableName(masterTableId)
                                + "）中未找到对应的继承字段，请检查！");
                    }
                }
            }

            String sql = "select " + cols.substring(1) + " from " + masterTableName + " t where t.ID='" + masterDataId + "'";

            if (1 == mList.size() && null == inheritColArr) {
                List<Object> list = DatabaseHandlerDao.getInstance().queryForList(sql);
                Object obj = list.get(0);
                rlt.put(dList.get(0), obj);
            } else {
                List<Object[]> list = DatabaseHandlerDao.getInstance().queryForList(sql);
                Object[] obj = list.get(0);
                for (int i = 0; i < obj.length; i++) {
                    if (i < dList.size()) {
                        rlt.put(dList.get(i), obj[i]);
                    } else {
                        rlt.put(inheritColArr[i - dList.size()], obj[i]);
                    }
                }
            }
        } catch (Exception e) {
            log.error("获取表单对应的主表关系数据和继承数据出错", e);
        }
        return rlt;
    }

    /**
     * qiucs 2014-9-9 
     * <p>描述: 批量修改</p>
     * @param  tableId
     * @param  scope
     * @param  ids
     * @param  fields
     * @return MessageModel    返回类型   
     * @throws
     */
    @Transactional
    public MessageModel batchUpdate(String moduleId, String tableId, String scope, String ids, String fields, String timestamp, Map<String, Object> paramMap) {
        String message = "";
        try {
            String tableName = getTableName(tableId);
            ObjectMapper mapper = new ObjectMapper();
            JsonNode array = mapper.readTree(fields);
            String filterTag = (tableId + timestamp);
            String filter  = getUpdateFilter(scope, ids, filterTag);
            int total = 0;

            for (JsonNode node : array) {
                String mode = node.get("updateMode").asText();
                if ("fixed".equals(mode)) {
                    total = fixedValue(tableName, filter, node, paramMap);
                } else if ("find".equals(mode)) {
                    total = findValue(tableName, filter, node, paramMap);
                } else if ("step".equals(mode)) {
                    total = stepValue(tableName, filter, node, filterTag, paramMap);
                }
                message += getUpdateModeName(mode) + "成功更新(" + total + ")条;";
            }
        } catch (Exception e) {
            log.error("批量修改出错(service)", e);
        }
        return MessageModel.trueInstance(message);
    }

    /**
     * qiucs 2014-9-16 
     * <p>描述: 批量修改方式中文名称</p>
     * @return String    返回类型   
     * @throws
     */
    protected String getUpdateModeName(String mode) {
        if ("fixed".equals(mode)) return "固定替换";
        if ("find".equals(mode))  return "查找替换";
        if ("step".equals(mode))  return "等差替换";
        return "";
    }
    /**
     * qiucs 2014-9-16 
     * <p>描述: 批量修改过滤条件</p>
     * @return String    返回类型   
     * @throws
     */
    protected String getUpdateFilter(String scope, String ids, String filterTag) {
        if ("0".equals(scope)) {
            return ("id in('" + ids.replace(",", "','") + "')");
        }
        return CommonUtil.getQueryFilter(filterTag);
    }

    /**
     * qiucs 2014-9-11 
     * <p>描述: 固定替换</p>
     * @return int    返回类型   
     * @throws
     */
    @Transactional
    protected int fixedValue(String tableName, String filter, JsonNode node, Map<String, Object> paramMap) {
        int total = 0;
        try {
            StringBuffer sb = new StringBuffer();
            String columnName = node.get("columnName").asText();
            String newValue   = node.get("newValue").asText();
            sb.append("update ").append(tableName).append(" set ")
                    .append(columnName).append("=").append("'").append(newValue).append("'");
            if (StringUtil.isNotEmpty(filter)) {
                sb.append(" where ").append(filter);
            }
            total = DatabaseHandlerDao.getInstance().executeSql(sb.toString());
        } catch (Exception e) {
            log.error("批量更新(固定替换)出错", e);
        }

        return total;
    }

    /**
     * qiucs 2014-9-11 
     * <p>描述: 查找替换</p>
     * @return int    返回类型   
     * @throws
     */
    @Transactional
    protected int findValue(String tableName, String filter, JsonNode node, Map<String, Object> paramMap) {
        int total = 0;
        try {
            StringBuffer count = new StringBuffer();
            StringBuffer sb = new StringBuffer();
            String[] itArr = {"combobox","combotree","combogrid","checkbox","radio"};
            String columnName = node.get("columnName").asText();
            String oldValue   = node.get("oldValue").asText();
            String newValue   = node.get("newValue").asText();
            String inputType  = node.get("inputType").asText();
            if (StringUtil.isNotEmpty(oldValue)) {
                if (Arrays.asList(itArr).contains(inputType)) {
                    count.append("select count(*) from ").append(tableName);
                    count.append(" where ").append(filter);
                    count.append(" and ").append(columnName).append(" = '").append(oldValue).append("'");
                    total = Integer.parseInt(StringUtil.null2zero(DatabaseHandlerDao.getInstance().queryForObject(count.toString())));
                    sb.append("update ").append(tableName).append(" set ")
                            .append(columnName)
                            .append("=")
                            .append("'").append(newValue).append("' where " + columnName + "= '" + oldValue + "'");
                    if (StringUtil.isNotEmpty(filter)) {
                        sb.append(" and ").append(filter);
                    }
                    DatabaseHandlerDao.getInstance().executeSql(sb.toString());
                } else {
                    count.append("select count(*) from ").append(tableName);
                    count.append(" where ").append(filter);
                    count.append(" and ").append(columnName).append(" like '%").append(oldValue).append("%'");
                    total = Integer.parseInt(StringUtil.null2zero(DatabaseHandlerDao.getInstance().queryForObject(count.toString())));
                    sb.append("update ").append(tableName).append(" set ")
                            .append(columnName)
                            .append("=")
                            .append("replace(").append(columnName).append(",'").append(oldValue).append("','").append(newValue).append("')");
                    if (StringUtil.isNotEmpty(filter)) {
                        sb.append(" where ").append(filter);
                    }
                    DatabaseHandlerDao.getInstance().executeSql(sb.toString());
                }
            } else {
                sb.append("update ").append(tableName).append(" set ")
                        .append(columnName)
                        .append("=")
                        .append("'").append(newValue).append("'")
                        .append(" where ").append(columnName).append(" is null");
                if (StringUtil.isNotEmpty(filter)) {
                    sb.append(" and ").append(filter);
                }
                total = DatabaseHandlerDao.getInstance().executeSql(sb.toString());
            }
        } catch (Exception e) {
            log.error("批量更新(查找替换)出错", e);
        }
        return total;
    }

    /**
     * qiucs 2014-9-11 
     * <p>描述: 等差替换</p>
     * @return int    返回类型   
     * @throws
     */
    @Transactional
    protected int stepValue(String tableName, String filter, JsonNode node, String filterTag, Map<String, Object> paramMap) {
        int total = 0;
        String tempTableName = getTempTableName();
        try {
            int startNo = node.get("startNo").asInt();
            int step    = node.get("step").asInt();
            String columnName = node.get("columnName").asText();
            generateTempStepNo(tableName, filter, tempTableName, startNo, step, filterTag);
            total = updateStepValue(tableName, columnName, tempTableName);
        } catch (Exception e) {
            log.error("批量更新(等差替换)出错", e);
        } finally {
            dropTempStepNo(tempTableName);
        }

        return total;
    }

    /**
     * qiucs 2014-9-10 
     * <p>描述: 随机生成临时表名</p>
     * @return String    返回类型   
     * @throws
     */
    protected String getTempTableName() {
        StringBuffer sb = new StringBuffer("T_");
        sb.append(System.currentTimeMillis())
                .append("_");
        for (int i = 0; i < 4; i++) {
            sb.append(((char) (65 + Math.round(Math.random() * 25))));
        }
        return sb.toString();
    }

    /**
     * qiucs 2014-9-10 
     * <p>描述: 在临时表中生成等差系列</p>
     * @return int    返回类型   
     * @throws
     */
    protected int generateTempStepNo(String tableName, String filter, String tempTableName, int startNo, int step, String filterTag) {
        int total = 0, begin = (startNo - step);
        String sql = "", sort = CommonUtil.getQuerySort(filterTag);
        if (StringUtil.isEmpty(sort)) {
            sort = "ID desc";//默认按ID倒序
        }
        if (DatabaseHandlerDao.isOracle()) {
            StringBuffer sb = new StringBuffer();
            sb.append("create table " + tempTableName + " as select (" + begin + "+" + step + "*(row_number() over(order by " + sort + "))) as NO_, ID FROM " + tableName);
            if (StringUtil.isNotEmpty(filter)) {
                sb.append(" where ").append(filter);
            }
            sql =  sb.toString();
        } else if (DatabaseHandlerDao.isSqlserver()) {
            StringBuffer sb = new StringBuffer();
            sb.append("select (" + begin + "+" + step + "*(row_number() over(order by " + sort + "))) as NO_, ID into " + tempTableName + " FROM " + tableName);
            if (StringUtil.isNotEmpty(filter)) {
                sb.append(" where ").append(filter);
            }
            sql =  sb.toString();
        }
        if (StringUtil.isNotEmpty(sql)) {
            // create table and copy data
            total = DatabaseHandlerDao.getInstance().executeSql(sql);
            // add primary key constraint
            DatabaseHandlerDao.getInstance().executeSql("alter table " + tempTableName + " add constraint PK" + tempTableName.substring(1) + " primary key(ID)");
        }
        return total;
    }

    /**
     * qiucs 2014-9-10 
     * <p>描述: 删除临时表</p>
     * @throws
     */
    protected void dropTempStepNo(String tableName) {
        if (DatabaseHandlerDao.getInstance().tableExists(tableName)) {
            DatabaseHandlerDao.getInstance().executeSql("drop table " + tableName);
        }
    }

    /**
     * qiucs 2014-9-12 
     * <p>描述: 把临时表生成好的数据更新到相应记录中</p>
     * @return int    返回类型   
     * @throws
     */
    @Transactional
    protected int updateStepValue(String tableName, String columnName,  String tempTableName) {
        int total = 0;
        StringBuffer sb = new StringBuffer();
        if (DatabaseHandlerDao.isOracle()) {
            sb.append("update (select t1_.").append(columnName).append(", t2_.no_ from ")
                    .append(tableName).append(" t1_, ").append(tempTableName).append(" t2_ where t1_.id=t2_.id) ")
                    .append("set ").append(columnName).append("=").append("no_");
        } else if (DatabaseHandlerDao.isSqlserver()) {
            sb.append("update t1_ set t1_.").append(columnName).append("=t2_.no_ from ")
                    .append(tableName).append(" t1_, ").append(tempTableName).append(" t2_ where t1_.id=t2_.id ");
        }
        if (sb.length() > 0) {
            total = DatabaseHandlerDao.getInstance().executeSql(sb.toString());
        }
        return total;
    }

    /**
     * qiucs 2014-1-9 
     * <p>描述: 过滤条件【列表自身配置的过滤、当前用户的数据权限】</p>
     */
    public String buildControlFilter(String tableId, String componentVersionId, String moduleId, String menuId, Map<String, Object> paramMap) {
        StringBuffer filter = new StringBuffer();
        // 1. 列表自身配置的过滤、当前用户的数据权限
        String topComVersionId = StringUtil.null2empty(paramMap.get("topComVersionId"));
        String treeNodeId = StringUtil.null2empty(paramMap.get("treeNodeId"));
        String masterTableId = StringUtil.null2empty(paramMap.get("M_tableId"));
        String masterDataId = StringUtil.null2empty(paramMap.get("M_dataId"));
        filter.append(AuthorityUtil.getInstance().getDataAuthority(menuId, componentVersionId, tableId, topComVersionId, treeNodeId, masterTableId, masterDataId));
        // 2. 用户二次开发自己的过滤条件
        filter.append(buildCustomerFilter(tableId, componentVersionId, moduleId, menuId, paramMap));
        return String.valueOf(filter);
    }

    /**
     * qiucs 2015-3-9 下午2:00:31
     * <p>描述: 列表查询（webservice） </p>
     * @return Page<Object>
     */
    public Page<Object> search(Map<String, Object> pMap) {
        SearchParameter parameter = makeupSearchParameter(pMap);
        return search(parameter);
    }

    /**
     * qiucs 2013-9-23 
     * <p>描述: 模块展现的列表查询</p>
     */
    public Page<Object> search(SearchParameter parameter) {
        // 
        if (parameter.isMaster() && StringUtil.isNotEmpty(parameter.box)
                && null == WorkflowUtil.getWorkflowEntity(parameter.workflowId)) {
            return new PageImpl<Object>(Lists.newArrayList(), parameter.pageable, 0L);
        }
        try {
            // 1. 获取表名
            String tableName = null;
            if (StringUtil.isNotEmpty(parameter.workflowId)) {
                WorkflowDefine flowDefine = WorkflowUtil.getWorkflowEntity(parameter.workflowId);
                String code = flowDefine.getWorkflowCode();
                tableName = WorkflowUtil.getViewName(code);
                parameter.addParamMap(WF_PROCESS_ID, WorkflowUtil.getProcessIdByCode(code));
            } else {
                tableName = getTableName(parameter.tableId);
                if (StringUtil.isNotEmpty(parameter.tableSuffix)) {
                    tableName += parameter.tableSuffix; // 查询对应的后缀表
                }
            }
            // 2. 过滤条件
            parameter = buildFilter(parameter);
            // 3. 存储过滤条件 
            storeQuery(parameter);
            // 4. 构造统计结果
            return page(parameter, tableName);
        } catch (Exception e) {
            log.error("自定义检索出错", e);
            throw new RuntimeException(e.getMessage());
        }
    }

    /**
     * qiucs 2015-5-22 下午4:06:00
     * <p>描述: 数据查询总入口 </p>
     * @return Page<Object>
     */
    protected Page<Object> page(SearchParameter parameter, String tableName) {
        Page<Object> page = null;
        // 开始数据查询
        Object st = parameter.getParamValue(P_SEARCH_TYPE);
        if (StringUtil.isEmpty(st) ||
                String.valueOf(AppGrid.ST_DATABASE).equals(st)) {
            // 数据库查询
            page = pageFromDatabase(parameter, tableName);
        } else if (String.valueOf(AppGrid.ST_INDEXLIB).equals(st)) {
            // 索引库查询
            page = pageFromIndexLib(parameter, tableName);
        } else {
            // 业务系统扩展查询接口
            page = pageFromExtend(parameter, tableName);
        }

        return page;
    }

    /**
     * qiucs 2013-9-4 上午10:15:03
     * <p>描述: 构造查询条件 </p>
     * @return SearchParameter
     */
    protected SearchParameter buildFilter(SearchParameter parameter) {
        StringBuffer filter = new StringBuffer();
        // 0. 后台控制的过滤条件
        filter.append(buildControlFilter(parameter.tableId, parameter.componentVersionId, parameter.moduleId, parameter.menuId, parameter.getParamMap()));
        // 1. 检索过滤条件
        filter.append(buildSearchFilter(parameter.searchFilter));
        // 2. 关联的主表过滤条件
        filter.append(buildMasterTable(parameter.masterTableId, parameter.tableId, parameter.masterDataId));
        // 删除第一个 AND 或 OR
        if (filter.length() > 0) {
            if (filter.indexOf(AppDefineUtil.RELATION_AND) == 0) {
                filter.delete(0, AppDefineUtil.RELATION_AND.length());
            } else if (filter.indexOf(AppDefineUtil.RELATION_OR) == 0) {
                filter.delete(0, AppDefineUtil.RELATION_OR.length());
            }
        }
        if (StringUtil.isEmpty(filter)) filter.append("1=1");
        parameter.setFilter(String.valueOf(filter));
        log.debug("filter: " + filter);
        return parameter;
    }

    /**
     * qiucs 2013-9-4 上午10:15:36
     * <p>描述: 前台传入过来的查询条件处理 </p>
     * @return String
     */
    protected String buildSearchFilter(String searchFilter) {
        if (StringUtil.isEmpty(searchFilter)) return StringUtil.EMPTY;

        StringBuffer filter = new StringBuffer();
        String[] filterItems = searchFilter.split(";");
        for (String item : filterItems) {
            filter.append(AppDefineUtil.RELATION_AND);
            if (item.indexOf("(") > -1) {
                filter.append(AppDefineUtil.processComplexFilterItem(item));
            } else {
                filter.append(AppDefineUtil.processFilterItem(item));
            }
        }
        return filter.toString();
    }

    /**
     * qiucs 2013-9-4 上午10:16:26
     * <p>描述: 关联的主表条件 </p>
     * @return String
     */
    protected String buildMasterTable(String masterTableId, String tableId, String id) {
        if (StringUtil.isEmpty(masterTableId)) return StringUtil.EMPTY;
        if (StringUtil.isEmpty(id)) return (AppDefineUtil.RELATION_AND + "1>1");
        // 1.关联表字段
        Map<String, List<String>> map = getTableRelateColumns(masterTableId, tableId);
        if (null == map || map.isEmpty()) {
            return StringUtil.EMPTY;
        }
        // 2. 主表字段值
        StringBuffer filter = new StringBuffer();
        List<String> mList = map.get(masterTableId);
        String tableName = getTableName(masterTableId);
        StringBuffer sb = new StringBuffer("SELECT ");
        for (String column : mList) {
            sb.append(column).append(", ");
        }
        sb.append(AppDefineUtil.C_ID).append(" AS MASTER_ID_ FROM ").append(tableName)
                .append(" WHERE ")
                .append(AppDefineUtil.C_ID).append("='").append(id).append("'");
        Map<String, Object> vals = DatabaseHandlerDao.getInstance().queryForMap(String.valueOf(sb));
        // 3. 拼接主表过滤条件
        List<String> cList = map.get(tableId);
        if (null != vals && !vals.isEmpty()) {
            for (int i = 0; i < cList.size(); i++) {
                filter.append(AppDefineUtil.RELATION_AND).append(cList.get(i))
                        .append("='")
                        .append(StringUtil.null2empty(vals.get(mList.get(i).toUpperCase()))).append("'");
            }
        } else {
            for (int i = 0; i < cList.size(); i++) {
                filter.append(AppDefineUtil.RELATION_AND).append(cList.get(i)).append(" IS NULL");
            }
        }
        System.out.println("master table filter: " + filter);
        return String.valueOf(filter);
    }

    protected String buildGlobalFilter(String tableId, String componentVersionId, String moduleId, String menuId, Map<String, Object> paramMap) {
        // 返回过滤条件时，要以AppDefineUtil.RELATION_AND、AppDefineUtil.RELATION_OR开头，如下所示：
        // return AppDefineUtil.RELATION_AND + "DELETE_FLAG=0";
        return "";
    }

    /**
     * qiucs 2014-10-9 
     * <p>描述: 用户二次开发时，添加自己的过滤条件接口</p>
     * @param  paramMap --其他参数，详细见ShowModuleDefineServiceDaoController.getMarkParamMap方法介绍
     * @return String    返回类型   
     * @throws
     */
    protected String buildCustomerFilter(String tableId, String componentVersionId, String moduleId, String menuId, Map<String, Object> paramMap) {
        // 返回过滤条件时，要以AppDefineUtil.RELATION_AND、AppDefineUtil.RELATION_OR开头，如下所示：
        // return AppDefineUtil.RELATION_AND + "DELETE_FLAG=0";
        return "";
    }

    /**
     * qiucs 2013-9-2 上午10:13:17
     * <p>描述: 数据库查询 </p>
     * @return Page<Object>
     */
    protected Page<Object> pageFromDatabase(SearchParameter parameter, String tableName) {
        // 统计总条数
        int total = count(parameter, tableName);
        // 分页查询
        List<Object> content = new ArrayList<Object>();
        if (total> 0) {
            content = content(parameter, tableName, total);
        }
        content = processPageData(content);
        return new PageImpl<Object>(content, parameter.pageable, total);
    }

    /**
     * qiucs 2015-2-12 下午3:06:21
     * <p>描述: 索引库查询 </p>
     * @return Page<Object>
     */
    protected Page<Object> pageFromIndexLib(SearchParameter parameter, String tableName) {

        String columns = processDataColumns(parameter);
        Page<Object> page = IndexCommonUtil.queryPage(parameter, tableName, columns);
        long total = page.getTotalElements();
        List<Object> content = processQueryData(page.getContent(), parameter, columns);

        content = processPageData(content);

        return new PageImpl<Object>(content, parameter.pageable, total);
    }

    /**
     * qiucs 2015-5-22 下午3:58:34
     * <p>描述: 扩展查询接口（第三方查询接口） </p>
     * @return Page<Object>
     */
    protected Page<Object> pageFromExtend(SearchParameter parameter, String tableName) {

        return null;
    }

    /**
     * qiucs 2015-5-7 下午6:03:58
     * <p>描述: 处理查询结果集的二次开发接口 </p>
     * @return List<Object>
     */
    protected List<Object> processPageData(List<Object> list) {
        return list;
    }

    /**
     * qiucs 2014-6-4 上午10:12:49
     * <p>描述: 存储过滤条件 </p>
     * @return void
     */
    protected void storeQuery(SearchParameter parameter) {
        ServletActionContext.getRequest().getSession().setAttribute(ConstantVar.QUERY_FILTER + parameter.tableId + parameter.timestamp, parameter.getFilter());
        ServletActionContext.getRequest().getSession().setAttribute(ConstantVar.QUERY_SORT + parameter.tableId + parameter.timestamp, parameter.getOrders());
    }

    /**
     * qiucs 2013-9-23 上午10:09:53
     * <p>描述: 统计总数 </p>
     * @return int
     */
    protected int count(SearchParameter parameter, String tableName) {
        int total = 0;
        StringBuilder sql = new StringBuilder(256),
                filter = new StringBuilder(256);

        sql.append("select count(*) as TOTAL_NUM from ").append(tableName).append(" t_ ");
        //String filter = parameter.getFilter();
        if (parameter.isMaster() && StringUtil.isNotEmpty(parameter.box)) {
            String processId = String.valueOf(parameter.getParamValue(WF_PROCESS_ID));
            if (WorkflowUtil.Box.applyfor.equals(parameter.box)) {
                filter.append(processInstanceId).append("=0")
                        .append(AppDefineUtil.RELATION_AND)
                        .append(WorkflowUtil.C_REGISTER_USER_ID).append("='").append(CommonUtil.getUser().getId()).append("'")
                        .append(AppDefineUtil.RELATION_AND);
            } else if (WorkflowUtil.Box.todo.equals(parameter.box)) {
                filter.append(workitemOwnerId).append("='").append(CommonUtil.getUser().getId()).append("'")
                        .append(AppDefineUtil.RELATION_AND)
                        .append(WorkflowUtil.Alias.processId).append("='").append(processId).append("'")
                        .append(AppDefineUtil.RELATION_AND)
                        .append(workitemStatus).append("<> 13")
                        .append(AppDefineUtil.RELATION_AND)
                        .append(workitemStatus).append("<> 14")
                        .append(AppDefineUtil.RELATION_AND)
                        .append(workitemStatus).append("<> 15")
                        .append(AppDefineUtil.RELATION_AND);
            } else {
                sql.append(" JOIN ").append(getCoflowSql(parameter.box, processId)).append(" ON(")
                        .append("    PWI_.").append(workitemId).append("=t_.").append(workitemId)
                        .append(")");
            }
        }
        filter.append(parameter.getFilter());
        sql.append(" where ").append(filter);
        Map<String, Object> rlt = DatabaseHandlerDao.getInstance().queryForMap(sql.toString());
        if (null != rlt && !rlt.isEmpty()) {
            total = Integer.parseInt(StringUtil.null2zero(rlt.get("TOTAL_NUM")));
        }
        return total;
    }

    /**
     * qiucs 2013-9-23  上午10:11:23
     * <p>描述: 查询结果 </p>
     * @return List<Object>
     */
    @SuppressWarnings({"unchecked"})
    protected List<Object> content(SearchParameter parameter, String tableName, int total) {
        PageRequest pageable = parameter.pageable;
        int begin = pageable.getOffset();
        int end   = begin + pageable.getPageSize();
        //String filter  = parameter.getFilter();
        String orders  = parameter.getOrders();
        String columns = processDataColumns(parameter);
        String col;

        String[] columnArr = columns.split(",");
        StringBuilder columnAlias = new StringBuilder();
        int len = columnArr.length;
        for (int i = 0; i < len; i++) {
            col = columnArr[i].trim();
            if (col.equalsIgnoreCase(processInstanceId) || col.equalsIgnoreCase(workitemId) ||
                    col.equalsIgnoreCase(workitemStatus) || col.equalsIgnoreCase(workitemActivityId)) {
                columnAlias.append(",t_.").append(col);
            } else if (col.equals(WorkflowUtil.Alias.workitemStatusKey)) {
                columnAlias.append(",t_.").append(workitemStatus).append(" AS ").append(col);
            } else {
                columnAlias.append(",").append(col);
            }
        }

        if (columnAlias.length() > 0) columnAlias = columnAlias.deleteCharAt(0);//*/
        StringBuilder filter = new StringBuilder(256);
        StringBuilder sql =  new StringBuilder(256);
        sql.append(" select ").append(columnAlias).append(" from ").append(tableName).append(" t_ ") ;
        if (parameter.isMaster() && StringUtil.isNotEmpty(parameter.box)) {
            String processId = String.valueOf(parameter.getParamValue(WF_PROCESS_ID));
            if (WorkflowUtil.Box.applyfor.equals(parameter.box)) {
                filter.append(processInstanceId).append("=0")
                        .append(AppDefineUtil.RELATION_AND).append(WorkflowUtil.C_REGISTER_USER_ID).append("='").append(CommonUtil.getUser().getId()).append("'")
                        .append(AppDefineUtil.RELATION_AND);
            } else if (WorkflowUtil.Box.todo.equals(parameter.box)) {
                filter.append(workitemOwnerId).append("='").append(CommonUtil.getUser().getId()).append("'")
                        .append(AppDefineUtil.RELATION_AND)
                        .append(WorkflowUtil.Alias.processId).append("='").append(processId).append("'")
                        .append(AppDefineUtil.RELATION_AND)
                        .append(workitemStatus).append("<> 13")
                        .append(AppDefineUtil.RELATION_AND)
                        .append(workitemStatus).append("<> 14")
                        .append(AppDefineUtil.RELATION_AND)
                        .append(workitemStatus).append("<> 15")
                        .append(AppDefineUtil.RELATION_AND);
            } else {
                sql.append(" JOIN ").append(getCoflowSql(parameter.box, processId)).append(" ON(")
                        .append("     PWI_.").append(workitemId).append("=t_.").append(workitemId)
                        .append(")");
            }
        }

        filter.append(parameter.getFilter());

        sql.append(" where ").append(filter);

        if (StringUtil.isNotEmpty(orders)) {
            sql.append(" order by ").append(orders);
        }

        List<Object> list = DatabaseHandlerDao.getInstance().pageMaps(sql.toString(), begin, end);

        return processQueryData(list, parameter, columns);
    }
    /**
     * qiucs 2015-2-13 上午10:33:05
     * <p>描述: 查询字段处理 </p>
     * @return String
     */
    protected String processDataColumns(SearchParameter parameter) {
        StringBuilder sb = new StringBuilder();
        sb.append(AppDefineUtil.C_ID);
        String highgoColumns = parameter.columns;
        String[] highgoArray = highgoColumns.split(",");
        for (int i =0 ; i<highgoArray.length ; i++){
            if( highgoArray[i].contains("GRID_LINK_RESERVE_ZONE")){
                String[] lit = highgoArray[i].split("AS");
                sb.append(",cast("+lit[0].trim() +"  as character varying ) as \""+  lit[1].trim()+"\"");
            }else{
                sb.append(","+highgoArray[i]);
            }

        }
//    	sb.append(AppDefineUtil.C_ID).append(",").append(StringUtil.isNotEmpty(parameter.columns) ? parameter.columns : "0 AS EMPTY__");

        if (parameter.isMaster() && StringUtil.isNotEmpty(parameter.box) && !WorkflowUtil.Box.applyfor.equals(parameter.box)) {
            if (sb.indexOf(processInstanceId) < 0) sb.append(",").append(processInstanceId);
            if (sb.indexOf(workitemId) < 0) sb.append(",").append( workitemId);
            if (sb.indexOf(workitemActivityId) < 0) sb.append(",").append(workitemActivityId);
            sb.append(",").append(WorkflowUtil.Alias.workitemStatusKey);
        }

        return sb.toString();
    }
    /**
     * qiucs 2015-2-12 下午3:01:19
     * <p>描述: 查询结果集数据处理 </p>
     * @return List<Object>
     */
    @SuppressWarnings("unchecked")
    protected List<Object> processQueryData(List<Object> list, SearchParameter parameter, String columns) {
        if (null == list) {
            return new ArrayList<Object>();
        }
        // 是否按驼峰格式封装数据
        long lStart = System.currentTimeMillis();
        Map<String, Object> row = null;
        String key, camelKey;
        Object val;
        Iterator<String> it = null;
        int i = 0, size = list.size();
        if (parameter.isCamelCase) {
            Map<String, String> colMap = new HashMap<String, String>();
            if (size > 0) {
                row = (Map<String, Object>) list.get(0);
                it  = row.keySet().iterator();
                // 属性转换成驼峰格式
                while (it.hasNext()) {
                    key = it.next();
                    val = row.get(key);
                    camelKey = StringUtil.toCamelCase(key);
                    row.remove(key);
                    row.put(camelKey, val);
                    colMap.put(key, camelKey);
                }
                for (i = 1; i < size; i++) {
                    row = (Map<String, Object>) list.get(i);
                    it  = colMap.keySet().iterator();
                    while (it.hasNext()) {
                        key = it.next();
                        val = row.get(key);
                        row.remove(key);
                        row.put(colMap.get(key), StringUtil.processHtmlTag(val));
                    }
                }
            }
        } else if (String.valueOf(AppGrid.ST_INDEXLIB).equals(parameter.getParamValue(P_SEARCH_TYPE))) {
            // 索引库查询
        } else {
            for (i = 0; i < size; i++) {
                row = (Map<String, Object>) list.get(i);
                it  = row.keySet().iterator();
                while (it.hasNext()) {
                    key = it.next();
                    val = row.get(key);
                    row.put(key, StringUtil.processHtmlTag(val));
                }
            }
        }
        System.out.println("process search data cost time: " + (System.currentTimeMillis() - lStart));
        return list;
    }

    /**
     * qiucs 2013-12-4 上午10:17:19
     * <p>描述: 工作箱过滤条件 </p>
     * @return String
     */
    protected String getCoflowSql(String box, String processId) {
        String userId = CommonUtil.getCurrentUserId();
        StringBuilder sql = new StringBuilder(256);
        if (WorkflowUtil.Box.todo.equals(box)) {
            sql.append("(select ").append(processInstanceId).append(",").append(workitemId).append(",").append(workitemStatus).append(",").append(workitemActivityId)
                    .append(" from (select cc.id as ").append(processInstanceId).append(", aa.id as ").append(workitemId).append(", aa.status as ").append(workitemStatus).append(",aa.activity_id as ").append(workitemActivityId).append(", ")
                    .append("               row_number() over(partition by aa.process_instance_id order by aa.id desc) no_")
                    .append("          from t_wf_workitem aa,")
                    .append("               T_WF_PROCESS_INSTANCE cc ")
                    .append("         where aa.process_instance_id = cc.id")
                    .append("           and aa.process_id = '").append(processId).append("'")
                    .append("           and aa.status <> 13")
                    .append("           and aa.status <> 15")
                    .append("           and aa.status <> 14")
                    .append("           and aa.owner_id = ").append(userId).append(") TEMP_ ")
                    .append(" where TEMP_.no_= 1) PWI_ ");
        } else if (WorkflowUtil.Box.hasdone.equals(box)) {
            sql.append("(select ").append(processInstanceId).append(",").append(workitemId).append(",").append(workitemStatus).append(",").append(workitemActivityId)
                    .append(" from (select cc.id as ").append(processInstanceId).append(",aa.id as ").append(workitemId).append(",aa.status as ").append(workitemStatus).append(",aa.activity_id as ").append(workitemActivityId).append(", ")
                    .append("               row_number() over(partition by aa.process_instance_id order by aa.id desc) no_")
                    .append("          from t_wf_workitem aa,")
                    .append("               T_WF_PROCESS_INSTANCE cc ")
                    .append("         where aa.process_instance_id = cc.id")
                    .append("           and aa.process_id = '").append(processId).append("'")
                    .append("           and aa.owner_id = ").append(userId)
                    .append("           and aa.status = 14")
                    .append("           and cc.status <> 1005 ) TEMP_ ")
                    .append(" where TEMP_.no_ = 1) PWI_ ");
        } else if (WorkflowUtil.Box.complete.equals(box)) {// 办结箱
            sql.append("(select ").append(processInstanceId).append(",").append(workitemId).append(",").append(workitemStatus).append(",").append(workitemActivityId)
                    .append(" from (select cc.id as ").append(processInstanceId).append(",aa.id as ").append(workitemId).append(",aa.status as ").append(workitemStatus).append(",aa.activity_id as ").append(workitemActivityId).append(",")
                    .append("               row_number() over(partition by aa.process_instance_id order by aa.id desc) no_")
                    .append("          from t_wf_workitem aa,")
                    .append("               T_WF_PROCESS_INSTANCE cc")
                    .append("         where aa.process_instance_id = cc.id")
                    .append("           and aa.process_id = '").append(processId).append("'")
                    .append("           and aa.owner_id = ").append(userId)
                    .append("           and cc.status = 1005) TEMP_ ")
                    .append(" where TEMP_.no_ = 1) PWI_ ");
        } else if (WorkflowUtil.Box.toread.equals(box)) { // 待阅箱
            sql.append("(select ").append(processInstanceId).append(",").append(workitemId).append(",").append(workitemStatus).append(",").append(workitemActivityId)
                    .append(" from (select cc.id as ").append(processInstanceId).append(",aa.id as ").append(workitemId).append(",aa.status as ").append(workitemStatus).append(",aa.activity_id as ").append(workitemActivityId).append(",")
                    .append("               row_number() over(partition by aa.process_instance_id order by aa.id desc) no_")
                    .append("          from t_wf_workitem aa,")
                    .append("               t_wf_lend_out lo,")
                    .append("               T_WF_PROCESS_INSTANCE cc")
                    .append("         where aa.process_instance_id = cc.id and lo.PROCESS_INSTANCE_ID = cc.id")
                    .append("           and aa.owner_id = ").append(userId)
                    .append("           and cc.PROCESS_ID = '").append(processId).append("' and lo.STATUS = 0) TEMP_ ")
                    .append(" where TEMP_.no_ = 1) PWI_ ");
        } else if (WorkflowUtil.Box.hasread.equals(box)) { // 待阅箱
            sql.append("(select ").append(processInstanceId).append(",").append(workitemId).append(",").append(workitemStatus).append(",").append(workitemActivityId)
                    .append(" from (select cc.id as ").append(processInstanceId).append(",aa.id as ").append(workitemId).append(",aa.status as ").append(workitemStatus).append(",aa.activity_id as ").append(workitemActivityId).append(",")
                    .append("               row_number() over(partition by aa.process_instance_id order by aa.id desc) no_")
                    .append("          from t_wf_workitem aa,")
                    .append("               t_wf_lend_out lo,")
                    .append("               T_WF_PROCESS_INSTANCE cc")
                    .append("         where aa.process_instance_id = cc.id and lo.PROCESS_INSTANCE_ID = cc.id")
                    .append("           and aa.owner_id = ").append(userId)
                    .append("           and cc.PROCESS_ID = '").append(processId).append("' and lo.STATUS = 1) TEMP_ ")
                    .append(" where TEMP_.no_ = 1) PWI_ ");
        }
        return sql.toString();
    }

    /**
     * qiucs 2013-9-24 
     * <p>描述: 列表数据删除</p>
     * @param  tableId
     * @param  dTableId
     * @param  ids    设定参数   
     */
    @Transactional
    public void delete(String tableId, String dTableIds, String ids, boolean isLogicalDelete, Map<String, Object> paramMap) {
        // 1. 获取所有关联表的要删除的IDS
        Map<String, DeleteFilter> filterMap = Maps.newLinkedHashMap();
        DeleteFilter dFilter = new DeleteFilter();
        String tableName = getTableName(tableId);
        String filter    = tableName + ".ID IN ('" + ids.replace(",", "','") + "')";
        dFilter.setTableName(tableName);
        dFilter.setFilter(filter);
        filterMap.put(tableId, dFilter);

        String[] dTableIdArr = StringUtil.isNotEmpty(dTableIds) ? dTableIds.split(",") : new String[] {};

        StringBuffer tableNames = new StringBuffer(tableName);
        StringBuffer filters    = new StringBuffer(filter);

        for (int i = 0; i < dTableIdArr.length; i++) {
            String relation = null, mTableId = null, dTableId = dTableIdArr[i], dTableName = getTableName(dTableId);
            dFilter = new DeleteFilter();
            dFilter.setTableName(dTableName);
            if (i == 0) {
                mTableId = tableId;
            } else {
                mTableId = dTableIdArr[i - 1];
            }

            relation = getTableRelation(mTableId, tableName, dTableId, dTableName);
            filters.append(AppDefineUtil.RELATION_AND).append(relation);
            //filter = "EXISTS(SELECT 1 FROM " + tableNames + " WHERE " + filters + ")";
            dFilter.setRelateTableNames(tableNames.toString());
            dFilter.setFilter(filters.toString());

            tableNames.append(",").append(dTableName);
            tableName = dTableName;

            filterMap.put(dTableId, dFilter);
        }
        // 2. 删除所关联的表数据
        for (int i = dTableIdArr.length - 1; i >= 0; i--) {
            String dTableId = dTableIdArr[i];
            deleteByTableId(dTableId, filterMap.get(dTableId), false, isLogicalDelete);
        }
        // 3. 删除自身的表数据
        deleteByTableId(tableId, filterMap.get(tableId), false, isLogicalDelete);
    }

    /**
     * qiucs 2015-2-10 下午3:57:24
     * <p>描述: 根据条件删除指定表数据 </p>
     * @return void
     */
    @Transactional
    private void deleteByTableId(String tableId, DeleteFilter dFilter, boolean isDoc, boolean isLogicalDelete) {
        //
        if (!isDoc) {
            deleteDocument(tableId, dFilter, isLogicalDelete);
        }
        //
        String sql = null;
        if (isLogicalDelete) {
            // 逻辑删除
            sql = dFilter.getLogicalDeleteSql();
        } else {
            // 物理删除
            sql = dFilter.getDeleteSql();
            // 记录物理删除（solr索引同步）
            deleteLog(tableId, dFilter);
        }
        //
        DatabaseHandlerDao.getInstance().executeSql(sql);
    }

    /**
     * qiucs 2014-1-16 
     * <p>描述: 删除相关联的电子全文表数据</p>
     */
    @Transactional
    private void deleteDocument(String tableId, DeleteFilter dFilter, boolean isLogicalDelete) {
        // 如果自身就是电子全文表，则返回
        String logicTableCode = TableUtil.getLogicTableCode(tableId);
        if (ConstantVar.Labels.Document.CODE.equals(logicTableCode)) {
            return;
        }
        List<Object[]> list = getService(TableRelationService.class).findDocumentTableByTableId(tableId);
        for (int i = 0; i < list.size(); i++) {
            DeleteFilter docFilter = new DeleteFilter();
            Object[] objArr = list.get(i);
            String dTableId = String.valueOf(objArr[0]);
            String dTableName = String.valueOf(objArr[2]);
            docFilter.setTableName(dTableName);
            docFilter.setRelateTableNames(
                    (StringUtil.isNotEmpty(dFilter.getRelateTableNames()) ? (dFilter.getRelateTableNames() + ",") : "")
                            + dFilter.getTableName());

            String relation = getTableRelation(tableId, dFilter.getTableName(), dTableId, dTableName);
            docFilter.setFilter(dFilter.getFilter() + AppDefineUtil.RELATION_AND + relation);
            //filter += AppDefineUtil.RELATION_AND + relation;
            deleteByTableId(dTableId, docFilter, true, isLogicalDelete);
        }
    }

    /**
     * qiucs 2015-2-11 下午3:04:02
     * <p>描述: 记录删除信息 </p>
     * @return void
     */
    @Transactional
    protected void deleteLog(String tableId, DeleteFilter dFilter) {
        StringBuilder sb = new StringBuilder();
        sb.append("INSERT INTO T_XTPZ_INDEX_DELETE_LOG(ID, RECORD_ID, TABLE_ID, DELETE_TIME) SELECT ID AS ID, ID AS RECORD_ID,'")
                .append(tableId).append("','").append(DateUtil.currentTime()).append("' FROM ")
                .append(TableUtil.getTableName(tableId)).append(" WHERE ").append(dFilter.getDeleteFilter());
        DatabaseHandlerDao.getInstance().executeSql(sb.toString());
    }

    /**
     * qiucs 2014-7-28 
     * <p>描述: 根据ids删除数据，并删除相关联的附件数据</p>
     * @param  tableId
     * @param  idMap    设定参数   

     @Transactional
     private void deleteOneTable(String tableId, Map<String, String> idMap) {
     String tableName = getTableName(tableId),
     filter = idMap.get(tableId);
     // 删除关联附件表数据
     deleteDocumentRelation(tableId, tableName, filter);
     // 删除自身表数据
     String sql = "DELETE FROM " + tableName + " WHERE " + filter;

     DatabaseHandlerDao.getInstance().executeSql(sql);
     }//*/

    private String getTableRelation(String tableId, String tableName, String dTableId, String dTableName) {
        StringBuffer sb = new StringBuffer("(");
        Map<String, List<String>> relationMap = getTableRelateColumns(tableId, dTableId);

        List<String> mList = relationMap.get(tableId);
        List<String> dList = relationMap.get(dTableId);
        for (int i = 0; i < mList.size(); i++) {
            sb.append(" ").append(tableName ).append(".").append(mList.get(i))
                    .append("=")
                    .append(" ").append(dTableName).append(".").append(dList.get(i));
            if (i >= 0 && i < mList.size() - 1) {
                sb.append(AppDefineUtil.RELATION_AND);
            }
        }
        sb.append(")");
        return sb.toString();
    }

    /**
     * qiucs 2014-1-16 
     * <p>描述: 删除所有关联数据</p>
     */
    @Transactional
    protected void deleteAllRelation(String tableId, String tableName, String dTableId, String ids) {
        // 删除明细表
        if (StringUtil.isNotEmpty(dTableId)) {
            PhysicalTableDefine table = getService(PhysicalTableDefineService.class).getByID(dTableId);
            if (null != table) {
                String dTableName = table.getTableName();
                Map<String, List<String>> relationMap = getTableRelateColumns(tableId, dTableId);
                deleteOneRelation(tableId, tableName, dTableId, dTableName, relationMap, ids);
                if (ConstantVar.Labels.Document.CODE.equals(table.getLogicTableCode())) {
                    return;
                }
                // 获取明细表IDS
                String detailIds = getDetailIds(tableId, tableName, dTableId, dTableName, relationMap, ids);
                // 删除明细表相关联的电子全文表数据
                deleteDocumentRelation(dTableId, dTableName, detailIds);
            }
        }
        // 删除主表相关联的电子全文表数据
        deleteDocumentRelation(tableId, tableName, ids);
    }

    /**
     * qiucs 2014-7-28 
     * <p>描述: 获取要删除的明细表数据IDS</p>
     * @param  tableId
     * @param  dTableId
     * @param  idMap
     * @return Map<String,String>    返回类型   

    private Map<String, String> getDetailIds(String tableId, String dTableId, Map<String, String> idMap) {
    String tableName = getTableName(dTableId),
    dTableName = getTableName(dTableId),
    ids = null;
    Map<String, List<String>> relationMap = getTableRelateColumns(tableId, dTableId);
    ids = getDetailIds(tableId, tableName, dTableId, dTableName, relationMap, idMap.get(tableId));
    idMap.put(dTableId, ids);
    return idMap;
    }//*/

    /**
     * qiucs 2014-1-16 
     * <p>描述: 根据主表获取明细表IDS</p>
     */
    @SuppressWarnings("unchecked")
    private String getDetailIds(String tableId, String tableName,
                                String dTableId, String dTableName,
                                Map<String, List<String>> relationMap, String ids) {
        StringBuffer sb = new StringBuffer("select d.id from " + tableName + " m " +
                "join " + dTableName + " d on(");
        List<String> mList = relationMap.get(tableId);
        List<String> dList = relationMap.get(dTableId);
        for (int i = 0; i < mList.size(); i++) {
            sb.append(" m.").append(mList.get(i))
                    .append("=")
                    .append("d.").append(dList.get(i));
            if (i >= 0 && i < mList.size() - 1) {
                sb.append(AppDefineUtil.RELATION_AND);
            }
        }
        sb.append(") where m.id in('" + ids.replace(",", "','") + "')");
        List<String> list = DatabaseHandlerDao.getInstance().queryForList(sb.toString());
        return (null == list || list.isEmpty()) ? "" : list.toString();
    }

    /**
     * qiucs 2014-1-16 
     * <p>描述: 删除相关联的电子全文表数据</p>
     */
    @Transactional
    private void deleteDocumentRelation(String tableId, String tableName, String filter) {
        // 如果自身就是电子全文表，则返回
        String logicTableCode = TableUtil.getLogicTableCode(tableId);
        if (ConstantVar.Labels.Document.CODE.equals(logicTableCode)) {
            return;
        }
        List<Object[]> list = getService(TableRelationService.class).findDocumentTableByTableId(tableId);
        for (int i = 0; i < list.size(); i++) {
            Object[] objArr = list.get(i);
            String dTableId = String.valueOf(objArr[0]);
            String dTableName = String.valueOf(objArr[2]);
            //Map<String, List<String>> relationMap = getTableRelateColumns(tableId, dTableId);
            //deleteOneRelation(tableId, tableName, dTableId, dTableName, relationMap, ids);
            String relation = getTableRelation(tableId, tableName, dTableId, dTableName);
            filter += AppDefineUtil.RELATION_AND + relation;
        }
    }

    /**
     * qiucs 2014-1-16 
     * <p>描述: 删除指定的表关系数据</p>
     */
    @Transactional
    private void deleteOneRelation(String tableId, String tableName,
                                   String dTableId, String dTableName,
                                   Map<String, List<String>> relationMap, String ids) {
        StringBuffer sb = new StringBuffer("delete from " + dTableName + " where exists(select 1 from " + tableName + " where ");
        List<String> mList = relationMap.get(tableId);
        List<String> dList = relationMap.get(dTableId);
        for (int i = 0; i < mList.size(); i++) {
            sb.append(tableName).append(".").append(mList.get(i))
                    .append("=")
                    .append(dTableName).append(".").append(dList.get(i))
                    .append(AppDefineUtil.RELATION_AND);
        }
        sb.append(tableName).append(".ID in('" + ids.replace(",", "','") + "'))");
        DatabaseHandlerDao.getInstance().executeSql(sb.toString());
    }
    /**
     * qiucs 2013-9-16 
     * <p>描述: 获取表名</p>
     */
    public String getTableName(String tableId) {
        if (StringUtil.isEmpty(tableId)) return "";
        return TableUtil.getTableName(tableId);
    }
    /**
     * qiucs 2013-9-24 
     * <p>描述: 关联表字段</p>
     */
    protected Map<String, List<String>> getTableRelateColumns(String tableId, String relateTableId) {
        return TableUtil.getTableRelation(tableId, relateTableId);
    }

    public Object test() {
        // try {
            /*String tableName = "t_" + System.currentTimeMillis();
            String sql = "create table " + tableName + " as select row_number() over(order by id) no_, id from t_xtpz_test";
            int num = DatabaseHandlerDao.getInstance().executeSql(sql);
            System.out.println(num);*/

        //num = DatabaseHandlerDao.getInstance().executeSql("drop table " + tableName);
    	/*String[] sqls = new String[] {
    			"insert into T_A(package_time) values ('aaaa1')",
    			"insert into T_A(package_time) values ('aaaa2')"
    	};
    	DatabaseHandlerDao.getInstance().jdbcExecuteBatch(sqls);*/
        //System.out.println("test3==================================");
        // DatabaseHandlerDao.getInstance().jdbcExecuteSql("insert into T_A(package_time) values ('aaaa3')");
        //if (true) throw new RuntimeException("dddddd");
        	/*DatabaseHandlerDao.getInstance().jdbcQuery("select * from t_a", new RowMapHandler() {
				
				@Override
				public void processRowData(Map<String, Object> rowMap) {
					// TODO Auto-generated method stub
					System.out.println(rowMap);
				}
			});*/
        //System.out.println(num);
        //} catch (Exception e) {
        //    e.printStackTrace();
        //} 
        //SysUser user = CommonUtil.getUser();
        //System.out.println(user.getRoles());
        List<Map<String, Object>> data = new ArrayList<Map<String, Object>>();
        Map<String, Object> item = new HashMap<String, Object>();
        item.put("id", "111_id");
        item.put("code", "111_code");
        item.put("simpleCode", "111_simpleCode");
        item.put("name", "111_name");
        data.add(item);
        item = new HashMap<String, Object>();
        item.put("id", "222_id");
        item.put("code", "222_code");
        item.put("simpleCode", "222_simpleCode");
        item.put("name", "222_name");
        data.add(item);
        System.out.println("sssssssssssssssssssssssss========================");
        Map<String, Object> gridData = new HashMap<String, Object>();
        gridData.put("data", data);
        return gridData;
    }

    /***********************************(工作流相关接口)************************************/
    protected ClientAPI getClientAPI() {
        return Coflow.getClientAPI(CommonUtil.getCurrentUserId());
    }
    /**
     * qiucs 2013-10-8 
     * <p>描述: </p>
     * @param  tableId
     * @param  id
     * @param  op
     * @param  workitemId
     * @param  note    设定参数   
     * @throws WFException
     */
    @Transactional
    public long runCoflow(String workflowId, String tableId, String id, String op, long workitemId, WFContext context, String opinion) throws WFException {
        if (WorkflowUtil.Op.start.equals(op)) { // 启动
            workitemId = start(workflowId, tableId, id, "");
        } else if (WorkflowUtil.Op.checkout.equals(op)) { // 签收
            checkout(workflowId, workitemId, tableId, id);
        } else if (WorkflowUtil.Op.complete.equals(op)) { // 提交
            complete(workflowId, workitemId, tableId, id, context, opinion);
        } else if (WorkflowUtil.Op.untread.equals(op)) {  // 退回
            untread(workflowId, workitemId, tableId, id, opinion);
        } else if (WorkflowUtil.Op.recall.equals(op)) {   // 撤回
            recall(workflowId, workitemId, tableId, id);
        } else if (WorkflowUtil.Op.reassign.equals(op)) { // 转办
            reassign(workflowId, workitemId, tableId, id, context);
        } else if (WorkflowUtil.Op.hasten.equals(op)) {   // 催办
            hasten(workflowId, workitemId, tableId, id, context);
        } else if (WorkflowUtil.Op.deliver.equals(op)) {  // 传阅
            deliver(workflowId, workitemId, tableId, id, context);
        } else if (WorkflowUtil.Op.hasread.equals(op)) {  // 阅毕
            hasread(workflowId, workitemId, tableId, id, context, opinion);
        } else if (WorkflowUtil.Op.suspend.equals(op)) {  // 中止（删除）
            suspend(workflowId, workitemId, tableId, id);
        } else if (WorkflowUtil.Op.termination.equals(op)) {// 终止
            termination(workflowId, workitemId, tableId, id, context);
        } //*/
        return workitemId;
    }

    /**
     * qiucs 2013-10-8 
     * <p>描述: 启动工作流</p>
     */
    @Transactional
    protected long start(String workflowId, String tableId, String dataId, String note) throws WFException {
        WorkflowDefine wf= WorkflowUtil.getWorkflowEntity(workflowId);
        String packageId = WorkflowUtil.getPackageIdByCode(wf.getWorkflowCode());
        String processId = WorkflowUtil.getProcessIdByCode(wf.getWorkflowCode());
        ClientAPI client = getClientAPI();
        Workitem wi = client.startProcessInstanceAndGetWorkitem(packageId, processId, note);
        // 更新工作流与业务表关系
        updateProcessInstanceId(tableId, dataId, String.valueOf(wi.getProcessInstanceId()));
        // 同步工作流相关数据
        syncWFReventData(wi, tableId, dataId, null);
        if (wi != null) {
            if (wi.getStatus() == WFStatus.WORKITEM_INITIALIZED.getValue()) {
                client.checkoutWorkitem(wi.getId());
            }
        }
        // 用户业务处理
        afterStartCoflow(workflowId, wi, tableId, dataId);
        return wi.getId();
    }

    /**
     * qiucs 2014-9-26 
     * <p>描述: 工作流启动时，二次开发用户业务操作接口</p>
     */
    protected void afterStartCoflow(String workflowId, Workitem wi, String tableId, String dataId) {

    }
    /**
     * qiucs 2013-10-8 
     * <p>标题: checkout</p>
     * <p>描述: 签收工作项</p>
     * @param  workitemId
     */
    protected void checkout(String workflowId, long workitemId, String tableId, String dataId) throws WFException {
        Assert.isTrue(0 != workitemId, "签收工作项时必须指定工作项ID");
        getClientAPI().checkoutWorkitem(workitemId);
    }
    /**
     * qiucs 2013-10-8 
     * <p>描述: 完成工作项</p>
     * @param  workitemId
     * @param  context
     * @throws WFException
     */
    @Transactional
    protected void complete(String workflowId, long workitemId, String tableId, String dataId, WFContext context, String opinion) throws WFException{
        Assert.isTrue(0 != workitemId, "完成工作项时必须指定工作项ID");
        //getClientAPI().completeWorkitem(workitemId, context);
        //if (StringUtil.isNotEmpty(opinion)) opinion = "无";
        Workitem wi = getClientAPI().getWorkitem(workitemId);
        // 
        if (!isFirstActivity(wi)) {
            getService(WorkflowConfirmOpinionService.class).save(workflowId, dataId, wi, opinion, CoflowOpinion.TYPE_COMPLETE);
        }
        // 如果不同意，直接终止流程
        if (StringUtil.null2empty(opinion).startsWith("0")) {
            termination(workflowId, workitemId, tableId, dataId, context);
        } else {
            getClientAPI().completeWorkitem(workitemId, context);
        }
        //DefineService ds = Coflow.getDefineService();
        //Activity activity= ds.getActivity(wi.getPackageId(), wi.getProcessId(), String.valueOf(context.get("activityId")));
        //Activity activity= ds.getActivity(wi.getPackageId(), wi.getPackageVersion(), wi.getProcessId(), wi.getActivityId());
        // 业务操作接口
        //boolean isFinished = (null == activity ? false : activity.isFinishActivity());
        // 同步相关数据表
        String confirmResult = StringUtil.isEmpty(opinion) ? "" : opinion.substring(0, 1);
        syncWFReventData(wi, tableId, dataId, confirmResult);
        // 业务操作接口
        afterCompleteCoflow(workflowId, wi, tableId, dataId, null);
    }

    /**
     * qiucs 2014-9-26 
     * <p>描述: 工作流流转时，二次开发用户业务操作接口</p>
     * @param tableId
     * @param dataId
     * @param paramMap   预留参数
     * @throws WFDefineException
     */
    protected void afterCompleteCoflow(String workflowId, Workitem wi, String tableId, String dataId, Map<String, Object> paramMap) throws WFException {
        //Activity activity = getCurrentActivity(wi);
        //List<Activity> toActivitys = getNextActivities(wi);
    }

    /**
     * qiucs 2015-1-7 下午1:11:37
     * <p>描述: 获取提交下一步的所有节点信息 </p>
     * @return List<Activity>
     */
    protected List<Activity> getNextActivities(long workitemId) throws WFException {
        Workitem wi = getClientAPI().getWorkitem(workitemId);
        return getNextActivities(wi);
    }

    /**
     * qiucs 2015-1-7 下午1:10:50
     * <p>描述: 获取提交下一步的所有节点信息 </p>
     * @return List<Activity>
     */
    protected List<Activity> getNextActivities(Workitem wi) throws WFException {
        List<Activity> list = new ArrayList<Activity>();
        Authorization[] authorizations = getClientAPI().listAuthorizationsOfTargetActivities(wi.getId());
        String packageId = wi.getPackageId();
        String packageVersion = wi.getPackageVersion();
        String processId = wi.getProcessId();
        DefineService defineService = Coflow.getDefineService();
        Authorization auth = null;
        String activityId = "";
        Activity activity = null;
        for (int i = 0; i < authorizations.length; i++) {
            auth = authorizations[i];
            if (activityId.equals(auth.getActivityId())) {
                continue;
            }
            activityId = auth.getActivityId();
            activity   = defineService.getActivity(packageId, packageVersion, processId, activityId);
            list.add(activity);
        }
        return list;
    }

    /**
     * qiucs 2015-1-7 下午1:14:37
     * <p>描述: 获取当前工作项的节点信息 </p>
     * @return Activity
     */
    protected Activity getCurrentActivity(Workitem wi) throws WFDefineException {
        return Coflow.getDefineService().getActivity(wi.getPackageId(), wi.getPackageVersion(), wi.getProcessId(), wi.getActivityId());
    }

    /**
     * qiucs 2015-1-7 下午1:18:39
     * <p>描述: 获取当前工作项的节点信息 </p>
     * @return Activity
     */
    protected Activity getCurrentActivity(long workitemId) throws WFException {
        Workitem wi = getClientAPI().getWorkitem(workitemId);
        return Coflow.getDefineService().getActivity(wi.getPackageId(), wi.getPackageVersion(), wi.getProcessId(), wi.getActivityId());
    }

    /**
     * qiucs 2013-10-8 
     * <p>描述: 退回工作项</p>
     * @param  workitemId
     * @throws WFException
     */
    protected void untread(String workflowId, long workitemId, String tableId, String dataId, String opinion) throws WFException {
        Assert.isTrue(0 != workitemId, "退回工作项时必须指定工作项ID");
        getClientAPI().untread(workitemId);
        if (StringUtil.isNotEmpty(opinion)) {
            Workitem wi = getClientAPI().getWorkitem(workitemId);
            getService(WorkflowConfirmOpinionService.class).save(workflowId, dataId, wi, opinion, WorkflowConfirmOpinion.TYPE_UNTREAD);
        }
    }
    /**
     * qiucs 2013-10-8 
     * <p>描述: 撤回工作项</p>
     * @param  workitemId
     */
    protected void recall(String workflowId, long workitemId, String tableId, String dataId) throws WFException {
        Assert.isTrue(0 != workitemId, "撤回工作项时必须指定工作项ID");
        getClientAPI().recall(workitemId);
    }
    /**
     * qiucs 2014-10-8 
     * <p>描述: 转办</p>
     * @param  workitemId
     */
    protected void reassign(String workflowId, long workitemId, String tableId, String dataId, WFContext context) throws WFException {
        Object targetUserId = context.get("targetUserIds");
        Assert.isTrue(0    != workitemId,   "转办工作项时必须指定工作项ID");
        Assert.isTrue(null != targetUserId, "转办工作项时必须指定用户ID");
        getClientAPI().reassignWorkitem(workitemId, String.valueOf(targetUserId));
    }
    /**
     * qiucs 2014-8-19 
     * <p>描述: 催办</p>
     * @param  workitemId
     */
    protected void hasten(String workflowId, long workitemId, String tableId, String dataId, WFContext context) throws WFException {
        Assert.isTrue(0 != workitemId, "催办工作项时必须指定工作项ID");
        ClientAPI client = getClientAPI();
        Workitem item    = client.getWorkitem(workitemId);
        client.hastenProcess(item.getProcessInstanceId());
    }
    /**
     * qiucs 2014-8-19 
     * <p>描述: 传阅</p>
     * @param  workitemId
     */
    protected void deliver(String workflowId, long workitemId, String tableId, String dataId, WFContext context) throws WFException {
        Object targetUserIds = context.get("targetUserIds");
        Assert.isTrue(0    != workitemId,   "传阅工作项时必须指定工作项ID");
        Assert.isTrue(null != targetUserIds,"传阅工作项时必须指定用户ID");
        String[] targetUserIdArr = targetUserIds.toString().split(",");
        ClientAPI client = getClientAPI();
        Workitem item    = client.getWorkitem(workitemId);
        client.deliver(item.getProcessInstanceId(), targetUserIdArr, "");
    }
    /**
     * qiucs 2014-8-19 
     * <p>描述: 阅毕</p>
     * @param  workitemId
     */
    protected void hasread(String workflowId, long workitemId, String tableId, String dataId, WFContext context, String opinion) throws WFException {
        Assert.isTrue(0 != workitemId, "阅毕时必须指定工作项ID");
        ClientAPI client = getClientAPI();
        Workitem wi = client.getWorkitem(workitemId);
        client.readProcess(wi.getProcessInstanceId(), 2, opinion);//0未阅读 1在阅 2 阅闭
        if (StringUtil.isNotEmpty(opinion)) {
            getService(WorkflowConfirmOpinionService.class).save(workflowId, dataId, wi, opinion, WorkflowConfirmOpinion.TYPE_HASREAD);
        }
    }

    /**
     * qiucs 2015-1-8 上午11:38:04
     * <p>描述: 中止（删除）流程 </p>
     * @return void
     * @throws WFException
     */
    protected void suspend(String workflowId, long workitemId, String tableId, String dataId) throws WFException {
        ClientAPI client = getClientAPI();
        Workitem wi    = client.getWorkitem(workitemId);
        client.deleteProcessInstance(wi.getProcessInstanceId());
        // 删除业务表数据
        delete(tableId, null, dataId, false, null);
        // 业务操作接口
        afterSuspendCoflow(workflowId, wi, tableId, dataId, null);
    }

    /**
     * qiucs 2015-1-8 上午11:42:40
     * <p>描述: 中止（删除）流程后，业务操作 </p>
     * @return void
     */
    protected void afterSuspendCoflow(String workflowId, Workitem wi, String tableId, String dataId, Map<String, Object> paramMap) {
        // TODO Auto-generated method stub

    }

    /**
     * qiucs 2015-1-8 上午11:38:43
     * <p>描述: 终止流程 </p>
     * @return void
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    protected void termination(String workflowId, long workitemId, String tableId, String dataId, WFContext context) throws WFException {
        ClientAPI client = getClientAPI();
        DefineService defineService = Coflow.getDefineService();
        Workitem wi    = client.getWorkitem(workitemId);
        List<Activity> activities = (List<Activity>)defineService.getFinishActivities(
                wi.getPackageId(),
                wi.getPackageVersion(),
                wi.getProcessId());

        Activity act = null;
        Authorization auth = null;
        for (int i = 0, len = activities.size(); i < len; i++) {
            act = activities.get(i);
            //Authorization[] authorizations = client.listAuthorizations(wi.getProcessInstanceId(), act.getId());
            auth = new Authorization();
            auth.setActivityId(act.getId());
            auth.setProcessInstanceId(wi.getProcessInstanceId());
            auth.setPerformerId(String.valueOf(Integer.MIN_VALUE + 1));
            auth.setPerformerName("系统");
            client.manualTransfer(workitemId, new Authorization[] {auth}, WFStatus.PROCESS_INSTANCE_TERMINATED.getValue());
        }
        // 业务操作接口
        afterTerminationCoflow(workflowId, wi, tableId, dataId, null);
    }

    /**
     * qiucs 2015-1-8 上午11:57:48
     * <p>描述: 终止流程后，业务操作 </p>
     * @return void
     */
    protected void afterTerminationCoflow(String workflowId, Workitem wi, String tableId, String dataId, Map<String, Object> paramMap) {
        // TODO Auto-generated method stub

    }

    /**
     * qiucs 2013-10-8 
     * <p>描述: 保存工作流与数据表数据的关联信息</p>
     * @param  tableId
     * @param  dataId
     * @param  processInstanceId    设定参数   
     */
    @Transactional
    private void updateProcessInstanceId(String tableId, String dataId, String processInstanceId) {
        String tableName = getTableName(tableId);
        String sql = "UPDATE " + tableName + " SET " + WorkflowUtil.C_PROCESS_INSTANCE_ID + "='" + processInstanceId + "'" +
                " WHERE ID='" + dataId + "'";
        DatabaseHandlerDao.getInstance().executeSql(sql);
    }

    /**
     * qiucs 2014-10-20 
     * <p>描述: 同步工作流相关数据</p>
     * @param  tableId
     * @param  dataId
     * @param  wi    设定参数   
     * @return void    返回类型   
     * @throws
     */
    @Transactional
    private void syncWFReventData(Workitem wi, String tableId, String dataId, String confirmResult) {
        try {
            String iSql = WorkflowUtil.C_PROCESS_INSTANCE_ID;
            String sSql = WorkflowUtil.C_PROCESS_INSTANCE_ID;
            WorkflowProcess wp = Coflow.getDefineService().getProcess(wi.getPackageId(), wi.getProcessId());
            String ywTableName = getTableName(tableId);
            String rdTablename = Coflow.getDefineManager().getReventDatetablename(wi.getPackageId(), wi.getPackageVersion(), wi.getProcessId());
            @SuppressWarnings("unchecked")
            List<DataField> dfs = wp.getDataFields();
            int len = dfs.size();
            DataField df = null;
            String iCol = null, sCol = null;
            for (int i = 0; i < len; i++) {
                df = dfs.get(i);
                iCol = df.getName().toUpperCase();
                sCol = iCol.substring(2);
                if ("CONFIRM_RESULT".equals(sCol)) {
                    if (StringUtil.isEmpty(confirmResult)) sSql += ",''";
                    else sSql += ",'" + confirmResult + "'";
                } else {
                    iSql += "," + iCol;
                    sSql += "," + sCol;
                }
            }
            String sql = "INSERT INTO " + rdTablename + " (" + iSql + ") SELECT " + sSql + " FROM " + ywTableName + " WHERE ID='" + dataId + "'";
            DatabaseHandlerDao.getInstance().executeSql(sql);
        } catch (Exception e) {
            log.error("同步工作流相关数据出错", e);
        }
    }

    /**
     * qiucs 2013-10-8 
     * <p>描述: 获取流程实例ID</p>
     * @param  tableId
     * @param  dataId
     */
    protected long  getProcessInstanceId(String tableId, String dataId) {
        String tableName = getTableName(tableId);
        String sql = "SELECT T." + WorkflowUtil.C_PROCESS_INSTANCE_ID + " FROM " + tableName + " T WHERE T.ID='" + dataId + "'";
        @SuppressWarnings("unchecked")
        List<Object> list = DatabaseHandlerDao.getInstance().queryForList(sql);

        if (null == list || list.isEmpty()) return 0L;
        return (Long.parseLong(StringUtil.null2zero(list.get(0))));
    }
    /**
     * qiucs 2013-10-8 
     * <p>描述: 获取工作项ID</p>
     * @param  tableId
     * @param  dataId
     */
    protected long getWorkitemId(String tableId, String dataId, WFStatus status) throws NumberFormatException, WFException {
        Workitem wi = getWorkitemByDataId(tableId, dataId, status);
        if (null == wi) return 0;

        return wi.getId();
    }
    /**
     * qiucs 2013-10-8 
     * <p>描述: 获取工作项</p>
     * @param  tableId
     * @param  dataId
     */
    protected Workitem getWorkitemByDataId(String tableId, String dataId, WFStatus status) throws NumberFormatException, WFException {
        long pId = getProcessInstanceId(tableId, dataId);
        if (0 == pId) return null;
        return getWorkitem(pId, status);
    }
    /**
     * qiucs 2013-10-11 
     * <p>描述: 获取工作项</p>
     * @param  processInstanceId
     * @param  status
     */
    protected Workitem getWorkitem(long processInstanceId, WFStatus status) throws WFException {
        WFQuery query = new WFQuery(WFQuery.OWNER_ID, WFQuery.EQUALS, CommonUtil.getCurrentUserId());
        query = new WFQuery(query, WFQuery.AND, new WFQuery(WFQuery.PROCESS_INSTANCE_ID, WFQuery.EQUALS, processInstanceId));
        query = new WFQuery(query, WFQuery.AND, new WFQuery(WFQuery.STATUS, WFQuery.EQUALS, status.getValue()));
        WFFilter filter = new WFFilter(query);
        //filter.setOrderBy(WFQuery.COMPLETED_TIME, "desc");
        Workitem[] ws = getClientAPI().listWorkitems(filter);
        if (ws.length > 0) {
            return ws[0];
        }

        return null;
    }

    /**
     * qiucs 2015-3-24 下午2:42:22
     * <p>描述: 判断是否为流程第一个节点 </p>
     * @return boolean
     */
    @SuppressWarnings("unchecked")
    protected boolean isFirstActivity(Workitem wi) {
        boolean isFirstActivity = false;
        WorkflowProcess wfp = null;
        try {
            wfp = Coflow.getDefineService().getProcess(
                    wi.getPackageId(),
                    wi.getPackageVersion(),
                    wi.getProcessId());
        } catch (WFDefineException e) {
            log.error("获取工作流流程出错", e);
        }
        String firstActivityId = null;
        if (null != wfp) {
            List<Activity> activities = wfp.getActivities();
            for (Activity a : activities) {
                if (a.isStartActivity()) {
                    List<Transition> list = a.getSplitTransitions();
                    if (CollectionUtils.isNotEmpty(list)) {
                        firstActivityId = list.get(0).getTo();
                    }
                    break;
                }
            }
            isFirstActivity = wi.getActivityId().equals(firstActivityId);
        }
        return isFirstActivity;
    }
    /**
     * qiucs 2014-8-13 
     * <p>描述: 判断当前工作项是否为人工控制</p>
     * @param  workitemId
     * @return Object    返回类型   
     * @throws
     */
    public boolean isUserControl(long workitemId) {
        try {
            DefineService defineService = Coflow.getDefineService();
            Workitem workitem = getClientAPI().getWorkitem(workitemId);
            Activity activity = defineService.getActivity(workitem.getPackageId(), workitem.getPackageVersion(), workitem.getProcessId(), workitem.getActivityId());
            return (workitem.isUserControl() && !WFDefineConstant.ACTIVITY_DISPATCH_TYPE_FREE.getValue().equals(activity.getDispatchType()));
        } catch (Exception e) {
            log.error("判断当前工作项是否为人工控制出错", e);
        }
        return false;
    }

    /**
     * qiucs 2014-8-13 
     * <p>描述: 获取当前工作项节点信息</p>
     * @param  workitemId
     * @return Object    返回类型   
     * @throws
     */
    public Object getCurrentActivityCanChange(long workitemId) {
        try {
            ClientAPI client = getClientAPI();
            DefineService defineService = Coflow.getDefineService();
            Workitem workitem = client.getWorkitem(workitemId);
            String packageId = workitem.getPackageId();
            String pVersion  = workitem.getPackageVersion();
            String processId = workitem.getProcessId();
            String curActivityId = workitem.getActivityId();
//          return defineService.getActivity(packageId, pVersion, processId, curActivityId).getPerformerChanageFlag();
            return defineService.getActivity(packageId, pVersion, processId, curActivityId).getPerformerChanageFlag();
//          return workitem.isUserControl();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }
    }

    /**
     * qiucs 2014-10-22 
     * <p>描述: 处理下一个节点操作</p>   
     * @throws
     */
    protected List<Map<String, Object>> processNextActivityPerformers (List<Map<String, Object>> performers) {
        return performers;
    }

    /**
     * qiucs 2014-8-13 
     * <p>描述: 手动提交到下一节点</p>
     * @param  workitemId
     * @return Map<String, Object>   返回类型   
     * @throws
     */
    public Object coflowNextStep (long workitemId) {
        Map<String, Object> actsMap = Maps.newHashMap();
        try {
            ClientAPI     client        = getClientAPI();
            DefineService defineService = Coflow.getDefineService();
            Map<String, Object> entity = null;
            //获取工作项
            Workitem workitem = client.getWorkitem(workitemId);
            String packageId = workitem.getPackageId();
            String packageVersion = workitem.getPackageVersion();
            String processId = workitem.getProcessId();
            //发送判断可以选择的线路数目
            Activity activity = defineService.getActivity(packageId, packageVersion, processId, workitem.getActivityId());
            int maxLine = -1, minLine = -1;
            String boxStyle = "checkbox";
            String maxv = activity.getExtendedAttributeValue(WFDefineConstant.JOIN_SPLIT_PARAM_MAX_T.getValue());
            if(maxv != null){
                maxLine = Integer.parseInt(maxv);
            }
            String minv = activity.getExtendedAttributeValue(WFDefineConstant.JOIN_SPLIT_PARAM_MIN_T.getValue());
            if(minv != null){
                minLine = Integer.parseInt(minv);
            }
            if(maxLine == -1){
                maxLine = Integer.MAX_VALUE;
            }
            if(minLine == -1){
                minLine = 0;
            }
            System.out.println("current activity name: " + activity.getName() + ", minLine=" + minLine + ", maxLine=" + maxLine);
            if(maxLine == 1){
                boxStyle = "radio";
            }
            // 当前节点信息
            entity = Maps.newHashMap();
            entity.put("id", activity.getId());
            entity.put("name", activity.getName());
            entity.put("minLine", minLine);
            entity.put("maxLine", maxLine);
            entity.put("boxStyle", boxStyle);
            actsMap.put("currentActivity", entity);
            //获取发送流转的目标节点的相关信息数组
            Authorization[] authorizations = client.listAuthorizationsOfTargetActivities(workitemId);
            List<Map<String, Object>> entities = Lists.newArrayList();
            actsMap.put("nextActivities", entities);
            if (null == authorizations || 0 == authorizations.length) {
                return actsMap;
            }
            Authorization authorization = null;
            String previousActivityId = "", activityId, performerId, performerName;
            List<Map<String, String>> optionPerformers = null, defaultPerformers = null;
            Map<String, String> item = null;
            //boolean isPerformerControl = false/*, isOptionPerformer = false*/;
            for (int i = 0; i < authorizations.length; i++) {
                authorization = authorizations[i];
                activityId    = authorization.getActivityId();
                performerId   = authorization.getPerformerId();
                performerName = authorization.getPerformerName();
                //authorization.isOptionPerformer(); // true : 显示在左边；false:  显示在右边；
                //authorization.isPerformerControl();// true : 需要人员控制(显示已)；
                if (!previousActivityId.equals(activityId)) {
                    defaultPerformers = new ArrayList<Map<String, String>>();
                    optionPerformers  = new ArrayList<Map<String, String>>();
                    activity = defineService.getActivity(packageId, packageVersion, processId, activityId);
                    if (activity.isFinishActivity() || (!String.valueOf(Integer.MIN_VALUE+1).equals(performerId)
                            && !String.valueOf(Integer.MIN_VALUE).equals(performerId))) {
                        item = new HashMap<String, String>();
                        item.put("id", performerId);
                        item.put("name", performerName);
                        if (authorization.isOptionPerformer()) optionPerformers.add(item);
                        else defaultPerformers.add(item);
                    }

                    entity = Maps.newHashMap();
                    entity.put("id"  , activity.getId());
                    entity.put("name", activity.getName());
                    entity.put("defaultPerformers", defaultPerformers);
                    entity.put("optionPerformers", optionPerformers);
                    entity.put("isPerformerControl", activity.isPerformChange());
                    entity.put("isRollback", authorization.isRollback());
                    entity.put("disabled"  , activity.isFinishActivity() ? true : false);
                    entity.put("checked"   , (i == 0));
                    entity.put("ended"     , activity.isFinishActivity());

                    entities.add(entity);
                    System.out.println("next activity : " + entity.toString());
                } else {
                    item = new HashMap<String, String>();
                    item.put("id", performerId);
                    item.put("name", performerName);
                    if (authorization.isOptionPerformer()) optionPerformers.add(item);
                    else defaultPerformers.add(item);
                }

                previousActivityId = activityId;
            }
            entities = processNextActivityPerformers(entities);
        } catch (Exception e) {
            log.error("获取下一节点信息出错", e);
        }
        return actsMap;
    }

    public Object getNextActivityPerformer(long workitemId) {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        try {
            ClientAPI client = getClientAPI();
            DefineService defineService = Coflow.getDefineService();
            Workitem workitem = client.getWorkitem(workitemId);
            String packageId = workitem.getPackageId();
            String pVersion  = workitem.getPackageVersion();
            String processId = workitem.getProcessId();
            String curActivityId = workitem.getActivityId();
            //当前节点名称
            String curActivityName = defineService.getActivity(packageId, pVersion, processId, curActivityId).getName();
            //获取发送流转的目标节点的相关信息数组
            Map<String, Object> activityMap = new HashMap<String, Object>();
            activityMap.put("activityId", curActivityId);
            activityMap.put("activityName", curActivityName);
            list.add(activityMap);
            Authorization[] authorizations = client.listAuthorizationsOfTargetActivities(workitemId);
            for (int i = 0; i < authorizations.length; i++) {
                Authorization authorization = authorizations[i];
                String activityId = authorization.getActivityId();
                String activityName = "";
                String description = "";
                Activity activity = defineService.getActivity(packageId, pVersion, processId, activityId);
                if (null != activity) {
                    activityName = activity.getName();
                    description = "<font color='red'>（" + activity.getDescription() + "）</font>";
                }
                Boolean isRollback = new Boolean(authorization.isRollback());
                Boolean disabled = new Boolean(!authorization.canChange());
                Boolean checked = new Boolean(authorization.isSelected());
                String performerId = authorization.getPerformerId();
                String performerName = authorization.getPerformerName();
                activityMap = new HashMap<String, Object>();
                activityMap.put("activityId", activityId);
                activityMap.put("activityName", activityName);
                activityMap.put("description", description);
                activityMap.put("performerId", performerId);
                activityMap.put("performerName", performerName);
                activityMap.put("ended", activity.isFinishActivity());
                activityMap.put("isRollback", isRollback);
                activityMap.put("disabled", disabled);
                activityMap.put("checked", checked);
                System.out.println(activityMap.toString());
                list.add(activityMap);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }
        return list;
    }
    @SuppressWarnings("rawtypes")
    public Object getBackActivityPerformer(long workitemId) {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        try {
            SysUser sysUser = ((SysUser)SecurityContextHolder.getContext().getAuthentication().getPrincipal());
            ClientAPI client = Coflow.getClientAPI(sysUser.getId());
            Workitem wkid  = client.getWorkitem(workitemId);
            ProcessInstance processInstance = client.getProcessInstance(wkid.getProcessInstanceId());
            if (processInstance == null) {
                return null;
            }
            if (processInstance.getStatus() != WFStatus.PROCESS_INSTANCE_RUNNING.getValue()) {
                return null;
            }
            DefineService defineService = Coflow.getDefineService();
            Activity activity = defineService.getActivity(processInstance.getPackageId(),processInstance.getPackageVersion(),wkid.getProcessId(),wkid.getActivityId());
            Map<String, Object> activityMap = new HashMap<String, Object>();
            activityMap.put("activityId", activity.getId());
            activityMap.put("activityName", activity.getName());
            list.add(activityMap);
            List transitions = activity.getSplitTransitions();
            Workitem qswk = getWorkitemByInstanceId(processInstance.getId());
            if(qswk != null){
                for (int i = 0; i < transitions.size(); i++) {
                    //遍历所有的流转
                    Transition transition = (Transition) transitions.get(i);
                    //获得目标节点
                    Activity toActivity = defineService.
                            getActivity(processInstance.getPackageId(),
                                    processInstance.getPackageVersion(),
                                    processInstance.getProcessId(),
                                    transition.getTo());
                    if (transition.getExtendedAttributeValue(WFDefineConstant.
                            TRANSITION_ROLLBACK.getValue()) != null) {
                        //退回的路线不算
                        String activityId = toActivity.getId();
                        String activityName = "";
                        String description = "";
                        if (null != toActivity) {
                            activityName = toActivity.getName();
                            description = "<font color='red'>（" + activity.getDescription() + "）</font>";
                        }
                        activityMap = new HashMap<String, Object>();
                        activityMap.put("activityId", activityId);
                        activityMap.put("activityName", activityName);
                        activityMap.put("performerId", sysUser.getId());
                        activityMap.put("performerName", sysUser.getName());
                        activityMap.put("description", description);
                        activityMap.put("ended", toActivity.isFinishActivity());
                        System.out.println(activityMap.toString());
                        list.add(activityMap);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }
        return list;
    }

    public Workitem getWorkitemByInstanceId(long instanceId) throws WFException {
        SysUser sysUser = ((SysUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
        ClientAPI client = Coflow.getClientAPI(sysUser.getId());
        WFQuery query1 = new WFQuery(WFQuery.OWNER_ID, WFQuery.EQUALS, sysUser.getId()); // 查询条件--当前用户
        query1.setIncludeUser(true); // 包括代办的工作项
        // 代办工作项的状态，1
        WFQuery query2 = new WFQuery(WFQuery.STATUS, WFQuery.EQUALS, 11);
        // 合并查询条件
        query2 = new WFQuery(query2, WFQuery.AND, new WFQuery(WFQuery.PROCESS_INSTANCE_ID, WFQuery.EQUALS, instanceId));
        WFQuery queryTemp = new WFQuery(query1, WFQuery.AND, query2);
        WFFilter filter = new WFFilter(queryTemp);
        Workitem[] ws = client.listWorkitems(filter);
        if (ws.length > 0) {
            return ws[0];
        }
        return null;
    }

    /**
     * qiucs 2015-1-9 下午5:19:18
     * <p>描述: 工作流待办箱按钮设置 </p>
     * @return List<String>
     */
    public List<String> getCoflowButtonSetting(String workflowId, String workitemId, String activityId) throws NumberFormatException, WFException {
        Workitem wi = getClientAPI().getWorkitem(Long.parseLong(workitemId));
        WorkflowVersion version = WorkflowUtil.getWorkflowVersion(workflowId, wi.getPackageVersion());
        return WorkflowUtil.getButtonSetting(version.getId(), activityId, AppButton.BUTTON_GRID);
    }

    /**
     * <p>描述: 树节点 数据量统计</p>
     * @return Long    返回类型   
     */
    public Long countRecords(TreeDefine treeDefine, String componentVersionId, String moduleId, String menuId) {
        Long total = Long.parseLong("0");
        if (TreeDefine.T_BOX.equals(treeDefine.getType())) {
            if (getService(WorkflowDefineService.class).existsWorkflow(treeDefine.getTableId())) {
                total = countCoflow(treeDefine.getTableId(), componentVersionId, moduleId, menuId, treeDefine.getValue());
            }
        } else {
            total = countCoflowNot(treeDefine, componentVersionId, moduleId, menuId);
        }
        return total;
    }

    /**
     * <p>描述: 非工作流树节点 数据量统计</p>
     * @return Long    返回类型   
     */
    public Long countCoflowNot(TreeDefine treeDefine, String componentVersionId, String moduleId, String menuId) {
        Long total = Long.parseLong("0");
        String filter = buildControlFilter(treeDefine.getTableId(), componentVersionId, moduleId, menuId, new HashMap<String, Object>());
        String[] columnArr = treeDefine.getColumnNames().split(",");
        String[] valuesArr = treeDefine.getColumnValues().split(",");
        for (int i = 0; i < columnArr.length; i++) {
            String column = columnArr[i];
            if (!column.startsWith(TreeDefine.TAB_PREFIX) && !column.startsWith(TreeDefine.EMP_PREFIX)) {
                filter += AppDefineUtil.RELATION_AND + column + "='" + valuesArr[i] + "'";
            }
        }
        String tableName = getTableName(treeDefine.getTableId());
        String sql = "select count(*) from " + tableName + " where 1=1 "
                + (StringUtil.isEmpty(filter) ? "" : filter);
        Object cnt = DatabaseHandlerDao.getInstance().queryForObject(sql);
        if (null != cnt) {
            total = Long.parseLong(cnt.toString());
        }
        return total;
    }

    /**
     * qiucs 2013-10-13 
     * <p>描述: 工作流树节点 数据量统计</p>
     * @return Long    返回类型   
     */
    public Long countCoflow(String tableId, String componentVersionId, String moduleId, String menuId, String box) {
        Long total = Long.parseLong("0");
        String sql = null;
        //
        String filter = buildControlFilter(tableId, componentVersionId, moduleId, menuId, new HashMap<String, Object>());
        String tableName = getTableName(tableId);
        if (WorkflowUtil.Box.applyfor.equals(box)) {
            sql = "select count(*) from " + tableName + " t where t." + WorkflowUtil.C_PROCESS_INSTANCE_ID + "=0"
                    + (StringUtil.isEmpty(filter) ? "" : filter);
        } if (WorkflowUtil.Box.todo.equals(box)) {
            sql = "select count(*) from " + tableName + " DT_,"
                    + " (select process_instance_id from (select aa.process_instance_id, "
                    + "               row_number() over(partition by aa.process_instance_id order by aa.id desc) no_"
                    + "          from t_wf_workitem aa,"
                    + "               T_WF_PROCESS_INSTANCE cc "
                    + "         where aa.process_instance_id = cc.id"
                    + "           and aa.status <> 13"
                    + "           and aa.status <> 15"
                    + "           and aa.status <> 14"
                    + "           and aa.owner_id = " + CommonUtil.getCurrentUserId() + ") T_ "
                    + " where T_.no_= 1) P_ "
                    + " where P_.process_instance_id = DT_." + WorkflowUtil.C_PROCESS_INSTANCE_ID
                    + (StringUtil.isEmpty(filter) ? "" : filter);
        } else if (WorkflowUtil.Box.hasdone.equals(box)) {
            sql = "select count(*) from " + tableName + " DT_,"
                    + " (select process_instance_id from (select aa.process_instance_id, "
                    + "               row_number() over(partition by aa.process_instance_id order by aa.id desc) no_"
                    + "          from t_wf_workitem aa,"
                    + "               T_WF_PROCESS_INSTANCE cc "
                    + "         where aa.process_instance_id = cc.id"
                    + "           and aa.owner_id = " + CommonUtil.getCurrentUserId()
                    + "           and cc.COMPLETED_TIME is null ) T_ "
                    + " where T_.no_ = 1) P_ "
                    + " where P_.process_instance_id = DT_." + WorkflowUtil.C_PROCESS_INSTANCE_ID
                    + (StringUtil.isEmpty(filter) ? "" : filter);
        } else if (WorkflowUtil.Box.complete.equals(box)) {// 办结箱
            sql = "select count(*) from " + tableName + " DT_,"
                    + " (select process_instance_id from (select aa.process_instance_id,"
                    + "               row_number() over(partition by aa.process_instance_id order by aa.id desc) no_"
                    + "          from t_wf_workitem aa,"
                    + "               T_WF_PROCESS_INSTANCE cc"
                    + "         where aa.process_instance_id = cc.id"
                    + "           and aa.owner_id = " + CommonUtil.getCurrentUserId()
                    + "           and cc.COMPLETED_TIME is not null) T_ "
                    + " where T_.no_ = 1) P_ "
                    + " where P_.process_instance_id = DT_." + WorkflowUtil.C_PROCESS_INSTANCE_ID
                    + (StringUtil.isEmpty(filter) ? "" : filter);
        } else if (WorkflowUtil.Box.toread.equals(box)) { // 待阅箱
            String processId = getService(WorkflowDefineService.class).getProcessId(tableId);
            sql = "select count(*) from " + tableName + " DT_,"
                    + "(select lo.PROCESS_INSTANCE_ID  from t_wf_lend_out lo"
                    + " inner join T_WF_PROCESS_INSTANCE pi on lo.PROCESS_INSTANCE_ID = pi.id"
                    + " where pi.PROCESS_ID = '" + processId + "' and lo.STATUS = 0"
                    + " and lo.TO_USER = " + CommonUtil.getUser().getId() + ") P_ "
                    + " where P_.process_instance_id = DT_." + WorkflowUtil.C_PROCESS_INSTANCE_ID
                    + (StringUtil.isEmpty(filter) ? "" : filter);
        }
        Object cnt = DatabaseHandlerDao.getInstance().queryForObject(sql);
        if (null != cnt) {
            total = Long.parseLong(cnt.toString());
        }

        return total;
    }

    /**
     * qiucs 2015-3-8 下午9:32:47
     * <p>描述: 获取附件表ID </p>
     * @return String
     */
    public String findDocumentTableId(String tableId) {
        List<Object[]> list = getService(TableRelationService.class).findDocumentTableByTableId(tableId);
    	/*if (null == list || 0 == list.size()) {
    		log.warn("表名(" + TableUtil.getTableEntity(tableId).getShowName() + ")不存在附件表，请查检！");
            return StringUtil.EMPTY;
        }*/
        if (list.size() > 1) {
            log.warn("表名(" + TableUtil.getTableEntity(tableId).getShowName() + ")存在多个附件表，请查检！");
        }
        return String.valueOf(list.get(0)[0]);
    }

    /**
     * qiucs 2015-3-16 下午3:52:47
     * <p>描述: 获取关联表表ID </p>
     * @return String
     */
    public String findDetailTableId(String tableId) {
        List<TableRelation> list = getService(TableRelationService.class).findByTableId(tableId);
        List<String> relateTableIds = new ArrayList<String>();
        if (CollectionUtils.isNotEmpty(list)) {
            for (TableRelation t : list) {
                if (relateTableIds.contains(t.getRelateTableId())) continue;
                relateTableIds.add(t.getRelateTableId());
            }
        } else {
            list = getService(TableRelationService.class).findByRelateTableId(tableId);
            for (TableRelation t : list) {
                if (relateTableIds.contains(t.getTableId())) continue;
                relateTableIds.add(t.getTableId());
            }
        }
        if (relateTableIds.size() > 1) {
            log.warn("表（" + TableUtil.getTableName(tableId) + "）存在多个关联表（返回第一个关联表），请查检！");
        }

        return (CollectionUtils.isEmpty(relateTableIds) ?
                StringUtil.EMPTY : relateTableIds.get(0));
    }

    /**
     * qiucs 2014-10-15 
     * <p>描述: </p>
     * @param  tableId
     * @return MessageModel    返回类型   
     * @throws
     */
    public MessageModel checkUpload(String tableId) {
        String docTableId = null;
        PhysicalTableDefine td = getService(PhysicalTableDefineService.class).getByID(tableId);
        if (ConstantVar.Labels.Document.CODE.equals(td.getLogicTableCode())) {
            docTableId = td.getId();
        } else {
            List<Object[]> list = getService(TableRelationService.class).findDocumentTableByTableId(tableId);
            if (null == list || 0 == list.size()) {
                return MessageModel.falseInstance("表名(" + TableUtil.getTableEntity(tableId).getShowName() + ")不存在附件表，请查检！");
            }
            if (list.size() > 1) {
                log.warn("表名(" + TableUtil.getTableEntity(tableId).getShowName() + ")存在多个附件表，请查检！");
            }
            docTableId = String.valueOf(list.get(0)[0]);
        }
        return checkDocTable(docTableId);
    }
    /**
     * qiucs 2014-10-15 
     * <p>描述: 检查附件表结构是否合格规则</p>
     * @return MessageModel    返回类型   
     *                    检查成功时,message中的信息为附件表表ID
     * @throws
     */
    private MessageModel checkDocTable(String tableId) {
        List<Object> labels = getService(ColumnDefineService.class).getColumnLabelsByTableId(tableId);
        if (null == labels || 0 == labels.size()) {
            return MessageModel.falseInstance("字段标签未绑定,请先绑定好,再上传!");
        }
        List<String> msgList = new ArrayList<String>();
        if (!labels.contains(ConstantVar.Labels.Document.Columns.Code.CREATE_TIME)) {
            msgList.add(ConstantVar.Labels.Document.Columns.Name.CREATE_TIME);
        }
        if (!labels.contains(ConstantVar.Labels.Document.Columns.Code.CREATE_USER)) {
            msgList.add(ConstantVar.Labels.Document.Columns.Name.CREATE_USER);
        }
        if (!labels.contains(ConstantVar.Labels.Document.Columns.Code.FILE_FORMAT)) {
            msgList.add(ConstantVar.Labels.Document.Columns.Name.FILE_FORMAT);
        }
        if (!labels.contains(ConstantVar.Labels.Document.Columns.Code.FILE_SIZE)) {
            msgList.add(ConstantVar.Labels.Document.Columns.Name.FILE_SIZE);
        }
        if (!labels.contains(ConstantVar.Labels.Document.Columns.Code.LOCATION)) {
            msgList.add(ConstantVar.Labels.Document.Columns.Name.LOCATION);
        }
        if (!labels.contains(ConstantVar.Labels.Document.Columns.Code.ORIGIN_NAME)) {
            msgList.add(ConstantVar.Labels.Document.Columns.Name.ORIGIN_NAME);
        }
        if (!labels.contains(ConstantVar.Labels.Document.Columns.Code.OWNER_ID)) {
            msgList.add(ConstantVar.Labels.Document.Columns.Name.OWNER_ID);
        }
        if (!labels.contains(ConstantVar.Labels.Document.Columns.Code.PATH)) {
            msgList.add(ConstantVar.Labels.Document.Columns.Name.PATH);
        }
        if (!labels.contains(ConstantVar.Labels.Document.Columns.Code.REAL_NAME)) {
            msgList.add(ConstantVar.Labels.Document.Columns.Name.REAL_NAME);
        }
        if (msgList.size() > 0) {
            return MessageModel.falseInstance("字段标签" + msgList.toString() + "未绑定,请到先绑定好再上传!");
        }
        return MessageModel.trueInstance(tableId);
    }

    /**
     * qiucs 2014-10-15 
     * <p>描述: </p>
     * @return void    返回类型   
     * @throws
     */
    public void docUpload(String relateId, File file, String fileName, String docTableId, String tableId, Map<String, Object> paramMap) {

        String location = docUploadStoreLocation(docTableId, tableId, paramMap);
        String filePath = docUploadStorePath(docTableId, tableId, paramMap);
        String storePath    = location + "/" + filePath + "/";
        String absolutePath = "";
        String extName = "";
        String orgName = "";
        String userId = String.valueOf(paramMap.get("currentUserId"));
        String newName = docUploadFileName(docTableId, tableId, paramMap);
        double fileSize = new BigDecimal(file.length() / 1024D).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
        if (fileSize < 1) fileSize = 1;
        if (fileName.lastIndexOf(".") > 0) {
            orgName = fileName.substring(0, fileName.lastIndexOf("."));
            extName = fileName.substring(fileName.lastIndexOf(".") + 1).toUpperCase();
        }
        storePath = storePath.replace("\\", "/").replace("//", "/");
        File distFile = new File(storePath);
        if (!distFile.exists()) {
            distFile.mkdirs();
        }
        absolutePath = storePath + newName + "." + extName;
        file.renameTo(new File(absolutePath));

        StringBuffer values = new StringBuffer();

        values.append("'").append(UUIDGenerator.uuid()).append("'").append(",") // ID
                .append("'").append(relateId).append("'").append(",")   // LOCATION
                .append("'").append(location).append("'").append(",")   // LOCATION
                .append("'").append(filePath).append("'").append(",")
                .append("'").append(orgName).append("'").append(",")
                .append("'").append(extName).append("'").append(",")
                .append("'").append(fileSize).append("'").append(",")
                .append("'").append(newName).append("'").append(",")
                .append("'").append(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date())).append("'").append(",")
                .append("'").append(userId).append("'");

        paramMap.put(ConstantVar.Labels.Document.Columns.Code.PAHT_ALL, absolutePath);

        // 保存文件上传条目数据
        docUploadSave(docTableId, tableId, values, paramMap);
    }
    /**
     * qiucs 2014-10-15 
     * <p>描述: 保存文件上传条目数据</p>
     * @return void    返回类型   
     * @throws
     */
    @Transactional
    protected void docUploadSave(String docTableId, String tableId, StringBuffer values, Map<String, Object> paramMap) {
        String tableName = TableUtil.getTableName(docTableId);
        StringBuffer columns = new StringBuffer("ID");
        Map<String, String> lcMap = getService(ColumnDefineService.class).getColumnLabelMap(docTableId);
        columns.append(",").append(lcMap.get(ConstantVar.Labels.Document.Columns.Code.OWNER_ID))
                .append(",").append(lcMap.get(ConstantVar.Labels.Document.Columns.Code.LOCATION))
                .append(",").append(lcMap.get(ConstantVar.Labels.Document.Columns.Code.PATH))
                .append(",").append(lcMap.get(ConstantVar.Labels.Document.Columns.Code.ORIGIN_NAME))
                .append(",").append(lcMap.get(ConstantVar.Labels.Document.Columns.Code.FILE_FORMAT))
                .append(",").append(lcMap.get(ConstantVar.Labels.Document.Columns.Code.FILE_SIZE))
                .append(",").append(lcMap.get(ConstantVar.Labels.Document.Columns.Code.REAL_NAME))
                .append(",").append(lcMap.get(ConstantVar.Labels.Document.Columns.Code.CREATE_TIME))
                .append(",").append(lcMap.get(ConstantVar.Labels.Document.Columns.Code.CREATE_USER));

        if (lcMap.containsKey(ConstantVar.Labels.Document.Columns.Code.OWNER_TABLE_ID)) {
            columns.append(",").append(lcMap.get(ConstantVar.Labels.Document.Columns.Code.OWNER_TABLE_ID));
            values.append(",'").append(tableId).append("'");
        }
        if (lcMap.containsKey(ConstantVar.Labels.Document.Columns.Code.PAHT_ALL)) {
            columns.append(",").append(lcMap.get(ConstantVar.Labels.Document.Columns.Code.PAHT_ALL));
            values.append(",'").append(paramMap.get(ConstantVar.Labels.Document.Columns.Code.PAHT_ALL)).append("'");
        }

        StringBuffer sb = new StringBuffer();

        sb.append("INSERT INTO ").append(tableName).append("(").append(columns).append(") VALUES (").append(values).append(")");

        DatabaseHandlerDao.getInstance().executeSql(sb.toString());
    }

    /**
     * qiucs 2014-10-15 
     * <p>描述: 文件上传的名称</p>
     * @return String    返回类型   
     * @throws
     */
    protected String docUploadFileName(String docTableId, String tableId, Map<String, Object> paramMap) {
        return new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()) + "_" + new Random().nextInt(100);
    }

    /**
     * qiucs 2014-10-15 
     * <p>描述: 文件上传的存储相对路径</p>
     * @return String    返回类型   
     * @throws
     */
    protected String docUploadStorePath(String docTableId, String tableId, Map<String, Object> paramMap) {
        return new SimpleDateFormat("yyyyMMdd").format(new Date());
    }

    /**
     * qiucs 2015-7-27 下午8:49:01
     * <p>描述: 文件上传的存储位置 </p>
     * @return String
     */
    protected String docUploadStoreLocation(String docTableId, String tableId, Map<String, Object> paramMap) {
        return getDao().findDocUploadStoreLaction();
    }

    /**
     * qiucs 2015-1-15 下午2:57:29
     * <p>描述: 获取附件信息 </p>
     * @return Document
     */
    public DefaultDocumentScheme getDocumentScheme(String tableId, String id) {
        List<DefaultDocumentScheme> docs = getDocumentSchemeList(tableId, id, false, false);
        return docs.size() > 0 ? docs.get(0) : null;
    }

    /**
     * qiucs 2015-5-15 下午3:02:50
     * <p>描述: 获取电子文件下拉选项 </p>
     * @return Object
     */
    public Object getDocumentOptions(String tableId, String masterDataId) {
        List<DefaultDocumentScheme> docs = getDocumentSchemeList(tableId, masterDataId, true, true);
        int i = 0, len = docs.size();
        List<Map<String, String>> list = new ArrayList<Map<String, String>>(len);
        if (len > 0) {
            Map<String, String> opt = null;
            DefaultDocumentScheme doc = null;
            opt = new HashMap<String, String>();
            opt.put("value", "");
            opt.put("text", "请选择附件");
            list.add(opt);
            for (; i < len; i++) {
                opt = new HashMap<String, String>();
                doc = docs.get(i);
                opt.put("value", doc.getId());
                opt.put("text", doc.getOriginName());
                list.add(opt);
            }
        }
        return list;
    }

    /**
     * qiucs 2015-5-15 下午3:03:16
     * <p>描述: 获取电子文件信息 </p>
     * @return List<DefaultDocumentScheme>
     */
    @SuppressWarnings("unchecked")
    protected List<DefaultDocumentScheme> getDocumentSchemeList(String tableId, String id, boolean isMasterDataId, boolean isQueryAll) {
        String docTableId = tableId;
        PhysicalTableDefine table = TableUtil.getTableEntity(tableId);
        List<Object[]> list = null;
        if (!ConstantVar.Labels.Document.CODE.equals(table.getLogicTableCode())) {
            list = getService(TableRelationService.class).findDocumentTableByTableId(tableId);
            if (null == list || 0 == list.size()) {
                log.warn("表名(" + table.getShowName() + ")不存在附件表，请查检！");
                return null;
            }
            if (list.size() > 1) {
                log.warn("表名(" + table.getShowName() + ")存在多个附件表，请查检！");
            }
            docTableId = String.valueOf(list.get(0)[0]);
            list = null;
        }
        String docTableName = TableUtil.getTableName(docTableId);
        List<String> labels = getDocumentLabels();
        Map<String, String> cMap = getService(ColumnDefineService.class).getColumnLabelMap(docTableId);
        List<String> labelCodes = new ArrayList<String>();
        StringBuffer sql = new StringBuffer("select id");
        for (int i = 0; i < labels.size(); i++) {
            String labelValue = labels.get(i);
            if (cMap.containsKey(labelValue)) {
                sql.append(",").append(cMap.get(labelValue)).append(" as ").append(labelValue);
                labelCodes.add(labelValue);
            }
        }
        sql.append(" from ").append(docTableName);
        if (isMasterDataId) {
            sql.append(" where ").append(cMap.get(ConstantVar.Labels.Document.Columns.Code.OWNER_ID)).append(" = '").append(id).append("'");;
        } else {
            sql.append(" where id='").append(id).append("'");
        }
        DatabaseHandlerDao dao = DatabaseHandlerDao.getInstance();
        if (isQueryAll) {
            list = dao.queryForList(sql.toString());
        } else {
            list = dao.page(sql.toString(), 0, 1);
        }
        int i = 0, len = list.size();
        List<DefaultDocumentScheme> docs = new ArrayList<DefaultDocumentScheme>(len);
        if (len > 0) {
            DefaultDocumentScheme document  = null;
            for (; i < len; i++) {
                document  = newDocumentInstance(labelCodes, list.get(i));
                document.setTableId(docTableId);
                docs.add(document);
            }
        }
        list = null;
        return docs;
    }

    /**
     * qiucs 2015-5-15 下午3:03:50
     * <p>描述: 封装成电子格式实体对象 </p>
     * @return DefaultDocumentScheme
     */
    protected DefaultDocumentScheme newDocumentInstance(List<String> list, Object[] valArr) {
        DefaultDocumentScheme doc = new DefaultDocumentScheme();
        doc.setId(StringUtil.null2empty(valArr[0]));
        for (int i = 0; i < list.size(); i++) {
            String labelCode = list.get(i);
            int idx = i + 1;
            if ((ConstantVar.Labels.Document.Columns.Code.CREATE_TIME).equals(labelCode)) {
                doc.setCreateTime(StringUtil.null2empty(valArr[idx]));
            } else if ((ConstantVar.Labels.Document.Columns.Code.CREATE_USER).equals(labelCode)) {
                doc.setCreateUser(StringUtil.null2empty(valArr[idx]));
            } else if ((ConstantVar.Labels.Document.Columns.Code.FILE_FORMAT).equals(labelCode)) {
                doc.setFileFormat(StringUtil.null2empty(valArr[idx]));
            } else if ((ConstantVar.Labels.Document.Columns.Code.FILE_SIZE).equals(labelCode)) {
                doc.setFileSize(StringUtil.null2empty(valArr[idx]));
            } else if ((ConstantVar.Labels.Document.Columns.Code.LOCATION).equals(labelCode)) {
                doc.setLocation(StringUtil.null2empty(valArr[idx]));
            } else if ((ConstantVar.Labels.Document.Columns.Code.ORIGIN_NAME).equals(labelCode)) {
                doc.setOriginName(StringUtil.null2empty(valArr[idx]));
            } else if ((ConstantVar.Labels.Document.Columns.Code.OWNER_ID).equals(labelCode)) {
                doc.setOwnerId(StringUtil.null2empty(valArr[idx]));
            } else if ((ConstantVar.Labels.Document.Columns.Code.OWNER_TABLE_ID).equals(labelCode)) {
                doc.setOwnerTableId(StringUtil.null2empty(valArr[idx]));
            } else if ((ConstantVar.Labels.Document.Columns.Code.PATH).equals(labelCode)) {
                doc.setPath(StringUtil.null2empty(valArr[idx]));
            } else if ((ConstantVar.Labels.Document.Columns.Code.REAL_NAME).equals(labelCode)) {
                doc.setRealName(StringUtil.null2empty(valArr[idx]));
            }
        }
        return doc;
    }

    /**
     * qiucs 2015-5-15 下午3:04:28
     * <p>描述: 电子文件对应的字段标签 </p>
     * @return List<String>
     */
    protected List<String> getDocumentLabels() {
        List<String> list = new ArrayList<String>();

        list.add(ConstantVar.Labels.Document.Columns.Code.CREATE_TIME);
        list.add(ConstantVar.Labels.Document.Columns.Code.CREATE_USER);
        list.add(ConstantVar.Labels.Document.Columns.Code.FILE_FORMAT);
        list.add(ConstantVar.Labels.Document.Columns.Code.FILE_SIZE);
        list.add(ConstantVar.Labels.Document.Columns.Code.LOCATION);
        list.add(ConstantVar.Labels.Document.Columns.Code.ORIGIN_NAME);
        list.add(ConstantVar.Labels.Document.Columns.Code.OWNER_ID);
        list.add(ConstantVar.Labels.Document.Columns.Code.OWNER_TABLE_ID);
        list.add(ConstantVar.Labels.Document.Columns.Code.PATH);
        list.add(ConstantVar.Labels.Document.Columns.Code.REAL_NAME);

        return list;
    }

    /**
     * <p>描述: 级联删除</p>
     * <p>公司: 上海中信信息发展股份有限公司</p>
     * @author qiucs
     * @date 2014-7-29 下午1:04:41
     */
    protected static class DeleteFilter {
        String tableName;
        String relateTableNames;
        String filter;
        public String getTableName() {
            return tableName;
        }
        public void setTableName(String tableName) {
            this.tableName = tableName;
        }
        public String getRelateTableNames() {
            return relateTableNames;
        }
        public void setRelateTableNames(String relateTableNames) {
            this.relateTableNames = relateTableNames;
        }
        public String getFilter() {
            return this.filter;
        }
        public void setFilter(String filter) {
            this.filter = filter;
        }

        public String getDeleteSql() {
            return "DELETE FROM " + tableName + " WHERE " + getDeleteFilter();
        }

        /**
         * qiucs 2015-8-13 上午11:36:53
         * <p>描述: 逻辑删除SQL语句 </p>
         * @return String
         */
        public String getLogicalDeleteSql() {
            StringBuffer sb = new StringBuffer(256);
            sb.append("UPDATE ").append(tableName)
                    .append(" SET IS_DELETE='1',")
                    .append(" DELETE_TIME='").append(DateUtil.currentTime()).append("', ")
                    .append(" DELETE_USER='").append(CommonUtil.getCurrentUserId()).append("' ")
                    .append(" WHERE ").append(getDeleteFilter());
            return sb.toString();
        }

        public String getDeleteFilter() {
            String deleteFilter = filter;
            if (StringUtil.isNotEmpty(relateTableNames)) {
                deleteFilter = "EXISTS(SELECT 1 FROM " + relateTableNames + "" +
                        " WHERE " + filter + ")";
            }
            return deleteFilter;
        }
    }

    /**
     * qiucs 2015-3-9 下午1:49:46
     * <p>描述: 构造 </p>
     * @return SearchParameter
     */
    public SearchParameter makeupSearchParameter(String tableId, String tableSuffix, String moduleId, String componentVersionId, String menuId,
                                                 String columns, String datatypes, String codetypes, String types, String urls,
                                                 String searchFilter, String masterTableId, String masterDataId, String workflowId, String box,
                                                 PageRequest pageable, String timestamp,
                                                 String cgridDivId, Map<String, Object> paramMap) {

        return new SearchParameter(tableId, tableSuffix, moduleId, componentVersionId, menuId,
                columns, /*datatypes, codetypes, types, urls, */
                searchFilter, masterTableId, masterDataId, workflowId, box,
                pageable, timestamp, cgridDivId, paramMap);
    }

    /**
     * qiucs 2015-3-9 下午1:49:52
     * <p>描述: TODO(这里用一句话描述这个方法的作用) </p>
     * @return SearchParameter
     */
    public SearchParameter makeupSearchParameter(Map<String, Object> pMap) {
        return new SearchParameter(pMap);
    }

    /**
     * <p>描述: 模块展现查询时，参数传递</p>
     * <p>公司: 上海中信信息发展股份有限公司</p>
     * @author qiucs
     * @date 2013-10-28 下午11:14:25
     */
    public static class SearchParameter {
        /* 表ID.*/
        public final String tableId;
        /* 表名后缀.*/
        public final String tableSuffix;
        /* 模块ID.*/
        public final String moduleId;
        /* 版本构件ID.*/
        public final String componentVersionId;
        /* 菜单ID.*/
        public final String menuId;
        /* 列表对应字段.*/
        public final String columns;
        /* 字段对应的数据类型.*/
        //public final String datatypes;
        /* 字段对编码的值.*/
        //public final String codetypes;
        /* 字段字段类型.*/
        //public final String types;
        /* 字段字段类型.*/
        //public final String urls;
        /* 前台查询过滤条件.*/
        public final String searchFilter;
        /* 主表ID.*/
        public final String masterTableId;
        /* 主列表选中的记录ID.*/
        public final String masterDataId;
        /* 工作流ID.*/
        public final String workflowId;
        /* 工作箱.*/
        public final String box;
        /* 分页信息.*/
        public final PageRequest pageable;
        /* 生成自定义构件是的时间戳.*/
        public final String timestamp;
        /* 生成自定义列表构件ID.*/
        public final String cgridDivId;
        /* 是否按驼峰格式封装数据.*/
        public final boolean isCamelCase;
        /* 数据库查询过滤条件.*/
        private String filter;

        private Map<String, Object> paramMap;

        public SearchParameter(String tableId, String tableSuffix, String moduleId, String componentVersionId, String menuId,
                               String columns, /*String datatypes, String codetypes, String types, String urls, */
                               String searchFilter, String masterTableId, String masterDataId, String workflowId, String box,
                               PageRequest pageable, String timestamp, String cgridDivId, Map<String, Object> paramMap) {
            this.tableId = tableId;
	        /* 表名后缀.*/
            this.tableSuffix  = tableSuffix;
	        /* 模块ID.*/
            this.moduleId     = moduleId;
	        /* 版本构件ID.*/
            this.componentVersionId = componentVersionId;
	        /* 菜单ID.*/
            this.menuId       = menuId;
	        /* 列表对应字段.*/
            this.columns      = columns;
	        /* 字段对应的数据类型.*/
            //this.datatypes    = datatypes;
	        /* 字段对编码的值.*/
            //this.codetypes    = codetypes;
	        /* 字段字段类型.*/
            //this.types   	 = types;
	        /* 字段字段类型.*/
            //this.urls         = urls;
	        /* 前台查询过滤条件.*/
            this.searchFilter = searchFilter;
	        /* 主表ID.*/
            this.masterTableId= masterTableId;
	        /* 主列表选中的记录ID.*/
            this.masterDataId = masterDataId;
	        /* 工作流ID.*/
            this.workflowId   = workflowId;
	        /* 工作箱.*/
            this.box          = box;
	        /* 分页信息.*/
            this.pageable= pageable;
	        /* 生成自定义构件是的时间戳.*/
            this.timestamp    = timestamp;
	        /* 生成自定义构件是的时间戳.*/
            this.cgridDivId= cgridDivId;
	        /* 是否按驼峰格式封装数据.*/
            this.isCamelCase = false;
	        /* 生成自定义列表构件ID.*/
            this.paramMap = paramMap;
        }

        /**
         * 构造检索参数对象(WebService接口调用search方法)
         * @param pMap
         *       注意：1. tableId 为必须传入
         *           2. menuId 为菜单ID，如果不传入默认为 -1 表示按组合构件（null != componentVersionId）或自定义构件（null != moduleId）来检索
         *              componentVersionId 为组合构件ID，如果不传入默认为 -1 表示按组合构件（null != menuId）或自定义构件（null != moduleId）来检索
         *              moduleId 为自定义构件ID，如果不传入默认为 -1 表示按组合构件（null != menuId）或自定义构件（null != componentVersionId）来检索
         *              如果 menuId、componentVersionId、moduleId都没传入，则按构件默认配置来检索
         *              如果要组合传入，只有（menuId、componentVersionId）这个组合，没有其他组合
         *              如果这个三个只传入一个，表示按传入这个配置来检索
         *           3. pageNumber 为当前页数 默认为 1
         *           4. pageSize   为当前页数量 默认为 20
         *           5. searchFilter 为过滤条件 格式如：EQ_C_NAME≡xxx;LIKE_C_TITLE≡xxx (相当于 name=xxx and title like '%xxx%') 多个用英文分号(;)分隔
         *              AppDefineUtil.SPLICE = "_C_"
         *              AppDefineUtil.SPLIT  = "≡"
         *              AppDefineUtil.Operator 是前缀 如 EQ LIKE...
         *              复杂条件：如   (name like '%qiu%' or birth_day=1983) and height > 168)
         *              则格式为 (((LIKE_C_NAME≡qiu)OR(EQ_C_BIRTH_DAY≡1983))AND(GT_C_HEIGHT≡168)) 注：每一项需要用括号括起来
         *           6. isCamelCase 是否按驼峰格式封装数据 默认false 如果传入值 true/1/on，则为true，否则为false
         */
        public SearchParameter(Map<String, Object> pMap) {

            String tableId = StringUtil.null2empty(pMap.get("tableId")),
                    menuId = StringUtil.null2empty(pMap.get("menuId")),
                    componentVersionId = StringUtil.null2empty(pMap.get("componentVersionId")),
                    moduleId = StringUtil.null2empty(pMap.get("moduleId"));
            ComponentVersion version;
            int pageNumber = Integer.parseInt(StringUtil.null2zero(pMap.get("pageNumber"))),
                    pageSize = Integer.parseInt(StringUtil.null2zero(pMap.get("pageSize")));
            if (StringUtil.isNotEmpty(menuId) && StringUtil.isEmpty(componentVersionId)) {
                componentVersionId = XarchListener.getBean(MenuService.class).getComponentVersionIdById(menuId);
            }
            if (StringUtil.isNotEmpty(componentVersionId) && StringUtil.isEmpty(moduleId)) {
                version = XarchListener.getBean(ComponentVersionService.class).getByID(componentVersionId);
                if (null != version) moduleId = version.getModuleId();
            }
            if (StringUtil.isEmpty(moduleId)) moduleId = AppDefine.DEFAULT_DEFINE_ID;
            if (StringUtil.isEmpty(componentVersionId)) componentVersionId = AppDefine.DEFAULT_DEFINE_ID;
            if (StringUtil.isEmpty(menuId)) menuId = AppDefine.DEFAULT_DEFINE_ID;

            GridPageModel pageModel = new GridPageModel();
            pageModel.setMenuId(menuId);
            pageModel.setComponentVersionId(componentVersionId);
            pageModel.setModuleId(moduleId);
            pageModel.setTableId(tableId);

            pageModel.init();

            String[] orders = pageModel.getOrders().toArray(new String[0]);

            if (0 == pageNumber) pageNumber = 1;
            if (0 == pageSize) pageSize = 20;

            this.tableId = tableId;
	        /* 表名后缀.*/
            this.tableSuffix  = StringUtil.null2empty(pMap.get("tableSuffix"));
	        /* 模块ID.*/
            this.moduleId     = moduleId;
	        /* 版本构件ID.*/
            this.componentVersionId = componentVersionId;
	        /* 菜单ID.*/
            this.menuId       = menuId;
	        /* 列表对应字段.*/
            this.columns      = StringUtil.join(pageModel.getColumns());
	        /* 字段对应的数据类型.*/
            //this.datatypes    = StringUtil.join(pageModel.getDatatypes());
	        /* 字段对编码的值.*/
            //this.codetypes    = StringUtil.join(pageModel.getCodetypes());
	        /* 字段字段类型.*/
            //this.types   	  = StringUtil.join(pageModel.getTypes());
	        /* 字段字段类型.*/
            //this.urls         = StringUtil.join(pageModel.getUrls());
	        /* 前台查询过滤条件.*/
            this.searchFilter = StringUtil.null2empty(pMap.get("searchFilter"));
	        /* 主表ID.*/
            this.masterTableId= StringUtil.null2empty(pMap.get("masterTableId"));
	        /* 主列表选中的记录ID.*/
            this.masterDataId = StringUtil.null2empty(pMap.get("masterDataId"));
	        /* 工作流ID.*/
            this.workflowId   = StringUtil.null2empty(pMap.get("workflowId"));
	        /* 工作箱.*/
            this.box          = StringUtil.null2empty(pMap.get("box"));
	        /* 分页信息.*/
            this.pageable= SearchHelper.buildPageRequest(pageNumber, pageSize, orders);
	        /* 生成自定义构件是的时间戳.*/
            this.timestamp    = StringUtil.null2empty(pMap.get("timestamp"));
	        /* 生成自定义列表构件ID.*/
            this.cgridDivId= StringUtil.null2empty(pMap.get("cgridDivId"));
	        /* 是否按驼峰格式封装数据.*/
            this.isCamelCase = StringUtil.isBooleanTrue(StringUtil.null2empty(pMap.get("isCamelCase")));
        }
        /* 数据库查询排序条件.*/
        public String getOrders() {
            if (null != pageable) {
                Sort sort = pageable.getSort();
                if (null != sort) {
                    return String.valueOf(sort).replaceAll(":", "");
                }
            }
            return "";
        }
        /* 判断是否为主列表.*/
        public boolean isMaster() {
            return StringUtil.isEmpty(masterTableId);
        }
        public String getFilter() {
            return filter;
        }
        public void setFilter(String filter) {
            this.filter = filter;
        }
        /* 判断前台是否为组件库*/
        public boolean isCoral() {
            return true;
        }

        /**
         * qiucs 2015-8-5 下午3:21:59
         * <p>描述: 添加参数 </p>
         * @return void
         */
        public void addParamMap(String key, Object val) {
            if (null == paramMap) paramMap = new HashMap<String, Object>();
            paramMap.put(key, val);
        }

        public Map<String, Object> getParamMap() {
            return paramMap;
        }

        /**
         * qiucs 2015-8-5 下午3:21:32
         * <p>描述: 获取参数值 </p>
         * @return Object
         */
        public Object getParamValue(String key) {
            if (null == paramMap) {
                return "";
            }
            return paramMap.get(key);
        }
    }

}
