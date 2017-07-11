



//字段截取
function initZDJQ(zdtabbar){
	MODEL_URL = AppActionURI.columnSplit;
	var JQ_From = zdtabbar.cells("tab$column$02").attachForm();
	zdtabbar.cells("tab$column$02").detachToolbar();
	var toolbar = zdtabbar.cells("tab$column$02").attachToolbar();
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
				var startPosition = detailForm.getItemValue("startPosition");
				var endPosition = detailForm.getItemValue("endPosition");
				if(startPosition>=endPosition){
					dhtmlx.alert("输入截取位数时值为整数且左边的值要比右边的小!");
					return;
				}
				if(cid == ""){
					SAVE_URL = MODEL_URL;
		        	detailForm.setItemValue("_method", "post");
			        var url = AppActionURI.columnRelation + "!getAllColumnRelationList.json?E_model_name=datagrid&tableId="+nodeId;
	        		detailForm.send(SAVE_URL, "post", function(loader, response){
	        			initGridAgain(CRGrid, grid, url);
	        			dhtmlx.message(getMessage("save_success"));
	        			detailForm.setItemValue("name","");
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
		        {type: "block", width: "600", list:[
		        	{type: "input", label: "名        称:", name: "name", maxLength:50, inputWidth: 200,required: true,width: "150",tooltip:"请输入名称!"},
					{type: "combo", label: "选取字段:", name: "fromColumnId", maxLength:25, required: true, tooltip:"请选择字段!" },
				]},
				{type: "block", width: "600", list:[
					
					{type: "input", label: "左边第：&nbsp;&nbsp;", name: "startPosition", maxLength:4,inputWidth: 40,validate:"validPositiveInteger",width: "40",tooltip:"请输入开始截取位数，为整数，第一位为1而不是0!"},
					{type:"newcolumn"},
					{type: "input", label: "位到第&nbsp;&nbsp;", name: "endPosition", maxLength:4,labelWidth: 55,inputWidth: 40,validate:"validPositiveInteger",tooltip:"请输入结束截取位数，为整数!"},
					{type:"newcolumn"},
					{type: "itemlabel", label: "位",labelWidth: 15}
				]},
				{type: "block", width: "600", list:[
					{type: "combo", label: "目标字段:", name: "toColumnId", maxLength:25, required: true, tooltip:"请选择目标字段!" },
				]}
	        ],
			settings: {labelWidth: 80, inputWidth: 150}
		};
	detailForm = zdtabbar.cells("tab$column$02").attachForm(initFormFormat(detailFormData));

	var dhxCombo = detailForm.getCombo("fromColumnId");
	var dhxCombo_1 = detailForm.getCombo("toColumnId");
	dhxCombo.addOption(columns_data);
	dhxCombo.selectOption(0);
	dhxCombo_1.addOption(columns_data);
	dhxCombo_1.selectOption(0);
}
/**
 * 正整数
 */
function validPositiveInteger(text) {
    var reg = new RegExp(/^[1-9]\d*$/);
    return reg.test(text);
}