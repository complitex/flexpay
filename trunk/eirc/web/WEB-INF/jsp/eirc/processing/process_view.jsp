<%@ include file="/WEB-INF/jsp/common/taglibs.jsp" %>

<table>

  <tr>
    <td class="th" colspan="2">
      process properties
    </td>
  </tr>

  <tr valign="middle" class="cols_1">
    <td class="col">
      id
    </td>
    <td class="col">
      <s:property value="process.id"/>
    </td>
  </tr>
  <tr valign="middle" class="cols_1">
    <td class="col">
      logFileName
    </td>
    <td class="col">
      <s:property value="process.logFileName"/>
    </td>
  </tr>
  <tr valign="middle" class="cols_1">
    <td class="col">
      startDate
    </td>
    <td class="col">
      <s:property value="process.processStartDate"/>
    </td>
  </tr>
  <tr valign="middle" class="cols_1">
    <td class="col">
      endDate
    </td>
    <td class="col">
      <s:property value="process.processEndDate"/>
    </td>
  </tr>
  <tr valign="middle" class="cols_1">
    <td class="col">
      state
    </td>
    <td class="col">
      <s:property value="process.processState"/>
    </td>
  </tr>
  <tr valign="middle" class="cols_1">
    <td class="col">
      definitionName
    </td>
    <td class="col">
      <s:property value="process.processDefinitionName"/>
    </td>
  </tr>
  <tr valign="middle" class="cols_1">
    <td class="col">
      instanceId
    </td>
    <td class="col">
      <s:property value="process.processInstaceId"/>
    </td>
  </tr>
  <tr valign="middle" class="cols_1">
    <td class="col">
      definitionVersion
    </td>
    <td class="col">
      <s:property value="process.processDefenitionVersion"/>
    </td>
  </tr>
  
  <tr>
    <td>
      &nbsp;
    </td>
  </tr>
  
  <tr>
    <td class="th" colspan="2">
      process variables
    </td>
  </tr>
  
  <s:iterator value="process.parameters" >
    <tr valign="middle" class="cols_1">
      <td class="col">
        <s:property value="key"/>
      </td>
      <td class="col">
        <s:property value="value"/>
      </td>
    </tr>
  </s:iterator>




</table>