<%@include file="/WEB-INF/jsp/common/taglibs.jsp"%>
<%@include file="/WEB-INF/jsp/common/includes/jquery_bgiframe.jsp"%>
<%@include file="/WEB-INF/jsp/common/includes/jquery_ui_core.jsp"%>
<%@include file="/WEB-INF/jsp/common/includes/jquery_ui_datepicker.jsp" %>
<%@include file="/WEB-INF/jsp/ab/includes/flexpay_filter.jsp"%>

<script type="text/javascript">

    $("#formRegistration").ready(function() {
        FF.createFilter("country", {
            action: "<s:url action="countryFilterAjax" namespace="/dicts" />",
            defaultValue: "<s:property value="countryFilter != null ? countryFilter : ''" />"
        });
        FF.createFilter("region", {
            action: "<s:url action="regionFilterAjax" namespace="/dicts" />",
            parents: ["country"],
            defaultValue: "<s:property value="regionFilter != null ? regionFilter : ''" />"
        });
        FF.createFilter("town", {
            action: "<s:url action="townFilterAjax" namespace="/dicts" />",
            parents: ["region"],
            defaultValue: "<s:property value="townFilter != null ? townFilter : ''" />"
        });
        FF.createFilter("street", {
            action: "<s:url action="streetFilterAjax" namespace="/dicts" />",
            parents: ["town"],
            defaultValue: "<s:property value="streetFilter != null ? streetFilter : ''" />"
        });
        FF.createFilter("building", {
            action: "<s:url action="buildingFilterAjax" namespace="/dicts" />",
            isArray: true,
            parents: ["street"],
            defaultValue: "<s:property value="buildingFilter != null ? buildingFilter : ''" />"
        });
        FF.createFilter("apartment", {
            action: "<s:url action="apartmentFilterAjax" namespace="/dicts" />",
            isArray: true,
            parents: ["building"],
            defaultValue: "<s:property value="apartmentFilter != null ? apartmentFilter : ''" />"
        });

    });

    FP.calendars("beginDate", true);
    FP.calendars("endDate", true);

</script>

<form id="formRegistration">

    <table cellpadding="3" cellspacing="1" border="0" width="100%">
        <tr class="cols_1">
            <td class="col_1s" colspan="4"><strong><s:text name="ab.person.registration_address" /></strong></td>
        </tr>
        <tr>
            <td class="filter"><s:text name="ab.country" /></td>
            <td id="country_raw"></td>
            <td class="filter"><s:text name="ab.region" /></td>
            <td id="region_raw"></td>
        </tr>
        <tr>
            <td class="filter"><s:text name="ab.town" /></td>
            <td id="town_raw"></td>
            <td class="filter"><s:text name="ab.street" /></td>
            <td id="street_raw"></td>
        </tr>
        <tr>
            <td class="filter"><s:text name="ab.building" /></td>
            <td id="building_raw"></td>
            <td class="filter"><s:text name="ab.apartment" /></td>
            <td id="apartment_raw"></td>
        </tr>
        <tr>
            <td class="filter">
                <s:text name="ab.person.registration.begin_date" />
            </td>
            <td>
                <s:textfield name="beginDate" readonly="true" />
            </td>
            <td class="filter">
                <s:text name="ab.person.registration.end_date" />
            </td>
            <td>
                <s:textfield name="endDate" readonly="true" />
            </td>
        </tr>
        <tr>
            <td colspan="3">
                <input type="button" class="btn-exit" value="<s:text name="common.save" />" onclick="submitRegistration();" />
            </td>
        </tr>
    </table>

</form>
