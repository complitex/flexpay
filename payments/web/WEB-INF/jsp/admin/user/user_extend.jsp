<%@include file="/WEB-INF/jsp/common/taglibs.jsp"%>
<input type="button" class="btn-exit"
									 onclick="window.location='<s:url action="userPaymentParametersEdit" includeParams="none"><s:param name="preference.username" value="%{currentUserPreferences.username}" /></s:url>'"
									 value="<s:text name="admin.payment.parameters.edit" />" />