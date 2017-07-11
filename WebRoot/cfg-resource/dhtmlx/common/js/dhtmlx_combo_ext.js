/*
dhtmlXCombo扩展，覆盖以下两个方法(_fillFromXML和_fetchOptions)，
实现enableFilteringMode(mode,url,cache,autosubload)方法对json的支持.
使用了此覆盖以后，实现enableFilteringMode将不再支持xml
*/
dhtmlXCombo.prototype._fillFromXML = function(obj,b,c,d,xml){
//if (obj._xmlCache) obj._xmlCache[xml._cPath]=xml;
	
	var opt_data = [];
	var opts = [];
	eval("opts="+xml.xmlDoc.responseText);
	for (var i=0; i<opts.data.length; i++) {
		opt_data[i] = {text:opts.data[i][obj._text_attrName||'text'], value:opts.data[i][obj._value_attrName||'value']};
	};					
	obj.clearAll();
	obj.addOption(opt_data);
	if (obj._activeMode){
		obj._positList();
		obj.DOMlist.style.display="block";
		//if (_isIE) obj._IEFix(true);
	}
	opts = null;
	opt_data = null;
	obj._load=false;
};

dhtmlXCombo.prototype._fetchOptions=function(ind,text){
	if (text=="") { this.closeAll();  return this.clearAll();   }
	if (!this._text_attrName) {
		dhtmlx.message('必须为参数：_text_attrName设置相应值！');
		return;
	}
	if (!this._value_attrName) {
		dhtmlx.message('必须为参数：_value_attrName设置相应值！');
		return;
	}
	var url=this._xml+((this._xml.indexOf("?")!=-1)?"&":"?")+"pos="+ind+"&Q_LIKE_"+this._text_attrName+"="+encodeURIComponent(text)+"&E_model_name=combo&F_in="+this._text_attrName+","+this._value_attrName;
	
	this._lasttext=text;
	if (this._load) this._load=url;
	else {
		if (!this.callEvent("onDynXLS",[text,ind])) return;
		this.loadXML(url);
	}
};

dhtmlXCombo.prototype.loadJson = function(obj,jsonUrl,text_attrName,value_attrName){	
	if (!this._text_attrName&&!text_attrName) {
		dhtmlx.message('必须为参数：_text_attrName设置相应值！');
		return;
	 }
	 if (!this._value_attrName&&!value_attrName) {
		dhtmlx.message('必须为参数：_value_attrName设置相应值！');
		return;
	 }
	 url = jsonUrl + "?E_model_name=combo&F_in="+[this._text_attrName||text_attrName]+","+[this._value_attrName||value_attrName]; 
	dhtmlxAjax.get(url, function(loader) {
		var opt_data = [];
		var opts = [];
		eval("opts="+loader.xmlDoc.responseText);						
		for (var i=0; i<opts.data.length; i++) {
			opt_data[i] = {text:opts.data[i][this._text_attrName||text_attrName], value:opts.data[i][this._value_attrName||value_attrName]};
		};	
		obj.addOption(opt_data);
		opts = null;
		opt_data = null;
	});	
};

/**
 * @desc: create self HTML
 * @type: private
 * @topic: 0
 */
