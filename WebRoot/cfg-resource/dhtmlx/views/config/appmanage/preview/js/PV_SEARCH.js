
/**
 * 展现列表检索区配置
 * @param that
 * @param layout
 * @param tableId
 * @param scfg
 */
function PV_search_area(that, layout, tableId, scfg) {
	var subLayout = layout.attachLayout("2E");
	subLayout.setCollapsedText("a", "检索区");
	var sLayout = subLayout.cells("a");
	var gLayout = subLayout.cells("b");
	sLayout.setText("检索区");
	gLayout.hideHeader();
	if (1 == scfg.area) {
		sLayout.setHeight(scfg.height);
		sLayout.collapse();
		that.sform = PV_b_search(that, sLayout, scfg, tableId);
	} else if (2 == scfg.area) {
		sLayout.setHeight(210);
		sLayout.collapse();
		PV_g_search(that, sLayout, tableId);
	} else if (3 == scfg.area) {
		sLayout.setHeight(scfg.height + 30);
		sLayout.collapse();
		var tabbar = sLayout.attachTabbar();
		tabbar.setImagePath(IMAGE_PATH);
		tabbar.addTab("grid$search$b", "基本检索", "120px");
		tabbar.addTab("grid$search$g", "高级检索", "120px");
		that.sform = PV_b_search(that, tabbar.cells("grid$search$b"), scfg, tableId);
		PV_g_search(that, tabbar.cells("grid$search$g"), tableId);
		// 默认为基本检索
		tabbar.setTabActive("grid$search$b");
		// tab页切换事件
		tabbar.attachEvent("onSelect", function (id, last_id) {
			if ("grid$search$b" == id) {
				sLayout.setHeight(scfg.height + 30);
			} else if ("grid$search$g" == id) {
				sLayout.setHeight(240);
			}
			return true;
		});
	}
	that.sLayout = sLayout;
	that.gLayout = gLayout;
}
/**
 * 高级检索区
 * @param that
 * @param sLayout
 * @param tableId
 */
