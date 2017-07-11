<%@page language="java" pageEncoding="UTF-8"%>
<script type="text/javascript">
/***************************************************!
 * @author qiucs 
 * @date   2014-7-15
 * 系统配置平台应用自定义二次开发JS模板 
 ***************************************************/


(function(subffix) {
    var _djcpsl = "";
    var _zhcpsl = "";
/**
 * 二次开发定位自己controller
 * @returns {String}
 **/
window[CFG_actionName(subffix)] = function (ui) {
	// ui.assembleData 就是 configInfo
	return $.contextPath + "/zzbzgl";
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
//    ui.init=function(ui){
////        ui.uiGrid;
//    }
    ui.beforeInitGrid = function(setting) {
        setting.fitStyle = "width";
        return setting;
    };

    ui.beforeInitGrid = function(setting) {
        setting.fitStyle = "width";
        return setting;
    };

	ui.clickSecondDev = function(id){
        if (id == $.config.preButtonId + "print") {
            if(isSwt || isNewSwt){
                if(ui.getSelectedRowId().length != 1){
                    CFG_message("请选择一条记录", "warning");
                    return;
                }
            }else{
                $.alert("请在程序环境中写卡");
                return;
            }
            showPrintDialog();
        }
        <%--else if(id == $.config.preButtonId + "chuchang"){--%>
            <%--var gridid=ui.uiGrid.grid('option','selarrrow');--%>
            <%--if(gridid.length!=1){$.alert("请选择一条纪录");return}--%>
            <%--var id=ui.uiGrid.grid('option','selrow');--%>
           <%--var url= '/cfg-resource/coral40/views/component/zzccglxz/zzccglxz.jsp?constructId=402881ec4f6ccb2f014f6de66c6e000a&ISVIEW=NO&componentVersionId=402881ec4f6ccb2f014f6de66c510009&constructDetailId=40289481504bafb301504be7e935000a&assembleType=1&menuId=838385d54f63be47014f64006502002b&menuCode=ccgl&topComVersionId=838385d54f63be47014f63fcf115000f&status=true&___t=1447828837837&id='+id;--%>
<%--//            var url=--%>
<%--//            var url= $.contextPath+'/cfg-resource/coral40/views/component/zzccglxz/zzccglxz.jsp';--%>
<%--//            var url= $.contextPath+"cfg-resource/coral40/views/component/zzccglxz/zzccglxz.jsp?menuId=838385d54f63be47014f64006502002b"--%>
<%--//            var url= $.contextPath+"cfg-resource/coral40/views/component/zzccglxz/zzccglxz.jsp?constructId=402881ec4f6ccb2f014f6de66c6e000a&ISVIEW=NO&componentVersionId=402881ec4f6ccb2f014f6de66c510009&constructDetailId=40289481504bafb301504be7e935000a&assembleType=1&menuId=838385d54f63be47014f64006502002b&menuCode=ccgl&topComVersionId=838385d54f63be47014f63fcf115000f"--%>
            <%--var configInfo = $('#mtc_${timestamp}').data("configInfo");--%>
<%--//        /cfg-resource/coral40/views/selfdefine/zzccbzcpxx/js/JQ_override.jsp--%>
            <%--$.ajax({--%>
                <%--type : "POST",--%>
                <%--url : $.contextPath+'/cfg-resource/coral40/views/selfdefine/zzccbzcpxx/js/JQ_override.jsp?bzid='+id,--%>
                <%--data : {id:id},--%>
                <%--dataType : "html",--%>
                <%--context : document.body,--%>
<%--//              async : false--%>
            <%--})--%>
            <%--menuClick('出场管理',url);--%>


            <%--//menuClick('出场管理','/cfg-resource/coral40/views/component/zzccglxz/zzccglxz.jsp');--%>


            <%--///cfg-resource/coral40/views/selfdefine/zzccbzcpxx/js/JQ_override.jsp--%>
            <%--var jq=$('#mtc_${timestamp}');--%>
            <%--jq.data("parentConfigInfo", configInfo);--%>
<%--//            $.ajax({--%>
<%--//                type : "POST",--%>
<%--//                url : $.contextPath+'/cfg-resource/coral40/views/selfdefine/zzccbzcpxx/js/JQ_override.jsp',--%>
<%--//                data : {id:id},--%>
<%--//                dataType : "html",--%>
<%--//                context : document.body,--%>
<%--//                async : false--%>
<%--//            })--%>

        <%--}--%>
	}

    /************************打印dialog bigen************************/
    function showPrintDialog(){
        var jqGlobal = $(ui.options.global);
        var jqUC = $("<div id=\"jqUC\"></div>").appendTo("body");
        jqUC.dialog({
            appendTo: jqGlobal,
            modal: true,
            title: "打印",
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
  //              var jqDiv = $("<div class=\"app-inputdiv-full\" style=\"padding:10px 20px;\">" +
   //                     "   <div class=\"fillwidth colspan2 clearfix\">" +
   //                     "       <div class=\"app-inputdiv6\">" +
    //                    "           <label class=\"app-input-label\" style=\"width:90%;\">二维码分类：</label>" +
   //                     "       </div>" +
  //                      "       <div class=\"app-inputdiv6\">" +
   //                     "           <label class=\"app-input-label\" style=\"width:70%;\">打印份数：</label>" +
   //                     "       </div>" +
//                        "       <div class=\"app-inputdiv6\">" +
//                        "       </div>" +
  //                      "   </div>" +
   //                     "</div>" +
   //                     "       <div class=\"app-inputdiv12\" style=\"position: absolute;margin-top: 17px;width:100%;z-index:-1\">" +
   //                     "           <hr style=\"BORDER-BOTTOM-STYLE: dotted; BORDER-LEFT-STYLE: dotted; BORDER-RIGHT-STYLE: dotted; BORDER-TOP-STYLE: dotted\" color=#111111 size=1> " +
   //                     "       </div>" +
   //                     "       <div style='margin-left:90px;display: inline-block;'>" +
   //                     "           <input id='checkboxlist1' name='checkboxlist' >" +
   //                     "       </div>" +
   //                     "       <div style='margin-left:30px;display: inline-block;'>" +
   //                     "           <input id='djcp' name='DJCP' readonly/>" +
   //                     "       </div>" +
   //                     "       <div style='margin-left:90px;display: inline-block;'>" +
   //                     "           <input id='checkboxlist2' name='checkboxlist' >" +
    //                    "       </div>" +
   //                     "       <div style='margin-left:30px;display: inline-block;'>" +
   //                     "           <input id='zhcp${idSuffix}' name='ZHCP' readonly/>" +
   //                     "       </div>"
   //             ).appendTo(this);
                //jqDiv为新增内容，如果恢复之前功能 请删除
                var jqDiv = $("<div class=\"app-inputdiv-full\" style=\"padding:10px 20px;\">" +
                        "       <div class=\"app-inputdiv11\" style=\"float: left; width: 60px;padding-top: 8px\"  >" +
                        "           <label >打印份数：</label>" +
                        "       </div>" +
                        "       <div class=\"app-inputdiv12\" style=\"float: left;width: 250px;\" >" +
                        "           <input id='djcp' name='DJCP' />" +
                        "       </div>" +
                        "   </div>"
                ).appendTo(this);

                $('#djcp,#zhcp').textbox({
//                    readonly:true
                });
                var rowData = ui.uiGrid.grid("getRowData",ui.getSelectedRowId());
                var booll = rowData.BZXS.indexOf("散装");
                if(booll != -1){
                    $("#checkboxlist2").checkboxlist("disable");
                }

            },
            onOpen: function () {
                var jqPanel = $(this).dialog("buttonPanel").addClass("app-bottom-toolbar"),
                        jqDiv = $("<div class=\"dialog-toolbar\">").appendTo(jqPanel);
                jqDiv.toolbar({
                    data: ["->", {id: "sure", label: "确定", type: "button"}, {
                        id: "cancel",
                        label: "取消",
                        type: "button"
                    }],
                    onClick: function (e, data) {
                        _djcpsl =1;_zhcps1 =0;//新增内容 使得_djcpsl,_zhcps1失去区分选择单件还是组合产品的功能
                        if ("sure" === data.id) {
                            if(_djcpsl == 1){
                                if (/^[0-9]+$/.test( $("#djcp").val() )) {
                                } else {
                                    CFG_message("请输入自然数！", "warning");
                                    return false;
                                }
                            }else if(_zhcps1 == 1){
                                if (/^[0-9]+$/.test( $("#djcp").val() )) {//此处修改了内容 原值为#zhcp
                                } else {
                                    CFG_message("请输入自然数！", "warning");
                                    return false;
                                }
                            }else{
                                CFG_message("请输入打印份数！", "warning");//新增内容
                               // CFG_message("请选择打印形式和打印份数！", "warning");
                                return false;
                            }
                            var rowData = ui.uiGrid.grid("getRowData",ui.getSelectedRowId());
                            var bxq = "";
                            for(var i in rowData.BZSJ.split("-")){
                                if(i==0){
                                    bxq = parseInt(rowData.BZSJ.split("-")[i]) + 1 ;
                                }else{
                                    bxq += "-" + rowData.BZSJ.split("-")[i];
                                }
                            }

                            if(_djcpsl == 1){
                                for(var i = 0;i<parseInt($("#djcp").val());i++){
                                    var cpzsm = rowData.CPZSM;
                                    var data = {
                                        qymc: rowData.QYMC,
                                        cpmc: rowData.CPMC,
                                        cpdj: rowData.CPDJ,
                                        bzrq: rowData.BZSJ,
                                        bxq: bxq,
                                        cpzsm: cpzsm,
                                        bzgg: rowData.BZGG
                                    };
                                    if(isSwt){
                                        var result = _window("printSczzBz", JSON.stringify(data));
                                        var resultData = JSON.parse(result);
                                        if (resultData.status == "true" || resultData.status == true) {
                                            var savePrint = $.loadJson($.contextPath + "/zzbzgl!savePrint.json?bzlsh="+rowData.BZLSH+"&bzxs=xbz&cpzsm="+cpzsm+"&id="+rowData.ID);
                                            if(savePrint != true ){
                                                $.alert("打印失败：" + resultData.msg);
                                                return false;
                                            }
                                        } else {
                                            $.alert("打印失败：" + resultData.msg);
                                            return false;
                                        }
                                    }else if(isNewSwt){
                                        var result=_print.print("bzglPrint",{
                                            "CPMC":"产品名称 ：" + rowData.CPMC,
                                            "CPDJ":"产品等级 ：" + rowData.CPDJ,
                                            "BZRQ":"包装日期 ：" + rowData.BZSJ,
                                            "BXQ":"保鲜期 ：" + bxq,
                                            "ZL":"重量 ：" + rowData.BZGG,
                                            "CPZSM": cpzsm,
                                            "URL":"http://www.zhuisuyun.net/" + cpzsm
                                        });

                                        if (result.MSG == "SUCCESS") {
                                            var savePrint = $.loadJson($.contextPath + "/zzbzgl!savePrint.json?bzlsh="+rowData.BZLSH+"&bzxs=xbz&cpzsm="+cpzsm+"&id="+rowData.ID);
                                            if(savePrint != true ){
                                                $.alert("打印失败");
                                                return false;
                                            }
                                        } else {
                                            $.alert("打印失败");
                                            return false;
                                        }
                                    }
                                }
                                if(_zhcpsl != 1){
                                    $.alert("打印成功");
                                }
                            }
                            if(_zhcpsl == 1){
                                for(var i = 0;i<$("#zhcp").val();i++){
                                    var cpzsm = rowData.CPZSM;
                                    if(isSwt){
                                        var data = {
                                            qymc: rowData.QYMC,
                                            cpmc: rowData.CPMC,
                                            cpdj: rowData.CPDJ,
                                            bzrq: rowData.BZSJ,
                                            bxq: bxq,
                                            cpzsm: cpzsm,
                                            bzgg: rowData.BZGG
                                        };
                                        var result = _window("printSczzBz", JSON.stringify(data));
                                        var resultData = JSON.parse(result);
                                        if (resultData.status == "true" || resultData.status == true) {
                                            var savePrint = $.loadJson($.contextPath + "/zzbzgl!savePrint.json?bzlsh="+rowData.BZLSH+"&bzxs=dbz&cpzsm="+cpzsm+"&id="+rowData.ID);
                                            if(savePrint != true ){
                                                $.alert("打印失败：" + resultData.msg);
                                                return false;
                                            }
                                        } else {
                                            $.alert("打印失败：" + resultData.msg);
                                            return false;
                                        }
                                    }else if(isNewSwt){
                                        var result=_print.print("bzglPrint",{
                                            "CPMC":"产品名称 ：" + rowData.CPMC,
                                            "CPDJ":"产品等级 ：" + rowData.CPDJ,
                                            "BZRQ":"包装日期 ：" + rowData.BZSJ,
                                            "BXQ":"保鲜期 ：" + bxq,
                                            "ZL":"重量 ：" + rowData.BZGG,
                                            "CPZSM": cpzsm,
                                            "URL":"http://www.zhuisuyun.net/" + cpzsm
                                        });

                                        if (result.MSG == "SUCCESS") {
                                            var savePrint = $.loadJson($.contextPath + "/zzbzgl!savePrint.json?bzlsh="+rowData.BZLSH+"&bzxs=xbz&cpzsm="+cpzsm+"&id="+rowData.ID);
                                            if(savePrint != true ){
                                                $.alert("打印失败");
                                                return false;
                                            }
                                        } else {
                                            $.alert("打印失败");
                                            return false;
                                        }
                                    }

                                }
                                $.alert("打印成功");
                            }
                        }
                        jqUC.dialog("close");
                        _djcpsl = "";
                        _zhcpsl = "";
                    }
                });
            }
        });
    }
    /************************打印dialog end************************/

    ui.beforeDelete=function (idArr, isLogicalDelete) {
        var isExistZzdyxx = $.loadJson($.contextPath + "/trace!canDelete.json?ids="+idArr+"&mk=BZGL");
        if(isExistZzdyxx.length>0){
            CFG_message("有正在使用的包装信息，不能删除！","error");
            return false;
        }
        return true;
    }

    function uapdteKczlForDelete(ids){
        var resultJson = $.loadJson($.contextPath + "/zzbzgl!updateKczlForDelete.json?ids=" + ids);
    }


    ui.databaseDelete = function (idArr, isLogicalDelete) {
        var _this = ui,
                url   = ui.getAction() + "!destroy.json?P_tableId=" + ui.options.tableId +
                        "&P_D_tableIds=" + ui.getDetailTableIds() +
                        "&P_isLogicalDelete=" + isLogicalDelete +
                        "&P_menuId=" + ui.options.menuId +
                        "&P_menuCode=" + ui.getParamValue("menuCode");
        // 删除前，业务处理
        if (false === ui.beforeDelete(idArr, isLogicalDelete)) {
            return false;
        }

        $.confirm("所选择的记录将被删除，确定吗？", function(sure) {
            if (sure) {
                uapdteKczlForDelete(idArr);
                $.ajax({
                    type : "post",
                    url  : url,
                    data : {"id": idArr.toString()},
                    dataType:"json",
                    success: function (msg) {
                        if (msg.success === false) {
                            CFG_message(msg.message, "warning");
                        } else {
                            // 删除后，业务处理
                            _this.afterDelete(idArr, isLogicalDelete);
                            _this.reload();
                            CFG_message("操作成功!", "success");
                        }
                    },
                    error : function () {
                        CFG_message("操作失败!", "error");
                    }
                });
            }
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
			btns.push(data[i]);
            if(i == 4){
                btns.push({
                    id:$.config.preButtonId + "print",
                    icon:"icon-print",
                    label: "打印",
                    type : "button"
                });
            }
//            if(i==5){
//                btns.push({
//                    id:$.config.preButtonId + "chuchang",
////                    icon:"chuchang",
//                    label: "出场",
//                    type : "button"
//                });
//            }
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
