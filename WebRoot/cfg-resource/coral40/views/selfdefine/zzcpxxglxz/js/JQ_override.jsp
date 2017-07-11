<%@ page import="com.ces.component.trace.utils.SerialNumberUtil" %>
<%@page language="java" pageEncoding="UTF-8" %>
<%
    String companyCode = SerialNumberUtil.getInstance().getCompanyCode();
%>
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
            return $.contextPath + "/zzcpxxglxz";
        };


        /**
         * 二次开发：复写自定义表单
         */
        function _override_form(ui) {
            if (ui.options.number == 1) {
                ui._init = function () {
                    if (isEmpty(ui.options.dataId)) {
                        var qybmJQ = ui.getItemJQ("QYBM");
                        var cpbhJQ = ui.getItemJQ("CPBH");
                        var qybmData = $.loadJson($.contextPath + "/zzcpxxglxz!getQybm.json");
                        qybmJQ.textbox("setValue", qybmData);
                        var cpbhData = $.loadJson($.contextPath + "/zzcpxxglxz!getCpbhLsh.json");
                        cpbhJQ.textbox("setValue", cpbhData);
                    }
                }
                ui.bindEvent = function () {

                }
            } else if (ui.options.number == 2) {
                ui._init = function () {
                    var pzJQ = ui.getItemJQ("PZ");
                    if (isEmpty(ui.options.dataId)) {

                    }
                }

                ui.bindEvent = function () {
                    var dpzJQ = ui.getItemJQ("DPZ");
                    var pzJQ = ui.getItemJQ("PZ");
                    dpzJQ.combogrid("option", "onChange", function (e, data) {
                        var text = data.text;
                        var value = data.value;
                        ui.setFormData({DPZ: text, DPZBH: value});
                        ui.setFormData({PZ: "", PZBH: ""});
                        pzJQ.combogrid("reload", $.contextPath + "/tcszzxpzxx!getGrid.json?plbh=" + value);
                    });
                    pzJQ.combogrid("option", "onChange", function (e, data) {
                        var text = data.text;
                        var value = data.value;
                        ui.setFormData({PZ: text, PZBH: value});
                    });

                }


                ui.clickAdd = function (op) {
                    var cGrid = ui.getSelfGrid(), // 对应列表
                            jqForm = $("form", ui.uiForm),
                            formData;
                    //获取表单使用添加剂编号
                    var pzbhJQ = ui.getItemJQ("PZBH");
                    //判断是否重复
                    var rowIdArr = cGrid.getSelectedRowId();
                    if (rowIdArr.length > 1) {
                        $.message({message: "最多选择一条记录!", cls: "warning"});
                        return;
                    }
                    var bool = false;
                    var cGridData = cGrid.uiGrid.grid("getRowData");
                    var cRowGridData = cGrid.uiGrid.grid("getRowData",rowIdArr[0]);
                    $.each(cGridData, function (e, data) {
                        if(cRowGridData.PZBH != data.PZBH){
                            if (pzbhJQ.textbox("getValue") == data.PZBH) {
                                bool = true;
                            }
                        }
                    });
                    if (bool) {
                        $.message({message: "所选品种重复!", cls: "warning"});
                        return;
                    }
                    // 表单检验
                    if (!jqForm.form("valid")) return false;
                    // 获取表单数据
                    formData = jqForm.form("formData", false);
                    // 向列表添加数据
                    cGrid.addRowData(formData);
                    var pzJQ = ui.getItemJQ("PZ");
                    pzJQ.combogrid("reload", $.contextPath + "/tcszzxpzxx!getGrid.json?plbh=-1");
                    if ("close" === op) {
                        // 关闭或返回
                        ui.clickBackToGrid(false);
                    } else {
                        // 重置表单数据
                        ui.clickCreate();
                    }
                }

                //大品种
                ui.addCallback("setComboGridValue_dpz", function (o) {
                    if (null == o) return;
                    var obj = o.rowData;
                    if (null == obj) return;
                    ui.setFormData({DPZ: obj.PL, DPZBH: obj.PLBH});
                    ui.setFormData({PZ: "", PZBH: ""});
                    var pzJQ = ui.getItemJQ("PZ");
                    pzJQ.combogrid("reload", $.contextPath + "/tcszzxpzxx!getGrid.json?plbh=" + obj.PLBH);
                });

                //小品种
                ui.addOutputValue("setPzColumns", function (o) {
                    var dpzbh = ui.getItemJQ("DPZBH").textbox("getValue");
                    var o = {
                        status: true,
                        P_columns: "EQ_C_PLBH≡" + dpzbh
                    }
                    return o;
                });

                ui.addCallback("setComboGridValue_pz", function (o) {
                    if (null == o) return;
                    var obj = o.rowData;
                    if (null == obj) return;
                    ui.setFormData({PZ: obj.PZ, PZBH: obj.PZBH});
                });

                ui.clickSecondDev = function (id) {
                    if (id == $.config.preButtonId + "del") {
                        var cGrid = this.getSelfGrid();
                        var rowIdArr = cGrid.getSelectedRowId();
                        if (rowIdArr === null || 0 === rowIdArr.length) {
                            $.message({message: "请选择记录!", cls: "warning"});
                            return;
                        }
                        for (var i = rowIdArr.length - 1; i > -1; i--) {
                            cGrid.uiGrid.grid("delRowData", rowIdArr[i]);
                        }
                    }
                }
            }
        };
        /**
         *  二次开发：复写自定义列表
         */
        function _override_grid(ui) {
            ui.eSelectRow = function (e, data) {
                var dataId = null, idArr = ui.getSelectedRowId(),
                        master = null, detail = null, jqUI = null;
                // 工作流按钮处理
                ui._processCoflowButton(idArr);
                // 相关列表或表单处理
                if (idArr.length == 0 || idArr.length > 1) {
                    ui.selectedRowId = null;
                    ui.clearDetailGridData();
                    return;
                }
                dataId = idArr[0];
                //
                ui.selectedRowId = dataId;
                // 列表数据是临时的，未保存
                if (dataId.indexOf("UNSAVE_") === 0) {
                    jqUI = ui.getSelfForm();
                    if (jqUI) {
                        jqUI.setFormData(ui.getRowData(dataId));
                        var pzJQ = jqUI.getItemJQ("PZ");
                        var dpzbhJQ = jqUI.getItemJQ("DPZBH");
                        pzJQ.combogrid("reload", $.contextPath + "/tcszzxpzxx!getGrid.json?plbh=" + dpzbhJQ.textbox("getValue"));
                    }
                    return;
                }
                detail = ui._getDetail();
                if (detail) {
                    jqUI = detail.jqUI;
                    if ($.config.contentType.isGrid(detail.type)) {
                        jqUI.enableToolbar();
                        jqUI.load();
                    } else if ($.config.contentType.isForm(detail.type) && ui.options.tableId === detail.tableId) {
                        jqUI.load(dataId);
                    }
                } else {
                    master = ui._getMaster();
                    if (master) {
                        jqUI = master.jqUI;
                        if ($.config.contentType.isForm(master.type) && ui.options.tableId === master.tableId) {
                            jqUI.load(dataId);
                        }
                    }
                }
            }
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
            if (2 === ui.options.number) {
                ui.processItems = function (data) {
                    var btns = [];
                    for (var i = 0; i < data.length; i++) {
                        btns.push(data[i]);
                    }
                    btns.push({
                        id: $.config.preButtonId + "del",
                        label: "删除",
                        type: "button"
                    });
                    return btns;
                };

            }
        };
        /**
         *  二次开发：复写自定义布局
         */
        function _override_layout(ui) {
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
