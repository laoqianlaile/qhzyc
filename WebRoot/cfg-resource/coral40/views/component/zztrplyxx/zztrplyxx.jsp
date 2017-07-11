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
                   data="">
      </ces:toolbar>
    </div>
    <form id="trplyZbForm${idSuffix}" method="post" class="coralui-form">
      <div class="app-inputdiv4" style ="height:32px;display: none">
        <input id="ID${idSuffix}" class="coralui-textbox" name="ID"/>
      </div>

      <div class="fillwidth colspan3 clearfix">
        <!------------------ 第一排开始---------------->
        <div class="app-inputdiv4"  style="display: none">
          <label class="app-input-label" >领用人：</label>
          <input id="LYR${idSuffix}" name="LYR"  class="coralui-textbox"/>
        </div>
        <div class="app-inputdiv4">
          <label class="app-input-label" >领用人：</label>
          <input id="LYRBH${idSuffix}" name="LYRBH" data-options="required:true" />
        </div>
        <div class="app-inputdiv4">
          <label class="app-input-label" >实际领用人：</label>
          <input id="SJLYR${idSuffix}" name="SJLYR"  data-options="required:true" />
        </div>
        <div class="app-inputdiv4" style="display: none">
          <label class="app-input-label" >实际领用人编号：</label>
          <input id="SJLYRBH${idSuffix}" name="SJLYRBH"  class="coralui-textbox"/>
        </div>

        <div class="app-inputdiv4">
          <label class="app-input-label" >领用时间：</label>
          <ces:datepicker id="LYSJ${idSuffix}"  required="true"  name="LYSJ" />
        </div>
        <!------------------ 第一排结束---------------->

        <!------------------第七排结束----------------->
      </div>
    </form>

    <ces:grid id="pGridId${idSuffix}"
              forceFit="true" fitStyle="fill" datatype="local" clicksToEdit="1" height="160">
      <ces:gridCols>
        <ces:gridCol name="ID" hidden="true" key="true" width="100">ID</ces:gridCol>
        <ces:gridCol name="PID" hidden="true" width="80">PID</ces:gridCol>
        <ces:gridCol name="SCDALX" hidden="true" width="80">生产档案类型</ces:gridCol>
        <ces:gridCol name="NSXBH" hidden="true" width="80">农事项编号</ces:gridCol>
        <ces:gridCol name="TRPBH" hidden="true" width="80">投入品编号</ces:gridCol>
        <ces:gridCol name="SSQYBH" hidden="true" edittype="combobox" width="120" formatoptions="required: true,disabled:true" >所属区域</ces:gridCol>
        <ces:gridCol name="SSQY"  hidden="true" width="80">所属区域2</ces:gridCol>
        <ces:gridCol name="DKBH" edittype="combobox" width="120" formatoptions="required: true,disabled:true" >地块编号</ces:gridCol>
        <ces:gridCol name="SCDA" edittype="combobox" formatoptions="required: true" width="150">生产档案编号</ces:gridCol>
        <ces:gridCol name="NSXMC"  width="120">农事项名称</ces:gridCol>
        <ces:gridCol name="TRPLX" formatter="combobox" width="120" >投入品类型</ces:gridCol>
        <ces:gridCol name="TRPMC"  width="120">投入品名称</ces:gridCol>
        <ces:gridCol name="TRPBH" hidden="true" width="120">投入品名称</ces:gridCol>
        <ces:gridCol name="SCPCH"  width="120">生产批次号</ces:gridCol>
        <ces:gridCol name="GYSMC"  width="120">供应商名称</ces:gridCol>
        <ces:gridCol name="CKSL"  width="120">出库数量</ces:gridCol>
        <ces:gridCol name="CZ" formatter="toolbar"
                     formatoptions="onClick:$.ns('namespaceId${idSuffix}').toolbarClick,
						             data:[{'label': '详细', 'id':'updBtn', 'disabled': 'false','type': 'button'}]" width="120">操作</ces:gridCol>
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
      if (button.id == "save") {//保存的方法
        $("#trplyZbForm${idSuffix}").form().submit();
      } else if (button.id == "addTrply") {//返回 或关闭
        showTrplyDialog();
      } else if (button.id == "CFG_closeComponentZone") {//返回 或关闭
        CFG_clickCloseButton($('#max${idSuffix}').data('configInfo'));
      } else if ( button.id == "updBtn"){
        initUpdateCkxx(event.data.rowId);
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
        showTreeDialog($form.form("formData",false));
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
        var lyrbhData = $.loadJson($.contextPath + "/zztrplylb!searchQyfzr.json");
        //领用人信息加载
        $("#LYRBH${idSuffix}").combobox({
          valueField:'FZRBH',
          textField:'FZR',
          sortable: true,
          width: "auto",
          data:lyrbhData.data
        });
        //领用人onChange事件
        $("#LYRBH${idSuffix}").combobox("option","onChange",function( e , data ){
          $("#LYR${idSuffix}").textbox("setValue",data.text);
          $("#SJLYR${idSuffix}").combogrid("setValue",data.text);
          $("#SJLYRBH${idSuffix}").textbox("setValue",data.value);
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
        $("#pGridId${idSuffix}").grid("setColProp", "TRPLX",{"formatoptions":{"data":trplx,"readonly":"true","textField":"name","valueField":"value",onChange:function(ui, data) {
        }}});
        var inputParamId = CFG_getInputParamValue(configInfo, 'inputParamId');
        if(isEmpty(inputParamId)){
          $("#LYSJ${idSuffix}").datepicker("setDate",new Date());
        }else{
          var $obj = $.loadJson($.contextPath + "/zztrplylb!searchTrckxx.json?id="+inputParamId);
          $("#trplyZbForm${idSuffix}").form("loadData",$obj);
          var $grid = $("#pGridId${idSuffix}");
          //生产单列表数据初始化
          $grid.grid("option", "datatype", "json");
          $grid.grid("option","url", $.contextPath + "/zztrplylb!searchTrpscdaxx.json?pid="+inputParamId);
          $grid.grid("reload");
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
  var _colNames=["id","pid","农事项编号","投入品编号","投入品类型","投入品名称","生产批次号","供应商名称","库存数量","出库数量"];
  var _colModel = [
    {name:'ID',hidden:true,width:100},
    {name:'PID',hidden:true,width:100},
    {name:'NSX',hidden:true,width:100},
    {name:'RKLSH',hidden:true,width:50},
    {name:'TRPLX',formatter:'combobox',width:100,readonly:true},
    {name:'TRPMC',width:150},
    {name:'CPPCH',width:120},
    {name:'GYSMC',width:120},
    {name:'KCSL',width:100},
    {name:'CKSL',formatter:'text',width:165}
  ];
  var _setting = {
    width : 'auto',
    height : '170' ,
    shrinkToFit : "true",
    forceFit : "true" ,
    fitStyle : "fill" ,
    datatype : "local"  ,
    colModel : _colModel,
    colNames : _colNames
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
                '<div class="app-inputdiv6"><label class="app-input-label" >生产档案类型：</label><input id="SCDALX${idSuffix}" data-options="required:true" name="SCDALX" /></div>'+
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
              var $grid = $("#trplyGrid${idSuffix}");
              var gData = $grid.grid("getRowData");
              var $form = $("#trplyForm${idSuffix}").form("formData",this);
              var isValid = $grid.grid('valid');
              //不保存到数据库，显示到主表列表上面
              initZbgrid($form,gData);
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
    var fzrbh =  (isEmpty(dFrzbh) ? "001":dFrzbh);
    var qyJson = $.loadJson($.contextPath + "/zztrplylb!searchQyxx.json?fzrbh="+fzrbh);
    //1、区域数据格式定义并添加onChange事件
    $("#SSQYBH${idSuffix}").combobox({
      textField :"QYMC",
      valueField :"QYBH",
      width :"auto",
      data :qyJson.data,
      onChange : function ( e ,data ){
        //添加onChange事件 进行地块信息数据加载
        var dkJson=$.loadJson($.contextPath + "/zztrplylb!searchDkxx.json?qybh="+data.value);
        $("#DKBH${idSuffix}").combobox("reload",dkJson.data);
      }
    });
    var dkJson = $.loadJson($.contextPath + "/zztrplylb!searchDkxxByQyfzr.json?fzrbh="+fzrbh);
    //2、地块数据格式定义并添加onChange事件
    $("#DKBH${idSuffix}").combobox({
      textField :"DKBH",
      valueField :"DKBH",
      width :"auto",
      data :dkJson.data,
      onChange : function ( e , data ){
        //根据区域编号获得对应区域下面的地块信息
        var scdaJson = $.loadJson($.contextPath + "/zztrplylb!searchScda.json?dkbh="+data.value);
        $("#SCDA${idSuffix}").combobox("reload",scdaJson.data);
      }
    });
    var scdaJson = $.loadJson($.contextPath + "/zztrplylb!searchScdaByQyfzr.json?fzrbh="+fzrbh);
    //3、生产档案数据格式定义并添加onChange事件
    $("#SCDA${idSuffix}").combobox({
      textField :"SCDABH",
      valueField :"SCDABH",
      width :"auto",
      data :scdaJson.data,
      onChange : function ( e , data ){
        var scdalx = $("#SCDALX${idSuffix}").combobox("getValue");
        reloadNsczx( data.value , scdalx );
      }
    });
    //4、生产方案数据初始化，格式定义并添加onChange事件
    var zzfaJson = $.loadJson($.contextPath + "/trace!getDataDictionary.json?lxbm=CZLX");
    $("#SCDALX${idSuffix}").combobox({
      textField : "text" ,
      valueField : "value" ,
      width : "auto" ,
      data :zzfaJson,
      onChange : function ( e , data ){
        var scdabh = $("#SCDA${idSuffix}").combobox("getValue");
        reloadNsczx( scdabh , data.value );
      }
    });
    $("#NSXBH${idSuffix}").combobox({
      textField : "NSZYXMC" ,
      valueField : "NSZYXBH" ,
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
        $("#trplyGrid${idSuffix}").grid("option","datatype","json");
        $("#trplyGrid${idSuffix}").grid("option","url",$.contextPath + "/zztrplylb!searchSelectedTrpxx.json?rklshs="+rklshs);
        $("#trplyGrid${idSuffix}").grid("reload");
        tdialogDivJQ.remove();
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
      if(!nodes[node].isParent){
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
    var gData = $grid.grid("getRowData");
    var isValid = $grid.grid('valid');
    var postData = {E_entityJson: $.config.toString(formData),
      E_dEntitiesJson: $.config.toString(gData)};
    if(isValid){//
      $.ajax({
        type: 'POST',
        url: $.contextPath +"/zztrplylb!saveTrplyxx_modify.json",
        data: postData,
        dataType:"json",
        success: function (data) {
          $("#ID${idSuffix}").textbox("setValue",data);
          $grid.grid("option", "datatype", "json");
          $grid.grid("option","url", $.contextPath + "/zztrplylb!searchTrpscdaxx.json?pid="+data);
          $grid.grid("reload");
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
                '<div class="app-inputdiv6"><label class="app-input-label" >投入品名称：</label><input id="TRPBH${idSuffix}" name="TRPBH" data-options="required:true,readonly:true" /></div>' +
                '<div class="app-inputdiv6"><label class="app-input-label" >供应商名称：</label><input id="GYSMC${idSuffix}" name="GYSMC"  class="coralui-textbox" data-options="required:true,readonly:true"  /></div>'+
                '<div class="app-inputdiv6"><label class="app-input-label" >库存数量：</label><input id="KCSL${idSuffix}" name="KCSL"  class="coralui-textbox" data-options="required:true,readonly:true"/></div>'+
                '<div class="app-inputdiv6"><label class="app-input-label" >出库数量：</label><input id="CKSL${idSuffix}" name="CKSL" class="coralui-textbox" data-options="required:true,readonly:true"/></div>'+
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
          data: ["->", {id:"cancel", label:"取消", type:"button"}],
          onClick: function(e, ui) {

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
</script>
