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

  .min-div {
    width: 35%;
    float: left;
    display: inline;
  }

  .min-div2 {
    width: 35%;
    float: right;
    display: inline;
  }
</style>
<div id="max${idSuffix}" class="fill">
  <div class="fill">
    <div class="toolbarsnav clearfix">
      <ces:toolbar id="toolbarId${idSuffix}" onClick="$.ns('namespaceId${idSuffix}').toolbarClick"
                   data="['->',{'label': '保存', 'id':'save', 'disabled': 'false','cls':'save_tb','type': 'button'},{'label': '返回', 'id':'CFG_closeComponentZone','cls':'return_tb', 'disabled': 'false','type': 'button'}]"
              >
      </ces:toolbar>
      <div class='homeSpan' style="margin-top: -23px;"><div><div style='margin-left:20px;width: 150px;' id="nva${idSuffix}"> - 投入品信息 - 修改 </div></div></div>
    </div>
    <form id="trplyForm${idSuffix}" method="post" class="coralui-form">
      <div class="app-inputdiv4" style ="height:32px;display: none">
        <input id="ID${idSuffix}" class="coralui-textbox" name="ID"/>
      </div>

      <div class="fillwidth colspan2 clearfix">
        <!------------------ 第一排开始---------------->
        <div class="app-inputdiv6"  style="display: none">
          <label class="app-input-label" >投入品编号：</label>
          <input id="TRPBH${idSuffix}" name="TRPBH"  class="coralui-textbox"/>
        </div>
        <div class="app-inputdiv6">
          <label class="app-input-label" >类型：</label>
          <input id="LX${idSuffix}" name="LX"  data-options="required:true" />
        </div>
        <div class="app-inputdiv6">
          <label class="app-input-label" >投入品名称：</label>
          <input id="TRPMC${idSuffix}" name="TRPMC" class="coralui-textbox" data-options="required:true" />
        </div>

        <div id="PP1${idSuffix}" class="app-inputdiv6">
          <label class="app-input-label" >品牌：</label>
          <input id="PP${idSuffix}" name="PP"  data-options="required:true" class="coralui-textbox" />
        </div>
        <div id="ML1${idSuffix}" class="app-inputdiv6">
          <label class="app-input-label" >马力：</label>
          <input id="ML${idSuffix}" name="ML"  data-options="required:true" class="coralui-textbox"/>
        </div>
        <div id="XH1${idSuffix}" class="app-inputdiv6">
          <label class="app-input-label" >型号：</label>
          <input id="XH${idSuffix}" name="XH"  data-options="required:true" class="coralui-textbox"/>
        </div>

        <div id="TYM1${idSuffix}" class="app-inputdiv6">
          <label class="app-input-label" >通用名：</label>
          <input id="TYM${idSuffix}" name="TYM"  data-options="required:true" class="coralui-textbox"/>
        </div>
        <div id="BZGG1${idSuffix}" class="app-inputdiv6">
          <label class="app-input-label" >包装规格：</label>
          <div class="min-div">
            <input id="BZGG${idSuffix}" name="BZGG" data-options="required:true" class="coralui-textbox"/>
          </div>
          <div class="min-div2"><input id="BZGGDW${idSuffix}" name="BZGGDW"/></div>
          <%--<input id="BZGG${idSuffix}" name="BGZZ"  data-options="required:true" class="coralui-textbox"/>--%>
          <%--<input id="BZGG${idSuffix}" name="SJLYR"  data-options="required:true" />--%>
        </div>
        <div id="TJYL1${idSuffix}" class="app-inputdiv6">
          <label class="app-input-label" >推荐用量(亩)：</label>
          <input id="TJYL${idSuffix}" name="TJYL"  data-options="required:true" class="coralui-textbox"/>
        </div>
        <div id="YT1${idSuffix}" class="app-inputdiv6">
          <label class="app-input-label" >用途：</label>
          <input id="YT${idSuffix}" name="YT"  data-options="required:true" class="coralui-textbox"/>
        </div>
        <div id="ZXBZ1${idSuffix}" class="app-inputdiv6">
          <label class="app-input-label" >执行标准：</label>
          <input id="ZXBZ${idSuffix}" name="ZXBZ" class="coralui-textbox"/>
        </div>
        <div id="YXCF1${idSuffix}" class="app-inputdiv6">
          <label class="app-input-label" >有效成分：</label>
          <input id="YXCF${idSuffix}" name="YXCF"  data-options="required:true" class="coralui-textbox"/>
        </div>
        <div id="AQQ1${idSuffix}" class="app-inputdiv6">
          <label class="app-input-label" >安全期(天)：</label>
          <input id="AQQ${idSuffix}" name="AQQ"  data-options="required:true" class="coralui-textbox"/>
        </div>
        <div id="FLLX1${idSuffix}" class="app-inputdiv6">
          <label class="app-input-label" >肥料类型：</label>
          <input id="FLLX${idSuffix}" name="FLLX"  data-options="required:true" />
        </div>
        <%--<div class="app-inputdiv4">--%>
          <%--<label class="app-input-label" >备注：</label>--%>
          <%--<input id="BZ${idSuffix}" name="BZ"  data-options="required:true" class="coralui-textbox"/>--%>
        <%--</div>--%>
        <div class="app-inputdiv12" id="line22${idSuffix}">
          <label class="app-input-label">备注：</label>
          <textarea id="BZ${idSuffix}" name="BZ" class="coralui-textbox"/>
        </div>

      </div>
    </form>
  </div>
