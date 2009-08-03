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
    $(function() {
        FF.addListener("street", function(filter) {
            var id = "result";
            FF.loading(id);
            $.post("<s:url action="buildingsDialogListAjax" namespace="/payments" includeParams="none"/>",
                    {streetId: filter.value.val()},
                    function(data) {
                        $("#" + id).html(data);
                    });
        });

        FF.addEraseFunction("street", function(filter) {
            $("#result").html("");
        });

    });

    function pagerAjax(element) {
        var isSelect = element.name == "pager.pageSize";
        $.post("<s:url action="buildingsDialogListAjax" namespace="/payments" includeParams="none"/>",
                {
                    streetId: FF.filters["street"].value.val(),
                    pageSizeChanged: isSelect,
                    "pager.pageNumber": isSelect ? "" : element.value,
                    "pager.pageSize": isSelect ? element.value : $('select[name="pager.pageSize"]').val()
                },
                function(data) {
                    $("#result").html(data);
                });
    }

</script>
