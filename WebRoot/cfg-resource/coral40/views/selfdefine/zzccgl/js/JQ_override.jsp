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
	return $.contextPath + "/zzccgl";
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
    ui.beforeInitGrid = function(setting) {
        setting.fitStyle = "width";
        return setting;
    };
    ui.clickSecondDev = function (id) {
        if(id==$.config.preButtonId+"delete"){
            var ids = ui.getSelectedRowId();
            var idStr = ids.join(",");
            if (0 === ids.length) {
                CFG_message( "请选择记录!", "warning");
                return;
            }
            $.confirm("当前记录将从列表移除，确定吗？", function(sure) {
                if (sure) {
                    $.ajax({
                        type : "POST",
                        url : $.contextPath + '/zzccgl!deleteCcgl.json',
                        dataType : 'json',
                        data : {ids:idStr },
                        success : function (data) {
                            CFG_message("删除成功", "success");
                            ui.reload();
                        },
                        error : function (data) {
                            CFG_message("删除失败", "error");
                            ui.reload();
                        }
                    });
                }
            });
        } else if(id==$.config.preButtonId+"create"){
//            showPrintDialog();
            goto("zzapccc/zzapccc.jsp");
        } else if(id== $.config.preButtonId+"printAttach"){
            if (!isSwt && !isNewSwt) {
                CFG_message("请在程序中操作","warning");
                return;
            }
            var selectedRowId = ui.getSelectedRowId();
            if (selectedRowId.length!=1) {

                if(selectedRowId.length>1) CFG_message("只能选择一条记录","warning");
                else CFG_message("请选择记录","warning");
                return;
            }
            var selectedRowData = ui.getRowData(selectedRowId);
            var data = {
                xsddh:selectedRowData.XSDDH,
                khmc:selectedRowData.KHMC,
                ccsj:selectedRowData.CCSJ,
                psdz:selectedRowData.PSDZ,
                sfdh:selectedRowData.SFDH
            }
            if(isSwt){
                var result = _window("printKhccsfd",JSON.stringify(data));
                var resultData = JSON.parse(result);
                if (resultData.status == "true" || resultData.status == true) {
                }
            }else if(isNewSwt){
                var shList = $.loadJson($.contextPath +　"/zzccgl!getShListByPid.json?pid=" + selectedRowId[0]);
                var result = _print.print("zzccsfdPrint",{
                    "XSDDH":"销售订单号 ：" + selectedRowData.XSDDH,
                    "KHMC":"客户名称 ：" + selectedRowData.KHMC,
                    "CCSJ":"出场时间 ：" + selectedRowData.CCSJ,
                    "PSDZ":"配送地址 ：" + selectedRowData.PSDZ,
                    "SFDH": selectedRowData.SFDH,
                    "SHLIST": shList
                });
            }
        }
    }

    /************************出场dialog bigen************************/
    function showPrintDialog(){
        $("#jqUC").remove();
        var jqGlobal = $(ui.options.global);
        var jqUC = $("<div id=\"jqUC\"></div>").appendTo("body");
        jqUC.dialog({
            appendTo: jqGlobal,
            modal: true,
            title: "选择出场方式",
            width: 400,
            height: 160,
            resizable: false,
            position: {
                at: "center top+200"
            },
            onClose: function () {
                jqUC.dialog("close");
                jqUC.remove();
                _djcpsl = "";
                _zhcpsl = "";
            },
            onCreate: function () {
                var jqDiv = $("<div class=\"app-inputdiv-full\" style=\"padding:30px 20px;\">" +
                        "   <div class=\"fillwidth colspan2 clearfix\">" +
                        "   </div>" +
                        "</div>" +
                        "       <div style='margin-left:90px;'>" +
                        "           <input id='radiolist' name='radiolist' >" +
                        "       </div>"
                ).appendTo(this);
                $("#radiolist").radiolist({
                    column:2,
                    data : [{value:"AKHCC",text:"按客户出场"},
                        {value:"APCCC",text:"按批次出场"}
                    ],
                    onChange:function(e,data){
                    }
                });
            },
            onOpen: function () {
                var jqPanel = $(this).dialog("buttonPanel").addClass("app-bottom-toolbar"),
                        jqDiv = $("<div class=\"dialog-toolbar\">").appendTo(jqPanel);
                jqDiv.toolbar({
                    data: ["->", {id: "sure", label: "下一步", type: "button"},"->"],
                    onClick: function (e, data) {
                        if ("sure" === data.id) {
                            var value = $("#radiolist").radiolist("getValue");
                            if(value == ""){
                                return false;
                            }else if(value == "APCCC"){
                                goto("zzapccc/zzapccc.jsp");
                            }
                        }
                        jqUC.dialog("close");
                    }
                });
            }
        });
    }
    /************************出场dialog end************************/

    function goto(_url){
        var configInfo = $(ui.options.global).data("configInfo");
        var jq = $(ui.options.global);
        jq.data("parentConfigInfo", configInfo);
        var url= $.contextPath+'/cfg-resource/coral40/views/component/'+_url;
        $.ajax({
            type : "POST",
            url : url,
            data : {
            },
            dataType : "html",
            context : document.body,
            async : false
        }).done(function(html) {
            jq.empty();
            jq.append(html);
            $.parser.parse(jq);
            configInfo.childConfigInfo.CFG_bodyOnLoad(configInfo.childConfigInfo);
        });
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
    ui.processItems = function (data) {
        var btns = [];
        for (var i = 0; i < data.length; i++) {
//            if(i == 2){
//                btns.push({
//                    id:$.config.preButtonId + "create",
//                    icon:"icon-create",
//                    label: "新增",
//                    type : "button"
//                });
//            }
            btns.push(data[i]);
        }
        btns.push({
            id: $.config.preButtonId + "printAttach",
            label: "打印随附单",
            type: "button"
        });

        btns.splice(4,0,{
            id:$.config.preButtonId + "delete",
            icon:"icon-delete",
            label: "删除",
            type : "button"
        })
//        btns.push({
//            id:$.config.preButtonId + "delete",
//            icon:"icon-delete",
//            label: "删除",
//            type : "button"
//        });
//        for (var i = data.length-3; i < data.length; i++) {
//            btns.push(data[i]);
//        }
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
