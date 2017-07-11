package com.ces.config.dhtmlx.action.code;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.collections.CollectionUtils;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.rest.DefaultHttpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.jpa.domain.Specification;

import com.ces.config.dhtmlx.action.base.ConfigDefineServiceDaoController;
import com.ces.config.dhtmlx.dao.code.CodeTypeDao;
import com.ces.config.dhtmlx.entity.code.Code;
import com.ces.config.dhtmlx.entity.code.CodeType;
import com.ces.config.dhtmlx.entity.menu.Menu;
import com.ces.config.dhtmlx.json.entity.common.DhtmlxTreeNode;
import com.ces.config.dhtmlx.service.code.CodeService;
import com.ces.config.dhtmlx.service.code.CodeTypeService;
import com.ces.config.dhtmlx.service.menu.MenuService;
import com.ces.config.utils.CodeUtil;
import com.ces.config.utils.ConstantVar;
import com.ces.xarch.core.exception.FatalException;
import com.ces.xarch.core.utils.SearchFilter;
import com.ces.xarch.core.utils.SearchHelper;
import com.ces.xarch.core.utils.ServletUtils;
import com.google.common.collect.Lists;

/**
 * 编码类型Controller
 * 
 * @author wanglei
 * @date 2013-07-15
 */
public class CodeTypeController extends ConfigDefineServiceDaoController<CodeType, CodeTypeService, CodeTypeDao> {

    private static final long serialVersionUID = -8339780449025617169L;

    /*
     * (non-Javadoc)
     * @see com.ces.xarch.core.web.struts2.BaseController#initModel()
     */
    @Override
    protected void initModel() {
        setModel(new CodeType());
    }

    /*
     * (非 Javadoc) <p>标题: setService</p> <p>描述: 注入自定义服务层SERVICE</p> @param service
     * @see com.ces.config.dhtmlx.action.base.StringIDDhtmlxConfigController#setService(com.ces.xarch.core.service.
     * StringIDService)
     */
    @Autowired
    @Qualifier("codeTypeService")
    @Override
    protected void setService(CodeTypeService service) {
        super.setService(service);
    }

    /*
     * (非 Javadoc)
     * @see com.ces.config.dhtmlx.action.base.ConfigDefineServiceDaoController#create()
     */
    @Override
    public Object create() throws FatalException {
        Integer maxShowOrder = getService().getMaxShowOrder(model.getSystemId());
        int showOrder = 0;
        if (maxShowOrder == null) {
            showOrder = 1;
        } else {
            showOrder = maxShowOrder + 1;
        }
        model.setShowOrder(showOrder);
        model = getService().save(model);
        CodeUtil.getInstance().putCodeType(model.getCode(), model.getName());
        return SUCCESS;
    }

    /*
     * (非 Javadoc)
     * @see com.ces.xarch.core.web.struts2.BaseController#update()
     */
    @Override
    public Object update() throws FatalException {
        CodeType codeType = getService().getByID(model.getId());
        getService().save(model);
        if (!codeType.getName().equals(model.getName())) {
            CodeUtil.getInstance().putCodeType(model.getCode(), model.getName());
        }
        return SUCCESS;
    }

