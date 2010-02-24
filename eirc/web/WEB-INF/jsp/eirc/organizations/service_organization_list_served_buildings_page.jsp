<%@include file="/WEB-INF/jsp/common/taglibs.jsp"%>
<%@include file="/WEB-INF/jsp/common/includes/errors_messages.jsp"%>

<table cellpadding="3" cellspacing="1" border="0" width="100%">
<%--
    <tr>
        <td>
            <%@include file="/WEB-INF/jsp/ab/filters/groups/country_region_town_street_ajax.jsp"%>
        </td>
    </tr>
--%>
    <tr>
        <td id="result">
<%--
            <input type="button" class="btn-exit"
                   onclick="window.location='<s:url action="serviceOrganizationAddServedBuildingPage" includeParams="none"><s:param name="serviceOrganization.id" value="serviceOrganization.id" /></s:url>'"
                   value="<s:text name="eirc.add_served_buildings" />" />
--%>
        </td>
    </tr>
</table>

<script type="text/javascript">

    $(function() {

        pagerAjax();

<%--
        FF.addListener("street", function(filter) {
            FP.pagerAjax(null, {
                action:"<s:url action="serviceOrganizationListServedBuildingsAjax" includeParams="none" />",
                params: {
                    streetFilter: filter.value.val(),
                    "serviceOrganization.id": <s:property value="serviceOrganization.id" />
                }
            });
        });
--%>

    });

    function pagerAjax(element) {
        FP.pagerAjax(element, {
            action:"<s:url action="serviceOrganizationListServedBuildingsAjax" includeParams="none" />",
            params:{
//                streetFilter: FF.filters["street"].value.val(),
                "serviceOrganization.id": <s:property value="serviceOrganization.id" />
            }
        });
    }

    function deleteAjax() {
        FP.serviceElements("<s:url action="serviceOrganizationRemoveServedBuildings" includeParams="none" />", "objectIds", pagerAjax);
    }

</script>
