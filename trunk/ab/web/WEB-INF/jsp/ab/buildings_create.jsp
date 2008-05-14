<%@ include file="/WEB-INF/jsp/common/taglibs.jsp" %>


<table>

<form id="fobjects" method="post" action="<s:url value="/dicts/buildingsCreateAction.action" includeParams="none" />">
		<tr>
			<td class="th" width="100%" colspan="4" align="center">
				<%@ include file="filters/country_filter.jsp" %>
			</td>
		</tr>
		<tr>
			<td class="th" width="100%" colspan="4" align="center">
				<%@ include file="filters/region_filter.jsp" %>
			</td>
		</tr>
		<tr>
			<td class="th" width="100%" colspan="4" align="center">
				<%@ include file="filters/town_filter.jsp" %>
			</td>
		</tr>
		<tr>
			<td class="th" width="100%" colspan="4" align="center">
				<%@ include file="filters/street_filter.jsp" %>
			</td>
		</tr>
		
  <tr>
    <td>
      <s:property value="%{getTranslation(typeNumber.translations).name}" />
    </td>
    <td>
      <s:textfield name="numberVal" />
    </td>
  </tr>
  <tr>
    <td>
      <s:property value="%{getTranslation(typeBulk.translations).name}" />
    </td>
    <td>
      <s:textfield name="bulkVal" />
    </td>
  </tr>
  
  <tr>
    <td>
      <input type="button" class="btn-exit"
				       onclick="window.location='<s:url action="buildingsCreateAction"/>?action=create'"
					   value="<s:text name="ab.create"/>"/>
	  <input type="submit" class="btn-exit"
					   value="<s:text name="common.refresh"/>"/>
    </td>
  </tr>




</form>

</table>