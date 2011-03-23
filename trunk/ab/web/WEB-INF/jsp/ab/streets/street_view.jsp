<%@include file="/WEB-INF/jsp/common/taglibs.jsp"%>

<div id="response">
    <%@include file="/WEB-INF/jsp/common/includes/errors_messages.jsp"%>
</div>

<table cellpadding="3" cellspacing="1" border="0" width="100%">
	<tr>
		<td class="th">&nbsp;</td>
		<td class="th"><s:text name="common.from" /></td>
		<td class="th"><s:text name="common.till" /></td>
        <td class="th"><s:text name="common.language" /></td>
		<td class="th"><s:text name="ab.street" /></td>
	</tr>
	<s:set name="index" value="1" />
	<s:iterator value="object.namesTimeLine.intervals" status="rowstatus">
		<s:set name="rowSet" value="0" />
		<s:set name="temporalId" value="id" />
		<s:iterator value="value.translations">
			<tr valign="middle" class="cols_1">
				<td class="col_1s" align="right">
					<s:if test="#rowSet == 0"><s:property value="#index" /><s:set name="index" value="#index + 1" /></s:if>
				</td>
				<td class="col">
					<s:if test="#rowSet == 0">
						<s:property value="format(begin)" />
					</s:if>
				</td>
				<td class="col">
					<s:if test="#rowSet == 0">
						<s:property value="format(end)" />
					</s:if>
				</td>
				<td class="col">
					<s:property value="getLangName(lang)" />
				</td>
				<td class="col">
					<s:property value="name" />
				</td>
			</tr>
			<s:set name="rowSet" value="1" />
		</s:iterator>
	</s:iterator>
	<tr>
		<td class="th">&nbsp;</td>
		<td class="th"><s:text name="common.from" /></td>
		<td class="th"><s:text name="common.till" /></td>
        <td class="th"><s:text name="common.language" /></td>
		<td class="th"><s:text name="ab.street.type" /></td>
	</tr>
	<s:set name="index" value="1" />
	<s:iterator value="object.typesTimeLine.intervals" status="rowstatus">
		<s:set name="rowSet" value="0" />
		<s:set name="temporalId" value="id" />
		<s:iterator value="value.translations">
			<tr valign="middle" class="cols_1">
				<td class="col_1s" align="right">
					<s:if test="#rowSet == 0"><s:property value="#index" /><s:set name="index" value="#index + 1" /></s:if>
				</td>
				<td class="col">
					<s:if test="#rowSet == 0">
						<s:property value="format(begin)" />
					</s:if>
				</td>
				<td class="col">
					<s:if test="#rowSet == 0">
						<s:property value="format(end)" />
					</s:if>
				</td>
				<td class="col">
					<s:property value="getLangName(lang)" />
				</td>
				<td class="col">
					<s:property value="name" />
				</td>
			</tr>
			<s:set name="rowSet" value="1" />
		</s:iterator>
	</s:iterator>
	<tr>
		<td class="th">&nbsp;</td>
		<td class="th"><s:text name="ab.district" /></td>
		<td class="th" colspan="3">&nbsp;</td>
	</tr>
	<s:iterator value="districts" status="status">
		<tr valign="middle" class="cols_1">
			<td class="col_1s" align="right"><s:property value="#status.index + 1" /></td>
			<td class="col"><s:property value="getTranslationName(currentName.translations)" /></td>
			<td class="col" colspan="3">
				<a href="<s:url action="districtView"><s:param name="object.id" value="id" /></s:url>"><s:text name="common.view" /></a>
            </td>
		</tr>
	</s:iterator>
	<tr>
		<td colspan="5">
			<input type="button" class="btn-exit"
				   onclick="window.location='<s:url action="streetEdit"><s:param name="street.id" value="object.id" /></s:url>';"
				   value="<s:text name="common.edit" />" />
			<input type="button" class="btn-exit"
				   onclick="window.location='<s:url action="streetDistrictEdit"><s:param name="street.id" value="object.id" /></s:url>';"
				   value="<s:text name="ab.street.manage_districts" />" />
		</td>
	</tr>
</table>
