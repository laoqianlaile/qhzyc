package com.ces.config.service.base;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.ces.config.datamodel.message.MessageModel;
import com.ces.config.utils.StringUtil;
import com.ces.xarch.core.entity.StringIDEntity;
import com.ces.xarch.core.persistence.jpa.StringIDDao;
import com.ces.xarch.core.service.StringIDDefineDaoService;
import com.ces.xarch.core.utils.SearchFilter;
import com.ces.xarch.core.utils.SearchFilter.Operator;
import com.ces.xarch.core.utils.SearchHelper;
import com.ces.xarch.core.web.listener.XarchListener;

/**
 * <p>描述: 系统配置平台基类Service</p>
 * <p>公司: 上海中信信息发展股份有限公司</p>
 * @author qiucs   
 * @date 2013-7-12 下午2:13:21
 *
 * @param <T>
 * @param <Dao>
 */
@Transactional(readOnly = true)
public class StringIDConfigDefineDaoService<T extends StringIDEntity, Dao extends StringIDDao<T>> extends StringIDDefineDaoService<T, Dao>{
    
    /** 日志 **/
    private static Log log = LogFactory.getLog(StringIDConfigDefineDaoService.class);
    
    /**
     * qiucs 2014-1-20 
     * <p>描述: 获取Service容器中Dao的类全名</p>
     * @return Class<Dao>    返回类型   
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    private Class<Dao> getDaoClass() {
        Class clazz = getClass();  
        
        while (clazz != Object.class) {  
            Type t = clazz.getGenericSuperclass();  
            if (t instanceof ParameterizedType) {  
                Type[] args = ((ParameterizedType) t).getActualTypeArguments();  
                if (args.length > 1 && args[1] instanceof Class) {  
                    return (Class<Dao>) args[1];  
                } 
            }  
            clazz = clazz.getSuperclass();  
        }  
        
        return null;
    }
    
    /**
     * qiucs 2014-1-20 
     * <p>描述: 获取Service容器中Dao的类全名</p>
     * @return Class<Dao>    返回类型   
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    private Class<T> getModelClass() {
        Class clazz = getClass();  
        
        while (clazz != Object.class) {  
            Type t = clazz.getGenericSuperclass();  
            if (t instanceof ParameterizedType) {  
                Type[] args = ((ParameterizedType) t).getActualTypeArguments();  
                if (args.length > 1 && args[0] instanceof Class) {  
                    return (Class<T>) args[0];  
                } 
            }  
            clazz = clazz.getSuperclass();  
        }  
        
        return null;
    }
    
    /*
     * (非 Javadoc)   
     * <p>描述: 复写获取Dao，可以自动获取Service容器中指定的Dao</p>   
     * @see com.ces.xarch.core.service.AbstractService#getDao()
     * modify by qiucs @2014-1-20
     */
    @Override
    protected Dao getDao() {
        Dao dao = super.getDao();
        if (null == dao) {
            dao = XarchListener.getBean(getDaoClass());
            setDao(dao);
        }
        return dao;
    }

    /* 
     * (非 Javadoc)   
     * <p>标题: delete</p>   
     * <p>描述: 多个删除处理</p>   
     * @param ids
     *        多个ID用逗号隔开   
     * @see com.ces.xarch.core.service.AbstractService#delete(java.io.Serializable)
     */
    @Override
    @Transactional
    public void delete(String ids) {
        String[] idArr = ids.split(",");
        for (int i = 0; i < idArr.length; i++) {
            super.delete(idArr[i]);
        }
    }
    
    /**
     * <p>标题: checkUnique</p>
     * <p>描述: 唯一性检查</p>
     * @param  spec
     * @param  id
     * @return boolean    返回类型   
     *         true 唯一 
     *         false 不唯一
     * @throws
     */
    @Transactional(propagation=Propagation.NOT_SUPPORTED, readOnly=true)
    public MessageModel checkUnique(Specification<T> spec, String id) {
        try {
            List<T> list = find(spec);
            if (null == list || list.isEmpty() || 
                    (StringUtil.isNotEmpty(id) && list.size() == 1 && id.equals(list.get(0).getId()))) {
                return MessageModel.trueInstance("OK");
            }
            return MessageModel.falseInstance("OK");
        } catch (Exception e) {
            log.error("唯一性检查(" + spec + ")", e);
            return MessageModel.falseInstance("ERROR");
        }
    }
    
