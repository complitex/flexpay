
<%@include file="/WEB-INF/jsp/common/taglibs.jsp"%>

<s:form id="fObjects" method="post">
    <table cellpadding="3" cellspacing="1" border="0" width="100%">
        <tr>
            <td colspan="10">
                <%@include file="/WEB-INF/jsp/ab/filters/pager.jsp" %>
            </td>
        </tr>
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
            <td class="th">
                <s:text name="sz.user_name" />
            </td>
        </tr>
        <s:iterator value="szFiles" status="status">
            <tr valign="middle" class="cols_1">
                <td class="col_1s">
                    <input type="checkbox" value="<s:property value="id" />" name="szFileIds" />
                </td>
                <td class="col_1s">
                    <s:property value="%{#status.index + pager.thisPageFirstElementNumber + 1}" />
                </td>
                <td class="col">
                    <s:property value="oszn.description"/>
                </td>
                <td class="col">
                    <a href="<s:url value='/szFileDownloadServlet'><s:param name="szFileId" value="%{id}"/><s:param name="req" value="true" /></s:url>">
                        <s:property value="uploadedFile.originalName"/>
                    </a>
                </td>
                <td class="col">
                    <s:if test="fileToDownload">
                        <a href="<s:url value='/szFileDownloadServlet'><s:param name="szFileId" value="%{id}"/></s:url>">
                            <s:property value="fileToDownload.originalName"/>
                        </a>
                    </s:if>
                </td>
                <td class="col">
                    <s:text name="%{type.name}" />
                </td>
                <td class="col">
                    <s:property value="%{format('MMMMM yyyy', userPreferences.locale)}" />
                </td>
                <td class="col">
                    <s:date name="importDate" format="dd.MM.yyyy hh:mm:ss" />
                </td>
                <td class="col">
                    <s:text name="%{status.name}"/>
                </td>
                <td class="col">
                    <s:property value="userName"/>
                </td>
            </tr>
        </s:iterator>
        <tr>
            <td colspan="10" height="3" bgcolor="#4a4f4f"/>
        </tr>
        <tr>
            <td colspan="10">
                <input type="submit" class="btn-exit" onclick="doAction('loadToDB');" value="<s:text name="sz.file_list.action.load_to_db" />" />
                &nbsp;
                <input type="submit" class="btn-exit" onclick="doAction('fullDelete');" value="<s:text name="sz.file_list.action.full_delete" />" />
                &nbsp;
                <input type="submit" class="btn-exit" onclick="doAction('loadFromDB');" value="<s:text name="sz.file_list.action.load_from_db" />" />
                &nbsp;
                <input type="submit" class="btn-exit" onclick="doAction('deleteFromDB');" value="<s:text name="sz.file_list.action.delete_from_db" />" />
                &nbsp;
                <input type="button" class="btn-exit" onclick="location.reload();" value="<s:text name="sz.file_list.refresh_list" />" />
                <input id="action1" type="hidden" name="" value="loadToDB" />
                <input id="pageNum" type="hidden" name="" value="<s:property value="%{pager.pageNumber}"/>"/>
            </td>
        </tr>
        <tr>
            <td colspan="10">
                <%@include file="/WEB-INF/jsp/ab/filters/pager.jsp" %>
            </td>
        </tr>
    </table>
</s:form>

<script type="text/javascript">

    function doAction(action) {
        $("#pageNum").attr("name", "pager.pageNumber");
        $("#action1").attr("name", "action1").val(action);
        $("#fObjects").attr("action", "<s:url action="szFileOperation" includeParams="none" />");
    }

</script>
