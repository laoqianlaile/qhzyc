<%@ page import="com.ces.config.utils.CommonUtil"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="ces" tagdir="/WEB-INF/tags"%>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
    request.setAttribute("gurl","");
    request.setAttribute("turl","");
    request.setAttribute("idSuffix", CommonUtil.generateUIId(""));
    request.setAttribute("path", path);
%>
<style type="text/css">
    .app-input-label {
        float: left;
    }
    .coral-toolbar-border{
        height:35px;
    }
</style>
<div id="max${idSuffix}" class="fill">
    <div class="fill">
        <div class="toolbarsnav clearfix">
            <ces:toolbar id="toolbarId${idSuffix}" onClick="$.ns('namespaceId${idSuffix}').toolbarClick"
                         data="['->',{'label': '关闭', 'id':'CFG_closeComponentZone','cls':'return_tb', 'disabled': 'false','type': 'button'}]">
            </ces:toolbar>
            <div class='homeSpan' style="margin-top: -23px;"><div><div style='margin-left:20px;width: 150px;' id="nva${idSuffix}"> - 投入品领用管理 - 新增 </div></div></div>
        </div>
        <form id="trplyZbForm${idSuffix}" method="post" class="coralui-form">
            <div class="app-inputdiv4" style ="height:32px;display: none">
                <input id="ID${idSuffix}" class="coralui-textbox" name="ID"/>
            </div>

            <div class="fillwidth colspan3 clearfix">
                <!------------------ 第一排开始---------------->
                <%--<div class="app-inputdiv4"  style="">--%>
                <%--<label class="app-input-label" >出库流水号：</label>--%>
                <%--<input id="CKLSH${idSuffix}" name="CKLSH"  class="coralui-textbox"/>--%>
                <%--</div>--%>
                <%--<div class="app-inputdiv4"  style="display: none">--%>
                <%--<label class="app-input-label" >领用时间：</label>--%>
                <%--<ces:datepicker id="LYSJ1${idSuffix}"  required="true"  name="LYSJ1" />--%>
                <%--</div>--%>
                <%--<div class="app-inputdiv4"  style="display: none">--%>
                <%--<label class="app-input-label" >操作时间：</label>--%>
                <%--<ces:datepicker id="CZSJ${idSuffix}"  required="true"  name="CZSJ" />--%>
                <%--</div>--%>

                <div class="app-inputdiv4"  style="display: none">
                    <label class="app-input-label" >领用人：</label>
                    <input id="LYR${idSuffix}" name="LYR"  class="coralui-textbox"/>
                </div>
                <div class="app-inputdiv4">
                    <label class="app-input-label" >领用人：</label>
                    <input id="LYRBH${idSuffix}" name="LYRBH" data-options="" />
                </div>
                <%--<div class="app-inputdiv4">--%>
                <%--<label class="app-input-label" >实际领用人：</label>--%>
                <%--<input id="SJLYR${idSuffix}" name="SJLYR"  data-options="required:true" />--%>
                <%--</div>--%>
                <%--<div class="app-inputdiv4" style="display: none">--%>
                <%--<label class="app-input-label" >实际领用人编号：</label>--%>
                <%--<input id="SJLYRBH${idSuffix}" name="SJLYRBH"  class="coralui-textbox"/>--%>
                <%--</div>--%>

                <div class="app-inputdiv4">
                    <label class="app-input-label" >农事项预计开始时间：</label>
                    <ces:datepicker id="LYSJ${idSuffix}"   name="LYSJ" />
                    <%--<ces:datepicker id="JSLYSJ${idSuffix}"  required="true"  name="LYSJ" />--%>
                </div>
                <div class="app-inputdiv4">
                    <label class="app-input-label" >农事项预计结束时间：</label>
                    <ces:datepicker id="JSLYSJ${idSuffix}"    name="LYSJ" />
                </div>

                <%--<div class="app-inputdiv4"  style="display: none">--%>
                <%--<label class="app-input-label" >操作人编号：</label>--%>
                <%--<input id="CZR${idSuffix}" name="CZR"  class="coralui-textbox"/>--%>
                <%--</div>--%>
                <%--<div class="app-inputdiv4"  style="">--%>
                <%--<label class="app-input-label" >操作人：</label>--%>
                <%--<input id="CZRBH${idSuffix}" name="CZRBH"  />--%>
                <%--</div>--%>
                <!------------------ 第一排结束---------------->

                <!------------------第七排结束----------------->
            </div>
        </form>
        <div class="toolbarsnav clearfix">
            <ces:toolbar id="toolbarId2${idSuffix}" onClick="$.ns('namespaceId${idSuffix}').toolbarClick"

                    >
            </ces:toolbar>
        </div>
        <ces:grid id="pGridId${idSuffix}"
                  forceFit="true" fitStyle="fill"  datatype="local" clicksToEdit="1" height="160">
            <ces:gridCols>
                <ces:gridCol name="ID" hidden="true" key="true" width="100">ID</ces:gridCol>
                <ces:gridCol name="PID"  hidden="true" width="80">PID</ces:gridCol>
                <ces:gridCol name="BID" hidden="true" width="80">BID</ces:gridCol>
                <ces:gridCol name="SCDALX" hidden="true" width="80">生产档案类型</ces:gridCol>
                <ces:gridCol name="NSXBH" hidden="true" width="80">农事项编号</ces:gridCol>
                <ces:gridCol name="TRPBH"  width="80">投入品编号</ces:gridCol>
                <ces:gridCol name="SSQYBH" hidden="true" edittype="combobox" width="120" formatoptions="required: true,disabled:true" >所属区域</ces:gridCol>
                <ces:gridCol name="SSQY"  hidden="true" width="80">所属区域2</ces:gridCol>
                <ces:gridCol name="DKBH" edittype="combobox" width="120" formatoptions="required: true,disabled:true" >地块编号</ces:gridCol>
                <ces:gridCol name="SCDA" edittype="combobox" formatoptions="required: true" width="150">生产档案编号</ces:gridCol>
                <ces:gridCol name="NSXMC"  width="120">农事项名称</ces:gridCol>
                <ces:gridCol name="TRPLX" formatter="combobox" width="120" >投入品类型</ces:gridCol>
                <ces:gridCol name="TRPMC"  width="120">投入品名称</ces:gridCol>
                <%--<ces:gridCol name="TRPBH" hidden="true" width="120">投入品名称</ces:gridCol>--%>
                <ces:gridCol name="GYSMC"  width="120" formatoptions="{textField:'GYSMC',valueField:'GYSBH'}" formatter="combobox">供应商名称</ces:gridCol>
                <%--<ces:gridCol name="SCPCH"  width="120"  formatter="combobox">生产批次号</ces:gridCol>--%>
                <ces:gridCol name="RKLSH"  width="120"  formatter="combobox">入库流水号</ces:gridCol>

                <ces:gridCol name="KCSL" formatoptions="readonly:true"  width="120" formatter="text" >库存重量</ces:gridCol>

                <%--<ces:gridCol name="GYSBH"  width="120" formatter="text" editable="true" formatoptions="readonly:false" >供应商编号</ces:gridCol>--%>
                <ces:gridCol name="CKSL"  width="120" formatter="text" editable="true" formatoptions="required: true,readonly:true" >出库数量</ces:gridCol>
                <%--<ces:gridCol name="CZ" formatter="toolbar"--%>
                             <%--formatoptions="onClick:$.ns('namespaceId${idSuffix}').toolbarClick,--%>
						             <%--data:[{'label': '删除', 'id':'delBtn', 'disabled': 'false','type': 'button'}]" width="120">操作</ces:gridCol>--%>
            </ces:gridCols>
        </ces:grid>

    </div>