    /**
     * <p>标题: findOne</p>
     * <p>描述: </p>
     * @param  spec
     * @return T    返回类型   
     * @throws
     */
    public T findOne(Specification<T> spec) {
        return getDao().findOne(spec);
    }
    
    /**
     * qiucs 2014-12-26 下午1:51:04
     * <p> 描述: 条件检索  </p>
     * @param filters  
     *        格式："EQ_name=xx;EQ_age=10": 相当于  name='xx' and age = 10
     * @return T 
     *        返回实体对象
     */
    public T findOne(String filters) {
    	List<T> list = find(filters);
    	if (list.size() > 0) {
    		if (list.size() > 1) {
    			log.warn("此条件查询结果有（" + list.size() + "）个，请检查查询条件是否正确！");
    		}
    		return list.get(0);
    	}
        return null;
    }
    
    /**
     * <p>标题: findAll</p>
     * @return List<T>
     */
    public List<T> findAll() {
        return getDao().findAll();
    }
    
    /**
     * qiucs 2014-4-15 
     * <p>描述: 根据ID判断实体存不存在</p>
     * @param  id
     * @return boolean    返回类型   
     */
    public MessageModel exists(String id) {
    	long cnt = count("EQ_id=" + id);
    	if (cnt == 0) return MessageModel.falseInstance("OK");
        return MessageModel.trueInstance("OK");
    }
    
    /**
     * qiucs 2014-12-15 
     * <p> 描述: 条件检索  </p>
     * @param filters  
     *        格式："EQ_name=xx;LT_age=10": 相当于  name='xx' and age < 10
     * @param sort
     *        排序字段，如 new Sort("showOrder") / new Sort("showOrder", "desc") 
     */
    @Transactional(propagation=Propagation.NOT_SUPPORTED, readOnly=true)
    public List<T> find(String filters, Sort sort) {
    	return find(buildSpecification(filters), sort);
    }
    
    /**
     * qiucs 2014-12-15 
     * <p> 描述: 条件检索  </p>
     * @param filters  
     *        格式："EQ_name=xx;LT_age=10": 相当于  name='xx' and age < 10
     */
    @Transactional(propagation=Propagation.NOT_SUPPORTED, readOnly=true)
    public List<T> find(String filters) {
    	return find(buildSpecification(filters));
    }
    
    /**
     * qiucs 2014-12-16 
     * <p> 描述: 按条件统计  </p>
     * @param filters  
     *        格式："EQ_name=xx;LT_age=10": 相当于  name='xx' and age < 10
     */
    @Transactional(propagation=Propagation.NOT_SUPPORTED, readOnly=true)
    public long count(String filters) {
    	return getDao().count(buildSpecification(filters));
    }
    
    /**
     * qiucs 2014-12-15 
     * <p>描述: 将filters转换为Specification对象</p>
     */
    protected Specification<T> buildSpecification(String filters) {
    	List<SearchFilter> list = new ArrayList<SearchFilter>();
    	String[] filterArr = filters.split(";");
    	String filterName, op, value;
    	for (String filter: filterArr) {
    		String[] items = filter.split("=");
    		int idx = items[0].indexOf("_");
    		op = items[0].substring(0, idx);
    		filterName= items[0].substring(idx + 1);
    		if (items.length > 1) value = items[1];
    		else value = "";
    		
    		list.add(new SearchFilter(filterName, getSearchOperator(op), value));
    	}
    	
    	return SearchHelper.buildSpecification(list, getModelClass());
    }
    
    /**
     * qiucs 2014-12-15 
     * <p>描述: 将操作符转换为SearchFilter.Operator对象</p>
     */
    private Operator getSearchOperator(String op) {
    	// EQ, LIKE, GT, LT, GTE, LTE, NULL, BLANK, NNULL, NBLANK
    	if ("EQ".equals(op)) return Operator.EQ;
    	if ("LIKE".equals(op)) return Operator.LIKE;
    	if ("GT".equals(op)) return Operator.GT;
    	if ("LT".equals(op)) return Operator.LT;
    	if ("GTE".equals(op)) return Operator.GTE;
    	if ("LTE".equals(op)) return Operator.LTE;
    	if ("NULL".equals(op)) return Operator.NULL;
    	if ("BLANK".equals(op)) return Operator.BLANK;
    	if ("NNULL".equals(op)) return Operator.NNULL;
    	if ("NBLANK".equals(op)) return Operator.NBLANK;
    	return Operator.EQ;
    }
}
