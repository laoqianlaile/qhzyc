<%@ page import="com.ces.config.utils.CommonUtil"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="ces" tagdir="/WEB-INF/tags"%>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
    request.setAttribute("basePath",basePath);
    request.setAttribute("gurl","");
    request.setAttribute("turl","");
    request.setAttribute("idSuffix", CommonUtil.generateUIId(""));
%>
<style type="text/css">
    .app-input-label {
        float: left;
    }
</style>
<div id="max${idSuffix}" class="fill">
<ces:layout id="layout${idSuffix}" name="" style="width:800px;height:700px;" fit="true">
    <ces:layoutRegion region="north" split="true" style="height:180px;" title="农事项跟踪">
    <a style="display: none" href="" id="download${idSuffix}" >下载</a>


        <form id="csForm${idSuffix}" action="zzcsgl!saveCsxx.json"
              enctype="multipart/form-data" method="post" class="coralui-form">
            <div class="app-inputdiv6" style ="height:32px;display: none">
                <%--<input id="ID${idSuffix}" class="coralui-textbox" name="ID"/>--%>
            </div>

            <div class="fillwidth colspan2 clearfix">
                <!------------------ 第一排开始---------------->
                <div class="app-inputdiv6">
                    <label class="app-input-label" >客户类型：</label>
                    <input id="KHLX${idSuffix}" name="KHLX" readonly="readonly" />
                </div>
                <div class="app-inputdiv6">
                    <label class="app-input-label" >客户名称：</label>
                    <input id="KHMC${idSuffix}" name="KHMC"  data-options=""/>

                </div>

                <div class="app-inputdiv6">
                    <label class="app-input-label" >产品名称：</label>
                    <input id="CPMC${idSuffix}" name="CPMC"  data-options=""/>
                </div>

                <div class="app-inputdiv6" style="margin-top:10px">
                    <div>
                        <label class="app-input-label" >下单时间：</label>
                        <input id="QSXDSJ${idSuffix}" width="36%" name="QSXDSJ" data-options="" /><span style="margin:2%;text-align: center">~</span><input id="JSXDSJ${idSuffix}" name="JSXDSJ" width="36%" data-options="" />
                    </div>

                </div>
                <div class="app-inputdiv6">

                </div>

            </div>
        </form>
        <div class="toolbarsnav clearfix">
            <ces:toolbar id="toolbarId${idSuffix}"  align="center" onClick="$.ns('namespaceId${idSuffix}').toolbarClick"
                         data="[{'label': '查询', 'id':'search${idSuffix}', 'cls':'save_tb','disabled': 'false','type': 'button'},{'label': '重置', 'id':'CFG_closeComponentZone${idSuffix}','cls':'return_tb', 'disabled': 'false','type': 'button'}]">
            </ces:toolbar>
            <%--<div class='homeSpan' style="margin-top: -23px;"><div><div style='margin-left:20px;width: 150px;' id="nva${idSuffix}"> - 订单管理 - 订单统计 </div></div></div>--%>
        </div>
        </ces:layoutRegion>
    <ces:layoutRegion region="center" split="true">
        <div class="toolbarsnav clearfix">

        </div>
        <div class="toolbarsnav clearfix" style="">
            <ces:toolbar id="toolbarId1${idSuffix}" onClick="$.ns('namespaceId${idSuffix}').toolbarClick1"
                         data="['->',{'label': '导出', 'id':'print${idSuffix}', 'disabled': 'false','type': 'button'}]">
            </ces:toolbar>
            <div class='homeSpan' style="margin-top: -23px;"><div><div style='margin-left:20px;width: 100px;' id="nva${idSuffix}"> -订单统计 </div></div></div>
        </div>

        <ces:grid id="gridId${idSuffix}" afterInlineSaveRow="afterInlineSaveRow" multiselect="true"
                  shrinkToFit="true" rowNum="10" forceFit="true" fitStyle="fill"  datatype="local">
            <ces:gridCols>
                <ces:gridCol name="ID"  hidden="true"  editable="false"     width="100">订单编号</ces:gridCol>
                <ces:gridCol name="KHMC" edittype="combobox" align="center" editable="false" revertCode="true"  width="180">客户名称</ces:gridCol>
                <ces:gridCol name="CPMC"  editable="false" align="center" edittype="text" editoptions="required: false" width="180">产品名称</ces:gridCol>
                <ces:gridCol name="CPSL"  align="center" formatoptions="required: false" width="80">产品数量</ces:gridCol>
                <ces:gridCol name="PZ" editable="false"  edittype="text" editoptions="required: false" width="180">散货品种</ces:gridCol>
                <ces:gridCol name="ZLHJ"  align="center" formatoptions="required: false" width="80">散货总重量(KG)</ces:gridCol>
                <%--<ces:gridCol name="CZ" editable="false" hidden="true" formatter="toolbar"--%>
                             <%--formatoptions="onClick:$.ns('namespaceId${idSuffix}').toolbarClick3,--%>
						             <%--data:[{'label': '编辑', 'id':'bjBtn', 'disabled': 'false','type': 'button'}]" width="220">操作</ces:gridCol>--%>
            </ces:gridCols>
            <ces:gridPager gridId="gridId${idSuffix}"></ces:gridPager>
        </ces:grid>
        <div id="jqUC" style="display: none"></div>
    </div>
    </ces:layoutRegion>
        </ces:layout>
