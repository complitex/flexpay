<%@include file="/WEB-INF/jsp/common/taglibs.jsp"%>
<%@include file="/WEB-INF/jsp/common/includes/errors_messages.jsp"%>

<form id="fobjects" method="post"
	  action="<s:url action="registryRecordCorrectAddress" includeParams="none" />"
	  onsubmit="return FP.validateSubmit('<s:text name="eirc.need_select_apartment" />');">

    <s:hidden name="record.id" value="%{record.id}" />

    <%@include file="/WEB-INF/jsp/payments/data/registry_record_info.jsp"%>
    <%@include file="/WEB-INF/jsp/ab/filters/groups/country_region_town_street_building_ajax.jsp"%>
    <span id="result"></span>

</form>

<script type="text/javascript">

    var resultId = "result";

    $(function() {

        FF.addListener("building", function(filter) {
            FP.pagerAjax(null, {
                action:"<s:url action="apartmentsDialogListAjax" namespace="/payments" includeParams="none" />",
                params:{buildingFilter: filter.value.val()}
            });
        });
        FF.addEraser("building", function() {
            $("#" + resultId).html("");
        });
    });

    function pagerAjax(element) {
        FP.pagerAjax(element, {
            action:"<s:url action="apartmentsDialogListAjax" namespace="/payments" includeParams="none" />",
            params:{buildingFilter: FF.filters["building"].value.val()}
        });
    }

</script>
