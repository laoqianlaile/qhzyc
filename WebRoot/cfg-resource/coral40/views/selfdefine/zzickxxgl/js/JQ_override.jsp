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
	return $.contextPath + "/appmanage/show-module";
};
	

/**
 * 二次开发：复写自定义表单
 */
function _override_form (ui) {
	var XMinit = ui.getItemJQ("XM").textbox('getValue');
	var ICKKHinit = ui.getItemJQ("ICKKH").textbox('getValue');
	var GWinit = ui.getItemJQ("GW").combobox('getValue');
	var ZJHMinit=ui.getItemJQ("ZJHM").textbox('getValue');
	var XBinit=ui.getItemJQ("XB").combobox('getValue');
	var QYZTinit=ui.getItemJQ("QYZT").combobox('getValue');
	if(!isEmpty(ui.options.dataId)){
		ui._init = function(){
			ui.getItemJQ("ICKKH").textbox('disable');
		}

	}
	ui.clickSave=function(op){
		//alert(11);

		var XM = ui.getItemJQ("XM").textbox('getValue');
		var ICKKH = ui.getItemJQ("ICKKH").textbox('getValue');
		var GW = ui.getItemJQ("GW").combobox('getValue');
		var ZJHM=ui.getItemJQ("ZJHM").textbox('getValue');
		var XB=ui.getItemJQ("XB").combobox('getValue');
		var QYZT=ui.getItemJQ("QYZT").radiolist('getValue');
		var ID=ui.getItemJQ("ID").textbox("getValue");
		var BZ=ui.getItemJQ("BZ").textbox("getValue");


		$.ajax({
			type : "post",

			url : $.contextPath+"/zzickxxgl!saveicbgb.json",
			data : {
				XM:XM,
				SFZH:ZJHM,
				ICKKH:ICKKH,
				XB:XB,
				QYZT:QYZT,
				GW:GW,
				ID:ID,
				BZ:BZ,

			},
			dataType : 'json',

			success : function(entity) {
				ui.options.dataId = entity;
				CFG_message("操作成功！", "success");
			},
			error : function() {
				CFG_message("操作失败！", "error");
			}
		});

	}






};
/**
 *  二次开发：复写自定义列表
 */
function _override_grid (ui) {
//		ui.addOutputValue("Ickh",function(o){
//		var ids = ui.selectedRowId;
//		//var tableData = $.loadJson($.contextPath+"/zzickymxq!initForm.json?id="+ids);
//		//var ickkh=tableData.ICKKH;
//		//alert(tableData.ICKH);
//		return {
//			status:true,
//			P_columns: "EQ_C_ID≡"+ids
//		}
//	})
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
