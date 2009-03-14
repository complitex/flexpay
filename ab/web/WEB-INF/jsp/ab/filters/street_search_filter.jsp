<%@ include file="/WEB-INF/jsp/common/taglibs.jsp" %>
<s:hidden name="streetNameFilter.selectedId" id="selectedStreetId" value="%{streetNameFilter.selectedId}" />
<s:if test="streetNameFilter.showSearchString && streetNameFilter.searchString != null">
	<s:set name="streetNameFilter.field.value" value="streetNameFilter.searchString" />
</s:if><s:else>
	<s:set name="streetNameFilter.field.value" value="'ab.street.search'" />
</s:else>

<input type="text" class="form-search" id="streetFilter"
	   name="streetNameFilter.searchString"
       onfocus="if(jQuery('#selectedStreetId').val().length == 0)this.value='';" onblur="if(jQuery('#selectedStreetId').val().length == 0)this.value='<s:text name="%{streetNameFilter.field.value}" />';"
	   value="<s:text name="%{streetNameFilter.field.value}" />" />

<script type="text/javascript">

    function findValue(li) {
        if (li == null) {
            alert("No match!");
            return;
        }

        var sValue = !!li.extra ? li.extra[0] : li.selectValue;
        jQuery("#selectedStreetId").val(sValue);
        if (li.extra) {
            jQuery("#streetFilter").val(li.selectValue);
        }
        jQuery("#streetFilter")[0].form.submit();
    }

    function formatItem(row) {
        var street = row[0].toLowerCase();
        var value = jQuery("#streetFilter").val().toLowerCase();
        var i = street.indexOf(value);
        return row[0].substr(0, i) + "<strong>" + row[0].substr(i, value.length) + "</strong>" + row[0].substr(i + value.length);
    }

    function selectItem(li) {
        findValue(li);
    }

    jQuery(function() {
        jQuery("#streetFilter").autocomplete(
        "<s:url action="streetSearchAjax" namespace="/dicts" includeParams="none"/>",
        {
  			delay:10,
  			minChars:3,
  			matchSubset:1,
            selectOnly:1,
  			matchContains:1,
  			cacheLength:10,
            formatItem:formatItem,
  			onItemSelect:selectItem,
            extraParams: {"townId":jQuery("#townFilter").val()}
  		});
    });

</script>
