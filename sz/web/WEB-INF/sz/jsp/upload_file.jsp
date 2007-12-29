<%@ taglib prefix="s" uri="/struts-tags" %>

<s:form action="doUpload" method="POST" enctype="multipart/form-data">
<tr>
<td colspan="2"><h1>File Upload Example</h1></td>
</tr>

<s:file name="upload" label="File"/>
<s:submit />
</s:form>
