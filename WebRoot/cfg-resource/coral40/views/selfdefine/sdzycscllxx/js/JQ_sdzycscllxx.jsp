<%@page language="java" pageEncoding="UTF-8"%>
<%@ page import="com.ces.component.trace.utils.SerialNumberUtil" %>
<%
    String companyCode = SerialNumberUtil.getInstance().getCompanyCode();
%>
<script type="text/javascript">
/***************************************************!
 * @date   2016-04-15 10:00:37
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
	return jQuery.contextPath + "/sdzycscllxx";
};
	

/**
 * 二次开发：复写自定义表单
 */
function _override_form (ui) {
    //初始化添加企业编码
    ui._init = function(){
        var  qybm = ui.getItemJQ("QYBM");
        qybm.textbox("setValue","<%=companyCode%>");

    };
    ui.bindEvent = function(){
        var ylpch = ui.getItemJQ("PCH");
        var rowData = "";
        ylpch.combogrid("option","onChange",function(e,data) {
            rowData = ylpch.combogrid("grid").grid("getRowData",data.value);
            ui.setFormData({PCH:rowData.PCH,YLMC:rowData.YCMC,LLZZL:rowData.KC,YCDM:rowData.YCDM});
        });
        //验证领料重量小于库存重量
        ui.setItemOption("LLZZL","onChange",function(e,data){
            var jyzl = ui.getItemValue("LLZZL");
            var rkzl = rowData.KC;
            if(jyzl>rkzl){
                CFG_message("请输入小于库存"+rkzl+"的数字","warning");
                ui.setItemValue("LLZZL",rkzl);
            }
        });
    }

	/*负责人的回调函数*/
	ui.addCallback("setComboGridValue_fzr",function(o){
		if(null == o) return;
		var obj = o.rowData;
		if(null == obj) return;
		ui.setFormData({FZR:obj.XM,DH:obj.LXFS});
	});

	/*发料仓库的回调函数*/
	ui.addCallback("setComboGridValue_flck",function(o){
		if(null == o) return;
		var obj = o.rowData;
		if(null == obj) return;
		ui.setFormData({FLCK:obj.CKMC});
	});
/*原料批次号的回调函数*/
    ui.addCallback("setComboGridValue_ylpch",function(o){
        if(null == o) return;
        var obj = o.rowData;
        if(null ==o) return;
        ui.setFormData({PCH:obj.PCH,YLMC:obj.YCMC,LLZZL:obj.RKZL});
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
	ui.clickDelete = function (isLogicalDelete) {
		var ids = ui.uiGrid.grid("option", "selarrrow");
		if (0 === ids.length) {
			CFG_message( "请选择记录!", "warning");
			return;
		}
		for(var i=0;i<ids.length;i++){
			if(ui.uiGrid.grid('getRowData',ids[i]).ISSC=='0'){
				ui.databaseDelete(ids, isLogicalDelete);
			}else{
				$.alert("不能删除!已进行饮片生产");
				return;
			}
		}
	}
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
