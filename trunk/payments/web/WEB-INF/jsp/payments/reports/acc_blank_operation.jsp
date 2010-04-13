<%@include file="/WEB-INF/jsp/common/taglibs.jsp"%>
<%@include file="/WEB-INF/jsp/payments/includes/stylesheet.jsp"%>
<%@include file="/WEB-INF/jsp/common/includes/errors_messages.jsp"%>

<s:text name="payments.report.blank.operation.count" />: <s:property value="%{blankOperationsCount}" />

<s:form action="accBlankOperationReport">
	<s:submit cssClass="btn-exit" name="submitted" value="%{getText('common.delete_selected')}" />
</s:form>