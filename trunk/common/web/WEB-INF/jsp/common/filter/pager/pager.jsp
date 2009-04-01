
<%@ include file="/WEB-INF/jsp/common/taglibs.jsp" %>

<script type="text/javascript">

	if (typeof(submitForm) == 'undefined') {
		function submitForm(select) {
            var elms = document.getElementsByName('pager.pageSize');
            for (var i = 0; i < elms.length; ++i ) {
                var el = elms[i];
                if (el != select && el != null) {
                    el.name = null;
                }
            }
			select.name = 'pager.pageSize';
            $("#pageSizeChanged").val(true);
			select.form.submit();
		}
	}
</script>

<div>
    <input type="hidden" id="pageSizeChanged" name="pageSizeChanged" value="false" />
	<s:if test="%{pager.totalNumberOfElements > 0}">
	<span style="float:right;" class="text-small">
		&nbsp;<s:text name="common.show_by"/>&nbsp;
		<select name="pager.pageSize" class="form-select" onchange="submitForm(this);">
			<option value="10"
					<s:if test="%{pager.pageSize == 10}">selected</s:if> >10
			</option>
			<option value="20"
					<s:if test="%{pager.pageSize == 20}">selected</s:if> >20
			</option>
			<option value="30"
					<s:if test="%{pager.pageSize == 30}">selected</s:if> >30
			</option>
		</select>&nbsp;
		<s:text name="common.pages"/>:&nbsp;
		<s:if test="%{pager.isFirstPage() == false}">
			<input type="submit" name="pager.pageNumber" value="1" class="btn-link"/>&nbsp;
		</s:if>

		<s:if test="%{pager.hasPreviousPage() && pager.previousPageNumber > 2}">
			...&nbsp;
		</s:if>

		<s:if test="%{pager.hasPreviousPage() && pager.previousPageNumber > 1}">
			<input type="submit" name="pager.pageNumber" value="<s:property value="%{pager.previousPageNumber}"/>"
				   class="btn-link"/>&nbsp;
		</s:if>

		<strong><s:property value="%{pager.pageNumber}"/></strong>&nbsp;

		<s:if test="%{pager.hasNextPage() && pager.nextPageNumber < pager.lastPageNumber}">
			<input type="submit" name="pager.pageNumber" value="<s:property value="%{pager.nextPageNumber}"/>"
				   class="btn-link"/>&nbsp;
		</s:if>

		<s:if test="%{pager.hasNextPage() && pager.nextPageNumber < pager.lastPageNumber - 1}">
			...&nbsp;
		</s:if>

		<s:if test="%{pager.isLastPage() == false}">
			<input type="submit" name="pager.pageNumber" value="<s:property value="%{pager.lastPageNumber}"/>"
				   class="btn-link"/>&nbsp;
		</s:if>
		&nbsp;
	</span>
	</s:if>
</div>
