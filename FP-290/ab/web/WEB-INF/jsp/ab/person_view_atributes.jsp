<%@ include file="/WEB-INF/jsp/common/taglibs.jsp" %>

<table cellpadding="3" cellspacing="1" border="0" width="100%">
	<tr class="cols_1">
		<td class="col_1s" colspan="3"><b><s:text name="ab.person.attributes"/></b></td>
	</tr>
	<tr class="cols_1">
		<td class="th" width="1%">&nbsp;</td>
		<td class="th"><s:text name="ab.person.attribute.name"/></td>
		<td class="th"><s:text name="ab.person.attribute.value"/></td>
	</tr>
	<s:iterator value="person.personAttributes" status="rowstatus">
		<tr class="cols_1">
			<td class="col" align="right"><s:property
					value="%{#rowstatus.index + 1}"/>&nbsp;</td>
			<td class="col"><s:property value="%{name}"/></td>
			<td class="col"><s:property value="%{value}"/></td>
		</tr>
	</s:iterator>
</table>
