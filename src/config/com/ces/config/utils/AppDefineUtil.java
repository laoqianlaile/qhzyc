package com.ces.config.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.collections.CollectionUtils;

import com.ces.config.dhtmlx.dao.common.DatabaseHandlerDao;
import com.ces.config.dhtmlx.entity.appmanage.Module;
import com.ces.config.dhtmlx.entity.component.ComponentReserveZone;
import com.ces.config.dhtmlx.service.component.ComponentReserveZoneService;
import com.ces.xarch.core.web.listener.XarchListener;

public class AppDefineUtil {

    /** 模板类型：整张页面. */
    public static final String TYPE_1C = "1C";

    /** 模板类型：左右结构. */
    public static final String TYPE_2U = "2U";

    /** 模板类型：上下结构. */
    public static final String TYPE_2E = "2E";

    /** 模板类型：上下结构(上查询区，下列表). */
    public static final String TYPE_2E_S = "2E_S";

    /** 模板类型：上中下结构. */
    public static final String TYPE_3E = "3E";

    /** 模板类型：左上下结构. */
    public static final String TYPE_3L = "3L";

    /** 模板内容：界面页面. */
    public static final String L_FORM = "0";

    /** 模板内容：文本列表. */
    public static final String L_GRID = "1";

    /** 模板内容：缩略图列表. */
    public static final String L_PICG = "2";
    
    /** 模板内容：缩略图列表. */
    public static final String L_SEARCH = "3";

    /** 界面页面预留区前缀. */
    public static final String RZ_FORM = "RZ_F";

    /** 列表页面预留区前缀. */
    public static final String RZ_GRID = "RZ_G";

    /** 预留区名称前缀. */
    public static final String RZ_NAME_PRE = "MT_zone_";

    /** 预留区别名前缀. */
    public static final String RZ_ALIAS_PRE = "区域";

    /** 预留区类型：界面工具条. */
    public static final String RZ_TYPE_FORM = "0";

    /** 预留区类型：列表工具条. */
    public static final String RZ_TYPE_GRID = "1";

    /** 预留区类型：列表超链接. */
    public static final String RZ_TYPE_GRID_LINK = "2";

    /** 字段ID英文名称. */
    public static final String C_ID = ConstantVar.ColumnName.ID;

    /** 连接符. */
    public static final String SPLICE = "_C_";

    /** 字段与值分隔符. */
    public static final String SPLIT = "≡";

    /** 条件关系 (并且). */
    public static final String RELATION_AND = " AND ";

    /** 条件关系 (或者). */
    public static final String RELATION_OR = " OR ";

    /** 自定义构件中的预置按钮的code和name的Map */
    private static Map<String, String> buttonMap = new HashMap<String, String>();

    /** 操作符. */
    public interface Operator {
        /** 操作符关键字. */
        interface Key {
            /** 等于. */
            String EQ = "EQ";

            /** 大于. */
            String GT = "GT";

            /** 大等于. */
            String GTE = "GTE";

            /** 小于. */
            String LT = "LT";

            /** 小等于. */
            String LTE = "LTE";

            /** 不等于. */
            String NOT = "NOT";

            /** 包含. */
            String LIKE = "LIKE";

            /** 范围. */
            String BT = "BT";

            /** 为空. */
            String NLL = "NLL";

            /** 属于. */
            String IN = "IN";
        }

        /** 操作符值. */
        interface Val {
            /** 等于. */
            String EQ = "=";

            /** 大于. */
            String GT = ">";

            /** 大等于. */
            String GTE = ">=";

            /** 小于. */
            String LT = "<";

            /** 小等于. */
            String LTE = "<=";

            /** 不等于. */
            String NOT = "<>";

            /** 包含. */
            String LIKE = "LIKE";

            /** 为空. */
            String NLL = "IS NULL";

            /** 属于. */
            String IN = "IN";
        }
    }

