
<%@include file="/WEB-INF/jsp/common/taglibs.jsp" %>
<s:actionerror />
<s:form>
	<s:hidden name="provider.id" />
	<table cellpadding="3" cellspacing="1" border="0" width="100%">
        <tr valign="top" class="cols_1">
            <td class="col"><s:text name="eirc.organization"/>:</td>
            <td class="col">
                <%@include file="../filters/organization_filter.jsp" %>
            </td>
        </tr>
		<tr valign="top" class="cols_1">
			<td class="col"><s:text name="eirc.service_provider.description"/>:</td>
			<td class="col">
				<s:iterator value="descriptions"><s:set name="l" value="%{getLang(key)}" />
					<s:textfield name="descriptions[%{key}]" value="%{value}"/>(<s:if test="%{#l.default}">*</s:if><s:property value="%{getLangName(#l)}" />)<br />
				</s:iterator>
			</td>
		</tr>
		<tr valign="middle" class="cols_1">
			<td colspan="2"><input type="submit" name="submitted" class="btn-exit" value="<s:text name="common.save"/>"/></td>
		</tr>
	</table>
</s:form>
