package com.ces.config.dhtmlx.entity.component;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.ces.xarch.core.entity.StringIDEntity;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * 构件版本实体类
 * 
 * @author wanglei
 * @date 2013-07-22
 */
@Entity
@Table(name = "T_XTPZ_COMPONENT_VERSION")
public class ComponentVersion extends StringIDEntity {

    private static final long serialVersionUID = 7212061828730979547L;

    /** * 构件版本号 */
    private String version;

    /** * 前台技术[dhtmlx/coral40] */
    private String views;

    /** * 访问构件入口 */
    private String url;

    /** * 构件说明 */
    private String remark;

    /** * 构件分类ID */
    private String areaId;

    /** * 组合构件分类ID */
    private String assembleAreaId;

    /** * 构件存储路径 */
    private String path;

    /** * 构件导入时间 */
    private Date importDate;

    /** * 是否打包：0-未打包 1-打包 */
    private String isPackage;

    /** * 构件 */
    private Component component;

    /** * 绑定系统参数（0-未完成、1-完成、2-无需绑定） */
    private String systemParamConfig;

    /** * 是否应用到本系统 */
    private String isSystemUsed;

    /** * 构件打包时间 */
    private String packageTime;

    /** * 打开构件前调用的js方法 */
    private String beforeClickJs;

    /** * 按钮项（用来标记是否出现在构件组装中按钮的选择构件下拉框中） */
    private String buttonUse;

    /** * 菜单项（用来标记是否出现在菜单的选择构件下拉框中） */
    private String menuUse;

    /** * 存储构件分类路径 */
    private String areaPath;

    /** * 存储组合构件分类路径 */
    private String assembleAreaPath;

    private List<ComponentClass> componentClassList;

    /** * 构件中的Jars */
    private List<ComponentJar> componentJarList;

    /** * 以下属性是组合构件导入时使用的 */
    /** * 构件中的classes */
    private List<Map<String, String>> classMapList;

    /** * 构件中的Jars */
    private List<Map<String, String>> jarMapList;

    /** * 构件中的预留区 */
    private List<Map<String, Object>> reserveZoneMapList;

    /** * 构件中的系统参数 */
    private List<Map<String, String>> systemParamMapList;

    /** * 构件中的自身参数 */
    private List<Map<String, String>> selfParamMapList;

    /** * 构件中的输入参数 */
    private List<Map<String, String>> inputParamMapList;

    /** * 构件中的输出参数 */
    private List<Map<String, String>> outputParamMapList;

    /** * 构件中的方法 */
    private List<Map<String, Object>> functionMapList;

    /** * 构件中的回调函数 */
    private List<Map<String, Object>> callbackMapList;

    /** * 构件中的按钮 */
    private List<Map<String, String>> buttonMapList;

    /** * 构件中的表 */
    private List<Map<String, Object>> tableMapList;

    /** * 构件中的表字段关系 */
    private List<Map<String, String>> componentTableColumnRelationMapList;

    /** * 构件与公用关系 */
    private List<Map<String, String>> commonComponentRelationMapList;

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getViews() {
        return views;
    }

