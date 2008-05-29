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

