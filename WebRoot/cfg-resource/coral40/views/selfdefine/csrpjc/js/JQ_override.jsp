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
window[CFG_actionName(subffix)] = function (ui) {
	// ui.assembleData 就是 configInfo
	return $.contextPath + "/csrpjc";
};
	

/**
 * 二次开发：复写自定义表单
 */
function _override_form (ui) {
	// ui.assembleData 就是 configInfo
	//console.log("override grid!");
	//ui.getAction = function () { 
	//	return $.contextPath + "/appmanage/show-module";
	//};
	var barCode = "";
	function Map() {
		var struct = function(key, value) { 
					this.key = key; 
					this.value = value;
		}  
   
 		var put = function(key, value){  
  				for (var i = 0; i < this.arr.length; i++) {
  					if ( this.arr[i].key === key ) {
  						this.arr[i].value = value; return;
  					}
  				}  
   				this.arr[this.arr.length] = new struct(key, value);
   		}  
   
 		var get = function(key) {  
  				for (var i = 0; i < this.arr.length; i++) {  
   					if ( this.arr[i].key === key ) { 
   						return this.arr[i].value; 
   					}  
  				}  
  				return null;  
 		}
 		this.arr = new Array();  
		this.get = get;  
		this.put = put;  
	}
	var spmcMap = new Map();
	
	/**
	 * 设置经营者列表过滤条件
	 */
	ui.addOutputValue("setJyzColumns",function(o){
		var o = {
			status : true,
			P_columns : ""
		}
		return o;
	});
	/**
	 * 获取选择的经营者信息
	 */
	ui.addCallback("getLssInfo",function(data) {
		if(data==null) return;
		var jyzInfo = data.jyzInfo;
		if(jyzInfo==null) return;
		//填充form表单
		$(ui.uiForm).form();
		var jyzData = {"GYSMC":jyzInfo.A_JYZMC,"GYSBM":jyzInfo.A_JYZBM};
		ui.uiForm.form("loadData",jyzData);
		
	});
	//设置经营者下拉列表
	ui.addCallback("setComboGridValue_Jyzmc",function(o){
		if( null == o ) return ;
		var rowData = o.result;
		if( null == rowData )  return ;
		ui.setFormData({GYSMC:rowData.A_JYZMC,GYSBM:rowData.A_JYZBM});
	});
	/**
	 * 获取弹出框商品信息
	 */
	ui.addCallback("getRpxx",function(data) {
		if(data==null) return;
		var spxx = data.rpxx;
		if(spxx==null) return;
		//填充form表单
		$(ui.uiForm).form();
		var spxxData = {"SPMC":spxx.SPMC,"SPBM":spxx.SPBM};
		ui.uiForm.form("loadData",spxxData);
	});
	
	//设置商品名称下拉列表
	ui.addCallback("setComboGridValue_Spmc",function(o){
		if( null == o ) return ;
		var rowData = o.result;
		if( null == rowData )  return ;
		ui.setFormData({CDMC:rowData.CDMC});
	});
	
	//初始化表单数据
	ui._init = function(){
		var csbm = ui.getItemJQ("CSBM");
		var csmc = ui.getItemJQ("CSMC");
		var jhpch = ui.getItemJQ("JHPCH");
		var lspzh = ui.getItemJQ("LSPZH");
		if(jhpch.textbox("getValue")==null||jhpch.textbox("getValue")==''){
				jQuery.ajax({
					type:'post',
					url:$.contextPath +'/csrpjc!initFormData.json',
					dataType:'json',
					async:false,
					success:function(data){
						jhpch.textbox("setValue",data.jhpch);
						lspzh.textbox("setValue",data.lspzh);
						csbm.textbox("setValue",data.lsscbm);
						csmc.textbox("setValue",data.lsscmc);						
					},
					error:function(data){
					}
				});
			}
				
		
		//加载批发商名称下拉列表
		var jqjyz = ui.getItemJQ("GYSMC");
		var jyzData = $.loadJson($.contextPath +"/jyzxx!getJyzxx.json?xtlx=CS&zt=1");
		jqjyz.combogrid("reload",jyzData.data);				
		//初始化时设置供应商名称
		if(ui.options.dataId != null){
		var gysData  =$.loadJson($.contextPath+"/prrpjcxx!getGysData.json?id="+ui.options.dataId);		
		jqjyz.combogrid("setValue",gysData.GYSMC);
		}
		
		
		//加载肉品名称
		var spmc = ui.getItemJQ("SPMC");
		var spmcData = $.loadJson($.contextPath + "/csrpjc!getAllRpmc.json");
		for(var i=0;i<spmcData.length;i++){
			spmcMap.put(spmcData[i][0],spmcData[i][1]);
		}
		spmc.combogrid("reload",spmcData.data);
		if(!isEmpty(ui.options.dataId)){
		var gysData  =$.loadJson($.contextPath+"/prrpjcxx!getGysData.json?id="+ui.options.dataId);				
		spmc.combogrid("setValue",gysData.SPBM);		
		}
	}
	
	ui.bindEvent = function () {
		//批发商名称下拉列表
		var jqPfsmc = ui.getItemJQ("GYSMC");
		jqPfsmc.combogrid("option","onChange",function(e,data){
			var text = data.text;
			var value = data.value;
			ui.setFormData({GYSMC:text,GYSBM:value});
		});	
		
		var jqspmc = ui.getItemJQ("SPMC");
		jqspmc.combogrid("option","onChange",function(e,data){
			var spmc = data.text;
			var spbm = data.value;
			ui.setFormData({SPBM:spbm,SPMC:spmc});
		})
		
		var je = ui.getItemJQ("JE"),
		zl = ui.getItemJQ("ZL"),
		dj = ui.getItemJQ("DJ");
		zl.textbox("option", "onChange", function(e, data) {
			var djValue = dj.textbox("getValue"), 
				zlValue = data.value,
				jeValue;
			if (!$.isNumeric(djValue)) djValue = 0;
			if (!$.isNumeric(zlValue)) zlValue = 0;
			jeValue = parseFloat(djValue, 10) * parseFloat(zlValue, 10);
			je.textbox("setValue", Math.round(jeValue*100)/100);
		});
		dj.textbox("option", "onChange", function(e, data) {
			var zlValue = zl.textbox("getValue"), 
				djValue = data.value,
				jeValue;
			if (!$.isNumeric(djValue)) djValue = 0;
			if (!$.isNumeric(zlValue)) zlValue = 0;
			jeValue = parseFloat(djValue, 10) * parseFloat(zlValue, 10);
			je.textbox("setValue", Math.round(jeValue*100)/100);
		});
	}
	
	ui.clickSecondDev = function(id){
		if (id==$.config.preButtonId+"inputBarCode") {
			showDialog();
		}
		
		if(id==$.config.preButtonId + "dk"){
			/**
			 * 从卡内读取信息
			 */
		 	if (isSwt) {
				var result = _window("readAllInfo");
				var res = JSON.parse(result);
                if(res.status == false || res.status == "false"){
                    $.message( {message:res.msg, cls:"warning"});
                    return;
                }
				var user = res.user;
				var records = res.records;
				var business = res.businessInfo;
				//将进场日期处理成YYYY-MM-DD格式
				if(res.status == "true" || res.status == true){
					var jcrq = business.bDate;
					if(jcrq!=null&&jcrq!=''&&jcrq.length==8){
						jcrq = jcrq.substr(0,4)+'-'+jcrq.substr(4,2)+'-'+jcrq.substr(6,2);
					}
					//var pfsmc = $("#LSSMC_"+ ui.timestamp);
					//pfsmc.combobox("setValue",userValue.userId);
					//判断卡类型是否为菜卡
					if(user.cardType != 1 || user.cardType != '1'){
						$.message( {message:"本卡不是肉卡，请选择肉卡进行操作！", cls:"info"});
						return;
					}
					var detailData = records[0];
					ui.setFormData({GYSBM:user.userId,
									GYSMC:user.userName,
									//<!--JCRQ:jcrq,-->
									ZSPZH:detailData.tracecode,
									SPBM:detailData.productCode,
									SPMC:spmcMap.get(detailData.productCode),
									ZL:detailData.dealWeight,
									DJ:detailData.dealPrice,
									JE:parseFloat(detailData.dealWeight, 10) * parseFloat(detailData.dealPrice, 10),
//									CSBM:detailData.businessMarketCode,
//									CSMC:detailData.businessMarketName,
									JHPCH:detailData.batchCode,
									LSPZH:detailData.tracecode});
				}
				
			} else {
				$.alert("请在程序中操作");
			}
		 }
	}
	
	//保存
		ui.clickSave = function(op) {
			var _this = this, formUI = $("form", this.uiForm),
			    url = this.getAction() + "!save.json?P_tableId=" + this.options.tableId + "&P_op=" + op + "&P_menuCode=" + CFG_getInputParamValue(this.assembleData, "menuCode"),
			    formData;

			if (!formUI.form("valid")) return;
			// 保存前回调方法
			if (_this.processBeforeSave(formUI, op) === false) return;
			// 获取表单数据
		    formData = formUI.form("formData", false);
			$.ajax({
				type : "post",
				url : url,
				data : {E_entityJson: $.config.toString(formData),barCode:barCode},
				dataType : 'json',
				success : function(entity) {
					if (false === entity.success || !("ID" in entity)) {
						if (isEmpty(entity.message)) CFG_message("操作失败！", "warning");
						else CFG_message(entity.message, "warning");
						return;
					}
					// 保存后回调方法
					_this.processAfterSave(entity, op);
					CFG_message("操作成功！", "success");
					if ("save" === op) {
						formUI.form("loadData", entity);
					} else if ("close" === op) {
						if (!_this.isNested) _this.uiForm.dialog("close");
						else _this.clickBackToGrid();
					} else if ("create" === op) {
						_this.clickCreate();
					}
					//清卡
					if (isSwt) {
						var lspzhArray = new Array();
//						for(var i=0;i<rowData.length;i++){
//							var detail = rowData[i];
//							lspzhArray.push(detail.LSPZH);
//						}
//						_window("clearBusinessInfoAndDetail",lspzhArray);
						_window("clearBusinessInfoAndDetail");
					}
				},
				error : function() {
					CFG_message("操作失败！", "error");
				}
			});
		}

	function setPfsxx(jyzbm,jyzmc,barCode){//根据读码信息获取本市场内该经营者信息
		var csbm = ui.getItemJQ("CSBM").textbox("getValue");
		var getJyzId = $.loadJson($.contextPath+'/jyzxx!getJyzxxByBmAndQybm.json?jyzbm='+jyzbm+"&qybm="+csbm+"&barCode="+barCode);
		if("FATAL" == getJyzId.result){
			CFG_message("批发商信息错误，请录入！", "warning");
		}else if("SUCCESS" == getJyzId.result){
			ui.setFormData({
				GYSMC:jyzmc,
				GYSBM:jyzbm
			});
		} else {
			var id = getJyzId.ID;
			var createJyz = $.loadJson($.contextPath + '/jyzxx!createJyz.json?id='+id+"&qybm="+csbm+"&xltx=CS&barCode="+barCode);
			var jsonData=$.loadJson($.contextPath +"/jyzxx!getJyzxx.json?xtlx=CS&zt=1");
			var pfsmc = ui.getItemJQ("GYSMC");
			pfsmc.combogrid("reload",jsonData.data);
			ui.setFormData({
				GYSMC:jyzmc,
				GYSBM:jyzbm
			});
			CFG_message("已添加经营者:"+jyzmc+"！", "success");
		}
	}
	function showDialog(){
		var _this = ui;
		var jqGlobal = $(ui.options.global);
		var jqUC = $("<div id=\"jqUC\"></div>").appendTo(jqGlobal);
		jqUC.dialog({
			appendTo : jqGlobal,
			modal : true,
			title : "请输入条码",
			width : 240,
			height : 80,
			resizable : false,
			position : {
				at: "center top+200"
			},
			onClose : function() {
				jqUC.remove();
				jqUC.remove();
			},
			onCreate : function() {
				var jqDiv = $("<div class=\"app-inputdiv-full\" style=\"padding:10px 20px;\"></div>").appendTo(this);
				var jq = $("<input id=\"UNTREAD_OPINION_" + _this.uuid + "\" name=\"opinion\"></textarea>").appendTo(jqDiv);
				jq.textbox({width: 200});
			},
			onOpen : function() {
				_this.close(false);
				var jqPanel = $(this).dialog("buttonPanel").addClass("app-bottom-toolbar"),
						jqDiv   = $("<div class=\"dialog-toolbar\">").appendTo(jqPanel);
				jqDiv.toolbar({
					data: ["->", {id:"sure", label:"确定", type:"button"}, {id:"cancel", label:"取消", type:"button"}],
					onClick: function(e, ui) {
						if ("sure" === ui.id) {
							barCode=$("#UNTREAD_OPINION_" + _this.uuid).val();
							setFormData(barCode);
							_this.close(jqUC);
							$("#UNTREAD_OPINION_" + _this.uuid).remove();
						} else {
							_this.close(jqUC);
							$("#UNTREAD_OPINION_" + _this.uuid).remove();
						}
					}
				});
			}
		});
	}
	function setFormData(barCode){
		if(barCode.length != 22){
			CFG_message("请输入有效的条形码!", "warning");
			return;
		}
		var prefix = barCode.substring(0,2);
		var spmc = ui.getItemJQ("SPMC");
		if("TZ" === prefix){
			var jsonData = $.loadJson($.contextPath + '/csrpjc!getTzjyxx.json?tztmh='+barCode);
			if ("ERROR" === jsonData) {
				CFG_message("请输入有效的条形码!", "warning");
			}else{
				var gysmc = jsonData.MZMC;
				var gysbm = jsonData.MZBM;
				setPfsxx(gysbm,gysmc,barCode);
				//加载主表数据
				ui.setFormData({
					ZSPZH:jsonData.ZSM,
					SPMC:jsonData.SPMC,
					SPBM:jsonData.SPBM,//
					ZL:jsonData.ZL,
					DJ:jsonData.DJ,
					JE:jsonData.JE,
					JHPCH:jsonData.JCPCH,
					LSPZH:jsonData.ZSM
				});
				//spmc.combogrid("option","disabled",true);
			}
		}else if("PR" === prefix){
			var jsonData = $.loadJson($.contextPath + '/csrpjc!getPrjyxx.json?prtmh='+barCode);
			if("ERROR" === jsonData){
				CFG_message("请输入有效的条形码!", "warning");
			}else{
				var gysmc = jsonData.LSSMC;
				var gysbm = jsonData.LSSBM;
				setPfsxx(gysbm,gysmc,barCode);
				//加载主表数据
				ui.setFormData({
					ZSPZH:jsonData.ZSM,
					SPMC:jsonData.SPMC,
					SPBM:jsonData.SPBM,//
					ZL:jsonData.ZL,
					DJ:jsonData.DJ,
					JE:jsonData.JE,
					JHPCH:jsonData.JHPCH,
					LSPZH:jsonData.ZSM
				});
				//spmc.combogrid("option","disabled",true);
			}
		}else {
			CFG_message("请输入有效的条形码!", "warning");
			return;
		}
	}
};
/**
 *  二次开发：复写自定义列表
 */
