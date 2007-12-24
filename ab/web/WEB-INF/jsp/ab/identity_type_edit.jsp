<%@ include file="/WEB-INF/jsp/common/taglibs.jsp" %>


<s:form>
<table cellpadding="3" cellspacing="1" border="0" width="100%">
		<tr>
			<td class="th">&nbsp;</td>
			<td class="th"><s:text name="ab.language"/></td>
			<td class="th"><s:text name="ab.identity_type"/></td>
		</tr>
		<s:iterator value="identityType.translations" status="rowstatus">
		  <tr valign="middle" class="cols_1">
		    <td class="col_1s">
		      <s:property value="#rowstatus.index + 1" />
		    </td>
		    <td class="col">
		      <s:property value="getLangName(lang)" />
		      <s:if test="lang.default == true">
		        (default)<font color="red">*</font>
		      </s:if>
		    </td>
		    <td class="col">
		      <s:textfield name="translationMap['%{id}'].name" value="%{name}" />
		      <s:fielderror>
                <s:param value="translationMap['%{id}'].name" />
              </s:fielderror>
		    </td>
		  </tr>
		</s:iterator>
		<tr>
			<td colspan="3" height="3" bgcolor="#4a4f4f"/>
		</td>	
		<tr>
		  <td colspan="3">
		    <s:hidden name="id" value="%{streetType.id}" />
		    <s:submit name="submit" value="%{getText('ab.update')}" cssClass="btn-exit" />
		  </td>
		</tr>
</table>
</s:form>