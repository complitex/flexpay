<%@include file="/WEB-INF/jsp/common/taglibs.jsp"%>

<div id="response">
    <%@include file="/WEB-INF/jsp/common/includes/errors_messages.jsp"%>
</div>

<table cellpadding="3" cellspacing="1" border="0" width="100%">
	<tr>
		<td class="th" width="1%">&nbsp;</td>
		<td class="th"><s:text name="common.language" /></td>
		<td class="th"><s:text name="common.measure_unit" /></td>
	</tr>
	<s:iterator value="measureUnit.unitNames" status="rowstatus">
		<tr valign="middle" class="cols_1">
			<td class="col_1s">
				<s:property value="#rowstatus.index + 1" />
			</td>
			<td class="col">
				<s:property value="getLangName(lang)" />
				<s:if test="lang.default">
                    (default)
				</s:if>
			</td>
			<td class="col">
				<s:property value="name" />
			</td>
		</tr>
	</s:iterator>
	<tr>
		<td colspan="3" height="3" bgcolor="#4a4f4f"></td>
	</tr>
	<tr>
		<td colspan="3">
            <input type="button" class="btn-exit"
                   onclick="window.location='<s:url action="measureUnitEdit"><s:param name="measureUnit.id" value="measureUnit.id" /></s:url>';"
                   value="<s:text name="common.edit" />" />
		</td>
	</tr>
</table>
