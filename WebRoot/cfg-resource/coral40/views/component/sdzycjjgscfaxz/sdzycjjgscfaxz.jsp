<%@ page import="com.ces.config.utils.CommonUtil" %>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" %>
<%@ taglib prefix="ces" tagdir="/WEB-INF/tags" %>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
    request.setAttribute("idSuffix", CommonUtil.generateUIId(""));
    request.setAttribute("basePath", basePath);
%>
<style>
    .coral-toolbar-border {
        display: block;
        /*width: auto;
        height: auto;*/
        /* overflow */
        /*overflow: hidden;*/
        position: relative;
        float: left;
        left: 0;
        top: 0;
        height: 48px;
    }
</style>
<div id="maxDiv${idSuffix}" class="fill">
    <div class="toolbarsnav clearfix">
        <ces:toolbar id="toolbarId${idSuffix}" onClick="$.ns('namespaceId${idSuffix}').toolbarClick"
                     data="[{'label': '保存', 'id':'save', 'disabled': 'false','type': 'button'}]">
        </ces:toolbar>
    </div>
    <form id="trplyForm${idSuffix}" method="post" class="coralui-form">

            <div class="fillwidth colspan2 clearfix" style="margin-top: 10px">
            <div class="app-inputdiv6">
                <label class="app-input-label">生产方案编号:</label>
                <input  id="schemeId${idSuffix}" name="SCFABH" class="coralui-textbox" />
            </div>
            <div class="app-inputdiv6">
                <label class="app-input-label">饮片名称:</label>
                <input  id="piecesName${idSuffix}" name="YPMC" class="coralui-textbox" />
            </div>
            <div class="app-inputdiv6">
                <label class="app-input-label">原材料:</label>
                <input  id="rawMaterial${idSuffix}" name="YCL" class="coralui-textbox" />
            </div>
            <div class="app-inputdiv6">
                <label class="app-input-label">原材料比例:</label>
                <input  id="rawMaterialPropertion${idSuffix}" name="YCLBL" class="coralui-textbox" />
            </div>
            <div class="app-inputdiv6">
                <label class="app-input-label" style="width:20%;">加工工艺:</label>
                <div id="processTechnique${idSuffix}" class="coralui-checkboxlist" name="JGGY"
                         data-options="value:'1',column:6,data:[{value:'1',text:'浸润'},{value:'2',text:'研磨'},{value:'3',text:'烘干'},{value:'4',text:'萃取'},
                         {value:'5',text:'速冻'},{value:'6',text:'搅拌'}]"></div>
            </div>
            </div>
        </form>
            <div class="app-bsearch-tbar">
                    <%--<div style="margin-top: 10px;height:60px">--%>
                    <%--</div>--%>
            </div>

        <ces:layoutRegion region="center" split="true">

            <div class='homeSpan' style="margin-top: -23px;"><div><div style='margin-left:20px;width: 250px;' id="nva${idSuffix}"> - 数据维护 </div></div></div>
            <div id="nsxgzGridDiv${idSuffix}" style="height:632px">
                <ces:grid rowNum="10" multiselect='true' id="pGridId${idSuffix}" height="auto"  rownumbers="true" fitStyle="fill"  >
                    <ces:gridCols>
                        <ces:gridCol name="qydm" width="10" hidden="true">企业代码</ces:gridCol>
                        <ces:gridCol name="scfabh" width="10" hidden="true">生产方案编号</ces:gridCol>
                        <ces:gridCol name="FLMC" width="120" formatter="text" >辅料品名</ces:gridCol>
                        <ces:gridCol name="FLCD" width="80" formatter="combobox">辅料产地</ces:gridCol>
                        <ces:gridCol name="FLSYBL" width="80" formatter="combobox">辅料使用比例</ces:gridCol>
                        <ces:gridCol name="FLJYB" width="80" formatter="combobox">辅料净药比</ces:gridCol>
                        <ces:gridCol name="CZ" formatter="toolbar"
                                     formatoptions="onClick:$.ns('namespaceId${idSuffix}').toolbarClick1,
						             data:[{'label': '添加', 'id':'addBtn', 'disabled': 'false','type': 'button'},{'label': '删除', 'id':'delBtn', 'disabled': 'false','type': 'button'}]" width="120">操作</ces:gridCol>
                    </ces:gridCols>
                    <ces:gridPager gridId="pGridId${idSuffix}"></ces:gridPager>
                </ces:grid>
            </div>
        </ces:layoutRegion>

