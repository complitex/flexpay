<%@include file="/WEB-INF/jsp/common/taglibs.jsp"%>

<ul class="folders">
    <li class="open"><a href="123"><b>Dictionaries</b></a>
        <ul class="docs">
            <li><a href="<c:url value='/dicts/list_countries.action' />"><spring:message code="menu34.dictionaries.countries"/></a></li>
            <li><a href="123"><spring:message code="menu34.dictionaries.regions"/></a></li>
            <li><a href="123"><spring:message code="menu34.dictionaries.cities"/></a></li>
        </ul>
    </li>

    <!--
    <li><a href="123123">Some folder</a>
        <ul class="docs">
            <li><a href="123">Some document</a></li>
        </ul>
    </li>
    -->

</ul>
        