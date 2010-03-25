<%@include file="/WEB-INF/jsp/common/taglibs.jsp"%>
<%@include file="/WEB-INF/jsp/common/includes/errors_messages.jsp"%>

<script type="text/javascript">

    function toggleElements(elements, showButton) {
        $.each(elements, function(i, value) {
            $(value).toggle();
        });
        $(showButton).val($(elements[0]).is(":visible") ? "<s:text name="common.hide"/>" : "<s:text name="common.show"/>");
    }

    function toggleProcessVariables() {
        toggleElements($("#processVariables"), "#toggle_variables_button");
    }

	function scrollTextArea() {
		var t = $('#logText').get(0);
		t.scrollTop = t.scrollHeight;
	}
	$(document).ready(scrollTextArea);

</script>

<s:if test="process != null">
    <table cellpadding="3" cellspacing="1" border="0" width="100%">
        <tr>
            <td class="th" colspan="2"><s:text name="common.processing.process.properties"/></td>
        </tr>
        <tr valign="middle" class="cols_1">
            <td class="col"><s:text name="common.processing.process.id"/></td>
            <td class="col"><s:property value="process.id"/></td>
        </tr>
        <tr valign="middle" class="cols_1">
            <td class="col"><s:text name="common.processing.process.log_file"/></td>
            <td class="col"><s:property value="process.logFileName"/></td>
        </tr>
        <tr valign="middle" class="cols_1">
            <td class="col"><s:text name="common.processing.process.start_date"/></td>
            <td class="col"><s:property value="process.processStartDate"/></td>
        </tr>
        <tr valign="middle" class="cols_1">
            <td class="col"><s:text name="common.processing.process.end_date"/></td>
            <td class="col"><s:property value="process.processEndDate"/></td>
        </tr>
        <tr valign="middle" class="cols_1">
            <td class="col"><s:text name="common.processing.process.state"/></td>
            <td class="col"><s:property value="process.processState"/></td>
        </tr>
        <tr valign="middle" class="cols_1">
            <td class="col"><s:text name="common.processing.process.definition_name"/></td>
            <td class="col"><s:text name="%{process.processDefinitionName}"/></td>
        </tr>
        <tr valign="middle" class="cols_1">
            <td class="col"><s:text name="common.processing.process.instance_id"/></td>
            <td class="col"><s:property value="process.processInstaceId"/></td>
        </tr>
        <tr valign="middle" class="cols_1">
            <td class="col"><s:text name="common.processing.process.definition_version"/></td>
            <td class="col"><s:property value="process.processDefenitionVersion"/></td>
        </tr>
        <s:if test="logText != null">
            <tr>
                <td class="th" colspan="2" style="padding:0;">
                    <table style="width:100%;font-size:100%;font-weight:bold;">
                        <tr>
                            <td><s:text name="common.processing.process.process_log_file"/></td>
                            <td style="text-align:right;">
                                <a href="<s:url value='/processLogDownloadServlet'><s:param name="processId" value="%{process.id}"/></s:url>">
                                    <s:text name="common.processing.process.process_download_log_file"/>
                                </a>
                            </td>
                        </tr>
                    </table>
                </td>
            </tr>
            <tr>
                <td class="cols_1" colspan="2">
                    <textarea rows="10" cols="100" wrap="off" id="logText"><s:property value="logText"/></textarea>
                </td>
            </tr>
        </s:if>
        <tr>
            <td class="th" colspan="2" style="padding:0;">
                <table style="width:100%;font-size:100%;font-weight:bold;">
                    <tr>
                        <td><s:text name="common.processing.process.context_variables"/></td>
                        <td style="text-align: right;">
                            <input type="button" class="btn-exit" value="<s:text name="common.hide"/>"
                                   id="toggle_variables_button" onclick="toggleProcessVariables();"/>
                        </td>
                    </tr>
                </table>
            </td>
        </tr>
        <tr>
            <td colspan="2">
                <table id="processVariables">
                    <s:iterator value="process.parameters">
                        <tr class="cols_1">
                            <td class="col"><s:property value="key"/></td>
                            <td class="col"><s:property value="value"/></td>
                        </tr>
                    </s:iterator>
                </table>
            </td>
        </tr>
    </table>
</s:if>
