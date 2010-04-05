<%@include file="/WEB-INF/jsp/common/taglibs.jsp"%>

<div id="response">
    <%@include file="/WEB-INF/jsp/common/includes/errors_messages.jsp"%>
</div>

<s:form action="createUser" method="POST">
	<s:set name="readonly" value="%{false}" />
	<%@include file="user_form_data.jsp"%>
</s:form>