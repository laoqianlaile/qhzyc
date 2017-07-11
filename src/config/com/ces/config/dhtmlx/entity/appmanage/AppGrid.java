package com.ces.config.dhtmlx.entity.appmanage;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.ces.xarch.core.entity.StringIDEntity;

@Entity
@Table(name="T_XTPZ_APP_GRID")
public class AppGrid extends StringIDEntity {

    private static final long serialVersionUID = -1649542882385515851L;

    public static final int DBLCLICK_NO    = 0;
    public static final int DBLCLICK_UPDATE= 1;
    public static final int DBLCLICK_VIEW  = 2;

    public static final int ST_DATABASE = 0; // 数据库检索
    public static final int ST_INDEXLIB = 1; // 索引库检索
    
    /* 表ID.*/
    private String tableId;
    /* 模块ID.*/
    private String componentVersionId;
    /* 菜单ID.*/
    private String menuId;
    /* 用户ID.*/
    private String userId;
    /* 是否有序号列.*/
    private int hasRowNumber;
    /* 列表双击事件：0-无事件 1-修改 2-查看.*/
    private int dblclick = 0;
    /* 检索方式：0-数据库 1-索引库.*/
    private int searchType = 0;
    /* 是否启用翻页条 0：否 1：是；默认值：1*/
    private int pageable=1;
    /*是否自适应 0：否 1：是 ；；默认值：1*/
    private int adaptive=1;
    /*操作列位置 0：第一列；1：最后一列；默认值：1*/
    private int opeColPosition=1;
    /*操作列名称 默认值：操作*/
    private String opeColName="操作";
    /*操作列宽度 默认值：120*/
    private int opeColWidth = 120;
    /* 是否开启列表表头拖动或拖拽时，保存当前用户个性化设置：0-否 1-是.*/
    private int headerSetting = 1;
    //选择模式： 0--无; 1-单选; 2-多选;
    private int selectModel = 2; 
    /*编辑模式：1：单击；2：双击*/
    private int editModel = 1;
    
    public String getTableId() {
        return tableId;
    }
    public void setTableId(String tableId) {
        this.tableId = tableId;
    }
    public String getComponentVersionId() {
        return componentVersionId;
    }
    public void setComponentVersionId(String componentVersionId) {
        this.componentVersionId = componentVersionId;
    }
    public String getMenuId() {
        return menuId;
    }
    public void setMenuId(String menuId) {
        this.menuId = menuId;
    }
    public String getUserId() {
        return userId;
    }
    public void setUserId(String userId) {
        this.userId = userId;
    }
    public int getHasRowNumber() {
        return hasRowNumber;
    }
    public void setHasRowNumber(int hasRowNumber) {
        this.hasRowNumber = hasRowNumber;
    }
    public int getDblclick() {
        return dblclick;
    }
    public void setDblclick(int dblclick) {
        this.dblclick = dblclick;
    }
	public int getSearchType() {
		return searchType;
	}
	public void setSearchType(int searchType) {
		this.searchType = searchType;
	}
	public int getPageable() {
		return pageable;
	}
	public void setPageable(int pageable) {
		this.pageable = pageable;
	}
	public int getAdaptive() {
		return adaptive;
	}
	public void setAdaptive(int adaptive) {
		this.adaptive = adaptive;
	}
	public int getOpeColPosition() {
		return opeColPosition;
	}
	public void setOpeColPosition(int opeColPosition) {
		this.opeColPosition = opeColPosition;
	}
	public String getOpeColName() {
		return opeColName;
	}
	public void setOpeColName(String opeColName) {
		this.opeColName = opeColName;
	}
	public int getOpeColWidth() {
		return opeColWidth;
	}
	public void setOpeColWidth(int opeColWidth) {
		this.opeColWidth = opeColWidth;
	}
	public int getHeaderSetting() {
		return headerSetting;
	}
	public void setHeaderSetting(int headerSetting) {
		this.headerSetting = headerSetting;
	}
	public int getSelectModel() {
		return selectModel;
	}
	public void setSelectModel(int selectModel) {
		this.selectModel = selectModel;
	}
	public int getEditModel() {
		return editModel;
	}
	public void setEditModel(int editModel) {
		this.editModel = editModel;
	}
}
