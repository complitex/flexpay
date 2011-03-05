<%@include file="/WEB-INF/jsp/common/taglibs.jsp"%>
<%@include file="/WEB-INF/jsp/common/includes/errors_messages.jsp"%>

<script type="text/javascript">

    function validateForm() {
        var fileVal = $("input[name='upload']").val();
        if (fileVal == null || fileVal == "") {
            alert("<s:text name="common.error.no_file" />");
            return false;
        }
        return true;
    }

    function submitForm() {
        <s:if test="%{rulesFile.id <= 0}">
            if (!validateForm()) {
                return;
            }
        </s:if>
        $("#inputForm").submit();
    }

</script>

<s:form id="inputForm" action="rulesFileEdit" enctype="multipart/form-data">

    <s:hidden name="rulesFile.id" />

    <table cellpadding="3" cellspacing="1" border="0" width="100%">
        <tr valign="top" class="cols_1">
            <td class="col">
                <s:text name="tc.rules_file.name"/>:
            </td>
            <td class="col">
                <s:iterator value="names">
                    <s:set name="l" value="%{getLang(key)}" />
                    <s:textfield name="names[%{key}]" value="%{value}" />(<s:if test="%{#l.default}">*</s:if><s:property value="%{getLangName(#l)}" />)<br />
                </s:iterator>
            </td>
        </tr>
        <tr valign="top" class="cols_1">
            <td class="col">
                <s:text name="tc.rules_file.description"/>:
            </td>
            <td class="col">
                <s:iterator value="descriptions">
                    <s:set name="l" value="%{getLang(key)}" />
                    <s:textfield name="descriptions[%{key}]" value="%{value}" />(<s:property value="%{getLangName(#l)}" />)<br />
                </s:iterator>
            </td>
        </tr>
        <tr valign="top" class="cols_1">
            <td class="col">
                <s:text name="tc.rules_file.file" />
            </td>
            <td class="col">
                <s:if test="%{rulesFile.id > 0}">
                    <s:property value="rulesFile.file.originalName"/>
                </s:if><s:else>
                    <s:file name="upload" label="File" required="true" size="60" />
                </s:else>
            </td>
        </tr>
        <tr valign="middle">
            <td colspan="2">
                <input type="hidden" id="submitted" name="submitted" value="true" />
                <input type="button" class="btn-exit" value="<s:text name="common.save"/>" onclick="submitForm();" />
            </td>
        </tr>
    </table>

</s:form>
