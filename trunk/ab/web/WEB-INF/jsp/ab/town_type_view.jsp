<%@ include file="/WEB-INF/jsp/common/taglibs.jsp" %>

<table cellpadding="3" cellspacing="1" border="0" width="100%">
		<tr>
			<td class="th" width="1%">&nbsp;</td>
			<td class="th"><s:text name="ab.language"/></td>
			<td class="th"><s:text name="ab.town_type"/></td>
			<td class="th"><s:text name="ab.short_name"/></td>
		</tr>
		<s:iterator value="townType.translations" status="rowstatus">
		  <tr valign="middle" class="cols_1">
		    <td class="col_1s">
		      <s:property value="#rowstatus.index + 1" />
		    </td>
		    <td class="col">
		      <s:property value="getLangName(lang)" />
		      <s:if test="lang.default == true">
		        (default)
		      </s:if>
		    </td>
		    <td class="col">
		      <s:property value="name" />
		    </td>
		    <td class="col">
		      <s:property value="shortName" />
		    </td>
		  </tr>
		</s:iterator>
		<tr>
			<td colspan="4" height="3" bgcolor="#4a4f4f"/>
		</tr>
		<tr>
		  <td colspan="4">
		    <a href="<s:url action='editTownType' includeParams="none"><s:param name="townType.id" value="%{townType.id}"/></s:url>">
	          <s:text name="ab.edit" />
	        </a>
		  </td>
		</tr>
</table>