<%@ include file="/WEB-INF/jsp/common/taglibs.jsp" %>

<ul class="folders">
	<li class="open"><b><s:text name="eirc.organisations"/></b>
		<ul class="docs">
			<li><a href="<s:url action='organisations_list' includeParams="none" namespace="/eirc" />"><s:text name="eirc.organisations"/></a></li>
			<li><a href="<s:url action='service_providers_list' includeParams="none" namespace="/eirc" />"><s:text name="eirc.service_providers"/></a></li>
		</ul>
	</li>
	<li class="open"><b><s:text name="eirc.services"/></b>
		<ul class="docs">
			<li><a href="<s:url action='service_types_list' includeParams="none" namespace="/eirc" />"><s:text name="eirc.service_types"/></a></li>
			<li><a href="<s:url action='services_list' includeParams="none" namespace="/eirc" />"><s:text name="eirc.services"/></a></li>
		</ul>
	</li>
</ul>