</div>
<script type="text/javascript">

    var temId = "tem_${idSuffix}";
    var trplx = $.loadJson($.contextPath + "/zztrpcglb!searchTrplx.json");
    $.extend($.ns("namespaceId${idSuffix}"), {
        //整体按钮条事件
        toolbarClick : function(event, button) {
            var $grid = $("#pGridId${idSuffix}");
            var rowId = event.data.rowId ;
            if (button.id == "save") {//保存的方法
                $("#trplyZbForm${idSuffix}").form().submit();
            } else if (button.id == "addTrply") {//返回 或关闭
                showTrplyDialog();
            } else if (button.id == "CFG_closeComponentZone") {//返回 或关闭
                CFG_clickCloseButton($('#max${idSuffix}').data('configInfo'));
            } else if ( button.id == "updBtn"){
                initUpdateCkxx(rowId);
            } else if ( button.id == "delBtn"){
                if( (rowId).indexOf("tem") > -1 ){ // 没有进行保存不需要走后台操作
                    $grid.grid("delRowData",rowId);
                }else{//已经保存过需走后台删除，并还原库存量
                    $.ajax({
                        type : "post" ,
                        url : $.contextPath + "/zztrplylb!deleteScdalyxx.json?id="+rowId,
                        dataType  : "json",
                        success : function ( res ){
                            $grid.grid("delRowData",rowId);
                            CFG_message("操作成功","success");
                        },error : function ( res ){
                            CFG_message("操作失败","error");
                        }
                    })
                }
            }
        },
        //第一个列表工具条事件
        toolbarClick1 : function(event, button) {
            if(button.id == "searchBtn"){
                var $form = $("#trplyForm${idSuffix}") ;
                if(!$form.form("valid")){
                    CFG_message("请选择农事操作项","warn");
                    return;
                }

                <%--var zbgridobj=$('#trplyZbForm${idSuffix}');--%>
                var idsArr=$('#pGridId${idSuffix}').grid('getRowData');
                var ids='';
                for(var le=0;le<idsArr.length;le++){
                    if(le==(idsArr.length-1)){
                        ids=ids+idsArr[le].BID;
                    }else{ids=ids+idsArr[le].BID+',';}
                }

                var qybhform=$("#SSQYBH${idSuffix}").combobox('getValue');
                var dkbhform=$("#DKBH${idSuffix}").combobox('getValue');
                var scdaform=$("#SCDA${idSuffix}").combobox('getValue');
                var nxczx=$("#NSXBH${idSuffix}").combobox('getValue');
                var nxczxmc=$("#NSXBH${idSuffix}").combobox('getText');



//                var tjGridData=$.loadJson($.contextPath + "/zztrplylb!tjGridGysData.json?qybh="+qybhform+"&dkbh="+dkbhform+"&scda="+scdaform);
                //var dadadadad=$.loadJson($.contextPath + "/zztrplylb!tjGridGysData.json?qybh="+qybhform+"&dkbh="+dkbhform+"&scda="+scdaform+"&nsczxbh="+nxczx+"&ids="+ids);

                //trplyForm${idSuffix}
                var trplygrid=$("#trplyGrid${idSuffix}");
                trplygrid.grid("option", "datatype", "json");
                trplygrid.grid("option","url", $.contextPath + "/zztrplylb!tjGridGysData.json?qybh="+qybhform+"&dkbh="+dkbhform+"&scda="+scdaform+"&nsczxbh="+nxczx+"&ids="+ids+"&nxczxmc="+nxczxmc);
                trplygrid.grid("reload");

//                showTreeDialog($form.form("formData",false));
            }
        },
        toolbarClick2 : function ( e , ui ){

        },
        toolbarClick3 : function(e, ui) {

        },
        /* 构件方法和回调函数 */
        getCategoryId : function() {
            alert("调用方法getCategoryId！");
            var o = {
                status : true
            };
            o.categoryId = '1';
            return o;
        },
        refreshGrid : function(cbParam1) {
            alert("调用回到函数refreshGrid(" + cbParam1 + ")");
        }
    });
    var buttonOpts_lyr = {
        id: "combogrid_buttonId",
        label: "构件按钮",
        icons: "icon-checkmark-circle",
        text: false,
        onClick: $.ns('namespaceId${idSuffix}').sjlyrClick
    };
    var _colNames_lyr = ["领用人编号", "领用人名称"];
    var _colModel_lyr = [{name: "FZRBH", width: 65, sortable: true}, {name: "FZR", width: 155, sortable: true}];
    $(function() {
        var configInfo = CFG_initConfigInfo({
            /** 页面名称 */
            'page' : 'zztrplyxz.jsp',
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
            /** 获取返回按钮添加的位置 */
            'setReturnButton' : function(configInfo) {
                //CFG_setReturnButton(configInfo, $('#toolbarId${idSuffix}'));
                CFG_setCloseButton(configInfo, $('#toolbarId${idSuffix}'));
            },
            /** 获取关闭按钮添加的位置 */
            'setCloseButton' : function(configInfo) {
                CFG_setCloseButton(configInfo, $('#toolbarId${idSuffix}'));
            },
            /** 页面初始化的方法 */
            'bodyOnLoad' : function(configInfo) {




                $("#pGridId${idSuffix}").grid("setColProp", "GYSMC",{"formatoptions":{"textField":"name","readonly":"true","valueField":"value",

                    onClick:function(event, ui){
                        var trpbh=$("#pGridId${idSuffix}").grid('getRowData',event.data.rowId).TRPBH;
                        var scpch=$("#pGridId${idSuffix}").grid('getRowData',event.data.rowId).SCPCH;
//                        even.data.rowId
//                        alert(trpbh);
                        var gysData=$.loadJson($.contextPath + "/zztrplylb!getGysData.json?trpbh="+trpbh+"&scpch="+scpch);


//                        var gysBox=event.currentTarget;


                        $("#pGridId${idSuffix}").grid("getCellComponent", event.data.rowId, "GYSMC").combobox({
                            textField:"GYSMC",
                            valueField:"GYSBH",
                        });

                        $("#pGridId${idSuffix}").grid("getCellComponent", event.data.rowId, "GYSMC").combobox("reload", gysData);



                    },
                    onChange:function(ui, data) {

                        var trpbh=$("#pGridId${idSuffix}").grid('getRowData',ui.data.rowId).TRPBH;
                        var scpch=$("#pGridId${idSuffix}").grid('getRowData',ui.data.rowId).SCPCH;
//                        even.data.rowId
//                        alert(trpbh);
                        var comboValue=$("#pGridId${idSuffix}").grid("getCellComponent", ui.data.rowId, "GYSMC").combobox("getValue");
                        var gysData=$.loadJson($.contextPath + "/zztrplylb!getGysData.json?trpbh="+trpbh+"&scpch="+scpch);
                        $("#pGridId${idSuffix}").grid("getCellComponent", ui.data.rowId, "RKLSH").combobox("setValue",'');
                        <%--$("#pGridId${idSuffix}").grid("getCellComponent", ui.data.rowId, "KCSL").combobox("setValue",gysData.KCSL);--%>

//                        var gysData=$.loadJson($.contextPath + "/zztrplylb!getGysData.json?trpbh="+trpbh+"&scpch="+scpch);
                        <%--$("#pGridId${idSuffix}").grid("getCellComponent", ui.data.rowId, "GYSBH").textbox("setText", 11);--%>
//                        alert('进入onchange');
                    },
                }});

                $("#pGridId${idSuffix}").grid("setColProp", "RKLSH",{"formatoptions":{"textField":"RKLSH","readonly":"true","valueField":"RKLSH",
//                    onChange:function(ui, data) {

////                    alert(11111);
//                },
                    onClick:function(event, ui){
                        var trpbh=$("#pGridId${idSuffix}").grid('getRowData',event.data.rowId).TRPBH;
                        var gysbh=$("#pGridId${idSuffix}").grid('getRowData',event.data.rowId).GYSMC;
                        if(gysbh==''){return;}
                        var pchData=$.loadJson($.contextPath + "/zztrplylb!getScpchData.json?trpbh="+trpbh+"&gysbh="+gysbh);

                        $("#pGridId${idSuffix}").grid("getCellComponent", event.data.rowId, "RKLSH").combobox({
                            textField:"RKLSH",
                            valueField:"RKLSH",
                        });

                        $("#pGridId${idSuffix}").grid("getCellComponent", event.data.rowId, "RKLSH").combobox("reload", pchData);
//                        even.data.rowId
//                        alert(trmbh);
                    },
                    onChange:function(ui,data){

                        var trpbh=$("#pGridId${idSuffix}").grid('getRowData',ui.data.rowId).TRPBH;
                        var gysbh=$("#pGridId${idSuffix}").grid('getRowData',ui.data.rowId).GYSMC;
                        if(gysbh==''){return;}
//                        var pchData=$.loadJson($.contextPath + "/zztrplylb!getScpchData.json?trpbh="+trpbh+"&gysbh="+gysbh);

                        var ckslData=$.loadJson($.contextPath + "/zztrplylb!getCkslData.json?rklsh="+data.text);

                        <%--$("#pGridId${idSuffix}").grid("getCellComponent", event.data.rowId, "RKLSH").combobox({--%>
                        <%--textField:"RKLSH",--%>
                        <%--valueField:"RKLSH",--%>
                        <%--});--%>
                        $("#pGridId${idSuffix}").grid("getCellComponent", ui.data.rowId, "KCSL").textbox('setValue',ckslData.KCSL);


                        <%--$("#pGridId${idSuffix}").grid("getCellComponent", event.data.rowId, "RKLSH").combobox("reload", pchData);--%>
                    }
                }});
                var lyrbhData = $.loadJson($.contextPath + "/zztrplylb!searchQyfzr.json");

                //领用人信息加载
                $("#LYRBH${idSuffix}").combobox({
                    valueField:'ZZDYGLYBH',
                    textField:'ZZDYGLY',
                    sortable: true,
                    width: "auto",
                    data:lyrbhData.data
                });
                $('#LYRBH${idSuffix}').combobox('disable');
                //操作人信息加载
                var czrData=$.loadJson($.contextPath + "/zztrplylb!searchCzrxx.json");
                $("#CZRBH${idSuffix}").combobox({
                    valueField:'CZRBH',
                    textField:'CZR',
                    sortable: true,
                    width: "auto",
                    data:czrData
                });
                //操作人onchange事件
                $("#CZRBH${idSuffix}").combobox('option','onChange',function(e,data){
                    $("#CZR${idSuffix}").textbox('setValue',data.text);
                });
                //领用人onChange事件
                $("#LYRBH${idSuffix}").combobox("option","onChange",function( e , data ){
                    $("#LYR${idSuffix}").textbox("setValue",data.text);
                    $("#SJLYR${idSuffix}").combogrid("setValue",data.text);
                    $("#SJLYRBH${idSuffix}").textbox("setValue",data.value);
                    searchNsx();

                    //联动子表中区域选择下拉框数据
//                    reloadQyxx(data.value);
                });
                $("#SJLYR${idSuffix}").combogrid({
                    url: $.contextPath + "/zztrplylb!searchQyfzr.json",
                    multiple: false,
                    sortable: true,
                    colModel: _colModel_lyr,
                    colNames: _colNames_lyr,
                    width: "auto",
                    panelWidth : "300",
                    textField: "FZR",
                    valueField: "FZRBH",
                    height: "auto",
                    buttonOptions: buttonOpts_lyr
                });
                $("#SJLYR${idSuffix}").combogrid("option","onChange",function(e,data){
                    $("#SJLYR${idSuffix}").combogrid("setValue",data.text);
                    $("#SJLYRBH${idSuffix}").textbox("setValue",data.value);
                });

                $('#LYSJ${idSuffix}').datepicker('option','onChange',function(e,data){
                    searchNsx()
                });
                $('#LYSJ${idSuffix}').datepicker('disable',true);
                $('#JSLYSJ${idSuffix}').datepicker('option','onChange',function(e,data){
                    searchNsx()
                });
                $('#JSLYSJ${idSuffix}').datepicker('disable',true);

                $("#pGridId${idSuffix}").grid("setColProp", "TRPLX",{"formatoptions":{"data":trplx,"readonly":"true","textField":"name","valueField":"value",onChange:function(ui, data) {
                }}});
                var inputParamId = CFG_getInputParamValue(configInfo, 'inputParam1');
                if(isEmpty(inputParamId)){
                    <%--$("#LYSJ${idSuffix}").datepicker("setDate",new Date());--%>



                }else{
                    $("#nva${idSuffix}").html(" - 投入品领用管理 - 修改");
                    var $obj = $.loadJson($.contextPath + "/zztrplylb!searchTrckxx.json?id="+inputParamId);
                    $("#trplyZbForm${idSuffix}").form("loadData",$obj);
                    var $grid = $("#pGridId${idSuffix}");
                    var pGrid=$("#pGridId${idSuffix}");
                    var $griddata = $.loadJson($.contextPath + "/zztrplylb!searchTrpscdaxx.json?pid="+inputParamId);
                    //生产单列表数据初始化
//                    $grid.grid("option", "datatype", "json");
//                    $grid.grid("option","url", $.contextPath + "/zztrplylb!searchTrpscdaxx.json?pid="+inputParamId);
//                    $grid.grid("reload");
                    var xggysData=$.loadJson($.contextPath + "/zztrplylb!getXgGysData.json");
                    <%--$("#pGridId${idSuffix}").grid("setColProp", "GYSMC",{"formatoptions":{"data":xggysData,"textField":"GYSMC","valueField":"GYSBH"--%>
                    <%--}});--%>

                    for(var len=0;len<$griddata.data.length;len++){
                        pGrid.grid('addRowData',$griddata.data[len].ID,$griddata.data[len]);
                        var trpbh=$griddata.data[len].TRPBH;
//            var scpch=getgridData.data[len].SCPCH;
                        var scpch=11;
                        var gysData=$.loadJson($.contextPath + "/zztrplylb!getGysData.json?trpbh="+trpbh+"&scpch="+scpch);
                        $("#pGridId${idSuffix}").grid("getCellComponent", $griddata.data[len].ID, "GYSMC").combobox({
                            textField:"GYSMC",
                            valueField:"GYSBH",
                        });
                        $("#pGridId${idSuffix}").grid("getCellComponent", $griddata.data[len].ID, "GYSMC").combobox("setValue", $griddata.data[len].GYSMC);


                        //if(gysData.length==0){continue;}
                        //$("#pGridId${idSuffix}").grid("getCellComponent", $griddata.data[len].ID, "GYSMC").combobox("setValue", gysData[0].GYSBH);
                        <%--var trpbh=$("#pGridId${idSuffix}").grid('getRowData',event.data.rowId).TRPBH;--%>
                        <%--var gysbh=$("#pGridId${idSuffix}").grid('getRowData',len).GYSMC;--%>
                        var gysbh=gysData[0].GYSBH;

                        var pchData=$.loadJson($.contextPath + "/zztrplylb!getScpchData.json?trpbh="+trpbh+"&gysbh="+gysbh);

                        $("#pGridId${idSuffix}").grid("getCellComponent", $griddata.data[len].ID, "RKLSH").combobox({
                            textField:"RKLSH",
                            valueField:"RKLSHBH",
                        });
                        $("#pGridId${idSuffix}").grid("getCellComponent", $griddata.data[len].ID, "RKLSH").combobox("reload", pchData);
                        $("#pGridId${idSuffix}").grid("getCellComponent", $griddata.data[len].ID, "RKLSH").combobox("setValue", $griddata.data[len].RKLSH);
                        var ckslData=$.loadJson($.contextPath + "/zztrplylb!getCkslData.json?rklsh="+pchData[0].RKLSH);
                        $("#pGridId${idSuffix}").grid("getCellComponent", $griddata.data[len].ID, "KCSL").textbox('setValue',$griddata.data[len].KCSL);
                        $("#pGridId${idSuffix}").grid("getCellComponent", $griddata.data[len].ID, "CKSL").textbox('setValue',$griddata.data[len].CKSL);

                        <%--$("#pGridId${idSuffix}").grid("setColProp", "GYSMC",{"formatoptions":{"data":gysData,"textField":"GYSMC","valueField":"GYSBH",--%>
                        <%--}});--%>
                        <%--$("#pGridId${idSuffix}").grid("setColProp", "SCPCH",{"formatoptions":{"textField":"name","valueField":"value",--%>
                        <%--}});--%>
                    }

                }
            }
        });
        if (configInfo) {
            //alert("系统参数：\t" + "关联的系统参数=" + CFG_getSystemParamValue(configInfo, 'sysParam1')
            //		+ "\n构件自身参数：\t" + "selfParam1=" + CFG_getSelfParamValue(configInfo, 'selfParam1')
            //		+ "\n构件入参：\t" + "inputParamName_1=" + CFG_getInputParamValue(configInfo, 'inputParamId'));
            //alert(CFG_getInputParamValue(configInfo, 'inputParamId'));
            //alert(CFG_getInputParamValue(configInfo, 'inputParamId1'));

        }
    });
    var loadData = "" ;
    var _colNames=["id","pid","农事项编号","生产档案编号","投入品编号","投入品类型","投入品名称","地块编号","生产批次号","供应商名称","库存数量","出库数量","BID"];
    var _colModel = [
        {name:'ID',hidden:true,width:100},
        {name:'PID',hidden:true,width:100},
        {name:'NSXMC',width:100},
        {name:'SCDA',width:100},
        {name:'TRPBH',width:100},
        {name:'TRPLX',formatter:'combobox',width:100,readonly:true},
        {name:'TRPMC',width:100},
        {name:'DKBH',width:150},
        {name:'CPPCH',hidden:true,width:100},
        {name:'GYSMC',hidden:true,width:120},
        {name:'KCSL',hidden:true,width:100},
        {name:'CKSL',formatter:'text',width:125},
        {name:'BID',hidden:true,width:100},
    ];
    var _setting = {
        width : 'auto',
        height : '235' ,
        shrinkToFit : "true",
        forceFit : "true" ,
        //fitStyle : "fill" ,
        datatype : "local"  ,
        colModel : _colModel,
        colNames : _colNames,
        multiselect:true
    };

    function showTrplyDialog(){
        var jqGlobal = document.body;
        var dialogDivJQ = $("<div></div>").appendTo(jqGlobal);
        dialogDivJQ.dialog({
            appendTo : jqGlobal,
            modal : true,
            autoOpen : true,
            title : "农事操作项投入品领用",
            maxWidth : 800,
            width : 800,
            height : 400,
            maxHeight : 400,
            resizable : false,
            position : {of:jqGlobal},
            onCreate  : function(e, ui){
                var fromHtml = '<div class="fillwidth colspan2 clearfix">' +
                        '<form id="trplyForm${idSuffix}" method="post" class="coralui-form">' +
                        '<div class="app-inputdiv6"><label class="app-input-label" >所属区域：</label><input id="SSQYBH${idSuffix}" name="SSQYBH" data-options="required:true" /></div>' +
                        '<div class="app-inputdiv6"><label class="app-input-label" >地块编号：</label><input id="DKBH${idSuffix}" name="DKBH" data-options="required:true" /></div>' +
                        '<div class="app-inputdiv6"><label class="app-input-label" >生产档案：</label><input id="SCDA${idSuffix}"  data-options="required:true"  name="SCDA"  /></div>'+
                        <%--'<div class="app-inputdiv6"><label class="app-input-label" >生产档案类型：</label><input id="SCDALX${idSuffix}" data-options="required:true" name="SCDALX" /></div>'+--%>
                        '<div class="app-inputdiv6"><label class="app-input-label" >农事操作项：</label><input id="NSXBH${idSuffix}" data-options="required:true" name="NSXBH" /></div>'+
                        '<div class="app-inputdiv6" style ="display:none"><label class="app-input-label" >农事操作项：</label><input id="NSXMC${idSuffix}" data-options="required:true" name="NSXMC" class="coralui-textbox"/></div>'+
                        '<div class="app-inputdiv12" style="margin-left:15px;">' +
                        '<div class="toolbarsnav clearfix">' +
                        '<div id="toolbarId3${idSuffix}" style="height:35px;" class="coralui-toolbar" data-options="onClick:$.ns(\'namespaceId${idSuffix}\').toolbarClick1,data:[{\'label\': \'搜索\', \'id\':\'searchBtn\', \'disabled\': \'false\',\'type\': \'button\'}]"></div></div>'+
                        '<div id="trplyGrid${idSuffix}"></div></div>'+
                        '</form></div>';
                $(fromHtml).appendTo(dialogDivJQ);
                $.parser.parse(e.target);
                initTrylyForm();
                $("#trplyGrid${idSuffix}").grid(_setting);


                $("#trplyGrid${idSuffix}").grid("setColProp", "TRPLX",{"formatoptions":{"data":trplx,"readonly":"true","textField":"name","valueField":"value",onChange:function(ui, data) {
                }}});
            },
            onOpen : function(e, ui) {
                var jqPanel = $(this).dialog("buttonPanel").addClass("app-bottom-toolbar"),
                        jqDiv   = $("<div style=\"height:35px;\" class=\"dialog-toolbar\">").appendTo(jqPanel);
                jqDiv.toolbar({
                    data: ["->", {id:"save", label:"确定", type:"button"}, {id:"cancel", label:"取消", type:"button"}],
                    onClick: function(e, ui) {
                        if ("save" === ui.id) {//执行保存操作

                            var fbgridataid=$("#trplyGrid${idSuffix}").grid('option','selarrrow');
                            var zbgrid=$("#pGridId${idSuffix}");
                            var sel = $("#trplyGrid${idSuffix}").grid("option","selarrrow").concat();
                            var lensd=zbgrid.grid('getRowData').length;
                            var testdata=zbgrid.grid('getRowData');

                            $.each(sel, function(i){

                                var lastitem='tem_'+(lensd+i+1)

                                var rd = $("#trplyGrid${idSuffix}").grid("getRowData", sel[i]);
                                delete rd.ID;
                                delete rd.CPPCH;
                                delete rd.GYSMC;
                                delete rd.PID;
                                delete rd.NSX;
                                delete rd.RKLSH;
                                var rownumber=lensd-1+i;


                                zbgrid.grid('addRowData',lensd-1+i,rd);

                                var gysData=$.loadJson($.contextPath + "/zztrplylb!getGysData.json?trpbh="+rd.TRPBH+"&scpch="+"");
                                $("#pGridId${idSuffix}").grid("getCellComponent", rownumber, "GYSMC").combobox({
                                    textField:"GYSMC",
                                    valueField:"GYSBH",
                                });
                                $("#pGridId${idSuffix}").grid("getCellComponent", rownumber, "GYSMC").combobox("reload", gysData);
                                $("#pGridId${idSuffix}").grid("getCellComponent", rownumber, "GYSMC").combobox("setValue", gysData[0].GYSBH);

                                var gysbh=gysData[0].GYSBH;
                                if(gysbh==''){return;}
                                var pchData=$.loadJson($.contextPath + "/zztrplylb!getScpchData.json?trpbh="+rd.TRPBH+"&gysbh="+gysbh);

                                $("#pGridId${idSuffix}").grid("getCellComponent", rownumber, "RKLSH").combobox({
                                    textField:"RKLSH",
                                    valueField:"RKLSHBH",
                                });
                                $("#pGridId${idSuffix}").grid("getCellComponent", rownumber, "RKLSH").combobox("reload", pchData);
                                $("#pGridId${idSuffix}").grid("getCellComponent", rownumber, "RKLSH").combobox("setValue", pchData[0].RKLSHBH);
                                var ckslData=$.loadJson($.contextPath + "/zztrplylb!getCkslData.json?rklsh="+pchData[0].RKLSH);
                                $("#pGridId${idSuffix}").grid("getCellComponent", rownumber, "KCSL").textbox('setValue',ckslData.KCSL);

                            });
                        }
                        dialogDivJQ.dialog("close");
                    }
                });
            },onClose : function (){
                dialogDivJQ.remove();
            }
        });
    }


    function initTrylyForm(){
        //根据刷卡带过来的数据用户名和编号 加载区域信息
        var dFrzbh = $("#SJLYRBH${idSuffix}").textbox("getValue");
        var lyrbh=$('#LYRBH${idSuffix}').combobox('getValue');

//        var fzrbh =  (isEmpty(dFrzbh) ? "001":dFrzbh);
        var qyJson = $.loadJson($.contextPath + "/zztrplylb!searchQyxx.json");
        //1、区域数据格式定义并添加onChange事件
        $("#SSQYBH${idSuffix}").combobox({
            textField :"SSQYMC",
            valueField :"SSQYBH",
            width :"auto",
            data :qyJson.data,
            onChange : function ( e ,data ){
                //添加onChange事件 进行地块信息数据加载
                var dkJson=$.loadJson($.contextPath + "/zztrplylb!searchDkxx.json?qybh="+data.value);

                $("#DKBH${idSuffix}").combobox("reload",dkJson.data);
            }
        });
        var dkJson = $.loadJson($.contextPath + "/zztrplylb!searchDkxxByQyfzr.json?qybh="+$("#SSQYBH${idSuffix}").combobox('getValue'));
        //2、地块数据格式定义并添加onChange事件
        $("#DKBH${idSuffix}").combobox({
            textField :"DKBH",
            valueField :"DKBH",
            width :"auto",
            data :dkJson.data,
            onChange : function ( e , data ){
                //根据区域编号获得对应区域下面的地块信息
                // var scdaJson = $.loadJson($.contextPath + "/zztrplylb!searchScda.json?dkbh="+data.value);
                var scdaJson = $.loadJson($.contextPath + "/zztrplylb!searchScdaByQyfzr.json?qybh="+$("#SSQYBH${idSuffix}").combobox('getValue')+"&dkbh="+data.value);
                $("#SCDA${idSuffix}").combobox("reload",scdaJson.data);
            }
        });
        var scdaJson = $.loadJson($.contextPath + "/zztrplylb!searchScdaByQyfzr.json?qybh="+$("#SSQYBH${idSuffix}").combobox('getValue')+"&dkbh="+$("#DKBH${idSuffix}").combobox('getValue'));
        //3、生产档案数据格式定义并添加onChange事件
        $("#SCDA${idSuffix}").combobox({
            textField :"SCDABH",
            valueField :"SCDABH",
            width :"auto",
            data :scdaJson.data,
            onChange : function ( e , data ){

                var nsxJson = $.loadJson($.contextPath + "/zztrplylb!searchNsx.json?qybh="+$("#SSQYBH${idSuffix}").combobox('getValue')+"&dkbh="+$("#DKBH${idSuffix}").combobox('getValue')+"&scdabh="+data.value);

                //var nsxbhObj = $.loadJson($.contextPath + "/zztrplylb!searchScdaObj.json?scdabh="+scdabh);
                <%--var scdalx = $("#SCDALX${idSuffix}").combobox("getValue");--%>
                <%--reloadNsczx( data.value , scdalx );--%>
                $("#NSXBH${idSuffix}").combobox('reload',nsxJson)
            }
        });
        //4、生产方案数据初始化，格式定义并添加onChange事件
        // var zzfaJson = $.loadJson($.contextPath + "/trace!getDataDictionary.json?lxbm=CZLX");
        <%--$("#SCDALX${idSuffix}").combobox({--%>
        <%--textField : "text" ,--%>
        <%--valueField : "value" ,--%>
        <%--width : "auto" ,--%>
        <%--data :zzfaJson,--%>
        <%--onChange : function ( e , data ){--%>
        <%--var scdabh = $("#SCDA${idSuffix}").combobox("getValue");--%>
        //reloadNsczx( scdabh , data.value );
        <%--}--%>
        <%--});--%>
        $("#NSXBH${idSuffix}").combobox({
            textField : "NSZYXMC" ,
            valueField : "ID" ,
            width : "auto",
            onChange : function (ｅ , data) {
                $("#NSXMC${idSuffix}").textbox("setValue" , data.text);
            }
        });
    }
    function showTreeDialog($form){
        var jqGlobal = document.body;
        var tdialogDivJQ = $("<div></div>").appendTo(jqGlobal);
        tdialogDivJQ.dialog({
            appendTo : jqGlobal,
            modal : true,
            autoOpen : true,
            title : "投入品批次选择",
            maxWidth : 800,
            width : 800,
            height : 400,
            maxHeight : 400,
            resizable : false,
            position : {of:jqGlobal},
            onCreate : function (e, ui){
                var treeHtml = "<ul id='treeUi'></ul>" ;
                $(treeHtml).appendTo(tdialogDivJQ);

                var scdaObj = $.loadJson($.contextPath + "/zztrplylb!searchScdaObj.json?scdabh="+$form.SCDA);
                //1.农事项详细
                var nsxObj = $.loadJson($.contextPath + "/zztrplylb!searchNsxObj.json?pid="+scdaObj.ID+"&scdalx="+$form.SCDALX+"&nszyxbh="+$form.NSXBH);

                var $treeData = $.loadJson($.contextPath + "/zztrplylb!searchTreeParentNode.json?pid="+nsxObj.ID+"&scdalx="+$form.SCDALX+"&nszyxbh="+$form.NSXBH );

                $.parser.parse(e.target);
                $('#treeUi').tree({
                    data : $treeData.data,
                    checkable:true
                });
            },
            onOpen : function(e, ui) {

                var jqPanel = $(this).dialog("buttonPanel").addClass("app-bottom-toolbar"),
                        jqDiv   = $("<div style=\"height:35px;\" class=\"dialog-toolbar\">").appendTo(jqPanel);
                jqDiv.toolbar({
                    data: ["->", {id:"sure", label:"确定", type:"button"}, {id:"cancel", label:"取消", type:"button"}],
                    onClick: function(e, ui) {
                        if ("sure" === ui.id) {
                            tdialogDivJQ.dialog("close");
                        } else {
                            tdialogDivJQ.remove();
                        }
                    }
                });
            },
            onClose : function ( e , ui ){
                var rklshs = checkedNodesIds();

                var acolums={ID:1231,PID:1221,NSX:'SASA',RKLSH:'12312313',TRPLX:'LX',TRPMC:'DAWDA',CPPCH:'1233131',GYSMC:'DADAD',KCSL:123,CKSL:2131};
//                var dataGrid={"data":{ID:1231,PID:1221,NSX:'SASA',RKLSH:'12312313',TRPLX:'LX',TRPMC:'DAWDA',CPPCH:'1233131',GYSMC:'DADAD',KCSL:123,CKSL:2131}};


                var getGridData= $.loadJson($.contextPath + "/zztrplylb!searchSelectedTrpxx.json?rklshs="+rklshs);

                <%--$("#trplyGrid${idSuffix}").grid("option","data
                type","json");--%>
                <%--$("#trplyGrid${idSuffix}").grid("option","url",$.contextPath + "/zztrplylb!searchSelectedTrpxx.json?rklshs="+rklshs);--%>
                <%--$("#trplyGrid${idSuffix}").grid("reload");--%>
                for(var i=0;i<getGridData.data.length;i++){
                    $("#trplyGrid${idSuffix}").grid('addRowData',"tem_"+(i+1),getGridData.data[i]);
                }

                <%--$("#trplyGrid${idSuffix}").grid("addRowData",0,acolums);--%>

//                tdialogDivJQ.remove();
            }
        });
    }
    function reloadNsczx( scdabh , scdalx){
        if(!isEmpty(scdabh) && !isEmpty(scdalx)){
            var scdaObj = $.loadJson($.contextPath + "/zztrplylb!searchScdaObj.json?scdabh="+scdabh);
            if(!isEmpty(scdaObj)){
                //获得农事项下拉框数据
                var nsxJson = $.loadJson($.contextPath + "/zztrplylb!searchNsx.json?pid="+scdaObj.ID+"&scdalx="+scdalx);
                $("#NSXBH${idSuffix}").combobox("reload",nsxJson.data);
            }
        }
    }

    /**
     * 获得选中的投入品信息
     * @returns {string}
     */
    function checkedNodesIds(){

        var nodes = $('#treeUi').tree("getCheckedNodes");
        var rklshs = new Array();
        for(var node  in nodes){

            if(nodes[node].isParent){
                rklshs.push(nodes[node].id);
            }
        }
        return rklshs.toString();
    }

    function initZbgrid($cForm , $cGrid ){
        var $grid = $("#pGridId${idSuffix}");
        for( var i in $cGrid){
            var totalRecords = $grid.grid("option", "records");
            lastsel = "tem_"+(totalRecords + 1);
            var dataAdded = {
                PID:temId,
                SSQYBH : $cForm.SSQYBH,
                SSQY : $cForm.SSQY,
                DKBH : $cForm.DKBH,
                SCDA : $cForm.SCDA,
                NSXMC : $cForm.NSXMC,
                SCDALX : $cForm.SCDALX,
                NSXBH : $cForm.NSXBH,
                NSXMC : $cForm.NSXMC,
                TRPLX : $cGrid[i].TRPLX,
                TRPMC : $cGrid[i].TRPMC,
                TRPBH : $cGrid[i].RKLSH,
                SCPCH : $cGrid[i].CPPCH,
                GYSMC : $cGrid[i].GYSMC,
                CKSL : $cGrid[i].CKSL
            };
            $grid.grid("addRowData", lastsel, dataAdded, "last");
        }
    }

    //提交表单
    $("#trplyZbForm${idSuffix}").submit(function (){
        if (!$("#trplyForm${idSuffix}").form("valid")){CFG_message("页面校验不通过","error"); return false;}

        var $grid = $("#pGridId${idSuffix}");
        var formData = $("#trplyZbForm${idSuffix}").form("formData",this);


        //将领用人进行更改


        var gData = $grid.grid("getRowData");
        if(gData.length==0){CFG_message("列表没数据,无法保存！", "error");return false;}

        var isValid = $grid.grid('valid');
        var postData = {E_entityJson: $.config.toString(formData),
            E_dEntitiesJson: $.config.toString(gData)};
        if(isValid){//
            $.ajax({
                type: 'POST',
                url: $.contextPath +"/zztrplylb!saveTrplyxx_modify.json",
                data: postData,
                dataType:"json",
                async: false,
                success: function (data) {
                    if(data=='fail'){
                        CFG_message("出库数量大于入库数量！", "warning");
                        return false;}
                    $("#ID${idSuffix}").textbox("setValue",data);
                    CFG_message("操作成功！", "success");
//                    $grid.grid("option", "datatype", "json");
//                    $grid.grid("option","url", $.contextPath + "/zztrplylb!searchTrpscdaxx.json?pid="+data);
//                    $grid.grid("reload");

                },
                error: function () {
                    CFG_message("操作失败！", "error");
                }
            })
        }else{
            $.message("列表校验不通过");
        }
        return false;
    });

    function initUpdateCkxx(id){
        var jqGlobal = document.body;
        var dialogDivJQ = $("<div></div>").appendTo(jqGlobal);
        dialogDivJQ.dialog({
            appendTo: jqGlobal,
            modal: true,
            autoOpen: true,
            title: "投入品批次选择",
            maxWidth: 800,
            width: 800,
            height: 400,
            maxHeight: 400,
            resizable: false,
            position: {of: jqGlobal},
            onCreate: function (e, ui) {
                var fromHtml = '<div class="fillwidth colspan2 clearfix">' +
                        '<form id="trplyUpdForm${idSuffix}" method="post" class="coralui-form">' +
                        '<div class="app-inputdiv12" style="margin-left:15px;">' +
                        '<div class="toolbarsnav clearfix"><span style="color:#66CC00;font-size:16px;">农事项信息</span></div>'+
                        '</div>'+
                        '<div class="app-inputdiv6"><label class="app-input-label" >区域名称：</label><input id="SSQYBH${idSuffix}" name="SSQYBH" data-options="required:true,readonly:true" /></div>' +
                        '<div class="app-inputdiv6"><label class="app-input-label" >地块编号：</label><input id="DKBH${idSuffix}" name="DKBH" data-options="required:true,readonly:true" /></div>' +
                        '<div class="app-inputdiv6"><label class="app-input-label" >生产档案编号：</label><input id="SCDA${idSuffix}"  data-options="required:true,readonly:true"  name="SCDA"  /></div>'+
                        '<div class="app-inputdiv6"><label class="app-input-label" >作业项类型：</label><input id="SCDALX${idSuffix}" data-options="required:true,readonly:true" name="SCDALX" /></div>'+
                        '<div class="app-inputdiv6"><label class="app-input-label" >农事项名称：</label><input id="NSXBH${idSuffix}" data-options="required:true,readonly:true" name="NSXBH" /></div>'+
                        '<div class="app-inputdiv6" style ="display:none"><label class="app-input-label" >农事操作项：</label><input id="NSXMC${idSuffix}" data-options="required:true,readonly:true" name="NSXMC" class="coralui-textbox"/></div>'+
                        '<div class="app-inputdiv12" style="margin-left:15px;">' +
                        '<div class="toolbarsnav clearfix"><span style="color:#FF9900;font-size:16px;">投入品信息</span></div>'+
                        '</div>'+
                        '<div class="app-inputdiv6"><label class="app-input-label" >投入品类型：</label><input id="TRPLX${idSuffix}" name="TRPLX" data-options="required:true,readonly:true" /></div>' +
                        '<div class="app-inputdiv6"><label class="app-input-label" >投入品名称：</label><input id="TRPBH${idSuffix}" name="TRPBH" data-options="required:true" /></div>' +
                        '<div class="app-inputdiv6"><label class="app-input-label" >供应商名称：</label><input id="GYSMC${idSuffix}" name="GYSMC"  class="coralui-textbox" data-options="required:true,readonly:true"  /></div>'+
                        '<div class="app-inputdiv6"><label class="app-input-label" >库存数量：</label><input id="KCSL${idSuffix}" name="KCSL"  class="coralui-textbox" data-options="required:true,readonly:true"/></div>'+
                        '<div class="app-inputdiv6"><label class="app-input-label" >出库数量：</label><input id="CKSL${idSuffix}" name="CKSL" class="coralui-textbox" data-options="required:true"/></div>'+
                        '<div class="app-inputdiv6" style="display:none">' +
                        '<label class="app-input-label" >投入品入库流水号：</label><input id="OLDTRPBH${idSuffix}" name="OLDTRPBH" class="coralui-textbox" data-options="required:true" />' +
                        '<label class="app-input-label" >ID：</label><input id="ID${idSuffix}" name="ID" class="coralui-textbox" data-options="required:true" />' +
                        '<label class="app-input-label" >PID：</label><input id="PID${idSuffix}" name="PID" class="coralui-textbox" data-options="required:true" />' +
                        '<label class="app-input-label" >投入品名称：</label><input id="TRPMC${idSuffix}" name="TRPMC" class="coralui-textbox" data-options="required:true" />' +
                        '<label class="app-input-label" >投入品出库数量：</label><input id="OLDCKSL ${idSuffix}" name="OLDCKSL" class="coralui-textbox" data-options="required:true" />' +
                        '</div>' +
                        '</form>'+
                        '</div>';
                $(fromHtml).appendTo(dialogDivJQ);
                $.parser.parse(e.target);
                initTrylyForm();
                initTrplyUpdForm(id);
            },
            onOpen : function ( e, ui){
                var jqPanel = $(this).dialog("buttonPanel").addClass("app-bottom-toolbar"),
                        jqDiv   = $("<div style=\"height:35px;\" class=\"dialog-toolbar\">").appendTo(jqPanel);
                jqDiv.toolbar({
                    data: ["->", {id:"save", label:"保存", type:"button"}, {id:"cancel", label:"取消", type:"button"}],
                    onClick: function(e, ui) {
                        if ("save" === ui.id) {//执行保存操作
                            var $grid = $("#pGridId${idSuffix}");
                            <%--var gData = $grid.grid("getRowData");--%>
                            var $form = $("#trplyUpdForm${idSuffix}");
                            var $formData = $("#trplyUpdForm${idSuffix}").form("formData",this);
                            if($formData.TRPBH == $formData.OLDTRPBH){
                                var lastNum = parseInt($formData.KCSL) + parseInt($formData.OLDCKSL) - parseInt($formData.CKSL);
                                if(lastNum < 0){
                                    CFG_message("投入品出库数量大于库存数量");
                                    return;
                                }
                            }else {
                                if (parseInt($formData.KCSL) < parseInt($formData.CKSL)) {
                                    CFG_message("投入品出库数量大于库存数量");
                                    return;
                                }
                            }
                            var isValid = $form.form("valid");
                            if(!isValid) return ;
                            var dataAdded = {
//                                ID : $formData.ID,
//                                PID: $formData.PID,
//                                SSQYBH : $formData.SSQYBH,
//                                SSQY : $formData.SSQY,
//                                DKBH : $formData.DKBH,
//                                SCDA : $formData.SCDA,
//                                NSXMC : $formData.NSXMC,
//                                SCDALX : $formData.SCDALX,
//                                NSXBH : $formData.NSXBH,
//                                NSXMC : $formData.NSXMC,
//                                TRPLX : $formData.TRPLX,
                                TRPMC : $formData.TRPMC,
                                TRPBH : $formData.RKLSH,
                                SCPCH : $formData.CPPCH,
                                GYSMC : $formData.GYSMC,
                                CKSL : $formData.CKSL
                            };
                            $grid.grid("setRowData",id,dataAdded);
                            //直接更新数据库
                            $("#trplyZbForm${idSuffix}").form().submit();
                        }
                        dialogDivJQ.dialog("close");
                    }
                });
            },onClose : function ( e, ui){
                dialogDivJQ.remove();
            }
        });

    }

    function initTrplyUpdForm(id){
        //加载投入品类型
        $("#TRPLX${idSuffix}").combobox({
            valueField: 'value',
            textField: 'name',
            sortable: true,
            url: $.contextPath + "/zztrpcglb!searchTrplx.json"
        });
        var $form = $("#trplyUpdForm${idSuffix}");
        var $formData ;
        var trplx = "";
        //获得列表上面的数据
        $formData = $("#pGridId${idSuffix}").grid("getRowData",id);
        var trplxCombobox = $("#pGridId${idSuffix}").grid("getCellComponent",id ,"TRPLX");
        trplx = trplxCombobox.combobox("getValue");
        var trpData = $.loadJson($.contextPath + "/zztrplylb!searchTrpmc.json?trplx="+$formData.TRPLX);
        $("#TRPBH${idSuffix}").combobox({
            valueField: 'TRPBH',
            textField: 'TRPMC',
            sortable: true,
            data : trpData,
            onChange : function (  e ,data){
                $("#TRPMC${idSuffix}").textbox("setValue",data.text);
            }
        });
        $formData.OLDTRPBH = $formData.TRPBH;
        reloadNsczx($formData.SCDA,$formData.SCDALX);
        var trpxx = $.loadJson($.contextPath + "/zztrplylb!searchTrpxx.json?trpbh="+$formData.TRPBH);
        $formData.KCSL = trpxx.KCSL;
        $formData.OLDCKSL = $formData.CKSL;
        $form.form("loadData",$formData);
    }

    function searchNsx(){

        //清空列表数据
        <%--$('#pGridId${idSuffix}').empty();--%>
        $('#pGridId${idSuffix}').grid('clearGridData');


        var lyr=$('#LYRBH${idSuffix}').combobox("getValue");
        var qssj=$('#LYSJ${idSuffix}').datepicker("getValue");
        var jssj=$('#JSLYSJ${idSuffix}').datepicker('getValue');

        if(lyr==''||qssj==''||jssj==''){return;}
        <%--$('#pGridId${idSuffix}').grid('clearGridData');--%>
//        var nsxGrid=$.loadJson($.contextPath + "/zztrplylb!searchTrpxx.json?lyr="+lyr+"&qssj="+qssj+"&jssj="+jssj);
        var getgridData=$.loadJson($.contextPath+"/zztrplylb!searchNsxgrid.json?lyr="+lyr+"&qssj="+qssj+"&jssj="+jssj);

        pGrid=$('#pGridId${idSuffix}');
//        pGrid.grid("option", "datatype", "json");
//        pGrid.grid('option','url',$.contextPath+"/zztrplylb!searchNsxgrid.json?E_frame_name=coral&E_model_name=datagrid&lyr="+lyr+"&qssj="+qssj+"&jssj="+jssj);
//        pGrid.grid('reload');
        pGrid.grid('reload',{});
        for(var len=0;len<getgridData.data.length;len++){
            pGrid.grid('addRowData',len,getgridData.data[len]);
            var trpbh=getgridData.data[len].TRPBH;
//            var scpch=getgridData.data[len].SCPCH;
            var scpch=11;
            var gysData=$.loadJson($.contextPath + "/zztrplylb!getGysData.json?trpbh="+trpbh+"&scpch="+scpch);
            $("#pGridId${idSuffix}").grid("getCellComponent", len, "GYSMC").combobox({
                textField:"GYSMC",
                valueField:"GYSBH",
            });
            $("#pGridId${idSuffix}").grid("getCellComponent", len, "GYSMC").combobox("reload", gysData);

            if(gysData.length==0){continue;}
            $("#pGridId${idSuffix}").grid("getCellComponent", len, "GYSMC").combobox("setValue", gysData[0].GYSBH);
            <%--var trpbh=$("#pGridId${idSuffix}").grid('getRowData',event.data.rowId).TRPBH;--%>
            <%--var gysbh=$("#pGridId${idSuffix}").grid('getRowData',len).GYSMC;--%>
            var gysbh=gysData[0].GYSBH;

            var pchData=$.loadJson($.contextPath + "/zztrplylb!getScpchData.json?trpbh="+trpbh+"&gysbh="+gysbh);

            $("#pGridId${idSuffix}").grid("getCellComponent", len, "RKLSH").combobox({
                textField:"RKLSH",
                valueField:"RKLSHBH",
            });
            $("#pGridId${idSuffix}").grid("getCellComponent", len, "RKLSH").combobox("reload", pchData);
            $("#pGridId${idSuffix}").grid("getCellComponent", len, "RKLSH").combobox("setValue", pchData[0].RKLSHBH);
            var ckslData=$.loadJson($.contextPath + "/zztrplylb!getCkslData.json?rklsh="+pchData[0].RKLSH);
            $("#pGridId${idSuffix}").grid("getCellComponent", len, "KCSL").textbox('setValue',ckslData.KCSL);

            <%--$("#pGridId${idSuffix}").grid("setColProp", "GYSMC",{"formatoptions":{"data":gysData,"textField":"GYSMC","valueField":"GYSBH",--%>
            <%--}});--%>
            <%--$("#pGridId${idSuffix}").grid("setColProp", "SCPCH",{"formatoptions":{"textField":"name","valueField":"value",--%>
            <%--}});--%>
        }


    }


</script>
