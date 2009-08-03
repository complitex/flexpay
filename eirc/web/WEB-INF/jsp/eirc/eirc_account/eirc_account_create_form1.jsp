
<%@ include file="/WEB-INF/jsp/common/taglibs.jsp" %>

<s:actionerror/>

<form id="srform" method="post" action="<s:url action="eircAccountCreateForm2" includeParams="none" />">

    <%@include file="/WEB-INF/jsp/ab/filters/groups/country_region_town_street_building_apartment_ajax.jsp" %>
    <input type="submit" id="submitButton" class="btn-exit" value="<s:text name="common.next"/>" disabled="true" />
</form>

<script type="text/javascript">
    $(function() {

        FF.addEraseFunction("apartment", function(filter) {
            $("#submitButton").attr("disabled", true);
        });

        FF.addListener("apartment", function(filter) {
            $("#submitButton").removeAttr("disabled");
        });

    });

</script>
