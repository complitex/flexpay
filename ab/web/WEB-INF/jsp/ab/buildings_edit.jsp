<%@ include file="/WEB-INF/jsp/common/taglibs.jsp" %>



<s:form>

<s:hidden name="buildings.id" value="%{buildings.id}" />

<table cellpadding="3" cellspacing="1" border="0" width="100%">

  <s:if test="!alternateBuildingsList.isEmpty()">
    <s:iterator value="%{alternateBuildingsList}">
      <tr valign="middle" class="cols_1">
        <td class="th">
          <s:property value="%{street.format(getUserPreferences().getLocale(), true)}" />,
          <s:property value="%{format(getUserPreferences().getLocale(), true)}" />
          <s:if test="primaryStatus">(<s:text name="ab.buildings.primary_status"/>)</s:if>
        </td>
        <td class="th">
          <a href="<s:url value="/dicts/buildingsEditAction.action?buildings.id=%{id}"/>">
				<img src="<s:url value="/resources/common/img/i_edit.gif" />"
				     alt="<s:text name="common.edit"/>"
				     title="<s:text name="common.edit"/>" />
		  </a>
		  &nbsp;
		  <a href="<s:url action="buildingsDeleteAction" includeParams="none" />?objectIds=<s:property value="id" />&redirectBuildingsId=<s:property value="buildings.id" />">
				<img src="<s:url value="/resources/common/img/i_delete.gif" />"
				     alt="<s:text name="ab.delete"/>"
					 title="<s:text name="ab.delete"/>" />
		  </a>
		  &nbsp;	 
		  <input type="button"
	         class="btn-exit"
	         <s:if test="primaryStatus">disabled="1"</s:if>
			 onclick="window.location='<s:url action="buildingsSetPrimaryStatusAction" includeParams="none" />?buildings.Id=<s:property value="id" />&redirectBuildingsId=<s:property value="buildings.id" />'"
			 value="<s:text name="ab.ab.buildings.set_primary_status"/>"/>	 
        </td>
      </tr>
    </s:iterator>
  </s:if>

  <tr>
    <td>
      &nbsp;
    </td>
  </tr>


  <tr>
    <td class="th" colspan="2">
      <s:property value="%{buildings.street.format(getUserPreferences().getLocale(), true)}" />,
      <s:property value="%{buildings.format(getUserPreferences().getLocale(), true)}" />
      <s:if test="buildings.primaryStatus">(<s:text name="ab.buildings.primary_status"/>)</s:if>
    </td>
  </tr>

  <tr valign="middle" class="cols_1">
    <td class="col">
      <s:property value="%{getTranslation(typeNumber.translations).name}" />
    </td>
    <td class="col" align="center">
      <s:textfield name="numberVal" value="%{buildings.number}" />
    </td>
  </tr>
  <tr valign="middle" class="cols_1" >
    <td class="col">
      <s:property value="%{getTranslation(typeBulk.translations).name}" />
    </td>
    <td class="col" align="center">
      <s:textfield name="bulkVal" value="%{buildings.bulk}" />
    </td>
  </tr>

  <tr>
	<td colspan="2">
      <s:submit name="submit"
                value="%{getText('common.save')}"
                cssClass="btn-exit" />
	  <input type="button"
	         class="btn-exit"
			 onclick="window.location='<s:url action="buildingsCreateAction" includeParams="none" />?buildingId=<s:property value="buildings.building.id" />'"
			 value="<s:text name="common.new"/>"/>
	  <input type="button"
	         class="btn-exit"
			 onclick="window.location='<s:url action="buildingsDeleteAction" includeParams="none" />?objectIds=<s:property value="buildings.id" /><s:if test="!alternateBuildingsList.isEmpty()">&redirectBuildingsId=<s:property value="alternateBuildingsList.get(0).id" /></s:if>'"
			 value="<s:text name="ab.delete"/>" />
	  <input type="button"
	         class="btn-exit"
	         <s:if test="buildings.primaryStatus">disabled="1"</s:if>
			 onclick="window.location='<s:url action="buildingsSetPrimaryStatusAction" includeParams="none" />?buildings.id=<s:property value="buildings.id" />'"
			 value="<s:text name="ab.ab.buildings.set_primary_status"/>"/>		 		 
	</td>
  </tr>					   
					   
</table>

</s:form>
