<%@ page contentType="text/html;charset=UTF-8" pageEncoding="utf-8" language="java" %>
<%@ include file="/WEB-INF/jsp/common/taglibs.jsp" %>
<input type="text" name="personSearchFilter.searchString" class="form-textfield"
			 value="<s:property value="%{personSearchFilter.searchString}" />"/>
<input type="button" onclick="this.form.submit();" value="<s:text name="common.search" />" />