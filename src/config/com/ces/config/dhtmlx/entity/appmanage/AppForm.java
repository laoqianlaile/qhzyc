package com.ces.config.dhtmlx.entity.appmanage;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.ces.xarch.core.entity.StringIDEntity;

@Entity
@Table(name = "T_XTPZ_APP_FORM")
public class AppForm extends StringIDEntity implements Cloneable{

    private static final long serialVersionUID = 2808135897397565590L;
    /** 表ID */
    private String tableId;
    /** 自定义构件ID 
     *  工作流表单时，为-1
     * */
    private String componentVersionId;
    /** 菜单ID 
     *  工作流表单时，为 WorkflowVersion.id
     * */
    private String menuId;
    /** 占用列数 */
    private Integer colspan = 2;
    /** 加粗：0-否 1-是 */
    private String border = "0";
    /** 打开方式：0-弹出式 1-嵌入式 **/
    private String type;

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

    public Integer getColspan() {
        return colspan;
    }

    public void setColspan(Integer colspan) {
        this.colspan = colspan;
    }

    public String getBorder() {
        return border;
    }

    public void setBorder(String border) {
        this.border = border;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public Object clone() {
        try {
            return super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        
        return null;
    }
    
}
