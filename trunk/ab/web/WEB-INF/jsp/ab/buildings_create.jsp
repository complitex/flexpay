<%@ include file="/WEB-INF/jsp/common/taglibs.jsp" %>


<ul>
	<li><span class="errorMessage"><s:text name="%{filterError}" /></span></li>
</ul>

<table cellpadding="3" cellspacing="1" border="0" width="100%">

<form id="fobjects" method="post" action="<s:url value="/dicts/buildingsCreateAction.action" includeParams="none" />">
<s:hidden name="buildingId" value="%{buildingId}" />

		<tr>
			<td colspan="4">
				<table width="100%">
					<col width="16%" align="center">
					<col width="16%" align="center">
					<col width="16%" align="right">
					<col width="16%" align="left">
					<col width="18%" align="right">
					<col width="18%" align="left">
			
					<tr>
						<td class="filter"><s:text name="ab.country"/></td>
						<td>
							<select name="countryFilter.selectedId" onchange="this.form.submit()" class="form-select" <s:if test="district != null" >disabled="1"</s:if>>
  								<s:iterator value="countryFilter.names" >
									<option  value="<s:property value="translatable.id"/>"<s:if test="%{translatable.id == countryFilter.selectedId}"> selected</s:if>><s:property value="name"/>
									</option>
  								</s:iterator>
							</select>
						</td>
						<td class="filter"><s:text name="ab.region"/></td>
						<td>
							<select name="regionFilter.selectedId" onchange="this.form.submit()" class="form-select" <s:if test="district != null" >disabled="1"</s:if>><s:iterator value="regionFilter.names">
								<option value="<s:property value="translatable.object.id"/>"<s:if test="%{translatable.object.id == regionFilter.selectedId}"> selected</s:if>><s:property value="name"/></option></s:iterator>
							</select>
						</td>
						<td class="filter"><s:text name="ab.town"/></td>
						<td>
							<select name="townFilter.selectedId" onchange="this.form.submit()" class="form-select" <s:if test="district != null" >disabled="1"</s:if>><s:iterator value="townFilter.names">
								<option value="<s:property value="translatable.object.id"/>"<s:if test="%{translatable.object.id == townFilter.selectedId}"> selected</s:if>><s:property value="name"/></option></s:iterator>
							</select>
						</td>
					</tr>
					<tr>
						<td class="filter"><s:text name="ab.street"/></td>
						<td><%@include file="filters/street_filter.jsp" %></td>
						<td colspan="4">&nbsp;</td>
					</tr>
				</table>
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
  
  <s:iterator value="buildings.buildingAttributes">
  <tr valign="middle" class="cols_1">
    <td class="col">
      <s:property value="%{getTranslation(buildingAttributeType.translations).name}" />
    </td>
    <td class="col" align="center">
      <s:textfield name="attributeMap['%{buildingAttributeType.type}'].value" value="" />
    </td>
  </tr>
  </s:iterator>
  		
  
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
	    <li><span class="errorMessage"><s:text name="%{creatingError}" /></span></li>
      </ul>
    </td>
  </tr>
  
  




</form>

</table>