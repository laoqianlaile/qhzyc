/**
 * 反向追溯js
 * Created by bdz on 2015/7/2.
 */
var zxUrl=baseUrl+"/services/jaxrs/traceThain";
//$(function () {
//    var zsm = GetQueryString("zsm");
//    if(!$.isEmptyObject(zsm)){
//        $("#zsmInput").val(zsm);
//        toSearch(zsm)
//    }
//})
/* 产品历程信息 */
function getCplc(list) {
    var jinchang = list.JINCHANG;
    var jiaoyi = list.JIAOYI;
    var parent = list.PARENT;
    var jysj = "";//交易时间
    var jyz = "";//卖家
    var jyqy = "";//交易企业
    var jcz = "";//买家
    var jcsj = "";//进厂时间
    var jcqy = "";//进厂企业
    if (!$.isEmptyObject(jiaoyi)) {
        jysj = jiaoyi.CREATE_TIME;
        jyz = jiaoyi.JYZMC;
        jyqy = jiaoyi.QYMC;
    }
    if (!$.isEmptyObject(jinchang)) {
        jysj = jiaoyi.CREATE_TIME;
        jcz = jinchang.JYZMC;
        jcsj = jinchang.CREATE_TIME;
        jcqy = jinchang.QYMC;
    }
    if (!$.isEmptyObject(parent)) {
        getCplc(parent)
    }
    if (jiaoyi.XTLX == "1" || jiaoyi.XTLX == "2"){//不显示种植养殖历程
        createLi(jcsj, "进入“" + jcqy + "”");
        return;
    }
    if (jiaoyi.XTLX == "4") {
        $("#revTrace").append("<div name='SZCDJYZH' style='display:none'>" + jiaoyi.SZCDJYZH + "</div>");
        createLi(jiaoyi.TZRQ, "在“" + jiaoyi.QYMC + "”屠宰");
    } else if (jiaoyi.XTLX == "3") {
        $("#revTrace").append("<div name='JCLHBH' style='display:none'>" + jiaoyi.JCLHBH + "</div>");
    } else if (jiaoyi.XTLX == "5") {
        $("#revTrace").append("<div name='ZSPZH' style='display:none'>" + jiaoyi.ZSPZH + "</div>");
    }
    createLi(jysj, "在“" + jyqy + "”由“" + jyz + "”出售给“" + jcz + "”");
    createLi(jcsj, "进入“" + jcqy + "”");
}
/**
 * 创建产品历程信息<li>
 * @param date 日期
 * @param str  操作文本
 */
function createLi(date, str) {
    if (!$.isEmptyObject(date)) {
        var ul = $("#revTrace");
        var li = $("<li></li>");
        li.append("<span class='link'></span>");
        var span = $("<span class='lifetext'></span>");
        span.append("<span class='lifetextmid'>于<span class='lifetime'>" + date + "</span>" + str + "</span>");
        li.append(span);
        ul.append(li);
    }
}
/**
 * 加载配料
 * @param list 配料list
 */
function loadPlxx(list) {
    var div = $("#plDiv");
    $.each(list, function (index) {
        //var aPan = $("<a class='mixturebtn' href='tracingcodeSearch.html?zsm=" + this.ZSPZH + "' target='_blank'><span>" + this.YLMC + "</span><span class='mixturesearch'></span></a>");
        var aPan = $("<a class='mixturebtn' onclick='toSearch(\""+this.ZSPZH+"\")'><span>" + this.YLMC + "</span><span class='mixturesearch'></span></a>");
        if (index != list.length - 1) {
            aPan.append(",");
        }
        div.append(aPan);
    })
}

/**
 * 插入table内容
 * @param tableId
 * @param titleList
 * @param dataList
 */
function setTableList(tableId, titleList, titleBmList, dataList) {
    var tableItem = $("#" + tableId);
    tableItem.append(setThead(titleList));
    tableItem.append(setTbody(titleBmList, dataList));
}
/**
 * 插入thead
 * @param titleList
 * @returns {*|jQuery|HTMLElement}
 */
function setThead(titleList) {
    var tHead = $("<thead></thead>");
    $.each(titleList, function () {
        tHead.append("<th>" + this + "</th>");
    })
    return tHead;
}
/**
 * 插入tbody
 * @param dataList
 * @returns {*|jQuery|HTMLElement}
 */
function setTbody(titleBmList, dataList) {
    var tBody = $("<tbody></tbody>");
    $.each(dataList, function (index, element1) {
        var tr = $("<tr></tr>");
        $.each(titleBmList, function (index, element2) {
            var td = $("<td></td>");
            td.append(element1[element2]);
            tr.append(td);
        })
        tBody.append(tr);
    })
    return tBody;
}