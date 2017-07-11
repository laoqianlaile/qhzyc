package com.ces.config.dhtmlx.action.appmanage;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javassist.expr.Instanceof;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.rest.DefaultHttpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.ces.config.dhtmlx.action.base.ConfigDefineServiceDaoController;
import com.ces.config.dhtmlx.dao.appmanage.ColumnDefineDao;
import com.ces.config.dhtmlx.entity.appmanage.ColumnDefine;
import com.ces.config.dhtmlx.entity.appmanage.PhysicalTableDefine;
import com.ces.config.dhtmlx.entity.code.Code;
import com.ces.config.dhtmlx.entity.component.ComponentConfig;
import com.ces.config.datamodel.message.MessageModel;
import com.ces.config.dhtmlx.service.appmanage.ColumnDefineService;
import com.ces.config.dhtmlx.service.appmanage.WorkflowDefineService;
import com.ces.config.utils.CodeUtil;
import com.ces.config.utils.IndexCommonUtil;
import com.ces.config.utils.StringUtil;
import com.ces.xarch.core.exception.BusinessException;
import com.ces.xarch.core.exception.FatalException;
import com.ces.xarch.core.logger.Logger;

/**
 * @author wangmi
 * @date 2013-06-21 13:12:21
 */
public class ColumnDefineController extends ConfigDefineServiceDaoController<ColumnDefine, ColumnDefineService, ColumnDefineDao> {
    private static final long serialVersionUID = 5844358820296803993L;

    private static Log log = LogFactory.getLog(ColumnDefineController.class);

    @Override
    protected void initModel() {
        setModel(new ColumnDefine());
    }

    /*
     * (非 Javadoc) <p>标题: setService</p> <p>描述: 注入服务层(Service)</p> @param service
     * @see com.ces.xarch.core.web.struts2.BaseController#setService(com.ces.xarch.core.service.AbstractService)
     */
    @Override
    @Autowired
    @Qualifier("columnDefineService")
    protected void setService(ColumnDefineService service) {
        super.setService(service);
    }

    public Object updateStatus() throws FatalException {
        try {
            String columnName = getParameter("columName");
            String value = getParameter("value");
            String id = getParameter("id");
            getService().updateStatus(columnName, value, id);
            model = getService().getByID(id);
            IndexCommonUtil.syncCache(model.getTableId());
        } catch (Exception e) {
            setStatus(false);
            setMessage(e.getMessage());
        }
        return new DefaultHttpHeaders(SUCCESS).disableCaching();
    }
    
    /*
     * qiucs 2015-6-30 下午5:10:00
     * (non-Javadoc)
     * @see com.ces.config.action.base.StringIDConfigDefineServiceDaoController#create()
     */
    @Override
	public Object create() throws FatalException {
        try {
			model = getService().save(model);
			// 同步索引结构
			IndexCommonUtil.syncCache(model.getTableId());
			// 如果对应表存在流程的话，同步流程视图字段
			getService(WorkflowDefineService.class).syncBusinessViewByTableId(model.getId());
			setReturnData(MessageModel.trueInstance("保存成功"));
		} catch (Exception e) {
			if (e instanceof BusinessException) {
				setReturnData(MessageModel.falseInstance("字段类型修改出错!"));
			} else {
				setReturnData(MessageModel.falseInstance("修改字段出错!"));
			}
			log.error("修改字段出错",e);
		}
        return SUCCESS;
	}

    /*
     * qiucs 2015-6-30 下午5:10:14
     * (non-Javadoc)
     * @see com.ces.config.action.base.StringIDConfigDefineServiceDaoController#update()
     */
	@Override
	public Object update() throws FatalException {
        try {
			model = getService().save(model);
			// 同步索引结构
			IndexCommonUtil.syncCache(model.getTableId());
			// 如果对应表存在流程的话，同步流程视图字段
			getService(WorkflowDefineService.class).syncBusinessViewByTableId(model.getId());
			setReturnData(MessageModel.trueInstance("修改成功"));
		} catch (Exception e) {
			if (e instanceof BusinessException) {
				setReturnData(MessageModel.falseInstance("字段类型修改出错!"));
			} else {
				setReturnData(MessageModel.falseInstance("修改字段出错!"));
			}
			log.error("修改字段出错",e);
		}
        return SUCCESS;
	}
	/***
     * <p>描述: 保存字段</p>
     * @return
     */
	public Object save() {
        try {
			model = getService().save(model);
			// 同步索引结构
			IndexCommonUtil.syncCache(model.getTableId());
			// 如果对应表存在流程的话，同步流程视图字段
			getService(WorkflowDefineService.class).syncBusinessViewByTableId(model.getId());
			setReturnData(MessageModel.trueInstance("修改成功"));
		} catch (Exception e) {
			if (e instanceof BusinessException) {
				setReturnData(MessageModel.falseInstance("字段类型修改出错!"));
			} else {
				setReturnData(MessageModel.falseInstance("修改字段出错!"));
			}
			log.error("修改字段出错",e);
		}
        return SUCCESS;
	}

