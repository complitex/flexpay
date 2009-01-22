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
            <input type="checkbox" onclick="FP.setCheckboxes(this.checked, 'rulesFileIds');" />
        </td>
        <td class="th">
            &nbsp;
        </td>
        <td class="th">
            <s:text name="tc.rules_file.name" />
        </td>
        <td class="th">
            <s:text name="tc.rules_file.description" />
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
            <s:text name="tc.rules_file.file_status" />
        </td>
    </tr>
    <s:iterator value="rulesFiles" status="status">
        <tr valign="middle" class="cols_1">
            <td class="col_1s">
                <input type="checkbox" value="<s:property value="rulesFile.id" />" name="rulesFileIds" />
            </td>
            <td class="col_1s">
                <s:property value="%{#status.index + pager.thisPageFirstElementNumber + 1}" />
            </td>
            <td class="col">
                <s:property value="%{getTranslation(rulesFile.translations).name}" />
            </td>
            <td class="col">
                <s:property value="%{getTranslation(rulesFile.translations).description}" />
                <a href="<s:url value='/rulesFileDownloadServlet'><s:param name="rulesFileId" value="%{rulesFile.id}"/></s:url>">
                    <s:property value="rulesFile.file.originalName"/>
                </a>
            </td>
            <td class="col">
                <s:date name="rulesFile.creationDate" format="dd.MM.yyyy hh:mm:ss" />
            </td>
            <td class="col">
                <s:property value="rulesFile.userName"/>
            </td>
            <td class="col">
                <s:text name="%{rulesFile.status.name}"/>
            </td>
        </tr>
    </s:iterator>

    <tr>
        <td colspan="10" height="3" bgcolor="#4a4f4f"/>
    <tr>

    <tr>
        <td colspan="6">
            <input type="submit" value="<s:text name="common.delete_selected" />" class="btn-exit" onclick="$('fObjects').action='<s:url action="rulesFileDelete"/>';" />
        </td>
        <td colspan="4">
            <%@include file="/WEB-INF/jsp/ab/filters/pager.jsp" %>
        </td>
    <tr>

    </table>
</s:form>