    public void setViews(String views) {
        this.views = views;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getAreaId() {
        return areaId;
    }

    public void setAreaId(String areaId) {
        this.areaId = areaId;
    }

    public String getAssembleAreaId() {
        return assembleAreaId;
    }

    public void setAssembleAreaId(String assembleAreaId) {
        this.assembleAreaId = assembleAreaId;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    @JsonFormat(pattern = "yyyy-MM-dd")
    public Date getImportDate() {
        return importDate;
    }

    public void setImportDate(Date importDate) {
        this.importDate = importDate;
    }

    public String getIsPackage() {
        return isPackage;
    }

    public void setIsPackage(String isPackage) {
        this.isPackage = isPackage;
    }

    @ManyToOne
    @JoinColumn(name = "COMPONENT_ID")
    public Component getComponent() {
        return component;
    }

    public void setComponent(Component component) {
        this.component = component;
    }

    public String getSystemParamConfig() {
        return systemParamConfig;
    }

    public void setSystemParamConfig(String systemParamConfig) {
        this.systemParamConfig = systemParamConfig;
    }

    public String getIsSystemUsed() {
        return isSystemUsed;
    }

    public void setIsSystemUsed(String isSystemUsed) {
        this.isSystemUsed = isSystemUsed;
    }

    public String getPackageTime() {
        return packageTime;
    }

    public void setPackageTime(String packageTime) {
        this.packageTime = packageTime;
    }

    public String getBeforeClickJs() {
        return beforeClickJs;
    }

    public void setBeforeClickJs(String beforeClickJs) {
        this.beforeClickJs = beforeClickJs;
    }

    public String getButtonUse() {
        return buttonUse;
    }

    public void setButtonUse(String buttonUse) {
        this.buttonUse = buttonUse;
    }

    public String getMenuUse() {
        return menuUse;
    }

    public void setMenuUse(String menuUse) {
        this.menuUse = menuUse;
    }

    public String getAreaPath() {
        return areaPath;
    }

    public void setAreaPath(String areaPath) {
        this.areaPath = areaPath;
    }

    public String getAssembleAreaPath() {
        return assembleAreaPath;
    }

    public void setAssembleAreaPath(String assembleAreaPath) {
        this.assembleAreaPath = assembleAreaPath;
    }

    @JsonIgnore
    @Transient
    public List<ComponentClass> getComponentClassList() {
        return componentClassList;
    }

    public void setComponentClassList(List<ComponentClass> componentClassList) {
        this.componentClassList = componentClassList;
    }

    @JsonIgnore
    @Transient
    public List<ComponentJar> getComponentJarList() {
        return componentJarList;
    }

    public void setComponentJarList(List<ComponentJar> componentJarList) {
        this.componentJarList = componentJarList;
    }

    @JsonIgnore
    @Transient
    public List<Map<String, String>> getClassMapList() {
        return classMapList;
    }

    public void setClassMapList(List<Map<String, String>> classMapList) {
        this.classMapList = classMapList;
    }

    @JsonIgnore
    @Transient
    public List<Map<String, String>> getJarMapList() {
        return jarMapList;
    }

    public void setJarMapList(List<Map<String, String>> jarMapList) {
        this.jarMapList = jarMapList;
    }

    @JsonIgnore
    @Transient
    public List<Map<String, Object>> getReserveZoneMapList() {
        return reserveZoneMapList;
    }

    public void setReserveZoneMapList(List<Map<String, Object>> reserveZoneMapList) {
        this.reserveZoneMapList = reserveZoneMapList;
    }

    @JsonIgnore
    @Transient
    public List<Map<String, String>> getSystemParamMapList() {
        return systemParamMapList;
    }

    public void setSystemParamMapList(List<Map<String, String>> systemParamMapList) {
        this.systemParamMapList = systemParamMapList;
    }

    @JsonIgnore
    @Transient
    public List<Map<String, String>> getSelfParamMapList() {
        return selfParamMapList;
    }

    public void setSelfParamMapList(List<Map<String, String>> selfParamMapList) {
        this.selfParamMapList = selfParamMapList;
    }

    @JsonIgnore
    @Transient
    public List<Map<String, String>> getInputParamMapList() {
        return inputParamMapList;
    }

    public void setInputParamMapList(List<Map<String, String>> inputParamMapList) {
        this.inputParamMapList = inputParamMapList;
    }

    @JsonIgnore
    @Transient
    public List<Map<String, String>> getOutputParamMapList() {
        return outputParamMapList;
    }

    public void setOutputParamMapList(List<Map<String, String>> outputParamMapList) {
        this.outputParamMapList = outputParamMapList;
    }

    @JsonIgnore
    @Transient
    public List<Map<String, Object>> getFunctionMapList() {
        return functionMapList;
    }

    public void setFunctionMapList(List<Map<String, Object>> functionMapList) {
        this.functionMapList = functionMapList;
    }

    @JsonIgnore
    @Transient
    public List<Map<String, Object>> getCallbackMapList() {
        return callbackMapList;
    }

    public void setCallbackMapList(List<Map<String, Object>> callbackMapList) {
        this.callbackMapList = callbackMapList;
    }

    @JsonIgnore
    @Transient
    public List<Map<String, String>> getButtonMapList() {
        return buttonMapList;
    }

    public void setButtonMapList(List<Map<String, String>> buttonMapList) {
        this.buttonMapList = buttonMapList;
    }

    @JsonIgnore
    @Transient
    public List<Map<String, Object>> getTableMapList() {
        return tableMapList;
    }

    public void setTableMapList(List<Map<String, Object>> tableMapList) {
        this.tableMapList = tableMapList;
    }

    @JsonIgnore
    @Transient
    public List<Map<String, String>> getComponentTableColumnRelationMapList() {
        return componentTableColumnRelationMapList;
    }

    public void setComponentTableColumnRelationMapList(List<Map<String, String>> componentTableColumnRelationMapList) {
        this.componentTableColumnRelationMapList = componentTableColumnRelationMapList;
    }

    @JsonIgnore
    @Transient
    public List<Map<String, String>> getCommonComponentRelationMapList() {
        return commonComponentRelationMapList;
    }

    public void setCommonComponentRelationMapList(List<Map<String, String>> commonComponentRelationMapList) {
        this.commonComponentRelationMapList = commonComponentRelationMapList;
    }

    @Transient
    public String getModuleId() {
        if (null != url) {
            int start = url.indexOf("?");
            String paramStr = url.substring(start + 1);
            String[] paramArr = paramStr.split("&");
            String[] items = null;
            for (int i = 0, len = paramArr.length; i < len; i++) {
                items = paramArr[i].split("=");
                if ("P_moduleId".equals(items[0]))
                    return items[1];
            }
        }
        return null;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        final ComponentVersion other = (ComponentVersion) obj;
        if (getId() == null) {
            if (other.getId() != null)
                return false;
        } else if (!getId().equals(other.getId()))
            return false;
        return true;
    }
}
