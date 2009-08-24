<%@include file="/WEB-INF/jsp/common/taglibs.jsp" %>

<table width="100%">
	<col width="16%" align="right">
	<col width="16%" align="left">
	<col width="16%" align="right">
	<col width="16%" align="left">
	<col width="18%" align="right">
	<col width="18%" align="left">
	<tr>
		<td class="filter"><s:text name="ab.person.fio"/></td>
		<td colspan="5">
            <input type="text" name="personSearchFilter.searchString" class="form-textfield"
                         value="<s:property value="%{personSearchFilter.searchString}" />"/>
            <input type="button" onclick="this.form.submit();" value="<s:text name="common.search" />" />
        </td>
	</tr>
</table>