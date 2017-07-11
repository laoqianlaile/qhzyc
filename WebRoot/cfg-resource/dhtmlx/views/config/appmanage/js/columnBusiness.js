
/**
 * 字段特殊业务字段处理 
 * @param {Object} zdtabbar
 */

function initTSYW(zdtabbar){
	MODEL_URL = AppActionURI.columnBusiness;
	var zz_From = zdtabbar.cells("tab$column$06").attachForm();
	zdtabbar.cells("tab$column$06").detachToolbar();
	var toolbar = zdtabbar.cells("tab$column$06").attachToolbar();
	toolbar.setIconsPath(TOOLBAR_IMAGE_PATH);
	toolbar.addButton("reset", 3, "新增", "new.gif");
	toolbar.addText("empty", 4, "    ");
	toolbar.addButton("submit", 1, "保存", "save.gif");
	toolbar.addSeparator("septr$01", 2);

	toolbar.attachEvent("onClick", function(id) {
			 if (id == "reset") {
				   initFormData(detailForm, true);
           		   t_Change();
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
				{type: "hidden", name: "type",value:"5", maxLength:50, inputWidth: 200,width: "200"},
				{type: "block", width: "600", list:[
					{type: "input", label: "名称:", name: "name", maxLength:50, inputWidth: 200,required: true,width: "200",tooltip:"请输入名称!"}
					
				]},
				     {type: "block", width: "1000", list:[
							{type: "combo", label: "卷内件号:", name: "itemColumnId", maxLength:25, required: true, tooltip:"请选择卷内件号字段!"},
							{type:"newcolumn"},
							{type: "combo", label: "页数:", name: "pagenoColumnId", maxLength:25, required: true, tooltip:"请选择页数字段!"},
								
							{type:"newcolumn"},
							{type: "combo", label: "页号:", name: "pagesColumnId", maxLength:25, required: true, tooltip:"请选择页号字段!"}
					]},
				 {type: "block", width: "1000", list:[
						{type: "radio", label: "由页数计算页号", name: "ptype",checked:"true" ,value:"0",labelWidth:100,offsetLeft:"20",labelAlign:"left",position:"label-right"},
						{type:"newcolumn"},
						{type: "radio", label: "由页号计算页数", name: "ptype",value:"1",labelWidth:100,offsetLeft:"20",labelAlign:"left",position:"label-right"}
					]}
	        ],
			settings: {labelWidth: 80, inputWidth: 100}
		};
	detailForm = zdtabbar.cells("tab$column$06").attachForm(initFormFormat(detailFormData));
	
	var dhxCombo_item = detailForm.getCombo("itemColumnId");
	var dhxCombo_pageno = detailForm.getCombo("pagenoColumnId");
	var dhxCombo_pages = detailForm.getCombo("pagesColumnId");
	
	dhxCombo_item.addOption(columns_data);
	dhxCombo_item.selectOption(0);
	
	dhxCombo_pageno.addOption(columns_data);
	dhxCombo_pageno.selectOption(0);
	
	dhxCombo_pages.addOption(columns_data);
	dhxCombo_pages.selectOption(0);
}