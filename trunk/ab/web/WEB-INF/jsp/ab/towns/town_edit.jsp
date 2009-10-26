<%@include file="/WEB-INF/jsp/common/taglibs.jsp" %>

<s:actionerror />

<s:form action="townEdit">
	<s:hidden name="town.id" value="%{town.id}" />

	<table cellpadding="3" cellspacing="1" border="0" width="100%">
		<tr>
			<td colspan="2">
                <s:set name="readonly" value="%{town.id > 0}" />
				<%@include file="../filters/groups/country_region_ajax.jsp" %>
			</td>
		</tr>
        <tr valign="middle" class="cols_1">
            <td class="col">
                <s:text name="common.begin_date" />
            </td>
            <td class="col">
                <%@include file="/WEB-INF/jsp/common/filter/begin_date_filter.jsp" %>
            </td>
        </tr>
		<tr valign="top" class="cols_1">
			<td class="col"><s:text name="ab.town_type" />:</td>
			<td class="col">
				<%@include file="../filters/town_type_filter.jsp" %>
			</td>
		</tr>
		<tr valign="top" class="cols_1">
			<td class="col"><s:text name="ab.name" />:</td>
			<td class="col">
				<s:iterator value="names"><s:set name="l" value="%{getLang(key)}" />
					<s:textfield name="names[%{key}]" value="%{value}" />(<s:if test="%{#l.default}">*</s:if><s:property
							value="%{getLangName(#l)}" />)<br />
				</s:iterator>
			</td>
		</tr>
		<tr>
			<td colspan="2">
				<input type="submit" class="btn-exit" value="<s:text name="common.save"/>" name="submitted" />
			</td>
		</tr>
	</table>
</s:form>
