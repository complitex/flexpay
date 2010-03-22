<%@include file="/WEB-INF/jsp/common/taglibs.jsp" %>
<%@include file="/WEB-INF/jsp/common/includes/errors_messages.jsp"%>

<s:form action="subdivisionEdit">

	<s:hidden name="organization.id" />
	<s:hidden name="subdivision.id" />

	<table cellpadding="3" cellspacing="1" border="0" width="100%">
        <tr valign="top" class="cols_1">
            <td class="col" width="35%"><s:text name="eirc.subdivision.name" />:</td>
            <td class="col" width="65%">
                <s:iterator value="names"><s:set name="l" value="%{getLang(key)}" />
                    <s:textfield name="names[%{key}]" value="%{value}" />(<s:if test="%{#l.default}">*</s:if><s:property value="%{getLangName(#l)}" />)<br />
                </s:iterator>
            </td>
        </tr>
        <tr valign="middle" class="cols_1">
            <td class="col"><s:text name="eirc.subdivision.real_address" />:</td>
            <td class="col"><s:textfield name="subdivision.realAddress" /></td>
        </tr>
		<tr valign="middle" class="cols_1">
			<td class="col"><s:text name="eirc.subdivision.juridical_person" />:</td>
			<td class="col"><%@include file="../filters/organization_filter.jsp"%></td>
		</tr>
		<tr valign="middle" class="cols_1">
			<td class="col"><s:text name="eirc.subdivision.parent_subdivision" />:</td>
			<td class="col"><%@include file="../filters/subdivision_filter.jsp"%></td>
		</tr>
		<tr valign="top" class="cols_1">
			<td class="col"><s:text name="eirc.subdivision.description" />:</td>
			<td class="col">
				<s:iterator value="descriptions"><s:set name="l" value="%{getLang(key)}" />
					<s:textarea name="descriptions[%{key}]" value="%{value}" rows="3" cols="30" cssClass="form-textarea-no-size" />
					(<s:if test="%{#l.default}">*</s:if><s:property value="%{getLangName(#l)}" />)<br />
				</s:iterator>
			</td>
		</tr>
		<tr valign="middle">
			<td colspan="2">
                <input type="submit" class="btn-exit" name="submitted" value="<s:text name="common.save" />" />
            </td>
		</tr>
	</table>

</s:form>
