<%@include file="/WEB-INF/jsp/common/taglibs.jsp" %>

<s:actionerror />

<s:form id="fObjects" method="post">
	<table cellpadding="3" cellspacing="1" border="0" width="100%">

		<tr>
			<td>
				<s:text name="bti.building.attribute.import.date" />
			</td>
			<td>
				<%@include file="/WEB-INF/jsp/common/filter/begin_date_filter.jsp" %>
			</td>
		<tr>
		<tr>
			<td>
				<s:text name="bti.building.attribute.import.file" />
			</td>
			<td>
				<s:file name="upload" required="true" size="75" accept="*.csv" />
			</td>
		</tr>
		<tr>
			<td colspan="2">
				<input type="submit" class="btn-exit" name="submitted" value="<s:text name="common.import"/>">
			</td>
		</tr>
	</table>
</s:form>