dhtmlXCombo.prototype._createSelf = function(selParent, name, width, tab) {
	if (width.toString().indexOf("%") != -1) {
		var self = this;
		var resWidht = parseInt(width) / 100;
		window.setInterval(function() {
			if (!selParent.parentNode)
				return;
			var ts = selParent.parentNode.offsetWidth * resWidht - 2;
			if (ts < 0)
				return;
			if (ts == self._lastTs)
				return;
			self.setSize(self._lastTs = ts);
		}, 500);
		var width = parseInt(selParent.offsetWidth); // mm
	}
	var width = parseInt(width || 100); // mm
	this.ListPosition = "Bottom"; // set optionlist positioning
	this.DOMParent = selParent;
	this._inID = null;
	this.name = name;

	this._selOption = null; // selected option object pointer
	this.optionsArr = Array();

	var opt = new this._optionObject();
	opt.DrawHeader(this, name, width, tab);
	// HTML select part 2 - options list DIV element
	this.DOMlist = document.createElement("DIV");
	this.DOMlist.className = 'dhx_combo_list '
			+ (dhtmlx.skin ? dhtmlx.skin + "_list" : "");
	this.DOMlist.style.width = width - (_isIE ? 0 : 0) + "px";
	if (_isOpera || _isKHTML)
		this.DOMlist.style.overflow = "auto";
	this.DOMlist.style.display = "none";
	document.body.insertBefore(this.DOMlist, document.body.firstChild);
	if (_isIE) {
		this.DOMlistF = document.createElement("IFRAME");
		this.DOMlistF.style.border = "0px";
		this.DOMlistF.className = 'dhx_combo_list';
		this.DOMlistF.style.width = width - (_isIE ? 0 : 0) + "px";
		this.DOMlistF.style.display = "none";
		this.DOMlistF.src = "javascript:false;";
		document.body.insertBefore(this.DOMlistF, document.body.firstChild);
	}

	this.DOMlist.combo = this.DOMelem.combo = this;
	if (_isIE) {
		//this.DOMelem_input.onpropertychange = this._onKey;
		this.DOMelem_input.onkeydown = this._onKey;
	} else {
		this.DOMelem_input.oninput = this._onKey;
	}
	//this.DOMelem_input.onkeydown = this._onKey;
	this.DOMelem_input.onkeypress = this._onKeyF;
	this.DOMelem_input.onblur = this._onBlur;
	this.DOMelem.onclick = this._toggleSelect;
	this.DOMlist.onclick = this._selectOption;
	this.DOMlist.onmousedown = function() {
		this._skipBlur = true;
	};

	this.DOMlist.onkeydown = function(e) {
		// this.combo.DOMelem_input.focus();
		(e || event).cancelBubble = true;
		this.combo.DOMelem_input.onkeydown(e);
	};
	this.DOMlist.onmouseover = this._listOver;
	//默認開啟過濾
	this.enableFilteringMode("between");
	//默认开启自动渲染位置
	this.enableOptionAutoPositioning(true);
};

/**
 * 下拉框过滤，可以输入拼音首字母、全拼或者名称
 */
dhtmlXCombo.prototype.filterSelf = function(mode) {
	var text = ((undefined == mode) ? "" : this.getComboText());
	if (this._xml) {
		this._lkmode = mode;
		this._fetchOptions(0, text);
	}
	var escapeExp = new RegExp("([" + this.filterEntities.join("\\") + "])", "g");
	text = text.replace(escapeExp, "\\$1");
	var filterExp = (this._anyPosition ? "" : "^") + text;
	var filter = new RegExp(filterExp, "i");
	this.filterAny = false;
	for ( var i = 0; i < this.optionsArr.length; i++) {
		var optionText = this.optionsArr[i].content ? this.optionsArr[i].data()[1] : this.optionsArr[i].text;
		var z = (filter.test(PinYin4Js.getFirstChar(optionText))) || filter.test(optionText) || filter.test(PinYin4Js.toPinyin(optionText));
		this.filterAny |= z;
		this.optionsArr[i].hide(!z);
	}
	if (!this.filterAny) {
		this.closeAll();
		this._activeMode = true;
	} else {
		if (this.DOMlist.style.display != "block")
			this.openSelect();
		if (_isIE)
			this._IEFix(true);
	}
};
/*
dhtmlXCombo.prototype._onBlur = function()
{
    var self = this.parentNode._self;
    window.setTimeout(function(){
      if (self.DOMlist._skipBlur) return !(self.DOMlist._skipBlur=false);
      self._skipFocus = true;
      self._confirmSelection();        
      self.callEvent("onBlur",[]);
      self.closeAll();
    },100)
    
}
*/