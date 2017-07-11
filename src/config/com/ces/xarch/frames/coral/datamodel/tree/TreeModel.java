package com.ces.xarch.frames.coral.datamodel.tree;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ognl.Ognl;
import ognl.OgnlException;

import org.springframework.beans.BeanUtils;

import com.ces.utils.StringUtils;
import com.ces.xarch.core.entity.BaseEntity;
import com.ces.xarch.core.exception.BusinessException;
import com.ces.xarch.core.utils.FilterHelper;
import com.ces.xarch.core.web.frame.impl.DefaultDataModel;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * <p>描述: 树 数据模型</p>
 * <p>公司: 上海中信信息发展股份有限公司</p>
 * @author qiucs   
 * @date 2014-7-4 下午5:14:37
 *
 * @param <T>
 * @param <ID>
 */
public class TreeModel<T extends BaseEntity<ID>,ID extends Serializable> extends DefaultDataModel<T, ID> {
	
	/** 节点展开参数名称. */
	public static final String GLOBALOPEN_PARAM = "P_OPEN";
	/** 勾选节点参数名称. */
	public static final String CHECKED_PARAM = "P_CHECKED";
	/** 是否为父节点参数名称. */
	public static final String ISPARENT_PARAM = "P_ISPARENT";
    /** 图标参数名称. */
    public static final String ICON_PARAM = "P_ICON";
    /** 图标参数名称. */
    public static final String ID_PARAM = "P_ID";
	
	/** 所有节点是否默认展开. */
	private boolean open;
	/** 勾选的节点ID. */
	private String checked;
	/** 是否默认没有子节点. */
	private String parentAttrName;
    /** 图标参数名称. */
	private String iconAttrName;
	/** 图标参数名称. */
    private String idAttrName;
	
    private final String PROP_OPEN     = "open";
    private final String PROP_ISPARENT = "isParent";
    private final String PROP_CHECKED  = "checked";
    private final String PROP_TEXT     = "name";   
    private final String PROP_ID       = "id";  
    private final String PROP_ICON     = "icon";      // 叶子结点图标
    private final String PROP_ICONCLOSE= "iconClose"; // 结点展开图标
    private final String PROP_ICONOPEN = "iconOpen";  // 结点收缩图标
    

    // 实体bean Class
    private Class<T> clazz = null;
    // 需要转JSON的属性集
    private String[] inProperties;
	
	
	/**
     * qiucs 2014-7-7 
     * <p>描述: 构造函数.</p>
	 */
	public TreeModel() {
	    
	}

	@Override
	@JsonProperty("item")
	public Object getData() {
		return super.getData();
	}

	@Override
	@JsonIgnore
	public int getPageNumber() {
		return super.getPageNumber();
	}

	@Override
	@JsonIgnore
	public int getPageSize() {
		return super.getPageSize();
	}

	@Override
	@JsonIgnore
	public long getTotal() {
		return super.getTotal();
	}

	@Override
	@JsonIgnore
	public int getTotalPages() {
		return super.getTotalPages();
	}

	@SuppressWarnings("unchecked")
	@Override
	public void setData(List<T> dataList) {
	    
		if (dataList != null && dataList.size() > 0) {
			data = new ArrayList<Object>(dataList.size());
			
			for (T bean : dataList) {
				((List<Object>)data).add(toTreeItem(bean, true));
			}
			
		}
	}
	
	@SuppressWarnings("unchecked")
    public void setData(Object dataList) {
	    if (null != dataList && dataList instanceof List) {
            data = new ArrayList<Object>(((List<Object>)dataList).size());
            
            for (Object bean : (List<Object>)dataList) {
                ((List<Object>)data).add(toTreeItem(bean, false));
            }
	    }
	}
	
