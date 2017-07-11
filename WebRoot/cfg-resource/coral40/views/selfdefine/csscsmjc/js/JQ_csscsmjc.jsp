<%@page language="java" pageEncoding="UTF-8"%>
<style>
	.toolHeight{height: 35px}
	.coral-toolbar .coral-toolbar-border{
		height: 35px
	}

</style>
<script type="text/javascript">
	/***************************************************!
	 * @date   2015-12-03 14:24:34
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
			return jQuery.contextPath + "/csscsmjc";
		};

		/**
		 * 二次开发：复写自定义表单
		 */
		function _override_form (ui) {
			var __jjg__xsddhs = [];
			ui._init = function(){
				if(isEmpty(ui.options.dataId)) {//新增时默认打开条形码录入页面
					showDiv();
				}
			}
			/*	var csbm = $("#CSBM_"+ui.timestamp);
			 var csmc = $("#CSMC_"+ui.timestamp);
			 var jhpch = $("input[name='JHPCH']", this.uiForm);
			 //加载表单数据
			 if(jhpch.textbox("getValue")==null||jhpch.textbox("getValue")==''){
			 jQuery.ajax({
			 type:'post',
			 url:$.contextPath +'/csscjcxx!initFormData.json',
			 dataType:'json',
			 success:function(data){
			 jhpch.textbox("setValue",data.jhpch);
			 csbm.textbox("setValue",data.csbm);
			 csmc.textbox("setValue",data.csmc);
			 },
			 error:function(data){
			 }
			 });
			 }
			 }
			 ui.bindEvent = function (){
			 var gys = ui.getItemJQ("GYSMC");
			 gys.combogrid("option","onChange",function(e, data){
			 gys.combogrid("setValue",data.text);
			 });
			 }*/
			var showDiv;
			//录入条形码页面
			function showDiv() {
				var jqGlobal = $(ui.options.global);
				showDiv = $("<div id='showDialog'style='top: 20px'>" +
						"<div class = 'fillwidth colspan3 clearfix' style='margin-top:40px;top:20px'>" +
						"<label class ='app-input-label' style='width:120px;text-align:right'>二维码:</label>" +
						"<input id = 'barCode' /></div></div>" +
						"</div>").appendTo(jqGlobal);
				showDiv.dialog({
					appendTo : jqGlobal,
					modal : true,
					autoOpen : true,
					title : "请输入二维码",
					maxWidth : 400,
					width : 400,
					height : 150,
					maxHeight : 200,
					resizable : false,
				/*	position : {at: "center top+250"},*/
					onOpen : function() {
						$("#barCode").textbox({
							required : true
						});
						$("#barCode").textbox("option","onKeyDown",function( e,data){
							if(e.keyCode =='13'){//添加回车事件
								setFormData(data.text);
								showDiv.remove();
							}
						});
						var jqPanel = $(this).dialog("buttonPanel").addClass("app-bottom-toolbar"),
								jqDiv   = $("<div class=\"dialog-toolbar\" style='height: 35px'>").appendTo(jqPanel);
						jqDiv.toolbar({
							data: ["->", {id:"sure", label:"确定", type:"button"}, {id:"cancel", label:"取消", type:"button"}],
							onClick: function(e, ui) {
								if ("sure" === ui.id) {
									var barCode = $("#barCode").textbox("getValue");
									setFormData(barCode);
								} else {
									showDiv.dialog("close");
								}
							}
						});
					},
					onClose : function () {
						$(".coral-tabs-active").find('.icon').trigger("click");
						ui.clickBackToGrid();
						showDiv.remove();
					}
				});
			}
			function setFormData(barCode){
				var prefix = barCode.substring(0,2);
				//var detailForm = ui.getDetailForm();
				var detailGrid = ui.getDetailGrid();
				/*if("PC" === prefix){*/
				var jsonData = $.loadJson($.contextPath+"/csscsmjc!searchjjgjyData.json?barCode="+barCode);
				if(jsonData.result == "error"){
					CFG_message("请输入有效的销售订单号或该订单号已进场!","warning");
				}else {
					var ccxx = jsonData.jyData;
					var qyxx = jsonData.qyData;
					$.each(ccxx,function(index,data){
						__jjg__xsddhs.push(data.XSDDH);
					});
					for (var i = 0; i < ccxx.length; i++) {
						var cc = ccxx[i];
						var zje=cc.JYDJ*cc.JYZL
						//加载主表数据
						ui.setFormData({
							GYSMC: qyxx.QYMC,
							CDMC: cc.CD,
							ZZL: cc.JYZL,
							ZJE: zje,
							ZJS: cc.BZSL,
							JHPCH: barCode
						});
					}
					//添加
					var shxx = jsonData.xqData;
					/*detailGrid.clearGridData();*/
					if (shxx != null && shxx.length > 0) {
						var zzl = 0;

						for (var i = 0; i < shxx.length; i++) {
							var sh = shxx[i];
							var jydj=sh.JYDJ;
							var jyzl=sh.JYZL;
							var zje= jydj*jyzl;
							//加载从表列表数据
							var detailForm = {
								SPMC: sh.YPMC,
								/*SPBM:sh.PLBH,*/
								/*ZL: zl.toFixed(2),*/
//							ZSM:sh.CPZSM,
								JS: sh.BZSL,
								ZL: sh.JYZL,
								DJ: sh.JYDJ,
								JE: zje,
								SCRQ:sh.SCRQ,
								CDMC:sh.CDMC,
								SCPCH:sh.QYPCH,
								QYSCPCH:sh.PCH,
								XSDDH:sh.XSDDH,
								QYXSDDH:sh.QYXSDDH,
								BZPCH:sh.BZPCH,
								QYBZPCH:sh.QYBZPCH,
								SPLX: 1
							}

							detailGrid.addRowData(detailForm);
						}
						/*$("input[name='ZZL']").val(zzl.toFixed(2));*/
						showDiv.remove();
					}
				}
				/*	}/**!/else{
				 debugger
				 CFG_message("请输入有效的二维码!", "warning");
				 return;
				 }*/
			}

			/*function setPfsxx(jyzbm,barCode){//根据读码信息获取本市场内该经营者信息
			 var jyzxx = $.loadJson($.contextPath+'/jyzxx!getJyzxxByBmAndQybm.json?jyzbm='+jyzbm);
			 if("FATAL" == jyzxx.result){
			 CFG_message("批发商信息错误，请录入！", "warning");
			 }else if("SUCCESS" == jyzxx.result){
			 ui.setFormData({
			 GYSMC:jyzxx.JYZMC,
			 GYSBM:jyzxx.JYZBM
			 });
			 } else {
			 var id = jyzxx.ID;
			 var createJyz = $.loadJson($.contextPath + '/jyzxx!createJyz.json?id='+id+"&xltx=CS&barCode="+barCode);
			 var jsonData=$.loadJson($.contextPath +"/jyzxx!getJyzxx.json?xtlx=CS&zt=1");
			 var pfsmc = ui.getItemJQ("GYSMC");
			 pfsmc.combogrid("reload",jsonData.data);
			 ui.setFormData({
			 GYSMC:jyzxx.JYZMC,
			 GYSBM:jyzxx.JYZBM
			 });
			 CFG_message({message:"已添加经营者"+jyzxx.JYZMC+"！", position: {at:"center top+210"}}, "success");
			 }
			 }
			 */
			ui.clickSaveAll = function (){
				var jqForm  = $("form", this.uiForm),
						rowData ,
						formData, url, postData,op = "saveAll";
				var detailGrid = ui.getDetailGrid();
				rowData =detailGrid.getRowData();
				// 表单检验
				if (!jqForm.form("valid")) return false;
				// 检验
				if (!rowData.length) {
					CFG_message("请先添加明细列表数据再保存！", "warning");
					return false;
				}
				// 保存前回调方法
				if (ui.processBeforeSave(jqForm, op) === false) return;
				// 获取表单数据
				formData = jqForm.form("formData", false);
				postData = {E_entityJson: $.config.toString(formData),
					E_dEntitiesJson: $.config.toString(rowData),xsddhs:JSON.stringify(__jjg__xsddhs)};
				postData = this.processPostData(postData, CFG_common.P_SAVE_ALL);
				// 向列表添加数据
				// 重置表单数据

				url = ui.getAction() + "!saveAll.json?P_tableId=" + ui.options.tableId + "&P_D_tableIds=" + detailGrid.options.tableId
						+ "&P_componentVersionId=" + ui.options.componentVersionId
						+ "&P_menuId=" + ui.options.menuId
						+ "&P_menuCode=" + ui.getParamValue("menuCode");
				// console.log("master: " + $.config.toString(formData));
				// console.log("detail: " + $.config.toString(rowData));

				$.ajax({
					url : url,
					type : "post",
					data : postData,
					dataType : "json",
					success : function(rlt) {
						if (rlt.success) {
							jqForm.form("loadData", rlt.data.master);
							detailGrid.clearGridData();
							detailGrid.addRowData(rlt.data.detail);
							CFG_message("保存成功！", "success");
							// 保存后回调方法
							//ui.processAfterSave(rlt.data, op);
							$(".navigationbar").find(".navigationbar_element").trigger("click");
						} else {
							CFG_message(rlt.message, "warning");
						}
					},
					error : function() {
						CFG_message("保存主从表数据失败！", "error");
						$(".navigationbar").find(".navigationbar_element").trigger("click");
					}
				});
			}
		};

		/**
		 *  二次开发：复写自定义列表
		 */
		function _override_grid (ui) {
			ui.beforeInitGrid = function (setting) {
				var dj = $.inArray("单价", setting.colNames);
				setting.colModel[dj].formatter = 'text';
				setting.colModel[dj].formatoptions = {
					onChange : function(e, keyCode){
						if (keyCode.text.length === 0){
							CFG_message("'单价'不能为空", "error");
						}
						var reg = /^[0-9\\.]*$/
						if (!reg.test(keyCode.text)){
							CFG_message("'单价'格式错误", "error");
						}
						else{
							grid = $("#" + e.data.gridId);
							zl = parseFloat(grid.grid("getCellComponent",e.data.rowId,'ZL').val());
							if (!(zl>=0) && !(zl<0)) zl = 0;
							js = parseInt(grid.grid("getCellComponent",e.data.rowId,'JS').val());
							if (!(js>=0) && !(js<0)) js = 0;
							dj = parseFloat(keyCode.text);
							if (!(dj>=0) && !(dj<0)) dj = 0;
							grid.grid("getCellComponent",e.data.rowId,'DJ').textbox("setValue",dj.toFixed(2));

							je = zl * dj + js * dj;
							grid.grid("getCellComponent",e.data.rowId,'JE').textbox("setValue",je.toFixed(2));

							zje = parseFloat($("input[name='ZJE']").val());
							if (!(zje>=0) && !(zje<0)) zje = 0;
							zje += je;
							$("input[name='ZJE']").val(zje.toFixed(2));
						}
					}
				};


				var zl = $.inArray("重量", setting.colNames);
				setting.colModel[zl].formatter = 'text';
				setting.colModel[zl].formatoptions = { readonly : true}
				var js = $.inArray("件数", setting.colNames);
				setting.colModel[js].formatter = 'text';
				setting.colModel[js].formatoptions = { readonly : true}
				var dj = $.inArray("单价", setting.colNames);
				setting.colModel[dj].formatter = 'text';
				setting.colModel[dj].formatoptions = { readonly : true}
				var je = $.inArray("金额", setting.colNames);
				setting.colModel[je].formatter = 'text';
				setting.colModel[je].formatoptions = { readonly : true}

				return setting;
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
