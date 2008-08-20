<%@ include file="/WEB-INF/jsp/common/taglibs.jsp" %>

<s:actionerror/>

<table cellpadding="3" cellspacing="1" border="0" width="100%">
	<s:form action="create_street" method="post">

		<tr>
			<td colspan="3">
				<%@ include file="filters/groups/country_region_town.jsp" %>
			</td>
		</tr>
		<tr valign="top" class="cols_1">
			<td class="col"><s:text name="ab.street.creation_date"/>:</td>
			<td class="col">
				<s:textfield name="date" id="date"/>
				<img src="<s:url value="/resources/common/js/jscalendar/img.gif"/>" alt=""
					 id="trigger_from"
					 style="cursor: pointer; border: 1px solid red;"
					 title="<s:text name="common.calendar"/>"
					 onmouseover="this.style.background='red';"
					 onmouseout="this.style.background=''"/>
				<script type="text/javascript">
					Calendar.setup({
						inputField	 : "date",
						ifFormat	 : "%Y/%m/%d",
						button		 : "trigger_from",
						align		 : "Tl",
						singleClick  : true
					});
				</script>
			</td>
		</tr>

		<tr valign="top" class="cols_1">
			<td class="col"><s:text name="ab.street.name"/>:</td>
			<td class="col">
				<s:iterator value="nameTranslations">
					<input type="text" name="translation.<s:property value="lang.id"/>"
						   value="<s:property value="name"/>"/>
					<s:property value="getLangName(lang)"/>
					<s:if test="%{lang.default}">*</s:if><br/>
				</s:iterator>
			</td>
		</tr>

		<tr valign="top" class="cols_1">
			<td class="col"><s:text name="ab.street.type"/>:</td>
			<td class="col">
				<%@include file="filters/street_type_filter.jsp" %>
			</td>
		</tr>

		<tr>
			<td colspan="3">
				<input type="submit" class="btn-exit" name="submitted"
					   value="<s:text name="common.save"/>"/>
			</td>
		</tr>
	</s:form>
</table>
