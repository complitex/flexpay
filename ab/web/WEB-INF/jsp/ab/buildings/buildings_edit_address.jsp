<%@include file="/WEB-INF/jsp/common/taglibs.jsp" %>

<%@include file="/WEB-INF/jsp/common/includes/jquery_ui.jsp"%>
<%@include file="/WEB-INF/jsp/ab/includes/flexpay_filter.jsp" %>

<script type="text/javascript">

    $("#formRegistration").ready(function() {
        FF.createFilter("country", {
            action: "<s:url action="countryFilterAjax" namespace="/dicts" includeParams="none"/>",
            defaultValue: "<s:text name="%{countryFilter != null ? countryFilter : ''}" />"
        });
        FF.createFilter("region", {
            action: "<s:url action="regionFilterAjax" namespace="/dicts" includeParams="none"/>",
            parents: ["country"],
            defaultValue: "<s:text name="%{regionFilter != null ? regionFilter : ''}" />"
        });
        FF.createFilter("town", {
            action: "<s:url action="townFilterAjax" namespace="/dicts" includeParams="none"/>",
            parents: ["region"],
            defaultValue: "<s:text name="%{townFilter != null ? townFilter : ''}" />"
        });
        FF.createFilter("street", {
            action: "<s:url action="streetFilterAjax" namespace="/dicts" includeParams="none"/>",
            parents: ["town"],
            defaultValue: "<s:text name="%{streetFilter != null ? streetFilter : ''}" />"
        });        
    });
</script>

<s:actionerror />

<s:form action="editBuildingAddress">

	<s:hidden name="building.id" value="%{building.id}" />
	<s:hidden name="address.id" value="%{address.id}" />

	<%--<%@ include file="../filters/groups/country_region_town_street_ajax.jsp" %>--%>
	<%-- TODO replace with filters --%>
	<table width="100%">
		<tr>
			<td class="filter"><s:text name="ab.country"/></td>
			<td id="country_raw"></td>
			<td class="filter"><s:text name="ab.region"/></td>
			<td id="region_raw"></td>
			<td class="filter"><s:text name="ab.town"/></td>
			<td id="town_raw"></td>
		</tr>
		<tr>
			<td class="filter"><s:text name="ab.street"/></td>
			<td id="street_raw" colspan="5"></td>
		</tr>
	</table>

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

<s:if test="%{address.id != 0}">
    <script type="text/javascript">
        $(function() {
            FF.updateFilter("street", {readonly:true});
        });
    </script>
</s:if>
