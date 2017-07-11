<%@page language="java" pageEncoding="UTF-8"%>
<%@ page import="com.ces.component.trace.utils.SerialNumberUtil" %>
<%
	String companyCode = SerialNumberUtil.getInstance().getCompanyCode();
%>
<script type="text/javascript">
	/***************************************************!
	 * @date   2016-05-18 14:45:45
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
			return jQuery.contextPath + "/sdzycjjgscllxz";
		};


		/**
		 * 二次开发：复写自定义表单
		 */
		function _override_form (ui) {
			//初始化默认添加企业编码
			ui._init = function(){
				var  qybm = ui.getItemJQ("QYBM");
				qybm.textbox("setValue","<%=companyCode%>");
//				ui.getItemJQ("LLBM").textbox("destroy");
//				ui.getItemJQ("LLBM").combogrid({
//					colModel:[{name:"BMBH"},{name:"BMMC"}],
//					//colModel:[{name:"BMBH"},{name:"BMMC"},{name:"1111",hidden:true}],
//					colNames:["部门编号","部门名称"],
//					url:"sdzycscllxx!searchLlbmxx.json",
//					panelWidth:"290",
//					textField:"BMMC",
//					//valueField:"BMBH",
//					valueField:"BMMC",
//
//				})
			};
			ui.bindEvent = function(){
				// var ylpch = ui.getItemJQ("QYPCH");
				var pch = ui.getItemJQ("PCH");
				var rowData = "";
				pch.combogrid("option","onChange",function(e,data) {
					rowData = pch.combogrid("grid").grid("getRowData",data.value);
					var jsonData = $.loadJson($.contextPath+"/sdzycscllxx!getFlck.json?pch="+rowData.PCH);//获取发料仓库
					ui.setFormData({PCH:rowData.PCH,LLMC:rowData.YCMC,LLZZL:rowData.KC,YCDM:rowData.YCDM,FLCK:jsonData.FLCK,CSPCH:rowData.CSPCH,QYPCH:rowData.QYPCH,CD:rowData.CD});
				});
				//验证领料重量小于库存重量
				ui.setItemOption("LLZZL","onChange",function(e,data){
					var jyzl = ui.getItemValue("LLZZL");
					var rkzl = rowData.KC;
					if(parseFloat(jyzl)>parseFloat(rkzl)){
						CFG_message("请输入小于库存"+rkzl+"的数字","warning");
						ui.setItemValue("LLZZL",rkzl);
					}
					var wlgg = ui.getItemValue("WLGG");
					if(!isEmpty(wlgg)){
						if( parseFloat(jyzl) < parseFloat(wlgg)){
							CFG_message("请输入小于领料总重量"+jyzl+"的物料规格","warning");
							ui.setItemValue("WLGG","");

						}
					}
				});
				ui.setItemOption("WLGG","onChange",function (e ,data){
					var jyzl = ui.getItemValue("LLZZL");
					if( parseFloat(jyzl) < parseFloat(data.value)){
						CFG_message("请输入小于领料总重量"+jyzl+"的物料规格","warning");
						ui.setItemValue("WLGG","");
					}
				})
			}
			ui.afterSave = function(){
				ui.clickBackToGrid();
			}
		};

		function initToolbar(){
			$.fn['ctbar'].defaults = {
				processData: function(data, pos) {
					var ui =  this;
					if ("top" === pos) {
						var op = ui.options.op.toString();
						var poss = "";
						// 表单
						if ($.config.contentType.isForm(ui.options.type)) {
							if (op == '0') {
								poss =" - 新增";
							} else if (op == '1') {
								poss =" - 修改";
							} else if (op == '2') {
								poss =" - 详情";
							}
							if(data.length == 0){
								poss =" - 详请";
							}
						}

						var menuObj = $.loadJson($.contextPath + "/trace!getMenuById.json?id="+ui.options.menuId);
						if(menuObj.name != undefined){
							if("企业信息" == menuObj.name){
								menuObj.name = $(".coral-state-active").children().html();
							}


							if(ui.options.number ==1 || (ui.options.number === 2 && ui.options.type !== $.config.contentType.form)
							) {
								data.unshift({
									"type": "html",
									"content": "<div class='homeSpan'><div><div style='margin-left:20px'> - " + menuObj.name + poss + "</div>",
									frozen: true
								});
							}
						}
					}

					return data;
				}
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
