<%@page language="java" pageEncoding="UTF-8"%>
<%@ page import="com.ces.component.trace.utils.SerialNumberUtil" %>
<%
    String companyCode = SerialNumberUtil.getInstance().getCompanyCode();
%>
<script type="text/javascript">
/***************************************************!
 * @date   2016-04-25 18:22:29
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
	return jQuery.contextPath + "/sdzycjjgycrkwgxz";
};
	

/**
 * 二次开发：复写自定义表单
 */
function _override_form (ui) {
    ui._init = function(){
        hUI = ui;
        //初始化图片删除区域
        var cdzmtpDiv = ui.getItemBorderJQ("cdzmtp");
        cdzmtpDiv.empty();
        if(!isEmpty(ui.options.dataId)) {//修改或详细出事pch下拉框为文本框
            var url = ui.getAction() + "!show.json?P_tableId=" + ui.options.tableId +
                            "&P_componentVersionId=" + ui.options.componentVersionId +
                            "&P_menuId=" + ui.options.menuId +
                            "&P_workflowId=" + null2empty(ui.options.workflowId) +
                            "&P_processInstanceId=" + ui._getProccessInstanceId() +
                            "&id=" + ui.options.dataId + "&P_menuCode=" + ui.getParamValue("menuCode"),
                    formData = null;
            formData = $.loadJson(url);
            $("form", ui.uiForm).form("loadData",formData);
            $("#data_list").attr("src",$.contextPath+"/spzstpfj/"+formData.CDZM);
            if(ui.options.isView){
                ui.getItemBorderJQ("CDZM").remove();
            }
            cdzmtpDiv.html(initTPdiv());
        }
        var  qybm = ui.getItemJQ("QYBM");
        qybm.textbox("setValue","<%=companyCode%>");
	//	ui.getItemJQ("FZR").combogrid("reload",$.contextPath+"/zzgzryda!getGzrydaGrid.json?dwlx=JJGQY");
        var qypch = ui.getItemJQ("QYPCH");
        qypch.combogrid("destroy");
        qypch.textbox();
    };
    ui.afterSave = function (){
        ui._assembleReturn(true);
        var ltc_id = $("#mainContent div div")[0].getAttribute("id");
        var gd_id = $("#LTC_"+ltc_id.substr(5,ltc_id.length)+" div ")[0].getAttribute("id");
        $("#gd_" + gd_id.substr(3,gd_id.length)).grid("reload");
    }

	// ui.assembleData 就是 configInfo
	//ui.bindEvent = function () {
	//	  ui.setItemOption("NAME", "onChange", function(e, data) {});// 添加onChange事件
	//    ui.callItemMethod("USER_ID", "disable");// 禁用USER_ID
	//};

	ui.addCallback("setComboGridValue_fzr",function(o){
		if (null == o) return;
		var rowData = o.rowData;
		if (null == rowData) return;
		ui.setFormData({
			FZR:rowData.XM,
			FZRBH:rowData.RYBH,
			LXDH:rowData.LXFS
		});
	});

	ui.addOutputValue("setCombogridValue_fzr", function (o){

		return {
			status : true ,
			P_columns: 'EQ_C_DWLX≡JJGQY'
		}
	});

	ui.bindEvent = function (){
		ui.setItemOption("FZR", "onChange", function(e,data){
			var rowData = ui.getItemJQ("FZR").combogrid("grid").grid("getRowData",data.value);
			ui.setFormData({
				FZR:rowData.XM
			});
		})

	}

	ui.bindEvent = function (){
		ui.setItemOption("YCMC", "onChange", function(e,data){
			var rowData = ui.getItemJQ("YCMC").combogrid("grid").grid("getRowData",data.value);
			ui.setFormData({
				YCMC:rowData.YCMNAME,
				YCDM:rowData.ZSSPM
			});
		})
        var cdzm = ui.getItemJQ("CDZM");
        var rowData = "";
        cdzm.combogrid("option", "onChange", function (e, data) {
            rowData = cdzm.combogrid("grid").grid("getRowData", data.value);
            var tpmc = rowData.TPBCMC;
            ui.getItemBorderJQ("cdzmtp").html(initTPdiv(tpmc));
        });

		ui.getItemJQ("GYS").combogrid("option", "onChange", function (e, data){
			//var gys_value = data.value;
			var gys_value =  ui.getItemJQ("GYS").combogrid("grid").grid("getRowData", data.value);
//			var gys_data = $.loadJson($.contextPath +"/sdzycgysxx!searchYcgysxxByGysbh.json?gysbh="+gys_value);
			var gys_data = $.loadJson($.contextPath +"/sdzycgysxx!searchYcgysxxByGysbh.json?gysbh="+gys_value.GYSBH);
			ui.getItemJQ("CDZM").combogrid("reload", gys_data);
			var cdzm = ui.getItemJQ("CDZM");
			cdzm.combogrid("option", "onChange", function (e, data) {
				var rowData = cdzm.combogrid("grid").grid("getRowData", data.value);
				var tpmc = rowData.TPBCMC;
				ui.getItemBorderJQ("cdzmtp").html(initTPdiv(tpmc));
			});
		});

	}

	/*药材名称的回调函数*/
	ui.addCallback("setComboGridValue_ycmc",function(o){
		if(null == o){
			return;
		}
		var obj = o.rowData;
		if(null == obj) return;
		ui.setFormData({YCMC:obj.YCMNAME,YCDM:obj.ZSSPM});
	});

	ui.addOutputValue("setCombogridValue_ycmc", function (o){

		return {
			status : true ,
			P_columns: 'EQ_C_QYLX≡JJG'
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
    function initTPdiv(tpmc) {
        var path = "spzstpfj/thumb/"+tpmc;
        var html = "<div style=\"margin-bottom: 50px;margin-left:55px;display:none\">" +
                "<div class=\"fillwidth colspan2 clearfix\">" +
                "<div class=\"app-inputdiv6\">" +
                "<label class=\"app-input-label\" style=\"width:55%;\">产地证明图片显示：</label>" +
                "<div id=\"file${idSuffix}\">" +
                "<input class=\"inputfile\" type=\"file\" style=\"width:160px;display:none\" id=\"imageUpload${idSuffix}\" multiple=\"multiple\" accept=\"image/*\" name=\"imageUpload\" onchange=\"$.ns('ns" + subffix + "').viewImage(this)\"/>" +
                "</div>" +
                "<div style=\"margin-left: 10%\" id=\"view${idSuffix}\"></div>" +
                "<img id=\"data_list\" src='"+path+"'>" +
                "</div>" +
                "</div>" +
                "</div>";
        return html;
    }








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
