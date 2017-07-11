/**
 * <p>Copyright:Copyright(c) 2013</p>
 * <p>Company:上海中信信息发展股份有限公司</p>
 * <p>包名:com.ces.xarch.core.web.frame.dhtmlx.datamodel.tree</p>
 * <p>文件名:TreeItem.java</p>
 * <p>类更新历史信息</p>
 * @todo Reamy(杨木江 yangmujiang@sohu.com) 创建于 2013-06-07 17:18:43
 */
package com.ces.xarch.frames.dhtmlx.datamodel.tree;

import java.io.Serializable;
import java.util.List;

/**
 * Tree节点描述Bean.
 * <p>描述:存储Tree单个节点信息</p>
 * <p>Company:上海中信信息发展股份有限公司</p>
 * @author Reamy(杨木江 yangmujiang@sohu.com)
 * @date 2013-06-07  17:18:43
 * @version 1.0.2013.0607
 * 
 * 复写 xarch-dhtmlx-1.0.jar中TreeItem.class
 */
class TreeItem<ID extends Serializable> implements Serializable {
	private static final long serialVersionUID = -3044389757397529467L;

	/** 节点ID. */
	private ID id;
	/** 节点名称. */
	private String text;
	/** 是否有子节点. */
	private boolean child;
	/** 自定义属性集合. */
	private List<TreeUserData> userdata;
	/** 节点是否处于选中状态. */
	private boolean select;
	/** 节点是否勾选. */
	private boolean checked;
	/** 是否打开节点. */
	private boolean open;
	
	private String im0;
    
    private String im1;
    
    private String im2;
	/**
	 * 构造函数.
	 * @param id 节点ID
	 * @param text 节点名称
	 * @param child 是否有子节点
	 * @param userdata 自定义属性
	 * @author Reamy(杨木江 yangmujiang@sohu.com)
	 * @date 2013-06-07  17:23:40
	 */
	public TreeItem(ID id, String text, boolean child, List<TreeUserData> userdata) {
		this.id = id;
		this.text = text;
		this.child = child;
		this.userdata = userdata;
	}

	/**
	 * 获取节点ID.
	 * @author Reamy(杨木江 yangmujiang@sohu.com)
	 * @date 2013-06-07  17:20:48
	 */
	public ID getId() {
		return id;
	}

	/**
	 * 获取节点名称.
	 * @author Reamy(杨木江 yangmujiang@sohu.com)
	 * @date 2013-06-07  17:20:48
	 */
	public String getText() {
		return text;
	}

	/**
	 * 获取是否有子节点.
	 * @author Reamy(杨木江 yangmujiang@sohu.com)
	 * @date 2013-06-07  17:20:48
	 */
	public boolean isChild() {
		return child;
	}

	/**
	 * 获取自定义属性集合.
	 * @author Reamy(杨木江 yangmujiang@sohu.com)
	 * @date 2013-06-13  14:40:34
	 */
	public List<TreeUserData> getUserdata() {
		return userdata;
	}

	/**
	 * 获取选中状态.
	 * @author Reamy(杨木江 yangmujiang@sohu.com)
	 * @date 2013-06-20  10:26:31
	 */
	public boolean isSelect() {
		return select;
	}

	/**
	 * 设置选中状态.
	 * @author Reamy(杨木江 yangmujiang@sohu.com)
	 * @date 2013-06-20  10:26:31
	 */
	public void setSelect(boolean select) {
		this.select = select;
	}

	/**
	 * 获取勾选状态.
	 * @author Reamy(杨木江 yangmujiang@sohu.com)
	 * @date 2013-06-20  10:26:31
	 */
	public boolean isChecked() {
		return checked;
	}

	/**
	 * 设置勾选状态.
	 * @author Reamy(杨木江 yangmujiang@sohu.com)
	 * @date 2013-06-20  10:26:31
	 */
	public void setChecked(boolean checked) {
		this.checked = checked;
	}

	/**
	 * 获取打开状态.
	 * @author Reamy(杨木江 yangmujiang@sohu.com)
	 * @date 2013-06-20  10:26:31
	 */
	public boolean isOpen() {
		return open;
	}

	/**
	 * 设置打开状态.
	 * @author Reamy(杨木江 yangmujiang@sohu.com)
	 * @date 2013-06-20  10:26:31
	 */
	public void setOpen(boolean open) {
		this.open = open;
	}

    public String getIm0() {
        return im0;
    }

    public void setIm0(String im0) {
        this.im0 = im0;
    }

    public String getIm1() {
        return im1;
    }

    public void setIm1(String im1) {
        this.im1 = im1;
    }

    public String getIm2() {
        return im2;
    }

    public void setIm2(String im2) {
        this.im2 = im2;
    }
	
}
