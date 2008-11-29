<%@ include file="/WEB-INF/jsp/common/taglibs.jsp" %>

<ul class="folders">
	<li class="open">
        <b><s:text name="eirc.organizations"/></b>
		<ul class="docs">
			<li>
                <a href="<s:url action='organizationsList' includeParams="none" namespace="/eirc" />">
                    <s:text name="eirc.organizations"/>
                </a>
            </li>
			<li>
                <a href="<s:url action='banksList' includeParams="none" namespace="/eirc" />">
                    <s:text name="eirc.banks"/>
                </a>
            </li>
            <li>
                <a href="<s:url action='serviceOrganizationsList' includeParams="none" namespace="/eirc" />">
                    <s:text name="eirc.service_organizations"/>
                </a>
            </li>
			<li>
                <a href="<s:url action='serviceProvidersList' includeParams="none" namespace="/eirc" />">
                    <s:text name="eirc.service_providers"/>
                </a>
            </li>
			<li>
                <a href="<s:url action='eircAccountsList' includeParams="none" namespace="/eirc" />">
                    <s:text name="eirc.eirc_account.list"/>
                </a>
            </li>
		</ul>
	</li>
	<li class="open">
        <b><s:text name="eirc.services"/></b>
		<ul class="docs">
			<li>
                <a href="<s:url action='serviceTypesList' includeParams="none" namespace="/eirc" />">
                    <s:text name="eirc.service_types"/>
                </a>
            </li>
			<li>
                <a href="<s:url action='servicesList' includeParams="none" namespace="/eirc" />">
                    <s:text name="eirc.services"/>
                </a>
            </li>
		</ul>
	</li>
</ul>
