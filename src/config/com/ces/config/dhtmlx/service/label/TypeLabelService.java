package com.ces.config.dhtmlx.service.label;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.ces.config.dhtmlx.dao.label.TypeLabelDao;
import com.ces.config.dhtmlx.entity.label.TypeLabel;
import com.ces.config.dhtmlx.service.base.ConfigDefineDaoService;

/**
 * 分类标签Service
 * 
 * @author wangjianmin
 * @date 2013-07-25
 */
@Component("typeLabelService")
public class TypeLabelService extends ConfigDefineDaoService<TypeLabel, TypeLabelDao> {

    /*
     * (non-Javadoc)
     * @see com.ces.xarch.core.service.StringIDService#setDaoUnBinding(com.ces.xarch.core.persistence.jpa.StringIDDao)
     */
    @Autowired
    @Qualifier("typeLabelDao")
    @Override
    protected void setDaoUnBinding(TypeLabelDao dao) {
        super.setDaoUnBinding(dao);
    }

    /*
     * (non-Javadoc)
     * @see com.ces.xarch.core.service.AbstractService#save(com.ces.xarch.core.entity.BaseEntity)
     */
    @Override
    @Transactional
    public TypeLabel save(TypeLabel entity) {
        return super.save(entity);
    }

    /*
     * (non-Javadoc)
     * @see com.ces.config.dhtmlx.service.base.ConfigDefineDaoService#delete(java.lang.String)
     */
    @Override
    @Transactional
    public void delete(String ids) {
        String[] idArr = ids.split(",");
        for (int i = 0; i < idArr.length; i++) {
            getDao().delete(idArr[i]);
        }
    }

    /**
     * 根据名称获取分类标签
     * 
     * @param name 分类标签名称
     * @return TypeLabel
     */
    public TypeLabel getTypeLabelByName(String name) {
        return getDao().getByName(name);
    }

    /**
     * 根据编码获取分类标签
     * 
     * @param code 分类标签编码
     * @return TypeLabel
     */
    public TypeLabel getTypeLabelByCode(String code) {
        return getDao().getByCode(code);
    }
    
    /**
     * 根据名称获取分类标签(区分菜单)
     * 
     * @param name 分类标签名称
     * @return TypeLabel
     */
    public TypeLabel getTypeLabelByName(String name, String menuId) {
        return getDao().getByNameAndMenuId(name, menuId);
    }

    /**
     * 根据编码获取分类标签(区分菜单)
     * 
     * @param code 分类标签编码
     * @return TypeLabel
     */
    public TypeLabel getTypeLabelByCode(String code, String menuId) {
        return getDao().getByCodeAndMenuId(code, menuId);
    }

    /**
     * 获取分类标签最大显示顺序
     * 
     * @return Integer
     */
    public Integer getMaxShowOrder() {
        return getDao().getMaxShowOrder();
    }

    /**
     * 获取显示顺序范围内的分类标签
     * 
     * @param start 开始显示顺序
     * @param end 结束显示顺序
     * @return List<TypeLabel>
     */
    public List<TypeLabel> getTypeLabelListByShowOrder(Integer start, Integer end) {
        return getDao().getByShowOrderBetween(start, end);
    }
    
    /**
     * 获取显示顺序范围内的分类标签(区分菜单)
     * 
     * @param start 开始显示顺序
     * @param end 结束显示顺序
     * @return List<TypeLabel>
     */
    public List<TypeLabel> getTypeLabelListByShowOrder(Integer start, Integer end, String menuId) {
        return getDao().getByShowOrderBetweenAndMenuId(start, end, menuId);
    }
}
