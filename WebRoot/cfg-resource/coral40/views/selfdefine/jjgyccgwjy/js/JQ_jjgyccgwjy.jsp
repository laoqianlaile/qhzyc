<%@page language="java" pageEncoding="UTF-8"%>
<%@ page import="com.ces.component.trace.utils.*" %>
<% String qybm=SerialNumberUtil.getInstance().getCompanyCode();
%>
<script type="text/javascript">
/***************************************************!
 * @date   2016-11-07 10:26:25
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
	return jQuery.contextPath + "/jjgyccgwjy";
};
	

/**
 * 二次开发：复写自定义表单
 */
function _override_form (ui) {
    ui._init = function () {//初始化，自动填充企业编码
        if (isEmpty(ui.options.dataId)) {
            var qybmJQ = ui.getItemJQ("QYBM");
            var qybm = "<%=qybm%>";
            ui.setFormData({QYBM: qybm});
        }
        //将下拉框改为文本框
        ui.getItemJQ("YCMC").combogrid("destroy");
        ui.getItemJQ("YCMC").textbox();
        ui.getItemJQ("QYPCH").combogrid("destroy");
        ui.getItemJQ("QYPCH").textbox();
    }

    ui.bindEvent = function(){//入库编号自动导入数据
        var rkbh = ui.getItemJQ("RKBH");
        rkbh.combobox("option","onChange",function(e,data){
            var jsonData = $.loadJson($.contextPath+"/jjgyccgwjy!getDataByrkbh.json?rkbh="+data.value);
            ui.setFormData({QYPCH:jsonData[0].QYPCH,
                            YCMC:jsonData[0].YCMC,
                            CD:jsonData[0].CDMC,
                            RKZL:jsonData[0].RKZL,
                            PCH:jsonData[0].PCH,
                            CSPCH:jsonData[0].CSPCH,
                            YCDM:jsonData[0].YCDM});
        });
        //入库重量数值检验
        ui.getItemJQ("RKZL").textbox("option","onChange",function(e,data){
            var pch = ui.getItemValue("PCH");
            var jsonData =  $.loadJson($.contextPath +"/jjgyccgwjy!searchKcByPch.json?pch="+pch);
            var rkzl = data.value;
            if(rkzl>jsonData.data[0].RKZL){
                CFG_message("所填重量超出库存"+jsonData.data[0].RKZL+"，请重新输入", "warning");
                ui.getItemJQ("RKZL").val("");//入库重量数据清空
            }
        });
    }
    ui.afterSave = function (entity){//更新出库状态
        var rkbh= entity.RKBH;
        var flag = $.ajax({type:"get",async:false,url:$.contextPath+"/jjgyccgwjy!processSfck?rkbh="+rkbh});
        ui._assembleReturn(true);
        var ltc_id = $("#mainContent div div")[0].getAttribute("id");
        var gd_id = $("#LTC_"+ltc_id.substr(5,ltc_id.length)+" div ")[0].getAttribute("id");
        $("#gd_" + gd_id.substr(3,gd_id.length)).grid("reload");
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
