<%@include file="/WEB-INF/jsp/common/taglibs.jsp" %>

<s:hidden value="%{buildingFilter}" name="object.id" />
<input type="hidden" id="setupType" name="setupType" value="setupType" />
<input type="submit" onclick="$('#setupType').val('building');" class="btn-exit" value="<s:text name="common.set"/>"/>
