<%@ include file="/WEB-INF/jsp/common/taglibs.jsp" %>

<s:actionerror />

<s:form action="editBuildingAddress">
	<s:hidden name="building.id" value="%{building.id}" />
	<s:hidden name="address.id" value="%{address.id}" />

	<%@ include file="filters/groups/country_region_town_street_ajax.jsp" %>

	<table cellpadding="3" cellspacing="1" border="0" width="100%">

		<s:iterator value="attributeMap">
			<tr valign="middle" class="cols_1">
				<td class="col">
					<s:property value="%{getTypeName(key)}" />
				</td>
				<td class="col">
					<s:textfield name="attributeMap[%{key}]" value="%{value}" />
				</td>
			</tr>
		</s:iterator>

		<tr>
			<td colspan="2">
				<input type="submit" class="btn-exit" name="submitted" value="<s:text name="common.save"/>" />
			</td>
		</tr>

	</table>
</s:form>

<script type="text/javascript">
    $(function() {
        FF.updateFilter("street", {readonly:true});
    });
</script>
