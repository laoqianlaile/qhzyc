/**
 *  该js处理dhtmlxtree功能
**/
var TREE_URL = "";
var T_WIN_ID = "twin_id";
var dhxTreeWins, treeWin, detailTreeForm, nodeId;
var WIN_LEFT;
var WIN_WIDTH = 200;
var WIN_HEIGHT = 300;

var treeDetailFormat = [{
            type : "settings",
            position : "label-left",
            offsetTop : "10",
            labelWidth : 75,
            inputWidth : 180,
            labelAlign : "left"
        }, {
            type : "block",
            width : "280",
            list : []
        }];

// 创建window
function create_window(title, initToolbar) {
    dhxTreeWins = new dhtmlXWindows();
    dhxTreeWins.enableAutoViewport(false);
    dhxTreeWins.attachViewportTo(top.window.document.body);
    treeWin = dhxTreeWins.createWindow(T_WIN_ID, 0, 0, WIN_WIDTH, WIN_HEIGHT);
    treeWin.setModal(true);
    treeWin.setText(title);
    treeWin.center();
    var treeDetailFormat = initDetailTreeFormat();
    detailTreeForm = treeWin.attachForm(treeDetailFormat);
    var statusBar = treeWin.attachStatusBar();
    var toolBar = new dhtmlXToolbarObject(statusBar);
    if (initToolbar && typeof initToolbar == "function") {
    	initToolbar.call(this, toolBar);
    }
    detailTreeForm.attachEvent("onValidateError", function(input, value, result) {
    });
    return detailTreeForm;
}
function edit_window(id, initToolbar) {
    var loadUrl = TREE_URL + "/" + id + ".json";
    detailTreeForm = create_window("修改", initToolbar);
    loadTreeForm(detailTreeForm, loadUrl);
}
// 删除节点
function delById(nodeId, callback) {
    var deleteUrl = TREE_URL + "/" + nodeId + ".json?_method=delete";
    if (callback && typeof callback == "function") {
        dhtmlxAjax.get(deleteUrl, callback);
    } else {
	    dhtmlxAjax.get(deleteUrl, function(loader) {
	        var parId = tree.getParentId(nodeId);
	        tree.refreshItem(parId);
	    });
    }
}
// 加载数据到窗体控件里
function loadTreeForm(formObj, url) {
    var formData = loadJson(url);
    formObj.forEachItem(function(name) {
        var type = formObj.getItemType(name);
        var _name = "", value = "";
        if ((type == "input") || (type == "combo") || (type == "hidden") || (type == "calendar")) {
            _name = getTreeFormItemName(name);
            value = obtainNonNullValue(_name, formData);
            formObj.setItemValue(name, value);
        }
    });
}
function getTreeFormItemName(name) {
    if (name == undefined)
        return "";
    var names = name.split("_");
    if ((names.length >= 2) && (names[1] == "temp"))
        return names[0];
    return name;
}
// 初始化修改、查看数据
function initDetailTreeFormat() {
    treeDetailFormat[1].list = detailTreeFormData.format;
    if ((detailTreeFormData.settings == null) || (detailTreeFormData.settings == undefined))
        return treeDetailFormat;
    for (var key in detailTreeFormData.settings) {
        treeDetailFormat[0][key] = detailTreeFormData.settings[key];
    }
    return treeDetailFormat;
}
