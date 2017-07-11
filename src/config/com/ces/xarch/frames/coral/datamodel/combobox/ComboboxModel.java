package com.ces.xarch.frames.coral.datamodel.combobox;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ognl.Ognl;
import ognl.OgnlException;

import org.springframework.beans.BeanUtils;

import com.ces.xarch.core.entity.BaseEntity;
import com.ces.xarch.core.exception.BusinessException;
import com.ces.xarch.core.utils.FilterHelper;
import com.ces.xarch.core.web.frame.impl.DefaultDataModel;
import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * <p>描述: 下拉框 数据模型</p>
 * <p>公司: 上海中信信息发展股份有限公司</p>
 * @author qiucs   
 * @date 2014-7-18 下午5:9:37
 *
 * @param <T>
 * @param <ID>
 */
public class ComboboxModel<T extends BaseEntity<ID>,ID extends Serializable> extends DefaultDataModel<T, ID> {
	
	/** 选中选项参数名称. */
	public static final String SELECTED_PARAM = "P_SELECTED";
	
	/** 选中的option值. */
	private String selected;
	
    private final String PROP_VALUE     = "value";  
    private final String PROP_SELECTED = "selected";
    private final String PROP_TEXT     = "text";   
    
    // 实体bean Class
    private Class<T> clazz = null;
    // 需要转JSON的属性集
    private String[] inProperties;
	
	/**
     * qiucs 2014-7-18  
     * <p>描述: 构造函数.</p>
	 */
	public ComboboxModel() {
	    
	}

	@SuppressWarnings("unchecked")
	@Override
	public void setData(List<T> dataList) {
	    
		if (dataList != null && dataList.size() > 0) {
			data = new ArrayList<Object>(dataList.size());
			
			for (T bean : dataList) {
				((List<Object>)data).add(toOption(bean, true));
			}
			
		}
	}
	
	@SuppressWarnings("unchecked")
    public void setData(Object dataList) {
	    if (null != dataList && dataList instanceof List) {
            data = new ArrayList<Object>(((List<Object>)dataList).size());
            
            for (Object bean : (List<Object>)dataList) {
                ((List<Object>)data).add(toOption(bean, false));
            }
	    }
	}
	
    private Object toOption(Object bean, boolean isModelBean) {
	    String[] props = findPropertys();
	    
	    if (null == props || props.length < 2) {
	        throw new BusinessException("请给参数名F_in指定下拉选项的“隐藏值/显示值”属性名称！");
	    }
	    Map<String, Object> item = new HashMap<String, Object>();
	    Object value = getValueFromBean(props[0], bean, false), // 第一个属性为下拉选项"隐藏值"属性
	            text = getValueFromBean(props[1], bean, false); // 第二个属性为下拉选项"显示值"属性
        
        // 第一个属性为节点显示名称属性
        item.put(PROP_TEXT , text); 
        item.put(PROP_VALUE, value);
        // 是否选中(selected)处理
        if (isSelected(value)) item.put(PROP_SELECTED, true);
        
        return item;
    }
	
	/**
     * qiucs 2014-7-18 
     * <p>描述: 验证是否选中.</p>
	 */
    private boolean isSelected(Object value) {
        if (!org.apache.commons.lang3.StringUtils.isBlank(selected)) {
            if (("," + selected + ",").indexOf("," + value + ",") != -1) {
                return true;
            }
        }

        return false;
    }
    
    /**
     * qiucs 2014-7-18 
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
     * qiucs 2014-7-18  
     * <p>描述: 从实体中获取节点名称及是否存在子节点属性.</p>
     */
    private String[] findPropertys() {
        if (null == this.inProperties) {
            this.inProperties = getFilter().getInPropertys(FilterHelper.getFilterId(getModelClass()));
        }
        
        if (null == this.inProperties || this.inProperties.length == 0){
            throw new BusinessException("必须使用F_in指定实体中的那个属性用来作为下拉框选项属性");
        }
        
        return this.inProperties;
    }
	/**
	 * qiucs 2014-7-18  
	 * <p>描述: 设置实体CLASS</p>
	 * @return Class<Service>    返回类型   
	 */
    public void setModelClass(Class<T> clazz) {
        this.clazz = clazz;
    }
    
    /**
     * qiucs 2014-7-18  
     * <p>描述: </p>
     * @return Class<Service>    返回类型   
     */
    @JsonIgnore
    public Class<T> getModelClass() {
        return this.clazz;
    }
    @JsonIgnore
    public String getSelected() {
        return selected;
    }

    public void setSelected(String selected) {
        this.selected = selected;
    }

}
