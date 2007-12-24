<%@ include file="/WEB-INF/jsp/common/taglibs.jsp" %>

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
		        (default)
		      </s:if>
		    </td>
		    <td class="col">
		      <s:property value="name" />
		    </td>
		  </tr>
		</s:iterator>
		<tr>
			<td colspan="3" height="3" bgcolor="#4a4f4f"/>
		</td>
		<tr>
		  <td colspan="3">
		    <a href="<s:url action='identity_type_edit'><s:param name="id" value="%{identityType.id}"/></s:url>">
	          <s:text name="ab.edit" />
	        </a>
		  </td>
		</tr>
</table>