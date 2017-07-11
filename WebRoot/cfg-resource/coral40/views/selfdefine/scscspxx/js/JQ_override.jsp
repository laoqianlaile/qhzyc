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
	return $.contextPath + "/scscspxx";
};
	var oldspbh = "";

/**
 * 二次开发：复写自定义表单
 */
function _override_form (ui){
	if(isEmpty(ui.options.dataId)){
        ui._init = function (){
			oldspbh = "";
            var obj = $.loadJson($.contextPath + "/scqyda!getQyda.json");
            ui.setFormData({QYBM:obj.QYBM,QYMC:obj.QYMC});
        }
    }else{
		ui._init = function (){
			oldspbh =  ui.getItemJQ("SPBH").val();
		}
	}

	ui.bindEvent = function(){
		var spbhJQ = ui.getItemJQ("SPBH");
		spbhJQ.textbox("option","onChange",function(e,data){
		});
	}

	ui.beforeSave = function(jq/* form Jquery对象. */, op) {
		var spbhJQ = ui.getItemJQ("SPBH");
		var checkData = $.loadJson($.contextPath + "/scscspxx!getCheckspbh.json?spbh="+spbhJQ.val());
		if(spbhJQ.val() == oldspbh){
			checkData = true;
		}
		if(!checkData){
			$.message({message: "店铺编号已重复！请重新输入", cls: "warning"});
			return false;
		}else{
			return true;
		}
	}

	ui.afterSave = function (entity, op){
		oldspbh = ui.getItemJQ("SPBH").val();
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
