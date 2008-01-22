<%@ include file="/WEB-INF/jsp/common/taglibs.jsp" %>

<s:actionerror/>

<table cellpadding="3" cellspacing="1" border="0" width="100%">
	<s:form action="create_town" method="post">

		<tr>
			<td class="th" width="100%" colspan="3" align="center">
				<%@ include file="filters/town_type_filter.jsp" %>
			</td>
		</tr>
		<tr>
			<td colspan="3" height="3" bgcolor="#4a4f4f"/>
		<tr>
			<td colspan="3" height="20"/>
		<tr>
			<td class="th" width="100%" colspan="3" align="center">
				<%@ include file="filters/country_filter.jsp" %>
			</td>
		</tr>
		<tr>
			<td class="th" width="100%" colspan="3" align="center">
				<%@ include file="filters/region_filter.jsp" %>
			</td>
		</tr>
		<tr>
			<td class="th" width="100%" colspan="3" align="center">
				<s:textfield name="date" id="date"/>
				<img src="<s:url value="/js/jscalendar/img.gif"/>" alt=""
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

		<tr>
			<td class="th">&nbsp;</td>
			<s:if test="nameTranslations.size() > 1">
				<td class="th"><s:text name="ab.language"/></td>
			</s:if>
			<td class="th"><s:text name="ab.town"/></td>
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
				<input type="submit" class="btn-exit"
					   value="<s:text name="common.create"/>"/>
			</td>
		</tr>
	</s:form>
</table>
