/** 
 * <p>描述: TODO(用一句话描述该文件做什么)</p>
 * <p>公司：上海中信信息发展股份有限公司</p>
 * @author qiucs
 * @date 2013-6-19 上午10:20:54   
 * @version 1.0.2013    
 */ 

package com.ces.config.dhtmlx.entity.appmanage;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;
import org.apache.struts2.ServletActionContext;

import com.ces.config.dhtmlx.utils.DhtmlxCommonUtil;
import com.ces.config.utils.ConstantVar;
import com.ces.config.utils.WorkflowUtil;
import com.ces.xarch.core.entity.StringIDEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "T_XTPZ_APP_DEFINE")
public class AppDefine extends StringIDEntity {

    private static final long serialVersionUID = 4243140696047351266L;
    /** “所有构件”使用的ID值/菜单ID默认值.*/
    public static final String DEFAULT_DEFINE_ID = "-1";
    /** 已定义.*/
    public static final String DEFINE_YES = "1";
    /** 未定义.*/
    public static final String DEFINE_NO  = "0";
    
    private String tableId;
    
    private String componentVersionId;
    
    private String menuId;//默认为-1，表示按构件配置，否则为按菜单配置
    
    private String userId;
    
    private String name;
    
    private String searched = DEFINE_NO;
    
    private String columned = DEFINE_NO;
    
    private String sorted = DEFINE_NO;
    
    private String gridButtoned = DEFINE_NO;
    
    private String formed = DEFINE_NO;
    
    private String formButtoned = DEFINE_NO;
    
    private String filtered = DEFINE_NO;
    
    private String reported = DEFINE_NO;
    // 临时处理用的，不保存数据库
    private String classification;
    
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

    @Transient
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @JsonIgnore
    public String getSearched() {
        return searched;
    }
    
    @Transient
    public String getSearchedAlias() {
        return getDefineStatusAlias(searched, TYPE_SEARCH);
    }  

    public void setSearched(String searched) {
        this.searched = searched;
    }

    @JsonIgnore
    public String getColumned() {
        return columned;
    }
    
    @Transient
    public String getColumnedAlias() {
        return getDefineStatusAlias(columned, TYPE_COLUMN);
    }  

    public void setColumned(String columned) {
        this.columned = columned;
    }
    
    @JsonIgnore
    public String getSorted() {
        return sorted;
    }
    
    @Transient
    public String getSortedAlias() {
        return getDefineStatusAlias(sorted, TYPE_SORT);
    }

    public void setSorted(String sorted) {
        this.sorted = sorted;
    }

    @JsonIgnore
    public String getGridButtoned() {
        return gridButtoned;
    }
    
    @Transient
    public String getGridButtonedAlias() {
        /*if (ConstantVar.TableClassification.VIEW.equals(getClassification())) {
            return getDefineStatusAlias(DEFINE_STAUTS_NON, TYPE_GRID_BUTTON);
        }*/
        return getDefineStatusAlias(gridButtoned, TYPE_GRID_BUTTON);
    }

    public void setGridButtoned(String gridButtoned) {
        this.gridButtoned = gridButtoned;
    }

    @JsonIgnore
    public String getFormed() {
        return formed;
    }

    @Transient
    public String getFormedAlias() {
        if (ConstantVar.TableClassification.VIEW.equals(getClassification())) {
            return getDefineStatusAlias(DEFINE_STAUTS_NON, TYPE_FORM);
        }
        return getDefineStatusAlias(formed, TYPE_FORM);
    }

    public void setFormed(String formed) {
        this.formed = formed;
    }

    @JsonIgnore
    public String getFormButtoned() {
        return formButtoned;
    }
    
    @Transient
    public String getFormButtonedAlias() {
        if (ConstantVar.TableClassification.VIEW.equals(getClassification())) {
            return getDefineStatusAlias(DEFINE_STAUTS_NON, TYPE_FORM_BUTTON);
        }
        return getDefineStatusAlias(formButtoned, TYPE_FORM_BUTTON);
    }

    public void setFormButtoned(String formButtoned) {
        this.formButtoned = formButtoned;
    }
    
    @JsonIgnore
    public String getFiltered() {
        return filtered;
    }
    
    @Transient
    public String getFilteredAlias() {
        /*if (ConstantVar.TableClassification.VIEW.equals(getClassification())) {
            return getDefineStatusAlias(DEFINE_STAUTS_NON, TYPE_FILTER);
        }*/
        return getDefineStatusAlias(filtered, TYPE_FILTER);
    }

    public void setReported(String reported) {
        this.reported = reported;
    }
    
    @JsonIgnore
    public String getReported() {
        return reported;
    }
    
    @Transient
    public String getReportedAlias() {
        /*if (ConstantVar.TableClassification.VIEW.equals(getClassification())) {
            return getDefineStatusAlias(DEFINE_STAUTS_NON, TYPE_FILTER);
        }*/
        return getDefineStatusAlias(reported, TYPE_REPORT);
    }

    public void setFiltered(String filtered) {
        this.filtered = filtered;
    }
    
