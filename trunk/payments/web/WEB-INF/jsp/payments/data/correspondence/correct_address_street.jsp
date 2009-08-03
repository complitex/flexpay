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
    $(function() {
        FF.addListener("town", function(filter) {
            var id = "result";
            FF.loading(id);
            $.post("<s:url action="streetsDialogListAjax" namespace="/payments" includeParams="none"/>",
                    {townId: filter.value.val()},
                    function(data) {
                        $("#" + id).html(data);
                    });
        });

        FF.addEraseFunction("town", function(filter) {
            $("#result").html("");
        });

        FF.addListener("street", function(filter) {
            var id = "result";
            FF.loading(id);
            $.post("<s:url action="streetsDialogListAjax" namespace="/payments" includeParams="none"/>",
                    {streetId: filter.value.val()},
                    function(data) {
                        $("#" + id).html(data);
                    });
        });

    });

    function pagerAjax(element) {
        var isSelect = element.name == "pager.pageSize";
        $.post("<s:url action="streetsDialogListAjax" namespace="/payments" includeParams="none"/>",
                {
                    townId: FF.filters["town"].value.val(),
                    pageSizeChanged: isSelect,
                    "pager.pageNumber": isSelect ? "" : element.value,
                    "pager.pageSize": isSelect ? element.value : $('select[name="pager.pageSize"]').val()
                },
                function(data) {
                    $("#result").html(data);
                });
    }

</script>