	@SuppressWarnings("unchecked")
    private Object toTreeItem(Object bean, boolean isModelBean) {
	    String[] props = findPropertys();
	    // 如果bean不是T类型的实体且没有指定F_in值，则不进行模型转换.
	    if ((null == props || props.length == 0) && !isModelBean) {
	        return bean;
	    }
	    Map<String, Object> item = new HashMap<String, Object>();
        
        String prop     = props[0];
        Object value    = null, idValue  = null;
        // 第一个属性为节点显示名称属性
        item.put(PROP_TEXT, getValueFromBean(props[0], bean, false)); 
        // 设置节点的其他属性
        for (int i = 1; i < props.length; i++) {
            prop = props[i];
            value= getValueFromBean(props[i], bean, false);
            if (prop.equals(getIdAttrName())) {
                prop = PROP_ID;
                idValue = value;
            }
            item.put(prop, value);
        }
        if (null != getIdAttrName() && ("," + props.toString() + ",").indexOf(getIdAttrName()) < 0) {
            idValue = getValueFromBean(getIdAttrName(), bean, false);
            item.put(PROP_ID, idValue);
        }
        // 是否选中(checked)属处理
        if (null != checked && checked.length() > 0) {
            if (isModelBean) {
                if (isChecked(((T)bean).getId())) item.put(PROP_CHECKED, Boolean.TRUE);
            } else {
                if (null == getIdAttrName()) {
                    throw new BusinessException("设置处理是否选中失败，请给参数名P_ID传入id属性名称！");
                }
                if (null == idValue) {
                    throw new BusinessException("设置处理是否选中失败(原因id为空)，请检查参数名P_ID传入id属性名称是否正确？");
                }
                if (isChecked(idValue)) item.put(PROP_CHECKED, Boolean.TRUE);
            }
        }
        // 是否打开(open)属性处理
        if (getOpen()) {
            item.put(PROP_OPEN, Boolean.TRUE);
        }
        // 是否为父节点(isParent)属性处理
        if (null != getParentAttrName()) {
            item.put(PROP_ISPARENT, getValueFromBean(getParentAttrName(), bean, true));                
        } else {
            item.put(PROP_ISPARENT, Boolean.TRUE);   
        }
        // 节点图标(icon/iconClose/iconOpen)处理
        if (null != getIconAttrName()) {
            //
            String[] iconArr = getIconAttrName().split(",");
            if (iconArr.length > 0) {
                item.put(PROP_ICON, getValueFromBean(iconArr[0], bean, false));
            }
            if (iconArr.length > 1) {
                item.put(PROP_ICONCLOSE, getValueFromBean(iconArr[1], bean, false));
            }
            if (iconArr.length > 2) {
                item.put(PROP_ICONOPEN, getValueFromBean(iconArr[2], bean, false));
            }
        }
        
        return item;
    }
	
	/**
     * qiucs 2014-7-7 
     * <p>描述: 验证给定ID是否在勾选列表中.</p>
	 */
    private boolean isChecked(Object id) {
        if (!org.apache.commons.lang3.StringUtils.isBlank(checked)) {
            if ("all".equals(checked) || ("," + checked + ",").indexOf("," + id + ",") != -1) {
                return true;
            }
        }

        return false;
    }
    
    /**
     * qiucs 2014-7-7 
     * <p>描述: 获取bean中prop属性的值</p>
     * @param  prop 属性名称
     * @param  bean 实体
     * @param  isBoolean 是否强转为boolean类型
     * @return Object    返回类型   
     * @throws
     */
    @SuppressWarnings("rawtypes")
    private Object getValueFromBean(String prop, Object bean, boolean isBoolean) {
        Object value = null;
        try {
            if (bean instanceof Map) {
                if (!((Map)bean).containsKey(prop)) {
                    throw new BusinessException("从实体类["+bean.getClass() + "]中键值[" + prop + "]不存在！");
                }
                return ((Map)bean).get(prop);
            }
            Class<?> toType = BeanUtils.findPropertyType(prop, new Class[]{ bean.getClass() });
            if (toType == Boolean.class) {
                value = (Boolean)Ognl.getValue(prop, bean, toType);
            } else {
                if (isBoolean) {
                    value = Long.parseLong(Ognl.getValue(prop, bean, toType).toString()) > 1;
                } else {
                    value = Ognl.getValue(prop, bean, toType);
                }
            }
        } catch (OgnlException e) {
            throw new BusinessException("从实体类["+bean.getClass() + "." + prop + "]中获取节点属性失败", e);
        }
        return value;
    }
	
	/**
     * qiucs 2014-7-7 
     * <p>描述: 从实体中获取节点名称及是否存在子节点属性.</p>
     */
    private String[] findPropertys() {
        if (null == this.inProperties) {
            this.inProperties = getFilter().getInPropertys(FilterHelper.getFilterId(getModelClass()));
        }
        
        if (null == this.inProperties || this.inProperties.length == 0){
            throw new BusinessException("必须使用F_in指定实体中的那个属性用来作为数节点属性");
        }
        
        return this.inProperties;
    }
	
	/**
	 * qiucs 2014-7-7 
	 * <p>描述: 设置实体CLASS</p>
	 * @return Class<Service>    返回类型   
	 */
    public void setModelClass(Class<T> clazz) {
        this.clazz = clazz;
    }
    
    /**
     * qiucs 2014-7-7 
     * <p>描述: </p>
     * @return Class<Service>    返回类型   
     */
    @JsonIgnore
    public Class<T> getModelClass() {
        return this.clazz;
    }
    
    @JsonIgnore
	public boolean getOpen() {
        return open;
    }

	public void setOpen(String open) {
        this.open = StringUtils.isBooleanTrue(open);
    }

	@JsonIgnore
    public String getParentAttrName() {
        return parentAttrName;
    }

    public void setParentAttrName(String parentAttrName) {
        this.parentAttrName = parentAttrName;
    }

    @JsonIgnore
    public String getIconAttrName() {
        return iconAttrName;
    }

    public void setIconAttrName(String iconAttrName) {
        this.iconAttrName = iconAttrName;
    }

    @JsonIgnore
    public String getChecked() {
        return checked;
    }

	public void setChecked(String checked) {
		this.checked = checked;
	}

	@JsonIgnore
    public String getIdAttrName() {
        return idAttrName;
    }

    public void setIdAttrName(String idAttrName) {
        this.idAttrName = idAttrName;
    }

}
