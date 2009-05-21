<%@ include file="/WEB-INF/jsp/common/taglibs.jsp" %>
<html>
<%
	String browser = request.getHeader("User-Agent").toLowerCase();
	if (browser.contains("msie")) {
%>
<%@ include file="../layouts/scripts.jsp" %>
<body>
<script type="text/javascript">
	function printMainFrame() {
		document.getElementById("pdf").Print();
	}
</script>
<div align="center">
	<input type='button' onclick='printMainFrame(); return false;' value='<s:text name="print" />' class="btn-exit" />
	<input type='button' onclick="window.close();" value="<s:text name="close_window" />" class="btn-exit" />
	<object data="<s:url value="/download/" includeParams="none"/><s:property value="%{file.id}"/>.pdf"
			type="application/pdf" id="pdf" width="100%" height="100%">
	</object>
</div>
</body>
<%
} else {
%>
<frameset rows="25,*" cols="*">
	<frame src="<s:url value="/resources/common/jsp/print.jsp" includeParams="none" />" id="topFrame" name="topFrame"
		   scrolling="no" />
	<frame src="<s:url value="/download/" includeParams="none"/><s:property value="%{file.id}"/>.pdf" id="mainFrame"
		   name="mainFrame" />
</frameset>
<%
	}
%>
</html>
