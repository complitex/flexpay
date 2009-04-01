
<%@ include file="/WEB-INF/jsp/common/taglibs.jsp" %>

<s:actionerror/>

<form id="fobjects" method="post"
	  action="<s:url action="registryRecordCorrectPerson" includeParams="none" />"
      onsubmit="return FP.validateSubmit('<s:text name="eirc.need_select_person" />');">

	<%@include file="../registry_record_info.jsp" %>
	<%@ include file="/WEB-INF/jsp/ab/filters/groups/person_search.jsp" %>

	<table cellpadding="3" cellspacing="1" border="0" width="100%">

		<tr>
			<td class="th" width="1%">&nbsp;</td>
			<td class="th" width="1%">&nbsp;</td>
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
				<td class="col">
					<input type="radio" value="<s:property value="%{id}"/>"
						   name="object.id"/>
				</td>
				<td class="col"><s:property value="%{defaultIdentity.lastName}"/></td>
				<td class="col"><s:property value="%{defaultIdentity.firstName}"/></td>
				<td class="col"><s:property value="%{defaultIdentity.middleName}"/></td>
				<td class="col"><s:property
						value="%{format(defaultIdentity.birthDate)}"/></td>
				<td class="col">
					<a target="_blank"
					   href="<s:url action="personView" namespace="/dicts" includeParams="none"><s:param name="person.id" value="%{id}"/></s:url>">
						<s:text name="common.view"/></a></td>
			</tr>
		</s:iterator>
		<tr>
			<td colspan="7">
				<%@ include file="/WEB-INF/jsp/common/filter/pager/pager.jsp" %>
				<input type="hidden" id="setupType" name="setupType" value="setupType"/>
				<s:hidden name="record.id" value="%{record.id}"/>
				<input type="submit" onclick="$('#setupType').val('person');"
					   class="btn-exit"
					   value="<s:text name="common.set"/>"/>
				<input type="button" value="<s:text name="common.close" />"
					   class="btn-exit"
					   onclick="parent.Windows.closeAll();"/>
			</td>
		</tr>

	</table>
</form>
