<%@ include file="/WEB-INF/jsp/common/taglibs.jsp" %>

<s:actionerror/>

<table cellpadding="3" cellspacing="1" border="0" width="100%">

	<form id="fobjects" method="post" action="<s:url action="list_persons" includeParams="none" />">

		<tr>
			<td colspan="7">
				<s:textfield name="searchString" value="%{searchString}"/>
				<input type="submit" class="btn-exit" value="<s:text name="menu1.search"/>"/>
			</td>
		</tr>
		<tr>
			<td colspan="7">&nbsp;</td>
		</tr>


		<tr>
			<td class="th" width="1%">&nbsp;</td>
			<td class="th" width="1%"><input type="checkbox"
											 onchange="FP.setCheckboxes(this.checked, 'objectIds')">
			</td>
			<td class="th" width="20%"><s:text name="ab.person.last_name"/></td>
			<td class="th" width="20%"><s:text name="ab.person.first_name"/></td>
			<td class="th" width="20%"><s:text name="ab.person.middle_name"/></td>
			<td class="th" width="20%"><s:text name="ab.person.birth_date"/></td>
			<td class="th" width="18%">&nbsp;</td>
		</tr>
		<s:iterator value="%{persons}" status="status">
			<tr valign="middle" class="cols_1">
				<td class="col_1s" align="right"><s:property
						value="%{#status.index + pager.thisPageFirstElementNumber + 1}"/>&nbsp;</td>
				<td class="col"><input type="checkbox" name="objectIds"
									   value="<s:property value="%{object.id}"/>"/></td>
				<td class="col"><s:property value="%{defaultIdentity.lastName}"/></td>
				<td class="col"><s:property value="%{defaultIdentity.firstName}"/></td>
				<td class="col"><s:property value="%{defaultIdentity.middleName}"/></td>
				<td class="col"><s:property
						value="%{format(defaultIdentity.birthDate)}"/></td>
				<td class="col">
					<a href="<s:url action="view_person"><s:param name="person.id" value="%{id}"/></s:url>">
						<s:text name="common.view"/></a></td>
			</tr>
		</s:iterator>
		<tr>
			<td colspan="7">
				<%@ include file="filters/pager.jsp" %>
				<input type="button" class="btn-exit"
				<%-- onclick="$('fobjects').action='<s:url action="delete_persons"/>';$('fobjects').submit()" --%>
					   onclick="alert('<s:text name="error.not_implemented" />')"
					   value="<s:text name="common.delete_selected"/>"/>
				<input type="button" class="btn-exit"
					   onclick="window.location='<s:url action="edit_person"><s:param name="person.id" value="0"/></s:url>'"
					   value="<s:text name="common.new"/>"/>
			</td>
		</tr>

	</form>
</table>
