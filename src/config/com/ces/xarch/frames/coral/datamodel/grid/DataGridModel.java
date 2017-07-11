package com.ces.xarch.frames.coral.datamodel.grid;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;

import com.ces.xarch.core.entity.BaseEntity;
import com.ces.xarch.core.exception.BusinessException;
import com.ces.xarch.core.utils.FilterHelper;
import com.ces.xarch.core.web.frame.impl.DefaultDataModel;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.common.collect.Maps;

/**
 * <p>描述: 组件库列表数据模型</p>
 * <p>公司: 上海中信信息发展股份有限公司</p>
 * @author qiucs   
 * @date 2014-7-23 下午3:12:29
 *
 * @param <T>
 * @param <ID>
 */
public class DataGridModel<T extends BaseEntity<ID>,ID extends Serializable> extends DefaultDataModel<T, ID> {

    // 实体bean Class
    private Class<T> clazz = null;
    // 需要转JSON的属性集
    private String[] inProperties;
    
    /*
     * (非 Javadoc)   
     * <p>描述: </p>   
     * @param dataList   
     * @see com.ces.xarch.core.web.frame.impl.DefaultDataModel#setData(java.lang.Object)
     */
    public void setData(Object dataList) {
        if (null != dataList && dataList instanceof List) {
            cast2model((List<?>)dataList);
             
        } else if (null != dataList && dataList instanceof Page) {
            cast2model(((Page<?>)dataList).getContent());
            this.pageNumber = ((Page<?>)dataList).getNumber()+1;
            this.pageSize = ((Page<?>)dataList).getSize();
            this.totalPages = ((Page<?>)dataList).getTotalPages();
            this.total = ((Page<?>)dataList).getTotalElements();
        } else {
            this.data = dataList;
        }
    }
    
    /**
     * qiucs 2014-7-23 
     * <p>描述: 转换成组件库列表数据模型</p>
     * @param data    设定参数   
     * @throws
     */
    @SuppressWarnings("unchecked")
    private void cast2model(List<?> data) {
        if (null == data) return;
        if (((List<?>)data).get(0) instanceof Object[]) {
            this.data = new ArrayList<Object>(((List<?>)data).size());
            for (Object[] bean : (List<Object[]>)data) {
                ((List<Object>)this.data).add(toRowItem(bean, false));
            }
        } else {
            this.data = data;
        }
    }

    /**
     * qiucs 2014-7-23 
     * <p>描述: 转换为组件库列表一行数据格式</p>
     * @param  bean
     * @param  isModelBean
     * @return Object    返回类型   
     * @throws
     */
    private Object toRowItem(Object[] bean, boolean isModelBean) {
        Map<String, Object> item = Maps.newHashMap();
        String[] props = findPropertys();
        
        if (bean.length < props.length) {
            throw new BusinessException("必须使用F_in指定属性数(" + props.length + ")多于查询结果集中列数(" + bean.length +")，请检查！");
        }
        
        for (int i = 0; i < props.length; i++) {
            item.put(props[i], bean[i]);
        }
        
        return item;
    }
    
    /**
     * qiucs 2014-7-23 
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

    @JsonIgnore
    public Class<T> getModelClass() {
        return clazz;
    }

    public void setModelClass(Class<T> clazz) {
        this.clazz = clazz;
    }
    
}
