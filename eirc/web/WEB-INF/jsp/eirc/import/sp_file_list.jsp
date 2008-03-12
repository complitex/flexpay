<%@ taglib prefix="s" uri="/struts-tags" %>

<table cellpadding="3" cellspacing="1" border="0" width="100%">

  <tr>
    <td class="th">
	  &nbsp;
	</td>
	<td class="th">
	  <s:text name="eirc.original_file_name" />
    </td>
	<td class="th">
	  <s:text name="eirc.import_date" />
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
  <s:iterator value="spFileList" status="rowstatus">
    <tr valign="middle" class="cols_1">
	  <td class="col_1s">
	    <s:property value="#rowstatus.index + 1" />
	  </td>
	  <td class="col">
	    <s:property value="requestFileName"/>
	  </td>
	  <td class="col">
	    <s:property value="importDate"/>
	  </td>
	  <!-- <td class="col">
	    <s:property value="userName"/>
	  </td> -->
	  <td class="col">
	    <a href="<s:url action='spFileAction'><s:param name="spFileId" value="%{id}"/><s:param name="action" value="'loadToDb'"/></s:url>">
	      load to DB
	    </a>
	  </td>
	  <td class="col">
	    <!-- <s:if test="loadedToDb">
	      <a href="<s:url action='loadSzFileToDb'><s:param name="szFileId" value="%{szFile.id}"/><s:param name="action" value="'deleteFromDb'"/></s:url>">
	        delete from DB
	      </a>
	      </s:if> -->
	      ---
	  </td>
	  <td class="col">
	    <!-- <s:if test="loadedToDb">
	      <a href="<s:url action='loadSzFileToDb'><s:param name="szFileId" value="%{szFile.id}"/><s:param name="action" value="'loadFromDb'"/></s:url>">
	        load from DB
	      </a>
	    </s:if> -->
	    ---
	  </td>
	  <td class="col">
	    <s:if test="internalResponseFileName != null">
	      <a href="<s:url value='/spFileDownloadServlet'><s:param name="spFileId" value="%{id}"/></s:url>">
	        <s:property value="requestFileName"/>
	      </a>
	    </s:if>
	  </td>
	  <td class="col">
	    <!--  <a href="<s:url action='loadSzFileToDb'><s:param name="szFileId" value="%{szFile.id}"/><s:param name="action" value="'fullDelete'"/></s:url>">
	      delete
	    </a> -->
	    ---
	  </td>
	  
	</tr>
  </s:iterator>
  
  
  <tr>
    <td colspan="12" height="3" bgcolor="#4a4f4f"/>
  <tr>
  
  <tr>
    <td>
      <s:property value="spFileFormatException" />
      <s:if test="spFileFormatException != null">
        SpFile format exception. Position: <s:property value="spFileFormatException.position" />
      </s:if>
    </td>
  <tr>

</table>
