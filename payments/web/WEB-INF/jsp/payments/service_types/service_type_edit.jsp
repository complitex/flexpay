<%@include file="/WEB-INF/jsp/common/taglibs.jsp"%>
<%@include file="/WEB-INF/jsp/common/includes/errors_messages.jsp"%>

<s:form action="serviceTypeEdit" method="POST">

	<s:hidden name="serviceType.id" />

	<table cellpadding="3" cellspacing="1" border="0" width="100%">
		<tr valign="top" class="cols_1">
			<td class="col"><s:text name="payments.service_type.name" />:</td>
			<td class="col">
				<s:iterator value="names">
                    <s:set name="l" value="%{getLang(key)}" />
					<s:textfield name="names[%{key}]" value="%{value}" />(<s:if test="#l.default">*</s:if><s:property value="getLangName(#l)" />)<br />
				</s:iterator>
			</td>
		</tr>
		<tr valign="top" class="cols_1">
			<td class="col"><s:text name="payments.service_type.description" />:</td>
			<td class="col">
				<s:iterator value="descriptions">
                    <s:set name="l" value="%{getLang(key)}" />
					<s:textfield name="descriptions[%{key}]" value="%{value}" />(<s:if test="#l.default">*</s:if><s:property value="getLangName(#l)" />)<br />
				</s:iterator>
			</td>
		</tr>
		<tr valign="top" class="cols_1">
			<td class="col"><s:text name="payments.service_type.code" />:</td>
			<td class="col">
				<s:textfield name="serviceType.code" />
			</td>
		</tr>
		<tr valign="middle" class="cols_1">
			<td colspan="2">
                <input type="submit" name="submitted" class="btn-exit" value="<s:text name="common.save" />" />
            </td>
		</tr>
	</table>

</s:form>
