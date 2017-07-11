<%@ page import="com.ces.config.utils.CommonUtil" %>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" %>
<%@ taglib prefix="ces" tagdir="/WEB-INF/tags" %>
<%
    String path = request.getContextPath();
    String resourceFolder = path + "/cfg-resource/coral40/common";
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
    request.setAttribute("idSuffix", CommonUtil.generateUIId(""));
%>
<div id="max${idSuffix}" class="fill">
    <ces:toolbar onClick="$.ns('namespaceId${idSuffix}').toolbarClick"
                 data="[{'label': '打印', 'id':'print', 'disabled': 'false','type': 'button'}]">
    </ces:toolbar>
    <%--<ces:panel title="散货" width="800" height="200" componentCls="custom-panel" collapsible="true"--%>
    <%--collapsed="false">--%>
    <div id="shGridDiv${idSuffix}" style="height: 80%;">
        <ces:grid id="shGrid${idSuffix}" height="auto" rownumbers="true" fitStyle="fill"
                  cellEdit="true">
            <ces:gridCols>
                <ces:gridCol name="PZMC" width="160">品种名称</ces:gridCol>
                <ces:gridCol name="CPZSM" width="160">追溯码</ces:gridCol>
                <ces:gridCol name="DYZS" formatter="text"
                             formatoptions="validType: 'naturalnumber'"
                             sorttype="naturalnumber"
                             width="80">打印张数</ces:gridCol>
            </ces:gridCols>
        </ces:grid>
    </div>
    <%--</ces:panel>--%>
    <%--<ces:panel title="包装产品" width="800" height="200" componentCls="custom-panel" collapsible="true"--%>
    <%--collapsed="false">--%>
    <%--<div id="bzGridDiv${idSuffix}" style="height: 250px;">--%>
    <%--<ces:grid id="bzGrid${idSuffix}" height="auto" rownumbers="true" fitStyle="fill"--%>
    <%--cellEdit="true">--%>
    <%--<ces:gridCols>--%>
    <%--<ces:gridCol name="ID" hidden="true">ID</ces:gridCol>--%>
    <%--<ces:gridCol name="CPMC" width="80">产品名称</ces:gridCol>--%>
    <%--<ces:gridCol name="ZSM" width="80">追溯码</ces:gridCol>--%>
    <%--<ces:gridCol name="DYZS" formatter="textbox" formatoptions="required:true"--%>
    <%--width="80">打印张数</ces:gridCol>--%>
    <%--</ces:gridCols>--%>
    <%--</ces:grid>--%>
    <%--</div>--%>
    <%--</ces:panel>--%>
</div>
<script type="text/javascript">
    $.extend($.ns("namespaceId${idSuffix}"), {
        toolbarClick: function (e, data) {
            if (data.id == "print") {
                if (isSwt) {
                    var $shGrid = $("#shGrid${idSuffix}");
                    var gridData = $shGrid.grid("getRowData");
                    var ids = $shGrid.grid("getDataIDs");
                    for (var index in ids) {
                        if (!$shGrid.grid("valid", ids[index])) {
                            CFG_message("请输入正确的数字", "warning");
                            return;
                        }
                    }
//                    var qymc = $.loadJson($.contextPath + '');
                    for (var i in gridData) {
                        var dyzs = parseInt(gridData[i].DYZS==""?0:gridData[i].DYZS);
                        for (var j = 0; j < parseInt(gridData[i].DYZS); j++) {
                            var data = {
                                pzmc : gridData[i].PZMC,
                                cpzsm : gridData[i].CPZSM,
                                qymc : "海岛田原"
                            }
                            var result = _window("printSczzCcsh", JSON.stringify(data));
                            var resultData = JSON.parse(result);
                            if (resultData.status == "true" || resultData.status == true) {
                                CFG_message("打印成功!", "success");
                            } else {
                                CFG_message("打印失败：" + resultData.msg, "error");
                                return false;
                            }
                        }
                    }
                } else {
                    CFG_message("请在程序中操作", "warning");
                }
            }
        },
        initData: function () {
            var ccid = CFG_getInputParamValue($('#max${idSuffix}').data('configInfo'), "ccid");
            var gridData = $.loadJson($.contextPath + '/zzccgl!getCcxx.json?ccid=' + ccid);
            var shxx = gridData.shxx;
//            var bzcpxx = gridData.bzcpxx;
            var $shGrid = $("#shGrid${idSuffix}");
            <%--var $bzGrid = $("#bzGrid${idSuffix}");--%>
            for (var index in shxx) {
                var tempId = generateId("temp");
                $shGrid.grid("addRowData", tempId, {PZMC: shxx[index].PZ, CPZSM: shxx[index].CPZSM}, "last");
            }
//            for (var index in bzcpxx) {
//                $bzGrid.grid();
//            }
        }
    });
    $(function () {
        var configInfo = CFG_initConfigInfo({
            /** 页面名称 */
            'page': 'zzcczsmdy.jsp',
            /** 页面中的最大元素 */
            'maxEleInPage': $('#max${idSuffix}'),
            /** 获取构件嵌入的区域 */
            'getEmbeddedZone': function () {
                <%--return $('#layoutId${idSuffix}').layout('panel', 'center');--%>
            },
            'setReturnButton': function (configInfo) {
                <%--CFG_setReturnButton(configInfo, $('#toolbarId${idSuffix}'));--%>
            },
            /** 页面初始化的方法 */
            'bodyOnLoad': function (configInfo) {
                $.ns("namespaceId${idSuffix}").initData();

            }
        });
    });
</script>