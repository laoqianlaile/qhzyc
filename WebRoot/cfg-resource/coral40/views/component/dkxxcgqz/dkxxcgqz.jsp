<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ page import="com.ces.config.utils.CommonUtil"%>
<%@ taglib prefix="ces" tagdir="/WEB-INF/tags"%>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
    String gurl = path + "/appmanage/column-define!search.json?E_frame_name=coral&E_model_name=jqgrid&F_in=columnName,showName";
    String turl = path + "/appmanage/tree-define!tree.json?E_frame_name=coral&E_model_name=tree&P_ISPARENT=child&F_in=name,id&P_OPEN=true&P_filterId=parentId&P_CHECKED=838386ae45acc0c60145acc412bb0003";
    request.setAttribute("idSuffix", CommonUtil.generateUIId(""));
%>
<div id="max${idSuffix}" class="fill">
    <ces:form id="form${idSuffix}" name="form">
        <div class="fillwidth colspan3 clearfix ">
            <div class="app-inputdiv4">
                    <%--<label class="app-input-label">日期类型：</label>--%>
                    <%--<input id="rqlx${idSuffix}" name="RQLX" data-options="required:true"/>--%>
            </div>
            <div class="app-inputdiv8">
                    <%--<label class="app-input-label">入库日期：</label>--%>
                    <%--<div style="width:35%;float: left;">--%>
                    <%--<input class="coralui-datepicker" id="kssj${idSuffix}" name="KSSJ" data-options="required:true"/>--%>
                    <%--</div>--%>
                <div style="float: left;margin-right: 7%">-</div>
                <div style="width:35%;float: left;">
                        <%--<input class="coralui-datepicker" startDateId="kssj${idSuffix}" id="jssj${idSuffix}"  name="JSSJ" data-options="required:true"/>--%>
                </div>
            </div>
        </div>
    </ces:form>
    <div class="fillwidth colspan3 clearfix ">
        <div class="toolbarsnav clearfix">
            <%--<ces:toolbar id="toolbarId${idSuffix}" onClick="$.ns('namespaceId${idSuffix}').toolbarClick" align="center"--%>
            <%--data="[{'label': '查询', 'id':'search', 'disabled': 'false','type': 'button'},{'label': '重置', 'id':'reset', 'disabled': 'false','type': 'button'}]">--%>
            <%--</ces:toolbar>--%>
        </div>

    </div>

    <div id="charts${idSuffix}" style="min-width:700px;">

    </div>

