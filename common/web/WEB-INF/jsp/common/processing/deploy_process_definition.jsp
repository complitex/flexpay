<%@ include file="/WEB-INF/jsp/common/taglibs.jsp" %>

<s:actionerror />
<s:form action="processDefinitionDeploy" method="post" enctype="multipart/form-data">

	<s:text name="common.processing.definition_file" />:&nbsp;
	<s:file name="upload" /><br />
	<input type="submit" name="submitted" value="<s:text name="common.upload" />" />
</s:form>
