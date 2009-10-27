<%@include file="/WEB-INF/jsp/common/taglibs.jsp"%>

<table cellpadding="3" cellspacing="1" border="0" width="100%">

	<s:set name="fio" value="%{person.FIOIdentity}" />
	<tr class="cols_1">
		<td class="col_1s" colspan="4"><strong><s:text name="ab.person.fio" /></strong></td>
	</tr>
	<tr class="cols_1">
		<td class="col_1"><s:text name="ab.person.last_name" /></td>
		<td class="col_1"><s:property value="#fio.lastName" /></td>
		<td class="col_1"><s:text name="ab.person.first_name" /></td>
		<td class="col_1"><s:property value="#fio.firstName" /></td>
	</tr>
	<tr class="cols_1">
		<td class="col_1"><s:text name="ab.person.middle_name" /></td>
		<td class="col_1"><s:property value="#fio.middleName" /></td>
		<td class="col_1"><s:text name="ab.person.sex" /></td>
		<td class="col_1">
			<s:if test="#fio.isMan()">
                <s:text name="ab.person.sex.man" />
            </s:if><s:elseif test="#fio.isWoman()">
                <s:text name="ab.person.sex.woman" />
            </s:elseif>
		</td>
	</tr>
	<tr class="cols_1">
		<td class="col_1"><s:text name="ab.person.birth_date" /></td>
		<td class="col_1" colspan="3"><s:property value="format(#fio.birthDate)" /></td>
	</tr>
</table>
