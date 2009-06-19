<%@include file="/WEB-INF/jsp/common/taglibs.jsp" %>

<s:actionerror/>

<s:form id="fregistries">
  <%@include file="/WEB-INF/jsp/orgs/filters/sender_organization_filter.jsp" %>
  &nbsp;&nbsp;
  <%@include file="/WEB-INF/jsp/orgs/filters/recipient_organization_filter.jsp" %>
  &nbsp;&nbsp;
  <%@include file="../filters/registry_type_filter.jsp" %>
  &nbsp;&nbsp;

  <br/>

	<span class="text">
	<s:text name="eirc.generated"/>&nbsp;
	<%@include file="../filters/date_interval_filter.jsp" %>
	</span>


  <table cellpadding="3" cellspacing="1" border="0" width="100%">
    <tr>
      <td colspan="6">
        <input type="submit" value="<s:text name="eirc.filter" />" class="btn-exit"/>
      </td>
      <td colspan="6" align="right">
        <%@include file="/WEB-INF/jsp/common/filter/pager/pager.jsp" %>
      </td>
    </tr>
    <tr>
      <td class="th" width="1%">&nbsp;</td>
      <td class="th"><input type="checkbox" onchange="FP.setCheckboxes(this.checked, 'objectIds');"></td>
      <td class="th"><s:text name="eirc.date"/></td>
      <td class="th"><s:text name="eirc.sender"/></td>
      <td class="th"><s:text name="eirc.recipient"/></td>
      <td class="th"><s:text name="eirc.registry_type"/></td>
      <td class="th"><s:text name="eirc.load_date"/></td>
      <td class="th"><s:text name="eirc.records_number"/></td>
      <td class="th"><s:text name="eirc.status"/></td>
      <td class="th"><s:text name="payments.registry.annotation.change"/></td>
      <td class="th">&nbsp;</td>

      <td class="th"><s:text name="eirc.registry.file_download"/></td>
    </tr>
    <s:iterator value="registries" status="status">
      <tr valign="middle" class="cols_1">
        <td class="col" width="1%"><s:property
                value="%{#status.index + pager.thisPageFirstElementNumber + 1}"/></td>
        <td class="col" width="1%"><input type="checkbox" name="objectIds" value="<s:property value="%{id}"/>"/>
        </td>
        <td class="col"><s:date name="creationDate" format="yyyy/MM/dd"/></td>
        <td class="col"><s:property value="getTranslation(getSenderOrg(properties).names).name"/></td>
        <td class="col"><s:property value="getTranslation(getRecipientOrg((properties)).names).name"/></td>
        <td class="col"><s:text name="%{registryType.i18nName}"/></td>
        <td class="col"><s:date name="spFile.creationDate" format="yyyy/MM/dd HH:mm:ss"/></td>
        <td class="col"><s:property value="recordsNumber"/></td>
        <td class="col"><s:text name="%{registryStatus.i18nName}"/></td>
        <td class="col">
          <a href="<s:url action="changeAnnotation"><s:param name="registry.id" value="%{id}"/></s:url>"><s:text
                  name="common.edit"/></a>
        </td>
        <td class="col">
          <a href="<s:url action="registryView"><s:param name="registry.id" value="%{id}"/></s:url>"><s:text
                  name="common.edit"/></a>
        </td>
        <td class="col">
          <a href="<s:url value='/download/' includeParams="none"/><s:property value="%{spFile.id}"/>.registry"><s:property
                  value="spFile.originalName"/></a>
        </td>
      </tr>
    </s:iterator>
    <tr>
      <!--
			<td colspan="5">
				<input type="submit" value="<s:text name="eirc.process" />" class="btn-exit"
					   onclick="$('#fregistries').attr('action', '<s:url action="registriesProcess" includeParams="none" />');"/>
			</td>
      -->
      <td colspan="12">
        <%@include file="/WEB-INF/jsp/common/filter/pager/pager.jsp" %>
      </td>
    </tr>
  </table>
</s:form>
