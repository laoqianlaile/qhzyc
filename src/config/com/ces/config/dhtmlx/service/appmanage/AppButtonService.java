package com.ces.config.dhtmlx.service.appmanage;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.ces.config.datamodel.message.MessageModel;
import com.ces.config.dhtmlx.dao.appmanage.AppButtonDao;
import com.ces.config.dhtmlx.entity.appmanage.AppButton;
import com.ces.config.dhtmlx.entity.appmanage.AppDefine;
import com.ces.config.dhtmlx.service.base.ConfigDefineDaoService;
import com.ces.config.utils.CommonUtil;
import com.ces.utils.BeanUtils;
import com.google.common.collect.Lists;

@Component
public class AppButtonService extends ConfigDefineDaoService<AppButton, AppButtonDao> {
    
    /**
     * qiucs 2013-9-12 
     * <p>描述: 获取按钮配置</p>
     */
    public List<AppButton> findByFk(String tableId, String componentVersionId, String menuId, String type) {
        return getDao().findByFk(tableId, componentVersionId, menuId, type);
    }

    /**
     * qiucs 2014-12-23 下午2:20:15
     * <p>描述: 保存按钮配置 </p>
     * @return MessageModel
     */
    @Transactional
    public MessageModel save(String tableId, String componentVersionId, String menuId, String type, String rowsValue) {
        // 1. delete
        getDao().deleteByFk(tableId, componentVersionId, menuId, type);
        // 2. save
        List<AppButton> list = Lists.newArrayList();
        String[] rowArr = rowsValue.split(";");
        for (int i = 0; i < rowArr.length; i++) {
            String row = rowArr[i];
            String[] propArr = row.split(",");
            AppButton button = new AppButton();
            button.setButtonType(type);
            button.setTableId(tableId);
            button.setComponentVersionId(componentVersionId);
            button.setMenuId(menuId);
            button.setButtonCode(propArr[0]);
            button.setButtonName(propArr[1]);
            button.setShowName(propArr[2]);
            if(propArr.length > 3) {
            	button.setButtonClass(propArr[3]);
            }              
            if(propArr.length > 4) {
            	button.setRemark(propArr[4]);
            }              
            button.setShowOrder(new Integer(i + 1));
            list.add(button);
        }
        save(list);
        // 3. 更新按钮配置标记
        getService(AppDefineService.class).updateAppDefine(tableId, componentVersionId, menuId, CommonUtil.SUPER_ADMIN_ID, 
                AppButton.BUTTON_GRID.equals(type) ? AppDefine.TYPE_GRID_BUTTON : AppDefine.TYPE_FORM_BUTTON, AppDefine.DEFINE_YES);
        
        return MessageModel.trueInstance("OK");
    }

    /**
     * qiucs 2014-12-23 下午2:22:43
     * <p>描述: 清除按钮配置 </p>
     * @return MessageModel
     */
    @Transactional
    public MessageModel clear(String tableId, String componentVersionId, String menuId, String type) {
        // 1. 
        getDao().deleteByFk(tableId, componentVersionId, menuId, type);
        // 2. 更新按钮配置标记
        getService(AppDefineService.class).updateAppDefine(tableId, componentVersionId, menuId, CommonUtil.SUPER_ADMIN_ID,
                AppButton.BUTTON_GRID.equals(type) ? AppDefine.TYPE_GRID_BUTTON : AppDefine.TYPE_FORM_BUTTON, AppDefine.DEFINE_NO);
        
        return MessageModel.trueInstance("OK");
    }
    
    /**
     * qiucs 2013-11-27 
     * <p>描述: 根据表ID删除配置</p>
     * @param  tableId    设定参数   
     */
    @Transactional
    public void deleteByTableId(String tableId) {
        getDao().deleteByTableId(tableId);
    }
    
    /**
     * qiucs 2013-11-27 
     * <p>描述: 根据模块ID删除配置</p>
     * @param  componentVersionId    设定参数   
     */
    @Transactional
    public void deleteByComponentVersionId(String componentVersionId) {
        getDao().deleteByComponentVersionId(componentVersionId);
    }
    
    /**
     * qiucs 2013-11-27 
     * <p>描述: 根据表ID和模块ID删除配置</p>
     * @param  tableId    设定参数   
     * @param  componentVersionId    设定参数   
     */
    @Transactional
    public void deleteByTableIdAndComponentVersionId(String tableId, String componentVersionId) {
        getDao().deleteByTableIdAndComponentVersionId(tableId, componentVersionId);
    }
    
    /**
     * qiucs 2015-1-21 下午1:24:57
     * <p>描述: 根据菜单ID删除配置 </p>
     * @return void
     */
    @Transactional
    public void deleteByMenuId(String menuId) {
        getDao().deleteByMenuId(menuId);
    }

	/**
	 * qiucs 2015-4-27 下午7:46:55
	 * <p>描述: 工作流基本检索（从一个版本复制到另一个版本） </p>
	 * @return void
	 */
    @Transactional
	public void copyWorkflow(String tableId, String fromVersionId, String toVersionId) {
		List<AppButton> list = find("EQ_tableId=" + tableId + ";EQ_menuId=" + fromVersionId);
    	int i = 0, len = list.size();
    	AppButton entity = null;
    	Set<String> gridSet = new HashSet<String>();
    	Set<String> formSet = new HashSet<String>();
    	Iterator<String> it = null;
    	List<AppButton> destList = new ArrayList<AppButton>();
    	for (; i < len; i++) {
    		entity = new AppButton();
    		BeanUtils.copy(list.get(i), entity);
    		entity.setId(null);
    		entity.setMenuId(toVersionId);
    		if (AppButton.BUTTON_GRID.equals(entity.getButtonType())) {
    			gridSet.add(entity.getComponentVersionId());
    		} else {
    			formSet.add(entity.getComponentVersionId());
    		}
    		destList.add(entity);
    	}
    	list = null;
    	if (!destList.isEmpty()) {
    		save(destList);
    		// 更新列表按钮配置标记
    		it = gridSet.iterator();
    		while (it.hasNext()) {
    			getService(AppDefineService.class).updateAppDefine(tableId, it.next(), toVersionId, CommonUtil.SUPER_ADMIN_ID, 
    					AppDefine.TYPE_GRID_BUTTON, AppDefine.DEFINE_YES);
    		}
    		// 更新表单按钮配置标记
    		it = formSet.iterator();
    		while (it.hasNext()) {
    			getService(AppDefineService.class).updateAppDefine(tableId, it.next(), toVersionId, CommonUtil.SUPER_ADMIN_ID, 
    					AppDefine.TYPE_FORM_BUTTON, AppDefine.DEFINE_YES);
    		}
    		it = null;
    	}
    	gridSet = null;
    	formSet = null;
    	destList = null;
	}
}
