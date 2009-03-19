<%@ page contentType="text/html;charset=UTF-8" pageEncoding="utf-8" language="java" %>
<%@ include file="/WEB-INF/jsp/common/taglibs.jsp" %>

<%@include file="/WEB-INF/jsp/common/jquery_ui.jsp"%>

<script type="text/javascript">
    FP.calendars("#date", "<s:url value="/resources/common/js/jquery/jquery-ui/images/calendar.gif" includeParams="none" />");
</script>

<s:actionerror />

<s:form action="districtCreate" method="post">
	<table cellpadding="3" cellspacing="1" border="0" width="100%">

		<tr>
			<td colspan="4">
				<%@ include file="filters/groups/country_region_town.jsp" %>
			</td>
		</tr>
		<tr>
			<td colspan="4">
				<s:textfield name="date" id="date" readonly="true" />
			</td>
		</tr>

		<tr>
			<td class="th" width="1%">&nbsp;</td>
			<s:if test="nameTranslations.size() > 1">
				<td class="th"><s:text name="ab.language" /></td>
			</s:if>
			<td class="th"><s:text name="ab.district" /></td>
		</tr>
		<s:iterator value="nameTranslations" status="rowstatus">
			<tr valign="middle" class="cols_1">
				<td class="col_1s"><s:property value="#rowstatus.index + 1" /></td>
				<s:if test="nameTranslations.size() > 1">
					<td class="col">
						<s:property value="getLangName(lang)" />
						<s:if test="%{lang.default}">*</s:if>
					</td>
				</s:if>
				<td class="col">
					<input type="text" name="translation.<s:property value="lang.id"/>"
						   value="<s:property value="name"/>" />
				</td>
			</tr>
		</s:iterator>

		<tr>
			<td colspan="3">
				<input type="submit" class="btn-exit" name="submitted" value="<s:text name="common.create"/>" />
			</td>
		</tr>
	</table>
</s:form>
