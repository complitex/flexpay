<%@include file="/WEB-INF/jsp/common/taglibs.jsp" %>

<s:form id="fobjects" action="streetsListAjax" namespace="/dicts">

    <table cellpadding="3" cellspacing="1" border="0" width="100%">
        <tr>
            <td>
                <%@include file="filters/groups/country_region_town_streetname_ajax.jsp" %>
            </td>
        </tr>
        <tr>
            <td id="result">
                <input type="submit" class="btn-exit"
                       onclick="$('#fobjects').attr('action','<s:url action="streetDelete" includeParams="none" />');"
                       value="<s:text name="common.delete_selected"/>"/>
                <input type="button" class="btn-exit"
                       onclick="window.location='<s:url action="streetEdit" includeParams="none"><s:param name="street.id" value="0"/></s:url>';"
                       value="<s:text name="common.new"/>"/>
            </td>
        </tr>
    </table>

</s:form>

<script type="text/javascript">
    $(function() {
        FF.addListener("town", function(filter) {
            var id = "result";
            FF.loading(id);
            $.post("<s:url action="streetsListAjax" namespace="/dicts" includeParams="none"/>",
                    {townId: filter.value.val()},
                    function(data) {
                        $("#" + id).html(data);
                    });
        });

        FF.addEraseFunction("town", function(filter) {
            $("#result").html('<input type="submit" class="btn-exit" '
                    + 'onclick="$(\'#fobjects\').attr(\'action\',\'<s:url action="streetDelete" includeParams="none" />\');" '
                    + 'value="<s:text name="common.delete_selected"/>"/>\n'
                    + '<input type="button" class="btn-exit" '
                    + 'onclick="window.location = \'<s:url action="streetEdit"><s:param name="street.id" value="0"/></s:url>\'" '
                    + 'value="<s:text name="common.new"/>"/>');
        });

        FF.addListener("street", function(filter) {
            var id = "result";
            FF.loading(id);
            $.post("<s:url action="streetsListAjax" namespace="/dicts" includeParams="none"/>",
                    {streetId: filter.value.val()},
                    function(data) {
                        $("#" + id).html(data);
                    });
        });

    });

    function pagerAjax(element) {
        var isSelect = element.name == "pager.pageSize";
        $.post("<s:url action="streetsListAjax" namespace="/dicts" includeParams="none"/>",
                {
                    townId: FF.filters["town"].value.val(),
                    "streetSorterByName.active": $("#streetSorterByNameActive").val(),
                    "streetSorterByName.order": $("#streetSorterByNameOrder").val(),
                    "streetSorterByType.active": $("#streetSorterByTypeActive").val(),
                    "streetSorterByType.order": $("#streetSorterByTypeOrder").val(),
                    pageSizeChanged: isSelect,
                    "pager.pageNumber": isSelect ? "" : element.value,
                    "pager.pageSize": isSelect ? element.value : $('select[name="pager.pageSize"]').val()
                },
                function(data) {
                    $("#result").html(data);
                });
    }

    function sorterAjax() {
        $.post("<s:url action="streetsListAjax" namespace="/dicts" includeParams="none"/>",
                {
                    townId: FF.filters["town"].value.val(),
                    "streetSorterByName.active": $("#streetSorterByNameActive").val(),
                    "streetSorterByName.order": $("#streetSorterByNameOrder").val(),
                    "streetSorterByType.active": $("#streetSorterByTypeActive").val(),
                    "streetSorterByType.order": $("#streetSorterByTypeOrder").val()
                },
                function(data) {
                    $("#result").html(data);
                });
    }

</script>
