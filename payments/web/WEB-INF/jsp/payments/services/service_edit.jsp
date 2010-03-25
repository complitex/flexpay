<%@include file="/WEB-INF/jsp/common/taglibs.jsp"%>
<%@include file="/WEB-INF/jsp/common/includes/errors_messages.jsp"%>

<s:form action="serviceEdit" method="POST">

	<s:hidden name="service.id" />

	<table cellpadding="3" cellspacing="1" border="0" width="100%">
        <tr valign="top" class="cols_1">
            <td class="col"><s:text name="eirc.service.external_code" />:</td>
            <td class="col"><s:textfield name="service.externalCode" /></td>
        </tr>
        <tr valign="top" class="cols_1">
            <td class="col"><s:text name="eirc.service.name" />:</td>
            <td class="col">
                <s:iterator value="names"><s:set name="l" value="%{getLang(key)}" />
                    <s:textfield name="names[%{key}]" value="%{value}"/>(<s:if test="#l.default">*</s:if><s:property value="getLangName(#l)" />)<br />
                </s:iterator>
            </td>
        </tr>        
        <tr valign="top" class="cols_1">
			<td class="col"><s:text name="eirc.service_provider" />*:</td>
			<td class="col"><%@include file="/WEB-INF/jsp/payments/filters/service_provider_filter.jsp"%></td>
		</tr>
		<tr valign="top" class="cols_1">
			<td class="col"><s:text name="payments.service_type" />*:</td>
			<td class="col"><%@include file="/WEB-INF/jsp/payments/filters/service_type_filter.jsp"%></td>
		</tr>
		<tr valign="top" class="cols_1">
			<td class="col"><s:text name="eirc.service.parent_service" />:</td>
			<td class="col"><%@include file="/WEB-INF/jsp/payments/filters/parent_service_filter.jsp"%></td>
		</tr>
		<tr valign="top" class="cols_1">
			<td class="col"><s:text name="common.measure_unit" />:</td>
			<td class="col"><%@include file="/WEB-INF/jsp/common/filter/measure_unit_filter.jsp"%></td>
		</tr>
		<tr valign="top" class="cols_1">
			<td class="col"><s:text name="eirc.service.begin_date" />*:</td>
			<td class="col"><%@include file="/WEB-INF/jsp/common/filter/begin_date_filter.jsp"%></td>
		</tr>
		<tr valign="top" class="cols_1">
			<td class="col"><s:text name="eirc.service.end_date" />:</td>
			<td class="col"><%@include file="/WEB-INF/jsp/common/filter/end_date_filter.jsp"%></td>
		</tr>
		<tr valign="middle" class="cols_1">
			<td colspan="2">
                <input type="submit" name="submitted" class="btn-exit" value="<s:text name="common.save" />" />
            </td>
		</tr>
	</table>

</s:form>
