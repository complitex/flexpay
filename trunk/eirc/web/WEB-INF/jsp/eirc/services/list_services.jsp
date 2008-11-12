<%@include file="/WEB-INF/jsp/common/taglibs.jsp" %>

<s:form method="post" id="fservices">

	<table cellpadding="3" cellspacing="1" border="0" width="100%">
		<tr class="cols_1">
			<td class="col" colspan="10">
				<%@include file="../filters/group/begin_end_dates_service_provider.jsp" %>
			</td>
		</tr>
		<tr>
			<td class="th" width="1%">&nbsp;</td>
			<td class="th" width="1%"><input type="checkbox" onchange="FP.setCheckboxes(this.checked, 'objectIds');"></td>
			<td class="th"><s:text name="eirc.service_type.name"/></td>
			<td class="th"><s:text name="eirc.service.description"/></td>
			<td class="th"><s:text name="eirc.service.service_provider"/></td>
			<td class="th"><s:text name="eirc.service_type.code"/></td>
			<td class="th"><s:text name="eirc.service.external_code"/></td>
			<td class="th"><s:text name="eirc.service.begin_date"/></td>
			<td class="th"><s:text name="eirc.service.end_date"/></td>
			<td class="th">&nbsp;</td>
		</tr>
		<s:iterator value="services" status="status">
			<tr valign="middle" class="cols_1">
				<td class="col" width="1%"><s:property
						value="%{#status.index + pager.thisPageFirstElementNumber + 1}"/></td>
				<td class="col" width="1%"><input type="checkbox" name="objectIds" value="<s:property value="%{id}"/>"/>
				</td>
				<td class="col" nowrap="nowrap">
					<s:if test="%{isSubService()}">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</s:if>
					<s:property value="getTranslation(serviceType.typeNames).name"/></td>
				<td class="col"><s:property value="getTranslation(descriptions).name"/></td>
				<td class="col"><s:property value="getTranslation(serviceProvider.organisation.names).name"/></td>
				<td class="col"><s:property value="serviceType.code"/></td>
				<td class="col"><s:property value="externalCode"/></td>
				<td class="col"><s:date name="beginDate" format="yyyy/MM/dd"/></td>
				<td class="col"><s:date name="endDate" format="yyyy/MM/dd"/></td>
				<td class="col"><a href="<s:url action="serviceEdit"><s:param name="service.id" value="%{id}"/></s:url>">
					<s:text name="common.edit"/></a></td>
			</tr>
		</s:iterator>
		<tr>
			<td colspan="10">
				<%@include file="/WEB-INF/jsp/ab/filters/pager.jsp" %>
				<input type="button" class="btn-exit"
					   onclick="window.location='<s:url action="serviceEdit"><s:param name="service.id" value="0"/></s:url>';"
					   value="<s:text name="common.new"/>"/>
			</td>
		</tr>
	</table>
</s:form>
