package com.ces.config.dhtmlx.service.component;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.ces.config.dhtmlx.dao.component.ComponentAssembleAreaDao;
import com.ces.config.dhtmlx.entity.component.ComponentAssembleArea;
import com.ces.config.dhtmlx.json.entity.common.DhtmlxComboOption;
import com.ces.config.dhtmlx.service.base.ConfigDefineDaoService;
import com.ces.config.utils.ComponentInfoUtil;

/**
 * 组合构件分类Service
 * 
 * @author qiujinwei
 * @date 2015-04-08
 */
@Component("componentAssembleAreaService")
public class ComponentAssembleAreaService extends ConfigDefineDaoService<ComponentAssembleArea, ComponentAssembleAreaDao> {

    /*
     * (non-Javadoc)
     * @see com.ces.xarch.core.service.StringIDService#setDaoUnBinding(com.ces.xarch.core.persistence.jpa.StringIDDao)
     */
    @Autowired
    @Qualifier("componentAssembleAreaDao")
    @Override
    protected void setDaoUnBinding(ComponentAssembleAreaDao dao) {
        super.setDaoUnBinding(dao);
    }

    /*
     * (non-Javadoc)
     * @see com.ces.xarch.core.service.AbstractService#save(com.ces.xarch.core.entity.BaseEntity)
     */
    @Override
    @Transactional
    public ComponentAssembleArea save(ComponentAssembleArea entity) {
        ComponentAssembleArea componentAssembleArea = super.save(entity);
        ComponentInfoUtil.getInstance().putComponentAssembleArea(componentAssembleArea);
        return componentAssembleArea;
    }

    /**
     * 根据名称获取构件分类
     * 
     * @param name 构件分类名称
     * @param parentId 父构件分类ID
     * @return ComponentAssembleArea
     */
    public ComponentAssembleArea getByNameAndParentId(String name, String parentId) {
        return getDao().getByNameAndParentId(name, parentId);
    }

    /**
     * 获取子构件分类
     * 
     * @param parentId 父构件分类ID
     * @return List<ComponentAssembleArea>
     */
    public List<ComponentAssembleArea> getComponentAssembleAreaListByParentId(String parentId) {
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
     * @return List<ComponentAssembleArea>
     */
    public List<ComponentAssembleArea> getComponentAssembleAreaListByShowOrder(Integer start, Integer end, String parentId) {
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
    public Object comboOfComponentAssembleArea() {
        List<DhtmlxComboOption> opts = new ArrayList<DhtmlxComboOption>();
        List<ComponentAssembleArea> list = getComponentAssembleAreaListByParentId("-1");
        for (ComponentAssembleArea entity : list) {
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
        List<ComponentAssembleArea> list = getComponentAssembleAreaListByParentId(parentId);
        if (list.isEmpty())
            return opts;
        for (ComponentAssembleArea entity : list) {
            DhtmlxComboOption option = new DhtmlxComboOption();
            option.setValue(entity.getId());
            option.setText(preText + entity.getName());
            opts.add(option);
            opts = cet(opts, entity.getId(), preText);
        }
        return opts;
    }
}
