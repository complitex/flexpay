<%@ include file="/WEB-INF/jsp/common/taglibs.jsp" %>

<table cellpadding="3" cellspacing="1" border="0" width="100%">
		<tr>
			<td class="th">&nbsp;</td>
			<td class="th"><spring:message code="ab.language"/></td>
			<td class="th"><spring:message code="ab.town_type_name"/></td>
		</tr>
		<s:iterator value="streetType.translations" status="rowstatus">
		  <tr>
		    <td>
		      <s:property value="#rowstatus.index + 1" />
		    </td>
		    <td>
		      <s:property value="getLangName(lang)" />
		      <s:if test="lang.default == true">
		        (default)
		      </s:if>
		    </td>
		    <td>
		      <s:property value="name" />
		    </td>
		  </tr>
		</s:iterator>
		<tr>
		  <td>
		    <a href="">edit<a/>
		  </td>
		</tr>
</table>