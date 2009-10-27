<%@include file="/WEB-INF/jsp/common/taglibs.jsp"%>

<%@include file="/WEB-INF/jsp/common/includes/errors_messages.jsp"%>

<s:form action="addressAttributeTypeEdit" method="POST">

	<s:hidden name="attributeType.id" />

	<table cellpadding="3" cellspacing="1" border="0" width="100%">
		<tr valign="top" class="cols_1">
			<td class="col" colspan="2"><s:text name="ab.buildings.attribute_type" /></td>
		</tr>
		<tr valign="top" class="cols_1">
			<td class="col"><s:text name="ab.buildings.attribute_type.name" /></td>
			<td class="col">
				<s:iterator value="names">
                    <s:set name="l" value="%{getLang(key)}" />
					<s:textfield name="names[%{key}]" value="%{value}" />(<s:if test="#l.default">*</s:if><s:property value="getLangName(#l)" />)<br>
				</s:iterator>
			</td>
		</tr>
		<tr valign="top" class="cols_1">
			<td class="col"><s:text name="ab.buildings.attribute_type.short_name" /></td>
			<td class="col">
				<s:iterator value="shortNames">
                    <s:set name="l" value="%{getLang(key)}" />
					<s:textfield name="shortNames[%{key}]" value="%{value}" />(<s:if test="#l.default">*</s:if><s:property value="getLangName(#l)" />)<br>
				</s:iterator>
			</td>
		</tr>
		<tr valign="middle">
			<td colspan="2">
                <s:submit cssClass="btn-exit" name="submitted" value="%{getText('common.save')}" />
            </td>
		</tr>
	</table>

</s:form>