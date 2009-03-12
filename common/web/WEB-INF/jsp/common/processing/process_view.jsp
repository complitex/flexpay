<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/WEB-INF/jsp/common/taglibs.jsp" %>

<script type="text/javascript" src="<c:url value="/resources/common/js/prototype.js" />"></script>
<%--<script type="text/javascript" src="<c:url value="/resources/eirc/js/flexpay_eirc.js" />"></script>--%>
<script type="text/javascript" src="<c:url value="/resources/common/js/jquery/jquery-1.3.2.js"/>"></script>
<script type="text/javascript" src="<c:url value="/resources/common/js/flexpay_common.js"/>"></script>

<script type="text/javascript">

    // folding functions
    function toggleElements(elements, showButton) {
        jQuery.each(elements, function(i, value) {
            jQuery(value).toggle();
        });

        if (jQuery(elements[0]).is(':visible')) {
            jQuery(showButton).val('<s:text name="common.hide"/>');
        } else {
            jQuery(showButton).val('<s:text name="common.show"/>');
        }
    }
    ;

    function toggleProcessVariables() {
        toggleElements(jQuery('#processVariables'), '#toggle_variables_button');
    }

</script>

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
                <textarea rows="10" cols="100" wrap="off"><s:property value="logText"/></textarea>
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

            <%--<a id="hideProcessVariablesDiv" href="#" onclick="hideProcessVariables();">--%>
            <%--<s:text name="common.processing.process.hide_variables"/>--%>
            <%--</a>--%>
            <%--<a id="showProcessVariablesDiv" href="#" onclick="showProcessVariables();" style="display:none;">--%>
            <%--<s:text name="common.processing.process.show_variables"/>--%>
            <%--</a>--%>
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