    @Transient
    @JsonIgnore
    public String getClassification() {
        return classification;
    }

    public void setClassification(String classification) {
        this.classification = classification;
    }
    
    public void disable() {
    	setColumned(DEFINE_STAUTS_NON);
    	setSearched(DEFINE_STAUTS_NON);
    	setSorted(DEFINE_STAUTS_NON);
    	setFiltered(DEFINE_STAUTS_NON);
    	setFormed(DEFINE_STAUTS_NON);
    	setReported(DEFINE_STAUTS_NON);
    	setGridButtoned(DEFINE_STAUTS_NON);
    	setFormButtoned(DEFINE_STAUTS_NON);
    }

    /** 所有模块ID值 */
    private static final String DEFINE_ALL_MENU_ID = "-1";
    /** 设置状态值：不可设置 */
    private static final String DEFINE_STAUTS_NON = "-1";
    /** 设置状态值：已设置/个性设置 */
    private static final String DEFINE_STAUTS_YES = "1";
    /** 设置状态值(-1)描述：不可设置 */
    private static final String DEFINE_STAUTS_ALIAS_NON = "不可设置";
    /** 所有模块 设置状态值(1)描述：已设置 */
    private static final String DEFINE_STAUTS_ALIAS_YES_ALL = "已设置";
    /** 所有模块 设置状态值(0)描述：未设置 */
    private static final String DEFINE_STAUTS_ALIAS_NOT_ALL = "未设置";
    /** 设置状态值(1)描述：个性设置 */
    private static final String DEFINE_STAUTS_ALIAS_YES_ONE = "个性设置";
    /** 设置状态值(0)描述：默认设置 */
    private static final String DEFINE_STAUTS_ALIAS_NOT_ONE = "默认设置";
    /** 应用定义类别：0-检索 */
    public static final String TYPE_SEARCH = "0";
    /** 应用定义类别：1-字段 */
    public static final String TYPE_COLUMN = "1";
    /** 应用定义类别：2-排序 */
    public static final String TYPE_SORT   = "2";
    /** 应用定义类别：3-列表按钮 */
    public static final String TYPE_GRID_BUTTON = "3";
    /** 应用定义类别：4-界面 */
    public static final String TYPE_FORM   = "4";
    /** 应用定义类别：5-界面按钮 */
    public static final String TYPE_FORM_BUTTON = "5";
    /** 应用定义类别：6-过滤条件 */
    public static final String TYPE_FILTER = "6";
    /** 应用定义类别：7-报表 */
    public static final String TYPE_REPORT = "7";
    
    /**
     * <p>描述: TODO(这里用一句话描述这个方法的作用)</p>
     * @param  stutas
     * @param  type  
     *         0-检索，1-字段，2-排序，3-列表按钮，4-界面，5-表单按钮，6-过滤条件
     * @return String    返回类型   
     * @throws
     */
    private String getDefineStatusAlias(String status, String type) {
    	String statusAlias = DEFINE_STAUTS_ALIAS_NON;
    	String path = ServletActionContext.getRequest().getContextPath() + DhtmlxCommonUtil.DHX_FOLDER + "/common/images/";
        String img = path + "deny.gif";
        if (DEFINE_ALL_MENU_ID.equals(componentVersionId) ) {
            if (  TYPE_FILTER.equals(type) || TYPE_GRID_BUTTON.equals(type) || TYPE_FORM_BUTTON.equals(type)) {
                return img + "^alt " + statusAlias;
            }
            if (DEFINE_STAUTS_YES.equals(status)) {                
            	statusAlias =  DEFINE_STAUTS_ALIAS_YES_ALL;
                img = path + "yes.gif";
            } else if (!DEFINE_STAUTS_NON.equals(status)) {                
            	statusAlias =  DEFINE_STAUTS_ALIAS_NOT_ALL;
                img = path + "no_def.gif";
            } else {
                return img + "^alt " + statusAlias;
            }
        } else if (TYPE_FORM_BUTTON.equals(type) && 
        		(WorkflowUtil.Box.complete.equals(componentVersionId) ||
        				WorkflowUtil.Box.hasread.equals(componentVersionId))) {
        	return img + "^alt " + statusAlias;
        } else {
            if (DEFINE_STAUTS_YES.equals(status)) {
            	statusAlias =  DEFINE_STAUTS_ALIAS_YES_ONE;
                img = path + "yes.gif";
            } else if (!DEFINE_STAUTS_NON.equals(status)) {
                if (   TYPE_FILTER.equals(type)) { // type is button or filter and status is 0 
                	statusAlias =  DEFINE_STAUTS_ALIAS_NOT_ALL;
                    img = path + "no_def.gif";
                } else {
                	statusAlias =  DEFINE_STAUTS_ALIAS_NOT_ONE;
                    img = path + "no_def.gif";
                }
            } else {
                return img + "^alt " + statusAlias;
            }
        }
        return img + "^alt " + statusAlias +"^javascript:opencfg(" + type + "," + "\"" + componentVersionId + "\")^_self";
    }
}
