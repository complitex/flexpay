
<%@ include file="/WEB-INF/jsp/common/taglibs.jsp" %>

<%@include file="/WEB-INF/jsp/common/jquery_ui.jsp"%>

<script type="text/javascript">
    FP.calendars("#date", "<s:url value="/resources/common/js/jquery/jquery-ui/images/calendar.gif" includeParams="none" />");
</script>

<s:actionerror/>

<table cellpadding="3" cellspacing="1" border="0" width="100%">
	<s:form action="editStreetType" method="post">

		<tr>
			<td class="th" width="100%" colspan="3" align="center">
				<s:text name="ab.from" />&nbsp;
				<s:textfield name="date" id="date" value="%{date}" readonly="true" />
				&nbsp;
				<%@include file="filters/street_type_filter.jsp"%>
			</td>
		</tr>

		<tr>
			<td colspan="3" height="3" bgcolor="#4a4f4f"/>
		<tr>
			<td colspan="3">
				<input type="submit" class="btn-exit" value="<s:text name="common.save"/>"/>
			</td>
		</tr>
		<s:hidden name="temporal.id" value="%{temporal.id}" />
		<s:hidden name="object.id" value="%{object.id}" />
	</s:form>
</table>
