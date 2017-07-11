<%@ page import="com.ces.config.utils.CommonUtil" %>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" %>
<%@ taglib prefix="ces" tagdir="/WEB-INF/tags" %>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
    request.setAttribute("gurl", path + "/qyptmdgl!getMdxx.json");
    request.setAttribute("turl", path + "/qyptmdgl!getQyxx.json");
    request.setAttribute("idSuffix", CommonUtil.generateUIId(""));
%>
<div id="max${idSuffix}" class="fill">
    <ces:layout id="layoutId${idSuffix}" name="" style="width:800px;height:600px;" fit="true">
        <ces:layoutRegion region="west" split="true" minWidth="300" maxWidth="400" style="width:150px;padding:10px;overflow:visible;overflow-x:hidden;overflow-y:scroll;overflow-base-color:white">
            <ces:tree id="treeId${idSuffix}" asyncEnable="true" asyncType="post"
                      data="[{ name:\"企业门店管理\",id:\"-1\",isParent:true,mdgl:\"mdgl\"}]" asyncUrl="${turl}" asyncAutoParam="ID,NAME"
                      onClick="jQuery.ns('namespaceId${idSuffix}').asyncOnclick" onLoad="asyncExpandnode">
            </ces:tree>
        </ces:layoutRegion>
        <ces:layoutRegion region="center">
            <div class="fill">
                <div class="toolbarsnav clearfix">
                    <ces:toolbar isOverflow="false"
                                 id="toolbarId${idSuffix}" onClick="$.ns('namespaceId${idSuffix}').toolbarClick"
                                 data="[{'label': '修改', 'id':'update', 'disabled': 'false','type': 'button'}]">
                    </ces:toolbar>
                </div>
                <!-- <button class="coral-button coral-component coral-state-default coral-corner-all coral-button-text-only" onClick="alert(1)"><span class="coral-button-text">保存</span></button> -->
                <ces:grid id="gridId${idSuffix}" multiselect="true" rownumbers="false" shrinkToFit="true"
                          forceFit="true" fitStyle="fill" model="grid" url="${gurl}">
                    <ces:gridCols>
                        <ces:gridCol name="ZHBH" edittype="text" width="100">门店编号</ces:gridCol>
                        <ces:gridCol name="QYMC" width="180">门店名称</ces:gridCol>
                        <ces:gridCol name="AUTH_ID" width="180" hidden="true">子节点</ces:gridCol>
                    </ces:gridCols>
                    <ces:gridPager gridId="gridId${idSuffix}"/>
                </ces:grid>
                <div id="hiddenDiv${idSuffix}" style="display: none;">
                    <div id="formDiv${idSuffix}">
                        <ces:toolbar
                                id="formToolbarId${idSuffix}" onClick="$.ns('namespaceId${idSuffix}').formToolbarClick"
                                isOverflow="true"
                                data="[{'label': '保存', 'id':'save', 'disabled': 'false','type': 'button'},{'label': '返回', 'id':'CFG_closeComponentZone', 'disabled': 'false','type': 'button'}]">
                        </ces:toolbar>

                        <div class="toolbarsnav clearfix">
                        </div>
                        <ces:form id="form${idSuffix}" name="form" action="/qyptmdgl!saveQyht.json">
                            <input id="id${idSuffix}" name="id" type="hidden"/>
                            <input id="categoryId${idSuffix}" name="categoryId" type="hidden"/>

                            <div>
                                <table style="border-collapse:separate;
                                        border-spacing:10px 10px;">
                                    <tr>
                                        <td width="100" align="right"><label>账户编号:</label></td>
                                        <td><ces:input id="ZHBH_${idSuffix}" name="ZHBH" width="200"
                                                       readonly="true"/></td>
                                        <td width="100" align="right"><label>企业名称:</label></td>
                                        <td><ces:input id="QYMC_${idSuffix}" name="QYMC" width="200"
                                                       readonly="true"/></td>
                                    </tr>
                                    <tr>
                                        <td width="100" align="right"><label>营业执照:</label></td>
                                        <td><ces:input id="YYZZ_${idSuffix}" name="YYZZ" width="200"
                                                       /></td>
                                        <td width="100" align="right"><label>联系人:</label></td>
                                        <td><ces:input id="LXR_${idSuffix}" name="LXR" width="200"
                                                       /></td>
                                    </tr>

                                    <tr>
                                        <td width="100" align="right"><label>邮箱:</label></td>
                                        <td><ces:input id="YX_${idSuffix}" name="YX" width="200" /></td>
                                        <td width="100" align="right"><label>手机:</label></td>
                                        <td><ces:input id="SJ_${idSuffix}" name="SJ" width="200" pattern='//^((13[0-9]|15[012356789]|17[678]|18[0-9]|14[57])[0-9]{8})$//' /></td>
                                    </tr>
                                    <tr>
                                        <td width="100" align="right"><label>座机:</label></td>
                                        <td><ces:input id="ZJ_${idSuffix}" name="ZJ" width="200" pattern='//^((0[0-9]{2,3}\-)?([2-9][0-9]{6,7})+(\-[0-9]{1,4})?)$//'/></td>
                                        <td width="100" align="right"><label>传真:</label></td>
                                        <td><ces:input id="CZ_${idSuffix}" name="CZ" width="200" pattern='//^([0-9]{3,4}-)?[0-9]{7,8}$//' /></td>
                                    </tr>
                                    <tr>
                                        <td width="100" align="right"><label>收信手机:</label></td>
                                        <td><ces:input id="SXSJ_${idSuffix}" name="SXSJ" width="200" pattern='//^((13[0-9]|15[012356789]|17[678]|18[0-9]|14[57])[0-9]{8})$//'
                                                       /></td>
                                        <td width="100" align="right"><label>地址:</label></td>
                                        <td><ces:input id="DZ_${idSuffix}" name="DZ" width="200" /></td>
                                    </tr>
                                    <tr>
                                        <td width="100" align="right"><label>经度:</label></td>
                                        <td><ces:input id="JD_${idSuffix}" name="JD" required="true" width="200"/></td>
                                        <td width="100" align="right"><label>纬度:</label></td>
                                        <td><ces:input id="WD_${idSuffix}" name="WD" required="true" width="200"/></td>
                                    </tr>
                                    <tr>
                                        <td width="100" align="right"><label>城市信息:</label></td>
                                        <td><input id="CSXX_${idSuffix}" name="CSXX" required="true" width="200"/> </td>
                                        <td width="100" align="right" ><label>系统类型:</label></td>
                                        <td ><ces:combobox  id="XTLX_${idSuffix}" name="XTLX" required="true"
                                                                       width="200"/></td>
                                        <td width="100" align="right" hidden = "true"><label>城市名称:</label></td>
                                        <td hidden = "true"><ces:input id="CSMC_${idSuffix}" name="CSMC" required="true"
                                                       width="200"/></td>
                                    </tr>
                                </table>
                            </div>
                        </ces:form>
                    </div>
                </div>
            </div>
        </ces:layoutRegion>
    </ces:layout>