    /**
     * qiucs 2013-9-23
     * <p>描述: 根据操作关键字找操作符值</p>
     * 
     * @param key
     * @return String 返回类型
     * @throws
     */
    public static String getOperatorVal(String key) {
        if (Operator.Key.EQ.equals(key))
            return Operator.Val.EQ;
        else if (Operator.Key.GT.equals(key))
            return Operator.Val.GT;
        else if (Operator.Key.LT.equals(key))
            return Operator.Val.LT;
        else if (Operator.Key.LIKE.equals(key))
            return Operator.Val.LIKE;
        else if (Operator.Key.GTE.equals(key))
            return Operator.Val.GTE;
        else if (Operator.Key.LTE.equals(key))
            return Operator.Val.LTE;
        else if (Operator.Key.NOT.equals(key))
            return Operator.Val.NOT;
        else if (Operator.Key.IN.equals(key))
            return Operator.Val.IN;
        return null;
    }

    /**
     * qiucs 2013-9-23
     * <p>描述: 根据操作符值找操作符关键字</p>
     * 
     * @param val
     * @return String 返回类型
     * @throws
     */
    public static String getOperatorKey(String val) {
        val = val.toUpperCase();
        if (Operator.Val.EQ.equals(val))
            return Operator.Key.EQ;
        else if (Operator.Val.GT.equals(val))
            return Operator.Key.GT;
        else if (Operator.Val.LT.equals(val))
            return Operator.Key.LT;
        else if (Operator.Val.LIKE.equals(val))
            return Operator.Key.LIKE;
        else if (Operator.Val.GTE.equals(val))
            return Operator.Key.GTE;
        else if (Operator.Val.LTE.equals(val))
            return Operator.Key.LTE;
        else if (Operator.Val.NOT.equals(val))
            return Operator.Key.NOT;
        else if (Operator.Val.NLL.equals(val))
            return Operator.Key.NLL;
        else if (Operator.Val.IN.equals(val))
            return Operator.Key.IN;
        return null;
    }

    /**
     * qiucs 2013-12-24
     * <p>描述: 组装成查询表单字段</p>
     * 
     * @param columnName
     *            字段英文名称
     * @param operator
     *            操作符值,如“=”/“EQ”、“>”/“GT”...
     * @param isKey
     *            是否为操作关键字：true-是，false-否
     * @return String 返回类型
     *         操作符关键字 拼接 分隔符 拼接 字段英文名称，如：EQ_C_NAME
     * @throws
     */
    public static String getItemName(String columnName, String operator, boolean isKey) {
        if (StringUtil.isEmpty(operator))
            return columnName;
        if (isKey)
            return (operator + SPLICE + columnName);
        return getItemName(columnName, operator);
    }

    /**
     * qiucs 2013-9-23
     * <p>描述: 组装成查询表单字段</p>
     * 
     * @param columnName
     *            字段英文名称
     * @param operatorVal
     *            操作符值,如“=”、“>”...
     * @return String 返回类型
     *         操作符关键字 拼接 分隔符 拼接 字段英文名称，如：EQ_C_NAME
     * @throws
     */
    public static String getItemName(String columnName, String operatorVal) {
        return (getOperatorKey(operatorVal) + SPLICE + columnName);
    }

    /**
     * qiucs 2013-10-11
     * <p>描述: 将页面栏位组装成查询条件</p>
     * 
     * @param filterItem
     * @param tableAlias
     *            --表别名
     */
    public static String processFilterItem(String filterItem, String tableAlias) {
        try {
            System.out.println(filterItem);
            String[] prop = filterItem.split(SPLIT);
            String itemName = prop[0];
            String value = prop.length > 1 ? prop[1] : "";
            String[] optAndcol = itemName.split(SPLICE);
            String key = optAndcol[0];
            String col = optAndcol[1];
            return processColumnFilter(tableAlias, col, key, value);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }
    }

