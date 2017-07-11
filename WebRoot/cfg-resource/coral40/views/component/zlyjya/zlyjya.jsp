<%@ page import="com.ces.config.utils.CommonUtil" %>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" %>
<%@ taglib prefix="ces" tagdir="/WEB-INF/tags" %>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
    request.setAttribute("gurl", "");
    request.setAttribute("turl", "");
    request.setAttribute("idSuffix", CommonUtil.generateUIId(""));
%>
<style>
#search{
	margin-top:0px; 
}
.coral-tabs .coral-tabs-panel {
    border-top : #2ac79f solid 1px;
}
</style>
<div id="max${idSuffix}" class="fill">
    <div class="fill" >
        <%--<div class="toolbarsnav clearfix">--%>
        <%--<ces:toolbar id="toolbarId${idSuffix}" onClick="$.ns('namespaceId${idSuffix}').toolbarClick"--%>
        <%--data="[{'label': '预案管理', 'id':'manage', 'disabled': 'false','type': 'button'}]">--%>
        <%--</ces:toolbar>--%>
        <%--</div>--%>
        <div class="toolbarsnav clearfix" style="margin-top:15px">
            <ces:toolbar id="toolbarId2${idSuffix}" onClick="$.ns('namespaceId${idSuffix}').toolbarClick"  
                         data="['->',{'label': '查询', 'id':'search', 'disabled': 'false','type': 'button'}]" >
            </ces:toolbar>
        </div>
        <form id="csForm${idSuffix}" action="zzcsgl!saveCsxx.json"
              enctype="multipart/form-data" method="post" class="coralui-form" style="padding-top:20px ">
            <div class="app-inputdiv4" style="height:50px;display: none">
            </div>

            <div class="fillwidth colspan3 clearfix">
                <!------------------ 第一排开始---------------->
                <div class="app-inputdiv4">
                    <label class="app-input-label">产品名称：</label>
                    <input id="CPMC${idSuffix}" name="CPMC" class="coralui-textbox"/>
                </div>


                <div class="app-inputdiv4">
                    <label class="app-input-label">批次号：</label>
                    <input id="PCH${idSuffix}" name="PCH" id="CPPC${idSuffix}" data-options="" class="coralui-textbox"/>

                </div>
                <!------------------ 第一排结束---------------->

                <!------------------ 第二排开始---------------->
                <div class="app-inputdiv4">
                    <label class="app-input-label">产品追溯码：</label>
                    <input id="CPZSM${idSuffix}" name="CPZSM" data-options="" class="coralui-textbox"/>
                </div>


                <div class="app-inputdiv12">
                    <label class="app-input-label">预案选择：</label>
                    <ces:radiolist id="dadio${idSuffix}" url="zlyjya!loadRadio.json" textField="YAMC" valueField="YABH"
                                   repeatLayout="flow" itemWidth="auto"></ces:radiolist>
                </div>
            </div>
        </form>
<%--         <div class="toolbarsnav clearfix" style="height:24px">
            <ces:toolbar id="toolbarId${idSuffix}" onClick="$.ns('namespaceId${idSuffix}').toolbarClick" align="right"  style="height:22px" 
                         data="[{'label': '预案管理', 'id':'manage', 'disabled': 'false','type': 'button'},{'label': '导出', 'id':'export', 'disabled': 'false','type': 'button'}]">
            <a href="" id="yaexport${idSuffix}" style="display:hidden" ></a>             
            </ces:toolbar>
        </div> --%>


        <%--<ces:grid id="gridId${idSuffix}" afterInlineSaveRow="afterInlineSaveRow"--%>
        <%--shrinkToFit="true" forceFit="true" fitStyle="fill" rownumbers="true" datatype="local" width="auto">--%>
        <%--<ces:gridCols >--%>
        <%--</ces:gridCols>--%>
        <%--</ces:grid>--%>
        <ces:tabs id="tabs${idSuffix}" >
            <ul style="float:left">
                <li><a href="#fatherId${idSuffix}">数据分析</a></li>
                <li><a href="#chart${idSuffix}">数据报表</a></li>
            </ul>
            <div class="toolbarsnav clearfix" style="float:right" >
            <ces:toolbar id="toolbarId${idSuffix}" onClick="$.ns('namespaceId${idSuffix}').toolbarClick" align="right"  
                         data="[{'label': '预案管理', 'id':'manage', 'disabled': 'false','type': 'button'},{'label': '导出', 'id':'export', 'disabled': 'false','type': 'button'}]">
          	<a href="" id="yaexport${idSuffix}" style="display:hidden" ></a>             
            </ces:toolbar>
        	</div>
            <div id="fatherId${idSuffix}" style="height: 600px;clear:both"></div>
            <div id="chart${idSuffix}"  style = "height:450px;clear:both">
                <div id="yyxxChart${idSuffix}" style = "width:48%;float:left;margin-top: 20px">

                </div>
                <div id="xsqxChart${idSuffix}" style = "width:48%;float:left;margin-top: 20px">

                </div>
            </div>
        </ces:tabs>
        <%--<div id="jqUC" style="display: none"></div>--%>
    </div>
