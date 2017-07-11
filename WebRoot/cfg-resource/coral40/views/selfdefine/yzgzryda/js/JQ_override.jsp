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
	return $.contextPath + "/yzgzryda";
};
	

/**
 * 二次开发：复写自定义表单
 */
function _override_form (ui) {
	if(isEmpty(ui.options.dataId)){
		ui._init = function () {
			var gzrybh=$.loadJson($.contextPath + '/yzgzryda!getYgbm.json');
			//默认添加所属账户编号
			var qybm=$.loadJson($.contextPath + "/yzcdda!getQybm.json");
			//初始化加载账号编码和种植批次号
			ui.setFormData({GZRYBH:gzrybh,QYBM:qybm});
		}
	
	}
};
/**
 *  二次开发：复写自定义列表
 */
function _override_grid (ui) {
	ui.addOutputValue("setTptableIdColumns", function(o){
		var ids = ui.getSelectedRowId();
		if(ids.length==0){
			CFG_message("请选择记录", "warning");
			return {status:false};
		}
		if (ids.length > 1) {
			CFG_message("只能选择一条记录", "warning");
			return {status:false};
		}
		var tableId = $.loadJson($.contextPath +'/tyttgj!getTpTableId.json?pTableId='+ui.options.tableId);
		o={
			status:true,
			P_tableId : tableId,
			P_columns : "EQ_C_ZBID≡"+ids
		};
		return o;

	})
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
