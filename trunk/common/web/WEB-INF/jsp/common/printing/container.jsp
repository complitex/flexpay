<%@include file="/WEB-INF/jsp/common/taglibs.jsp"%>
<html>
    <head></head>
<%
	String browser = request.getHeader("User-Agent").toLowerCase();
	if (browser.contains("msie")) {
%>
    <s:if test="file.extension == '.html'">
        <frameset rows="25,*" cols="*" onload="mainFrame.print();">
            <frame src="<s:url value="/resources/common/jsp/print.jsp" includeParams="none" />" id="topFrame" name="topFrame" scrolling="no" />
            <frame src="<s:url value="/download/%{file.id}%{file.getExtension()}?inline" includeParams="none" />" id="mainFrame" name="mainFrame" />
        </frameset>
    </s:if><s:elseif test="file.extension == '.pdf'">
        <body>
            <script type="text/javascript">
                function printMainFrame() {
                    document.getElementById("doc").print();
                }
            </script>
            <div align="center">
                <input type="button" onclick="printMainFrame();return false;" value="<s:text name="print" />" class="btn-exit" />
                <input type="button" onclick="window.close();" value="<s:text name="close_window" />" class="btn-exit" />
                <object data="<s:url value="/download/%{file.id}%{file.extension}?inline" includeParams="none" />"
                        type="application/pdf" id="doc" width="100%" height="100%">
                </object>
            </div>
        </body>
    </s:elseif>
<%
} else {
%>
    <frameset rows="25,*" cols="*"<s:if test="file.extension == '.html'"> onload="mainFrame.print();"</s:if>>
        <frame src="<s:url value="/resources/common/jsp/print.jsp" includeParams="none" />" id="topFrame" name="topFrame" scrolling="no" />
        <frame src="<s:url value="/download/%{file.id}%{file.getExtension()}?inline" includeParams="none" />" id="mainFrame" name="mainFrame" />
    </frameset>
<%
	}
%>
</html>
