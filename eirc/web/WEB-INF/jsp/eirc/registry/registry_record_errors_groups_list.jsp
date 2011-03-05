<%@include file="/WEB-INF/jsp/common/taglibs.jsp"%>
<%@include file="/WEB-INF/jsp/common/includes/errors_messages.jsp"%>

<input id="selectedGroupIndex" type="hidden" />

<table cellpadding="3" cellspacing="1" border="0" width="100%">
    <tr>
        <td colspan="5">
            <%@include file="/WEB-INF/jsp/common/filter/pager/pager_ajax.jsp"%>
        </td>
    </tr>
    <tr>
        <td class="th">&nbsp;</td>
        <td class="<s:property value="recordErrorsGroupSorterByName.activated ? 'th_s' : 'th'" />" nowrap>
            <%@include file="/WEB-INF/jsp/eirc/sorters/record_errors_group_sort_by_name_header.jsp"%>
        </td>
        <td class="<s:property value="recordErrorsGroupSorterByNumberOfErrors.activated ? 'th_s' : 'th'" />" nowrap>
            <%@include file="/WEB-INF/jsp/eirc/sorters/record_errors_group_sort_by_number_of_errors_header.jsp"%>
        </td>
        <td class="th"><s:text name="eirc.correspondence" /></td>
        <td class="th">&nbsp;</td>
    </tr>
    <s:iterator value="errorGroups" status="status">
        <tr class="cols_1">
            <td class="col" width="1%" align="right">
                <s:property value="#status.index + pager.thisPageFirstElementNumber + 1" />
            </td>
            <td id="group<s:property value="#status.index" />" class="col group">
                <s:property value="name" />
            </td>
            <td class="col">
                <s:property value="group.numberOfRecords" />
            </td>
            <td class="col">
                <a href="javascript:void(0);" onclick="createDialogForGroup(<s:property value="#status.index" />);"><s:text name="common.edit" /></a>
            </td>
            <td class="col">
                <a href="javascript:void(0);" onclick="createItem(<s:property value="#status.index" /><s:if test="checkStreetTypeType(#status.index)">, 'st'</s:if><s:elseif test="checkBuildingType(#status.index)">, 'b'</s:elseif>);"><s:text name="common.add" /></a>
            </td>
        </tr>
        <tr id="panelGroupTr<s:property value="#status.index" />" class="panelGroupTr" style="display:none;">
            <td colspan="5">
                <div id="panelGroup<s:property value="#status.index" />" class="panelGroup" style="display:none;"></div>
            </td>
        </tr>
    </s:iterator>
    <tr>
        <td colspan="5">
            <%@include file="/WEB-INF/jsp/common/filter/pager/pager_ajax.jsp"%>
        </td>
    </tr>
</table>

<div id="streetTypeModal" style="display:none;">
    <%@include file="/WEB-INF/jsp/eirc/registry/modal/street_type_modal.jsp"%>
</div>

<div id="buildingModal" style="display:none;">
    <%@include file="/WEB-INF/jsp/eirc/registry/modal/building_modal.jsp"%>
</div>

<script type="text/javascript">

    $(function() {

        FP.switchSorter(["recordErrorsGroupSorterByNameButton", "recordErrorsGroupSorterByNumberOfErrorsButton"]);

        FPR.openedGroup = -1;
        FPR.groups = [];

        for (var i = 0; i < <s:property value="errorGroups.size()" />; i++) {
            createGroupCollapser(i);
        }

        FPR.addGroups([
            <s:iterator value="errorGroups" status="status">
                [
                    <s:property value="townName != null ? '\"' + townName + '\",' : 'null,'" escape="false" />
                    <s:property value="streetType != null ? '\"' + streetType + '\",' : 'null,'" escape="false" />
                    <s:property value="streetName != null ? '\"' + streetName + '\",' : 'null,'" escape="false" />
                    <s:property value="buildingNumber != null ? '\"' + buildingNumber + '\",' : 'null,'" escape="false" />
                    <s:property value="buildingBulk != null ? '\"' + buildingBulk + '\",' : 'null,'" escape="false" />
                    <s:property value="apartmentNumber != null ? '\"' + apartmentNumber + '\",' : 'null,'" escape="false" />
                    <s:property value="lastName != null ? '\"' + lastName + '\",' : 'null,'" escape="false" />
                    <s:property value="middleName != null ? '\"' + middleName + '\",' : 'null,'" escape="false" />
                    <s:property value="firstName != null ? '\"' + firstName + '\",' : 'null,'" escape="false" />
                    <s:property value="group.numberOfRecords != null ? group.numberOfRecords : 0" />
                ]<s:property value="#status.index < errorGroups.size() - 1 ? ',' : ''" />
            </s:iterator>
        ]);

    });

</script>
