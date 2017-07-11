/**
 * <p>Copyright:Copyright(c) 2013</p>
 * <p>Company:上海中信信息发展股份有限公司</p>
 * <p>包名:com.ces.xarch.core.web.frame.dhtmlx.datamodel.tree</p>
 * <p>文件名:TreeModel.java</p>
 * <p>类更新历史信息</p>
 * @todo Reamy(杨木江 yangmujiang@sohu.com) 创建于 2013-06-07 17:27:32
 */
package com.ces.xarch.frames.dhtmlx.datamodel.tree;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import ognl.Ognl;
import ognl.OgnlException;

import org.springframework.beans.BeanUtils;

import com.ces.utils.StringUtils;
import com.ces.xarch.core.entity.BaseEntity;
import com.ces.xarch.core.exception.BusinessException;
import com.ces.xarch.core.utils.FilterHelper;
import com.ces.xarch.core.web.frame.impl.DefaultDataModel;
import com.ces.xarch.core.web.jackson.JacksonFilterProvider;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Tree数据Bean.
 * <p>描述:存储Tree数据集</p>
 * <p>Company:上海中信信息发展股份有限公司</p>
 * @author Reamy(杨木江 yangmujiang@sohu.com)
 * @date 2013-06-07  17:27:32
 * @version 1.0.2013.0607
 * 
 * 复写 xarch-dhtmlx-1.0.jar中TreeModel.class
 */
public class TreeModel<T extends BaseEntity<ID>,ID extends Serializable> extends DefaultDataModel<T, ID> {
	/** 自定义数据参数名称. */
	public static final String USERDATA_PARAM = "P_UD";
	/** 节点展开参数名称. */
	public static final String GLOBALOPEN_PARAM = "P_OPEN";
	/** 选中节点参数名称. */
	public static final String SELECT_PARAM = "P_SELECT";
	/** 勾选节点参数名称. */
	public static final String CHECKED_PARAM = "P_CHECKED";
	/** 默认没有子节点参数名称. */
	public static final String NOCHILD_PARAM = "P_NOCHILD";
	
	
	/** 当前节点ID. */
	private ID id;
	
	/** 自定义属性. */
	private String userData;
	/** 所有节点是否默认展开. */
	private boolean open;
	/** 选中的节点ID. */
	private String select;
	/** 勾选的节点ID. */
	private String checked;
	/** 是否默认没有子节点. */
	private boolean noChild;
	
	/** 节点名称属性名. */
	private String textAttrName;
	/** 是否有子节点属性名. */
	private String childAttrName;
    /** 节点图标属性名. */
    private String[] iconAttrName;
	
	/**
	 * 构造函数.
	 * @param id 当前节点ID
	 * @author Reamy(杨木江 yangmujiang@sohu.com)
	 * @date 2013-06-07  17:33:03
	 */
	public TreeModel(ID id) {
		this.id = id;
	}

	/* (non-Javadoc)
	 * @see com.ces.xarch.core.web.frame.impl.DefaultDataModel#getData()
	 * @author Reamy(杨木江 yangmujiang@sohu.com)
	 * @date 2013-06-07 17:31:47
	 */
	@Override
	@JsonProperty("item")
	public Object getData() {
		return super.getData();
	}

	/**
	 * 获取当前节点ID.
	 * @author Reamy(杨木江 yangmujiang@sohu.com)
	 * @date 2013-06-07  17:31:07
	 */
	public ID getId() {
		return id;
	}

	/* (non-Javadoc)
	 * @see com.ces.xarch.core.web.frame.impl.DefaultDataModel#getPageNumber()
	 * @author Reamy(杨木江 yangmujiang@sohu.com)
	 * @date 2013-06-07 17:31:47
	 */
	@Override
	@JsonIgnore
	public int getPageNumber() {
		return super.getPageNumber();
	}

