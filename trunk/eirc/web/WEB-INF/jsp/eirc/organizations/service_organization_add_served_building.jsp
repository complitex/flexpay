<%@ include file="/WEB-INF/jsp/common/taglibs.jsp" %>

<s:actionerror/>

<s:form action="serviceOrganizationAddServedBuilding">

    <s:hidden name="serviceOrganization.id" />

    <table cellpadding="3" cellspacing="1" border="0" width="100%">
        <tr>
            <td>
                <%@ include file="/WEB-INF/jsp/ab/filters/groups/country_region_town_street_ajax.jsp" %>
            </td>
        </tr>
        <tr>
            <td id="result">
                <input type="button" class="btn-exit"
                       onclick="window.location='<s:url action="serviceOrganizationsList"><s:param name="serviceOrganization.id" value="%{id}" /></s:url>'"
                       value="<s:text name="common.cancel"/>"/>
            </td>
		</tr>
    </table>
</s:form>

<script type="text/javascript">
    $(function() {
        FF.addListener("street", function(filter) {
            var id = "result";
            FF.loading(id);
            $.post("<s:url action="serviceOrganizationListServedBuildingsAjax" namespace="/eirc" includeParams="none"/>",
                    {streetId: filter.value.val()},
                    function(data) {
                        $("#" + id).html(data);
                    });
        });

        FF.addEraseFunction("street", function(filter) {
            $("#result").html('<input type="button" class="btn-exit"'
                   + 'onclick="window.location=\'<s:url action="serviceOrganizationListServedBuildings"><s:param name="serviceOrganization.id" value="%{id}" /></s:url>\'"'
                   + 'value="<s:text name="common.cancel"/>"/>');
        });
    });

    function pagerAjax(element) {
        var isSelect = element.name == "pager.pageSize";
        $.post("<s:url action="serviceOrganizationListServedBuildingsAjax" namespace="/eirc" includeParams="none"/>",
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
