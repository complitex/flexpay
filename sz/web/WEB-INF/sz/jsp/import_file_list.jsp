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
	<td class="th">
	  status(debug info)
	</td>
	<td class="th">
	  actuality status(debug info)
	</td>
	<td class="th">
	  load to DB
	</td>
	<td class="th">
	  load from DB
	</td>
	<td class="th">
	  response file
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
	    <s:property value="requestFileName"/>
	  </td>
	  <td class="col">
	    <s:text name="%{szFileType.description}"/>
	  </td>
	  <td class="col">
	    <s:property value="fileYear"/>
	  </td>
	  <td class="col">
	    <s:property value="fileMonth + 1"/>
	  </td>
	  <td class="col">
	    <s:property value="importDate"/>
	  </td>
	  <td class="col">
	    <s:property value="userName"/>
	  </td>
	  <td class="col">
	    <s:text name="%{szFileStatus.description}"/>
	  </td>
	  <td class="col">
	    <s:text name="%{szFileActualityStatus.description}"/>
	  </td>
	  <td class="col">
	    <a href="<s:url action='loadSzFileToDb'><s:param name="szFileId" value="%{id}"/></s:url>">
	      load to DB
	    </a>
	  </td>
	  <td class="col">
	    <a href="<s:url action='loadSzFileFromDb'><s:param name="szFileId" value="%{id}"/></s:url>">
	      load from DB
	    </a>
	  </td>
	  <td class="col">
	    <s:if test="%{internalResponseFileName != null}">
	      <s:property value="szDataRoot + separator + yyyyMm + separator + internalResponseFileName"/>
	    </s:if>
	  </td>
	  
	</tr>
  </s:iterator>
  
  <tr>
    <td colspan="9" height="3" bgcolor="#4a4f4f"/>
  <tr>

</table>
