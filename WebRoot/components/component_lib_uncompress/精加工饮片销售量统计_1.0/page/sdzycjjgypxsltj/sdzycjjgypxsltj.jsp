<%@ page import="com.ces.config.utils.CommonUtil"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="ces" tagdir="/WEB-INF/tags"%>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
    request.setAttribute("idSuffix", CommonUtil.generateUIId(""));
%>
<style>
    .coralui-datepicker{
        width: 100px;
    }
    .toolbarsnav,.coral-toolbar-border{
        background: none;
        height: 58px;
    }
</style>
<div id="max${idSuffix}" class="fill">
    <ces:form id="form${idSuffix}" name="form">
        <div class="fillwidth colspan3 clearfix ">
            <div class="app-inputdiv4">
                <label class="app-input-label">日期类型：</label>
                <input id="rqlx${idSuffix}" name="RQLX"/>
            </div>
            <div class="app-inputdiv8">
                <label class="app-input-label">交易日期：</label>
                <div style="width:35%;float: left;">
                    <input class="coralui-datepicker" id="kssj${idSuffix}" name="KSSJ"/>
                </div>
                <div style="float: left;margin-right: 7%">-</div>
                <div style="width:35%;float: left;">
                    <input class="coralui-datepicker" startDateId="kssj${idSuffix}" id="jssj${idSuffix}" name="JSSJ"/>
                </div>
            </div>

        </div>
    </ces:form>
    <div class="fillwidth colspan3 clearfix ">
        <div class="toolbarsnav clearfix">
            <ces:toolbar id="toolbarId${idSuffix}" onClick="$.ns('namespaceId${idSuffix}').toolbarClick" align="center"
                         data="[{'label': '查询', 'id':'search', 'disabled': 'false','type': 'button'},{'label': '重置', 'id':'reset', 'disabled': 'false','type': 'button'}]">
            </ces:toolbar>
        </div>

    </div>

    <div id="charts${idSuffix}" style="min-width:700px;">

    </div>

</div>
<script type="text/javascript">

    $.extend($.ns("namespaceId${idSuffix}"), {
        toolbarClick : function ( e , ui ){
            var kssj = $("#kssj${idSuffix}");
            var jssj = $("#jssj${idSuffix}");
            var $form = $("#form${idSuffix}");
            if( ui.id == 'reset' ){
                var $form = $("#form${idSuffix}");
                $form.form("reset");
                kssj.datepicker("setDate","");
                jssj.datepicker("setDate","");
            }
            if( ui.id == 'search' ){
                var $formData = $form.form("formData",false);
                var jsonData = $.loadJson($.contextPath + "/sdzyccjgycjgltj!searchj.json?kssj="+$formData.KSSJ+"&jssj="+$formData.JSSJ+"&rqlx="+$formData.RQLX);
                initCharts(jsonData);
            }
        }
    });
    $(function() {
        var configInfo = CFG_initConfigInfo({
            /** 页面名称 */
            'page' : 'sdzycjjgypxsltj.jsp',//入库量趋势分析页面
            /** 页面中的最大元素 */
            'maxEleInPage' : $('#max${idSuffix}'),
            /** 页面初始化的方法 */
            'bodyOnLoad' : function(configInfo) {
                var kssj = $("#kssj${idSuffix}");
                var jssj = $("#jssj${idSuffix}");
                var rqlx = $("#rqlx${idSuffix}");
                kssj.datepicker("option","onChange",function ( e ,data){
                    //设置结束日期最小日期
                    jssj.datepicker("option","minDate",data.value);
                });
                jssj.datepicker("option","onChange",function ( e ,data){
                    //设置开始时间最大日期
                    kssj.datepicker("option","maxDate",data.value);
                });

                //给日期类型添加数据和onChange事件
                rqlx.combobox({
                    data:[{value:1,text:'日',selected:true},{value:2,text:'月'}],
                    onChange:function( e , data ){
                        if(data.value == 1){
                            kssj.datepicker("option","dateFormat","yyyy-MM-dd");
                            jssj.datepicker("option","dateFormat","yyyy-MM-dd");
                        }else{
                            kssj.datepicker("option","dateFormat","yyyy-MM");
                            jssj.datepicker("option","dateFormat","yyyy-MM");
                        }
                    }
                });
            }
        });
    });
    function initCharts(jsonData){
        $('#charts${idSuffix}').highcharts({
            credits:{
                enabled:false // 禁用版权信息
            },
            chart: {
                type: 'column'
            },
            title: {
                text: '药材加工量统计'
            },
            subtitle: {
                text: '日(月)累计加工量'
            },
            xAxis: {
                categories:jsonData.categories,
                crosshair: true
            },
            yAxis: {
                min: 0,
                title: {
                    text: '加工量  单位：千克(kg)'
                }
            },
            tooltip: {
                headerFormat: '<span style="font-size:10px">{point.key}</span><table>',
                pointFormat: '<tr><td style="color:{series.color};padding:0">{series.name}: </td>' +
                '<td style="padding:0"><b>{point.y:.1f} kg</b></td></tr>',
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
            series: jsonData.series
        });
    }




</script>
