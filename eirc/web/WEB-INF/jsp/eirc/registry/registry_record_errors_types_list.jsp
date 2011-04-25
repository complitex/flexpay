<%@include file="/WEB-INF/jsp/common/taglibs.jsp"%>
<%@include file="/WEB-INF/jsp/common/includes/errors_messages.jsp"%>

<script type="text/javascript">
    FPR.openedType = -1;
    FPR.openedGroup = -1;
    FPR.groups = [];
</script>

<table cellpadding="3" cellspacing="1" border="0" width="100%">
    <tr>
        <td class="th"><s:text name="common.import.error_type.all" /></td>
    </tr>
    <s:iterator value="errorsTypes" status="status">
        <tr class="cols_1">
            <td id="type<s:property value="#status.index" />" class="col type">
                <s:text name="%{i18nName}" /> (<s:property value="numberOfRecords" />)
            </td>
        </tr>
        <tr id="panelTypeTr<s:property value="#status.index" />" class="panelTypeTr" style="display:none;">
            <td colspan="4">
                <div id="panelType<s:property value="#status.index" />" class="panelType" style="display:none;"></div>
                <input id="et<s:property value="#status.index" />" type="hidden" name="et<s:property value="#status.index" />" value="<s:property value="errorType" />" />
            </td>
        </tr>
    </s:iterator>
</table>

<script type="text/javascript">

    $(function() {

        for (var i = 0; i < <s:property value="errorsTypes.size()" />; i++) {
            createTypeCollapser(i);
        }

   });

</script>
