package com.ces.config.dhtmlx.service.code;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.ces.config.application.CodeApplication;
import com.ces.config.dhtmlx.dao.code.BusinessCodeDao;
import com.ces.config.dhtmlx.dao.common.DatabaseHandlerDao;
import com.ces.config.dhtmlx.entity.code.BusinessCode;
import com.ces.config.dhtmlx.entity.code.Code;
import com.ces.config.dhtmlx.service.base.ConfigDefineDaoService;
import com.ces.config.utils.AuthDatabaseUtil;
import com.ces.config.utils.BusinessCodeTimeManager;
import com.ces.config.utils.CodeApplicationUtil;
import com.ces.config.utils.CodeUtil;
import com.ces.config.utils.EhcacheUtil;
import com.ces.config.utils.StringUtil;
import com.ces.config.utils.authority.AuthorityUtil;

/**
 * 业务表编码Service
 * 
 * @author wanglei
 * @date 2014-09-17
 */
@Component("businessCodeService")
public class BusinessCodeService extends ConfigDefineDaoService<BusinessCode, BusinessCodeDao> {

    /*
     * (non-Javadoc)
     * @see com.ces.xarch.core.service.StringIDService#setDaoUnBinding(com.ces.xarch.core.persistence.jpa.StringIDDao)
     */
    @Autowired
    @Qualifier("businessCodeDao")
    @Override
    protected void setDaoUnBinding(BusinessCodeDao dao) {
        super.setDaoUnBinding(dao);
    }

    /*
     * (non-Javadoc)
     * @see com.ces.xarch.core.service.AbstractService#save(com.ces.xarch.core.entity.BaseEntity)
     */
    @Override
    @Transactional
    public BusinessCode save(BusinessCode entity) {
        BusinessCode businessCode = getDao().save(entity);
        if ("1".equals(entity.getIsTimingUpdate())) {
            BusinessCodeTimeManager.startSchedule(entity.getCodeTypeCode(), entity.getPeriod());
        } else {
            BusinessCodeTimeManager.stopSchedule(entity.getCodeTypeCode());
        }
        return businessCode;
    }

    /**
     * 根据编码类型编码获取相关的业务表编码
     * 
     * @param codeTypeCode 编码类型编码
     * @return BusinessCode
     */
    public BusinessCode getByCodeTypeCode(String codeTypeCode) {
        return getDao().getByCodeTypeCode(codeTypeCode);
    }

    /**
     * 根据业务表名称获取相关的业务表编码
     * 
     * @param tableName 业务表名称
     * @return BusinessCode
     */
    public BusinessCode getByTableName(String tableName) {
        return getDao().getByTableName(tableName);
    }

    /**
     * 根据编码类型编码删除业务表编码
     * 
     * @param codeTypeCode 编码类型编码
     */
    @Transactional
    public void deleteByCodeTypeCode(String codeTypeCode) {
        getDao().deleteByCodeTypeCode(codeTypeCode);
    }

    /**
     * 同步业务表编码到内存中
     * 
     * @param codeTypeCode 编码类型编码
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    public void syncBusinessCode(String codeTypeCode) {
        BusinessCode businessCode = getByCodeTypeCode(codeTypeCode);
        if (null == businessCode) return;
        if (BusinessCode.TABLE.equals(businessCode.getBusinessCodeType())) {
            if (StringUtil.isEmpty(businessCode.getTableName()) || StringUtil.isEmpty(businessCode.getCodeNameField())
                    || StringUtil.isEmpty(businessCode.getCodeValueField())) {
                return;
            }
            StringBuilder sql = new StringBuilder();
            boolean isSingleField = true;
            if (businessCode.getCodeNameField().equalsIgnoreCase(businessCode.getCodeValueField())) {
                sql.append("select distinct ").append(businessCode.getCodeNameField());
            } else {
                sql.append("select distinct ").append(businessCode.getCodeNameField()).append(", ").append(businessCode.getCodeValueField());
                isSingleField = false;
            }
            if (StringUtil.isNotEmpty(businessCode.getShowOrderField())) {
                sql.append(", ").append(businessCode.getShowOrderField()).append(" s");
                isSingleField = false;
            }
            if (StringUtil.isNotEmpty(businessCode.getIdField())) {
                sql.append(", ").append(businessCode.getIdField());
                isSingleField = false;
            }
            if (StringUtil.isNotEmpty(businessCode.getParentIdField())) {
                sql.append(", ").append(businessCode.getParentIdField());
                isSingleField = false;
            }
            sql.append(" from ").append(businessCode.getTableName());
            if (StringUtil.isNotEmpty(businessCode.getShowOrderField())) {
                sql.append(" order by ").append(businessCode.getShowOrderField());
            }
            List list = null;
            if ("0".equals(businessCode.getIsAuth())) {
                list = DatabaseHandlerDao.getInstance().queryForList(sql.toString());
            } else {
                list = AuthDatabaseUtil.queryForList(sql.toString());
            }
            if (CollectionUtils.isNotEmpty(list)) {
                CodeUtil.getInstance().removeCode(codeTypeCode);
                List<Code> codeList = new ArrayList<Code>();
                Code code = null;
                int i = 0;
                if (isSingleField) {
                    for (Object obj : list) {
                        code = new Code();
                        code.setCodeTypeCode(codeTypeCode);
                        code.setName(String.valueOf(obj));
                        code.setValue(String.valueOf(obj));
                        code.setShowOrder(i++);
                        codeList.add(code);
                    }
                } else {
                    for (Object[] objs : (List<Object[]>) list) {
                        code = new Code();
                        int j = 0;
                        code.setCodeTypeCode(codeTypeCode);
                        code.setName(String.valueOf(objs[j]));
                        if (businessCode.getCodeNameField().equalsIgnoreCase(businessCode.getCodeValueField())) {
                            code.setValue(String.valueOf(objs[j]));
                        } else {
                            code.setValue(String.valueOf(objs[++j]));
                        }
                        j++;
                        if (StringUtil.isNotEmpty(businessCode.getShowOrderField()) && objs.length > j) {
                            code.setShowOrder(Integer.valueOf(StringUtil.null2zero(objs[j++])));
                        } else {
                            code.setShowOrder(i++);
                        }
                        if (StringUtil.isNotEmpty(businessCode.getIdField()) && objs.length > j) {
                            code.setId(String.valueOf(objs[j++]));
                        }
                        if (StringUtil.isNotEmpty(businessCode.getParentIdField()) && objs.length > j) {
                            code.setParentId(String.valueOf(objs[j++]));
                        }
                        codeList.add(code);
                    }
                }
                CodeUtil.getInstance().putCodeList(codeTypeCode, codeList);
                EhcacheUtil.removeAllCache(AuthorityUtil.AUTHORITY_CODE);
            }
        } else {
        	syncJavaCode(businessCode);
        }
    }
    
    /**
     * qiucs 2015-1-22 下午10:34:51
     * <p>描述: 同步JAVA类型业务编码 </p>
     * @return void
     */
    private void syncJavaCode(BusinessCode entity) {
    	CodeApplication application = CodeApplicationUtil.getApplicationInstance(entity);
    	if (null == application) return;
    	CodeUtil.getInstance().putCodeList(entity.getCodeTypeCode(), application.getCodeList(entity.getCodeTypeCode()));
    	CodeUtil.getInstance().putCodeTree(entity.getCodeTypeCode(), application.getCodeTree(entity.getCodeTypeCode()));
    }
}
