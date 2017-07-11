<%@page language="java" pageEncoding="UTF-8"%>
<%@ page import="com.ces.component.trace.utils.SerialNumberUtil" %>
<%
    String companyCode = SerialNumberUtil.getInstance().getCompanyCode();
%>
<script type="text/javascript">
/***************************************************!
 * @date   2016-04-22 11:08:33
 * 系统配置平台自动生成的自定义构件二次开发JS模板
 * 详细的二次开发操作，请查看文档（二次开发手册/自定义构件.docx）
 * 注：请勿修改模板结构（若需要调整结构请联系系统配置平台）
 ***************************************************/
 
 
(function(subffix) {
	var _djcpsl = "";
	var _zhcpsl = "";

/**
 * 二次开发定位自己controller
 * 系统默认的controller: jQuery.contextPath + "/appmanage/show-module"
 * @returns {String}
 **/
window[CFG_actionName(subffix)] = function (ui) {
	// ui.assembleData 就是 configInfo
	return jQuery.contextPath + "/sdzycyljyxx";
};
	

/**
 * 二次开发：复写自定义表单
 */
function _override_form (ui) {
    ui._init = function(){
        var pch = ui.getItemJQ("PCH");
        pch.combogrid("destroy");
        pch.textbox();
        //添加企业编码
        var  qybm = ui.getItemJQ("QYBM");
        qybm.textbox("setValue","<%=companyCode%>");
    }
	// ui.assembleData 就是 configInfo
	//ui.bindEvent = function () {
	//	  ui.setItemOption("NAME", "onChange", function(e, data) {});// 添加onChange事件
	//    ui.callItemMethod("USER_ID", "disable");// 禁用USER_ID
	//};
	ui._init = function () {
//		var qybm = $.loadJson($.contextPath + "/trace!getQybm.json");
		if(isEmpty(ui.options.dataId)){
		var qybm = ui.getItemJQ("QYBM");
		qybm.textbox("setValue", $.loadJson($.contextPath + "/trace!getQybm.json"));
		}
	}
	ui.bindEvent = function (){
		ui.setItemOption("QYPCH","onChange", function(e ,data ){
			var rowData = ui.getItemJQ("QYPCH").combogrid("grid").grid("getRowData",data.value);
			ui.setFormData({
				CSPCH : rowData.CSPCH,
				YCMC : rowData.YCMC,
				YCDM : rowData.YCDM,
				JYZL : rowData.CSZL});
		});
        ui.setItemOption("JYDJ","onKeyUp",function( e ,data){
            var jyzl = ui.getItemValue("JYZL");
            var ze = jyzl*data.value;
            ui.setItemValue("ZE",ze);
        });
        ui.setItemOption("JYZL","onKeyUp",function ( e ,data){
            var jydj = ui.getItemValue("JYDJ");
            var ze = jydj*data.value;
            ui.setItemValue("ZE",ze);
        });
	}
	ui.addCallback("setComboGridValue_yljy",function(o){
		if (null == o) return;
		var rowData = o.rowData;
		if (null == rowData) return;
		ui.setFormData({
			CSPCH:rowData.CSPCH,
			YCDM:rowData.YCDM,
			YCMC:rowData.YCMC
		});
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
		var idArr = ui.uiGrid.grid("option", "selarrrow");
		if (0 == idArr.length) {
			CFG_message( "请选择记录!", "warning");
			return;
		}
		for(var i in idArr) {
			var rowData = ui.uiGrid.grid("getRowData",idArr[i]);
			var url = $.contextPath + "/sdzyccscggl!queryJyCspch.json?cspch="+rowData.CSPCH;
			var ssqy = $.loadJson(url);
		if((ssqy.data[0].SFJC=='0')){
			ui.databaseDelete(idArr, isLogicalDelete);
		}else{
			$.alert("不能删除!药材已采购入库");
			return;
		}
		}
	}
	ui.clickSecondDev = function(id){

		if(id == $.config.preButtonId + "print"){
			if(isSwt || isNewSwt){
				if(ui.getSelectedRowId().length != 1){
					CFG_message("请选择一条记录", "warning");
					return;
				}
			}else{
				$.alert("请在程序环境中写卡");
				return;
			}
			showPrintDialog();
		}
	}


	/************************打印dialog bigen************************/
	function showPrintDialog(){
		var jqGlobal = $(ui.options.global);
		var jqUC = $("<div id=\"jqUC\"></div>").appendTo("body");
		jqUC.dialog({
			appendTo: jqGlobal,
			modal: true,
			title: "打印",
			width: 400,
			height: 160,
			resizable: false,
			position: {
				at: "center top+200"
			},
			onClose: function () {
				jqUC.dialog("close");
				jqUC.remove();
				_djcpsl = "";
				_zhcpsl = "";
			},
			onCreate: function () {
				//jqDiv为新增内容，如果恢复之前功能 请删除
				var jqDiv = $("<div class=\"app-inputdiv-full\" style=\"padding:10px 20px;\">" +
						"       <div class=\"app-inputdiv11\" style=\"float: left; width: 60px;padding-top: 8px\"  >" +
						"           <label >打印份数：</label>" +
						"       </div>" +
						"       <div class=\"app-inputdiv12\" style=\"float: left;width: 250px;\" >" +
						"           <input id='djcp' name='DJCP' />" +
						"       </div>" +
						"   </div>"
				).appendTo(this);
				$('#djcp,#zhcp').textbox({
//					readonly: true
				});
			},
			onOpen: function () {
				var jqPanel = $(this).dialog("buttonPanel").addClass("app-bottom-toolbar"),
						jqDiv = $("<div class=\"dialog-toolbar\">").appendTo(jqPanel);
				jqDiv.toolbar({
					data: ["->", {id: "sure", label: "确定", type: "button"}, {
						id: "cancel",
						label: "取消",
						type: "button"
					}],
					onClick: function (e, data) {
						_djcpsl =1;_zhcps1 =0;//新增内容 使得_djcpsl,_zhcps1失去区分选择单件还是组合产品的功能
						if ("sure" === data.id) {
							if(_djcpsl == 1){
								if (/^[0-9]+$/.test( $("#djcp").val() )) {
								} else {
									CFG_message("请输入自然数！", "warning");
									return false;
								}
							}else if(_zhcps1 == 1){
								if (/^[0-9]+$/.test( $("#djcp").val() )) {//此处修改了内容 原值为#zhcp
								} else {
									CFG_message("请输入自然数！", "warning");
									return false;
								}
							}else{
								CFG_message("请输入打印份数！", "warning");//新增内容
								// CFG_message("请选择打印形式和打印份数！", "warning");
								return false;
							}
							var rowData = ui.uiGrid.grid("getRowData",ui.getSelectedRowId());
							var bxq = "";
							for(var i in rowData.JYSJ.split("-")){
								if(i==0){
									bxq = parseInt(rowData.JYSJ.split("-")[i]) + 1 ;
								}else{
									bxq += "-" + rowData.JYSJ.split("-")[i];
								}
							}

							if(_djcpsl == 1){
								for(var i = 0;i<parseInt($("#djcp").val());i++){
									var cpzsm = rowData.XSDDH;
									var data = {
										qymc: rowData.YCMC,
										cpmc: rowData.YCMC,
										cpdj: rowData.YCMC,
										bzrq: rowData.JYSJ,
										bxq: bxq,
										cpzsm: cpzsm,
										bzgg: rowData.YCMC
									};
									if(isSwt){
										var result = _window("printSczzBz", JSON.stringify(data));
										var resultData = JSON.parse(result);
										if (resultData.status == "true" || resultData.status == true) {
											var savePrint = $.loadJson($.contextPath + "/zzbzgl!savePrint.json?bzlsh="+rowData.CSPCH+"&bzxs=xbz&cpzsm="+cpzsm+"&id="+rowData.ID);
											if(savePrint != true ){
												$.alert("打印失败：" + resultData.msg);
												return false;
											}
										} else {
											$.alert("打印失败：" + resultData.msg);
											return false;
										}
									}else if(isNewSwt){
										var result=_print.print("bzglPrint",{
											"CPMC":"产品名称 ：" + rowData.YCMC,
											"CPDJ":"产品等级 ：" + rowData.YCMC,
											"BZRQ":"包装日期 ：" + rowData.JYSJ,
											"BXQ":"保鲜期 ：" + bxq,
											"ZL":"重量 ：" + rowData.YCMC,
											"CPZSM": cpzsm,
											"URL":"http://www.zhuisuyun.net/" + cpzsm
										});

										if (result.MSG == "SUCCESS") {
											var savePrint = $.loadJson($.contextPath + "/zzbzgl!savePrint.json?bzlsh="+rowData.CSPCH+"&bzxs=xbz&cpzsm="+cpzsm+"&id="+rowData.ID);
											if(savePrint != true ){
												$.alert("打印失败");
												return false;
											}
										} else {
											$.alert("打印失败");
											return false;
										}
									}
								}
								if(_zhcpsl != 1){
									$.alert("打印成功");
								}
							}
							if(_zhcpsl == 1){
								for(var i = 0;i<$("#zhcp").val();i++){
									var cpzsm = rowData.XSDDH;
									if(isSwt){
										var data = {
											qymc: rowData.YCMC,
											cpmc: rowData.YCMC,
											cpdj: rowData.YCMC,
											bzrq: rowData.JYSJ,
											bxq: bxq,
											cpzsm: cpzsm,
											bzgg: rowData.YCMC
										};
										var result = _window("printSczzBz", JSON.stringify(data));
										var resultData = JSON.parse(result);
										if (resultData.status == "true" || resultData.status == true) {
											var savePrint = $.loadJson($.contextPath + "/zzbzgl!savePrint.json?bzlsh="+rowData.CSPCH+"&bzxs=dbz&cpzsm="+cpzsm+"&id="+rowData.ID);
											if(savePrint != true ){
												$.alert("打印失败：" + resultData.msg);
												return false;
											}
										} else {
											$.alert("打印失败：" + resultData.msg);
											return false;
										}
									}else if(isNewSwt){
										var result=_print.print("bzglPrint",{
											"CPMC":"产品名称 ：" + rowData.YCMC,
											"CPDJ":"产品等级 ：" + rowData.YCMC,
											"BZRQ":"包装日期 ：" + rowData.JYSJ,
									 		"BXQ":"保鲜期 ：" + bxq,
											"ZL":"重量 ：" + rowData.YCMC,
											"CPZSM": cpzsm,
											"URL":"http://www.zhuisuyun.net/" + cpzsm
										});

										if (result.MSG == " ") {
											var savePrint = $.loadJson($.contextPath + "/zzbzgl!savePrint.json?bzlsh="+rowData.CSPCH+"&bzxs=xbz&cpzsm="+cpzsm+"&id="+rowData.ID);
											if(savePrint != true ){
												$.alert("打印失败");
												return false;
											}
										} else {
											$.alert("打印失败");
											return false;
										}
									}

								}
								$.alert("打印成功");
							}
						}
						jqUC.dialog("close");
						_djcpsl = "";
						_zhcpsl = "";
					}
				});
			}
		});
	}
	/************************打印dialog end************************/
};

/**
 *  二次开发：复写自定义工具条
 */
function _override_tbar (ui) {
	// ui.assembleData 就是 configInfo
	ui.processItems = function (data) {
		var btns = [];
		for (var i = 0; i < data.length; i++) {
			btns.push(data[i]);
		}
		if(ui.options.type==$.config.contentType.grid)
		btns.push({
			id: $.config.preButtonId + "print",
			icon: "icon-print",
			label: "打印",
			type: "button"
		});
		return btns;
	};
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

    //添加查询区构件按钮回调
    ui.addCallback("setComboGridValue_yljy",function(o){
        if (null == o) return;
        var rowData = o.rowData;
        if (null == rowData) return;
        ui.setItemValue("CSPCH",rowData.CSPCH);
N
    });
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
