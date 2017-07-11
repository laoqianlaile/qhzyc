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
</style>
<div id="max${idSuffix}" class="fill">
  <div class="fill">
    <div class="toolbarsnav clearfix">
      <ces:toolbar id="toolbarId${idSuffix}" onClick="$.ns('namespaceId${idSuffix}').toolbarClick"
                   data="[{'label': '保存', 'id':'save', 'disabled': 'false','type': 'button'}]">
      </ces:toolbar>
    </div>
    <form id="trplyForm${idSuffix}" method="post" class="coralui-form">
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

    <!--投入品领用：生产档案选择-->
    <div class="toolbarsnav clearfix">
      <ces:toolbar id="toolbarId1${idSuffix}" onClick="$.ns('namespaceId${idSuffix}').toolbarClick1"
                   data="[{'label': '添加', 'id':'add', 'disabled': 'false','type': 'button'}]">
      </ces:toolbar>
    </div>
    <ces:grid id="pGridId${idSuffix}"
             forceFit="true" fitStyle="width" datatype="local" clicksToEdit="1" height="160">
      <ces:gridCols>
        <ces:gridCol name="ID" hidden="true"  width="100">ID</ces:gridCol>
        <ces:gridCol name="PID" hidden="true" width="80">PID</ces:gridCol>
        <ces:gridCol name="SSQYBH" formatter="combobox" width="120" formatoptions="required: true" >所属区域</ces:gridCol>
        <ces:gridCol name="SSQY"  hidden="true" width="80">所属区域2</ces:gridCol>
        <ces:gridCol name="DKBH" formatter="combobox" width="120">地块编号</ces:gridCol>
        <ces:gridCol name="SCDA" formatter="combobox" formatoptions="required: true" width="120">生产档案</ces:gridCol>
        <ces:gridCol name="SCFA" formatter="combobox" formatoptions="required: true" width="120">生产方案类型</ces:gridCol>
        <ces:gridCol name="NSX" formatter="combobox" width="120">农事项</ces:gridCol>
        <ces:gridCol name="CZ" formatter="toolbar"
                     formatoptions="onClick:$.ns('namespaceId${idSuffix}').toolbarClick1,
						             data:[{'label': '投入品领用', 'id':'trplyBtn', 'disabled': 'false','type': 'button'},{'label': '删除', 'id':'delBtn', 'disabled': 'false','type': 'button'}]" width="120">操作</ces:gridCol>
      </ces:gridCols>
    </ces:grid>
    <!--生产档案与农事项目关联列表 -->
    <ces:grid id="gridId${idSuffix}"
              shrinkToFit="true" forceFit="true" fitStyle="fill" datatype="local"  clicksToEdit="1" >
      <ces:gridCols>
        <ces:gridCol name="ID"  hidden="true"  width="100">ID</ces:gridCol>
        <ces:gridCol name="NSX"  hidden="true"  width="100">农事项编号</ces:gridCol>
        <ces:gridCol name="PID"   hidden="true" width="80">PID</ces:gridCol>
        <ces:gridCol name="TRPLX"  edittype="combobox" editoptions="required: true" width="180">投入品类型</ces:gridCol>
        <ces:gridCol name="TRPMC"  edittype="combobox"  width="180">投入品名称</ces:gridCol>
        <ces:gridCol name="TRPBH"  edittype="text" hidden="true"  width="80">投入品编号</ces:gridCol>
        <ces:gridCol name="SCPCH"    width="180">生产批次号</ces:gridCol>
        <ces:gridCol name="GYSMC"  editoptions="required: true" width="180">供应商名称</ces:gridCol>
        <ces:gridCol name="KCSL"  edittype="text"  width="80">库存数量</ces:gridCol>
        <ces:gridCol name="CKSL"  edittype="text" editoptions="required: true" width="80">出库数量</ces:gridCol>
        <ces:gridCol name="CZ"  formatter="toolbar"
                     formatoptions="onClick:$.ns('namespaceId${idSuffix}').toolbarClick2,
						             data:[{'label': '修改批次', 'id':'xgpcBtn', 'disabled': 'false','type': 'button'}]" width="80">操作</ces:gridCol>
      </ces:gridCols>
    </ces:grid>
    <div id="jqUC" style="display: none"></div>
    <div id="jqPchDiv" style="display: none">

    </div>
  </div>
