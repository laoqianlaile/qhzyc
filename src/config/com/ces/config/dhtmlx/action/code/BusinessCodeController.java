package com.ces.config.dhtmlx.action.code;

import org.apache.struts2.rest.DefaultHttpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.ces.config.dhtmlx.action.base.ConfigDefineServiceDaoController;
import com.ces.config.dhtmlx.dao.code.BusinessCodeDao;
import com.ces.config.dhtmlx.dao.common.DatabaseHandlerDao;
import com.ces.config.dhtmlx.entity.code.BusinessCode;
import com.ces.config.dhtmlx.service.code.BusinessCodeService;
import com.ces.config.utils.AuthDatabaseUtil;
import com.ces.config.utils.StringUtil;

/**
 * 业务表编码Controller
 * 
 * @author wanglei
 * @date 2014-09-17
 */
public class BusinessCodeController extends ConfigDefineServiceDaoController<BusinessCode, BusinessCodeService, BusinessCodeDao> {

    private static final long serialVersionUID = -5968930966418061950L;

    /*
     * (non-Javadoc)
     * @see com.ces.xarch.core.web.struts2.BaseController#initModel()
     */
    @Override
    protected void initModel() {
        setModel(new BusinessCode());
    }

    /*
     * (非 Javadoc) <p>标题: setService</p> <p>描述: 注入自定义服务层SERVICE</p> @param service
     * @see com.ces.config.dhtmlx.action.base.StringIDDhtmlxConfigController#setService(com.ces.xarch.core.service.
     * StringIDService)
     */
    @Autowired
    @Qualifier("businessCodeService")
    @Override
    protected void setService(BusinessCodeService service) {
        super.setService(service);
    }

    /**
     * 根据编码类型编码获取相关的业务表编码
     * 
     * @return BusinessCode
     */
    public Object getByCodeTypeCode() {
        String codeTypeCode = getParameter("codeTypeCode");
        BusinessCode businessCode = getService().getByCodeTypeCode(codeTypeCode);
        if (businessCode == null) {
            businessCode = new BusinessCode();
            businessCode.setCodeTypeCode(codeTypeCode);
        }
        setReturnData(businessCode);
        return new DefaultHttpHeaders(SUCCESS).disableCaching();
    }

