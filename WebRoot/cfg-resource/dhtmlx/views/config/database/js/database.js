MODEL_URL = contextPath + "/database/database";
QUERY_URL = MODEL_URL + "!search.json";
detailFormData = {
	format: [
        {type: "block", width: "800", list:[
			{type: "hidden", name: "_method"},
			{type: "hidden", name: "id"},
			{type: "input", label: "数据源名称:", name: "name", maxLength:100, required: true, tooltip: '数据源名称不能为空'},
			{type: "input", label: "数据库实例名:", name: "instanceName", maxLength:100, required: true, tooltip: '数据库实例名不能为空'},
			{type: "input", label: "服务器地址:", name: "ip", maxLength:20, required: true, validate: "ValidIPv4", tooltip: '服务器地址不能为空，格式为IP地址格式'},
			{type: "input", label: "用户名:", name: "userName", maxLength:100, required: true, tooltip: '用户名不能为空'},
			{type: "newcolumn"},
			{type: "block", offsetTop:"27"},
			{type: "combo", label: "数据库类型:", name: "type", required: true, options:[
      				{value: "", text: "请选择数据类型"},
      				{value: "0", text: "ORACLE"},
      				{value: "1", text: "SQLSERVER"}
      		], tooltip: '数据库类型不能为空'},
			{type: "input", label: "端口号:", name: "port", maxLength:5, validate: "ValidInteger", required: true, tooltip: '端口号不能为空'},
			{type: "password", label: "密码:", name: "password", maxLength:100, required: true, tooltip: '密码不能为空'},
			{type: "newcolumn"},
			{type: "block", width: "120", offsetTop:"50", list:[
				{type: "button", name: "connDB", value: "测试连接"},
				{type: "button", name: "save", value: "保存", width:100}
			]}
		]}
    ],
	settings: {labelWidth: 120, inputWidth: 200}
};
gridData = {
	format: {
		headers: ["&nbsp;", "<center>数据源名称</center>", "<center>数据库实例名</center>", "<center>数据库类型</center>", "<center>服务器地址</center>", "<center>端口号</center>" ,""],
		cols: ["id", "name", "instanceName", "type", "ip", "port"],
		colWidths: ["30", "120", "150", "120", "150", "100", "*"],
		colTypes: ["sub_row_form", "ro", "ro", "co", "ro", "ro", "ro"],
		colAligns: ["right", "left", "left", "left", "left", "left"],
		colTooltips: ["false", "true", "true", "true", "true", "true", "false"]
	}
};
/**
 * 初始化数据库列表
 */
