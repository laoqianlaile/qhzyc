<%@ page import="com.ces.component.trace.utils.SerialNumberUtil" %>
<%@page language="java" pageEncoding="UTF-8"%>
<%
	String companyCode = SerialNumberUtil.getInstance().getCompanyCode();
%>
<script type="text/javascript">
/***************************************************!
 * @date   2017-02-10 16:17:01
 * 系统配置平台自动生成的自定义构件二次开发JS模板
 * 详细的二次开发操作，请查看文档（二次开发手册/自定义构件.docx）
 * 注：请勿修改模板结构（若需要调整结构请联系系统配置平台）
 ***************************************************/
 
 
(function(subffix) {

/**
 * 二次开发定位自己controller
 * 系统默认的controller: jQuery.contextPath + "/appmanage/show-module"
 * @returns {String}
 **/
window[CFG_actionName(subffix)] = function (ui) {
	// ui.assembleData 就是 configInfo
	return jQuery.contextPath + "/sdzyccjgggxx";
};
	

/**
 * 二次开发：复写自定义表单
 */
function _override_form (ui) {
	// ui.assembleData 就是 configInfo
	//ui.bindEvent = function () {
	//	  ui.setItemOption("NAME", "onChange", function(e, data) {});// 添加onChange事件
	//    ui.callItemMethod("USER_ID", "disable");// 禁用USER_ID
	//};
	ui._init = function() {
		var qybm = ui.getItemJQ("QYBM");
		qybm.textbox("setValue", "<%=companyCode%>");
		ui.setItemOption("SZ","valid",function(){
			var zz = /^([1-9]+(\.[0-9]+[1-9])?)|([0]+(\.([0-9]+)?[1-9]))$/;
			var errorMessage = "请修改！",
					isValid;
			var szz = ui.getItemValue("SZ")
			var re = new RegExp(zz);
			if(re.test(szz)){
				return;
			}else{
				isValid = false;
				return { isValid: isValid, errMsg: errorMessage };
			}

		});

	}
	ui.bindEvent = function(){

		var sldw = ui.getItemJQ("SLDW");
		var gg = ui.getItemJQ("GG");
		var zldw = ui.getItemJQ("ZLDW");
		var sz = ui.getItemJQ("SZ");
		var zlhs = ui.getItemJQ("ZLHS");


		var szValue ;
		var slValue ;
		var zlValue ;
		var zlhsValue ;

		sz.textbox("option","onBlur",function(e,data){
			szValue = sz.textbox("getText");
			slValue = sldw.combobox("getText");
			zlValue = zldw.combobox("getText");
			if(isEmpty(slValue)||isEmpty(zlValue)){
				gg.textbox("setValue","");
			}else{
				gg.textbox("setValue",szValue+""+zlValue+"/"+slValue);
				//ui.setFormData({GG:szValue+""+zlValue+"/"+slValue});
				if("千克"==zlValue){
					var hs = parseFloat(szValue)*1000;
					zlhs.textbox("setValue",hs+"");
				}else{
					zlhs.textbox("setValue",szValue);
				}
			}


		})
		sldw.combobox("option","onChange",function(e,data){
			szValue = sz.textbox("getText");
			slValue = sldw.combobox("getText");
			zlValue = zldw.combobox("getText");
			if(isEmpty(zlValue)||isEmpty(szValue)){
				gg.textbox("setValue","");
			}else{
				gg.textbox("setValue",szValue+""+zlValue+"/"+slValue);
				if("千克"==zlValue){
					var hs = parseFloat(szValue)*1000;
					zlhs.textbox("setValue",hs+"");
				}else{
					zlhs.textbox("setValue",szValue);
				}
				//ui.setFormData({GG:szValue+""+zlValue+"/"+slValue});
			}
		});
		zldw.combobox("option","onChange",function(e,data){
			szValue = sz.textbox("getText");
			slValue = sldw.combobox("getText");
			zlValue = zldw.combobox("getText");
			if(isEmpty(slValue)||isEmpty(szValue)){
				gg.textbox("setValue","");
			}else{
				gg.textbox("setValue",szValue+""+zlValue+"/"+slValue);
				if("千克"==zlValue){
					var hs = parseFloat(szValue)*1000;
					zlhs.textbox("setValue",hs+"");
				}else{
					zlhs.textbox("setValue",szValue);
				}
				//ui.setFormData({GG:szValue+""+zlValue+"/"+slValue});
			}
		});
	}
};

/**
 *  二次开发：复写自定义列表
 */
function _override_grid (ui) {
	// ui.assembleData 就是 configInfo
	//ui.beforeInitGrid = function (setting) {
	//	return setting;
	//};
};

/**
 *  二次开发：复写自定义工具条
 */
function _override_tbar (ui) {
	// ui.assembleData 就是 configInfo
};

/**
 *  二次开发：复写基本查询区
 */
function _override_bsearch (ui) {
	// ui.assembleData 就是 configInfo
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
};

/**
 *  二次开发：复写自定义布局
 */
function _override_layout (ui) {
	// ui.assembleData 就是 configInfo
};

/**
 *  二次开发：复写自定义树
 */
function _override_tree (ui) {
	// ui.assembleData 就是 configInfo
};

/***
 * 当前构件全局函数实现位置
 * 如果有需要的，可打开以下实现体
 * 使用方法： 与开发构件一致
 **/
//jQuery.extend(jQuery.ns("ns" + subffix), {
//	name : "",
//	click: function() {}
//});









/**
 * 在此可以复写所有自定义JS类
 */
window[CFG_overrideName(subffix)] = function () {
	if (this instanceof jQuery.config.cform) {
		_override_form(this);
	} else if (this instanceof jQuery.config.cgrid) {
		_override_grid(this);
	} else if (this instanceof jQuery.config.cbsearch) {
		_override_bsearch(this);
	} else if (this instanceof jQuery.config.cgsearch) {
		_override_gsearch(this);
	} else if (this instanceof jQuery.config.ctree) {
		_override_tree(this);
	} else if (this instanceof jQuery.config.ctbar) {
		_override_tbar(this);
	} else if (this instanceof jQuery.config.clayout) {
		_override_layout(this);
	}
};

	
	
	
	
})("${timestamp}");
</script>
