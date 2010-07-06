<%@include file="/WEB-INF/jsp/common/taglibs.jsp"%>
<%@include file="/WEB-INF/jsp/common/includes/errors_messages.jsp"%>

<s:form action="changeAnnotation" method="POST">
    <s:hidden name="registry.id" />
    <table>
        <tr>
            <td colspan="2">
                <s:textarea rows="10" cols="50" name="registryAnnotation" /><br/>
            </td>
        </tr>
        <tr>
            <td align="left">
                <input type="submit" name="submitChange" class="" value="<s:text name="payments.registry.annotation.submit" />" />
            </td>
            <td align="right">
                <input type="submit" name="cancel" class="" value="<s:text name="payments.registry.annotation.cancel" />" />
            </td>
        </tr>
    </table>
</s:form>
