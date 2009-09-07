<%@ include file="/WEB-INF/jsp/common/taglibs.jsp" %>

<table border="0" cellpadding="0" cellspacing="0" width="100%">
	<tr class="verytop">
		<td>
            <a href="<s:url value="/" includeParams="none" />">
                <img src="<s:url value='/resources/common/img/logo.gif' includeParams="none"/>"
                     width="123" height="37" alt="FlexPay" border="0" hspace="25" vspace="6" />
            </a>
		</td>
		<sec:authorize ifAnyGranted="ROLE_BASIC">
			<td align="right">
    			<span class="text-small">
                    <s:text name="login.username" />: <sec:authentication property="principal.username" />
				    <a href="<s:url value="/logout" includeParams="none" />"><s:text name="logout.link.title" /></a>
                </span>
			</td>
		</sec:authorize>
	</tr>
</table>

<p>

<s:i18n name="/i18n/common-messages">

	<div style="text-align:center;">
		<h1><s:text name="forbidden_page_message" /></h1>
	</div>

</s:i18n>