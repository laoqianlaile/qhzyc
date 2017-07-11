package com.ces.config.dhtmlx.json.entity.appmanage;

import java.util.ArrayList;
import java.util.List;

public class DhtmlxGrid {
    
    private String tableName = "";

    private List<String> headers = new ArrayList<String>();
    
    private List<String> widths  = new ArrayList<String>();
    
    private List<String> types   = new ArrayList<String>();
    
    private List<String> aligns  = new ArrayList<String>();
    
    private List<String> columns = new ArrayList<String>();
    
    private List<String> orders  = new ArrayList<String>();
    
    private List<String> datatypes = new ArrayList<String>();
    
    private List<String> codetypes = new ArrayList<String>();
    
    private List<String> columnIds = new ArrayList<String>();
    
    private List<String> urls      = new ArrayList<String>();
    
    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public List<String> getHeaders() {
        return headers;
    }

    public void setHeaders(List<String> headers) {
        this.headers = headers;
    }

    public List<String> getWidths() {
        return widths;
    }

    public void setWidths(List<String> widths) {
        this.widths = widths;
    }

    public List<String> getTypes() {
        return types;
    }

    public void setTypes(List<String> types) {
        this.types = types;
    }

    public List<String> getAligns() {
        return aligns;
    }

    public void setAligns(List<String> aligns) {
        this.aligns = aligns;
    }
    
    public List<String> getColumns() {
        return columns;
    }

    public void setColumns(List<String> columns) {
        this.columns = columns;
    }

    public List<String> getOrders() {
        return orders;
    }

    public void setOrders(List<String> orders) {
        this.orders = orders;
    }

    public List<String> getDatatypes() {
        return datatypes;
    }

    public void setDatatypes(List<String> datatypes) {
        this.datatypes = datatypes;
    }

    public List<String> getCodetypes() {
        return codetypes;
    }

    public void setCodetypes(List<String> codetypes) {
        this.codetypes = codetypes;
    }

	public List<String> getColumnIds() {
		return columnIds;
	}

	public void setColumnIds(List<String> columnIds) {
		this.columnIds = columnIds;
	}

	public List<String> getUrls() {
        return urls;
    }

    public void setUrls(List<String> urls) {
        this.urls = urls;
    }

    public class UserData {
        
    }
    
}
