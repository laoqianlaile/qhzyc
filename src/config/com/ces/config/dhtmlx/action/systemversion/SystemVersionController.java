package com.ces.config.dhtmlx.action.systemversion;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.struts2.rest.DefaultHttpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.ces.config.dhtmlx.action.base.ConfigDefineServiceDaoController;
import com.ces.config.dhtmlx.dao.systemvesion.SystemVersionDao;
import com.ces.config.dhtmlx.entity.resource.Resource;
import com.ces.config.dhtmlx.entity.systemversion.SystemVersion;
import com.ces.config.dhtmlx.service.resource.ResourceService;
import com.ces.config.dhtmlx.service.systemversion.SystemVersionResourceService;
import com.ces.config.dhtmlx.service.systemversion.SystemVersionService;
import com.ces.xarch.core.exception.FatalException;

/**
 * 系统版本Controller
 * 
 * @author wanglei
 * @date 2015-04-18
 */
public class SystemVersionController extends ConfigDefineServiceDaoController<SystemVersion, SystemVersionService, SystemVersionDao> {

    private static final long serialVersionUID = 5448108829911125571L;

    /*
     * (non-Javadoc)
     * @see com.ces.xarch.core.web.struts2.BaseController#initModel()
     */
    @Override
    protected void initModel() {
        setModel(new SystemVersion());
    }

    /*
     * (非 Javadoc) <p>标题: setService</p> <p>描述: 注入自定义服务层SERVICE</p> @param service
     * @see com.ces.config.dhtmlx.action.base.StringIDDhtmlxConfigController#setService(com.ces.xarch.core.service.
     * StringIDService)
     */
    @Autowired
    @Qualifier("systemVersionService")
    @Override
    protected void setService(SystemVersionService service) {
        super.setService(service);
    }

    /*
     * (non-Javadoc)
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
        return SUCCESS;
    }

    /**
     * 排序
     * 
     * @return Object
     */
    public Object sort() {
        String start = getParameter("start");
        String end = getParameter("end");
        SystemVersion startSystemVersion = getService().getByID(start);
        SystemVersion endSystemVersion = getService().getByID(end);
        if (startSystemVersion.getShowOrder().intValue() > endSystemVersion.getShowOrder().intValue()) {
            // 向上
            List<SystemVersion> systemVersionList = getService().getSystemVersionListByShowOrder(endSystemVersion.getShowOrder(),
                    startSystemVersion.getShowOrder(), startSystemVersion.getSystemId());
            startSystemVersion.setShowOrder(endSystemVersion.getShowOrder());
            getService().save(startSystemVersion);
            for (SystemVersion systemVersion : systemVersionList) {
                if (systemVersion.getId().equals(startSystemVersion.getId())) {
                    continue;
                }
                systemVersion.setShowOrder(systemVersion.getShowOrder() + 1);
                getService().save(systemVersion);
            }
        } else {
            // 向下
            List<SystemVersion> systemVersionList = getService().getSystemVersionListByShowOrder(startSystemVersion.getShowOrder(),
                    endSystemVersion.getShowOrder(), startSystemVersion.getSystemId());
            startSystemVersion.setShowOrder(endSystemVersion.getShowOrder());
            getService().save(startSystemVersion);
            for (SystemVersion systemVersion : systemVersionList) {
                if (systemVersion.getId().equals(startSystemVersion.getId())) {
                    continue;
                }
                systemVersion.setShowOrder(systemVersion.getShowOrder() - 1);
                getService().save(systemVersion);
            }
        }
        setReturnData("排序成功");
        return new DefaultHttpHeaders(SUCCESS).disableCaching();
    }

    /**
     * 校验字段是否已经存在
     * 
     * @return Object
     */
    public Object validateSystemVersion() {
        SystemVersion systemVersion = (SystemVersion) getModel();
        SystemVersion temp = getService().getBySystemIdAndName(systemVersion.getSystemId(), systemVersion.getName());
        boolean nameExist = false;
        if (null != systemVersion.getId() && !"".equals(systemVersion.getId())) {
            SystemVersion oldSystemVersion = getService().getByID(systemVersion.getId());
            if (null != temp && null != oldSystemVersion && !temp.getId().equals(oldSystemVersion.getId())) {
                nameExist = true;
            }
        } else {
            if (null != temp) {
                nameExist = true;
            }
        }
        setReturnData("{'nameExist':" + nameExist + "}");
        return new DefaultHttpHeaders(SUCCESS).disableCaching();
    }

