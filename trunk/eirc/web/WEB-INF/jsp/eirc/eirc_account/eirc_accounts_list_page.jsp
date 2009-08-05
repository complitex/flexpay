<%@ include file="/WEB-INF/jsp/common/taglibs.jsp" %>

<s:actionerror/>

<form id="fobjects">

	<%@ include file="/WEB-INF/jsp/ab/filters/groups/country_region_town_street_building_apartment_ajax.jsp" %>
    <table>
        <tr>
            <td class="filter"><s:text name="ab.person.fio"/></td>
            <td colspan="5">
                <input type="text" id="personFio" name="personFio" />
                <input type="button" class="btn-exit" onclick="personSearch();" value="<s:text name="common.search" />" />
            </td>
        </tr>
    </table>

    <span id="result"></span>

</form>

<script type="text/javascript">
    $(function() {

        FF.addEraseFunction("apartment", function(filter) {
            var id = "result";
            FF.loading(id);
            $.post("<s:url action="eircAccountsListAjax" namespace="/eirc" includeParams="none"/>",
                    {},
                    function(data) {
                        $("#" + id).html(data);
                    });
        });

        FF.addListener("apartment", function(filter) {
            var id = "result";
            FF.loading(id);
            $.post("<s:url action="eircAccountsListAjax" namespace="/eirc" includeParams="none"/>",
                    {apartmentId: filter.value.val()},
                    function(data) {
                        $("#" + id).html(data);
                    });
        });

    });

    $.post("<s:url action="eircAccountsListAjax" namespace="/eirc" includeParams="none"/>",
            {},
            function(data) {
                $("#result").html(data);
            });

    function personSearch() {
        $.post("<s:url action="eircAccountsListAjax" namespace="/eirc" includeParams="none"/>",
                {personFio: $("#personFio").val()},
                function(data) {
                    $("#result").html(data);
                });
    }

    function pagerAjax(element) {
        var isSelect = element.name == "pager.pageSize";
        $.post("<s:url action="eircAccountsListAjax" namespace="/eirc" includeParams="none"/>",
                {
                    apartmentId: FF.filters["apartment"].value.val(),
                    personFio: $("#personFio").val() == "" ? null : $("#personFio").val(),
                    pageSizeChanged: isSelect,
                    "pager.pageNumber": isSelect ? "" : element.value,
                    "pager.pageSize": isSelect ? element.value : $('select[name="pager.pageSize"]').val()
                },
                function(data) {
                    $("#result").html(data);
                });
    }

</script>
