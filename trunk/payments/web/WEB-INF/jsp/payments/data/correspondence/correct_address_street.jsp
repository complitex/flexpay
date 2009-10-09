<%@ include file="/WEB-INF/jsp/common/taglibs.jsp" %>

<form id="fobjects" method="post"
	  action="<s:url action="registryRecordCorrectAddressStreet" includeParams="none"/>"
	  onsubmit="return FP.validateSubmit('<s:text name="eirc.need_select_street" />');">

    <%@include file="../registry_record_info.jsp" %>

    <%@ include file="/WEB-INF/jsp/ab/filters/groups/country_region_town_streetname_ajax.jsp" %>
    <span id="result"></span>
    <s:hidden name="record.id" value="%{record.id}" />

</form>

<script type="text/javascript">

    var shadowId = "shadow";
    var resultId = "result";

    $(function() {

        FP.createShadow(shadowId);

        FF.addListener("town", function(filter) {
            FP.resizeShadow(shadowId, resultId, {visibility:"visible"});
            $.post("<s:url action="streetsDialogListAjax" namespace="/payments" includeParams="none"/>",
                    {townId: filter.value.val()},
                    function(data) {
                        $("#" + resultId).html(data);
                        FP.hideShadow(shadowId);
                    });
        });
        FF.addEraser("town", function() {
            $("#" + resultId).html("");
        });
        FF.addListener("street", function(filter) {
            FP.resizeShadow(shadowId, resultId, {visibility:"visible"});
            $.post("<s:url action="streetsDialogListAjax" namespace="/payments" includeParams="none"/>",
                    {streetId: filter.value.val()},
                    function(data) {
                        $("#" + resultId).html(data);
                        FP.hideShadow(shadowId);
                    });
        });

    });

    function pagerAjax(element) {
        FP.pagerAjax(element, {
            action:"<s:url action="streetsDialogListAjax" namespace="/payments" includeParams="none"/>",
            params:{
                townId: FF.filters["town"].value.val()
            }
        });
    }

</script>
