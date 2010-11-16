<%@include file="/WEB-INF/jsp/common/taglibs.jsp"%>

<span style="float:right;" class="text-small innerPaging">
    <s:hidden name="pageSizeChanged" value="false" />&nbsp;
    <s:hidden name="curPage" value="%{pager.pageNumber}" />&nbsp;
<s:if test="pager.totalNumberOfElements > 0">
    <link rel="stylesheet" type="text/css" href="<s:url value="/resources/common/style/paging.css" includeParams="none" />" />

    <s:if test="pager.lastPageNumber > 1">
        &nbsp;<s:text name="common.go_to" />&nbsp;
        <s:textfield id="" size="4" value="" maxLength="6" onkeypress="if(event.keyCode == 13)pagerInnerAjax(this);" cssClass="text" />&nbsp;
    </s:if>

    &nbsp;<s:text name="common.show_by" />&nbsp;
    <s:select id="" name="pager.pageSize" list="#{10:10,20:20,30:30}" cssClass="form-select vertical-top" onchange="pagerInnerAjax(this);" />&nbsp;&nbsp;

    <s:if test="pager.lastPageNumber > 1">
        <span class="paging">
            <s:if test="pager.hasPreviousPage()">
                <a value="<s:property value="pager.previousPageNumber" />"><</a>
            </s:if>
            <s:if test="!pager.isFirstPage()">
                <a>1</a>
            </s:if>
            <s:property value="pager.hasPreviousPage() && pager.previousPageNumber > 4 ? '...' : ''" />
            <s:if test="pager.hasPreviousPage() && pager.previousPageNumber > 3">
                <a><s:property value="pager.previousPageNumber - 2" /></a>
            </s:if>
            <s:if test="pager.hasPreviousPage() && pager.previousPageNumber > 2">
                <a><s:property value="pager.previousPageNumber - 1" /></a>
            </s:if>
            <s:if test="pager.hasPreviousPage() && pager.previousPageNumber > 1">
                <a><s:property value="pager.previousPageNumber" /></a>
            </s:if>
            <span class="current"><s:property value="pager.pageNumber" /></span>
            <s:if test="pager.hasNextPage() && pager.nextPageNumber < pager.lastPageNumber">
                <a><s:property value="pager.nextPageNumber" /></a>
            </s:if>
            <s:if test="pager.hasNextPage() && pager.nextPageNumber < pager.lastPageNumber - 1">
                <a><s:property value="pager.nextPageNumber + 1" /></a>
            </s:if>
            <s:if test="pager.hasNextPage() && pager.nextPageNumber < pager.lastPageNumber - 2">
                <a><s:property value="pager.nextPageNumber + 2" /></a>
            </s:if>
            <s:property value="pager.hasNextPage() && pager.nextPageNumber < pager.lastPageNumber - 3 ? '...' : ''" />
            <s:if test="!pager.isLastPage()">
                <a><s:property value="pager.lastPageNumber" /></a>
            </s:if>
            <s:if test="pager.hasNextPage()">
                <a value="<s:property value="pager.nextPageNumber" />">></a>
            </s:if>
        </span>
    </s:if>
</s:if>
</span>

<script type="text/javascript">
    $("span.innerPaging").ready(function() {
        $("span.innerPaging").find("span.paging a").each(function() {
            var $this = $(this);
            var href = $this.attr("href");
            if (href == undefined || href == null || href == "") {
                $this.attr("href", "javascript:void(0);").click(function() {
                    pagerInnerAjax(this);
                });
            }
        });
    });
</script>
