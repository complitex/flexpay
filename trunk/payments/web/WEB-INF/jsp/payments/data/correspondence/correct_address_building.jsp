<%@ include file="/WEB-INF/jsp/common/taglibs.jsp" %>

<s:actionerror/>

<form id="fobjects" method="post"
	  action="<s:url action="registryRecordCorrectAddressBuilding" includeParams="none"/>"
	  onsubmit="return FP.validateSubmit('<s:text name="eirc.need_select_building" />');">

    <%@include file="../registry_record_info.jsp" %>

    <%@ include file="/WEB-INF/jsp/ab/filters/groups/country_region_town_street_ajax.jsp" %>
    <span id="result"></span>
    <s:hidden name="record.id" value="%{record.id}" />

</form>

<script type="text/javascript">

    var shadowId = "shadow";
    var resultId = "result";

    $(function() {

        FP.createShadow(shadowId);

        FF.addListener("street", function(filter) {
            FP.resizeShadow(shadowId, resultId, {visibility:"visible"});
            $.post("<s:url action="buildingsDialogListAjax" namespace="/payments" includeParams="none"/>",
                    {streetId: filter.value.val()},
                    function(data) {
                        $("#" + resultId).html(data);
                        FP.hideShadow(shadowId);
                    });
        });
        FF.addEraser("street", function() {
            $("#" + resultId).html("");
        });
    });

    function pagerAjax(element) {
        FP.pagerAjax(element, {
            action:"<s:url action="buildingsDialogListAjax" namespace="/payments" includeParams="none"/>",
            params:{
                streetId: FF.filters["street"].value.val()
            }
        });
    }

</script>
