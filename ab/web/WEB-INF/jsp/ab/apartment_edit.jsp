<%@ include file="/WEB-INF/jsp/common/taglibs.jsp" %>


<s:form>
<table cellpadding="3" cellspacing="1" border="0" width="100%">

<tr>
	<td colspan="4">
		<%@ include file="filters/groups/country_region_town_street_building.jsp" %>
	</td>
</tr>



		<tr>
			<td class="col">
				<s:text name="ab.apartment.number" />
			</td>
			<td class="col">
				<s:textfield name="apartmentNumber" value="%{apartment.number}" />
			</td>
			<td colspan="4">
		      <s:hidden name="apartment.id" value="%{apartment.id}" />
		      <s:submit name="submit" value="%{getText('common.save')}" cssClass="btn-exit" />
		    </td>
		</tr>
		<tr>
			<td colspan="4" height="3" bgcolor="#4a4f4f"/>
		</tr>
			
</table>
</s:form>



<s:if test="!apartment.persons.empty">
<table cellpadding="3" cellspacing="1" border="0" width="100%">
  <tr>
      <td colspan="5" width="100%">
        Registered in apartment
      </td>
  </tr>
 
  <tr>
      <td class="th" width="1%">
        &nbsp;
      </td>
	  <td class="th">
	    FIO
	  </td>
	  <td class="th">
	    Birth date
	  </td>
	  <td class="th">
	    Registration from date
	  </td>
	  <td class="th">
	    Registration till date
	  </td>
  </tr>
  <s:iterator value="apartment.validPersonRegistrations" status="status">
	<tr valign="middle" class="cols_1">
	  <td class="col_1s" align="right">
		<s:property	value="%{#status.index + 1}"/>
	  </td>
	  <td class="col">
	    <s:property value="person.defaultIdentity.lastName" />
	    <s:property value="person.defaultIdentity.firstName" />
	    <s:property value="person.defaultIdentity.middleName" />
	  </td>
	  <td class="col">
	    <s:property value="person.defaultIdentity.birthDate" />
	  </td>
	  <td class="col">
	    <s:property value="beginDate" />
	  </td>
	  <td class="col">
	    <s:property value="endDate" />
	  </td>
	</tr>
  </s:iterator>
</table>  
</s:if>


<s:if test="!apartment.personRegistrations.empty">
<table cellpadding="3" cellspacing="1" border="0" width="100%">
  <tr>
      <td colspan="5" width="100%">
        Registration history
      </td>
  </tr>
 
  <tr>
      <td class="th" width="1%">
        &nbsp;
      </td>
	  <td class="th">
	    FIO
	  </td>
	  <td class="th">
	    Birth date
	  </td>
	  <td class="th">
	    Registration from date
	  </td>
	  <td class="th">
	    Registration till date
	  </td>
  </tr>
  <s:iterator value="%{sortPersonRegistrations(apartment.personRegistrations)}" status="status">
	<tr valign="middle" class="cols_1">
	  <td class="col_1s" align="right">
		<s:property	value="%{#status.index + 1}"/>
	  </td>
	  <td class="col">
	    <s:property value="person.defaultIdentity.lastName" />
	    <s:property value="person.defaultIdentity.firstName" />
	    <s:property value="person.defaultIdentity.middleName" />
	  </td>
	  <td class="col">
	    <s:property value="person.defaultIdentity.birthDate" />
	  </td>
	  <td class="col">
	    <s:property value="beginDate" />
	  </td>
	  <td class="col">
	    <s:property value="endDate" />
	  </td>
	</tr>
  </s:iterator>
</table>  
</s:if>