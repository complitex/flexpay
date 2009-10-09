<%@include file="/WEB-INF/jsp/common/taglibs.jsp" %>

<s:actionerror />

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
                       value="<s:text name="common.cancel"/>" />
            </td>
		</tr>
    </table>
</s:form>

<script type="text/javascript">

    var shadowId = "shadow";
    var resultId = "result";

    $(function() {

        FP.createShadow(shadowId);

        FF.addListener("street", function(filter) {
            FP.resizeShadow(shadowId, resultId, {visibility:"visible"});
            $.post("<s:url action="serviceOrganizationListServedBuildingsAjax" namespace="/eirc" includeParams="none"/>",
                    {streetId: filter.value.val()},
                    function(data) {
                        $("#" + resultId).html(data);
                        FP.hideShadow(shadowId);
                    });
        });
        FF.addEraser("street", function() {
            $("#" + resultId).html('<input type="button" class="btn-exit"'
                   + 'onclick="window.location=\'<s:url action="serviceOrganizationListServedBuildings"><s:param name="serviceOrganization.id" value="%{id}" /></s:url>\'"'
                   + 'value="<s:text name="common.cancel"/>"/>');
        });
    });

    function pagerAjax(element) {
        FP.pagerAjax(element, {
            action:"<s:url action="serviceOrganizationListServedBuildingsAjax" namespace="/eirc" includeParams="none"/>",
            params:{streetId: FF.filters["street"].value.val()}
        });
    }

</script>
