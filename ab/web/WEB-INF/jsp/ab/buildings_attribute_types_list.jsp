<%@ include file="/WEB-INF/jsp/common/taglibs.jsp" %>


<table cellpadding="3" cellspacing="1" border="0" width="100%">

<tr>
  <td class="th" width="1%">
        &nbsp;
  </td>
  <td class="th" width="1%">
    <input type="checkbox" onclick="FP.setCheckboxes(this.checked,'objectIds')" disabled="1">
  </td>
  <td class="th">
    <s:text name="ab.buildings.attribute_type"/>
  </td>
  <td class="th">
	&nbsp;
  </td>
</tr>
<s:iterator value="types" status="status">
	<tr valign="middle" class="cols_1">
	  <td class="col_1s" align="right">
		<s:property	value="%{#status.index + 1}"/>
	  </td>
	  <td class="col" align="center">
		<input type="checkbox" name="objectIds" disabled="1">
	  </td>
	  <td class="col_1s" align="left">
		<s:property	value="%{getTranslation(translations).name}"/>
	  </td>
	  <td class="col_1s" align="right">
		<a href="<s:url action='buildingAttributeTypeEditAction'><s:param name="id" value="%{translatable.id}"/></s:url>">
	      <s:text name="ab.edit" />
	    </a>
	  </td>
    </tr>
</s:iterator>
<tr>
  <td colspan="3">
        <input type="submit" class="btn-exit"
        			 disabled="1"
					 value="<s:text name="common.delete_selected"/>"/>
		<input type="button" class="btn-exit"
					 onclick="window.location='<s:url action="buildingAttributeTypeCreateAction.action"/>'"
                     value="<s:text name="common.new"/>"/>			 
  </td>
</tr>  
  	  
			
</table>



