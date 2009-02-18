<%@ include file="/WEB-INF/jsp/common/taglibs.jsp" %>
<%@ taglib prefix="menu" uri="http://struts-menu.sf.net/tag" %>

<ul class="folders">
	<li class="open"><strong><s:text name="eirc.menu2.processing"/></strong>
		<ul class="docs">
			<li><a href="<s:url action='processesList' namespace="/eirc" includeParams="none" />"><s:text name="eirc.menu34.processing.process_list"/></a></li>
		</ul>
		
		<ul class="docs">
			<li><a href="<s:url action='processDefinitionDeploy' namespace="/eirc" includeParams="none" />"><s:text name="eirc.menu34.processing.definition"/></a></li>
		</ul>
	</li>
</ul>

<%--
<ul class="folders">
    <menu:useMenuDisplayer name="Simple" config="MenuStrings" bundle="struts.custom.i18n.resources">
        <menu:displayMenu name="L3Processing" />
    </menu:useMenuDisplayer>
</ul>
--%>
