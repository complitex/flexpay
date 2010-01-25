<%@include file="/WEB-INF/jsp/common/taglibs.jsp"%>

<s:hidden value="%{streetFilter}" name="object.id" />
<input type="hidden" id="setupType" name="setupType" value="setupType" />
<input type="submit" onclick="$('#setupType').val('street');" class="btn-exit" value="<s:text name="common.set" />" />
