<%@page language="java" pageEncoding="UTF-8"%>
<%@ page import="com.ces.component.trace.utils.SerialNumberUtil" %>
<%
    String companyCode = SerialNumberUtil.getInstance().getCompanyCode();
%>
<script type="text/javascript">
/***************************************************!
 * @date   2016-04-15 10:21:43
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
	return jQuery.contextPath + "/sdzyccpjyxz";
};


/**
 * 二次开发：复写自定义表单
 */
function _override_form (ui) {
	ui.afterSave = function(){
		ui.clickBackToGrid();
	}
    //初始化添加企业编码
    ui._init = function(){
        var  qybm = ui.getItemJQ("QYBM");
        qybm.textbox("setValue","<%=companyCode%>");

//        ui.getItemJQ("CGF").textbox("destroy");
//        ui.getItemJQ("CGF").combogrid({
//            colModel:[{name:"CGSBH"},{name:"CGSMC"}],
//            //colModel:[{name:"BMBH"},{name:"BMMC"},{name:"1111",hidden:true}],
//            colNames:["采购商编号","采购商名称"],
//            url:"sdzycjjgcgfxx!getGzrydaGrid.json",
//            panelWidth:"290",
//            textField:"CGSMC",
//            //valueField:"BMBH",
//            valueField:"CGSMC",
//
//        })
    };
    ui.bindEvent = function(){
       // var pch = ui.getItemJQ("QYPCH");
        var bzpch = ui.getItemJQ("QYBZPCH");
        var rowData = "";
        bzpch.combogrid("option","onChange",function(e,data){
            rowData = bzpch.combogrid("grid").grid("getRowData",data.value);
            ui.setFormData({BZPCH:rowData.BZPCH,QYBZPCH:rowData.QYBZPCH,PCH:rowData.PCH,YPMC:rowData.YPMC,JYZL:rowData.KC,YCDM:rowData.YCDM,QYPCH:rowData.QYPCH});
        });

        //验证交易重量小于入库重量
        ui.setItemOption("JYZL","onChange",function(e,data){
            var jqForm = $("form", ui.uiForm);
            var jyzl = ui.getItemValue("JYZL");
            var rkzl = rowData.KC;
            if(parseFloat(jyzl)>parseFloat(rkzl)){
                CFG_message("请输入小于库存值："+rkzl+"的数字","warning");
                ui.setItemValue("JYZL",rkzl);
            }
            var jydj = ui.getItemValue("JYDJ");
            var jyje = jyzl* jydj;
            var growData = ui.getSelfGrid().toFormData(); // 对应列表
            var pch = ui.getItemValue("PCH");
            var ckStatic = jQuery.ns("ns" + subffix).checkKc(growData,jyzl,pch);
            if(ckStatic ==1 || ckStatic ==4 ){//1批次为第一次在当前页面使用，不做处理 4 小于库存重量
            }else if(ckStatic ==2 || ckStatic ==3 ){ //2标识没有录入批次号 ，3标识查询无数据
                CFG_message("请选择有效批次","waring");
                jqForm.form("clear");
            }else if(ckStatic ==5){
                CFG_message("批次库存不足，不支持修改","waring");
                jqForm.form("clear");
                ui.setItemValue("JYJE","");
            }else{
                ui.setItemValue("JYJE",jyje);
            }
        });
        ui.setItemOption("JYDJ","onKeyUp",function ( e ,data){
            var jyzl = ui.getItemValue("JYZL");
            var jyje = jyzl* data.value;
            jyje =Math.round(jyje*100)/100;
            ui.setItemValue("JYJE",jyje);
        })
    }
    //计算饮片重量
    ui.clickAdd = function(){
        var cGrid = this.getSelfGrid(), // 对应列表
                jqForm = $("form", this.uiForm),
                formData;
        // 表单检验
        if (!jqForm.form("valid")) return false;
        // 获取表单数据
        formData = jqForm.form("formData", false);

        if (!cGrid) return;
        var rowData = cGrid.toFormData();//获取从列表的数据
        var ckStatic = jQuery.ns("ns" + subffix).checkKc(rowData,formData.JYZL,formData.BZPCH);
        if(ckStatic ==2 || ckStatic ==3 ){ //2标识没有录入批次号 ，3标识查询无数据
            CFG_message("请选择有效批次","waring");
            jqForm.form("clear");
            return;
        }else if(ckStatic ==5){
            CFG_message("批次库存不足，不支持修改","waring");
            jqForm.form("clear");
            ui.setItemValue("JYJE","");
            return;
        }
        $.each(rowData , function (key ,value){
            if(value.QYPCH == formData.QYPCH){
                value.JYZL = parseFloat(formData.JYZL)+parseFloat(value.JYZL);
                value.JYJE = parseFloat(formData.JYJE)+parseFloat(value.JYJE);
                value.JYDJ = (parseFloat(formData.JYDJ)+parseFloat(value.JYDJ))/2;
                formData = value;
                return;
            }
        })
        // 向列表添加数据
        cGrid.addRowData(formData);
        // 重置表单数据和刷新类别
        ui.clickCreate();
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
jQuery.extend(jQuery.ns("ns" + subffix), {
    checkKc: function(growData,jyzl,pch) {
        var chk = 0;
        if(isEmpty(growData)){
            chk =1;
        }else{ //根据批次号，获取库存重量。
            if(isEmpty(pch)) {
                chk =2;
            }else{
                var kcxx = $.loadJson($.contextPath +"/sdzyccpjyxz!getPchkcxx.json?pch="+pch);
                if(isEmpty(kcxx)){
                    chk= 3;
                }else{
                    var ylkc = kcxx.KC;
                    $.each(growData,function ( key ,data){
                        if(data.PCH ==pch ){
                            chk = 5;
                            var oldxx = $.loadJson($.contextPath+"/sdzyccpjyxz!show.json?P_tableId=402883f754519dac015451b9afb80003&P_componentVersionId=8a8a4a2b54177b53015417bbb70a012b&P_menuId=8a8a4afc5403e19a01540568f077008a&P_workflowId=&P_processInstanceId=&id="+data.ID+"&P_menuCode=&___t="+new Date().getTime());
                            if(isEmpty(oldxx.JYZL)){
                                if(parseFloat(ylkc)-parseFloat(data.JYZL)-parseFloat(jyzl)>=0){
                                    chk = 4;
                                }
                            }else{
                                if(parseFloat(ylkc)+parseFloat(oldxx.JYZL)-parseFloat(data.JYZL)-parseFloat(jyzl)>=0){
                                    chk = 4;
                                }
                            }
                            return chk;
                        }
                    })
                }
            }
        }
        return chk;
    }
});









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
