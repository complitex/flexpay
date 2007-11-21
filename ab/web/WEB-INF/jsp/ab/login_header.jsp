<table border="1" cellpadding="0" cellspacing="0" border="0" width="100%">
    <tr class="verytop">
        <td><a href="/"><img src="img/logo.gif" width="123" height="37" alt="FlexPay" border="0" hspace="25"
                                   vspace="6"/></a>
        </td>
        <%
            if(request.getUserPrincipal() != null)
            {
        %>
        <td>
             <span class="text-small">User: <%=request.getUserPrincipal()%> <a href="<%=request.getContextPath()+"/logout.jsp"%>"> &nbsp logout</a></span>
        </td>
        <%
            }
        %>
    </tr>
</table>