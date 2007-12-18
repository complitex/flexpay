<%@ include file="/WEB-INF/jsp/common/taglibs.jsp" %>
<div>
	<span style="float:left;">
		&nbsp;<s:text name="common.show_by"/>&nbsp;
		<select name="pager.pageSize" class="form-select">
			<option value="10"
					<s:if test="%{pager.pageSize == 10}">selected</s:if> >10
			</option>
			<option value="20"
					<s:if test="%{pager.pageSize == 20}">selected</s:if> >20
			</option>
			<option value="30"
					<s:if test="%{pager.pageSize == 30}">selected</s:if> >30
			</option>
		</select>
		&nbsp;<s:text name="common.items"/>
	</span>
	<span style="float:right;">
		<s:text name="common.pages"/>:&nbsp;
		<s:if test="%{pager.isFirstPage() == false}">
			1&nbsp;
		</s:if>

		<s:if test="%{pager.hasPreviousPage() && pager.previousPageNumber > 2}">
			...&nbsp;
		</s:if>

		<s:if test="%{pager.hasPreviousPage() && pager.previousPageNumber > 0}">
			<s:property value="%{pager.previousPageNumber}"/>&nbsp;
		</s:if>

		<input name="pager.pageNumber" type="text" class="form-input-pager"
			   value="<s:property value="%{pager.pageNumber}" />">&nbsp;

		<s:if test="%{pager.hasNextPage() && pager.nextPageNumber < pager.lastPageNumber}">
			<s:property value="%{pager.nextPageNumber}"/>&nbsp;
		</s:if>

		<s:if test="%{pager.hasNextPage() && pager.nextPageNumber < pager.lastPageNumber - 2}">
			...&nbsp;
		</s:if>

		<s:if test="%{pager.isLastPage() == false}">
			<s:property value="pager.lastPageNumber"/>
		</s:if>
		&nbsp;
	</span>
</div>
