<%@ include file="/WEB-INF/jsp/common/taglibs.jsp" %>


<ul>
	<li><span class="errorMessage"><s:text name="%{filterError}" /></span></li>
</ul>

<table cellpadding="3" cellspacing="1" border="0" width="100%">

<form id="fobjects" method="post" action="<s:url value="/dicts/buildingsCreateAction.action" includeParams="none" />">
<s:hidden name="buildingId" value="%{buildingId}" />

		<tr>
			<td colspan="4">
				<%@ include file="filters/groups/country_region_town_street.jsp" %>
			</td>
		</tr>
		
  <tr valign="middle" class="cols_1">
    <td>
      <s:text name="ab.district" />
    </td>
    <td>
      <s:if test="district != null" >
        <s:property value="%{getTranslation(district.currentName.translations).name}" />
        <s:hidden name="districtId" value="%{district.id}" />
      </s:if>
      <s:if test="district == null" >
        <s:select name="districtId" list="districtList" listKey="id" listValue="%{getTranslation(currentName.translations).name}" />
      </s:if>  
    </td>
  </tr>		
  <tr valign="middle" class="cols_1">
    <td>
      <s:property value="%{getTranslation(typeNumber.translations).name}" />
    </td>
    <td>
      <s:textfield name="numberValue" value="%{numberValue}"/>
    </td>
  </tr>
  <tr valign="middle" class="cols_1">
    <td>
      <s:property value="%{getTranslation(typeBulk.translations).name}" />
    </td>
    <td>
      <s:textfield name="bulkValue" value="%{bulkValue}"/>
    </td>
  </tr>
  
  <tr>
    <td>

	  <input type="submit"
	         class="btn-exit"
	         onclick="$('fobjects').action='<s:url action="buildingsCreateAction" includeParams="none" />?action=create';$('fobjects').submit()"
	         value="<s:text name="ab.create"/>"/>
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
  
  




</form>

</table>