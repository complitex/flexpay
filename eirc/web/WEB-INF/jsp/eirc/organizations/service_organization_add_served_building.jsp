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

    var resultId = "result";

    $(function() {

        FF.addListener("street", function(filter) {
            FP.pagerAjax(null, {
                action:"<s:url action="serviceOrganizationListServedBuildingsAjax" namespace="/eirc" includeParams="none" />",
                params:{streetId: filter.value.val()}
            });
        });
        FF.addEraser("street", function() {
            $("#" + resultId).html('<input type="button" class="btn-exit"'
                   + 'onclick="window.location=\'<s:url action="serviceOrganizationListServedBuildings" includeParams="none"><s:param name="serviceOrganization.id" value="%{id}" /></s:url>\'"'
                   + 'value="<s:text name="common.cancel"/>"/>');
        });
    });

    function pagerAjax(element) {
        FP.pagerAjax(element, {
            action:"<s:url action="serviceOrganizationListServedBuildingsAjax" namespace="/eirc" includeParams="none" />",
            params:{streetId: FF.filters["street"].value.val()}
        });
    }

</script>