    /*
     * (non-Javadoc)
     * 编码类型树
     * @see com.ces.config.dhtmlx.action.base.ConfigDefineServiceDaoController#tree()
     */
    public Object tree() {
        String view = getParameter("view");
        list = getDataModel(getModelTemplate());
        processFilter(list);
        String treeId = getId();
        List<DhtmlxTreeNode> treeNodelist = Lists.newArrayList();
        DhtmlxTreeNode treeNode = null;
        if ("-1".equals(treeId)) {

            treeNode = new DhtmlxTreeNode();
            treeNode.setId("COMMON_" + treeId);
            treeNode.setText("公用编码");
            treeNode.setIm0(ConstantVar.IconTreeNode.FOLDER_CLOSE);
            treeNode.setIm1(ConstantVar.IconTreeNode.FOLDER_OPEN);
            treeNode.setIm2(ConstantVar.IconTreeNode.FOLDER_CLOSE);
            treeNode.setChild("1");
            // codeTypeCode
            treeNode.setProp0(null);
            // codeParentId
            treeNode.setProp1(null);
            // 是否是业务表编码
            treeNode.setProp2("0");
            // systemId系统ID
            treeNode.setProp3(null);
            treeNodelist.add(treeNode);
            List<Menu> menuList = getService(MenuService.class).getMenuByParentId("-1");
            for (Iterator<Menu> iterator = menuList.iterator(); iterator.hasNext();) {
                Menu menu = iterator.next();
                if (menu.getId().equals("sys_0")) {
                    iterator.remove();
                    break;
                }
            }
            if (CollectionUtils.isNotEmpty(menuList)) {
                for (Menu m : menuList) {
                    treeNode = new DhtmlxTreeNode();
                    treeNode.setId("SYSTEM_" + m.getId());
                    treeNode.setText(m.getName());
                    treeNode.setIm0(ConstantVar.IconTreeNode.LEAF);
                    treeNode.setIm1(ConstantVar.IconTreeNode.FOLDER_OPEN);
                    treeNode.setIm2(ConstantVar.IconTreeNode.FOLDER_CLOSE);
                    treeNode.setChild("1");
                    treeNode.setOpen(false);
                    // systemId系统ID
                    treeNode.setProp3(m.getId());
                    treeNodelist.add(treeNode);
                }
            }
        } else {
            List<Code> codeList = null;
            if (treeId.startsWith("COMMON_")) {
                List<CodeType> codeTypeList = getService().getCodeTypeListBySystemId("");
                if (CollectionUtils.isNotEmpty(codeTypeList)) {
                    for (CodeType codeType : codeTypeList) {
                        treeNode = new DhtmlxTreeNode();
                        treeNode.setId("CT_" + codeType.getCode());
                        treeNode.setText(codeType.getName());
                        treeNode.setIm0(ConstantVar.IconTreeNode.FOLDER_CLOSE);
                        treeNode.setIm1(ConstantVar.IconTreeNode.FOLDER_OPEN);
                        treeNode.setIm2(ConstantVar.IconTreeNode.FOLDER_CLOSE);
                        treeNode.setChild("1");
                        // codeTypeCode
                        treeNode.setProp0(codeType.getCode());
                        // codeParentId
                        treeNode.setProp1(null);
                        // 是否是业务表编码
                        treeNode.setProp2(codeType.getIsBusiness());
                        // systemId系统ID
                        treeNode.setProp3(null);
                        treeNodelist.add(treeNode);
                    }
                }
            }
            if (treeId.startsWith("SYSTEM_")) {
                List<CodeType> codeTypeList = getService().getCodeTypeListBySystemId(treeId.replaceFirst("SYSTEM_", ""));
                if (CollectionUtils.isNotEmpty(codeTypeList)) {
                    for (CodeType codeType : codeTypeList) {
                        treeNode = new DhtmlxTreeNode();
                        treeNode.setId("CT_" + codeType.getCode());
                        treeNode.setText(codeType.getName());
                        treeNode.setIm0(ConstantVar.IconTreeNode.FOLDER_CLOSE);
                        treeNode.setIm1(ConstantVar.IconTreeNode.FOLDER_OPEN);
                        treeNode.setIm2(ConstantVar.IconTreeNode.FOLDER_CLOSE);
                        treeNode.setChild("1");
                        // codeTypeCode
                        treeNode.setProp0(codeType.getCode());
                        // codeParentId
                        treeNode.setProp1(null);
                        // 是否是业务表编码
                        treeNode.setProp2(codeType.getIsBusiness());
                        // systemId系统ID
                        treeNode.setProp3(null);
                        treeNodelist.add(treeNode);
                    }
                }
            }
            if (treeId.startsWith("CT_")) {
                treeId = treeId.replaceFirst("CT_", "");
                codeList = getService(CodeService.class).getByCodeTypeCodeAndParentIdIsNUll(treeId);
            } else if (treeId.startsWith("C_")) {
                treeId = treeId.replaceFirst("C_", "");
                codeList = getService(CodeService.class).getByParentId(treeId);
            }
            if (CollectionUtils.isNotEmpty(codeList)) {
                for (Code code : codeList) {
                    treeNode = new DhtmlxTreeNode();
                    treeNode.setId("C_" + code.getId());
                    treeNode.setText(code.getName());
                    treeNode.setIm0(ConstantVar.IconTreeNode.FOLDER_CLOSE);
                    treeNode.setIm1(ConstantVar.IconTreeNode.FOLDER_OPEN);
                    treeNode.setIm2(ConstantVar.IconTreeNode.FOLDER_CLOSE);
                    treeNode.setChild("1");
                    // codeTypeCode
                    treeNode.setProp0(code.getCodeTypeCode());
                    // codeParentId
                    treeNode.setProp1(code.getId());
                    treeNodelist.add(treeNode);
                }
            }
        }
        list.setData(treeNodelist);
        if (!"dhtmlx".equals(view)) {
            setReturnData(list.getData());
        }
        return new DefaultHttpHeaders("list").disableCaching();
    }

