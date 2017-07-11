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
	return $.contextPath + "/appmanage/show-module";
};
	

/**
 * 二次开发：复写自定义表单
 */
function _override_form (ui) {
	if(ui.options.number == 1){
		ui._init = function(){
			//加载调出商名称下拉列表
			var jqjyz = ui.getItemJQ("LSSMC");
			//var jyzData = $.loadJson($.contextPath + "/lsjyzda!getPfsGrid.json");
			var jsonData=$.loadJson($.contextPath+"/jyzxx!getJyzxx.json?xtlx=LS&zt=1");
			jqjyz.combogrid("reload",jsonData.data);
			//加载进场编号下拉列表
			var jqjcbh = ui.getItemJQ("SCJCBH");
			var jcbhData = $.loadJson($.contextPath + "/lsscjcxx!getJcbh.json");
			jqjcbh.combogrid("reload",jcbhData);
			//加载调入商名称下拉列表
			var drs = ui.getItemJQ("LSSMC2");
			//var drsData = $.loadJson($.contextPath + "/lsjyzda!getPfsGrid.json");
			drs.combogrid("reload",jsonData.data);
			//加载表单数据
			var lsscbm = ui.getItemJQ("LSSCBM");
			if(lsscbm.textbox("getValue")==null||lsscbm.textbox("getValue")==''){
				jQuery.ajax({
					type:'post',
					url:$.contextPath +'/lssctbxz!initFormData.json',
					dataType:'json',
					success:function(data){
						lsscbm.textbox("setValue",data.lsscbm);
					},
					error:function(data){
					}
				});
			}
		}
		/**
		 * 设置经营者1列表过滤条件
		 */
		ui.addOutputValue("setJyzColumns",function(o){
            var lssbmJQ = ui.getItemJQ("LSSBM2");
           // if(isEmpty(lssbmJQ.val())){
				return {
					status : true,
					P_columns :null
				}
//			}
//			var o = {
//				status : true,
//				P_columns :"EQ_C_B_ZT≡1;EQ_C_B_XTLX≡LS;NOT_C_B_JYZBM≡"+lssbmJQ.val()
//			}
//			return o;
		});

		
		/**
		 * 设置调入商列表过滤条件
		 */
		ui.addOutputValue("setJyz2Columns",function(o){
		    var lssbmJQ = ui.getItemJQ("LSSBM");
            if(isEmpty(lssbmJQ.val())){
               return {
				   status : true,
				   P_columns :null
			   }
            }
			var o = {
				status : true,
				P_columns :"EQ_C_B_ZT≡1;EQ_C_B_XTLX≡LS;NOT_C_B_JYZBM≡"+lssbmJQ.val()
			}
			return o;
		});
		
		/**
		 * 设置蔬菜进场编号列表过滤条件
		 */
		ui.addOutputValue("setScjcbhColumns",function(o){
			var dcsbm = $("#LSSBM_"+ui.timestamp).textbox("getValue");
			var o = {
				status : true,
				P_columns : "EQ_C_PFSBM≡"+dcsbm           
			}
			return o;
		});
		/**
		 * 获取调出商信息
		 */
		ui.addCallback("getDcs",function(data) {
			if(data==null) return;
			var dcsInfo = data.dcsInfo;
			if(dcsInfo==null) return;
			//填充form表单
			$(ui.uiForm).form();
			var dcsData = {"LSSMC":dcsInfo.JYZMC,"LSSBM":dcsInfo.JYZBM};
			ui.uiForm.form("loadData",dcsData);
			var jqJcbh = $("#SCJCBH_" + ui.timestamp);
			var jsonData = $.loadJson($.contextPath + "/lsscjcxx!getJcbhByPfsbm.json?pfsbm=" +dcsInfo.JYZBM);
			jqJcbh.combogrid("reload",jsonData);
		});
		
		/**
		 * 获取调入商信息
		 */
		ui.addCallback("getDrs",function(data) {
			if(data==null) return;
			var drsInfo = data.drsInfo;
			if(drsInfo==null) return;
			//填充form表单
			$(ui.uiForm).form();
			var drsData = {"LSSMC2":drsInfo.JYZMC,"LSSBM2":drsInfo.JYZBM};
			ui.uiForm.form("loadData",drsData);
		});
		
		/**
		 * 获取蔬菜进场编号
		 */
		ui.addCallback("getScjcbh",function(data) {
			if(data==null) return;
			var scjcbh = data.scjcbh;
			if(scjcbh==null) return;
			//填充form表单
			$(ui.uiForm).form();
			var jcbhData = {"SCJCBH":scjcbh.SCJCBH};
			ui.uiForm.form("loadData",jcbhData);
			var detailFrom = ui.getDetailForm();
			var jqSpmc = $("#SPMC_" + detailFrom.timestamp);//商品名称
			var jsonData = $.loadJson($.contextPath + "/lsjcxxxz!getSpmcByJcbh.json?jcbh=" +scjcbh.SCJCBH);
			jqSpmc.combogrid("reload",jsonData);
		});
		//设置调出商下拉列表
		ui.addCallback("setComboGridValue_Dcs",function(o){
			if( null == o) return ;
			var rowData = o.result;
			if( null == rowData) return;
			ui.setFormData({LSSMC:rowData.A_JYZMC,LSSBM:rowData.B_JYZBM});
			var jqDrs = ui.getItemJQ("LSSMC2");
			var jsonData=$.loadJson($.contextPath+"/jyzxx!getJyzxx.json?xtlx=LS&zt=1&jyzbm="+rowData.B_JYZBM);
			jqDrs.combogrid("reload",jsonData.data);
		});
		//设置调入商下拉列表
		ui.addCallback("setComboGridValue_Drs",function(o){
			if( null == o) return ;
			var rowData = o.result;
			if( null == rowData) return;
			ui.setFormData({LSSMC2:rowData.A_JYZMC,LSSBM2:rowData.B_JYZBM});
		});
		ui.bindEvent = function(){//事件方法
			var jqDcs = ui.getItemJQ("LSSMC"), //调出商名称
				jqJcbh = ui.getItemJQ("SCJCBH"),//进场理货编号
			 	jqDrs = ui.getItemJQ("LSSMC2");//调入商名称
			jqDcs.combogrid("option","onChange",function(e,data){//调出商名称下拉框事件
				var newValue = data.newValue;
				var newText = data.newText;
				var jsonData = $.loadJson($.contextPath + "/lsscjcxx!getJcbhByPfsbm.json?pfsbm=" +newValue);
				jqJcbh.combogrid("reload",jsonData);
				ui.setFormData({LSSMC:newText,LSSBM:newValue});
				ui.setFormData({LSSBM2:"",LSSMC2:"",SCJCBH:""});
				//重新加载调入商名称下拉列表
				var jsonData=$.loadJson($.contextPath+"/jyzxx!getJyzxx.json?xtlx=LS&zt=1&jyzbm="+newValue);
				//var drsData = $.loadJson($.contextPath + "/lsjyzda!getPfsGrid.json?gljyzbm="+newValue);
				jqDrs.combogrid("reload",jsonData.data);
			})
			jqJcbh.combogrid("option","onChange",function(e,data){//进场理货编号下拉框事件
				var detailFrom = ui.getDetailForm();
				var jqSpmc = $("#SPMC_" + detailFrom.timestamp);//商品名称
				var newValue = data.newValue;
				var newText = data.newText;
				var jsonData = $.loadJson($.contextPath + "/lsjcxxxz!getSpmcByJcbh.json?jcbh=" +newText);
				jqSpmc.combogrid("reload",jsonData);
				ui.setFormData({SCJCBH:newText});
			})
			jqDrs.combogrid("option","onChange",function(e,data){
				var lssmc = data.newText;
				var lssbm = data.newValue;
				ui.setFormData({LSSBM2:lssbm,LSSMC2:lssmc});
			})
		}
		ui.clickSecondDev = function(id){
			if(id==$.config.preButtonId+"dk"){
				/**
				 * 从卡内读取经营者信息
				 */
			 	if (isSwt) {
					var userInfo = _window("readUserInfo");
					var userData = JSON.parse(userInfo);
					if(userData.status == false || userData.status == "false"){
						$.message( {message:userData.msg, cls:"warning"});
						return;
					}
					var userValue = userData.value;
					
					if(userData.status == "true" || userData.status == true){
						//判断卡类型是否为菜卡
						if(user.cardType != 2 || user.cardType != '2'){
							$.message( {message:"本卡不是菜卡，请选择菜卡进行操作！", cls:"info"});
							return;
						}
						//var pfsmc = $("#LSSMC_"+ ui.timestamp);
						//pfsmc.combobox("setValue",userValue.userId);
						ui.setFormData({LSSBM:userValue.userId,LSSMC:userValue.userName});
					}
					
				} else {
					$.alert("请在程序中操作");
				}
			 }else if(id==$.config.preButtonId+"xc"){
			 	if (isSwt) {
			 		var dGrid = this.getDetailGrid();
					if (!dGrid) return;
					var jqForm  = $("form", this.uiForm),
					    rowData = dGrid.toFormData(), 
					    formData, url;
					// 表单检验
					if (!jqForm.form("valid")) return false;
					// 检验
					if (!rowData.length) {
						CFG_message("请先添加明细列表数据再保存！", "warning");
						return false;
					}
					// 获取表单数据
				    formData = jqForm.form("formData", false);
				    // 向列表添加数据
				    // 重置表单数据
				    url = this.getAction() + "!saveAll.json?P_tableId=" + this.options.tableId + "&P_D_tableIds=" + dGrid.options.tableId;
					// console.log("master: " + $.config.toString(formData));
					// console.log("detail: " + $.config.toString(rowData));
				    //写卡
					var length = rowData.length;
			    	var array = new Array();
			    	for(var i = 0;i < length;i++){
			    		var data = {
		    				productCode : rowData[i].SPBM,
	    					dealWeight : rowData[i].TBZL,
	    					tracecode : rowData[i].JYPZH,
	    					batchCode : rowData[i].JHPCH,
	    					sellerCode : ui.getItemJQ("PFSBM").text("getValue"),
	    					sellerName : ui.getItemJQ("PFSMC").text("getValue"),
	    					businessMarketCode : ui.getItemJQ("LSSCBM").text("getValue"),
	    					businessMarketName : ui.getItemJQ("LSSCMC").text("getValue")
	   					};
			    		array.push(data);
			    	}
			    	var date = formData.SCTBRQ;
			    	var bussinessInfo = {
		    			bDate:date.replace('-','').replace('-','').replace('-',''),
		    			bCount:String(length),
		    			bType:"?"
			    	};
			    	//var result = _window("writeBusinessDetailList",JSON.stringify(array));
			    	var result = _window("writeBusinessInfoAndDetail","2",JSON.stringify(bussinessInfo),JSON.stringify(array));
					var resultData = JSON.parse(result);
					if (resultData.status == "true" || resultData.status == true) {
						$.alert("写卡成功！：" + resultData.msg);
					} else {
						$.alert("写卡失败：" + resultData.msg);
					}
					//保存数据
				    $.ajax({
				    	url : url,
						type : "post",
						data : {E_entityJson: $.config.toString(formData),
							    E_dEntitiesJson: $.config.toString(rowData)},
						dataType : "json",
						success : function(rlt) {
							if (rlt.success) {
								jqForm.form("loadData", rlt.data.master);
								dGrid.clearGridData();
								dGrid.addRowData(rlt.data.detail);
								CFG_message("保存成功！", "success");
							} else {
								CFG_message(rlt.message, "warning");
							}
						},
						error : function() {
							CFG_message("保存主从表数据失败！", "error");
						}
				    });
				} else {
					$.alert("请在程序中操作");
				}
			 }
		}
	}else if(ui.options.number==2){
		ui._init = function(){
			//加载商品名称下拉列表
			var jqspmc = ui.getItemJQ("SPMC");
			var spmcData = $.loadJson($.contextPath + "/cpfl!getCpflGrid.json");
			jqspmc.combogrid("reload",spmcData);
		}
		ui.bindEvent = function(){
			var jqspmc = ui.getItemJQ("SPMC");
			jqspmc.combogrid("option","onChange",function(e,data){
				var spmc = data.newText;
				var spbm = data.newValue;
				ui.setFormData({SPBM:spbm,SPMC:spmc});
			})
		}
		/**
		 * 设置进场商品列表过滤条件
		 */
		ui.addOutputValue("setJcspmc",function(o){
			var masterForm = ui.getMasterForm();
			var scjcbh = $("#SCJCBH_"+masterForm.timestamp).combogrid("getText");
			var jcId;
			jQuery.ajax({
				type:'post',
				url:$.contextPath +'/lssctbxz!getIdByScjcbh.json?scjcbh='+scjcbh,
				dataType:'json',
				async:false,
				success:function(data){
					jcId=data;
				},
				error:function(data){
					
				}
			});
			var o = {
				status : true,
				P_columns : "EQ_C_PID≡"+jcId     
			}
			return o;
		});
		
		/**
		 * 获取进场商品编号
		 */
		ui.addCallback("getJcspmc",function(data) {
			if(data==null) return;
			var jcspmc = data.jcspmc;
			if(jcspmc==null) return;
			//填充form表单
			$(ui.uiForm).form();
			var jcspmcData = {"SPBM":jcspmc.SPBM,"SPMC":jcspmc.SPMC};
			ui.uiForm.form("loadData",jcspmcData);
		});
		
		ui.clickAdd = function(){
			var masterForm = ui.getMasterForm();
			var masterFormData =  $('form',masterForm.uiForm).form('formData',false);
			var cGrid = this.getSelfGrid(), // 对应列表
			    jqForm = $("form", this.uiForm),
			    formData;
			// 表单检验
			if (!jqForm.form("valid")) return false;
			// 获取表单数据
		    formData = jqForm.form("formData", false);
		    //写入进货批次号
		    formData.JHPCH = masterFormData.JHPCH;
		    //写入交易凭证号
		    jQuery.ajax({
					type:'post',
					url:$.contextPath +'/lssctbxz!getJypzh.json',
					dataType:'json',
					async:false,
					success:function(data){
						formData.JYPZH = data.jypzh;
					},
					error:function(data){
					}
				});
		    // 向列表添加数据
			cGrid.addRowData(formData);
		    // 重置表单数据
			ui.clickCreate();
			//更新主表单总调拨重量
			var rowData = cGrid.getRowData(), zl,lhzzl=0;
		    for (var i = 0, len = rowData.length; i < len; i++) {
		    	zl = rowData[i].TBZL;
		    	if (!$.isNumeric(zl)) zl = 0;
		    	lhzzl += parseInt(zl, 10);
		    }
			masterForm.setFormData({ZTBZL: lhzzl});
		};
		//删除功能
		ui.clickSecondDev = function(id){
			var masterForm = ui.getMasterForm();
			var masterFormData =  $('form',masterForm.uiForm).form('formData',false);
			if(id==$.config.preButtonId+"del"){
				var cGrid = this.getSelfGrid();
				var rowIdArr = cGrid.getSelectedRowId();
				if (rowIdArr === null||0 === rowIdArr.length) {
					$.message({message: "请选择记录!", cls: "warning"});
					return;
				}
				for(var i = rowIdArr.length - 1; i > -1; i-- ){
					cGrid.uiGrid.grid("delRowData",rowIdArr[i]);
				}
				//更新主表单总调拨重量
				var rowData = cGrid.getRowData(), zl,lhzzl=0;
			    for (var i = 0, len = rowData.length; i < len; i++) {
			    	zl = rowData[i].TBZL;
			    	if (!$.isNumeric(zl)) zl = 0;
			    	lhzzl += parseInt(zl, 10);
			    }
				masterForm.setFormData({ZTBZL: lhzzl});
			}
		};
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
	ui.eSelectRow = function(e, data) {return null;};
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
	if(ui.options.number == 1){
		ui.processItems = function (data) {
			var btns = [];
			btns.push({
				id:$.config.preButtonId + "dk",
				label: "读卡",
				type : "button"
			});
			btns.push({
				id:$.config.preButtonId + "xc",
				label: "写码保存",
				type : "button"
			});
			for (var i = 0; i < data.length; i++) {
				btns.push(data[i]);
			}
			return btns;
		};
	}else if(ui.options.number ==2 ){
		ui.processItems = function (data) {
			var btns = [];
			for (var i = 0; i < data.length; i++) {
				btns.push(data[i]);
			}
			btns.push({
				id:$.config.preButtonId + "del",
				label: "删除",
				type : "button"
			});
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
	
	var startTime = new Date().getTime();
	
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
	
	console.log("over ride cost time: " + (new Date().getTime() - startTime));
};

	
	
	
	
})("${timestamp}");
</script>