    /**
     * 校验字段是否已经存在
     * 
     * @return Object
     */
    public Object validateFields() {
        BusinessCode businessCode = (BusinessCode) getModel();
        //BusinessCode temp = getService().getByTableName(businessCode.getTableName());
        //boolean tableNameExist = false;
        boolean dbError = false;
        StringBuilder sb = new StringBuilder();
        /*if (null != businessCode.getId() && !"".equals(businessCode.getId())) {
            BusinessCode oldBusinessCode = getService().getByID(businessCode.getId());
            if (null != temp && null != oldBusinessCode && !temp.getId().equals(oldBusinessCode.getId())) {
                tableNameExist = true;
            }
        } else {
            if (null != temp) {
                tableNameExist = true;
            }
        }*/
        if ("0".equals(businessCode.getIsAuth())) {
            boolean tableExists = DatabaseHandlerDao.getInstance().tableExists(businessCode.getTableName());
            if (tableExists) {
                sb.append("字段");
                boolean columnExists1 = DatabaseHandlerDao.getInstance().columnExists(businessCode.getTableName(), businessCode.getCodeNameField());
                if (!columnExists1) {
                    dbError = true;
                    sb.append("、").append(businessCode.getCodeNameField());
                }
                boolean columnExists2 = DatabaseHandlerDao.getInstance().columnExists(businessCode.getTableName(), businessCode.getCodeValueField());
                if (!columnExists2) {
                    dbError = true;
                    sb.append("、").append(businessCode.getCodeValueField());
                }
                if (StringUtil.isNotEmpty(businessCode.getShowOrderField())) {
                    boolean columnExists3 = DatabaseHandlerDao.getInstance().columnExists(businessCode.getTableName(), businessCode.getShowOrderField());
                    if (!columnExists3) {
                        dbError = true;
                        sb.append("、").append(businessCode.getShowOrderField());
                    }
                }
                if (StringUtil.isNotEmpty(businessCode.getIdField())) {
                    boolean columnExists3 = DatabaseHandlerDao.getInstance().columnExists(businessCode.getTableName(), businessCode.getIdField());
                    if (!columnExists3) {
                        dbError = true;
                        sb.append("、").append(businessCode.getIdField());
                    }
                }
                if (StringUtil.isNotEmpty(businessCode.getParentIdField())) {
                    boolean columnExists3 = DatabaseHandlerDao.getInstance().columnExists(businessCode.getTableName(), businessCode.getParentIdField());
                    if (!columnExists3) {
                        dbError = true;
                        sb.append("、").append(businessCode.getParentIdField());
                    }
                }
                sb.append("不存在！");
                if (sb.indexOf("、") != -1) {
                    sb.replace(2, 3, "");
                }
            } else {
                sb.append("数据库中该表不存在！");
                dbError = true;
            }
        } else {
            boolean tableExists = AuthDatabaseUtil.tableExists(businessCode.getTableName());
            if (tableExists) {
                sb.append("字段");
                boolean columnExists1 = AuthDatabaseUtil.columnExists(businessCode.getTableName(), businessCode.getCodeNameField());
                if (!columnExists1) {
                    dbError = true;
                    sb.append("、").append(businessCode.getCodeNameField());
                }
                boolean columnExists2 = AuthDatabaseUtil.columnExists(businessCode.getTableName(), businessCode.getCodeValueField());
                if (!columnExists2) {
                    dbError = true;
                    sb.append("、").append(businessCode.getCodeValueField());
                }
                if (StringUtil.isNotEmpty(businessCode.getShowOrderField())) {
                    boolean columnExists3 = AuthDatabaseUtil.columnExists(businessCode.getTableName(), businessCode.getShowOrderField());
                    if (!columnExists3) {
                        dbError = true;
                        sb.append("、").append(businessCode.getShowOrderField());
                    }
                }
                if (StringUtil.isNotEmpty(businessCode.getIdField())) {
                    boolean columnExists3 = AuthDatabaseUtil.columnExists(businessCode.getTableName(), businessCode.getIdField());
                    if (!columnExists3) {
                        dbError = true;
                        sb.append("、").append(businessCode.getIdField());
                    }
                }
                if (StringUtil.isNotEmpty(businessCode.getParentIdField())) {
                    boolean columnExists3 = AuthDatabaseUtil.columnExists(businessCode.getTableName(), businessCode.getParentIdField());
                    if (!columnExists3) {
                        dbError = true;
                        sb.append("、").append(businessCode.getParentIdField());
                    }
                }
                sb.append("不存在！");
                if (sb.indexOf("、") != -1) {
                    sb.replace(2, 3, "");
                }
            } else {
                sb.append("数据库中该表不存在！");
                dbError = true;
            }
        }
        //setReturnData("{'tableNameExist' : " + tableNameExist + ", 'dbError' : " + dbError + ", 'dbErrorMsg' : '" + sb.toString() + "'" + "}");
        setReturnData("{'dbError' : " + dbError + ", 'dbErrorMsg' : '" + sb.toString() + "'" + "}");
        return new DefaultHttpHeaders(SUCCESS).disableCaching();
    }

    /**
     * 根据编码类型编码获取相关的业务表编码
     * 
     * @return BusinessCode
     */
    public Object syncBusinessCode() {
        String codeTypeCode = getParameter("codeTypeCode");
        boolean success = false;
        try {
            getService().syncBusinessCode(codeTypeCode);
            success = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        setReturnData("{'success' : " + success + "}");
        return new DefaultHttpHeaders(SUCCESS).disableCaching();
    }
}
