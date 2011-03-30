<%@include file="/WEB-INF/jsp/common/taglibs.jsp"%>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <%@include file="/WEB-INF/jsp/common/layouts/scripts.jsp"%>
    </head>
<body onload="window.print();">
    <s:if test="file.extension == '.html' || file.extension == '.txt' || file.extension == '.csv'">
        <script type="text/javascript">
            function printMainFrame() {
                var iframe=$("#mainFrame");
                iframe.contentWindow.focus();
                iframe.contentWindow.print();
            }
        </script>
        <div align="center">
            <input type="button" onclick="printMainFrame();return false;" value="<s:text name="print" />" class="btn-exit" />
            <input type="button" onclick="parent.close();" value="<s:text name="close_window" />" class="btn-exit" />
        </div>
        <iframe src="<s:url value="/download/%{file.id}%{file.getExtension()}?inline" />" id="mainFrame" name="mainFrame" width="100%" height="98%"></iframe>
    </s:if><s:elseif test="file.extension == '.pdf'">
        <div align="center">
            <input type="button" onclick="window.close();" value="<s:text name="close_window" />" class="btn-exit" />
            <object data="<s:url value="/download/%{file.id}%{file.extension}?inline" />"
                    type="application/pdf" id="doc" width="100%" height="97%">
            </object>
        </div>
    </s:elseif>
</body>
</html>
