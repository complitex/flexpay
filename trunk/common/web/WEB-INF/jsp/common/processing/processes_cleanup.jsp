<%@include file="/WEB-INF/jsp/common/taglibs.jsp"%>
<%@include file="/WEB-INF/jsp/common/includes/errors_messages.jsp"%>

<s:form method="POST">
	<table cellpadding="3" cellspacing="1" border="0" width="100%">
		<tr>
			<td nowrap="nowrap">
				<s:text name="common.process.complete.begin_date" />
				<%@include file="/WEB-INF/jsp/common/filter/begin_date_filter.jsp"%>
			</td>
		</tr>
		<tr>
			<td nowrap="nowrap">
				<s:text name="common.process.complete.end_date" />
				<%@include file="/WEB-INF/jsp/common/filter/end_date_filter.jsp"%>
			</td>
		</tr>
		<tr>
			<td nowrap="nowrap">
				<s:text name="common.processing.process.filter.name" />
				<%@include file="/WEB-INF/jsp/common/processing/filters/process_name_filter.jsp"%>
			</td>
		</tr>
		<tr>
			<td>
				<input type="submit" class="btn-exit" value="<s:text name="common.process.cleanup"/>"
					   name="submitted" />
			</td>
		</tr>
	</table>
</s:form>
