
/**
 * 字段最值计算
 * @param {Object} zdtabbar
 * @param {Object} treeName
 */

function initZZJS(zdtabbar,treeName){
	MODEL_URL = AppActionURI.columnOperation;
	var zz_From = zdtabbar.cells("tab$column$05").attachForm();
	zdtabbar.cells("tab$column$05").detachToolbar();
	var toolbar = zdtabbar.cells("tab$column$05").attachToolbar();
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
				var columnId = detailForm.getItemValue("columnId");
				var result = eval("(" + loadJson(MODEL_URL + "!validateMostValue.json?columnId=" + columnId) + ")");
				if (typeof result == 'string') {
                    result = eval("(" + result + ")");
                }
                if (result.isCode && !result.hasMostValueShowOrder) {
                    dhtmlx.message("子表字段为编码型，请设置该编码的最值顺序！");
                }
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
				{type: "hidden", name: "type",value:"2", maxLength:50, inputWidth: 200,width: "200"},
				{type: "block", width: "600", list:[
		        	{type: "input", label: "名        称:", name: "name", maxLength:50, inputWidth: 200,required: true,width: "150",tooltip:"请输入名称!"}
				]},
				     {type: "block", width: "1000", list:[
							{type: "combo", label: "父表:", name: "originTableId", maxLength:25, required: true,width: "150", readonly: true},
							{type: "combo", label: "父表字段:", name: "originColumnId", maxLength:50, width: "150",required: true, readonly: true,width: "150"},
							{type:"newcolumn"},
							{type: "input", label: "子表:&nbsp;&nbsp;&nbsp;", name: "mTable",value:treeName, maxLength:50,width: "150",readonly: true},
							
							{type: "combo", label: "子表字段:", name: "columnId", maxLength:25, required: true, width: "150"},
						 	{type:"newcolumn"},
							{type: "radio", label: "最小", name: "operator",value:"0",checked:"true" ,offsetLeft:"50",labelAlign:"left",position:"label-right"},
							{type:"newcolumn"},
							{type: "radio", label: "最大", name: "operator",value:"1",labelWidth: 50,labelAlign:"left",position:"label-right"},
						]},
					 {type:"newcolumn"},
					{type: "label", label: "注：选取的目标字段需要排除表关系的绑定字段",labelWidth: 300,offsetLeft:"30"}
	        ],
			settings: {labelWidth: 80, inputWidth: 120}
		};
	detailForm = zdtabbar.cells("tab$column$05").attachForm(initFormFormat(detailFormData));
	
	//初始化combo,修改onchange事件,改变父表的内容
	var dhxCombo = detailForm.getCombo("originTableId");
	dhxCombo.addOption(f_combo_data);
	dhxCombo.selectOption(0);
	//初始化combo,根据父表的选择更新父表字段的内容
	var dhxCombo_c = detailForm.getCombo("originColumnId");
	dhxCombo.attachEvent("onChange", function(){
		if(dhxCombo.getSelectedValue()!=""){
			setComboValue(dhxCombo_c,contextPath +"/appmanage/column-define!search.json?F_in=showName,id&Q_EQ_tableId="+dhxCombo.getSelectedValue());
		}
	});  
	var dhxCombo_ch = detailForm.getCombo("columnId");
	dhxCombo_ch.addOption(columns_data);
	dhxCombo_ch.selectOption(0);
}