<%@ include file="/WEB-INF/jsp/common/taglibs.jsp" %>

<ul class="folders">
    <li class="open"><strong><s:text name="tc.menu2.dictionaries"/></strong>
        <ul class="docs">
            <li>
                <a href="<s:url action="rulesFilesList" namespace="/tc" includeParams="none" />"><s:text name="tc.menu34.rules_files"/></a>
            </li>
            <li>
                <a href="<s:url action="buildingAttributeTypesList" namespace="/tc" includeParams="none" />"><s:text name="bti.building.attribute.types"/></a>
            </li>
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
