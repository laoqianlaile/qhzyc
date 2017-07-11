<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.ces.config.utils.CommonUtil" %>
<%@ taglib prefix="ces" tagdir="/WEB-INF/tags" %>
<%
  request.setAttribute("idSuffix", CommonUtil.generateUIId(""));
  String id=request.getParameter("ID");
  request.setAttribute("id",id);
%>
<style>
  .app-input-label {
    float: left;
  }
  .min-div{
    width: 20%;
    float: left;
  }
  .min-div2{
    width: 30%;
    float: left;
  }

</style>
<div id="maxDiv${idSuffix}" class="fill">
  <ces:toolbar id="toolbarId${idSuffix}" onClick="$.ns('namespaceId${idSuffix}').toolbarClick"
               data="['->',{'label': '关闭', 'id':'CFG_closeComponentZone','cls':'return_tb', 'disabled': 'false','type': 'button'}]">
  </ces:toolbar>
  <div class='homeSpan' style="margin-top: -23px;"><div><div style='margin-left:20px;width: 150px;' id="nva${idSuffix}"> - 订单管理 - 详情 </div></div></div>
  <%--[{'label': '保存', 'id':'save', 'disabled': 'false','type': 'button'}]--%>
  <div id="ddFormDiv${idSuffix}" style="margin-left: 20px;overflow: auto;height: 225px; ">
    <form id="ddForm${idSuffix}" enctype="multipart/form-data" method="post" class="coralui-form">
      <div class="fillwidth colspan3 clearfix">
        <div class="app-inputdiv4" style="display: none">
          <label class="app-input-label">订单编号:</label>
          <input id="DDBH${idSuffix}" class="coralui-textbox" name="DDBH">
          <input id="ID${idSuffix}" class="coralui-textbox" name="ID">
          <input id="QYBM${idSuffix}" class="coralui-textbox" name="QYBM">
          <input id="XDRQ${idSuffix}" class="coralui-datepicker" name="XDRQ"
                 data-options="required:true">
        </div>
        <div class="app-inputdiv4" style="display: none">
          <label class="app-input-label">客户名称:</label>
          <input id="KHMC${idSuffix}" class="coralui-textbox" name="KHMC"/>
        </div>
        <div class="app-inputdiv4">
          <label class="app-input-label">客户名称:</label>
          <input id="KHBH${idSuffix}"   name="KHBH"
                 data-options="required:true"/>
        </div>
        <div class="app-inputdiv4">
          <label class="app-input-label">客户联系人:</label>
          <input id="KHLXR${idSuffix}" name="KHLXR" class="coralui-textbox" data-options="readonly:true"/>
        </div>
        <div class="app-inputdiv4">
          <label class="app-input-label">联系电话:</label>
          <input id="KHLXDH${idSuffix}" class="coralui-textbox" name="KHLXDH" data-options="readonly:true">
        </div>
        <div class="app-inputdiv8">
          <label class="app-input-label" >客户地址:</label>
          <input id="KHDZ${idSuffix}" name="KHDZ" class="coralui-textbox" data-options="readonly:true,required:true"/>
        </div>
        <div class="app-inputdiv4" style="height:34px">

        </div>
        <div class="app-inputdiv4">
          <label class="app-input-label">门店名称:</label>
          <input id="MDMC${idSuffix}" name="MDMC"/>
        </div>
        <div class="app-inputdiv4">
          <label class="app-input-label">门店联系人:</label>
          <input id="MDLXR${idSuffix}" name="MDLXR" class="coralui-textbox"
                 data-options="readonly:true"/>
        </div>
        <div class="app-inputdiv4">
          <label class="app-input-label">门店联系电话:</label>
          <input id="MDLXDH${idSuffix}" class="coralui-textbox" name="MDLXDH"
                 data-options="readonly:true">
        </div>

        <div class="app-inputdiv8">
          <label class="app-input-label">门店地址:</label>
          <input id="MDDZ${idSuffix}" class="coralui-textbox" name="MDDZ"
                 data-options="readonly:true">
        </div>
        <div class="app-inputdiv4" style="height:34px">

        </div>
        <div class="app-inputdiv8">
          <label class="app-input-label">送货地址:</label>
          <input id="SHDZ${idSuffix}" class="coralui-textbox" name="SHDZ"
                 data-options="required:true">
        </div>
        <div class="app-inputdiv4"  style="height:34px;margin-left: 10px;">
          <input id="SHDZTY${idSuffix}"  name="SHDZTY" />
        </div>
        <div class="app-inputdiv4">
          <label class="app-input-label">送货时间:</label>
          <input id="SHSJ${idSuffix}" class="coralui-datepicker" name="SHSJ">
        </div>
        <div class="app-inputdiv12">
          <label class="app-input-label">备注:</label>
          <textarea id="BZ${idSuffix}"   class="coralui-textbox" name="BZ"/>
        </div>
      </div>
    </form>
  </div>
  <div id="tabs${idSuffix}" class="coralui-tabs" data-options="heightStyle:'fill'">
    <ul>
      <li><a href="#shdd${idSuffix}">散货订单</a></li>
      <li><a href="#cpdd${idSuffix}">产品订单</a></li>
    </ul>
    <div id="shdd${idSuffix}">
      <div class="toolbarsnav clearfix">
        <%--<ces:toolbar id="toolbarId1${idSuffix}" onClick="$.ns('namespaceId${idSuffix}').toolbarClick1"--%>
                     <%--data="[{'label': '添加', 'id':'add', 'disabled': 'false','type': 'button'}]">--%>
        <%--</ces:toolbar>--%>
      </div>
      <ces:grid id="shddGrid${idSuffix}" height="auto" rownumbers="true" fitStyle="fill" datatype="local">
        <ces:gridCols>
          <ces:gridCol name="ID" editable="false" hidden="true"  width="100">ID</ces:gridCol>
          <ces:gridCol name="PLBH" formatter="text" hidden="true" formatoptions="required: true" width="100">PLBH</ces:gridCol>
          <ces:gridCol name="PL" formatter="combobox" formatoptions="readonly:true" width="80">品类</ces:gridCol>
          <ces:gridCol name="PZ" formatter="combobox" formatoptions="required: true,readonly:true,'textField':'name','valueField':'name'"
                       width="80">品种</ces:gridCol>
          <ces:gridCol name="ZL" formatter="text" formatoptions="required: true,readonly:true"
                       sorttype="float" width="80">重量</ces:gridCol>
          <%--<ces:gridCol name="CZ" editable="false" formatter="toolbar"--%>
                       <%--formatoptions="onClick:$.ns('namespaceId${idSuffix}').toolbarClick1,--%>
						             <%--data:[--%>
						             <%--{'label': '删除', 'id':'delBtn', 'disabled': 'false','type': 'button'}]" width="50">操作</ces:gridCol>--%>
        </ces:gridCols>

      </ces:grid>
    </div>
    <div id="cpdd${idSuffix}">
      <div class="toolbarsnav clearfix">
        <%--<ces:toolbar id="toolbarId2${idSuffix}" onClick="$.ns('namespaceId${idSuffix}').toolbarClick2"--%>
                     <%--data="[{'label': '添加', 'id':'add', 'disabled': 'false','type': 'button'}]">--%>
        <%--</ces:toolbar>--%>
      </div>
      <ces:grid id="cpddGrid${idSuffix}" fitStyle="fill"  height="auto" rownumbers="true" datatype="local" >
        <ces:gridCols>
          <ces:gridCol name="ID" editable="false" hidden="true"  width="100">ID</ces:gridCol>
          <ces:gridCol name="CPMC" width="100" editable="true" formatter="combobox" formatoptions="required: true" sortable="true" >产品名称</ces:gridCol>
          <ces:gridCol name="BZXS" width="80" formatter="combobox" formatoptions="required: true,readonly:true">包装形式</ces:gridCol>
          <ces:gridCol name="CPGG" width="80" editable="false" >产品规格</ces:gridCol>
          <ces:gridCol name="CPSL" width="80" editable="true" formatter="text" formatoptions="readonly:true" >产品数量</ces:gridCol>
          <%--<ces:gridCol name="CZ" editable="false" formatter="toolbar"--%>
                       <%--formatoptions="onClick:$.ns('namespaceId${idSuffix}').toolbarClick2,--%>
						             <%--data:[{'label': '删除', 'id':'delBtn', 'disabled': 'false','type': 'button'}]" width="50">操作</ces:gridCol>--%>
        </ces:gridCols>
      </ces:grid>
    </div>
  </div>
