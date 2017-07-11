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
	return $.contextPath + "/zzctda";
};
	

/**
 * 二次开发：复写自定义表单
 */
function _override_form (ui) {
	//页面数据加载完执行
	ui._init = function () {
		if (isEmpty(ui.options.dataId)) {//自动菜田编号生成
			var qybm=$.loadJson($.contextPath + '/zzcdda!getQybm.json');
			var ctbh=$.loadJson($.contextPath + "/zzctda!getCtbh.json");
			ui.setFormData({CTBH:ctbh,QYBM:qybm});
		}
	}
	//获得工作人员弹出框列表返回的数据
	ui.addCallback("setComboGridValue_gzry", function(o){
		if (null == o) return;
		var obj =o.rowData;
		if (null == obj) return;
 		ui.setItemValue("FZR", obj.XM);
 		ui.setFormData({FZRBH:obj.GZRYBH});
 		
 	}); 
 
 	ui.bindEvent =  function() {
			var fzrbh = ui.getItemJQ("FZRBH");
			//给负责人绑定onChange事件 处理负责人编号
			fzrbh.combogrid("option","onChange",function(e,data){
			var newText = data.text;
			var newValue = data.value;
			ui.setFormData({FZR:newText,FZRBH:newValue});
		});
	}
	if(ui.options.number==1){
		/**
		 * 设置菜种列表过滤条件：工作人员
		 */
		ui.addOutputValue("setGzrydaColumns",function(o){
			var o = {
				status : true,
				P_columns : "EQ_C_QYZT≡1"
			}
			return o;
		});
 	
 	}
		
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
