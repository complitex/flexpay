<%@ page contentType="text/html;charset=UTF-8" pageEncoding="utf-8" language="java" %>
<%@include file="/WEB-INF/jsp/common/taglibs.jsp"%>

<s:actionerror />

<s:form id="inputForm" action="rulesFileEdit" method="POST" enctype="multipart/form-data">

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
                </s:if>
                <s:else>
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

<script type="text/javascript">

    function validateForm() {
        var fileVal = jQuery('input[name="upload"]').val();
        if (fileVal == null || fileVal == "") {
            alert("<s:text name="tc.error.rules_file_field_cant_be_null" />");
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

        jQuery("#inputForm").submit();
    }

</script>
