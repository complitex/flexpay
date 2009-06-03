
<%@ include file="/WEB-INF/jsp/common/taglibs.jsp" %>

<table cellpadding="3" cellspacing="1" border="0" width="100%">
	<tr>
		<td class="th" width="1%">&nbsp;</td>
		<td class="th"><s:text name="ab.from"/></td>
		<td class="th"><s:text name="ab.till"/></td>
		<td class="th"><s:text name="common.language"/></td>
		<td class="th"><s:text name="ab.district"/></td>
	</tr>
	<s:set name="index" value="-1" />
	<s:iterator value="object.namesTimeLine.intervals" status="rowstatus">
		<s:iterator value="value.translations">
			<tr valign="middle" class="cols_1">
				<td class="col_1s">
					<s:if test="#{#rowstatus.index > index}">
						<s:property value="#rowstatus.index + 1"/>
					</s:if>
				</td>
				<td class="col">
					<s:if test="#{#rowstatus.index > index}">
						<s:property value="format(begin)"/>
					</s:if>
				</td>
				<td class="col">
					<s:if test="#{#rowstatus.index > index}">
						<s:property value="format(end)"/>
					</s:if>
				</td>
				<td class="col">
					<s:property value="getLangName(lang)"/>
				</td>
				<td class="col">
					<s:property value="name"/>
				</td>
			</tr>
		</s:iterator>
	</s:iterator>
	<tr>
		<td colspan="6">
			<input type="button" class="btn-exit"
				   onclick="window.location='<s:url action='districtEdit' includeParams="none"><s:param name="district.id" value="%{object.id}"/></s:url>';"
				   value="<s:text name="common.edit"/>"/>
		</td>
	</tr>
</table>
