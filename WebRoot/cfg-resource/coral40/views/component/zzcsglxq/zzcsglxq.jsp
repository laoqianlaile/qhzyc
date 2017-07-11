<%@ page import="com.ces.config.utils.CommonUtil"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="ces" tagdir="/WEB-INF/tags"%>
<%
  String path = request.getContextPath();
  String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
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
  <div class="fill">
    <div class="toolbarsnav clearfix">
      <ces:toolbar id="toolbarId${idSuffix}" onClick="$.ns('namespaceId${idSuffix}').toolbarClick"
                   data="['->',{'label': '关闭', 'id':'CFG_closeComponentZone','cls':'return_tb', 'disabled': 'false','type': 'button'}]">
      </ces:toolbar>
      <div class='homeSpan' style="margin-top: -23px;"><div><div style='margin-left:20px;width: 150px;' id="nva${idSuffix}"> - 分拣管理 - 详情 </div></div></div>
      <%--[{'label': '保存', 'id':'save', 'disabled': 'false','type': 'button'}]--%>
    </div>
    <form id="csForm${idSuffix}" action="zzcsgl!saveCsxx.json"
          enctype="multipart/form-data" method="post" class="coralui-form">
      <div class="app-inputdiv4" style ="height:32px;display: none">
        <%--<input id="ID${idSuffix}" class="coralui-textbox" name="ID"/>--%>
      </div>

      <div class="fillwidth colspan3 clearfix">
        <!------------------ 第一排开始---------------->
        <div class="app-inputdiv4">
          <label class="app-input-label" >采收流水号：</label>
          <input id="CSLSH${idSuffix}" name="CSLSH" readonly="readonly" class="coralui-textbox"/>
        </div>
        <div class="app-inputdiv4">
          <label class="app-input-label" >区域名称：</label>
          <input id="QYBH${idSuffix}" name="QYBH"  data-options="required:true"/>

        </div>
        <div class="app-inputdiv4">
          <label class="app-input-label" >地块名称：</label>
          <input id="DKBH${idSuffix}" name="DKBH"  data-options="required:true"/>

        </div>
        <!------------------ 第一排结束---------------->

        <!------------------ 第二排开始---------------->
        <div class="app-inputdiv4">
          <label class="app-input-label" >种植单元名称：</label>
          <input id="ZZDYBH${idSuffix}" name="ZZDYBH" data-options="required:true" />

        </div>
        <div class="app-inputdiv4">
          <label class="app-input-label" >生产档案编号：</label>
          <input id="SCDABH${idSuffix}" name="SCDABH" data-options="required:true" />
        </div>
        <div class="app-inputdiv4">
          <label class="app-input-label" >品类：</label>
          <input id="PL${idSuffix}" name="PL" readonly="readonly" class="coralui-textbox" data-options="required:true"/>
        </div>
        <!------------------ 第二排结束---------------->

        <!------------------ 第三排开始---------------->
        <div class="app-inputdiv4">
          <label class="app-input-label" >品种：</label>
          <input id="PZ${idSuffix}" name="PZ" readonly="readonly" data-options="required:true" class="coralui-textbox"/>
        </div>

        <div class="app-inputdiv4">
          <label class="app-input-label" >重量合计：</label>
          <input id="ZLHJ${idSuffix}" name="ZLHJ" readonly="readonly"  class="coralui-textbox"/>
        </div>
        <div  class="app-inputdiv4">
          <label class="app-input-label" >采收时间：</label>
          <ces:datepicker id="CSSJ${idSuffix}" name="CSSJ" dateFormat="yyyy-MM-dd HH:mm:ss" required="true"></ces:datepicker>
          <%--<input id="CSSJ${idSuffix}" name="CSSJ" class="coralui-textbox"/>--%>
          <%--<input id="TP${idSuffix}" name="TP" class="coralui-textbox" type="hidden"/>--%>
        </div>


        <div  class="app-inputdiv4" style="display:none">
          <input id="ID${idSuffix}" name="ID"  class="coralui-textbox" />
          <input id="QYBM${idSuffix}" name="QYBM"  class="coralui-textbox" />
          <input id="KCZL${idSuffix}" name="KCZL"  class="coralui-textbox" />
          <input id="YCCZL${idSuffix}" name="YCCZL"  class="coralui-textbox" />
          <input id="ZZDYMC${idSuffix}"  name="ZZDYMC"  class="coralui-textbox"/>
          <input id="DKMC${idSuffix}" name="DKMC"  class="coralui-textbox"/>
          <input id="QVMC${idSuffix}" name="QVMC"   class="coralui-textbox"/>
          <input id="PZBH${idSuffix}" name="PZBH"   class="coralui-textbox"/>
          <input id="PLBH${idSuffix}" name="PLBH"   class="coralui-textbox"/>

        </div>


      </div>
    </form>

    <div class="toolbarsnav clearfix">

    </div>
    <%--<ces:grid id="gridId${idSuffix}" afterInlineSaveRow="afterInlineSaveRow"--%>
              <%--shrinkToFit="true" forceFit="true" fitStyle="fill" rownumbers="true" datatype="local" editableRows="true"  >--%>
      <%--<ces:gridCols>--%>
        <%--<ces:gridCol name="ID" editable="false" hidden="true"  width="100">ID</ces:gridCol>--%>
        <%--<ces:gridCol name="PID" editable="false" hidden="true"  width="80">PID</ces:gridCol>--%>
        <%--&lt;%&ndash;<ces:gridCol name="XH" editable="false" edittype="text" editoptions="required: true" width="180">序号</ces:gridCol>&ndash;%&gt;--%>
        <%--<ces:gridCol name="ZLDJ" edittype="combobox" editable="false" revertCode="true"  width="180">质量等级</ces:gridCol>--%>
        <%--<ces:gridCol name="PCH"  editable="false" edittype="text" editoptions="required: false" width="180">批次号</ces:gridCol>--%>
        <%--<ces:gridCol name="ZL" editable="true" edittype="text" editoptions="required: false" width="180">重量(KG)</ces:gridCol>--%>
        <%--<ces:gridCol name="KCZL" editable="false" edittype="text" editoptions="required: false" width="180">库存重量(KG)</ces:gridCol>--%>
        <%--<ces:gridCol name="DYZS" editable="true" edittype="text" editoptions="required: false" width="80">打印张数</ces:gridCol>--%>
        <%--<ces:gridCol name="CZ" editable="false" formatter="toolbar"--%>
                     <%--formatoptions="onClick:$.ns('namespaceId${idSuffix}').toolbarClick3,--%>
						             <%--data:[{'label': '编辑', 'id':'bjBtn', 'disabled': 'false','type': 'button'}]" width="220">操作</ces:gridCol>--%>
      <%--</ces:gridCols>--%>
    <%--</ces:grid>--%>
    <ces:grid id="gridId${idSuffix}" afterInlineSaveRow="afterInlineSaveRow"
              shrinkToFit="true" forceFit="true" fitStyle="fill" rownumbers="true" datatype="local">
      <ces:gridCols>
        <ces:gridCol name="ID" editable="false"   hidden="true"  width="100">ID</ces:gridCol>
        <ces:gridCol name="PID" editable="false"  hidden="true" width="80">PID</ces:gridCol>
        <%--<ces:gridCol name="XH" editable="false" edittype="text" editoptions="required: true" width="180">序号</ces:gridCol>--%>
        <ces:gridCol name="ZLDJ" edittype="combobox" align="center" editable="false" revertCode="true"  width="180">质量等级</ces:gridCol>
        <ces:gridCol name="PCH"  editable="false" align="center" edittype="text" editoptions="required: false" width="180">批次号</ces:gridCol>
        <ces:gridCol name="ZL" formatter="text" align="center" formatoptions="required: false,readonly:true" width="80">重量(KG)</ces:gridCol>
        <ces:gridCol name="KCZL" editable="false" hidden="true" edittype="text" editoptions="required: false" width="180">库存重量(KG)</ces:gridCol>
        <ces:gridCol name="DYZS" formatter="text" align="center" formatoptions="required: false,readonly:true" width="80">打印张数</ces:gridCol>
        <ces:gridCol name="CZ" editable="false" hidden="true" formatter="toolbar"
                     formatoptions="onClick:$.ns('namespaceId${idSuffix}').toolbarClick3,
						             data:[{'label': '编辑', 'id':'bjBtn', 'disabled': 'false','type': 'button'}]" width="220">操作</ces:gridCol>
      </ces:gridCols>
    </ces:grid>
    <div id="jqUC" style="display: none"></div>
  </div>
