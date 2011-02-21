<%@include file="/WEB-INF/jsp/common/taglibs.jsp"%>

<div id="response">
    <%@include file="/WEB-INF/jsp/common/includes/errors_messages.jsp"%>
</div>

<s:form action="regionEdit" method="POST">

    <s:hidden name="region.id" />

    <table cellpadding="3" cellspacing="1" border="0" width="100%">
        <tr>
            <td colspan="2">
                <s:set name="readonly" value="%{region.id > 0}" />
                <%@include file="/WEB-INF/jsp/ab/filters/groups/country_ajax.jsp"%>
            </td>
        </tr>
        <tr valign="middle" class="cols_1">
            <td class="col">
                <s:text name="common.begin_date" />
            </td>
            <td class="col">
                <%@include file="/WEB-INF/jsp/common/filter/begin_date_filter.jsp"%>
            </td>
        </tr>
        <tr valign="top" class="cols_1">
            <td class="col"><s:text name="ab.region.name" />:</td>
            <td class="col">
                <s:iterator value="names">
                    <s:set name="l" value="%{getLang(key)}" />
                    <s:textfield name="names[%{key}]" value="%{value}" />(<s:if test="#l.default">*</s:if><s:property value="getLangName(#l)" />)<br>
                </s:iterator>
            </td>
        </tr>
        <tr>
            <td colspan="2">
                <s:submit name="submitted" cssClass="btn-exit" value="%{getText('common.save')}" />
            </td>
        </tr>
    </table>
</s:form>
