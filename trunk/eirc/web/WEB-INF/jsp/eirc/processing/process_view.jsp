<%@ include file="/WEB-INF/jsp/common/taglibs.jsp" %>

<table>

  <tr>
    <td class="th" colspan="2">
      <s:text name="eirc.processing.process.properties" />
    </td>
  </tr>

  <tr valign="middle" class="cols_1">
    <td class="col">
      <s:text name="eirc.processing.process.id" />
    </td>
    <td class="col">
      <s:property value="process.id"/>
    </td>
  </tr>
  <tr valign="middle" class="cols_1">
    <td class="col">
      <s:text name="eirc.processing.process.log_file" />
    </td>
    <td class="col">
      <s:property value="process.logFileName"/>
    </td>
  </tr>
  <tr valign="middle" class="cols_1">
    <td class="col">
      <s:text name="eirc.processing.process.start_date" />
    </td>
    <td class="col">
      <s:property value="process.processStartDate"/>
    </td>
  </tr>
  <tr valign="middle" class="cols_1">
    <td class="col">
      <s:text name="eirc.processing.process.end_date" />
    </td>
    <td class="col">
      <s:property value="process.processEndDate"/>
    </td>
  </tr>
  <tr valign="middle" class="cols_1">
    <td class="col">
      <s:text name="eirc.processing.process.state" />
    </td>
    <td class="col">
      <s:property value="process.processState"/>
    </td>
  </tr>
  <tr valign="middle" class="cols_1">
    <td class="col">
      <s:text name="eirc.processing.process.definition_name" />
    </td>
    <td class="col">
      <s:property value="process.processDefinitionName"/>
    </td>
  </tr>
  <tr valign="middle" class="cols_1">
    <td class="col">
      <s:text name="eirc.processing.process.instance_id" />
    </td>
    <td class="col">
      <s:property value="process.processInstaceId"/>
    </td>
  </tr>
  <tr valign="middle" class="cols_1">
    <td class="col">
      <s:text name="eirc.processing.process.definition_version" />
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
      <s:text name="eirc.processing.process.context_variables" />
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