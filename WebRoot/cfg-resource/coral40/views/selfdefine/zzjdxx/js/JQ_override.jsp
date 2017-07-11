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
	return $.contextPath + "/zzjdxx";
};
	

/**
 * 二次开发：复写自定义表单
 */
function _override_form (ui) {

    ui._init = function(){
        if(isEmpty(ui.options.dataId)){
            var jdbhJQ = ui.getItemJQ("JDBH");
            var jdbhData = $.loadJson($.contextPath + "/zzjdxx!getJdbhLsh.json");
            var qybmJQ = ui.getItemJQ("QYBM");
            var qybmData = $.loadJson($.contextPath + "/zzcpxxglxz!getQybm.json");
            var jdbh = qybmData + jdbhData;
            ui.setFormData({JDBH:jdbh,QYBM:qybmData});
        }
    }



    ui.bindEvent = function(){
            var ssdqJQ = ui.getItemJQ("SSDQ");//所属地区
            var fzrJQ = ui.getItemJQ("FZR");//负责人
            ssdqJQ.combogrid("option","onChange",function(e,data){
                var text = data.text;
                var value = data.value;
                ui.setFormData({SSDQ:text,SSDQBM:value});
            });
            fzrJQ.combogrid("option","onChange",function(e,data){
                var text = data.text;
                var value = data.value;
                ui.setFormData({FZR:text,FZRBH:value});
            });
    }
    //所属地区回调函数
	ui.addCallback("setComboGridValue_ssdq",function(o){
		if(null == o) return;
        var obj = o.rowData;
        if(null == obj) return;
        ui.setFormData({SSDQ:obj.CDMC,SSDQBM:obj.CDBM});
	});
    //负责人回调函数
    ui.addCallback("setComboGridValue_fzr",function(o){
        if(null == o) return;
        var obj = o.rowData;
        if(null == obj) return;
        ui.setFormData({FZR:obj.XM,FZRBH:obj.GZRYBH});
    });



    ui.clickSave = function(op) {
        var _this = ui, jqForm = $("form", ui.uiForm),
                url = ui.getAction() + "!save.json?P_tableId=" + ui.options.tableId + "&P_op=" + op
                        + "&P_componentVersionId=" + ui.options.componentVersionId
                        + "&P_menuId=" + ui.options.menuId
                        + "&P_menuCode=" + ui.getParamValue("menuCode"),
                formData, selfGrid, postData;

        if (!jqForm.form("valid")) return;
        // 保存前回调方法
        if (_this.processBeforeSave(jqForm, op) === false) return;
        // 获取表单数据
        formData = jqForm.form("formData", false);
        postData = {E_entityJson: $.config.toString(formData)};
        postData = ui.processPostData(postData, CFG_common.P_SAVE);
        $.ajax({
            type : "post",
            url : url,
            data : postData,
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
                    jqForm.form("loadData", entity);
                    if (_this.options.model === $.config.contentType.form) {
                        selfGrid = _this.getSelfGrid();
                        if (selfGrid) {
                            selfGrid.reload();
                        }
                    }
                } else if ("close" === op) {
                    _this.clickBackToGrid();
                } else if ("create" === op) {
                    _this.clickCreate();
                    var jdbhJQ = ui.getItemJQ("JDBH");
                    var jdbhData = $.loadJson($.contextPath + "/zzjdxx!getJdbhLsh.json");
                    var qybmJQ = ui.getItemJQ("QYBM");
                    var qybmData = $.loadJson($.contextPath + "/zzcpxxglxz!getQybm.json");
                    var jdbh = qybmData + jdbhData;
                    ui.setFormData({JDBH:jdbh,QYBM:qybmData});
                } else {
                    _this.doViewRecord(op);
                }
            },
            error : function() {
                CFG_message("操作失败！", "error");
            }
        });
    }
};
/**
 *  二次开发：复写自定义列表
 */
function _override_grid (ui) {
    ui.beforeInitGrid = function(setting) {
        setting.fitStyle = "width";
        return setting;
    };
    ui.clickDelete = function (isLogicalDelete) {
        var idArr = ui.uiGrid.grid("option", "selarrrow");

        if (0 === idArr.length) {
            CFG_message( "请选择记录!", "warning");
            return;
        }
        var deJdbh = "";
        for(var i in idArr){
            var rowData = ui.uiGrid.grid("getRowData",idArr[i]);
            var url = $.contextPath + "/zzjdxx!querySsqy.json?jdbh="+rowData.JDBH;
            var ssqy = $.loadJson(url);
            if(ssqy.length != 0){
                deJdbh += ssqy[0].JDBH;
                if(idArr.length - 1 != i){
                    deJdbh += "、";
                }else{
                    deJdbh += "。";
                }
            }
        }
        if(deJdbh.length == 0){
            ui.databaseDelete(idArr, isLogicalDelete);
        }else{
            $.alert("删除失败!以下基地编号有所属区域："+deJdbh);
            return;
        }

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
     *  二次开发：复写基本查询区
     */
    function _override_bsearch (ui) {
//		ui._init = function() {
//			ui.callItemMethod("QVMC", "destroy");
//			ui.getItemJQ("QVMC").textbox({});
//			ui.callItemMethod("DKBH", "destroy");
//			ui.getItemJQ("DKBH").textbox({});
//		};
        // ui.assembleData 就是 configInfo
        //console.log("override search!");
        //ui.bindEvent = function () {
        // 添加onChange事件
        //	  ui.setItemOption("NAME", "onChange", function(e, data) {})
        //};
    };
    /**
     *  二次开发：复写高级查询区
     */
    function _override_gsearch (ui) {
        // ui.assembleData 就是 configInfo
        //console.log("override search!");
        //ui.bindEvent = function () {
        // 添加onChange事件
        //	  ui.setItemOption("NAME", "onChange", function(e, data) {})
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
        } else if (this instanceof $.config.cbsearch) {
            _override_bsearch(this);
        } else if (this instanceof $.config.cgsearch) {
            _override_gsearch(this);
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
