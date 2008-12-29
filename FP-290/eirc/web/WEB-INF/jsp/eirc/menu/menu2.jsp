<%@ include file="/WEB-INF/jsp/common/taglibs.jsp" %>

<table cellpadding="0" cellspacing="0" border="0" width="100%" height="30">
	<tr class="secondtop">
		<td class="second-padding">
            <a href="<s:url action='organizationsList' namespace="/eirc" includeParams="none" />" class="menu2">
                <s:text name="eirc.menu2.dictionaries"/>
            </a>
        </td>
		<td>
            <img src="<s:url value="/resources/common/img/separator1.gif" includeParams="none"/>" hspace="12" alt=""/>
        </td>
		<td class="second-padding">
            <a href="<s:url action='main' namespace="/eirc" includeParams="none"/>" class="menu2">
                <s:text name="eirc.menu2.import"/>
            </a>
        </td>
		<td>
            <img src="<s:url value="/resources/common/img/separator1.gif" includeParams="none"/>" hspace="12" alt=""/>
        </td>
		<td class="second-padding">
            <a href="<s:url action='processing' namespace="/eirc" includeParams="none"/>" class="menu2">
                <s:text name="eirc.menu2.processing"/>
            </a>
        </td>
		<td>
            <img src="<s:url value="/resources/common/img/separator1.gif" includeParams="none"/>" hspace="12" alt=""/>
        </td>
		<td class="second-padding">
            <a href="<s:url action='quittances' namespace="/eirc" includeParams="none" />" class="menu2">
                <s:text name="eirc.menu2.tickets"/>
            </a>
        </td>
		<td width="100%">&nbsp;</td>
	</tr>
</table>