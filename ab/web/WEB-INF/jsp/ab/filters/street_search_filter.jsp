<%@ include file="/WEB-INF/jsp/common/taglibs.jsp" %>
<input type="hidden" name="streetNameFilter.selectedId" id="streetNameFilter.selectedId"/>
<input type="text" class="form-search" id="streetNameFilter.searchString"
	   onfocus="this.value='';" onblur="this.value='<s:text name="ab.street.search" />'"
	   name="streetNameFilter.searchString"
	   value="<s:text name="ab.street.search" />"/>
<script type="text/javascript">
	var nameAutoSuggest = new bsn.AutoSuggest("streetNameFilter.searchString",
	{
		script : function (input) {
			return '<s:url action="streetSearchAjax" namespace="/dicts"/>?searchString=' + input +
					  '&town.id=' + $('townFilter.selectedId').value;
		},
		json : true,
		minchars : 3,
		callback : function(obj) {
			$('streetNameFilter.selectedId').value = obj.id;
			$('streetNameFilter.searchString').value = obj.value;
			$('streetNameFilter.searchString').form.submit();
		}
	});
</script>
