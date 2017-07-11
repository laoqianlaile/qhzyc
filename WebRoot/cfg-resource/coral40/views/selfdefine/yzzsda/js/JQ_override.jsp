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
	return $.contextPath + "/yzzsda";
};
	

/**
 * 二次开发：复写自定义表单
 */
function _override_form (ui) {
	//回调函数处理使负责人信息
	ui.addCallback("setComboGridValue_fzr", function(o){
		if (null == o) return;
		var obj =o.rowData;
		if (null == obj) return;
 		ui.setFormData({FZR:obj.XM,FZRBH:obj.GZRYBH});
 	}); 
 	
 	ui.bindEvent = function () {
 		//负责人名称和编号处理 zsbh
 		var fzrbh = ui.getItemJQ("FZRBH");
 		fzrbh.combogrid("option","onChange",function(e,data){
			var newText = data.text;
			var newValue = data.value;
			ui.setFormData({FZR:newText,FZRBH:newValue});
		});
		var csbh = ui.getItemJQ("CSBH");
	    var i= 0;
 		csbh.textbox("option","onChange",function(e,data){
 			var res=$.loadJson($.contextPath+ "/yzzsda!checkOnlybh.json?csbh="+data.value);
		    if(res=='have'){
			    ui.setFormData({CSBH:""});
			    //csbh.textbox("setValue","",false);
			   // alert(++i);
 			}
		});
        var mj = ui.getItemJQ("MJ");
        mj.textbox("option","onChange",function(e,data){
            if (data.value<=0 || !$.isNumeric(data.value)) {
                CFG_message("请输入有效面积","warning");
                mj.textbox("setValue","");
            }
        });
 	};

	if(isEmpty(ui.options.dataId)){
		ui._init = function () {
			//默认添加所属账户编号
			//自动企业编码
			var qybm=$.loadJson($.contextPath + "/yzcdda!getQybm.json");
			ui.setFormData({QYBM:qybm});
		}
	
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