</div>
<script type="text/javascript">
    $.extend($.ns("namespaceId${idSuffix}"), {
        csxxClick: function () {
            CFG_clickButtonOrTreeNode($('#max${idSuffix}').data('configInfo'), "csxx", "城市信息", 2, $.ns("namespaceId${idSuffix}"));
        },
        setComboGridValue_Csxx: function (o) {
            if (null == o) return;
            var rowData = o.rowData;
            if (null == rowData) return;
            $("#CSXX_${idSuffix}").combogrid("setValue", rowData.CDBM);
            $("#CSMC_${idSuffix}").textbox("setValue",rowData.CDMC);
        },
        currentTreeNodeId: '',
        asyncOnclick: function (e, treeId, treeNode) {
            if (treeNode.name == "TreeNodeReserve") {
                alert(1);
                CFG_clickButtonOrTreeNode($('#max${idSuffix}').data('configInfo'), "TreeNodeReserve", "树节点预留区", "3", $.ns("namespaceId${idSuffix}"));
                return;
            }
            if (treeNode.name == "TreeNodeReserve2") {
                alert(1);
                CFG_clickButtonOrTreeNode($('#max${idSuffix}').data('configInfo'), "TreeNodeReserve2", "树节点预留区2", "3", $.ns("namespaceId${idSuffix}"));
                return;
            }
            CFG_clearComponentZone($('#max${idSuffix}').data('configInfo'));
            //alert($.ns("namespaceId${idSuffix}").currentTreeNodeId);
            $.ns("namespaceId${idSuffix}").currentTreeNodeId = treeNode.id;
            //自定义url 获得指定的列表数据
            //$('#gridId${idSuffix}').grid('option', 'url', "${gurl}?id=" + treeNode.id);

            $('#gridId${idSuffix}').grid('option', 'url', "${gurl}?auth_parent_id=" + $.ns("namespaceId${idSuffix}").currentTreeNodeId);
            $('#gridId${idSuffix}').grid('reload');
        },
        toolbarClick: function (event, button) {
            //alert(button.id);
            if (button.id == "update") {
                var gGrid = $("#gridId${idSuffix}");
                var ids = gGrid.grid("option", "selarrrow");

                if (ids.length == 0) {
                    CFG_message("请至少选择一条记录");
                    return;
                } else if (ids.length > 1) {
                    CFG_message("请选择一条记录");
                    return;
                } else {

                    var div = CFG_getEmbeddedDiv($('#max${idSuffix}').data('configInfo'), "", "修改", function () {
                        $('#hiddenDiv${idSuffix}').append($('#formDiv${idSuffix}'));
                    });

                    rowData = gGrid.grid("getRowData", ids.toString());
                    url = $.contextPath + "/qyptmdgl!initForm.json?auth_parent_id=" + $.ns("namespaceId${idSuffix}").currentTreeNodeId + "&auth_id=" + rowData.AUTH_ID;
                    $.ajax({

                        type: "post",
                        url: url,
                        data: {
                            ZHBH: $("#ZHBH_${idSuffix}").val(),
                            QYMC: $("#QYMC_${idSuffix}").val(),
                            YYZZ: $("#YYZZ_${idSuffix}").val(),
                            LXR: $("#LXR_${idSuffix}").val(),
                            YX: $("#YX_${idSuffix}").val(),
                            SJ: $("#SJ_${idSuffix}").val(),
                            ZJ: $("#ZJ_${idSuffix}").val(),
                            CZ: $("#CZ_${idSuffix}").val(),
                            SXSJ: $("#SXSJ_${idSuffix}").val(),
                            DZ: $("#DZ_${idSuffix}").val(),
                            JD: $("#JD_${idSuffix}").val(),
                            WD: $("#WD_${idSuffix}").val(),
                            CSXX: $("#CSXX_${idSuffix}").val()
                        },
                        dataType: 'json',
                        success: function (res) {
                            $("#ZHBH_${idSuffix}").textbox("setValue", res.ZHBH);
                            $("#QYMC_${idSuffix}").textbox("setValue", res.QYMC);
                            $("#YYZZ_${idSuffix}").textbox("setValue", res.YYZZ);
                            $("#LXR_${idSuffix}").textbox("setValue", res.LXR);
                            $("#YX_${idSuffix}").textbox("setValue", res.YX);
                            $("#SJ_${idSuffix}").textbox("setValue", res.SJ);
                            $("#ZJ_${idSuffix}").textbox("setValue", res.ZJ);
                            $("#CZ_${idSuffix}").textbox("setValue", res.CZ);
                            $("#SXSJ_${idSuffix}").textbox("setValue", res.SXSJ);
                            $("#DZ_${idSuffix}").textbox("setValue", res.DZ);
                            $("#JD_${idSuffix}").textbox("setValue", res.JD);
                            $("#WD_${idSuffix}").textbox("setValue", res.WD);
                            $("#CSXX_${idSuffix}").combogrid("setValue", res.CSXX);
                            $("#XTLX_${idSuffix}").combobox("setValue", res.XTLX);

                        }
                    })

                    $("#CSXX_${idSuffix}").combogrid({
                        url: $.contextPath + "/cdxx!getCdxxGrid.json",
                        multiple: false,
                        sortable: true,
                        colModel: _colModel,
                        colNames: _colNames,
                        width: 200,
                        textField: "CDMC",
                        valueField: "CDBM",
                        panelWidth: "220",
                        height: "auto",
                        buttonOptions: buttonOpts
                    });
                    var jsonData = $.loadJson($.contextPath + "/qyptmdgl!getXtlxGrid.json");
                   // var datas=[{"text":"种植场","value":"1"},{"text":"养殖场","value":"2"},{"text":"蔬菜批发市场","value":"3"},{"text":"屠宰场","value":"4"},{"text":"猪肉批发市场","value":"5"},{"text":"团体采购","value":"6"},{"text":"超市","value":"7"},{"text":"零售市场","value":"8"},{"text":"餐饮","value":"9"},{"text":"加工","value":"10"}];
                    $("#XTLX_${idSuffix}").combobox("reload",jsonData);
                    $("#CSXX_${idSuffix}").combogrid("option","onChange",function(e,data){
                        $("#CSMC_${idSuffix}").textbox("setValue",data.newText);
                    });

                    //rowData.AUTH_ID ,rowData.QYMC
                    div.append($('#formDiv${idSuffix}'));
                    $(":coral-toolbar").toolbar("refresh");
                }
            }
        },
        formatName: function () {
            return "<a href='#'>aaaa</a>";
        },
        formToolbarClick: function (event, button) {


            // alert(button.id);
            if (button.id == "save") {
                //$('#dialog${idSuffix}').dialog('open');
                // url = this.getAction() + "qyptmdgl!saveQyht.json";
                // alert($.ns("namespaceId${idSuffix}").currentTreeNodeId +"   auth_id=" +rowData.AUTH_ID);
                //alert($("#CSXX_${idSuffix}").combogrid("getValue"))
                var pattern = /^([-]){0,1}([0-9]){1,}([.]){0,1}([0-9]){0,}$/;
                if (!$('#form${idSuffix}').form("valid")){CFG_message("请将数据填写完整！", "warning");return ;}else{
                if((!pattern.test($("#JD_${idSuffix}").val()))||(!pattern.test($("#WD_${idSuffix}").val()))){CFG_message("经纬度数据格式有误！", "warning");return;};
               // if(($("#JD_${idSuffix}").val()=='')||($("#WD_${idSuffix}").val()=='')||($("#CSXX_${idSuffix}").val()=='')){alert("请将信息填写完成！");return;};
                url = $.contextPath + "/qyptmdgl!saveQyht.json?auth_parent_id=" + $.ns("namespaceId${idSuffix}").currentTreeNodeId + "&auth_id=" + rowData.AUTH_ID;
                   // alert($("#XTLX_${idSuffix}").combobox("getValue"));

                $.ajax({
                    type: "post",
                    url: url,
                    data: {
                        //YYZZ,LXR,YX,SJ,ZJ,CZ,SXSJ,DZ,JD,WD,CSXX,XTLX
                        YYZZ: $("#YYZZ_${idSuffix}").val(),
                        LXR: $("#LXR_${idSuffix}").val(),
                        YX: $("#YX_${idSuffix}").val(),
                        SJ: $("#SJ_${idSuffix}").val(),
                        ZJ: $("#ZJ_${idSuffix}").val(),
                        CZ: $("#CZ_${idSuffix}").val(),
                        SXSJ: $("#SXSJ_${idSuffix}").val(),
                        DZ: $("#DZ_${idSuffix}").val(),
                        JD: $("#JD_${idSuffix}").val(),
                        WD: $("#WD_${idSuffix}").val(),
                        CSXX: $("#CSXX_${idSuffix}").combogrid("getValue"),
                        CSMC:$("#CSMC_${idSuffix}").val(),
                        XTLX:$("#XTLX_${idSuffix}").combobox("getValue")
                    },
                    dataType: 'json',
                    success: function (res) {
                        if (0 === res) {
                            CFG_message("操作失败！", "warning");
                            return;
                        } else {
                            CFG_message("操作成功！", "success");

                            gGrid.grid('reload', "${gurl}?auth_parent_id=" + $.ns("namespaceId${idSuffix}").currentTreeNodeId);
                        }
                    },
                    error: function () {
                        CFG_message("操作失败！", "error");
                    }
                });

            }} else if (button.id == "CFG_closeComponentZone") {
                if (window.CFG_clickReturnButton) {
                    CFG_clickReturnButton($('#max${idSuffix}').data('configInfo'));
                    $("#ZHBH_${idSuffix}").textbox("setValue", "");
                    $("#QYMC_${idSuffix}").textbox("setValue", '');
                    $("#YYZZ_${idSuffix}").textbox("setValue", '');
                    $("#LXR_${idSuffix}").textbox("setValue", '');
                    $("#YX_${idSuffix}").textbox("setValue", '');
                    $("#SJ_${idSuffix}").textbox("setValue", '');
                    $("#ZJ_${idSuffix}").textbox("setValue", '');
                    $("#CZ_${idSuffix}").textbox("setValue",'');
                    $("#SXSJ_${idSuffix}").textbox("setValue", '');
                    $("#DZ_${idSuffix}").textbox("setValue", '');
                    $("#JD_${idSuffix}").textbox("setValue", '');
                    $("#WD_${idSuffix}").textbox("setValue", '');
                    $("#CSXX_${idSuffix}").combogrid("setValue", '');
                    $("#XTLX_${idSuffix}").combobox("setValue", '');

                }
            }
            },
        /* 构件方法和回调函数 */
        getCategoryId: function () {
            alert("调用方法getCategoryId！");
            var o = {
                status: true
            };
            o.categoryId = '1';
            return o;
        },
        transferTo: function () {
            var o = {
                status: true
            };
            o.condition = 'c';
            o.params = 'inputParamId≡111;inputParamId1≡112';
            return o;
        },
        refreshGrid: function (o) {
            if (o) {
                alert("调用回到函数refreshGrid(" + o.cbParam1 + ")");
            } else {
                alert("调用回到函数refreshGrid");
            }
        }
    });
    var buttonOpts = {
        id: "combogrid_buttonId",
        label: "构件按钮",
        icons: "icon-checkmark-circle",
        text: false,
        onClick: $.ns('namespaceId${idSuffix}').csxxClick
    };
    var _colNames = ["城市编号", "城市名称"];
    var _colModel = [{name: "CDBM", width: 65, sortable: true}, {name: "CDMC", width: 155, sortable: true}];
    //var _colNames_ = [ "系统类型"];
    //var _colModel_ = [ {name: "NAME", width: 155, sortable: true}];

    var rowData;
    $(function () {
        var configInfo = CFG_initConfigInfo({
            /** 页面名称 */
            'page': 'qyptmdgl.jsp',
            /** 页面中的最大元素 */
            'maxEleInPage': $('#max${idSuffix}'),
            /** 获取构件嵌入的区域 */
            'getEmbeddedZone': function () {
                return $('#layoutId${idSuffix}').layout('panel', 'center');
            },
            <%--/** 初始化预留区 */--%>
            <%--'initReserveZones': function (configInfo) {--%>
                <%--CFG_addToolbarButtons(configInfo, $('#toolbarId${idSuffix}'), 'toolBarReserve', $('#toolbarId${idSuffix}').toolbar("getLength") - 1);--%>
            <%--},--%>
            <%--/** 获取返回按钮添加的位置 */--%>
            <%--'setReturnButton': function (configInfo) {--%>
                <%--CFG_setReturnButton(configInfo, $('#toolbarId${idSuffix}'));--%>
            <%--},--%>
            /** 页面初始化的方法 */
            'bodyOnLoad': function (configInfo) {
                var $tree = $("#treeId${idSuffix}");
                var nodes = $tree.tree("getNodes");
                for (var i in nodes) {

                    if (nodes[i].name == "企业门店管理") {
                        $tree.tree("expandNode",nodes[i]);
                        <%--CFG_clickButtonOrTreeNode($('#max${idSuffix}').data('configInfo'), "mdgl", nodes[i].name, "3", $.ns("namespaceId${idSuffix}"));--%>
                        <%--CFG_clickButtonOrTreeNode($('#max${idSuffix}').data('configInfo'), "TreeNodeReserve", "树节点预留区", "3", $.ns("namespaceId${idSuffix}"));--%>
                        $.ns("namespaceId${idSuffix}").currentTreeNodeId = nodes[i].id;
                        //自定义url 获得指定的列表数据
                        //$('#gridId${idSuffix}').grid('option', 'url', "${gurl}?id=" + treeNode.id);

                        CFG_clearComponentZone($('#max${idSuffix}').data('configInfo'));
                        $('#gridId${idSuffix}').grid('option', 'url', "${gurl}?auth_parent_id=" + $.ns("namespaceId${idSuffix}").currentTreeNodeId);
                        $('#gridId${idSuffix}').grid('reload');
                    }
                }
                // 按钮权限控制
                //alert(configInfo.notAuthorityComponentButtons);
                if (configInfo.notAuthorityComponentButtons) {
                    //$("toolbar").toolbar("refresh");
                    $.each(configInfo.notAuthorityComponentButtons, function (i, v) {
                        if (v == 'add') {
                            $('#toolbarId${idSuffix}').toolbar('disableItem', 'add');
                            //$('#toolbarId${idSuffix}').toolbar('hide', 'add');
                        } else if (v == 'update') {
                            //$('#toolbarId${idSuffix}').toolbar('disableItem', 'update');
                            //$('#toolbarId${idSuffix}').toolbar('hide', 'update');
                        } else if (v == 'delete') {
                            //$('#toolbarId${idSuffix}').toolbar('disableItem', 'delete');
                            $('#toolbarId${idSuffix}').toolbar('hide', 'delete');
                        }
                    });
                }
            }
        });
//        var systemParam1 = CFG_getSystemParamValue(configInfo, 'systemParam1'); // 获取构件系统参数
//        var selfParam1 = CFG_getSelfParamValue(configInfo, 'selfParam1'); // 获取构件自身参数
//        var inputParam1 = CFG_getInputParamValue(configInfo, 'inputParamName_1'); // 获取构件输入参数
//        //alert(inputParam1);
//        configInfo.CFG_outputParams.xxx = ''; // 设置输出参数
    });
</script>
