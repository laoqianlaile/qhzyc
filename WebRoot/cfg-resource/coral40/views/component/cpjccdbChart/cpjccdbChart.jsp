<%@ page import="com.ces.config.utils.CommonUtil" %>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" %>
<%@ taglib prefix="ces" tagdir="/WEB-INF/tags" %>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://"
            + request.getServerName() + ":" + request.getServerPort()
            + path + "/";
    request.setAttribute("idSuffix", CommonUtil.generateUIId(""));
%>
<div id="maxDiv${idSuffix}" class="fill">
    <div id="container${idSuffix}" style="min-width:700px;height:400px"></div>
</div>

<script type="text/javascript">
    $(function () {
        var configInfo = CFG_initConfigInfo({
            /** 页面名称 */
            'page': 'prjcccdbChart.jsp',
            /** 页面中的最大元素 */
            'maxEleInPage': $('#maxDiv${idSuffix}'),
            /** 获取构件嵌入的区域 */
            'getEmbeddedZone': function () {
                return $('#maxDiv${idSuffix}');
            },
            /** 初始化预留区 */
            <%--'initReserveZones' : function(configInfo) {--%>
            <%--CFG_addToolbarButtons(configInfo, $('#toolbarId${idSuffix}'), 'cdmc', $('#toolbarId${idSuffix}').toolbar("getLength")-1);--%>
            <%--},--%>
            /** 获取返回按钮添加的位置 */
            <%--'setReturnButton' : function(configInfo) {--%>
            <%--CFG_setReturnButton(configInfo, $('#toolbarId${idSuffix}'));--%>
            <%--},--%>
            /** 页面初始化的方法 */
            'bodyOnLoad': function (configInfo) {
                //alert("bodyOnLoad");
                // 按钮权限控制
                //alert(configInfo.notAuthorityComponentButtons);
                <%--if (configInfo.notAuthorityComponentButtons) {--%>
                <%--$.each(configInfo.notAuthorityComponentButtons, function (i, v) {--%>
                <%--if (v == 'add') {--%>
                <%--//$('#toolbarId${idSuffix}').toolbar('disableItem', 'add');--%>
                <%--$('#toolbarId${idSuffix}').toolbar('hide', 'add');--%>
                <%--} else if (v == 'update') {--%>
                <%--//$('#toolbarId${idSuffix}').toolbar('disableItem', 'update');--%>
                <%--$('#toolbarId${idSuffix}').toolbar('hide', 'update');--%>
                <%--} else if (v == 'delete') {--%>
                <%--//$('#toolbarId${idSuffix}').toolbar('disableItem', 'delete');--%>
                <%--$('#toolbarId${idSuffix}').toolbar('hide', 'delete');--%>
                <%--}--%>
                <%--});--%>
                <%--}--%>

            }
        });

        //入参 数据行id
        var inputParam = CFG_getInputParamValue(configInfo, 'in_param');
        var chartData = $.loadJson($.contextPath+'/csgl!getCpjccdbChartByweek.json?id='+inputParam);
        var pfscmc = "";
        if(chartData.length!=0){
            pfscmc = chartData[0].PFSCMC;
        }
        var categories = new Array();
        var jczlData = new Array();
        var cczlData = new Array();
        for(var i=0;i<chartData.length;i++){
            categories.push(chartData[i].RQ);
            jczlData.push(chartData[i].JCZL);
            cczlData.push(chartData[i].CCZL);
        }
        var series = [{
            name:'进场重量',
            data:jczlData
        },{
            name:'出场重量',
            data:cczlData
        }];
        $('#container${idSuffix}').highcharts({
            chart: {
                type: 'column'
            },
            title: {
                text: pfscmc + '一周进出场对比'
            },
            subtitle: {
                text: 'Source: @cesgroup'
            },
            xAxis: {
                categories: categories
            },
            yAxis: {
                min: 0,
                title: {
                    text: '重量 (斤)'
                }
            },
            tooltip: {
                headerFormat: '<span style="font-size:10px">{point.key}</span><table>',
                pointFormat: '<tr><td style="color:{series.color};padding:0">{series.name}: </td>' +
                '<td style="padding:0"><b>{point.y:.1f} mm</b></td></tr>',
                footerFormat: '</table>',
                shared: true,
                useHTML: true
            },
            plotOptions: {
                column: {
                    pointPadding: 0.2,
                    borderWidth: 0
                }
            },
            series: series
        });
    });
</script>