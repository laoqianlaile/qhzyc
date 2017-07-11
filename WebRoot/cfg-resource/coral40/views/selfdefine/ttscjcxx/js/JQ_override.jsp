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
	return $.contextPath + "/ttscjcxx";
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
};
/**
 *  二次开发：复写自定义列表
 */
function _override_grid (ui) {
	var barCode;
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
	
	ui._init = function(){
		//加载菜品名称
		var spmcData = $.loadJson($.contextPath + "/ttscjcxx!getAllSpmc.json");
		for(var i=0;i<spmcData.length;i++){
			spmcMap.put(spmcData[i][0],spmcData[i][1]);
		}
	}
	
	ui.clickSecondDev = function(id){
		if(id==$.config.preButtonId+"dk"){
			//从卡内读取供应商及进场信息
		 	if (isSwt) {
				var result = _window("readAllInfo");
				
					var res = JSON.parse(result);
					
					var user = res.user;
					var records = res.records;
					var business = res.businessInfo;
					if(res.status == "true" || res.status == true){
						//将进场日期处理成YYYY-MM-DD格式
						var jcrq = business.bDate;
						if(jcrq!=null&&jcrq!=''&&jcrq.length==8){
							jcrq = jcrq.substr(0,4)+'-'+jcrq.substr(4,2)+'-'+jcrq.substr(6,2);
						}
						//判断卡类型是否为菜卡
						if(user.cardType != 2 || user.cardType != '2'){
							$.message( {message:"本卡不是菜卡，请选择菜卡进行操作！", cls:"info"});
							return;
						}
						var ids = '';
						//读出并写入进场信息
						for(var i = 0; i<records.length;i++){
							var detailData = records[i];
							jQuery.ajax({
								type:'post',
								url:$.contextPath +'/ttscjcxx!add.json',
								data:{
									"JCRQ":jcrq,
									"GYSMC":detailData.sellerName,
									"GYSBM":detailData.sellerCode,
									"SPBM":detailData.productCode,
									"SPMC":spmcMap.get(detailData.productCode),
									"ZL":detailData.dealWeight,
									"DJ":detailData.dealPrice,
									"JHPCH":detailData.batchCode,
									"JYPZH":detailData.tracecode,
									"GHSCBM":detailData.businessMarketCode,
									"GHSCMC":detailData.businessMarketName
								},
								dataType:'json',
								async:false,
								success:function(data){
									if(i==0){
										ids = data;
									}else{
										ids += ","+data;
								}
							},
							error:function(data){
							}
						});
					}
					//清卡
					_window("clearBusinessInfoAndDetail");
					showDiv(ids);
				}else{
					$.message( {message:res.msg, cls:"warning"});
				}
			} else {
				$.alert("请在程序中操作");
			}
		 }
		if (id == $.config.preButtonId + "dm") {
			showDialog () ;
		}
	}

	/**
	 * 显示进场信息DIV
	 */
	function showDiv(ids) {
		var _options = {
			tableId: ui.options.tableId,
			menuId:ui.options.menuId,
			componentVersionId:ui.options.componentVersionId,
			toolbar:false,
			columns:"IN_C_ID≡"+ids,
			global: ui.options.global,
			isAlone:true
		};
		var jqGlobal = $(ui.options.global);
		var showDiv = $("<div id=\"showDialog\"></div>").appendTo(jqGlobal);
		showDiv.dialog({
			appendTo : jqGlobal,
			modal : true,
			autoOpen : true,
			title : "进场信息",
			maxWidth : 800,
			width : 800,
			height : 400,
			maxHeight : 400,
			resizable : false,
			position : {of:jqGlobal},
			onOpen : function() {
				//表单自适应
				$.coral.adjusted(showDiv);
				var jqShowGrid = $("<div  class=\"coral-adjusted\" style='height: 370px;'></div>").appendTo(showDiv);
				//加载表单数据
				new $.config.cgrid(_options, jqShowGrid);
			},
			onClose : function () {
				//清空ids
				ids = '';
				//销毁
				showDiv.remove();
			}
		});
		//重新加载列表数据
		ui.reload();
	}
	function showDialog(){
		var _this = ui;
		var jqGlobal = $(ui.options.global);
		var jqUC = $("<div id=\"jqUC\"></div>").appendTo(jqGlobal);
		jqUC.dialog({
			appendTo : jqGlobal,
			modal : true,
			title : "请输入条码",
			width : 300,
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
				jq.textbox("option","onKeyDown",function( e,data){
					if(e.keyCode =='13'){//添加回车事件
						$('#sure').trigger("click");
					}
				});
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
						} else {
							_this.close(jqUC);
						}
						$("#UNTREAD_OPINION_" + _this.uuid).remove();
					}
				});
			}
		});
	}
	function setFormData(barCode){
		var sysName = barCode.substr(0,2);
		if (sysName.toUpperCase() != "PC" && sysName.toUpperCase()!="ZZ") {
			CFG_message("请输入有效的条形码!", "warning");
			return;
		}
		var ids = $.loadJson($.contextPath + "/ttscjcxx!barCodeSave.json?barCode=" + barCode);
		if (ids.length == 0) {
			CFG_message("请输入有效的条形码!", "warning");
			return;
		}
		showDiv(ids);
	}
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
	ui.processItems = function (data) {
		var btns = [];
//		btns.push({
//			id:$.config.preButtonId + "dk",
//			label: "读卡",
//			type : "button"
//		});
		btns.push({
			id:$.config.preButtonId + "dm",
			label: "读码",
			type : "button"
		});
		for (var i = 0; i < data.length; i++) {
			btns.push(data[i]);
		}
		return btns;
	};
	
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
