<%@page language="java" pageEncoding="UTF-8"%>
<%@ page import="com.ces.component.trace.utils.*" %>
<% String qybm =SerialNumberUtil.getInstance().getCompanyCode();
%>
<script type="text/javascript">
/***************************************************!
 * @date   2016-04-14 17:28:23
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
	return jQuery.contextPath + "/sdzyccjgyldbxx";
};
	

/**
 * 二次开发：复写自定义表单
 */
function _override_form (ui) {
	ui._init = function(){
		if(isEmpty(ui.options.dataId)){
			var qybmJQ = ui.getItemJQ("QYBM");
			var qybm ="<%=qybm%>";
			ui.setFormData({QYBM:qybm});
		}
		ui.getItemJQ("FZR").combogrid("reload",$.contextPath+"/zzgzryda!getGzrydaGrid.json?dwlx=CJGQY");
	};
	ui.bindEvent = function(){
		var ylpch = ui.getItemJQ("QYPCH");
        var pch = ui.getItemJQ("PCH");
        pch.combogrid("option","onChange",function(e,data) {
			var rowData = ylpch.combogrid("grid").grid("getRowData",data.value);
			ui.setFormData({PCH:rowData.PCH,YLMC:rowData.YLMC,DCCK:rowData.SLCK,ZL:rowData.YLKC,QYPCH:rowData.QYPCH});
			var ckJson = $.loadJson($.contextPath + "/sdzyccjgckxx!searchDbCkGridData.json?slck="+rowData.SLCK );
			ui.getItemJQ("DRCK").combogrid("reload",ckJson);
		});
		ui.setItemOption("FZR" , "onChange", function ( e ,data) {
			var rowData = ui.getItemJQ("FZR").combogrid("grid").grid("getRowData",data.value);
			ui.setFormData({FZR:rowData.XM,DH:rowData.LXFS});

		});
		ui.setItemOption("DRCK" , "onChange", function ( e ,data) {
			ui.setFormData({DRCK:data.text});

		});
	}
	ui.addCallback("setComboGridValue_fzr",function(o){
		if( null == o) return;
		var rowData = o.rowData;
		if( null == rowData) return ;
		ui.setFormData({FZR:rowData.XM,DH:rowData.LXFS});
	})
	ui.addCallback("setComboGridValue_ylpch",function(o){
		if( null == o) return;
		var rowData = o.rowData;
		if( null == rowData) return ;
		ui.setFormData({PCH:rowData.PCH,YLMC:rowData.YLMC,DCCK:rowData.SLCK,ZL:rowData.RKZL,QYPCH:rowData.QYPCH});
	})
	ui.addCallback("setComboGridValue_drck",function(o){
		if( null == o) return;
		var rowData = o.rowData;
		if( null == rowData) return ;
		ui.setFormData({DRCK:rowData.CKMC});
	})
	//过滤掉调出仓库信息
	ui.addOutputValue("setComboGridValue_ckxx",function(  ){
		var dcck= ui.getItemValue("DCCK");
		if(isEmpty(dcck)) return { status: true };
		return{
			status: true,
			P_columns:"NOT_C_CKMC≡"+dcck
		}
	})
	ui.addOutputValue("setComboGridValue_fzr", function (o){

		return {
			status : true ,
			P_columns: 'EQ_C_DWLX≡CJGQY'
		}
	});

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
