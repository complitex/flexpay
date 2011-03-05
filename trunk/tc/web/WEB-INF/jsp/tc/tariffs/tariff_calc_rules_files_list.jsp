<%@include file="/WEB-INF/jsp/common/taglibs.jsp"%>
<%@include file="/WEB-INF/jsp/common/includes/jquery_bgiframe.jsp"%>
<%@include file="/WEB-INF/jsp/common/includes/jquery_ui_core.jsp"%>
<%@include file="/WEB-INF/jsp/common/includes/jquery_ui_datepicker.jsp"%>
<%@include file="/WEB-INF/jsp/common/includes/errors_messages.jsp"%>

<form id="fObjects">

    <s:hidden name="id" />

    <table cellpadding="3" cellspacing="1" border="0" width="100%">
        <tr>
            <td colspan="8">
                <%@include file="/WEB-INF/jsp/common/filter/pager/pager_ajax.jsp"%>
                <input type="button" class="btn-exit"
                       onclick="window.location='<s:url action="rulesFileEdit" includeParams="none"><s:param name="rulesFile.id" value="0" /></s:url>';"
                       value="<s:text name="common.new" />" />
                <input type="button" class="btn-exit" value="<s:text name="common.delete_selected" />" onclick="deleteAjax();" />
                <input type="button" class="btn-exit" value="<s:text name="tc.calculate" />" onclick="calculate();" />
            </td>
        </tr>
        <tr>
            <td class="th">&nbsp;</td>
            <td class="th">
                <input type="checkbox" onchange="FP.setCheckboxes(this.checked, 'objectIds');">
            </td>
            <td class="th"><s:text name="tc.rules_file.name" /></td>
            <td class="th"><s:text name="tc.rules_file.file_name" /></td>
            <td class="th"><s:text name="tc.rules_file.creation_date" /></td>
            <td class="th"><s:text name="tc.rules_file.user_name" /></td>
            <td class="th"><s:text name="tc.rules_file.description" /></td>
            <td class="th">&nbsp;</td>
        </tr>
        <s:iterator value="rulesFiles" status="status">
            <tr valign="middle" class="cols_1">
                <td class="col_1s">
                    <s:property value="#status.index + pager.thisPageFirstElementNumber + 1" />
                </td>
                <td class="col">
                    <input type="checkbox" value="<s:property value="id" />" name="objectIds" />
                </td>
                <td class="col">
                    <a href="<s:url action="rulesFileView"><s:param name="rulesFile.id" value="id" /></s:url>">
                        <s:property value="getTranslationName(translations)" />
                    </a>
                </td>
                <td class="col">
                    <a href="<s:url value="/download/%{file.id}" includeParams="none" />"><s:property value="file.originalName" /></a>
                </td>
                <td class="col"><s:date name="creationDate" format="dd.MM.yyyy hh:mm:ss" /></td>
                <td class="col"><s:property value="userName" /></td>
                <td class="col"><s:property value="getTranslation(translations).description" /></td>
                <td class="col">
                    <a href="<s:url action="rulesFileEdit" includeParams="none"><s:param name="rulesFile.id" value="id" /></s:url>"><s:text name="common.edit" /></a>
                </td>
            </tr>
        </s:iterator>
        <tr>
            <td colspan="8">
                <%@include file="/WEB-INF/jsp/common/filter/pager/pager_ajax.jsp"%>
                <input type="button" class="btn-exit"
                       onclick="window.location='<s:url action="rulesFileEdit" includeParams="none"><s:param name="rulesFile.id" value="0" /></s:url>';"
                       value="<s:text name="common.new" />" />
                <input type="button" class="btn-exit" value="<s:text name="common.delete_selected" />" onclick="deleteAjax();" />
                <input type="button" class="btn-exit" value="<s:text name="tc.calculate" />" onclick="calculate();" />
            </td>
        </tr>
    </table>

    <div id="dialog" style="display:none;">
        <s:hidden name="calcDate" />
        <div id="calendar"></div>
    </div>

</form>

<script type="text/javascript">

    function createDialog() {

        $("#calendar").datepicker("dialog", "" , function(dateText) {
            $("input[name='calcDate']").get(0).value = dateText;
            $("#fObjects").attr("action", "<s:url action="tariffCalculate" includeParams="none" />").submit();
        },
        {
            dateFormat: "yy/mm/dd",
            showButtonPanel: true,
            changeMonth: true,
            changeYear: true
        });
    }

    function calculate() {
        var objectIds = $("input[name='objectIds']:checked");
        if (objectIds.length > 1) {
            alert("<s:text name="tc.error.calculate.must_be_one_element_selected" />");
        } else if (objectIds.length == 0) {
        } else {
            $("input[name='id']").get(0).value = objectIds.get(0).value;
            createDialog();
        }
    }

</script>