    /**
     * qiucs 2014-2-12
     * <p>描述: 处理复杂的过滤条件
     * 如下所示
     * (((LIKE_C_NAME≡1)OR(NOT_C_SEX≡2012))AND(GT_C_AGE≡23))AND(EQ_C_CODE≡4)
     * 前提是每最小的过滤条件必须用括号括起来(EQ_C_CODE≡4)
     * </p>
     * 
     * @param complexFilterItem
     * @param tableAlias
     * @return String 返回类型
     */
    public static String processComplexFilterItem(String complexFilterItem, String tableAlias) {
        String bak = complexFilterItem.replace("((", "(").replace("))", ")");
        while (bak.indexOf("((") > -1) {
            bak = bak.replace("((", "(").replace("))", ")");
        }
        Pattern pat = Pattern.compile("(?<=\\()(.+?)(?=\\))");
        Matcher mat = pat.matcher(bak);
        while (mat.find()) {
            String item = mat.group();
            String filter = AppDefineUtil.processFilterItem(item, tableAlias);
            complexFilterItem = complexFilterItem.replace(item, filter);
            int pos = mat.end() + 1;
            bak = bak.substring(pos);
            mat = pat.matcher(bak);
        }
        return complexFilterItem;
    }

    /**
     * qiucs 2014-2-12
     * <p>描述: 处理复杂的过滤条件</p>
     */
    public static String processComplexFilterItem(String complexFilterItem) {
        return processComplexFilterItem(complexFilterItem, null);
    }

    /**
     * qiucs 2013-10-17
     * <p>描述: 组装为查询过滤条件</p>
     * 
     * @param tableAlias
     * @param columnName
     * @param operatorKey
     * @param value
     * @return String 返回类型
     * @throws
     */
    public static String processColumnFilter(String tableAlias, String columnName, String operatorKey, String value) {
        StringBuffer filter = new StringBuffer();
        if (StringUtil.isNotEmpty(tableAlias)) {
            filter.append(tableAlias).append(".");
        }
        if (Operator.Key.LIKE.equals(operatorKey)) {
            filter.append(columnName).append(" ").append(Operator.Val.LIKE).append(" '%").append(value).append("%'");
        } else if (Operator.Key.NLL.equals(operatorKey)) {
            /*
             * if ("1".equals(value)) {
             * filter.append(columnName).append(" is not null");
             * } else {
             * filter.append(columnName).append(" is null");
             * }
             */
            filter.append(processNull(columnName, value));
        } else if (Operator.Key.IN.equals(operatorKey)) {
            filter.append(columnName).append(" ").append(Operator.Val.IN).append(" ('").append(value.replace(",", "','")).append("')");
        } else {
            filter.append(columnName).append(" ").append(getOperatorVal(operatorKey)).append(" '").append(value).append("'");
        }
        return String.valueOf(filter);
    }

    /**
     * qiucs 2014-9-25
     * <p>描述: 为NULL处理</p>
     * 
     * @throws
     */
    public static String processNull(String columnName, String value) {
        StringBuffer filter = new StringBuffer();
        if (DatabaseHandlerDao.isSqlserver()) {
            filter.append("isnull(").append(columnName).append(", '') ").append(("1".equals(value) ? "<>" : "=")).append(" ''");
        } else {
            filter.append(columnName).append(" is ").append(("1".equals(value) ? " not " : "")).append("null");
        }
        return filter.toString();
    }

    /**
     * qiucs 2013-9-23
     * <p>描述: 将页面栏位组装成查询条件</p>
     * 
     * @param filterItem
     * @return String 返回类型
     * @throws
     */
    public static String processFilterItem(String filterItem) {
        return processFilterItem(filterItem, null);
    }

    /**
     * qiucs 2013-9-26
     * <p>描述: 模块访问地址</p>
     * 
     * @param type
     *            --模板类型
     * @param moduleId
     *            --模块ID
     * @param ui
     *            --0-dhtmlx 1-coral
     * @return String 返回类型
     * @throws
     */
    public static String getUrl(String type, String moduleId, String ui) {
        String parmater = "?P_moduleId=" + moduleId;
        if (Module.UI_DHTMLX.equals(ui)) {
            if (TYPE_2U.equals(type)) {
                return "config/appmanage/show/MT_2U.jsp" + parmater;
            } else if (TYPE_2E.equals(type)) {
                return "config/appmanage/show/MT_2E.jsp" + parmater;
            } else if (TYPE_1C.equals(type)) {
                return "config/appmanage/show/MT_1C.jsp" + parmater;
            }

            return null;
        }
        return "config/appmanage/show/MT_component.jsp" + parmater;
    }

