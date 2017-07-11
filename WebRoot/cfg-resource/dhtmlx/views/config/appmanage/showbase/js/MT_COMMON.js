/* 模块展现中全局变量(回调函数与输出参数寄存对象).*/
var _M_this = this;

/* 自定义模块URI. */
var MT_app_uri = contextPath + "/appmanage";
/* 二次开发按钮前缀. */
var MT_btnid_pre = "S$DM$";

/** 公用类(常量). */
var MT_common = inherit(CFG_common, {
    /* 表单与列表的预留区前缀. */
    RZ_PRE_FORM : "RZ_F",
    RZ_PRE_GRID : "RZ_G",
    /* 工作流按钮. */
    P_START : "start", // 签收
    P_COMPLETE : "complete",// 提交
    P_CHECKOUT : "checkout",// 签收
    P_UNTREAD : "untread", // 退回
    P_RECALL : "recall", // 撤回
    P_REASSIGN : "reassign",// 转办
    P_DELIVER : "deliver", // 传阅
    P_TRACK : "track", // 跟踪
    P_HASREAD : "hasread", // 阅毕
    /* 预留区名称前缀. */
    ZONE_NAME_PRE : "MT_zone_",
    /* 工作箱. */
    BOX_APPLYFOR : "applyfor", // 申请箱
    BOX_TODO : "todo", // 待办箱
    BOX_HASDONE : "hasdone", // 已办箱
    BOX_COMPLETE : "complete", // 办结箱
    BOX_TOREAD : "toread", // 待阅箱
    P_ID : "processInstanceId",// 流程实例ID属性
    W_ID : "workitemId", // 工作项ID属性
    W_STATUS : "workitemStatus", // 工作项状态属性
    ROW_ID : "rowId", // 列表ID属性
    ACTIVITY_ID : "activityId", // 流程节点ID属性
    /* 按钮分隔符前缀. */
    SEPARATOR_PRE : "MT_sep_",
    /* 检索方式. */
    S_BASE : "b", // 基本检索
    S_GREAT : "g" // 高级检索
});

/**
 * 获取模块配置信息
 * @param moduleId 模块ID
 * @returns {object}
 */
function MT_GetModule(moduleId) {
    var url = contextPath + "/appmanage/module/" + moduleId + ".json?_method=get";
    return loadJson(addTimestamp(url));
}
/**
 * 初始化指定区域内容
 * @param that 全局变量
 * @param layout 区域(dhtmlXLayoutObject)
 * @param tableId 表ID
 * @param contentType 内容形式：0-表单、1-列表
 * @param areaIndex 区域索引位置：如区域一为1， 区域二为2
 */
function MT_SubLayoutInit(that, layout, tableId, contentType, areaIndex) {
    if (that.fNum == undefined) {
        that.fNum = 0;
    }
    if (that.gNum == undefined) {
        that.gNum = 0;
    }
    if (MT_common.L_FORM == contentType) {
        // 表单
        that.fNum++;
        MT_FORM_init(that, layout, tableId, areaIndex, that.fNum);
    } else if (MT_common.L_GRID == contentType) {
        // 列表
        that.gNum++;
        MT_GRID_init(that, layout, tableId, areaIndex, that.gNum);
    } else {
        // 其他
    }
}
/**
 * 创建弹出窗口
 * @param config 配置信息，格式：{id:"", parentId:"", title:"", width:"", height:""}
 */
function MT_CreateDhxWindow(config) {
    var subDhxWins = new dhtmlXWindows();
    if (config.parentId) {
        subDhxWins.enableAutoViewport(false);
        subDhxWins.attachViewportTo(config.parentId);
    }
    subDhxWins.setImagePath(IMAGE_PATH);
    var w = config.width ? config.width : 300;
    var h = config.height ? config.height : 400;
    var win = subDhxWins.createWindow(config.id, 0, 0, w, h);
    win.setModal((config.modal ? config.modal : true));
    win.button("park").hide();
    win.button("minmax1").hide();
    win.button("minmax2").hide();
    win.setText(config.title);
    win.center();
    return win;
}
/**
 * JS对象属性名：用于存储表单相关操作
 * @param tableId 表ID
 * @returns {String}
 */
function MT_P_FormId(tableId) {
    return "FORM$" + tableId;
}
/**
 * JS对象属性名：用于存储列表相关操作
 * @param tableId 表ID
 * @returns {String}
 */
function MT_P_GridId(tableId) {
    return "GRID$" + tableId;
}
/**
 * JS对象属性名：用于存储列表对应的表ID值
 * @param index 第几个列表
 * @returns {String}
 */
function MT_P_GridTableId(index) {
    return "GRID$TABLEID$00" + index;
}
/**
 * JS对象属性名：用于存储表单对应的表ID值
 * @param index 第几个表单
 * @returns {String}
 */
function MT_P_FormTableId(index) {
    return "FORM$TABLEID$00" + index;
}
/**
 * 查看对象属性(测试用)
 * @param obj
 */
function MT_SeeProperty(obj) {
    if (null == obj)
        return;
    for (var prop in obj) {
        var data = obj[prop];
        alert("property=" + prop);
        if (typeof data == "object") {
            MT_SeeProperty(data);
        // } else if (typeof data == "function"){
        // data();
        } else {
            alert("value=" + data);
        }
    }
}

/**
 * 获取预留区名称
 * @param tableId 表ID
 * @param areaIndex 预留区位置
 * @returns {string}
 */
function MT_ZoneName(tableId, type, areaIndex) {
    return MT_common.ZONE_NAME_PRE + tableId + "_" + type + "_" + areaIndex;
}

/**
 * 获取回调函数名称前缀
 * @param areaIndex 预留区位置
 * @returns {string}
 */
function MT_ReturnPrefix(areaIndex) {
    return MT_common.ZONE_NAME_PRE + areaIndex;
}
/**
 * 获取输出参数方法名
 * @param areaIndex 预留区位置
 * @param type 内容形式：0-表单、1-列表
 * @returns {string}
 */
function MT_ReturnFunctionName(areaIndex, type) {
    if (MT_common.L_FORM == type)
        return MT_ReturnPrefix(areaIndex) + "_f_return";
    return MT_ReturnPrefix(areaIndex) + "_g_return";
}
