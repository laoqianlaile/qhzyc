package com.ces.config.dhtmlx.service.component;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.ces.config.dhtmlx.dao.component.ComponentAreaDao;
import com.ces.config.dhtmlx.entity.component.ComponentArea;
import com.ces.config.dhtmlx.json.entity.common.DhtmlxComboOption;
import com.ces.config.dhtmlx.service.base.ConfigDefineDaoService;
import com.ces.config.utils.ComponentInfoUtil;

/**
 * 构件分类Service
 * 
 * @author wanglei
 * @date 2013-07-18
 */
@Component("componentAreaService")
public class ComponentAreaService extends ConfigDefineDaoService<ComponentArea, ComponentAreaDao> {

    /*
     * (non-Javadoc)
     * @see com.ces.xarch.core.service.StringIDService#setDaoUnBinding(com.ces.xarch.core.persistence.jpa.StringIDDao)
     */
    @Autowired
    @Qualifier("componentAreaDao")
    @Override
    protected void setDaoUnBinding(ComponentAreaDao dao) {
        super.setDaoUnBinding(dao);
    }

    /*
     * (non-Javadoc)
     * @see com.ces.xarch.core.service.AbstractService#save(com.ces.xarch.core.entity.BaseEntity)
     */
    @Override
    @Transactional
    public ComponentArea save(ComponentArea entity) {
        ComponentArea componentArea = super.save(entity);
        ComponentInfoUtil.getInstance().putComponentArea(componentArea);
        return componentArea;
    }

    /**
     * 根据名称获取构件分类
     * 
     * @param name 构件分类名称
     * @param parentId 父构件分类ID
     * @return ComponentArea
     */
    public ComponentArea getByNameAndParentId(String name, String parentId) {
        return getDao().getByNameAndParentId(name, parentId);
    }

    /**
     * 获取子构件分类
     * 
     * @param parentId 父构件分类ID
     * @return List<ComponentArea>
     */
    public List<ComponentArea> getComponentAreaListByParentId(String parentId) {
        return getDao().getByParentId(parentId);
    }

    /**
     * 获取构件分类下子构件分类最大显示顺序
     * 
     * @param parentId 父构件分类ID
     * @return Integer
     */
    public Integer getMaxShowOrder(String parentId) {
        return getDao().getMaxShowOrder(parentId);
    }

    /**
     * 获取显示顺序范围内的构件分类
     * 
     * @param start 开始显示顺序
     * @param end 结束显示顺序
     * @param parentId 父构件分类ID
     * @return List<ComponentArea>
     */
    public List<ComponentArea> getComponentAreaListByShowOrder(Integer start, Integer end, String parentId) {
        return getDao().getByShowOrderBetweenAndParentId(start, end, parentId);
    }

    /**
     * 将构件分类的显示顺序加一
     * 
     * @param start 开始显示顺序
     * @param parentId 父构件分类ID
     */
    public void updateShowOrderPlusOne(Integer start, String parentId) {
        getDao().updateShowOrderPlusOne(start, parentId);
    }

    /**
     * qiucs 2014-2-20
     * <p>
     * 描述: 构件分类下拉框
     * </p>
     */
    public Object comboOfComponentArea() {
        List<DhtmlxComboOption> opts = new ArrayList<DhtmlxComboOption>();
        List<ComponentArea> list = getComponentAreaListByParentId("-1");
        for (ComponentArea entity : list) {
            DhtmlxComboOption option = new DhtmlxComboOption();
            option.setValue(entity.getId());
            option.setText(entity.getName());
            String preText = "";
            opts.add(option);
            opts = cet(opts, entity.getId(), preText);
        }
        return opts;
    }

    /**
     * qiucs 2014-2-20
     * <p>
     * 描述:
     * </p>
     */
    private List<DhtmlxComboOption> cet(List<DhtmlxComboOption> opts, String parentId, String preText) {
        preText += "　　";
        List<ComponentArea> list = getComponentAreaListByParentId(parentId);
        if (list.isEmpty())
            return opts;
        for (ComponentArea entity : list) {
            DhtmlxComboOption option = new DhtmlxComboOption();
            option.setValue(entity.getId());
            option.setText(preText + entity.getName());
            opts.add(option);
            opts = cet(opts, entity.getId(), preText);
        }
        return opts;
    }
}
