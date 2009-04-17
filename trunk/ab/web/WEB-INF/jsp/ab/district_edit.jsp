
<%@ include file="/WEB-INF/jsp/common/taglibs.jsp" %>

<%@include file="/WEB-INF/jsp/common/includes/jquery_ui.jsp"%>

<script type="text/javascript">
    FP.calendars("#date", true);
</script>

<s:actionerror/>

<table cellpadding="3" cellspacing="1" border="0" width="100%">
	<s:form action="districtEdit" method="post">

		<tr>
			<td class="th" width="100%" colspan="3" align="center">
				<s:textfield name="date" id="date" value="%{date}" readonly="true" />
			</td>
		</tr>

		<tr>
			<td class="th">&nbsp;</td>
			<s:if test="nameTranslations.size() > 1">
				<td class="th"><s:text name="ab.language"/></td>
			</s:if>
			<td class="th"><s:text name="ab.district"/></td>
		</tr>
		<s:iterator value="nameTranslations" status="rowstatus">
			<tr valign="middle" class="cols_1">
				<td class="col_1s"><s:property value="#rowstatus.index + 1"/></td>
				<s:if test="nameTranslations.size() > 1">
					<td class="col">
						<s:property value="getLangName(lang)"/>
						<s:if test="%{lang.default}">*</s:if>
					</td>
				</s:if>
				<td class="col">
					<input type="text" name="translation.<s:property value="lang.id"/>"
						   value="<s:property value="name"/>"/>
				</td>
			</tr>
		</s:iterator>

		<tr>
			<td colspan="3" height="3" bgcolor="#4a4f4f"/>
		<tr>
			<td colspan="3">
				<input type="submit" class="btn-exit" name="submitted"
					   value="<s:text name="common.save"/>"/>
			</td>
		</tr>
		<s:hidden name="temporalId" value="%{temporalId}" />
		<s:hidden name="object.id" value="%{object.id}" />
	</s:form>
</table>
