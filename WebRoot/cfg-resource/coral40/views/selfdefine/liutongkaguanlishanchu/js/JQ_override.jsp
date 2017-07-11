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
	return $.contextPath + "/liutongkaguanlishanchu";
//	return $.contextPath + "/appmanage/show-module";
};
	

/**
 * 二次开发：复写自定义表单
 */
function _override_form (ui) {
	/**
	 * 二次开发第一区域表单功能
	 */
	if (ui.options.number === 1) {
		/**
		 * 绑定自定按钮的事件
		 */
		ui.clickSecondDev = function (id) {
			/**
			 * 从卡内读取用户卡信息在表单上显示
			 */
			if (id == $.config.preButtonId + "readUserInfo") {
				if (isSwt) {
					var result = _window("readCard", "userInfo");
					var resultData = JSON.parse(result);
					var value = resultData.value;
					if (resultData.status == "true" || resultData.status == true) {
						ui.setFormData({
							A_LTKH : value.cardId,
							A_LTKLX : value.cardType,
							A_JYZBM : value.userId,
							B_JYZMC : value.userName,
							B_JYZXZ : value.userType,
							B_GSZCDJZHHSFZH : value.userCertificate,
							B_SJHM : value.userPhone,
							B_FRDB : value.other
						});
						$.message( {message:"读卡成功", cls:"info"});
					} else {
						$.message( {message:"读卡失败，请重试", cls:"warning"});
					}
				} else {
					$.alert("请在程序中操作");
				}
			}
			/**
			 * 清空卡片（回收卡片）并清空表单数据
			 */
			if (id == $.config.preButtonId + "clearCard") {
				if (isSwt) {
					var result = _window("clearCard");
					var resultData = JSON.parse(result);
					var value = resultData.value;
					if (resultData.status == "true" || resultData.status == true) {
						ui.setFormData({
							A_LTKH : "",
							A_LTKLX : "",
							A_JYZBM : "",
							B_JYZMC : "",
							B_JYZXZ : "",
							B_GSZCDJZHHSFZH : "",
							B_SJHM : "",
							B_FRDB : ""
						});
						$.message( {message:"回收卡成功", cls:"info"});
					} else {
						$.message( {message:"回收卡失败，请重试", cls:"warning"});
					}
				} else {
					$.alert("请在程序中操作");
				}
			}
		};
		/*
		ui.addOutputValue("cardDelView",function(o) {//回收界面查看卡信息
			var o = {
				status : false,
				operate : "cardDelView"
			}
			if (isSwt) {
				var result = _window("readCard", "userInfo");
				var resultData = JSON.parse(result);
				if (resultData.status == "true" || resultData.status == true) {
					o.value = resultData.value.cardNo;
					o.status = true;
				} else {
					$.alert("读卡失败：" + resultData.msg);
				}
			} else {
				$.alert("请在程序环境中操作");
			}
			return o;
		});
		ui.addOutputValue("cardDel",function(o) {//回收卡
			var o = {
				status : false,
				operate : "cardDel"
			}
			if (isSwt) {
				var result = _window("readCard", "userInfo");
				var resultData = JSON.parse(result);
				if (resultData.status == "true" || resultData.status == true) {
					var cardNo = resultData.value.cardNo;
					result = _window("clearCard");
					resultData = JSON.parse(result);
					if (resultData.status == "true" || resultData.status == true) {
						o.status = true;
						o.value = cardNo;
					} else {
						$.alert("回收卡失败：" + resultData.msg);
					}
				} else {
					$.alert("回收卡失败请重试");
				}
			} else {
				$.alert("请在程序环境中写卡");
			}
			return o;
		});
		ui.addCallback("getResultDel",function(res) {
			var operate = res.operate;
			var result = res.result;
			var value = res.value;
			if (result == true || result == "true") {
				if (operate == "cardDelView") {
					ui.setFormData({
						A_LTKH : value.cardId,
						A_LTKLX : value.cardType,
						A_JYZBM : value.userId,
						A_JYZMC : value.userName,
						A_JYZXZ : value.userType,
						B_GSZCDJZHHSFZH : value.userCertificate,
						B_SJHM : value.userPhone,
						B_FRDB : value.userArtificialPerson
					});
				} else if (operate == "cardDel") {
					ui.setFormData({
						A_LTKH : "",
						A_LTKLX : "",
						A_JYZBM : "",
						A_JYZMC : "",
						A_JYZXZ : "",
						B_GSZCDJZHHSFZH : "",
						B_SJHM : "",
						B_FRDB : ""
					});
				}
			} else {

			}
		});
		*/
	}
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
	/**
	 * 添加自定义按钮
	 * data为原来的按钮
	 */
	if (1 === ui.options.number) {
		ui.processItems = function (data) {
			var btns = [];
			btns.push({
				id:$.config.preButtonId + "readUserInfo",
				label: "读卡",
				type : "button"
			});
			btns.push({
				id:$.config.preButtonId + "clearCard",
				label: "回收",
				type : "button"
			});
			for (var i = 0; i < data.length; i++) {
				btns.push(data[i]);
			}
			return btns;
		};
	}
	// ui.assembleData 就是 configInfo
	//console.log("override tbar!");
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
