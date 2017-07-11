package com.ces.config.dhtmlx.entity.appmanage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.ces.config.utils.JsonUtil;
import com.ces.config.utils.StringUtil;
import com.ces.xarch.core.entity.StringIDEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.JsonNode;

/**
 * <p>描述: 模块构件布局</p>
 * <p>公司: 上海中信信息发展股份有限公司</p>
 * 
 * @author qiucs
 * @date 2014-2-20 下午2:32:28
 */
@Entity
@Table(name = "T_XTPZ_MODULE")
public class Module extends StringIDEntity {
    private static final long serialVersionUID = -4989586296811633396L;

    public static final String UI_CORAL = "1";

    public static final String UI_DHTMLX = "0";

    // 模块类型
    public static final String L_2U = "2U";

    public static final String L_2E = "2E";

    public static final String L_2E_S = "2E_S";
    
    public static final String L_1C = "1C";

    public static final String L_3L = "3L";

    public static final String L_3E = "3E";

    /** 类型 3-树构件 4-物理表构件 5-逻辑表构件 6-通用表构件* */
    private String type;

    /** 逻辑表组Code * */
    private String logicTableGroupCode;

    /** 布局类型: 2U-左右结构 2E-上下结构 1C-整张页面 3L-左上下结构 3E-上中下结构  2E_S-单列表查询区**/
    private String templateType;

    /** 构件名称 * */
    private String name;

    /** 构件类名 **/
    private String componentClassName;

    /** 如果templateType=1，则存树根节点ID；否则为空 **/
    private String treeId;

    /**
     * 布局信息，用json格式字符串存储，[[{tableId:'',type:'',thumbnail:'',sort:''}],…].
     * tableId: 表ID
     * type : 0-表单界面 1-列表界面 2-列表界面(含缩略图)
     * thumbnail : 0-页面打开时默认显示列表 1-页面打开时默认显示缩略图 （只有type=2时才有效）
     * sort : 0-不支持拖动排序 1-支持拖动排序（只有type=2或3时有效）
     **/
    private String areaLayout;

    /** 前台类型(0-dhtmlx 1-coral40) **/
    private String uiType = "0";

    /** 显示顺序 **/
    private Integer showOrder;

    /** 备注 **/
    private String remark;

    /** 对应构件版本ID **/
    private String componentVersionId;

    /** 构件访问URL **/
    private String componentUrl;

    /** 构件分类ID **/
    private String componentAreaId;

    /** 保存构件布局时是否重新配置构件的相关配置 */
    private String updateComponentConfig = "1";

    /********************* (辅助属性，不存在数据表中) ************************/
    /** 表1ID或逻辑表1Code **/
    private String table1Id;

    /** 区域1ID：0-表单界面 1-列表界面 2-列表界面(含缩略图) 3-查询区**/
    private String area1Id;
    
    /** 区域1默认折叠复选框，只有布局类型为单列表查询区时启用**/
    private String collapse1;

    /** 表1缩略图配置： 0-页面打开时默认显示列表 1-页面打开时默认显示缩略图 （只有type=2时才有效） **/
    private String thumbnail1;

    /** 表1拖动排序配置： 0-不支持拖动排序 1-支持拖动排序（只有type=2或3时有效） **/
    private String sort1;

    /** 区域1大小  **/
    private String area1Size;

    /** 表2ID或逻辑表2Code **/
    private String table2Id;

    /** 区域2ID：0-表单界面 1-列表界面 2-列表界面(含缩略图) **/
    private String area2Id;

    /** 表2缩略图配置： 0-页面打开时默认显示列表 1-页面打开时默认显示缩略图 （只有type=2时才有效） **/
    private String thumbnail2;

    /** 表2拖动排序配置： 0-不支持拖动排序 1-支持拖动排序（只有type=2或3时有效） **/
    private String sort2;

    /** 区域2大小  **/
    private String area2Size;

    /** 表3ID或逻辑表3Code **/
    private String table3Id;

    /** 区域3ID：0-表单界面 1-列表界面 2-列表界面(含缩略图) **/
    private String area3Id;

    /** 表3缩略图配置： 0-页面打开时默认显示列表 1-页面打开时默认显示缩略图 （只有type=2时才有效） **/
    private String thumbnail3;