</div>
<script>
    var radiolistdata = $.loadJson($.contextPath + "/zlyjya!loadRadio.json");
    var localgrid;
    var tableHeader;
    var yalx;
    //var radiodata=$.loadJson($.contextPath+"/zlyjya!loadRadio.json");
    // $("#dadio${idSuffix}").radiolist({'textField':'YAMC',
    // 'valueField':'YABH',
    // "data":radiodata,

    //});
    //url="zlyjya!loadRadio"


    $("#button${idSuffix}").button();
    function loadColumns(yabh) {
        var jqBorder = $("#fatherId${idSuffix}");
        jqBorder.empty();
//        $.coral.adjusted(jqBorder); //此处会导致jqBorder的heigh缩小
        var $grid = $("<div id='gridId${idSuffix}'><div class='gridId${idSuffix}'></div></div>").appendTo(jqBorder);
        localgrid = $grid;
        var jsondata = $.loadJson($.contextPath + "/zlyjya!getColdata.json?yabh=" + yabh);
        var yalxdata = $.loadJson($.contextPath + "/zlyjya!getYalx.json?yabh=" + yabh);
        yalx = yalxdata.YALX;
        //var length=jsondata.length;
        var _colModel = new Array();
        var _colNames = new Array();
        var _colNamesvalue;
        var _colModelvalue;
        var listForGrid = [];
        var listForGrid = new Array();
        for (var i = 0; i < jsondata.length; i++) {
            listForGrid.push(jsondata[i].ZDMC);
            _colModel.push({name: jsondata[i].ZDMC});
            _colNames.push(jsondata[i].XSMC);
        }
        tableHeader = listForGrid;

        var _setting = {
            width: '150px',
            height: 'auto',
            //shrinkToFit: "true",
            forceFit: "true",
            fitStyle: "fill",
            datatype: "local",
            multiselect: true,
            colModel: _colModel,
            colNames: _colNames
        };
        $grid.grid(_setting);

        // var gridData=$.loadJson($.contextPath+"/zlyjya!getGriddata.json?data="+listForGrid.toString());
//    $grid.grid("option","datatype","json");
//    //$grid.grid({"data":gridData.data});
//    $grid.grid({"reload":gridData.data})
        $grid.grid("option", "datatype", "json");
        $grid.grid("option", "url", $.contextPath + "/zlyjya!getGriddata.json?data=" + listForGrid.toString() + "&yalx=" + yalx);
        $grid.grid("reload");
    }

    //点击查询后进行过滤
    function searchGridInfo() {

        var formdata = $("#csForm${idSuffix}").form("formData", false);
        //var searchData= $.loadJson($.contextPath+"/zlyjya!searchGridInfo.json?CPMC="+formdata.CPMC+"&PCH="+formdata.PCH+"&CPZSM="+formdata.CPZSM);
        localgrid.grid("option", "datatype", "json");
        localgrid.grid("option", "url", $.contextPath + "/zlyjya!searchGridInfo.json?CPMC=" + formdata.CPMC + "&PCH=" + formdata.PCH + "&CPZSM=" + formdata.CPZSM + "&yalx=" + yalx);
        localgrid.grid("reload");
        <%--$("#CPMC${idSuffix}").textbox('getValue');--%>
        <%--$("#CPPC${idSuffix}").textbox('getValue');--%>
        <%--$("#ZSH${idSuffix}").textbox('getValue')--%>
        var sad;
//    $.ajax({
//      type:'post',
//
//    })
    }

    function exportDrid() {

        var i = tableHeader;

        var iddata = localgrid.grid("option", 'selarrrow');

        if (iddata.length == 0) {
            CFG_message("请选择导出的行！", "error");
            ;
            return false;
        }
        ;
        var rowdata = localgrid.grid('getRowData', iddata[0]);
        var yabh = $('#dadio${idSuffix}').radiolist("getValue");
        var excelArray = new Array();
        //var jsondata= $.loadJson($.contextPath+"/zlyjya!getColdata.json?yabh="+yabh);
        var searchData = $.loadJson($.contextPath + "/zlyjya!exportExcel.json?ids=" + iddata.toString() + "&tableHeader=" + tableHeader.toString() + "&yabh=" + yabh + "&yalx=" + yalx);
        var root = document.getElementById("yaexport${idSuffix}");
        root.href=$.contextPath+"/spzstpfj/exportTable.xls";
        root.click();
    }

    $("#dadio${idSuffix}").radiolist({
        "onChange": function (event, ui) {
            var value = $("#dadio${idSuffix}").radiolist("getValue");
            loadColumns(value);
            $("#CPMC${idSuffix}").textbox('setValue', '');
            $("#PCH${idSuffix}").textbox('setValue', '');
            $("#CPZSM${idSuffix}").textbox('setValue', '');
        }
    })

    $.extend($.ns("namespaceId${idSuffix}"), {
        refresh: function (o) {

        },
        cdmcClick: function () {

            CFG_clickButtonOrTreeNode($('#max${idSuffix}').data('configInfo'), "zlyjyaxz", "应急预案管理", 2, $.ns("namespaceId${idSuffix}"));
        },
        setComboGridValue_Cdmc: function (o) {
            if (null == o) return;
            var rowData = o.rowData;
            if (null == rowData) return;
            var zzcdbm = $("#zzcdbm${idSuffix}");
            var yzcdbm = $("#yzcdbm${idSuffix}");

            $("#cdmc${idSuffix}").textbox("setValue", rowData.CDMC);
            $("#cdbm${idSuffix}").combogrid("setValue", rowData.CDBM);
//            $("input[name='cdmc']").combogrid("setText",rowData.CDMC);
        },

        toolbarClick: function (event, button) {
            if (button.id == "manage") {
                $.ns("namespaceId${idSuffix}").cdmcClick();
            } else if (button.id == "export") {
                exportDrid();
            } else if (button.id == "search") {
                searchGridInfo()
            }
        }
    });

    if (radiolistdata.length != 0) {
        $('#dadio${idSuffix}').radiolist("setValue", radiolistdata[0].YABH);
        loadColumns(radiolistdata[0].YABH);
    }
    //$('#dadio${idSuffix}').radiolist({"value":radiolistdata[0].YABH});

    $.parseDone(function () {
        var tab = $("#tabs${idSuffix}");
        tab.tabs("option","onActivate",function(e,ui){
            if (ui.newPanel.attr("id") == "chart${idSuffix}") {
                $("#toolbarId${idSuffix}").hide();
            } else {
                $("#toolbarId${idSuffix}").show();
            }
        });

        var configInfo = CFG_initConfigInfo({
            /** 页面名称 */
            'page': 'zlyjya.jsp',
            /** 页面中的最大元素 */
            'maxEleInPage': $('#max${idSuffix}'),
            /** 获取构件嵌入的区域 */
            'getEmbeddedZone': function () {
                return $('#max${idSuffix}');
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
                var yyxx = $.loadJson($.contextPath + '/zlyjya!getYyChartData.json');
                var xsqxxx = $.loadJson($.contextPath + '/zlyjya!getXsqxChartData.json');
                var yyxxSeries = [];
                var xsqxSeries = [];
                $.each(yyxx,function(index){
                    yyxxSeries.push([yyxx[index].TRPMC== null?"unknown" : yyxx[index].TRPMC,   yyxx[index].YL== null?0 : parseFloat(yyxx[index].YL)]);
                });
                $.each(xsqxxx,function(index){
                    xsqxSeries.push([xsqxxx[index].KHMC == null ?"unknown" : xsqxxx[index].KHMC,xsqxxx[index].SL == null ?0 : parseFloat(xsqxxx[index].SL)]);
                });
                $('#yyxxChart${idSuffix}').highcharts({
                    chart: {
                        plotBackgroundColor : null,
                        plotBorderWidth : null,
                        plotShadow : false
                    },
                    title: {text: '农作物用药信息'},
                    credits: {//版权显示
                        enabled: false
                    },
                    tooltip: {pointFormat: '{series.name}: <b>{point.percentage:.1f}%</b>'},
                    plotOptions: {
                        pie: {
                            allowPointSelect: true,
                            cursor: 'pointer',
                            dataLabels: {
                                enabled: true,
                                color: '#000000',
                                connectorColor: '#000000',
                                format: '<b>{point.name}</b>: {point.percentage:.1f} %'
                            }
                        }
                    },
                    series: [{
                        type: 'pie',
                        name: '用药比例',
                        data: yyxxSeries
//                        data: [['Firefox', 45.0], ['IE', 26.8], {
//                            name: 'Chrome',
//                            y: 12.8,
//                            sliced: true,
//                            selected: true
//                        }, ['Safari', 8.5], ['Opera', 6.2], ['Others', 0.7]]
                    }]
                });

                $('#xsqxChart${idSuffix}').highcharts({
                    chart: {
                        plotBackgroundColor: null,
                        plotBorderWidth: null,
                        plotShadow: false
                    },
                    title: {text: '销售去向信息'},
                    tooltip: {pointFormat: '{series.name}: <b>{point.percentage:.1f}%</b>'},
                    plotOptions: {
                        pie: {
                            allowPointSelect: true,
                            cursor: 'pointer',
                            dataLabels: {
                                enabled: true,
                                color: '#000000',
                                connectorColor: '#000000',
                                format: '<b>{point.name}</b>: {point.percentage:.1f} %'
                            }
                        }
                    },
                    series: [{
                        type: 'pie',
                        name: '去向比例',
                        data: xsqxSeries
//                        data: [['Firefox', 45.0], ['IE', 26.8], {
//                            name: 'Chrome',
//                            y: 12.8,
//                            sliced: true,
//                            selected: true
//                        }, ['Safari', 8.5], ['Opera', 6.2], ['Others', 0.7]]
                    }],
                    credits: {//版权显示
                        enabled: false
                    }
                });

            }
        });
    });

</script>