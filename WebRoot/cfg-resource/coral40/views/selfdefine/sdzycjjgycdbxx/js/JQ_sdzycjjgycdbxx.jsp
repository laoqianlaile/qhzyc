<%@page language="java" pageEncoding="UTF-8"%>
<%@ page import="com.ces.component.trace.utils.SerialNumberUtil" %>
<%
    String companyCode = SerialNumberUtil.getInstance().getCompanyCode();
%>
<script type="text/javascript">
/***************************************************!
 * @date   2016-04-15 09:55:57
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
	return jQuery.contextPath + "/sdzycjjgycdbxx";
};
	

/**
 * 二次开发：复写自定义表单
 */
function _override_form (ui) {
    ui._init = function(){
        var  qybm = ui.getItemJQ("QYBM");
        qybm.textbox("setValue","<%=companyCode%>");
        ui.getItemJQ("FZR").combogrid("reload",$.contextPath+"/zzgzryda!getGzrydaGrid.json?dwlx=JJGQY");
    }

    ui.addOutputValue("setCombogridValue_fzr", function (o){

        return {
            status : true ,
            P_columns: 'EQ_C_DWLX≡JJGQY'
        }
    });

    ui.bindEvent = function(){
        //批次号下拉列表赋值
        //var pch = ui.getItemJQ("QYPCH");
        var pch = ui.getItemJQ("PCH");
        pch.combogrid("option","onChange",function(e,data) {
           // var jsonData = $.loadJson($.contextPath +"/sdzycjjgycdbxx!searchPchGridData.json");
            var rowData = pch.combogrid("grid").grid("getRowData",data.value);
            ui.setFormData({ZL:rowData.RKZL,YCMC:rowData.YCMC,PCH:rowData.PCH,QYPCH:rowData.QYPCH});
        });
        //负责人下拉列表
        var fzr = ui.getItemJQ("FZR");
        fzr.combogrid("option","onChange",function(e,data){
//            debugger
//            var jsonData = $.loadJson($.contextPath +"/sdzycjjgycdbxx!searchFzrxx.json?rybh="+data.value);
            var rowData = ui.getItemJQ("FZR").combogrid("grid").grid("getRowData",data.value);
//            ui.setFormData({DH:jsonData.data[0].LXFS,FZR:jsonData.XM});
            ui.setFormData({
                FZR:rowData.XM,
                DH:rowData.LXFS
            });
        });
//        var dcck = ui.getItemJQ("DRCK");//调入仓库
//        debugger
//           dcck.combogrid("option","onChange",function(e,data){
//               var rowData = ui.getItemJQ("DRCK").combogrid("grid").grid("getRowData",data.value);
//               ui.setFormData({DRCK:rowData.text});
//               var drckData = $.loadJson($.contextPath+'/sdzycjjgycdbxx!getDrckExceptDCCK.json?ckbh='+ rowData.DRCK);
//               ui.getItemJQ("DCCK").combobox("reload",drckData);
//           });
        //调入仓库下拉框(调入仓库选中后调出仓库下拉框中无该仓库)
        var drck = ui.getItemJQ("DRCK");//调入仓库
        ui.setItemOption("DCCK","onChange",function(e,data){
            var dcckValue = ui.getItemValue("DCCK");
            if(dcckValue != null){
                drck.combobox("destroy");
                var drckData = $.loadJson($.contextPath+'/sdzycjjgycdbxx!getDrckExceptDCCK.json?ckbh='+ dcckValue);
                ui.getItemJQ("DRCK").combobox({
                    data: drckData,
                    textField: 'CKMC',
                    valueField: 'CKMC'
                });
            }
        });

        /*批次号的回调函数*/
        ui.addCallback("setComboGridValue",function(o){
            if(null == o) return;
            var obj = o.rowData;
            if(null == obj) return;
            ui.setFormData({PCH:obj.PCH,ZL:obj.RKZL,YCMC:obj.YCMC});
        });
        /*调入仓库的回调函数*/
        ui.addCallback("setComboGridValue_drck",function(o){
            if(null == o) return;
            var obj = o.rowData;
            if(null == obj) return;
            ui.setFormData({DCCK:obj.CKMC});
        });

        /*调出仓库的回调函数*/
        ui.addCallback("setComboGridValue_dcck",function(o){
            if(null == o) return;
            var obj = o.rowData;
            if(null == obj) return;
            ui.setFormData({DRCK:obj.CKMC});
        });

        /*负责人的回调函数*/
        ui.addCallback("setComboGridValue_fzr",function(o){
            if(null == o) return;
            var obj = o.rowData;
            if(null == obj) return;
            ui.setFormData({FZR:obj.XM,DH:obj.LXFS});
        });
    };

}


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
