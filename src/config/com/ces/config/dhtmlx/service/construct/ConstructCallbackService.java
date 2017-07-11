package com.ces.config.dhtmlx.service.construct;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.ces.config.dhtmlx.dao.common.DatabaseHandlerDao;
import com.ces.config.dhtmlx.dao.construct.ConstructCallbackDao;
import com.ces.config.dhtmlx.entity.component.ComponentVersion;
import com.ces.config.dhtmlx.entity.construct.Construct;
import com.ces.config.dhtmlx.entity.construct.ConstructCallback;
import com.ces.config.dhtmlx.service.base.ConfigDefineDaoService;
import com.ces.config.dhtmlx.service.component.ComponentVersionService;
import com.ces.config.utils.ComponentParamsUtil;
import com.ces.config.utils.ConstantVar;
import com.ces.config.utils.StringUtil;
import com.google.common.collect.Lists;

/**
 * 预留区绑定的构件和回调函数的绑定关系Service
 * 
 * @author wanglei
 * @date 2013-09-28
 */
@Component("constructCallbackService")
public class ConstructCallbackService extends ConfigDefineDaoService<ConstructCallback, ConstructCallbackDao> {

    /*
     * (non-Javadoc)
     * @see com.ces.xarch.core.service.StringIDService#setDaoUnBinding(com.ces.xarch.core.persistence.jpa.StringIDDao)
     */
    @Autowired
    @Qualifier("constructCallbackDao")
    @Override
    protected void setDaoUnBinding(ConstructCallbackDao dao) {
        super.setDaoUnBinding(dao);
    }

    /**
     * 获取回调函数参数列表数据（按钮预设、逻辑表构件、物理表构件 的配置用公用配置）
     * 
     * @param componentVersionId 页面构件版本ID
     * @param constructDetailId 组合构件中构件和预留区绑定关系ID
     * @return List<String[]>
     */
    public List<String[]> getCallbackParamList(String componentVersionId, String constructDetailId) {
        List<Object[]> callbackParamList = null;
        if (StringUtil.isEmpty(componentVersionId)) {
            callbackParamList = getDao().getCommonCallbackParamList(constructDetailId);
        } else {
            ComponentVersion componentVersion = getService(ComponentVersionService.class).getByID(componentVersionId);
            if (ConstantVar.Component.Type.ASSEMBLY.equals(componentVersion.getComponent().getType())) {
                Construct construct = getService(ConstructService.class).getByAssembleComponentVersionId(componentVersion.getId());
                componentVersionId = construct.getBaseComponentVersionId();
                componentVersion = getService(ComponentVersionService.class).getByID(componentVersionId);
            }
            if (ConstantVar.Component.Type.LOGIC_TABLE.equals(componentVersion.getComponent().getType())
                    || ConstantVar.Component.Type.PHYSICAL_TABLE.equals(componentVersion.getComponent().getType())
                    || ConstantVar.Component.Type.NO_TABLE.equals(componentVersion.getComponent().getType())) {
                callbackParamList = getDao().getCommonCallbackParamList(constructDetailId);
                // 加载二次开发的方法
                callbackParamList.addAll(getDao().getCallbackParamList(componentVersionId, constructDetailId));
            } else {
                callbackParamList = getDao().getCallbackParamList(componentVersionId, constructDetailId);
            }
        }
        List<Object[]> constructCallBackList = getDao().getConstructCallbackList(constructDetailId);
        List<String[]> list = Lists.newArrayList();
        boolean exist = false;
        if (CollectionUtils.isNotEmpty(callbackParamList)) {
            for (Object[] objects : callbackParamList) {
                exist = false;
                if (CollectionUtils.isNotEmpty(constructCallBackList)) {
                    for (Object[] objs : constructCallBackList) {
                        if ((objs[0] + "-" + objs[1]).equals(objects[0] + "-" + objects[1])) {
                            exist = true;
                            break;
                        }
                    }
                }
                if (!exist) {
                    String[] strs = new String[5];
                    strs[0] = objects[0] + "-" + objects[1];
                    strs[1] = null == objects[2] ? "" : String.valueOf(objects[2]);
                    strs[2] = null == objects[3] ? "" : String.valueOf(objects[3]);
                    strs[3] = null == objects[4] ? "" : String.valueOf(objects[4]);
                    strs[4] = null == objects[5] ? "" : String.valueOf(objects[5]);
                    list.add(strs);
                }
            }
        }
        return list;
    }

    /**
     * 获取预留区绑定的构件的出参列表数据
     * 
     * @param componentVersionId 预留区绑定的构件版本ID
     * @return List<Object[]>
     */
    public List<Object[]> getOutputParamList(String componentVersionId) {
        return getDao().getOutputParamList(componentVersionId);
    }

