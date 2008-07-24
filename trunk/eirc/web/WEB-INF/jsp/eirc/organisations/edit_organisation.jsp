<%@include file="/WEB-INF/jsp/common/taglibs.jsp" %>

<a href="<s:url action="subdivisionsList"><s:param name="organisation.id" value="organisation.id"/></s:url>"><s:text name="eirc.subdivisions"/></a> 
<br />

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
                <s:if test="organisation.notNew"><s:property value="organisation.id" /></s:if>
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
			<td class="col"><s:text name="eirc.organisation.juridical_address"/>:</td>
			<td class="col"><s:textfield name="organisation.juridicalAddress" /></td>
		</tr>
		<tr valign="middle" class="cols_1">
			<td class="col"><s:text name="eirc.organisation.postal_address"/>:</td>
			<td class="col"><s:textfield name="organisation.postalAddress" /></td>
		</tr>
		<tr valign="middle">
			<td colspan="2"><input type="submit" class="btn-exit" name="submitted"
								   value="<s:text name="common.save"/>"/></td>
		</tr>
	</table>
</s:form>