    /**
     * qiucs 2013-9-26
     * <p>描述: 刷新列表回调函数名称</p>
     * 
     * @param areaIndex
     *            --区域索引位置
     * @param ui
     *            --0-dhtmlx 1-coral
     * @return String 返回类型
     * @throws
     */
    public static String getGridCallback(int areaIndex, String ui) {
        if (Module.UI_DHTMLX.equals(ui)) {
            return returnPrefix(areaIndex).concat("_g_reload");
        }
        return "callback.reload";
    }

    /**
     * qiucs 2013-9-26
     * <p>描述: 列表输出参数函数名称</p>
     * 
     * @param areaIndex
     *            --区域索引位置
     * @param ui
     *            --0-dhtmlx 1-coral
     * @return String 返回类型
     * @throws
     */
    public static String getGridReturnData(int areaIndex, String ui) {
        if (Module.UI_DHTMLX.equals(ui)) {
            return returnPrefix(areaIndex).concat("_g_return");
        }
        return "value";
    }

    /**
     * qiucs 2013-9-26
     * <p>描述: 表单输出参数函数名称</p>
     * 
     * @param areaIndex
     *            --区域索引位置
     * @param ui
     *            --0-dhtmlx 1-coral
     * @return String 返回类型
     * @throws
     */
    public static String getFormReturnData(int areaIndex, String ui) {
        if (Module.UI_DHTMLX.equals(ui)) {
            return returnPrefix(areaIndex).concat("_f_return");
        }
        return "value";
    }

    /**
     * qiucs 2013-9-29
     * <p>描述: 预留区名称</p>
     * 
     * @param areaIndex
     *            --区域索引位置
     * @return String 返回类型
     * @throws
     */
    public static String returnPrefix(int areaIndex) {
        return RZ_NAME_PRE.concat(String.valueOf(areaIndex));
    }

    /**
     * qiucs 2014-3-10
     * <p>描述: 预留区名称</p>
     * 
     * @param subType
     *            -- 如果是超链接为"LINK"，否则为空
     */
    public static String getZoneName(String tableId, String type, int areaIndex, String subType, String position) {
        String zoneName = RZ_NAME_PRE.concat(String.valueOf(tableId + "_" + contentType(type) + "_" + areaIndex));
        // position 0：上工具条 1：下工具条
        if (isForm(type) && StringUtil.isNotEmpty(position)) {
            zoneName += "_" + position;
        }
        // subType 超链接
        if (StringUtil.isNotEmpty(subType)) {
            zoneName += "_" + subType;
        }
        return zoneName;
    }

    /**
     * 获取公用预留区
     * 
     * @param module
     * @param reserveZoneType 预留区类型
     * @return
     */
    public static List<ComponentReserveZone> getCommonReserveZone(Module module, String reserveZoneType) {
        List<ComponentReserveZone> reserveZoneList = new ArrayList<ComponentReserveZone>();
        List<String> reserveZoneNameList = new ArrayList<String>();
        if (StringUtil.isNotEmpty(module.getTable1Id())) {
            getCommonReserveZones(reserveZoneNameList, module.getTable1Id(), reserveZoneType);
        }
        if (StringUtil.isNotEmpty(module.getTable2Id()) && !module.getTable2Id().equals(module.getTable1Id())) {
            getCommonReserveZones(reserveZoneNameList, module.getTable2Id(), reserveZoneType);
        }
        if (StringUtil.isNotEmpty(module.getTable3Id()) && !module.getTable3Id().equals(module.getTable2Id())
                && !module.getTable3Id().equals(module.getTable1Id())) {
            getCommonReserveZones(reserveZoneNameList, module.getTable3Id(), reserveZoneType);
        }
        if (CollectionUtils.isNotEmpty(reserveZoneNameList)) {
            ComponentReserveZone temp = null;
            for (String reserveZoneName : reserveZoneNameList) {
                temp = XarchListener.getBean(ComponentReserveZoneService.class).getCommonReserveZoneByName(reserveZoneName);
                if (temp != null) {
                    reserveZoneList.add(temp);
                }
            }
        }
        return reserveZoneList;
    }