</div>
<script type="text/javascript">
    var process_data = [];
    var chart;
    $(function() {
        var configInfo = CFG_initConfigInfo({
            /** 页面名称 */
            'page' : 'dkxxcgqz.jsp',//入库量趋势分析页面
            /** 页面中的最大元素 */
            'maxEleInPage' : $('#max${idSuffix}'),
            /** 页面初始化的方法 */
            'bodyOnLoad' : function(configInfo) {
                var id = CFG_getInputParamValue(configInfo, "inputPrama");
                var jsondata = $.loadJson($.contextPath+'/sdzyczzdkxx!getDkxx.json?id='+id);
                var sbsbh_json =$.loadJson($.contextPath+'/sdzyczzdkxx!getSbsbh.json?cgqz='+jsondata.data[0].CGQZ);
                loadData(sbsbh_json);
                initCharts(process_data);
                chart = $('#charts${idSuffix}').highcharts();
                // add new series for the chart
//                setTimeout(function(){
//                    var series=chart.series;
//                    while(series.length > 0) {
//                        series[0].remove(false);
//                    }
//                    var xAxis = chart.xAxis[0].categories;//x轴数据
//                    while(xAxis.length > 0) {
//                        xAxis = [];
//                    }
//                    loadData(jsondata);
//                    var seriesData = process_data.HisData;//温湿度数据的拆分
//                    var TimeValue = [];
//                    var TempValue = [];
//                    var HumiValue = [];
//                    for(var i=0;i<seriesData.length;i++){
//                        // TimeValue.push(seriesData[i].TimeValue);
//                        xAxis.push(seriesData[i].TimeValue);
//                        TempValue.push(parseFloat(seriesData[i].TempValue));
//                        HumiValue.push(parseFloat(seriesData[i].HumiValue));
//                    };
//                    chart.addSeries({
//                        id:1,
//                        name: "湿度",
//                        type: 'column',
//                        yAxis: 1,
//                        data: HumiValue
//                    }, false);
//                    chart.addSeries({
//                        id:2,
//                        name: "温度",
//                        data: TempValue
//                    }, false);
//                    chart.redraw();
//                },2000);
            }
        });

    });
    /**获取数据**/
    function loadData(jsondata){
        var before = getNowFormatDate(120);
        var now = getNowFormatDate(0);
        $.ajax({
            type: "GET",
            url: "http://123.232.26.175:8080/0531yun/wsjc/Device/getDevHisData.do",
            data:"devKey="+ jsondata.data[0].XH +"&beginTime="+before+"&endTime="+now+"&userID=160808jyyyy&userPassword=160808jyyyy",
            async : false,
            success: function (data) {
                process_data = data;
//                initCharts(data);
                $.message({message: "获取数据成功", cls: "success"});
            }, error: function () {
                $.message({message: "获取数据失败，请重试或联系技术人员", cls: "danger"});
            }
        });
    }
    //初始化图标
    function initCharts(jsonData){
        var seriesData = jsonData.HisData;//温湿度数据的拆分
        var TimeValue = [];//时间
        var TempValue = [];//温度
        var HumiValue = [];//湿度
        for(var i=0;i<seriesData.length;i++){
            TimeValue.push(seriesData[i].TimeValue);
            TempValue.push(parseFloat(seriesData[i].TempValue));
            HumiValue.push(parseFloat(seriesData[i].HumiValue));
        }
        $('#charts${idSuffix}').highcharts({
            chart: {
                zoomType: 'xy'
            },
            title: {
                text: '温湿度采集'
            },
            subtitle: {
                text: ''
            },
            xAxis: [{
                categories: TimeValue
            }],
            yAxis: [{ // Primary yAxis
                labels: {
                    formatter: function() {
                        return this.value +'°C';
                    },
                    style: {
                        color: '#89A54E'
                    }
                },
                title: {
                    text: '温度',
                    style: {
                        color: '#89A54E'
                    }
                },
                opposite: true

            }, { // Secondary yAxis
                gridLineWidth: 0,
                title: {
                    text: '湿度',
                    style: {
                        color: '#4572A7'
                    }
                },
                labels: {
                    formatter: function() {
                        return this.value +' %RH';
                    },
                    style: {
                        color: '#4572A7'
                    }
                }
            }],
            credits: {
                enabled: false // remove high chart logo hyper-link
            },
            tooltip: {
                shared: false
            },
            legend: {
                layout: 'vertical',
                align: 'left',
                x: 120,
                verticalAlign: 'top',
                y: 80,
                floating: true,
                backgroundColor: '#FFFFFF'
            },
            series: [{
                name: '湿度',
                color: '#4572A7',
                type: 'column',
                yAxis: 1,
                data: HumiValue,
                tooltip: {
                    valueSuffix: ' %RH'
                }

            }, {
                name: '温度',
                color: '#89A54E',
                type: 'spline',
                data: TempValue,
                tooltip: {
                    valueSuffix: ' °C'
                }
            }]
        });
    }
    //获取当前时间，格式YYYYMMDDHHmm
    function getNowFormatDate(da) {
        var date = new Date();
        var year = date.getFullYear();
        var month = date.getMonth() + 1;
        var strDate = date.getDate();
        var hour = date.getHours();
        var minute = date.getMinutes();
        if(da >60 ){
            minute = (da%60) >= minute?60-(da%60-minute):minute-(da%60);
            hour = hour - parseInt(da/60)-1;
        } else if(da> minute){
            minute = 60 -(da-minute);
            hour = hour -1;
        }
        if (month >= 1 && month <= 9) {
            month = "0" + month;
        }
        if (strDate >= 0 && strDate <= 9) {
            strDate = "0" + strDate;
        }
        if(hour >=0 && hour <= 9){
            hour = "0"+hour;
        }
        if(minute >= 0 && minute<=9){
            minute = "0"+minute;
        }
        var currentdate = year  + month  + strDate + hour + minute;
        return currentdate;
    }


</script>
