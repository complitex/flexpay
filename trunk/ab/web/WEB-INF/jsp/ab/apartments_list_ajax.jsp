<%@include file="/WEB-INF/jsp/common/taglibs.jsp" %>

<s:actionerror/>

<s:form id="fobjects">

    <table cellpadding="3" cellspacing="1" border="0" width="100%">
        <tr>
            <td>
                <%@ include file="filters/groups/country_region_town_streetname_building.jsp" %>
            </td>
        </tr>
        <tr>
            <td id="result">
            </td>
        </tr>
    </table>

</s:form>