function _override_grid (ui) {
	// ui.assembleData 就是 configInfo
	//console.log("override grid!");
	//ui.getAction = function () {
	//	return $.contextPath + "/appmanage/show-module";
	//};
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
	// ui.assembleData 就是 configInfo
	//console.log("override tbar!");
	//ui.getAction = function () {
	//	return $.contextPath + "/appmanage/show-module";
	//};
	if(ui.options.type == $.config.contentType.form){
		ui.processItems = function (data) {
			var btns = [];
			btns.push('->',{
				id:$.config.preButtonId + "inputBarCode",
				label: "输入条码",
				type : "button"
			});
//			btns.push({
//				id:$.config.preButtonId + "dk",
//				label: "读卡",
//				type : "button"
//			});
			for (var i = 2; i < data.length; i++) {
				btns.push(data[i]);
			}
			return btns;
		};
	}
};
/**
 *  二次开发：复写自定义布局
 */
function _override_layout (ui) {
	//console.log("override layout!");
	//ui.getAction = function () {
	//	return $.contextPath + "/appmanage/show-module";
	//};
};








/**
 * 在此可以复写所有自定义JS类
 * @param selector
 * @returns {JQ_override}
 */
window[CFG_overrideName(subffix)] = function () {
	
	//var startTime = new Date().getTime();
	
	if (this instanceof $.config.cform) {
		_override_form(this);
	} else if (this instanceof $.config.cgrid) {
		_override_grid(this);
	} else if (this instanceof $.config.ctree) {
		_override_tree(this);
	} else if (this instanceof $.config.ctbar) {
		_override_tbar(this);
	} else if (this instanceof $.config.clayout) {
		_override_layout(this);
	}
	
	//console.log("over ride cost time: " + (new Date().getTime() - startTime));
};

	
	
	
	
})("${timestamp}");
</script>
