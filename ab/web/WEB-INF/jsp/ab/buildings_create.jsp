<%@ include file="/WEB-INF/jsp/common/taglibs.jsp" %>


<ul>
	<li><span class="errorMessage"><s:text name="%{filterError}" /></span></li>
</ul>

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
      <s:text name="ab.district" />
    </td>
    <td>
      <s:select name="districtId" list="districtList" listKey="id" listValue="%{getTranslation(currentName.translations).name}" />
    </td>
  </tr>		
  <tr>
    <td>
      <s:property value="%{getTranslation(typeNumber.translations).name}" />
    </td>
    <td>
      <s:textfield name="numberValue" value="%{numberValue}"/>
    </td>
  </tr>
  <tr>
    <td>
      <s:property value="%{getTranslation(typeBulk.translations).name}" />
    </td>
    <td>
      <s:textfield name="bulkValue" value="%{bulkValue}"/>
    </td>
  </tr>
  
  <tr>
    <td>
      <ul>
	    <li><span class="errorMessage"><s:text name="%{districtError}" /></span></li>
	    <li><span class="errorMessage"><s:text name="%{streetError}" /></span></li>
	    <li><span class="errorMessage"><s:text name="%{buildingAttrError}" /></span></li>
      </ul>
    </td>
  </tr>
  
  <tr>
    <td>
	  <input type="submit"
	         class="btn-exit"
	         onclick="$('fobjects').action='<s:url action="buildingsCreateAction" includeParams="none" />?action=create';$('fobjects').submit()"
	         value="<s:text name="ab.create"/>"/>
	         				   
	  <input type="submit"
	         class="btn-exit"
			 value="<s:text name="common.refresh"/>"/>
    </td>
  </tr>




</form>

</table>