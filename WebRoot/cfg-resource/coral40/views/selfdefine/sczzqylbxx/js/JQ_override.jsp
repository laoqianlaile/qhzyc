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
	return $.contextPath + "/sczzqylbxx";
};
	

/**
 * 二次开发：复写自定义表单
 */
function _override_form (ui) {
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
	ui.beforeInitGrid = function(setting) {
		setting.fitStyle = "width";
		return setting;
	};

	// ui.assembleData 就是 configInfo
	//console.log("override grid!");
	//ui.getAction = function () {
	//	return $.contextPath + "/appmanage/show-module";
	//};
	ui.beforeDelete = function(idArr, isLogicalDelete){
		var qybhArray="";
		$.each(idArr,function(index){
			qybhArray +=idArr[index];
			if(index<idArr.length-1){
				qybhArray+=",";
			}
		})
		var flag = false;
		$.ajax({
			url: $.contextPath+"/sczzqylbxx!hasChild.json",
			type:"post",
			data:{"qybhArray":qybhArray},
			async:false,
			success:function(data){
				if(data){
					$.message("区域下已有地块，不可删除！");
				}else{
					flag = true;
				}
			},
			error:function(e){
				$.message("系统错误！"+e);
			}
		})
		return flag;
	}
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
	 *  二次开发：复写基本查询区
	 */
	function _override_bsearch (ui) {
//		ui._init = function() {
//			ui.callItemMethod("QVMC", "destroy");
//			ui.getItemJQ("QVMC").textbox({});
//			ui.callItemMethod("DKBH", "destroy");
//			ui.getItemJQ("DKBH").textbox({});
//		};
		// ui.assembleData 就是 configInfo
		//console.log("override search!");
		//ui.bindEvent = function () {
		// 添加onChange事件
		//	  ui.setItemOption("NAME", "onChange", function(e, data) {})
		//};
	};
	/**
	 *  二次开发：复写高级查询区
	 */
	function _override_gsearch (ui) {
		// ui.assembleData 就是 configInfo
		//console.log("override search!");
		//ui.bindEvent = function () {
		// 添加onChange事件
		//	  ui.setItemOption("NAME", "onChange", function(e, data) {})
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
		} else if (this instanceof $.config.cbsearch) {
			_override_bsearch(this);
		} else if (this instanceof $.config.cgsearch) {
			_override_gsearch(this);
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
