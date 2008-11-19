<%@ include file="/WEB-INF/jsp/common/taglibs.jsp" %>

<table cellpadding="3" cellspacing="1" border="0" width="100%">
	<tr>
		<td class="th">&nbsp;</td>
		<td class="th"><s:text name="ab.from"/></td>
		<td class="th"><s:text name="ab.till"/></td>
		<td class="th"><s:text name="common.language"/></td>
		<td class="th"><s:text name="ab.street"/></td>
		<td class="th">&nbsp;</td>
	</tr>
	<s:set name="index" value="-1" />
	<s:iterator value="object.namesTimeLine.intervals" status="rowstatus">
		<s:set name="temporalId" value="id" />
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
					<s:property value="getLangName(lang)"/>
				</td>
				<td class="col">
					<s:property value="name"/>
				</td>
				<td class="col">
					<s:if test="#{#rowstatus.index > index}">
						<s:set name="index" value="#rowstatus.index" />
						<a href="<s:url action='streetEdit'><s:param name="temporalId" value="%{temporalId}"/></s:url>">
							<s:text name="ab.edit"/>
						</a>
					</s:if>
				</td>
			</tr>
		</s:iterator>
	</s:iterator>
	<tr>
		<td colspan="6" height="3" bgcolor="#4a4f4f"/>
	</tr>
	<tr>
		<td class="th">&nbsp;</td>
		<td class="th"><s:text name="ab.from"/></td>
		<td class="th"><s:text name="ab.till"/></td>
		<td class="th" colspan="2"><s:text name="ab.street_type"/></td>
		<td class="th">&nbsp;</td>
	</tr>
	<s:set name="index" value="-1" />
	<s:iterator value="object.typesTimeLine.intervals" status="rowstatus">
		<s:set name="temporalId" value="id" />
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
				<td class="col">
					<s:if test="#{#rowstatus.index > index}">
						<s:set name="index" value="#rowstatus.index" />
						<a href="<s:url action='editStreetType'><s:param name="temporal.id" value="%{temporalId}"/></s:url>">
							<s:text name="ab.edit"/>
						</a>
					</s:if>
				</td>
			</tr>
	</s:iterator>
	<tr>
		<td colspan="6" height="3" bgcolor="#4a4f4f"/>
	</tr>
	<tr>
		<td class="th">&nbsp;</td>
		<td class="th" colspan="3">&nbsp;</td>
		<td class="th"><s:text name="ab.district"/></td>
		<td class="th">&nbsp;</td>
	</tr>
	<s:iterator value="%{object.districts}" status="status">
		<tr valign="middle" class="cols_1">
			<td class="col_1s" align="right"><s:property
					value="%{#status.index + 1}"/>&nbsp;</td>
			<td colspan="3">&nbsp;</td>
			<td class="col"><s:property value="%{getTranslation(currentName.translations).name}"/></td>
			<td class="col">
				<a href="<s:url value="/dicts/view_district.action?object.id=%{object.id}"/>"><s:text
						name="common.view"/></a></td>
		</tr>
	</s:iterator>
	<tr>
		<td colspan="6">
			<input type="button" class="btn-exit" title="<s:text name="ab.street.add_street_name"/>"
				   onclick="window.location='<s:url action='streetEdit'><s:param name="temporalId" value="0"/></s:url>';"
				   value="<s:text name="ab.street.add_street_name"/>"/>
			<input type="button" class="btn-exit" title="<s:text name="ab.street.add_street_type"/>"
				   onclick="window.location='<s:url action='editStreetType'><s:param name="temporal.id" value="0"/></s:url>';"
				   value="<s:text name="ab.street.add_street_type"/>"/>
			<input type="button" class="btn-exit" title="<s:text name="ab.street.manage.districts"/>"
				   onclick="window.location='<s:url action='streetDistrictEdit'><s:param name="street.id" value="%{object.id}"/></s:url>';"
				   value="<s:text name="ab.street.manage.districts"/>"/>
		</td>
	</tr>
</table>
