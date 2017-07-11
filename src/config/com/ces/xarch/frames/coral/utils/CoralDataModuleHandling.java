package com.ces.xarch.frames.coral.utils;

import java.io.Serializable;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.ServletActionContext;
import org.springframework.stereotype.Component;

import com.ces.xarch.core.entity.BaseEntity;
import com.ces.xarch.core.web.frame.FrameDataModel;
import com.ces.xarch.core.web.frame.impl.DefaultDataModel;
import com.ces.xarch.frames.coral.datamodel.combobox.ComboboxModel;
import com.ces.xarch.frames.coral.datamodel.grid.DataGridModel;
import com.ces.xarch.frames.coral.datamodel.tree.TreeModel;

/**
 * <p>描述: 组件库4.0数据模型处理</p>
 * <p>公司: 上海中信信息发展股份有限公司</p>
 * @author qiucs   
 * @date 2014-7-4 下午5:38:22
 *
 */
@Component
public class CoralDataModuleHandling {
    
	/**
	 * qiucs 2014-7-4 
	 * <p>描述: 转换DataGrid数据模型.</p>
	 * @return FrameDataModel<OT,OID>    返回类型   
	 */
	public <OID extends Serializable,OT extends BaseEntity<OID>> FrameDataModel<OT,OID> datagrid(Class<OT> otClass, OID oid) {
	    DataGridModel<OT, OID> model = new DataGridModel<OT, OID>();
	    model.setModelClass(otClass);
		return new DefaultDataModel<OT,OID>();
	}
	
	/**
	 * qiucs 2014-7-4 
	 * <p>描述: 转换Tree数据模型.</p>
	 * @return FrameDataModel<OT,OID>    返回类型   
	 */
	public <OID extends Serializable,OT extends BaseEntity<OID>> FrameDataModel<OT,OID> tree(Class<OT> otClass, OID oid) {
		TreeModel<OT,OID> model = new TreeModel<OT,OID>();
		
		HttpServletRequest request = ServletActionContext.getRequest();
		
		model.setOpen(request.getParameter(TreeModel.GLOBALOPEN_PARAM));
		model.setChecked(request.getParameter(TreeModel.CHECKED_PARAM));
		model.setIconAttrName(request.getParameter(TreeModel.ICON_PARAM));
		model.setParentAttrName(request.getParameter(TreeModel.ISPARENT_PARAM));
		model.setIdAttrName(request.getParameter(TreeModel.ID_PARAM));
		model.setModelClass(otClass);
		
		return model;
	}
    
    /**
     * qiucs 2014-7-4 
     * <p>描述: 转换Tree数据模型.</p>
     * @return FrameDataModel<OT,OID>    返回类型   
     */
    public <OID extends Serializable,OT extends BaseEntity<OID>> FrameDataModel<OT,OID> combobox(Class<OT> otClass, OID oid) {
        ComboboxModel<OT,OID> model = new ComboboxModel<OT,OID>();
        
        HttpServletRequest request = ServletActionContext.getRequest();
        model.setSelected(request.getParameter(ComboboxModel.SELECTED_PARAM));
        model.setModelClass(otClass);
        
        return model;
    }
	
}