</div>

<script>

  var tempId="temp${idSuffix}";
  var gData;
  $.extend($.ns("namespaceId${idSuffix}"), {
    khClick:function(){
      CFG_clickButtonOrTreeNode($('#maxDiv${idSuffix}').data('configInfo'), "khxx", "客户名称", 2, $.ns("namespaceId${idSuffix}"));
    },
    setComboGridValue_khxx : function ( o ){
      if ( null == o )  return ;
      var obj = o.rowData ;
      if ( null == obj ) return ;
      $("#KHBH${idSuffix}").combogrid("setValue",obj.KHBH);
      $("#KHMC${idSuffix}").textbox("setValue",obj.KHMC);
      //根据ID加载默认带
      setKhxx(obj.KHBH);
    },
    mdClick : function(){
      CFG_clickButtonOrTreeNode($('#maxDiv${idSuffix}').data('configInfo'), "mdxx", "门店名称", 2, $.ns("namespaceId${idSuffix}"));
    },
    setComboGridValue_mdxx : function ( o ){
      if ( null == o )  return ;
      var obj = o.rowData ;
      if ( null == obj ) return ;
      $("#MDMC${idSuffix}").combogrid("setValue",obj.MDMC);
      setMdxx(obj.ID);
    },
    setMdxxPidValue :function ( o ){
      var khbh = $("#KHBH${idSuffix}").combogrid("getValue");
      if(khbh == "" || khbh == null){
        CFG_message("请先选择客户信息","warn");
        return{status:true};
      }
      var khxx = $.loadJson($.contextPath + "/tcszzkhxx!searchKhxx.json?khbh="+khbh);
      return {status:true,
        P_columns:"EQ_C_PID≡"+khxx.ID
      }
    },
    toolbarClick: function (e, ui) {
      if (ui.id == "save") {//保存的方法
        $("#ddForm${idSuffix}").form().submit();

      } else if (ui.id == "CFG_closeComponentZone") {

                CFG_clickCloseButton($('#maxDiv${idSuffix}').data('configInfo'));

      }
    },
    toolbarClick1 : function (e, ui){
      var $grid = $("#shddGrid${idSuffix}");
      var rowId = e.data.rowId;
      if( ui.id == "add"){
        var totalRecords = $grid.grid("option", "records");
        lastsel = "tem_"+(totalRecords + 1);
        var dataAdded = {
          PID:tempId
        };
        $grid.grid("addRowData", lastsel, dataAdded, "last");
        $grid.grid('editRow', lastsel);
      }
      if(ui.id == "delBtn" ){
        gData = $grid.grid("getRowData",rowId);
        var gDataId = gData.ID;
        if(gDataId == ""){//如果是删除编辑状态的数据并且是没有进行保存的数据则执行grid的remove
          $grid.grid( 'clearEdited', rowId );
          $grid.grid("delRowData",rowId);
          CFG_message("操作成功!","success");
        }else{
          $.ajax({
            url : $.contextPath + "/zzddxxlb!deleteShxxOrCpxx.json?type=1&id="+gDataId,
            type : "post",
            dataType : "json",
            success :function ( res) {
              CFG_message("操作成功!","success");
              $grid.grid("delRowData",rowId);
            },error : function ( res ){
              CFG_message("操作失败!","error");
            }
          });
        }
      }
    },
    toolbarClick2 : function (e, ui){
      var $grid = $("#cpddGrid${idSuffix}");
      var rowId = e.data.rowId;
      if( ui.id == "add"){
        var totalRecords = $grid.grid("option", "records");
        lastsel = "tem_"+(totalRecords + 1);
        var dataAdded = {
          PID:tempId
        };
        $grid.grid("addRowData", lastsel, dataAdded, "last");
//        $grid.grid('editRow', lastsel);
      }
      if(ui.id == "delBtn" ){
        gData = $grid.grid("getRowData",rowId);
        var gDataId = gData.ID;
        if(gDataId == ""){//如果是删除编辑状态的数据并且是没有进行保存的数据则执行grid的remove
          $grid.grid( 'clearEdited', rowId );
          $grid.grid("delRowData",rowId);
          CFG_message("操作成功!","success");
        }else{ //数据库删除
          //deleteShxxOrCpxx
          $.ajax({
            url : $.contextPath + "/zzddxxlb!deleteShxxOrCpxx.json?type=2&id="+gDataId,
            type : "post",
            dataType : "json",
            success :function ( res) {
              CFG_message("操作成功!","success");
              $grid.grid("delRowData",rowId);
            },error : function ( res ){
              CFG_message("操作失败!","error");
            }
          });
        }
      }
    }
  });

   var buttonOptsKhxx ={
     id: "combogrid_buttonId2",
     label: "构件按钮",
     icons: "icon-checkmark-circle",
     text: false,
     onClick: $.ns('namespaceId${idSuffix}').khClick
   };
  var _colNamesGys = ["客户编号", "客户名称"];
  var _colModelGys = [{name: "KHBH", width: 120, sortable: true,align:"left"}, {name: "KHMC", width: 155, sortable: true,align:"left"}];
  var buttonOptsMdxx ={
    id: "combogrid_buttonId2",
    label: "构件按钮",
    icons: "icon-checkmark-circle",
    text: false,
    onClick: $.ns('namespaceId${idSuffix}').mdClick
  };
  var _colNamesMd = ["门店名称", "联系人","ID"];
  var _colModelMd = [{name: "MDMC", width: 80, sortable: true,align:"left"}, {name: "MDLXR", width: 100, sortable: true,align:"left"}, {name: "ID", width: 100,hidden:true}];

  $(function () {
    var configInfo = CFG_initConfigInfo({
      /** 页面名称 */
      'page': 'zzddxxxzxq.jsp',
      /** 页面中的最大元素 */
      'maxEleInPage': $('#maxDiv${idSuffix}'),
      /** 获取构件嵌入的区域 */
      'getEmbeddedZone': function () {
        return $('#maxDiv${idSuffix}');
      },
      /** 初始化预留区 */
      'initReserveZones': function (configInfo) {
        <%--CFG_addToolbarButtons(configInfo, $('#toolbarId${idSuffix}'), 'toolBarReserve', 0);--%>
      },
      /** 获取返回按钮添加的位置 */
      'setReturnButton' : function(configInfo) {
        //CFG_setReturnButton(configInfo, $('#toolbarId${idSuffix}'));
        CFG_setCloseButton(configInfo, $('#toolbarId${idSuffix}'));
      },
      /** 获取关闭按钮添加的位置 */
      'setCloseButton' : function(configInfo) {
        <%--CFG_setCloseButton(configInfo, $('#toolbarId1${idSuffix}'));--%>
      },
      /** 页面初始化的方法 */
      'bodyOnLoad': function (configInfo) {
        //页面初始化进行默认设置
        var inputParamId = "";
        if("${id}" != ""){
          inputParamId = "${id}";

        }else{
          inputParamId = CFG_getInputParamValue(configInfo, 'rowDataId');
        }

        var $shddgrid = $("#shddGrid${idSuffix}");
        var $cpddgrid = $("#cpddGrid${idSuffix}");
        var $khbh = $("#KHBH${idSuffix}");
        var $qybm = $("#QYBM${idSuffix}");
        var $mdmc = $("#MDMC${idSuffix}");
        $mdmc.combogrid({
          disabled: true,
          multiple: false,
          sortable: true,
          colModel: _colModelMd,
          colNames: _colNamesMd,
          width: "auto",
          panelWidth:180,
          textField: "MDMC",
          valueField: "MDMC",
          height: "auto",
          buttonOptions: buttonOptsMdxx
        });
        $khbh.combogrid({
          url: $.contextPath + "/tcszzkhxx!searchGridData.json",
          multiple: false,
          sortable: true,
          colModel: _colModelGys,
          colNames: _colNamesGys,
          width: "auto",
          panelWidth:355,
          textField: "KHMC",
          valueField: "KHBH",
          height: "auto",
          buttonOptions: buttonOptsKhxx});
        $khbh.combogrid("option","onChange",function ( e , data ) {
          $mdmc.combogrid("option","disabled",false);
          var mdData = $.loadJson($.contextPath + "/tcszzmdxx!searchGridData.json?khbh=" + data.value);
          $mdmc.combogrid("reload",mdData.data );
          setKhxx(data.value);
        });
        $mdmc.combogrid("option","onChange",function( e , data ){
          var $mcGrid = $mdmc.combogrid("grid");
          var selectedRowId = $mcGrid.grid("option","selrow");
          var rowData = $mcGrid.grid("getRowData",selectedRowId);
          //初始化门店相关信息
          setMdxx(rowData.ID);
        });
        //初始化地址同门店或客户地址标签
        $("#SHDZTY${idSuffix}").radiolist({column:2,valueField:"value",textField:"text",url:$.contextPath + "/trace!getDataDictionary.json?lxbm=DZDTY"});
        $("#SHDZTY${idSuffix}").radiolist("option","onChange",function ( e , data ){
          var value = data.value;
          var dzData = "";
          if(value == 1){//地址同客户地址
            dzData=$("#KHDZ${idSuffix}").textbox("getValue");
          }else{
            dzData=$("#MDDZ${idSuffix}").textbox("getValue");
          }
          $("#SHDZ${idSuffix}").textbox("setValue",dzData);
        });
        //获得品类信息
        var plData = $.loadJson($.contextPath + "/trace!getZzDplOrXplData.json");
        var bzxsData = $.loadJson($.contextPath + "/trace!getDataDictionary.json?lxbm=BZXS");
        //初始化包装形式
        $cpddgrid.grid("setColProp", "BZXS",{"formatoptions":{"data":bzxsData,readonly:true,"textField":"text","valueField":"value",required:true,readonly:true}});
        $shddgrid.grid("setColProp", "PL",{"formatoptions":{"data":plData,readonly:true,"textField":"name","valueField":"value",onChange:function(ui, data) {
          var pzData = $.loadJson($.contextPath + "/trace!getXplData.json?plbh="+data.value);
          var $pzCombobox = $shddgrid.grid("getCellComponent", ui.data.rowId, "PZ");
          var $plCombobox = $shddgrid.grid("getCellComponent", ui.data.rowId, "PL");
          var $plbhTextbox = $shddgrid.grid("getCellComponent", ui.data.rowId, "PLBH");

          //根据品类联动品种下拉框数据
          $pzCombobox.combobox("reload", pzData);
          $plCombobox.combobox("setValue", data.text);
          $plbhTextbox.textbox("setValue",data.value);
        }}});
//        $shddgrid.grid("setColProp", "PL",{"editoptions":{onChange:function(ui, data) {
//          var $plCombobox = $shddgrid.grid("getCellComponent", ui.data.rowId, "PZ");
//        }}});
        //初始化产品信息列表下拉框
        var cpDta = $.loadJson($.contextPath + "/zzddxxlb!getCpxxData.json");
        $cpddgrid.grid("setColProp", "CPMC",{"formatoptions":{"data":cpDta,readonly:true,"textField":"CPMC","valueField":"CPBH",onChange:function(ui, data) {
          //根据产品编号获得对应产品信息
          var rowData = $.loadJson($.contextPath + "/zzddxxlb!getCpxxByBh.json?cpbh="+data.value);
          //默认带入包装形式
          $cpddgrid.grid("getCellComponent",ui.data.rowId,"BZXS").combobox("setValue", rowData.BZXS);;
          //默认带入包装规格
          $cpddgrid.grid("setCell",ui.data.rowId,"CPGG", rowData.GG);
        }}});
        //将表单属性设为只读
        $("#ddForm${idSuffix}").form("setReadOnly",true);
        if(isEmpty(inputParamId)){//新增时默认初始化
          var qyObj = $.loadJson($.contextPath+"/trace!getQybmAndQymc.json");
          $qybm.textbox("setValue",qyObj.qybm);
          $("#XDRQ${idSuffix}").datepicker("setDate",new Date());
        }else{
          var $ddObj = $.loadJson($.contextPath+"/zzddxxlb!searchDdxx.json?id="+inputParamId);
          $("#ddForm${idSuffix}").form("loadData",$ddObj);
          $("#SHSJ${idSuffix}").datepicker('setValue',$ddObj.SHSJ);
          //散货列表数据初始化
          var driddata = $.loadJson($.contextPath+"/zzddxxlb!searchShddxx.json?pid="+inputParamId);
          $shddgrid.grid("option", "datatype", "json");
          $shddgrid.grid("option","url", $.contextPath + "/zzddxxlb!searchShddxx.json?pid="+inputParamId);
          $shddgrid.grid("reload");
          $shddgrid.grid("option","onComplete",function( e, ui){
            var ids = $shddgrid.grid("getDataIDs");
            for(var i = 0 ; i < ids.length ; i++){
              var gData = $shddgrid.grid("getRowData",ids[i]);
              var pzData = $.loadJson($.contextPath + "/trace!getXplData.json?plbh="+gData.PLBH);
              var $pzCombobox = $shddgrid.grid("getCellComponent",ids[i], "PZ");
              //根据品类联动品种下拉框数据
              var $plCombobox = $shddgrid.grid("getCellComponent",ids[i], "PL");
              //根据品类联动品种下拉框数据

              $pzCombobox.combobox("reload", pzData);
              $plCombobox.combobox('setValue',driddata.data[i].PL);
              $pzCombobox.combobox('setValue',driddata.data[i].PZ);
            }
        });


          //产品列表数据初始化
          $cpddgrid.grid("option", "datatype", "json");
          $cpddgrid.grid("option","url", $.contextPath + "/zzddxxlb!searchCpddxx.json?pid="+inputParamId);
          $cpddgrid.grid("reload");
          //id为不为空，说明页面是从控制台跳转过来的，因此将关闭按钮隐藏。
          if("${id}" != "") {
            $('#CFG_closeComponentZone').css("visibility","hidden");
          }
        }
      }


    });
    if (configInfo) {
      //alert("系统参数：\t" + "关联的系统参数=" + CFG_getSystemParamValue(configInfo, 'systemParam1')
      //		+ "\n构件自身参数：\t" + "selfParam1=" + CFG_getSelfParamValue(configInfo, 'selfParam1')
      //		+ "\n构件入参：\t" + "inputParamName_1=" + CFG_getInputParamValue(configInfo, 'inputParamName_1'));
    }
    // 设置输出参数
    // configInfo.CFG_outputParams.xxx = '';
  });
  //设置门店相关信息
  function setMdxx(id){
    var $obj = $.loadJson($.contextPath + "/tcszzmdxx!searchMdxx.json?id="+id);
    //联系人
    $("#MDLXR${idSuffix}").textbox("setValue",$obj.MDLXR);
    //联系电话
    $("#MDLXDH${idSuffix}").textbox("setValue",$obj.MDLXDH);
    //门店地址
    $("#MDDZ${idSuffix}").textbox("setValue",$obj.MDDZ);
  }
  //设置客户相关信息
  function setKhxx(khbh){
    var $obj = $.loadJson($.contextPath + "/tcszzkhxx!searchKhxx.json?khbh="+khbh);
    //联系人
    $("#KHLXR${idSuffix}").textbox("setValue",$obj.LXR);
    $("#KHMC${idSuffix}").textbox("setValue",$obj.KHMC);
    //联系电话
    $("#KHLXDH${idSuffix}").textbox("setValue",$obj.DH);
    //门店地址
    $("#KHDZ${idSuffix}").textbox("setValue",$obj.DZ);
  }

  $("#ddForm${idSuffix}").submit(
    function (){

      if (!$("#ddForm${idSuffix}").form("valid")){CFG_message("页面校验不通过","error"); return false;}
      var $shddgrid = $("#shddGrid${idSuffix}");
      var $cpddgrid = $("#cpddGrid${idSuffix}");
      var formData = $("#ddForm${idSuffix}").form("formData", false);//处理表单数据
      var shGridData =$shddgrid.grid("getRowData");
      var cpGridData =$cpddgrid.grid("getRowData");
      var postData = {E_entityJson: $.config.toString(formData),
        S_dEntitiesJson: $.config.toString(shGridData),
        C_dEntitiesJson: $.config.toString(cpGridData)};
      $.ajax({
        url: $.contextPath + "/zzddxxlb!saveDdxx.json",
        type:"post",
        data:postData,
        dataType:"json",
        success : function (res){
          CFG_message("保存成功","success");
          CFG_clickCloseButton($('#maxDiv${idSuffix}').data('configInfo'));
        },error : function (res){
          CFG_message("保存失败","error");
        }
      })
      return false;
  });
</script>