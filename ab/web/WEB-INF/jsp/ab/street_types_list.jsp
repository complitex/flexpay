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
	  <spring:message code="ab.town_type_name"/>
	</td>
	<td class="th">
	  &nbsp;
	</td>
  </tr>
  <s:iterator value="translationList" status="rowstatus">
    <tr>
	  <td>
	    <s:property value="#rowstatus.index + 1" />
	  </td>
	  <td>
	    <s:checkbox name="name" value="streetTypeIds"/>
	  </td>
	  <td>
	    <a href="<s:url action='street_type_view'>     <s:param name="id" value="%{streetType.id}"/>       </s:url>">
	      <s:property value="name"/>
	    </a>
	  </td>
	  <td>
	    edit url
	  </td>
	</tr>
  </s:iterator>
  
  
  <tr>
    <td>
      <a href="<s:url action='street_type_create'/>">create</a>
    </td>
  </tr>

</table>