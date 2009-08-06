<%@include file="/WEB-INF/jsp/common/taglibs.jsp" %>

<s:actionerror />

<s:form action="districtEdit">

    <s:hidden name="district.id" value="%{district.id}" />

	<%@ include file="../filters/groups/country_region_town_ajax.jsp" %>
	<table cellpadding="3" cellspacing="1" border="0" width="100%">

		<tr valign="middle" class="cols_1">
			<td class="col">
				<s:text name="common.begin_date" />
			</td>
			<td class="col">
				<%@ include file="/WEB-INF/jsp/common/filter/begin_date_filter.jsp" %>
			</td>
		</tr>

		<tr valign="top" class="cols_1">
			<td class="col"><s:text name="ab.district.name" />:</td>
			<td class="col">
				<s:iterator value="names"><s:set name="l" value="%{getLang(key)}" />
					<s:textfield name="names[%{key}]" value="%{value}" />(<s:if test="%{#l.default}">*</s:if><s:property
							value="%{getLangName(#l)}" />)<br />
				</s:iterator>
			</td>
		</tr>

		<tr>
			<td colspan="2" valign="middle">
				<input type="submit" class="btn-exit" name="submitted" value="<s:text name="common.save"/>" />
			</td>
		</tr>
	</table>
</s:form>
<s:if test="%{district.id != 0}">
    <script type="text/javascript">
        $(function() {
            FF.updateFilter("town", {readonly:true});
        });
    </script>
</s:if>