    /**
     * 获取公用预留区
     * 
     * @param reserveZoneNameList 预留区名称List
     * @param logicTableCode 逻辑表编码
     * @param reserveZoneType 复制的预留区类型（过滤用的）
     */
    public static void getCommonReserveZones(List<String> reserveZoneNameList, String logicTableCode, String reserveZoneType) {
        if (StringUtil.isNotEmpty(logicTableCode)) {
            if (StringUtil.isNotEmpty(reserveZoneType)) {
                if (ConstantVar.Component.ReserveZoneType.TOOLBAR.equals(reserveZoneType)) {
                    reserveZoneNameList.add(logicTableCode + "_GRID");
                    reserveZoneNameList.add(logicTableCode + "_FORM_0");
                    reserveZoneNameList.add(logicTableCode + "_FORM_1");
                } else {
                    reserveZoneNameList.add(logicTableCode + "_GRID_LINK");
                }
            } else {
                reserveZoneNameList.add(logicTableCode + "_GRID");
                reserveZoneNameList.add(logicTableCode + "_GRID_LINK");
                reserveZoneNameList.add(logicTableCode + "_FORM_0");
                reserveZoneNameList.add(logicTableCode + "_FORM_1");
            }
        }
    }

    /**
     * qiucs 2014-10-23
     * <p>描述: 公共预留区名称</p>
     * 
     * @param subType
     *            -- 如果是超链接为"LINK"，否则为空
     */
    public static String getCommonZoneName(String logicTableCode, String type, String subType, String position) {
        String commonZoneName = null;
        if (isGrid(type)) {
            commonZoneName = logicTableCode + "_GRID";
            if (StringUtil.isNotEmpty(subType)) {
                commonZoneName += "_" + subType;
            }
        } else if (isForm(type)) {
            commonZoneName = logicTableCode + "_FORM";
            if (StringUtil.isNotEmpty(position)) {
                commonZoneName += "_" + position;
            }
        }
        return commonZoneName;
    }

    /**
     * 获取自定义构件的预留区的类型
     * 
     * @param reserveZoneName 预留区名称
     * @param isCommon 是否是公用的
     * @return String
     */
    public static String getZoneType(String reserveZoneName, boolean isCommon) {
        String zoneType = null;
        if (isCommon) {
            if (reserveZoneName.indexOf("_FORM") != -1) {
                zoneType = RZ_TYPE_FORM;
            } else if (reserveZoneName.indexOf("_GRID") != -1) {
                zoneType = RZ_TYPE_GRID;
            } else if (reserveZoneName.indexOf("_GRID_LINK") != -1) {
                zoneType = RZ_TYPE_GRID_LINK;
            }
        } else {
            String[] strs = reserveZoneName.split("_");
            if (strs.length == 5) {
                if ("0".equals(strs[3])) {
                    zoneType = RZ_TYPE_FORM;
                } else if ("1".equals(strs[3])) {
                    zoneType = RZ_TYPE_GRID;
                }
            } else if (strs.length == 6) {
                if ("0".equals(strs[3])) {
                    zoneType = RZ_TYPE_FORM;
                } else if ("1".equals(strs[3])) {
                    zoneType = RZ_TYPE_GRID_LINK;
                }
            }
        }
        return zoneType;
    }

    /**
     * qiucs 2013-9-29
     * <p>描述: 预留区别名</p>
     * 
     * @param areaIndex
     *            --区域索引位置
     * @return String 返回类型
     * @throws
     */
    public static String getZoneAlias(String type, int areaIndex, String subType, String position) {
        String zoneAlias = RZ_ALIAS_PRE.concat(String.valueOf(areaIndex)).concat((isForm(type) ? "-表单" : "-列表"));
        if (isForm(type)) {
            if ("0".equals(position)) {
                zoneAlias += "_" + "上工具条";
            } else if ("1".equals(position)) {
                zoneAlias += "_" + "下工具条";
            } else {
                zoneAlias += "_" + "工具条";
            }
        } else if (isGrid(type)) {
            if (StringUtil.isNotEmpty(subType)) {
                zoneAlias += "_" + subType;
            } else {
                zoneAlias += "_" + "工具条";
            }
        }
        return zoneAlias;
    }

