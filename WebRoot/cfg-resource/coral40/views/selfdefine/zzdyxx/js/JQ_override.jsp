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
            return $.contextPath + "/zzdyxx";
        };

        /**
         * 二次开发：复写自定义表单
         */
        function _override_form(ui) {
            // ui.assembleData 就是 configInfo
            //console.log("override grid!");
            //ui.getAction = function () {
            //	return $.contextPath + "/appmanage/show-module";
            //};
            formId = ui.options.tableId;

            ui.beforeSave = function (jq, options) {
                var dkmjJQ = ui.getItemJQ("ZZDYMJ");//地块面积
                var dkbh = ui.getItemJQ("DKBH").combogrid("getValue");
                var zzdybh = ui.getItemJQ("ZZDYBH").textbox("getValue");
                if (dkbh.length == 0 || zzdybh.length == 0) {
                    return false;
                }
                var mj = dkmjJQ.textbox("getValue");
                if (mj == 0) {
                    CFG_message("剩余面积不够！", "error");
                    return false;
                }
                var jsonData = $.loadJson($.contextPath + "/trace!checkMj.json?mk=dy&bh=" + dkbh + "&mj=" + mj + "&sbh=" + zzdybh);
                if (jsonData.result == "ERROR") {
                    dkmjJQ.textbox("setValue", "");
                    CFG_message(jsonData.msg, "error");
                    return false;
                }
            }

            //初始化数据方法
            ui._init = function () {
                var qyxx = $.loadJson($.contextPath + '/trace!getQybmAndQymc.json?sysName=ZZ');
                ui.setFormData({QYBM: qyxx.qybm});
                if (isEmpty(ui.options.dataId)) {
                    ui.setFormData({SYZT: "XG"});
                }
                //获取当前企业基地信息
                var getQyxx = $.loadJson($.contextPath + '/qyxxjdxx!getJDXX.json');
                //alert(getQyxx.JDMC);
                ui.setFormData({JDBH: getQyxx.JDBH, SSJD: getQyxx.JDMC});
                var $dkbh = ui.getItemJQ("DKBH");
                var qybh = ui.getItemValue("QYBH");
                $dkbh.combogrid("reload", 'zzdyxx!getDkxx.json?qybh=' + qybh);
            }

            ui.bindEvent = function () {
                var gly = ui.getItemJQ("GLYBH");//负责人
                var dkbh = ui.getItemJQ("DKBH");//地块
                var jdbh = ui.getItemJQ("JDBH");//所属基地
                var qybh = ui.getItemJQ("QYBH");//所属区域

                /*          jdbh.combogrid("option","onChange",function(e,data){
                 //获取下拉列表选中行数据
                 //            var grid = ssjd.combogrid("grid");
                 //            var selectedRowId = grid.grid("option","selrow");
                 //            var rowData = grid.grid("getRowData",selectedRowId);
                 //            var jdbh = rowData.JDBH;
                 //设定值
                 ui.setFormData({
                 JDBH:data.newValue,
                 SSJD:data.newText
                 });
                 qybh.combogrid("reload",'zzdyxx!getQyxx.json?jdbh='+data.newValue);
                 ui.setFormData({
                 SSQY:"",
                 QYBH:"",
                 DKBH:"",
                 DKMC:"",
                 DKMJ:"",
                 DKFZR:"",
                 ZZDYBH:""
                 });
                 //            alert(ui.getItemValue("SSJD")+ui.getItemValue("JDBH"));
                 }); */
                qybh.combogrid("option", "onChange", function (e, data) {
                    ui.setFormData({
                        QYBH: data.newValue,
                        SSQY: data.newText
                    });
                    dkbh.combogrid("reload", 'zzdyxx!getDkxx.json?qybh=' + data.newValue);
                    ui.setFormData({
                        DKBH: "",
                        DKMC: "",
                        DKMJ: "",
                        DKFZR: "",
                        ZZDYBH: ""
                    });
                });
                gly.combogrid("option", "onChange", function (e, data) {
                    ui.setFormData({
                        GLY: data.newText,
                        GLYBH: data.newValue
                    });
                });
                dkbh.combogrid("option", "onChange", function (e, data) {

                    var jsonData = $.loadJson($.contextPath + '/zzdyxx!getDkxx.json?dkbh=' + data.newValue + '&dkbh=' + ui.getItemValue("DKBH"));
                    var dataMaps = jsonData.data;
                    ui.setFormData({
                        DKBH: data.newValue,
                        DKMC: dataMaps[0].DKMC,
                        DKMJ: dataMaps[0].MJ,
                        DKFZR: dataMaps[0].DKFZR,
                        FZRBH: dataMaps[0].DKFZRBH
                    });
                    var dylsh = $.loadJson($.contextPath + '/zzdyxx!getDylsh.json?dkbh=' + data.value);
                    ui.setFormData({
                        ZZDYBH: dylsh
                    });
                });
            }
            //弹出式负责人回调
            ui.addCallback("setComboGrid_Fzr", function (o) {
                if (null == o) return;
                var rowData = o.result;
                if (null == rowData) return;
                ui.setFormData({
                    GLY: rowData.XM,
                    GLYBH: rowData.GZRYBH
                });
            });
            //弹出式地块信息回调
            ui.addCallback("setComboGrid_Dkxx", function (o) {
                if (null == o) return;
                var rowData = o.result;
                if (null == rowData) return;
                ui.setFormData({
                    DKBH: rowData.DKBH,
                    DKMC: rowData.DKMC,
                    DKMJ: rowData.MJ,
                    DKFZR: rowData.DKFZR,
                    FZRBH: rowData.DKFZRBH
                });
                var dylsh = $.loadJson($.contextPath + '/zzdyxx!getDylsh.json?qybh=' + ui.getItemValue("QYBH"));
                ui.setFormData({
                    ZZDYBH: dylsh
                });
            });
            //弹出式区域信息回调
            ui.addCallback("setComboGrid_Ssqy", function (o) {
                if (null == o) return;
                var rowData = o.result;
                if (null == rowData) return;
                ui.setFormData({
                    SSQY: rowData.QYMC,
                    QYBH: rowData.QYBH
                });
                var dkbh = ui.getItemJQ("DKBH");
                dkbh.combogrid("reload", 'zzdyxx!getDkxx.json?qybh=' + rowData.QYBH);
                ui.setFormData({
                    DKBH: "",
                    DKMC: "",
                    DKMJ: "",
                    DKFZR: "",
                    ZZDYBH: ""
                });
            });
            //弹出式基地信息回调
            ui.addCallback("setComboGrid_Jdxx", function (o) {
                if (null == o) return;
                var rowData = o.result;
                if (null == rowData) return;
                ui.setFormData({
                    JDBH: rowData.JDBH,
                    SSJD: rowData.JDMC
                });
                var qybh = ui.getItemJQ("QYBH");
                qybh.combogrid("reload", 'zzdyxx!getQyxx.json?jdbh=' + rowData.JDBH);
                ui.setFormData({
                    SSQY: "",
                    QYBH: "",
                    DKBH: "",
                    DKMC: "",
                    DKMJ: "",
                    DKFZR: "",
                    ZZDYBH: ""
                });
//        alert(ui.getItemValue("SSJD")+ui.getItemValue("JDBH"));
            })
            //弹出式地块信息过滤
            ui.addOutputValue("setTcsDkxx", function (o) {
                var qybh = ui.getItemValue("QYBH");
                var o = {
                    status: true,
                    P_columns: "EQ_C_QYBH≡" + qybh
                }
                return o;
            });
            //弹出式基地信息过滤
            ui.addOutputValue("setTcsSsqy", function (o) {
                var jdbh = ui.getItemValue("JDBH");
                var o = {
                    status: true,
                    P_columns: "EQ_C_JDBH≡" + jdbh
                }
                return o;
            });
        };
        /**
         *  二次开发：复写自定义列表
         */
        function _override_grid(ui) {
            ui.beforeInitGrid = function (setting) {
                setting.fitStyle = "width";
                return setting;
            };
            // ui.assembleData 就是 configInfo
            //console.log("override grid!");
            //ui.getAction = function () {
            //	return $.contextPath + "/appmanage/show-module";
            //};
            ui.beforeDelete = function (idArr, isLogicalDelete) {
                var notBindedIds = $.loadJson($.contextPath + '/zzdyxx!checkBindle.json?idArr=' + JSON.stringify(idArr));
                var bindedIdArr = $.arrayIntersect(idArr, notBindedIds);
                var bindedDy = "";
                for (var i in bindedIdArr) {
                    var rowData = ui.getRowData(bindedIdArr[i]);
                    bindedDy += rowData.ZZDYMC;
                    bindedDy += ",";
                }
                if (bindedDy != "") {
                    bindedDy = bindedDy.substring(0, bindedDy.length - 1);
                    CFG_message("单元（" + bindedDy + ")已绑定信息,不可删除", "warning");
//            idArr = notBindedIds;
                    return false;
                }
                return true;
            };
            $.arrayIntersect = function (a, b) {
                return $.grep(a, function (i) {
                    return $.inArray(i, b) == -1;
                });
            };
        };
        /**
         *  二次开发：复写自定义树
         */
        function _override_tree(ui) {
            // ui.assembleData 就是 configInfo
            //console.log("override tree!");
            //ui.getAction = function () {
            //	return $.contextPath + "/appmanage/show-module";
            //};
        };
        /**
         *  二次开发：复写自定义工具条
         */
        function _override_tbar(ui) {
            // ui.assembleData 就是 configInfo
            //console.log("override tbar!");
            //ui.getAction = function () {
            //	return $.contextPath + "/appmanage/show-module";
            //};
        };
        /**
         *  二次开发：复写自定义布局
         */
        function _override_layout(ui) {

        };


        /**
         *  二次开发：复写基本查询区
         */
        function _override_bsearch(ui) {
            ui._init = function () {
                ui.callItemMethod("DKBH", "destroy");
                ui.getItemJQ("DKBH").textbox({});
                ui.callItemMethod("DKBH", "destroy");
                ui.getItemJQ("DKBH").textbox({});
            };
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
        function _override_gsearch(ui) {
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
