<%@ include file="/WEB-INF/jsp/common/taglibs.jsp" %>

<table cellpadding="3" cellspacing="1" border="0" width="100%">
  <tr>
	<td class="th">
	  &nbsp;
	</td>
	<td class="th">
	  <input type="checkbox" onchange="FP.setCheckboxes(this.checked, 'streetTypeIds')">
    </td>
	<td class="th">
	  <s:text name="ab.street_type_name"/>
	</td>
	<td class="th">
	  &nbsp;
	</td>
  </tr>
  <s:iterator value="translationList" status="rowstatus">
    <tr valign="middle" class="cols_1">
	  <td class="col_1s">
	    <s:property value="#rowstatus.index + 1" />
	  </td>
	  <td class="col">
	    <s:checkbox name="name" value="streetTypeIds"/>
	  </td>
	  <td class="col">
	    <a href="<s:url action='street_type_view'><s:param name="id" value="%{streetType.id}"/></s:url>">
	      <s:property value="name"/>
	    </a>
	  </td>
	  <td class="col">
	    <a href="<s:url action='street_type_edit'><s:param name="id" value="%{streetType.id}"/></s:url>">
	      <s:text name="ab.street_type.edit.link" />
	    </a>
	  </td>
	</tr>
  </s:iterator>
  
  <tr>
    <td colspan="4" height="3" bgcolor="#4a4f4f"/>
  <tr>
  
  <tr>
    <td>
      <a href="<s:url action='street_type_create'/>"><s:text name="ab.street_type.create.link" /></a>
    </td>
  </tr>

</table>