<%@page language="java" pageEncoding="UTF-8"%>
<%@ page import="com.ces.component.trace.utils.*" %>
<% String qybm=SerialNumberUtil.getInstance().getCompanyCode();
%>
<script type="text/javascript">
	/***************************************************!
	 * @date   2016-04-14 17:38:00
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
			return jQuery.contextPath + "/sdzyccjgycrkxx";
		};


		/**
		 * 二次开发：复写自定义表单
		 */
		function _override_form (ui) {
			//获取企业编码
			ui._init = function(){

				if(isEmpty(ui.options.dataId)){
					var qybmJQ = ui.getItemJQ("QYBM");
					var qybm ="<%=qybm%>"
					ui.setFormData({QYBM:qybm});
				}else{

					ui.getItemJQ("QYPCH").combogrid("destroy");
					ui.getItemJQ("QYPCH").textbox();
					ui.getItemJQ("QYPCH").textbox("option","readonly",true);
					var url = ui.getAction() + "!show.json?P_tableId=" + ui.options.tableId +
									"&P_componentVersionId=" + ui.options.componentVersionId +
									"&P_menuId=" + ui.options.menuId +
									"&P_workflowId=" + null2empty(ui.options.workflowId) +
									"&P_processInstanceId=" + ui._getProccessInstanceId() +
									"&id=" + ui.options.dataId + "&P_menuCode=" + ui.getParamValue("menuCode"),
							formData = null;
					formData = $.loadJson(url);
					ui.setFormData({QYPCH:formData.QYPCH});
				}


			}
			//获取下拉框联动数据
			ui.bindEvent = function(){
				var ycrk = ui.getItemJQ("CKMC");
				ycrk.combogrid("option","onChange",function(e,data) {
					var rowData =ycrk.combogrid("grid").grid("getRowData",data.value);
					ui.setFormData({CKMC:rowData.CKMC,RKBH:rowData.CKBH});
				});
				//获取药材入库下拉框联动数据
				var pch = ui.getItemJQ("QYPCH");
				//var pch = ui.getItemJQ("PCH");
				pch.combogrid("option","onChange",function(e,data) {
					var rowData =pch.combogrid("grid").grid("getRowData",data.value);
					ui.setFormData({PCH:rowData.JGPCH,YCMC:rowData.YLMC,CDMC:rowData.YLCD,RKZL:rowData.JGZZL,CSPCH:rowData.YLPCH,QYPCH:rowData.QYJGPCH,BZGG:rowData.BZGG});
				});
			}

		};

		/**
		 *  二次开发：复写自定义列表
		 */
		function _override_grid (ui) {
			ui._init = function(){
				console.log();
				var onChangeFunction = "if (window.FileReader) {var file = this.files; var f = file[file.length-1]; var ImgSuffix = f.name.substring(f.name.lastIndexOf('.')+1);if(ImgSuffix != 'xls'){CFG_message('导入文件格式不正确', 'error');return false;} var formData = new FormData(document.forms.namedItem('fileinfo'));$.ajax({type: 'POST',url: '" + $.contextPath + "/sdzyccjgycrkxx!ImportXls.json',data: formData,contentType: false,processData: false,timestamp: false,async: false,success: function (data) {$('#imageUpload').val('');if(data.RESULT == 'SUCCESS'){CFG_message(data.MSG, 'success');}else if(data.RESULT == 'ERROR'){CFG_message(data.MSG, 'error');}$('#" + ui.uiGrid.attr("id") + "').grid('reload');},error: function () {$('#imageUpload').val('');CFG_message('操作失败！', 'error');}});}";
				var importDiv = "<div id=\"importDiv\" style=\"display:none\"><form id=\"importDiv\" name=\"fileinfo\" action=\"" + $.contextPath + "/sdzyccjgycrkxx" + "\" enctype=\"multipart	/form-data\" method=\"post\"><input class=\"inputfile\" type=\"file\" style=\"width:160px;display:none\" id=\"imageUpload\" lable=\"预览\" accept=\"application/vnd.ms-excel\" name=\"imageUpload\" onchange=\"" + onChangeFunction + "\"/></form></div>";
				$(ui.options.global).append(importDiv);
			}
			ui.beforeInitGrid = function(setting) {
				setting.fitStyle = "width";
				return setting;
			};
			ui.clickSecondDev = function(id){
				if (id == $.config.preButtonId + "import") {
					$("#imageUpload").click();
				}else if(id == $.config.preButtonId + "export"){
					window.location.href=$.contextPath + "/sdzyccjgycrkxx!downLoad";
				}
			};
		};

		/**
		 *  二次开发：复写自定义工具条
		 */
		function _override_tbar (ui) {
			ui.processItems = function (data) {
				var btns = [];
				for (var i = 0; i < data.length; i++) {
					btns.push(data[i]);
				}
				if(ui.options.type  == 1){  //grid(type =1 ) form(type =2 )
					btns.push({
						id: $.config.preButtonId + "import",
						icon: "icon-print",
						label: "导入药材入库",
						type: "button"
					});
					btns.push({
						id: $.config.preButtonId + "export",
						icon: "icon-print",
						label: "模板下载",
						type: "button"
					});
				}
				return btns;
			};
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
