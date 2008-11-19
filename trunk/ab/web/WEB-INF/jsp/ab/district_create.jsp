<%@ include file="/WEB-INF/jsp/common/taglibs.jsp" %>

<s:actionerror />

<s:form action="create_district" method="post">
	<table cellpadding="3" cellspacing="1" border="0" width="100%">

		<tr>
			<td colspan="4">
				<%@ include file="filters/groups/country_region_town.jsp" %>
			</td>
		</tr>
		<tr>
			<td colspan="4">
				<s:textfield name="date" id="date" />
				<img src="<s:url value="/resources/common/js/jscalendar/img.gif"/>" alt=""
					 id="trigger_from"
					 style="cursor: pointer; border: 1px solid red;"
					 title="<s:text name="common.calendar"/>"
					 onmouseover="this.style.background='red';"
					 onmouseout="this.style.background='';" />
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
