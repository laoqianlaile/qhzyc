package com.ces.config.dhtmlx.action.toolmanage;

import org.apache.struts2.rest.DefaultHttpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.ces.config.dhtmlx.action.base.ConfigDefineServiceDaoController;
import com.ces.config.dhtmlx.dao.toolmanage.ToolManageDao;
import com.ces.config.dhtmlx.service.toolmanage.ToolManageService;
import com.ces.xarch.core.entity.StringIDEntity;

/**
 * 工具管理Controller
 * 
 * @author wanglei
 * @date 2015-08-28
 */
public class ToolManageController extends ConfigDefineServiceDaoController<StringIDEntity, ToolManageService, ToolManageDao> {

    private static final long serialVersionUID = 5856174244951863043L;

    /*
     * (non-Javadoc)
     * @see com.ces.xarch.core.web.struts2.BaseController#initModel()
     */
    @Override
    protected void initModel() {
        setModel(new StringIDEntity());
    }

    /*
     * (非 Javadoc) <p>标题: setService</p> <p>描述: 注入自定义服务层SERVICE</p> @param service
     * @see com.ces.config.dhtmlx.action.base.StringIDDhtmlxConfigController#setService(com.ces.xarch.core.service.
     * StringIDService)
     */
    @Autowired
    @Qualifier("toolManageService")
    @Override
    protected void setService(ToolManageService service) {
        super.setService(service);
    }

    /**
     * 将“表单工具条”改成“表单_上工具条”（历史数据更改）
     * 
     * @return Object
     */
    public Object changeFormTbToTopTb() {
        String msg = getService().changeFormTbToTopTb();
        setReturnData("{'msg':'" + msg + "'}");
        return new DefaultHttpHeaders(SUCCESS).disableCaching();
    }

    /**
     * 将“表单工具条”改成“表单_下工具条”（历史数据更改）
     * 
     * @return Object
     */
    public Object changeFormTbToBottomTb() {
        String msg = getService().changeFormTbToBottomTb();
        setReturnData("{'msg':'" + msg + "'}");
        return new DefaultHttpHeaders(SUCCESS).disableCaching();
    }

    /**
     * 生成所有的“表单_上工具条”（历史数据更改）
     * 
     * @return Object
     */
    public Object createTopTb() {
        String msg = getService().createTopTb();
        setReturnData("{'msg':'" + msg + "'}");
        return new DefaultHttpHeaders(SUCCESS).disableCaching();
    }

    /**
     * 生成所有的“表单_下工具条”（历史数据更改）
     * 
     * @return Object
     */
    public Object createBottomTb() {
        String msg = getService().createBottomTb();
        setReturnData("{'msg':'" + msg + "'}");
        return new DefaultHttpHeaders(SUCCESS).disableCaching();
    }

    /**
     * 将所有“表单_上工具条”上的按钮复制到“表单_下工具条”
     * 
     * @return Object
     */
    public Object copyButtonFormTopTb() {
        String msg = getService().copyButtonFormTopTb();
        setReturnData("{'msg':'" + msg + "'}");
        return new DefaultHttpHeaders(SUCCESS).disableCaching();
    }

    /**
     * 将所有“表单_下工具条”上的按钮复制到“表单_上工具条”
     * 
     * @return Object
     */
    public Object copyButtonFormBottomTb() {
        String msg = getService().copyButtonFormBottomTb();
        setReturnData("{'msg':'" + msg + "'}");
        return new DefaultHttpHeaders(SUCCESS).disableCaching();
    }

    /**
     * 删除所有“表单_上工具条”上的按钮
     * 
     * @return Object
     */
    public Object deleteButtonOfTopTb() {
        String msg = getService().deleteButtonOfTopTb();
        setReturnData("{'msg':'" + msg + "'}");
        return new DefaultHttpHeaders(SUCCESS).disableCaching();
    }

    /**
     * 删除所有“表单_下工具条”上的按钮
     * 
     * @return Object
     */
    public Object deleteButtonOfBottomTb() {
        String msg = getService().deleteButtonOfBottomTb();
        setReturnData("{'msg':'" + msg + "'}");
        return new DefaultHttpHeaders(SUCCESS).disableCaching();
    }

    /**
     * 设置新增和修改按钮的表单显示类型 “弹出”或“嵌入”
     * 
     * @return Object
     */
    public Object setFormAssembleType() {
        String assembleType = getParameter("assembleType");
        String msg = getService().setFormAssembleType(assembleType);
        setReturnData("{'msg':'" + msg + "'}");
        return new DefaultHttpHeaders(SUCCESS).disableCaching();
    }
}
