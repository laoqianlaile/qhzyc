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
	return $.contextPath + "/scjysh";
};

/**
 * 二次开发：复写自定义表单
 */
function _override_form (ui) {
	if(isEmpty(ui.options.dataId)){
        ui._init = function (){
            var obj = $.loadJson($.contextPath + "/scqyda!getQyda.json");
            ui.setFormData({QYBM:obj.QYBM,QYMC:obj.QYMC});
        }
    }else{
        ui._init = function (){
            var obj = $.loadJson($.contextPath + "/scqyda!getQyda.json");
            ui.setFormData({QYBM:obj.QYBM,QYMC:obj.QYMC});
            var spbhJQ = ui.getItemJQ("SPBH");
            var spbhValue = spbhJQ.combogrid("getValue");
            var spbhData = $.loadJson($.contextPath + "/scjysh!getUpdateSpbh.json?spbh="+spbhValue);
//          spbhData = spbhData.data;
            spbhJQ.combogrid("reload", spbhData.data);
        }
    }

    ui.beforeSave = function(){
        var formData = $("form" ,ui.uiForm).form("formData",false);
        var flag = $.loadJson( $.contextPath + "/scjysh!checkUserName.json?id="+formData.ID+"&dlm="+formData.DLM);
        if(flag == 1){
            $.alert("登录名重复");
            return false;
        }
        return true;
    }

    //经营品类回调函数
    ui.addCallback("setComboGridValue_jypl",function ( o ){
        if( null == o ) return;
        var array = o.rowData;
        if( null == array ) return ;
        //获得回调过来的数据 SPMC:经营品类商品名称
        getCallbackData(array , "SPMC")

    });
    //商铺信息回调函数
    ui.addCallback("setComboGridValue_spxx",function (o){
        if( null == o ) return;
        var array = o.rowData;
        if( null == array ) return ;
        getCallbackData(array , "SPBH")
    });
    ui.bindEvent = function (){
        var jypl = ui.getItemJQ("JYPL");
        var spbhJQ = ui.getItemJQ("SPBH");
        jypl.combogrid("option" , "onChange" , function ( e , data ){
            ui.setFormData({JYPL:data.newText,JYPLBM:data.newValue});
        });
    }

    ui.addOutputValue("setComboGridValue_spxx" ,function (o){
        var spbhJQ = ui.getItemJQ("SPBH");
        var spbhValue = spbhJQ.combogrid("getValue");
        return {
            status:true,
            P_columns: "(EQ_C_1≡1)OR(IN_C_SPBH≡"+spbhValue+")"
        }

    });

    function  getCallbackData( array ,index){
        //获得回调过来的数据
        var spData ="";
        var spbmData = "";
        var len = array.length;
        for(var i =0 ;i< len;i++){
            var obj= array[i];
            spData += obj[index];
            if("SPMC" === index) {
                spbmData += obj["SPBM"]
            }
            if(i<len-1){
                spData += ",";
                if(spbmData.length != 0){
                    spbmData += ",";
                }
            }
        }
        //商铺信息
        if("SPBH" === index){
            ui.setFormData({SPBH:spData});
        }
        //经营品类信息
        if("SPMC" === index){
            ui.setFormData({JYPL:spData,JYPLBM:spbmData});
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
