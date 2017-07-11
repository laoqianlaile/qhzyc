package com.ces.config.dhtmlx.json.entity.appmanage;

public class DhtmlxForm {

    private String initJs;

    private String changeJs;

    private String beforeSaveJs;

    private String afterSaveJs;

    private String formJson;

    private String keptColumns;

    private String increaseColumns;

    private String inheritColumns;

    private String defaultValues;

    public DhtmlxForm(String formJson, String initJs, String changeJs, String beforeSaveJs, String afterSaveJs, String keptColumns, String increaseColumns,
            String inheritColumns, String defaultValues) {
        this.formJson = formJson;
        this.initJs = initJs;
        this.changeJs = changeJs;
        this.beforeSaveJs = beforeSaveJs;
        this.afterSaveJs = afterSaveJs;
        this.keptColumns = keptColumns;
        this.increaseColumns = increaseColumns;
        this.inheritColumns = inheritColumns;
        this.defaultValues = defaultValues;
    }

    public String getInitJs() {
        return initJs;
    }

    public void setInitJs(String initJs) {
        this.initJs = initJs;
    }

    public String getChangeJs() {
        return changeJs;
    }

    public void setChangeJs(String changeJs) {
        this.changeJs = changeJs;
    }

    public String getBeforeSaveJs() {
        return beforeSaveJs;
    }

    public void setBeforeSaveJs(String beforeSaveJs) {
        this.beforeSaveJs = beforeSaveJs;
    }

    public String getAfterSaveJs() {
        return afterSaveJs;
    }

    public void setAfterSaveJs(String afterSaveJs) {
        this.afterSaveJs = afterSaveJs;
    }

    public String getFormJson() {
        return formJson;
    }

    public void setFormJson(String formJson) {
        this.formJson = formJson;
    }

    public String getKeptColumns() {
        return keptColumns;
    }

    public void setKeptColumns(String keptColumns) {
        this.keptColumns = keptColumns;
    }

    public String getIncreaseColumns() {
        return increaseColumns;
    }

    public void setIncreaseColumns(String increaseColumns) {
        this.increaseColumns = increaseColumns;
    }

    public String getInheritColumns() {
        return inheritColumns;
    }

    public void setInheritColumns(String inheritColumns) {
        this.inheritColumns = inheritColumns;
    }

    public String getDefaultValues() {
        return defaultValues;
    }

    public void setDefaultValues(String defaultValues) {
        this.defaultValues = defaultValues;
    }

}
