<%@include file="/WEB-INF/jsp/common/taglibs.jsp" %>

<s:form method="post" id="forganisations">

	<table cellpadding="3" cellspacing="1" border="0" width="100%">
		<tr>
			<td class="th" width="1%">&nbsp;</td>
			<td class="th"><input type="checkbox" onchange="FP.setCheckboxes(this.checked, 'objectIds')"></td>
            <td class="th"><s:text name="eirc.organisation.id"/></td>
            <td class="th"><s:text name="eirc.organisation.name"/></td>
			<%--<td class="th"><s:text name="eirc.organisation.description"/></td>--%>
			<td class="th"><s:text name="eirc.organisation.kpp"/></td>
			<td class="th"><s:text name="eirc.organisation.inn"/></td>
			<td class="th">&nbsp;</td>
		</tr>
		<s:iterator value="organisations" status="status">
			<tr valign="middle" class="cols_1">
				<td class="col" width="1%"><s:property
						value="%{#status.index + pager.thisPageFirstElementNumber + 1}"/></td>
				<td class="col" width="1%"><input type="checkbox" name="objectIds" value="<s:property value="%{id}"/>"/>
				</td>
                <td class="col"><s:property value="id"/></td>
                <td class="col"><s:property value="getTranslation(names).name"/></td>
				<%--<td class="col"><s:property value="getTranslation(descriptions).name"/></td>--%>
				<td class="col"><s:property value="kpp"/></td>
				<td class="col"><s:property value="individualTaxNumber"/></td>
				<td class="col"><a href="<s:url value="/eirc/organisation_edit.action?organisation.id=%{id}"/>">
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
					   onclick="$('forganisations').action='<s:url action="organisations_delete"/>';"/>
				<input type="button" class="btn-exit"
					   onclick="window.location='<s:url action="organisation_edit"><s:param name="organisation.id" value="0"/></s:url>'"
					   value="<s:text name="common.new"/>"/>
			</td>
		</tr>
	</table>
</s:form>
