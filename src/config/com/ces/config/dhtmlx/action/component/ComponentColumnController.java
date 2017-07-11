package com.ces.config.dhtmlx.action.component;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.rest.DefaultHttpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.ces.config.dhtmlx.action.base.ConfigDefineServiceDaoController;
import com.ces.config.dhtmlx.dao.component.ComponentColumnDao;
import com.ces.config.dhtmlx.entity.component.ComponentConfig;
import com.ces.config.dhtmlx.entity.component.ComponentColumn;
import com.ces.config.dhtmlx.entity.component.ComponentTable;
import com.ces.config.dhtmlx.service.component.ComponentColumnService;
import com.ces.config.utils.StringUtil;

/**
 * 构件相关表的字段Controller
 * 
 * @author wanglei
 * @date 2013-08-16
 */
public class ComponentColumnController extends
        ConfigDefineServiceDaoController<ComponentColumn, ComponentColumnService, ComponentColumnDao> {

    private static final long serialVersionUID = -8037122071030457027L;

    /*
     * (non-Javadoc)
     * 
     * @see com.ces.xarch.core.web.struts2.BaseController#initModel()
     */
    @Override
    protected void initModel() {
        setModel(new ComponentColumn());
    }

    /*
     * (非 Javadoc) <p>标题: setService</p> <p>描述: 注入自定义服务层SERVICE</p> @param service
     * 
     * @see com.ces.config.dhtmlx.action.base.StringIDDhtmlxConfigController#setService(com.ces.xarch.core.service.
     *      StringIDService)
     */
    @Autowired
    @Qualifier("componentColumnService")
    @Override
    protected void setService(ComponentColumnService service) {
        super.setService(service);
    }

    /**
     * 获取构件关联的表的列
     * 
     * @return Object
     * @throws InstantiationException
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     */
    public Object getComponentColumns() throws IllegalAccessException, InvocationTargetException,
            InstantiationException {
        HttpServletRequest request = ServletActionContext.getRequest();
        String componentConfigKey = request.getParameter("componentConfigKey");
        String tableName = request.getParameter("tableName");
        String tableId = request.getParameter("tableId");
        ComponentConfig componentConfig = (ComponentConfig) request.getSession().getAttribute(componentConfigKey);

        if (componentConfig == null) {
            setReturnData("上传失败");
        } else {
            List<ComponentTable> componentTables = componentConfig.getComponentTables();
            // 构件的表字段
            List<ComponentColumn> componentColumnListOfSession = null;
            if (CollectionUtils.isNotEmpty(componentTables)) {
                for (ComponentTable componentTable : componentTables) {
                    if (componentTable.getName().equals(tableName)) {
                        componentColumnListOfSession = copyTo(componentTable.getComponentColumnList(),
                                ComponentColumn.class);
                        for (int i = 0; i < componentColumnListOfSession.size(); i++) {
                            componentColumnListOfSession.get(i).setId("temp_component_column" + i);
                        }
                        break;
                    }
                }
            }
            // 数据库中已经存在的其他构件的表字段
            List<ComponentColumn> componentColumnListOfDb = getService().getByTableId(tableId);
            List<String[]> componentColumnsBoth = new ArrayList<String[]>();
            String[] componentColumn = null;
            ComponentColumn componentColumnOfSession = null;
            ComponentColumn componentColumnOfDb = null;
            for (Iterator<ComponentColumn> columnOfSessionIterator = componentColumnListOfSession.iterator(); columnOfSessionIterator
                    .hasNext();) {
                componentColumnOfSession = columnOfSessionIterator.next();
                for (Iterator<ComponentColumn> columnOfDbIterator = componentColumnListOfDb.iterator(); columnOfDbIterator
                        .hasNext();) {
                    componentColumnOfDb = columnOfDbIterator.next();
                    if (componentColumnOfSession.getName().equals(componentColumnOfDb.getName())) {
                        componentColumn = new String[11];
                        componentColumn[0] = componentColumnOfSession.getId() + "_" + componentColumnOfDb.getId();
                        componentColumn[1] = componentColumnOfSession.getName();
                        componentColumn[2] = componentColumnOfSession.getType();
                        componentColumn[3] = componentColumnOfSession.getIsNull();
                        componentColumn[4] = StringUtil.null2empty(componentColumnOfSession.getDefaultValue());
                        componentColumn[5] = StringUtil.null2empty(componentColumnOfSession.getRemark());
                        componentColumn[6] = componentColumnOfDb.getName();
                        componentColumn[7] = componentColumnOfDb.getType();
                        componentColumn[8] = componentColumnOfDb.getIsNull();
                        componentColumn[9] = StringUtil.null2empty(componentColumnOfDb.getDefaultValue());
                        componentColumn[10] = StringUtil.null2empty(componentColumnOfDb.getRemark());
                        componentColumnsBoth.add(componentColumn);
                        columnOfSessionIterator.remove();
                        columnOfDbIterator.remove();
                        break;
                    }
                }
            }
            List<String[]> componentColumns = new ArrayList<String[]>();
            componentColumns.addAll(componentColumnsBoth);
            for (ComponentColumn columnOfSession : componentColumnListOfSession) {
                componentColumn = new String[11];
                componentColumn[0] = columnOfSession.getId();
                componentColumn[1] = columnOfSession.getName();
                componentColumn[2] = columnOfSession.getType();
                componentColumn[3] = columnOfSession.getIsNull();
                componentColumn[4] = StringUtil.null2empty(columnOfSession.getDefaultValue());
                componentColumn[5] = StringUtil.null2empty(columnOfSession.getRemark());
                componentColumn[6] = "";
                componentColumn[7] = "";
                componentColumn[8] = "";
                componentColumn[9] = "";
                componentColumn[10] = "";
                componentColumns.add(componentColumn);
            }
            for (ComponentColumn columnOfDb : componentColumnListOfDb) {
                componentColumn = new String[11];
                componentColumn[0] = columnOfDb.getId();
                componentColumn[1] = "";
                componentColumn[2] = "";
                componentColumn[3] = "";
                componentColumn[4] = "";
                componentColumn[5] = "";
                componentColumn[6] = columnOfDb.getName();
                componentColumn[7] = columnOfDb.getType();
                componentColumn[8] = columnOfDb.getIsNull();
                componentColumn[9] = StringUtil.null2empty(columnOfDb.getDefaultValue());
                componentColumn[10] = StringUtil.null2empty(columnOfDb.getRemark());
                componentColumns.add(componentColumn);
            }
            setReturnData(componentColumns);
        }
        return new DefaultHttpHeaders(SUCCESS).disableCaching();
    }

    /**
     * 复制集合
     * 
     * @param <E>
     * @param source 源List
     * @param destinationClass 目标类
     * @return List<E>
     * @throws InstantiationException
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     */
    private <E> List<E> copyTo(List<?> source, Class<E> destinationClass) throws IllegalAccessException,
            InvocationTargetException, InstantiationException {
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

}