    /** 表3拖动排序配置： 0-不支持拖动排序 1-支持拖动排序（只有type=2或3时有效） **/
    private String sort3;

    /** 区域3大小  **/
    private String area3Size;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getLogicTableGroupCode() {
        return logicTableGroupCode;
    }

    public void setLogicTableGroupCode(String logicTableGroupCode) {
        this.logicTableGroupCode = logicTableGroupCode;
    }

    public String getTemplateType() {
        return templateType;
    }

    public void setTemplateType(String templateType) {
        this.templateType = StringUtil.null2empty(templateType);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getComponentClassName() {
        return componentClassName;
    }

    public void setComponentClassName(String componentClassName) {
        this.componentClassName = componentClassName;
    }

    public String getTreeId() {
        return treeId;
    }

    public void setTreeId(String treeId) {
        this.treeId = treeId;
    }

    public String getAreaLayout() {
        return areaLayout;
    }

    public void setAreaLayout(String areaLayout) {
        this.areaLayout = areaLayout;
    }

    public String getUiType() {
        return uiType;
    }

    public void setUiType(String uiType) {
        this.uiType = uiType;
    }

    public Integer getShowOrder() {
        return showOrder;
    }

    public void setShowOrder(Integer showOrder) {
        this.showOrder = showOrder;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getComponentVersionId() {
        return componentVersionId;
    }

    public void setComponentVersionId(String componentVersionId) {
        this.componentVersionId = componentVersionId;
    }

    public String getComponentUrl() {
        return componentUrl;
    }

    public void setComponentUrl(String componentUrl) {
        this.componentUrl = componentUrl;
    }

    public String getComponentAreaId() {
        return componentAreaId;
    }

    public void setComponentAreaId(String componentAreaId) {
        this.componentAreaId = componentAreaId;
    }

    @Transient
    @JsonIgnore
    public String getUpdateComponentConfig() {
        return updateComponentConfig;
    }

    public void setUpdateComponentConfig(String updateComponentConfig) {
        this.updateComponentConfig = updateComponentConfig;
    }

    @Transient
    public String getTable1Id() {
        if (StringUtil.isNotEmpty(table1Id))
            return table1Id;
        if (StringUtil.isEmpty(templateType))
            return "";
        return obtainJsonValue(0, "tableId");
    }

    public void setTable1Id(String table1Id) {
        this.table1Id = StringUtil.null2empty(table1Id);
    }

    @Transient
    public String getArea1Id() {
        if (StringUtil.isNotEmpty(area1Id))
            return area1Id;
        if (StringUtil.isEmpty(templateType))
            return "";
        return obtainJsonValue(0, "type");
    }

    public void setArea1Id(String area1Id) {
        this.area1Id = StringUtil.null2empty(area1Id);
    }

    @Transient
    public String getCollapse1() {
    	if (StringUtil.isNotEmpty(collapse1))
            return collapse1;
        if (StringUtil.isEmpty(templateType))
            return "";
        return obtainJsonValue(0, "collapse1");
	}

	public void setCollapse1(String collapse1) {
		this.collapse1 = StringUtil.null2empty(collapse1);
	}

	@Transient
    public String getThumbnail1() {
        if (!"2".equals(getArea1Id()))
            return "";
        if (StringUtil.isNotEmpty(thumbnail1))
            return thumbnail1;
        return obtainJsonValue(0, "thumbnail");
    }

    public void setThumbnail1(String thumbnail1) {
        this.thumbnail1 = StringUtil.null2empty(thumbnail1);
    }

    @Transient
    public String getSort1() {
        if (!"1".equals(getArea1Id()) && !"2".equals(getArea1Id()))
            return "";
        if (StringUtil.isNotEmpty(sort1))
            return sort1;
        return obtainJsonValue(0, "sort");
    }

    public void setSort1(String sort1) {
        this.sort1 = sort1;
    }

    @Transient
    public String getArea1Size() {
    	// 1C:height; 2E:height; 2U: width; 3E:height; 3L:width;
    	if (L_1C.equals(templateType)) {
    		return "100%";
    	}
    	if (StringUtil.isNotEmpty(area1Size)) {
    		return area1Size;
    	}
        return obtainJsonValue(0, "size");
    }

    public void setArea1Size(String area1Size) {
        this.area1Size = area1Size;
    }

    @Transient
    public String getTable2Id() {
        if (StringUtil.isNotEmpty(table2Id))
            return table2Id;
        if (StringUtil.isEmpty(templateType) || L_1C.equals(templateType))
            return "";
        return obtainJsonValue(1, "tableId");
    }

    public void setTable2Id(String table2Id) {
        this.table2Id = StringUtil.null2empty(table2Id);
    }

    @Transient
    public String getArea2Id() {
        if (StringUtil.isNotEmpty(area2Id))
            return area2Id;
        if (StringUtil.isEmpty(templateType) || L_1C.equals(templateType))
            return "";
        return obtainJsonValue(1, "type");
    }

    public void setArea2Id(String area2Id) {
        this.area2Id = StringUtil.null2empty(area2Id);
    }

    @Transient
    public String getThumbnail2() {
        if (!"2".equals(getArea2Id()))
            return "";
        if (StringUtil.isNotEmpty(thumbnail2))
            return thumbnail2;
        return obtainJsonValue(1, "thumbnail");
    }

    public void setThumbnail2(String thumbnail2) {
        this.thumbnail2 = StringUtil.null2empty(thumbnail2);
    }

    @Transient
    public String getSort2() {
        if (!"1".equals(getArea2Id()) && !"2".equals(getArea2Id()))
            return "";
        if (StringUtil.isNotEmpty(sort2))
            return sort2;
        return obtainJsonValue(1, "sort");
    }

    public void setSort2(String sort2) {
        this.sort2 = sort2;
    }

    @Transient
    public String getArea2Size() {
    	// 2E/3E/3L
    	if (!L_2E.equals(templateType) && !L_2E_S.equals(templateType) && !L_3E.equals(templateType) && !L_3L.equals(templateType)) {
    		return "";
    	}
    	if (StringUtil.isNotEmpty(area2Size)) {
    		return area2Size;
    	}
        return obtainJsonValue(1, "size");
    }

    public void setArea2Size(String area2Size) {
        this.area2Size = area2Size;
    }

    @Transient
    public String getTable3Id() {
        if (StringUtil.isNotEmpty(table3Id))
            return table3Id;
        if (L_3L.equals(templateType) || L_3E.equals(templateType))
        	return obtainJsonValue(2, "tableId");
        return "";
    }

    public void setTable3Id(String table3Id) {
        this.table3Id = StringUtil.null2empty(table3Id);
    }

    @Transient
    public String getArea3Id() {
        if (StringUtil.isNotEmpty(area3Id))
            return area3Id;
        if (L_3L.equals(templateType) || L_3E.equals(templateType))
        	return obtainJsonValue(2, "type");
        return "";
    }

    public void setArea3Id(String area3Id) {
        this.area3Id = StringUtil.null2empty(area3Id);
    }

    @Transient
    public String getThumbnail3() {
        if (!"2".equals(getArea3Id()))
            return "";
        if (StringUtil.isNotEmpty(thumbnail3))
            return thumbnail3;
        return obtainJsonValue(2, "thumbnail");
    }

    public void setThumbnail3(String thumbnail3) {
        this.thumbnail3 = StringUtil.null2empty(thumbnail3);
    }

    @Transient
    public String getSort3() {
        if (!"1".equals(getArea3Id()) && !"2".equals(getArea3Id()))
            return "";
        if (StringUtil.isNotEmpty(sort3))
            return sort3;
        return obtainJsonValue(2, "sort");
    }

    public void setSort3(String sort3) {
        this.sort3 = sort3;
    }

    @Transient
    public String getArea3Size() {
    	// 3E/3L
    	if (!L_3E.equals(templateType) && !L_3L.equals(templateType)) {
    		return "";
    	}
    	if (StringUtil.isNotEmpty(area3Size)) {
    		return area3Size;
    	}
        return obtainJsonValue(2, "size");
    }

    public void setArea3Size(String area3Size) {
        this.area3Size = area3Size;
    }
    
    private String getAreaSize(int index) {
    	if (1 == index) {
    		if (L_1C.equals(templateType)) {
        		return "100%";
        	}
    		return this.area1Size;
    	} else if (2 == index) {
    		if (!L_2E.equals(templateType) && !L_2E_S.equals(templateType) && !L_3E.equals(templateType) && !L_3L.equals(templateType)) {
        		return "";
        	}
    		return this.area2Size;
    	} else if (3 == index) {
    		if (!L_3E.equals(templateType) && !L_3L.equals(templateType)) {
        		return "";
        	}
    		return this.area3Size;
    	}
    	return "";
    }

    @Transient
    @JsonIgnore
    public boolean isCoralUI() {
        return UI_CORAL.equals(getUiType());
    }

    @Transient
    @JsonIgnore
    public boolean isDhtmlxUI() {
        return UI_DHTMLX.equals(getUiType());
    }

    /**
     * 是表ID和areaId对应的JsonNode
     * table1Id/table2Id/table3Id对应JsonNode中的tableId属性
     * area1Id/area2Id/area3Id 对应JsonNode中的type属性
     */
    private JsonNode node = null;

    /**
     * 把布局配置信息转成JsonNode
     * 
     * @return JsonNode
     */
    public JsonNode layout2node() {
        String str = getAreaLayout();
        if (StringUtil.isEmpty(str))
            return null;
        return JsonUtil.json2node(str);
    }

    /**
     * 获取JsonNode中对应属性值
     * 
     * @return String
     */
    private String obtainJsonValue(int index, String prop) {
        if (null == node)
            node = layout2node();
        if (null != node && node.size() > index) {
            JsonNode distNode = node.get(index);
            JsonNode jsonNode = distNode.get(0).get(prop);
            if (jsonNode == null)
                return "";
            return jsonNode.asText();
        }
        return "";
    }

    /**
     * 更新AreaLayout
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    public void updateAreaLayout() {
        if ("4".equals(type) || "5".equals(type) || "6".equals(type)) {
            List list = new ArrayList();
            List temp = null;
            Map<String, String> map = null;
            if (L_1C.equals(templateType)) {
                temp = new ArrayList();
                map = new HashMap<String, String>();
                map.put("tableId", getTable1Id());
                map.put("type", getArea1Id());
                map.put("thumbnail", getThumbnail1());
                map.put("sort", getSort1());
                map.put("size", getAreaSize(1));
                temp.add(map);
                list.add(temp);
            } else if (L_2E.equals(templateType) || L_2E_S.equals(templateType)) {
                temp = new ArrayList();
                map = new HashMap<String, String>();
                map.put("tableId", getTable1Id());
                map.put("type", getArea1Id());
                if(L_2E_S.equals(templateType)) {
                	map.put("collapse1", getCollapse1());
                }
                map.put("thumbnail", getThumbnail1());
                map.put("sort", getSort1());
                map.put("size", getAreaSize(1));
                temp.add(map);
                list.add(temp);
                temp = new ArrayList();
                map = new HashMap<String, String>();
                map.put("tableId", getTable2Id());
                map.put("type", getArea2Id());
                map.put("thumbnail", getThumbnail2());
                map.put("sort", getSort2());
                map.put("size", getAreaSize(2));
                temp.add(map);
                list.add(temp);
            } else if (L_3L.equals(templateType) || L_3E.equals(templateType)) {
                temp = new ArrayList();
                map = new HashMap<String, String>();
                map.put("tableId", getTable1Id());
                map.put("type", getArea1Id());
                map.put("thumbnail", getThumbnail1());
                map.put("sort", getSort1());
            	map.put("size", getAreaSize(1));
                temp.add(map);
                list.add(temp);
                temp = new ArrayList();
                map = new HashMap<String, String>();
                map.put("tableId", getTable2Id());
                map.put("type", getArea2Id());
                map.put("thumbnail", getThumbnail2());
                map.put("sort", getSort2());
                map.put("size", getAreaSize(2));
                temp.add(map);
                list.add(temp);
                temp = new ArrayList();
                map = new HashMap<String, String>();
                map.put("tableId", getTable3Id());
                map.put("type", getArea3Id());
                map.put("thumbnail", getThumbnail3());
                map.put("sort", getSort3());
                map.put("size", getAreaSize(3));
                temp.add(map);
                list.add(temp);
            }
            setAreaLayout(JsonUtil.bean2json(list));
        } else {
            setAreaLayout("");
        }
    }
}
