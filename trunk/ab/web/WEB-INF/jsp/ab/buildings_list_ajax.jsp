<%@include file="/WEB-INF/jsp/common/taglibs.jsp" %>

<s:form id="fobjects" action="apartmentsListAjax" namespace="/dicts">

    <table cellpadding="3" cellspacing="1" border="0" width="100%">
        <tr>
            <td>
                <%@include file="filters/groups/country_region_town_streetname_ajax.jsp" %>
            </td>
        </tr>
        <tr>
            <td id="result">
                <input type="submit" class="btn-exit"
                       onclick="$('#fobjects').attr('action','<s:url action="buildingDelete" includeParams="none" />');"
                       value="<s:text name="common.delete_selected"/>"/>
                <input type="button" class="btn-exit"
                       onclick="window.location = '<s:url action="buildingCreate" includeParams="none" />'"
                       value="<s:text name="common.new"/>"/>
            </td>
        </tr>
    </table>

</s:form>
