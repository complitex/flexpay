<%@ include file="/WEB-INF/jsp/common/taglibs.jsp" %>

<s:actionerror/>

<table cellpadding="3" cellspacing="1" border="0" width="100%">

	<form id="fobjects" method="post" action="<s:url value="/eirc/eircAccountCreateForm2Action.action" includeParams="none" />">
	<input type="hidden" name="apartmentId" value="<s:property value="apartmentId"/>" />

	<tr>
	  <td colspan="7">
	    <s:textfield name="searchString" value="%{searchString}"/>
	    <input type="submit" class="btn-exit" value="<s:text name="menu1.search"/>"/>
	  </td>
	</tr>
	<tr>
	  <td>
	    &nbsp;
	  </td>
	</tr>

	

		<tr>
			<td class="th" width="1%">&nbsp;</td>
			<td class="th" width="1%">&nbsp;</td>
			<td class="th" width="20%"><s:text name="ab.person.last_name"/></td>
			<td class="th" width="20%"><s:text name="ab.person.first_name"/></td>
			<td class="th" width="20%"><s:text name="ab.person.middle_name"/></td>
			<td class="th" width="20%"><s:text name="ab.person.birth_date"/></td>
		</tr>
		<s:iterator value="%{persons}" status="status">
			<tr valign="middle" class="cols_1">
				<td class="col_1s" align="right"><s:property
						value="%{#status.index + pager.thisPageFirstElementNumber + 1}"/>&nbsp;</td>
				<td class="col"><input type="radio" name="personId"
									   value="<s:property value="%{id}"/>"/></td>
				<td class="col"><s:property value="%{defaultIdentity.lastName}"/></td>
				<td class="col"><s:property value="%{defaultIdentity.firstName}"/></td>
				<td class="col"><s:property value="%{defaultIdentity.middleName}"/></td>
				<td class="col"><s:property value="%{format(defaultIdentity.birthDate)}"/></td>
				
			</tr>
		</s:iterator>
		<tr>
			<td colspan="6">
				<%@ include file="/WEB-INF/jsp/ab/filters/pager.jsp" %>
			</td>
		</tr>
		<tr>
			<td colspan="6">
				<input type="submit" class="btn-exit"
					   onclick="$('fobjects').action='<s:url action="eircAccountCreateAction" includeParams="none" />';$('fobjects').submit()"
					   value="<s:text name="common.create"/>"/>
			</td>
		</tr>

	</form>
</table>
