<%@attribute name="id"%><%@attribute name="style"%><%@attribute name="cls"%><%@attribute name="componentCls" %><%@ attribute name="rendered" type="java.lang.Boolean" %><%@ attribute name="authorized" %><%@attribute name="active"%><%@attribute name="icons"%><%@attribute name="name"%><%@attribute name="disabled"%><%@attribute name="collapsible"%><%@attribute name="heightStyle"%><%@attribute name="onActivate"%><%@attribute name="beforeActivate"%><%@include file="TagUtil.jsp" %>
<% 
id = tagUtil.getClientId( id );
String dataOption = tagUtil.add("disabled", disabled)
.add("id", id)
.add("cls", cls)
.add("componentCls", componentCls)
.add("rendered", rendered).add("authorized", authorized)
.add("icons", icons)
.add("active", active)
.add("name", name)
.add("collapsible", collapsible)
.add("heightStyle", heightStyle)
.add("onActivate", onActivate)
.add("beforeActivate", beforeActivate).toString();
%><div id="<%=id %>" class="<%=cls==null?"":cls%>" style="<%=style==null?"":style%>" data-options="<%=dataOption %>" ><jsp:doBody /></div>
<script <%= "id=" + id + "_s" %>>
Coral.cc("accordion", "<%= id%>");
</script>