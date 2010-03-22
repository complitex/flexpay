<%@include file="/WEB-INF/jsp/common/taglibs.jsp"%>
<%@include file="/WEB-INF/jsp/common/includes/errors_messages.jsp"%>

<s:form action="organizationEdit">

	<s:hidden name="organization.id" />

	<table cellpadding="3" cellspacing="1" border="0" width="100%">
        <tr valign="top" class="cols_1">
            <td class="col"><s:text name="eirc.organization.name" />:</td>
            <td class="col">
                <s:iterator value="names"><s:set name="l" value="%{getLang(key)}" />
                    <s:textfield name="names[%{key}]" value="%{value}" />(<s:if test="%{#l.default}">*</s:if><s:property value="%{getLangName(#l)}" />)<br />
                </s:iterator>
            </td>
        </tr>
		<tr valign="middle" class="cols_1">
			<td class="col"><s:text name="eirc.organization.kpp" />:</td>
			<td class="col"><s:textfield name="organization.kpp" /></td>
		</tr>
		<tr valign="middle" class="cols_1">
			<td class="col"><s:text name="eirc.organization.inn" />:</td>
			<td class="col"><s:textfield name="organization.individualTaxNumber" /></td>
		</tr>
		<tr valign="top" class="cols_1">
			<td class="col"><s:text name="eirc.organization.comment" />:</td>
			<td class="col">
				<s:iterator value="descriptions"><s:set name="l" value="%{getLang(key)}" />
					<s:textfield name="descriptions[%{key}]" value="%{value}" />(<s:if test="%{#l.default}">*</s:if><s:property value="%{getLangName(#l)}" />)<br />
				</s:iterator>
			</td>
		</tr>
		<tr valign="middle" class="cols_1">
			<td class="col"><s:text name="eirc.organization.juridical_address" />:</td>
			<td class="col"><s:textfield name="organization.juridicalAddress" /></td>
		</tr>
		<tr valign="middle" class="cols_1">
			<td class="col"><s:text name="eirc.organization.postal_address" />:</td>
			<td class="col"><s:textfield name="organization.postalAddress" /></td>
		</tr>
		<tr valign="middle">
			<td colspan="2">
                <input type="submit" class="btn-exit" name="submitted" value="<s:text name="common.save" />" />
            </td>
		</tr>
	</table>

</s:form>
