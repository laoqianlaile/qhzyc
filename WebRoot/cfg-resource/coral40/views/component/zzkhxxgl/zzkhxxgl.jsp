<%@ page import="com.ces.config.utils.CommonUtil"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="ces" tagdir="/WEB-INF/tags"%>
<%
	String path = request.getContextPath();
	String resourceFolder = path + "/cfg-resource/coral40/common";
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
	request.setAttribute("gurl","");
	request.setAttribute("turl","");
	request.setAttribute("idSuffix", CommonUtil.generateUIId(""));
	request.setAttribute("basePath", basePath);
%>
<style type="text/css">
	.app-input-label {
		float: left;
	}

	.tablewp {
		height: 983px;
	}

	 .overflow-auto{
		width: 100%;
		height: 940px;
		overflow: hidden;
	}

</style>
<div id="max${idSuffix}" class="fill" >
				<div class="toolbarsnav clearfix">
					<ces:toolbar id="toolbarId${idSuffix}" onClick="$.ns('namespaceId${idSuffix}').toolbarClick"
						data="['->',{'label': '保存', 'id':'save','cls':'save_tb', 'disabled': 'false','type': 'button'},{'label': '返回', 'id':'CFG_closeComponentZone','cls':'return_tb', 'disabled': 'false','type': 'button'}]">
					</ces:toolbar>
					<div class='homeSpan' style="margin-top: -23px;"><div><div style='margin-left:20px;width: 100px;' id="nva${idSuffix}"> -客户信息-新增 </div></div></div>
				</div>
	<div id="ddFormDiv${idSuffix}" style="margin-left: 20px;overflow: auto;height: 452px; ">
				<form id="khForm${idSuffix}" action="${basePath}zzkhxxg!saveKhxx.json"
				      enctype="multipart/form-data" method="post" class="coralui-form">
					<div class="app-inputdiv4" style ="height:32px;display: none">
						<input id="ID${idSuffix}" class="coralui-textbox" name="ID"/>
					</div>
    
					<div class="fillwidth colspan3 clearfix">
						<!------------------ 第一排开始---------------->
						<div class="app-inputdiv4">
							<label class="app-input-label" >客户编号：</label>
							<input id="KHBH${idSuffix}" name="KHBH" class="coralui-textbox"/>
						</div>
						<div class="app-inputdiv4">
							<label class="app-input-label" >客户名称：</label>
							<input id="KHMC${idSuffix}" name="KHMC"  data-options="required:true" class="coralui-textbox"/>
						</div>
						<div class="app-inputdiv4">
							<label class="app-input-label" >客户类型：</label>
							<input id="KHLX${idSuffix}"  data-options="required:true" name="KHLX" />
						</div>
						<!------------------ 第一排结束---------------->

						<!------------------ 第二排开始---------------->
						<div class="app-inputdiv4">
							<label class="app-input-label" >客户等级：</label>
							<input id="KHDJ${idSuffix}" name="KHDJ" />
						</div>
						<div class="app-inputdiv4">
							<label class="app-input-label" >联系人：</label>
							<input id="LXR${idSuffix}" name="LXR"  data-options="required:true" class="coralui-textbox"/>
						</div>
						<div class="app-inputdiv4">
							<label class="app-input-label" >职务：</label>
							<input id="ZW${idSuffix}" name="ZW" class="coralui-textbox"/>
						</div>
						<!------------------ 第二排结束---------------->

						<!------------------ 第三排开始---------------->
						<div class="app-inputdiv4">
							<label class="app-input-label" >电话：</label>
							<input id="DH${idSuffix}" name="DH" class="coralui-textbox"  data-options="required:true,pattern:'//^((0[0-9]{2,3}\-)?([2-9][0-9]{6,7})+(\-[0-9]{1,4})?)|((13[0-9]|15[012356789]|17[678]|18[0-9]|14[57])[0-9]{8})$//',errMsg:'请输入手机号码或者固话'" />
						</div>

						<div class="app-inputdiv4">
							<label class="app-input-label" >传真：</label>
							<input id="CZ${idSuffix}" name="CZ" class="coralui-textbox"/>
						</div>
						<div class="app-inputdiv4">
							<label class="app-input-label" >经度：</label>
							<input id="JD${idSuffix}" name="JD" class="coralui-textbox"/>
						</div>
						<!------------------ 第三排结束---------------->

						<!------------------ 第四排开始---------------->
						<div class="app-inputdiv4">
							<label class="app-input-label" >纬度：</label>
							<input id="WD${idSuffix}" name="WD" class="coralui-textbox"/>
						</div>



						<div class="app-inputdiv4" style ="height:32px;">
							<label class="app-input-label" >所属地区：</label>
							<input id="LSXZQH${idSuffix}" name="LSXZQH" />
						</div>
						<div class="app-inputdiv4" style ="height:32px;display: none;">
							<input id="LSXZQHBM${idSuffix}" name="LSXZQHBM"  class="coralui-textbox" />
						</div>
						<!------------------ 第四排结束---------------->
						<div class="app-inputdiv12">
							<label class="app-input-label" >地址：</label>
							<input id="DZ${idSuffix}" name="DZ"  data-options="required:true"  class="coralui-textbox"/>
						</div>

						<!------------------ 第五排开始---------------->


						<!------------------第五排结束----------------->
						<!------------------第六排开始----------------->
						<div class="app-inputdiv12">
							<label class="app-input-label" >网址：</label>
							<input id="WZ${idSuffix}" name="WZ" class="coralui-textbox"/>
						</div>

						<!------------------第六排结束----------------->
                        <!------------------第七排开始----------------->
                        <div class="app-inputdiv4"  style="display: none">
                            <label class="app-input-label" >用户名：</label>
                            <input id="YHM${idSuffix}" name="YHM" class="coralui-textbox" data-options="readonly:true" />
                        </div>

                        <div class="app-inputdiv4"  style="display: none">
                            <label class="app-input-label" >密码：</label>
                            <input id="MM${idSuffix}" type="password" name="MM" value="123456" data-options="readonly:true" class="coralui-textbox"/>
                        </div>
                        <div class="app-inputdiv4"  style="display: none">
                            <label class="app-input-label">账户状态:</label>
                            <div id="SFKTZH${idSuffix}" class="coralui-radiolist" name="SFKTZH"
                                 data-options="value:'1',column:2,data:[{value:'1',text:'启用'},{value:'2',text:'禁用'}]"></div>
                        </div>
                        <!------------------第七排结束----------------->
						<!------------------第九排开始----------------->
						<div class="app-inputdiv12">
							<label class="app-input-label" >备注：</label>
							<textarea id="BZXX${idSuffix}" name="BZXX" class="coralui-textbox"/>
						</div>

						<!------------------第九排结束----------------->

						<div class="app-inputdiv4">
							<label class="app-input-label" >图片：</label>
							<div id="file${idSuffix}_1">
								<input  type="file" style="width:160px;display:none"
										id="tp${idSuffix}" multiple="multiple" lable="预览"
										accept="image/*" name="tpFile" onchange="viewImageTp(this)"/>
										<div id="view${idSuffix}" style="float:left"></div>
							</div>
							<%-- <div id="view${idSuffix}" style="float:left"></div> --%>
							<button class='ctrl-toolbar-element ctrl-init ctrl-init-button coral-button coral-component coral-state-default coral-corner-all coral-button-text-only coral-toolbar-item-component'
									type='button' onclick="$('#tp${idSuffix}').click() " style="position:relative">
								<span class="coral-button-text ">选择图片</span>
							</button>
							<div id="viewImageTp" style="margin-left: 35%"></div>
						</div>
						<div class="app-inputdiv4">
							<label class="app-input-label" >LOGO图片：</label>
							<div id="file${idSuffix}">
								<input  type="file" style="width:160px;display:none"
										id="logoTp${idSuffix}" multiple="multiple" lable="预览"
										accept="image/*" name="logoTpFile"  onchange="viewImageLogo(this)"/>
										<div id="view1${idSuffix}" style="float:left"></div>
							</div>
							<%-- <div style="margin-left: 10%" id="view${idSuffix}"></div> --%>
							<div id=buttonlogo${idSuffix} ;style="margin-left: 0px,position:relative">
							<button style="margin-left: 0px;" id="chooseImage${idSuffix}"
							class='ctrl-toolbar-element ctrl-init ctrl-init-button coral-button coral-component coral-state-default coral-corner-all coral-button-text-only coral-toolbar-item-component'
									type='button' onclick="$('#logoTp${idSuffix}').click()"style="position:absolute">
								<span id="imageCheckSpan${idSuffix}" class="coral-button-text ">选择图片</span>
							</button>
							</div>
							<div id="viewImageLogo" style="margin-left: 35%"></div>

						</div>
						<div class="app-inputdiv4" style ="height:34px;">
						</div>
					</div>
				</form>
		</div>


				<div id="jqUC" style="display: none"></div>
	<div id="mdxxxz${idSuffix}" style="display: none">
		<form id="khForm${idSuffix}" action="${basePath}zzkhxxg!saveKhxx.json"
			  enctype="multipart/form-data" method="post" class="coralui-form">
			<div class="app-inputdiv4" style ="height:32px;display: none">
				<input id="ID${idSuffix}" class="coralui-textbox" name="ID"/>
			</div>

			<div class="fillwidth colspan3 clearfix">
				<!------------------ 第一排开始---------------->
				<div class="app-inputdiv4">
					<label class="app-input-label" >门店名称：</label>
					<input id="MDMC1${idSuffix}" name="MDMC1" data-options="required:true" class="coralui-textbox"/>
				</div>
				<div class="app-inputdiv4">
					<label class="app-input-label" >门店地址：</label>
					<input id="MDDZ1${idSuffix}" name="MDDZ1"  data-options="required:true" class="coralui-textbox"/>
				</div>
				<div class="app-inputdiv4">
					<label class="app-input-label" >门店联系人：</label>
					<input id="MDLXR1${idSuffix}"  data-options="required:true" name="MDLXR1" class="coralui-textbox"/>
				</div>
				<!------------------ 第一排结束---------------->

				<!------------------ 第二排开始---------------->
				<div class="app-inputdiv4">
					<label class="app-input-label" >用户名：</label>
					<input id="HYM1${idSuffix}" name="HYM1" data-options="readonly:true" class="coralui-textbox"/>
				</div>
				<div class="app-inputdiv4">
					<label class="app-input-label" >密码：</label>
					<input id="MM1${idSuffix}" name="MM1"  data-options="readonly:true" class="coralui-textbox"/>
				</div>
				<div class="app-inputdiv4">
					<label class="app-input-label" >门店联系电话：</label>
					<input id="MDLXDH${idSuffix}" name="MDLXDH"  class="coralui-textbox"/>
				</div>
				<!------------------ 第二排结束---------------->
				<div class="app-inputdiv4">
					<label class="app-input-label" >门店联系人手机：</label>
					<input id="MDLXRSJ${idSuffix}" name="MDLXRSJ" class="coralui-textbox"/>
				</div>

				<!------------------ 第三排开始---------------->
				<div class="app-inputdiv4">
					<label class="app-input-label" >经度：</label>
					<input id="JD1${idSuffix}" name="JD1" class="coralui-textbox"/>
				</div>

				<div class="app-inputdiv4">
					<label class="app-input-label" >纬度：</label>
					<input id="WD1${idSuffix}" name="WD1" class="coralui-textbox"/>
				</div>
				<div class="app-inputdiv4">
					<label class="app-input-label" >所属地区：</label>
					<input id="SSDQ1${idSuffix}" name="SSDQ1" />
				</div>
				<!------------------ 第三排结束---------------->

				<!------------------ 第四排开始---------------->
				<div class="app-inputdiv4">
					<label class="app-input-label" >所属地区编码：</label>
					<input id="SSDQBM1${idSuffix}" name="SSDQBM1" class="coralui-textbox"/>
				</div>
				<div class="app-inputdiv4">

					<%--<input id="sctp${idSuffix}" type="file" name="sctp" class="coralui-textbox"/>--%>
				</div>

			</div>
			<%--<input id="save1${idSuffix}" type="button" value="保存" style="position: absolute;bottom: 0;right: 70px;width: 50px">--%>
			<%--<input id="save2${idSuffix}" type="button" value="返回" style="position: absolute;bottom: 0;right: 10px;width: 50px">--%>
		</form>
	</div>

