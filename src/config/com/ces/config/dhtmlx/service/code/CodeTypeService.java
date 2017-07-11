package com.ces.config.dhtmlx.service.code;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.ces.config.dhtmlx.dao.code.CodeDao;
import com.ces.config.dhtmlx.dao.code.CodeTypeDao;
import com.ces.config.dhtmlx.dao.common.DatabaseHandlerDao;
import com.ces.config.dhtmlx.entity.code.CodeType;
import com.ces.config.dhtmlx.json.entity.common.DhtmlxComboOption;
import com.ces.config.dhtmlx.service.appmanage.ColumnDefineService;
import com.ces.config.dhtmlx.service.base.ConfigDefineDaoService;
import com.ces.config.utils.CodeUtil;
import com.ces.config.utils.StringUtil;
import com.google.common.collect.Lists;

/**
 * 编码类型Service
 * 
 * @author wanglei
 * @date 2013-07-15
 */
@Component("codeTypeService")
public class CodeTypeService extends ConfigDefineDaoService<CodeType, CodeTypeDao> {

    /*
     * (non-Javadoc)
     * @see com.ces.xarch.core.service.StringIDService#setDaoUnBinding(com.ces.xarch.core.persistence.jpa.StringIDDao)
     */
    @Autowired
    @Qualifier("codeTypeDao")
    @Override
    protected void setDaoUnBinding(CodeTypeDao dao) {
        super.setDaoUnBinding(dao);
    }

    /*
     * (non-Javadoc)
     * @see com.ces.xarch.core.service.AbstractService#save(com.ces.xarch.core.entity.BaseEntity)
     */
    @Override
    @Transactional
    public CodeType save(CodeType entity) {
        if (StringUtil.isNotEmpty(entity.getId())) {
            CodeType oldEntity = getByID(entity.getId());
            if (!oldEntity.getCode().equals(entity.getCode())) {
                getDaoFromContext(CodeDao.class).updateCodeTypeCode(oldEntity.getCode(), entity.getCode());
            }
            // 如果从业务表编码改成非业务表编码，删除缓存中的编码
            if ("1".equals(oldEntity.getIsBusiness()) && "0".equals(entity.getIsBusiness())) {
                CodeUtil.getInstance().removeCode(oldEntity.getCode());
                getService(BusinessCodeService.class).deleteByCodeTypeCode(oldEntity.getCode());
            }
            // 如果从非业务表编码改成业务表编码，删除缓存中的编码
            if ("0".equals(oldEntity.getIsBusiness()) && "1".equals(entity.getIsBusiness())) {
                CodeUtil.getInstance().removeCode(oldEntity.getCode());
                getDaoFromContext(CodeDao.class).deleteByCodeTypeCode(oldEntity.getCode());
            }
        }
        return super.save(entity);
    }

    /**
     * 根据名称获取编码类型
     * 
     * @param name 编码类型名称
     * @return CodeType
     */
    public CodeType getCodeTypeByName(String name) {
        return getDao().getByName(name);
    }

    /**
     * 根据编码获取编码类型
     * 
     * @param code 编码类型编码
     * @return CodeType
     */
    public CodeType getCodeTypeByCode(String code) {
        return getDao().getByCode(code);
    }

    /*
     * (non-Javadoc)
     * @see com.ces.config.dhtmlx.servie.base.ConfigDefineDaoService#delete(java.lang.String)
     */
    @Override
    @Transactional
    public void delete(String ids) {
        Assert.notNull(ids, "要删除的实体ID不能为空");
        String[] idArr = ids.split(",");
        for (int i = 0; i < idArr.length; i++) {
            CodeType codeType = getDao().findOne(idArr[i]);
            CodeUtil.getInstance().removeCodeType(codeType.getCode());
            getDaoFromContext(CodeDao.class).deleteByCodeTypeCode(codeType.getCode());
            getDao().delete(idArr[i]);
            // 更新字段定义中引用到的编码类型
            getService(ColumnDefineService.class).updateByCodeTypeCode(idArr[i]);
        }
    }

    /**
     * 获取编码类型的dhtmlx下拉框选项
     * 
     * @return List<DhtmlxComboOption>
     */
    public List<DhtmlxComboOption> getCodeTypeSelect() {
        List<DhtmlxComboOption> optionList = Lists.newArrayList();
        Map<String, String> codeTypeMap = CodeUtil.getInstance().getCodeTypeMap();
        Set<String> keys = codeTypeMap.keySet();
        for (Iterator<String> it = keys.iterator(); it.hasNext();) {
            String key = it.next();
            DhtmlxComboOption option = new DhtmlxComboOption();
            option.setValue(key);
            option.setText(codeTypeMap.get(key));
            optionList.add(option);
        }
        return optionList;
    }

    /***
     * 根据系统ID获取参数类型
     * 
     * @param systemId 系统ID
     * @return Object
     */
    public List<CodeType> getCodeTypeListBySystemId(String systemId) {
        String hql = "from CodeType where systemId ";
        if (!"".equals(systemId)) {
            hql += "='" + systemId + "'";
        } else {
            hql += "is null";
        }
        return DatabaseHandlerDao.getInstance().queryEntityForList(hql, CodeType.class);
    }

    /**
     * 获取系统下编码类型的最大显示顺序
     * 
     * @param systemId 系统ID
     * @return Integer
     */
    public Integer getMaxShowOrder(String systemId) {
        return getDao().getMaxShowOrder(systemId);
    }

    /**
     * 获取显示顺序范围内的编码类型
     * 
     * @param start 开始显示顺序
     * @param end 结束显示顺序
     * @param systemId 系统ID
     * @return List<CodeType>
     */
    public List<CodeType> getCodeTypeListByShowOrder(Integer start, Integer end, String systemId) {
        return getDao().getByShowOrderBetweenAndSystemId(start, end, systemId);
    }
}
