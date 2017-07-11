package com.ces.config.dhtmlx.service.construct;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.ces.config.dhtmlx.dao.common.DatabaseHandlerDao;
import com.ces.config.dhtmlx.dao.construct.ConstructFunctionDao;
import com.ces.config.dhtmlx.entity.component.ComponentFunctionData;
import com.ces.config.dhtmlx.entity.component.ComponentVersion;
import com.ces.config.dhtmlx.entity.construct.Construct;
import com.ces.config.dhtmlx.entity.construct.ConstructFunction;
import com.ces.config.dhtmlx.service.base.ConfigDefineDaoService;
import com.ces.config.dhtmlx.service.component.ComponentVersionService;
import com.ces.config.utils.ComponentParamsUtil;
import com.ces.config.utils.ConstantVar;
import com.ces.config.utils.StringUtil;
import com.google.common.collect.Lists;

/**
 * 预留区绑定的构件和页面方法的绑定关系Service
 * 
 * @author wanglei
 * @date 2013-08-27
 */
@Component("constructFunctionService")
public class ConstructFunctionService extends ConfigDefineDaoService<ConstructFunction, ConstructFunctionDao> {

    /*
     * (non-Javadoc)
     * @see com.ces.xarch.core.service.StringIDService#setDaoUnBinding(com.ces.xarch.core.persistence.jpa.StringIDDao)
     */
    @Autowired
    @Qualifier("constructFunctionDao")
    @Override
    protected void setDaoUnBinding(ConstructFunctionDao dao) {
        super.setDaoUnBinding(dao);
    }

