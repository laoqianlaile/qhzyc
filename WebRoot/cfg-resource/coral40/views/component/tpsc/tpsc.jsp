<%--
  Created by IntelliJ IDEA.
  User: WILL
  Date: 15/7/21
  Time: 下午5:38
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.ces.config.utils.CommonUtil" %>
<%@ taglib prefix="ces" tagdir="/WEB-INF/tags" %>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
	request.setAttribute("idSuffix", CommonUtil.generateUIId(""));
%>
<div id="maxDiv${idSuffix}" class="fill">
	<div id="buttonDiv${idSuffix}">
		<ces:layoutRegion region="north" split="true">
			<ces:toolbar onClick="$.ns('namespaceId${idSuffix}').toolbarClick"
						 data="[{'label': '全部上传', 'id':'uploadAll', 'disabled': 'false','type': 'button'},{'label': '关闭', 'id':'close', 'disabled': 'false','type': 'button'}]">
			</ces:toolbar>
		</ces:layoutRegion>
	</div>
	<ces:layoutRegion region="south" split="true">
		<div id="upload${idSuffix}" style="margin-left: 20px;overflow: auto;height: 380px; "></div>
	</ces:layoutRegion>
</div>
<script>
	var upNum =0;
	var isUpOne = false;

	$.extend($.ns("namespaceId${idSuffix}"), {
		up : null,
		rowId : null,
		tableId : null,
		initUpload : function(fjlx,isOne,fileType) {

			$.ns('namespaceId${idSuffix}').up = $('#upload${idSuffix}').Huploadify({
				auto:false,
				fileTypeExts:fileType,
				multi:isOne,
				formData:{
					masterTableId : $.ns('namespaceId${idSuffix}').tableId,
					rowId : $.ns('namespaceId${idSuffix}').rowId,
					fjlx : fjlx,//附件类型
					isOne: isOne
				},
				showUploadedPercent:true,
				showUploadedSize:true,
				fileSizeLimit:10240,
				removeTimeout: 500,
				removeCompleted:true,
				uploader:'<%=basePath%>/trace!upload',
				async:false,
				onSelect : function(queue){ //upNum=queue.selected;
					upNum=queue.index;
//					isUpOne=isOne;
				},onUploadComplete : function() {

				},onCancel : function(file) {
					upNum--;
				}
			});
		},
		toolbarClick : function(e, ui) {
			if (ui.id == "uploadAll") {//关闭
				if(!isUpOne){
					if(upNum>1){
						CFG_message("只允许上传一个文件", "warning");
						return ;
					}
					
				}
				if(upNum=="0"){
					CFG_message("没有文件可以上传，请选择文件", "warning");
						return ;
						}
						
				
				$.ns('namespaceId${idSuffix}').up.upload('*');
				CFG_message("上传成功", "success");
			} else if (ui.id == "close") {//关闭
				var configInfo = $("#maxDiv${idSuffix}").data('configInfo');
				configInfo.dialog.dialog("close");
			}
		}
	});

	$(function () {
		var configInfo = CFG_initConfigInfo({
			/** 页面名称 */
			'page': 'tpsc.jsp',
			/** 页面中的最大元素 */
			'maxEleInPage': $('#maxDiv${idSuffix}'),
			/** 获取构件嵌入的区域 */
			'getEmbeddedZone': function () {
				return $('#layout${idSuffix}').layout('panel', 'center');
				//return $("#layout${idSuffix}");
			},
			/** 初始化预留区 */
			'initReserveZones': function (configInfo) {
				CFG_addToolbarButtons(configInfo, $('#toolbarId${idSuffix}'), 'toolBarReserve', 0);
			},
			/** 获取返回按钮添加的位置 */
			'setReturnButton': function (configInfo) {
				CFG_setReturnButton(configInfo, $('#toolbarId${idSuffix}'));
			},
			/** 页面初始化的方法 */
			'bodyOnLoad': function (configInfo) {
				$.ns('namespaceId${idSuffix}').rowId = "<%=request.getParameter("paramIn1")%>";
				$.ns('namespaceId${idSuffix}').tableId = "<%=request.getParameter("paramIn2")%>";
				var fjlx = CFG_getSelfParamValue(configInfo, 'selfParam1');
				var flag =CFG_getSelfParamValue(configInfo, 'selfParam2');
				var isFile =CFG_getSelfParamValue(configInfo, 'selfParam3');
				var fileType = '*.jpg;*.png;*.bmp;*.jpeg';

				if(isFile == 1 || isFile == "1"){//不是图片文件
					fileType = '*.pdf;*.doc;*.docx;*.xls;*.xlsx;*.rar';
				}


				var isOne = (flag == true || flag == "true" || flag=="") ? true : false ;
				if(!isOne){

					document.getElementById('close').style.left='15px';
				}
				isUpOne = isOne;
				if(!isUpOne){
					$("#buttonDiv${idSuffix}").hide();
				}
				$.ns('namespaceId${idSuffix}').initUpload(fjlx,isOne,fileType);
			}
		});
		if (configInfo) {
			//alert("系统参数：\t" + "关联的系统参数=" + CFG_getSystemParamValue(configInfo, 'systemParam1')
			//		+ "\n构件自身参数：\t" + "selfParam1=" + CFG_getSelfParamValue(configInfo, 'selfParam1')
			//		+ "\n构件入参：\t" + "inputParamName_1=" + CFG_getInputParamValue(configInfo, 'inputParamName_1'));
		}
		// 设置输出参数
		// configInfo.CFG_outputParams.xxx = '';
	});
</script>