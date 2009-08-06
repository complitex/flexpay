<%@include file="/WEB-INF/jsp/common/taglibs.jsp" %>

<s:actionerror/>

<table cellpadding="3" cellspacing="1" border="0" width="100%">
	<s:form action="streetDistrictEdit" method="post">
		<s:hidden name="street.id" value="%{street.id}"/>
		<tr>
			<td class="th" width="1%">&nbsp;</td>
			<td class="th" width="99%"><s:text name="ab.street"/>:&nbsp;
				<s:property value="%{getTranslation(street.currentType.translations).shortName}"/>&nbsp;
				<s:property value="%{getTranslation(street.currentName.translations).name}"/></td>
		</tr>

		<tr>
			<td class="th">&nbsp;</td>
			<td class="th"><s:text name="ab.district"/></td>
		</tr>
		<s:iterator value="%{districtNames}" status="status">
			<tr class="cols_1">
				<td class="col_1s"><input type="checkbox" name="objectIds" value="<s:property value="%{object.id}" />" <s:if test="%{doesDistrictContainsStreet(object.id)}">checked</s:if> /></td>
				<td class="col"><s:property value="%{getTranslation(translations).name}"/></td>
			</tr>
		</s:iterator>

		<tr>
			<td colspan="2"><input type="submit" class="btn-exit" name="submitted"
								   value="<s:text name="common.save"/>"/>
			</td>
		</tr>
	</s:form>
</table>
