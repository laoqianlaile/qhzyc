package com.ces.config.dhtmlx.entity.component;

import java.util.List;
import java.util.Map;

/**
 * 构件配置文件对应的实体类
 * 
 * @author wanglei
 * @date 2013-07-22
 */
public class ComponentConfig {

    /** * 构件标识符 */
    private String code;

    /** * 构件名称 */
    private String name;

    /** * 构件别名 */
    private String alias;

    /** * 构件版本号 */
    private String version;

    /** * 构件类型 */
    private String type;

    /** * 前台技术[dhtmlx/ext] */
    private String views;

    /** * 构件访问入口 */
    private String url;

    /** * 所需jar包 */
    private List<String> jars;

    /** * class文件 */
    private List<String> classes;

    /** * 构件入参 */
    private List<ComponentInputParam> inputParams;

    /** * 构件出参 */
    private List<ComponentOutputParam> outputParams;

    /** * 预留区 */
    private List<ComponentReserveZone> reserveZones;

    /** * 构件前台JS方法 */
    private List<ComponentFunction> functions;

    /** * 构件前台JS方法(供页面构件关闭时使用) */
    private List<ComponentCallback> callbacks;

    /** * 构件自身参数配置 */
    private List<ComponentSelfParam> selfParams;

    /** * 构件相关的系统参数 */
    private List<ComponentSystemParameter> componentSystemParameters;

    /** * 关联的表 */
    private List<ComponentTable> componentTables;

    /** * 构件按钮 */
    private List<ComponentButton> buttons;

    /** * 构件说明 */
    private String remark;

    /** * 构件打包时间 */
    private String packageTime;

    /** * 上传后的构件包文件名称 */
    private String packageFileName;

    /** * 打开构件前调用的js方法 */
    private String beforeClickJs;

    /** * 自定义构件配置信息 */
    private Map<String, Object> selfDefineConfig;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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

    public List<String> getJars() {
        return jars;
    }

    public void setJars(List<String> jars) {
        this.jars = jars;
    }

    public List<String> getClasses() {
        return classes;
    }

    public void setClasses(List<String> classes) {
        this.classes = classes;
    }

    public List<ComponentInputParam> getInputParams() {
        return inputParams;
    }

    public void setInputParams(List<ComponentInputParam> inputParams) {
        this.inputParams = inputParams;
    }

    public List<ComponentOutputParam> getOutputParams() {
        return outputParams;
    }

    public void setOutputParams(List<ComponentOutputParam> outputParams) {
        this.outputParams = outputParams;
    }

    public List<ComponentReserveZone> getReserveZones() {
        return reserveZones;
    }

    public void setReserveZones(List<ComponentReserveZone> reserveZones) {
        this.reserveZones = reserveZones;
    }

    public List<ComponentFunction> getFunctions() {
        return functions;
    }

    public void setFunctions(List<ComponentFunction> functions) {
        this.functions = functions;
    }

    public List<ComponentCallback> getCallbacks() {
        return callbacks;
    }

    public void setCallbacks(List<ComponentCallback> callbacks) {
        this.callbacks = callbacks;
    }

    public List<ComponentSelfParam> getSelfParams() {
        return selfParams;
    }

    public void setSelfParams(List<ComponentSelfParam> selfParams) {
        this.selfParams = selfParams;
    }

    public List<ComponentSystemParameter> getComponentSystemParameters() {
        return componentSystemParameters;
    }

    public void setComponentSystemParameters(List<ComponentSystemParameter> componentSystemParameters) {
        this.componentSystemParameters = componentSystemParameters;
    }

    public List<ComponentTable> getComponentTables() {
        return componentTables;
    }

    public void setComponentTables(List<ComponentTable> componentTables) {
        this.componentTables = componentTables;
    }

    public List<ComponentButton> getButtons() {
        return buttons;
    }

    public void setButtons(List<ComponentButton> buttons) {
        this.buttons = buttons;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getPackageTime() {
        return packageTime;
    }

    public void setPackageTime(String packageTime) {
        this.packageTime = packageTime;
    }

    public String getPackageFileName() {
        return packageFileName;
    }

    public void setPackageFileName(String packageFileName) {
        this.packageFileName = packageFileName;
    }

    public String getBeforeClickJs() {
        return beforeClickJs;
    }

    public void setBeforeClickJs(String beforeClickJs) {
        this.beforeClickJs = beforeClickJs;
    }

    public Map<String, Object> getSelfDefineConfig() {
        return selfDefineConfig;
    }

    public void setSelfDefineConfig(Map<String, Object> selfDefineConfig) {
        this.selfDefineConfig = selfDefineConfig;
    }
}
