package com.ces.config.action.base;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.rest.DefaultHttpHeaders;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;

import com.ces.config.datamodel.message.MessageModel;
import com.ces.config.service.base.StringIDConfigDefineDaoService;
import com.ces.config.utils.StringUtil;
import com.ces.utils.BeanUtils;
import com.ces.xarch.core.entity.StringIDEntity;
import com.ces.xarch.core.exception.FatalException;
import com.ces.xarch.core.persistence.jpa.StringIDDao;
import com.ces.xarch.core.utils.SearchFilter;
import com.ces.xarch.core.utils.SearchHelper;
import com.ces.xarch.core.utils.ServletUtils;
import com.ces.xarch.core.web.frame.FrameDataModel;
import com.ces.xarch.core.web.frame.utils.FrameDataModuleHandling;
import com.ces.xarch.core.web.jackson.BeanFilter;
import com.ces.xarch.core.web.listener.XarchListener;
import com.ces.xarch.core.web.struts2.BaseController;

/**
 * <p>描述: 系统配置平台Controller基类</p>
 * <p>公司: 上海中信信息发展股份有限公司</p>
 * @author qiucs   
 * @date 2013-7-12 上午10:18:02
 *
 * @param <T>
 * @param <Service>
 * @param <Dao>
 */
public class StringIDConfigDefineServiceDaoController<T extends StringIDEntity,Service extends StringIDConfigDefineDaoService<T, Dao>, Dao extends StringIDDao<T>> 
        extends BaseController<String, T, Service, Dao> {

    private static final long serialVersionUID = -9082920693515325673L;
    
    // 日志记录
    private static Log log = LogFactory.getLog(StringIDConfigDefineServiceDaoController.class);

    /** 参数名P_filterId: 值是用于getId()的值的过滤字段，默认值parentId*/
    protected final String FILTER_ID_PARAM = "P_filterId";
    // 数据模型名称: 树型
    protected final String DATA_MODEL_TREE = "tree";
    // 数据模型名称: 下拉框
    protected final String DATA_MODEL_COMBOBOX = "combobox";
    // 前台框架: 组件库
    protected final String FRAME_NAME_CORAL = "coral";
    // 前台框架 : dhtmlx
    protected final String FRAME_NAME_DHTMLX= "dhtmlx";

    /**
     * qiucs 2014-1-20 
     * <p>描述: 获取Controller容器中的Service类全名</p>
     * @return Class<Service>    返回类型   
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    private Class<Service> getServiceClass() {
        Class clazz = getClass();

        while (clazz != Object.class) {
            Type t = clazz.getGenericSuperclass();
            if (t instanceof ParameterizedType) {
                Type[] args = ((ParameterizedType) t).getActualTypeArguments();
                if (args.length > 1 && args[1] instanceof Class) {
                    System.out.println(args[1]);
                    return (Class<Service>) args[1];
                }
            }
            clazz = clazz.getSuperclass();
        }

        return null;
    }

    /*
     * (非 Javadoc)   
     * <p>描述: 复写获取Service方法，可以自动获取容器中的指定Service</p>   
     * @return   
     * @see com.ces.xarch.core.web.struts2.BaseController#getService()
     * modify by qiucs @2014-1-20
     */
    @Override
    protected Service getService() {
        Service service = super.getService();
        if (null == service) {
            service = XarchListener.getBean(getServiceClass());
            setService(service);
        }
        return service;
    }

    /*
     * (非 Javadoc)   
     * <p>描述: 复写新增保存把保存后的ID传回模型</p>   
     * @return
     * @throws FatalException   
     * @see com.ces.xarch.core.web.struts2.BaseController#create()
     */
    @Override
    public Object create() throws FatalException {
        model = getService().save(model);
        return SUCCESS;
    }

    /* 
     * (非 Javadoc)  
     * @see com.ces.xarch.core.web.struts2.AbstractController#update()
     */
    @Override
    public Object update() throws FatalException {
        model = getService().save(model);
        return SUCCESS;
    }

    /* 
     * (非 Javadoc)  
     * @see com.ces.xarch.core.web.struts2.AbstractController#update()
     */
    @Override
    public Object show() throws FatalException {
    	BeanUtils.copy(getService().getByID(getId()), model);
		processFilter((BeanFilter)model);
        return SUCCESS;
    }
    
    /*
     * qiucs 2015-6-9 下午4:30:22
     * (non-Javadoc)
     * @see com.ces.xarch.core.web.struts2.BaseController#search()
     */
    @Override
    public Object search() throws FatalException {
    	processSearch();
    	return SUCCESS;
    }

    /* (非 Javadoc)
     * @see com.ces.xarch.core.web.struts2.AbstractController#destroy()
     */
    @Override
    public Object destroy() throws FatalException {
        try {
            getService().delete(getId());
            setReturnData(MessageModel.trueInstance("删除成功！"));
        } catch (Exception e) {
            log.error("删除出错(id=" + getId() + ")", e);
            setReturnData(MessageModel.falseInstance(e.getMessage()));
        }
        return SUCCESS;
    }

    /**
     * <p>标题: tree</p>
     * <p>描述: 树模型处理</p>
     * @return Object    返回类型   
     */
    public Object tree() {
        processTree();
        return new DefaultHttpHeaders("success").disableCaching();
    }

    /**
     * <p>标题: checkUnique</p>
     * <p>描述: 唯一性检查</p>
     * @return Object    返回类型   
     */
    public Object checkUnique() {
        try {
            // true is unique; false is not unique
            setReturnData(getService().checkUnique(buildSpecification(), getId()));
        } catch (Exception e) {
            log.error("唯一性检查出错", e);
        }

        return NONE;
    }

    /**
     * 处理树请求.
     * @throws FatalException
     * @author qiucs
     * @date 2013-07-10  15:48:52
     */
    protected void processTree() throws FatalException {
        list = getDataModel(DATA_MODEL_TREE);
        processFilter(list);
        PageRequest pageRequest = buildPageRequest();
        Sort sort = (null != pageRequest ? (null != pageRequest.getSort() ? pageRequest.getSort() : null) : null);
        if (null == sort) {
            list.setData(beforeProcessTreeData(getService().find(buildSpecification())));
        } else {
            list.setData(beforeProcessTreeData(getService().find(buildSpecification(), sort)));
        }
        
        if (FRAME_NAME_CORAL.equals(getFrameName())) {
            if (null == list.getData()) {
                setReturnData(new ArrayList<T>());
            } else {
                setReturnData(list.getData());
            }
        }
    }

    /**
     * qiucs 2013-10-16 
     * <p>标题: beforeProcessTreeData</p>
     * <p>描述: 在进行数据转换前调用的方法， 可以进行一些数据预处理</p>
     * @param  data
     * @return List<T>    返回类型   
     */
    protected List<T> beforeProcessTreeData(List<T> data) {
        return data;
    }
    
    /**
     * qiucs 2014-7-18 
     * <p>描述: 下拉框选项</p>
     * @return Object    返回类型   
     */
    public Object combobox () {
        processCombobox();
        return NONE;
    }

    /**
     * 处理下拉框请求.
     * @throws FatalException
     * @author qiucs
     * @date 2013-07-18
     */
    protected void processCombobox() throws FatalException {
        list = getDataModel(DATA_MODEL_COMBOBOX);
        processFilter(list);
        PageRequest pageRequest = buildPageRequest();
        Sort sort = (null != pageRequest ? (null != pageRequest.getSort() ? pageRequest.getSort() : null) : null);
        if (null == sort) {
            list.setData(beforeProcessComboboxData(getService().find(buildSpecification())));
        } else {
            list.setData(beforeProcessComboboxData(getService().find(buildSpecification(), sort)));
        }
        if (null == list.getData()) {
            setReturnData(new ArrayList<T>());
        } else {
            setReturnData(list.getData());
        }
    }

    /**
     * qiucs 2014-7-18 
     * <p>描述: 在进行数据转换前调用的方法， 可以进行一些数据预处理</p>
     * @param  data
     * @return List<T>    返回类型   
     */
    protected List<T> beforeProcessComboboxData(List<T> data) {
        return data;
    }

    /**
     * qiucs 2013-06-18  17:56:38
     * <p>描述: 构建查询条件.</p>
     * @return 查询条件
     */
    @Override
    public Specification<T> buildSpecification() throws FatalException {
        if (DATA_MODEL_TREE.equals(getModelTemplate())) {
            HttpServletRequest request = ServletActionContext.getRequest();
            Map<String, SearchFilter> filterMap = SearchFilter.parse(ServletUtils.getParametersStartingWith(request,
                    SearchFilter.searchPre));
            String filterId = getParameter(FILTER_ID_PARAM);
            String id       = getId();
            if (StringUtil.isEmpty(id)) id = "-1";
            if (StringUtil.isNotEmpty(filterId)) {
                filterMap.put("EQ_" + filterId, new SearchFilter(filterId, SearchFilter.Operator.EQ, id));
            }

            return SearchHelper.buildSpecification(filterMap.values(), getModelClass());
        }

        return super.buildSpecification();
    }

    /**
     * qiucs 2014-4-15 
     * <p>描述: 处理从request中取得参数值</p>
     * @return String    返回类型   
     */
    protected String getParameter(String param) {
        HttpServletRequest request = ServletActionContext.getRequest();
        return StringUtil.null2empty(request.getParameter(param));
    }
    
    /**
     * qiucs 2014-4-15 
     * <p>描述: 根据ID判断实体存不存在</p>
     * @return Object    返回类型   
     */
    public Object exists() {
        try {
            setReturnData(getService().exists(getId()));
        } catch (Exception e) {
            log.error("判断实体存不存在出错(id:" + getId() + ")", e);
            setReturnData(MessageModel.falseInstance("判断实体存不存在出错！"));
        }
        
        return NONE;
    }
    
    /**
     * qiucs 2014-8-21 
     * <p>描述: 查找单个实体</p>
     * @return Object    返回类型   
     */
    public Object findOne() {
        try {
            model = getService().findOne(buildSpecification());
        } catch (Exception e) {
            log.error("查找单个实体出错", e);
        }
        
        return SUCCESS;
    }

    /* (non-Javadoc)
     * (非 Javadoc)   
     * <p>标题: getDataModel</p>   
     * <p>描述: </p>   
     * @param modelName
     * @see com.ces.xarch.core.web.struts2.BaseController#getDataModel(java.lang.String)
     */
    @Override
    public FrameDataModel<T, String> getDataModel(String modelName) {
        ServletActionContext.getRequest().setAttribute(FrameDataModuleHandling.FRAME_PARAM, "coral");
        return super.getDataModel(modelName);
    }
    
    /**
     * qiucs 2014-7-4 
     * <p>标题: getFrameName</p>
     * <p>描述: 获取前台框架类型</p>
     * @return String    返回类型   
     * @throws
     */
    protected final String getFrameName() {
        HttpServletRequest request = ServletActionContext.getRequest();
        String frameName = request.getParameter(FrameDataModuleHandling.FRAME_PARAM);
        
        if (frameName != null && !"".equals(frameName)) {
            return frameName;
        }
        
        frameName = (String)request.getAttribute(FrameDataModuleHandling.FRAME_PARAM);
        
        if (frameName != null && !"".equals(frameName)) {
            return frameName;
        }
        
        return (String)request.getSession().getAttribute(FrameDataModuleHandling.FRAME_PARAM);
    }
}