</div>
<script type="text/javascript">

  var ycczl=0;
  var rownumber;
  $("#DKBH${idSuffix}").combobox({
  valueField:'DKBH',
  textField:'DKMC',
  sortable: true,
  width: "auto"
  });
  $("#DKBH${idSuffix}").combobox("disable");
  $("#ZZDYBH${idSuffix}").combobox({
    valueField:'ZZDYBH',
    textField:'ZZDYMC',
    sortable: true,
    width: "auto"
  });
  $("#SCDABH${idSuffix}").combobox({
    valueField:'SCDABH',
    textField:'SCDABH',
    sortable: true,
    width: "auto"
  });
  $("#ZZDYBH${idSuffix}").combobox("disable");
  $("#SCDABH${idSuffix}").combobox('disable');
  var gData;
  var temId="tem${idSuffix}";
  $.extend($.ns("namespaceId${idSuffix}"), {
    cdmcClick: function () {
      CFG_clickButtonOrTreeNode($('#max${idSuffix}').data('configInfo'), "lsxzqh", "地区名称", 2, $.ns("namespaceId${idSuffix}"));
    },
    setComboGridValue_lsxzqh:function ( o ){
      if( null == o ) return ;
      var obj = o.rowData;
      if( null ==  obj) return ;
      $("#LSXZQH${idSuffix}").combogrid("setValue",obj.CDMC);
      $("#LSXZQHBM${idSuffix}").textbox("setValue",obj.CDBM);
    },
    toolbarClick : function(event, button) {

      if (button.id == "save") {//保存的方法
        if (!$("#csForm${idSuffix}").form("valid")){
          CFG_message("页面校验不通过","error");
          return false;
        }
        $("#csForm${idSuffix}").form().submit();
        var pid=$("#ID${idSuffix}").textbox("getValue");
        <%--$("#gridId${idSuffix}").grid("option", "datatype", "json");--%>
        <%--$("#gridId${idSuffix}").grid("option","url", $.contextPath + "/zzcsgl!searchNzwxq.json?pid="+pid);--%>
        <%--$("#gridId${idSuffix}").grid("reload");--%>

        <%--//保存的同时打印--%>
        <%--if(isSwt){--%>

          <%--var obj = $.loadJson($.contextPath + "/trace!getQybmAndQymc.json?sysName=ZZ");--%>
          <%--var qymc = obj.qymc;--%>
          <%--var qvmc=$("#QYMC${idSuffix}").combobox('getValue');--%>
          <%--var dkmc=$("#DKMC${idSuffix}").combobox('getText');--%>
          <%--var pz=$("#PZ${idSuffix}").textbox('getValue');--%>
          <%--var cssj=$("#CSSJ${idSuffix}").datepicker('option','getValue');--%>
          <%--for(var numi=0;numi<rownumber-1;numi++){--%>
            <%--var printtimes--%>
            <%--var pch=$("#gridId${idSuffix}").grid("getRowData","tem_"+numi).PCH;--%>
            <%--if($("#gridId${idSuffix}").grid("getRowData","tem_"+numi).DYZS!=''){--%>
              <%--printtimes=parseInt($("#gridId${idSuffix}").grid("getRowData","tem_"+numi).DYZS);}else{return;}--%>
            <%--//(qymc+'   '+dkmc+'  '+pz+'   '+cssj);--%>
            <%--printdata={--%>
              <%--scqy:qymc,--%>
              <%--qy:qvmc,--%>
              <%--dk:dkmc,--%>
              <%--pz:pz,--%>
              <%--cssj:cssj,--%>
              <%--cspch:pch--%>
            <%--};--%>
            <%--for(var times=0;times<printtimes;times++){--%>
              <%--var result=_window('printSczzCs',JSON.stringify(printdata));--%>

              <%--if (resultData.status == "true" || resultData.status == true) {--%>
                <%--//var savePrint = $.loadJson($.contextPath + "/zzbzgl!savePrint.json?bzlsh="+rowData.BZLSH+"&bzxs=xbz&cpzsm="+cpzsm);--%>
                <%--if(savePrint != true ){--%>
                  <%--$.alert("打印失败：" + resultData.msg);--%>
                  <%--return false;--%>
                <%--}--%>
              <%--} else {--%>
                <%--$.alert("打印成功：" + resultData.msg);--%>
                <%--return false;--%>
              <%--}}}}else{CFG_message("请在环境中操作","error");}--%>



      } else if (button.id == "CFG_closeComponentZone") {
        //CFG_clickReturnButton($('#max${idSuffix}').parent().data('parentConfigInfo'));
        CFG_clickCloseButton($('#max${idSuffix}').data('configInfo'));
        //给列表每行的pid赋值
      }
      <%--else{--%>

        <%--if(isSwt){--%>
          <%--var obj = $.loadJson($.contextPath + "/trace!getQybmAndQymc.json?sysName=ZZ");--%>
          <%--var qymc = obj.qymc;--%>
          <%--var qvmc=$("#QYMC${idSuffix}").combobox('getValue');--%>
          <%--var dkmc=$("#DKMC${idSuffix}").combobox('getText');--%>
          <%--var pz=$("#PZ${idSuffix}").textbox('getValue');--%>
          <%--var cssj=$("#CSSJ${idSuffix}").datepicker('option','getValue');--%>
          <%--for(var numi=0;numi<rownumber-1;numi++){--%>
            <%--var printtimes--%>
            <%--var pch=$("#gridId${idSuffix}").grid("getRowData","tem_"+numi).PCH;--%>
            <%--if($("#gridId${idSuffix}").grid("getRowData","tem_"+numi).DYZS!=''){--%>
             <%--printtimes=parseInt($("#gridId${idSuffix}").grid("getRowData","tem_"+numi).DYZS);}else{return;}--%>
            <%--//(qymc+'   '+dkmc+'  '+pz+'   '+cssj);--%>
            <%--printdata={--%>
              <%--scqy:qymc,--%>
              <%--qy:qvmc,--%>
              <%--dk:dkmc,--%>
              <%--pz:pz,--%>
              <%--cssj:cssj,--%>
              <%--cspch:pch--%>
            <%--};--%>
            <%--for(var times=0;times<printtimes;times++){--%>
              <%--var result=_window('printSczzCs',JSON.stringify(printdata));--%>

              <%--if (resultData.status == "true" || resultData.status == true) {--%>
                <%--//var savePrint = $.loadJson($.contextPath + "/zzbzgl!savePrint.json?bzlsh="+rowData.BZLSH+"&bzxs=xbz&cpzsm="+cpzsm);--%>
                <%--if(savePrint != true ){--%>
                  <%--$.alert("打印失败：" + resultData.msg);--%>
                  <%--return false;--%>
                <%--}--%>
              <%--} else {--%>
                <%--$.alert("打印成功：" + resultData.msg);--%>
                <%--return false;--%>
              <%--}}}}else{CFG_message("请在环境中操作","error");}--%>
      <%--}--%>
    },
    toolbarClick1 : function(event, button) {
      var $grid = $("#gridId${idSuffix}");
      if (button.id == "add") {
        var pch='';
        var $zldj= $.loadJson($.contextPath + "/zzcsgl!getZldj.json");

        $grid.grid("setColProp", "ZLDJ",{"formatoptions":{"data":$zldj,"textField":"name","valueField":"value",onChange:function(e,data){
          pch=data.value;
          var pchtrue=$("#CSLSH${idSuffix}").textbox("getValue")+pch;
          $grid.grid("setCell", e.data.rowId,'PCH',pchtrue);
          rownumber=e.data.rowId;
        }}});
        var pid=document.getElementById('ID${idSuffix}').value;
        $grid.grid("addRowData", "last");
        var totalRecords = $grid.grid("option", "records");
        var index=(totalRecords + 1);
        lastsel = "tem_"+index;
        var dataAdded = {
          PID:pid,
        };
        $grid.grid("addRowData", lastsel, dataAdded, "last");
        $grid.grid('editRow', lastsel);
      }
    },
    toolbarClick3 : function(e, ui) {
      var rowId = e.data.rowId;
      var $grid = $("#gridId${idSuffix}");
      if (ui.id == "delBtn") {
        gData = $grid.grid("getRowData",rowId);

        var gDataId = gData.ID;
        if(gDataId.indexOf("span") != -1 || gDataId.indexOf("tem") !=-1){//如果是删除编辑状态的数据并且是没有进行保存的数据则执行grid的remove
          $grid.grid( 'clearEdited', rowId );
          $grid.grid("delRowData",rowId);
        }else{
          $.ajax({
            type:"post",
            url: "${basePath}zzcsgl!deleteNzwxq.json",
            data:{id:gDataId},
            dataType:"json",
            success :function (res){
              CFG_message("删除成功！", "success");
              $grid.grid("delRowData",rowId);
            },error :function (res){
              CFG_message("删除失败！", "error");
            }
          })
        }


      }
      if(ui.id == "bjBtn"){
        $grid.grid('editRow', rowId);
      }
      if(ui.id == "uploadBtn"){
        gData = $grid.grid("getRowData",rowId);
        if(!isEmpty(gData)){
          var gDataId = gData.ID;
          if(gDataId.indexOf("span") != -1){
            $.alert("请先更新信息！");
          }else{
            showDialog(rowId);
          }
        }

      }
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
  var buttonOpts = {
    id: "combogrid_buttonId",
    label: "构件按钮",
    icons: "icon-checkmark-circle",
    text: false,
    onClick: $.ns('namespaceId${idSuffix}').cdmcClick
  };
  var _colNames = ["地区编号", "地区名称"];
  var _colModel = [{name: "CDBM", width: 65, sortable: true}, {name: "CDMC", width: 155, sortable: true}];
  $(function() {
    var configInfo = CFG_initConfigInfo({
      /** 页面名称 */
      'page' : 'zzkhxxgl.jsp',
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
        CFG_setCloseButton(configInfo, $('#toolbarId1${idSuffix}'));
      },
      /** 页面初始化的方法 */
      'bodyOnLoad' : function(configInfo) {


        var cslsh = $.loadJson($.contextPath + "/zzcsgl!getCslsh.json");
        $("#CSLSH${idSuffix}").textbox("setValue",cslsh);
        //已出场重量初始化为0
        $("#YCCZL${idSuffix}").textbox("setValue",0);
        //页面初始化进行默认设置
        var inputParamId =CFG_getInputParamValue(configInfo, 'rowDataId');
        $("#QYBH${idSuffix}").combobox({
          valueField:'QYBH',
          textField:'QYMC',
          sortable: true,
          width: "auto",
          url:$.contextPath + "/zzcsgl!getQvmc.json"});
        //区域编号的onchange事件
        $("#QYBH${idSuffix}").combobox('option','onChange',function(e,data){
           var value=data.value;
          $("#QVMC${idSuffix}").textbox("setValue",data.text);
          $("#ZZDYMC${idSuffix}").textbox("setValue",'');
          $("#ZZDYBH${idSuffix}").combobox('setValue','');
          $("#DKBH${idSuffix}").combobox('enable');
          $("#SCDABH${idSuffix}").combobox('disable');
          $("#ZZDYBH${idSuffix}").combobox('disable');
          var jsonData = $.loadJson($.contextPath + "/zzcsgl!getDkbh.json?QYBH="+value);
          $("#DKBH${idSuffix}").combobox("reload",jsonData);
          $("#DKMC${idSuffix}").textbox("setValue",'');
          $("#SCDABH${idSuffix}").combobox('setValue','');
          $("#PZ${idSuffix}").textbox("setValue",'');
          $("#PL${idSuffix}").textbox("setValue",'');
          $("#PLBH${idSuffix}").textbox("setValue",'');
          $("#PZBH${idSuffix}").textbox("setValue",'');
        });
        //地块编号的onchange事件
        $("#DKBH${idSuffix}").combobox('option','onChange',function(e,data){
          var dkbh=data.text;
          document.getElementById("DKMC${idSuffix}").value=data.value;
          document.getElementById("ZZDYMC${idSuffix}").value='';
          $("#DKMC${idSuffix}").textbox('setValue',data.text);
          $("#ZZDYBH${idSuffix}").combobox('enable');
          $("#ZZDYBH${idSuffix}").combobox('setValue','');
          $("#ZZDYMC${idSuffix}").textbox('setValue','');
          $("#SCDABH${idSuffix}").combobox('setValue','');
          $("#PZ${idSuffix}").textbox("setValue",'');
          $("#PL${idSuffix}").textbox("setValue",'');
          $("#PLBH${idSuffix}").textbox("setValue",'');
          $("#PZBH${idSuffix}").textbox("setValue",'');
          var jsonData = $.loadJson($.contextPath + "/zzcsgl!getZzdybh.json?DKBH="+dkbh);
          $("#ZZDYBH${idSuffix}").combobox("reload",jsonData);
        });
        //种植单元编号的onchange时间给生产档案下拉框赋予数值
        $("#ZZDYBH${idSuffix}").combobox('option','onChange',function(e,data){
          var zzdybh=data.text;
          var zzdymc=data.value;
          var jsonData=$.loadJson($.contextPath + "/zzcsgl!getScdabh.json?ZZDYBH="+zzdybh);
          $("#SCDABH${idSuffix}").combobox("reload",jsonData);
          $("#SCDABH${idSuffix}").combobox('enable');
          $("#ZZDYBH${idSuffix}").textbox('setValue',data.text);
          document.getElementById('PL${idSuffix}').value='';
          document.getElementById('PZ${idSuffix}').value='';
          document.getElementById('ZZDYBH${idSuffix}').value=data.value;
        });
        //生产档案的onchange时间自动带入品种，品类和重量合计
        $("#SCDABH${idSuffix}").combobox('option','onChange',function (e,data){
          var scdabh=data.text;
          var jsonData = $.loadJson($.contextPath + "/zzcsgl!getPlandPz.json?scdabh="+scdabh);
          var jsonDatabh = $.loadJson($.contextPath + "/zzcsgl!getPlbhandPzbh.json?scdabh="+scdabh);
          document.getElementById('PL${idSuffix}').value=jsonData.PL;
          document.getElementById('PZ${idSuffix}').value=jsonData.PZ;
          document.getElementById('PZBH${idSuffix}').value=jsonDatabh.PZBH;
          document.getElementById('PLBH${idSuffix}').value=jsonDatabh.PLBH;
        })
        //初始化完成后，将表单设为只读。alan
        $("#csForm${idSuffix}").form("setReadOnly",true);

        var $grid = $("#gridId${idSuffix}");
        //初始化列表
        var $zldj= $.loadJson($.contextPath + "/zzcsgl!getZldj.json");
        $grid.grid("setColProp", "ZLDJ",{"editoptions":{"data":$zldj,"textField":"name","valueField":"value"}});

        if(isEmpty(inputParamId)){//如果是新增操作在设置临时ID
          var obj = $.loadJson($.contextPath + "/trace!getQybmAndQymc.json?sysName=ZZ");
          var qybm=obj.qybm;
          var qymc = obj.qymc;
          $("#QYBM${idSuffix}").textbox("setValue",qybm);
          //初始化列表
          var $zldj= $.loadJson($.contextPath + "/zzcsgl!getZldj.json");
          $grid.grid("setColProp", "ZLDJ",{"editoptions":{"data":$zldj,"textField":"name","valueField":"value"}});

          //新增时在增加初始化列表
          rownumber=$zldj.length;
          for(var i=0;i<$zldj.length;i++){
            var pid=$('#ID${idSuffix}').textbox('getValue');
            $grid.grid("addRowData", "last");
            var totalRecords = $grid.grid("option", "records");
            lastsel = "tem_"+i;
            var dataAdded = {
              PID:pid,
            };
            $grid.grid("addRowData", lastsel, dataAdded, "last");
            $grid.grid("setCell", lastsel,'ZLDJ',i+1);
            $grid.grid("setCell", lastsel,'PCH',cslsh+''+(i+1));
            $grid.grid("setCell",lastsel,'PID',$('#ID${idSuffix}').textbox('getValue'));
          }


        }else{//修改操作初始化页面操作
          var $csObj = $.loadJson($.contextPath + "/zzcsgl!searchCsxx.json?id="+inputParamId);
          qyvalue=$("#QYBH${idSuffix}").combobox('getValue');
          var dkObj = $.loadJson($.contextPath + "/zzcsgl!getDkbh.json?QYBH="+$csObj.QYBH);
          $("#DKBH${idSuffix}").combobox('reload',dkObj);
          $("#DKBH${idSuffix}").combobox({
            valueField:'DKBH',
            textField:'DKMC',
            sortable: true,
            width: "auto",
            url:$.contextPath + "/zzcsgl!getDkbh.json?QYBH="+$csObj.QYBH
          });
          var zzObj = $.loadJson($.contextPath + "/zzcsgl!getZzdybh.json?DKBH="+$csObj.DKBH);
          $("#ZZDYBH${idSuffix}").combobox('reload',zzObj);
          dkvalue=$("#DKBH${idSuffix}").combobox('getValue');
          $("#ZZDYBH${idSuffix}").combobox({
            valueField:'ZZDYBH',
            textField:'ZZDYMC',
            sortable: true,
            width: "auto",
            url:$.contextPath + "/zzcsgl!getZzdybh.json?DKBH="+$csObj.DKBH
          });
          $("#csForm${idSuffix}").form("loadData",$csObj);
          //列表数据加载

          var pid=$("#ID${idSuffix}").textbox("getValue");
          $grid.grid("option", "datatype", "json");
          $grid.grid("option","url", $.contextPath + "/zzcsgl!searchNzwxq.json?pid="+pid);
          $grid.grid("reload");
          var s=1;
      }
    }});
    if (configInfo) {
    }
    configInfo.CFG_outputParams = {'success':'otp'};
  });
  //表单的更新方法
  function afterInlineSaveRow(e, data){
    if($("#ID${idSuffix}").textbox("getValue")==''){CFG_message("请先将主表保存！", "error");

      $("#gridId${idSuffix}").grid("setCell",data.rowId,'ZL',null);
      $("#gridId${idSuffix}").grid("setCell",data.rowId,'KCZL',null);
      $("#gridId${idSuffix}").grid("setCell",data.rowId,'DYZS',null);
      return}else{
      //判断打印张数是否为空
      if($("#gridId${idSuffix}").grid("getRowData",data.rowId).DYZS==''){CFG_message("打印张数不能为空！", "error");
        $("#gridId${idSuffix}").grid("setCell",data.rowId,'ZL',null);
        $("#gridId${idSuffix}").grid("setCell",data.rowId,'KCZL',null);
        $("#gridId${idSuffix}").grid("setCell",data.rowId,'DYZS',null);
        return}
      var sk=data.rowId;
      var zlhj=0;
      var kczl=0;
      $("#gridId${idSuffix}").grid("editRow",data.rowid,"false");
      var gridData = $("#gridId${idSuffix}").grid("getRowData",data.rowId);
      $("#gridId${idSuffix}").grid("setCell",data.rowId,'KCZL',gridData.ZL);
      var cslsh=$("#CSLSH${idSuffix}").textbox("getValue");
      var in1=data.rowId.indexOf('_');
      var lastnum=data.rowId.substring(in1+1);
      lastnum=parseInt(lastnum);
      $("#gridId${idSuffix}").grid("setCell",data.rowId,'PCH',cslsh+''+lastnum);
      var kbh=$("#gridId${idSuffix}").grid("getCell","tem_"+0,'ZL');
      //获得所有品类重量
      for(var i=0;i<rownumber;i++){
        if($("#gridId${idSuffix}").grid("getRowData",1+i).ZL!=''){
          var dawdaw=$("#gridId${idSuffix}").grid("getRowData",1+i);
          var zdad=$("#gridId${idSuffix}").grid("getRowData",""+i+1).ZL;
          var zl=parseInt($("#gridId${idSuffix}").grid("getRowData",1+i).ZL);
          zlhj=parseInt(zlhj)+zl;}
      }
      //获得1到3等品重量值和
      for(var i=0;i<rownumber-1;i++){
        if($("#gridId${idSuffix}").grid("getRowData",1+i).ZL!=''){
        var zl=parseInt($("#gridId${idSuffix}").grid("getRowData",1+i).ZL);
        kczl=parseInt(kczl)+zl;}
      }
      var i=zlhj;
      var k=kczl;
      //重量合计和库存重量赋值
      $("#ZLHJ${idSuffix}").textbox('setValue',zlhj);
      $("#KCZL${idSuffix}").textbox('setValue',kczl);
    var obj = $.loadJson($.contextPath + "/trace!getQybmAndQymc.json?sysName=ZZ");
    var qybm=obj.qybm;
    formid= $("#ID${idSuffix}").textbox("getValue")
    $grid = $("#gridId${idSuffix}");
      var gData =$grid.grid("getRowData",data.rowId);

      $.ajax({
        url: $.contextPath+"/zzcsgl!saveNzwxq.json?qybm="+qybm,
        type:"post",
        data:gData,
        dataType:"json",
        success:function (res){

          var dataAdded={
            ID : res.ID,
            ZLDJ : res.ZLDJ,
            PCH : res.PCH,
            ZL : res.ZL,
            DYZS : res.DYZS,
            PID :res.PID,
          }
          //替换掉当前编辑的行数据：主要作用添加ID，防止再次编辑的时候多次添加重复的数据
          $grid.grid("setRowData", data.rowId, dataAdded);
          CFG_message("更新成功！", "success");
        },error:function (res){
          CFG_message("更新失败！", "error");
        }
      });
  //保存
      $.ajax({
        url: $.contextPath+"/zzcsgl!editNzwxq.json?zlhj="+i+"&kczl="+k+"&id="+formid,
        type:"post",
        dataType:"json",
        success:function (res){
          CFG_message("更新成功！", "success");
        },error:function (res){
          CFG_message("更新失败！", "error");
        }
      });

      //更新当前编辑行的数据
    }
  }
  $("#khForm${idSuffix}").submit(function (){
    var $grid = $("#gridId${idSuffix}");
    var editrow = $grid.grid("option", "editrow");
    var formdata = new FormData(this);
    var isValid = $grid.grid('valid', editrow);
    if(isValid){//
      $.ajax({
        type: 'POST',
        url: "${basePath}zzkhxxgllb!saveKhxx.json",
        data: formdata,
        /**
         *必须false才会自动加上正确的Content-Type
         */
        contentType: false,
        /**
         * 必须false才会避开jQuery对 formdata 的默认处理
         * XMLHttpRequest会对 formdata 进行正确的处理
         */
        processData: false,
        timestamp: false,
        success: function (data) {
          $("#ID${idSuffix}").textbox("setValue", data.ID);
          CFG_message("操作成功！", "success");
        },
        error: function () {
          CFG_message("操作失败！", "error");
        }
      })
    }else{
      $.message("列表校验不通过");
    }
    return false;
  })
  function showDialog(rowId){
    var jqUC = $("#jqUC");
    jqUC.dialog({
      modal : true,
      title : "请选择文件",
      width : 440,
      height : 220,
      resizable : false,
      position : {
        at: "center"
      },
      onClose : function() {
        jqUC.dialog("close");
      },
      onCreate : function() {
        var html = "<form id=\"mdtpForm${idSuffix}\" enctype=\"multipart/form-data\"  method=\"post\" class=\"coralui-form\" onSubmit = 'return submitForm(this)'  >" +
                "<input type='file' style='display:none' id='mdtpFile${idSuffix}'  onchange=\"viewImage(this)\"  name=\"mdtpFile\"/>" +
                "<input type='text'  name=\"ID\" value='"+gData.ID+"'/>" +
                "<button class='ctrl-toolbar-element ctrl-init ctrl-init-button coral-button coral-component coral-state-default coral-corner-all coral-button-text-only coral-toolbar-item-component'"
                +"type='button' onclick=\"$('#mdtpFile${idSuffix}').click()\">"
                +"<span class=\"coral-button-text \">选择图片</span>"
                +"</button><div id=\"preview${idSuffix}\" style=\"text-align:center\"></div></form>";
        //var jqDiv = $("<div class=\"app-inputdiv-full\" style=\"padding:10px 20px;\"></div>").appendTo(this);
        var jq = $(html).appendTo(this);
        //jq.textbox({width: 200, maxlength: 22});
      },
      onOpen : function() {
        $("div > div[role=dialog]").appendTo("form#mdtpForm${idSuffix}");
        var jqPanel = $(this).dialog("buttonPanel").addClass("app-bottom-toolbar"),
                jqDiv   = $("<div class=\"dialog-toolbar\">").appendTo(jqPanel);
        jqDiv.toolbar({
          data: ["->", {id:"sure", label:"确定", type:"button"}, {id:"cancel", label:"取消", type:"button"}],
          onClick: function(e, ui) {
            if ("sure" === ui.id) {
              var $grid = $("#gridId${idSuffix}");
              var dataFile = $("#mdtpFile${idSuffix}").val();
              var isValid = $grid.grid('valid', rowId);
              if(isValid){
                if(isEmpty(dataFile)){
                  $.alert("请选择图片");
                  return;
                }else{
                  $("#mdtpForm${idSuffix}").submit();
                  $grid.grid( 'clearEdited', rowId );
                  jqUC.dialog("close");
                  jqUC.empty();
                }

              }else{
                $.alert("当前编辑行校验不通过");
                jqUC.dialog("close");
                return;
              }

            } else {
              jqUC.dialog("close");
            }
          }
        });
      }
    });
  }
  //门店图片上传
  $("#mdtpForm${idSuffix}").submit(function(){
    alert("我是提交操作");
    return false;
  });
  function submitForm(form) {
    form =$("#mdtpForm${idSuffix}").form();
    var fileObj = document.getElementById('mdtpFile${idSuffix}').files[0];
    var formdata = new FormData();
    formdata.append("ID",gData.ID);
    formdata.append("mdtpFile",fileObj);

    $.ajax({
      type: 'POST',
      url: "${basePath}zzcsgl!saveMdtpxx.json",
      data: formdata,
      contentType: false,//必须false才会自动加上正确的Content-Type
      processData: false,//必须false才会避开jQuery对 formdata 的默认处理XMLHttpRequest会对 formdata 进行正确的处理
      timestamp: false,
      success: function (data) {
        CFG_message("操作成功！", "success");
      },
      error: function () {
        CFG_message("操作失败！", "error");
      }
    })
    return false;
  }
  function viewImage(fileInput) {
    if (window.FileReader) {
      var p = $("#preview${idSuffix}");
      var file = fileInput.files;
      for (var i = 0, f; f = file[i]; i++) {
        var fileReader = new FileReader();
        fileReader.onload = (function (file) {
          return function (e) {
            var span = document.createElement('span');
            span.innerHTML = '<img width="200px" style = "position:relative;margin:10px 5px 0px 0px" src = "' + this.result + '" alt = "' + file.name + '" />';
            p.append(span);
          }
        })(f);
        fileReader.readAsDataURL(f);
      }
    }
  }

  $("#csForm${idSuffix}").submit(function(){
    //判断用户输入的是否符合规范
    var $grid = $("#gridId${idSuffix}");

//判断列表中是否重量和打印张数是否同时填写
    //($grid.grid("getRowData","tem_"+0).DYZS!='')&&($grid.grid("getRowData","tem_"+0).ZL=='')
    for(var k=0;k<rownumber;k++){
      if((($grid.grid("getRowData","tem_"+k).DYZS!='')&&($grid.grid("getRowData","tem_"+k).ZL==''))||(($grid.grid("getRowData","tem_"+k).DYZS=='')&&($grid.grid("getRowData","tem_"+k).ZL!=''))){
        CFG_message("重量和打印张数必须同时填写！", "error");return false;
      }
    };
//判断是否下面列表全为空
    var flag=0;
    for(var k=0;k<rownumber;k++){
      if(($grid.grid("getRowData","tem_"+k).DYZS!='')&&($grid.grid("getRowData","tem_"+k).ZL!='')){flag++;}
    };
    if(flag==0){CFG_message("必须填写一行！", "error");return false;}
    var editrow = $grid.grid("option", "editrow");
    var formdata = new FormData(this);

    //var isValid = $grid.grid('valid', editrow);
    //将form中的数据保存到数据库中
      $.ajax({
        type: 'POST',
        url: $.contextPath +"/zzcsgl!saveCsxx.json",
        data: formdata,
        /**
         *必须false才会自动加上正确的Content-Type
         */
        contentType: false,
        /**
         * 必须false才会避开jQuery对 formdata 的默认处理
         * XMLHttpRequest会对 formdata 进行正确的处理
         */
        processData: false,
        timestamp: false,
        success: function (data) {

          var obj = $.loadJson($.contextPath + "/trace!getQybmAndQymc.json?sysName=ZZ");
          var qybm=obj.qybm;
          $("#ID${idSuffix}").textbox("setValue", data.ID);
         // $("#CSLSH${idSuffix}").textbox("setValue", data.CSLSH);
          //给每个表单的pidpid复制
          for(var k=0;k<rownumber;k++){
            $grid.grid("setCell","tem_"+k,'PID',$('#ID${idSuffix}').textbox('getValue'));
            $grid.grid("setCell","tem_"+k,'KCZL',$grid.grid("getRowData","tem_"+k).ZL);
          };
          //将列表中的数据存放到数据库中
          for(var m=0;m<rownumber;m++){
              $.ajax({
                url:$.contextPath+"/zzcsgl!saveNzwxq.json?qybm="+qybm,
                type:"post",
                data:$grid.grid("getRowData","tem_"+m),
                dataType:"json",
                success:function(res){
                  var pid=$("#ID${idSuffix}").textbox("getValue");
                  $grid.grid("option", "datatype", "json");
                  $grid.grid("option","url", $.contextPath + "/zzcsgl!searchNzwxq.json?pid="+pid);
                  $grid.grid("reload");
                },
                error:function(){}
              })
          }

          //将下拉列表的每行都寸进数据库
          <%--var obj = $.loadJson($.contextPath + "/trace!getQybmAndQymc.json?sysName=ZZ");--%>
          <%--var qybm=obj.qybm;--%>
          <%--$grid = $("#gridId${idSuffix}");--%>
          <%--//将4个列表信息保存到数据库中--%>
          <%--for(var m=0;m<rownumber;m++){--%>
          <%--var gData =$grid.grid("getRowData","tem_"+m);--%>
          <%--$.ajax({--%>
            <%--url: $.contextPath+"/zzcsgl!saveNzwxq.json?qybm="+qybm,--%>
            <%--type:"post",--%>
            <%--data:gData,--%>
            <%--dataType:"json",--%>
            <%--success:function (res){--%>
              <%--var pid=$("#ID${idSuffix}").textbox("getValue");--%>
              <%--$grid.grid("option", "datatype", "json");--%>
              <%--$grid.grid("option","url", $.contextPath + "/zzcsgl!searchNzwxq.json?pid="+pid);--%>
              <%--$grid.grid("reload");--%>

            <%--},error:function (res){--%>
              <%--CFG_message("更新失败！", "error");--%>
            <%--}--%>
          <%--});}--%>
          //获得采收中重量
          var zlhj=0;
          var kczl=0;
          for(var i=0;i<rownumber;i++){
            if($("#gridId${idSuffix}").grid("getRowData","tem_"+i).ZL!=''){
              var dawdaw=$("#gridId${idSuffix}").grid("getRowData","tem_"+i);
              var zdad=$("#gridId${idSuffix}").grid("getRowData","tem_"+i).ZL;
              var zl=parseInt($("#gridId${idSuffix}").grid("getRowData","tem_"+i).ZL);
              zlhj=parseInt(zlhj)+zl;}
          }
          //获得1到3等品重量值和
          for(var i=0;i<rownumber-1;i++){
            if($("#gridId${idSuffix}").grid("getRowData","tem_"+i).ZL!=''){
              var zl=parseInt($("#gridId${idSuffix}").grid("getRowData","tem_"+i).ZL);
              kczl=parseInt(kczl)+zl;}
          }
          //var i=zlhj;
          //var k=kczl;
          //重量合计和库存重量赋值
          $("#ZLHJ${idSuffix}").textbox('setValue',zlhj);
          $("#KCZL${idSuffix}").textbox('setValue',kczl);
          //将库存重量和采收重量同步到采收表中
          $.ajax({
            url:$.contextPath+"/zzcsgl!updateCsgl.json?id="+ $("#ID${idSuffix}").textbox("getValue")+"&kczl="+kczl+"&zlhj="+zlhj,
            type:"post",
            //data:$grid.grid("getRowData","tem_"+m),
            dataType:"json",
            success:function(res){
            },
            error:function(){}
          })



          CFG_message("操作成功！", "success");
          var pid=$("#ID${idSuffix}").textbox("getValue");
          $grid.grid("option", "datatype", "json");
          $grid.grid("option","url", $.contextPath + "/zzcsgl!searchNzwxq.json?pid="+pid);
          $grid.grid("reload");
        },
        error: function () {
          CFG_message("操作失败！", "error");
        }
      })

    return false;
  })
</script>
