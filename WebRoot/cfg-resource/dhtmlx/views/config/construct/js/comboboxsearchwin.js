var comboboxSearchForm;
/**
 * 初始化下拉框检索配置窗口
 */
function initComboboxSearchWin(comboboxSearchWin) {
    comboboxSearchWin.setModal(true);
    comboboxSearchWin.setText("下拉框检索配置");
    comboboxSearchWin.center();
    var copyStatusBar = comboboxSearchWin.attachStatusBar();
    var copyToolBar = new dhtmlXToolbarObject(copyStatusBar);
    copyToolBar.setIconsPath(TOOLBAR_IMAGE_PATH);
    copyToolBar.setAlign("right");
    copyToolBar.addButton("save", 0, "保存", "save.gif");
    copyToolBar.addSeparator("bottom$septr$03", 1);
    copyToolBar.addButton("delete", 2, "删除", "delete.gif");
    copyToolBar.addSeparator("bottom$septr$03", 3);
    copyToolBar.addButton("bottom$close", 4, "关闭", "close.gif");
    copyToolBar.attachEvent("onClick", function(itemId) {
        if ("save" == itemId) {
		    var url = CONSTRUCT_DETAIL_MODEL_URL + "!saveComboboxSearch.json";
    		comboboxSearchForm.send(url, "post", function(loader, response) {
				var comboboxSearchFormData = eval("(" + loader.xmlDoc.responseText + ")");
				if (comboboxSearchFormData.id == null || comboboxSearchFormData.id == "") {
					dhtmlx.message(getMessage("save_failure"));
					return;
				}
				comboboxSearchForm.setFormData(comboboxSearchFormData);
				dhtmlx.message(getMessage("save_success"));
				constructDetailGridLoadData();
			});
    	} else if ("delete" == itemId) { 
    		var id = comboboxSearchForm.getItemValue("id");
    		if (isEmpty(id)) {
    			dhtmlx.message("配置未保存，无需删除！");
    			return;
    		}
    		var url = CONSTRUCT_DETAIL_MODEL_URL + "!destroy.json?id=" + id;
    		var rlt = loadJson(url);
    		if (rlt.success) {
    			dhtmlx.message(getMessage("delete_success"));
    			comboboxSearchForm.setItemValue("id", "");
    			comboboxSearchForm.setItemValue("buttonName", "");
    			comboboxSearchForm.setItemValue("buttonDisplayName", "");
    			comboboxSearchForm.setItemValue("searchComboOptions", "");
    			constructDetailGridLoadData();
    		} else {
    			dhtmlx.message(getMessage("delete_failure"));
    		}
    	} else {
    		comboboxSearchWin.close();
    	}
    });
    var colName = {type: "input", label: "字段名称", labelAlign:"right", labelWidth: 80, name: "buttonName", required:true, width:300, maxLength:25, tooltip:"请输入字段英文名称!"};
    var isCommonReserveZone = "1";
    if (currentReserveZoneName.indexOf("GRID") == -1) {
    	isCommonReserveZone = "0";
    	var strs = currentReserveZoneName.split("_");
        var tableId = "";
        if (strs && strs.length && strs.length >= 4) {
            tableId = strs[2];
        }
    	var opts = loadJson(contextPath + "/appmanage/column-define!comboOfTableColumns.json?P_optionValue=1&P_tableId=" + tableId);
    	colName = {type: "combo", label: "字段名称", labelAlign:"right", labelWidth: 80, name: "buttonName", required:true, width:300, tooltip:"请输入请选择字段!",
    			options: opts};
    }
    var comboboxSearchFormJson = [
   		{type: "setting", labelWidth: 80, inputWidth: 300},
   		{type: "hidden", name: "_method"},
   		{type: "hidden", name: "id"},
        {type: "hidden", name: "constructId", value: currentConstructIdOfTree},
        {type: "hidden", name: "reserveZoneId", value: currentReserveZoneId},
        {type: "hidden", name: "isCommonReserveZone", value: isCommonReserveZone},
        {type: "hidden", name: "showOrder", value: "0"},
        {type: "hidden", name: "buttonCode", value: "COMBOBOX_SEARCH"},
        {type: "hidden", name: "buttonSource", value: "0"},
        {type: "hidden", name: "buttonType", value: "0"},
        {type: "hidden", name: "position", value: "2"},
   		{type: "block", name:"block_alias", width: 400, list:[
	        {type: "input", label: "标题名称：", labelAlign:"right", labelWidth: 80, name: "buttonDisplayName", offsetTop:20, width:300, maxLength:25, tooltip:"请输入标题名称!"}                                              
	    ]},
   		{type: "block", name:"block_name" , width: 400, list:[colName]},
   		{type: "block", name: "block_remark", width: 400, list:[
			{type: "input", label: "下拉选项", labelAlign:"right", labelWidth: 80, name: "searchComboOptions", required:true, rows:10, width:300, maxLength:100}
  		]},
       	{type: "block", name: "block_remark", width: 400, list:[
			{type: "itemlabel", label: "<font color=\"red\">下拉选项格式(隐藏值与显示值用英文冒号(:)分隔，多个选项用英文分号(;)分隔)：，如：:请选择;0:不同意;1:同意</font>"}
		]}
   	];
    comboboxSearchForm = comboboxSearchWin.attachForm(comboboxSearchFormJson);
    var comboboxSearchFormData = loadJson(CONSTRUCT_DETAIL_MODEL_URL + "!showComboboxSearch.json?constructId=" + currentConstructIdOfTree + 
    		"&reserveZoneId=" + currentReserveZoneId);
    if (isNotEmpty(comboboxSearchFormData)) {
    	comboboxSearchForm.setFormData(comboboxSearchFormData);
    }
}