    /**
     * qiucs 2014-3-10
     * <p>描述: 预留区别名</p>
     */
    public static String getZoneAlias(String text, String type, int areaIndex, String subType, String position) {
        return StringUtil.null2empty(text) + getZoneAlias(contentType(type), areaIndex, subType, position);
    }

    /**
     * qiucs 2013-9-29
     * <p>描述: 供回調函數、預留區、輸出參數使用的</p>
     * 
     * @param type
     * @param areaIndex
     * @return String 返回类型
     * @throws
     */
    public static String getPage(int areaIndex) {
        // return "MT_page_".concat(String.valueOf(areaIndex));
        // 所有预留区的page都一样
        return "MT_page_";
    }

    public static boolean isForm(String type) {
        return L_FORM.equals(type);
    }

    public static boolean isGrid(String type) {
        return L_GRID.equals(type) || L_PICG.equals(type);
    }

    public static String contentType(String type) {
        if (isForm(type))
            return L_FORM;
        if (isGrid(type))
            return L_GRID;
        return null;
    }

    /**
     * <p>描述: SearchParameter中toParamMap中的KEY</p>
     * <p>公司: 上海中信信息发展股份有限公司</p>
     * 
     * @author qiucs
     * @date 2014-11-3 下午11:35:37
     */
    public interface PN {
        String componentVersionId = "componentVersionId";

        String masterTableId = "masterTableId";

        String masterDataId = "masterDataId";

        String box = "box";

        String timestamp = "timestamp";
    }

    /**
     * <p>描述: 自定义构件默认按钮</p>
     * <p>公司: 上海中信信息发展股份有限公司</p>
     * 
     * @author qiucs
     * @date 2014-12-3 下午8:46:37
     */
    public static interface ButtonUI {
        // 代码值
        interface Code {
            String UPDATE = "update";

            String BATCH_UPDATE = "batchUpdate";

            String DELETE = "delete";

            String LOGICAL_DELETE = "logicalDelete";

            // String NESTED_SEARCH = "nestedSearch";
            String REFRESH = "refresh";

            String INTEGRATION_SEARCH = "integrationSearch";

            String BASE_SEARCH = "baseSearch";

            String GREAT_SEARCH = "greatSearch";

            String SETTING = "setting";

            String REPORT = "report";

            String UPLOAD = "upload";

            String VIEW_DOCUMENT = "viewDocument";

            String VIEW = "view";

            String EXPORT = "export";

            String EXPORT_SETTING = "exportSetting";

            String MODIFY = "modify";

            String REMOVE = "remove";

            String MORE = "more";

            String SAVE = "save";

            String ADD = "add";

            String SAVE_ALL = "saveAll";

            String SAVE_AND_CREATE = "saveAndCreate";

            String SAVE_AND_UPDATE = "saveAndUpdate";

            String SUBMIT = "submit";

            String SAVE_AND_SUBMIT = "saveAndSubmit";

            String CLOSE = "close";

            String SAVE_AND_CLOSE = "saveAndClose";

            String ADD_AND_CLOSE = "addAndClose";

            String RESET = "reset";

            String FIRST_RECORD = "firstRecord";

            String PREVIOUS_RECORD = "previousRecord";

            String NEXT_RECORD = "nextRecord";

            String LAST_RECORD = "lastRecord";

            String CREATE = "create";

            String OTHER = "other";

            String TRACK = "track";

            String LINK_VIEW_DFORM = "linkViewDForm";

            String LINK_VIEW_DGRID = "linkViewDGrid";

            String LINK_VIEW_DOCUMENT = "linkViewDocument";

            String LINK_UPDATE = "linkUpdate";

            String LINK_DELETE_GD = "linkDeleteGD";

            String LINK_DELETE_DB = "linkDeleteDB";

            String LINK_DELETE_LG = "linkDeleteLG";
        }

        // 显示名称
        interface Name {
            String UPDATE = "修改";

            String BATCH_UPDATE = "批量修改";

            String DELETE = "删除（物理）";

            String LOGICAL_DELETE = "删除（逻辑）";

            String SEARCH = "检索";

            String REFRESH = "刷新";

            String REPORT = "报表";

            String UPLOAD = "上传电子全文";

