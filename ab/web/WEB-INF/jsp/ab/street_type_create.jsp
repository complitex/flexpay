<%@ include file="/WEB-INF/jsp/common/taglibs.jsp" %>


<s:form>
<table cellpadding="3" cellspacing="1" border="0" width="100%">
		<tr>
			<td class="th">&nbsp;</td>
			<td class="th"><spring:message code="ab.language"/></td>
			<td class="th"><spring:message code="ab.town_type_name"/></td>
		</tr>
		<s:iterator value="translationList" status="rowstatus">
		  <tr>
		    <td>
		      <s:property value="#rowstatus.index + 1" />
		    </td>
		    <td>
		      <s:property value="getLangName(lang)" />
		      <s:if test="lang.default == true">
		        <font color="red">*</font>
		      </s:if>
		    </td>
		    <td>
		      <s:textfield name="translationList[%{#rowstatus.index}].name" />
		      <s:fielderror>
                <s:param value="translationList[%{#rowstatus.index}].name" />
              </s:fielderror>
		    </td>
		  </tr>
		</s:iterator>
		<tr>
		  <td>
		    <s:submit name="submit" />
		  </td>
		</tr>
</table>
</s:form>