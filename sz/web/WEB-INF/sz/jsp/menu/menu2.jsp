<%@ page contentType="text/html;charset=UTF-8" pageEncoding="utf-8" language="java" %>
<%@ include file="/WEB-INF/jsp/common/taglibs.jsp" %>

<table cellpadding="0" cellspacing="0" border="0" width="100%" height="30">
	<tr class="secondtop">
		<td class="second-padding"><a href="<c:url value='/sz/main.action' />" class="menu2"><s:text name="sz.menu2.dictionaries"/></a></td>
		<td><img src="<c:url value="/resources/common/img/separator1.gif" />" hspace="12"/></td>
		<td class="second-padding"><a href="<c:url value='/sz/import.action' />" class="menu2"><s:text name="sz.menu2.import"/></a></td>
		<td><img src="<c:url value="/resources/common/img/separator1.gif" />" hspace="12"/></td>
		<td class="second-padding"><a href="<c:url value='/sz/processing.action' />" class="menu2"><s:text name="sz.menu2.processing"/></a></td>
		<td width="100%">&nbsp;</td>
	</tr>
</table>