</div>
<script>

    $(function() {
        var configInfo = CFG_initConfigInfo({
            /** 页面名称 */
            'page' : 'zzddtj.jsp',
            /** 页面中的最大元素 */
            'maxEleInPage' : $('#max${idSuffix}'),
            /** 获取构件嵌入的区域 */
            'getEmbeddedZone' : function() {
                <!--return $('#layoutId${idSuffix}').layout('panel', 'center');-->
                return $('#max${idSuffix}');
            },
            /** 初始化预留区 */
            'initReserveZones' : function(configInfo) {
                //CFG_addToolbarButtons(configInfo, $('#toolbarId${idSuffix}'), 'toolBarReserve', $('#toolbarId${idSuffix}').toolbar("getLength"));
            },
            <%--/** 获取返回按钮添加的位置 */--%>
            <%--'setReturnButton' : function(configInfo) {--%>
            <%--//CFG_setReturnButton(configInfo, $('#toolbarId${idSuffix}'));--%>
            <%--CFG_setCloseButton(configInfo, $('#toolbarId${idSuffix}'));--%>
            <%--},--%>
            /** 获取关闭按钮添加的位置 */
            'setCloseButton' : function(configInfo) {
                CFG_setCloseButton(configInfo, $('#toolbarId1${idSuffix}'));
            },
            /** 页面初始化的方法 */
            'bodyOnLoad' : function(configInfo) {
                $('#KHMC${idSuffix}').combobox({
                    valueField:'KHBH',
                    textField:'KHMC',
                    sortable: true,
                    width: "auto",
                });
                $('#KHLX${idSuffix}').combobox({
                    valueField:'value',
                    textField:'text',
                    sortable: true,
                    width: "auto",
                    url:$.contextPath + "/zzddtj!loadKhlx.json",
                    onChange:function(e,data){
                        var khmcdata=$.loadJson($.contextPath + "/zzddtj!loadKhmc.json?khlx="+data.value);

                        $('#KHMC${idSuffix}').combobox('reload',khmcdata);
                        $('#CPMC${idSuffix}').combobox('setValue','');
                    }
                });
                $('#CPMC${idSuffix}').combobox({
                    valueField:'CPBH',
                    textField:'CPMC',
                    sortable: true,
                    width: "auto",
                    url:$.contextPath + "/zzddtj!loadCpmc.json"
                });
                //var khlxdata=$.loadJson($.contextPath + "/zzddtj!loadKhlx.json?sysName=ZZ");

                //var cpmcdata=$.loadJson($.contextPath + "/zzddtj!loadCpmc.json?sysName=ZZ");
                $('#QSXDSJ${idSuffix}').datepicker({width:'34%'});
                $('#JSXDSJ${idSuffix}').datepicker({width:'35%'});
                document.getElementById('search${idSuffix}').addEventListener('click',function(){
                 //CFG_closeComponentZone${idSuffix}
                    var khlx;
                    var khmc;
                    var cpmc;
                    var qssj;
                    var jssj;
                    search(khlx,khmc,cpmc,qssj,jssj);
                })
                document.getElementById('CFG_closeComponentZone${idSuffix}').addEventListener('click',function(){
                     $('#KHLX${idSuffix}').combobox('setValue','');
                     $('#KHMC${idSuffix}').combobox('setValue','');
                     $('#CPMC${idSuffix}').combobox('setValue','');
                     $('#QSXDSJ${idSuffix}').datepicker('setValue','');
                     $('#JSXDSJ${idSuffix}').datepicker('setValue','');
                })
                document.getElementById('print${idSuffix}').addEventListener('click',function(){

                    var ids=$('#gridId${idSuffix}').grid('option','selarrrow');


                    var filepath='${basePath}'+"spzstpfj/ddtj.xls";


//                    if(ids.length<1){return false;}
                    var idsstring='';
                    for(var i=0;i<ids.length;i++){
                        if(i==(ids.length-1)){
                            idsstring+=ids[i];
                        }else{
                            idsstring+=ids[i]+',';
                        }
                    }

                    var griddata=$.loadJson($.contextPath + "/zzddtj!printGrid.json?ids="+idsstring);
                    if((griddata-1)==0){
                        document.getElementById('download${idSuffix}').href=filepath;
                        document.getElementById('download${idSuffix}').click();
                    }


                })

                $('#search${idSuffix}').click=function(){

                    var khlx;
                    var khmc;
                    var cpmc;
                    var qssj;
                    var jssj;
                    search(khlx,khmc,cpmc,qssj,jssj);
                }

                function search(khlx,khmc,cpmc,qssj,jssj){

                    var khlx= $('#KHLX${idSuffix}').combobox('getValue');
                    var khmc= $('#KHMC${idSuffix}').combobox('getValue');
                    var cpmc= $('#CPMC${idSuffix}').combobox('getValue');
                    var qssj= $('#QSXDSJ${idSuffix}').datepicker('getValue');
                    var jssj= $('#JSXDSJ${idSuffix}').datepicker('getValue');
                    //var griddata=$.loadJson($.contextPath + "/zzddtj!searchGridData.json?khlx="+khlx+"&khmc="+khmc+"&cpmc="+cpmc+"&qssj="+qssj+"&jssj="+jssj);

                    var $grid= $('#gridId${idSuffix}');
//                    for(var i=0;i<griddata.length;i++){
//                        //$grid.grid();
//                    }
                    $grid.grid("option", "datatype", "json");
                    $grid.grid("option","url",$.contextPath + "/zzddtj!searchGridData.json?khlx="+khlx+"&khmc="+khmc+"&cpmc="+cpmc+"&qssj="+qssj+"&jssj="+jssj);
                    $grid.grid("reload");
                }


            }});
        if (configInfo) {
        }
        configInfo.CFG_outputParams = {'success':'otp'};
    });



</script>