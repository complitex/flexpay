<%@ include file="/WEB-INF/jsp/common/taglibs.jsp" %>


<s:form>
<table cellpadding="3" cellspacing="1" border="0" width="100%">
		<tr>
			<td class="th">&nbsp;</td>
			<td class="th"><s:text name="common.language"/></td>
			<td class="th"><s:text name="ab.street_type_name"/></td>
		</tr>
		<s:iterator value="streetType.translations" status="rowstatus">
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
		      <s:textfield name="streetType.translations[%{#rowstatus.index}].name" value="%{name}" />
		      <s:fielderror>
                <s:param value="streetType.translations[%{#rowstatus.index}].name" />
              </s:fielderror>
		    </td>
		  </tr>
		</s:iterator>
		<tr>
			<td colspan="3" height="3" bgcolor="#4a4f4f"/>
		</td>	
		<tr>
		  <td>
		    <s:hidden name="id" value="%{streetType.id}" />
		    <s:submit name="submit" value="%{getText('ab.street_type.create.button')}" cssClass="btn-exit" />
		  </td>
		</tr>
</table>
</s:form>