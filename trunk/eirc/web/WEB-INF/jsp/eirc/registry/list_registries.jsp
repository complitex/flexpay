<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/WEB-INF/jsp/common/taglibs.jsp" %>

<s:form method="post">
	<%@include file="../filters/sender_organisation_filter.jsp" %>&nbsp;&nbsp;
	<%@include file="../filters/recipient_organisation_filter.jsp" %>&nbsp;&nbsp;
	<%@include file="../filters/registry_type_filter.jsp" %>&nbsp;&nbsp;

	<br/>

	<s:text name="eirc.generated"/>&nbsp;
	<%@include file="../filters/date_interval_filter.jsp" %>
	<input type="submit" value="<s:text name="eirc.filter" />"/>

	<table cellpadding="3" cellspacing="1" border="0" width="100%">
		<tr>
			<td class="th">&nbsp;</td>
			<td class="th"><s:text name="eirc.date"/></td>
			<td class="th"><s:text name="eirc.sender"/></td>
			<td class="th"><s:text name="eirc.recipient"/></td>
			<td class="th"><s:text name="eirc.registry_type"/></td>
			<td class="th"><s:text name="eirc.load_date"/></td>
			<td class="th"><s:text name="eirc.records_number"/></td>
			<td class="th"><s:text name="eirc.status"/></td>
		</tr>
		<s:iterator value="registries">
			<tr valign="middle" class="cols_1">
				<td class="col_1s"><s:checkbox name="objectIds"/></td>
				<td class="col"><s:property value="creationDate"/></td>
				<td class="col"><s:property value="sender.name"/></td>
				<td class="col"><s:property value="recipient.name"/></td>
				<td class="col"><s:property value="registryType.name"/></td>
				<td class="col"><s:property value="spFile.importDate"/></td>
				<td class="col"><s:property value="recordsNumber"/></td>
				<td class="col">-----</td>
			</tr>
		</s:iterator>
		<tr>
			<td colspan="8">
				<input type="submit" value="<s:text name="eirc.process" />"
					onclick="alert('Not implemented yet'); return false;"	/>
				<%@include file="/WEB-INF/jsp/ab/filters/pager.jsp" %>
			</td>
		</tr>
	</table>
</s:form>
