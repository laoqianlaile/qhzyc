/*!
 * 组件库4.0：元素位置辅助工具
 */
(function(e,c){e.coral=e.coral||{};var j,m,k=Math.max,p=Math.abs,n=Math.round,d=/left|center|right/,h=/top|center|bottom/,a=/[\+\-]\d+(\.[\d]+)?%?/,l=/^\w+/,b=/%$/,g=e.fn.position;function o(s,r,q){return[parseFloat(s[0])*(b.test(s[0])?r/100:1),parseFloat(s[1])*(b.test(s[1])?q/100:1)]}function i(q,r){return parseInt(e.css(q,r),10)||0}function f(r){var q=r[0];if(q.nodeType===9){return{width:r.width(),height:r.height(),offset:{top:0,left:0}}}if(e.isWindow(q)){return{width:r.width(),height:r.height(),offset:{top:r.scrollTop(),left:r.scrollLeft()}}}if(q.preventDefault){return{width:0,height:0,offset:{top:q.pageY,left:q.pageX}}}return{width:r.outerWidth(),height:r.outerHeight(),offset:r.offset()}}e.position={scrollbarWidth:function(){if(j!==c){return j}var r,q,t=e("<div style='display:block;position:absolute;width:50px;height:50px;overflow:hidden;'><div style='height:100px;width:auto;'></div></div>"),s=t.children()[0];e("body").append(t);r=s.offsetWidth;t.css("overflow","scroll");q=s.offsetWidth;if(r===q){q=t[0].clientWidth}t.remove();return(j=r-q)},getScrollInfo:function(u){var t=u.isWindow||u.isDocument?"":u.element.css("overflow-x"),s=u.isWindow||u.isDocument?"":u.element.css("overflow-y"),r=t==="scroll"||(t==="auto"&&u.width<u.element[0].scrollWidth),q=s==="scroll"||(s==="auto"&&u.height<u.element[0].scrollHeight);return{width:q?e.position.scrollbarWidth():0,height:r?e.position.scrollbarWidth():0}},getWithinInfo:function(r){var s=e(r||window),q=e.isWindow(s[0]),t=!!s[0]&&s[0].nodeType===9;return{element:s,isWindow:q,isDocument:t,offset:s.offset()||{left:0,top:0},scrollLeft:s.scrollLeft(),scrollTop:s.scrollTop(),width:q||t?s.width():s.outerWidth(),height:q||t?s.height():s.outerHeight()}}};e.fn.position=function(A){if(!A||!A.of){return g.apply(this,arguments)}A=e.extend({},A);var B,x,v,z,u,q,w=e(A.of),t=e.position.getWithinInfo(A.within),r=e.position.getScrollInfo(t),y=(A.collision||"flip").split(" "),s={};q=f(w);if(w[0].preventDefault){A.at="left top"
}x=q.width;v=q.height;z=q.offset;u=e.extend({},z);e.each(["my","at"],function(){var E=(A[this]||"").split(" "),D,C;if(E.length===1){E=d.test(E[0])?E.concat(["center"]):h.test(E[0])?["center"].concat(E):["center","center"]}E[0]=d.test(E[0])?E[0]:"center";E[1]=h.test(E[1])?E[1]:"center";D=a.exec(E[0]);C=a.exec(E[1]);s[this]=[D?D[0]:0,C?C[0]:0];A[this]=[l.exec(E[0])[0],l.exec(E[1])[0]]});if(y.length===1){y[1]=y[0]}if(A.at[0]==="right"){u.left+=x}else{if(A.at[0]==="center"){u.left+=x/2}}if(A.at[1]==="bottom"){u.top+=v}else{if(A.at[1]==="center"){u.top+=v/2}}B=o(s.at,x,v);u.left+=B[0];u.top+=B[1];return this.each(function(){var D,M,F=e(this),H=F.outerWidth(),E=F.outerHeight(),G=i(this,"marginLeft"),C=i(this,"marginTop"),L=H+G+i(this,"marginRight")+r.width,K=E+C+i(this,"marginBottom")+r.height,I=e.extend({},u),J=o(s.my,F.outerWidth(),F.outerHeight());if(A.my[0]==="right"){I.left-=H}else{if(A.my[0]==="center"){I.left-=H/2}}if(A.my[1]==="bottom"){I.top-=E}else{if(A.my[1]==="center"){I.top-=E/2}}I.left+=J[0];I.top+=J[1];if(!m){I.left=n(I.left);I.top=n(I.top)}D={marginLeft:G,marginTop:C};e.each(["left","top"],function(O,N){if(e.coral.position[y[O]]){e.coral.position[y[O]][N](I,{targetWidth:x,targetHeight:v,elemWidth:H,elemHeight:E,collisionPosition:D,collisionWidth:L,collisionHeight:K,offset:[B[0]+J[0],B[1]+J[1]],my:A.my,at:A.at,within:t,elem:F})}});if(A.using){M=function(Q){var S=z.left-I.left,P=S+x-H,R=z.top-I.top,O=R+v-E,N={target:{element:w,left:z.left,top:z.top,width:x,height:v},element:{element:F,left:I.left,top:I.top,width:H,height:E},horizontal:P<0?"left":S>0?"right":"center",vertical:O<0?"top":R>0?"bottom":"middle"};if(x<H&&p(S+P)<x){N.horizontal="center"}if(v<E&&p(R+O)<v){N.vertical="middle"}if(k(p(S),p(P))>k(p(R),p(O))){N.important="horizontal"}else{N.important="vertical"}A.using.call(this,Q,N)}}F.offset(e.extend(I,{using:M}))})};e.coral.position={fit:{left:function(u,t){var s=t.within,w=s.isWindow?s.scrollLeft:s.offset.left,y=s.width,v=u.left-t.collisionPosition.marginLeft,x=w-v,r=v+t.collisionWidth-y-w,q;
if(t.collisionWidth>y){if(x>0&&r<=0){q=u.left+x+t.collisionWidth-y-w;u.left+=x-q}else{if(r>0&&x<=0){u.left=w}else{if(x>r){u.left=w+y-t.collisionWidth}else{u.left=w}}}}else{if(x>0){u.left+=x}else{if(r>0){u.left-=r}else{u.left=k(u.left-v,u.left)}}}},top:function(t,s){var r=s.within,x=r.isWindow?r.scrollTop:r.offset.top,y=s.within.height,v=t.top-s.collisionPosition.marginTop,w=x-v,u=v+s.collisionHeight-y-x,q;if(s.collisionHeight>y){if(w>0&&u<=0){q=t.top+w+s.collisionHeight-y-x;t.top+=w-q}else{if(u>0&&w<=0){t.top=x}else{if(w>u){t.top=x+y-s.collisionHeight}else{t.top=x}}}}else{if(w>0){t.top+=w}else{if(u>0){t.top-=u}else{t.top=k(t.top-v,t.top)}}}}},flip:{left:function(w,v){var u=v.within,A=u.offset.left+u.scrollLeft,D=u.width,s=u.isWindow?u.scrollLeft:u.offset.left,x=w.left-v.collisionPosition.marginLeft,B=x-s,r=x+v.collisionWidth-D-s,z=v.my[0]==="left"?-v.elemWidth:v.my[0]==="right"?v.elemWidth:0,C=v.at[0]==="left"?v.targetWidth:v.at[0]==="right"?-v.targetWidth:0,t=-2*v.offset[0],q,y;if(B<0){q=w.left+z+C+t+v.collisionWidth-D-A;if(q<0||q<p(B)){w.left+=z+C+t}}else{if(r>0){y=w.left-v.collisionPosition.marginLeft+z+C+t-s;if(y>0||p(y)<r){w.left+=z+C+t}}}},top:function(v,u){var t=u.within,C=t.offset.top+t.scrollTop,D=t.height,q=t.isWindow?t.scrollTop:t.offset.top,x=v.top-u.collisionPosition.marginTop,z=x-q,w=x+u.collisionHeight-D-q,A=u.my[1]==="top",y=A?-u.elemHeight:u.my[1]==="bottom"?u.elemHeight:0,E=u.at[1]==="top"?u.targetHeight:u.at[1]==="bottom"?-u.targetHeight:0,s=-2*u.offset[1],B,r;if(z<0){r=v.top+y+E+s+u.collisionHeight-D-C;if((v.top+y+E+s)>z&&(r<0||r<p(z))){v.top+=y+E+s}}else{if(w>0){B=v.top-u.collisionPosition.marginTop+y+E+s-q;if((v.top+y+E+s)>w&&(B>0||p(B)<w)){v.top+=y+E+s}}}}},flipfit:{left:function(){e.coral.position.flip.left.apply(this,arguments);e.coral.position.fit.left.apply(this,arguments)},top:function(){e.coral.position.flip.top.apply(this,arguments);e.coral.position.fit.top.apply(this,arguments)}}};(function(){var u,w,r,t,s,q=document.getElementsByTagName("body")[0],v=document.createElement("div");
u=document.createElement(q?"div":"body");r={visibility:"hidden",width:0,height:0,border:0,margin:0,background:"none"};if(q){e.extend(r,{position:"absolute",left:"-1000px",top:"-1000px"})}for(s in r){u.style[s]=r[s]}u.appendChild(v);w=q||document.documentElement;w.insertBefore(u,w.firstChild);v.style.cssText="position: absolute; left: 10.7432222px;";t=e(v).offset().left;m=t>10&&t<11;u.innerHTML="";w.removeChild(u)})()}(jQuery));