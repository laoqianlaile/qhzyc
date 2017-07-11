<%@ page import="com.ces.config.utils.CommonUtil" %>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" %>
<%@ taglib prefix="ces" tagdir="/WEB-INF/tags" %>
<%
	String path = request.getContextPath();
	String resourceFolder = path + "/cfg-resource/coral40/common";
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
	request.setAttribute("idSuffix", CommonUtil.generateUIId(""));
	String plbh = request.getParameter("plbh");
	String pl = request.getParameter("pl");
	String treeId = request.getParameter("treeId");
%>
<style type="text/css">
	.pzxxBorder {
		position: relative;
		border: #E2E2E2 solid 1px;
		border-radius: 4px;
		margin: 4px;
	}
	.pzxxCheckbox {
		width: 20px;
		height: 80px;
		position: absolute;
		margin-left: 8px;
	}
	.pzxxImg {
		position: absolute;
		width: 80px;
		height: 80px;
		margin: 8px 30px;
	}
	.pzxxDiv {
		height: 26px;
	}
	.pzxxLabel {
		width: 100px;
		height: 20px;
		line-height: 20px;
		text-align: right;
	}
	.pzxxLabel {
		position:absolute ;
	}
	.pzxxSpan {
		position:absolute ;
		width: 80%;
		display: inline-block;
		margin-left: 100px;
	}
</style>
<div id="max${idSuffix}" >
	<div id="ft${idSuffix}" class="toolbarsnav clearfix top0">
		<ces:toolbar id="toolbarId${idSuffix}">
		</ces:toolbar>
		<div class='homeSpan' style="margin-top: -23px;"><div><div style='margin-left:20px;width: 150px;' id="nva${idSuffix}"> -品种信息 </div></div></div>
	</div>
	<div id="pzxxContext${idSuffix}" >
	</div>
