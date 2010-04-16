<%@include file="/WEB-INF/jsp/common/taglibs.jsp"%>
<%@include file="/WEB-INF/jsp/common/includes/errors_messages.jsp"%>

<s:form action="createUser" method="POST">
	<s:set name="readonly" value="%{false}" />
	<%@include file="user_form_data.jsp"%>
</s:form>
