

/**
 * 字段继承
 * @param {Object} zdtabbar
 * @param {Object} treeName
 */
function initJC(zdtabbar,treeName){
	MODEL_URL = AppActionURI.columnOperation;
	var JC_From = zdtabbar.cells("tab$column$03").attachForm();
	zdtabbar.cells("tab$column$03").detachToolbar();
	var toolbar = zdtabbar.cells("tab$column$03").attachToolbar();
	toolbar.setIconsPath(TOOLBAR_IMAGE_PATH);
	toolbar.addButton("reset", 3, "新增", "new.gif");
	toolbar.addText("empty", 4, "    ");
	toolbar.addButton("submit", 1, "保存", "save.gif");
	toolbar.addSeparator("septr$01", 2);
	
	toolbar.attachEvent("onClick", function(id) {
			 if (id == "reset") {
				   initFormData(detailForm, true);
           		   t_Change();
           	       detailForm.setItemValue("mTable",treeName);
			}else if(id == "submit"){
				var cid = detailForm.getItemValue("id");
				if(cid == ""){
					SAVE_URL = MODEL_URL;
		        	detailForm.setItemValue("_method", "post");
			        var url = AppActionURI.columnRelation + "!getAllColumnRelationList.json?E_model_name=datagrid&tableId="+nodeId;
	        		detailForm.send(SAVE_URL, "post", function(loader, response){
	        			initGridAgain(CRGrid, grid, url);
	        			dhtmlx.message(getMessage("save_success"));
	        			detailForm.setItemValue("name", "");
	        		});
				}else{
					SAVE_URL = MODEL_URL + "/" + cid;
	        		detailForm.setItemValue("_method", "put");
	        		var surl = AppActionURI.columnRelation + "!getAllColumnRelationList.json?E_model_name=datagrid&tableId="+nodeId;
        			detailForm.send(SAVE_URL, "post", function(loader, response){
        				initGridAgain(CRGrid, grid, surl);
        				dhtmlx.message(getMessage("save_success"));
        			});
	        	}
			}
		});
	
		detailFormData = {
			format: [
				{type: "hidden", name: "_method"},
				{type: "hidden", name: "id"},
				{type: "hidden", name: "tableId",value:nodeId, maxLength:50, inputWidth: 200,width: "200"},
				{type: "hidden", name: "type",value:"0", maxLength:50, inputWidth: 200,width: "200"},
				{type: "block", width: "600", list:[
		        	{type: "input", label: "名        称:", name: "name", maxLength:50, inputWidth: 200,required: true,width: "150",tooltip:"请输入名称!"}
				]},
		        {type: "block", width: "600", list:[
					{type: "combo", label: "父表:", name: "originTableId", maxLength:25, required: true, readonly: true, width: "150"},
					{type: "combo", label: "父表字段:", name: "originColumnId", maxLength:50, width: "150", readonly: true, required: true},
					{type:"newcolumn"},
					{type: "input", label: "子表:&nbsp;&nbsp;&nbsp;", name: "mTable",value:treeName, maxLength:50,width: "150",readonly: true},
					{type: "combo", label: "子表字段:", name: "columnId", maxLength:25, required: true, width: "150"}
				]}
	        ],
			settings: {labelWidth: 80, inputWidth: 120}
		};
	detailForm = zdtabbar.cells("tab$column$03").attachForm(initFormFormat(detailFormData));
	//初始化combo,修改onchange事件,改变父表的内容
	var dhxCombo = detailForm.getCombo("originTableId");
	
	var url_1 = AppActionURI.tableRelation + "!queryRelationTableByTableID.json?tableId="+nodeId;
	var f_combo_data1 = loadJson(url_1);
	
	
	dhxCombo.addOption(f_combo_data1);
	dhxCombo.selectOption(0);
	//初始化combo,根据父表的选择更新父表字段的内容
	var dhxCombo_c = detailForm.getCombo("originColumnId");
	dhxCombo.attachEvent("onChange", function(){
		if(dhxCombo.getSelectedValue()!=""){
			setComboValue(dhxCombo_c, AppActionURI.columnDefine + "!search.json?F_in=showName,id&Q_EQ_tableId="+dhxCombo.getSelectedValue());
			//条件过滤
			var ulr_filter = AppActionURI.tableRelation + "!queryRelationCols.json?child_tid="+nodeId+"&parent_tid="+dhxCombo.getSelectedValue();
			filterCombo(ulr_filter,dhxCombo_c,detailForm.getCombo("columnId"));
		}
	});  
	var dhxCombo_ch = detailForm.getCombo("columnId");
	dhxCombo_ch.addOption(columns_data);
	dhxCombo_ch.selectOption(0);
}

