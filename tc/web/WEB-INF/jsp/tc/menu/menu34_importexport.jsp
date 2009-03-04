<%@ include file="/WEB-INF/jsp/common/taglibs.jsp" %>

<ul class="folders">
    <li class="open"><strong><s:text name="tc.menu2.import_export"/></strong>
        <ul class="docs">
            <li>
                <a href="<s:url action="calcResultExport" namespace="/tc" includeParams="none" />"><s:text name="tc.menu34.calc_results_export"/></a>
            </li>
            <li>
                <a href="<s:url action="buildingAttributesImport" namespace="/tc" includeParams="none" /> "><s:text name="bti.building.attribute.import"/></a>
            </li>
            <li>
                <a href="<s:url action="buildingAttributesImport" namespace="/tc" includeParams="none" /> "><s:text name="bti.building.attribute.import"/></a>
            </li>
        </ul>
    </li>

    <li class="open"><strong><s:text name="tc.menu34.processes"/></strong>
        <ul class="docs">
            <li>
                <a href="<s:url action='processesList' namespace="/tc" includeParams="none" />"><s:text name="common.processing.process_list"/></a>
            </li>
            <li>
                <a href="<s:url action='processDefinitionDeploy' namespace="/tc" includeParams="none" />"><s:text name="common.processing.definition"/></a>
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
