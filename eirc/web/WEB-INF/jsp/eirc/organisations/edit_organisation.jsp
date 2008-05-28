<%@include file="/WEB-INF/jsp/common/taglibs.jsp" %>
<s:actionerror />
<s:form method="post">
	<s:hidden name="organisation.id" />
	<table cellpadding="3" cellspacing="1" border="0" width="100%">
        <tr valign="top" class="cols_1">
            <td class="col"><s:text name="eirc.organisation.name"/>:</td>
            <td class="col">
                <s:iterator value="names"><s:set name="l" value="%{getLang(key)}" />
                    <s:textfield name="names[%{key}]" value="%{value}"/>(<s:if test="%{#l.default}">*</s:if><s:property value="%{getLangName(#l)}" />)<br />
                </s:iterator>
            </td>
        </tr>
        <tr valign="middle" class="cols_1">
            <td class="col"><s:text name="eirc.organisation.id"/>:</td>
            <td class="col">
                <s:if test="organisation.id > 0"><s:hidden name="organisation.uniqueId" /><s:property value="organisation.uniqueId" /></s:if>
                <s:else><s:textfield name="organisation.uniqueId" /></s:else>
            </td>
        </tr>
		<tr valign="middle" class="cols_1">
			<td class="col"><s:text name="eirc.organisation.kpp"/>:</td>
			<td class="col"><s:textfield name="organisation.kpp" /></td>
		</tr>
		<tr valign="middle" class="cols_1">
			<td class="col"><s:text name="eirc.organisation.inn"/>:</td>
			<td class="col"><s:textfield name="organisation.individualTaxNumber" /></td>
		</tr>
		<tr valign="top" class="cols_1">
			<td class="col"><s:text name="eirc.organisation.comment"/>:</td>
			<td class="col">
				<s:iterator value="descriptions"><s:set name="l" value="%{getLang(key)}" />
					<s:textfield name="descriptions[%{key}]" value="%{value}"/>(<s:if test="%{#l.default}">*</s:if><s:property value="%{getLangName(#l)}" />)<br />
				</s:iterator>
			</td>
		</tr>
		<tr valign="middle" class="cols_1">
			<td colspan="2"><input type="submit" class="btn-exit" value="<s:text name="common.save"/>"/></td>
		</tr>
	</table>
</s:form>
