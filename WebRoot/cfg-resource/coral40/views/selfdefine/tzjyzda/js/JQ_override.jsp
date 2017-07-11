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
	return $.contextPath + "/tzjyzda";
};


/**
 * 二次开发：复写自定义表单
 */
function _override_form (ui) {
	if(1 == ui.options.number){
		ui.clickSecondDev = function (id) {
			if (id == $.config.preButtonId + "dk") {//读卡按钮
				if (isSwt) {
					var result = _window("readCard", "userInfo");
					var resultData = JSON.parse(result);
					var value = resultData.value;
					if (resultData.status == "true" || resultData.status == true) {
						//alert("value.cardId卡号:="+value.cardId+";<br/>value.cardType卡类型:="+value.cardType+"<br/>;value.userId经营者编码:="+value.userId+";<br/>value.userName(经营者):="+value.userName+";<br/>value.userType(经营者性质):="+value.userType+";<br/>userCertificate(证件号):="+value.userCertificate+";userPhone手机号:="+value.userPhone);
						ui.setFormData({
								JYZMC:value.userName,
								JYZBM:value.userId,
								GSZCDJZHHSFZH:value.userCertificate,
								JYZXZ:value.userType,
								FRDB:value.other,
								//BAPFSCBM:"",
								//BAPFSCMC:"" 
								});
						ui.setFormData(formData);
						$.message( {message:"读卡成功", cls:"info"});
					} else {
						$.message( {message:"读卡失败，请重试", cls:"warning"});
					}
				} else {
					$.alert("请在程序中操作");
				}
			}
		};
	}

	ui.bindEvent = function(){
        //商品到达地下拉列表onChange事件
		var spdddJQ = ui.getItemJQ("SPDDD");
		spdddJQ.combogrid("option","onChange",function(e,data){
			var newText = data.newText;
			ui.setFormData({SPDDD:newText});
		});
	}

    //商品到达地弹出框回调事件
    ui.addCallback("setComboGridValue_ddd",function(o){
        if(null == o) return;
        var obj = o.rowData;
        if(null == obj) return;
        ui.setFormData({SPDDD:obj.CDMC});
    });
	
	ui._init = function (){
		if(isEmpty(ui.options.dataId)){
			var datas=$.loadJson($.contextPath +'/tzqyda!getQyda.json?prefix=TZ');
			ui.setFormData({
				BALTJDMC:datas.tzcmc,
				BALTJDBM:datas.tzcbm
			});
			$.ajax({
				type:'post',
				url:$.contextPath + "/tzjyzda!getJyzbm.json",
				data:{Zl:"JYZBM"},
				dataType:'json',
				success:function(data){
					ui.setFormData({JYZBM:data});
				}
			})
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
	if(isEmpty(ui.options.model)){
		if(ui.options.number == 1){
			ui.processItems = function (data) {
				var btns = [];
				btns.push({
					id:$.config.preButtonId + "dk",
					label: "读卡",
					type : "button"
				});
				for (var i = 0; i < data.length; i++) {
					btns.push(data[i]);
				}
				return btns;
			};
		}
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
