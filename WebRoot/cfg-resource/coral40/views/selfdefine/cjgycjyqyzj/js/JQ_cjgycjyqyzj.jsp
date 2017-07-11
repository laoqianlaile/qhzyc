<%@page language="java" pageEncoding="UTF-8"%>
<%@ page import="com.ces.component.trace.utils.*" %>
<% String qybm = SerialNumberUtil.getInstance().getCompanyCode();
%>
<script type="text/javascript">
/***************************************************!
 * @date   2016-07-21 15:22:49
 * 系统配置平台自动生成的自定义构件二次开发JS模板
 * 详细的二次开发操作，请查看文档（二次开发手册/自定义构件.docx）
 * 注：请勿修改模板结构（若需要调整结构请联系系统配置平台）
 ***************************************************/
 
 
(function(subffix) {
	var resourceFolder = $.contextPath + "/cfg-resource/coral40/common";
	var hUI;

/**
 * 二次开发定位自己controller
 * 系统默认的controller: jQuery.contextPath + "/appmanage/show-module"
 * @returns {String}
 **/
window[CFG_actionName(subffix)] = function (ui) {
	// ui.assembleData 就是 configInfo
	return jQuery.contextPath + "/cjgycjyqyzj";
};
	

/**
 * 二次开发：复写自定义表单
 */
function _override_form (ui) {
		<%--ui._init = function(){--%>
			<%--var  qybm = ui.getItemJQ("QYBM");--%>
			<%--qybm.textbox("setValue","<%=companyCode%>");--%>
		<%--ui.getItemJQ("QYPCH").combogrid("reload",$.contextPath+"/sdzyccjgycjgxx!getqyzjpch.json");--%>
	<%--}--%>
//		debugger
//		ui.getItemJQ("QYPCH").combogrid("reload",$.contextPath+"/sdzyccjgycjgxx!getGzrydaGrid.json");
//	}
		ui._init = function () {
			hUI = ui;
			//初始化图片删除区域
			debugger
			var jywjDiv = ui.getItemBorderJQ("jywj");
			jywjDiv.empty();
			jywjDiv.html(initTPdiv());
			ui.getItemJQ("JYRY").combogrid("reload",$.contextPath+"/zzgzryda!getGzrydaGrid.json?dwlx=CJGQY");
			alert("cjgycjyqyzj");
			if (isEmpty(ui.options.dataId)) {
				var qybmJQ = ui.getItemJQ("QYBM");
				var qybm = "<%=qybm%>"
				ui.setFormData({QYBM: qybm});
			} else {
				var url = ui.getAction() + "!show.json?P_tableId=" + ui.options.tableId +
						"&P_componentVersionId=" + ui.options.componentVersionId +
						"&P_menuId=" + ui.options.menuId +
						"&P_workflowId=" + null2empty(ui.options.workflowId) +
						"&P_processInstanceId=" + ui._getProccessInstanceId() +
						"&id=" + ui.options.dataId + "&P_menuCode=" + ui.getParamValue("menuCode");
				var fromData = $.loadJson(url);
				if (!isEmpty(fromData.JYWJ))
					$.ns("ns" + subffix + "").uploadImage($.contextPath + "/spzstpfj/" + fromData.JYWJ);
			}
			ui.getItemJQ("QYPCH").combogrid("reload",$.contextPath+"/sdzyccjgycjgxx!getqyzjpch.json");
		}
		ui.bindEvent = function () {
			var jgpch = ui.getItemJQ("QYPCH");
			jgpch.combogrid("option", "onChange", function (e, data) {
				var rowData = jgpch.combogrid("grid").grid("getRowData", data.value);
				ui.setFormData({PCH: rowData.JGPCH, YCMC: rowData.YLMC, CSPCH:rowData.YLPCH, QYPCH:rowData.QYJGPCH});
			});

		}
		// ui.assembleData 就是 configInfo
		//ui.bindEvent = function () {
		//	  ui.setItemOption("NAME", "onChange", function(e, data) {});// 添加onChange事件
		//    ui.callItemMethod("USER_ID", "disable");// 禁用USER_ID
		//};

		ui.clickSave = function (op) {

			var _this = ui, jqForm = $("form", this.uiForm),
					url = this.getAction() + "!save.json?",
					formData, selfGrid, postData;

			if (!jqForm.form("valid")) return;
			// 保存前回调方法
			if (_this.processBeforeSave(jqForm, op) === false) return;
			// 获取表单数据
			postData = new FormData(jqForm);
			alert(postData);
			formData = jqForm.form("formData", false);
			alert(formData);
			$.each(formData, function (i, data) {
				postData.append(i, data);
				alert(i);
				alert(data);
			})
			var dfile = document.forms[jqForm.attr('id')]["imageUpload"].files[0];
			if (undefined != dfile && 'undefined' != dfile)
				postData.append("imageUpload", dfile);
			$.ajax({
				type: "POST",
				url: url,
				data: postData,
				/**
				 *必须false才会自动加上正确的Content-Type
				 */
				contentType: false,
				/**
				 * 必须false才会避开jQuery对 formdata 的默认处理
				 * XMLHttpRequest会对 formdata 进行正确的处理
				 */
				processData: false,
				timestamp: false,
				async: false,
				success: function (entity) {
					if (false === entity.success || !("ID" in entity)) {
						if (isEmpty(entity.message)) CFG_message("操作失败！", "warning");
						else CFG_message(entity.message, "warning");
						return;
					}
					// 保存后回调方法
					_this.processAfterSave(entity, op);
					CFG_message("操作成功！", "success");
					if ("save" === op) {
						jqForm.form("loadData", entity);
						if (_this.options.model === $.config.contentType.form) {
							selfGrid = _this.getSelfGrid();
							if (selfGrid) {
								selfGrid.reload();
							}
						}
					} else if ("close" === op) {
						_this.clickBackToGrid();
					} else if ("create" === op) {
						_this.clickCreate();
					} else {
						_this.doViewRecord(op);
					}
				},
				error: function () {
					CFG_message("操作失败！", "error");
				}
			});
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
function _override_tree(ui) {
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

	jQuery.extend(jQuery.ns("ns" + subffix), {
		viewImage: function (fileInput) {
			var view = $("#view${idSuffix}");
//        view.empty();
			if (window.FileReader) {
				var p = $("#preview${idSuffix}");
				var file = fileInput.files;
				var f = file[file.length - 1];

				var ImgSuffix = f.name.substring(f.name.lastIndexOf('.') + 1);

				//图片不能超过2M，
				if (!(f.size < 2097152 && (ImgSuffix == 'jpg' || ImgSuffix == 'bmp' || ImgSuffix == 'png'))) {
					CFG_message("图片必须小于2M，且格式必须为.png, .jpg 或 .bmp！", "error");
					$("#imageUpload${idSuffix}").val("");
					return false;
				}
				var fileReader = new FileReader();
				fileReader.onload = (function (file) {
					return function (e) {
						$.ns('ns' + subffix).uploadImage(this.result);
						return;
					}
				})(f);
				fileReader.readAsDataURL(f);
			}
		},
		uploadImage: function (tplj) {
			var view = $("#view${idSuffix}");
			view.empty();
			var image = new Image();
			image.src = tplj;
			image.className = "thumb";
			image.onload = function (e) {

				var div = document.createElement('div');
				$(div).append(this);
//				$(div).append('<span style="display: none;position: absolute; margin-top: 0px; margin-left: 6px; width: 20px; height: 20px;"><img onclick = "$.ns(\'ns' + subffix + '\').deleteImage(\'' + this.src + '\')" style = "height:20px" src = "' + resourceFolder + '/css/images/trace-image/trash_bin.png"></span>');
				var originWidth = this.width;
				var originHeight = this.height;
				div.style.float = "left";
//            div.style.marginTop = "10px";
				div.style.marginRight = "5px";
				div.style.height = "100px";
				if (((originWidth * 100) / originHeight) > 750) {
					div.style.width = "750px";
				} else {
					div.style.width = (originWidth * 100) / originHeight + "px";
				}
				image.style.height = div.style.height;
				image.style.width = div.style.width;
				this.style.position = "absolute";
				this.style.border = "1px solid #2AC79F",
						$(div).hover(function () {
							$(div).find("span").toggle();
						});
				view.append(div);
				if (view.css("width") != "0px") {
					$("#chooseImage${idSuffix}").css("margin-top", "75px");
				}
			}
		},
		deleteImage: function (src) {
			var strArray = src.split("/");
			var tplj = strArray[strArray.length - 1];
			$.ajax({
				type: "POST",
				url: $.contextPath + '/sdzyccjgycyxx!deleteTp.json',
				data: {'id': hUI.options.dataId},
				dataType: "json",
				success: function (data) {
					var view = $("#view${idSuffix}");
					hUI.setFormData({"CDZM": ""});
					view.empty();
					$("#imageUpload${idSuffix}").val("");
					$("#chooseImage${idSuffix}").css("margin-top", "0px");
				}
			})
		}
	});
	function initTPdiv() {//
		var html = "<div style=\"margin-bottom: 50px;margin-left:55px\">" +
				"<div class=\"fillwidth colspan2 clearfix\">" +
				"<div class=\"app-inputdiv6\">" +
				"<label class=\"app-input-label\" style=\"width:20%;\">检验文件：</label>" +
				"<div id=\"file${idSuffix}\">" +
				"<input class=\"inputfile\" type=\"file\" style=\"width:160px;display:none\" id=\"imageUpload${idSuffix}\" multiple=\"multiple\" lable=\"预览\"	accept=\"image/*\" name=\"imageUpload\" onchange=\"$.ns('ns" + subffix + "').viewImage(this)\"/>" +
				"</div>" +
				"<div style=\"margin-left: 10%\" id=\"view${idSuffix}\"></div>" +
				"<button style=\"margin-left: 0px\" id=\"chooseImage${idSuffix}\" class='ctrl-toolbar-element ctrl-init ctrl-init-button coral-button coral-component coral-state-default coral-corner-all coral-button-text-only coral-toolbar-item-component' type='button' onclick=\"$('#imageUpload${idSuffix}').click()\">" +
				"<span class=\"coral-button-text \">上传图片</span>" +
				"</button>" +
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
