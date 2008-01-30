<%@ include file="/WEB-INF/jsp/common/taglibs.jsp" %>

<s:actionerror />

<table cellpadding="3" cellspacing="1" border="0" width="100%">
	<s:form action="edit_town_type" namespace="/dicts" method="post">
		<input type="hidden" name="townTypeId" value="<s:property value="%{townTypeId}"/>"/>
		<tr>
			<td class="th">&nbsp;</td>
			<td class="th"><s:text name="ab.language"/></td>
			<td class="th"><s:text name="ab.town_type"/></td>
		</tr>
		<s:iterator value="%{townTypeTranslations}" status="rowStatus">
			<tr valign="middle" class="cols_1">
				<td class="col_1s"><s:property value="%{$rowStatus.index + 1}"/></td>
				<td class="col"><s:property value="%{getLangName(lang)}"/></td>
				<td class="col"><input type="text" name="name_<s:property value="%{lang.id}" />"
						   value="<s:property value="%{name}" />"/></td>
			</tr>
		</s:iterator>
		<tr>
			<td colspan="3" height="3" bgcolor="#4a4f4f"/>
		<tr>
			<td colspan="3">
				<input type="submit" class="btn-exit"
					   value="<s:text name="common.save"/>"/>
			</td>
		</tr>
	</s:form>
</table>
