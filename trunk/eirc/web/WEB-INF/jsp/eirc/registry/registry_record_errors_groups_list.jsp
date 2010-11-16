<%@include file="/WEB-INF/jsp/common/taglibs.jsp"%>
<%@include file="/WEB-INF/jsp/common/includes/errors_messages.jsp"%>

<script type="text/javascript">
    FP.switchSorter(["recordErrorsGroupSorterByNameButton", "recordErrorsGroupSorterByNumberOfErrorsButton"]);
    FPR.openedGroup = -1;
    FPR.groups = [];
</script>

<table cellpadding="3" cellspacing="1" border="0" width="100%">
    <tr>
        <td colspan="4">
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
        </tr>
        <tr id="panelGroupTr<s:property value="#status.index" />" class="panelGroupTr" style="display:none;">
            <td colspan="4">
                <div id="panelGroup<s:property value="#status.index" />" class="panelGroup" style="display:none;"></div>
            </td>
        </tr>
    </s:iterator>
    <tr>
        <td colspan="4">
            <%@include file="/WEB-INF/jsp/common/filter/pager/pager_ajax.jsp"%>
        </td>
    </tr>
</table>

<script type="text/javascript">

    FPR.addGroups([
        <s:iterator value="errorGroups" status="status">
            [
                <s:property value="group.townName != null ? '\"' + group.townName + '\",' : 'null,'" escape="false" />
                <s:property value="group.streetType != null ? '\"' + group.streetType + '\",' : 'null,'" escape="false" />
                <s:property value="group.streetName != null ? '\"' + group.streetName + '\",' : 'null,'" escape="false" />
                <s:property value="group.buildingNumber != null ? '\"' + group.buildingNumber + '\",' : 'null,'" escape="false" />
                <s:property value="group.buildingBulk != null ? '\"' + group.buildingBulk + '\",' : 'null,'" escape="false" />
                <s:property value="group.apartmentNumber != null ? '\"' + group.apartmentNumber + '\",' : 'null,'" escape="false" />
                <s:property value="group.lastName != null ? '\"' + group.lastName + '\",' : 'null,'" escape="false" />
                <s:property value="group.middleName != null ? '\"' + group.middleName + '\",' : 'null,'" escape="false" />
                <s:property value="group.firstName != null ? '\"' + group.firstName + '\",' : 'null,'" escape="false" />
                <s:property value="group.numberOfRecords != null ? group.numberOfRecords : 0" />
            ]<s:property value="#status.index < errorGroups.size() - 1 ? ',' : ''" />
        </s:iterator>
    ]);

    $(function() {

        for (var i = 0; i < <s:property value="errorGroups.size()" />; i++) {
            createGroupCollapser(i);
        }

   });

</script>
