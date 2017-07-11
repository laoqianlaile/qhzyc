<%@page language="java" pageEncoding="UTF-8" %>
<script type="text/javascript">
    /***************************************************!
     * @author qiucs
     * @date   2014-7-15
     * 系统配置平台应用自定义二次开发JS模板
     ***************************************************/


    (function (subffix) {

        /**
         * 二次开发定位自己controller
         * @returns {String}
         **/
        window[CFG_actionName(subffix)] = function (ui) {
            // ui.assembleData 就是 configInfo
            return $.contextPath + "/zzfabd";
        };


        /**
         * 二次开发：复写自定义表单
         */
        function _override_form(ui) {
            //修改操作加载表单数据
            var dataId = CFG_getInputParamValue(ui.assembleData.parentConfigInfo, "FAID");
            var isView = CFG_getSelfParamValue(ui.assembleData.parentConfigInfo, "VIEW");
            ui.options.dataId = dataId;
            ui._init = function () {
                //主表数据id 属性，提供列表区域调用
                ui.assembleData.dataId = dataId;
                var ycmc = ui.getItemJQ("YCMC");
                ycmc.combobox({
                    valueField:"value",
                    textField:"name",
                    url:$.contextPath + "/zzfabd!getYcmc.json",
                    onChange : function(e,data){
                        ui.setFormData({
                            YCMC: data.text,
                            ZSSPM: data.value,
                            YCDM: data.value
                        })
                    }
                });
                ycmc.combobox("reload");
                if (!isEmpty(ui.options.dataId)){
                    ui.getItemJQ("ZZZMMC").textbox("destroy");
                    var zmzzData = $.loadJson($.contextPath + '/zzfabd!getZmzz.json');
                    ui.getItemJQ("ZZZMMC").combobox({
                        data: zmzzData,
                        textField: 'ZZZMMC',
                        valueField: 'ZZZMBH',
                        required: true
                    });
                    var zzzmObj = $.loadJson($.contextPath + '/zzfabd!getZmzzById.json?id='+ui.options.dataId);
                    var ycmcObj = $.loadJson($.contextPath + '/zzfabd!getYcmcById.json?id='+ui.options.dataId);
                    ui.getItemJQ("YCMC").combobox("setValue", ycmcObj.YCMC);
                    ui.getItemJQ("ZZZMMC").combobox("setValue", zzzmObj.ZZZMMC);
                    ui.setItemOption("ZZZMMC", "onChange", function (e, data) {
                        ui.setFormData({
                            ZZZMMC: data.text,
                            ZZZMBH: data.value
                        })
                    })
                }
                //新增
                if (isEmpty(ui.options.dataId)) {
                    ui.getItemJQ("ZZZMMC").textbox("destroy");
                    var zmzzData = $.loadJson($.contextPath + '/zzfabd!getZmzz.json');
                    ui.getItemJQ("ZZZMMC").combobox({
                        data: zmzzData,
                        textField: 'ZZZMMC',
                        valueField: 'ZZZMBH',
                        required: true
                    });
                    ui.setItemOption("ZZZMMC", "onChange", function (e, data) {
                        ui.setFormData({
                            ZZZMMC: data.text,
                            ZZZMBH: data.value
                        })
                    })
                }
                //详细查看
                if (isView == "DETAIL") {
                    $("form", ui.uiForm).form("setReadOnly", true);

                }

                //品类品种可搜索
                var plbh = ui.getItemJQ("PLBH");
                var pzbh = ui.getItemJQ("PZBH");
                plbh.combobox("option", "enableFilter", true);
                pzbh.combobox("option", "enableFilter", true);
            };

            //绑定事件
            ui.bindEvent = function () {
                var plbh = ui.getItemJQ("PLBH");
                var pzbh = ui.getItemJQ("PZBH");
                plbh.combobox("option", "onChange", function (e, data) {
                    ui.setFormData({
                        PL: data.text
                    });
                    pzbh.combobox("option", "valueField", "PZBH");
                    pzbh.combobox("option", "textField", "PZ");
                    pzbh.combobox("reload", 'zzfabd!getPzxxByPlbh.json?plbh=' + data.value);
                });
                pzbh.combobox("option", "onChange", function (e, data) {
                    ui.setFormData({
                        PZ: data.text
                    });
                });
            };

            //复写保存按钮，设置属性:主表记录id
            ui.clickSave = function (op) {
                var allData = {};
                //主表
                var jqForm = $("form", ui.uiForm), formData;
                if (!jqForm.form("valid")) return;
                formData = jqForm.form("formData", false);
                allData.fa = formData;
                //获取标签页configInfo
                var tabConfigInfo = ui.assembleData.parentConfigInfo.parentConfigInfo.bottomChildConfigInfo;
                var tabId = tabConfigInfo.tabId;        //标签页id
                var childrenConfigInfo = tabConfigInfo.childConfigInfos;		//tab页下的grid们
                //校验
                var errorTab = "";
                var tabName = "";
                var trpTabName = "";
                var flag = false;
                for (var i in childrenConfigInfo) {
                    if (childrenConfigInfo[i].title == "播种方案") {
                        errorTab = "播种方案";
                        tabName = "bzfa";
                        trpTabName = "bzfatrp";
                    } else if (childrenConfigInfo[i].title == "灌溉") {
                        errorTab = "灌溉";
                        tabName = "gg";
                        trpTabName = "ggtrp";
                    } else if (childrenConfigInfo[i].title == "施肥") {
                        errorTab = "施肥";
                        tabName = "sf";
                        trpTabName = "sftrp";
                    } else if (childrenConfigInfo[i].title == "用药") {
                        errorTab = "用药";
                        tabName = "yy";
                        trpTabName = "yytrp";
                    } else if (childrenConfigInfo[i].title == "锄草") {
                        errorTab = "锄草";
                        tabName = "cc";
                        trpTabName = "cctrp";
                    } else if (childrenConfigInfo[i].title == "采收") {
                        errorTab = "采收";
                        tabName = "cs";
                        trpTabName = "cstrp";
                    } else if (childrenConfigInfo[i].title == "其他") {
                        errorTab = "其他";
                        tabName = "qt";
                        trpTabName = "qttrp";
                    }


                    var jqMasterGrid = $("#" + childrenConfigInfo[i].cMasterGridDivId);
                    var jqDetailGrid = $("#" + childrenConfigInfo[i].cDetailGridDivId);
                    if (!jqMasterGrid.cgrid("validGridData") || !jqDetailGrid.cgrid("validGridData")) {
                        $('#' + tabId).tabs("option", "active", parseInt(i));
                        CFG_message("请检查" + errorTab + "信息", "warning");
                        return;
                    }
                    var masterGridRowData = jqMasterGrid.cgrid("getRowData");
                    var detailGridRowData = jqDetailGrid.cgrid("getRowData");
                    allData[tabName] = masterGridRowData;
                    allData[trpTabName] = detailGridRowData;
                    for (var i in masterGridRowData) {
                        if (masterGridRowData[i].IS_START == '1') {
                            flag = true;
                        }
                    }
                }
                if (!flag) {
                    CFG_message("请先设置起始农事项", "warning");
                    return;
                }
                $.ajax({
                    url: $.contextPath + "/zzfabd!saveFaxx.json",
                    type: "POST",
                    dataType: "json",
                    async: false,
                    data: {faxx: JSON.stringify(allData)},
                    success: function () {
                        CFG_message("保存成功", "success");
                        CFG_clickCloseButton(ui.assembleData.parentConfigInfo.parentConfigInfo);
                        //CFG_clickReturnButton(ui.assembleData.parentConfigInfo.parentConfigInfo.parentConfigInfo, false);
                    },
                    error: function () {
                        CFG_message("保存失败", "error");
                    }
                });
            };
            ui.clickSecondDev = function (id) {
                if (id == $.config.preButtonId + "return") {
                    CFG_clickCloseButton(ui.assembleData.parentConfigInfo.parentConfigInfo);
                }
            }

        };
        /**
         *  二次开发：复写自定义列表
         */
        function _override_grid(ui) {
            // ui.assembleData 就是 configInfo
            //console.log("override grid!");
            //ui.getAction = function () {
            //	return $.contextPath + "/appmanage/show-module";
            //};

        }
        ;
        /**
         *  二次开发：复写自定义树
         */
        function _override_tree(ui) {
            // ui.assembleData 就是 configInfo
            //console.log("override tree!");
            //ui.getAction = function () {
            //	return $.contextPath + "/appmanage/show-module";
            //};
        }
        ;
        /**
         *  二次开发：复写自定义工具条
         */
        function _override_tbar(ui) {
            ui.processItems = function (data,pos) {
                var isView = CFG_getSelfParamValue(ui.assembleData.parentConfigInfo, "VIEW");
                var btns = [];
                var poss = "";
                var op = ui.options.op.toString();
                if ($.config.contentType.isForm(ui.options.type)) {
                    if (op == '0') {
                        poss =" - 新增";
                    } else if (op == '1') {
                        poss =" - 修改";
                    } else if (op == '2') {
                        poss =" - 详情";
                    }
                    if(data.length == 0){
                        poss =" - 详请";
                    }
                }
                if(pos == 'top') {// 在顶部的时候添加当前位置
                    btns.push({
                        "type": "html",
                        "content": "<div class='homeSpan'><div><div style='margin-left:20px'> - 种植方案配置管理 " + poss + "</div>",
                        frozen: true
                    });
                }
                if (isView != "VIEW") {
                    for (var i = 0; i < data.length; i++) {
                        btns.push(data[i]);
                    }
                }
                if(isView =="DETAIL"){
                    btns = [];
                    btns.push({
                        "type": "html",
                        "content": "<div class='homeSpan'><div><div style='margin-left:20px'> - 种植方案配置管理 - 详情</div>",
                        frozen: true
                    });
                    btns.push("->");
                }
                btns.push({
                    id: $.config.preButtonId + "return",
                    label: "返回",
                    cls:"return_tb",
                    type: "button"
                });
                return btns;
            };

        }

        /**
         *  二次开发：复写自定义布局
         */
        function _override_layout(ui) {
            //console.log("override layout!");
            //ui.getAction = function () {
            //	return $.contextPath + "/appmanage/show-module";
            //};
        }
        ;


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


    })
    ("${timestamp}");
</script>
