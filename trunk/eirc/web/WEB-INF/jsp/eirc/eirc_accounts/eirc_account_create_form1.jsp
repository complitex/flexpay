<%@include file="/WEB-INF/jsp/common/taglibs.jsp"%>
<%@include file="/WEB-INF/jsp/common/includes/errors_messages.jsp"%>

<s:form action="eircAccountCreateForm2" method="post">

    <%@include file="/WEB-INF/jsp/ab/filters/groups/country_region_town_street_building_apartment_ajax.jsp"%>
    <s:submit id="submitButton" cssClass="btn-exit" name="submitted" value="%{getText('common.save')}" disabled="true" />

</s:form>

<script type="text/javascript">

    $(function() {

        FF.addListener("apartment", function() {
            $("#submitButton").removeAttr("disabled");
        });

        FF.addEraser("apartment", function() {
            $("#submitButton").attr("disabled", true);
        });

    });

</script>
