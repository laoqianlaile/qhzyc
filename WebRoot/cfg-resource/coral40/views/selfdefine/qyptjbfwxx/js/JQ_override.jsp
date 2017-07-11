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
window[CFG_actionName(subffix)] = function () {
	// this.assembleData 就是 configInfo
	return $.contextPath + "/qyptjbfwxx";
};
	

/**
 * 二次开发：复写自定义表单
 */
function _override_form (ui) {
	//保存进入表单时的原始值
	var _fwbh,_qysj,_dqsj;
	/**
	 * 初始化完成后放数据
	 */
	ui._init = function(){
		var rowId = $.config.getGlobalData(ui.options.global).rowId;
		var data = $.loadJson($.contextPath + "/qyptjbfw!getBaseServicesValue.json?rowId=" + rowId);
		ui.setFormData({
			ZHBH: data.ZHBH,
			FWBH: data.FWBH,
			QYSJ: data.QYSJ,
			DQSJ: data.DQSJ
		});
		_fwbh = data.FWBH;
		_qysj = data.QYSJ;
		_dqsj = data.DQSJ;
	}
	/**
	 * 保存前校验
	 */
	ui.processBeforeSave = function() {
		var formData = ui.getFormData();
		if (formData.QYSJ == _qysj && formData.FWBH == _fwbh && formData.DQSJ == _dqsj) {
			CFG_message("未修改任何数据,请点击关闭按钮", "warning");
			return false;
		}
		var qysj = new Date(formData.QYSJ.replace("/-/g","/"));
		var dqsj = new Date(formData.DQSJ.replace("/-/g","/"));
		if (qysj > dqsj) {
			CFG_message("到期时间必须大于启用时间", "warning");
			return false;
		}
		return true;
	}
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
