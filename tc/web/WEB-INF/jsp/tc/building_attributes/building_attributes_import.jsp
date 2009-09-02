<%@include file="/WEB-INF/jsp/common/taglibs.jsp" %>

<s:actionerror />
<s:actionmessage />

<s:form enctype="multipart/form-data">
	<table cellpadding="3" cellspacing="1" border="0" width="100%">
		<tr>
			<td><s:text name="bti.building.attribute.import.date" /></td>
			<td>
				<%@include file="/WEB-INF/jsp/common/filter/begin_date_filter.jsp" %>
			</td>
		<tr>
		<tr>
			<td><s:text name="bti.building.attribute.import.file" /></td>
			<td>
				<s:file name="upload" required="true" size="75" accept="*.csv" />
				<s:hidden name="moduleName" value="tc" />
			</td>
		</tr>
		<tr>
			<td colspan="2">
                <s:submit name="submitted" value="%{getText('common.import')}" cssClass="btn-exit" />
			</td>
		</tr>
	</table>
</s:form>
