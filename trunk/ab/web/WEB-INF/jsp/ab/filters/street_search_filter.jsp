<%@ include file="/WEB-INF/jsp/common/taglibs.jsp" %>
<s:hidden name="streetNameFilter.selectedId" id="streetNameFilter.selectedId" value="%{streetNameFilter.selectedId}"/>
<s:if test="streetNameFilter.showSearchString && streetNameFilter.searchString != null">
	<s:set name="streetNameFilter.field.value" value="streetNameFilter.searchString"/>
</s:if><s:else>
	<s:set name="streetNameFilter.field.value" value="'ab.street.search'"/>
</s:else>
<input type="text" class="form-search" id="streetNameFilter.searchString"
	   onfocus="this.value='';" onblur="this.value='<s:text name="%{streetNameFilter.field.value}" />'"
	   name="streetNameFilter.searchString"
	   value="<s:text name="%{streetNameFilter.field.value}" />"/>
<script type="text/javascript">
	var nameAutoSuggest = new bsn.AutoSuggest("streetNameFilter.searchString",
	{
		script : function (input) {
			return '<s:url action="streetSearchAjax" namespace="/dicts" includeParams="none"/>?searchString=' + input +
				   '&town.id=' + $('townFilter.selectedId').value;
		},
		json : true,
		minchars : 3,
		callback : function(obj) {
			$('streetNameFilter.selectedId').value = obj.id;
			$('streetNameFilter.searchString').value = obj.info + ' ' + obj.value;
			$('streetNameFilter.searchString').form.submit();
		}
	});
</script>
