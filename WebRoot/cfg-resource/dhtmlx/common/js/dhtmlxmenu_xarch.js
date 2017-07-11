dhtmlXMenuObject.prototype.setParentIdName = function (name) {
	this._parentIdName = name;
};
dhtmlXMenuObject.prototype.setTopId = function (id) {
	this._topId = id;
};
dhtmlXMenuObject.prototype.enableDynamicLoading = function(url, icon, type) {
	if (typeof icon != 'boolean') {
		type = icon;
		icon = false;
	}

	this._data_type = type || 'xml';
	this._xmlLoader = new dtmlXMLLoaderObject(this["_"+this._data_type+"Parser"], this);
	this.dLoad = true;
	this.dLoadUrl = url;
	this.dLoadSign = (String(this.dLoadUrl).search(/\?/)==-1?"?":"&");
	this.loaderIcon = icon;
	this.init();
};
dhtmlXMenuObject.prototype.init = function() {
	if (this._isInited == true) return;
	if (this.dLoad) {
		this.callEvent("onXLS", []);
		if (this.dLoadUrl.indexOf('E_model_name') < 0) {
			this._xmlLoader.loadXML(this.dLoadUrl+this.dLoadSign+"E_model_name=menu&Q_EQ_"+(this._parentIdName||'parentId')+'='+(this._topId||"-1")+"&etc="+new Date().getTime()); // &&parentId=topId&"+this.topId+"&topId="+this.topId);
		} else {
			this._xmlLoader.loadXML(this.dLoadUrl+this.dLoadSign+"Q_EQ_"+(this._parentIdName||'parentId')+'='+(this._topId||"-1")+"&etc="+new Date().getTime());
		}
	} else {
		this._initTopLevelMenu();
		this._isInited = true;
	}
};
/* add top menu item, complex define that submenues are in presence */
dhtmlXMenuObject.prototype._renderToplevelItem = function(id, pos) {
	var main_self = this;
	var m = document.createElement("DIV");
	m.id = id;
	// custom css
	if (this.itemPull[id]["state"] == "enabled" && this.itemPull[id]["cssNormal"] != null) {
		m.className = this.itemPull[id]["cssNormal"];
	} else {
		m.className = "dhtmlxMenu_"+this.skin+"_TopLevel_Item_"+(this.itemPull[id]["state"]=="enabled"?"Normal":"Disabled");
	}
	
	// text
	if (this.itemPull[id]["title"] != "") {
		var t1 = document.createElement("DIV");
		t1.className = "top_level_text";
		t1.innerHTML = this.itemPull[id]["title"];
		m.appendChild(t1);
	}
	// tooltip
	if (this.itemPull[id]["tip"].length > 0) m.title = this.itemPull[id]["tip"];
	//
	// image in top level
	if ((this.itemPull[id]["imgen"]!="")||(this.itemPull[id]["imgdis"]!="")) {
		var imgTop=this.itemPull[id][(this.itemPull[id]["state"]=="enabled")?"imgen":"imgdis"];
		if (imgTop) {
			var img = document.createElement("IMG");
			img.border = "0";
			img.id = "image_"+id;
			img.src= this.imagePath+imgTop;
			img.className = "dhtmlxMenu_TopLevel_Item_Icon";
			if (m.childNodes.length > 0 && !this._rtl) m.insertBefore(img, m.childNodes[0]); else m.appendChild(img);
		}
	}
	m.onselectstart = function(e) { e = e || event; e.returnValue = false; return false; };
	m.oncontextmenu = function(e) { e = e || event; e.returnValue = false; return false; };
	// add container for top-level items if not exists yet
	if (!this.cont) {
		this.cont = document.createElement("DIV");
		this.cont.dir = "ltr";
		this.cont.className = (this._align=="right"?"align_right":"align_left");
		this.base.appendChild(this.cont);
	}
	// insert
	/*
	if (pos != null) { pos++; if (pos < 0) pos = 0; if (pos > this.base.childNodes.length - 1) pos = null; }
	if (pos != null) this.base.insertBefore(m, this.base.childNodes[pos]); else this.base.appendChild(m);
	*/
	if (pos != null) { pos++; if (pos < 0) pos = 0; if (pos > this.cont.childNodes.length - 1) pos = null; }
	if (pos != null) this.cont.insertBefore(m, this.cont.childNodes[pos]); else this.cont.appendChild(m);
	
	
	//
	this.idPull[m.id] = m;
	// create submenues
	if (this.itemPull[id]["complex"] && (!this.dLoad)) this._addSubMenuPolygon(this.itemPull[id]["id"], this.itemPull[id]["id"]);
	// events
	m.onmouseover = function() {
		if (main_self.menuMode == "web") { window.clearTimeout(main_self.menuTimeoutHandler); }
		// kick polygons and decelect before selected menues
		var i = main_self._getSubItemToDeselectByPolygon("parent");
		main_self._removeSubItemFromSelected(-1, -1);
		for (var q=0; q<i.length; q++) {
			if (i[q] != this.id) { main_self._hidePolygon(i[q]); }
			if ((main_self.idPull[i[q]] != null) && (i[q] != this.id)) {
				// custom css
				if (main_self.itemPull[i[q]]["cssNormal"] != null) {
					main_self.idPull[i[q]].className = main_self.itemPull[i[q]]["cssNormal"];
				} else {
					if (main_self.idPull[i[q]].className == "sub_item_selected") main_self.idPull[i[q]].className = "sub_item";
					main_self.idPull[i[q]].className = main_self.idPull[i[q]].className.replace(/Selected/g, "Normal");
				}
			}
		}
		// check if enabled
		if (main_self.itemPull[this.id]["state"] == "enabled") {
			this.className = "dhtmlxMenu_"+main_self.skin+"_TopLevel_Item_Selected";
			//
			main_self._addSubItemToSelected(this.id, "parent");
			main_self.menuSelected = (main_self.menuMode=="win"?(main_self.menuSelected!=-1?this.id:main_self.menuSelected):this.id);
			if (main_self.dLoad && (main_self.itemPull[this.id]["loaded"]=="no")) {
				if (main_self.menuModeTopLevelTimeout && main_self.menuMode == "web" && !main_self.context) {
					this._mouseOver = true;
					this._dynLoadTM = new Date().getTime();
				}
				var xmlLoader = new dtmlXMLLoaderObject(main_self["_"+(main_self._data_type||'xml')+"Parser"], main_self);
				main_self.itemPull[this.id]["loaded"] = "get";
				main_self.callEvent("onXLS", []);
				if (main_self.dLoadUrl.indexOf('E_model_name') < 0) {
					xmlLoader.loadXML(main_self.dLoadUrl+main_self.dLoadSign+"E_model_name=menu&id="+this.id.replace(main_self.idPrefix,"")+"&Q_EQ_"+(main_self._parentIdName||'parentId')+"="+this.id.replace(main_self.idPrefix,"")+"&etc="+new Date().getTime());
				} else {
					xmlLoader.loadXML(main_self.dLoadUrl+main_self.dLoadSign+"id="+this.id.replace(main_self.idPrefix,"")+"&Q_EQ_"+(main_self._parentIdName||'parentId')+"="+this.id.replace(main_self.idPrefix,"")+"&etc="+new Date().getTime());
				}
			}
			if ((!main_self.dLoad) || (main_self.dLoad && (!main_self.itemPull[this.id]["loaded"] || main_self.itemPull[this.id]["loaded"]=="yes"))) {
				if ((main_self.itemPull[this.id]["complex"]) && (main_self.menuSelected != -1)) {
					if (main_self.menuModeTopLevelTimeout && main_self.menuMode == "web" && !main_self.context) {
						this._mouseOver = true;
						var showItemId = this.id;
						this._menuOpenTM = window.setTimeout(function(){main_self._showPolygon(showItemId, main_self.dirTopLevel);}, main_self.menuModeTopLevelTimeoutTime);
					} else {
						main_self._showPolygon(this.id, main_self.dirTopLevel);
					}
				}
			}
		}
		main_self._doOnTouchMenu(this.id.replace(main_self.idPrefix, ""));
	};
	m.onmouseout = function() {
		if (!((main_self.itemPull[this.id]["complex"]) && (main_self.menuSelected != -1)) && (main_self.itemPull[this.id]["state"]=="enabled")) {
			// custom css
			// console.log(main_self.itemPull[this.id])
			if (main_self.itemPull[this.id]["cssNormal"] != null) {
				m.className = main_self.itemPull[this.id]["cssNormal"];
			} else {
				// default css
				m.className = "dhtmlxMenu_"+main_self.skin+"_TopLevel_Item_Normal";
			}
		}
		if (main_self.menuMode == "web") {
			window.clearTimeout(main_self.menuTimeoutHandler);
			main_self.menuTimeoutHandler = window.setTimeout(function(){main_self._clearAndHide();}, main_self.menuTimeoutMsec, "JavaScript");
		}
		if (main_self.menuModeTopLevelTimeout && main_self.menuMode == "web" && !main_self.context) {
			this._mouseOver = false;
			window.clearTimeout(this._menuOpenTM);
		}
	};
	m.onclick = function(e) {
		if (main_self.menuMode == "web") { window.clearTimeout(main_self.menuTimeoutHandler); }
		// fix, added in 0.4
		if (main_self.menuMode != "web" && main_self.itemPull[this.id]["state"] == "disabled") { return; }
		//
		e = e || event;
		e.cancelBubble = true;
		e.returnValue = false;
		
		if (main_self.menuMode == "win") {
			if (main_self.itemPull[this.id]["complex"]) {
				if (main_self.menuSelected == this.id) { main_self.menuSelected = -1; var s = false; } else { main_self.menuSelected = this.id; var s = true; }
				if (s) { main_self._showPolygon(this.id, main_self.dirTopLevel); } else { main_self._hidePolygon(this.id); }
			}
		}
		var tc = (main_self.itemPull[this.id]["complex"]?"c":"-");
		var td = (main_self.itemPull[this.id]["state"]!="enabled"?"d":"-");
		var cas = {"ctrl": e.ctrlKey, "alt": e.altKey, "shift": e.shiftKey};
		main_self._doOnClick(this.id.replace(main_self.idPrefix, ""), tc+td+"t", cas);
		return false;
	};
	
	if (this.skin == "dhx_terrace") {
		this._improveTerraceSkin();
	}
};
dhtmlXMenuObject.prototype._new_redistribSubLevelSelection = function(id, parentId) {
	// clear previosly selected items
	while (this._openedPolygons.length > 0) this._openedPolygons.pop();
	// this._openedPolygons = new Array();
	var i = this._getSubItemToDeselectByPolygon(parentId);
	this._removeSubItemFromSelected(-1, -1);
	for (var q=0; q<i.length; q++) { if ((this.idPull[i[q]] != null) && (i[q] != id)) { if (this.itemPull[i[q]]["state"] == "enabled") { this.idPull[i[q]].className = "sub_item"; } } }
	// hide polygons
	for (var q=0; q<this._openedPolygons.length; q++) { if (this._openedPolygons[q] != parentId) { this._hidePolygon(this._openedPolygons[q]); } }
	// add new selection into list new
	if (this.itemPull[id]["state"] == "enabled") {
		this.idPull[id].className = "sub_item_selected";
		if (this.itemPull[id]["complex"] && this.dLoad && (this.itemPull[id]["loaded"]=="no")) {
			if (this.loaderIcon == true) { this._updateLoaderIcon(id, true); }
			var xmlLoader = new dtmlXMLLoaderObject(this["_"+(this._data_type||'xml')+"Parser"], this);
			this.itemPull[id]["loaded"] = "get";
			this.callEvent("onXLS", []);
			if (this.dLoadUrl.indexOf('E_model_name') < 0) {
				xmlLoader.loadXML(this.dLoadUrl+this.dLoadSign+"E_model_name=menu&id="+id.replace(this.idPrefix,"")+"&Q_EQ_"+(this._parentIdName||'parentId')+"="+id.replace(this.idPrefix,"")+"&etc="+new Date().getTime());
			} else {
				xmlLoader.loadXML(this.dLoadUrl+this.dLoadSign+"id="+id.replace(this.idPrefix,"")+"&Q_EQ_"+(this._parentIdName||'parentId')+"="+id.replace(this.idPrefix,"")+"&etc="+new Date().getTime());
			}
		}
		// show
		if (this.itemPull[id]["complex"] || (this.dLoad && (this.itemPull[id]["loaded"] == "yes"))) {
			// make arrow over
			if ((this.itemPull[id]["complex"]) && (this.idPull["polygon_" + id] != null))  {
				this._updateItemComplexState(id, true, true);
				this._showPolygon(id, this.dirSubLevel);
			}
		}
		this._addSubItemToSelected(id, parentId);
		this.menuSelected = id;
	}
};
dhtmlXMenuObject.prototype._renderSublevelItem = function(id, pos) {
	var main_self = this;

	if (this._redistribSubLevelSelection != this._new_redistribSubLevelSelection) {
		this._redistribSubLevelSelection = this._new_redistribSubLevelSelection;
	}
	var tr = document.createElement("TR");
	tr.className = (this.itemPull[id]["state"]=="enabled"?"sub_item":"sub_item_dis");
	
	// icon
	var t1 = document.createElement("TD");
	t1.className = "sub_item_icon";
	var icon = this.itemPull[id][(this.itemPull[id]["state"]=="enabled"?"imgen":"imgdis")];
	if (icon != "") {
		var tp = this.itemPull[id]["type"];
		if (tp=="checkbox"||tp=="radio") {
			var img = document.createElement("DIV");
			img.id = "image_"+this.itemPull[id]["id"];
			img.className = "sub_icon "+icon;
			t1.appendChild(img);
		}
		if (!(tp=="checkbox"||tp=="radio")) {
			var img = document.createElement("IMG");
			img.id = "image_"+this.itemPull[id]["id"];
			img.className = "sub_icon";
			img.src = this.imagePath+icon;
			t1.appendChild(img);
		}
	}
	
	// text
	var t2 = document.createElement("TD");
	t2.className = "sub_item_text";
	if (this.itemPull[id]["title"] != "") {
		var t2t = document.createElement("DIV");
		t2t.className = "sub_item_text";
		t2t.innerHTML = this.itemPull[id]["title"];
		t2.appendChild(t2t);
	} else {
		t2.innerHTML = "&nbsp;";
	}
	
	// hotkey/sublevel arrow
	var t3 = document.createElement("TD");
	t3.className = "sub_item_hk";
	if (this.itemPull[id]["complex"]) {
		
		var arw = document.createElement("DIV");
		arw.className = "complex_arrow";
		arw.id = "arrow_"+this.itemPull[id]["id"];
		t3.appendChild(arw);
		
	} else {
		if (this.itemPull[id]["hotkey"].length > 0 && !this.itemPull[id]["complex"]) {
			var t3t = document.createElement("DIV");
			t3t.className = "sub_item_hk";
			t3t.innerHTML = this.itemPull[id]["hotkey"];
			t3.appendChild(t3t);
		} else {
			t3.innerHTML = "&nbsp;";
		}
	}
	tr.appendChild(this._rtl?t3:t1);
	tr.appendChild(t2);
	tr.appendChild(this._rtl?t1:t3);
	
	
	//
	tr.id = this.itemPull[id]["id"];
	tr.parent = this.itemPull[id]["parent"];
	// tooltip, added in 0.4
	if (this.itemPull[id]["tip"].length > 0) tr.title = this.itemPull[id]["tip"];
	//
	if (!this._hideTMData) this._hideTMData = {};
	
	tr.onselectstart = function(e) { e = e || event; e.returnValue = false; return false; };
	tr.onmouseover = function(e) {
		if (main_self.menuMode == "web") {
			if (main_self._hideTMData[this.id]) window.clearTimeout(main_self._hideTMData[this.id]);
			window.clearTimeout(main_self.menuTimeoutHandler);
		}
		if (!this._visible) main_self._redistribSubLevelSelection(this.id, this.parent); // if not visible
		this._visible = true;
	};
	if (main_self.menuMode == "web") {
		tr.onmouseout = function() {
			if (main_self.menuTimeoutHandler) window.clearTimeout(main_self.menuTimeoutHandler);
			main_self.menuTimeoutHandler = window.setTimeout(function(){main_self._clearAndHide();}, main_self.menuTimeoutMsec, "JavaScript");
			var k = this;
			if (main_self._hideTMData[this.id]) window.clearTimeout(main_self._hideTMData[this.id]);
			main_self._hideTMData[this.id] = window.setTimeout(function(){k._visible=false;}, 50);
		};
	}
	tr.onclick = function(e) {
		// added in 0.4, preven complex closing if user event not defined
		if (!main_self.checkEvent("onClick") && main_self.itemPull[this.id]["complex"]) return;
		//
		e = e || event; e.cancelBubble = true;
		e.returnValue = false;

		tc = (main_self.itemPull[this.id]["complex"]?"c":"-");
		td = (main_self.itemPull[this.id]["state"]=="enabled"?"-":"d");
		var cas = {"ctrl": e.ctrlKey, "alt": e.altKey, "shift": e.shiftKey};
		switch (main_self.itemPull[this.id]["type"]) {
			case "checkbox":
				main_self._checkboxOnClickHandler(this.id.replace(main_self.idPrefix, ""), tc+td+"n", cas);
				break;
			case "radio":
				main_self._radioOnClickHandler(this.id.replace(main_self.idPrefix, ""), tc+td+"n", cas);
				break;
			case "item":
				main_self._doOnClick(this.id.replace(main_self.idPrefix, ""), tc+td+"n", cas);
				break;
		}
		return false;
	};
	// add
	var polygon = this.idPull["polygon_"+this.itemPull[id]["parent"]];
	if (pos != null) { pos++; if (pos < 0) pos = 0; if (pos > polygon.tbd.childNodes.length - 1) pos = null; }
	if (pos != null && polygon.tbd.childNodes[pos] != null) polygon.tbd.insertBefore(tr, polygon.tbd.childNodes[pos]); else polygon.tbd.appendChild(tr);
	this.idPull[tr.id] = tr;
};

