<%@include file="/WEB-INF/jsp/common/taglibs.jsp" %>

<table cellpadding="3" cellspacing="1" border="0" width="100%">
	<tr>
		<td class="th">&nbsp;</td>
		<td class="th"><s:text name="ab.from"/></td>
		<td class="th"><s:text name="ab.till"/></td>
		<td class="th"><s:text name="ab.street"/></td>
	</tr>
	<s:set name="index" value="-1" />
	<s:iterator value="object.namesTimeLine.intervals" status="rowstatus">
		<s:iterator value="value.translations">
			<tr valign="middle" class="cols_1">
				<td class="col_1s" align="right">
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
					<s:property value="name"/>
				</td>
			</tr>
		</s:iterator>
	</s:iterator>
	<tr>
		<td class="th">&nbsp;</td>
		<td class="th"><s:text name="ab.from"/></td>
		<td class="th"><s:text name="ab.till"/></td>
		<td class="th"><s:text name="ab.street.type"/></td>
	</tr>
	<s:set name="index" value="-1" />
	<s:iterator value="object.typesTimeLine.intervals" status="rowstatus">
		<s:set name="translation" value="%{getTranslation(value.translations)}" />
			<tr valign="middle" class="cols_1">
				<td class="col_1s" align="right">
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
				<td class="col" colspan="2">
					<s:property value="%{#translation.name}"/>
				</td>
			</tr>
	</s:iterator>
	<tr>
		<td class="th">&nbsp;</td>
		<td class="th"><s:text name="ab.district"/></td>
		<td class="th" colspan="2">&nbsp;</td>
	</tr>
	<s:iterator value="%{districts}" status="status">
		<tr valign="middle" class="cols_1">
			<td class="col_1s" align="right"><s:property
					value="%{#status.index + 1}"/>&nbsp;</td>
			<td class="col"><s:property value="%{getTranslation(currentName.translations).name}"/></td>
			<td class="col" colspan="2">
				<a href="<s:url value="/dicts/districtView.action?object.id=%{object.id}"/>"><s:text
						name="common.view"/></a></td>
		</tr>
	</s:iterator>
	<tr>
		<td colspan="4">
			<input type="button" class="btn-exit" title="<s:text name="ab.street.add_street_name"/>"
				   onclick="window.location='<s:url action="streetEdit" includeParams="none"><s:param name="street.id" value="%{object.id}"/></s:url>';"
				   value="<s:text name="common.edit"/>"/>
			<input type="button" class="btn-exit" title="<s:text name="ab.street.manage.districts"/>"
				   onclick="window.location='<s:url action="streetDistrictEdit" includeParams="none"><s:param name="street.id" value="%{object.id}"/></s:url>';"
				   value="<s:text name="ab.street.manage.districts"/>"/>
		</td>
	</tr>
</table>