            String VIEW_DOCUMENT = "查看电子全文";

            String VIEW = "查看";

            String EXPORT = "导出";

            String EXPORT_SETTING = "导出设置";

            String MODIFY = "修改（前台）";

            String REMOVE = "删除（前台）";

            String MORE = "更多";

            // String NESTED_SEARCH = "查询（嵌入式）";
            String INTEGRATION_SEARCH = "一体化检索";

            String BASE_SEARCH = "基本检索";

            String GREAT_SEARCH = "高级检索";

            String SETTING = "设置";

            String SAVE = "保存";

            String ADD = "添加";

            String SAVE_ALL = "保存（包括从列表信息）";

            String SAVE_AND_CREATE = "保存并新增";

            String SAVE_AND_UPDATE = "保存并修改";

            String SUBMIT = "提交";

            String SAVE_AND_SUBMIT = "保存并提交";

            String CLOSE = "关闭";

            String SAVE_AND_CLOSE = "保存并关闭";

            String ADD_AND_CLOSE = "添加并关闭";

            String RESET = "重置";

            String FIRST_RECORD = "首条";

            String PREVIOUS_RECORD = "上一条";

            String NEXT_RECORD = "下一条";

            String LAST_RECORD = "末条";

            String CREATE = "新增";

            String OTHER = "其他功能";

            String TRACK = "跟踪";

            String LINK_VIEW_DFORM = "详细";

            String LINK_VIEW_DGRID = "明细";

            String LINK_VIEW_DOCUMENT = "查看电子全文";

            String LINK_UPDATE = "修改";

            String LINK_DELETE_DB = "删除（物理）";

            String LINK_DELETE_LG = "删除（逻辑）";

            String LINK_DELETE_GD = "删除（前台）";
        }

        // 图片
        interface Image {
            String UPDATE = "update.gif";

            String BATCH_UPDATE = "update.gif";

            String DELETE = "delete.gif";

            String SEARCH = "search.gif";

            String REPORT = "print.gif";

            String UPLOAD = "upload.gif";

            String SAVE = "save.gif";

            String SAVE_AND_CREATE = "save.gif";

            String SAVE_AND_UPDATE = "save.gif";

            String SUBMIT = "submit";

            String SAVE_AND_SUBMIT = "save.gif";

            String CLOSE = "close.gif";

            String SAVE_AND_CLOSE = "save.gif";

            String RESET = "reset.gif";

            String FIRST_RECORD = "first.gif";

            String PREVIOUS_RECORD = "previou.gif";

            String NEXT_RECORD = "next.gif";

            String LAST_RECORD = "last.gif";

            String CREATE = "new.gif";

            String OTHER = "button.gif";
        }
    }

