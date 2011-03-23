<%@include file="/WEB-INF/jsp/common/taglibs.jsp"%>
<%@include file="/WEB-INF/jsp/common/includes/errors_messages.jsp"%>

<table cellpadding="3" cellspacing="1" border="0" width="100%">
    <tr>
        <td>
            <%@include file="/WEB-INF/jsp/ab/filters/groups/country_region_town_street_ajax.jsp"%>
        </td>
    </tr>
    <tr>
        <td id="result">
            <input type="button" class="btn-exit"
                   onclick="window.location='<s:url action="serviceOrganizationListServedBuildings"><s:param name="serviceOrganization.id" value="serviceOrganization.id" /></s:url>'"
                   value="<s:text name="common.cancel" />" />
        </td>
    </tr>
</table>

<script type="text/javascript">

    var resultId = "result";

    $(function() {

        FF.addListener("street", function(filter) {
            FP.pagerAjax(null, {
                action:"<s:url action="buildingsListAjax" namespace="/eirc" />",
                params: {
                    streetFilter: filter.value.val(),
                    "serviceOrganization.id": <s:property value="serviceOrganization.id" />
                }
            });
        });
        FF.addEraser("street", function() {
            $("#" + resultId).html('<input type="button" class="btn-exit"'
                   + 'onclick="window.location=\'<s:url action="serviceOrganizationListServedBuildings"><s:param name="serviceOrganization.id" value="serviceOrganization.id" /></s:url>\'"'
                   + 'value="<s:text name="common.cancel" />" />');
        });
    });

    function pagerAjax(element) {
        FP.pagerAjax(element, {
            action:"<s:url action="buildingsListAjax" namespace="/eirc" />",
            params: {
                streetFilter: FF.filters["street"].value.val(),
                "serviceOrganization.id": <s:property value="serviceOrganization.id" />
            }
        });
    }

    function addBuildings() {
        FP.serviceElements("<s:url action="serviceOrganizationAddServedBuildingsAjax" namespace="/eirc" />", "objectIds", pagerAjax,
                            {params:{"serviceOrganization.id": <s:property value="serviceOrganization.id" />}});
    }

    function backF() {
        window.location="<s:url action="serviceOrganizationListServedBuildings"><s:param name="serviceOrganization.id" value="serviceOrganization.id" /></s:url>";
    }

</script>
