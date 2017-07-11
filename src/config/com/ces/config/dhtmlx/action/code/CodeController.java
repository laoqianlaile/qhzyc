package com.ces.config.dhtmlx.action.code;

import java.util.List;

import org.apache.struts2.rest.DefaultHttpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.ces.config.dhtmlx.action.base.ConfigDefineServiceDaoController;
import com.ces.config.dhtmlx.dao.code.CodeDao;
import com.ces.config.dhtmlx.entity.code.Code;
import com.ces.config.dhtmlx.entity.code.CodeType;
import com.ces.config.dhtmlx.service.code.CodeService;
import com.ces.config.dhtmlx.service.code.CodeTypeService;
import com.ces.config.utils.CodeUtil;
import com.ces.config.utils.StringUtil;
import com.ces.xarch.core.exception.FatalException;

/**
 * 编码Controller
 * 
 * @author wanglei
 * @date 2013-07-15
 */
public class CodeController extends ConfigDefineServiceDaoController<Code, CodeService, CodeDao> {

    private static final long serialVersionUID = -8037122071030457027L;

    /*
     * (non-Javadoc)
     * @see com.ces.xarch.core.web.struts2.BaseController#initModel()
     */
    @Override
    protected void initModel() {
        setModel(new Code());
    }

    /*
     * (非 Javadoc) <p>标题: setService</p> <p>描述: 注入自定义服务层SERVICE</p> @param service
     * @see com.ces.config.dhtmlx.action.base.StringIDDhtmlxConfigController#setService(com.ces.xarch.core.service.
     * StringIDService)
     */
    @Autowired
    @Qualifier("codeService")
    @Override
    protected void setService(CodeService service) {
        super.setService(service);
    }

    /*
     * (non-Javadoc)
     * @see com.ces.config.dhtmlx.action.base.ConfigDefineServiceDaoController#create()
     */
    @Override
    public Object create() throws FatalException {
        Integer maxShowOrder = getService().getMaxShowOrder(model.getCodeTypeCode());
        int showOrder = 0;
        if (maxShowOrder == null) {
            showOrder = 1;
        } else {
            showOrder = maxShowOrder + 1;
        }
        if (StringUtil.isEmpty(model.getParentId())) {
            model.setParentId(null);
        }
        model.setShowOrder(showOrder);
        model = getService().save(model);
        CodeType codeType = getService(CodeTypeService.class).getCodeTypeByCode(model.getCodeTypeCode());
        CodeUtil.getInstance().putCode(codeType.getCode(), model);
        CodeUtil.getInstance().removeCodeTree(codeType.getCode());
        return SUCCESS;
    }

    /*
     * (non-Javadoc)
     * @see com.ces.xarch.core.web.struts2.BaseController#update()
     */
    @Override
    public Object update() throws FatalException {
        CodeType codeType = getService(CodeTypeService.class).getCodeTypeByCode(model.getCodeTypeCode());
        Code oldCode = getService().getByID(model.getId());
        CodeUtil.getInstance().removeCode(codeType.getCode(), oldCode);
        if (StringUtil.isEmpty(model.getParentId())) {
            model.setParentId(null);
        }
        getService().save(model);
        CodeUtil.getInstance().putCode(codeType.getCode(), model);
        CodeUtil.getInstance().removeCodeTree(codeType.getCode());
        return SUCCESS;
    }

    /*
     * (non-Javadoc)
     * @see com.ces.xarch.core.web.struts2.BaseController#destroy()
     */
    @Override
    public Object destroy() throws FatalException {
        String[] idArr = getId().split(",");
        Code code = null;
        CodeType codeType = null;
        for (int i = 0; i < idArr.length; i++) {
            code = getService().getByID(idArr[i]);
            codeType = getService(CodeTypeService.class).getCodeTypeByCode(code.getCodeTypeCode());
            CodeUtil.getInstance().removeCode(codeType.getCode(), code);
            CodeUtil.getInstance().removeCodeTree(codeType.getCode());
        }
        getService().delete(getId());
        return SUCCESS;
    }

    /**
     * 获取编码
     * 
     * @return Object
     */
    public Object getCodeList() {
        list = getDataModel(getModelTemplate());
        processFilter(list);
        String codeTypeCode = getParameter("codeTypeCode");
        list.setData(CodeUtil.getInstance().getCodeList(codeTypeCode));
        return new DefaultHttpHeaders("list").disableCaching();
    }

    /**
     * 校验字段是否已经存在
     * 
     * @return Object
     */
    public Object validateFields() {
        Code code = (Code) getModel();
        Code temp1 = getService().getCodeByName(code.getName(), code.getCodeTypeCode());
        Code temp2 = getService().getCodeByValue(code.getValue(), code.getCodeTypeCode());
        boolean nameExist = false;
        boolean valueExist = false;
        if (null != code.getId() && !"".equals(code.getId())) {
            Code oldCode = getService().getByID(code.getId());
            if (null != temp1 && null != oldCode && !temp1.getId().equals(oldCode.getId())) {
                nameExist = true;
            }
            if (null != temp2 && null != oldCode && !temp2.getId().equals(oldCode.getId())) {
                valueExist = true;
            }
        } else {
            if (null != temp1) {
                nameExist = true;
            }
            if (null != temp2) {
                valueExist = true;
            }
        }
        setReturnData("{'nameExist' : " + nameExist + ", 'valueExist' : " + valueExist + "}");
        return new DefaultHttpHeaders(SUCCESS).disableCaching();
    }

    /**
     * 排序
     * 
     * @return Object
     */
    public Object sort() {
        String start = getParameter("start");
        String end = getParameter("end");
        Code startCode = getService().getByID(start);
        Code endCode = getService().getByID(end);
        if (startCode.getShowOrder().intValue() > endCode.getShowOrder().intValue()) {
            // 向上
            List<Code> codeList = getService().getCodeListByShowOrder(endCode.getShowOrder(), startCode.getShowOrder(), startCode.getCodeTypeCode());
            startCode.setShowOrder(endCode.getShowOrder());
            getService().save(startCode);
            for (Code code : codeList) {
                if (code.getId().equals(startCode.getId())) {
                    continue;
                }
                code.setShowOrder(code.getShowOrder() + 1);
                getService().save(code);
            }
        } else {
            // 向下
            List<Code> codeList = getService().getCodeListByShowOrder(startCode.getShowOrder(), endCode.getShowOrder(), startCode.getCodeTypeCode());
            startCode.setShowOrder(endCode.getShowOrder());
            getService().save(startCode);
            for (Code code : codeList) {
                if (code.getId().equals(startCode.getId())) {
                    continue;
                }
                code.setShowOrder(code.getShowOrder() - 1);
                getService().save(code);
            }
        }
        List<Code> codeList = getService().getCodeList(startCode.getCodeTypeCode());
        CodeType codeType = getService(CodeTypeService.class).getByID(startCode.getCodeTypeCode());
        CodeUtil.getInstance().putCodeList(codeType.getCode(), codeList);
        CodeUtil.getInstance().removeCodeTree(codeType.getCode());
        setReturnData("排序成功");
        return new DefaultHttpHeaders(SUCCESS).disableCaching();
    }

    public Object combobox() {
        String codeTypeCode = getParameter("codeTypeCode");
        setReturnData(getService().getCombobox(codeTypeCode));
        return NONE;
    }
}
