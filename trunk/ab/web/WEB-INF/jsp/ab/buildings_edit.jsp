<%@ include file="/WEB-INF/jsp/common/taglibs.jsp" %>


<s:property value="%{buildings.street.format(getUserPreferences().getLocale(), true)}" />,
<s:property value="%{buildings.format(getUserPreferences().getLocale(), true)}" />
<s:form>

<table>
  <tr>
    <td>
      <s:property value="%{getTranslation(typeNumber.translations).name}" />
    </td>
    <td>
      <s:textfield name="numberVal" value="%{buildings.number}" />
    </td>
  </tr>
  <tr>
    <td>
      <s:property value="%{getTranslation(typeBulk.translations).name}" />
    </td>
    <td>
      <s:textfield name="bulkVal" value="%{buildings.bulk}" />
    </td>
  </tr>
</table>

<s:hidden name="buildings.id" value="%{buildings.id}" />
<s:submit name="submit" value="%{getText('common.save')}" cssClass="btn-exit" />

</s:form>