</div>
<script type="text/javascript">
	var plbh = "<%=plbh%>";
	var pl = "<%=pl%>";
	var treeId = "<%=treeId%>";
	$.extend($.ns("namespaceId${idSuffix}"), {
		initPzxx: function () {
			$("#pzxxContext${idSuffix}").empty();
			var pzxxsByPlxx = $.loadJson($.contextPath + "/sczzpzxx!getPzxxByPlxx.json?plbh=" + plbh);
			for (var i in pzxxsByPlxx) {
				var pzxxByPlxx = pzxxsByPlxx[i];
				var html = "";
				html += "<div class='pzxxBorder'>";
					html += "<input id='"+pzxxByPlxx.ID+"' class='pzxxCheckbox' type='checkbox' value='"+pzxxByPlxx.ID+"'>";
					html += "<div class='pzxxImg'>";
						html += "<img width='80' height='80' src='<%=basePath%>/spzstpfj/" + (pzxxByPlxx.TPBCMC != null ? pzxxByPlxx.TPBCMC : "null.jpg") + "'>";
					html += "</div>";
					html += "<div style='margin: 8px 100px;'>";
						html += "<div class='pzxxDiv'><label class='pzxxLabel' for='pzdlxx${idSuffix}' class='pzxxLabel'>品种大类：</label><span class='pzxxSpan' name='pzdlxx${idSuffix}'>"+pzxxByPlxx.PL+"</span></div>";
						html += "<div class='pzxxDiv'><label class='pzxxLabel' for='pzxlxx${idSuffix}' class='pzxxLabel'>品种小类：</label><span class='pzxxSpan' name='pzxlxx${idSuffix}'>"+pzxxByPlxx.PZ+"</span></div>";
						html += "<div class='pzxxDiv'><label class='pzxxLabel' for='pzjsxx${idSuffix}' class='pzxxLabel'>品种介绍：</label><span class='pzxxSpan' name='pzjsxx${idSuffix}'>"+pzxxByPlxx.PZJS+"</span></div>";
					html += "</div>";
				html += "</div>";
				$("#pzxxContext${idSuffix}").append(html);
			}
		},
		toAdd: function () {
			CFG_clickButtonOrTreeNode($('#max${idSuffix}').data('configInfo'), "zzpzxgym", "新增", "2", $.ns("namespaceId${idSuffix}"));
			<%--CFG_clearComponentZone($('#max${idSuffix}').data('configInfo'));--%>
			//alert($.ns("namespaceId${idSuffix}").currentTreeNodeId);
			<%--$.ns("namespaceId${idSuffix}").currentTreeNodeId = treeNode.id;--%>
			//自定义url 获得指定的列表数据
			//$('#gridId${idSuffix}').grid('option', 'url', "${gurl}?id=" + treeNode.id);
		},
		del : function() {
			var checkedPzIds = [];
			$("#pzxxContext${idSuffix}").find("input[type='checkbox']").each(function(i){
				if ($(this).is(':checked')) {
					checkedPzIds.push($(this).val());
				}
			});
			if ($.isEmptyObject(checkedPzIds)) {
				CFG_message("请只少勾选一条记录", "warning");
				return;
			}
			$.confirm("确认删除选中的品种信息？", function(r) {
				if (r) {
					$.ajax({
						url : $.contextPath + "/sczzpzxx!delPzxxByPlxx.json",
						type : "post",
						dataType : "json",
						data : {
							plbh : plbh,
							checkedPzIds : checkedPzIds.join(",")
						},
						success : function(data) {
							if ($.isEmptyObject(data)) {//删除成功
								var $tree = $("#" + treeId);
								var selNode = $tree.tree("getSelectedNodes")[0];
								var childrenNodes = selNode.children;
								for (var i in checkedPzIds) {
									$("#" + checkedPzIds[i]).parents(".pzxxBorder").remove();
									for (var j in childrenNodes) {
										if(childrenNodes[j].id == checkedPzIds[i]){
											$tree.tree("removeNode",childrenNodes[j]);
											break;
										}
									}
								}
								CFG_message("删除成功！", "success");
							} else {
								CFG_message("品种【" + data.join("、") + "】存在关联信息，无法删除", "warning");
							}
						},
						error : function() {
							CFG_message("删除失败！", "error");
						}
					})
				}
			})
		},
		getPlxx: function (o) {
			return {
				status: true,
				plbh: plbh,
				pl: pl,
				treeId: treeId
			}
		}
	});
	var data = ['->',{
		"type": "button",
		"id": "addBtn${idSuffix}",
		"label": "新增",
		"name": "新增",
		"icon": "icon-create",
		"onClick": "$.ns(\"namespaceId${idSuffix}\").toAdd()"
	},{
		"type": "button",
		"id": "delBtn${idSuffix}",
		"label": "删除",
		"name": "删除",
		"icon": "icon-delete",
		"onClick": "$.ns(\"namespaceId${idSuffix}\").del()"
	}];
	$(function () {
		var configInfo = CFG_initConfigInfo({
			/** 页面名称 */
			'page': 'zzpzxsym.jsp',
			/** 页面中的最大元素 */
			'maxEleInPage': $('#max${idSuffix}'),
			/** 获取构件嵌入的区域 */
			'getEmbeddedZone': function () {
				return $('#max${idSuffix}');
			},
			/** 初始化预留区 */
			<%--'initReserveZones' : function(configInfo) {--%>
			<%--CFG_addToolbarButtons(configInfo, $('#toolbarId${idSuffix}'), 'cdmc', $('#toolbarId${idSuffix}').toolbar("getLength")-1);--%>
			<%--},--%>
			/** 获取返回按钮添加的位置 */
			<%--'setReturnButton' : function(configInfo) {--%>
			<%--CFG_setReturnButton(configInfo, $('#toolbarId${idSuffix}'));--%>
			<%--},--%>
			/** 页面初始化的方法 */
			'bodyOnLoad': function (configInfo) {
				//alert("bodyOnLoad");
				// 按钮权限控制
				//alert(configInfo.notAuthorityComponentButtons);
				$("#toolbarId${idSuffix}").toolbar("add", null, data);
				$.ns("namespaceId${idSuffix}").initPzxx();
			}
		});
		configInfo.namespace = $.ns("namespaceId${idSuffix}");
	});
</script>