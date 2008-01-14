<%@ taglib prefix="s" uri="/struts-tags" %>

<s:form action="doUpload" method="POST" enctype="multipart/form-data">

<table>
<tr>
  <td>
    <s:text name="year" />
  </td>
  <td>
    <s:select name="year" list="years" value="year" required="true" />
  </td>
</tr>

<tr>
  <td>
    <s:text name="month" />
  </td>
  <td>
    <s:select name="month" list="months" required="true" />
  </td>
</tr>

<tr>
  <td>
    <s:text name="ab.region_name" />
  </td>
  <td>
    <s:select name="regionId" list="regionNames" listKey="region.id" listValue="%{getTranslation(translations).name}" required="true" />
  </td>
</tr>

<tr>
  <td>
    <s:text name="sz.file" />
  </td>
  <td>
    <s:file name="upload" label="File" />
  </td>
</tr>

<tr>
  <td colspan="2">
    &nbsp
  </td>
</tr>

<tr>
  <td colspan="2">
    <s:submit name="submit" value="%{getText('common.upload')}" cssClass="btn-exit" />
  </td>
</tr>    

</table>

</s:form>
