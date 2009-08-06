<%@include file="/WEB-INF/jsp/common/taglibs.jsp" %>

<s:actionerror/>

<form id="fobjects">

	<%@include file="/WEB-INF/jsp/ab/filters/groups/country_region_town_street_building_apartment_ajax.jsp" %>
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

    var shadowId = "shadow";
    var resultId = "result";

    $(function() {

        FP.createShadow(shadowId);

        FF.addListener("apartment", function(filter) {
            FP.resizeShadow(shadowId, resultId, {visibility:"visible"});
            $.post("<s:url action="eircAccountsListAjax" namespace="/eirc" includeParams="none"/>",
                    {apartmentId: filter.value.val()},
                    function(data) {
                        $("#" + resultId).html(data);
                        FP.hideShadow(shadowId);
                    });
        });
        FF.addEraseFunction("apartment", function(filter) {
            FP.resizeShadow(shadowId, resultId, {visibility:"visible"});
            $.post("<s:url action="eircAccountsListAjax" namespace="/eirc" includeParams="none"/>",
                    {},
                    function(data) {
                        $("#" + resultId).html(data);
                        FP.hideShadow(shadowId);
                    });
        });
    });

    $.post("<s:url action="eircAccountsListAjax" namespace="/eirc" includeParams="none"/>",
            {},
            function(data) {
                $("#" + resultId).html(data);
            });

    function personSearch() {
        FP.resizeShadow(shadowId, resultId, {visibility:"visible"});
        $.post("<s:url action="eircAccountsListAjax" namespace="/eirc" includeParams="none"/>",
                {personFio: $("#personFio").val()},
                function(data) {
                    $("#" + resultId).html(data);
                    FP.hideShadow(shadowId);
                });
    }

    function pagerAjax(element) {
        FP.pagerAjax(element, {
            action:"<s:url action="eircAccountsListAjax" namespace="/eirc" includeParams="none"/>",
            params:{
                apartmentId: FF.filters["apartment"].value.val(),
                personFio: $("#personFio").val() == "" ? null : $("#personFio").val()
            }
        });
    }

</script>
