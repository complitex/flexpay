<%@ page contentType="text/html;charset=UTF-8" pageEncoding="utf-8" language="java" %>
<%@include file="/WEB-INF/jsp/common/taglibs.jsp"%>

<s:actionerror />

<s:form id="fObjects" method="post">
    <table cellpadding="3" cellspacing="1" border="0" width="100%">

        <tr>
            <td colspan="8">
                <%@include file="/WEB-INF/jsp/ab/filters/pager.jsp" %>
            </td>
        <tr>
        <tr>
            <td class="th">
                &nbsp;
            </td>
            <td class="th">
                &nbsp;
            </td>
            <td class="th">
                <s:text name="tc.rules_file.name" />
            </td>
            <td class="th">
                <s:text name="tc.rules_file.file_name" />
            </td>
            <td class="th">
                <s:text name="tc.rules_file.creation_date" />
            </td>
            <td class="th">
                <s:text name="tc.rules_file.user_name" />
            </td>
            <td class="th">
                <s:text name="tc.rules_file.description" />
            </td>
            <td class="th">&nbsp;</td>
        </tr>
    <s:iterator value="rulesFiles" status="status">
        <tr valign="middle" class="cols_1">
            <td class="col_1s">
                <s:property value="%{#status.index + pager.thisPageFirstElementNumber + 1}" />
            </td>
            <td class="col">
                <input type="radio" value="<s:property value="%{id}"/>" name="id" />
            </td>
            <td class="col">
                <s:property value="%{getTranslation(translations).name}" />
            </td>
            <td class="col">
                <a href="<s:url value='/rulesFileDownloadServlet'><s:param name="rulesFileId" value="%{id}"/></s:url>">
                    <s:property value="file.originalName"/>
                </a>
            </td>
            <td class="col">
                <s:date name="creationDate" format="dd.MM.yyyy hh:mm:ss" />
            </td>
            <td class="col">
                <s:property value="userName"/>
            </td>
            <td class="col">
                <s:property value="%{getTranslation(translations).description}" />
            </td>
            <td class="col">
                <a href="<s:url action="rulesFileEdit"><s:param name="rulesFile.id" value="%{id}" /></s:url>"><s:text name="common.edit"/></a>
            </td>
        </tr>
    </s:iterator>

        <tr>
            <td colspan="8">
                <%@include file="/WEB-INF/jsp/ab/filters/pager.jsp" %>
                <input type="button" class="btn-exit" onclick="location.href='<s:url action="rulesFileEdit"><s:param name="rulesFile.id" value="0" /></s:url>';" value="<s:text name="common.new"/>" />
                <input type="button" class="btn-exit" onclick="doAction('<s:url action="rulesFileDelete" includeParams="false" />');" value="<s:text name="common.delete_selected"/>" />
                <input type="button" class="btn-exit" onclick="calcDateWindow();" value="<s:text name="tc.calculate"/>" />
            </td>
        </tr>

    </table>

</s:form>

<script type="text/javascript">

    var x = 0;
    var y = 0;

    function getXY(e) {
        var left = document.documentElement.scrollLeft;
        var top = document.documentElement.scrollTop;
        if (Prototype.Browser.IE) {
            x = event.clientX + left;
            y = event.clientY + top;
        } else {
            x = e.clientX + left;
            y = e.clientY + top;
        }
    }

    document.onmousemove = getXY;

	function calcDateWindow() {

        var fileId = FP.getCheckedValue($("fObjects").elements["id"]);

        if (fileId == null) {
            alert("<s:text name="tc.error.calculate.element_not_selected" />");
            return;
        }

		var win = new Window({
            className: "spread",
            title: "<s:text name="tc.calculate.window_title" />",
			url: "<s:url action="tariffCalculate" includeParams="false" />?id=" + fileId,
            width:250,
            height:250,
            resizable: false,
            minimizable: false,
            maximizable: false,
            destroyOnClose: true
        });

		win.showCenter(true, y, x);
	}
</script>

<script type="text/javascript">

    function doAction(action) {
        var form = $("fObjects");
        form.action = action;
        form.submit();
    }

</script>
