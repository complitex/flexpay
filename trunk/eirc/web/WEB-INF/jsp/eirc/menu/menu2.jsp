<%@ include file="/WEB-INF/jsp/common/taglibs.jsp" %>

<table cellpadding="0" cellspacing="0" border="0" width="100%" height="30">
	<tr class="secondtop">
		<td class="second-padding"><a href="<s:url action='organisations_list' namespace="/eirc" includeParams="none" />" class="menu2"><s:text name="eirc.menu2.diсtionaries"/></a></td>
		<td><img src="<s:url value="/resources/common/img/separator1.gif" includeParams="none"/>" hspace="12" alt=""/></td>
		<td class="second-padding"><a href="<c:url value='/eirc/main.action' />" class="menu2"><s:text name="eirc.menu2.import"/></a></td>
		<td><img src="<s:url value="/resources/common/img/separator1.gif" includeParams="none"/>" hspace="12" alt=""/></td>
		<td class="second-padding"><a href="<c:url value='/eirc/data.action' />" class="menu2"><s:text name="eirc.menu2.data"/></a></td>
		<td><img src="<s:url value="/resources/common/img/separator1.gif" includeParams="none"/>" hspace="12" alt=""/></td>
		<td class="second-padding"><a href="<c:url value='/eirc/processing.action' />" class="menu2"><s:text name="eirc.menu2.processing"/></a></td>
		<td><img src="<s:url value="/resources/common/img/separator1.gif" includeParams="none"/>" hspace="12" alt=""/></td>
		<td class="second-padding"><a href="<c:url value='/eirc/tickets.action' />" class="menu2"><s:text name="eirc.menu2.tickets"/></a></td>
		<td width="100%">&nbsp;</td>
	</tr>
</table>