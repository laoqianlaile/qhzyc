var CFG_RES_PATH = contextPath + "/cfg-resource";
var DHX_FOLDER   = "/cfg-resource/dhtmlx";
var DHX_RES_PATH = contextPath + DHX_FOLDER;
var SKIN_TYPE = getCookie("skin") ? getCookie("skin") : "dhx_skyblue";
var Skin = SKIN_TYPE.indexOf("dhx_web") > -1 ? "dhx_web" : SKIN_TYPE;
var DHX_PATH = DHX_RES_PATH + "/common/dhx/";
document.write("<link href='" + DHX_PATH + SKIN_TYPE + "/dhtmlx.css' type='text/css' rel='STYLESHEET'>");
//document.write("<link href='" + DHX_PATH + SKIN_TYPE + "/dhtmlx_custom.css' type='text/css' rel='STYLESHEET'>");
document.write("<link href='" + DHX_RES_PATH + "/common/css/style.css' type='text/css' rel='STYLESHEET'>");
document.write("<link href='" + DHX_RES_PATH + "/common/css/dhtmlxvault.css' type='text/css' rel='STYLESHEET'>");
document.write("<link href='" + DHX_RES_PATH + "/common/css/dhtmlxmessage_dhx_skyblue.css' type='text/css' rel='STYLESHEET'>");
document.write("<link href='" + DHX_RES_PATH + "/common/css/dhtmlxgrid_hmenu.css' type='text/css' rel='STYLESHEET'>");
document.write("<script type='text/javascript' src='" + DHX_RES_PATH + "/common/js/dhtmlx.js'></script>");
document.write("<script type='text/javascript' src='" + DHX_RES_PATH + "/common/js/dhtmlx_combo_ext.js'></script>");
document.write("<script type='text/javascript' src='" + DHX_RES_PATH + "/common/js/dhtmlx_grid_ext.js'></script>");

document.write("<script type='text/javascript' src='" + DHX_RES_PATH + "/common/js/dhtmlxtree_json.js'></script>");
document.write("<script type='text/javascript' src='" + DHX_RES_PATH + "/common/js/dhtmlx_toolbar_ext.js'></script>");
document.write("<script type='text/javascript' src='" + DHX_RES_PATH + "/common/js/dhtmlxlayout_pattern4w.js'></script>");
document.write("<script type='text/javascript' src='" + DHX_RES_PATH + "/common/js/dhtmlxlayout_pattern4c.js'></script>");
document.write("<script type='text/javascript' src='" + DHX_RES_PATH + "/common/js/dhtmlxlayout_pattern4e.js'></script>");
document.write("<script type='text/javascript' src='" + DHX_RES_PATH + "/common/js/dhtmlxvault.js'></script>");
document.write("<script type='text/javascript' src='" + DHX_RES_PATH + "/common/js/dhmtlxform_item_custom.js'></script>");
document.write("<script type='text/javascript' src='" + DHX_RES_PATH + "/common/js/dhtmlxform_dyn.js'></script>");
document.write("<script type='text/javascript' src='" + DHX_RES_PATH + "/common/js/PinYin4Js.js'></script>");
document.write("<script type='text/javascript' src='" + DHX_RES_PATH + "/common/js/dhtmlxgrid_excell_sub_row.js'></script>");
document.write("<script type='text/javascript' src='" + DHX_RES_PATH + "/common/js/dhtmlx_ajax_ext.js'></script>");
document.write("<script type='text/javascript' src='" + DHX_RES_PATH + "/common/js/CFG_common.js'></script>");
document.write("<script type='text/javascript' src='" + DHX_RES_PATH + "/common/js/ZeroClipboard.js'></script>");
//增加cookie
function addCookie(objName, objValue, objHours) {
    var str = objName + "=" + escape(objValue) + "; path=/";
    if (objHours > 0) {
        var date = new Date();
        var ms = objHours * 3600 * 1000;
        date.setTime(date.getTime() + ms);
        str += "; expires=" + date.toGMTString();
    }
    document.cookie = str;
}
// 读取cookie
function getCookie(objName) {
    var arrStr = document.cookie.split("; ");
    for (var i = 0; i < arrStr.length; i++) {
        var temp = arrStr[i].split("=");
        if (temp[0] == objName)
            return unescape(temp[1]);
    }
}
String.prototype.trim = function() {
    return this.replace(/(^\s*)|(\s*$)/g, '');
};
/**
 * 禁用浏览器的右键菜单
 * @param {event} e 事件
 * @return {Boolean}
 */
document.oncontextmenu = function(e) {
    if (e) {
        e.returnValue = false;
    } else {
        window.event.returnValue = false;
    }
    return false;
}
/**
 * 下载
 * @param {} url 下载表单url
 * @param {} params 参数
 * @param {} target 目标表单存放位置
 */
function download(url, params, target) {
    var obj = document.getElementById("frm_download");
    if (obj == null) {
        obj = document.createElement("iframe");
        obj.setAttribute("id", "frm_download");
        obj.setAttribute("style", "display: none;");
        document.body.appendChild(obj);
    }
    if (params) {
        var submitForm = document.createElement("FORM");
        submitForm.target = target || 'frm_download';
        submitForm.action = url;
        document.body.appendChild(submitForm);
        submitForm.method = "POST";
        for (p in params) {
            if (!params[p] || !p)
                continue;
            var input = document.createElement('input');
            input.setAttribute('name', p);
            input.setAttribute('type', 'hidden');
            if (typeof(params[p]) == 'string') {
                input.setAttribute('value', params[p].replace(/\"/g, '\\"'));
            } else {
                input.setAttribute('value', params[p]);
            }
            submitForm.appendChild(input);
        }
        submitForm.submit();
        document.body.removeChild(submitForm);
    } else {
        var frame = document.getElementById('frm_download');
        frame.src = url;
    }
}
/**
 * @returns 当前时间戳
 */
function getTimestamp() {
    return new Date().getTime();
}
/**
 * url中添加时间戳
 * @param url
 * @returns {String}
 */
function addTimestamp(url) {
	if (isEmpty(url)) return url;
    if (url.indexOf("?") > 0)
        return (url + "&P_timestamp=" + getTimestamp());
    return (url + "?P_timestamp=" + getTimestamp());
}
/**
 * 将null或"null"字符串置为""
 * @param obj
 * @returns
 */
function null2empty(obj) {
    if (null == obj || "null" == obj)
        return "";
    for (var p in obj) {
        if (obj[p] instanceof Object) {
            obj[p] = null2empty(obj[p]);
        } else if (null == obj[p] || "null" == obj[p]) {
            obj[p] = "";
        }
    }
    return obj;
}
/**
 * 判断是否为空串（null/"null"/""都作为空串处理）
 * @param obj
 * @returns {Boolean}
 */
function isEmpty(obj) {
    if (null == obj || "" == obj || "null" == obj) {
        return true;
    }
    return false;
}
/**
 * 判断是否不为空串（null/"null"/""都作为空串处理）
 * @param obj
 * @returns
 */
function isNotEmpty(obj) {
    return !isEmpty(obj);
}
/**
 * 将null或"null"字符串置为"0"
 * @param obj
 * @returns
 */
function null2zero(obj) {
    return isEmpty(obj) ? "0" : obj;
}