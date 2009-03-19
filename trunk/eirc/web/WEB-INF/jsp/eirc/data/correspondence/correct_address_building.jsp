
<%@ include file="/WEB-INF/jsp/common/taglibs.jsp" %>

<s:actionerror/>

<form id="fobjects" method="post"
	  action="<s:url action="registryRecordCorrectAddressBuilding" includeParams="none"/>"
	  onsubmit="return FP.validateSubmit('<s:text name="eirc.need_select_building" />');">

	<%@include file="../registry_record_info.jsp" %>

	<table cellpadding="3" cellspacing="1" border="0" width="100%">

		<tr>
			<td colspan="4">
				<%@ include file="/WEB-INF/jsp/ab/filters/groups/country_region_town_streetname.jsp" %>
			</td>
		</tr>

		<tr>
			<td class="th" width="1%">&nbsp;</td>
			<td class="th" width="1%">&nbsp;</td>
			<td class="th"><s:text name="ab.building"/></td>
		</tr>
		<s:iterator value="%{buildingsList}" status="status">
		<tr valign="middle" class="cols_1">
			<td class="col_1s" align="right">
				<s:property value="%{#status.index + pager.thisPageFirstElementNumber + 1}"/>
			</td>
			<td class="col">
				<input type="radio" value="<s:property value="%{id}"/>" name="object.id"/>
			</td>
			<td class="col">
				<s:property value="%{getBuildingNumber(buildingAttributes)}"/>
			</td>
		</tr>
		</s:iterator>
		<tr>
			<td colspan="3">
				<%@ include file="/WEB-INF/jsp/ab/filters/pager.jsp" %>
				<input type="hidden" id="setupType" name="setupType" value="setupType"/>
				<s:hidden name="record.id" value="%{record.id}"/>
				<input type="submit" onclick="$('#setupType').val('building');" class="btn-exit"
					   value="<s:text name="common.set"/>"/>
			</td>
		</tr>
    </table>
</form>
