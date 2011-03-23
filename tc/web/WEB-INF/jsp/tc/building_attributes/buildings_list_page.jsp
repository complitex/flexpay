<%@include file="/WEB-INF/jsp/common/taglibs.jsp"%>

<table cellpadding="3" cellspacing="1" border="0" width="100%">
    <tr>
        <td>
            <%@include file="/WEB-INF/jsp/ab/filters/groups/country_region_town_street_ajax.jsp"%>
        </td>
    </tr>
    <tr>
        <td id="result"></td>
    </tr>
</table>

<script type="text/javascript">

    var resultId = "result";

    $(function() {

        FF.addListener("street", function() {
            pagerAjax();
        });
        FF.addEraser("street", function() {
            $("#" + resultId).html("");
        });
    });

    function pagerAjax(element) {
        FP.pagerAjax(element, {
            action:"<s:url action="buildingsListAjax" namespace="/tc"/>",
            params:{streetFilter: FF.filters["street"].value.val()}
        });
    }

</script>