    public Object getCodeTypeDataTree() {
        String view = getParameter("view");
        list = getDataModel(getModelTemplate());
        processFilter(list);
        String treeId = getId();
        List<DhtmlxTreeNode> treeNodelist = Lists.newArrayList();
        DhtmlxTreeNode treeNode = null;
        if ("-1".equals(treeId)) {
            List<CodeType> codeTypeList = getService().findAll();
            if (CollectionUtils.isNotEmpty(codeTypeList)) {
                for (CodeType codeType : codeTypeList) {
                    treeNode = new DhtmlxTreeNode();
                    treeNode.setId(codeType.getCode());
                    treeNode.setText(codeType.getName());
                    treeNode.setIm0(ConstantVar.IconTreeNode.FOLDER_CLOSE);
                    treeNode.setIm1(ConstantVar.IconTreeNode.FOLDER_OPEN);
                    treeNode.setIm2(ConstantVar.IconTreeNode.FOLDER_CLOSE);
                    treeNode.setChild("0");
                    // codeTypeCode
                    treeNode.setProp0(codeType.getCode());
                    // codeParentId
                    treeNode.setProp1(null);
                    // 是否是业务表编码
                    treeNode.setProp2(codeType.getIsBusiness());
                    treeNodelist.add(treeNode);
                }
            }
        }
        list.setData(treeNodelist);
        if (!"dhtmlx".equals(view)) {
            setReturnData(list.getData());
        }
        return new DefaultHttpHeaders(SUCCESS).disableCaching();
    }

    /*
     * (non-Javadoc)
     * @see com.ces.config.dhtmlx.action.base.ConfigDefineServiceDaoController#buildSpecification()
     */
    @Override
    public Specification<CodeType> buildSpecification() throws FatalException {
        if (DATA_MODEL_TREE.equals(getModelTemplate())) {
            HttpServletRequest request = ServletActionContext.getRequest();
            Map<String, SearchFilter> filterMap = SearchFilter.parse(ServletUtils.getParametersStartingWith(request, SearchFilter.searchPre));
            return SearchHelper.buildSpecification(filterMap.values(), getModelClass());
        }

        return super.buildSpecification();
    }

    /**
     * 校验字段是否已经存在
     * 
     * @return Object
     */
    public Object validateFields() {
        CodeType codeType = (CodeType) getModel();
        CodeType temp = getService().getCodeTypeByName(codeType.getName());
        CodeType temp1 = getService().getCodeTypeByCode(codeType.getCode());
        boolean nameExist = false;
        boolean codeExist = false;
        boolean cannotUpdateIsBusiness = false;
        if (null != codeType.getId() && !"".equals(codeType.getId())) {
            CodeType oldCodeType = getService().getByID(codeType.getId());
            if (null != temp && null != oldCodeType && !temp.getId().equals(oldCodeType.getId())) {
                nameExist = true;
            }
            if (null != temp1 && null != oldCodeType && !temp1.getId().equals(oldCodeType.getId())) {
                codeExist = true;
            }
            if ("1".equals(codeType.getIsBusiness())) {
                List<Code> codeList = getService(CodeService.class).getCodeList(oldCodeType.getCode());
                if (CollectionUtils.isNotEmpty(codeList)) {
                    cannotUpdateIsBusiness = true;
                }
            }
        } else {
            if (null != temp) {
                nameExist = true;
            }
            if (null != temp1) {
                codeExist = true;
            }
        }
        setReturnData("{'nameExist' : " + nameExist + ", 'codeExist' : " + codeExist + ", 'cannotUpdateIsBusiness' : " + cannotUpdateIsBusiness + "}");
        return new DefaultHttpHeaders(SUCCESS).disableCaching();
    }

    /**
     * 获取编码类型的dhtmlx下拉框选项
     * 
     * @return Object
     */
    public Object getCodeTypeSelect() {
        setReturnData(getService().getCodeTypeSelect());
        return null;
    }

    /**
     * 获取编码类型
     * 
     * @return Object
     */
    public Object getCodeType() {
        String codeTypeCode = getParameter("codeTypeCode");
        CodeType codeType = getService().getCodeTypeByCode(codeTypeCode);
        setReturnData(codeType);
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
        CodeType startCodeType = getService().getByID(start);
        CodeType endCodeType = getService().getByID(end);
        if (startCodeType.getShowOrder().intValue() > endCodeType.getShowOrder().intValue()) {
            // 向上
            List<CodeType> codeTypeList = getService().getCodeTypeListByShowOrder(endCodeType.getShowOrder(), startCodeType.getShowOrder(),
                    startCodeType.getSystemId());
            startCodeType.setShowOrder(endCodeType.getShowOrder());
            getService().save(startCodeType);
            for (CodeType codeType : codeTypeList) {
                if (codeType.getId().equals(startCodeType.getId())) {
                    continue;
                }
                codeType.setShowOrder(codeType.getShowOrder() + 1);
                getService().save(codeType);
            }
        } else {
            // 向下
            List<CodeType> codeTypeList = getService().getCodeTypeListByShowOrder(startCodeType.getShowOrder(), endCodeType.getShowOrder(),
                    startCodeType.getSystemId());
            startCodeType.setShowOrder(endCodeType.getShowOrder());
            getService().save(startCodeType);
            for (CodeType codeType : codeTypeList) {
                if (codeType.getId().equals(startCodeType.getId())) {
                    continue;
                }
                codeType.setShowOrder(codeType.getShowOrder() - 1);
                getService().save(codeType);
            }
        }
        setReturnData("排序成功");
        return new DefaultHttpHeaders(SUCCESS).disableCaching();
    }
}
