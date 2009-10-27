<%@include file="/WEB-INF/jsp/common/taglibs.jsp"%>

<%@include file="/WEB-INF/jsp/common/includes/errors_messages.jsp"%>

<s:form action="measureUnitEdit" namespace="/dicts" method="POST">

	<s:hidden name="measureUnit.id" />

	<table cellpadding="3" cellspacing="1" border="0" width="100%">
        <tr valign="top" class="cols_1">
            <td class="col"><s:text name="common.measure_unit" />:</td>
            <td class="col">
                <s:iterator value="names">
                    <s:set name="l" value="%{getLang(key)}" />
                    <s:textfield name="names[%{key}]" value="%{value}" />(<s:if test="#l.default">*</s:if><s:property value="getLangName(#l)" />)<br>
                </s:iterator>
            </td>
        </tr>
		<tr valign="middle">
			<td colspan="2">
                <s:submit name="submitted" cssClass="btn-exit" value="%{getText('common.save')}" />
            </td>
		</tr>
	</table>

</s:form>