	/* (non-Javadoc)
	 * @see com.ces.xarch.core.web.frame.impl.DefaultDataModel#getPageSize()
	 * @author Reamy(杨木江 yangmujiang@sohu.com)
	 * @date 2013-06-07 17:31:47
	 */
	@Override
	@JsonIgnore
	public int getPageSize() {
		return super.getPageSize();
	}

	/* (non-Javadoc)
	 * @see com.ces.xarch.core.web.frame.impl.DefaultDataModel#getTotal()
	 * @author Reamy(杨木江 yangmujiang@sohu.com)
	 * @date 2013-06-07 17:31:47
	 */
	@Override
	@JsonIgnore
	public long getTotal() {
		return super.getTotal();
	}

	/* (non-Javadoc)
	 * @see com.ces.xarch.core.web.frame.impl.DefaultDataModel#getTotalPages()
	 * @author Reamy(杨木江 yangmujiang@sohu.com)
	 * @date 2013-06-07 17:31:47
	 */
	@Override
	@JsonIgnore
	public int getTotalPages() {
		return super.getTotalPages();
	}

	/* (non-Javadoc)
	 * @see com.ces.xarch.core.web.frame.impl.DefaultDataModel#setData(java.util.List)
	 * @author Reamy(杨木江 yangmujiang@sohu.com)
	 * @date 2013-06-07 17:31:47
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void setData(List<T> dataList) {
		if (dataList != null && dataList.size() > 0) {
			data = new ArrayList<TreeItem<ID>>(dataList.size());
			TreeItemBuild build = new TreeItemBuild(null);
			
			for (T bean : dataList) {
				((List<TreeItem<ID>>)data).add(toTreeItem(bean,build));
			}
			
			build = null;
		}
	}

    @Override
	public void setData(Object dataList) {
        this.data = dataList;
    }
		
	/**
	 * 将实体转换成树节点.
	 * @author Reamy(杨木江 yangmujiang@sohu.com)
	 * @date 2013-06-07  17:38:02
	 */
	private TreeItem<ID> toTreeItem(T bean, TreeItemBuild build) {
		if (textAttrName == null) {
			findPropertys(bean);
		}
		
		boolean child = !noChild;
		List<TreeUserData> userdata = null;
		
		if (!org.apache.commons.lang3.StringUtils.isBlank(userData)) {
			if (build.getFilterProvider() == null) {
				build.setFilterProvider(getUserDataFilter(bean.getClass()));
			}
			if (build.getFilterProvider().getInPropertys(FilterHelper.getFilterId(bean.getClass())) != null
				&& build.getFilterProvider().getInPropertys(FilterHelper.getFilterId(bean.getClass())).length > 0) {
				userdata = new ArrayList<TreeUserData>(build.getFilterProvider().getInPropertys(FilterHelper.getFilterId(bean.getClass())).length);
				build.convert(bean, userdata);
			}
		}
		
		if (childAttrName != null && !"".equals(childAttrName.trim())) {
			try {
				Class<?> toType = BeanUtils.findPropertyType(childAttrName, new Class[]{bean.getClass()});
				
				if (toType == Boolean.class) {
					child = (Boolean)Ognl.getValue(childAttrName, bean, toType);
				} else {
					child = Long.valueOf(Ognl.getValue(childAttrName, bean, toType).toString()) > 0;
				}
			} catch (OgnlException e) {
				throw new BusinessException("从实体类["+bean.getClass()+"."+childAttrName+"]中获取节点是否有子节点属性失败",e);
			}
		}
		
		try {
			TreeItem<ID> item = new TreeItem<ID>(bean.getId(), Ognl.getValue(textAttrName, bean).toString(), child, userdata);
			item.setOpen(open);
			item.setSelect(isSelected(bean.getId()));
			item.setChecked(isChecked(bean.getId()));
			if (null != iconAttrName) {
			    //String str = Ognl.getValue(iconAttrName[0], bean).toString();
			    //System.out.println(iconAttrName[0] + ", str = " + str);
			    item.setIm0(Ognl.getValue(iconAttrName[0], bean).toString());
                item.setIm1(Ognl.getValue(iconAttrName[1], bean).toString());
                item.setIm2(Ognl.getValue(iconAttrName[2], bean).toString());
			}
			return item;
		} catch (OgnlException e) {
			throw new BusinessException("从实体类["+bean.getClass()+"."+textAttrName+"]中获取节点名称失败",e);
		}
	}
	
