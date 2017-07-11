

/**
 * 字段拼接
 * @param {Object} zdtabbar
 */
function initZDPJ(zdtabbar){
	MODEL_URL = AppActionURI.columnSplice;
	
	var PJ_From = zdtabbar.cells("tab$column$01").attachForm();
	zdtabbar.cells("tab$column$01").detachToolbar();
	var toolbar = zdtabbar.cells("tab$column$01").attachToolbar();
	toolbar.setIconsPath(TOOLBAR_IMAGE_PATH);
	toolbar.addButton("reset", 3, "新增", "new.gif");
	toolbar.addText("empty", 4, "    ");
	toolbar.addButton("submit", 1, "保存", "save.gif");
	toolbar.addSeparator("septr$01", 2);

	toolbar.attachEvent("onClick", function(id) {
			 if (id == "reset") {
				   initFormData(detailForm, true);
				   var CN_Combo = detailForm.getCombo("columnNum");
				   CN_Combo.setComboValue(5);
           		   t_Change();
			}else if(id == "submit"){
				var cid = detailForm.getItemValue("id");
				if(cid == ""){
					SAVE_URL = MODEL_URL;
		        	detailForm.setItemValue("_method", "post");
	        		detailForm.send(SAVE_URL, "post", function(loader, response){
	        			var url = AppActionURI.columnRelation + "!getAllColumnRelationList.json?E_model_name=datagrid&tableId="+nodeId;
	        			initGridAgain(CRGrid, grid, url);
	        			dhtmlx.message(getMessage("save_success"));
	        			detailForm.setItemValue("name", "");
	        		});
				}else{
					SAVE_URL = MODEL_URL + "/" + cid;
	        		detailForm.setItemValue("_method", "put");
        			detailForm.send(SAVE_URL, "post", function(loader, response){
        				var surl = AppActionURI.columnRelation + "!getAllColumnRelationList.json?E_model_name=datagrid&tableId="+nodeId;
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
				{type: "hidden", name: "type",value:"0"},
				{type: "hidden", name: "columnRelationId",value:uId},
				{type: "block", width: "850", list:[
					{type: "input", label: "名称:", name: "name", maxLength:50, inputWidth: 200,required: true,width: "100",tooltip:"请输入名称!"},
					{type:"newcolumn"},
					{type: "combo", label: "&nbsp;&nbsp;存储字段:", name: "storeColumnId",labelWidth: 90, maxLength:50, width: 100,required: true, readonly: true, tooltip:"请输入存储字段!"},
					{type:"newcolumn"},
					{type: "combo", label: "段数：　",  name: "columnNum",readonly:"true",width: "100",
						 options:[  {value: "5", text: "5段",selected:"1"},
							 		{value: "4", text: "4段"},
								    {value: "3", text: "3段"},
								    {value: "2", text: "2段"},
								    {value: "1", text: "1段"}]
					},
					{type:"newcolumn"},
					{type: "input", label: "补位字符:", name: "fill", maxLength:1, inputWidth: 200,width: "100"}
				]},
				{type: "block", width: "850",   list:[
					{type: "itemlabel", label: "　", name: "placeholder_10", labelWidth: 71 + widthDiffer},
					{type:"newcolumn"},
					{type: "itemlabel", label: "字段1", name: "label_column1", labelWidth: 100, labelAlign : "left"},
					{type:"newcolumn"},
					{type: "itemlabel", label: "　", name: "placeholder_11", labelWidth: 32 + widthDiffer},
					{type:"newcolumn"},
					{type: "itemlabel", label: "字段2", name: "label_column2", labelWidth: 100, labelAlign : "left"},
					{type:"newcolumn"},
					{type: "itemlabel", label: "　", name: "placeholder_12", labelWidth: 32 + widthDiffer},
					{type:"newcolumn"},
					{type: "itemlabel", label: "字段3", name: "label_column3", labelWidth: 100, labelAlign : "left"},
					{type:"newcolumn"},
					{type: "itemlabel", label: "　", name: "placeholder_13", labelWidth: 32 + widthDiffer},
					{type:"newcolumn"},
					{type: "itemlabel", label: "字段4", name: "label_column4", labelWidth: 100, labelAlign : "left"},
					{type:"newcolumn"},
					{type: "itemlabel", label: "　", name: "placeholder_14", labelWidth: 32 + widthDiffer},
					{type:"newcolumn"},
					{type: "itemlabel", label: "字段5", name: "label_column5", labelWidth: 100, labelAlign : "left"},
					{type:"newcolumn"},
					{type: "itemlabel", label: "　", name: "placeholder_15", labelWidth: 60 + widthDiffer}
				]},
				{type: "block", width: "850",   list:[
					{type: "input", label: " ", name: "prefix", maxLength:50, labelWidth: 10,inputWidth: 50},
					{type:"newcolumn"},
					{type: "combo", label: "  ", name: "column1Id", maxLength:25,  labelWidth: 10, width: 100},
					{type:"newcolumn"},
					{type: "input", label: "  ",  name: "seperator1", maxLength:10, inputWidth:10,labelWidth: 10},
					{type:"newcolumn"},
					{type: "combo", label: "  ", name: "column2Id", maxLength:25,  labelWidth: 10, width: 100},
					{type:"newcolumn"},
					{type: "input", label: "  ", name: "seperator2", maxLength:10, inputWidth:10,labelWidth: 10},
					{type:"newcolumn"},
					{type: "combo", label: "  ", name: "column3Id", maxLength:25,  labelWidth: 10, width: 100},
					{type:"newcolumn"},
					{type: "input", label: "  ", name: "seperator3", maxLength:10, inputWidth:10,labelWidth: 10},
					{type:"newcolumn"},
					{type: "combo", label: "  ", name: "column4Id", maxLength:25,  labelWidth: 10, width: 100},
					{type:"newcolumn"},
					{type: "input", label: "  ", name: "seperator4", maxLength:10, inputWidth:10,labelWidth: 10},
					{type:"newcolumn"},
					{type: "combo", label: "  ", name: "column5Id", maxLength:25,  labelWidth: 10, width: 100},
					{type:"newcolumn"},
					{type: "input", label: "  ", name: "suffix", maxLength:50, labelWidth: 10,inputWidth:50},
				]},
				{type: "block", width: "850",   list:[
  					{type: "itemlabel", label: " ", name: "placeholder_30", labelWidth: 60 + widthDiffer},
  					{type:"newcolumn"},
  					{type: "input", label: "补位位数：", name: "fillingNum1", value: "", maxLength:1, labelWidth: 50, width: 60, validate:"ValidNumeric", tooltip:"请输入一位整数！"},
  					{type:"newcolumn"},
  					{type: "itemlabel", label: " ", name: "placeholder_31", labelWidth: 20 + widthDiffer},
  					{type:"newcolumn"},
  					{type: "input", label: "补位位数：", name: "fillingNum2", value: "", maxLength:1, labelWidth: 50, width: 60, validate:"ValidNumeric", tooltip:"请输入一位整数！"},
  					{type:"newcolumn"},
  					{type: "itemlabel", label: " ", name: "placeholder_32", labelWidth: 20 + widthDiffer},
  					{type:"newcolumn"},
  					{type: "input", label: "补位位数：", name: "fillingNum3", value: "", maxLength:1, labelWidth: 50, width: 60, validate:"ValidNumeric", tooltip:"请输入一位整数！"},
  					{type:"newcolumn"},
  					{type: "itemlabel", label: " ", name: "placeholder_33", labelWidth: 20 + widthDiffer},
  					{type:"newcolumn"},
  					{type: "input", label: "补位位数：", name: "fillingNum4", value: "", maxLength:1, labelWidth: 50, width: 60, validate:"ValidNumeric", tooltip:"请输入一位整数！"},
  					{type:"newcolumn"},
  					{type: "itemlabel", label: " ", name: "placeholder_34", labelWidth: 20 + widthDiffer},
  					{type:"newcolumn"},
  					{type: "input", label: "补位位数：", name: "fillingNum5", value: "", maxLength:1, labelWidth: 50, width: 60, validate:"ValidNumeric", tooltip:"请输入一位整数！"},
  					{type:"newcolumn"},
  					{type: "itemlabel", label: " ", name: "placeholder_35", labelWidth: 50 + widthDiffer}
  				]}
	        ],
			settings: {labelWidth: 80, inputWidth: 100}
		};
	detailForm = zdtabbar.cells("tab$column$01").attachForm(initFormFormat(detailFormData));
	//初始化各个combo的值
	var dhxCombo = detailForm.getCombo("storeColumnId");
	var dhxCombo_1 = detailForm.getCombo("column1Id");
	var dhxCombo_2 = detailForm.getCombo("column2Id");
	var dhxCombo_3 = detailForm.getCombo("column3Id");
	var dhxCombo_4 = detailForm.getCombo("column4Id");
	var dhxCombo_5 = detailForm.getCombo("column5Id");

	dhxCombo.addOption(columns_data);
	dhxCombo.selectOption(0);
	dhxCombo_1.addOption(columns_data);
	dhxCombo_1.selectOption(0);
	dhxCombo_2.addOption(columns_data);
	dhxCombo_2.selectOption(0);
	dhxCombo_3.addOption(columns_data);
	dhxCombo_3.selectOption(0);
	dhxCombo_4.addOption(columns_data);
	dhxCombo_4.selectOption(0);
	dhxCombo_5.addOption(columns_data);
	dhxCombo_5.selectOption(0);
}

/**
 * 字段拼接根据段数显示内容
 * @param detailForm
 * @param columnNum
 */
function columnNumberChange(detailForm, columnNum) {
	if (columnNum == 1) {
		detailForm.showItem("label_column1");
		detailForm.showItem("column1Id");
		detailForm.showItem("fillingNum1");

		detailForm.hideItem("label_column5");
		detailForm.hideItem("seperator4");
		detailForm.hideItem("column5Id");
		detailForm.hideItem("fillingNum5");

		detailForm.hideItem("label_column4");
		detailForm.hideItem("seperator3");
		detailForm.hideItem("column4Id");
		detailForm.hideItem("fillingNum4");

		detailForm.hideItem("label_column3");
		detailForm.hideItem("seperator2");
		detailForm.hideItem("column3Id");
		detailForm.hideItem("fillingNum3");

		detailForm.hideItem("label_column2");
		detailForm.hideItem("seperator1");
		detailForm.hideItem("column2Id");
		detailForm.hideItem("fillingNum2");
	} else if (columnNum == 2) {
		detailForm.showItem("label_column1");
		detailForm.showItem("column1Id");
		detailForm.showItem("fillingNum1");

		detailForm.showItem("label_column2");
		detailForm.showItem("seperator1");
		detailForm.showItem("column2Id");
		detailForm.showItem("fillingNum2");

		detailForm.hideItem("label_column5");
		detailForm.hideItem("seperator4");
		detailForm.hideItem("column5Id");
		detailForm.hideItem("fillingNum5");

		detailForm.hideItem("label_column4");
		detailForm.hideItem("seperator3");
		detailForm.hideItem("column4Id");
		detailForm.hideItem("fillingNum4");

		detailForm.hideItem("label_column3");
		detailForm.hideItem("seperator2");
		detailForm.hideItem("column3Id");
		detailForm.hideItem("fillingNum3");
	} else if (columnNum == 3) {
		detailForm.showItem("label_column1");
		detailForm.showItem("column1Id");
		detailForm.showItem("fillingNum1");

		detailForm.showItem("label_column2");
		detailForm.showItem("seperator1");
		detailForm.showItem("column2Id");
		detailForm.showItem("fillingNum2");

		detailForm.showItem("label_column3");
		detailForm.showItem("seperator2");
		detailForm.showItem("column3Id");
		detailForm.showItem("fillingNum3");

		detailForm.hideItem("label_column5");
		detailForm.hideItem("seperator4");
		detailForm.hideItem("column5Id");
		detailForm.hideItem("fillingNum5");

		detailForm.hideItem("label_column4");
		detailForm.hideItem("seperator3");
		detailForm.hideItem("column4Id");
		detailForm.hideItem("fillingNum4");
	} else if (columnNum == 4) {
		detailForm.showItem("label_column1");
		detailForm.showItem("column1Id");
		detailForm.showItem("fillingNum1");

		detailForm.showItem("label_column2");
		detailForm.showItem("seperator1");
		detailForm.showItem("column2Id");
		detailForm.showItem("fillingNum2");

		detailForm.showItem("label_column3");
		detailForm.showItem("seperator2");
		detailForm.showItem("column3Id");
		detailForm.showItem("fillingNum3");

		detailForm.showItem("label_column4");
		detailForm.showItem("seperator3");
		detailForm.showItem("column4Id");
		detailForm.showItem("fillingNum4");

		detailForm.hideItem("label_column5");
		detailForm.hideItem("seperator4");
		detailForm.hideItem("column5Id");
		detailForm.hideItem("fillingNum5");
	} else if (columnNum == 5) {
		detailForm.showItem("label_column1");
		detailForm.showItem("column1Id");
		detailForm.showItem("fillingNum1");

		detailForm.showItem("label_column2");
		detailForm.showItem("seperator1");
		detailForm.showItem("column2Id");
		detailForm.showItem("fillingNum2");

		detailForm.showItem("label_column3");
		detailForm.showItem("seperator2");
		detailForm.showItem("column3Id");
		detailForm.showItem("fillingNum3");

		detailForm.showItem("label_column4");
		detailForm.showItem("seperator3");
		detailForm.showItem("column4Id");
		detailForm.showItem("fillingNum4");

		detailForm.showItem("label_column5");
		detailForm.showItem("seperator4");
		detailForm.showItem("column5Id");
		detailForm.showItem("fillingNum5");
	}
	clearValueByColumnNum(detailForm, columnNum);
}
/**
 * 把对应隐藏栏位清空
 * @param detailForm
 * @param columnNum
 */
function clearValueByColumnNum(detailForm, columnNum) {
	if (1 == columnNum) {
		detailForm.setItemValue("seperator1", "");
		detailForm.setItemValue("column2Id", "");
		detailForm.setItemValue("fillingNum2", "");
		
		detailForm.setItemValue("seperator2", "");
		detailForm.setItemValue("column3Id", "");
		detailForm.setItemValue("fillingNum3", "");
		
		detailForm.setItemValue("seperator3", "");
		detailForm.setItemValue("column4Id", "");
		detailForm.setItemValue("fillingNum4", "");
		
		detailForm.setItemValue("seperator4", "");
		detailForm.setItemValue("column5Id", "");
		detailForm.setItemValue("fillingNum5", "");
	} else if (2 == columnNum) {
		detailForm.setItemValue("seperator2", "");
		detailForm.setItemValue("column3Id", "");
		detailForm.setItemValue("fillingNum3", "");
		
		detailForm.setItemValue("seperator3", "");
		detailForm.setItemValue("column4Id", "");
		detailForm.setItemValue("fillingNum4", "");
		
		detailForm.setItemValue("seperator4", "");
		detailForm.setItemValue("column5Id", "");
		detailForm.setItemValue("fillingNum5", "");
	} else if (3 == columnNum) {
		detailForm.setItemValue("seperator3", "");
		detailForm.setItemValue("column4Id", "");
		detailForm.setItemValue("fillingNum4", "");
		
		detailForm.setItemValue("seperator4", "");
		detailForm.setItemValue("column5Id", "");
		detailForm.setItemValue("fillingNum5", "");
	} else if (4 == columnNum) {
		detailForm.setItemValue("seperator4", "");
		detailForm.setItemValue("column5Id", "");
		detailForm.setItemValue("fillingNum5", "");
	}
}
