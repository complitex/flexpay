<%@include file="/WEB-INF/jsp/common/taglibs.jsp"%>

<table cellpadding="3" cellspacing="1" border="0" width="100%">
    <tr>
        <td>
            <%@include file="/WEB-INF/jsp/ab/filters/groups/country_region_town_ajax.jsp"%>
            <table width="100%">
                <tr>
                    <td class="filter"><s:text name="ab.street" /></td>
                    <td colspan="5">
                        <input type="text" name="streetFilter.searchString" class="form-textfield"
                               value="<s:property value="streetFilter.searchString" />" />
                        <input type="button" onclick="pagerAjax();" value="<s:text name="common.search" />" />
                    </td>
                </tr>
            </table>
        </td>
    </tr>
    <tr>
        <td id="result">
            <input type="button" class="btn-exit"
                   onclick="window.location='<s:url action="streetEdit" includeParams="none"><s:param name="street.id" value="0" /></s:url>';"
                   value="<s:text name="common.new" />" />
        </td>
    </tr>
</table>

<script type="text/javascript">

    var resultId = "result";

    $(function() {

        FF.addListener("town", function(filter) {
            FP.pagerAjax(null, {
                action:"<s:url action="streetsListAjax" namespace="/dicts" includeParams="none" />",
                params:{townFilter: filter.value.val()}
            });
        });

        FF.addEraser("town", function() {
            $("#" + resultId).html('<input type="button" class="btn-exit" '
                    + 'onclick="window.location = \'<s:url action="streetEdit" includeParams="none"><s:param name="street.id" value="0" /></s:url>\'" '
                    + 'value="<s:text name="common.new" />" />');
        });

    });

    function pagerAjax(element) {
        FP.pagerAjax(element, {
            action:"<s:url action="streetsListAjax" namespace="/dicts" includeParams="none" />",
            params:{
                townFilter: FF.filters["town"].value.val(),
                "streetFilter.searchString": $("input[name='streetFilter.searchString']").get(0).value,
                "streetSorterByName.active": $("#streetSorterByNameActive").val(),
                "streetSorterByName.order": $("#streetSorterByNameOrder").val(),
                "streetSorterByType.active": $("#streetSorterByTypeActive").val(),
                "streetSorterByType.order": $("#streetSorterByTypeOrder").val()
            }
        });
    }

    function sorterAjax() {
        pagerAjax();
    }

    function deleteAjax() {
        FP.serviceElements("<s:url action="streetDelete" namespace="/dicts" includeParams="none" />", "objectIds", pagerAjax);
    }

</script>
