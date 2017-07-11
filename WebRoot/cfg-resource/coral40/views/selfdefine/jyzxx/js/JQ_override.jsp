<%@page language="java" pageEncoding="UTF-8"%>
<script type="text/javascript">
/***************************************************!
 * @author qiucs 
 * @date   2014-7-15
 * 系统配置平台应用自定义二次开发JS模板 
 ***************************************************/
 
(function(subffix) {

/**
 * 二次开发定位自己controller
 * @returns {String}
 **/
window[CFG_actionName(subffix)] = function () {
	// this.assembleData 就是 configInfo
	return $.contextPath + "/jyzxx";
};
	

/**
 * 二次开发：复写自定义表单
 */
function _override_form (ui) {

//	var _jyzId = "";
	var _isNew = true;
	ui._init = function(){
	var date = new Date();
	var barqJQ = ui.getItemJQ("A_BARQ");
	var formatDate = date.getFullYear() + "-" + (date.getMonth() + 1) + "-" + date.getDate() + " " + date.getHours() + ":" + date.getMinutes() + ":" + date.getSeconds();
	barqJQ.datepicker("setValue",formatDate);
		if(isEmpty(ui.options.dataId)) {
			showDiv();
			
		}
	}

	function showDiv() {
		var jqGlobal = $(ui.options.global);
		var showDiv = $("<div id='showDialog'><div class ='toolbarsnav clearfix'><div id = 'toolbar'></div></div><div class = 'fillwidth colspan3 clearfix' style='margin-top:40px'><label class ='app-input-label' style='width:120px;text-align:right'>身份证号:</label><input id = 'idnoInput'></input></div></div>").appendTo(jqGlobal);
		showDiv.dialog({
			appendTo : jqGlobal,
			modal : true,
			autoOpen : true,
			title : "请输入身份证号",
			maxWidth : 400,
			width : 400,
			height : 200,
			maxHeight : 200,
			resizable : false,
			position : ['center','center'],
			onOpen : function() {
				$("#idnoInput").textbox({
					validType : "idno",	
					required : true
				});
				$("#idnoInput").textbox("option","onKeyDown",function( e,data){
					if(e.keyCode =='13'){//添加回车事件
                        checkIdNo();
					}
				});
				$("#idnoInput").focus();
				var toolBarBtn = [{
					"id"		:	"confirmBtn",
					"label"		:	"确认",
					"disabled"	:	"false",
					"onClick"	:	checkIdNo,
					"type"		:	"button",
					"cls"		:	"greenbtn"
					}];	
				$("#toolbar").toolbar({
					data:toolBarBtn
				});
                function checkIdNo(){
                    var isValide = $("#idnoInput").textbox("valid");
                    if(isValide!=true){
                        return;
                    }
                    var idno = $("#idnoInput").textbox("getValue");
                    var jsonData = $.loadJson($.contextPath+'/jyzxx!validateIdno.json?idno='+idno);
                    var companyInfo = $.loadJson($.contextPath+'/jyzxx!getCompanyInfo.json?menuId='+ui.options.menuId);
                    var jyzbm = $.loadJson($.contextPath+'/jyzxx!getJyzbm.json');
                    if(jsonData == "NONE"){

                        ui.setFormData({
                            A_GSZCDJZHHSFZH:idno,
                            A_JYZBM:jyzbm,
                            B_QYBM:companyInfo.QYBM,
                            B_XTLX:companyInfo.sysName,
                            B_RECORD:1
                        });
                        _isNew = true;
                    }else{
                        var isInCurrentQy = $.loadJson($.contextPath + '/jyzxx!isInCurrentQy.json?idno='+idno);//检验是否已存在于当前企业
                        if (isInCurrentQy == "YES") {
                            CFG_message("该身份证已经存在","info");
                            return;
                        }
                        ui.setFormData({
                            A_GSZCDJZHHSFZH:jsonData.GSZCDJZHHSFZH,
                            A_JYZMC:jsonData.JYZMC,
                            A_JYZXZ:jsonData.JYZXZ,
                            A_FRDB:jsonData.FRDB,
                            A_BARQ:jsonData.BARQ,
                            A_JYZBM:jsonData.JYZBM,
                            B_QYBM:companyInfo.QYBM,
                            B_XTLX:companyInfo.sysName,
                            B_RECORD:0
                        });
//							_jyzId = jsonData.ID;
                        _isNew = false;
                    }

                    showDiv.remove();
				}

				
			},
			onClose : function () {
				ui.clickBackToGrid();
				showDiv.remove();
			}
		});
	}

	ui.clickSave = function(op){
		var	jqForm = $("form", ui.uiForm),
		formData;
		// 表单检验
		if (!jqForm.form("valid")) return false;
		// 获取表单数据
	    formData = jqForm.form("formData", false);
	    url = this.getAction() + "!save.json?P_tableId=" + ui.options.tableId + "&isNew=" + _isNew;
	    $.ajax({//保存数据库成功后写码
			url : url,
			type : "post",
			data : {E_entityJson: $.config.toString(formData)},
			dataType : "json",
			success : function(rlt) {
				 if (rlt!=null && rlt!="") {
					jqForm.form("loadData", rlt);

					CFG_message("保存成功！", "success");
				} else {
					CFG_message("保存失败", "warning");
					return;
				}
			},
			error : function() {
				CFG_message("保存主从表数据失败！", "error");
				return;
			}
		});//
	}
	

}
/**
 *  二次开发：复写自定义列表
 */
function _override_grid (ui) {
	// ui.assembleData 就是 configInfo
	//console.log("override grid!");
	//ui.getAction = function () {
	//	return $.contextPath + "/appmanage/show-module";
	//};
	//重写修改方法
	//ui.clickUpdate = function(){
		//alert(0);
	//	ui._init = function() {ui.setFormData({FRDB:"111"});
	//}
};
/**
 *  二次开发：复写自定义树
 */
function _override_tree (ui) {
	// ui.assembleData 就是 configInfo
	//console.log("override tree!");
	//ui.getAction = function () {
	//	return $.contextPath + "/appmanage/show-module";
	//};
};
/**
 *  二次开发：复写自定义工具条
 */
function _override_tbar (ui) {
	

};

/**
 * 在此可以复写所有自定义JS类
 * @param selector
 * @returns {JQ_override}
 */
window[CFG_overrideName(subffix)] = function () {
	if (this instanceof $.config.cform) {
		_override_form(this);
	} else if (this instanceof $.config.cgrid) {
		_override_grid(this);
	} else if (this instanceof $.config.ctree) {
		_override_tree(this);
	} else if (this instanceof $.config.ctbar) {
		_override_tbar(this);
	}
};

	
	
	
	
})("${timestamp}");
</script>