	/**
	 * 验证给定ID是否在勾选列表中.
	 * @author Reamy(杨木江 yangmujiang@sohu.com)
	 * @date 2013-06-20  11:46:15
	 */
	private boolean isChecked(ID id) {
		if (!org.apache.commons.lang3.StringUtils.isBlank(checked)) {
			if ((","+checked+",").indexOf(","+id+",") != -1) {
				return true;
			}
		}
		
		return false;
	}

	/**
	 * 验证给定ID是否在选中列表中.
	 * @author Reamy(杨木江 yangmujiang@sohu.com)
	 * @date 2013-06-20  11:43:18
	 */
	private boolean isSelected(ID id) {
		if (!org.apache.commons.lang3.StringUtils.isBlank(select)) {
			if ((","+select+",").indexOf(","+id+",") != -1) {
				return true;
			}
		}
		
		return false;
	}
	
	/**
	 * 获取自定义数据过滤器.
	 * @author Reamy(杨木江 yangmujiang@sohu.com)
	 * @date 2013-06-20  11:21:47
	 */
	@SuppressWarnings("rawtypes")
	private JacksonFilterProvider getUserDataFilter(Class clazz) {
		JacksonFilterProvider filterProvider = new JacksonFilterProvider();
		
		if (!org.apache.commons.lang3.StringUtils.isBlank(userData)) {
			filterProvider.addFilter(clazz,"in",userData.split(","));
		}
		
		return filterProvider;
	}
	
	/**
	 * 从实体中获取节点名称及是否存在子节点属性.
	 * @author Reamy(杨木江 yangmujiang@sohu.com)
	 * @date 2013-06-07  17:49:31
	 */
	private void findPropertys(T bean) {
		String[] inPropertys = getFilter().getInPropertys(FilterHelper.getFilterId(bean.getClass()));
		
		if (inPropertys != null && inPropertys.length > 0) {
			textAttrName = inPropertys[0];
			
			if (inPropertys.length > 1) {
				childAttrName = inPropertys[1];
			}
			if (inPropertys.length > 4) {
                iconAttrName = new String[]{inPropertys[2], inPropertys[3], inPropertys[4]};
            }
		}
		
		if (textAttrName == null || "".equals(textAttrName.trim())){
			throw new BusinessException("必须使用F_in指定实体中的那个属性用来作为数节点的显示名称");
		}
	}

	/**
	 * 设置自定义属性.
	 * @author Reamy(杨木江 yangmujiang@sohu.com)
	 * @date 2013-06-20  10:40:00
	 */
	public void setUserData(String userData) {
		this.userData = userData;
	}

	/**
	 * 设置默认展开.
	 * @author Reamy(杨木江 yangmujiang@sohu.com)
	 * @date 2013-06-20  10:40:00
	 */
	public void setOpen(String open) {
		this.open = StringUtils.isBooleanTrue(open);
	}

	/**
	 * 设置选中的节点ID.
	 * @author Reamy(杨木江 yangmujiang@sohu.com)
	 * @date 2013-06-20  10:40:00
	 */
	public void setSelect(String select) {
		this.select = select;
	}

	/**
	 * 设置勾选的节点ID.
	 * @author Reamy(杨木江 yangmujiang@sohu.com)
	 * @date 2013-06-20  10:40:00
	 */
	public void setChecked(String checked) {
		this.checked = checked;
	}

	/**
	 * 设置默认没有子节点.
	 * @author Reamy(杨木江 yangmujiang@sohu.com)
	 * @date 2013-06-20  10:55:08
	 */
	public void setNoChild(String noChild) {
		this.noChild = StringUtils.isBooleanTrue(noChild);
	}
}
