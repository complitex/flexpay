<%@ include file="/WEB-INF/jsp/common/taglibs.jsp" %>

<ul class="folders">
	<li class="open"><b><s:text name="eirc.organisations"/></b>
		<ul class="docs">
			<li><a href="<s:url action='organisationsList' includeParams="none" namespace="/eirc" />"><s:text name="eirc.organisations"/></a></li>
			<li><a href="<s:url action='banksList' includeParams="none" namespace="/eirc" />"><s:text name="eirc.banks"/></a></li>
			<li><a href="<s:url action='serviceProvidersList' includeParams="none" namespace="/eirc" />"><s:text name="eirc.service_providers"/></a></li>
			<li><a href="<s:url action='eircAccountList' includeParams="none" namespace="/eirc" />"><s:text name="eirc.eirc_account.list"/></a></li>
		</ul>
	</li>
	<li class="open"><b><s:text name="eirc.services"/></b>
		<ul class="docs">
			<li><a href="<s:url action='serviceTypesList' includeParams="none" namespace="/eirc" />"><s:text name="eirc.service_types"/></a></li>
			<li><a href="<s:url action='servicesList' includeParams="none" namespace="/eirc" />"><s:text name="eirc.services"/></a></li>
		</ul>
	</li>
</ul>
