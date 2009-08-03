<%@ include file="/WEB-INF/jsp/common/taglibs.jsp" %>

<s:actionerror/>

<form id="fobjects" method="post"
	  action="<s:url action="registryRecordCorrectAddress" includeParams="none"/>"
	  onsubmit="return FP.validateSubmit('<s:text name="eirc.need_select_apartment" />');">

    <%@include file="../registry_record_info.jsp" %>

    <%@ include file="/WEB-INF/jsp/ab/filters/groups/country_region_town_district_street_building_ajax.jsp" %>
    <span id="result"></span>
    <s:hidden name="record.id" value="%{record.id}" />

</form>

<script type="text/javascript">
    $(function() {
        FF.addListener("building", function(filter) {
            var id = "result";
            FF.loading(id);
            $.post("<s:url action="apartmentsDialogListAjax" namespace="/payments" includeParams="none"/>",
                    {buildingId: filter.value.val()},
                    function(data) {
                        $("#" + id).html(data);
                    });
        });

        FF.addEraseFunction("building", function(filter) {
            $("#result").html("");
        });

    });

    function pagerAjax(element) {
        var isSelect = element.name == "pager.pageSize";
        $.post("<s:url action="apartmentsDialogListAjax" namespace="/payments" includeParams="none"/>",
                {
                    buildingId: FF.filters["building"].value.val(),
                    pageSizeChanged: isSelect,
                    "pager.pageNumber": isSelect ? "" : element.value,
                    "pager.pageSize": isSelect ? element.value : $('select[name="pager.pageSize"]').val()
                },
                function(data) {
                    $("#result").html(data);
                });
    }

</script>
