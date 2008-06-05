<%@ include file="/WEB-INF/jsp/common/taglibs.jsp" %>

<table>

<s:form>

<tr>
  <td class="th">
    &nbsp;
  </td>
  <td class="th">
    &nbsp;
  </td>
  <td class="th">
    id
  </td>
  <td class="th">
    name
  </td>
  <td class="th">
    start date
  </td>
  <td class="th">
    end date
  </td>
  <td class="th">
    user
  </td>
  <td class="th">
    state
  </td>
  <td class="th">
    pause
  </td>


<s:iterator value="processList" status="status">
  <tr valign="middle" class="cols_1">
	<td class="col" width="1%">
	  <s:property value="%{#status.index + 1}"/>
	</td>
	<td class="col" width="1%">
	  <input type="checkbox" name="objectIds" value="<s:property value="%{id}"/>"/>
	</td>
	<td class="col">
	  <s:property value="id"/>
	</td>
	<td class="col">
	  <a href="<s:url value="/eirc/processViewAction.action?process.id=%{id}"/>">
	    <s:property value="processDefinitionName"/>
	  </a>
	</td>
	<td class="col">
	  <s:date name="process_start_date" format="yyyy/MM/dd"/>
	</td>
	<td class="col">
	  <s:date name="process_end_date" format="yyyy/MM/dd"/>
	</td>
	<td class="col">
	  &nbsp;
	</td>
	<td class="col">
	  <s:property value="processState"/>
	</td>
	<td class="col">
	  &nbsp;
	</td>
  </tr>

</s:iterator>



<tr>
  <td colspan="2" align="center">
    &nbsp;
  </td>
</tr>

<tr>
  <td colspan="2" align="center">
    <s:submit name="submit" value="%{getText('ab.delete')}" cssClass="btn-exit" />
  </td>
</tr>  

</s:form>

</table>