</div>
<script type="text/javascript">

	var gData;
	var temId="tem${idSuffix}";
	$.extend($.ns("namespaceId${idSuffix}"), {
		cdmcClick: function () {
			CFG_clickButtonOrTreeNode($('#max${idSuffix}').data('configInfo'), "lsxzqh", "地区名称", 2, $.ns("namespaceId${idSuffix}"));
		},
		setComboGridValue_lsxzqh:function ( o ){
			if( null == o ) return ;
			var obj = o.rowData;
			if( null ==  obj) return ;
			$("#LSXZQH${idSuffix}").combogrid("setValue",obj.CDBM);
			$("#LSXZQHBM${idSuffix}").textbox("setValue",obj.CDBM);
			$("#SSDQ1${idSuffix}").combogrid("setValue",obj.CDMC);
			$("#SSDQBM1${idSuffix}").textbox("setValue",obj.CDBM);
			//加载门店所属地区
			$.ns("namespaceId${idSuffix}").updateMdssdq();
		},
		setComboGridValue_lsxz:function ( o ){
			if( null == o ) return ;
			var obj = o.rowData;
			if( null ==  obj) return ;
			$("#LSXZQH${idSuffix}").combogrid("setValue",obj.CDBM);
			$("#LSXZQHBM${idSuffix}").textbox("setValue",obj.CDBM);
			$("#SSDQ1${idSuffix}").combogrid("setValue",obj.CDMC);
			$("#SSDQBM1${idSuffix}").textbox("setValue",obj.CDBM);
			//加载门店所属地区
			$.ns("namespaceId${idSuffix}").updateMdssdq();
		},
		toolbarClick : function(event, button) {
			/* alert(button.id); */
			if (button.id == "save") {//保存的方法
				$("#khForm${idSuffix}").form().submit();

			} else if (button.id == "CFG_closeComponentZone") {
				//CFG_clickReturnButton($('#max${idSuffix}').parent().data('parentConfigInfo'));
			    CFG_clickCloseButton($('#max${idSuffix}').data('configInfo'));
			}
		},
		toolbarClick1 : function(event, button) {
			var $grid = $("#gridId${idSuffix}");
			var mdymh = $.loadJson($.contextPath + "/zzkhxxgllb!getMdyhlsh.json");
			if (button.id == "add"){

				var dataAdded = {
					PID:temId,
					YHM : mdymh,
					MM : '000000',
					MDMC:"",
					MDDZ:"",
					MDLXR:"",
					MDLXDH:"",
					JD:"",
					WD:"",
					SSDQ:"",
					SSDQBM:""
				};
				var $grid = $("#gridId${idSuffix}");
				var totalRecords = $grid.grid("option", "records");
				lastsel = "tem_"+(totalRecords + 1);
				$grid.grid("addRowData", lastsel, dataAdded, "last");

			}





			if (button.id == "add") {
//				if(!$grid.grid("valid")){
//					CFG_message("列表内容校验未通过！","warn");
//					return;
//				};
				//操作完成的用户名将不给于修改

//				readOnlySet();
				if(!isExistYhm()){ return ;}
				<%--var totalRecords = $grid.grid("option", "records");--%>
				<%--lastsel = "tem_"+(totalRecords + 1);--%>
				<%--var mdymh = $.loadJson($.contextPath + "/zzkhxxgllb!getMdyhlsh.json");--%>
				<%--var dataAdded = {--%>
					<%--PID:temId,--%>
					<%--YHM : mdymh,--%>
					<%--MM : '000000',--%>
					<%--MDMC:$("#MDMC1").val(),--%>
					<%--MDDZ:$("#MDDZ1").val(),--%>
					<%--MDLXR:$("#MDLXR1").val(),--%>
					<%--MDLXDH:$("#MDLXDH1").val(),--%>
					<%--JD:$("#JD1").val(),--%>
					<%--WD:$("#WD1").val(),--%>
					<%--SSDQ:$("#SSDQ1").val(),--%>
					<%--SSDQBM:$("#SSDQBM1").val(),--%>

				<%--};--%>
				<%--$grid.grid("addRowData", lastsel, dataAdded, "last");--%>
				<%--//加载门店所属地区--%>
				<%--$.ns("namespaceId${idSuffix}").addMdssdq(event.data.rowId);--%>
			}
		},
		toolbarClick3 : function(e, ui) {
			//进行操作的行
			var rowId = e.data.rowId;
			var $grid = $("#gridId${idSuffix}");
			if (ui.id == "delBtn") {
				gData = $grid.grid("getRowData",rowId);
				var gDataId = gData.ID;
				if(gDataId.indexOf("span") != -1 || gDataId.indexOf("tem") !=-1){//如果是删除编辑状态的数据并且是没有进行保存的数据则执行grid的remove
					$grid.grid( 'clearEdited', rowId );
					$grid.grid("delRowData",rowId);
				}else{
					$.ajax({
						type:"post",
						url: "${basePath}zzkhxxgllb!deleteKhmdxx.json",
						data:{id:gDataId},
						dataType:"json",
						success :function (res){
							CFG_message("删除成功！", "success");
							$grid.grid("delRowData",rowId);
						},error :function (res){
							CFG_message("删除失败！", "error");
						}
					})
				}


			}
			if(ui.id == "viewBtn"){
				gData = $grid.grid("getRowData",rowId);
				showImg(gData.TP);
			}
			if(ui.id == "uploadBtn"){
				var ids = $("#ID${idSuffix}").textbox("getValue");
//				if(isEmpty(ids)){//如果是新增操作先进行保存，在进行门店信息添加图片
//
//				}
				gData = $grid.grid("getRowData",rowId);
				var isValid = $grid.grid('valid', rowId);
				if (!$("#khForm${idSuffix}").form("valid")){CFG_message("信息输入不全","error"); return false;}
				if(!isValid){CFG_message("列表内容校验未通过！","warn"); return ;};
				//上传文件前进行数据保存；
				if(isValid){
					 gData = $grid.grid("getRowData",rowId);
					$.ajax({
						url:"${basePath}zzkhxxgllb!saveMdxx.json",
						type:"post",
						data:{E_entityJson: $.config.toString(gData)},
						dataType:"json",
						success:function (res){
							var dataAdded={
								ID : res.ID,
								MDMC : res.MDMC,
								MDDZ : res.MDDZ,
								MDLXR : res.MDLXR,
								MDLXDH : res.MDLXDH,
								JD : res.JD,
								TP : res.TP,
								PID :res.PID,
								WD :res.WD
							};
							//替换掉当前编辑的行数据：主要作用添加ID，防止再次编辑的时候多次添加重复的数据
							$grid.grid("setRowData",rowId, dataAdded);
							showDialog(rowId);
						},error:function (res){

						}
					});
				}
			}
		},
		/* 构件方法和回调函数 */
		getCategoryId : function() {
			alert("调用方法getCategoryId！");
			var o = {
		        status : true
		    };
		    o.categoryId = '1';
		    return o;
		},
		refreshGrid : function(cbParam1) {
			alert("调用回到函数refreshGrid(" + cbParam1 + ")");
		},
		updateMdssdq : function(){
			//加载门店所属地区
			var ids = $("#gridId${idSuffix}").grid("getDataIDs");
			var ssdqJson = $.loadJson($.contextPath + "/zzkhxxgllb!getMdssdqByKhssdq.json?ssdq="+$("#LSXZQHBM${idSuffix}").textbox("getValue"));
			for(var i in ids){
				var $ssdqCombobox = $("#gridId${idSuffix}").grid("getCellComponent", ids[i], "SSDQ");
				$ssdqCombobox.combobox("reload", ssdqJson);
				var $ssdqbmtextbox = $("#gridId${idSuffix}").grid("getCellComponent", ids[i], "SSDQBM");
				$ssdqbmtextbox.textbox("setValue", "");
			}
		},addMdssdq : function(rowId){
			//加载门店所属地区
			var ssdqJson = $.loadJson($.contextPath + "/zzkhxxgllb!getMdssdqByKhssdq.json?ssdq="+$("#LSXZQHBM${idSuffix}").textbox("getValue"));
				var $ssdqCombobox = $("#gridId${idSuffix}").grid("getCellComponent", rowId, "SSDQ");
				$ssdqCombobox.combobox("reload", ssdqJson);
				var $ssdqbmtextbox = $("#gridId${idSuffix}").grid("getCellComponent", rowId, "SSDQBM");
				$ssdqbmtextbox.textbox("setValue", "");
		}
	});
	var buttonOpts = {
		id: "combogrid_buttonId",
		label: "构件按钮",
		icons: "icon-checkmark-circle",
		text: false,
		onClick: $.ns('namespaceId${idSuffix}').cdmcClick
	};
	var _colNames = ["地区编号", "地区名称"];
	var _colModel = [{name: "CDBM", width: 65, sortable: true}, {name: "CDMC", width: 155, sortable: true}];
	$(function() {
		var configInfo = CFG_initConfigInfo({
			/** 页面名称 */
			'page' : 'zzkhxxgl.jsp',
			/** 页面中的最大元素 */
			'maxEleInPage' : $('#max${idSuffix}'),
			/** 获取构件嵌入的区域 */
			'getEmbeddedZone' : function() {
				<!--return $('#layoutId${idSuffix}').layout('panel', 'center');-->
				return $('#max${idSuffix}');
			},
			/** 初始化预留区 */
			'initReserveZones' : function(configInfo) {
				//CFG_addToolbarButtons(configInfo, $('#toolbarId${idSuffix}'), 'toolBarReserve', $('#toolbarId${idSuffix}').toolbar("getLength"));
			},
			<%--/** 获取返回按钮添加的位置 */--%>
			<%--'setReturnButton' : function(configInfo) {--%>
				<%--//CFG_setReturnButton(configInfo, $('#toolbarId${idSuffix}'));--%>
			    <%--CFG_setCloseButton(configInfo, $('#toolbarId${idSuffix}'));--%>
			<%--},--%>
			/** 页面初始化的方法 */
			'bodyOnLoad' : function(configInfo) {
				<%--$('#SSDQ1${idSuffix}').combobox({--%>
					<%--valueField:'value',--%>
					<%--textField:'text',--%>
				<%--});--%>
				//var ssdqdata=$.loadJson($.contextPath + "/zzkhxxgllb!getKhbh.json");
				$("#SSDQ1${idSuffix}").combogrid({
					url: $.contextPath + "/cdxx!getCdxxGrid.json",
					multiple: false,
					sortable: true,
					colModel: _colModel,
					colNames: _colNames,
					enableFilter:true,
					width: 200,
					textField: "CDMC",
					valueField: "CDBM",
					height: "auto",
					buttonOptions: buttonOpts,
				});
				$("#SSDQ1${idSuffix}").combogrid("option","onChange",function(e,data){
					$("#SSDQ1${idSuffix}").combogrid("setValue",data.text);
					$("#SSDQBM1${idSuffix}").textbox("setValue",data.value);
				})

				//$('#SSDQ1${idSuffix}').combobox('reload',ssdqdata);
				//$('#SSDQ1${idSuffix}').combobox('data',ssdqdata);
				//页面初始化进行默认设置
				var inputParamId =CFG_getInputParamValue(configInfo, 'inputParamId');
				<%--$("#KHMC${idSuffix}").textbox("option","readonly",true);--%>
				$("#KHLX${idSuffix}").combobox({
					valueField:'value',
					textField:'text',
					sortable: true,
					width: "auto",
					url:$.contextPath + "/zzkhxxgllb!getKhlx.json"});
				$("#KHDJ${idSuffix}").combobox({
					valueField:'value',
					textField:'text',
					sortable: true,
					width: "auto",
					url:$.contextPath + "/zzkhxxgllb!getKhdj" +
					".json"
				});
				$("#LSXZQH${idSuffix}").combogrid({
					//url: $.contextPath + "/cdxx!getCdxxGrid.json",
					url: $.contextPath + "/cdxx!getShdqxxGrid.json",
					multiple: false,
					sortable: true,
					colModel: _colModel,
					colNames: _colNames,
					width: "auto",
					textField: "CDMC",
					valueField: "CDBM",
					height: "auto",
					buttonOptions: buttonOpts,
					onChange : function(e,data){
						$("#LSXZQH${idSuffix}").combogrid("setValue",data.value);
						$("#LSXZQHBM${idSuffix}").textbox("setValue",data.value);
						//加载门店所属地区
						$.ns("namespaceId${idSuffix}").updateMdssdq();
					}
				});
				$("#LSXZQH${idSuffix}").combogrid("option","onChange",function(e,data){
					$("#LSXZQH${idSuffix}").combogrid("setValue",data.value);
					$("#LSXZQHBM${idSuffix}").textbox("setValue",data.value);
					//加载门店所属地区
					$.ns("namespaceId${idSuffix}").updateMdssdq();
				});
				$("#ID${idSuffix}").textbox({readonly:true});
				$("#KHBH${idSuffix}").textbox({readonly:true});
				var $grid = $("#gridId${idSuffix}");
				if(isEmpty(inputParamId)){//如果是新增操作在设置临时ID
					$("#ID${idSuffix}").textbox("setValue",temId);
					var khbh = $.loadJson($.contextPath + "/zzkhxxgllb!getKhbh.json");
					//debugger
					$("#KHBH${idSuffix}").textbox("setValue",khbh);
					//获得系统自动生成的用户名
					var khymh = $.loadJson($.contextPath + "/zzkhxxgllb!getKhyhlsh.json");
					$("#YHM${idSuffix}").textbox("setValue",khymh);
					$("#MM${idSuffix}").textbox("setValue","000000");
				}else{//修改操作初始化页面操作
					var $khObj = $.loadJson($.contextPath + "/zzkhxxgllb!searchKhxx.json?id="+inputParamId);
					if($khObj !=null && $khObj.MM && $khObj.MM.length > 0){
						$khObj.MM = "======";
					}
					$("#nva${idSuffix}").html("-客户信息-修改");
					$("#khForm${idSuffix}").form("loadData",$khObj);
					$("<img src='"+ $.contextPath +"/spzstpfj/"+$khObj.LOGOTP+"' height='100px' />").appendTo($("#viewImageLogo"));
					$("<img src='"+ $.contextPath +"/spzstpfj/"+$khObj.TP+"' height='100px' />").appendTo($("#viewImageTp"));
                    //如果有用户名，则把用户名设为只读
                    var yhmValue = $("#YHM${idSuffix}").textbox("getValue");
                    if(yhmValue.length > 0){
                        $("#YHM${idSuffix}").textbox("option", "readonly", true);
                    }
					//门店列表数据初始化
					$grid.grid("option", "datatype", "json");
					$grid.grid("option","url", $.contextPath + "/zzkhxxgllb!searchKhmdxx.json?pid="+inputParamId);
					$grid.grid("reload");
					$grid.grid("option","onComplete",function(){readOnlySet();});
					temId = inputParamId;
				}
				$grid.grid("setColProp", "YHM", {
					"formatoptions": {required: true,
						 onChange: function (e, data) {
							if(!isExistYhm()){
								$("#" + e.data.gridId).grid("getCellComponent", e.data.rowId, "YHM").textbox("setValue", "");
							}
						}
					}
				});
				//加载门店所属地区
				var ssdqJson = $.loadJson($.contextPath + "/zzkhxxgllb!getMdssdqByKhssdq.json?ssdq="+$("#LSXZQHBM${idSuffix}").textbox("getValue"));
				$grid.grid("setColProp", "SSDQ", {
					"formatoptions": {
						"data": ssdqJson, onChange: function (e, data) {
							$("#" + e.data.gridId).grid("getCellComponent", e.data.rowId, "SSDQBM").textbox("setValue", data.value);
						}
					}
				});

				$("#gridId${idSuffix}").css("height","300px");
			}
		});
		if (configInfo) {
			//alert("系统参数：\t" + "关联的系统参数=" + CFG_getSystemParamValue(configInfo, 'sysParam1')
			//		+ "\n构件自身参数：\t" + "selfParam1=" + CFG_getSelfParamValue(configInfo, 'selfParam1') 
			//		+ "\n构件入参：\t" + "inputParamName_1=" + CFG_getInputParamValue(configInfo, 'inputParamId'));
			//alert(CFG_getInputParamValue(configInfo, 'inputParamId'));
			//alert(CFG_getInputParamValue(configInfo, 'inputParamId1'));

		}
		configInfo.CFG_outputParams = {'success':'otp'};
	});

	$("#khForm${idSuffix}").submit(function (){

		if (!$("#khForm${idSuffix}").form("valid")){CFG_message("您的信息输入不全","error"); return false;}
		//debugger;
		//var $grid = $("#gridId${idSuffix}");
		//var editrow = $grid.grid("option", "editrow");
		var formdata = new FormData(this);
		//var isValid = $grid.grid('valid', editrow);
		//var isValid2 = $grid.grid('valid');
		//var rowData = $grid.grid('getRowData');
		//formdata.append("rowData", $.config.toString(rowData));
	//	if(isValid){
			$.ajax({
				type: 'POST',
				url: $.contextPath + "/zzkhxxgllb!saveKhxx.json",
				data: formdata,
				/**
				 *必须false才会自动加上正确的Content-Type
				 */
				contentType: false,
				/**
				 * 必须false才会避开jQuery对 formdata 的默认处理
				 * XMLHttpRequest会对 formdata 进行正确的处理
				 */
				processData: false,
				async : false,
				timestamp: false,
				success: function (data) {
					CFG_message("操作成功！", "success");
				},
				error: function () {
					CFG_message("操作失败！", "error");
				}
			})

		return false;
	})
	function showDialog(rowId){
		var jqUC = $("#jqUC");
		jqUC.dialog({
			modal : true,
			title : "请选择文件",
			width : 440,
			height : 220,
			resizable : false,
			position : {
				at: "center"
			},
			onClose : function() {
				jqUC.dialog("close");
			},
			onCreate : function() {
				var html = "<form id=\"mdtpForm${idSuffix}\" enctype=\"multipart/form-data\"  method=\"post\" class=\"coralui-form\" rowId=\"" + rowId + "\" onSubmit = 'return submitForm(this)'  >" +
						"<input type='file' style='display:none' id='mdtpFile${idSuffix}'  onchange=\"viewImage(this)\"  name=\"mdtpFile\"/>" +
						"<input type='text' style='display:none' name=\"ID\" value='"+gData.ID+"'/>" +
						"<button class='ctrl-toolbar-element ctrl-init ctrl-init-button coral-button coral-component coral-state-default coral-corner-all coral-button-text-only coral-toolbar-item-component'"
						+"type='button' onclick=\"$('#mdtpFile${idSuffix}').click()\">"
						+"<span class=\"coral-button-text \">选择图片</span>"
						+"</button><div id=\"preview${idSuffix}\" style=\"text-align:center\"></div></form>";
				//var jqDiv = $("<div class=\"app-inputdiv-full\" style=\"padding:10px 20px;\"></div>").appendTo(this);
				var jq = $(html).appendTo(this);
				//jq.textbox({width: 200, maxlength: 22});
			},
			onOpen : function() {
				$("div > div[role=dialog]").appendTo("form#mdtpForm${idSuffix}");
				var jqPanel = $(this).dialog("buttonPanel").addClass("app-bottom-toolbar"),
						jqDiv   = $("<div class=\"dialog-toolbar\">").appendTo(jqPanel);
				jqDiv.toolbar({
					data: ["->", {id:"sure", label:"确定", type:"button"}, {id:"cancel", label:"取消", type:"button"}],
					onClick: function(e, ui) {
						if ("sure" === ui.id) {
							var $grid = $("#gridId${idSuffix}");
							var dataFile = $("#mdtpFile${idSuffix}").val();
							var isValid = $grid.grid('valid', rowId);
							if(isValid){
								if(isEmpty(dataFile)){
									$.alert("请选择图片");
									return;
								}else{
									$("#mdtpForm${idSuffix}").submit();
									$grid.grid( 'clearEdited', rowId );
									jqUC.dialog("close");
									$("#preview${idSuffix}").empty();
								}
							}else{
								$.alert("当前编辑行校验不通过");
								jqUC.dialog("close");
								return;
							}
						} else {
							jqUC.dialog("close");
						}
					}
				});
			}
		});
	}

	function submitForm(form) {
		var $grid = $("#gridId${idSuffix}");
		var rowId = $(form).attr("rowId");
		form =$("#mdtpForm${idSuffix}").form();
		var fileObj = document.getElementById('mdtpFile${idSuffix}').files[0];
		var formdata = new FormData();
		if(gData.ID == ""){
			gData = $grid.grid("getRowData",rowId);
		}
		formdata.append("ID",gData.ID);
		formdata.append("mdtpFile",fileObj);
		$.ajax({
			type: 'POST',
			url: "${basePath}zzkhxxgllb!saveMdtpxx.json",
			data: formdata,
			contentType: false,//必须false才会自动加上正确的Content-Type
			processData: false,//必须false才会避开jQuery对 formdata 的默认处理XMLHttpRequest会对 formdata 进行正确的处理
			timestamp: false,
			success: function (data) {
				gData.TP = data.TP;
				/* alert(" 你的照片上传成功 " ); */
				$grid.grid("setRowData",rowId, gData);
				//$grid.grid("setRowData",rowId, dataAdded);
				CFG_message("操作成功！", "success");
			},
			error: function () {
				CFG_message("操作失败！", "error");
			}
		})
		return false;
	}
	

	function viewImageLogo(fileInput) {
		if (window.FileReader) {
			var p = $("#viewImageLogo");
			var file = fileInput.files;

			for (var i = 0, f; f = file[i]; i++) {

				var ImgSuffix = f.name.substring(f.name.lastIndexOf('.')+1);
				//图片不能超过2M，
				if(!(f.size<2097152 && (ImgSuffix=='jpg'||ImgSuffix=='bmp'||ImgSuffix=='png'))){
					CFG_message("图片必须小于2M，且格式必须为.png, .jpg 或 .bmp！", "error");
					$("#logoTp${idSuffix}").val("");
					return false;
				}
				var fileReader = new FileReader();
				fileReader.onload = (function (file) {
					return function (e) {
                        //$("#viewImageLogo img").remove();
						/* var span = document.createElement('span');
						span.innerHTML = '<img width="100px" style = "position:relative;margin:10px 5px 0px 0px" src = "' + this.result + '" alt = "' + file.name + '" onload="uploadImage(this.result)" />';
						p.append(span); */
						 uploadImageLOGO(this.result);
                          return;
						
					}
				})(f);
				fileReader.readAsDataURL(f);
			}
		}
	}
	
   function uploadImageLOGO(tplj) {
        var view = $("#view1${idSuffix}");
        view.empty();
        var image = new Image();
        image.src = tplj;
        image.className = "thumb";
        image.onload = function (e) {
            var div = document.createElement('div');
            $(div).append(this);
            $(div).append('<div style="display: none;position:absolute;top:5px; margin-top: 0px; margin-left: 6px; width: 20px; height: 20px;"><img onclick = "deleteImageLOGO(\'' + this.src + '\')" style = "height:20px" src = "<%=resourceFolder%>/css/images/trace-image/trash_bin.png"></div>');
            var originWidth = this.width;
            var originHeight = this.height;
            div.style.float = "left";
//            div.style.marginTop = "10px";
            div.style.marginRight = "5px";
            div.style.height = "100px";
            div.style.position="relative";
            if(((originWidth * 100) / originHeight) > 750){
                div.style.width = "750px";
            }else{
                div.style.width = (originWidth * 100) / originHeight + "px";
            }
            image.style.height = div.style.height;
            image.style.width = div.style.width;
            this.style.border = "1px solid #2AC79F"
            $(div).hover(function () {
                $(div).find("div").toggle();
            }); 
            view.append(div);
            if(view.css("width") != "0px"){
                $("#chooseImage${idSuffix}").css("margin-top","75px");
            }
        }
    }
     function deleteImageLOGO(src) {
        var strArray = src.split("/");
        var tplj = strArray[strArray.length - 1];
        $.ajax({
            type: "POST",
            url: $.contextPath + '/zzgzryda!deleteImage.json',
            data: {'tplj': tplj},
            dataType: "json",
            success: function (data) {
                var view = $("#view1${idSuffix}");
                view.empty();
                $("#logoTp${idSuffix}").val("");
                $("#chooseImage${idSuffix}").css("margin-top","0px");
            }
        })
    }
    
	function viewImageTp(fileInput) {
		if (window.FileReader) {
			var p = $("#viewImageTp");
			p.empty();/*清除之前新增的数据  */
			var file = fileInput.files;
			for (var i = 0, f; f = file[i]; i++) {

				var ImgSuffix = f.name.substring(f.name.lastIndexOf('.')+1);

				//图片不能超过2M，
				if(!(f.size<2097152 && (ImgSuffix=='jpg'||ImgSuffix=='bmp'||ImgSuffix=='png'))){
					CFG_message("图片必须小于2M，且格式必须为.png, .jpg 或 .bmp！", "error");
					$("#tp${idSuffix}").val("");
					return false;
				}
				var fileReader = new FileReader();
				fileReader.onload = (function (file) {
					return function (e) {
                       /*  $("#viewImageTp img").remove();
						var span = document.createElement('span');
						span.innerHTML = '<img height="100px" style = "position:relative;margin:10px 5px 0px 0px" src = "' + this.result + '" alt = "' + file.name + '" />';
						p.append(span); */
						uploadImageTp(this.result);
                          return;
					}
				})(f);
				fileReader.readAsDataURL(f);
			}
		}
	}
	function uploadImageTp(tplj) {
        var view = $("#view${idSuffix}");
        view.empty();
        var image = new Image();
        image.src = tplj;
        image.className = "thumb";
        image.onload = function (e) {
            var div = document.createElement('div');
            $(div).append(this);
            $(div).append('<div style="display: none;position:absolute;top:5px; margin-top: 0px; margin-left: 6px; width: 20px; height: 20px;"><img onclick = "deleteImageTp(\'' + this.src + '\')" style = "height:20px" src = "<%=resourceFolder%>/css/images/trace-image/trash_bin.png"></div>');
            var originWidth = this.width;
            var originHeight = this.height;
            div.style.float = "left";
//            div.style.marginTop = "10px";
            div.style.marginRight = "5px";
            div.style.height = "100px";
            div.style.position="relative";
            if(((originWidth * 100) / originHeight) > 750){
                div.style.width = "750px";
            }else{
                div.style.width = (originWidth * 100) / originHeight + "px";
            }
            image.style.height = div.style.height;
            image.style.width = div.style.width;
            this.style.border = "1px solid #2AC79F"
            $(div).hover(function () {
                $(div).find("div").toggle();
            }); 
            view.append(div);
            if(view.css("width") != "0px"){
                $("#chooseImage${idSuffix}").css("margin-top","75px");
            }
        }
    }
     function deleteImageTp(src) {
        var strArray = src.split("/");
        var tplj = strArray[strArray.length - 1];
        $.ajax({
            type: "POST",
            url: $.contextPath + '/zzgzryda!deleteImage.json',
            data: {'tplj': tplj},
            dataType: "json",
            success: function (data) {
                var view = $("#view${idSuffix}");
                view.empty();
                $("#tp${idSuffix}").val("");
                $("#chooseImage${idSuffix}").css("margin-top","0px");
            }
        })
    }
    /******************用户名  onChange 事件 begin*************************/
    $("#YHM${idSuffix}").textbox({
        onChange:  function (e, data) {
            if(data.value == ""){
                $("#MM${idSuffix}").textbox("setValue","");
            }else{
				var yhmJson = $.loadJson($.contextPath + "/zzkhxxgllb!validYhm.json?yhm="+data.value);
				if(yhmJson.length > 0){
                    CFG_message("用户名重复！", "error");
                    $("#YHM${idSuffix}").text终止khxxbox("setValue","");
                    $("#MM${idSuffix}").textbox("setValue","");
                    return false;
				}
				var num=$.loadJson($.contextPath + "/zzkhxxgllb!isExist.json?yhm="+data.value);
				if( num > 0 ) {
					CFG_message("用户名重复！","error");
					$("#YHM${idSuffix}").textbox("setValue","");
					$("#MM${idSuffix}").textbox("setValue","");
					return false;
				}
                $("#MM${idSuffix}").textbox("setValue","000000");
            }
        }
    });
    /******************用户名  onChange 事件 end*************************/
    /******************密码  onChange 事件 begin*************************/
    $("#MM${idSuffix}").textbox({
        onChange:  function (e, data) {
            if($("#YHM${idSuffix}").textbox("getValue").length > 0){
                if(data.value.length == 0){
                    $("#MM${idSuffix}").textbox("setValue","000000");
                    CFG_message("密码不能为空！", "error");
                }
            }
        }
    });
    /******************密码  onChange 事件 end*************************/
	function isExistYhm(){
		<%--var $grid = $("#gridId${idSuffix}");--%>
		<%--var yhm = $grid.grid("getCol","YHM");--%>
		<%--var khyhm = $("#YHM${idSuffix}").textbox("getValue");--%>
		<%--var s = yhm.join(",")+",";--%>
		<%--if(s.replace(khyhm+",","").indexOf(khyhm+",")>-1) {--%>
			<%--CFG_message("用户名与客户用户名重复！","warn");--%>
			<%--return false;--%>
		<%--}--%>
		<%--for(var i=0;i<yhm.length;i++) {--%>
			<%--if(s.replace(yhm[i]+",","").indexOf(yhm[i]+",")>-1) {--%>
				<%--CFG_message("用户名重复！","warn");--%>
				<%--return false;--%>
			<%--}--%>
			<%--var num=$.loadJson($.contextPath + "/zzkhxxgllb!isExist.json?yhm="+yhm[i]);--%>
			<%--if( num > 0 ) { CFG_message("用户名重复！","warn");	return false;}--%>
		<%--}--%>
		return true;
	}
	function viewImage(fileInput) {
		if (window.FileReader) {
			var p = $("#viewImage");
			p.empty();/*清除之前新增的数据  */
			var file = fileInput.files;
			for (var i = 0, f; f = file[i]; i++) {

				var ImgSuffix = f.name.substring(f.name.lastIndexOf('.')+1);

				//图片不能超过2M，
				if(!(f.size<2097152 && (ImgSuffix=='jpg'||ImgSuffix=='bmp'||ImgSuffix=='png'))){
					CFG_message("图片必须小于2M，且格式必须为.png, .jpg 或 .bmp！", "error");
					$("#tp${idSuffix}").val("");
					return false;
				}
				var fileReader = new FileReader();
				fileReader.onload = (function (file) {
					return function (e) {
                       /*  $("#viewImageTp img").remove();
						var span = document.createElement('span');
						span.innerHTML = '<img height="100px" style = "position:relative;margin:10px 5px 0px 0px" src = "' + this.result + '" alt = "' + file.name + '" />';
						p.append(span); */
						uploadImage(this.result);
                          return;
					}
				})(f);
				fileReader.readAsDataURL(f);
			}
		}
	}
	function uploadImage(tplj) {
        var view = $("#mdtpFile${idSuffix}");
        view.empty();
        var image = new Image();
        image.src = tplj;
        image.className = "thumb";
        image.onload = function (e) {
            var div = document.createElement('div');
            $(div).append(this);
            $(div).append('<div style="display: none;position:absolute;top:5px; margin-top: 0px; margin-left: 6px; width: 20px; height: 20px;"><img onclick = "deleteImageTp(\'' + this.src + '\')" style = "height:20px" src = "<%=resourceFolder%>/css/images/trace-image/trash_bin.png"></div>');
            var originWidth = this.width;
            var originHeight = this.height;
            div.style.float = "left";
//            div.style.marginTop = "10px";
            div.style.marginRight = "5px";
            div.style.height = "100px";
            div.style.position="relative";
            if(((originWidth * 100) / originHeight) > 750){
                div.style.width = "750px";
            }else{
                div.style.width = (originWidth * 100) / originHeight + "px";
            }
            image.style.height = div.style.height;
            image.style.width = div.style.width;
            this.style.border = "1px solid #2AC79F";
            $(div).hover(function () {
                $(div).find("div").toggle();
            }); 
            view.append(div);
            if(view.css("width") != "0px"){
                $("#chooseImage${idSuffix}").css("margin-top","75px");
            }
        }
    }


	function showImg(imgSrc){
		var jqUC = $("<div style='text-align: center'></div>").appendTo(document.body);
		jqUC.dialog({
			modal : true,
			title : "查看图片",
			width : 540,
			height : 420,
			resizable : false,
			position : {
				at: "center"
			},
			onClose : function() {
				jqUC.dialog("close");
			},
			onCreate : function (){
				var html = "<img src='"+ $.contextPath +"/spzstpfj/"+imgSrc+"' width='300px' />";
				$(html).appendTo(this);
			}
		});
	}
	function readOnlySet(){
		var $grid = $("#gridId${idSuffix}");
		var $ids = $grid.grid("getDataIDs");
		for( var i = 0 ; i < $ids.length ; i ++){
			var $yhm = $grid.grid("getCellComponent",$ids[i],"YHM");
			var $mm = $grid.grid("getCellComponent",$ids[i],"MM");
			$yhm.textbox("option", "readonly", true);
			$mm.textbox("option", "readonly", true);
		}
	}
</script>
