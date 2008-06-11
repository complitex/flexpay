<%@include file="/WEB-INF/jsp/common/taglibs.jsp" %>

<s:form method="post" id="ftypes">

	<table cellpadding="3" cellspacing="1" border="0" width="100%">
		<tr>
			<td class="th" width="1%">&nbsp;</td>
			<td class="th" width="1%"><input type="checkbox" onchange="FP.setCheckboxes(this.checked, 'objectIds')"></td>
			<td class="th"><s:text name="eirc.service_type.name"/></td>
			<td class="th"><s:text name="eirc.service_type.code"/></td>
			<td class="th">&nbsp;</td>
		</tr>
		<s:iterator value="serviceTypes" status="status">
			<tr valign="middle" class="cols_1">
				<td class="col" width="1%"><s:property
						value="%{#status.index + pager.thisPageFirstElementNumber + 1}"/></td>
				<td class="col" width="1%"><input type="checkbox" name="objectIds" value="<s:property value="%{id}"/>"/>
				</td>
				<td class="col"><s:property value="getTranslation(typeNames).name"/></td>
				<td class="col"><s:property value="code"/></td>
				<td class="col"><a href="<s:url action="service_type_edit"><s:param name="serviceType.id" value="%{id}"/></s:url>">
					<!-- <img src="<s:url value="/resources/common/img/i_edit.gif" />" alt="<s:text name="common.edit"/>"
						 title="<s:text name="common.edit"/>"/> -->
					<s:text name="common.edit"/>	 
						</a></td>
			</tr>
		</s:iterator>
		<tr>
			<td colspan="10">
				<%@include file="/WEB-INF/jsp/ab/filters/pager.jsp" %>
				<input type="submit" value="<s:text name="common.delete_selected" />" class="btn-exit"
					   onclick="$('ftypes').action='<s:url action="service_types_delete"/>';"/>
				<input type="button" class="btn-exit"
					   onclick="window.location='<s:url action="service_type_edit"><s:param name="serviceType.id" value="0"/></s:url>'"
					   value="<s:text name="common.new"/>"/>
			</td>
		</tr>
	</table>
</s:form>
