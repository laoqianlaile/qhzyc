package com.ces.config.dhtmlx.service.label;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.ces.config.dhtmlx.dao.label.ColumnLabelDao;
import com.ces.config.dhtmlx.entity.label.ColumnLabel;
import com.ces.config.dhtmlx.service.appmanage.ColumnDefineService;
import com.ces.config.dhtmlx.service.base.ConfigDefineDaoService;
import com.ces.config.utils.StringUtil;
import com.ces.config.utils.TableUtil;

/**
 * 字段标签Service
 * 
 * @author wanglei
 * @date 2013-07-23
 */
@Component("columnLabelService")
public class ColumnLabelService extends ConfigDefineDaoService<ColumnLabel, ColumnLabelDao> {

    /*
     * (non-Javadoc)
     * @see com.ces.xarch.core.service.StringIDService#setDaoUnBinding(com.ces.xarch.core.persistence.jpa.StringIDDao)
     */
    @Autowired
    @Qualifier("columnLabelDao")
    @Override
    protected void setDaoUnBinding(ColumnLabelDao dao) {
        super.setDaoUnBinding(dao);
    }

    /*
     * (non-Javadoc)
     * @see com.ces.xarch.core.service.AbstractService#save(com.ces.xarch.core.entity.BaseEntity)
     */
    @Override
    @Transactional
    public ColumnLabel save(ColumnLabel entity) {
        if (StringUtil.isNotEmpty(entity.getId())) {
            ColumnLabel oldColumnLabel = getByID(entity.getId());
            if (!entity.getCode().equals(oldColumnLabel.getCode())) {
                getService(ColumnDefineService.class).batchUpdateColumnLabel(oldColumnLabel.getCode(), entity.getCode());
            }
        }
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
            ColumnLabel columnLabel = getByID(idArr[i]);
            getService(ColumnDefineService.class).batchUpdateColumnLabel(columnLabel.getCode(), null);
            getDao().delete(idArr[i]);
            TableUtil.removeColumnLabel(columnLabel.getCode());
        }
    }

    /**
     * 根据名称获取字段标签
     * 
     * @param name 字段标签名称
     * @return ColumnLabel
     */
    public ColumnLabel getByName(String name) {
        return getDao().getByName(name);
    }

    /**
     * 根据编码获取字段标签
     * 
     * @param code 字段标签编码
     * @return ColumnLabel
     */
    public ColumnLabel getByCode(String code) {
        return getDao().getByCode(code);
    }

    /**
     * 获取字段标签最大显示顺序
     * 
     * @param categoryId 分类ID
     * @return Integer
     */
    public Integer getMaxShowOrder(String categoryId) {
        return getDao().getMaxShowOrder(categoryId);
    }

    /**
     * 获取显示顺序范围内的字段标签
     * 
     * @param start 开始显示顺序
     * @param end 结束显示顺序
     * @param categoryId 分类ID
     * @return List<ColumnLabel>
     */
    public List<ColumnLabel> getColumnLabelListByShowOrder(Integer start, Integer end, String categoryId) {
        return getDao().getByShowOrderBetweenAndCategoryId(start, end, categoryId);
    }

    /**
     * 获取所有字段标签
     * 
     * @return List<ColumnLabel>
     */
    public List<ColumnLabel> getAllColumnLabel() {
        return getDao().getAllColumnLabel();
    }

    /**
     * 根据分类Id获取字段标签
     * 
     * @param categoryId 分类ID
     * @return List<ColumnLabel>
     */
    public List<ColumnLabel> getColumnLabelList(String categoryId) {
        return getDao().getColumnLabelList(categoryId);
    }

    /**
     * 更改分类
     * 
     * @param categoryId 分类ID
     * @param columnLabelIds 字段标签IDs
     */
    @Transactional
    public void changeCategory(String categoryId, String[] columnLabelIds) {
        for (String columnLabelId : columnLabelIds) {
            ColumnLabel columnLabel = getByID(columnLabelId);
            if (columnLabel != null) {
                columnLabel.setCategoryId(categoryId);
                getDao().save(columnLabel);
            }
        }
    }

    /**
     * 剔除本表已经绑定的字段标签
     * 
     * @param tableId
     * @return
     */
    public Object getUnBindedLabel(String tableId) {
        return getDao().getUnBindedLabel(tableId);
    }
}
