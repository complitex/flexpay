<%@ include file="/WEB-INF/jsp/common/taglibs.jsp" %>

<s:actionerror/>

<table cellpadding="3" cellspacing="1" border="0" width="100%">
	<s:form id="fobjects">

		<tr>
			<td colspan="4">
				<%@ include file="filters/groups/country_region_town_streetname.jsp" %>
			</td>
		</tr>

		<tr>
			<td colspan="4">
				<%@include file="/WEB-INF/jsp/common/filter/pager/pager.jsp" %>
				<input type="submit" class="btn-exit"
				       onclick="$('#fobjects').attr('action','<s:url action="buildingDelete" includeParams="none" />');"
					   value="<s:text name="common.delete_selected"/>"/>
				<input type="button" class="btn-exit"
				       onclick="window.location='<s:url action="buildingCreate" includeParams="none" />';"
					   value="<s:text name="common.new"/>"/>
			</td>
		</tr>

		<tr>
			<td class="th" width="1%">&nbsp;</td>
			<td class="th" width="1%">
                <input type="checkbox" onchange="FP.setCheckboxes(this.checked, 'objectIds');">
			</td>
			<td class="th" ><s:text name="ab.building"/></td>
			<td class="th">&nbsp;</td>
		</tr>
        <tr>
            <td colspan="4" id="result">

            </td>
        </tr>
		<tr>
			<td colspan="4">
				<%@include file="/WEB-INF/jsp/common/filter/pager/pager.jsp" %>
				<input type="submit" class="btn-exit"
				       onclick="$('#fobjects').attr('action','<s:url action="buildingDelete" includeParams="none" />');"
					   value="<s:text name="common.delete_selected"/>"/>
				<input type="submit" class="btn-exit"
				       onclick="$('#fobjects').attr('action','<s:url action="buildingCreate" includeParams="none" />');"
					   value="<s:text name="common.new"/>"/>
			</td>
		</tr>
	</s:form>
</table>