    /**
     * 自定义构件中的预置按钮的code和name的Map
     * 
     * @return Map<String, String>
     */
    public static Map<String, String> getButtonMap() {
        if (buttonMap.isEmpty()) {
            buttonMap.put(ButtonUI.Code.UPDATE, ButtonUI.Name.UPDATE);
            buttonMap.put(ButtonUI.Code.BATCH_UPDATE, ButtonUI.Name.BATCH_UPDATE);
            buttonMap.put(ButtonUI.Code.MODIFY, ButtonUI.Name.MODIFY);
            buttonMap.put(ButtonUI.Code.DELETE, ButtonUI.Name.DELETE);
            buttonMap.put(ButtonUI.Code.LOGICAL_DELETE, ButtonUI.Name.LOGICAL_DELETE);
            buttonMap.put(ButtonUI.Code.REMOVE, ButtonUI.Name.REMOVE);
            buttonMap.put(ButtonUI.Code.REFRESH, ButtonUI.Name.REFRESH);

            // buttonMap.put(ButtonUI.Code.NESTED_SEARCH, ButtonUI.Name.NESTED_SEARCH);
            buttonMap.put(ButtonUI.Code.INTEGRATION_SEARCH, ButtonUI.Name.INTEGRATION_SEARCH);
            buttonMap.put(ButtonUI.Code.BASE_SEARCH, ButtonUI.Name.BASE_SEARCH);
            buttonMap.put(ButtonUI.Code.GREAT_SEARCH, ButtonUI.Name.GREAT_SEARCH);

            buttonMap.put(ButtonUI.Code.SETTING, ButtonUI.Name.SETTING);
            buttonMap.put(ButtonUI.Code.REPORT, ButtonUI.Name.REPORT);
            buttonMap.put(ButtonUI.Code.UPLOAD, ButtonUI.Name.UPLOAD);
            buttonMap.put(ButtonUI.Code.VIEW_DOCUMENT, ButtonUI.Name.VIEW_DOCUMENT);
            buttonMap.put(ButtonUI.Code.VIEW, ButtonUI.Name.VIEW);
            buttonMap.put(ButtonUI.Code.MORE, ButtonUI.Name.MORE);
            buttonMap.put(ButtonUI.Code.EXPORT, ButtonUI.Name.EXPORT);
            buttonMap.put(ButtonUI.Code.EXPORT_SETTING, ButtonUI.Name.EXPORT_SETTING);

            buttonMap.put(ButtonUI.Code.SAVE, ButtonUI.Name.SAVE);
            buttonMap.put(ButtonUI.Code.ADD, ButtonUI.Name.ADD);
            buttonMap.put(ButtonUI.Code.SAVE_ALL, ButtonUI.Name.SAVE_ALL);
            buttonMap.put(ButtonUI.Code.SAVE_AND_CREATE, ButtonUI.Name.SAVE_AND_CREATE);
            buttonMap.put(ButtonUI.Code.SAVE_AND_UPDATE, ButtonUI.Name.SAVE_AND_UPDATE);
            buttonMap.put(ButtonUI.Code.SUBMIT, ButtonUI.Name.SUBMIT);
            buttonMap.put(ButtonUI.Code.SAVE_AND_SUBMIT, ButtonUI.Name.SAVE_AND_SUBMIT);
            buttonMap.put(ButtonUI.Code.CLOSE, ButtonUI.Name.CLOSE);
            buttonMap.put(ButtonUI.Code.SAVE_AND_CLOSE, ButtonUI.Name.SAVE_AND_CLOSE);
            buttonMap.put(ButtonUI.Code.ADD_AND_CLOSE, ButtonUI.Name.ADD_AND_CLOSE);
            buttonMap.put(ButtonUI.Code.RESET, ButtonUI.Name.RESET);
            buttonMap.put(ButtonUI.Code.FIRST_RECORD, ButtonUI.Name.FIRST_RECORD);
            buttonMap.put(ButtonUI.Code.PREVIOUS_RECORD, ButtonUI.Name.PREVIOUS_RECORD);
            buttonMap.put(ButtonUI.Code.NEXT_RECORD, ButtonUI.Name.NEXT_RECORD);
            buttonMap.put(ButtonUI.Code.LAST_RECORD, ButtonUI.Name.LAST_RECORD);

            buttonMap.put(ButtonUI.Code.CREATE, ButtonUI.Name.CREATE);
            buttonMap.put(ButtonUI.Code.OTHER, ButtonUI.Name.OTHER);
            buttonMap.put(ButtonUI.Code.TRACK, ButtonUI.Name.TRACK);

            buttonMap.put(ButtonUI.Code.LINK_DELETE_DB, ButtonUI.Name.LINK_DELETE_DB);
            buttonMap.put(ButtonUI.Code.LINK_DELETE_LG, ButtonUI.Name.LINK_DELETE_LG);
            buttonMap.put(ButtonUI.Code.LINK_DELETE_GD, ButtonUI.Name.LINK_DELETE_GD);
            buttonMap.put(ButtonUI.Code.LINK_UPDATE, ButtonUI.Name.LINK_UPDATE);
            buttonMap.put(ButtonUI.Code.LINK_VIEW_DFORM, ButtonUI.Name.LINK_VIEW_DFORM);
            buttonMap.put(ButtonUI.Code.LINK_VIEW_DGRID, ButtonUI.Name.LINK_VIEW_DGRID);
            buttonMap.put(ButtonUI.Code.LINK_VIEW_DOCUMENT, ButtonUI.Name.LINK_VIEW_DOCUMENT);
        }
        return buttonMap;
    }
}
