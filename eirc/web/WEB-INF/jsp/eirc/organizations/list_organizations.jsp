
<%@include file="/WEB-INF/jsp/common/taglibs.jsp" %>

<s:actionerror />
<s:form id="forganizations">

	<table cellpadding="3" cellspacing="1" border="0" width="100%">
		<tr>
			<td class="th" width="1%">&nbsp;</td>
			<td class="th" width="1%"><input type="checkbox" onchange="FP.setCheckboxes(this.checked, 'objectIds');"></td>
            <td class="th"><s:text name="eirc.organization.id"/></td>
            <td class="th"><s:text name="eirc.organization.name"/></td>
			<td class="th"><s:text name="eirc.organization.kpp"/></td>
			<td class="th"><s:text name="eirc.organization.inn"/></td>
			<td class="th">&nbsp;</td>
		</tr>
		<s:iterator value="organizations" status="status">
			<tr valign="middle" class="cols_1">
				<td class="col" width="1%"><s:property
						value="%{#status.index + pager.thisPageFirstElementNumber + 1}"/></td>
				<td class="col" width="1%"><input type="checkbox" name="objectIds" value="<s:property value="%{id}"/>"/>
				</td>
                <td class="col"><s:property value="id"/></td>
                <td class="col">
					<a href='<s:url action="subdivisionsList"><s:param name="organization.id" value="%{id}"/></s:url>'>
					<s:property value="getTranslation(names).name"/></a>
				</td>
				<td class="col"><s:property value="kpp"/></td>
				<td class="col"><s:property value="individualTaxNumber"/></td>
				<td class="col"><a href="<s:url action="organizationEdit"><s:param name="organization.id" value="%{id}"/></s:url>">
						 <s:text name="common.edit"/>
						 </a></td>
			</tr>
		</s:iterator>
		<tr>
			<td colspan="10">
				<%@include file="/WEB-INF/jsp/ab/filters/pager.jsp" %>
				<input type="submit" value="<s:text name="common.delete_selected" />" class="btn-exit"
					   onclick="$('#forganizations').attr('action', '<s:url action="organizationDelete" includeParams="none" />');"/>
				<input type="button" class="btn-exit"
					   onclick="window.location='<s:url action="organizationEdit"><s:param name="organization.id" value="0"/></s:url>';"
					   value="<s:text name="common.new"/>"/>
			</td>
		</tr>
	</table>
</s:form>
