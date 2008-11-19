<%@ include file="/WEB-INF/jsp/common/taglibs.jsp" %>

<ul class="folders">
	<li class="open"><b><s:text name="eirc.menu2.processing"/></b>
		<ul class="docs">
			<li><a href="<s:url action='processesList' namespace="/eirc" includeParams="none" />"><s:text name="eirc.menu34.processing.process_list"/></a></li>
		</ul>
		
		<ul class="docs">
			<li><a href="<s:url action='processDefinitionDeploy' namespace="/eirc" includeParams="none" />"><s:text name="eirc.menu34.processing.definition"/></a></li>
		</ul>

	</li>

</ul>
