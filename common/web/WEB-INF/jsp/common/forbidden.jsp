<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/WEB-INF/jsp/common/taglibs.jsp" %>


<table border="0" cellpadding="0" cellspacing="0" width="100%">
	<tr class="verytop">
		<td><a href="<s:url value='/' />"><img src="<s:url value='/resources/common/img/logo.gif' includeParams="none"/>" width="123"
											   height="37" alt="FlexPay" border="0" hspace="25"
											   vspace="6" /></a>
		</td>
		<%
			if (request.getUserPrincipal() != null) {
		%>
		<td align="right">
			<span class="text-small"><s:text name="login.username" />: <%=request.getUserPrincipal()%> <a
					href="<s:url value="/resources/common/jsp/logout.jsp" includeParams="none" />"><s:text name="logout.link.title" /></a></span>
		</td>
		<%
			}
		%>
	</tr>
</table>

<p>

<s:i18n name="/i18n/common-messages">

	<center>
		<h1><s:text name="forbidden_page_message" /></h1>
	</center>

</s:i18n>