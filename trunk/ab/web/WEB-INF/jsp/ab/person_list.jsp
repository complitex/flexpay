<%@ include file="/WEB-INF/jsp/common/taglibs.jsp" %>

<s:actionerror/>

<table cellpadding="3" cellspacing="1" border="0" width="100%">
	<form id="fobjects" method="post" action="">

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
					<a href="<s:url value="/dicts/view_person.action?person.id=%{id}"/>">
						<img src="<s:url value="/resources/common/img/i_edit.gif" />" alt="<s:text name="common.set" />"
						title="<s:text name="common.set" />" />
					</a></td>
			</tr>
		</s:iterator>
		<tr>
			<td colspan="7">
				<%@ include file="filters/pager.jsp" %>
				<input type="submit" class="btn-exit"
					   onclick="$('fobjects').action='<s:url action="delete_persons"/>';$('fobjects').submit()"
					   value="<s:text name="common.delete_selected"/>"/>
				<input type="button" class="btn-exit"
					   onclick="window.location='<s:url action="create_person"/>'"
					   value="<s:text name="common.new"/>"/>
			</td>
		</tr>

	</form>
</table>
