<%@include file="/WEB-INF/jsp/common/taglibs.jsp"%>

<table width="100%">
	<tr>
		<td class="filter"><s:text name="payments.service.begin_date" /></td>
		<td><%@include file="/WEB-INF/jsp/common/filter/begin_date_filter.jsp"%></td>
		<td class="filter"><s:text name="payments.service.end_date" /></td>
		<td><%@include file="/WEB-INF/jsp/common/filter/end_date_filter.jsp"%></td>
		<td class="filter"><s:text name="orgs.service_provider" /></td>
		<td><%@include file="/WEB-INF/jsp/payments/filters/service_provider_filter.jsp"%></td>
		<td>
            <input type="submit" value="<s:text name="payments.filter" />" class="btn-exit" />
        </td>
	</tr>
</table>
