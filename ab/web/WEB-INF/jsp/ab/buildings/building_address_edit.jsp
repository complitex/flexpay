<%@include file="/WEB-INF/jsp/common/taglibs.jsp"%>
<%@include file="/WEB-INF/jsp/common/includes/jquery_bgiframe.jsp"%>
<%@include file="/WEB-INF/jsp/ab/includes/flexpay_filter.jsp"%>

<s:set name="readonly" value="%{address.id > 0}" />

<script type="text/javascript">

    $("#formRegistration").ready(function() {
        FF.createFilter("country", {
            action: "<s:url action="countryFilterAjax" namespace="/dicts" />",
            <s:if test="#readonly">display:"input-readonly",</s:if>
            defaultValue: "<s:property value="countryFilter != null ? countryFilter : userPreferences.countryFilter" />"
        });
        FF.createFilter("region", {
            action: "<s:url action="regionFilterAjax" namespace="/dicts" />",
            parents: ["country"],
            <s:if test="#readonly">display:"input-readonly",</s:if>
            defaultValue: "<s:property value="regionFilter != null ? regionFilter : userPreferences.regionFilter" />"
        });
        FF.createFilter("town", {
            action: "<s:url action="townFilterAjax" namespace="/dicts" />",
            parents: ["region"],
            <s:if test="#readonly">display:"input-readonly",</s:if>
            defaultValue: "<s:property value="townFilter != null ? townFilter : userPreferences.townFilter" />"
        });
        FF.createFilter("street", {
            action: "<s:url action="streetFilterAjax" namespace="/dicts" />",
            parents: ["town"],
            <s:if test="#readonly">display:"input-readonly",</s:if>
            defaultValue: "<s:property value="streetFilter != null ? streetFilter : userPreferences.streetFilter" />"
        });        
    });
</script>

<div id="response">
    <%@include file="/WEB-INF/jsp/common/includes/errors_messages.jsp"%>
</div>

<s:form action="buildingAddressEdit" method="POST">

	<s:hidden name="building.id" />
	<s:hidden name="address.id" />

	<table width="100%">
		<tr>
			<td class="filter"><s:text name="ab.country" /></td>
			<td id="country_raw"></td>
			<td class="filter"><s:text name="ab.region" /></td>
			<td id="region_raw"></td>
			<td class="filter"><s:text name="ab.town" /></td>
			<td id="town_raw"></td>
		</tr>
		<tr>
			<td class="filter"><s:text name="ab.street" /></td>
			<td id="street_raw" colspan="5"></td>
		</tr>
	</table>

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
