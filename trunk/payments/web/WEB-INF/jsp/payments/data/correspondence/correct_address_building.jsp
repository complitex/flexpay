<%@ include file="/WEB-INF/jsp/common/taglibs.jsp" %>

<s:actionerror/>

<form id="fobjects" method="post"
	  action="<s:url action="registryRecordCorrectAddressBuilding" includeParams="none"/>"
	  onsubmit="return FP.validateSubmit('<s:text name="eirc.need_select_building" />');">

    <%@include file="../registry_record_info.jsp" %>

    <%@ include file="/WEB-INF/jsp/ab/filters/groups/country_region_town_street_building_ajax.jsp" %>
    <span id="result"></span>
    <s:hidden name="record.id" value="%{record.id}" />

</form>

<script type="text/javascript">

    var resultId = "result";

    $(function() {

        FF.addListener("building", function(filter) {
            FP.pagerAjax(null, {
                action:"<s:url action="buildingDialogAjax" namespace="/payments" includeParams="none" />",
                params:{buildingFilter: filter.value.val()}
            });
        });
        FF.addEraser("building", function() {
            $("#" + resultId).html("");
        });
    });

    function pagerAjax(element) {
        FP.pagerAjax(element, {
            action:"<s:url action="buildingDialogAjax" namespace="/payments" includeParams="none" />",
            params:{
                buildingFilter: FF.filters["building"].value.val()
            }
        });
    }

</script>