</div>
<script type="text/javascript">
   var _colModel =[
       {name:'qydm',hidden:true,width:10},
       {name:'scfabh',hidden:true,width:10},
       {name:'FLMC',formatter:'convertCode',width:100},
       {name:'FLCD',edittype:'text',width:100},
       {name:'FLSYBL',edittype:'text',width:100},
       {name:'FLJYB',edittype:'text',width:100}];
   var _colNames=["企业代码","生产方案编号","辅料品名","辅料使用比例","辅料净药比"];
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
       //整体按钮条事件
       toolbarClick : function(event, button) {
           if (button.id == "save") {//保存的方法
               $("#trplyForm${idSuffix}").form().submit();

           } else if (button.id == "CFG_closeComponentZone") {//返回 或关闭
               CFG_clickCloseButton($('#maxDiv${idSuffix}').data('configInfo'));
           }
       },
       //第一个列表工具条事件
       toolbarClick1 : function(event,button) {
           var $grid = $("#pGridId${idSuffix}");
          // var $cgrid = $("#gridId${idSuffix}");
           //进行操作的行
           var rowId = event.data.rowId;
           pGridRowId = rowId;
           var gData = $grid.grid("getRowData",rowId);
           //添加事件
           if (button.id == "addBtn") {
               if(isUpdate){
                   $("#trplyForm${idSuffix}").form().submit();
                   //$cgrid.grid("clearGridData");
               }
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
                   });
               }
           }
       }


   });
   $(function() {
       var configInfo = CFG_initConfigInfo({
           /** 页面名称 */
           'page' : 'sdzycjjgscfaxz.jsp',
           /** 页面中的最大元素 */
           'maxEleInPage' : $('#maxDiv${idSuffix}'),
           /** 获取构件嵌入的区域 */
           'getEmbeddedZone' : function() {
               <!--return $('#layoutId${idSuffix}').layout('panel', 'center');-->
               return $('#max${idSuffix}');
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
       //var $grid = $("#gridId${idSuffix}");
       var lyrbhData = $.loadJson($.contextPath + "/zztrplylb!searchQyfzr.json");
       $("#schemeId${idSuffix}").textbox({readonly:true});
       var trplx = $.loadJson($.contextPath + "/zztrpcglb!searchTrplx.json");
       //初始化第一个列表数据

       //2、地块数据格式定义并添加onChange事件

       //3、生产档案数据格式定义并添加onChange事件

       //4、生产方案数据初始化，格式定义并添加onChange事件
       //5、农事项数据初始化，格式定义并添加onChange事件
       //初始化列表投入品名称列添加onChange事件
       //孙子列表投入品类型数据初始化，格式定义并添加onChange事件

       if(!isEmpty(inputParamId)){//修改操作初始化页面操作
           var $obj = $.loadJson($.contextPath + "/zztrplylb!searchTrckxx.json?id="+inputParamId);
           $("#trplyForm${idSuffix}").form("loadData",$obj);
           //区域信息下拉框数据初始化
          // reloadQyxx($obj.LYRBH);
           //地块信息下拉框数据初始化
         //  reloadDkxx($obj.LYRBH);
           //生产档案下拉框数据初始化
           //reloadScda($obj.LYRBH);
           //生产单列表数据初始化
           $pGrid.grid("option", "datatype", "json");
           $pGrid.grid("option","url", $.contextPath + "/sdzycypscda!searchTrpscdaxx.json?pid="+inputParamId);
           $pGrid.grid("reload");
           $pGrid.grid("option","onComplete",function (){reloadNsx();});
           temId = inputParamId;
       }else{
          // $("#LYSJ${idSuffix}").datepicker("setDate",new Date());
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
   //提交表单
   $("#trplyForm${idSuffix}").submit(function (){
      // if (!$("#trplyForm${idSuffix}").form("valid")){CFG_message("页面校验不通过","error"); return false;}
       var $grid = $("#pGridId${idSuffix}");
      // var $pGrid = $("#pGridId${idSuffix}");
       var editrow = $grid.grid("option", "editrow");
       var formData = $("#trplyForm${idSuffix}").form("formData",this);
       var gData = $grid.grid("getRowData");
      // var pgData = $pGrid.grid("getRowData");
       var isValid = $grid.grid('valid', editrow);
       var postData = {E_entityJson: $.config.toString(formData),
          // E_pEntityJson: $.config.toString(pgData),
           E_dEntitiesJson: $.config.toString(gData)};

       if(isValid){
           $.ajax({
               type: 'POST',
               url: $.contextPath +"/sdzycypscda!saveScdaxx.json",
               data: postData,
               dataType:"json",
               success: function (data) {
                  // $("#ID${idSuffix}").textbox("setValue",data.master.ID);
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

</script>
