<%@ include file="/WEB-INF/jsp/common/taglibs.jsp" %>

<s:actionerror />

<table cellpadding="3" cellspacing="1" border="0" width="100%">

	<s:form action="quittanceGenerate" method="POST">

		<tr valign="middle" class="cols_1">
			<td class="col">
				<%@include file="../filters/service_organisation_filter.jsp" %>
			</td>
		</tr>

		<tr>
			<td class="col">
				<s:text name="ab.from" />
				<%@include file="../../common/filter/begin_date_filter.jsp" %>
			</td>
		</tr>

		<tr>
			<td class="col">
				<s:text name="ab.till" />
				<%@include file="../../common/filter/end_date_filter.jsp" %>
			</td>
		</tr>

		<tr>
			<td colspan="2" align="center">
				<s:submit name="submitted" value="%{getText('common.upload')}" cssClass="btn-exit" />
			</td>
		</tr>

	</s:form>

</table>
