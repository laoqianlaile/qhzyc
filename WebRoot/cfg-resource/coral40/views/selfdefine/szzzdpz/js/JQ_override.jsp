<%@page language="java" pageEncoding="UTF-8"%>
<script type="text/javascript">
/***************************************************!
 * @author qiucs 
 * @date   2014-7-15
 * 系统配置平台应用自定义二次开发JS模板 
 ***************************************************/
 
 
(function(subffix) {

/**
 * 二次开发定位自己controller
 * @returns {String}
 **/
window[CFG_actionName(subffix)] = function (ui) {
	// ui.assembleData 就是 configInfo
	return $.contextPath + "/szzzdpz";
};
	

/**
 * 二次开发：复写自定义表单
 */
function _override_form (ui) {
	ui._init = function() {
		var qybmAndQymc = $.loadJson($.contextPath + "/trace!getQybmAndQymc.json?sysName=ZZ");
		var plbh = $.loadJson($.contextPath + "/sczzpzxx!getPlbh.json");
		//ui.getItemJQ("PLBH").textbox("setValue",plbh);
		ui.getItemJQ("QYBM").textbox("setValue",qybmAndQymc.qybm);
		ui.getItemJQ("QYMC").textbox("setValue",qybmAndQymc.qymc);
	};
	ui.bindEvent = function (){
		//品类下拉列表
		var pl = ui.getItemJQ("PL");
		
		pl.combogrid("option","onChange",function(e,data){
			var text = data.text;
			var value = data.value;
			var spbmData =  $.loadJson($.contextPath+"/szzzdpz!getSpbm.json?pl="+text);
			ui.setFormData({
				PLBH:spbmData.data[0].SPBM,
				PL:text
			});
		var plbh = ui.getItemValue("PLBH");
		var uniqueData =  $.loadJson($.contextPath+"/sczzpzxx!uniqueCheck.json?plbh="+plbh);
		if(uniqueData){
			CFG_message("品类编号已存在","warning");
			ui.getItemJQ("PLBH").textbox("setValue","");
		}
		});

	};
	ui.afterSave = function (entity, op) {
		var plbh = $.loadJson($.contextPath + "/sczzpzxx!getPlbh.json");
		ui.getItemJQ("PLBH").textbox("setValue",plbh);
		ui.assembleData.parentConfigInfo.namespace.addLeftNode({
			id : entity.PLBH,
			name : entity.PL,
			pzjd : "pzjd"
		});
	};
	//设置品类下拉列表
	ui.addCallback("setComboGridValue",function(o){
		if( null == o ) return ;
		var obj = o.rowData;
		if( null == obj ) return ;
		ui.setFormData({PL:obj.SPMC,PLBH:obj.SPBM});
		var uniqueData =  $.loadJson($.contextPath+"/sczzpzxx!uniqueCheck.json?plbh="+obj.SPBM);
		if(uniqueData){
			CFG_message("品类编号已存在","warning");
			ui.getItemJQ("PLBH").textbox("setValue","");
		}
	});
	// ui.assembleData 就是 configInfo
	//console.log("override grid!");
	//ui.getAction = function () { 
	//	return $.contextPath + "/appmanage/show-module";
	//};
};
/**
 *  二次开发：复写自定义列表
 */
function _override_grid (ui) {
	//复写删除方法
	ui.databaseDelete = function(idArr, isLogicalDelete) {
		//存在品种信息的品类
		var pls = $.loadJson($.contextPath + "/szzzdpz!hasPz.json?plids="+idArr.join(","));
		if (!$.isEmptyObject(pls)) {
			CFG_message("品类【"+pls.join("、")+"】底下存在品种信息，无法删除","warning");
			return;
		}
		var plbhs = [];
		for (var i in idArr) {
			var rowData = ui.uiGrid.grid("getRowData", idArr[i]);
			plbhs.push(rowData.PLBH);
		}
		var url = ui.getAction() + "!destroy.json?P_tableId=" + ui.options.tableId +
						"&P_D_tableIds=" + ui.getDetailTableIds() +
						"&P_isLogicalDelete=" + isLogicalDelete +
						"&P_menuId=" + ui.options.menuId +
						"&P_menuCode=" + ui.getParamValue("menuCode");

		$.confirm("所选择的记录将被删除，确定吗？", function(sure) {
			if (sure) {
				$.ajax({
					type : "post",
					url  : url,
					data : {"id": idArr.toString()},
					dataType:"json",
					success: function (msg) {
						ui.assembleData.parentConfigInfo.namespace.deleteLeftTreeNode(plbhs);
						ui.reload();
						CFG_message("操作成功!", "success");
					},
					error : function () {
						CFG_message("操作失败!", "error");
					}
				});
			}
		});
	}
	// ui.assembleData 就是 configInfo
	//console.log("override grid!");
	//ui.getAction = function () {
	//	return $.contextPath + "/appmanage/show-module";
	//};
};
/**
 *  二次开发：复写自定义树
 */
function _override_tree (ui) {
	// ui.assembleData 就是 configInfo
	//console.log("override tree!");
	//ui.getAction = function () {
	//	return $.contextPath + "/appmanage/show-module";
	//};
};
/**
 *  二次开发：复写自定义工具条
 */
function _override_tbar (ui) {
	// ui.assembleData 就是 configInfo
	//console.log("override tbar!");
	//ui.getAction = function () {
	//	return $.contextPath + "/appmanage/show-module";
	//};
};
/**
 *  二次开发：复写自定义布局
 */
function _override_layout (ui) {
	//console.log("override layout!");
	//ui.getAction = function () {
	//	return $.contextPath + "/appmanage/show-module";
	//};
};








/**
 * 在此可以复写所有自定义JS类
 * @param selector
 * @returns {JQ_override}
 */
window[CFG_overrideName(subffix)] = function () {
	
	//var startTime = new Date().getTime();
	
	if (this instanceof $.config.cform) {
		_override_form(this);
	} else if (this instanceof $.config.cgrid) {
		_override_grid(this);
	} else if (this instanceof $.config.ctree) {
		_override_tree(this);
	} else if (this instanceof $.config.ctbar) {
		_override_tbar(this);
	} else if (this instanceof $.config.clayout) {
		_override_layout(this);
	}
	
	//console.log("over ride cost time: " + (new Date().getTime() - startTime));
};

	
	
	
	
})("${timestamp}");
</script>
