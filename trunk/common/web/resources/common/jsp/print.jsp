<%@include file="/WEB-INF/jsp/common/taglibs.jsp"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
	<%@include file="/WEB-INF/jsp/common/layouts/scripts.jsp"%>
</head>
<body>
    <div align="center">
        <input type="button" onclick="parent.mainFrame.print();" value="<s:text name="print" />" class="btn-exit" />
        <input type="button" onclick="parent.close();" value="<s:text name="close_window" />" class="btn-exit" />
    </div>
</body>
</html>