function PV_g_search(that, sLayout, tableId) {
	var pGId = PV_P_GridId(tableId);
	var colOpts = loadJson(CFG_app_uri + "/column-define!comboOfTableColumns.json?P_optionValue=1&P_tableId=" + tableId);
	var conOpts = [{value:"LIKE", text: "包含", selected: true},
	               {value:"EQ", text: "等于"},
	               {value:"GT", text: "大于"},
	               {value:"GTE", text: "大于等于"},
	               {value:"LT", text: "小于"},
	               {value:"LTE", text: "小于等于"},
	               {value:"NOT", text: "不等于"}];
	var defOpts = loadJson(CFG_app_uri + "/app-search-condition!userCondition.json?P_tableId=" + tableId + "&P_moduleId=" + that.moduleId);
	var formJson = [
	    {type: "fieldset",  name: "area_1", label: "高级检索", width:300, list:[
			{type:"settings", position:"label-left", labelAlign: "right", labelWidth: 80},
            {type: "combo", label: " 　字段：", name: "columnName", width:200, options: colOpts},
            {type: "combo", label: " 　条件：", name: "condition", width:200, options: conOpts},
            {type: "input", label: " 　　值：", name: "value", width:200},
            {type: "button", width: 60, name:"add",value:"添加", offsetLeft: 220},
			{type: "combo", label: "检索条件：", name: "defineCondition", width:200, options: defOpts, tooltip: "在下拉框直接输入检索条件名称，再点击“检索保存”可以保存已定义的查询条件"},
            {type: "block", width: 300, list:[
				     {type: "button", width: 60, name:"defineSave",value:"检索保存", offsetLeft: 155},
		             //{type: "newcolumn"},
				     //{type: "button", width: 60, name:"defineModify",value:"检索修改	"},
		             {type: "newcolumn"},
				     {type: "button", width: 60, name:"defineDelete",value:"检索删除"}
			]}
        ]},
        {type: "newcolumn"},
	    {type: "fieldset",  name: "area_2", label: "已定义的查询条件列表", width:460, list:[
             {type: "block", width: 450, list:[
                 {type: "multiselect", name: "conditionlist", inputHeight:119, inputWidth:445, options: []}]},
             {type: "block", width: 450, list:[
                 {type: "button", width: 60, name:"search",value:"查询", offsetLeft: 0},
                 {type: "newcolumn"},
                 {type: "button", width: 60, name:"delete",value:"删除", offsetLeft: 10},
		         {type: "newcolumn"},
		         {type: "button", width: 60, name:"or",value:"或者", offsetLeft: 10},
		         {type: "newcolumn"},
		         {type: "button", width: 60, name:"and",value:"并且", offsetLeft: 10},
		         {type: "newcolumn"},
			     {type: "button", width: 60, name:"makeup",value:"组合", offsetLeft: 10},
		         {type: "newcolumn"},
			     {type: "button", width: 60, name:"split",value:"拆分", offsetLeft: 10}/*,
		         {type: "newcolumn"},
			 ]},
             {type: "block", width: 450, list:[
		         {type: "button", width: 60, name:"save",value:"保存"},
	             {type: "newcolumn"},
	             {type: "button", width: 60, name:"search",value:"查询", offsetLeft: 10},//*/
			 ]},
        ]}/*,
        {type: "newcolumn"},
	    {type: "fieldset",  name: "area_3", label: "已定义检索条件", width:260, list:[
			{type:"settings", position:"label-left", labelAlign: "right", labelWidth: 60, offsetTop:"5"},
			{type: "combo", label: "检索条件：", name: "defineCondition", width:160, options: []},
            {type: "newcolumn"},
            {type: "block", width: 240, list:[
				     {type: "button", width: 60, name:"defineModify",value:"检索修改", offsetLeft: 65},
		             {type: "newcolumn"},
				     {type: "button", width: 60, name:"defineDelete",value:"检索删除"},
				 ]}
	        ]}//*/
    ];
	var con = {code:{and:"AND", or:"OR"}, name: {and:"并且", or:"或者"}, brakets: {left:"(", right:")"}, space: " "};
	
	var gsForm = sLayout.attachForm(formJson);
	gsForm.attachEvent("onChange", function(id, value) {
		if ("defineCondition" == id) {
			if ("" == value.trim()) return;
			var multisel = gsForm.getSelect("conditionlist");
			for (var i = 0; i < defOpts.length; i++) {
				var defOption = defOpts[i];
				if ("value" in defOption && defOption.value == value) {
					multisel.options.length = 0;
					var conStr = defOption.prop1;
					var conArr = conStr.split(";");
					for (var j = 0; j < conArr.length; j++) {
						var optArr = conArr[j].split(",");
						multisel.options.add(new Option(optArr[1], optArr[0]));
					}
					break;
				}
			}
		}
	});
	gsForm.attachEvent("onButtonClick", function(id) {
		if (id == "add") {
			var colCombo = gsForm.getCombo("columnName");
			var conCombo = gsForm.getCombo("condition");
			var val = gsForm.getItemValue("value");
			if ("" == val) {
				alert("请输入值！");
				return;
			}
			var multisel = gsForm.getSelect("conditionlist");
			var len  = multisel.options.length;
			var optVal = con.brakets.left + conCombo.getSelectedValue() + CFG_oc_split + colCombo.getSelectedValue() + CFG_cv_split + val + con.brakets.right;
			var optTxt = con.brakets.left + colCombo.getSelectedText() + con.space + conCombo.getSelectedText() + con.space + val + con.brakets.right;
			if (len > 0) {
				optVal = con.code.and + optVal;
				optTxt = con.name.and + optTxt; 
			}
			multisel.options.add(new Option(optTxt, optVal));
		} else if (id == "and") {
			var multisel = gsForm.getSelect("conditionlist");
			if (-1 === operationMessage(multisel)) return;
			for (var i = 0; i < multisel.options.length; i++) {
				var opt = multisel.options[i];
				if (opt.selected) {
					if (0 == opt.value.indexOf(con.code.or)) {
						opt.value = opt.value.replace(con.code.or, con.code.and);
					}
					if (0 == opt.text.indexOf(con.name.or)) {
						opt.text  = opt.text.replace(con.name.or, con.name.and);
					}
				}
			}
			
		} else if (id == "or") {
			var multisel = gsForm.getSelect("conditionlist");
			if (-1 === operationMessage(multisel)) return;
			for (var i = 0; i < multisel.options.length; i++) {
				var opt = multisel.options[i];
				if (opt.selected) {
					if (0 == opt.value.indexOf(con.code.and)) {
						opt.value = opt.value.replace(con.code.and, con.code.or);
					}
					if (0 == opt.text.indexOf(con.name.and)) {
						opt.text  = opt.text.replace(con.name.and, con.name.or);
					}
				}
			}
		} else if (id == "delete") {
			var multisel = gsForm.getSelect("conditionlist");
			if (-1 === operationMessage(multisel)) return;
			for (var i = 0; i < multisel.options.length; i++) {
				var opt = multisel.options[i];
				if (opt.selected) {
					multisel.options.remove(i);
				}
			}
			if (multisel.options.length > 0) {
				var opt = multisel.options[0];
				if (0 == opt.text.indexOf(con.name.and)) {
					opt.value = opt.value.replace(con.code.and, "");
					opt.text  = opt.text.replace(con.name.and, "");
				} else if (0 == opt.text.indexOf(con.name.or)) {
					opt.value = opt.value.replace(con.code.or, "");
					opt.text  = opt.text.replace(con.name.or, "");
				}
			}
		} else if (id == "makeup") {
			conditionMakeup();
		} else if (id == "split") {
			conditionSplit();
		} else if (id == "defineSave") {
			defineSave();
		} else if (id == "search") {
			search();
		} else if (id == "defineModify") {
			defineModify();
		} else if (id == "defineDelete") {
			defineDelete();
		}
	});
	// 操作信息提示
	function operationMessage(multisel) {
		if (multisel.options.length < 1) {
			dhtmlx.alert("条件列表中没有条件，请先添加条件！");
			return -1;
		}
		if (multisel.options.selectedIndex < 0) {
			dhtmlx.alert("请先选择条件，再操作！");
			return -1;
		}
	}
	// 条件合并
	function conditionMakeup() {
		var multisel = gsForm.getSelect("conditionlist");
		if (-1 === operationMessage(multisel)) return;
		var val = "", txt = "", idx = -1, isFirst = true;
		for (var i = 0; i < multisel.options.length; i++) {
			var opt = multisel.options[i];
			if (!opt.selected) continue;
			val += opt.value;
			txt += opt.text;
			if (!isFirst) {
				multisel.options.remove(i); --i;
			} else {
				idx = i;
				isFirst = false;
				if (0 == val.indexOf(con.code.and)) val = val.replace(con.code.and, "");
				else if (0 == val.indexOf(con.code.or)) val = val.replace(con.code.or, "");
				if (0 == txt.indexOf(con.name.and)) txt = txt.replace(con.name.and, "");
				else if (0 == txt.indexOf(con.name.or)) txt = txt.replace(con.name.or, "");
			}
		}
		val = idx > 0 ? con.code.and + con.brakets.left + val + con.brakets.right : con.brakets.left + val + con.brakets.right;
		txt = idx > 0 ? con.name.and + con.brakets.left + txt + con.brakets.right : con.brakets.left + txt + con.brakets.right;
		multisel.options[idx].value = val;
		multisel.options[idx].text  = txt;
	}
	// 条件拆分
	function conditionSplit() {
		var multisel = gsForm.getSelect("conditionlist");
		if (-1 === operationMessage(multisel)) return;
		for (var i = 0; i < multisel.options.length; i++) {
			var opt = multisel.options[i];
			if (!opt.selected) continue;
			var val = opt.value, txt = opt.text;
			while (txt.indexOf("((") > -1) {
				val = val.replace(/\(\(/g, "(").replace(/\)\)/g, ")");
				txt = txt.replace(/\(\(/g, "(").replace(/\)\)/g, ")");
			}
			var optArr = [];
			
			while (true) {
				var vIdx = val.indexOf(")"), tIdx = txt.indexOf(")");
				var oneVal = val.substring(0, vIdx + 1), oneTxt = txt.substring(0, tIdx + 1);
				optArr.push(new Option(oneTxt, oneVal));
				if (vIdx == (val.length - 1)) break;
				val = val.substring(vIdx + 1);
				txt = txt.substring(tIdx + 1);
			}
			replaceOption(multisel, optArr, i);
		}
	}
	/**
	 * 替换下拉框选项
	 * @param multisel --下拉框对象
	 * @param optArr   --下拉框选项
	 * @param pos      --位置
	 */
	function replaceOption(multisel, optArr, pos) {
		if (pos < multisel.options.length - 1) {
			for (var i = pos + 1; i < multisel.options.length; i++) {
				optArr.push(multisel.options[i]);
			}
			multisel.options.length = pos;
		}
		for (var i = 0; i < optArr.length; i++) {
			multisel.options.add(optArr[i]);
		}
	}
	// 查询
	function search() {
		var multisel = gsForm.getSelect("conditionlist");
		var filter = "";
		for (var i = 0; i < multisel.options.length; i++) {
			filter += multisel.options[i].value;
		}
		that[pGId].PV_DATA_seach_type = PV_common.S_GREAT;
		that[pGId].PV_DATA_g_seach    = filter; 
		that[pGId].PV_GRID_reload();
	}
	// 保存或修改检索条件
	function defineSave() {
		var multisel = gsForm.getSelect("conditionlist");
		if (multisel.options.length < 1) {
			dhtmlx.alert("条件列表中没有条件，请先添加条件！");
			return;
		}
		var condition = "";
		for (var i = 0; i < multisel.options.length; i++) {
			condition += ";" + multisel.options[i].value + "," + multisel.options[i].text;
		}
		condition = condition.substring(1);
		var id   = gsForm.getCombo("defineCondition").getActualValue();
		//alert("id = " + id);
		var name = gsForm.getCombo("defineCondition").getComboText();
		//alert("name=" + name);
		if (isEmpty(id) || "-1" == id.trim() || isEmpty(name) || "" == name.trim()) {
			dhtmlx.alert("请在检索条件下拉框中输入检索条件名称再保存！");
			return;
		}
		var isSave = (id == name);
		/*var cUrl = CFG_app_uri + "/app-search-condition!checkUnique.json?id=" + id 
				+ "&EQ_name=" + name 
				+ "&EQ_tableId=" + tableId 
				+ "&EQ_moduleId=" + that.moduleId
				+ "&EQ_userId=" + userId;
		var rlt = loadJson(encodeURI(cUrl));
		if (rlt.success == false) {
			dhtmlx.alert("检索条件名称重复请修改！");
			return;
		}//*/
		dhtmlx.confirm({
			type:"confirm",
			text: (isSave ? "确定<font color=\"red\">新增</font>检索条件吗？" : "确定要<font color=\"red\">修改</font>检索条件(" + name + ")吗？"),
			ok: "确定",
			cancel: "取消",
			callback: function(flag) {
				if (!flag) { return; }
				var url = CFG_app_uri + "/app-search-condition!save.json?tableId=" + tableId 
						+ "&moduleId=" + that.moduleId
						+ "&condition=" + condition
						+ "&name=" + name;
				if (!isSave) url += "&id=" + id;
				dhtmlxAjax.get(encodeURI(url), function(loader) {
					var entity = eval("(" + loader.xmlDoc.responseText + ")");
					if (entity.id) {
						if (isSave) {
							gsForm.getCombo("defineCondition").addOption([{value: entity.id, text: entity.name, selected: true}]);
							gsForm.getCombo("defineCondition").deleteOption(name);
							gsForm.setItemValue("defineCondition", entity.id);
							defOpts.push({value: entity.id, text: entity.name, prop1: entity.condition});
						} else {
							updateDefOpts(entity);
						}
				    	dhtmlx.message(getMessage("save_success"));
				    } else {
				    	dhtmlx.message(getMessage("save_failure"));
				    }
				});
			}
		});//*/
	}
	// 删除检索条件
	function defineDelete() {
		var id   = gsForm.getCombo("defineCondition").getActualValue();
		//alert("id = " + id);
		var name = gsForm.getCombo("defineCondition").getComboText();
		//alert("name=" + name);
		if (isEmpty(id) || "-1" == id) {
			dhtmlx.alert("请在检索条件下拉框中选择检索条件，再删除！");
			return;
		}
		dhtmlx.confirm({
			type:"confirm",
			text: "确定要删除检索条件(" + name + ")吗？",
			ok: "确定",
			cancel: "取消",
			callback: function(flag) {
				if (!flag) { return; }
				var url = CFG_app_uri + "/app-search-condition!destroy.json?id=" + id;
				dhtmlxAjax.get(encodeURI(url),function(loader){
					var rlt = eval("(" + loader.xmlDoc.responseText + ")");
					if (rlt.success) {
						gsForm.getCombo("defineCondition").deleteOption(id);
						gsForm.setItemValue("defineCondition", "");
						var multisel = gsForm.getSelect("conditionlist");
						multisel.options.length = 0;
						deleteDefOpts(id);
				    	dhtmlx.message(getMessage("delete_success"));
				    } else {
				    	dhtmlx.message(getMessage("delete_failure"));
				    }
				});
			}
		});
	}
	// 更新指定defOpts内容
	function updateDefOpts(entity) {
		for (var i = 0; i < defOpts.length; i++) {
			var defOption = defOpts[i];
			if (entity.id == defOption.value) {
				defOpts[i] = {text: entity.name, value: entity.id, prop1: entity.condition};
				break;
			}
		}
	}
	// 删除指定defOpts内容
	function deleteDefOpts(id) {
		for (var i = 0; i < defOpts.length; i++) {
			var defOption = defOpts[i];
			if (id == defOption.value) {
				deleteByIndex(defOpts, i);
				break;
			}
		}
	}
	// 删除指定索引index数组中数据
	function deleteByIndex(arr, index) {
		if (!(arr instanceof Array)) return [];
		if (index > arr.length) return arr;
		if (index == 0) arr.shift();
		for (var i = index; i > 0 && i < arr.length - 1; i++) {
			arr[i] = arr[i+1];
		}
		if (index == arr.length - 1) arr.pop();
		return arr;
	}
}
/**
 * 基本检索区
 * @param that
 * @param sbLayout
 * @param scfg
 * @returns
 */
function PV_b_search(that, sbLayout, scfg, tableId) {
	var pGId = PV_P_GridId(tableId);
	var formJson = eval("(" + scfg.formJson + ")");
	var form = sbLayout.attachForm(formJson);
	form.attachEvent("onButtonClick", function (id) {
		if ("search" == id) {
			that[pGId].PV_DATA_seach_type = PV_common.S_BASE;
			that[pGId].PV_GRID_reload();
		} else if ("reset" == id) {
			initFormData(form);
		}
	});
	return form;
}