    /**
     * 获取回调函数参数和构件出参绑定关系列表数据
     * 
     * @param constructDetailId 组合构件中构件和预留区绑定关系ID
     * @return List<Object[]>
     */
    public List<String[]> getConstructCallbackList(String constructDetailId) {
        List<Object[]> constructCallBackList = getDao().getConstructCallbackList(constructDetailId);
        List<String[]> list = Lists.newArrayList();
        if (CollectionUtils.isNotEmpty(constructCallBackList)) {
            for (Object[] objects : constructCallBackList) {
                String[] strs = new String[7];
                strs[0] = String.valueOf(objects[0] + "-" + objects[1] + "-" + objects[2]);
                strs[1] = null == objects[3] ? "" : String.valueOf(objects[3]);
                strs[2] = null == objects[4] ? "" : String.valueOf(objects[4]);
                strs[3] = null == objects[5] ? "" : String.valueOf(objects[5]);
                strs[4] = null == objects[6] ? "" : String.valueOf(objects[6]);
                strs[5] = null == objects[7] ? "" : String.valueOf(objects[7]);
                strs[6] = null == objects[8] ? "" : String.valueOf(objects[8]);
                list.add(strs);
            }
        }
        return list;
    }

    /**
     * 获取所有回调函数参数和构件出参绑定关系列表数据
     * 
     * @return List<Object[]>
     */
    public Map<String, List<String[]>> getAllConstructCallbacks() {
        List<Object[]> constructCallBackList = getDao().getAllConstructCallbacks();
        Map<String, List<String[]>> map = new HashMap<String, List<String[]>>();
        List<String[]> list = null;
        String constructDetailId = null;
        if (CollectionUtils.isNotEmpty(constructCallBackList)) {
            for (Object[] objects : constructCallBackList) {
                constructDetailId = String.valueOf(objects[0]);
                list = map.get(constructDetailId);
                if (list == null) {
                    list = Lists.newArrayList();
                    map.put(constructDetailId, list);
                }
                String[] strs = new String[7];
                strs[0] = String.valueOf(objects[1] + "-" + objects[2] + "-" + objects[3]);
                strs[1] = null == objects[4] ? "" : String.valueOf(objects[4]);
                strs[2] = null == objects[5] ? "" : String.valueOf(objects[5]);
                strs[3] = null == objects[6] ? "" : String.valueOf(objects[6]);
                strs[4] = null == objects[7] ? "" : String.valueOf(objects[7]);
                strs[5] = null == objects[8] ? "" : String.valueOf(objects[8]);
                strs[6] = null == objects[9] ? "" : String.valueOf(objects[9]);
                list.add(strs);
            }
        }
        return map;
    }

    /**
     * 获取预留区绑定的构件和回调函数的绑定关系
     * 
     * @param constructDetailId 组合构件中构件和预留区绑定关系ID
     * @return List<ConstructCallback>
     */
    public List<ConstructCallback> getByConstructDetailId(String constructDetailId) {
        return getDao().getByConstructDetailId(constructDetailId);
    }

    /**
     * 获取预留区绑定的构件和回调函数的绑定关系
     * 
     * @param constructDetailIds 组合构件中构件和预留区绑定关系IDs
     * @return List<ConstructCallback>
     */
    public List<ConstructCallback> getByConstructDetailIds(String constructDetailIds) {
        List<ConstructCallback> constructCallbackList = new ArrayList<ConstructCallback>();
        if (StringUtil.isNotEmpty(constructDetailIds)) {
            String hql = "from ConstructCallback t where t.constructDetailId in ('" + constructDetailIds.replace(",", "','") + "')";
            constructCallbackList = DatabaseHandlerDao.getInstance().queryEntityForList(hql, ConstructCallback.class);
        }
        return constructCallbackList;
    }

    /**
     * 保存预留区绑定的构件和回调函数的绑定关系
     * 
     * @param rowIds 方法返回值列表IDs
     * @param constructDetailId 预留区和构件关系ID
     */
    @Transactional
    public void saveConstructCallback(String rowIds, String constructDetailId) {
        getDao().deleteByConstructDetailId(constructDetailId);
        String[] rowIdArray = rowIds.split(",");
        ConstructCallback constructCallback = null;
        String callbackId = null;
        String inputParamId = null;
        String outputParamId = null;
        for (String rowId : rowIdArray) {
            String[] ids = rowId.split("-");
            callbackId = ids[0];
            inputParamId = ids[1];
            outputParamId = ids[2];
            constructCallback = new ConstructCallback();
            constructCallback.setConstructDetailId(constructDetailId);
            constructCallback.setCallbackId(callbackId);
            constructCallback.setInputParamId(inputParamId);
            constructCallback.setOutputParamId(outputParamId);
            save(constructCallback);
        }
    }

    /**
     * 删除预留区绑定的构件和回调函数的绑定关系
     * 
     * @param constructDetailId 预留区和构件关系ID
     */
    @Transactional
    public void deleteConstructCallbacks(String constructDetailId) {
        getDao().deleteByConstructDetailId(constructDetailId);
        // 清除缓存
        ComponentParamsUtil.removeParamCallbacks(constructDetailId);
    }
}