</div>
<script>

  $.extend($.ns("namespaceId${idSuffix}"), {
    toolbarClick: function (event, button) {
      if (button.id == "save") {//保存的方法

        $("#trplyForm${idSuffix}").form().submit();

      } else if (button.id == "CFG_closeComponentZone" || button.id == "CFG_closeComponentDialog" ) {//返回 或关闭

        <%--CFG_clickCloseButton($('#max${idSuffix}').data('configInfo'));--%>
        var configInfo = $("#max${idSuffix}").data('configInfo');

        CFG_clickCloseButton(configInfo);
      }
    },
  })
  $(function(){
    //alert('xg');
    var configInfo = CFG_initConfigInfo({
      /** 页面名称 */
      'page': 'zztrpxxglczxg.jsp',
      /** 页面中的最大元素 */
      'maxEleInPage': $('#max${idSuffix}'),
      /** 获取构件嵌入的区域 */
      'getEmbeddedZone': function () {
        <!--return $('#layoutId${idSuffix}').layout('panel', 'center');-->
        return $('#max${idSuffix}');
      },
      /** 初始化预留区 */
      'initReserveZones': function (configInfo) {
        //CFG_addToolbarButtons(configInfo, $('#toolbarId${idSuffix}'), 'toolBarReserve', $('#toolbarId${idSuffix}').toolbar("getLength"));
      },
      /** 获取返回按钮添加的位置 */
      'setReturnButton': function (configInfo) {
        //CFG_setReturnButton(configInfo, $('#toolbarId${idSuffix}'));
        CFG_setCloseButton(configInfo, $('#toolbarId${idSuffix}'));
      },
      /** 获取关闭按钮添加的位置 */
      'setCloseButton': function (configInfo) {
        CFG_setCloseButton(configInfo, $('#toolbarId${idSuffix}'));
      },
      /** 页面初始化的方法 */
      'bodyOnLoad': function (configInfo) {
        $('#LX${idSuffix}').combobox({
          textField:'SJMC',
          valueField:'SJBM'
        });
        $('#BZGGDW${idSuffix}').combobox({
          textField:'SJMC',
          valueField:'SJBM'
        });
        $('#FLLX${idSuffix}').combobox({
          textField:'SJMC',
          valueField:'SJBM'
        });
        $('#TYM1${idSuffix}').css('display','none');
        $('#BZGG1${idSuffix}').css('display','none');
        $('#FLLX1${idSuffix}').css('display','none');
        $('#YT1${idSuffix}').css('display','none');
        $('#TJYL1${idSuffix}').css('display','none');
        $('#ZXBZ1${idSuffix}').css('display','none');
        $('#YXCF1${idSuffix}').css('display','none');
        $('#AQQ1${idSuffix}').css('display','none');
        $('#PP1${idSuffix}').css('display','none');
        $('#XH1${idSuffix}').css('display','none');
        $('#ML1${idSuffix}').css('display','none');
        var lxData = $.loadJson($.contextPath + "/zztrpxxglcz!getLx.json" );
        $('#LX${idSuffix}').combobox('reload',lxData);
        var fllxData = $.loadJson($.contextPath + "/zztrpxxglcz!getFllx.json");
        $('#FLLX${idSuffix}').combobox('reload',fllxData);
        var bzggdwData = $.loadJson($.contextPath + "/zztrpxxglcz!getBzggdw.json");
        $('#BZGGDW${idSuffix}').combobox('reload',bzggdwData);
        var id = CFG_getInputParamValue(configInfo, 'inputRowid');
        var formData = $.loadJson($.contextPath + "/zztrpxxglcz!gettheFormdata.json?id="+id);

        $('#ID${idSuffix}').textbox('setValue',formData.ID);
        $('#TRPBH${idSuffix}').textbox('setValue',formData.TRPBH);
        $('#LX${idSuffix}').combobox('setValue',formData.LX);
        $('#TRPMC${idSuffix}').textbox('setValue',formData.TRPMC);
        $('#PP${idSuffix}').textbox('setValue',formData.PP);
        $('#ML${idSuffix}').textbox('setValue',formData.ML);
        $('#XH${idSuffix}').textbox('setValue',formData.XH);
        $('#TYM${idSuffix}').textbox('setValue',formData.TYM);
        $('#BZGG${idSuffix}').textbox('setValue',formData.BZGG);
        $('#BZGGDW${idSuffix}').combobox('setValue',formData.BZGGDW);
        $('#TJYL${idSuffix}').textbox('setValue',formData.TJYL);
        $('#YT${idSuffix}').textbox('setValue',formData.YT);
        $('#ZXBZ${idSuffix}').textbox('setValue',formData.ZXBZ);
        $('#YXCF${idSuffix}').textbox('setValue',formData.YXCF);
        $('#AQQ${idSuffix}').textbox('setValue',formData.AQQ);
        $('#FLLX${idSuffix}').combobox('setValue',formData.FLLX==null?'':formData.FLLX);
        $('#BZ${idSuffix}').textbox('setValue',formData.BZ);
        <%--$('#LX${idSuffix}').combobox('option', 'onChange', function (e, data) {--%>
          // ui.getItemJQ('TRBMC').textbox('setValue','');
          // ui.getItemJQ('TYM').textbox('setValue','');
          //档类型发生变化时，清空数据
          <%--$('#PP${idSuffix}').textbox('setValue','');--%>
          <%--$('#ML${idSuffix}').textbox('setValue','');--%>
          <%--$('#XH${idSuffix}').textbox('setValue','');--%>
          <%--$('#TYM${idSuffix}').textbox('setValue','');--%>
          <%--$('#BZGG${idSuffix}').textbox('setValue','');--%>
          <%--$('#BZGGDW${idSuffix}').combobox('setValue','');--%>
          <%--$('#TJYL${idSuffix}').textbox('setValue','');--%>
          <%--$('#YT${idSuffix}').textbox('setValue','');--%>
          <%--$('#ZXBZ${idSuffix}').textbox('setValue','');--%>
          <%--$('#YXCF${idSuffix}').textbox('setValue','');--%>
          <%--$('#AQQ${idSuffix}').textbox('setValue','');--%>
          <%--$('#FLLX${idSuffix}').combobox('setValue','');--%>
          var bewlx = formData.LX;
          if(bewlx=='NY'){
            $('#BZGG1${idSuffix}').css('display','none');
            $('#FLLX1${idSuffix}').css('display','none');
            $('#YT1${idSuffix}').css('display','none');
            $('#TJYL1${idSuffix}').css('display','none');
            $('#ZXBZ1${idSuffix}').css('display','none');
            $('#YXCF1${idSuffix}').css('display','none');
            $('#AQQ1${idSuffix}').css('display','none');
            $('#PP1${idSuffix}').css('display','none');
            $('#XH1${idSuffix}').css('display','none');
            $('#ML1${idSuffix}').css('display','none');
            $('#TYM1${idSuffix}').css('display','inline');
            $('#BZGG1${idSuffix}').css('display','inline');
            $('#YT1${idSuffix}').css('display','inline');
            $('#ZXBZ1${idSuffix}').css('display','inline');
            $('#YXCF1${idSuffix}').css('display','inline');
            $('#TJYL1${idSuffix}').css('display','inline');
            $('#AQQ1${idSuffix}').css('display','inline');
          }
          else if(bewlx=='FL'){
            $('#BZGG1${idSuffix}').css('display','none');
            $('#FLLX1${idSuffix}').css('display','none');
            $('#YT1${idSuffix}').css('display','none');
            $('#TJYL1${idSuffix}').css('display','none');
            $('#ZXBZ1${idSuffix}').css('display','none');
            $('#YXCF1${idSuffix}').css('display','none');
            $('#AQQ1${idSuffix}').css('display','none');
            $('#PP1${idSuffix}').css('display','none');
            $('#XH1${idSuffix}').css('display','none');
            $('#ML1${idSuffix}').css('display','none');
            $('#TYM1${idSuffix}').css('display','inline');
            $('#BZGG1${idSuffix}').css('display','inline');
            $('#YT1${idSuffix}').css('display','inline');
            $('#ZXBZ1${idSuffix}').css('display','inline');
            $('#YXCF1${idSuffix}').css('display','inline');
            $('#TJYL1${idSuffix}').css('display','inline');
            $('#AQQ1${idSuffix}').css('display','inline');
            $('#FLLX1${idSuffix}').css('display','block');
            $('#BZ1${idSuffix}').css('display','block');
          }else if(bewlx=='ZZ'){
            $('#BZGG1${idSuffix}').css('display','none');
            $('#FLLX1${idSuffix}').css('display','none');
            $('#YT1${idSuffix}').css('display','none');
            $('#TJYL1${idSuffix}').css('display','none');
            $('#ZXBZ1${idSuffix}').css('display','none');
            $('#YXCF1${idSuffix}').css('display','none');
            $('#AQQ1${idSuffix}').css('display','none');
            $('#PP1${idSuffix}').css('display','none');
            $('#XH1${idSuffix}').css('display','none');
            $('#ML1${idSuffix}').css('display','none');
            $('#TYM1${idSuffix}').css('display','inline');
            $('#BZGG1${idSuffix}').css('display','inline');
            $('#TJYL1${idSuffix}').css('display','inline');
          }else if(bewlx=='NJJ'){
            $('#BZGG1${idSuffix}').css('display','none')
            $('#FLLX1${idSuffix}').css('display','none')
            $('#YT1${idSuffix}').css('display','none')
            $('#TJYL1${idSuffix}').css('display','none')
            $('#ZXBZ1${idSuffix}').css('display','none')
            $('#YXCF1${idSuffix}').css('display','none')
            $('#AQQ1${idSuffix}').css('display','none')
            $('#PP1${idSuffix}').css('display','none')
            $('#XH1${idSuffix}').css('display','none')
            $('#ML1${idSuffix}').css('display','none')
            $('#PP1${idSuffix}').css('display','inline');
            $('#XH1${idSuffix}').css('display','inline');
            $('#ML1${idSuffix}').css('display','inline');
            //在这个后面加一个换行符调整页面样式

          }
        $('#LX${idSuffix}').combobox('option', 'onChange', function (e, data) {
          // ui.getItemJQ('TRBMC').textbox('setValue','');
          // ui.getItemJQ('TYM').textbox('setValue','');
          //档类型发生变化时，清空数据
          $('#PP${idSuffix}').textbox('setValue','');
          $('#ML${idSuffix}').textbox('setValue','');
          $('#XH${idSuffix}').textbox('setValue','');
          $('#TYM${idSuffix}').textbox('setValue','');
          $('#BZGG${idSuffix}').textbox('setValue','');
          $('#BZGGDW${idSuffix}').combobox('setValue','');
          $('#TJYL${idSuffix}').textbox('setValue','');
          $('#YT${idSuffix}').textbox('setValue','');
          $('#ZXBZ${idSuffix}').textbox('setValue','');
          $('#YXCF${idSuffix}').textbox('setValue','');
          $('#AQQ${idSuffix}').textbox('setValue','');
          $('#FLLX${idSuffix}').combobox('setValue','');
          var bewlx1 = data.text;
          if(bewlx1=='农药'){
            $('#BZGG1${idSuffix}').css('display','none');
            $('#FLLX1${idSuffix}').css('display','none');
            $('#YT1${idSuffix}').css('display','none');
            $('#TJYL1${idSuffix}').css('display','none');
            $('#ZXBZ1${idSuffix}').css('display','none');
            $('#YXCF1${idSuffix}').css('display','none');
            $('#AQQ1${idSuffix}').css('display','none');
            $('#PP1${idSuffix}').css('display','none');
            $('#XH1${idSuffix}').css('display','none');
            $('#ML1${idSuffix}').css('display','none');
            $('#TYM1${idSuffix}').css('display','inline');
            $('#BZGG1${idSuffix}').css('display','inline');
            $('#YT1${idSuffix}').css('display','inline');
            $('#ZXBZ1${idSuffix}').css('display','inline');
            $('#YXCF1${idSuffix}').css('display','inline');
            $('#TJYL1${idSuffix}').css('display','inline');
            $('#AQQ1${idSuffix}').css('display','inline');
          }
          else if(bewlx1=='肥料'){
            $('#BZGG1${idSuffix}').css('display','none');
            $('#FLLX1${idSuffix}').css('display','none');
            $('#YT1${idSuffix}').css('display','none');
            $('#TJYL1${idSuffix}').css('display','none');
            $('#ZXBZ1${idSuffix}').css('display','none');
            $('#YXCF1${idSuffix}').css('display','none');
            $('#AQQ1${idSuffix}').css('display','none');
            $('#PP1${idSuffix}').css('display','none');
            $('#XH1${idSuffix}').css('display','none');
            $('#ML1${idSuffix}').css('display','none');
            $('#TYM1${idSuffix}').css('display','inline');
            $('#BZGG1${idSuffix}').css('display','inline');
            $('#YT1${idSuffix}').css('display','inline');
            $('#ZXBZ1${idSuffix}').css('display','inline');
            $('#YXCF1${idSuffix}').css('display','inline');
            $('#TJYL1${idSuffix}').css('display','inline');
            $('#AQQ1${idSuffix}').css('display','inline');
            $('#FLLX1${idSuffix}').css('display','block');
            $('#BZ1${idSuffix}').css('display','block');
          }else if(bewlx1=='种子'){
            $('#BZGG1${idSuffix}').css('display','none');
            $('#FLLX1${idSuffix}').css('display','none');
            $('#YT1${idSuffix}').css('display','none');
            $('#TJYL1${idSuffix}').css('display','none');
            $('#ZXBZ1${idSuffix}').css('display','none');
            $('#YXCF1${idSuffix}').css('display','none');
            $('#AQQ1${idSuffix}').css('display','none');
            $('#PP1${idSuffix}').css('display','none');
            $('#XH1${idSuffix}').css('display','none');
            $('#ML1${idSuffix}').css('display','none');
            $('#TYM1${idSuffix}').css('display','inline');
            $('#BZGG1${idSuffix}').css('display','inline');
            $('#TJYL1${idSuffix}').css('display','inline');
          }else if(bewlx1=='农机具'){
            $('#BZGG1${idSuffix}').css('display','none')
            $('#FLLX1${idSuffix}').css('display','none')
            $('#YT1${idSuffix}').css('display','none')
            $('#TJYL1${idSuffix}').css('display','none')
            $('#ZXBZ1${idSuffix}').css('display','none')
            $('#YXCF1${idSuffix}').css('display','none')
            $('#AQQ1${idSuffix}').css('display','none')
            $('#PP1${idSuffix}').css('display','none')
            $('#XH1${idSuffix}').css('display','none')
            $('#ML1${idSuffix}').css('display','none')
            $('#TYM1${idSuffix}').css('display','inline');
            $('#PP1${idSuffix}').css('display','inline');
            $('#XH1${idSuffix}').css('display','inline');
            $('#ML1${idSuffix}').css('display','inline');
            //在这个后面加一个换行符调整页面样式

          }
//                    if (bewlx == '2') {
//                        fllx.combobox('enable');
//                    } else {
//                        fllx.combobox('disable');
//                        fllx.combobox('setValue', '');
//                    }
        });
//                    if (bewlx == '2') {
//                        fllx.combobox('enable');
//                    } else {
//                        fllx.combobox('disable');
//                        fllx.combobox('setValue', '');
//                    }
//        });
      }

    })
    if (configInfo) {
      //alert("系统参数：\t" + "关联的系统参数=" + CFG_getSystemParamValue(configInfo, 'sysParam1')
      //		+ "\n构件自身参数：\t" + "selfParam1=" + CFG_getSelfParamValue(configInfo, 'selfParam1')
      //		+ "\n构件入参：\t" + "inputParamName_1=" + CFG_getInputParamValue(configInfo, 'inputParamId'));
      //alert(CFG_getInputParamValue(configInfo, 'inputParamId'));
      //alert(CFG_getInputParamValue(configInfo, 'inputParamId1'));

    }
    configInfo.CFG_outputParams = {'success': 'otp'};
//    SJMC,SJBM
    <%--$('#LX${idSuffix}').combobox({--%>
      <%--textField:'SJMC',--%>
      <%--valueField:'SJBM'--%>
    <%--});--%>
    <%--$('#BZGGDW${idSuffix}').combobox({--%>
      <%--textField:'SJMC',--%>
      <%--valueField:'SJBM'--%>
    <%--});--%>
    <%--$('#FLLX${idSuffix}').combobox({--%>
      <%--textField:'SJMC',--%>
      <%--valueField:'SJBM'--%>
    <%--});--%>
    <%--$('#TYM1${idSuffix}').css('display','none');--%>
    <%--$('#BZGG1${idSuffix}').css('display','none');--%>
    <%--$('#FLLX1${idSuffix}').css('display','none');--%>
    <%--$('#YT1${idSuffix}').css('display','none');--%>
    <%--$('#TJYL1${idSuffix}').css('display','none');--%>
    <%--$('#ZXBZ1${idSuffix}').css('display','none');--%>
    <%--$('#YXCF1${idSuffix}').css('display','none');--%>
    <%--$('#AQQ1${idSuffix}').css('display','none');--%>
    <%--$('#PP1${idSuffix}').css('display','none');--%>
    <%--$('#XH1${idSuffix}').css('display','none');--%>
    <%--$('#ML1${idSuffix}').css('display','none');--%>
    <%--var lxData = $.loadJson($.contextPath + "/zztrpxxglcz!getLx.json" );--%>
    <%--$('#LX${idSuffix}').combobox('reload',lxData);--%>
    <%--var fllxData = $.loadJson($.contextPath + "/zztrpxxglcz!getFllx.json");--%>
    <%--$('#FLLX${idSuffix}').combobox('reload',fllxData);--%>
    <%--var bzggdwData = $.loadJson($.contextPath + "/zztrpxxglcz!getBzggdw.json");--%>
    <%--$('#BZGGDW${idSuffix}').combobox('reload',bzggdwData);--%>
    <%--$('#LX${idSuffix}').combobox('option', 'onChange', function (e, data) {--%>
      <%--// ui.getItemJQ('TRBMC').textbox('setValue','');--%>
      <%--// ui.getItemJQ('TYM').textbox('setValue','');--%>
      <%--//档类型发生变化时，清空数据--%>
      <%--$('#PP${idSuffix}').textbox('setValue','');--%>
      <%--$('#ML${idSuffix}').textbox('setValue','');--%>
      <%--$('#XH${idSuffix}').textbox('setValue','');--%>
      <%--$('#TYM${idSuffix}').textbox('setValue','');--%>
      <%--$('#BZGG${idSuffix}').textbox('setValue','');--%>
      <%--$('#BZGGDW${idSuffix}').combobox('setValue','');--%>
      <%--$('#TJYL${idSuffix}').textbox('setValue','');--%>
      <%--$('#YT${idSuffix}').textbox('setValue','');--%>
      <%--$('#ZXBZ${idSuffix}').textbox('setValue','');--%>
      <%--$('#YXCF${idSuffix}').textbox('setValue','');--%>
      <%--$('#AQQ${idSuffix}').textbox('setValue','');--%>
      <%--$('#FLLX${idSuffix}').combobox('setValue','');--%>

      <%--var bewlx = data.text;--%>
      <%--if(bewlx=='农药'){--%>
        <%--$('#BZGG1${idSuffix}').css('display','none');--%>
        <%--$('#FLLX1${idSuffix}').css('display','none');--%>
        <%--$('#YT1${idSuffix}').css('display','none');--%>
        <%--$('#TJYL1${idSuffix}').css('display','none');--%>
        <%--$('#ZXBZ1${idSuffix}').css('display','none');--%>
        <%--$('#YXCF1${idSuffix}').css('display','none');--%>
        <%--$('#AQQ1${idSuffix}').css('display','none');--%>
        <%--$('#PP1${idSuffix}').css('display','none');--%>
        <%--$('#XH1${idSuffix}').css('display','none');--%>
        <%--$('#ML1${idSuffix}').css('display','none');--%>
        <%--$('#BZGG1${idSuffix}').css('display','inline');--%>
        <%--$('#YT1${idSuffix}').css('display','inline');--%>
        <%--$('#ZXBZ1${idSuffix}').css('display','inline');--%>
        <%--$('#YXCF1${idSuffix}').css('display','inline');--%>
        <%--$('#TJYL1${idSuffix}').css('display','inline');--%>
        <%--$('#AQQ1${idSuffix}').css('display','inline');--%>
      <%--}--%>
      <%--else if(bewlx=='肥料'){--%>
        <%--$('#BZGG1${idSuffix}').css('display','none');--%>
        <%--$('#FLLX1${idSuffix}').css('display','none');--%>
        <%--$('#YT1${idSuffix}').css('display','none');--%>
        <%--$('#TJYL1${idSuffix}').css('display','none');--%>
        <%--$('#ZXBZ1${idSuffix}').css('display','none');--%>
        <%--$('#YXCF1${idSuffix}').css('display','none');--%>
        <%--$('#AQQ1${idSuffix}').css('display','none');--%>
        <%--$('#PP1${idSuffix}').css('display','none');--%>
        <%--$('#XH1${idSuffix}').css('display','none');--%>
        <%--$('#ML1${idSuffix}').css('display','none');--%>
        <%--$('#BZGG1${idSuffix}').css('display','inline');--%>
        <%--$('#YT1${idSuffix}').css('display','inline');--%>
        <%--$('#ZXBZ1${idSuffix}').css('display','inline');--%>
        <%--$('#YXCF1${idSuffix}').css('display','inline');--%>
        <%--$('#TJYL1${idSuffix}').css('display','inline');--%>
        <%--$('#AQQ1${idSuffix}').css('display','inline');--%>
        <%--$('#FLLX1${idSuffix}').css('display','block');--%>
        <%--$('#BZ1${idSuffix}').css('display','block');--%>
      <%--}else if(bewlx=='种子'){--%>
        <%--$('#BZGG1${idSuffix}').css('display','none');--%>
        <%--$('#FLLX1${idSuffix}').css('display','none');--%>
        <%--$('#YT1${idSuffix}').css('display','none');--%>
        <%--$('#TJYL1${idSuffix}').css('display','none');--%>
        <%--$('#ZXBZ1${idSuffix}').css('display','none');--%>
        <%--$('#YXCF1${idSuffix}').css('display','none');--%>
        <%--$('#AQQ1${idSuffix}').css('display','none');--%>
        <%--$('#PP1${idSuffix}').css('display','none');--%>
        <%--$('#XH1${idSuffix}').css('display','none');--%>
        <%--$('#ML1${idSuffix}').css('display','none');--%>
        <%--$('#BZGG1${idSuffix}').css('display','inline');--%>
        <%--$('#TJYL1${idSuffix}').css('display','inline');--%>
      <%--}else if(bewlx=='农机具'){--%>
        <%--$('#BZGG1${idSuffix}').css('display','none')--%>
        <%--$('#FLLX1${idSuffix}').css('display','none')--%>
        <%--$('#YT1${idSuffix}').css('display','none')--%>
        <%--$('#TJYL1${idSuffix}').css('display','none')--%>
        <%--$('#ZXBZ1${idSuffix}').css('display','none')--%>
        <%--$('#YXCF1${idSuffix}').css('display','none')--%>
        <%--$('#AQQ1${idSuffix}').css('display','none')--%>
        <%--$('#PP1${idSuffix}').css('display','none')--%>
        <%--$('#XH1${idSuffix}').css('display','none')--%>
        <%--$('#ML1${idSuffix}').css('display','none')--%>
        <%--$('#PP1${idSuffix}').css('display','inline');--%>
        <%--$('#XH1${idSuffix}').css('display','inline');--%>
        <%--$('#ML1${idSuffix}').css('display','inline');--%>
        <%--//在这个后面加一个换行符调整页面样式--%>

      <%--}--%>
<%--//                    if (bewlx == '2') {--%>
<%--//                        fllx.combobox('enable');--%>
<%--//                    } else {--%>
<%--//                        fllx.combobox('disable');--%>
<%--//                        fllx.combobox('setValue', '');--%>
<%--//                    }--%>
    <%--});--%>

  })

  $("#trplyForm${idSuffix}").submit(function () {

    if (!$("#trplyForm${idSuffix}").form("valid")) {
      CFG_message("页面校验不通过", "error");
      return false;
    }
    var formdata = $("#trplyForm${idSuffix}").form("formData", false); //new FormData(this);//$("#trpcgForm${idSuffix}").serialize();
    $.ajax({
      type: 'POST',
      url: $.contextPath + "/zztrpxxglcz!save.json",
      data: {E_entityJson: $.config.toString(formdata)},
      timestamp: false,
      dataType: "json",
      success: function (data) {
        $("#ID${idSuffix}").textbox("setValue", data.ID);
        CFG_message("操作成功！", "success");
      },
      error: function () {
        CFG_message("操作失败！", "error");
      }
    })

    return false;
  })
</script>
