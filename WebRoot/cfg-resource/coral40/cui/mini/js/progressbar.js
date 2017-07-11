/*!
 * 组件库4.0.1：进度条
 *
 * 依赖JS文件:
 *   jquery.coral.code.js
 *   jquery.coral.component.js
 */
(function(a,b){a.component("coral.progressbar",{version:"4.0.1",options:{id:null,name:null,max:100,value:0,text:"{value}%",onChange:null,onComplete:null},min:0,_create:function(){this.oldValue=this.options.value=this._constrainedValue();this.element.addClass("coral-progressbar coral-component coral-corner-all");this.valueDiv=a('<div class="coral-progressbar-value coral-corner-left"></div>').appendTo(this.element);this.textDiv=a('<div class="coral-progressbar-text"></div>').appendTo(this.valueDiv);this.valueWidth=this.element.width()-(this.valueDiv.outerWidth(true)-this.valueDiv.outerWidth());this._refreshValue()},_destroy:function(){this.element.removeClass("coral-progressbar coral-component coral-component-content coral-corner-all");this.valueDiv.remove()},value:function(c){if(c===b){return this.options.value}this.options.value=this._constrainedValue(c);this._refreshValue()},_constrainedValue:function(c){if(c===b){c=this.options.value}this.indeterminate=c===false;if(typeof c!=="number"){c=0}return(this.indeterminate?false:Math.min(this.options.max,Math.max(this.min,c)))},_setOptions:function(c){var d=c.value;delete c.value;this._super(c);this.options.value=this._constrainedValue(d);this._refreshValue()},_setOption:function(c,d){if(c==="max"){d=Math.max(this.min,d)}if(c==="disabled"){this.element.toggleClass("coral-state-disabled",!!d).attr("aria-disabled",d)}this._super(c,d)},_percentage:function(){return this.indeterminate?100:100*(this.options.value-this.min)/(this.options.max-this.min)},_refreshValue:function(){var d=this.options.value,c=this._percentage().toFixed(0),g=this.options.text.replace(/{value}/,c),f=0,e=0;this.valueDiv.toggle(this.indeterminate||d>this.min).toggleClass("coral-corner-right",d===this.options.max).width(this.valueWidth*c/100);if(100==c){this.valueDiv.width("auto")}this.textDiv.html(g);f=(this.element.position().left+(this.element.outerWidth()-this.textDiv.outerWidth())/2);
e=(this.element.position().top+(this.element.outerHeight()-this.textDiv.outerHeight())/2);this.textDiv.position({of:this.element,my:f+" "+e,at:f+" "+e});this.element.toggleClass("coral-progressbar-indeterminate",this.indeterminate);if(this.indeterminate){if(!this.overlayDiv){this.overlayDiv=a("<div class='coral-progressbar-overlay'></div>").appendTo(this.valueDiv)}}else{if(this.overlayDiv){this.overlayDiv.remove();this.overlayDiv=null}}if(this.oldValue!==d){this.oldValue=d;this._trigger("onChange",null,{value:d,oldValue:this.oldValue})}if(d===this.options.max){this._trigger("onComplete")}}})})(jQuery);