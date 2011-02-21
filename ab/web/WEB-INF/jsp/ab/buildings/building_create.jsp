<%@include file="/WEB-INF/jsp/common/taglibs.jsp"%>

<div id="response">
    <%@include file="/WEB-INF/jsp/common/includes/errors_messages.jsp"%>
</div>

<s:form action="buildingCreate" method="POST">

	<s:hidden name="building.id" />

	<%@include file="/WEB-INF/jsp/ab/filters/groups/country_region_town_district_street_ajax.jsp"%>

	<table cellpadding="3" cellspacing="1" border="0" width="100%">
		<s:iterator value="attributesMap">
			<tr valign="middle" class="cols_1">
				<td class="col">
					<s:property value="getTypeName(key)" />
				</td>
				<td class="col">
					<s:textfield name="attributesMap[%{key}]" value="%{value}" />
				</td>
			</tr>
		</s:iterator>
		<tr>
			<td colspan="2">
                <s:submit cssClass="btn-exit" name="submitted" value="%{getText('common.save')}" />
			</td>
		</tr>
	</table>

</s:form>
