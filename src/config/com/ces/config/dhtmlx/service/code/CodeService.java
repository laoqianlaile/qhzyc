package com.ces.config.dhtmlx.service.code;

import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.ces.config.datamodel.option.OptionModel;
import com.ces.config.dhtmlx.dao.code.CodeDao;
import com.ces.config.dhtmlx.entity.code.Code;
import com.ces.config.dhtmlx.service.base.ConfigDefineDaoService;
import com.ces.config.utils.CodeUtil;
import com.google.common.collect.Lists;

/**
 * 编码Service
 * 
 * @author wanglei
 * @date 2013-07-15
 */
@Component("codeService")
public class CodeService extends ConfigDefineDaoService<Code, CodeDao> {

    /*
     * (non-Javadoc)
     * @see com.ces.xarch.core.service.StringIDService#setDaoUnBinding(com.ces.xarch.core.persistence.jpa.StringIDDao)
     */
    @Autowired
    @Qualifier("codeDao")
    @Override
    protected void setDaoUnBinding(CodeDao dao) {
        super.setDaoUnBinding(dao);
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
            deleteCode(idArr[i]);
        }
    }

    /**
     * 级联删除编码
     * 
     * @param codeId 编码ID
     */
    private void deleteCode(String codeId) {
        List<Code> codeList = getDao().getByParentId(codeId);
        if (CollectionUtils.isNotEmpty(codeList)) {
            for (Code code : codeList) {
                deleteCode(code.getId());
            }
        }
        super.delete(codeId);
    }

    /**
     * 根据编码类型Id和编码父ID获取编码
     * 
     * @param codeTypeCode 编码类型编码
     * @return List<Code>
     */
    public List<Code> getByCodeTypeCodeAndParentIdIsNUll(String codeTypeCode) {
        return getDao().getByCodeTypeCodeAndParentIdIsNull(codeTypeCode);
    }

    /**
     * 根据编码父ID获取编码
     * 
     * @param parentId 编码父ID
     * @return List<Code>
     */
    public List<Code> getByParentId(String parentId) {
        return getDao().getByParentId(parentId);
    }

    /**
     * 根据名称获取编码
     * 
     * @param name 编码名称
     * @param codeTypeCode 编码类型编码
     * @return Code
     */
    public Code getCodeByName(String name, String codeTypeCode) {
        return getDao().getByNameAndCodeTypeCode(name, codeTypeCode);
    }

    /**
     * 根据值获取编码
     * 
     * @param value 编码值
     * @param codeTypeCode 编码类型编码
     * @return Code
     */
    public Code getCodeByValue(String value, String codeTypeCode) {
        return getDao().getByValueAndCodeTypeCode(value, codeTypeCode);
    }

    /**
     * 获取编码类型下编码的最大显示顺序
     * 
     * @param codeTypeCode 编码类型编码
     * @return Integer
     */
    public Integer getMaxShowOrder(String codeTypeCode) {
        return getDao().getMaxShowOrder(codeTypeCode);
    }

    /**
     * 获取显示顺序范围内的编码
     * 
     * @param start 开始显示顺序
     * @param end 结束显示顺序
     * @param codeTypeCode 编码类型编码
     * @return List<Code>
     */
    public List<Code> getCodeListByShowOrder(Integer start, Integer end, String codeTypeCode) {
        return getDao().getByShowOrderBetweenAndCodeTypeCode(start, end, codeTypeCode);
    }

    /**
     * 根据编码类型编码获取编码
     * 
     * @param codeTypeCode 编码类型编码
     * @return List<Code>
     */
    public List<Code> getCodeList(final String codeTypeCode) {
        return getDao().findAll(new Specification<Code>() {
            public Predicate toPredicate(Root<Code> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
                Path<String> codeTypeCodeExp = root.get("codeTypeCode");
                return builder.equal(codeTypeCodeExp, codeTypeCode);
            }
        }, new Sort(Direction.ASC, "showOrder"));
    }

    /**
     * qiucs 2014-9-22
     * <p>描述: 根据编码类型code获取下拉框List</p>
     * 
     * @param codeTypeCode
     * @return Object 返回类型
     * @throws
     */
    public Object getCombobox(String codeTypeCode) {
        List<OptionModel> optionList = Lists.newArrayList();
        List<Code> clist = CodeUtil.getInstance().getCodeList(codeTypeCode);
        if (null == clist)
            return optionList;
        for (Code code : clist) {
            OptionModel option = new OptionModel();
            option.setValue(code.getValue());
            option.setText(code.getName());
            optionList.add(option);
        }
        return optionList;
    }
    
    /**
     * qiucs 2015-8-28 下午2:52:59
     * <p>描述: 获取需要缓存的编码 </p>
     * @return List<Code>
     */
    public List<Code> getUseCacheCodeList() {
    	return getDao().getUseCacheCodeList();
    }
}