    /**
     * 获取系统版本与资源关系列表格式
     * 
     * @return Object
     */
    public Object getVersionResourceGridFormat() {
        String systemId = getParameter("systemId");
        List<SystemVersion> systemVersionList = getService().getBySystemId(systemId);
        Map<String, Object> gridData = new HashMap<String, Object>();
        Map<String, Object> format = new HashMap<String, Object>();
        List<String> headersList = new ArrayList<String>();
        List<String> colWidthsList = new ArrayList<String>();
        List<String> colTypesList = new ArrayList<String>();
        List<String> colAlignsList = new ArrayList<String>();
        List<String> versionIdsList = new ArrayList<String>();
        headersList.add("<center>资源名称</center>");
        colWidthsList.add("300");
        colTypesList.add("ro");
        colAlignsList.add("left");
        versionIdsList.add("");
        format.put("headers", headersList);
        format.put("colWidths", colWidthsList);
        format.put("colTypes", colTypesList);
        format.put("colAligns", colAlignsList);
        format.put("versionIds", versionIdsList);
        gridData.put("format", format);
        if (CollectionUtils.isNotEmpty(systemVersionList)) {
            for (SystemVersion systemVersion : systemVersionList) {
                headersList.add("<center>" + systemVersion.getName() + "</center>");
                colWidthsList.add("150");
                colTypesList.add("ch");
                colAlignsList.add("center");
                versionIdsList.add(systemVersion.getId());
            }
        }
        headersList.add("");
        colWidthsList.add("*");
        colTypesList.add("ro");
        colAlignsList.add("center");
        setReturnData(gridData);
        return new DefaultHttpHeaders(SUCCESS).disableCaching();
    }

    /**
     * 获取系统版本与资源关系列表数据
     * 
     * @return Object
     */
    @SuppressWarnings("unchecked")
    public Object getVersionResourceGridData() {
        String systemId = getParameter("systemId");
        List<Object[]> list = new ArrayList<Object[]>();
        List<SystemVersion> systemVersionList = getService().getBySystemId(systemId);
        Resource rootResource = getService(ResourceService.class).getByTargetId(systemId);
        List<Resource> resourceList = getService(ResourceService.class).getBySystemId(systemId);
        if (CollectionUtils.isNotEmpty(resourceList)) {
            Set<String>[] resourceIdSets = new HashSet[systemVersionList.size()];
            List<String> tempResourceIdList = null;
            for (int i = 0; i < systemVersionList.size(); i++) {
                tempResourceIdList = getService(SystemVersionResourceService.class).getResourceIdsBySystemVersionId(systemVersionList.get(i).getId());
                resourceIdSets[i] = new HashSet<String>();
                if (CollectionUtils.isNotEmpty(tempResourceIdList)) {
                    resourceIdSets[i].addAll(tempResourceIdList);
                }
            }
            Object[] objs = new Object[resourceIdSets.length + 2];
            objs[0] = rootResource.getId();
            objs[1] = rootResource.getName();
            for (int i = 2; i < resourceIdSets.length + 2; i++) {
                if (i == 2) {
                    objs[i] = "1";
                } else {
                    if (resourceIdSets[i - 2].contains(rootResource.getId())) {
                        objs[i] = "1";
                    } else {
                        objs[i] = "0";
                    }
                }
            }
            list.add(objs);
            // 资源缩进显示
            parseResourceGridData(rootResource, resourceList, list, resourceIdSets, 1);
        }
        setReturnData(list);
        return new DefaultHttpHeaders(SUCCESS).disableCaching();
    }

    /**
     * 转换系统版本与资源关系列表数据的显示形式
     */
    private void parseResourceGridData(Resource parent, List<Resource> resourceList, List<Object[]> list, Set<String>[] resourceIdSets, int level) {
        if (CollectionUtils.isNotEmpty(resourceList)) {
            Resource resource = null;
            Object[] objs = null;
            int objsLength = resourceIdSets.length + 2;
            for (Iterator<Resource> it = resourceList.iterator(); it.hasNext();) {
                resource = it.next();
                if (resource.getParentId().equals(parent.getId())) {
                    objs = new Object[objsLength];
                    objs[0] = resource.getId();
                    objs[1] = getPrefixSpace(level) + resource.getName();
                    for (int i = 2; i < objsLength; i++) {
                        if (i == 2) {
                            objs[i] = "1";
                        } else {
                            if (resourceIdSets[i - 2].contains(resource.getId())) {
                                objs[i] = "1";
                            } else {
                                objs[i] = "0";
                            }
                        }
                    }
                    list.add(objs);
                    parseResourceGridData(resource, resourceList, list, resourceIdSets, level + 1);
                }
            }

        }
    }

    /**
     * 获取层级的缩进前缀
     * 
     * @return String
     */
    private String getPrefixSpace(int level) {
        String str = "";
        for (int i = 0; i < level; i++)
            str += "　  ";
        return str;
    }

    /**
     * 保存系统版本与资源关系列表数据
     * 
     * @return Object
     */
    public Object saveVersionResource() {
        String systemId = getParameter("systemId");
        String checkedIds = getParameter("checkedIds");
        boolean flag = true;
        try {
            getService().saveVersionResource(systemId, checkedIds);
        } catch (Exception e) {
            flag = false;
            e.printStackTrace();
        }
        setReturnData("{'success':" + flag + "}");
        return new DefaultHttpHeaders(SUCCESS).disableCaching();
    }
}