</div>
<script type="text/javascript">
  alert('12');
  var gData;
  var temId="tem${idSuffix}";
  var pGridRowId = "";
  var pchData =new Array();
  var isUpdate = false;
  var isUpdate_tb = false;
  var _colModel=[
                  {name:'ID',hidden:true,width:100},
                  {name:'PID',hidden:true,width:100},
                  {name:'NSX',hidden:true,width:100},
                  {name:'TRPBH',hidden:true,width:100},
                  {name:'TRPLX',edittype:'combobox',width:100},
                  {name:'TRPMC',width:100},
                  {name:'SCPCH',width:100},
                  {name:'GYSMC',width:100},
                  {name:'KCSL',width:100},
                  {name:'CKSL',formatter:'text',width:80}
                ];
  var _colNames=["id","pid","农事项编号","投入品编号","投入品类型","投入品名称","生产批次号","供应商名称","库存数量","出库数量"];
  var _setting = {
    width : 'auto',
    height : 'auto' ,
    shrinkToFit : "true",
    forceFit : "true" ,
    fitStyle : "fill" ,
    datatype : "local"  ,
    colModel : _colModel,
    colNames : _colNames
  };

  $.extend($.ns("namespaceId${idSuffix}"), {
    //实际领用人弹出框事件
    sjlyrClick: function () {
      CFG_clickButtonOrTreeNode($('#max${idSuffix}').data('configInfo'), "sjlyr", "实际领用人", 2, $.ns("namespaceId${idSuffix}"));
    },
    //实际领用人回调事件
    setComboGridValue_sjlyr:function( o ){
      if( null == o ) return ;
      var obj = o.rowData;
      if( null == obj ) return ;
      //实际领用人名称
      $("#SJLYR${idSuffix}").combogrid("setValue",obj.XM);
      //实际领用人编号
      $("#SJLYRBH${idSuffix}").textbox("setValue",obj.GZRYBH);
    },
    //整体按钮条事件
    toolbarClick : function(event, button) {
      if (button.id == "save") {//保存的方法
        $("#trplyForm${idSuffix}").form().submit();

      } else if (button.id == "CFG_closeComponentZone") {//返回 或关闭
        CFG_clickCloseButton($('#max${idSuffix}').data('configInfo'));
      }
    },
    //第一个列表工具条事件
    toolbarClick1 : function(event, button) {
      var $grid = $("#pGridId${idSuffix}");
      var $cgrid = $("#gridId${idSuffix}");
      //进行操作的行
      var rowId = event.data.rowId;
      pGridRowId = rowId;
      var gData = $grid.grid("getRowData",rowId);
      //添加事件
      if (button.id == "add") {
        if(isUpdate){
          $("#trplyForm${idSuffix}").form().submit();
          $cgrid.grid("clearGridData");
        }

        var fzrbh = $("#LYRBH${idSuffix}").combobox("getValue");
        reloadQyxx(fzrbh);
        var lyrbh  = $("#LYR${idSuffix}").textbox("getValue");
        if(isEmpty(lyrbh)){
          CFG_message("请选择领用人","warn");
          return;
        }
        var totalRecords = $grid.grid("option", "records");
        lastsel = "tem_"+(totalRecords + 1);
        var dataAdded = {
          PID:temId
        };
        $grid.grid("addRowData", lastsel, dataAdded, "last");
        isUpdate = true;
      }
      //删除事件
      if (button.id == "delBtn") {
        var gDataId = gData.ID;
        //判断是否在数据库中存在
        if(gDataId == "" || isEmpty(gDataId)){//如果是删除编辑状态的数据并且是没有进行保存的数据则执行grid的remove
          $grid.grid( 'clearEdited', rowId );
          $grid.grid("delRowData",rowId);
          CFG_message("删除成功！", "success");
        }else{//走数据库删除
          $.ajax({
            url : $.contextPath + "/zztrplylb!deleteScdalyxx.json?id="+gDataId,
            type : "post",
            dataType : "json",
            success : function ( res ){
              CFG_message("操作成功!","success");
              $grid.grid("delRowData",rowId);
            },error : function ( res ){
              CFG_message("操作失败!","error");
            }
          })
        }
      }
      if(button.id == "trplyBtn"){//投入品领用列表初始化
        //获得投入品领用列表需要的前提数据
        //需要获得农事项详细
        var scdaObj = $.loadJson($.contextPath + "/zztrplylb!searchScdaObj.json?scdabh="+gData.SCDA);
        //1.农事项详细
        var nsxObj = $.loadJson($.contextPath + "/zztrplylb!searchNsxObj.json?pid="+scdaObj.ID+"&scdalx="+gData.SCFA+"&nszyxbh="+gData.NSX);
        //所需投入品信息nsxObj
        var sxtrp = $.loadJson($.contextPath + "/zztrplylb!searchTrp.json?pid="+nsxObj.ID+"&scdalx="+gData.SCFA+"&nszyxbh="+gData.NSX);
        var $grid = $("#gridId${idSuffix}");
        $grid.grid("option", "datatype", "json");
        $grid.grid("option","url", $.contextPath + "/zztrplylb!searchTrp.json?pid="+nsxObj.ID+"&scdalx="+gData.SCFA+"&nszyxbh="+gData.NSX+"&id="+gData.ID);
        $grid.grid("reload");
      }
    },
    toolbarClick2 : function ( e , ui ){
      //进行操作的行
      var rowId = e.data.rowId;
      var $pchGrid = $("#pchGridId${idSuffix}");
      var $pGrid = $("#pGridId${idSuffix}");
      var $grid = $("#gridId${idSuffix}");
      var pgData = $pGrid.grid("getRowData",pGridRowId);
      gData = $grid.grid("getRowData",rowId);
      if (ui.id == "xgpcBtn") {
        var jqGlobal = document.body;
        var dialogDivJQ = $("<DIV>").appendTo(jqGlobal);

        dialogDivJQ.dialog({
          appendTo : jqGlobal,
          modal : true,
          autoOpen : true,
          title : "投入品选择",
          maxWidth : 800,
          width : 800,
          height : 400,
          maxHeight : 400,
          resizable : false,
          position : {of:jqGlobal},
          onOpen : function() {
            $("<div id='pchGridId${idSuffix}'></div>").appendTo(dialogDivJQ);
            $pchGrid = $("#pchGridId${idSuffix}");
            $pchGrid.grid(_setting);
            var trplx = $.loadJson($.contextPath + "/zztrpcglb!searchTrplx.json");
            $pchGrid.grid("setColProp", "TRPLX",{"editoptions":{"data":trplx,"textField":"name","valueField":"value",onChange:function(ui, data) {
            }}});
            $pchGrid.grid("setColProp", "CKSL",{"formatoptions":{onChange:function(ui, data) {
              var gData = $pchGrid.grid("getRowData",ui.data.rowId);
              if(parseInt(gData.KCSL) < parseInt(gData.CKSL)){
                CFG_message("出库数量不能大于出库数量！","warn");
              }
            }}});
            //获得投入品领用列表需要的前提数据
            //需要获得农事项详细
            var scdaObj = $.loadJson($.contextPath + "/zztrplylb!searchScdaObj.json?scdabh="+pgData.SCDA);
            //1.农事项详细
            var nsxObj = $.loadJson($.contextPath + "/zztrplylb!searchNsxObj.json?pid="+scdaObj.ID+"&scdalx="+pgData.SCFA+"&nszyxbh="+pgData.NSX);
            //所需投入品信息nsxTrpObj
            var sxtrp = $.loadJson($.contextPath + "/zztrplylb!searchTrp.json?pid="+nsxObj.ID+"&scdalx="+pgData.SCFA+"&nszyxbh="+pgData.NSX);
            //获得指定的投入品的详细信息
            var nsxTrpObj = $.loadJson($.contextPath + "/zztrplylb!searchNsxTrpObj.json?pid="+nsxObj.ID+"&scdalx="+pgData.SCFA+"&nszyxbh="+pgData.NSX+"&trpmc="+gData.TRPMC);
            var pchGridDataJson = $.loadJson($.contextPath + "/zztrplylb!searchUpdTrp.json?pid="+nsxObj.ID+"&scdalx="+pgData.SCFA+"&nszyxbh="+pgData.NSX+"&trpmc="+gData.TRPMC);
            ///数据库获得可以用的批次信息
            $pchGrid.grid("option", "datatype", "json");
            //根据推荐的信息更改修改的批次出库量信息
            var colNameData = getColNameData(gData.TRPMC);
            //获得可用批次信息
            ///对数据进行处理
            var pchGridData = pchGridDataJson.data;
            var newPchGridData = updatePchGridData_2(pchGridData,colNameData);
            $pchGrid.grid('addRowData',"id",newPchGridData);
            var jqPanel = dialogDivJQ.dialog("buttonPanel").addClass("app-bottom-toolbar"),
                    jqDiv   = $("<div class=\"dialog-toolbar\">").appendTo(jqPanel);
            jqDiv.toolbar({
              data: ["->", {id:"sure", label:"确定", type:"button"}, {id:"cancel", label:"取消", type:"button"}],
              onClick: function(e, ui) {
                if ("sure" === ui.id) {
                    if(!checkTrpsl(nsxTrpObj.YL)){
                      CFG_message("投入品用量不能为0","error");
                    }else{
                      //选择了批次后，更新列表数据；
                      var ids = $pchGrid.grid("getDataIDs");
                      //清除投入品名称等于gData.TRPMC
                      updatePchGridData(gData.TRPMC,pchData);
                      dialogDivJQ.dialog("close");
                      isUpdate = true;
                      isUpdate_tb = true;
                    }
                } else {
                  dialogDivJQ.dialog("close");
                }
              }
            });
          },
          onClose : function () {
            dialogDivJQ.remove();
          }
        });
      }
    },
    toolbarClick3 : function(e, ui) {
      //进行操作的行
      var rowId = e.data.rowId;
      var $grid = $("#gridId${idSuffix}");
      gData = $grid.grid("getRowData",rowId);
      if (ui.id == "delBtn") {

        var gDataId = gData.ID;
        if(gDataId.indexOf("span") != -1 || gDataId.indexOf("tem") !=-1){//如果是删除编辑状态的数据并且是没有进行保存的数据则执行grid的remove
          $grid.grid( 'clearEdited', rowId );
          $grid.grid("delRowData",rowId);
        }else{
          $.ajax({
            type:"post",
            url: "${path}/zzkhxxgllb!deleteKhmdxx.json",
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
    onClick: $.ns('namespaceId${idSuffix}').sjlyrClick
  };
  var _colNames = ["领用人编号", "领用人名称"];
  var _colModel = [{name: "FZRBH", width: 65, sortable: true}, {name: "FZR", width: 155, sortable: true}];
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
        //页面初始化进行默认设置
        var inputParamId =CFG_getInputParamValue(configInfo, 'inputParamId');
        //进行列表中的下拉框数据的初始化
        var $pGrid = $("#pGridId${idSuffix}");
        var $grid = $("#gridId${idSuffix}");
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
          reloadQyxx(data.value);
        });
        $("#SJLYR${idSuffix}").combogrid({
          url: $.contextPath + "/zztrplylb!searchQyfzr.json",
          multiple: false,
          sortable: true,
          colModel: _colModel,
          colNames: _colNames,
          width: "auto",
          textField: "FZR",
          valueField: "FZRBH",
          height: "auto",
          buttonOptions: buttonOpts
        });
        $("#SJLYR${idSuffix}").combogrid("option","onChange",function(e,data){
          $("#SJLYR${idSuffix}").combogrid("setValue",data.text);
          $("#SJLYRBH${idSuffix}").textbox("setValue",data.value);
        });
        $("#ID${idSuffix}").textbox({readonly:true});
        var trplx = $.loadJson($.contextPath + "/zztrpcglb!searchTrplx.json");
        //初始化第一个列表数据
        //1、区域数据格式定义并添加onChange事件
        $pGrid.grid("setColProp","SSQYBH",{"formatoptions":{"textField":"QYMC","valueField":"QYBH",onChange:function ( ui , data ){
          //添加onChange事件 进行地块信息数据加载
          var dkJson=$.loadJson($.contextPath + "/zztrplylb!searchDkxx.json?qybh="+data.value);
          var $DKBHCombobox = $pGrid.grid("getCellComponent", ui.data.rowId, "DKBH");
          $DKBHCombobox.combobox("reload",dkJson.data);
          $pGrid.grid("setCell",ui.data.rowId,"SSQY", data.text);
        }}});
        //2、地块数据格式定义并添加onChange事件
        $pGrid.grid("setColProp","DKBH",{"formatoptions":{'textField':'DKBH','valueField':'DKBH',onChange:function( ui ,data ){
          //根据区域编号获得对应区域下面的地块信息
          var scdaJson = $.loadJson($.contextPath + "/zztrplylb!searchScda.json?dkbh="+data.value);
          var $SCDACombobox = $pGrid.grid("getCellComponent", ui.data.rowId, "SCDA");
          $SCDACombobox.combobox("reload",scdaJson.data);
        }}});
        //3、生产档案数据格式定义并添加onChange事件
        $pGrid.grid("setColProp","SCDA",{"formatoptions":{"textField":"SCDABH","valueField":"SCDABH",onChange:function ( ui , data ){

          var gData = $("#pGridId${idSuffix}").grid("getRowData",ui.data.rowId) ;//
          if(!isEmpty(gData.SCDA) && !isEmpty(gData.SCFA)){
            var scdaObj = $.loadJson($.contextPath + "/zztrplylb!searchScdaObj.json?scdabh="+gData.SCDA);
            if(!isEmpty(scdaObj)){
                //获得农事项下拉框数据
                var nsxJson = $.loadJson($.contextPath + "/zztrplylb!searchNsx.json?pid="+scdaObj.ID+"&scdalx="+gData.SCFA);
                var $NSXCombobox = $pGrid.grid("getCellComponent", ui.data.rowId, "NSX");
                $NSXCombobox.combobox("reload",nsxJson.data);
            }
          }
      }}});
        //4、生产方案数据初始化，格式定义并添加onChange事件
        var zzfaJson = $.loadJson($.contextPath + "/trace!getDataDictionary.json?lxbm=CZLX");
        $pGrid.grid("setColProp","SCFA",{"formatoptions":{"textField":"text","valueField":"value","data":zzfaJson,onChange:function ( ui , data ){
          var gData = $("#pGridId${idSuffix}").grid("getRowData",ui.data.rowId) ;
          if(!isEmpty(gData.SCDA) && !isEmpty(gData.SCFA)){
            var scdaObj = $.loadJson($.contextPath + "/zztrplylb!searchScdaObj.json?scdabh="+gData.SCDA);
            var nsxJson = $.loadJson($.contextPath + "/zztrplylb!searchNsx.json?pid="+scdaObj.ID+"&scdalx="+gData.SCFA);
            var $NSXCombobox = $pGrid.grid("getCellComponent", ui.data.rowId, "NSX");
            $NSXCombobox.combobox("reload",nsxJson.data);
          }
        }}});
        //5、农事项数据初始化，格式定义并添加onChange事件
        $pGrid.grid("setColProp","NSX",{"formatoptions":{"textField":"NSZYXMC","valueField":"NSZYXBH",onChange:function ( ui , data ){

        }}});
        //初始化列表投入品名称列添加onChange事件
        $grid.grid("setColProp", "TRPMC",{"editoptions":{"textField":'TRPMC',"valueField":'TRPBH',onChange:function(ui,data){
            //初始化由投入品能够带出的数据
            var trpObj = $.loadJson($.contextPath + "/zztrplylb!searchTrpxx.json?trpbh="+data.value);
            //生产批次号
            $grid.grid("setCell",ui.data.rowId,"SCPCH", trpObj.CPPCH);
            //供应商名称
            $grid.grid("setCell",ui.data.rowId,"GYSMC", trpObj.GYSMC);
            //库存数量
            $grid.grid("setCell",ui.data.rowId,"KCSL", trpObj.KCSL);
        }}});
        //孙子列表投入品类型数据初始化，格式定义并添加onChange事件
        $grid.grid("setColProp", "TRPLX",{"editoptions":{"data":trplx,"textField":"name","valueField":"value",onChange:function(ui, data) {
        }}});
        if(!isEmpty(inputParamId)){//修改操作初始化页面操作
          var $obj = $.loadJson($.contextPath + "/zztrplylb!searchTrckxx.json?id="+inputParamId);
          $("#trplyForm${idSuffix}").form("loadData",$obj);
          //区域信息下拉框数据初始化
          reloadQyxx($obj.LYRBH);
          //地块信息下拉框数据初始化
          reloadDkxx($obj.LYRBH);
          //生产档案下拉框数据初始化
          reloadScda($obj.LYRBH);
          //生产单列表数据初始化
          $pGrid.grid("option", "datatype", "json");
          $pGrid.grid("option","url", $.contextPath + "/zztrplylb!searchTrpscdaxx.json?pid="+inputParamId);
          $pGrid.grid("reload");
          $pGrid.grid("option","onComplete",function (){reloadNsx();});
          temId = inputParamId;
        }else{
          $("#LYSJ${idSuffix}").datepicker("setDate",new Date());
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
    configInfo.CFG_outputParams = {'success':'otp'};
  });
  function afterInlineSaveRow(e, data){

    $grid = $("#gridId${idSuffix}");
    var isValid = $grid.grid('valid', data.rowId);
    if(!isValid){
      $.alert("当前编辑行校验不通过");
      $grid.grid('editRow',data.rowId);
      return;
    }
  }
  //提交表单
  $("#trplyForm${idSuffix}").submit(function (){
    if (!$("#trplyForm${idSuffix}").form("valid")){CFG_message("页面校验不通过","error"); return false;}
    var $grid = $("#gridId${idSuffix}");
    var $pGrid = $("#pGridId${idSuffix}");
    var editrow = $grid.grid("option", "editrow");
    var formData = $("#trplyForm${idSuffix}").form("formData",this);
    var gData = $grid.grid("getRowData");
    var pgData = $pGrid.grid("getRowData");
    var isValid = $grid.grid('valid', editrow);
    var postData = {E_entityJson: $.config.toString(formData),
      E_pEntityJson: $.config.toString(pgData),
      E_dEntitiesJson: $.config.toString(gData)};

    if(isValid){//
      $.ajax({
        type: 'POST',
        url: $.contextPath +"/zztrplylb!saveTrpckxx.json",
        data: postData,
        dataType:"json",
        success: function (data) {
          $("#ID${idSuffix}").textbox("setValue",data.master.ID);
          isUpdate_tb = false;
          isUpdate = false;
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
  //校验出库量
  function checkTrpsl(zckl){
    var rowData = $("#pchGridId${idSuffix}").grid("getRowData");
    var cksl = 0;
    pchData = new Array();
    //循环遍历可用批次获得选择的批次信息
    for(var i = 0 ; i<rowData.length ;i++){
      var obj = rowData[i];
      if(obj.CKSL!=0){
        pchData.push(obj);
        cksl += obj.CKSL;
      }
    }
    if(cksl<=0)
      return false;
    return true;
  }

  //修改批次号信息
  function updatePchGridData(colName,pchData){
    var $grid =  $("#gridId${idSuffix}");
    var rowDataIds = $grid.grid("getDataIDs");
      //循环遍历可用批次获得选择的批次信息
    var rowId;
    var num = 0;
    //删除选择批次前的数据
    for(var i = 0 ; i<rowDataIds.length ;i++){
      var rowData =$grid.grid("getRowData",rowDataIds[i]);
      if(rowData.TRPMC==colName){
        if(num==0){
          rowId=rowDataIds[i];
        }
        num++;
        rowId=rowDataIds[i];
        //执行删除
        $grid.grid("delRowData",rowDataIds[i]);
      }
    }
    //添加选择后的批次到列表中进行替换
    for(var i= 0; i<pchData.length; i++){
      //执行添加操作
      $grid.grid("addRowData", rowId, pchData[i],"last");
      rowId++;
    }
  }
  //根据投入品名称获得默认提供的批次信息
  function getColNameData(colName){
    var $grid =  $("#gridId${idSuffix}");
    var rowData = $grid.grid("getRowData");
    var dataArray = new Array();
    for( var i = 0; i<rowData.length ; i++){
      var obj = rowData[i];
      if(obj.TRPMC == colName){
        dataArray.push(obj);
      }
    }
    return dataArray;
  }

  //进行投入品列表数据与投入品选择列数据进行比对处理
  function updatePchGridData_2(pchGridData,colNameData){
    var newGridData = new Array();
    for(var i =0; i<pchGridData.length; i++){
      for( var j = 0 ; j<colNameData.length ; j++){
         var pchObj = pchGridData[i];
         var colObj = colNameData[j];
         if(pchObj.TRPBH == colObj.TRPBH){
            pchObj.CKSL = colObj.CKSL ;
         }
      }
      newGridData.push(pchObj);
    }
    return newGridData;
  }

  function reloadQyxx(fzrbh){
    var $pGrid = $("#pGridId${idSuffix}");
    //联动子表中区域选择下拉框数据
    var qyJson = $.loadJson($.contextPath + "/zztrplylb!searchQyxx.json?fzrbh="+fzrbh);
    //1、区域数据格式定义并添加onChange事件
    $pGrid.grid("setColProp","SSQYBH",{"formatoptions":{"textField":"QYMC","valueField":"QYBH","data":qyJson.data,onChange:function ( ui , data ){
      //添加onChange事件 进行地块信息数据加载
      var dkJson=$.loadJson($.contextPath + "/zztrplylb!searchDkxx.json?qybh="+data.value);
      var $DKBHCombobox = $pGrid.grid("getCellComponent", ui.data.rowId, "DKBH");
      $DKBHCombobox.combobox("reload",dkJson.data);
      $pGrid.grid("setCell",ui.data.rowId,"SSQY", data.text);
    }}});

  }
  function reloadDkxx(fzrbh){
    var $pGrid = $("#pGridId${idSuffix}");
    //联动子表中区域选择下拉框数据
    var qyJson = $.loadJson($.contextPath + "/zztrplylb!searchDkxxByQyfzr.json?fzrbh="+fzrbh);
    //1、区域数据格式定义并添加onChange事件
    $pGrid.grid("setColProp","DKBH",{"formatoptions":{"textField":"DKBH","valueField":"DKBH","data":qyJson.data}});
  }

  function reloadScda(fzrbh){
    var $pGrid = $("#pGridId${idSuffix}");
    //联动子表中区域选择下拉框数据
    var qyJson = $.loadJson($.contextPath + "/zztrplylb!searchScdaByQyfzr.json?fzrbh="+fzrbh);
    //1、区域数据格式定义并添加onChange事件
    $pGrid.grid("setColProp","SCDA",{"formatoptions":{"textField":"SCDABH","valueField":"SCDABH","data":qyJson.data}});
  }

  //修改时进行数据的
  function reloadNsx(){
    var $pGrid = $("#pGridId${idSuffix}");
    var ids = $pGrid.grid("getDataIDs");
    for (var i = 0 ; i < ids.length ; i++){
      var gData = $pGrid.grid("getRowData",ids[i]);
      var scdaObj = $.loadJson($.contextPath + "/zztrplylb!searchScdaObj.json?scdabh="+gData.SCDA);
      var nsxJson = $.loadJson($.contextPath + "/zztrplylb!searchNsx.json?pid="+scdaObj.ID+"&scdalx="+gData.SCFA);
      var $NSXCombobox = $pGrid.grid("getCellComponent",ids[i], "NSX");
      $NSXCombobox.combobox("reload",nsxJson.data);
    }
  }
</script>
