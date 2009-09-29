{result: <%
	HttpSession httpSession = request.getSession(false);

	if (httpSession != null) {
%>"OK"<%
} else {
%>"ERROR"<%
	}
%>}
