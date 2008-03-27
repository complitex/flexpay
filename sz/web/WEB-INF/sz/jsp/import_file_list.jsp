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
	<!-- <td class="th">
	  <s:text name="sz.user_name" />
	</td> -->
	<td class="th">
	  load to DB
	</td>
	<td class="th">
	  delete from DB
	</td>
	<td class="th">
	  load from DB
	</td>
	<td class="th">
	  response file
	</td>
	<td class="th">
	  delete
	</td>
  </tr>
  <s:iterator value="szFileWrapperList" status="rowstatus">
    <tr valign="middle" class="cols_1">
	  <td class="col_1s">
	    <s:property value="#rowstatus.index + 1" />
	  </td>
	  <td class="col">
	    <s:property value="szFile.oszn.description"/>
	  </td>
	  <td class="col">
	    <s:property value="szFile.requestFileName"/>
	  </td>
	  <td class="col">
	    <s:text name="%{szFile.szFileType.description}"/>
	  </td>
	  <td class="col">
	    <s:property value="szFile.fileYear"/>
	  </td>
	  <td class="col">
	    <s:property value="szFile.fileMonth + 1"/>
	  </td>
	  <td class="col">
	    <s:property value="szFile.importDate"/>
	  </td>
	  <!-- <td class="col">
	    <s:property value="userName"/>
	  </td> -->
	  <td class="col">
	    <a href="<s:url action='loadSzFileToDb'><s:param name="szFileId" value="%{szFile.id}"/><s:param name="actions" value="'loadToDb'"/></s:url>">
	      load to DB
	    </a>
	  </td>
	  <td class="col">
	    <s:if test="loadedToDb">
	      <a href="<s:url action='loadSzFileToDb'><s:param name="szFileId" value="%{szFile.id}"/><s:param name="actions" value="'deleteFromDb'"/></s:url>">
	        delete from DB
	      </a>
	      </s:if>
	  </td>
	  <td class="col">
	    <s:if test="loadedToDb">
	      <a href="<s:url action='loadSzFileToDb'><s:param name="szFileId" value="%{szFile.id}"/><s:param name="actions" value="'loadFromDb'"/></s:url>">
	        load from DB
	      </a>
	    </s:if>
	  </td>
	  <td class="col">
	    <s:if test="szFile.internalResponseFileName != null">
	      <a href="<s:url value='/szFileDownloadServlet'><s:param name="szFileId" value="%{szFile.id}"/></s:url>">
	        <s:property value="szFile.requestFileName"/>
	      </a>
	    </s:if>
	  </td>
	  <td class="col">
	    <a href="<s:url action='loadSzFileToDb'><s:param name="szFileId" value="%{szFile.id}"/><s:param name="actions" value="'fullDelete'"/></s:url>">
	      delete
	    </a>
	  </td>
	  
	</tr>
  </s:iterator>
  
  
  <tr>
    <td colspan="12" height="3" bgcolor="#4a4f4f"/>
  <tr>

</table>
