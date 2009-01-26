<%@ page contentType="text/html;charset=UTF-8" pageEncoding="utf-8" language="java" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@include file="/WEB-INF/jsp/common/taglibs.jsp"%>

<s:form id="fObjects" method="post">
    <table cellpadding="3" cellspacing="1" border="0" width="100%">

    <tr>
        <td colspan="10">
            <%@include file="/WEB-INF/jsp/ab/filters/pager.jsp" %>
        </td>
    <tr>
    <tr>
        <td class="th">
            <input type="checkbox" onclick="FP.setCheckboxes(this.checked, 'szFileIds');" />
        </td>
        <td class="th">
            &nbsp;
        </td>
        <td class="th">
            <s:text name="sz.oszn" />
        </td>
        <td class="th">
            <s:text name="sz.input_file" />
        </td>
        <td class="th">
            <s:text name="sz.output_file" />
        </td>
        <td class="th">
            <s:text name="sz.file_type" />
        </td>
        <td class="th">
            <s:text name="sz.month" />
        </td>
        <td class="th">
            <s:text name="sz.import_date" />
        </td>
        <td class="th">
            <s:text name="sz.file_status" />
        </td>
        <!-- <td class="th">
            <s:text name="sz.user_name" />
        </td> -->
        <td class="th">
            &nbsp;
        </td>
    </tr>
        <s:iterator value="szFileWrapperList" status="status">
            <tr valign="middle" class="cols_1">
                <td class="col_1s">
                    <input type="checkbox" value="<s:property value="szFile.id" />" name="szFileIds" />
                </td>
                <td class="col_1s">
                    <s:property value="%{#status.index + pager.thisPageFirstElementNumber + 1}" />
                </td>
                <td class="col">
                    <s:property value="szFile.oszn.description"/>
                </td>
                <td class="col">
                    <a href="<s:url value='/szFileDownloadServlet'><s:param name="szFileId" value="%{szFile.id}"/><s:param name="req" value="true" /></s:url>">
                        <s:property value="szFile.uploadedFile.originalName"/>
                    </a>
                </td>
                <td class="col">
                    <s:if test="szFile.fileToDownload">
                        <a href="<s:url value='/szFileDownloadServlet'><s:param name="szFileId" value="%{szFile.id}"/></s:url>">
                            <s:property value="szFile.fileToDownload.originalName"/>
                        </a>
                    </s:if>
                </td>
                <td class="col">
                    <s:text name="%{szFile.type.name}" />
                </td>
                <td class="col">
                    <s:property value="fileMonth" />&nbsp;<s:property value="szFile.fileYear"/>
                </td>
                <td class="col">
                    <s:date name="szFile.importDate" format="dd.MM.yyyy hh:mm:ss" />
                </td>
                <td class="col">
                    <s:text name="%{szFile.status.name}"/>
                </td>
                <!-- <td class="col">
                    <s:property value="userName"/>
                </td> -->
                <td class="col">
                    <s:if test="loadedToDb">
                        &nbsp;
                        <a href="javascript:void(0);" onclick="doActionId('deleteFromDB', <s:property value="szFile.id" />);">
                            <s:text name="sz.file_list.action.delete_from_db" />
                        </a>
                        &nbsp;
                        <a href="javascript:void(0);" onclick="doActionId('loadFromDB', <s:property value="szFile.id" />);">
                            <s:text name="sz.file_list.action.load_from_db" />
                        </a>
                    </s:if>
                </td>
            </tr>
        </s:iterator>

    <tr>
        <td colspan="10" height="3" bgcolor="#4a4f4f"/>
    <tr>

    <tr>
        <td colspan="6">
            <input type="button" class="btn-exit" onclick="doAction('loadToDB');" value="<s:text name="sz.file_list.action.load_to_db" />" />
            &nbsp;
            <input type="button" class="btn-exit" onclick="doAction('fullDelete');" value="<s:text name="sz.file_list.action.full_delete" />" />
            &nbsp;
            <input type="button" class="btn-exit" onclick="location.reload();" value="<s:text name="sz.file_list.refresh_list" />" />
            <input id="action1" type="hidden" name="action1" value="loadToDB" />
            <input id="szFileId" type="hidden" name="szFileId" value="" />
        </td>
        <td colspan="4">
            <%@include file="/WEB-INF/jsp/ab/filters/pager.jsp" %>
        </td>
    <tr>

    </table>
</s:form>

<script type="text/javascript">

    function doAction(action) {
        $('action1').value = action;
        var form = $('fObjects');
        form.action = "<s:url action="/szFileOperation" />";
        form.submit();
    }

    function doActionId(action, id) {
        $('szFileId').value = id;
        doAction(action);
    }

</script>