	/**
     * 表复制实现
     * 
     * @param logicTable_id
     * @param newTable_id
     * @return
     * @throws FatalException
     */
    public Object copyFileds2newTableByLogicTable() {
        try {
            String formTableCode = getParameter("logicTable_code");
            String toTableId = getParameter("newTable_id");
            setReturnData(getService().proceTableCopy(formTableCode, toTableId));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return NONE;

    }

    /**
     * qiucs 2013-8-27
     * <p>
     * 描述: 表字段下拉框
     * </p>
     * 
     * @return Object 返回类型
     * @throws
     */
    public Object comboOfTableColumns() {
        try {
            String tableId = getParameter("P_tableId");
            String optionValue = getParameter("P_optionValue"); // P_optionValue: 1为columnName 否则为id
            setReturnData(getService().getComboOfColumnsByTableId(tableId, optionValue));
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }
        return new DefaultHttpHeaders(SUCCESS).disableCaching();
    }

    /**
     * qiucs 2014-12-1
     * <p> 描述: 获取字段名称 （树节点）</p>
     * 
     * @return Object
     * @throws FatalException
     */
    public Object toColumnName() throws FatalException {
        try {
            String isLabel = getParameter("P_isLabel");
            if (StringUtil.isBooleanTrue(isLabel)) {
                String columnLabel = getParameter("P_columnLabel");
                String tableId = getParameter("P_tableId");
                setReturnData(getService().getFilterColumnNameByLabel(columnLabel, tableId));
            } else {
                setReturnData(getService().getFilterColumnNameById(getId()));
            }
        } catch (Exception e) {
            log.error("获取字段名称（树节点）出错", e);
        }
        return new DefaultHttpHeaders(SUCCESS).disableCaching();
    }

    /**
     * <p>
     * 描述: 字段复制时检查字段是否唯一
     * </p>
     * 
     * @return
     * @author Administrator
     * @date 2013-10-28 10:56:48
     */
    public Object checkColumn() {
        String Ids = getParameter("Q_EQ_Ids");
        String tableId = getParameter("Q_EQ_tableId");
        try {
            getService().checkColumn(Ids, tableId);
            setReturnData(MessageModel.trueInstance("OK"));
        } catch (Exception e) {
            e.printStackTrace();
            setReturnData(MessageModel.falseInstance(e.getMessage()));
        }
        return NONE;
    }

    /**
     * <p>
     * 描述: 对于模板表多个字段复制到多张表时检查字段是否唯一
     * </p>
     * 
     * @return
     * @author Administrator
     * @date 2014-4-24 10:56:48
     */
    public Object checkColumn2() {
        String Ids = getParameter("Q_EQ_Ids");
        String tableIds = getParameter("Q_EQ_tableId");
        try {
            for (String tableId : tableIds.split(",")) {
                getService().checkColumn(Ids, tableId);
            }
            setReturnData(MessageModel.trueInstance("OK"));
        } catch (Exception e) {
            e.printStackTrace();
            setReturnData(MessageModel.falseInstance(e.getMessage()));
        }
        return NONE;
    }

    /**
     * <p>
     * 描述: 字段复制
     * </p>
     * 
     * @return
     * @author Administrator
     * @date 2013-10-28 10:57:17
     */
    public Object columnCopy() {
        String Ids = getParameter("Q_EQ_Ids");
        String tableId = getParameter("Q_EQ_tableId");
        getService().columnCopy(Ids, tableId);
        setReturnData(MessageModel.trueInstance("OK"));
        return NONE;
    }

    /**
     * <p>
     * 描述: 模板表的字段复制到目标表
     * </p>
     * 
     * @return
     * @author Administrator
     * @date 2014-4-24 10:57:17
     */
    public Object columnCopy2() {
        String Ids = getParameter("Q_EQ_Ids");
        String tableIds = getParameter("Q_EQ_tableId");
        for (String tableId : tableIds.split(",")) {
            if (StringUtil.isEmpty(tableId)) {
                continue;
            }
            getService().columnCopy(Ids, tableId);
        }
        setReturnData(MessageModel.trueInstance("OK"));
        return NONE;
    }

    /**
     * 获取构件关联的表的列
     * 
     * @return Object
     * @throws InstantiationException
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     */
    @SuppressWarnings("unchecked")
    public Object getComponentColumns() throws IllegalAccessException, InvocationTargetException, InstantiationException {
        HttpServletRequest request = ServletActionContext.getRequest();
        String componentConfigKey = request.getParameter("componentConfigKey");
        String realTableName = request.getParameter("realTableName");
        String tableId = request.getParameter("tableId");
        ComponentConfig componentConfig = (ComponentConfig) request.getSession().getAttribute(componentConfigKey);

        if (componentConfig == null) {
            setReturnData("上传失败");
        } else {
            Map<String, PhysicalTableDefine> tableDefineMap = (Map<String, PhysicalTableDefine>) componentConfig.getSelfDefineConfig().get("physicalTable");
            Map<String, Map<String, ColumnDefine>> columnDefineMap = (Map<String, Map<String, ColumnDefine>>) componentConfig.getSelfDefineConfig().get(
                    "physicalColumn");
            // 构件的表字段
            List<ColumnDefine> columnDefineListOfSession = new ArrayList<ColumnDefine>();
            if (tableDefineMap != null && !tableDefineMap.isEmpty()) {
                String oldTableId = null;
                PhysicalTableDefine tableDefine = null;
                for (Iterator<String> iterator = tableDefineMap.keySet().iterator(); iterator.hasNext();) {
                    oldTableId = iterator.next();
                    tableDefine = tableDefineMap.get(oldTableId);
                    if (tableDefine.getTableName().equals(realTableName)) {
                        Map<String, ColumnDefine> columnMap = columnDefineMap.get(oldTableId);
                        columnDefineListOfSession = copyTo(columnMap.values(), ColumnDefine.class);
                        for (int i = 0; i < columnDefineListOfSession.size(); i++) {
                            columnDefineListOfSession.get(i).setId("temp_component_column" + i);
                        }
                        break;
                    }
                }
            }
            // 数据库中已经存在的表字段
            List<ColumnDefine> columnDefineListOfDb = getService().findByTableId(tableId);
            List<String[]> columnDefinesBoth = new ArrayList<String[]>();
            String[] columnDefine = null;
            ColumnDefine columnDefineOfSession = null;
            ColumnDefine columnDefineOfDb = null;
            List<String[]> columnDefines = new ArrayList<String[]>();
            if (CollectionUtils.isNotEmpty(columnDefineListOfSession)) {
                for (Iterator<ColumnDefine> columnOfSessionIterator = columnDefineListOfSession.iterator(); columnOfSessionIterator.hasNext();) {
                    columnDefineOfSession = columnOfSessionIterator.next();
                    for (Iterator<ColumnDefine> columnOfDbIterator = columnDefineListOfDb.iterator(); columnOfDbIterator.hasNext();) {
                        columnDefineOfDb = columnOfDbIterator.next();
                        if (columnDefineOfSession.getColumnName().equals(columnDefineOfDb.getColumnName())) {
                            columnDefine = new String[11];
                            columnDefine[0] = columnDefineOfSession.getId() + "_" + columnDefineOfDb.getId();
                            columnDefine[1] = columnDefineOfSession.getColumnName();
                            columnDefine[2] = columnDefineOfSession.getDataType();
                            columnDefine[3] = StringUtil.null2empty(columnDefineOfSession.getLength());
                            columnDefine[4] = StringUtil.null2empty(columnDefineOfSession.getDefaultValue());
                            columnDefine[5] = StringUtil.null2empty(columnDefineOfSession.getRemark());
                            columnDefine[6] = columnDefineOfDb.getColumnName();
                            columnDefine[7] = columnDefineOfDb.getDataType();
                            columnDefine[8] = StringUtil.null2empty(columnDefineOfDb.getLength());
                            columnDefine[9] = StringUtil.null2empty(columnDefineOfDb.getDefaultValue());
                            columnDefine[10] = StringUtil.null2empty(columnDefineOfDb.getRemark());
                            columnDefinesBoth.add(columnDefine);
                            columnOfSessionIterator.remove();
                            columnOfDbIterator.remove();
                            break;
                        }
                    }
                }
                columnDefines.addAll(columnDefinesBoth);
                for (ColumnDefine columnOfSession : columnDefineListOfSession) {
                    columnDefine = new String[11];
                    columnDefine[0] = columnOfSession.getId();
                    columnDefine[1] = columnOfSession.getColumnName();
                    columnDefine[2] = columnOfSession.getDataType();
                    columnDefine[3] = StringUtil.null2empty(columnOfSession.getLength());
                    columnDefine[4] = StringUtil.null2empty(columnOfSession.getDefaultValue());
                    columnDefine[5] = StringUtil.null2empty(columnOfSession.getRemark());
                    columnDefine[6] = "";
                    columnDefine[7] = "";
                    columnDefine[8] = "";
                    columnDefine[9] = "";
                    columnDefine[10] = "";
                    columnDefines.add(columnDefine);
                }
            }
            if (CollectionUtils.isNotEmpty(columnDefineListOfDb)) {
                for (ColumnDefine columnOfDb : columnDefineListOfDb) {
                    columnDefine = new String[11];
                    columnDefine[0] = columnOfDb.getId();
                    columnDefine[1] = "";
                    columnDefine[2] = "";
                    columnDefine[3] = "";
                    columnDefine[4] = "";
                    columnDefine[5] = "";
                    columnDefine[6] = columnOfDb.getColumnName();
                    columnDefine[7] = columnOfDb.getDataType();
                    columnDefine[8] = StringUtil.null2empty(columnOfDb.getLength());
                    columnDefine[9] = StringUtil.null2empty(columnOfDb.getDefaultValue());
                    columnDefine[10] = StringUtil.null2empty(columnOfDb.getRemark());
                    columnDefines.add(columnDefine);
                }
            }
            setReturnData(columnDefines);
        }
        return new DefaultHttpHeaders(SUCCESS).disableCaching();
    }

    /**
     * 复制集合
     * 
     * @param <E>
     * @param source 源List
     * @param destinationClass 目标类
     * @return Collection<E>
     * @throws InstantiationException
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     */
    private <E> List<E> copyTo(Collection<?> source, Class<E> destinationClass) throws IllegalAccessException, InvocationTargetException,
            InstantiationException {
        if (source.size() == 0)
            return Collections.emptyList();
        List<E> res = new ArrayList<E>(source.size());
        for (Object o : source) {
            E e = destinationClass.newInstance();
            BeanUtils.copyProperties(e, o);
            res.add(e);
        }
        return res;
    }

    /**
     * 取用户型、编码型、部门型对应的下拉框
     * 
     * @return
     * @author Administrator
     * @date 2013-11-19 09:33:07
     */
    public Object valComboOfTableColumns() {
        try {
            String id = getParameter("P_id");
            setReturnData(getService().getValComboOfColumnsById(id));
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }
        return new DefaultHttpHeaders(SUCCESS).disableCaching();
    }

    /*
     * (非 Javadoc)
     * <p>标题: destroy</p>
     * <p>描述: </p>
     * @return
     * @throws FatalException
     * @see com.ces.xarch.core.web.struts2.BaseController#destroy()
     */
    @Override
    @Logger(action = "字段删除", logger = "${id}")
    public Object destroy() throws FatalException {
        try {
        	String ids = getId();
        	model = getService().getByID(ids.split(",")[0]);
            getService().delete(getId());
            // 同步索引结构
            IndexCommonUtil.syncCache(model.getTableId());
            // 如果对应表存在流程的话，同步流程视图字段
            getService(WorkflowDefineService.class).syncBusinessViewByTableId(model.getTableId());
            setReturnData(new MessageModel(Boolean.TRUE, ""));
        } catch (Exception e) {
            e.printStackTrace();
            setReturnData(new MessageModel(Boolean.FALSE, e.getMessage()));
        }

        return NONE;
    }

    /**
     * wangmi 2013-12-10
     * <p>描述: 根据tableId 获取最大显示排序</p>
     * 
     * @param tableId
     * @return String 返回类型
     * @throws public Object getMaxShowOrder() throws FatalException {
     *             try {
     *             String tableId = getParameter("tableId");
     *             setReturnData(getService().getMaxShowOrderService(tableId));
     *             } catch (Exception e) {
     *             setStatus(false);
     *             setMessage(e.getMessage());
     *             }
     *             return new DefaultHttpHeaders("success").disableCaching();
     *             }
     */

    /**
     * wangmi 2013-12-10
     * <p>标题: sortShowOrder</p>
     * <p>描述: 拖拽调整顺序</p>
     * 
     * @return Object 返回类型
     * @throws
     */
    public Object sortShowOrder() throws FatalException {
        String start = getParameter("start");
        String end = getParameter("end");
        String tableId = getParameter("tableId");

        ColumnDefine startColumn = getService().getByID(start);
        ColumnDefine endColumn = getService().getByID(end);

        if (startColumn.getShowOrder() > endColumn.getShowOrder()) {
            // 向上拖拽
            List<ColumnDefine> columnList = getService().getColumnListByShowOrder(endColumn.getShowOrder() + 1, startColumn.getShowOrder(), tableId);
            startColumn.setShowOrder(endColumn.getShowOrder() + 1);
            getService().save(startColumn);
            for (ColumnDefine columnDefine : columnList) {
                if (columnDefine.getId().equals(startColumn.getId())) {
                    continue;
                }
                columnDefine.setShowOrder(columnDefine.getShowOrder() + 1);
                getService().save(columnDefine);
            }
        } else {
            // 向下拖拽
            List<ColumnDefine> columnList = getService().getColumnListByShowOrder(startColumn.getShowOrder(), endColumn.getShowOrder(), tableId);
            startColumn.setShowOrder(endColumn.getShowOrder());
            getService().save(startColumn);
            for (ColumnDefine columnDefine : columnList) {
                if (columnDefine.getId().equals(startColumn.getId())) {
                    continue;
                }
                columnDefine.setShowOrder(columnDefine.getShowOrder() - 1);
                getService().save(columnDefine);
            }
        }

        setReturnData("排序成功");
        return new DefaultHttpHeaders(SUCCESS).disableCaching();
    }

    /**
     * 将业务表字段复制到逻辑表
     * 
     * @return Object
     */
    public Object copyToLogic() {
        String logicCode = getParameter("logicCode");
        String columnIds = getParameter("columnIds");
        getService().copyToLogic(logicCode, columnIds);
        return new DefaultHttpHeaders("success").disableCaching();
    }

    /**
     * qiucs 2014-9-5
     * <p>描述: 获取修改字段</p>
     */
    public Object updateColumns() {

        try {
            String tableId = getParameter("P_tableId");
            setReturnData(getService().getUpdateColumns(tableId));
        } catch (Exception e) {
            log.error("获取修改字段出错", e);
        }

        return NONE;
    }
    
    /**
     * qiujinwei 2015-06-23
     * <p>描述: 判断字段是否有数据</p>
     */
    public Object dataExist() {
    	String columnName = getParameter("P_columnName");
    	String tableId = getParameter("P_tableId");
    	setReturnData(getService().dataExist(columnName, tableId));
    	return NONE;
    }
    
    /**
     * 获取日期格式
     * 
     * @return Object
     */
    public Object getDateFormat() {
        List<Code> codeList = CodeUtil.getInstance().getCodeList("DATE_FORMAT");
        setReturnData(codeList);
        return new DefaultHttpHeaders(SUCCESS).disableCaching();
    }
    
    /**
     * 获取预设正则校验
     * 
     * @return Object
     */
    public Object getRegex() {
        List<Code> codeList = CodeUtil.getInstance().getCodeList("REGEX");
        setReturnData(codeList);
        return new DefaultHttpHeaders(SUCCESS).disableCaching();
    }
}