function initSelfGrid() {
    dataGrid.setImagePath(IMAGE_PATH);
    dataGrid.setHeader(gridData.format.headers.toString());
    dataGrid.setInitWidths(gridData.format.colWidths.toString());
    dataGrid.setColTypes(gridData.format.colTypes.toString());
    dataGrid.setColAlign(gridData.format.colAligns.toString());
    if (gridData.format.colTooltips) {
        dataGrid.enableTooltips(gridData.format.colTooltips.toString());
    }
    dataGrid.setSkin(Skin);
    dataGrid.init();
    dataGrid.enableMultiselect(true);
    dataGrid.setStyle("font-weight:bold;font-size:12px;", "", "", "");
    dataGrid.attachEvent("onRowSelect", function(rId, cInd) {
        dataGrid.cells(rId, 0).open();
    });
    dataGrid.attachEvent("onBeforeSubFormLoadStruct", function(subform) {
        subform.c = initDetailFormFormat(detailFormData);
    });
    dataGrid.attachEvent("onSubFormLoaded", function(subform, id, index) {
        if (id != "") {
            var url = MODEL_URL + "/" + id + ".json?_method=get";
            loadForm(subform, url);
        }
        subform.attachEvent("onButtonClick", function(buttonName) {
            if (buttonName == "save") {
                var id = subform.getItemValue("id");
                var name = subform.getItemValue("name");
                var result = eval("(" + loadJson(MODEL_URL + "!validateFields.json?id=" + id + "&name=" + name) + ")");
                if (result.nameExist) {
                    dhtmlx.message(getMessage("form_field_exist", "数据库名称"));
                    return;
                }
                if (id == "") {
                    SAVE_URL = MODEL_URL;
                    subform.setItemValue("_method", "post");
                } else {
                    SAVE_URL = MODEL_URL + "/" + id;
                    subform.setItemValue("_method", "put");
                }
                subform.send(SAVE_URL, "post", function(loader, response) {
                    dhtmlx.message(getMessage("save_success"));
                    search();
                });
            } else if (buttonName == "connDB") {
                if (!subform.validate()) {
                    return;
                }
                var id = subform.getItemValue("id");
                var name = subform.getItemValue("name");
                var instanceName = subform.getItemValue("instanceName");
                var ip = subform.getItemValue("ip");
                var port = subform.getItemValue("port");
                var type = subform.getItemValue("type");
                var userName = subform.getItemValue("userName");
                var password = subform.getItemValue("password");
                var url = MODEL_URL + "!connDatabase.json?P_id=" + id + "&P_name=" + name + "&P_instanceName=" + instanceName + "&P_ip=" + ip + "&P_port=" + port + "&P_type=" + type + "&P_userName=" + userName + "&P_password=" + password;
                var result = eval("(" + loadJson(url) + ")");
                if (result.connSuccess) {
                    dhtmlx.message("连接测试成功！");
                } else {
                    dhtmlx.message("连接测试失败！");
                }
            }
        });
    });
    dataGrid.attachEvent("onSubRowOpen", function(id, expanded) {
        if (expanded) {
            dataGrid.forEachRow(function(rId) {
                if (id != rId) {
                    dataGrid.cells(rId, 0).close();
                }
            });
        }
    });
    if (pageable) {
        pagesize = getCookie("pagesize") || PAGE_SIZE;
        dataGrid.enablePaging(true, pagesize, 1, statusBar);
        dataGrid.setPagingSkin('toolbar', Skin);
    }
    search();
}
/**
 * 初始化数据库列表工具条
 */
function initSelfToolBar() {
    toolBar.setIconsPath(TOOLBAR_IMAGE_PATH);
    toolBar.addButton("add", 0, "新增", "new.gif");
    toolBar.addSeparator("septr$01", 1);
    toolBar.addButton("delete", 2, "删除", "delete.gif");
    toolBar.addSeparator("septr$01", 3);
    toolBar.addButton("refresh", 4, "刷新", "refresh.gif");
    toolBar.attachEvent('onClick', function(buttonName) {
        if (buttonName == "add") {
            dataGrid.addSubRow();
        } else if (buttonName == "delete") {
            var selectId = dataGrid.getSelectedRowId();
            if (selectId == undefined) {
                dhtmlx.message(getMessage("select_record"));
                return;
            } else if (selectId == "") {
                search();
                return;
            }
            dhtmlx.confirm({
                type : "confirm",
                text : getMessage("delete_warning"),
                ok : "确定",
                cancel : "取消",
                callback : function(flag) {
                    if (flag) {
                        deleteById(selectId);
                    }
                }
            });
        } else if (buttonName == "refresh") {
            search();
        }
    });
}
/**
 * 页面初始化方法
 */
function init() {
    dhxLayout = new dhtmlXLayoutObject("content", "1C");
    dhxLayout.cells("a").hideHeader();
    toolBar = dhxLayout.cells("a").attachToolbar();
    dataGrid = dhxLayout.cells("a").attachGrid();
    var typeCombo = dataGrid.getCombo(3);
    typeCombo.put("0", "ORACLE");
    typeCombo.put("1", "SQLSERVER");
    statusBar = dhxLayout.cells("a").attachStatusBar();
    initSelfToolBar();
    initSelfGrid();
}