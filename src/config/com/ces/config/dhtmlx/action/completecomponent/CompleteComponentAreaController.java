package com.ces.config.dhtmlx.action.completecomponent;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.struts2.rest.DefaultHttpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.ces.config.dhtmlx.action.base.ConfigDefineServiceDaoController;
import com.ces.config.dhtmlx.dao.completecomponent.CompleteComponentAreaDao;
import com.ces.config.dhtmlx.entity.completecomponent.CompleteComponentArea;
import com.ces.config.dhtmlx.entity.completecomponent.CompleteComponentVersion;
import com.ces.config.dhtmlx.service.completecomponent.CompleteComponentAreaService;
import com.ces.config.dhtmlx.service.completecomponent.CompleteComponentVersionService;
import com.ces.config.utils.StringUtil;
import com.ces.xarch.core.exception.FatalException;

/**
 * 成品构件分类Controller
 * 
 * @author wanglei
 * @date 2014-02-17
 */
public class CompleteComponentAreaController extends
        ConfigDefineServiceDaoController<CompleteComponentArea, CompleteComponentAreaService, CompleteComponentAreaDao> {

    private static final long serialVersionUID = 1035170721276195526L;

    /*
     * (non-Javadoc)
     * @see com.ces.xarch.core.web.struts2.BaseController#initModel()
     */
    @Override
    protected void initModel() {
        setModel(new CompleteComponentArea());
    }

    /*
     * (非 Javadoc) <p>标题: setService</p> <p>描述: 注入自定义服务层SERVICE</p> @param service
     * @see com.ces.config.dhtmlx.action.base.StringIDDhtmlxConfigController#setService(com.ces.xarch.core.service.
     * StringIDService)
     */
    @Autowired
    @Qualifier("completeComponentAreaService")
    @Override
    protected void setService(CompleteComponentAreaService service) {
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
            CompleteComponentArea parent = getService().getByID(model.getParentId());
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
        CompleteComponentArea completeComponentArea = (CompleteComponentArea) getModel();
        CompleteComponentArea temp = getService().getByName(completeComponentArea.getName());
        boolean nameExist = false;
        if (null != completeComponentArea.getId() && !"".equals(completeComponentArea.getId())) {
            CompleteComponentArea oldCompleteComponentArea = getService().getByID(completeComponentArea.getId());
            if (null != temp && null != oldCompleteComponentArea && !temp.getId().equals(oldCompleteComponentArea.getId())) {
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
        List<CompleteComponentArea> completeComponentAreaList = getService().getByParentId(model.getId());
        List<CompleteComponentVersion> completeComponentVersionList = getService(CompleteComponentVersionService.class).getByAreaId(model.getId());
        boolean flag = false;
        String message = null;
        if (CollectionUtils.isNotEmpty(completeComponentAreaList)) {
            message = "该成品构件分类下存在子分类，不能删除";
        } else if (CollectionUtils.isNotEmpty(completeComponentVersionList)) {
            message = "该成品构件分类下存在构件，不能删除";
        } else {
            CompleteComponentArea completeComponentArea = getService().getByID(model.getId());
            getService().delete(completeComponentArea);
            List<CompleteComponentArea> childList = getService().getByParentId(completeComponentArea.getParentId());
            if (CollectionUtils.isEmpty(childList)) {
                CompleteComponentArea parent = getService().getByID(completeComponentArea.getParentId());
                parent.setHasChild(false);
                getService().save(parent);
            }
            flag = true;
        }
        setReturnData("{'success':" + flag + ", 'message':'" + message + "'}");
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
        String targetId = getParameter("targetId");
        CompleteComponentArea startCompleteComponentArea = getService().getByID(start);
        if (startCompleteComponentArea.getParentId().equals(targetId)) {
            // 同个父分类中拖动排序
            if (StringUtil.isNotEmpty(end)) {
                CompleteComponentArea endCompleteComponentArea = getService().getByID(end);
                if (startCompleteComponentArea.getShowOrder().intValue() > endCompleteComponentArea.getShowOrder().intValue()) {
                    // 向上
                    List<CompleteComponentArea> completeComponentAreaList = getService().getCompleteComponentAreaListByShowOrder(
                            endCompleteComponentArea.getShowOrder(), startCompleteComponentArea.getShowOrder(), startCompleteComponentArea.getParentId());
                    startCompleteComponentArea.setShowOrder(endCompleteComponentArea.getShowOrder());
                    getService().save(startCompleteComponentArea);
                    for (CompleteComponentArea completeComponentArea : completeComponentAreaList) {
                        if (completeComponentArea.getId().equals(startCompleteComponentArea.getId())) {
                            continue;
                        }
                        completeComponentArea.setShowOrder(completeComponentArea.getShowOrder() + 1);
                        getService().save(completeComponentArea);
                    }
                } else {
                    // 向下
                    List<CompleteComponentArea> completeComponentAreaList = getService().getCompleteComponentAreaListByShowOrder(
                            startCompleteComponentArea.getShowOrder(), endCompleteComponentArea.getShowOrder(), startCompleteComponentArea.getParentId());
                    startCompleteComponentArea.setShowOrder(endCompleteComponentArea.getShowOrder() - 1);
                    getService().save(startCompleteComponentArea);
                    for (CompleteComponentArea completeComponentArea : completeComponentAreaList) {
                        if (completeComponentArea.getId().equals(startCompleteComponentArea.getId())
                                || completeComponentArea.getId().equals(endCompleteComponentArea.getId())) {
                            continue;
                        }
                        completeComponentArea.setShowOrder(completeComponentArea.getShowOrder() - 1);
                        getService().save(completeComponentArea);
                    }
                }
            }
            setReturnData("排序成功!");
        } else {
            String oldParentAreaId = startCompleteComponentArea.getParentId();
            // 拖动到不同的父分类中
            startCompleteComponentArea.setParentId(targetId);
            if (StringUtil.isNotEmpty(end)) {
                CompleteComponentArea endCompleteComponentArea = getService().getByID(end);
                startCompleteComponentArea.setShowOrder(endCompleteComponentArea.getShowOrder());
                getService().save(startCompleteComponentArea);
                getService().updateShowOrderPlusOne(endCompleteComponentArea.getShowOrder(), targetId);
            } else {
                Integer maxShowOrder = getService().getMaxShowOrder(targetId);
                int showOrder = 0;
                if (maxShowOrder == null) {
                    showOrder = 1;
                } else {
                    showOrder = maxShowOrder + 1;
                }
                startCompleteComponentArea.setShowOrder(showOrder);
                getService().save(startCompleteComponentArea);
                if (!"-1".endsWith(targetId)) {
                    CompleteComponentArea parent = getService().getByID(targetId);
                    if (!parent.getHasChild()) {
                        parent.setHasChild(true);
                        getService().save(parent);
                    }
                }
            }
            // 查询拖动分类原来的父分类中是否还有子分类，如果没有，将该分类hasChild设置成false
            List<CompleteComponentArea> oldChildAreaList = getService().getByParentId(oldParentAreaId);
            if (CollectionUtils.isEmpty(oldChildAreaList)) {
                CompleteComponentArea oldParentCompleteComponentArea = getService().getByID(oldParentAreaId);
                oldParentCompleteComponentArea.setHasChild(false);
                getService().save(oldParentCompleteComponentArea);
            }
            setReturnData("改变父成品构件分类成功!");
        }
        return new DefaultHttpHeaders(SUCCESS).disableCaching();
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
        List<CompleteComponentArea> areaList = getService(CompleteComponentAreaService.class).getByParentId(parentAreaId);
        String jsonStr = "";
        if (CollectionUtils.isNotEmpty(areaList)) {
            jsonStr = ", item:[";
            for (CompleteComponentArea area : areaList) {
                jsonStr += "{id:'" + area.getId() + "', text:'" + area.getName() + "', open:'1'" + getChildAreaJson(area.getId());
                jsonStr += "},";
            }
            jsonStr = jsonStr.substring(0, jsonStr.length() - 1);
            jsonStr += "]";
        }
        return jsonStr;
    }
}
