<%@ include file="/WEB-INF/jsp/common/taglibs.jsp" %>

<script type="text/javascript" src="<c:url value="/resources/common/js/prototype.js" />"></script>
<%--<script type="text/javascript" src="<c:url value="/resources/eirc/js/flexpay_eirc.js" />"></script>--%>

<script type="text/javascript">
    /*
     * Hides process variables section content
     */
    function hideProcessVariables() {
        $('hideProcessVariablesDiv').hide();
        $('showProcessVariablesDiv').show();
        $('processVariables').hide();
    }

    /**
     * Shows process variables section
     */
    function showProcessVariables() {
        $('hideProcessVariablesDiv').show();
        $('showProcessVariablesDiv').hide();
        $('processVariables').show();
    }
</script>

<table cellpadding="3" cellspacing="1" border="0" width="100%">

    <tr>
        <td class="th" colspan="2"><s:text name="eirc.processing.process.properties"/></td>
    </tr>

    <tr valign="middle" class="cols_1">
        <td class="col"><s:text name="eirc.processing.process.id"/></td>
        <td class="col"><s:property value="process.id"/></td>
    </tr>
    <tr valign="middle" class="cols_1">
        <td class="col"><s:text name="eirc.processing.process.log_file"/></td>
        <td class="col"><s:property value="process.logFileName"/></td>
    </tr>
    <tr valign="middle" class="cols_1">
        <td class="col"><s:text name="eirc.processing.process.start_date"/></td>
        <td class="col"><s:property value="process.processStartDate"/></td>
    </tr>
    <tr valign="middle" class="cols_1">
        <td class="col"><s:text name="eirc.processing.process.end_date"/></td>
        <td class="col"><s:property value="process.processEndDate"/></td>
    </tr>
    <tr valign="middle" class="cols_1">
        <td class="col"><s:text name="eirc.processing.process.state"/></td>
        <td class="col"><s:property value="process.processState"/></td>
    </tr>
    <tr valign="middle" class="cols_1">
        <td class="col"><s:text name="eirc.processing.process.definition_name"/></td>
        <td class="col"><s:property value="process.processDefinitionName"/></td>
    </tr>
    <tr valign="middle" class="cols_1">
        <td class="col"><s:text name="eirc.processing.process.instance_id"/></td>
        <td class="col"><s:property value="process.processInstaceId"/></td>
    </tr>
    <tr valign="middle" class="cols_1">
        <td class="col"><s:text name="eirc.processing.process.definition_version"/></td>
        <td class="col"><s:property value="process.processDefenitionVersion"/></td>
    </tr>

    <s:if test="logText != null">
        <tr>
            <td class="th" colspan="2">
                <s:text name="eirc.processing.process.process_log_file"/>
                <a href="<s:url value='/processLogDownloadServlet'><s:param name="processId" value="%{process.id}"/></s:url>">
                    <s:text name="eirc.processing.process.process_download_log_file"/>
                </a>
            </td>
        </tr>
        <tr>
            <td class="cols_1" colspan="2">
                <textarea rows="10" cols="100" wrap="off"><s:property value="logText"/></textarea>
            </td>
        </tr>
    </s:if>

    <tr>
        <td class="th" colspan="2">
            <s:text name="eirc.processing.process.context_variables"/>
            <a id="hideProcessVariablesDiv" href="#" onclick="hideProcessVariables();">
                <s:text name="eirc.processing.process.hide_variables"/>
            </a>
            <a id="showProcessVariablesDiv" href="#" onclick="showProcessVariables();" style="display: none;">
                <s:text name="eirc.processing.process.show_variables"/>
            </a>
        </td>
    </tr>

    <tr>
        <td colspan="2">
            <table id="processVariables">
                <s:iterator value="process.parameters">
                    <tr valign="middle" class="cols_1">
                        <td class="col"><s:property value="key"/></td>
                        <td class="col"><s:property value="value"/></td>
                    </tr>
                </s:iterator>
            </table>
        </td>
    </tr>

</table>