package com.ces.config.dhtmlx.action.component;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.struts2.rest.DefaultHttpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.ces.config.dhtmlx.action.base.ConfigDefineServiceDaoController;
import com.ces.config.dhtmlx.dao.component.ComponentAssembleAreaDao;
import com.ces.config.dhtmlx.entity.component.ComponentAssembleArea;
import com.ces.config.dhtmlx.entity.component.ComponentVersion;
import com.ces.config.dhtmlx.service.component.ComponentAssembleAreaService;
import com.ces.config.dhtmlx.service.component.ComponentVersionService;
import com.ces.config.utils.ComponentInfoUtil;
import com.ces.config.utils.StringUtil;
import com.ces.xarch.core.exception.FatalException;

/**
 * 组合构件分类Controller
 * 
 * @author qiujinwei
 * @date 2015-04-08
 */
public class ComponentAssembleAreaController extends
        ConfigDefineServiceDaoController<ComponentAssembleArea, ComponentAssembleAreaService, ComponentAssembleAreaDao> {

    private static final long serialVersionUID = 5448108829911125571L;

    /*
     * (non-Javadoc)
     * @see com.ces.xarch.core.web.struts2.BaseController#initModel()
     */
    @Override
    protected void initModel() {
        setModel(new ComponentAssembleArea());
    }

    /*
     * (非 Javadoc) <p>标题: setService</p> <p>描述: 注入自定义服务层SERVICE</p> @param service
     * @see com.ces.config.dhtmlx.action.base.StringIDDhtmlxConfigController#setService(com.ces.xarch.core.service.
     * StringIDService)
     */
    @Autowired
    @Qualifier("componentAssembleAreaService")
    @Override
    protected void setService(ComponentAssembleAreaService service) {
        super.setService(service);
    }

    /*
     * (non-Javadoc)
     * @see com.ces.config.dhtmlx.action.base.ConfigDefineServiceDaoController#create()
     */
    @Override
    public Object create() throws FatalException {
        Integer maxShowOrder = getService().getMaxShowOrder(model.getParentId());
        int showOrder = 0;
        if (maxShowOrder == null) {
            showOrder = 1;
        } else {
            showOrder = maxShowOrder + 1;
        }
        model.setHasChild(false);
        model.setShowOrder(showOrder);
        model = getService().save(model);
        if (!"-1".endsWith(model.getParentId())) {
            ComponentAssembleArea parent = getService().getByID(model.getParentId());
            if (!parent.getHasChild()) {
                parent.setHasChild(true);
                getService().save(parent);
            }
        }
        return SUCCESS;
    }

    /**
     * 校验字段是否已经存在
     * 
     * @return Object
     */
    public Object validateFields() {
        ComponentAssembleArea componentArea = (ComponentAssembleArea) getModel();
        ComponentAssembleArea temp = getService().getByNameAndParentId(componentArea.getName(), componentArea.getParentId());
        boolean nameExist = false;
        if (null != componentArea.getId() && !"".equals(componentArea.getId())) {
            ComponentAssembleArea oldComponentAssembleArea = getService().getByID(componentArea.getId());
            if (null != temp && null != oldComponentAssembleArea && !temp.getId().equals(oldComponentAssembleArea.getId())) {
                nameExist = true;
            }
        } else {
            if (null != temp) {
                nameExist = true;
            }
        }
        setReturnData("{'nameExist' : " + nameExist + "}");
        return new DefaultHttpHeaders(SUCCESS).disableCaching();
    }

    /*
     * (non-Javadoc)
     * @see com.ces.xarch.core.web.struts2.BaseController#destroy()
     */
    @Override
    public Object destroy() throws FatalException {
        List<ComponentAssembleArea> componentAreaList = getService().getComponentAssembleAreaListByParentId(model.getId());
        List<ComponentVersion> componentVersionList = getService(ComponentVersionService.class).getComponentVersionListByAssembleAreaId(model.getId());
        boolean flag = false;
        String message = null;
        if (CollectionUtils.isNotEmpty(componentAreaList)) {
            message = "该构件分类下存在子分类，不能删除";
        } else if (CollectionUtils.isNotEmpty(componentVersionList)) {
            message = "该构件分类下存在构件，不能删除";
        } else {
            ComponentAssembleArea componentArea = getService().getByID(model.getId());
            getService().delete(componentArea);
            ComponentInfoUtil.getInstance().removeComponentAssembleArea(model.getId());
            List<ComponentAssembleArea> childList = getService().getComponentAssembleAreaListByParentId(componentArea.getParentId());
            if (CollectionUtils.isEmpty(childList)) {
                ComponentAssembleArea parent = getService().getByID(componentArea.getParentId());
                parent.setHasChild(false);
                getService().save(parent);
                ComponentInfoUtil.getInstance().putComponentAssembleArea(parent);
            }
            flag = true;
        }
        setReturnData("{'success':" + flag + ", 'message':'" + message + "'}");
        return new DefaultHttpHeaders(SUCCESS).disableCaching();
    }

    /**
     * 树排序
     * 
     * @return Object
     */
    public Object sort() {
        String start = getParameter("start");
        String end = getParameter("end");
        String targetId = getParameter("targetId");
        ComponentAssembleArea startComponentAssembleArea = getService().getByID(start);
        if (startComponentAssembleArea.getParentId().equals(targetId)) {
            // 同个父分类中拖动排序
            if (StringUtil.isNotEmpty(end)) {
                ComponentAssembleArea endComponentAssembleArea = getService().getByID(end);
                if (startComponentAssembleArea.getShowOrder().intValue() > endComponentAssembleArea.getShowOrder().intValue()) {
                    // 向上
                    List<ComponentAssembleArea> componentAreaList = getService().getComponentAssembleAreaListByShowOrder(
                            endComponentAssembleArea.getShowOrder(), startComponentAssembleArea.getShowOrder(), startComponentAssembleArea.getParentId());
                    startComponentAssembleArea.setShowOrder(endComponentAssembleArea.getShowOrder());
                    getService().save(startComponentAssembleArea);
                    for (ComponentAssembleArea componentArea : componentAreaList) {
                        if (componentArea.getId().equals(startComponentAssembleArea.getId())) {
                            continue;
                        }
                        componentArea.setShowOrder(componentArea.getShowOrder() + 1);
                        getService().save(componentArea);
                    }
                } else {
                    // 向下
                    List<ComponentAssembleArea> componentAreaList = getService().getComponentAssembleAreaListByShowOrder(
                            startComponentAssembleArea.getShowOrder(), endComponentAssembleArea.getShowOrder(), startComponentAssembleArea.getParentId());
                    startComponentAssembleArea.setShowOrder(endComponentAssembleArea.getShowOrder() - 1);
                    getService().save(startComponentAssembleArea);
                    for (ComponentAssembleArea componentArea : componentAreaList) {
                        if (componentArea.getId().equals(startComponentAssembleArea.getId()) || componentArea.getId().equals(endComponentAssembleArea.getId())) {
                            continue;
                        }
                        componentArea.setShowOrder(componentArea.getShowOrder() - 1);
                        getService().save(componentArea);
                    }
                }
            }
            setReturnData("排序成功!");
        } else {
            String oldParentAreaId = startComponentAssembleArea.getParentId();
            // 拖动到不同的父分类中
            startComponentAssembleArea.setParentId(targetId);
            if (StringUtil.isNotEmpty(end)) {
                ComponentAssembleArea endComponentAssembleArea = getService().getByID(end);
                startComponentAssembleArea.setShowOrder(endComponentAssembleArea.getShowOrder());
                getService().save(startComponentAssembleArea);
                getService().updateShowOrderPlusOne(endComponentAssembleArea.getShowOrder(), targetId);
            } else {
                Integer maxShowOrder = getService().getMaxShowOrder(targetId);
                int showOrder = 0;
                if (maxShowOrder == null) {
                    showOrder = 1;
                } else {
                    showOrder = maxShowOrder + 1;
                }
                startComponentAssembleArea.setShowOrder(showOrder);
                getService().save(startComponentAssembleArea);
                if (!"-1".endsWith(targetId)) {
                    ComponentAssembleArea parent = getService().getByID(targetId);
                    if (!parent.getHasChild()) {
                        parent.setHasChild(true);
                        getService().save(parent);
                    }
                }
            }
            // 查询拖动分类原来的父分类中是否还有子分类，如果没有，将该分类hasChild设置成false
            List<ComponentAssembleArea> oldChildAreaList = getService().getComponentAssembleAreaListByParentId(oldParentAreaId);
            if (CollectionUtils.isEmpty(oldChildAreaList)) {
                ComponentAssembleArea oldParentComponentAssembleArea = getService().getByID(oldParentAreaId);
                oldParentComponentAssembleArea.setHasChild(false);
                getService().save(oldParentComponentAssembleArea);
            }
            setReturnData("改变父构件分类成功!");
        }
        ComponentInfoUtil.getInstance().putComponentAssembleAreas(getService().findAll());
        return new DefaultHttpHeaders(SUCCESS).disableCaching();
    }

    /**
     * qiucs 2014-2-20
     * <p>
     * 描述: 构件分类下拉框
     * </p>
     */
    public Object combo() {
        try {
            setReturnData(getService().comboOfComponentAssembleArea());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return NONE;
    }

    /**
     * 获取构件分类树
     * 
     * @return Object
     */
    public Object getAreaTree() {
        String jsonStr = "{id:'0', item:[{id:'-1', text:'构件库', open:'1'" + getChildAreaJson("-1") + "}]}";
        setReturnData(jsonStr);
        return new DefaultHttpHeaders(SUCCESS).disableCaching();
    }

    /**
     * 获取构件分类json
     * 
     * @param parentAreaId 父构件分类ID
     * @return String
     */
    private String getChildAreaJson(String parentAreaId) {
        List<ComponentAssembleArea> areaList = getService(ComponentAssembleAreaService.class).getComponentAssembleAreaListByParentId(parentAreaId);
        String jsonStr = "";
        if (CollectionUtils.isNotEmpty(areaList)) {
            jsonStr = ", item:[";
            for (ComponentAssembleArea area : areaList) {
                jsonStr += "{id:'" + area.getId() + "', text:'" + area.getName() + "', open:'1'" + getChildAreaJson(area.getId());
                jsonStr += "},";
            }
            jsonStr = jsonStr.substring(0, jsonStr.length() - 1);
            jsonStr += "]";
        }
        return jsonStr;
    }
}
