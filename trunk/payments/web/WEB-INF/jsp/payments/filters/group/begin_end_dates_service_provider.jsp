<%@include file="/WEB-INF/jsp/common/taglibs.jsp"%>

<table width="100%">
	<tr>
		<td class="filter"><s:text name="eirc.service.begin_date" /></td>
		<td><%@include file="/WEB-INF/jsp/common/filter/begin_date_filter.jsp"%></td>
		<td class="filter"><s:text name="eirc.service.end_date" /></td>
		<td><%@include file="/WEB-INF/jsp/common/filter/end_date_filter.jsp"%></td>
		<td class="filter"><s:text name="eirc.service_provider" /></td>
		<td><%@include file="../service_provider_filter.jsp"%></td>
		<td>
            <input type="submit" value="<s:text name="eirc.filter" />" class="btn-exit" />
        </td>
	</tr>
</table>
