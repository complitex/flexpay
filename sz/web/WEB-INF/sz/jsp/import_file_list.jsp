<%@ taglib prefix="s" uri="/struts-tags" %>

<table cellpadding="3" cellspacing="1" border="0" width="100%">

  <tr>
    <td class="th">
	  &nbsp;
	</td>
	<td class="th">
	  <s:text name="sz.oszn" />
	</td>
	<td class="th">
	  <s:text name="sz.original_file_name" />
    </td>
	<td class="th">
	  <s:text name="sz.file_type" />
	</td>
	<td class="th">
	  <s:text name="sz.year" />
	</td>
	<td class="th">
	  <s:text name="sz.month" />
	</td>
	<td class="th">
	  <s:text name="sz.import_date" />
	</td>
	<td class="th">
	  <s:text name="sz.user_name" />
	</td>
  </tr>
  <s:iterator value="importFileList" status="rowstatus">
    <tr valign="middle" class="cols_1">
	  <td class="col_1s">
	    <s:property value="#rowstatus.index + 1" />
	  </td>
	  <td class="col">
	    <s:property value="oszn.description"/>
	  </td>
	  <td class="col">
	    <s:property value="originalFileName"/>
	  </td>
	  <td class="col">
	    <s:property value="fileType"/>
	  </td>
	  <td class="col">
	    <s:property value="fileYear"/>
	  </td>
	  <td class="col">
	    <s:property value="fileMonth"/>
	  </td>
	  <td class="col">
	    <s:property value="importDate"/>
	  </td>
	  <td class="col">
	    <s:property value="userName"/>
	  </td>
	</tr>
  </s:iterator>
  
  <tr>
    <td colspan="9" height="3" bgcolor="#4a4f4f"/>
  <tr>

</table>
