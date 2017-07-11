MODEL_URL = contextPath + "/systemupdate/system-update";
QUERY_URL = MODEL_URL + "!search.json?P_orders=updateTime,desc";
gridData = {
    format : {
        headers : ["<center>更新包版本</center>", "<center>更新时间</center>", ""],
        cols : ["updateVersion", "updateTime"],
        colWidths : ["200", "200", "*"],
        colTypes : ["ro", "ro", "ro"],
        colAligns : ["left", "left"],
        colTooltips : ["true", "true", "false"]
    }
};
// 更新包导入时对应后台session中UpdatePackageConfig的key
var updatePackageConfigKey;
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
    toolBar.addButton("update", 0, "更新", "new.gif");
    toolBar.addSeparator("septr$01", 1);
    toolBar.addButton("view", 2, "查看", "view.gif");
    toolBar.attachEvent('onClick', function(buttonName) {
        if (buttonName == "update") {
        	var current = new Date();
        	updatePackageConfigKey = current.getTime() + "_" + Math.floor(Math.random()*100);
        	GWIN_WIDTH = 450;
			GWIN_HEIGHT = 280;
			if (!dhxWins) {
		    	dhxWins = new dhtmlXWindows();
		    }
		    dataWin = dhxWins.createWindow(WIN_ID, 0, 0, GWIN_WIDTH, GWIN_HEIGHT);
		    dataWin.setModal(true);
		    dataWin.setText("更新包导入");
		    dataWin.center();
		    dataWin.button('park').hide();
		    dataWin.button('minmax1').hide();
		    dataWin.denyResize();
		    var vaultDiv = document.createElement("div");
		    vaultDiv.setAttribute("id", "vaultDiv");
		    document.body.appendChild(vaultDiv);
		    var UPLOAD_URL = MODEL_URL + "!uploadHandler";
		    var GET_INFO_URL = MODEL_URL + "!getInfoHandler";
		    var GET_ID_URL = MODEL_URL + "!getIdHandler";
        	var vault = new dhtmlXVaultObject();
            vault.setImagePath(DHX_RES_PATH + "/common/css/imgs/");
            vault.setServerHandlers(UPLOAD_URL, GET_INFO_URL, GET_ID_URL);
            vault.setFilesLimit(1);
            vault.strings.btnAdd = "添加更新包";
			vault.strings.btnUpload = "上传";
			vault.strings.btnClean = "清空";
			vault.strings.remove = "删除";
			vault.strings.done = "完成";
			vault.strings.error = "错误"; 
            vault.onAddFile = function(fileName) { 
				var ext = this.getFileExtension(fileName); 
				if (ext.toLowerCase() != "zip") {
					dhtmlx.message("只能上传ZIP文件！");
					return false;
				} else {
					return true;
				}
			};
			vault.onFileUploaded = function(file) { 
				dhtmlxAjax.get(MODEL_URL + "!getUploadMessage.json?updatePackageConfigKey=" + updatePackageConfigKey, function(loader) {
					dataWin.close();
					var result = eval("(" + eval(loader.xmlDoc.responseText) + ")");
					if (!result.success) {
						dhtmlx.message(result.message);
					} else {
						importUpdatePackage();
					}
				});
			};
            vault.create("vaultDiv");
            vault.setFormField("updatePackageConfigKey", updatePackageConfigKey);
		    dataWin.attachObject(vaultDiv);
        } else if (buttonName == "view") {
        	var selectId = dataGrid.getSelectedRowId();
            if (selectId == undefined) {
                dhtmlx.message(getMessage("select_record"));
                return;
            } else if (selectId.indexOf(',') != -1) {
                dhtmlx.message(getMessage("select_only_one_record"));
                return;
            }
        }
    });
}
/**
 * 导入更新包
 */
function importUpdatePackage() {
    if (!dhxWins) {
    	dhxWins = new dhtmlXWindows();
    }
	var msgWin = dhxWins.createWindow("msgWin", 0, 0, 200, 50);
    msgWin.setModal(true);
    msgWin.hideHeader();
    msgWin.center();
    msgWin.button("park").hide();
    msgWin.button("minmax1").hide();
    msgWin.button("minmax2").hide();
    msgWin.button("close").hide();
    var obj = document.getElementById("DIV-msg");
	if (obj == null) {
		obj = document.createElement("DIV");
		obj.setAttribute("id", "DIV-msg");
		obj.innerHTML = "正在处理，请稍后...";
		obj.setAttribute("style", "position:relative;top:35%;left:20%;font-size:11px;");
		document.body.appendChild(obj);
	}
	msgWin.attachObject(obj);
	var result = eval("(" + loadJson(MODEL_URL + "!execUpdatePackage.json?updatePackageConfigKey=" + updatePackageConfigKey) + ")");
	msgWin.close();
}
/**
 * 页面初始化方法
 */
function init() {
    dhxLayout = new dhtmlXLayoutObject("content", "1C");
    dhxLayout.cells("a").hideHeader();
    toolBar = dhxLayout.cells("a").attachToolbar();
    dataGrid = dhxLayout.cells("a").attachGrid();
    statusBar = dhxLayout.cells("a").attachStatusBar();
    initSelfToolBar();
    initSelfGrid();
}