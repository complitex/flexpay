<%@ page contentType="text/html;charset=UTF-8" pageEncoding="utf-8" language="java" %>
<%@ include file="/WEB-INF/jsp/common/taglibs.jsp" %>

<ul class="folders">
    <li class="open"><b><s:text name="tc.menu2.dictionaries"/></b>
        <ul class="docs">
            <li><a href="<s:url action="sewerTypesList" namespace="/tc" includeParams="none" />"><s:text name="tc.menu34.sewer_types"/></a></li>
            <li><a href="<s:url action="sewerMaterialTypesList" namespace="/tc" includeParams="none" />"><s:text name="tc.menu34.sewer_material_types"/></a></li>
            <li><a href="<s:url action="rulesFilesList" namespace="/tc" includeParams="none" />"><s:text name="tc.menu34.rules_files"/></a></li>
        </ul>
    </li>

    <%--
        <li><a href="123123">Some folder</a>
            <ul class="docs">
                <li><a href="123">Some document</a></li>
            </ul>
        </li>
    --%>

</ul>
