<%@include file="/WEB-INF/jsp/common/taglibs.jsp" %>

<s:form method="post" id="fregistries">
	<%@include file="../filters/sender_organisation_filter.jsp" %>
	&nbsp;&nbsp;
	<%@include file="../filters/recipient_organisation_filter.jsp" %>
	&nbsp;&nbsp;
	<%@include file="../filters/registry_type_filter.jsp" %>
	&nbsp;&nbsp;

	<br/>

	<span class="text">
	<s:text name="eirc.generated"/>&nbsp;
	<%@include file="../filters/date_interval_filter.jsp" %>
	</span>

	<input type="submit" value="<s:text name="eirc.filter" />" class="btn-exit"/>

	<table cellpadding="3" cellspacing="1" border="0" width="100%">
		<tr>
			<td class="th" width="1%">&nbsp;</td>
			<td class="th"><input type="checkbox" onchange="FP.setCheckboxes(this.checked, 'objectIds')"></td>
			<td class="th"><s:text name="eirc.date"/></td>
			<td class="th"><s:text name="eirc.sender"/></td>
			<td class="th"><s:text name="eirc.recipient"/></td>
			<td class="th"><s:text name="eirc.registry_type"/></td>
			<td class="th"><s:text name="eirc.load_date"/></td>
			<td class="th"><s:text name="eirc.records_number"/></td>
			<td class="th"><s:text name="eirc.status"/></td>
			<td class="th">&nbsp;</td>
		</tr>
		<s:iterator value="registries" status="status">
			<tr valign="middle" class="cols_1">
				<td class="col" width="1%"><s:property
						value="%{#status.index + pager.thisPageFirstElementNumber + 1}"/></td>
				<td class="col" width="1%"><input type="checkbox" name="objectIds" value="<s:property value="%{id}"/>"/>
				</td>
				<td class="col"><s:date name="creationDate" format="yyyy/MM/dd"/></td>
				<td class="col"><s:property value="getTranslation(sender.names).name"/></td>
				<td class="col"><s:property value="getTranslation(recipient.names).name"/></td>
				<td class="col"><s:text name="%{registryType.i18nName}"/></td>
				<td class="col"><s:date name="spFile.importDate" format="yyyy/MM/dd HH:mm:ss"/></td>
				<td class="col"><s:property value="recordsNumber"/></td>
				<td class="col"><s:text name="%{registryStatus.i18nName}"/></td>
				<td class="col"><a href="<s:url value="/eirc/registry_view.action?registry.id=%{id}"/>">
					<img src="<s:url value="/resources/common/img/i_view.gif" />" alt="<s:text name="common.view"/>"
						 title="<s:text name="common.view"/>"/></a></td>
			</tr>
		</s:iterator>
		<tr>
			<td colspan="10">
				<%@include file="/WEB-INF/jsp/ab/filters/pager.jsp" %>
				<input type="submit" value="<s:text name="eirc.process" />" class="btn-exit"
					   onclick="$('fregistries').action='<s:url action="process_registries"/>';"/>
			</td>
		</tr>
	</table>
</s:form>