dhtmlXMenuObject.prototype._jsonParser = function(main_self) {
	if (main_self.dLoad) {
		var t = main_self._parser_json_data(this.xmlDoc.responseText);
		if (!t) {
			return;
		}
		parentId = (t.getAttribute("parentId")!=null?t.getAttribute("parentId"):null);
		if (parentId == null) {
			// main_self.idPrefix = main_self._genStr(12);
			main_self._buildMenu(t, null);
			main_self._initTopLevelMenu();
		} else {
			main_self._buildMenu(t, main_self.idPrefix+parentId);
			main_self._addSubMenuPolygon(main_self.idPrefix+parentId, main_self.idPrefix+parentId);//, main_self.idPull[main_self.idPrefix+parentId]);
			if (main_self.menuSelected == main_self.idPrefix+parentId) {
				var pId = main_self.idPrefix+parentId;
				var isTop = main_self.itemPull[main_self.idPrefix+parentId]["parent"]==main_self.idPrefix+main_self.topId;
				var level = ((isTop&&(!main_self.context))?main_self.dirTopLevel:main_self.dirSubLevel);
				var isShow = false;
				if (isTop && main_self.menuModeTopLevelTimeout && main_self.menuMode == "web" && !main_self.context) {
					var item = main_self.idPull[main_self.idPrefix+parentId];
					if (item._mouseOver == true) {
						var delay = main_self.menuModeTopLevelTimeoutTime - (new Date().getTime()-item._dynLoadTM);
						if (delay > 1) {
							item._menuOpenTM = window.setTimeout(function(){ main_self._showPolygon(pId, level); }, delay);
							isShow = true;
						}
					}
				}
				if (!isShow) { main_self._showPolygon(pId, level); }
			}
			main_self.itemPull[main_self.idPrefix+parentId]["loaded"] = "yes";
			// console.log(main_self.loaderIcon)
			if (main_self.loaderIcon == true) { main_self._updateLoaderIcon(main_self.idPrefix+parentId, false); }
		}
		this.destructor();
		main_self.callEvent("onXLE",[]);
	} else {
		var t = main_self._parser_json_data(this.xmlDoc.responseText);
		if (!t) {
			return;
		}
		// main_self.idPrefix = main_self._genStr(12);
		main_self._buildMenu(t, null);
		main_self.init();
		main_self.callEvent("onXLE",[]);
		main_self._doOnLoad();
	}
};
dhtmlXMenuObject.prototype._parser_json_data = function (jsonStr) {
	try {
		eval("var menu_json_data="+jsonStr+";");
	} catch (e) {
		return null;
	}
	menu_json_data.getAttribute = function (name) {
		return menu_json_data[name];
	};

	if (menu_json_data.item != null) {
		menu_json_data.childNodes = menu_json_data.item;

		for (var i=0; i<menu_json_data.item.length; i++) {
			var item = menu_json_data.item[i];
			item.tagName = this.itemTagName;
			item.getAttribute = function (name) {
				return this[name];
			};

			item.childNodes = [];

			if (item.userdata) {
				for (var j=0; j < item.userdata.length; j++) {
					item.childNodes[item.childNodes.length] = new node('userdata',item.userdata[j].name,item.userdata[j].content);
				}
			}
			if (item.tooltip) {
				item.childNodes[item.childNodes.length] = new node('tooltip','tip',item.tooltip);
			}
			if (item.hotkey) {
				item.childNodes[item.childNodes.length] = new node('hotkey','hotkey',item.hotkey);
			}
			/*
			if (item.href) {
				var hrefNode = new node('href','href_link',item.href);

				if (item.target) {
					hrefNode.target = item.target;
				}

				item.childNodes[item.childNodes.length] = hrefNode;
			}
			*/
			if(item["complex"] == false) {
				item["complex"] = null;
			}
		}
	} else {
		menu_json_data.childNodes = {length:0};
	}
	

	function node(tag,name,value) {
		return {tagName:tag,name:name,firstChild:{nodeValue:value},getAttribute:function(name){return this[name];}};
	};

	return menu_json_data;
};