    /**
     * 获取方法返回值列表数据
     * 
     * @param componentVersionId 页面构件版本ID
     * @param constructDetailId 组合构件中构件和预留区绑定关系ID
     * @return List<Object[]>
     */
    public List<Object[]> getFunctionDataList(String componentVersionId, String constructDetailId) {
        List<Object[]> functionDataList = null;
        if (StringUtil.isEmpty(componentVersionId)) {
            functionDataList = getDao().getCommonFunctionDataList(constructDetailId);
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
                functionDataList = getDao().getCommonFunctionDataList(constructDetailId);
                // 加载二次开发的方法
                functionDataList.addAll(getDao().getFunctionDataList(componentVersionId, constructDetailId));
            } else {
                functionDataList = getDao().getFunctionDataList(componentVersionId, constructDetailId);
            }
        }
        return functionDataList;
    }

    /**
     * 获取构件入参列表数据
     * 
     * @param componentVersionId 预留区绑定的构件版本ID
     * @param constructDetailId 组合构件中构件和预留区绑定关系ID
     * @return List<Object[]>
     */
    public List<Object[]> getInputParamList(String componentVersionId, String constructDetailId) {
        ComponentVersion componentVersion = getService(ComponentVersionService.class).getByID(componentVersionId);
        if (ConstantVar.Component.Type.PHYSICAL_TABLE.equals(componentVersion.getComponent().getType())
                || ConstantVar.Component.Type.LOGIC_TABLE.equals(componentVersion.getComponent().getType())
                || ConstantVar.Component.Type.NO_TABLE.equals(componentVersion.getComponent().getType())) {
            List<Object[]> inputParamList = getDao().getCommonInputParamList(constructDetailId);
            // 加载二次开发的参数
            List<Object[]> selfInputParamList = getDao().getInputParamList(componentVersionId, constructDetailId);
            if (CollectionUtils.isNotEmpty(selfInputParamList)) {
                List<String[]> constructFunctionList = getConstructFunctionList(constructDetailId);
                if (CollectionUtils.isNotEmpty(constructFunctionList)) {
                    for (Iterator<Object[]> it = selfInputParamList.iterator(); it.hasNext();) {
                        Object[] selfInputParam = it.next();
                        String selfInputParamId = StringUtil.null2empty(selfInputParam[0]);
                        for (String[] constructFunction : constructFunctionList) {
                            if (selfInputParamId.equals(constructFunction[0])) {
                                it.remove();
                            }
                        }
                    }
                }
                inputParamList.addAll(selfInputParamList);
            }
            return inputParamList;
        } else {
            return getDao().getInputParamList(componentVersionId, constructDetailId);
        }
    }

    /**
     * 获取方法出参和构件入参绑定关系列表数据
     * 
     * @param constructDetailId 组合构件中构件和预留区绑定关系ID
     * @return List<String[]>
     */
    public List<String[]> getConstructFunctionList(String constructDetailId) {
        List<Object[]> constructFunctionList = getDao().getConstructFunctionList(constructDetailId);
        List<String[]> list = Lists.newArrayList();
        if (CollectionUtils.isNotEmpty(constructFunctionList)) {
            for (Object[] objects : constructFunctionList) {
                String[] strs = new String[7];
                strs[0] = String.valueOf(objects[0] + "-" + objects[1]);
                strs[1] = String.valueOf(objects[2]);
                strs[2] = null == objects[3] ? "" : String.valueOf(objects[3]);
                strs[3] = String.valueOf(objects[4]);
                strs[4] = null == objects[5] ? "" : String.valueOf(objects[5]);
                strs[5] = String.valueOf(objects[6]);
                strs[6] = null == objects[7] ? "" : String.valueOf(objects[7]);
                list.add(strs);
            }
        }
        return list;
    }

    /**
     * 获取所有的方法出参和构件入参绑定关系列表数据
     * 
     * @return List<String[]>
     */
    public Map<String, List<String[]>> getAllConstructFunctions() {
        List<Object[]> constructFunctionList = getDao().getAllConstructFunctions();
        Map<String, List<String[]>> map = new HashMap<String, List<String[]>>();
        List<String[]> list = null;
        String constructDetailId = null;
        if (CollectionUtils.isNotEmpty(constructFunctionList)) {
            for (Object[] objects : constructFunctionList) {
                constructDetailId = String.valueOf(objects[0]);
                list = map.get(constructDetailId);
                if (list == null) {
                    list = Lists.newArrayList();
                    map.put(constructDetailId, list);
                }
                String[] strs = new String[7];
                strs[0] = String.valueOf(objects[1] + "-" + objects[2]);
                strs[1] = String.valueOf(objects[3]);
                strs[2] = null == objects[4] ? "" : String.valueOf(objects[4]);
                strs[3] = String.valueOf(objects[5]);
                strs[4] = null == objects[6] ? "" : String.valueOf(objects[6]);
                strs[5] = String.valueOf(objects[7]);
                strs[6] = null == objects[8] ? "" : String.valueOf(objects[8]);
                list.add(strs);
            }
        }
        return map;
    }

    /**
     * 获取预留区绑定的构件和页面方法的绑定关系
     * 
     * @param constructDetailId 组合构件中构件和预留区绑定关系ID
     * @return List<ConstructFunction>
     */
    public List<ConstructFunction> getByConstructDetailId(String constructDetailId) {
        return getDao().getByConstructDetailId(constructDetailId);
    }

    /**
     * 获取预留区绑定的构件和页面方法的绑定关系
     * 
     * @param constructDetailIds 组合构件中构件和预留区绑定关系IDs
     * @return List<ConstructFunction>
     */
    public List<ConstructFunction> getByConstructDetailIds(String constructDetailIds) {
        List<ConstructFunction> constructFunctionList = new ArrayList<ConstructFunction>();
        if (StringUtil.isNotEmpty(constructDetailIds)) {
            String hql = "from ConstructFunction t where t.constructDetailId in ('" + constructDetailIds.replace(",", "','") + "')";
            constructFunctionList = DatabaseHandlerDao.getInstance().queryEntityForList(hql, ConstructFunction.class);
        }
        return constructFunctionList;
    }

    /**
     * 保存预留区绑定的构件和页面方法的绑定关系
     * 
     * @param rowIds 方法返回值列表IDs
     * @param constructDetailId 预留区和构件关系ID
     */
    @SuppressWarnings("unchecked")
    @Transactional
    public void saveConstructFunction(String rowIds, String constructDetailId) {
        getDao().deleteByConstructDetailId(constructDetailId);
        String[] rowIdArray = rowIds.split(",");
        String outputParamId = null;
        String inputParamId = null;
        ComponentFunctionData componentFunctionData = null;
        ConstructFunction constructFunction = null;
        for (String rowId : rowIdArray) {
            String[] paramIds = rowId.split("-");
            inputParamId = paramIds[0];
            outputParamId = paramIds[1];
            componentFunctionData = (ComponentFunctionData) getDao(ComponentFunctionData.class).findOne(outputParamId);
            constructFunction = new ConstructFunction();
            constructFunction.setConstructDetailId(constructDetailId);
            constructFunction.setFunctionId(componentFunctionData.getFunctionId());
            constructFunction.setInputParamId(inputParamId);
            constructFunction.setOutputParamId(outputParamId);
            save(constructFunction);
        }
    }

    /**
     * 删除预留区绑定的构件和页面方法的绑定关系
     * 
     * @param constructDetailId 预留区和构件关系ID
     */
    @Transactional
    public void deleteConstructFunctions(String constructDetailId) {
        getDao().deleteByConstructDetailId(constructDetailId);
        // 清除缓存
        ComponentParamsUtil.removeParamFunctions(constructDetailId);
    }
}
