<%@include file="/WEB-INF/jsp/common/taglibs.jsp"%>

<table cellpadding="3" cellspacing="1" border="0" width="100%">
    <tr>
        <td colspan="10">
            <%@include file="/WEB-INF/jsp/common/filter/pager/pager_ajax.jsp"%>
        </td>
    </tr>
    <tr>
        <td colspan="10">
            <input type="button" class="btn-exit" onclick="doAction('loadToDB');" value="<s:text name="sz.file_list.action.load_to_db" />" />
            &nbsp;
            <input type="button" class="btn-exit" onclick="doAction('fullDelete');" value="<s:text name="sz.file_list.action.full_delete" />" />
            &nbsp;
            <input type="button" class="btn-exit" onclick="doAction('loadFromDB');" value="<s:text name="sz.file_list.action.load_from_db" />" />
            &nbsp;
            <input type="button" class="btn-exit" onclick="doAction('deleteFromDB');" value="<s:text name="sz.file_list.action.delete_from_db" />" />
            &nbsp;
            <input type="button" class="btn-exit" onclick="pagerAjax();" value="<s:text name="sz.file_list.refresh_list" />" />
        </td>
    </tr>
    <tr>
        <td class="th">&nbsp;</td>
        <td class="th">
            <input type="checkbox" onchange="FP.setCheckboxes(this.checked, 'objectIds');" />
        </td>
        <td class="th"><s:text name="sz.oszn" /></td>
        <td class="th"><s:text name="sz.input_file" /></td>
        <td class="th"><s:text name="sz.output_file" /></td>
        <td class="th"><s:text name="sz.file_type" /></td>
        <td class="th"><s:text name="sz.month" /></td>
        <td class="th"><s:text name="sz.import_date" /></td>
        <td class="th"><s:text name="sz.file_status" /></td>
        <td class="th"><s:text name="sz.user_name" /></td>
    </tr>
    <s:iterator value="szFiles" status="status">
        <tr valign="middle" class="cols_1">
            <td class="col_1s" width="1%">
                <s:property value="#status.index + pager.thisPageFirstElementNumber + 1" />
            </td>
            <td class="col_1s" width="1%">
                <input type="checkbox" name="objectIds" value="<s:property value="id" />" />
            </td>
            <td class="col"><s:property value="oszn.description" /></td>
            <td class="col">
                <a href="<s:url value="/download/" includeParams="none"><s:param name="id" value="%{uploadedFile.id}" /></s:url>">
                    <s:property value="uploadedFile.originalName" />
                </a>
            </td>
            <td class="col">
                <s:if test="fileToDownload">
                    <a href="<s:url value="/download/" includeParams="none"><s:param name="id" value="%{fileToDownload.id}" /></s:url>">
                        <s:property value="fileToDownload.originalName" />
                    </a>
                </s:if>
            </td>
            <td class="col"><s:text name="%{type.name}" /></td>
            <td class="col"><s:property value="format('MMMMM yyyy', userPreferences.locale)" /></td>
            <td class="col"><s:date name="importDate" format="dd.MM.yyyy hh:mm:ss" /></td>
            <td class="col"><s:text name="%{status.name}" /></td>
            <td class="col"><s:property value="userName" /></td>
        </tr>
    </s:iterator>
    <tr>
        <td colspan="10">
            <input type="button" class="btn-exit" onclick="doAction('loadToDB');" value="<s:text name="sz.file_list.action.load_to_db" />" />
            &nbsp;
            <input type="button" class="btn-exit" onclick="doAction('fullDelete');" value="<s:text name="sz.file_list.action.full_delete" />" />
            &nbsp;
            <input type="button" class="btn-exit" onclick="doAction('loadFromDB');" value="<s:text name="sz.file_list.action.load_from_db" />" />
            &nbsp;
            <input type="button" class="btn-exit" onclick="doAction('deleteFromDB');" value="<s:text name="sz.file_list.action.delete_from_db" />" />
            &nbsp;
            <input type="button" class="btn-exit" onclick="pagerAjax();" value="<s:text name="sz.file_list.refresh_list" />" />
        </td>
    </tr>
    <tr>
        <td colspan="10">
            <%@include file="/WEB-INF/jsp/common/filter/pager/pager_ajax.jsp"%>
        </td>
    </tr>
</table>

<script type="text/javascript">

    function doAction(action) {
        var objectIds = [];
        $("input[name=objectIds]:checked").each(function() {
            objectIds.push(this.value);
        });
        if (objectIds.length == 0) {
            return;
        }
        $.post("<s:url action="szFileOperation" includeParams="none" />",
                {
                    action1:action,
                    objectIds:objectIds
                },
                function(data, status) {
                    if (data == "" && status == "success") {
                        window.location.href = FP.base;
                    }
                    $("#startProcessResponse").html(data);
                });
    }

</script>
